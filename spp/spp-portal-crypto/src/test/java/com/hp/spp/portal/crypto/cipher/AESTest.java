package com.hp.spp.portal.crypto.cipher;

import java.io.UnsupportedEncodingException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.hp.spp.portal.crypto.utils.ByteUtils;

public class AESTest extends TestCase {

	public AESTest(String name) {
		super(name);
	}

	/**
	 * The suite of test to run. Uncomment "suite.setUpAtEachTest()" if you need to have a
	 * setUp/tearDown for each test
	 * 
	 * @return the test suite
	 * @throws Exception to make sure that each unexpected Exception will be noticed
	 */
	public static Test suite() throws Exception {
		TestSuite suite = new TestSuite();
		suite.addTest(new AESTest("testEncryptDecrypt"));
		return suite;
	}
	
	public void testEncryptDecrypt() throws Exception {
		BaseCipher cipher = new AES() ;
		cipher.generateKey() ;
		
		byte[] bytes = null ;
		byte[] encryptBytes = null ;
		byte[] decryptBytes = null ;
		
		String message = "Blowfish simple encryption/decryption test!" ;
		
		// Encryption
		try {
			bytes = message.getBytes(BaseCipher.UTF8) ;
		} catch (UnsupportedEncodingException e) {
			bytes = null ;
		}
		encryptBytes = cipher.encrypt(bytes) ;
		String encrypt = ByteUtils.bytes2String(encryptBytes) ;

		// Decryption
		encryptBytes = ByteUtils.string2Bytes(encrypt) ;
		decryptBytes = cipher.decrypt(encryptBytes) ;
		String decrypt;
		try {
			decrypt = new String(decryptBytes, BaseCipher.UTF8);
		} catch (UnsupportedEncodingException e) {
			decrypt = null ;
		}
		
		assertEquals("message not retrieved", message, decrypt);
	}
}
