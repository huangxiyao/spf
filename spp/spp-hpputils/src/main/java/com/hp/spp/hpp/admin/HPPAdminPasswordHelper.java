package com.hp.spp.hpp.admin;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.hp.spp.config.Config;
import com.hp.spp.config.ConfigException;

import com.hp.spp.portal.crypto.CryptoTools;
import com.hp.spp.portal.crypto.CryptoToolsException;
import com.hp.spp.portal.crypto.CryptoToolsImpl;

import com.hp.spp.portal.common.site.SiteManager;

import com.hp.spp.hpp.exception.HPPAdminException;

/**
 * This class provides utility methods for managing the HPP admin
 * password. In particular it provides two important methods:
 * <I>fetchHPPAdminPassword</I>
 * This is for fetching the HPP admin's password for a given site.
 *
 * <I>writeHPPAdminPassword</I>
 * This is for writing or storing the HPP admin's password for a given site.
 *
 * This class encapsulates the way the admin password is fetched and stored.
 * It also hides the encryption/decryption which is done when fetching or
 * storing the HPP admin password.
 *
 * @author akashs
 * 
 */
public class HPPAdminPasswordHelper{

	//Get a reference to a logger.
	private static final Logger mLog = Logger.getLogger(HPPAdminPasswordHelper.class);

	private static String mTripleDesSecretkey = null;

	/**
	 * This is the default constructor.
	 * It initializes the class. When the first instance of this class is
	 * created, this constructor loads the location of the Triple DES
	 * secret key in a class variable. This is done so that the key is
	 * not needlessly read again on each subsequent invocation of the
	 * class and its methods.
	 * 
	 * The location of the key is read from the SPP_CONFIG table. The key location
	 * should be stored in the table under the name: SPP.hpputils.DESKeyLocation 
	 *
	 * @throws HPPAdminException If an error occurs when getting the location of
	 * the secret key material or when reading the secret key material itself.
	 *
	 */
	public HPPAdminPasswordHelper() throws HPPAdminException {
		super();

		//FIXME (slawek) It seems this synchornized block is useless as the synchornization is
		//is done on an object being created therefore no other thread can use it until the creation
		//is complete.
		//In other words this means that this block is always executed. It also means that
		//if this object is construtcted in parallel by several threads, all of them may attempt
		//to set the value of mTripleDesSecretkey.
		synchronized (this){

			if(mTripleDesSecretkey == null) {
				//Step 1: Get the location of the TripleDES key.
				String locationOfKey = "/com/hp/spp/hpp/admin/keyfile";
					
				try{
					locationOfKey = Config.getValue("SPP.hpputils.DESKeyLocation");

				} catch (ConfigException e1) {

					//If key location not defined, it is a fatal error.
					//Do not proceed. Do not default key location.
					String error = "error occured while retrieving the secret key location " +
					"value from persistence store : " + e1.getMessage();
					 mLog.error(error);
					 throw new HPPAdminException (error, e1);
				}

				//Step 2: Read the key from the location as a String.
				mTripleDesSecretkey = loadKeyFromLocation(locationOfKey);
			}
		}

	}

	/**
	 * This constructor creates an object by taking the location of the secret key as parameter. 
	 * It loads the key from the given location and keeps it in the class variable.
	 * This is done so that the key is not needlessly read again on each subsequent 
	 * invocation of the class and its methods.
	 * In the normal course when the key location has been configured in the SPP_CONFIG
	 * table under the name:<B>SPP.hpputils.DESKeyLocation</B>, you should not use this   
	 * constructor. Use the default no argument constructor. 
	 * 
	 * @param keyLocation: Location of the key. This should be an absolute path staring with '\'
	 * and using '\' as path separator.
	 * @throws HPPAdminException If an error occurs when getting the location of
	 * the secret key material or when reading the secret key material itself.
	 *
	 */
	public HPPAdminPasswordHelper(String keyLocation) throws HPPAdminException {
		super();

		if(keyLocation == null ||keyLocation.trim().length() == 0){
			throw new IllegalArgumentException("Key location cannot be null or blank");
		}

		//FIXME (slawek) Creating several instances of this class results in the key being re-read.
		mTripleDesSecretkey = loadKeyFromLocation(keyLocation);
	}
	
