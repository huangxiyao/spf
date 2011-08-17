package com.hp.spp.portal.crypto.cipher;


/**
 * This class offers some methods to encrypt and decrypt message with
 * the TripleDES algorithm
 */
public class TripleDES extends BaseCipher {
	public final static int KEY_SIZE = 112; // [112 or 168]

	public TripleDES() {
		init(KEY_SIZE, TRIPLEDES, CBC, PKCS5Padding) ;
	}

}