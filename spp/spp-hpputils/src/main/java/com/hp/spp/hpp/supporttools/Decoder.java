package com.hp.spp.hpp.supporttools;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * Class which provides coding capabilities related to hpp.
 * 
 * @author mvidal@capgemini.fr
 *
 */
public class Decoder {
	/**
	 * Logger.
	 */
	private static final Logger mLog = Logger.getLogger(Decoder.class);

	/**
	 * Decode UTF-8 base 64.
	 * @param stringEncoded the encoded string
	 * @return the decoded string
	 */
	public static String decode(String stringEncoded){
		try {
			if (stringEncoded == null) {
				return stringEncoded;
			}
			//This case is added to handle decoding of SM_USERDN and SM_UNIVERSALID
			//as returned by SiteMider in case they are encode.
			if(stringEncoded.startsWith("?UTF-8?B?")){
				String stringToDecode = stringEncoded.substring("?UTF-8?B?".length());
				byte[] decodedStringBytes = Base64.decodeBase64(stringToDecode.getBytes());
				return new String(decodedStringBytes, "UTF-8");
			}
			else if (stringEncoded.startsWith("=?UTF-8?B?") && stringEncoded.endsWith("?=")) {
				String stringToDecode = stringEncoded.substring("=?UTF-8?B?".length(), stringEncoded.length() - "?=".length());
				byte[] decodedStringBytes = Base64.decodeBase64(stringToDecode.getBytes());
				return new String(decodedStringBytes, "UTF-8");
			}
			else {
				return stringEncoded;
			}
		}
		catch(UnsupportedEncodingException e) {
			throw new IllegalStateException("Error decoding: " + stringEncoded, e);
		}
	}

}
