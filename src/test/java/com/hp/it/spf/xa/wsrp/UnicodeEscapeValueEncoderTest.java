package com.hp.it.spf.xa.wsrp;

import junit.framework.TestCase;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */

public class UnicodeEscapeValueEncoderTest extends TestCase
{
	public void testAppendEncoded() {
		UnicodeEscapeValueEncoder encoder = new UnicodeEscapeValueEncoder();

		StringBuffer asis = encoder.appendEncoded(new StringBuffer(), "Wojtek");
		assertEquals("Value without non-ascii characters", "Wojtek", asis.toString());

		StringBuffer nullValue = encoder.appendEncoded(new StringBuffer(), null);
		assertTrue("Null value is ignored", nullValue.length() == 0);

		StringBuffer encoded1 = encoder.appendEncoded(new StringBuffer(), "W\u0142adys\u0142aw");
		assertEquals("Encoded value 1", "W\\u0142adys\\u0142aw", encoded1.toString());

		StringBuffer encoded2 = encoder.appendEncoded(new StringBuffer(), "\u0142ukasz");
		assertEquals("Encoded value 2", "\\u0142ukasz", encoded2.toString());

		StringBuffer encoded3 = encoder.appendEncoded(new StringBuffer(), "Micha\u0142");
		assertEquals("Encoded value 3", "Micha\\u0142", encoded3.toString());

		StringBuffer middleSlash = encoder.appendEncoded(new StringBuffer(), "x\\y");
		assertEquals("Encoded middle slash", "x\\\\y", middleSlash.toString());

		StringBuffer leadingSlash = encoder.appendEncoded(new StringBuffer(), "\\x");
		assertEquals("Encoded leading slash", "\\\\x", leadingSlash.toString());

		StringBuffer trailingSlash = encoder.appendEncoded(new StringBuffer(), "x\\");
		assertEquals("Encoded trailing slash", "x\\\\", trailingSlash.toString());
	}

	public void testDecodeFragment() {
		UnicodeEscapeValueEncoder encoder = new UnicodeEscapeValueEncoder();
		String input;

		input = "Wojtek";
		String asis = encoder.decodeFragment(input, 0, input.length());
		assertEquals("Value without non-ascii characters", input, asis);

		input = null;
		String nullValue = encoder.decodeFragment(input, 0, 0);
		assertNull("Null value", nullValue);

		input = "W\\u0142adys\\u0142aw";
		String decoded1 = encoder.decodeFragment(input, 0, input.length());
		assertEquals("Decoded value 1", "W\u0142adys\u0142aw", decoded1);

		input = "\\u0142ukasz";
		String decoded2 = encoder.decodeFragment(input, 0, input.length());
		assertEquals("Decoded value 2", "\u0142ukasz", decoded2);

		input = "Micha\\u0142";
		String decoded3 = encoder.decodeFragment(input, 0, input.length());
		assertEquals("Decoded value 3", "Micha\u0142", decoded3);

		input = "a\\\\b";
		String middleSlash = encoder.decodeFragment(input, 0, input.length());
		assertEquals("Decoded middle slash", "a\\b", middleSlash);

		input = "\\\\a";
		String leadingSlash = encoder.decodeFragment(input, 0, input.length());
		assertEquals("Decoded leading slash", "\\a", leadingSlash);

		input = "a\\\\";
		String trailingSlash = encoder.decodeFragment(input, 0, input.length());
		assertEquals("Decoded trailing slash", "a\\", trailingSlash);
	}

}
