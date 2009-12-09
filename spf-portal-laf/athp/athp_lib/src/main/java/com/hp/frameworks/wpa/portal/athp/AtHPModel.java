package com.hp.frameworks.wpa.portal.athp;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This model object represents all of the configurable properties of a standard
 * &#64;hp c-frame for use with the &#64;hp VAP Components. Each property exposed 
 * by this model maps to an element of the &#64;hp header, 
 * navigation menu or footer that can be customized by the application developer. 
 * This object is not initialized with any default
 * values, it is used to communicate app-specific customizations to whichever 
 * service that will ultimately render the layout. It is the responsibility of the
 * rendering service to, where appropriate, provide sensible defaults for values
 * that are not explicitly set through this model.
 */
public class AtHPModel {
	
	private String windowTitle;
	private String title;
	private String secondaryTitle;
	private String tertiaryTitle;
	private String greetingMessage;
	private String warningMessage;
	
	private boolean bodyStyleDisabled = false;
	private String loginUrl;
	private String logoutUrl;
	private String siteSearchID;
	private String siteSearchLabel;
	private boolean myLinksDisabled = false;;
	private boolean generateBreadcrumbs = false;
	
	private boolean horizontalMenuStyle = false;
	
	private String feedbackUrl;
	private String supportUrl;
	private String targetFrame;
	private boolean confidentialLabel = false;
	private boolean publicLabel = false;
	private boolean revisionDateDisplayed;
	private String versionNumber;
	
	private List breadcrumbItems;
	private Properties metaInfos;

	private List leftMenuItems;

	public AtHPModel() {
		breadcrumbItems = new ArrayList();
		metaInfos = new Properties();
		leftMenuItems = new ArrayList();
	}

	/**
	 * Get the browser window title.
	 */
	public String getWindowTitle() {
		return windowTitle;
	}

