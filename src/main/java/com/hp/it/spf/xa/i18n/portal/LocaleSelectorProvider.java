/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.portal;

import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * The abstract base class for locale selector providers of all kinds. Subclass
 * this to provide a base class for each general kind of locale selector.
 * </p>
 * <p>
 * For our purposes, a locale selector is a list (eg in a
 * <code>&lt;SELECT&&gt;</code> element) of available locales from which the
 * user can choose, perhaps with the current locale as a pre-selected default.
 * This element may be accompanied by other elements as well (hidden elements, a
 * submit button or image, some prompting label text, etc), all wrapped inside a
 * <code>&lt;FORM&gt;</code> which points to the SPF standard locale selector
 * secondary page. The elements are constructed such that, when the form is
 * submitted:
 * </p>
 * <ul>
 * <li>
 * <p>
 * the chosen locale will be in the form of an RFC 3066 language tag, contained
 * inside the input element named by the value of
 * <code>Consts.LOCALE_SELECTOR_INPUT_NAME</code>
 * </p>
 * </li>
 * <li>
 * <p>
 * the redirect target (where the locale selector secondary page should redirect
 * the user after processing the submission) will be in the form of a URL
 * string, contained inside the input element named by the value of
 * <code>Consts.LOCALE_SELECTOR_TARGET_NAME</code>
 * </p>
 * </li>
 * </ul>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class LocaleSelectorProvider {
	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The portal context for this locale selector provider.
	 */
	protected PortalContext portalContext = null;

	/**
	 * Protected to prevent external construction except by subclasses. Use an
	 * appropriate subclass instead. If a null PortalContext is provided, the
	 * getHTML method of this class will not work (ie will return null).
	 */
	protected LocaleSelectorProvider(PortalContext pContext) {
		this.portalContext = pContext;
	}

	/**
	 * <p>
	 * Get the HTML string for the locale selector widget from the concrete
	 * subclass and render it out, embedded inside a form which points to the
	 * locale selector secondary page (and sets the current URL as the target
	 * parameter for the eventual redirect from that page).
	 * </p>
	 * <p>
	 * This method is equivalent to calling the companion method with the second
	 * boolean parameter set to <code>false</code>.
	 * </p>
	 * 
	 * @param escape
	 *            Whether or not to escape HTML in the visible text content
	 *            (locale options are escaped automatically).
	 * @return The HTML string for complete locale selector form.
	 */
	public String getHTML(boolean escape) {
		return getHTML(escape, false);
	}

	/**
	 * <p>
	 * Get the HTML string for the locale selector widget from the concrete
	 * subclass and render it out, embedded inside a form which points to the
	 * locale selector secondary page (and sets the current URL as the target
	 * parameter for the eventual redirect from that page).
	 * </p>
	 * <p>
	 * The first boolean parameter controls whether or not to escape any HTML
	 * special characters like <code>&lt;</code> (ie convert them to
	 * corresponding HTML character entities so that they display literally)
	 * found in the locale selector content. <b>Note:</b> Locale option names
	 * are automatically escaped regardless. This parameter only controls
	 * escaping of any other text content (eg label text, if the subclass has
	 * it).
	 * </p>
	 * <p>
	 * The second boolean parameter controls whether or not to remove any HTML
	 * <code>&lt;SPAN&gt;</code> markup from the link content, which Vignette
	 * may have automatically added to any text retrieved from a message
	 * resource.
	 * </p>
	 * 
	 * @param escape
	 *            Whether or not to escape HTML in the visible text content
	 *            (locale options are escaped automatically).
	 * @param filterSpan
	 *            Whether or not to strip <code>&lt;SPAN&gt;</code> tags from
	 *            the visible text content.
	 * @return The HTML string for complete locale selector form.
	 */
	public String getHTML(boolean escape, boolean filterSpan) {

		if (portalContext == null) {
			return null;
		}
		HttpServletRequest request = portalContext.getHttpServletRequest();
		if (request == null) {
			return null;
		}

		// Get the current locale and list of available locales.
		Collection availableLocales = I18nUtility.getAvailableLocales(request);
		Locale currentLocale = I18nUtility.getLocale(request);

		// Get the form HTML to wrap around it.
		// Make the form action.
		String localeSelectorURI = portalContext.createProcessURI(
				Consts.PAGE_SELECT_LOCALE).toString();
		StringBuffer html = new StringBuffer();
		if (localeSelectorURI != null) {
			html.append("<form action=\"" + localeSelectorURI
					+ "\" method=\"post\">\n");
		} else {
			html.append("<form>\n");
		}
		// Make the redirect target.
		String currentURL = Utils.getRequestURL(request);
		html.append("<input type=\"hidden\" name=\""
				+ Consts.LOCALE_SELECTOR_TARGET_NAME + "\" value=\""
				+ currentURL + "\">\n");

		// Wrap the widget HTML inside the form HTML and return it.
		String widget = getWidgetHTML(Consts.LOCALE_SELECTOR_INPUT_NAME,
				availableLocales, currentLocale, escape, filterSpan);
		if (widget != null) {
			html.append(widget);
		}
		html.append("</form>");
		return html.toString();
	}

	/**
	 * <p>
	 * Get the HTML for the locale selector widget itself (the part of the form
	 * which is visible to the user), using the given parameters. This method
	 * should only return the various form elements that are visible; the
	 * <code>&lt;FORM&gt;</code> tag and hidden target input should not be
	 * part of it because they are provided by this base class.
	 * </p>
	 * <p>
	 * <b>Note:</b> The contract here is that the returned HTML must define
	 * some widget such that, when the surrounding form is submitted, the
	 * selected locale is in the element named with the given widget name, and
	 * its value is an RFC 3066 language tag (eg <code>zh-CN</code> for
	 * Simplified Chinese). That is what the locale selector secondary page type
	 * is expecting.
	 * </p>
	 * <p>
	 * The first boolean parameter controls whether or not to escape any HTML
	 * special characters like <code>&lt;</code> (ie convert them to
	 * corresponding HTML character entities so that they display literally)
	 * found in the link content. <b>Note:</b> Your subclass should escape
	 * locale options automatically, regardless of this parameter. Use this
	 * parameter only to control whether to escape any other visible text
	 * content (eg label messaging, if the subclass has it).
	 * </p>
	 * <p>
	 * The second boolean parameter controls whether or not to remove any HTML
	 * <code>&lt;SPAN&gt;</code> markup from the link content, which Vignette
	 * may have automatically added when fetching any content from a message
	 * resource bundle.
	 * </p>
	 * 
	 * @param widgetName
	 *            The form element name to use in this widget.
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
	 * @return The HTML string for the locale selector visible elements.
	 */
	protected abstract String getWidgetHTML(String widgetName,
			Collection availableLocales, Locale currentLocale, boolean escape,
			boolean filterSpan);

}