package com.hp.spp.portal.crypto.cipher;


/**
 * This class offers some methods to encrypt and decrypt message with
 * the Blowfish algorithm
 */
public class Blowfish extends BaseCipher {

	public final static int KEY_SIZE = 128; // [32..448]
	
	public Blowfish() {
		init(KEY_SIZE, BLOWFISH, CBC, PKCS5Padding) ;
	}

}