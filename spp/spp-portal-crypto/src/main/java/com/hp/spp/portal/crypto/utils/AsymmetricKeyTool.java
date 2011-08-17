package com.hp.spp.portal.crypto.utils; 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Hashtable;
import java.util.Arrays;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import sun.misc.BASE64Encoder;
import com.hp.spp.portal.crypto.utils.generator.GenerateAsymmetricKey;

/**
 * This tool is used to generate the Asymmetric private and public key pairs. The 
 * private key in written into the Config table in the database, while the public
 * key is stored into the filesystem.
 * <p><i>
 * Usage: AssymetricKeyTool 
 * -URL <weblogic url: t3://localhost:7001/> 
 *  -user  <userName weblogic admin name>
 *  -password <password weblogic admin password> 
 *  -dataSourceName  <<b><optional parameter></b> name of the datasource> 
 *  -mode <insert/update mode for the key >
 *  -configKey <name of the config key for private key storage>
 *  </i></p>
 */      

/**
 * @author girishsk
 *
 */


public class AsymmetricKeyTool {
	private static final String DEFAULT_JNDI_NAME = "jdbc/Vignette_PORTALDS";
	
	private static final String PRIVATE_KEY_FILE = "key.private";
	private static final String PUBLIC_KEY_FILE = "key.public";
	
	
	private  String mWeblogicURL = null;
	private  String mUserId  = null;
	private  String mPassword = null;
	private String mConfigKey = null;
	private String mMode = null;
	
	private String mDataSourceName = null;
	
	/**
	 * AsymmetricKeyTool  -URL <weblogic url> -user
	 *  userName -password <password>
	 *   -dataSourceName <optional>  -mode <insert/update>
	 */
	public static void main(String[] args) throws Exception {
		
		AsymmetricKeyTool asymmetricTool = new AsymmetricKeyTool();   
		
		if (args.length != 8) {
			printUsageAndExit();
		}
		
		for (int i = 0; i < args.length; i++){
			if ("-help".equals(args[i])) {
				printUsageAndExit();
			} 
			
			if ("-URL".equals(args[i])) {
				asymmetricTool.setWeblogicURL(args[i + 1]);
			} 
			if ("-user".equals(args[i])) {
				asymmetricTool.setUserId(args[i + 1]);
			}	
			if ("-password".equals(args[i])) {
				asymmetricTool.setPassword(args[i + 1]);
			} 
			/*
			 * TODO this can be hardcoded as SPP.privateKey.<siteName>
			 */
			
			if ("-configKey".equals(args[i])) {
				asymmetricTool.setConfigKey(args[i + 1]);
			}
			if ("-dataSourceName".equals(args[i])){
				asymmetricTool.setDataSourceName(args[i + 1]);
			}
			
			if ("-mode".equals(args[i])){
				try {
					asymmetricTool.setMode(args[i+1]);
				}
				catch (IllegalArgumentException e) {
					System.out.println("Invalid mode entered exiting ...");
					System.exit(-1);
				}
			}
		}

		/** Generate the keys now **/
		GenerateAsymmetricKey mAsymmetricKeys = new GenerateAsymmetricKey();
		KeyPair keyPair = mAsymmetricKeys.generateKey();
		
		/** Write the keys in db and and filesystem **/
		asymmetricTool.saveKeys(keyPair);
	}
	
	
	private static void printUsageAndExit(){
			System.out.println("Usage:" +  AsymmetricKeyTool.class.getName() +
								" -URL <weblogic url> -user " 
					+ " userName -password <password> -dataSourceName " + 
						" <optional>  -mode <insert/update> \n");

			System.out.println("e.g:" +  AsymmetricKeyTool.class.getName() +
				" -URL t3://g3u0488.houston.hp.com:50052" +
				" -user User -password Password  -dataSourceName \"jdbc/Vignette_PORTALDS\" " +
				" -mode insert -configKey spp.privateKey.sppqa ");
			
			System.out.println("Note: This tool requires the weblogic.jar" +
					" and spp-crypto jar files in classpath to run");

	}
 
	
	
	public AsymmetricKeyTool() {}
	
	
	/**
	 * Getters and setters
	 */
	private void setConfigKey(String configKey) {
		mConfigKey = configKey;
	}
	

	private void setPassword(String password) {
		mPassword = password;
	}
	
	
	
	private void setUserId(String userId) {
		mUserId = userId;
	}
	
	
	private void setWeblogicURL(String weblogicURL) {
		mWeblogicURL = weblogicURL;
	}

