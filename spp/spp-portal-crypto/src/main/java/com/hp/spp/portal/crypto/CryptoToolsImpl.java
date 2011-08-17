package com.hp.spp.portal.crypto;

import com.hp.spp.portal.crypto.cipher.TripleDES;
import com.hp.spp.portal.crypto.utils.ByteUtils;
import com.hp.spp.portal.crypto.utils.DigestUtils;

public class CryptoToolsImpl extends TripleDES implements CryptoTools {
	
	public CryptoToolsImpl(String key) {
		super() ;
		
		if(key == null)
			throw new NullPointerException("The SecretKey cannot be null!") ;
		
		setSecretKey(ByteUtils.string2Bytes(key)) ;
	}

	/* (non-Javadoc)
	 * @see com.hp.spp.portal.crypto.CryptoTools#encrypt(java.lang.String)
	 */
	public String encrypt(String plaintext) throws CryptoToolsException {
		if(plaintext == null)
			return null ;
		try {
			return ByteUtils.bytes2String(encrypt(plaintext.getBytes(UTF8))) ;
		} catch (Exception e) {
			throw new CryptoToolsException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.hp.spp.portal.crypto.CryptoTools#decrypt(java.lang.String)
	 */
	public String decrypt(String ciphertext) throws CryptoToolsException {
		if(ciphertext == null)
			return null ;
		try {
			return new String(decrypt(ByteUtils.string2Bytes(ciphertext)), UTF8) ;
		} catch (Exception e) {
			throw new CryptoToolsException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.hp.spp.portal.crypto.CryptoTools#hash(java.lang.String)
	 */
	public String hash(String plaintext) {
		if(null == plaintext)
			return null ;
		return DigestUtils.getMD5(DigestUtils.getSHA1(plaintext)) ;
	}
	
	/* (non-Javadoc)
	 * @see com.hp.spp.portal.crypto.CryptoTools#verify(java.lang.String, java.lang.String)
	 */
	public boolean verify(String plaintext, String hash) {
		if(null == plaintext || null == hash)
			return false ;
		return plaintext.equals(hash(hash));
	}
	
}
