package com.hp.globalops.hppcbl.passport.manager;

import java.util.Enumeration;
import java.util.ResourceBundle;

public class PassportParametersManager {

	private static PassportParametersManager M_wsManager = new PassportParametersManager() ;

	private String mEndPointSysTest = null ;
	private String mEndPointStating = null ;
	private String mEndPointProduction = null ;

	private String mNonProxyHostsSysTest = null ;
	private String mNonProxyHostsStating = null ;
	private String mNonProxyHostsProduction = null ;

	private String mUserNameSysTest = null ;
	private String mUserNameStating = null ;
	private String mUserNameProduction = null ;

	private String mPasswordSysTest = null ;
	private String mPasswordStating = null ;
	private String mPasswordProduction = null ;

	private String mDefaultLangCode = null ;
	private String mMode = null ;

	private String mProxyHost = null ;
	private String mProxyPort = null ;

	private PassportParametersManager() {
		super();
		init() ;
	}
	
	public static PassportParametersManager getInstance() {
		return M_wsManager ;
	}
	
	private void init() {
		ResourceBundle parameters = ResourceBundle.getBundle(IConstantPassportService.WSPARAMETERSFILENAME);
		Enumeration enumeration = parameters.getKeys() ;
		while(enumeration.hasMoreElements())
		{
			String key = (String)enumeration.nextElement() ;
			if(key.equals(IConstantPassportService.DEFAULTLANGCODE))
				setDefaultLangCode(parameters.getString(key)) ;
			else if(key.equals(IConstantPassportService.ENDPOINTPRODUCTION))
				setEndPointProduction(parameters.getString(key)) ;
			else if(key.equals(IConstantPassportService.ENDPOINTSTATING))
				setEndPointStating(parameters.getString(key)) ;
			else if(key.equals(IConstantPassportService.ENDPOINTSYSTEST))
				setEndPointSysTest(parameters.getString(key)) ;
			else if(key.equals(IConstantPassportService.MODE))
				setMode(parameters.getString(key)) ;
			else if(key.equals(IConstantPassportService.PASSWORDSYSTEST))
				setPasswordSysTest(parameters.getString(key)) ;
			else if(key.equals(IConstantPassportService.PASSWORDSTATING))
				setPasswordStating(parameters.getString(key)) ;
			else if(key.equals(IConstantPassportService.PASSWORDPRODUCTION))
				setPasswordProduction(parameters.getString(key)) ;
			else if(key.equals(IConstantPassportService.PROXYHOST))
				setProxyHost(parameters.getString(key)) ;
			else if(key.equals(IConstantPassportService.PROXYPORT))
				setProxyPort(parameters.getString(key)) ;
			else if(key.equals(IConstantPassportService.NONPROXYHOSTSSYSTEST))
				setNonProxyHostsSysTest(parameters.getString(key)) ;
			else if(key.equals(IConstantPassportService.NONPROXYHOSTSSTATING))
				setNonProxyHostsStating(parameters.getString(key)) ;
			else if(key.equals(IConstantPassportService.NONPROXYHOSTSPRODUCTION))
				setNonProxyHostsProduction(parameters.getString(key)) ;
			else if(key.equals(IConstantPassportService.USERNAMESYSTEST))
				setUserNameSysTest(parameters.getString(key)) ;
			else if(key.equals(IConstantPassportService.USERNAMESTATING))
				setUserNameStating(parameters.getString(key)) ;
			else if(key.equals(IConstantPassportService.USERNAMEPRODUCTION))
				setUserNameProduction(parameters.getString(key)) ;
		}
	}

	public String getDefaultLangCode() {
		return mDefaultLangCode;
	}

	private void setDefaultLangCode(String defaultLangCode) {
		mDefaultLangCode = defaultLangCode;
	}

	private String getEndPointProduction() {
		return mEndPointProduction;
	}

	private void setEndPointProduction(String endPointProduction) {
		mEndPointProduction = endPointProduction;
	}

	private String getEndPointStating() {
		return mEndPointStating;
	}

	private void setEndPointStating(String endPointStating) {
		mEndPointStating = endPointStating;
	}

	private String getEndPointSysTest() {
		return mEndPointSysTest;
	}

	private void setEndPointSysTest(String endPointSysTest) {
		mEndPointSysTest = endPointSysTest;
	}

	public String getMode() {
		return mMode;
	}

	private void setMode(String mode) {
		mMode = mode;
	}

