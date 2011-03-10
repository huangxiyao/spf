package com.hp.it.spf.portal.cleansheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * This model object represents all of the configurable properties of a standard
 * HP Clean sheet c-frame for use with the HP Clean sheet VAP Components. Each property exposed 
 * by this model maps to an element of the HP Clean sheet header, 
 * navigation menu or footer that can be customized by the application developer. 
 * This object is not initialized with any default
 * values, it is used to communicate app-specific customizations to whichever 
 * service that will ultimately render the layout. It is the responsibility of the
 * rendering service to, where appropriate, provide sensible defaults for values
 * that are not explicitly set through this model.
 * <br><br>
 */
public class HPCSModel {

   	private String metricsUrl;
   	private String feedbackText;
   	private String feedbackUrl;
   	private String saleTermsUrl;
   	private String termsUrl;
   	private String imprintUrl;
   	private String privacyUrl;
   	private String printableUrl;
   	private boolean printableDisabled;
   	private String tagline;
   	private String title;
   	private List breadcrumbItems;
   	private String searchAudience;
   	private String searchOmnitureTag;
   	private String searchReturnUrl;
   	private String searchContactUrl;
   	private String searchReturnText;
   	private String searchQueryPrefix;
   	private String searchSectionName;
   	private String searchUrl;
   	private String phoneNumber;
   	private String phoneLabel;
   	private String contactUrl;
   	private String registerUrl;
   	private String profileUrl;
   	private String signOutUrl;
   	private String signInUrl;
   	private String username;
   	private String headerStyle;
   	private String theme;
   	private String pageWidth;
   	private String sectionTitle;
   	private String contactText;
   	private String liveChatText;
   	private String liveChatUrl;
   	private String buyingOptText;
   	private String buyingOptUrl;
   	private String orderStatusText;
   	private String orderStatusUrl;
   	private String custSvcText;
   	private String custSvcUrl;
   	private String myAccountUrl;
   	private String cartText;
   	private String cartUrl;
   	private String cartItemText;
   	private String cartItemCount;
   	private String exploreUrl;
   	private String communitiesUrl;
   	private String siteMapUrl;

	private String windowTitle;
	private String leftPromotion;
	private String topPromotion;
	private String searchWidget;
	private String localeSelector;
	private boolean generateBreadcrumbs = false;
	private Properties metaInfos;
	private String helpUrl;
	private String helpText;
	private List topMenuItems = new ArrayList();
	
	public HPCSModel() {
		super();
		metaInfos = new Properties();
	}

	
	 
	 
	public String getThemeColor()
	{
	  return theme;
	}
 
	public void setThemeColor(String themeColor)
	{
		 this.theme = themeColor;
	}
 
	public String getHeaderStyle()
	{
	  return headerStyle;
	}
 
	public void setHeaderStyle(String headerStyle)
	{
		 this.headerStyle = headerStyle;
	}
 
	public String getPageWidth()
	{
	  return pageWidth;
	}
 
	public void setPageWidth(String pageWidth)
	{
		 this.pageWidth = pageWidth;
	}
 
	public String getUsername()
	{
	  return username;
	}
 
	public void setUsername(String username)
	{
		 this.username = username;
	}
 
	public String getSignInUrl()
	{
	  return signInUrl;
	}
 
	public void setSignInUrl(String signInUrl)
	{
		 this.signInUrl = signInUrl;
	}
 
	public String getSignOutUrl()
	{
	  return signOutUrl;
	}
 
	public void setSignOutUrl(String signOutUrl)
	{
		 this.signOutUrl = signOutUrl;
	}
 
	public String getProfileUrl()
	{
	  return profileUrl;
	}
 
	public void setProfileUrl(String profileUrl)
	{
		 this.profileUrl = profileUrl;
	}
 
	public String getRegisterUrl()
	{
	  return registerUrl;
	}
 
	public void setRegisterUrl(String registerUrl)
	{
		 this.registerUrl = registerUrl;
	}
 
	/** @deprecated */
	public String getReigsterUrl()
	{
	  return registerUrl;
	}
 
	/** @deprecated */
	public void setReigsterUrl(String registerUrl)
	{
		 this.registerUrl = registerUrl;
	}
 
	public String getMyAccountUrl()
	{
	  return myAccountUrl;
	}
 
	public void setMyAccountUrl(String myAccountUrl)
	{
		 this.myAccountUrl = myAccountUrl;
	}
 
	public String getSectionTitle()
	{
	  return sectionTitle;
	}
 
	public void setSectionTitle(String sectionTitle)
	{
		 this.sectionTitle = sectionTitle;
	}
 
	public String getContactText()
	{
	  return contactText;
	}
 
	public void setContactText(String contactText)
	{
		 this.contactText = contactText;
	}
 
	public String getContactUrl()
	{
	  return contactUrl;
	}
 
