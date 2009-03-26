/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.portal.tag;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.hp.it.spf.xa.i18n.portal.ClassicLocaleSelectorProvider;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.i18n.portal.tag.LocaleIndicatorBaseTag;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * The tag class for the "classic"-style locale indicator (ie the
 * <code>&lt;spf-i18n-portal:classicLocaleIndicator&gt;</code> tag). This tag
 * just expresses the display name of the current locale. It supports the
 * following optional tag attributes:
 * </p>
 * <ul>
 * <li>
 * <p>
 * The <code>order="<i>spec</i>"</code> attribute lets you optionally
 * specify the the display format for the locale name. Use
 * <code>order="language-country"</code> to display the language name first,
 * followed by country name (if any). Use <code>order="country-language"</code>
 * (the default and the current HPWeb standard) to give the country name
 * primacy. <b>Note:</b> The <code>order</code> attribute is only relevant
 * for a full locale (ie country-specific locale), since generic (ie
 * language-only) locales have only a language name to display.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>displayInLocale="<i>language-tag</i>"</code> attribute lets you
 * optionally specify a locale in which to display the locale name. You pass an
 * <a href="http://www.faqs.org/rfcs/rfc3066.html">RFC 3066</a> language tag
 * for the locale (for example, use <code>displayInLocale="en"</code> to
 * display in English). By default, the display will be in the user's current
 * locale (the HPWeb standard); or, instead of passing a language tag, you can
 * explicitly request that by using <code>displayInLocale="current"</code>.
 * </p>
 * </li>
 * </ul>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ClassicLocaleIndicatorTag extends LocaleIndicatorBaseTag {
	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	private static final LogWrapper LOGGER = new LogWrapper(
			ClassicLocaleIndicatorTag.class);

	/**
	 * Stores the <code>order</code> attribute from the tag.
	 */
	protected String orderValue;

	/**
	 * Stores the integer code for the <code>order</code> attribute from the
	 * tag.
	 */
	protected int order;

	/**
	 * Stores the <code>displayInLocale</code> attribute from the tag.
	 */
	protected String displayInLocaleValue;

	/**
	 * Stores the locale for the <code>displayInLocale</code> attribute from
	 * the tag.
	 */
	protected Locale displayInLocale;

	private static final String CURRENT_LOCALE = "current";
	private static final String COUNTRY_FIRST = "country-language";
	private static final String LANGUAGE_FIRST = "language-country";

	/**
	 * Constructor to initialize tag attributes.
	 * 
	 */
	public ClassicLocaleIndicatorTag() {
		super();
		displayInLocale = null;
		displayInLocaleValue = null;
		order = I18nUtility.LOCALE_BY_COUNTRY;
		orderValue = null;
	}

	/**
	 * Release resources (currently none) after processing this tag.
	 */
	public void release() {
		super.release();
		displayInLocale = null;
		displayInLocaleValue = null;
		order = -1;
		orderValue = null;
	}

	/**
	 * Set the locale in which to display the locale name, from the
	 * <code>displayInLocale</code> attribute in the tag. This converts the
	 * tag attribute value (a string) into the desired locale, where the special
	 * value <code>current</code> denotes the current locale.
	 * 
	 * @param value
	 *            The attribute value.
	 */
	public void setDisplayInLocale(String value) {
		displayInLocaleValue = normalize(value);
		if ((displayInLocaleValue == null)
				|| CURRENT_LOCALE.equalsIgnoreCase(displayInLocaleValue)) {
			// null means current locale
			displayInLocale = null;
		} else {
			displayInLocale = I18nUtility.languageTagToLocale(value);
		}
	}

	/**
	 * Get the value of the <code>displayInLocale</code> attribute.
	 * 
	 * @return String
	 */
	public String getDisplayInLocale() {
		return displayInLocaleValue;
	}

	/**
	 * Set the display-name order for the locale, from the <code>order</code>
	 * attribute in the tag. (Note that generic locales - ie language-only
	 * locales - always display language name only.) The supported tag values
	 * are:
	 * <ul>
	 * <li>"country-language" (the current HPWeb standard and the default) -
	 * display as <code><i>country-language</i></code> (eg,
	 * <code>United States-English</code>).</li>
	 * <li>"language-country" - display as <code><i>language-country</i></code>
	 * (eg, <code>English-United States</code>).</li>
	 * </ul>
	 * 
	 * @param value
	 */
	public void setOrder(String value) {
		orderValue = normalize(value);
		if (LANGUAGE_FIRST.equalsIgnoreCase(orderValue)) {
			order = 0;
		} else {
			order = I18nUtility.LOCALE_BY_COUNTRY;
		}
	}

	/**
	 * Get the value of the <code>order</code> attribute.
	 * 
	 * @return String
	 */
	public String getOrder() {
		return orderValue;
	}

	/**
	 * Get the HTML string for rendering the "classic"-style locale indicator
	 * for the given current locale.
	 * 
	 * @param currentLocale
	 *            The current locale.
	 * @throws JspException
	 * @return The HTML string for "classic"-style locale indicator.
	 */
	protected String getHTML(Locale currentLocale) throws JspException {

		String html = "";
		if (currentLocale == null) {
			String msg = "ClassicLocaleIndicatorTag error: the base tag did not provide a locale.";
			LOGGER.error(msg);
			throw new JspException(msg);
		}
		try {
			// prepare current settings
			int flags = I18nUtility.LOCALE_BY_COUNTRY;
			if (order == 0) {
				flags = 0;
			}
			Locale inLocale = currentLocale;
			if (displayInLocale != null) {
				inLocale = displayInLocale;
			}

			// get display name in same locale (not necessarily current
			// locale) - note that HPWeb standard is to display country
			// first, language second
			String dispName = I18nUtility.getLocaleDisplayName(currentLocale,
					inLocale, flags);
			html += dispName;
		} catch (Exception ex) {
			String errMsg = "ClassicLocaleIndicatorTag error: "
					+ ex.getMessage();
			LOGGER.error(errMsg);
			throw new JspException(errMsg, ex);
		}
		return html;
	}

}
