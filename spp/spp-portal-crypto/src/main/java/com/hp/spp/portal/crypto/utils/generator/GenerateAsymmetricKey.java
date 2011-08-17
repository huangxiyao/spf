package com.hp.spp.portal.crypto.utils.generator;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.KeyPair;


public class GenerateAsymmetricKey {
private static final int KEY_SIZE = 1024;
	/**
	 Generate the private and public keys
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	**/

	public KeyPair generateKey() throws NoSuchAlgorithmException, NoSuchProviderException  {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		keyGen.initialize(KEY_SIZE, random);
		return keyGen.generateKeyPair();
	}
}
