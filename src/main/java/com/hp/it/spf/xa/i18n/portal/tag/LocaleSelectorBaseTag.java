/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.portal.tag;

import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * An abstract base class for all locale selector tags, including the tag for
 * the "classic"-style locale selector (ie the
 * <code>&lt;spf-i18n-portal:classicLocaleSelector&gt;</code> tag). If you
 * create another style of rendering the locale selector, then you should
 * develop another locale selector tag class by subclassing from this one.
 * Implement the abstract method which should construct and return your tag HTML
 * from the tag attributes.
 * </p>
 * <p>
 * This base class defines no tag attributes. At the base class level, a locale
 * selector is comprised simply of an HTML form (pointing to the locale selector
 * secondary page) and containing a redirect target and HTML for the
 * locale-selector widget itself. The widget HTML is expected to be provided by
 * the concrete subclass.
 * </p>
 * 
 * @author <link href="maomao.guan@hp.com">Aaron</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class LocaleSelectorBaseTag extends TagSupport {
	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	private static final LogWrapper LOGGER = new LogWrapper(
			LocaleSelectorBaseTag.class);

	/**
	 * Constructor to initialize tag attributes (currently none).
	 * 
	 */
	public LocaleSelectorBaseTag() {
	}

	/**
	 * Release resources (currently none) after processing this tag.
	 */
	public void release() {
		super.release();
	}

	/**
	 * Do tag processing. Get the HTML string for the locale selector widget
	 * from the concrete subclass and render it out, embedded inside a form
	 * which points to the locale selector secondary page (and sets the current
	 * URL as the target parameter for the eventual redirect from that page).
	 * 
	 * @throws JspException
	 * @return int
	 */
	public int doEndTag() throws JspException {

		JspWriter out = pageContext.getOut();
		try {
			// Get the current locale and list of available locales.
			PortalContext portalContext = (PortalContext) pageContext
					.getRequest().getAttribute("portalContext");
			HttpServletRequest request = portalContext.getHttpServletRequest();
			Collection availableLocales = I18nUtility
					.getAvailableLocales(request);
			Locale currentLocale = I18nUtility.getLocale(request);

			// Get the HTML for the select widget itself.
			String widget = getWidgetHTML(Consts.LOCALE_SELECTOR_INPUT_NAME,
					availableLocales, currentLocale);

			// Get the form HTML to wrap around it.
			// Make the form action.
			String localeSelectorURI = portalContext.createProcessURI(
					Consts.PAGE_SELECT_LOCALE).toString();
			String html = "";
			if (localeSelectorURI != null) {
				html += "<form action=\"" + localeSelectorURI
						+ "\" method=post>\n";
			} else {
				html += "<form>\n";
			}
			// Make the redirect target.
			String currentURL = Utils.getRequestURL(request);
			html += "<input type=hidden name=\""
					+ Consts.LOCALE_SELECTOR_TARGET_NAME + "\" value=\""
					+ currentURL + "\">\n";

			// Wrap the widget HTML inside the form HTML.
			html += widget;
			html += "</form>";

			// Express the form HTML with embedded select widget.
			out.println(html);
		} catch (Exception ex) {
			LOGGER.error("LocaleSelectorBaseTag error: " + ex.getMessage());
			JspException je = new JspException(ex);
			throw je;
		}
		return super.doEndTag();
	}

	/**
	 * <p>
	 * Get the HTML for the locale selector widget itself (the part of the form
	 * which is visible to the user), using the given parameters. This method
	 * should only return the various form elements that are visible; the
	 * <code>&lt;FORM&gt;</code> tag and hidden target input should not be
	 * part of it because they are provided by this base class. A JspException
	 * should be thrown if there is an error.
	 * </p>
	 * <p>
	 * <b>Note:</b> The contract here is that the returned HTML must define
	 * some widget such that, when the surrounding form is submitted, the
	 * selected locale is in the element named with the given widget name, and
	 * its value is an RFC 3066 language tag (eg <code>zh-CN</code> for
	 * Simplified Chinese). That is what the locale selector secondary page type
	 * is expecting.
	 * </p>
	 * 
	 * @param widgetName
	 *            The form element name to use in this widget.
	 * @param availableLocales
	 *            An unsorted collection of all of the available locales (one or
	 *            more) from which the user may select.
	 * @param currentLocale
	 *            The user's current locale.
	 * @return The locale selector HTML markup.
	 * @throws JspException
	 */
	protected abstract String getWidgetHTML(String widgetName,
			Collection availableLocales, Locale currentLocale)
			throws JspException;

}