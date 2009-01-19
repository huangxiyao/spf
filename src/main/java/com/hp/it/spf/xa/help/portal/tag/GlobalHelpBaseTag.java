/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.xa.help.portal.tag;

import javax.servlet.jsp.JspException;

import com.hp.it.spf.xa.help.portal.GlobalHelpProvider;
import com.hp.it.spf.xa.help.tag.HelpBaseTag;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * An abstract base class for all global help tags, including the
 * "classic"-style portal global help tag (<code>&lt;spf-help-portal:classicGlobalHelp&gt;</code>).
 * If you create another style of rendering global help, and would like to
 * render that help with a custom tag, then you should develop your custom
 * global help tag class by subclassing from this one.
 * </p>
 * <p>
 * All global help kinds inherit from the HelpBaseTag class and thus take their
 * tags take the same attributes as documented for that class. In addition, this
 * class defines the <code>filterSpan="<i>true-or-false</i>"</code>
 * attribute. This attribute, when set to <code>true</code>, removes any
 * <code>&lt;SPAN&gt;</code> which Vignette may inject into the anchor text or
 * image alt messages (if you are using those attributes). This is an optional
 * attribute and is off by default.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class GlobalHelpBaseTag extends HelpBaseTag {
	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	private final LogWrapper LOG = new LogWrapper(GlobalHelpBaseTag.class);

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
			value = value.trim();
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
	 * Initialize the tag attributes.
	 */
	public GlobalHelpBaseTag() {
		super();
		filterSpan = null;
		filterSpanEnabled = false;
	}

	/**
	 * Releases tag resources.
	 */
	public void release() {

		super.release();
		filterSpan = null;
		filterSpanEnabled = false;
	}

	/**
	 * Concrete method for getting a message from a portal resource bundle. The
	 * message is returned unescaped and unfiltered; any necessary
	 * escaping/filtering will happen later, in the concrete getHTML method.
	 * If the message is not found, the key is returned instead.
	 * 
	 * @param key
	 *            The message key.
	 * @return The message value (unescaped and unfiltered), localized for the
	 *         user.
	 */
	protected String getMessage(String key) {
		PortalContext portalContext = (PortalContext) pageContext.getRequest()
				.getAttribute("portalContext");
		return I18nUtility.getValue(key, portalContext);
	}

	/**
	 * Concrete method for getting a localized image URL for a portal component.
	 * If no image URL is found, null is returned instead.
	 * 
	 * @param baseFilename
	 *            The image basefilename (eg <code>picture.jpg</code>).
	 * @return A URL, properly built and encoded, for the best-candidate
	 *         localized version of that image for the user.
	 */
	protected String getLocalizedImageURL(String baseFilename) {
		PortalContext portalContext = (PortalContext) pageContext.getRequest()
				.getAttribute("portalContext");
		return I18nUtility.getLocalizedFileURL(portalContext, baseFilename);
	}

	/**
	 * Gets a GlobalHelpProvider to get the HTML for the global help hyperlink
	 * and return it to the tag.
	 * 
	 * @param linkContent
	 *            The string of HTML markup to enclose inside the link.
	 * @return The total HTML markup for the global help hyperlink corresponding
	 *         to the attributes of this tag.
	 * @throws JspException
	 */
	protected String getHTML(String linkContent) throws JspException {
		// Get the link content (exception if none was defined or found)
		if (linkContent == null) {
			String msg = "GlobalHelpBaseTag error: no link content was found.";
			logError(this, msg);
			throw new JspException(msg);
		}
		// Get and return the global help hyperlink HTML
		GlobalHelpProvider g = getGlobalHelpProvider(linkContent);
		return g.getHTML(escapeEnabled, filterSpanEnabled);
	}

	/**
	 * Abstract method to get the concrete global help provider.
	 */
	protected abstract GlobalHelpProvider getGlobalHelpProvider(String linkContent)
			throws JspException;

	/**
	 * Log a tag error into the portal error log file.
	 * 
	 * @param obj
	 *            The object asking to log this error.
	 * @param msg
	 *            The error message.
	 */
	protected void logError(Object obj, String msg) {
		LOG.error(msg);
	}
}
