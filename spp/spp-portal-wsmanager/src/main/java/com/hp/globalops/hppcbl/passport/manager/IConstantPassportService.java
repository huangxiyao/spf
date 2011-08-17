package com.hp.globalops.hppcbl.passport.manager;

public class IConstantPassportService {

	// WSDL WebServices URL
	public final static String SYSTEST_MODE = "systest" ; // Dev. environement
	public final static String STAGING_MODE = "staging" ; // Int. environement
	public final static String PRODUCTION_MODE = "production" ; // Prod. environement
	
	// Default WebSservice URL -> see wsParameters.properties
	//public final static String DEFAULT_MODE = SYSTEST_MODE ; // TODO Choose your default mode...
	
	// Definition of parameters list name
	// TODO include file name in properties file
	public final static String WSPARAMETERSFILENAME = "wsParameters";

	public final static String ENDPOINTSYSTEST = "EndPointSysTest" ;
	public final static String ENDPOINTSTATING = "EndPointStating" ;
	public final static String ENDPOINTPRODUCTION = "EndPointProduction" ;

	public final static String NONPROXYHOSTSSYSTEST = "NonProxyHostsSysTest" ;
	public final static String NONPROXYHOSTSSTATING = "NonProxyHostsStating" ;
	public final static String NONPROXYHOSTSPRODUCTION = "NonProxyHostsProduction" ;

	public final static String USERNAMESYSTEST = "UserNameSysTest" ;
	public final static String USERNAMESTATING = "UserNameStating" ;
	public final static String USERNAMEPRODUCTION = "UserNameProduction" ;
	
	public final static String PASSWORDSYSTEST = "PasswordSysTest" ;
	public final static String PASSWORDSTATING = "PasswordStating" ;
	public final static String PASSWORDPRODUCTION = "PasswordProduction" ;
	
	public final static String DEFAULTLANGCODE = "DefaultLangCode" ;
	public final static String MODE = "Mode" ;
	
	public final static String PROXYHOST = "ProxyHost" ;
	public final static String PROXYPORT = "ProxyPort" ;

}
