/*
 * Project: Shared Portal Framework Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.portal;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.i18n.portal.LocaleSelectorProvider;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;

/**
 * <p>
 * A concrete locale selector provider, which provides the HTML markup for the
 * "classic"-style locale selector. This is the style of locale selector which
 * is rendered by the portal framework's
 * <code>&lt;spf-i18n-portal:classicLocaleSelector&gt;</code> tag. You can also
 * instantiate this class directly.
 * </p>
 * <p>
 * If you are not happy with the "classic" locale selector style, you can
 * implement your own. Just extend the abstract base class,
 * LocaleSelectorProvider, like this one does. You can even implement a tag for
 * it, similar to the above.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ClassicLocaleSelectorProvider extends LocaleSelectorProvider {
    /**
     * serialVersionUID long
     */
    private static final long serialVersionUID = 1L;

    /**
     * The code for sorting and displaying full locales by country first.
     * (Language-only locales, if any, will still be sorted by language and
     * display only language.)
     */
    public static final int ORDER_COUNTRY_FIRST = 1;

    /**
     * The sort order code for sorting full locales by language first.
     */
    public static final int ORDER_LANGUAGE_FIRST = 2;

    /**
     * The name of the submit button image for the classic locale selector form.
     * If this image file exists in the current component, it is used for the
     * &lt;img&gt; tag.
     */
    protected static String SUBMIT_BUTTON_IMG_NAME = "btn_submit.gif";

    /**
     * The HTTP HP.com URL for the HP.com classic submit-button image. If the
     * {@link #SUBMIT_BUTTON_IMG_NAME} image file does not exist in the current
     * component, this URL is used instead (when the current request was HTTP).
     */
    protected static String SUBMIT_BUTTON_IMG_HTTP_URL = "http://welcome.hp-ww.com/img/hpweb_1-2_arrw_sbmt.gif";

    /**
     * The HTTPS HP.com URL for the HP.com classic submit-button image. If the
     * {@link #SUBMIT_BUTTON_IMG_NAME} image file does not exist in the current
     * component, this URL is used instead (when the current request was HTTPS).
     */
    protected static String SUBMIT_BUTTON_IMG_HTTPS_URL = "https://secure.hp-ww.com/img/hpweb_1-2_arrw_sbmt.gif";

    /**
     * The message key for the tooltip string for the HP.com classic
     * submit-button image.
     */
    protected static String SUBMIT_BUTTON_IMG_ALT = "localeSelector.submit.alt";

    /**
     * The key for the locale selector alternative text
     */
    protected static String LABEL_ALT = "localeSelector.label.alt";

    /**
     * The default style to apply to the label. Currently null.
     */
    protected static String DEFAULT_LABEL_STYLE = null;

    /**
     * The default style to apply to the list. Currently null.
     */
    protected static String DEFAULT_LIST_STYLE = null;

    /**
     * The default sort order for the list of locales.
     */
    protected static int DEFAULT_SORT_ORDER = ORDER_COUNTRY_FIRST;

    /**
     * The content of the label to use in this classic locale selector.
     */
    protected String labelContent = "";

    /**
     * This is the style to use for the label in the classic locale selector.
     */
    protected String labelStyle = null;

    /**
     * This is the CSS class to use for the label in the classic locale
     * selector.
     */
    protected String labelClass = null;

    /**
     * This is the style to use for the <code>&lt;SELECT&gt;</code> list in the
     * classic locale selector.
     */
    protected String listStyle = null;

    /**
     * This is the CSS class to use for the <code>&lt;SELECT&gt;</code> list in
     * the classic locale selector.
     */
    protected String listClass = null;

    /**
     * This is the locale in which to render each locale name in the list. If
     * null, then each locale name is rendered in that native locale.
     */
    protected Locale displayLocale = null;

    /**
     * This is the code for the sort order to use for the list.
     */
    protected int order = DEFAULT_SORT_ORDER;

    /**
     * This is the locale in which to sort the locales in the list. If null,
     * then the locales are sorted in the current locale.
     */
    protected Locale sortLocale = null;

    /**
     * <p>
     * Constructor for the "classic"-style locale selector provider for a
     * particular request. If a null PortalContext is provided, the
     * getWidgetHTML method of this class will not work (ie will return null).
     * </p>
     */
    public ClassicLocaleSelectorProvider(PortalContext pContext) {
	super(pContext);
    }

    /**
     * Setter for the locale to use for each locale name, both when sorting the
     * names and displaying them in the selector's option list. If you set null
     * or do not use this setter, then each locale will be used to render its
     * own name.
     * 
     * @param locale
     *            The desired locale in which to display the locale names, or
     *            null for each to display according to its own native locale.
     */
    public void setDisplayLocale(Locale locale) {
	this.displayLocale = locale;
    }

    /**
     * Setter for the locale whose collating sequence should be used when
     * sorting the list of locales. If you set null or do not use this setter,
     * then the current locale for the user is assumed. Note: This only controls
     * the collating sequence used when sorting the locale names. The locale
     * names are still localized according to the display locale (see
     * {@link #setDisplayLocale(Locale)} method).
     * 
     * @param locale
     *            The desired locale in which to sort, or null for the current
     *            locale.
     */
    public void setSortLocale(Locale locale) {
	this.sortLocale = locale;
    }

    /**
     * Setter for the order in which to sort and display the locale names in the
     * selector list. Pass in an integer code indicating the desired order:
     * <ul>
     * <li>{@link #ORDER_COUNTRY_FIRST} (the current HPWeb standard and the
     * default) - sort the list of locales by country first and language second,
     * and display them as <code><i>country-language</i></code> (eg
     * <code>United States-English</code>). Note that language-only locales (ie
     * generic locales), if any are present, will still sort by language and
     * only display the language name.</li>
     * <li>{@link #ORDER_LANGUAGE_FIRST} - sort the list of locales by language
     * first and country second, and display them as
     * <code><i>language-country</i></code> (eg
     * <code>English-United States</code>).</li>
     * </ul>
     * 
     * @param order
     *            One of the above codes.
     */
    public void setOrder(int order) {
	if ((order == ORDER_COUNTRY_FIRST) || (order == ORDER_LANGUAGE_FIRST)) {
	    this.order = order;
	} else {
	    this.order = DEFAULT_SORT_ORDER;
	}
    }

    /**
     * Setter for the label content string: any string of text or HTML which you
     * want to display as the label for the "classic"-style locale selector
     * widget. Depending on how the <code>getWidgetHTML</code> method is
     * subsequently invoked, your label content may or may not later be escaped
     * (ie conversion of HTML special characters like <code>&lt;</code> inside
     * the label content to their corresponding HTML character entities) and/or
     * filtered (ie any <code>&lt;SPAN&gt;</code> tags removed). When using this
     * method, you should pass unescaped and unfiltered content.
     * 
     * @param pLabelContent
     *            The label content.
     */
    public void setLabelContent(String pLabelContent) {
	if (pLabelContent == null) {
	    pLabelContent = "";
	}
	pLabelContent = pLabelContent.trim();
	this.labelContent = pLabelContent;
    }

    /**
     * Setter for the CSS style to use for the label in the "classic"-style
     * locale selector widget. Provide a CSS string of properties as you would
     * for a standard inline HTML <code>style</code> attribute. The properties
     * should be CSS properties like <code>background-color</code> and
     * <code>color</code>, valid for styling the contents of an HTML
     * <code>&lt;TD&gt;</code> tag. There is currently no default.
     * 
     * @param pLabelStyle
     *            The CSS style for the label. For example,
     *            <code>background-color:black;color:white;font-weight:bold</code>
     *            .
     */
    public void setLabelStyle(String pLabelStyle) {
	if (pLabelStyle != null)
	    pLabelStyle = pLabelStyle.trim();
	this.labelStyle = pLabelStyle;
    }

    /**
     * Setter for the CSS class to use for the label in the "classic"-style
     * locale selector widget. Provide the name of a CSS class as you would for
     * a standard HTML <code>class</code> attribute. This should be a class
     * which you are planning to include in the same page as the HTML from this
     * ClassicLocaleSelectorProvider. That class can define any CSS properties
     * valid for the contents of an HTML <code>&lt;TD&gt;</code> tag, like
     * <code>background-color</code>. There is currently no default.
     * 
     * @param pLabelClass
     *            The name of the CSS class for the label.
     */
    public void setLabelClass(String pLabelClass) {
	if (pLabelClass != null)
	    pLabelClass = pLabelClass.trim();
	this.labelClass = pLabelClass;
    }

    /**
     * Setter for the CSS style to use for the list of locale options in the
     * "classic"-style locale selector widget. Provide a CSS string of
     * properties as you would for a standard inline HTML <code>style</code>
     * attribute. The properties should be CSS properties like
     * <code>background-color</code> and <code>color</code>, valid for styling
     * the contents of an HTML <code>&lt;SELECT&gt;</code> tag. There is
     * currently no default.
     * 
     * @param pListStyle
     *            The CSS style for the list. For example,
     *            <code>color:black;font-style:italic</code>.
     */
    public void setListStyle(String pListStyle) {
	if (pListStyle != null)
	    pListStyle = pListStyle.trim();
	this.listStyle = pListStyle;
    }

    /**
     * Setter for the CSS class to use for the list of locale options in the
     * "classic"-style locale selector widget. Provide the name of a CSS class
     * as you would for a standard HTML <code>class</code> attribute. This
     * should be a class which you are planning to include in the same page as
     * the HTML from this ClassicLocaleSelectorProvider. That class can define
     * any CSS properties valid for the contents of an HTML
     * <code>&lt;SELECT&gt;</code> tag, like <code>background-color</code>.
     * There is currently no default.
     * 
     * @param pListClass
     *            The name of the CSS class for the list.
     */
    public void setListClass(String pListClass) {
	if (pListClass != null)
	    pListClass = pListClass.trim();
	this.listClass = pListClass;
    }

    /**
     * <p>
     * Get the HTML for the "classic"-style locale selector widget (the part of
     * the form which is visible to the user), using the given parameters and
     * the previously-set class attributes.
     * </p>
     * <p>
     * This method is equivalent to calling the companion method, passing
     * <code>false</code> for the final boolean parameter.
     * </p>
     * 
     * @param widgetName
     *            The name to use for the <code>&lt;SELECT&gt;</code> element.
     * @param availableLocales
     *            An unsorted collection of all of the available locales (one or
     *            more) from which the user may select.
     * @param currentLocale
     *            The user's current locale.
     * @param escape
     *            Whether or not to escape HTML in the link content.
     * @return The HTML string for the locale selector visible elements.
     */
    protected String getWidgetHTML(String widgetName,
	    Collection availableLocales, Locale currentLocale, boolean escape) {
	return getWidgetHTML(widgetName, availableLocales, currentLocale,
		escape, false);
    }

    /**
     * <p>
     * Get the HTML for the "classic"-style locale selector widget (the part of
     * the form which is visible to the user), using the given parameters and
     * the previously-set class attributes.
     * </p>
     * <p>
     * The classic locale selector consists of a label and and an HTML
     * <code>&lt;SELECT&gt;</code> element listing all the available locales for
     * the current portal site. The current effective locale is the default. The
     * available locales from which to choose and the current locale are passed
     * in by the superclass.
     * </p>
     * <p>
     * <b>Note:</b> The contract here is that the returned HTML must only
     * include the selectable list widget itself (plus its submit button). The
     * surrounding HTML <code>&lt;FORM&gt;</code> tag and hidden form parameters
     * are expected to be provided by the parent class since all locale
     * selectors have that in common. Furthermore, the returned HTML here must
     * name the <code>&lt;SELECT&gt;</code> element with the given widget name
     * and available locales, and the selectable values must be RFC 3066
     * language tags (eg <code>zh-CN</code> for Simplified Chinese). That is
     * what the locale selector secondary page type is expecting.
     * </p>
     * <p>
     * The first boolean parameter controls whether or not to escape any HTML
     * special characters like <code>&lt;</code> (ie convert them to
     * corresponding HTML character entities so that they display literally)
     * found in the label content. Note that the locale options themselves are
     * escaped automatically (they generally will contain none anyway).
     * </p>
     * <p>
     * The second boolean parameter controls whether or not to remove any
     * <code>&lt;SPAN&gt;</code> tags found in the label content. In some
     * situations Vignette may have automatically inserted those (eg when the
     * label content provided to the setter was itself retrieved from a Vignette
     * resource bundle), so this allows you to remove them.
     * </p>
     * <p>
     * Other aspects of the returned HTML are determined by the setters for this
     * class. Please see each of the setter methods for more information.
     * </p>
     * 
     * @param widgetName
     *            The name to use for the <code>&lt;SELECT&gt;</code> element.
     * @param availableLocales
     *            An unsorted collection of all of the available locales (one or
     *            more) from which the user may select.
     * @param currentLocale
     *            The user's current locale.
     * @param escape
     *            Whether or not to escape HTML in the link content.
     * @param filterSpan
     *            Whether or not to strip <code>&lt;SPAN&gt;</code> tags from
     *            the link content.
     * @return The HTML string for the locale selector list and submit button.
     */
    protected String getWidgetHTML(String widgetName,
	    Collection availableLocales, Locale currentLocale, boolean escape,
	    boolean filterSpan) {

	StringBuffer html = new StringBuffer();
	if (widgetName != null) {
	    widgetName = Utils.escapeXml(widgetName.trim());
	}
	if ((widgetName == null) || (widgetName.length() == 0)) {
	    widgetName = Consts.PARAM_SELECT_LOCALE;
	}

	// Get the label to use.
	String label = this.labelContent;
	// Escape XML meta-characters if needed.
	if (escape) {
	    label = Utils.escapeXml(label);
	}
	if (filterSpan) {
	    label = I18nUtility.filterSpan(label);
	}
	label = I18nUtility.filterNoLocalizationTokens(label);

	// Get the label style to use.
	String labelStyleAttr = "";
	if (this.labelStyle != null)
	    labelStyleAttr += "style=\"" + Utils.escapeXml(this.labelStyle)
		    + "\" ";
	if (this.labelClass != null)
	    labelStyleAttr += "class=\"" + Utils.escapeXml(this.labelClass)
		    + "\" ";
	if ("".equals(labelStyleAttr) && (DEFAULT_LABEL_STYLE != null))
	    labelStyleAttr += "style=\"" + Utils.escapeXml(DEFAULT_LABEL_STYLE)
		    + "\" ";

	// Get the list style to use.
	String listStyleAttr = "";
	if (this.listStyle != null)
	    listStyleAttr += "style=\"" + Utils.escapeXml(this.listStyle)
		    + "\" ";
	if (this.listClass != null)
	    listStyleAttr += "class=\"" + Utils.escapeXml(this.listClass)
		    + "\" ";
	if ("".equals(listStyleAttr) && (DEFAULT_LIST_STYLE != null))
	    listStyleAttr += "style=\"" + Utils.escapeXml(DEFAULT_LIST_STYLE)
		    + "\" ";

	// begin selector table layout
	html
		.append("<table id=\"spfClassicLocaleSelector\" cellpadding=\"2\" cellspacing=\"0\">\n");
	html.append("<tr>\n");

	// label column
	// add the label tag to follow the accessibility requirements.
	String labelAlt = getLabelAlt();
	if ((label != null) && !("".equals(label))) {
		//If the label is non-blank, 
		//wrap '<label for="' + widgetName + '">' ... '</label>' 
		//around it and insert that label into the <td>
		html.append("<td ").append(labelStyleAttr)
				.append("valign=\"middle\">").append("<label for=\"")
				.append(widgetName).append("\">").append(label)
				.append("</label>").append("</td>\n");
	} else if ((labelAlt != null) && !("".equals(labelAlt))) {
		//If the label alt is non-blank, 
		//wrap '<label class="screenReading" for="' + widgetName + '">' ... '</label>' 
		//around the alt label string, and insert that into the <td>
		html.append("<td ").append(labelStyleAttr)
				.append("valign=\"middle\">")
				.append("<label class=\"screenReading\" for=\"")
				.append(widgetName).append("\">").append(labelAlt)
				.append("</label>").append("</td>\n");
	} else {
		//Else, just add a blank column
		html.append("<td ").append(labelStyleAttr).append("valign=\"middle\">").append("</td>\n");
	}

	// drop down list column
	// refactor inconsistent use of StringBuffer.append according to SPF_Enhancements_for_DP 3.2_3.3 CR216
	html.append("<td ").append(labelStyleAttr).append(">\n");
	html.append("<select ").append(listStyleAttr).append("id=\"").append(widgetName)
		.append("\" name=\"").append(widgetName).append("\">\n");
	if (availableLocales != null) {

	    // prepare current settings
	    int flags = I18nUtility.LOCALE_BY_COUNTRY;
	    if (order == ORDER_LANGUAGE_FIRST) {
		flags = 0;
	    }
	    Locale sortInLocale = currentLocale;
	    if (sortLocale != null) {
		sortInLocale = sortLocale;
	    }

	    // sort available locales according to the settings
	    availableLocales = I18nUtility.sortLocales(availableLocales, 
    		sortInLocale, displayLocale, flags);

	    // iterate over the sorted locales and display the select option for
	    // each one
	    Iterator atts = availableLocales.iterator();
	    int i = 1;
	    while (atts.hasNext()) {
		Locale locale = (Locale) atts.next();
		if (locale == null)
		    continue;
		
		Locale displayInLocale = displayLocale;
		// get display name according to the settings
		if (displayInLocale == null) {
		    displayInLocale = locale;
		}
		String dispName = I18nUtility.getLocaleDisplayName(locale,
			displayInLocale, flags);
		if ((dispName == null) || (dispName.trim().length() == 0))
		    continue;
		dispName = dispName.trim();
		
		String value = I18nUtility.localeToLanguageTag(locale);
		if ((value == null) || (value.trim().length() == 0))
		    continue;
		value = value.trim();
		
		// both the display name and value need to be HTML-escaped
		// just in case
		html.append("<option value=\"").append(Utils.escapeXml(value)).append("\"");
		// make the current locale selected if it is not empty
		if ((currentLocale != null) && (locale.equals(currentLocale))) {
		    html.append(" selected");
		}
		html.append(">").append(Utils.escapeXml(dispName)).append("</option>\n");
		i++;
	    }
	}
	html.append("</select>\n");
	html.append("</td>\n");

	// button column
	String imgLink = getSubmitImageURL();
	String imgAlt = getSubmitImageAlt();
	html.append("<td ").append(labelStyleAttr).append("valign=\"middle\">\n");
	html.append("<input type=\"image\" name=\"btn_").append(widgetName).append("\" src=\"").append(imgLink).append("\" alt=\"").append(imgAlt).append("\" title=\"").append(imgAlt).append("\">\n");
	html.append("</td>\n");

	// end selector table layout
	html.append("</tr>\n");
	html.append("</table>\n");
	return html.toString();
    }

    /**
     * A method to generate the image URL for the classic locale selector's
     * submit button. This button is presumed to be named
     * <code>btn_submit.gif</code> and located in the current portal component.
     * This image may be localized if desired; this method looks for the
     * particular localized image (loaded in the current portal component's
     * secondary support files) which is the best-candidate given the current
     * locale. If the image is not found there, then an appropriate HP.com image
     * URL is returned: the value of either {@link #SUBMIT_BUTTON_IMG_HTTP_URL}
     * or {@link #SUBMIT_BUTTON_IMG_HTTPS_URL}, depending on the scheme used in
     * the current request.
     */
    protected String getSubmitImageURL() {
	String url = SUBMIT_BUTTON_IMG_HTTP_URL;
	if (portalContext != null) {
	    if (I18nUtility.getLocalizedFileName(portalContext,
		    SUBMIT_BUTTON_IMG_NAME) != null) {
		url = I18nUtility.getLocalizedFileURL(portalContext,
			SUBMIT_BUTTON_IMG_NAME);
	    } else {
		HttpServletRequest request = portalContext
			.getHttpServletRequest();
		if (request != null) {
		    if ("https".equalsIgnoreCase(request.getScheme())) {
			url = SUBMIT_BUTTON_IMG_HTTPS_URL;
		    }
		}
		// Now return HP.com image URL by default.
		// url = portalContext.getPortalHttpRoot() + "/" + url;
		// String contextPath = portalContext.getPortalRequest()
		// .getContextPath();
		// if (!url.startsWith(contextPath)) {
		// return Utils.slashify(contextPath + "/" + url);
		// }
	    }
	}
	return Utils.slashify(url);
    }

    /**
     * A method to generate the tooltip string for the classic locale selector's
     * submit button. This string is presumed to exist in the message catalog
     * for the current portal component, under the
     * <code>localeSelector.submit.alt</code> message key. If the message does
     * not exist, then an empty string is returned (ie there is no default
     * message).
     */
    protected String getSubmitImageAlt() {
	String alt = "";
	if (portalContext != null) {
	    alt = I18nUtility
		    .getValue(SUBMIT_BUTTON_IMG_ALT, "", portalContext);
	}
	return alt;
    }

    /**
     * A method to generate the tooltip string for the classic locale selector's
     * label. This string is presumed to exist in the message catalog
     * for the current portal component, under the
     * <code>localeSelector.submit.alt</code> message key. If the message does
     * not exist, then an empty string is returned (ie there is no default
     * message).
     */
    protected String getLabelAlt() {
	String alt = "";
	if (portalContext != null) {
	    alt = I18nUtility
		    .getValue(LABEL_ALT, "", portalContext);
	}
	return alt;
    }
}