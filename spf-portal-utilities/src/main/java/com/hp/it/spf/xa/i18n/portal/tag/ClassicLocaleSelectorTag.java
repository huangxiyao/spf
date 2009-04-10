/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.portal.tag;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.i18n.portal.LocaleSelectorProvider;
import com.hp.it.spf.xa.i18n.portal.ClassicLocaleSelectorProvider;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;
import com.hp.it.spf.xa.i18n.portal.tag.LocaleSelectorBaseTag;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;

/**
 * <p>
 * The tag class for the "classic"-style locale selector (ie the
 * <code>&lt;spf-i18n-portal:classicLocaleSelector&gt;</code> tag). The
 * locales expressed are the ones currently available for the portal site,
 * sorted by locale. The locale currently resolved for the user is marked as the
 * pre-selected default.
 * </p>
 * <p>
 * This tag takes the following tag attributes:
 * </p>
 * <ul>
 * <li>
 * <p>
 * This tag requires a label string to insert next to the selector widget
 * itself, which can be passed via either the <code>label="<i>string</i>"</code>
 * attribute or the <code>labelKey="<i>message-key</i>"</code> attribute. If
 * the former, the given <i>string</i> is used as the label text directly. If
 * the latter, the given <i>message-key</i> is used to lookup an appropriate
 * localized message for the user's current locale in the current portal
 * component's resource bundle (if not found, then the <i>message-key</i>
 * itself is used for this purpose). If both <code>label</code> and
 * <code>labelKey</code> are provided, the <code>label</code> attribute
 * takes precedence. It is an error if neither <code>label</code> nor
 * <code>labelKey</code> are provided.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>order="<i>spec</i>"</code> attribute lets you optionally
 * specify the sort order used in the list in the selector widget, as well as
 * the display format for each locale name in that list. Use
 * <code>order="language-country"</code> to give the language name priority in
 * the sort, as well as display it first. Use
 * <code>order="country-language"</code> (the default and the current HPWeb
 * standard) to give the country name primacy. Regardless of this setting,
 * locales in the list are always sorted in ascending order according to their
 * names as displayed in the list. <b>Note:</b> The <code>order</code>
 * attribute is only relevant for lists that may contain full (ie
 * country-specific locales), since generic (ie language-only) locales have only
 * a language name with which to sort and display.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>sortInLocale="<i>language-tag</i>"</code> attribute lets you
 * optionally specify a locale in which to sort the list. Locales are always
 * sorted in ascending order according to their localized locale names (see
 * above); this attribute lets you set the locale to use for that. You pass an
 * <a href="http://www.faqs.org/rfcs/rfc3066.html">RFC 3066</a> language tag
 * for the locale in which you want to perform the sort (for example, use
 * <code>sortInLocale="en"</code> to sort using the English locale names). By
 * default, the sort will be done in the user's current locale (the HPWeb
 * standard); or, instead of passing a language tag, you can explicitly request
 * that by using <code>sortInLocale="current"</code>.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>displayInLocale="<i>language-tag</i>"</code> attribute lets you
 * optionally specify a locale in which to display each locale name in the list.
 * You pass an <a href="http://www.faqs.org/rfcs/rfc3066.html">RFC 3066</a>
 * language tag for the locale (for example, use
 * <code>displayInLocale="en"</code> to display in English). By default, the
 * display will be in the user's current locale (the HPWeb standard); or,
 * instead of passing a language tag, you can explicitly request that by using
 * <code>displayInLocale="current"</code>. Finally, you can use
 * <code>displayInLocale="various"</code> to display the locale name in its
 * native rendering.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>labelStyle="<i>title-style</i>"</code> and
 * <code>labelClass="<i>title-class</i>"</code> attributes are optional
 * alternative ways of specifying a CSS style for the locale selector's label.
 * With the <code>labelStyle</code> attribute, you pass an inline CSS style.
 * This should be a string of any CSS properties which are valid properties for
 * your label and/or the HTML <code>&lt;TD&gt;</code> tag. For example,
 * <code>labelStyle="background-color:black;color:white;font-weight:bold"</code>.
 * With the <code>labelClass</code> attribute, you pass the name of a CSS
 * class you have already included in your JSP. That class should specify the
 * same kind of properties as you would in <code>labelStyle</code> - for
 * example, <code>labelClass="my-label-style"</code> where you have elsewhere
 * defined the following CSS class:
 * <code>.my-label-style { background-color:black; ... }</code>. There is no
 * default applied.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>listStyle="<i>list-style</i>"</code> and
 * <code>listClass="<i>list-class</i>"</code> attributes are optional
 * alternative ways of specifying a CSS style for the locale selector's label.
 * With the <code>listStyle</code> attribute, you pass an inline CSS style.
 * This should be a string of any CSS properties which are valid properties for
 * locale list (ie, the HTML <code>&lt;SELECT&gt;</code> tag. For example,
 * <code>labelStyle="background-color:yellow;color:black;font-style:italic"</code>.
 * With the <code>listClass</code> attribute, you pass the name of a CSS class
 * you have already included in your JSP. That class should specify the same
 * kind of properties as you would in <code>listStyle</code> - for example,
 * <code>listClass="my-list-style"</code> where you have elsewhere defined the
 * following CSS class:
 * <code>.my-list-style { background-color:yellow; ... }</code>. There is no
 * default applied.
 * </p>
 * </li>
 * </ul>
 * <p>
 * Finally, from the base class, the following optional tags are also supported:
 * </p>
 * <ul>
 * <li>
 * <p>
 * <code>escape="<i>true-or-false</i>"</code> is an optional switch
 * (default: <code>"false"</code>) which if set to <code>"true"</code> will
 * convert any HTML special characters found in the label into their equivalent
 * HTML character entities. <b>Note:</b> Regardless of the value of this
 * switch, the locale options in the list are escaped (though generally they
 * should contain no HTML markup anyway).
 * </p>
 * </li>
 * <li>
 * <p>
 * <code>filterSpan="<i>true-or-false</i>"</code> is another optional switch
 * (default: <code>"false"</code>) which, when set to <code>true</code>,
 * removes any <code>&lt;SPAN&gt;</code> which Vignette may have injected into
 * the label, if any.
 * </p>
 * </li>
 * </ul>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ClassicLocaleSelectorTag extends LocaleSelectorBaseTag {
	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The <code>label</code> attribute from the tag.
	 */
	protected String label;

	/**
	 * The <code>labelKey</code> attribute from the tag.
	 */
	protected String labelKey;

	/**
	 * Stores the <code>sortInLocale</code> attribute from the tag.
	 */
	protected String sortInLocaleValue;

	/**
	 * Stores the locale for the <code>sortInLocale</code> attribute from the
	 * tag.
	 */
	protected Locale sortInLocale;

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

	/**
	 * Stores the value of the <code>labelStyle</code> attribute.
	 */
	protected String labelStyle;

	/**
	 * Stores the value of the <code>labelClass</code> attribute.
	 */
	protected String labelClass;

	/**
	 * Stores the value of the <code>listStyle</code> attribute.
	 */
	protected String listStyle;

	/**
	 * Stores the value of the <code>listClass</code> attribute.
	 */
	protected String listClass;

	protected static final LogWrapper LOGGER = new LogWrapper(
			ClassicLocaleSelectorTag.class);

	private static final String CURRENT_LOCALE = "current";
	private static final String VARIOUS_LOCALE = "various";
	private static final String COUNTRY_FIRST = "country-language";
	private static final String LANGUAGE_FIRST = "language-country";

	/**
	 * Constructor to initialize tag attributes.
	 * 
	 */
	public ClassicLocaleSelectorTag() {
		super();
		label = null;
		labelKey = null;
		labelClass = null;
		labelStyle = null;
		listClass = null;
		listStyle = null;
		sortInLocale = null;
		sortInLocaleValue = null;
		displayInLocale = null;
		displayInLocaleValue = null;
		order = ClassicLocaleSelectorProvider.ORDER_COUNTRY_FIRST;
		orderValue = null;
	}

	/**
	 * Release resources after processing this tag.
	 */
	public void release() {
		super.release();
		label = null;
		labelKey = null;
		labelClass = null;
		labelStyle = null;
		listClass = null;
		listStyle = null;
		sortInLocale = null;
		sortInLocaleValue = null;
		displayInLocale = null;
		displayInLocaleValue = null;
		order = -1;
		orderValue = null;
	}

	/**
	 * Return an instance of the "classic"-style locale selector provider,
	 * populated with the parameters from the current tag. This throws a
	 * JspException if the required parameters for that provider (ie the label
	 * content) were not specified in the tag.
	 * 
	 * @param currentLocale The current locale to assume.
	 * @return LocaleSelectorProvider
	 * @throws JspException
	 */
	protected LocaleSelectorProvider getLocaleSelectorProvider(Locale currentLocale)
			throws JspException {

		String labelContent = getLabelContent();
		if (labelContent == null) {
			String msg = "ClassicLocaleSelectorTag error: either the label or labelKey are required attributes.";
			LOGGER.error(msg);
			throw new JspException(msg);
		}

		// make the provider (and finalize the display locale which may have not
		// been set - we cannot leave it set to null because the provider
		// interprets that to mean "various" but we need an unset display locale
		// to mean "current" unless the user really did set it to "various")
		if ((displayInLocale == null)
				&& !VARIOUS_LOCALE.equalsIgnoreCase(displayInLocaleValue)) {
			displayInLocale = currentLocale;
		}
		PortalContext portalContext = (PortalContext) pageContext.getRequest()
		.getAttribute("portalContext");
		ClassicLocaleSelectorProvider c = new ClassicLocaleSelectorProvider(
				portalContext);

		// set its configuration
		c.setLabelContent(labelContent);
		c.setLabelStyle(labelStyle);
		c.setLabelClass(labelClass);
		c.setListStyle(listStyle);
		c.setListClass(listClass);
		c.setOrder(order);
		c.setSortLocale(sortInLocale);
		c.setDisplayLocale(displayInLocale);

		return c;
	}

	/**
	 * Set the locale in which to sort, from the <code>sortInLocale</code>
	 * attribute in the tag. This converts the tag attribute value (a string)
	 * into the desired locale, where the special value <code>current</code>
	 * denotes the current locale.
	 * 
	 * @param value
	 *            The attribute value.
	 */
	public void setSortInLocale(String value) {
		sortInLocaleValue = normalize(value);
		if ((sortInLocaleValue == null)
				|| CURRENT_LOCALE.equalsIgnoreCase(sortInLocaleValue)) {
			// null means current locale
			sortInLocale = null;
		} else {
			sortInLocale = I18nUtility.languageTagToLocale(value);
		}
	}

	/**
	 * Get the value of the <code>sortInLocale</code> attribute.
	 * 
	 * @return String
	 */
	public String getSortInLocale() {
		return sortInLocaleValue;
	}

	/**
	 * Set the locale in which to display locale names, from the
	 * <code>displayInLocale</code> attribute in the tag. This converts the
	 * tag attribute value (a string) into the desired locale, where the special
	 * value <code>current</code> denotes the current locale, and the special
	 * value <code>various</code> means to localize natively for each locale.
	 * 
	 * @param value
	 *            The attribute value.
	 */
	public void setDisplayInLocale(String value) {
		displayInLocaleValue = normalize(value);
		if ((displayInLocaleValue == null)
				|| CURRENT_LOCALE.equalsIgnoreCase(displayInLocaleValue)) {
			// null means current locale or various locale; we will later use
			// displayInLocaleValue to distinguish (see
			// getLocaleSelectorProvider method)
			displayInLocale = null;
		} else if (VARIOUS_LOCALE.equalsIgnoreCase(displayInLocaleValue)) {
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
	 * Set the sort and display-name order for the locale list, from the
	 * <code>order</code> attribute in the tag. (Note that generic locales -
	 * ie language-only locales - are always sorted by language only, and always
	 * display language name only.) The supported tag values are:
	 * <ul>
	 * <li>"country-language" (the current HPWeb standard and the default) -
	 * sort full locale by country first and language second, and display as
	 * <code><i>country-language</i></code> (eg,
	 * <code>United States-English</code>).</li>
	 * <li>"language-country" - sort full locale by language first and country
	 * second, and display as <code><i>language-country</i></code> (eg,
	 * <code>English-United States</code>).</li>
	 * </ul>
	 * 
	 * @param value
	 */
	public void setOrder(String value) {
		orderValue = normalize(value);
		if (LANGUAGE_FIRST.equalsIgnoreCase(orderValue)) {
			order = ClassicLocaleSelectorProvider.ORDER_LANGUAGE_FIRST;
		} else {
			order = ClassicLocaleSelectorProvider.ORDER_COUNTRY_FIRST;
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
	 * Set the label from the <code>label</code> attribute in the tag. This
	 * sets a normalized value (ie trimmed and blank converted to null).
	 * 
	 * @param label
	 *            The label text.
	 */
	public void setLabel(String label) {
		this.label = normalize(label);
	}

	/**
	 * Get the value of the <code>label</code> attribute.
	 * 
	 * @return String
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set the label key from the <code>labelKey</code> attribute in the tag.
	 * This sets a normalized value (ie trimmed and blank converted to null).
	 * 
	 * @param labelKey
	 *            The label key.
	 */
	public void setLabelKey(String labelKey) {
		this.labelKey = normalize(labelKey);
	}

	/**
	 * Get the value of the <code>labelKey</code> attribute.
	 * 
	 * @return String
	 */
	public String getLabelKey() {
		return label;
	}

	/**
	 * Get the value of the <code>labelStyle</code> attribute.
	 * 
	 * @return The <code>labelStyle</code> attribute.
	 */
	public String getLabelStyle() {
		return labelStyle;
	}

	/**
	 * Set the value from the <code>labelStyle</code> attribute. Blank values
	 * are not normalized to null.
	 * 
	 * @param value
	 *            The <code>labelStyle</code> attribute.
	 */
	public void setLabelStyle(String value) {
		this.labelStyle = value;
	}

	/**
	 * Get the value of the <code>labelClass</code> attribute.
	 * 
	 * @return The <code>labelClass</code> attribute.
	 */
	public String getLabelClass() {
		return labelClass;
	}

	/**
	 * Set the value from the <code>labelClass</code> attribute. Blank values
	 * are not normalized to null.
	 * 
	 * @param value
	 *            The <code>labelClass</code> attribute.
	 */
	public void setLabelClass(String value) {
		this.labelClass = value;
	}

	/**
	 * Get the value of the <code>listStyle</code> attribute.
	 * 
	 * @return The <code>listStyle</code> attribute.
	 */
	public String getListStyle() {
		return listStyle;
	}

	/**
	 * Set the value from the <code>listStyle</code> attribute. Blank values
	 * are not normalized to null.
	 * 
	 * @param value
	 *            The <code>listStyle</code> attribute.
	 */
	public void setListStyle(String value) {
		this.listStyle = value;
	}

	/**
	 * Get the value of the <code>listClass</code> attribute.
	 * 
	 * @return The <code>listClass</code> attribute.
	 */
	public String getListClass() {
		return listClass;
	}

	/**
	 * Set the value from the <code>listClass</code> attribute. Blank values
	 * are not normalized to null.
	 * 
	 * @param value
	 *            The <code>listClass</code> attribute.
	 */
	public void setListClass(String value) {
		this.listClass = value;
	}

	/**
	 * Returns the label content, from either the <code>label</code> attribute
	 * or the <code>labelKey</code> message.
	 * 
	 * @return The string to use as the label content.
	 */
	public String getLabelContent() {
		String actualLabel = label;
		if (actualLabel == null) {
			PortalContext portalContext = (PortalContext) pageContext
					.getRequest().getAttribute("portalContext");
			actualLabel = I18nUtility.getValue(labelKey, portalContext);
		}
		return normalize(actualLabel);
	}

}