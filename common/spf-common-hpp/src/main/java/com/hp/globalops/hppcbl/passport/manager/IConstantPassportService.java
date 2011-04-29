package com.hp.globalops.hppcbl.passport.manager;

public class IConstantPassportService {
	// Definition of parameters list name
	// TODO include file name in properties file
	public final static String WSPARAMETERSFILENAME = "wsParameters";
	
	public final static String MODE = "Mode";
	public final static String ENDPOINTS_PREFIX = "EndPoint";
	public final static String NONPROXYHOSTS_PREFIX = "NonProxyHosts";
	public final static String USERNAME_PREFIX = "UserName";
	public final static String PASSWORD_PREFIX = "Password";
	public final static String DEFAULTLANGCODE_PREFIX = "DefaultLangCode";
	public final static String PROXYHOST_PREFIX = "ProxyHost";
	public final static String PROXYPORT_PREFIX = "ProxyPort";
	public final static String ADMINUSER_PREFIX = "AdminUser";
	public final static String ADMINPASSWORD_PREFIX = "AdminPassword";

	//Definition for contorl characters error
	public final static int FAULT_MARSHALEXCEPTION_RULENUMBER = -100;
	public final static String FAULT_MARSHALEXCEPTION_DESCRIPTION = "Your input must not contain any control characters.";
	public final static String FAULT_MARSHALEXCEPTION_CODE = "control.char";
	public final static String FAULT_MARSHALEXCEPTION_FIELDNAME = "";
	public final static String FAULT_MARSHALEXCEPTION_FTYPE = "1";
}
