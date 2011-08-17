package com.hp.spp.portal.crypto.cipher;

public class AES extends BaseCipher {

	public final static int KEY_SIZE = 128; // [128 or 192 or 256]
	
	public AES() {
		String algorithm = AES ;
		if(KEY_SIZE != 128)
			algorithm = algorithm.concat(String.valueOf(KEY_SIZE)) ;
		init(KEY_SIZE, algorithm, CBC, PKCS5Padding) ;
	}

}