	private synchronized String loadKeyFromLocation(String locationOfKey) throws HPPAdminException{

		InputStream is = this.getClass().getResourceAsStream(locationOfKey);
		StringBuffer tempKeyBuffer = new StringBuffer();
		String tripleDesSecretkey = null;
		
		try {
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			int c = 0;
			new StringBuffer();

			while ((c = isr.read()) != -1){
				tempKeyBuffer.append((char)c);
			}

		} catch (Exception e) {
			//Error while reading the key. Do not proceed.
			//Log error and exit.
			String error = "Error occured when reading the key.";
			error += " location of key = "+locationOfKey;
			error += " Error message: "+e.getMessage();
			 mLog.error(error);
			 throw new HPPAdminException (error, e);
		}

		if(tempKeyBuffer != null && tempKeyBuffer.length() != 0){
			tripleDesSecretkey = tempKeyBuffer.toString();

		}else{
			//Throw exception. No point in proceeding further.
			throw new HPPAdminException ("Secret key material not found at location "+locationOfKey);
		}
		return tripleDesSecretkey;

	}

	/**
	 * This method is responsible for fetching the HPP admin password
	 * for a given site. This method does the following:
	 *
	 * Step 1: Get the encryted HPP admin password for the passed in site
	 * from the database. The HPP admin password for a given site is stored
	 * as an encrypted password in the database under the key of the
	 * format: <B>SPP.<siteName>.HPPAdminPassword</B>
	 *
	 * Step 2: Use the crypto tools to decrypt the admin password and return it
	 *
	 * @param siteName The name of the site whose HPP admin password is desired.
	 * The encrypted password for this site should exist in the SPP_CONFIG table
	 * under the property SPP.<siteName>.HPPAdminPassword
	 * @throws HPPAdminException If an error ocurs in getting the HPP admin password.
	 * @throws IllegalArgumentException If the site name passed in is null or blank
	 *
	 */
	public String fetchHPPAdminPassword(String siteName) throws HPPAdminException {

		if(siteName == null || siteName.trim().length() == 0) {
			throw new IllegalArgumentException("Site name passed in cannot be null or blank");
		}

		String decryptedPassword = null;

		//Step 1: Get the encryted HPP admin password for the passed in site
		//from the proper location
		String encryptedPassword = SiteManager.getInstance().getSite(siteName).getHPPAdminPassword();
        if(encryptedPassword == null){
           throw new HPPAdminException("HPPAdminPassword is not available for site: "+siteName);
        }

        /*try{
			encryptedPassword = Config.getValue("SPP."+siteName.trim()+".HPPAdminPassword");

		} catch (ConfigException e1) {

			//If the encrypted admin password in not available in database,
			//throw an exception. No point in proceeding.
			String error = "error occured while retrieving the hpp admin password ";
			error += "value from persistence store";
			error += " sitename = "+siteName;
			error += " message from error = "+e1.getMessage();
			mLog.error(error);
			throw new HPPAdminException (error, e1);
		}  */

		//To be removed
		//encryptedPassword = "T2SfR3qLkG+AOF9onm+mEQ==";

		//Step 2: Use the crypto tools to decrypt the admin password
		decryptedPassword = decryptPassword(encryptedPassword);
		return decryptedPassword;
	}
	
	
	/**
	 * This method is responsible for fetching the HPP admin login id
	 * for a given site. This method does the following:
	 *
	 *
	 * @param siteName The name of the site whose HPP admin login id is desired.
	 * The Hpp admin login id  should exist in the SPP_CONFIG table
	 * under the property SPP.<siteName>.HPPAdminLoginId
	 * @throws HPPAdminException If an error ocurs in getting the HPP admin password.
	 * @throws IllegalArgumentException If the site name passed in is null or blank
	 *
	 */
	public String fetchHPPAdminLoginId(String siteName) throws HPPAdminException {

		if(siteName == null || siteName.trim().length() == 0) {
			throw new IllegalArgumentException("Site name passed in cannot be null or blank");
		}

		//Step 1: Get the encryted HPP admin password for the passed in site
		//from the proper location
		String hppAdminLoginId = SiteManager.getInstance().getSite(siteName).getHPPAdminLoginId();
		if(hppAdminLoginId == null){
           throw new HPPAdminException("HPPAdminLoginID is not available for site: "+siteName); 
        }
		/*try{
			hppAdminLoginId = Config.getValue("SPP."+siteName.trim()+".HPPAdminLoginId");
		} catch (ConfigException e1) {

			//If the admin login id is not available in database,
			//throw an exception. No point in proceeding.
			String error = "error occured while retrieving the hpp admin login id ";
			error += " value from persistence store";
			error += " sitename = "+siteName;
			error += " message from error = "+e1.getMessage();
			mLog.error(error);
			throw new HPPAdminException (error, e1);
		}*/
		return hppAdminLoginId;
	}
	/**
	 * This method returns the clear text value from a given encrypted string.
	 * It uses the key from the key location configured in the SPP_CONFIG table or
	 * the key location given when creating an instance of this class.
	 * 
	 * @param encryptedPassword
	 * @return clear text password
	 * @throws HPPAdminException If there is an exception from the Crypto package
	 * @throws IllegalArgumentException If the passed in encrypted password is null or blank.
	 */
	public String decryptPassword(String encryptedPassword) throws HPPAdminException{
		String clearPassword = null;
		
		if(encryptedPassword == null ||encryptedPassword.trim().length() == 0){
			throw new IllegalArgumentException("The passed in encrypted password cannot be null or blank");
		}
		
		//Use the crypto tools to decrypt the admin password
		CryptoTools cryptoTools = new CryptoToolsImpl(mTripleDesSecretkey);
		try {
			clearPassword = cryptoTools.decrypt(encryptedPassword);
		} catch (CryptoToolsException e) {
			//Error while decrypting the key using cryptoTools.decrypt
			String error = "Error occured when decrypting the encrypted String using cryptoTools.decrypt";
			error += "Encrypted String: "+encryptedPassword;
			throw new HPPAdminException(error, e);
			
		}
		
		return clearPassword;
	}

