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
import com.hp.it.spf.xa.i18n.portal.LocaleSelectorProvider;
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
 * Implement the abstract method which should construct and return the proper
 * locale selector provider, based on the tag attributes.
 * </p>
 * <p>
 * This base tag takes two optional attributes:
 * </p>
 * <ul>
 * <li>
 * <p>
 * <code>escape="<i>true-or-false</i>"</code> is an optional switch
 * (default: <code>"false"</code>) which if set to <code>"true"</code> will
 * convert any HTML special characters found in the any of the visible text
 * generated by the tag (if any) into their equivalent HTML character entities.
 * <b>Note:</b> Regardless of the value of this switch, the locale options in
 * the selector are escaped (though generally they should contain no markup
 * anyway).
 * </p>
 * </li>
 * <li>
 * <p>
 * <code>filterSpan="<i>true-or-false</i>"</code> is another optional switch
 * (default: <code>"false"</code>) which, when set to <code>true</code>,
 * removes any <code>&lt;SPAN&gt;</code> which Vignette may have injected into
 * the visible text generated by the tag, if any.
 * </p>
 * </li>
 * </ul>
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
	 * The <code>filterSpan</code> attribute from the tag.
	 */
	protected String filterSpan;

	/**
	 * A boolean switch representing the <code>filterSpan</code> attribute in
	 * boolean form.
	 */
	protected boolean filterSpanEnabled;

	/**
	 * The <code>escape</code> attribute from the tag.
	 */
	protected String escape;

	/**
	 * A boolean switch representing the <code>escape</code> attribute in
	 * boolean form.
	 */
	protected boolean escapeEnabled;

	/**
	 * Set the filter-span switch from the <code>filterSpan</code> attribute,
	 * normalizing blank to null.
	 * 
	 * @param value
	 *            Value of the <code>filterSpan</code> attribute.
	 */
	public void setFilterSpan(String value) {
		this.filterSpan = normalize(value);
		this.filterSpanEnabled = false;
		if (value != null) {
			if ("true".equalsIgnoreCase(value)) {
				this.filterSpanEnabled = true;
			}
		}
	}

	/**
	 * Get the value of the <code>filterSpan</code> attribute.
	 * 
	 * @return Value of the <code>filterSpan</code> attribute.
	 */
	public String getFilterSpan() {
		return filterSpan;
	}

	/**
	 * Returns true if <code>filterSpan="true"</code> (case-insensitive) and
	 * false otherwise.
	 * 
	 * @return Whether filter-span behavior is enabled.
	 */
	public boolean isFilterSpanEnabled() {
		return filterSpanEnabled;
	}

	/**
	 * Set the escape-HTML switch from the <code>escape</code> attribute,
	 * normalizing blank to null.
	 * 
	 * @param value
	 *            Value of the <code>escape</code> attribute.
	 */
	public void setEscape(String value) {
		this.escape = normalize(value);
		this.escapeEnabled = false;
		if (value != null) {
			if ("true".equalsIgnoreCase(value)) {
				this.escapeEnabled = true;
			}
		}
	}

	/**
	 * Get the value of the <code>escape</code> attribute.
	 * 
	 * @return Value of the <code>escape</code> attribute.
	 */
	public String getEscape() {
		return escape;
	}

	/**
	 * Returns true if <code>escape="true"</code> (case-insensitive) and false
	 * otherwise.
	 * 
	 * @return Whether escape-HTML behavior is enabled.
	 */
	public boolean isEscapeEnabled() {
		return escapeEnabled;
	}

	/**
	 * Constructor to initialize tag attributes.
	 * 
	 */
	public LocaleSelectorBaseTag() {
		super();
		filterSpan = null;
		filterSpanEnabled = false;
		escape = null;
		escapeEnabled = false;
	}

	/**
	 * Release resources after processing this tag.
	 */
	public void release() {
		super.release();
		filterSpan = null;
		filterSpanEnabled = false;
		escape = null;
		escapeEnabled = false;
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
			// Get the locale selector provider, gets its HTML, and output it.
			LocaleSelectorProvider l = getLocaleSelectorProvider();
			String value = l.getHTML(escapeEnabled, filterSpanEnabled);
			if (value == null) {
				value = "";
			}
			out.print(value);
		} catch (Exception ex) {
			LOGGER.error("LocaleSelectorBaseTag error: " + ex.getMessage());
			JspException je = new JspException(ex);
			throw je;
		}
		return super.doEndTag();
	}

	/**
	 * Abstract method for getting the locale selector provider from the tag
	 * attributes. Throw a JspException if there was a problem. Different action
	 * depending on the particular style of contextual help, so this is
	 * abstract.
	 */
	protected abstract LocaleSelectorProvider getLocaleSelectorProvider()
			throws JspException;

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