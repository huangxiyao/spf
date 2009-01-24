/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.portal.tag;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.i18n.portal.tag.LocaleIndicatorBaseTag;
import com.vignette.portal.log.LogWrapper;

/**
 * <p>
 * The tag class for the "classic"-style locale indicator (ie the
 * <code>&lt;spf-i18n-portal:classicLocaleIndicator&gt;</code> tag). This tag
 * just expresses the display name of the current locale.
 * </p>
 * 
 * @author <link href="ming.zou@hp.com">Ming</link>
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
	 * Constructor to initialize tag attributes (currently none).
	 * 
	 */
	public ClassicLocaleIndicatorTag() {
	}

	/**
	 * Release resources (currently none) after processing this tag.
	 */
	public void release() {
		super.release();
	}

	/**
	 * Get the HTML string for rendering the "classic"-style locale indicator
	 * for the current locale.
	 * 
	 * @throws JspException
	 * @return The HTML string for "classic"-style locale indicator.
	 */
	protected String getHTML() throws JspException {

		String html = "";
		try {
			// get locale object and display it in the page
			Locale locale = I18nUtility
					.getLocale((HttpServletRequest) pageContext.getRequest());
			// get display name in same locale (not necessarily current
			// locale) - note that HPWeb standard is to display country
			// first, language second
			String dispName = I18nUtility.getLocaleDisplayName(locale,
					locale, I18nUtility.LOCALE_BY_COUNTRY);
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
