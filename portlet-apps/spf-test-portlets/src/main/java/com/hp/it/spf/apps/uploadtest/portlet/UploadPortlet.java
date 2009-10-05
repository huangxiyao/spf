package com.hp.it.spf.apps.uploadtest.portlet;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.RenderMode;

/**
 * Portlet used to test upload. It uploads 2 files and submits the additional parameters. It then
 * renders all these values including file content.
 *  
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class UploadPortlet extends GenericPortlet {

	@RenderMode(name = "view")
	public void showView(RenderRequest request, RenderResponse response) throws PortletException, IOException
	{
		getPortletContext().getRequestDispatcher("/WEB-INF/jsp/uploadtest/view.jsp").include(request, response);
		request.getPortletSession().removeAttribute("result");
	}

	

    public void processAction(ActionRequest request, ActionResponse response)
            throws PortletException, IOException {
		try {
			Map<String, String> result = new LinkedHashMap<String, String>();
			request.getPortletSession().setAttribute("result", result);

			result.put("Is multipart", String.valueOf(PortletFileUpload.isMultipartContent(request)));
			FileItemFactory factory = new DiskFileItemFactory();
			PortletFileUpload fileUpload = new PortletFileUpload(factory);
			@SuppressWarnings("unchecked")
			List<FileItem> fileItems = fileUpload.parseRequest(request);
			int i = 1;
			for (FileItem item : fileItems) {
				if (item.isFormField()) {
					result.put(
							String.format("[%d] Form field", i),
							String.format("name=%s, value=%s", item.getFieldName(), item.getString()));
				}
				else {
					result.put(
							String.format("[%d] File", i),
							String.format("name=%s, fileName=%s, contentType=%s<br /><hr />%s<br /><hr />",
							item.getFieldName(), item.getName(), item.getContentType(),
							new String(item.get(), "UTF-8").replaceAll("<", "&lt;").replaceAll(">", "&gt;")));
				}
				i++;
			}
			result.put("Upload successful", String.valueOf(fileItems != null && !fileItems.isEmpty()));
		}
		catch (FileUploadException e) {
			throw new PortletException(e);
		}
	}
}
