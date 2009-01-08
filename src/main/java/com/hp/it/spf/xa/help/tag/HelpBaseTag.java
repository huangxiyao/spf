/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.xa.help.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.hp.it.spf.xa.help.ContextualHelpProvider;
import com.hp.it.spf.xa.i18n.tag.MessageBaseTag;

/**
 * <p>
 * An abstract base class for all help tags, including both contextual and
 * global help tags, such as the "classic"-style portal global help tag (<code>&lt;spf-help-portal:classicGlobalHelp&gt;</code>)
 * and the "classic"-style portlet contextual help tag (<code>&lt;spf-help-portal:classicContextualHelp&gt;</code>).
 * If you create another style of rendering either kind of help, and would like
 * to render that help with a custom tag, then you should develop your custom
 * help tag class by subclassing from this one.
 * </p>
 * <p>
 * All help includes a help hyperlink. The content of the help hyperlink can be
 * specified through a proper mix of the following attributes:
 * </p>
 * <ul>
 * <li>
 * <p>
 * The <code>anchor="<i>anchor-text</i>"</code> and
 * <code>anchorKey="<i>anchor-text-key</i>"</code> attributes are
 * alternative ways of providing the some arbirary text content for the help
 * hyperlink. If you provide the <code>anchor</code> attribute, then its value
 * is used as the hyperlink content. Alternatively, if you provide the
 * <code>anchorKey</code> attribute, then its value is used as a message
 * resource key for a message string containing the link content. This message
 * string will be obtained from the resource bundle(s) available to your portlet
 * or portal component.
 * </p>
 * <p>
 * TODO: finish this JavaDoc.
 * </p>
 * </li>
 * </ul>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class HelpBaseTag extends TagSupport {
	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Stores the value of the <code>anchor</code> attribute.
	 */
	protected String anchor;

	/**
	 * Stores the value of the <code>anchorKey</code> attribute.
	 */
	protected String anchorKey;

	/**
	 * Stores the value of the <code>anchorImg</code> attribute.
	 */
	protected String anchorImg;

	/**
	 * Stores the value of the <code>anchorImgKey</code> attribute.
	 */
	protected String anchorImgKey;

	/**
	 * Stores the value of the <code>anchorImgAlt</code> attribute.
	 */
	protected String anchorImgAlt;

	/**
	 * Stores the value of the <code>anchorImgAltKey</code> attribute.
	 */
	protected String anchorImgAltKey;

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
	 * Set the value from the <code>anchor</code> attribute.
	 * 
	 * @param value
	 *            The <code>anchor</code> attribute.
	 */
	public void setAnchor(String value) {
		this.anchor = normalize(value);
	}

	/**
	 * Get the value of the <code>anchor</code> attribute.
	 * 
	 * @return The <code>anchor</code> attribute.
	 */
	public String getAnchor() {
		return anchor;
	}

	/**
	 * Set the value from the <code>anchorKey</code> attribute.
	 * 
	 * @param value
	 *            The <code>anchorKey</code> attribute.
	 */
	public void setAnchorKey(String value) {
		this.anchorKey = normalize(value);
	}

	/**
	 * Get the value of the <code>anchorKey</code> attribute.
	 * 
	 * @return The <code>anchorKey</code> attribute.
	 */
	public String getAnchorKey() {
		return anchorKey;
	}

	/**
	 * Set the value from the <code>anchorImg</code> attribute.
	 * 
	 * @param value
	 *            The <code>anchorImg</code> attribute.
	 */
	public void setAnchorImg(String value) {
		this.anchorImg = normalize(value);
	}

	/**
	 * Get the value of the <code>anchorImg</code> attribute.
	 * 
	 * @return The <code>anchorImg</code> attribute.
	 */
	public String getAnchorImg() {
		return anchorImg;
	}

	/**
	 * Set the value from the <code>anchorImgKey</code> attribute.
	 * 
	 * @param value
	 *            The <code>anchorImgKey</code> attribute.
	 */
	public void setAnchorImgKey(String value) {
		this.anchorImgKey = normalize(value);
	}

	/**
	 * Get the value of the <code>anchorImgKey</code> attribute.
	 * 
	 * @return The <code>anchorImgKey</code> attribute.
	 */
	public String getAnchorImgKey() {
		return anchorImgKey;
	}

	/**
	 * Set the value from the <code>anchorImgAlt</code> attribute.
	 * 
	 * @param value
	 *            The <code>anchorImgAlt</code> attribute.
	 */
	public void setAnchorImgAlt(String value) {
		this.anchorImgAlt = normalize(value);
	}

	/**
	 * Get the value of the <code>anchorImgAlt</code> attribute.
	 * 
	 * @return The <code>anchorImgAlt</code> attribute.
	 */
	public String getAnchorImgAlt() {
		return anchorImgAlt;
	}

	/**
	 * Set the value from the <code>anchorImgAltKey</code> attribute.
	 * 
	 * @param value
	 *            The <code>anchorImgAltKey</code> attribute.
	 */
	public void setAnchorImgAltKey(String value) {
		this.anchorImgAltKey = normalize(value);
	}

	/**
	 * Get the value of the <code>anchorImgAltKey</code> attribute.
	 * 
	 * @return The <code>anchorImgAltKey</code> attribute.
	 */
	public String getAnchorImgAltKey() {
		return anchorImgAltKey;
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
			value = value.trim();
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
	 * Initialize the tag attributes.
	 */
	public HelpBaseTag() {
		super();
		escape = null;
		escapeEnabled = false;
		anchorKey = null;
		anchor = null;
		anchorImgKey = null;
		anchorImg = null;
		anchorImgAltKey = null;
		anchorImgAlt = null;
	}

	/**
	 * Releases tag resources.
	 */
	public void release() {

		super.release();
		this.anchorKey = null;
		this.anchor = null;
		this.anchorImgKey = null;
		this.anchorImg = null;
		this.anchorImgAltKey = null;
		this.anchorImgAlt = null;
		this.escape = null;
		this.escapeEnabled = false;
	}

	/**
	 * Do the tag processing. An error is thrown if the tag is missing a
	 * required attribute. Otherwise, it gets the help hyperlink HTML from the
	 * concrete subclass and writes it out.
	 * 
	 * @return int
	 * @throws JspException
	 */
	public int doEndTag() throws JspException {
		if (anchor == null && anchorKey == null && anchorImg == null
				&& anchorImgKey == null) {
			String msg = "HelpBaseTag error: one of the following attributes is required: anchor, anchorKey, anchorImg, or anchorImgKey.";
			logError(this, msg);
			throw new JspException(msg);
		}
		JspWriter out = pageContext.getOut();
		try {
			String value = getHTML();
			if (value == null) {
				value = "";
			}
			out.print(value);
		} catch (Exception e) {
			logError(this, "HelpBaseTag error: " + e.getMessage());
			JspException jspE = new JspException(e);
			throw jspE;
		}
		return EVAL_PAGE;
	}

	/**
	 * Abstract method for getting the tag markup (ie the help hyperlink
	 * markup). The action is different depending on the type of help (global or
	 * contextual), so this method is abstract.
	 * 
	 * @return The help hyperlink markup.
	 */
	public abstract String getHTML();

	/**
	 * Abstract method for logging a tag error. Different action for portal and
	 * portlet, so this is an abstract method.
	 * 
	 * @param obj
	 *            The object asking to log this error.
	 * @param msg
	 *            The error message.
	 */
	public abstract void logError(Object obj, String msg);

	/**
	 * Normalize blank string values to null - so the return is either a
	 * non-blank string, or null.
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