	public String getPassword() {
		String password = "" ;
		
		if(IConstantPassportService.SYSTEST_MODE.equalsIgnoreCase(getMode()))
			password = getPasswordSysTest() ;
		else if(IConstantPassportService.STAGING_MODE.equalsIgnoreCase(getMode()))
			password = getPasswordStating() ;
		else if(IConstantPassportService.PRODUCTION_MODE.equalsIgnoreCase(getMode()))
			password = getPasswordProduction() ;
		else
			password = getPasswordProduction() ;
		
		return password ;
	}

	public String getUserName() {
		String userName = "" ;
		
		if(IConstantPassportService.SYSTEST_MODE.equalsIgnoreCase(getMode()))
			userName = getUserNameSysTest() ;
		else if(IConstantPassportService.STAGING_MODE.equalsIgnoreCase(getMode()))
			userName = getUserNameStating() ;
		else if(IConstantPassportService.PRODUCTION_MODE.equalsIgnoreCase(getMode()))
			userName = getUserNameProduction() ;
		else
			userName = getUserNameProduction() ;
		
		return userName ;
	}

	public String getEndPoint() {
		String endPoint = "" ;
		
		if(IConstantPassportService.SYSTEST_MODE.equalsIgnoreCase(getMode()))
			endPoint = getEndPointSysTest() ;
		else if(IConstantPassportService.STAGING_MODE.equalsIgnoreCase(getMode()))
			endPoint = getEndPointStating() ;
		else if(IConstantPassportService.PRODUCTION_MODE.equalsIgnoreCase(getMode()))
			endPoint = getEndPointProduction() ;
		else
			endPoint = getEndPointProduction() ;
		
		return endPoint ;
	}

	public String getProxyPort() {
		return mProxyPort;
	}

	private void setProxyPort(String proxyPort) {
		mProxyPort = proxyPort;
	}

	public String getProxyHost() {
		return mProxyHost;
	}

	private void setProxyHost(String proxyHost) {
		mProxyHost = proxyHost;
	}

	public String getNonProxyHosts() {
		String nonProxyHosts = "" ;
		
		if(IConstantPassportService.SYSTEST_MODE.equalsIgnoreCase(getMode()))
			nonProxyHosts = getNonProxyHostsSysTest() ;
		else if(IConstantPassportService.STAGING_MODE.equalsIgnoreCase(getMode()))
			nonProxyHosts = getNonProxyHostsStating() ;
		else if(IConstantPassportService.PRODUCTION_MODE.equalsIgnoreCase(getMode()))
			nonProxyHosts = getNonProxyHostsProduction() ;
		else
			nonProxyHosts = getNonProxyHostsProduction() ;
		
		return nonProxyHosts ;
	}

	private String getNonProxyHostsProduction() {
		return mNonProxyHostsProduction;
	}

	private void setNonProxyHostsProduction(String nonProxyHostsProduction) {
		mNonProxyHostsProduction = nonProxyHostsProduction;
	}

	private String getNonProxyHostsStating() {
		return mNonProxyHostsStating;
	}

	private void setNonProxyHostsStating(String nonProxyHostsStating) {
		mNonProxyHostsStating = nonProxyHostsStating;
	}

	private String getNonProxyHostsSysTest() {
		return mNonProxyHostsSysTest;
	}

	private void setNonProxyHostsSysTest(String nonProxyHostsSysTest) {
		mNonProxyHostsSysTest = nonProxyHostsSysTest;
	}

	private String getPasswordProduction() {
		return mPasswordProduction;
	}

	private void setPasswordProduction(String passwordProduction) {
		mPasswordProduction = passwordProduction;
	}

	private String getPasswordStating() {
		return mPasswordStating;
	}

	private void setPasswordStating(String passwordStating) {
		mPasswordStating = passwordStating;
	}

	private String getPasswordSysTest() {
		return mPasswordSysTest;
	}

	private void setPasswordSysTest(String passwordSysTest) {
		mPasswordSysTest = passwordSysTest;
	}

	private String getUserNameProduction() {
		return mUserNameProduction;
	}

	private void setUserNameProduction(String userNameProduction) {
		mUserNameProduction = userNameProduction;
	}

	private String getUserNameStating() {
		return mUserNameStating;
	}

	private void setUserNameStating(String userNameStating) {
		mUserNameStating = userNameStating;
	}

	private String getUserNameSysTest() {
		return mUserNameSysTest;
	}

	private void setUserNameSysTest(String userNameSysTest) {
		mUserNameSysTest = userNameSysTest;
	}

}
