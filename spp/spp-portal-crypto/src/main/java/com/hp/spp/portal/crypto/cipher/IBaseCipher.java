package com.hp.spp.portal.crypto.cipher;

import java.security.Key;

public interface IBaseCipher {

	public final static String UTF8 = "UTF-8" ;

	// Algorithm
	public final static String AES = "AES" ;
	public final static String AES192 = "AES192" ;
	public final static String AES256 = "AES256" ;
	public final static String BLOWFISH = "Blowfish" ;
	public final static String DESEDE = "DESede" ;
	public final static String TRIPLEDES = "DESede" ;
	
	// Mode
	public final static String CBC = "CBC" ;
	public final static String CFB8 = "CFB8" ;
	public final static String OFB32 = "OFB32" ;
	
	// Padding
	public final static String NoPadding = "NoPadding" ;
	public final static String PKCS5Padding = "PKCS5Padding" ;

	public abstract Key getSecretKey() ;

	/*
	 * Return all information of the key under a byte table. It can be stored
	 * to rebuild the key later with the setSecretKey(byte[] keyData) method.
	 */
	public byte[] getSecretKeyInBytes() ;

	public void setSecretKey(Key secretKey) ;

	/*
	 * Allow to rebuild the secret key with data stored in a byte table. 
	 */
	public void setSecretKey(byte[] keyData) ;

	public void generateKey() throws BaseCipherException ;

	public byte[] encrypt(byte[] plaintext) throws BaseCipherException ;

	public byte[] decrypt(byte[] ciphertext) throws BaseCipherException ;

}