	public void setContactUrl(String contactUrl)
	{
		 this.contactUrl = contactUrl;
	}
 
	public String getPhoneLabel()
	{
	  return phoneLabel;
	}
 
	public void setPhoneLabel(String phoneLabel)
	{
		 this.phoneLabel = phoneLabel;
	}
 
	public String getPhoneNumber()
	{
	  return phoneNumber;
	}
 
	public void setPhoneNumber(String phoneNumber)
	{
		 this.phoneNumber = phoneNumber;
	}
 
	public String getLiveChatText()
	{
	  return liveChatText;
	}
 
	public void setLiveChatText(String liveChatText)
	{
		 this.liveChatText = liveChatText;
	}
 
	public String getLiveChatUrl()
	{
	  return liveChatUrl;
	}
 
	public void setLiveChatUrl(String liveChatUrl)
	{
		 this.liveChatUrl = liveChatUrl;
	}
 
	public String getBuyingOptText()
	{
	  return buyingOptText;
	}
 
	public void setBuyingOptText(String buyingOptText)
	{
		 this.buyingOptText = buyingOptText;
	}
 
	public String getBuyingOptUrl()
	{
	  return buyingOptUrl;
	}
 
	public void setBuyingOptUrl(String buyingOptUrl)
	{
		 this.buyingOptUrl = buyingOptUrl;
	}
 
	public String getOrderStatusText()
	{
	  return orderStatusText;
	}
 
	public void setOrderStatusText(String orderStatusText)
	{
		 this.orderStatusText = orderStatusText;
	}
 
	public String getOrderStatusUrl()
	{
	  return orderStatusUrl;
	}
 
	public void setOrderStatusUrl(String orderStatusUrl)
	{
		 this.orderStatusUrl = orderStatusUrl;
	}
 
	public String getCustSvcText()
	{
	  return custSvcText;
	}
 
	public void setCustSvcText(String custSvcText)
	{
		 this.custSvcText = custSvcText;
	}
 
	public String getCustSvcUrl()
	{
	  return custSvcUrl;
	}
 
	public void setCustSvcUrl(String custSvcUrl)
	{
		 this.custSvcUrl = custSvcUrl;
	}
 
	public String getCartText()
	{
	  return cartText;
	}
 
	public void setCartText(String cartText)
	{
		 this.cartText = cartText;
	}
 
	public String getCartUrl()
	{
	  return cartUrl;
	}
 
	public void setCartUrl(String cartUrl)
	{
		 this.cartUrl = cartUrl;
	}
 
	public String getCartItemText()
	{
	  return cartItemText;
	}
 
	public void setCartItemText(String cartItemText)
	{
		 this.cartItemText = cartItemText;
	}
 
	public String getExploreUrl()
	{
	  return exploreUrl;
	}
 
	public void setExploreUrl(String exploreUrl)
	{
		 this.exploreUrl = exploreUrl;
	}
 
	public String getCommunitiesUrl()
	{
	  return communitiesUrl;
	}
 
	public void setCommunitiesUrl(String communitiesUrl)
	{
		 this.communitiesUrl = communitiesUrl;
	}
 
	public String getSearchUrl()
	{
	  return searchUrl;
	}
 
	public void setSearchUrl(String searchUrl)
	{
		 this.searchUrl = searchUrl;
	}
 
	public String getSearchSectionName()
	{
	  return searchSectionName;
	}
 
	public void setSearchSectionName(String searchSectionName)
	{
		 this.searchSectionName = searchSectionName;
	}
 
	public String getSearchQueryPrefix()
	{
	  return searchQueryPrefix;
	}
 
	public void setSearchQueryPrefix(String searchQueryPrefix)
	{
		 this.searchQueryPrefix = searchQueryPrefix;
	}
 
	public String getSearchReturnText()
	{
	  return searchReturnText;
	}
 
	public void setSearchReturnText(String searchReturnText)
	{
		 this.searchReturnText = searchReturnText;
	}
 
	public String getSearchContactUrl()
	{
	  return searchContactUrl;
	}
 
	public void setSearchContactUrl(String searchContactUrl)
	{
		 this.searchContactUrl = searchContactUrl;
	}
 
	public String getSearchReturnUrl()
	{
	  return searchReturnUrl;
	}
 
	public void setSearchReturnUrl(String searchReturnUrl)
	{
		 this.searchReturnUrl = searchReturnUrl;
	}
 
	public String getSearchOmnitureTag()
	{
	  return searchOmnitureTag;
	}
 
	public void setSearchOmnitureTag(String searchOmnitureTag)
	{
		 this.searchOmnitureTag = searchOmnitureTag;
	}
 
	public String getSearchAudience()
	{
	  return searchAudience;
	}
 
	public void setSearchAudience(String searchAudience)
	{
		 this.searchAudience = searchAudience;
	}
 
