package com.hp.spp.portal.crypto.utils;

import com.hp.spp.portal.crypto.CryptoTools;
import com.hp.spp.portal.crypto.CryptoToolsException;
import com.hp.spp.portal.crypto.CryptoToolsImpl;
import com.hp.spp.portal.crypto.cipher.BaseCipher;
import com.hp.spp.portal.crypto.cipher.TripleDES;

import sun.misc.BASE64Encoder;

public class EncryptDecryptTool {
	
	/**
	 * This key is used to encrypt/decrypt a text. 
	 * generateKey function need to be called to
	 * get the value. DES algorithm is used.
	 */
	private String key = "dUzLUvvpgFH4E4Z5Ue+RyHVMy1L76YBR";
	
	/**
	 * @param args
	 * args[0] stands for which function to invoke.
	 * args[1] if required contains text to be encrypted/decrypted
	 */
	public static void main(String[] args){
		if(args[0].equals("GenerateKey")){
			EncryptDecryptTool pass = new EncryptDecryptTool();
			pass.generateKey();
		}else if(args[0].equals("Encrypt")){
			EncryptDecryptTool pass = new EncryptDecryptTool();
			pass.encrypt(args[1]);
		}else if(args[0].equals("Decrypt")){
			EncryptDecryptTool pass = new EncryptDecryptTool();
			pass.decrypt(args[1]);
		}
	}
	
	/**
	 * Crypto tool utility is invoked to decrypt. 
	 * The key generated using generateKey() function is used.
	 * @param text  : value to be decrypted
	 */
	private void decrypt(String text){
		CryptoTools cryptoTools = new CryptoToolsImpl(key);
		String clearPassword = null;
		try {
			clearPassword = cryptoTools.decrypt(text);
		} catch (CryptoToolsException e) {
			e.printStackTrace();
		}
		System.out.println("Decrypted Password : "+clearPassword);
	}
	
	/**
	 * Crypto tool utility is invoked to encrypt plain text. 
	 * The key generated using generateKey() function is used.
	 * @param text  : value to be encrypted
	 */
	private void encrypt(String text){
		CryptoTools cryptoTools = new CryptoToolsImpl(key);
		String ecryptedPassword = null;
		try {
			ecryptedPassword = cryptoTools.encrypt(text);
		} catch (CryptoToolsException e) {
			e.printStackTrace();
		}
		System.out.println("Encrypted Password : "+ecryptedPassword);

	}
	
	/**
	 * This method is used to generate key for encryption/decryption
	 */
	private void generateKey(){
		BaseCipher cipher = new TripleDES();
		try{
			cipher.generateKey();
		}catch(Exception e){
			e.printStackTrace();
		}
		 System.out.println("Key :"+(new BASE64Encoder()).encodeBuffer(cipher.getSecretKeyInBytes()));
	}
}