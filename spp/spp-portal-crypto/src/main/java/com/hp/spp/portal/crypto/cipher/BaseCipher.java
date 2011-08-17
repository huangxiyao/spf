package com.hp.spp.portal.crypto.cipher;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public abstract class BaseCipher implements IBaseCipher {
	
//	private static final Random RANDOM = new Random(System.currentTimeMillis());

	private Key mSecretKey = null ;
	private IvParameterSpec mIV = null ;
	
	private String mAlgorithm = null ;
	private String mCipherAlgorithm = null ;
	private String mMode = null ;
	private int mKeySize ;
	
	protected void init(int keySize, String algorithm) {
		init(keySize, algorithm, "", "") ;
	}
	
	protected void init(int keySize, String algorithm, String mode, String padding) {
		if(algorithm == null)
			throw new NullPointerException("algorithm parameter cannot be null!") ;
		if(mode == null)
			throw new NullPointerException("mode parameter cannot be null!") ;
		if(padding == null)
			throw new NullPointerException("padding parameter cannot be null!") ;
		
		mKeySize = keySize ;
		mAlgorithm = algorithm ;
		
		mCipherAlgorithm = mAlgorithm ;
		if(!mode.equals("") && !padding.equals("")) {
			mMode = mode ;
			mCipherAlgorithm = mCipherAlgorithm.concat("/").concat(mMode).concat("/").concat(padding) ;
			
			if(mMode.equals(CBC)) {
				if(mAlgorithm.indexOf(AES) != -1)
					mIV = new IvParameterSpec(new byte[] { -19, 115, 67, -102, 80, -19, -110, -61, 104, -39, -101, 75, 111, -93, -88, -40 }) ;
				else
					mIV = new IvParameterSpec(new byte[] { 88, 42, -63, -84, 86, -13, -79, -77 }) ;
			}
		}
	}

	public Key getSecretKey() {
		return mSecretKey;
	}

	/**
	 * Return all information of the key under a byte table. It can be stored
	 * to rebuild the key later with the setSecretKey(byte[] keyData) method.
	 */
	public byte[] getSecretKeyInBytes() {
		return mSecretKey.getEncoded();
	}

	public void setSecretKey(Key secretKey) {
		this.mSecretKey = secretKey;
	}

	/**
	 * Allow to rebuild the secret key with data stored in a byte table. 
	 */
	public void setSecretKey(byte[] keyData) {
		mSecretKey = new SecretKeySpec(keyData, mAlgorithm);
	}

	public void generateKey() throws BaseCipherException {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance(mAlgorithm);
			keyGen.init(mKeySize);
			mSecretKey = keyGen.generateKey();
		} catch (Exception e) {
			throw new BaseCipherException(e) ;
		}
	}

	public byte[] encrypt(byte[] plaintext) throws BaseCipherException {
		try {
			Cipher cipher = Cipher.getInstance(mCipherAlgorithm);
			if(mIV == null)
				cipher.init(Cipher.ENCRYPT_MODE, mSecretKey);
			else
				cipher.init(Cipher.ENCRYPT_MODE, mSecretKey, mIV);
			return cipher.doFinal(plaintext);
		} catch (Exception e) {
			throw new BaseCipherException(e) ;
		}
	}

	public byte[] decrypt(byte[] ciphertext) throws BaseCipherException {
		try {
			Cipher cipher = Cipher.getInstance(mCipherAlgorithm);
			if(mIV == null)
				cipher.init(Cipher.DECRYPT_MODE, mSecretKey);
			else
				cipher.init(Cipher.DECRYPT_MODE, mSecretKey, mIV);
			return cipher.doFinal(ciphertext);
		} catch (Exception e) {
			throw new BaseCipherException(e) ;
		}
	}

//	private byte[] generateRandomBytes(int size) {
//		byte[] bytes = new byte[size] ;
//		RANDOM.nextBytes(bytes) ;
//		return bytes;
//	}

}
