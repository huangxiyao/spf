/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.portal.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.vignette.portal.log.LogWrapper;

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
			String html = getHTML();
			out.println(html);
		} catch (Exception ex) {
			LOGGER.error("LocaleIndicatorBaseTag error: " + ex.getMessage());
			JspException je = new JspException(ex);
			throw je;
		}
		return super.doEndTag();
	}

	/**
	 * Get the tag HTML. Because there could be different renderings by
	 * different subclasses, this is abstract. A JspException should be thrown
	 * if there is an error.
	 * 
	 * @return The locale selector HTML markup.
	 * @throws JspException
	 */
	protected abstract String getHTML() throws JspException;

}
