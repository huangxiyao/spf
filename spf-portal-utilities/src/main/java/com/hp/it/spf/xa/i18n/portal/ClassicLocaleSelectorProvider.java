/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
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
 * <code>&lt;spf-i18n-portal:classicLocaleSelector&gt;</code> tag. You can
 * also instantiate this class directly.
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
	 */
	protected static String SUBMIT_BUTTON_IMG_NAME = "btn_submit.gif";

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
	 * This is the style to use for the <code>&lt;SELECT&gt;</code> list in
	 * the classic locale selector.
	 */
	protected String listStyle = null;

	/**
	 * This is the CSS class to use for the <code>&lt;SELECT&gt;</code> list
	 * in the classic locale selector.
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
	 * Setter for the locale in which to display each locale name in the
	 * selector. If you set null, then each locale will be used to render its
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
	 * Setter for the locale in which to sort the list of locales in the
	 * selector. If you set null then the current locale for the user is
	 * assumed.
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
	 * <code>United States-English</code>). Note that language-only locales
	 * (ie generic locales), if any are present, will still sort by language and
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
	 * filtered (ie any <code>&lt;SPAN&gt;</code> tags removed). When using
	 * this method, you should pass unescaped and unfiltered content.
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
	 *            <code>background-color:black;color:white;font-weight:bold</code>.
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
	 * <code>background-color</code> and <code>color</code>, valid for
	 * styling the contents of an HTML <code>&lt;SELECT&gt;</code> tag. There
	 * is currently no default.
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
	 * <code>&lt;SELECT&gt;</code> element listing all the available locales
	 * for the current portal site. The current effective locale is the default.
	 * The available locales from which to choose and the current locale are
	 * passed in by the superclass.
	 * </p>
	 * <p>
	 * <b>Note:</b> The contract here is that the returned HTML must only
	 * include the selectable list widget itself (plus its submit button). The
	 * surrounding HTML <code>&lt;FORM&gt;</code> tag and hidden form
	 * parameters are expected to be provided by the parent class since all
	 * locale selectors have that in common. Furthermore, the returned HTML here
	 * must name the <code>&lt;SELECT&gt;</code> element with the given widget
	 * name and available locales, and the selectable values must be RFC 3066
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
		html.append("<table cellpadding=\"2\" cellspacing=\"0\">\n");
		html.append("<tr>\n");

		// label column
		html.append("<td " + labelStyleAttr + "valign=\"middle\">" + label
				+ "</td>\n");

		// drop down list column
		html.append("<td " + labelStyleAttr + ">\n");
		html.append("<select " + listStyleAttr + "id=\"" + widgetName
				+ "\" name=\"" + widgetName + "\">\n");
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
			Locale displayInLocale = displayLocale;

			// sort available locales according to the settings
			availableLocales = I18nUtility.sortLocales(availableLocales,
					sortInLocale, flags);

			// iterate over the sorted locales and display the select option for
			// each one
			Iterator atts = availableLocales.iterator();
			int i = 1;
			while (atts.hasNext()) {
				Locale locale = (Locale) atts.next();

				// get display name according to the settings
				if (displayInLocale == null) {
					displayInLocale = locale;
				}
				String dispName = I18nUtility.getLocaleDisplayName(locale,
						displayInLocale, flags);
				String value = I18nUtility.localeToLanguageTag(locale);
				// both the display name and value need to be HTML-escaped
				// just in case
				html.append("<option value=\"" + Utils.escapeXml(value) + "\"");
				// make the current locale selected if it is not empty
				if ((currentLocale != null) && (locale.equals(currentLocale))) {
					html.append(" selected");
				}
				html.append(">" + Utils.escapeXml(dispName) + "</option>\n");
				i++;
			}
		}
		html.append("</select>\n");
		html.append("</td>\n");

		// button column
		String imgLink = getSubmitImageURL();
		html.append("<td " + labelStyleAttr + "valign=\"middle\">\n");
		html.append("<input type=\"image\" name=\"btn_" + widgetName
				+ "\" src=\"" + imgLink + "\">\n");
		html.append("</td>\n");

		// end selector table layout
		html.append("</tr>\n");
		html.append("</table>\n");
		return html.toString();
	}

	/**
	 * A method to generate the image URL for the classic locale selector's
	 * submit button. This button is presumed to be named
	 * <code>btn_submit.gif</code> and located in the current portal
	 * component. This image may be localized if desired; this method looks for
	 * the particular localized image (loaded in the current portal component's
	 * secondary support files) which is the best-candidate given the current
	 * locale. If the image is not found there, then a URL pointing to
	 * <code>/images/btn_submit.gif</code> under the portal root path is
	 * assumed and returned.
	 */
	private String getSubmitImageURL() {
		String url = "/images/" + SUBMIT_BUTTON_IMG_NAME;
		if (portalContext != null) {
			if (I18nUtility.getLocalizedFileName(portalContext,
					SUBMIT_BUTTON_IMG_NAME) != null) {
				url = I18nUtility.getLocalizedFileURL(portalContext,
						SUBMIT_BUTTON_IMG_NAME);
			} else {
				url = portalContext.getPortalHttpRoot() + "/" + url;
				// make sure the path includes the portal application context
				// root
				String contextPath = portalContext.getPortalRequest()
						.getContextPath();
				if (!url.startsWith(contextPath)) {
					return Utils.slashify(contextPath + "/" + url);
				}
			}
		}
		return Utils.slashify(url);
	}

}