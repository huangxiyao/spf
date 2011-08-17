package com.hp.spp.portal.crypto.utils.generator;

import java.util.Random;

import sun.misc.BASE64Encoder;

import com.hp.spp.portal.crypto.cipher.AES;
import com.hp.spp.portal.crypto.cipher.BaseCipher;
import com.hp.spp.portal.crypto.cipher.BaseCipherException;

public class GenerateAESKey {

	public static void main(String[] args) {
		BaseCipher cipher = new AES();
		try {
			cipher.generateKey();
			System.out.println((new BASE64Encoder()).encodeBuffer(cipher.getSecretKeyInBytes()));
		} catch (BaseCipherException e) {
			e.printStackTrace();
		}

		byte[] bytes = new byte[16];
		Random random = new Random();
		random.nextBytes(bytes);

		for (int i = 0; i < bytes.length; i++) {
			if (i == 0)
				System.out.print("{ ");
			else
				System.out.print(", ");
			System.out.print(bytes[i]);
			if (i == bytes.length - 1)
				System.out.print(" }");
		}
	}

}
