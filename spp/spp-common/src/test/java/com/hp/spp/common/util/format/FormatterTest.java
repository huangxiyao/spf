package com.hp.spp.common.util.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;

public class FormatterTest extends TestCase {

	public void testEscapeAmpersand() throws Exception {
		Formatter formatter = new Formatter();
		String simpleCase = formatter.escapeAmpersand("this & that");
		assertEquals("Result should be 'this &amp; that'", "this &amp; that", simpleCase);
		String withOtherEntities = formatter
				.escapeAmpersand("&quot;service1&quot; & others &apos;&lt;&gt;&apos;");
		String expectedResult = "&quot;service1&quot; &amp; others &apos;&lt;&gt;&apos;";
		assertEquals("The result should be " + expectedResult, expectedResult,
				withOtherEntities);
		String withHexadecimalEntities = formatter
				.escapeAmpersand("this & that &#038; that too");
		assertEquals("Result should be 'this &amp; that &#038; that too'",
				"this &amp; that &#038; that too", withHexadecimalEntities);
		String escapedXml = formatter.escapeAmpersand(new String(
				getTextFromFile("xml_with_ampersand.xml")));
		expectedResult = new String(getTextFromFile("xml_with_escaped_ampersand.xml"));
		assertEquals("Result should be same as in file", expectedResult, escapedXml);
	}

	public void testEncodeXmlForHtml() throws Exception {
		Formatter formatter = new Formatter();
		String encodedXml = formatter.encodeXmlForHtml(getTextFromFile("valid_xml.xml"));
		assertTrue("No '<' characters should be in the HTML", encodedXml.indexOf('<') == -1);
		assertTrue("No '>' characters should be in the HTML", encodedXml.indexOf('>') == -1);
		String expectedResult = new String(getTextFromFile("encodedXml.html"));
		assertEquals("Result should be same as in file", expectedResult, encodedXml);
	}

	private byte[] getTextFromFile(String fileName) throws IOException {
		byte[] xml = null;
		if (fileName != null) {
			URL xmlUrl = getClass().getClassLoader().getResource(fileName);
			File file = new File(xmlUrl.getFile());
			InputStream is = new FileInputStream(file);

			// Get the size of the file
			long length = file.length();

			// You cannot create an array using a long type.
			// It needs to be an int type.
			// Before converting to an int type, check
			// to ensure that file is not larger than Integer.MAX_VALUE.
			if (length > Integer.MAX_VALUE) {
				// File is too large
			}

			// Create the byte array to hold the data
			xml = new byte[(int) length];

			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < xml.length
					&& (numRead = is.read(xml, offset, xml.length - offset)) >= 0) {
				offset += numRead;
			}
		}
		return xml;
	}

}