	public void writeHPPAdminPassword(String siteName, String clearTextPassword) throws HPPAdminException {
		
		if (siteName == null || "".equals(siteName.trim())){
			throw new IllegalArgumentException("site name cannot be null or blank");
			
		}
		
		if (clearTextPassword == null || "".equals(clearTextPassword)){
			throw new IllegalArgumentException("The given clear text password cannot be null or blank");
			
		}
		
		//Step 2: Store the encrypted password in the proper loacation.
		System.out.println(getEncryptedPassword(clearTextPassword));

		//Config.
	}

	public String getEncryptedPassword(String clearTextPassword) throws HPPAdminException {
		
		//Step 1: Encrypt the password using the crypto tools
		CryptoTools cryptoTools = new CryptoToolsImpl(mTripleDesSecretkey);
		String ecryptedPassword = null;
		try {
			ecryptedPassword = cryptoTools.encrypt(clearTextPassword);
		} catch (CryptoToolsException e) {
			//Error while decrypting the key using cryptoTools.decrypt
			String error = "Error occured when encrypting the string using cryptoTools.encrypt";
			throw new HPPAdminException(error, e);
		}

		//Step 2: Return the encrypted password.
		return ecryptedPassword;

		//Config.
	}
	
	public void printEncryptedPassword(String clearTextPassword){
		try {
			System.out.println(getEncryptedPassword(clearTextPassword));
			
		} catch (HPPAdminException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
