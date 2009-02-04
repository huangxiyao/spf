/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.xa.help.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.hp.it.spf.xa.help.ContextualHelpProvider;
import com.hp.it.spf.xa.i18n.tag.MessageBaseTag;
import com.hp.it.spf.xa.help.tag.HelpBaseTag;

/**
 * <p>
 * An abstract base class for all contextual help tags, including the tags for
 * "classic"-style contextual help (eg the portlet framework's
 * <code>&lt;spf-help-portlet:classicContextualHelp&gt;</code> tag). If you
 * create another style of rendering contextual help, and would like to render
 * that help with a custom tag, then you should develop your custom contextual
 * help tag class by subclassing from this one.
 * </p>
 * <p>
 * All contextual help kinds inherit from the HelpBaseTag class and thus take
 * their tags take the same attributes as documented for that class. In
 * addition, all contextual help has help content, so all contextual help
 * parameter tags support the following attributes to pass in the help content:
 * </p>
 * <ul>
 * <li>
 * <p> The <code>content="<i>content</i>"</code> and
 * <code>contentKey="<i>content-key</i>"</code> attributes are alternative
 * ways of providing the help content. If you provide the <code>content</code>
 * attribute, then its value is used directly. Alternatively, if you provide the
 * <code>contentKey</code> attribute, then its value is used as a message
 * resource key for a message string containing the content (this is the duty of
 * one of the abstract methods). One or the other attribute is required. If you
 * specify both, then <code>content</code> will take precedence.
 * </p>
 * </li>
 * </ul>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class ContextualHelpBaseTag extends HelpBaseTag {
	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Stores the value of the <code>content</code> attribute.
	 */
	protected String content;

	/**
	 * Stores the value of the <code>contentKey</code> attribute.
	 */
	protected String contentKey;

	/**
	 * Get the value of the <code>content</code> attribute.
	 * 
	 * @return The <code>content</code> attribute.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Set the value from the <code>content</code> attribute.
	 * 
	 * @param value
	 *            The <code>content</code> attribute.
	 */
	public void setContent(String value) {
		this.content = normalize(value);
	}

	/**
	 * Get the value of the <code>contentKey</code> attribute.
	 * 
	 * @return The <code>contentKey</code> attribute.
	 */
	public String getContentKey() {
		return contentKey;
	}

	/**
	 * Set the value from the <code>contentKey</code> attribute.
	 * 
	 * @param value
	 *            The <code>contentKey</code> attribute.
	 */
	public void setContentKey(String value) {
		this.contentKey = normalize(value);
	}

	/**
	 * Returns the help content, from either the <code>content</code> attribute
	 * or the <code>contentKey</code> message.
	 * 
	 * @return The string to use as the help content.
	 */
	public String getHelpContent() {
		String actualContent = content;
		if (actualContent == null) {
			actualContent = getMessage(contentKey);
		}
		return normalize(actualContent);
	}

	/**
	 * Initialize the tag attributes.
	 */
	public ContextualHelpBaseTag() {
		super();
		contentKey = null;
		content = null;
	}

	
	/**
	 * Gets a ContextualHelpProvider to get the HTML for the contextual help hyperlink
	 * and return it to the tag.
	 * 
	 * @param linkContent
	 *            The string of HTML markup to enclose inside the link.
	 * @return The total HTML markup for the contextual help hyperlink corresponding
	 *         to the attributes of this tag.
	 * @throws JspException
	 */
	protected String getHTML(String linkContent) throws JspException {
		// Get the link content (exception if none was defined or found)
		if (linkContent == null) {
			String msg = "ContextualHelpBaseTag error: one of the following attributes is required: anchor, anchorKey, anchorImg, anchorImgKey.";
			logError(this, msg);
			throw new JspException(msg);
		}
		// Get the help content (exception if none was defined or found)
		String helpContent = getHelpContent();
		if (helpContent == null) {
			String msg = "ContextualHelpBaseTag error: either the content or contentKey are required attributes.";
			logError(this, msg);
			throw new JspException(msg);
		}
		// Get and return the contextual help hyperlink HTML
		ContextualHelpProvider c = getContextualHelpProvider(linkContent);
		c.setLinkContent(linkContent);
		c.setHelpContent(helpContent);
		return c.getHTML(escapeEnabled);
	}

	/**
	 * Abstract method for getting the contextual help provider from the tag
	 * attributes. Throw a JspException if there was a problem. Different action
	 * depending on the particular style of contextual help, so this is
	 * abstract.
	 */
	protected abstract ContextualHelpProvider getContextualHelpProvider(String linkContent)
			throws JspException;

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
