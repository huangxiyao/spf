package com.hp.spp.portal.login.model;

/**
 * Class which determines an error worklow.
 * <p>
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public class Workflow {
	/**
	 * The path and menu item of the landing page.
	 */
	private String mPortal = null;

	/**
	 * The path and menu item of the landing page.
	 */
	private String mLandingPage = null;

	/**
	 * determines if urls need to be localized.
	 */
	private boolean mLocalizationInURL = false;

	/**
	 * the target URL of the workflow.
	 */
	private String mTargetURL = null;

	/**
	 * the error code.
	 */
	private long mErrorCode;

	/**
	 * the message error to display.
	 */
	private String mErrorMessage = null;

	/**
	 * if we need to display the error message.
	 */
	private boolean mDisplayErrorMessage = false;

	public Workflow() {
		super();
	}

	public Workflow(String portal, long errorCode, String targetURL, 
			int displayErrorMessage) {
		super();
		mPortal = portal;
		mErrorCode = errorCode;
		mTargetURL = targetURL;
		if (displayErrorMessage == 0)
			mDisplayErrorMessage = false;
		else
			mDisplayErrorMessage = true;
	}

	public String getLandingPage() {
		return mLandingPage;
	}

	public void setLandingPage(String landingPage) {
		this.mLandingPage = landingPage;
	}

	public boolean isLocalizationInURL() {
		return mLocalizationInURL;
	}

	public void setLocalizationInURL(boolean localizationInURL) {
		this.mLocalizationInURL = localizationInURL;
	}

	public String getErrorMessage() {
		return mErrorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.mErrorMessage = errorMessage;
	}

	public boolean isDisplayErrorMessage() {
		return mDisplayErrorMessage;
	}

	public void setDisplayErrorMessage(boolean displayErrorMessage) {
		this.mDisplayErrorMessage = displayErrorMessage;
	}

	public String getTargetURL() {
		return mTargetURL;
	}

	public void setTargetURL(String targetURL) {
		this.mTargetURL = targetURL;
	}

	public String getPortal() {
		return mPortal;
	}

	public void setPortal(String portal) {
		mPortal = portal;
	}

	public long getErrorCode() {
		return mErrorCode;
	}

	public void setErrorCode(long errorCode) {
		mErrorCode = errorCode;
	}

}
