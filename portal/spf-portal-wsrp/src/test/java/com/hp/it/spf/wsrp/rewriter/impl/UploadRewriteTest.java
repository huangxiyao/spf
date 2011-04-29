package com.hp.it.spf.wsrp.rewriter.impl;

import junit.framework.TestCase;
import oasis.names.tc.wsrp.v2.types.UploadContext;
import oasis.names.tc.wsrp.v2.types.NamedString;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeSet;
import java.util.Arrays;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class UploadRewriteTest extends TestCase {

	/**
	 * Tests successful conversion of the VAP-provided data into OpenPortal expected data.
	 * @throws Exception if an unexpected error occurs
	 */
	public void testConvert() throws Exception {
		UploadContext uploadContext = new UploadContext();
		uploadContext.setMimeType("multipart/form-data; boundary=---------------------------287032381131322");
		uploadContext.setUploadData(
				("-----------------------------287032381131322\r\n" +
						"Content-Disposition: form-data; name=\"photo\"; filename=\"test.txt\"\r\n" +
						"Content-Type: text/plain\r\n" +
						"\r\n" +
						"test\r\n" +
						"-----------------------------287032381131322\r\n" +
						"Content-Disposition: form-data; name=\"photo2\"; filename=\"test.xml\"\r\n" +
						"Content-Type: text/xml\r\n" +
						"\r\n" +
						"<test></test>\r\n" +
						"-----------------------------287032381131322\r\n" +
						"Content-Disposition: form-data; name=\"submit\"\r\n" +
						"\r\n" +
						"Add\r\n" +
						"-----------------------------287032381131322\r\n" +
						"Content-Disposition: form-data; name=\"muti\"\r\n" +
						"\r\n" +
						"checkbox1\r\n" +
						"-----------------------------287032381131322\r\n" +
						"Content-Disposition: form-data; name=\"muti\"\r\n" +
						"\r\n" +
						"checkbox2\r\n" +
						"-----------------------------287032381131322--").getBytes("UTF-8"));
		UploadRewriter.FormData input = new UploadRewriter.FormData(null, new UploadContext[] {uploadContext});

		UploadRewriter.FormData result = invokeConvert(input);

		assertNotNull("Conversion result is not null", result);

		// check form parameters first
		NamedString[] formParameters = result.getFormParameters();
		assertNotNull("Form parameters are not null", formParameters);
		assertEquals("Number of form parameters", 3, formParameters.length);
		Set<String> mutiValues = new TreeSet<String>();
		for (NamedString param : formParameters) {
			if ("submit".equals(param.getName())) {
				assertEquals("Value of 'submit'", "Add", param.getValue());
			}
			else if ("muti".equals(param.getName())) {
				mutiValues.add(param.getValue());
			}
		}
		assertEquals("Values of 'muti'", new TreeSet<String>(Arrays.asList("checkbox1", "checkbox2")), mutiValues);

		// check uploaded files
		UploadContext[] uploadContexts = result.getUploadContexts();
		assertNotNull("Upload contexts is not null", uploadContexts);
		assertEquals("Number of files", 2, uploadContexts.length);
		for (UploadContext file : uploadContexts) {
			assertEquals("Mime attribute count", 2, file.getMimeAttributes().length);
			assertNotNull("Upload data", file.getUploadData());
		}
	}

	/**
	 * Invokes {@link com.hp.it.spf.wsrp.rewriter.impl.UploadRewriter#convert(com.hp.it.spf.wsrp.rewriter.impl.UploadRewriter.FormData)}
	 * private method.
	 * @param input convert method input value
	 * @return convert result
	 * @throws Exception if an error occurs while invoking convert method
	 */
	private UploadRewriter.FormData invokeConvert(UploadRewriter.FormData input) throws Exception {
		Method m = UploadRewriter.class.getDeclaredMethod("convert", UploadRewriter.FormData.class);
		m.setAccessible(true);
		return (UploadRewriter.FormData) m.invoke(new UploadRewriter(), input);
	}
}