	public List getBreadcrumbItems()
	{
 
	  return breadcrumbItems;
	}
 
	public void setBreadcrumbItems(List breadcrumbs)
	{
		 this.breadcrumbItems = breadcrumbs;
	}
 
	public String getTitle()
	{
	  return title;
	}
 
	public void setTitle(String title)
	{
		 this.title = title;
	}
 
	public String getTagline()
	{
	  return tagline;
	}
 
	public void setTagline(String tagline)
	{
		 this.tagline = tagline;
	}
 
	public Boolean isPrintableDisabled()
	{
	  return printableDisabled;
	}
 
	public void setPrintableDisabled(Boolean printableDisabled)
	{
		 this.printableDisabled = printableDisabled;
	}
 
	public String getPrintableUrl()
	{
	  return printableUrl;
	}
 
	public void setPrintableUrl(String printableUrl)
	{
		 this.printableUrl = printableUrl;
	}
 
	public String getPrivacyUrl()
	{
	  return privacyUrl;
	}
 
	public void setPrivacyUrl(String privacyUrl)
	{
		 this.privacyUrl = privacyUrl;
	}
 
	public String getImprintUrl()
	{
	  return imprintUrl;
	}
 
	public void setImprintUrl(String imprintUrl)
	{
		 this.imprintUrl = imprintUrl;
	}
 
	public String getTermsUrl()
	{
	  return termsUrl;
	}
 
	public void setTermsUrl(String termsUrl)
	{
		 this.termsUrl = termsUrl;
	}
 
	public String getSaleTermsUrl()
	{
	  return saleTermsUrl;
	}
 
	public void setSaleTermsUrl(String saleTermsUrl)
	{
		 this.saleTermsUrl = saleTermsUrl;
	}
 
	public String getFeedbackUrl()
	{
	  return feedbackUrl;
	}
 
	public void setFeedbackUrl(String feedbackUrl)
	{
		 this.feedbackUrl = feedbackUrl;
	}
 
	public String getFeedbackText()
	{
	  return feedbackText;
	}
 
	public void setFeedbackText(String feedbackText)
	{
		 this.feedbackText = feedbackText;
	}
 
	public String getSiteMapUrl()
	{
	  return siteMapUrl;
	}
 
	public void setSiteMapUrl(String siteMapUrl)
	{
		 this.siteMapUrl = siteMapUrl;
	}
 
	public String getMetricsUrl()
	{
	  return metricsUrl;
	}
 
	public void setMetricsUrl(String metricsUrl)
	{
		 this.metricsUrl = metricsUrl;
	}
  	
	/**
	 * Get the top promotion area html string.
	 */
	public String getTopPromotion() {
		return topPromotion;
	}

	/**
	 * Set the html for the top promotion area.
	 */
	public void setTopPromotion(String topPromotion) {
		this.topPromotion = topPromotion;
	}

	/**
	 * Get the left promotion area html string.
	 */
	public String getLeftPromotion() {
		return leftPromotion;
	}

	/**
	 * Set the html for the left promotion area.
	 */
	public void setLeftPromotion(String leftPromotion) {
		this.leftPromotion = leftPromotion;
	}

	/**
	 * Get the search widget html string.
	 */
	public String getSearchWidget() {
		return searchWidget;
	}

	/**
	 * Set the html for the search widget.
	 */
	public void setSearchWidget(String searchWidget) {
		this.searchWidget = searchWidget;
	}

	/**
	 * Get the locale selector html string.
	 */
	public String getLocaleSelector() {
		return localeSelector;
	}

	/**
	 * Set the html for the locale selector.
	 */
	public void setLocaleSelector(String localeSelector) {
		this.localeSelector = localeSelector;
	}

	/**
	 * Get the boolean indicator for generating the breadcrumbs.
	 */
	public boolean isGenerateBreadcrumbs() {
		return generateBreadcrumbs;
	}

	/**
	 * Set the boolean indicator for generating the breadcrumbs.
	 */
	public void setGenerateBreadcrumbs(boolean generateBreadcrumbs) {
		this.generateBreadcrumbs = generateBreadcrumbs;
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
	 * Get the Help link url.
	 */
	public String getHelpUrl() {
		return helpUrl;
	}

	/**
	 * Set the Help link url.
	 */
	public void setHelpUrl(String helpUrl) {
		this.helpUrl = helpUrl;
	}

	/**
	 * Get the Help link text.
	 */
	public String getHelpText() {
		return helpText;
	}

	/**
	 * Set the Help link text.
	 */
	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	/**
	 * Set the top menu item list.
	 */
	public void setTopMenuItems(List topMenuItems) {
		this.topMenuItems = topMenuItems;
	}

	/**
	 * Get the top menu item list.
	 */
	public List getTopMenuItems() {
		return topMenuItems;
	}
}
