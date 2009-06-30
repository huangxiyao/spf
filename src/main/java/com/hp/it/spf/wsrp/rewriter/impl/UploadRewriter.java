package com.hp.it.spf.wsrp.rewriter.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import oasis.names.tc.wsrp.v2.types.GetResource;
import oasis.names.tc.wsrp.v2.types.InteractionParams;
import oasis.names.tc.wsrp.v2.types.NamedString;
import oasis.names.tc.wsrp.v2.types.PerformBlockingInteraction;
import oasis.names.tc.wsrp.v2.types.ResourceParams;
import oasis.names.tc.wsrp.v2.types.UploadContext;

import org.apache.axis.MessageContext;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import com.hp.it.spf.wsrp.misc.Predicates;
import com.hp.it.spf.wsrp.rewriter.IRewriter;

/**
 * Rewrites upload contexts and form parameters values provided by Vignette into the format expected
 * by OpenPortal WSRP producer.
 * Vignette encodes the entire form with all its parameters (normal and files) into a single upload
 * context. OpenPortal however expects each of the parameters to be present in form parameters,
 * and each of the files to be present in a separate upload context. This class performs that
 * transformations.
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class UploadRewriter implements IRewriter {

	public boolean shouldApply(MessageContext messageContext) {
		return Predicates.isOpenPortalProducer(messageContext) &&
				Predicates.isAnyOfMethods(messageContext,
						"v2:performBlockingInteraction", "v2:getResource");
	}

	/**
	 * Rewrites WSRP request argument if it is a <tt>performBlockingInteraction</tt> or <tt>getResource</tt> call.
	 * 
	 * @param data WSRP request object
	 */
	public void rewrite(Object data) {
		if (data instanceof PerformBlockingInteraction) {
			rewriteInteractionParams(((PerformBlockingInteraction) data).getInteractionParams());
		}
		else if (data instanceof GetResource) {
			rewriteResourceParams(((GetResource) data).getResourceParams());
		}
		else {
			throw new IllegalArgumentException("Unexpected argument type: " +
					(data == null ? "null" : data.getClass().getName()));
		}
	}

	/**
	 * Rewrites interaction parameters. This method is used for <tt>performBlockingInteraction</tt>
	 * requests.
	 *
	 * @param interactionParams interaction parameters containing form parameters and upload contexts
	 */
	private void rewriteInteractionParams(InteractionParams interactionParams) {
		UploadContext[] uploadContexts = interactionParams.getUploadContexts();
		if (uploadContexts == null || uploadContexts.length == 0) {
			return;
		}

		FormData input = new FormData(interactionParams.getFormParameters(), interactionParams.getUploadContexts());
		FormData conversionResult = convert(input);

		interactionParams.setFormParameters(conversionResult.getFormParameters());
		interactionParams.setUploadContexts(conversionResult.getUploadContexts());
	}

	/**
	 * Rewrites resource parameters. This method is used for <tt>getResource</tt> requests.
	 *
	 * @param resourceParams resource parameters containing form parameters and upload contexts
	 */
	private void rewriteResourceParams(ResourceParams resourceParams) {
		UploadContext[] uploadContexts = resourceParams.getUploadContexts();
		if (uploadContexts == null || uploadContexts.length == 0) {
			return;
		}

		FormData input = new FormData(resourceParams.getFormParameters(), resourceParams.getUploadContexts());
		FormData conversionResult = convert(input);

		resourceParams.setFormParameters(conversionResult.getFormParameters());
		resourceParams.setUploadContexts(conversionResult.getUploadContexts());
	}

	/**
	 * Performs the actual conversion from Vignette provided data to OpenPortal expected data.
	 *
	 * @param input input containing form parameters and upload contexts
	 * @return conversion result
	 * @throws RuntimeException if FileUploadException occurs during conversion
	 */
	private FormData convert(FormData input) throws RuntimeException {
		List<NamedString> formParameters = new ArrayList<NamedString>();
		if (input.getFormParameters() != null) {
			formParameters.addAll(Arrays.asList(input.getFormParameters()));
		}		
		List<UploadContext> uploadContexts = new ArrayList<UploadContext>();

		final UploadContext uploadContext = input.getUploadContexts()[0];
		RequestContext requestContext = createRequestContext(uploadContext.getUploadData(), uploadContext.getMimeType());

		FileItemFactory factory = new DiskFileItemFactory();
		FileUpload fileUpload = new FileUpload(factory);
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> fileItems = fileUpload.parseRequest(requestContext);
			for (FileItem item : fileItems) {
				if (item.isFormField()) {
					NamedString formParameter = createFormParameter(item);
					formParameters.add(formParameter);
				}
				else {
					UploadContext newUploadContext = createUploadContext(item);
					uploadContexts.add(newUploadContext);
				}
			}
		}
		catch (FileUploadException e) {
			throw new RuntimeException("Error parsing upload request: " + e, e);
		}

		return new FormData(
				formParameters.toArray(new NamedString[formParameters.size()]),
				uploadContexts.toArray(new UploadContext[uploadContexts.size()]));
	}

	/**
	 * Creates UploadContext based on the data in the given item.
	 * @param item item resulting from multipart request processing
	 * @return UploadContext with data from the item
	 */
	private UploadContext createUploadContext(FileItem item) {
        UploadContext newUploadContext = new UploadContext();
        newUploadContext.setMimeType(item.getContentType());
        newUploadContext.setUploadData(item.get());
        NamedString contenType = new NamedString();
        contenType.setName("Content-Type");
        contenType.setValue(item.getContentType());
        NamedString contentDisposition = new NamedString();
        contentDisposition.setName("Content-Disposition");
        try {
            // convert the filename to ascii characters
            contentDisposition.setValue(String.format("form-data; name=\"%s\"; filename=\"%s\"",
                                                      item.getFieldName(),
                                                      new String(item.getName().getBytes("UTF-8"),
                                                                 "iso-8859-1")));
        } catch (UnsupportedEncodingException ex) {
            //it should never happen
            throw new RuntimeException(ex);
        }
        newUploadContext.setMimeAttributes(new NamedString[] {contenType,
                                                              contentDisposition});
        return newUploadContext;
    }

	/**
	 * Creates form parameter based on the data in the given item.
	 * @param item item resulting from multipart request processing
	 * @return form parameter with the data from the item
	 */
	private NamedString createFormParameter(FileItem item) {
		NamedString formField = new NamedString();
		formField.setName(item.getFieldName());
		try {
			formField.setValue(item.getString("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			//it should never happen
			throw new RuntimeException(e);
		}
		return formField;
	}


	/**
	 * Creates file upload's request context based on the provided data.
	 * @param uploadData the actual request content
	 * @param contentType the request content type; usually multipart request
	 * @return request context as expected by file upload parseRequest method.
	 */
	private RequestContext createRequestContext(final byte[] uploadData, final String contentType) {
		return new RequestContext() {
			public String getCharacterEncoding() {
				return "UTF-8";
			}

			public String getContentType() {
				return contentType; 
			}

			public int getContentLength() {
				return uploadData.length;
			}

			public InputStream getInputStream() throws IOException {
				return new ByteArrayInputStream(uploadData);
			}
		};
	}


	/**
	 * Value object holding data being converted.
	 */
	static class FormData {
		private NamedString[] mFormParameters;
		private UploadContext[] mUploadContexts;

		FormData(NamedString[] formParameters, UploadContext[] uploadContexts) {
			mFormParameters = formParameters;
			mUploadContexts = uploadContexts;
		}

		public NamedString[] getFormParameters() {
			return mFormParameters;
		}

		public UploadContext[] getUploadContexts() {
			return mUploadContexts;
		}
	}
}