	/**
	 * Set the browser window title.
	 */
	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}
	
	/**
	 * Get the boolean value which indicates whether or not the standard 
	 * &#64;hp body content styles are disabled.   
	 */
	public boolean isBodyStyleDisabled() {
		return bodyStyleDisabled;
	}

	/**
	 * Set the boolean value which indicates whether or not the standard 
	 * &#64;hp body content styles are disabled.   
	 * The default is <code>false</code>.
	 */
	public void setBodyStyleDisabled(boolean bodyStyleDisabled) {
		this.bodyStyleDisabled = bodyStyleDisabled;
	}

	/**
	 * Get the destination url for the "Log On" hyperlink.
	 */
	public String getLoginUrl() {
		return loginUrl;
	}

	/**
	 * Set the destination url for the "Log On" hyperlink.
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	/**
	 * Get the destination URL for the "Log Off" hyperlink.
	 */
	public String getLogoutUrl() {
		return logoutUrl;
	}

	/**
	 * Set the "Log On" url.
	 */
	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	/**
	 * Get the search ID to use when searching the local site. 
	 */
	public String getSiteSearchID() {
		return siteSearchID;
	}

	/**
	 * Set the search ID to use when searching the local site. 
	 */
	public void setSiteSearchID(String siteSearchID) {
		this.siteSearchID = siteSearchID;
	}

	/**
	 * Get the text for the site search label.
	 */
	public String getSiteSearchLabel() {
		return siteSearchLabel;
	}

	/**
	 * Set the text for the site search label.
	 */
	public void setSiteSearchLabel(String siteSearchLabel) {
		this.siteSearchLabel = siteSearchLabel;
	}

	/**
	 * Get the boolean value that indicates whether or not the "My Links" 
	 * hyperlink should be displayed as part of the banner.
	 */
	public boolean isMyLinksDisabled() {
		return myLinksDisabled;
	}

	/**
	 * Set the boolean value that indicates whether or not the "My Links" 
	 * hyperlink should be displayed as part of the banner.
	 * The default is <code>false</code>.
	 */
	public void setMyLinksDisabled(boolean myLinksDisabled) {
		this.myLinksDisabled = myLinksDisabled;
	}

	/**
	 * Get the page title (sometimes referred to as the page header or banner).
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the page title (sometimes referred to as the page header or banner).
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the secondary page title rendered immediately after the value of the 
	 * title property.
	 */
	public String getSecondaryTitle() {
		return secondaryTitle;
	}

	/**
	 * Set the secondary page title rendered immediately after the value of the 
	 * title property.
	 */
	public void setSecondaryTitle(String secondaryTitle) {
		this.secondaryTitle = secondaryTitle;
	}

	/**
	 * Get the tertiary page title rendered immediately after the value of the 
	 * secondaryTitle property.
	 */
	public String getTertiaryTitle() {
		return tertiaryTitle;
	}

	/**
	 * Set the tertiary page title rendered immediately after the value of the 
	 * secondaryTitle property.
	 */
	public void setTertiaryTitle(String tertiaryTitle) {
		this.tertiaryTitle = tertiaryTitle;
	}

	/**
	 * Get the greeting message displayed beneath the top banner.
	 */
	public String getGreetingMessage() {
		return greetingMessage;
	}

	/**
	 * Set the greeting message displayed beneath the top banner.
	 */
	public void setGreetingMessage(String greetingMessage) {
		this.greetingMessage = greetingMessage;
	}

	/**
	 * Get the warning message displayed beneath the top banner.
	 */
	public String getWarningMessage() {
		return warningMessage;
	}

	/**
	 * Set the warning message displayed beneath the top banner.
	 */
	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}

	/**
	 * Get the boolean value that indicates whether or not the 2nd-level 
	 * of the top navigation menu should be rendered horizontally.
	 */
	public boolean isHorizontalMenuStyle() {
		return horizontalMenuStyle;
	}

	/**
	 * Get the boolean value that indicates whether or not the 2nd-level 
	 * of the top navigation menu should be rendered horizontally.
	 * The default is <code>false</code>.
	 */
	public void setHorizontalMenuStyle(boolean horizontalMenuStyle) {
		this.horizontalMenuStyle = horizontalMenuStyle;
	}

	/**
	 * Get the destination URL for the feedback link in the page footer. 
	 */
	public String getFeedbackUrl() {
		return feedbackUrl;
	}

	/**
	 * Set the destination URL for the feedback link in the page footer. 
	 */
	public void setFeedbackUrl(String feedbackUrl) {
		this.feedbackUrl = feedbackUrl;
	}

	/**
	 * Get the destination URL for the support link in the page footer.
	 */
	public String getSupportUrl() {
		return supportUrl;
	}

	/**
	 * Set the destination URL for the support link in the page footer.
	 */
	public void setSupportUrl(String supportUrl) {
		this.supportUrl = supportUrl;
	}

	/**
	 * Get the name of the window/frame in which the feedback and support 
	 * URLs will be opened.
	 */
	public String getTargetFrame() {
		return targetFrame;
	}

	/**
	 * Set the name of the window/frame in which the feedback and support 
	 * URLs will be opened.
	 */
	public void setTargetFrame(String targetFrame) {
		this.targetFrame = targetFrame;
	}

	/**
	 * Get the boolean value that indicates whether or not the 
	 * "HP Confidential" label is rendered as part of the page footer. 
	 */
	public boolean isConfidentialLabel() {
		return confidentialLabel;
	}

	/**
	 * Set the boolean value that indicates whether or not the 
	 * "HP Confidential" label is rendered as part of the page footer. 
	 * The default is <code>false</code>.
	 */
	public void setConfidentialLabel(boolean confidentialLabel) {
		this.confidentialLabel = confidentialLabel;
	}

	/**
	 * Get the boolean value that indicates whether or not the 
	 * "HP Public" label is rendered as part of the page footer.
	 */
	public boolean isPublicLabel() {
		return publicLabel;
	}

	/**
	 * Get the boolean value that indicates whether or not the 
	 * "HP Public" label is rendered as part of the page footer.
	 * The default is <code>false</code>.
	 */
	public void setPublicLabel(boolean publicLabel) {
		this.publicLabel = publicLabel;
	}

	/**
	 * Get the boolean value that indicates whether or not the page revision
	 * date should be rendered as part of the page footer. 
	 */
	public boolean isRevisionDateDisplayed() {
		return revisionDateDisplayed;
	}

	/**
	 * Set the boolean value that indicates whether or not the page revision
	 * date should be rendered as part of the page footer. 
	 */
	public void setRevisionDateDisplayed(boolean revisionDateDisplayed) {
		this.revisionDateDisplayed = revisionDateDisplayed;
	}

	/**
	 * Get the page version number in the page footer.
	 */
	public String getVersionNumber() {
		return versionNumber;
	}

	/**
	 * Set the page version number in the page footer.
	 */
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * Get the list of breadcrumbs that represents the current page's location
	 * in the site hierarchy, and are displayed immediately beneath the top
	 * navigation menu.
	 */
	public List getBreadcrumbItems() {
		return breadcrumbItems;
	}

	/**
	 * Set the list of breadcrumbs that represents the current page's location
	 * in the site hierarchy, and are displayed immediately beneath the top
	 * navigation menu.
	 */
	public void setBreadcrumbItems(List breadcrumbItems) {
		this.breadcrumbItems = breadcrumbItems;
	}

	/**
	 * Get the <code>&lt;meta name="name" contents="value"&gt;</code>
	 * elements to the <code>&lt;head&gt;</code> element.
	 */
	public Properties getMetaInfos() {
		return metaInfos;
	}

	/**
	 * Set additional <code>&lt;meta name="name" contents="value"&gt;</code>
	 * elements to the <code>&lt;head&gt;</code> element.
	 */
	public void setMetaInfos(Properties metaInfos) {
		this.metaInfos = metaInfos;
	}

	/**
	 * Get the left menu items, a <code>List</code> of {@link MenuItem} objects.
	 */
	public List getLeftMenuItems() {
		return leftMenuItems;
	}

	/**
	 * Set the left menu items, a <code>List</code> of {@link MenuItem} object.
	 */
	public void setLeftMenuItems(List leftMenuItems) {
		this.leftMenuItems = leftMenuItems;
	}

	/**
	 * Get the boolean value that indicates whether or not to generate the 
	 * default breadcrumb items.
	 */
	public boolean isGenerateBreadcrumbs() {
		return generateBreadcrumbs;
	}

	/**
	 * Set the boolean value that indicates whether or not to generate the 
	 * default breadcrumb items.
	 * The default is <code>false</code>.
	 */
	public void setGenerateBreadcrumbs(boolean generateBreadcrumbs) {
		this.generateBreadcrumbs = generateBreadcrumbs;
	}

}
