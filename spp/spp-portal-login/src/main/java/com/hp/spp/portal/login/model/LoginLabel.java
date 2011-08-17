package com.hp.spp.portal.login.model;

public class LoginLabel {
	private String mLocale;
	private String mLabel;
    private String mSiteName;
    private String mMessage;
	
	public LoginLabel(String locale, String label, String message, String siteName) {
		mLocale = locale;
		mLabel = label;
		mMessage = message;
        mSiteName = siteName;
    }

    public String getSiteName(){
        return mSiteName;
    }
    public void setSiteName(String siteName){
        mSiteName = siteName;
    }
    public String getLabel() {
		return mLabel;
	}
	public void setLabel(String label) {
		this.mLabel = label;
	}
	public String getLocale() {
		return mLocale;
	}
	public void setLocale(String locale) {
		this.mLocale = locale;
	}
	public String getMessage() {
		return mMessage;
	}
	public void setMessage(String message) {
		this.mMessage = message;
	}
	
}
