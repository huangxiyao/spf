package com.hp.it.spf.portal.cleansheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;

import com.vignette.portal.website.enduser.PortalContext;


/**
 * This model object represents all of the configurable properties of a standard
 * HP Cleansheet c-frame. Each property exposed by this model maps to an element of the
 * HP Cleansheet header, navigation menu or footer that can be customized by the
 * application developer. This object is not initialized with any default
 * values, it is used solely as a strongly-typed interface to a property map
 * that is used to communicate app-specific customizations to whichever service
 * that will ultimately render the layout. It is the responsibility of the
 * rendering service to, where appropriate, provide sensible defaults for values
 * that are not explicitly set through this model.
 */
public class HPCSModel
{

    // ------------------------------------------------------ Private fields

    private String contactUrl;
    private String contactText;
    private String communitiesUrl;
    private String dataRightsUrl;
    private String feedbackText;
    private String feedbackUrl;
	private String globalHeader;
	private String globalFooter;
    private String imprintUrl;
    private String headerStyle;
	private String helpUrl;
	private String helpText;
    private String metricUrl;
	private String pageTitle;
    private String phoneNumber;
    private String phoneLabel;
    private String privacyUrl;
    private String printableUrl;
    private String printableDisabled;
    private String profileUrl;
    private String registerUrl;
    private String searchAudience;
    private String searchOmnitureTag;
    private String searchReturnUrl;
    private String searchContactUrl;
    private String searchReturnText;
    private String searchQueryPrefix;
    private String searchSectionName;
	private String searchWidget;
    private String searchUrl;
    private String sectionTitle;
    private String signOutUrl;
    private String signInUrl;
    private String siteMapUrl;
    private String tagline;
    private String termsUrl;
    private String themeColor;
    private String username;
	private String windowTitle;
    
	private List<MenuItem> topMenuItems = new ArrayList<MenuItem>();
	private Properties metaInfos = new Properties();  
	private Map<String, String> supportedLocales = new HashMap<String, String>();

	// ------------------------------------------------------ Constructor and public methods
	
	/**
	 * Constructor to instantiate the object
	 */
	public HPCSModel() {
	}

	/**
	 * @return the contactUrl
	 */
	public String getContactUrl() {
		return contactUrl;
	}

	/**
	 * @param contactUrl the contactUrl to set
	 */
	public void setContactUrl(String contactUrl) {
		this.contactUrl = contactUrl;
	}

	/**
	 * @return the contactText
	 */
	public String getContactText() {
		return contactText;
	}

	/**
	 * @param contactText the contactText to set
	 */
	public void setContactText(String contactText) {
		this.contactText = contactText;
	}

	/**
	 * @return the communitiesUrl
	 */
	public String getCommunitiesUrl() {
		return communitiesUrl;
	}

	/**
	 * @param communitiesUrl the communitiesUrl to set
	 */
	public void setCommunitiesUrl(String communitiesUrl) {
		this.communitiesUrl = communitiesUrl;
	}

	/**
	 * @return the dataRightsUrl
	 */
	public String getDataRightsUrl() {
		return dataRightsUrl;
	}

	/**
	 * @param dataRightsUrl the dataRightsUrl to set
	 */
	public void setDataRightsUrl(String dataRightsUrl) {
		this.dataRightsUrl = dataRightsUrl;
	}

	/**
	 * @return the feedbackText
	 */
	public String getFeedbackText() {
		return feedbackText;
	}

