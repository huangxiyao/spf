package com.hp.spp.portal.crypto;

public interface CryptoTools {

	public abstract String encrypt(String plaintext) throws CryptoToolsException;

	public abstract String decrypt(String ciphertext) throws CryptoToolsException;

	public abstract String hash(String plaintext);

	public abstract boolean verify(String plaintext, String hash);

}