	private void setDataSourceName(String dataSourceName) {
		mDataSourceName = dataSourceName;
	}
	
	
	private void setMode(String mode) {
		if (!("insert".equals(mode) || "update".equals(mode))) {
			throw new IllegalArgumentException("Invalid mode: " + mode);
		}

		mMode = mode;
	}
	
	
	/** Saves the keys in database and filesystem.
	 * @param keyPair
	 * @throws IOException
	 * @throws SQLException
	 * @throws NamingException
	 */
	private void saveKeys(KeyPair keyPair) throws IOException, SQLException, NamingException {
		/** Precondition check **/
		if (this.mUserId == null || this.mPassword == null || this.mWeblogicURL == null)
			throw new IllegalArgumentException("UserId, password and weblogic URL should not be empty");
	
		System.out.println("URL : " + this.mWeblogicURL );
		System.out.println("User : " + this.mUserId );
		System.out.println("Password : " + this.mPassword );
		System.out.println("Config Key : " + this.mConfigKey );
		System.out.println("Mode : " + this.mMode );
		
		/** Write the keys in db and and filesystem **/
		this.savePrivateKeyToDb(keyPair);
		this.saveKeysToFile(keyPair);
	}
	
	private  void saveKeysToFile(KeyPair pair) throws IOException{ 
		PrivateKey privateKey = pair.getPrivate();
		PublicKey publicKey = pair.getPublic();
		FileOutputStream out;
		File file;
		file = new File(PRIVATE_KEY_FILE);
		out = new FileOutputStream(file);
		out.write(privateKey.getEncoded());
		out.close();
		System.out.println("Private key written to '" + file.getAbsolutePath() + "'");
		
		file = new File(PUBLIC_KEY_FILE);
		out = new FileOutputStream(file);
		out.write(publicKey.getEncoded());
		out.close();
		System.out.println("Public key written to '" + file.getAbsolutePath() + "'");
	}
	
	
	/**
	 * Performs query on the .
	 * @throws SQLException 
	 * @throws NamingException 
	 */
	private  void savePrivateKeyToDb(KeyPair keyPair) throws SQLException, NamingException {
		PrivateKey key = keyPair.getPrivate();
		PreparedStatement stmt = null;
		
		DataSource ds = this.getDataSource();
		
		if (ds == null){
			System.out.println("Datasource obtained is empty exiting");
			System.exit(-1);
		}
		
		/** Base-64 encode this key before storing it in database */
		String encodedKey = (new BASE64Encoder()).encodeBuffer(key.getEncoded());
		
		String query = null;
		String[] queryArgs = null;


		if("update".equals(this.mMode)){
			query = "UPDATE SPP_CONFIG_ENTRY set value=? where name=?";
			queryArgs = new String[] {encodedKey, mConfigKey};
		}else if("insert".equals(this.mMode)){
			query = "INSERT into SPP_CONFIG_ENTRY(name,value) values(?,?)";
			queryArgs = new String[] {mConfigKey, encodedKey};
		}
		
			Connection con = null ;
		try {
			con = ds.getConnection();
			
			System.out.print("Creating Statement... '" + query + "' with arguments " + Arrays.asList(queryArgs) + "\n");
			stmt = con.prepareStatement(query);
			for (int i = 0, len = queryArgs.length; i < len; ++i) {
				stmt.setString(i+1, queryArgs[i]);
			}

			System.out.print("Executing " + this.mMode + "  query...\n");
			stmt.executeUpdate();
			con.commit();
			
			System.out.print("Closing Statement...\n");
			System.out.print("Closing Connection...\n");
		} finally {
	       	close(con);
	       	close(stmt);
		}		
	}
	
	  private void close(Connection conn) {
	        try {
	            if (!conn.isClosed()) {
	                conn.close();
	            }
	        }
	        catch (Exception e) {
	            System.out.println("Error closing connection: " + e);
	        }
	    }

	    private void close(Statement stmt) {
	        try {
	            stmt.close();
	        }
	        catch (Exception e) {
	            System.out.println("Error closing statement: " + e);
	        }
	    }	   

	/**
	 * @throws NamingException 
	 * 
	 */
	private DataSource getDataSource() throws NamingException{
		InitialContext ctx = null;
		try {
			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					weblogic.jndi.WLInitialContextFactory.class.getName());
			env.put(Context.SECURITY_PRINCIPAL, this.mUserId);
			env.put(Context.SECURITY_CREDENTIALS, this.mPassword);
			env.put(Context.PROVIDER_URL, this.mWeblogicURL);
			ctx = new InitialContext(env);
			DataSource dataSource = (DataSource) ctx.lookup(this.mDataSourceName == null? 
										DEFAULT_JNDI_NAME: this.mDataSourceName);
			
			if (dataSource == null){
				System.out.println("Empty data source  exiting..");
				System.exit(-1);	    	    	
			}
			
			//Intialize the datasource 
			
			return dataSource;
		} catch (AuthenticationException e) {
			System.out.println("You've specified an invalid user name or password");
			throw e;
		}catch (CommunicationException e) {
			System.out.println(
					"Failed to contact " +
					((this.mWeblogicURL == null) ? "t3://localhost:7001" : this.mWeblogicURL) + ".\n" +
					"Is there a server running at this address?"
			);
		}
		
		finally {
			if (ctx != null) {                  
				// Always close context when finished.
				try {
					ctx.close();
				} catch (NamingException e) {
					System.out.println("Failed to close context due to: " + e);
				}
			}
		}

		return null;
	}
	
}





