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
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * An abstract base class for all locale indicator tags, including the tag for
 * the "classic"-style locale indicator (ie the
 * <code>&lt;spf-i18n-portal:classicLocaleIndicator&gt;</code> tag). If you
 * create another style of rendering the locale indicator, then you should
 * develop another locale indicator tag class by subclassing from this one.
 * Implement the abstract method which should construct and return your tag HTML
 * from the tag attributes.
 * </p>
 * <p>
 * This base class defines no tag attributes. At the base class level, a locale
 * indicator is comprised simply of the current locale. This is obtained
 * automatically using the I18nUtility.
 * </p>
 * 
 * @author <link href="ming.zou@hp.com">Ming</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class LocaleIndicatorBaseTag extends TagSupport {
	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	private static final LogWrapper LOGGER = new LogWrapper(
			LocaleIndicatorBaseTag.class);

	/**
	 * Constructor to initialize tag attributes (currently none).
	 * 
	 */
	public LocaleIndicatorBaseTag() {
	}

	/**
	 * Release resources (currently none) after processing this tag.
	 */
	public void release() {
		super.release();
	}

	/**
	 * Do tag processing. Get the HTML string from the concrete subclass and
	 * render it out.
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
			Locale currentLocale = I18nUtility.getLocale(request);

			// Get the widget for the locale indicator from the concrete
			// subclass and express it.
			String html = getHTML(currentLocale);
			out.println(html);
		} catch (Exception ex) {
			LOGGER.error("LocaleIndicatorBaseTag error: " + ex.getMessage());
			JspException je = new JspException(ex);
			throw je;
		}
		return super.doEndTag();
	}

	/**
	 * Get the tag HTML for the given current locale. A JspException should be
	 * thrown if there is an error.
	 * 
	 * @param currentLocale The current locale.
	 * @return The locale selector HTML markup.
	 * @throws JspException
	 */
	protected abstract String getHTML(Locale currentLocale) throws JspException;

	/**
	 * Normalize blank string values to null - so the return is either a
	 * non-blank, trimmed string, or null.
	 * 
	 * @param value
	 * @return
	 */
	protected String normalize(String value) {
		if (value != null) {
			value = value.trim();
			if (value.equals("")) {
				value = null;
			}
		}
		return value;
	}
}
