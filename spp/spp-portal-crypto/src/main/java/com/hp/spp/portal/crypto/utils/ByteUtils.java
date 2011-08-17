package com.hp.spp.portal.crypto.utils;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ByteUtils {

	public static String byteArrayToHexString(byte bytes[]) {
		StringBuffer hex_string = new StringBuffer(bytes.length * 2);
		if (bytes != null) {
			for (int i = 0; i < bytes.length; i++) {
				int byte_val = bytes[i];
				if (byte_val < 0)
					byte_val += 256;
				String hex_code = Integer.toHexString(byte_val);
				if (hex_code.length() % 2 == 1)
					hex_code = "0" + hex_code;
				hex_string.append(hex_code);
			}

		}
		return hex_string.toString();
	}

	public static byte[] hexStringToByteArray(String hex_string) {
		if (hex_string == null)
			return null;
		hex_string = hex_string.trim();
		int len = hex_string.length() / 2;
		byte bytes[] = new byte[len];
		for (int i = 0; i < len; i++) {
			int hex_pos = i * 2;
			String hex_code = hex_string.substring(hex_pos, hex_pos + 2);
			bytes[i] = (byte) Integer.parseInt(hex_code, 16);
		}

		return bytes;
	}
	
	public static String bytes2String(byte[] bytes) {
		return (new BASE64Encoder()).encodeBuffer(bytes) ; 
	}
	
	public static byte[] string2Bytes(String string) {
		try {
			return (new BASE64Decoder()).decodeBuffer(string) ;
		} catch (IOException e) {
			return null;
		}
	}

}
