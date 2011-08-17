package com.hp.spp.portal.crypto.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtils {
	
	private static MessageDigest mMessageDigest = null ;

	public static String getMD5(String input) {
		if (StringUtils.isEmpty(input))
			return "";
		if (mMessageDigest == null)
			try {
				mMessageDigest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nsaex) {
			}
		byte string_bytes[];
		try {
			string_bytes = input.getBytes("ascii");
		} catch (UnsupportedEncodingException e) {
			string_bytes = input.getBytes();
		}
		byte result[] = mMessageDigest.digest(string_bytes);
		return ByteUtils.byteArrayToHexString(result);
	}

	public static String getSHA1(String input_string) {
		if (StringUtils.isEmpty(input_string))
			return "";
		if (mMessageDigest == null)
			try {
				mMessageDigest = MessageDigest.getInstance("SHA1");
			} catch (NoSuchAlgorithmException nsaex) {
			}
		byte string_bytes[];
		try {
			string_bytes = input_string.getBytes("ascii");
		} catch (UnsupportedEncodingException e) {
			string_bytes = input_string.getBytes();
		}
		byte result[] = mMessageDigest.digest(string_bytes);
		return ByteUtils.byteArrayToHexString(result);
	}

}
