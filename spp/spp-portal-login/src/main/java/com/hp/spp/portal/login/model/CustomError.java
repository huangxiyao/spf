package com.hp.spp.portal.login.model;

public class CustomError {
	private String mPortal;
	private int mErrorCode;
	private String mLocale;
	private String mCustomErrorMessage;
	
	
	public CustomError(String portal, int errorCode, String locale, String customErrorMessage) {
		super();
		mPortal = portal;
		mErrorCode = errorCode;
		mLocale = locale;
		mCustomErrorMessage = customErrorMessage;
	}
	
	public String getCustomErrorMessage() {
		return mCustomErrorMessage;
	}
	public void setCustomErrorMessage(String customErrorMessage) {
		mCustomErrorMessage = customErrorMessage;
	}
	public long getErrorCode() {
		return mErrorCode;
	}
	public void setErrorCode(int errorCode) {
		mErrorCode = errorCode;
	}
	public String getLocale() {
		return mLocale;
	}
	public void setLocale(String locale) {
		mLocale = locale;
	}
	public String getPortal() {
		return mPortal;
	}
	public void setPortal(String portal) {
		mPortal = portal;
	}
	
	
}
