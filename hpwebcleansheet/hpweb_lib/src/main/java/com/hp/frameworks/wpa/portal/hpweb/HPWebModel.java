package com.hp.frameworks.wpa.portal.hpweb;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * This model object represents all of the configurable properties of a standard
 * HPWeb c-frame for use with the HPWeb VAP Components. Each property exposed 
 * by this model maps to an element of the HPWeb header, 
 * navigation menu or footer that can be customized by the application developer. 
 * This object is not initialized with any default
 * values, it is used to communicate app-specific customizations to whichever 
 * service that will ultimately render the layout. It is the responsibility of the
 * rendering service to, where appropriate, provide sensible defaults for values
 * that are not explicitly set through this model.
 * <br><br>
 * This class subclasses the <code>com.hp.frameworks.wpa.hpweb.HPWebModel</code>
 * class for its configurable properties.
 */
public class HPWebModel extends com.hp.frameworks.wpa.hpweb.HPWebModel {
	
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

	public HPWebModel() {
		super();
		metaInfos = new Properties();
		// Set default
		setCartItemCount(0);
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
