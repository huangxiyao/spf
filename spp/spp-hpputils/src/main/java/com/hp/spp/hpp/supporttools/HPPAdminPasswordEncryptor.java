/**
 * 
 */
package com.hp.spp.hpp.supporttools;

import com.hp.spp.hpp.admin.HPPAdminPasswordHelper;

/**
 * This class provides tools for the support people 
 * to work on HPP utilities.
 * 
 * One of the important functions of this class is to print out the 
 * encrypted form of a given HPP admin password. 
 * 
 * @author akashs
 *
 */
public class HPPAdminPasswordEncryptor {
	private static final String DEFAULT_LOC_OF_KEY_FILE = "/com/hp/spp/hpp/admin/keyfile";
	
	private static final void  printUsage(){
		System.out.println("Usage: java HPPAdminPasswordEncryptor <clear text admin password> <optional location of the secret key file>");
		System.out.println("The 'clear text admin password' is required and must not be null.");
		System.out.println("You may optionally specify the 'location of the secret key file'. It must be an absolute path which begins with '\' and uses '\' as path separator.");
		
	}
	
	/**
	 * 
	 */
	public HPPAdminPasswordEncryptor() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String keyFileLocation = DEFAULT_LOC_OF_KEY_FILE;		
		
		if(args == null || args.length < 1){
			
			printUsage();
			
			System.exit(0);
		}
		
		int argsLength =  args.length;
		
		if(args[0] == null || "".equals(args[0])){
			printUsage();
			
			System.exit(0);
		}
		
		if(argsLength >1 && args[1] != null && "".equals(args[1].trim())){
			keyFileLocation = args[1].trim();
		}
		
		HPPAdminPasswordHelper adminUtil = null;
	
		try {
			adminUtil = new HPPAdminPasswordHelper(keyFileLocation);
			adminUtil.printEncryptedPassword(args[0]);
			
		}catch (Exception e) {
			System.out.println("A problem occured while calculating the encrypted value");
			System.out.println("Location of the keyFile = "+keyFileLocation);
			e.printStackTrace();
		}

	}

}
