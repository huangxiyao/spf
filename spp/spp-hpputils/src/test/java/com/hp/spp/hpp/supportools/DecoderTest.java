package com.hp.spp.hpp.supportools;

import com.hp.spp.hpp.supporttools.Decoder;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DecoderTest extends TestCase {

	public void testDecoderForNull() throws Exception {
		assertNull("Decoding HPP header: ",Decoder.decode(null));
	}
	
	public void testDecoderForNonEncodedValue() throws Exception {
		assertEquals("Decoding Non Encoded HPP header: ","sppuser", Decoder.decode("sppuser"));
	}
	
	public void testDecoderForEncodedValue1() throws Exception {
		assertEquals("Decoding Endoded HPP header: ","sppuser", Decoder.decode("?UTF-8?B?c3BwdXNlcg=="));
	}
	
	public void testDecoderForEncodedValue2() throws Exception {
		assertEquals("Decoding Encoded HPP header: ","sppuser", Decoder.decode("=?UTF-8?B?c3BwdXNlcg==?="));
	}
}