	/**
	 * @param feedbackText the feedbackText to set
	 */
	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}

	/**
	 * @return the feedbackUrl
	 */
	public String getFeedbackUrl() {
		return feedbackUrl;
	}

	/**
	 * @param feedbackUrl the feedbackUrl to set
	 */
	public void setFeedbackUrl(String feedbackUrl) {
		this.feedbackUrl = feedbackUrl;
	}

	/**
	 * @return the globalHeader
	 */
	public String getGlobalHeader() {
		return globalHeader;
	}

	/**
	 * @param globalHeader the globalHeader to set
	 */
	public void setGlobalHeader(String globalHeader) {
		this.globalHeader = globalHeader;
	}

	/**
	 * @return the globalFooter
	 */
	public String getGlobalFooter() {
		return globalFooter;
	}

	/**
	 * @param globalFooter the globalFooter to set
	 */
	public void setGlobalFooter(String globalFooter) {
		this.globalFooter = globalFooter;
	}

	/**
	 * @return the imprintUrl
	 */
	public String getImprintUrl() {
		return imprintUrl;
	}

	/**
	 * @param imprintUrl the imprintUrl to set
	 */
	public void setImprintUrl(String imprintUrl) {
		this.imprintUrl = imprintUrl;
	}

	/**
	 * @return the headerStyle
	 */
	public String getHeaderStyle() {
		return headerStyle;
	}

	/**
	 * @param headerStyle the headerStyle to set
	 */
	public void setHeaderStyle(String headerStyle) {
		this.headerStyle = headerStyle;
	}

	/**
	 * @return the helpUrl
	 */
	public String getHelpUrl() {
		return helpUrl;
	}

	/**
	 * @param helpUrl the helpUrl to set
	 */
	public void setHelpUrl(String helpUrl) {
		this.helpUrl = helpUrl;
	}

	/**
	 * @return the helpText
	 */
	public String getHelpText() {
		return helpText;
	}

	/**
	 * @param helpText the helpText to set
	 */
	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	/**
	 * @return the metricUrl
	 */
	public String getMetricUrl() {
		return metricUrl;
	}

	/**
	 * @param metricUrl the metricUrl to set
	 */
	public void setMetricUrl(String metricUrl) {
		this.metricUrl = metricUrl;
	}

	/**
	 * @return the pageTitle
	 */
	public String getPageTitle() {
		return pageTitle;
	}

	/**
	 * @param pageTitle the pageTitle to set
	 */
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the phoneLabel
	 */
	public String getPhoneLabel() {
		return phoneLabel;
	}

	/**
	 * @param phoneLabel the phoneLabel to set
	 */
	public void setPhoneLabel(String phoneLabel) {
		this.phoneLabel = phoneLabel;
	}

	/**
	 * @return the privacyUrl
	 */
	public String getPrivacyUrl() {
		return privacyUrl;
	}

	/**
	 * @param privacyUrl the privacyUrl to set
	 */
	public void setPrivacyUrl(String privacyUrl) {
		this.privacyUrl = privacyUrl;
	}

	/**
	 * @return the printableUrl
	 */
	public String getPrintableUrl() {
		return printableUrl;
	}

	/**
	 * @param printableUrl the printableUrl to set
	 */
	public void setPrintableUrl(String printableUrl) {
		this.printableUrl = printableUrl;
	}

	/**
	 * @return the printableDisabled
	 */
	public String getPrintableDisabled() {
		return printableDisabled;
	}

	/**
	 * @param printableDisabled the printableDisabled to set
	 */
	public void setPrintableDisabled(String printableDisabled) {
		this.printableDisabled = printableDisabled;
	}

	/**
	 * @return the profileUrl
	 */
	public String getProfileUrl() {
		return profileUrl;
	}

	/**
	 * @param profileUrl the profileUrl to set
	 */
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	/**
	 * @return the registerUrl
	 */
	public String getRegisterUrl() {
		return registerUrl;
	}

	/**
	 * @param registerUrl the registerUrl to set
	 */
	public void setRegisterUrl(String registerUrl) {
		this.registerUrl = registerUrl;
	}

	/**
	 * @return the searchAudience
	 */
	public String getSearchAudience() {
		return searchAudience;
	}

	/**
	 * @param searchAudience the searchAudience to set
	 */
	public void setSearchAudience(String searchAudience) {
		this.searchAudience = searchAudience;
	}

	/**
	 * @return the searchOmnitureTag
	 */
	public String getSearchOmnitureTag() {
		return searchOmnitureTag;
	}

	/**
	 * @param searchOmnitureTag the searchOmnitureTag to set
	 */
	public void setSearchOmnitureTag(String searchOmnitureTag) {
		this.searchOmnitureTag = searchOmnitureTag;
	}

	/**
	 * @return the searchReturnUrl
	 */
	public String getSearchReturnUrl() {
		return searchReturnUrl;
	}

	/**
	 * @param searchReturnUrl the searchReturnUrl to set
	 */
	public void setSearchReturnUrl(String searchReturnUrl) {
		this.searchReturnUrl = searchReturnUrl;
	}

	/**
	 * @return the searchContactUrl
	 */
	public String getSearchContactUrl() {
		return searchContactUrl;
	}

	/**
	 * @param searchContactUrl the searchContactUrl to set
	 */
	public void setSearchContactUrl(String searchContactUrl) {
		this.searchContactUrl = searchContactUrl;
	}

	/**
	 * @return the searchReturnText
	 */
	public String getSearchReturnText() {
		return searchReturnText;
	}

	/**
	 * @param searchReturnText the searchReturnText to set
	 */
	public void setSearchReturnText(String searchReturnText) {
		this.searchReturnText = searchReturnText;
	}

	/**
	 * @return the searchQueryPrefix
	 */
	public String getSearchQueryPrefix() {
		return searchQueryPrefix;
	}

	/**
	 * @param searchQueryPrefix the searchQueryPrefix to set
	 */
	public void setSearchQueryPrefix(String searchQueryPrefix) {
		this.searchQueryPrefix = searchQueryPrefix;
	}

	/**
	 * @return the searchSectionName
	 */
	public String getSearchSectionName() {
		return searchSectionName;
	}

	/**
	 * @param searchSectionName the searchSectionName to set
	 */
	public void setSearchSectionName(String searchSectionName) {
		this.searchSectionName = searchSectionName;
	}

	/**
	 * @return the searchWidget
	 */
	public String getSearchWidget() {
		return searchWidget;
	}

	/**
	 * @param searchWidget the searchWidget to set
	 */
	public void setSearchWidget(String searchWidget) {
		this.searchWidget = searchWidget;
	}

	/**
	 * @return the searchUrl
	 */
	public String getSearchUrl() {
		return searchUrl;
	}

	/**
	 * @param searchUrl the searchUrl to set
	 */
	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}

	/**
	 * @return the sectionTitle
	 */
	public String getSectionTitle() {
		return sectionTitle;
	}

	/**
	 * @param sectionTitle the sectionTitle to set
	 */
	public void setSectionTitle(String sectionTitle) {
		this.sectionTitle = sectionTitle;
	}

	/**
	 * @return the signOutUrl
	 */
	public String getSignOutUrl() {
		return signOutUrl;
	}

	/**
	 * @param signOutUrl the signOutUrl to set
	 */
	public void setSignOutUrl(String signOutUrl) {
		this.signOutUrl = signOutUrl;
	}

	/**
	 * @return the signInUrl
	 */
	public String getSignInUrl() {
		return signInUrl;
	}

	/**
	 * @param signInUrl the signInUrl to set
	 */
	public void setSignInUrl(String signInUrl) {
		this.signInUrl = signInUrl;
	}

	/**
	 * @return the siteMapUrl
	 */
	public String getSiteMapUrl() {
		return siteMapUrl;
	}

	/**
	 * @param siteMapUrl the siteMapUrl to set
	 */
	public void setSiteMapUrl(String siteMapUrl) {
		this.siteMapUrl = siteMapUrl;
	}

	/**
	 * @return the tagline
	 */
	public String getTagline() {
		return tagline;
	}

	/**
	 * @param tagline the tagline to set
	 */
	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	/**
	 * @return the termsUrl
	 */
	public String getTermsUrl() {
		return termsUrl;
	}

	/**
	 * @param termsUrl the termsUrl to set
	 */
	public void setTermsUrl(String termsUrl) {
		this.termsUrl = termsUrl;
	}

	/**
	 * @return the themeColor
	 */
	public String getThemeColor() {
		return themeColor;
	}

	/**
	 * @param themeColor the themeColor to set
	 */
	public void setThemeColor(String themeColor) {
		this.themeColor = themeColor;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the windowTitle
	 */
	public String getWindowTitle() {
		return windowTitle;
	}

	/**
	 * @param windowTitle the windowTitle to set
	 */
	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}

	/**
	 * @return the topMenuItems
	 */
	public List<MenuItem> getTopMenuItems() {
		return topMenuItems;
	}

	/**
	 * @param topMenuItems the topMenuItems to set
	 */
	public void setTopMenuItems(List<MenuItem> topMenuItems) {
		this.topMenuItems = topMenuItems;
	}

	/**
	 * @return the metaInfos
	 */
	public Properties getMetaInfos() {
		return metaInfos;
	}

	/**
	 * @param metaInfos the metaInfos to set
	 */
	public void setMetaInfos(Properties metaInfos) {
		this.metaInfos = metaInfos;
	}

	/**
	 * @return the supportedLocales
	 */
	public Map<String, String> getSupportedLocales() {
		return supportedLocales;
	}

	/**
	 * @param supportedLocales the supportedLocales to set
	 */
	public void setSupportedLocales(Map<String, String> supportedLocales) {
		this.supportedLocales = supportedLocales;
	}
	
}
