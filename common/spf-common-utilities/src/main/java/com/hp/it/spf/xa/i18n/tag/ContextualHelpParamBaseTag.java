/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.xa.i18n.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.hp.it.spf.xa.help.ContextualHelpProvider;
import com.hp.it.spf.xa.i18n.tag.MessageBaseTag;

/**
 * <p>
 * An abstract base class for all contextual help parameter tags, including the
 * tags for "classic"-style contextual help parameters (eg the portlet
 * framework's <code>&lt;spf-i18n-portlet:classicContextualHelpParam&gt;</code>
 * tag). If you create another style of rendering contextual help, and would
 * like to render that style by embedding
 * <code>&lt;Contextual_Help&gt;...&lt;/Contextual_Help&gt;</code> tokens into
 * your message string, then you should develop a contextual help parameter tag
 * class by subclassing from this one. Implement the abstract methods which
 * should construct and return your new subclass of
 * {@link com.hp.it.spf.xa.help.ContextualHelpProvider} with your content
 * message; this base class will set that into the parent message tag for
 * output.
 * </p>
 * <p>
 * All contextual help has help content, so all contextual help parameter tags
 * support attributes to pass in the help conent. The
 * <code>content="<i>content</i>"</code> and
 * <code>contentKey="<i>content-key</i>"</code> attributes are alternative
 * ways of providing the help content. If you provide the <code>content</code>
 * attribute, then its value is used directly. Alternatively, if you provide the
 * <code>contentKey</code> attribute, then its value is used as a message
 * resource key for a message string containing the content (this is the duty of
 * one of the abstract methods). One or the other attribute is required. If you
 * specify both, then <code>content</code> will take precedence.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.i18n.tag.ClassicContextualHelpParamBaseTag</code>
 */
public abstract class ContextualHelpParamBaseTag extends TagSupport {
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
	 * Set the value from the <code>content</code> attribute, normalizing
	 * blank to null.
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
	 * Set the value from the <code>contentKey</code> attribute, normalizing
	 * blank to null.
	 * 
	 * @param value
	 *            The <code>contentKey</code> attribute.
	 */
	public void setContentKey(String value) {
		this.contentKey = normalize(value);
	}

	/**
	 * Returns the help content, from either the <code>content</code>
	 * attribute or the <code>contentKey</code> message.
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
	public ContextualHelpParamBaseTag() {
		super();
		contentKey = null;
		content = null;
	}

	/**
	 * Do the tag processing. An error is thrown if the tag discovers it is not
	 * contained inside the body of a surrounding message tag (any subclass of
	 * {@link MessageBaseTag}). Otherwise, it takes the
	 * {@link com.hp.it.spf.xa.help.ContextualHelpProvider} provided by the
	 * concrete subclass and sets it into the parent message tag.
	 * 
	 * @return int
	 * @throws JspException
	 */
	public int doEndTag() throws JspException {
		Tag parent = getParent();
		if (parent == null) {
			String msg = "ContextualHelpParamBaseTag error: requires surrounding message tag of type MessageBaseTag.";
			logError(this, msg);
			throw new JspException(msg);
		}
		if (!(parent instanceof MessageBaseTag)) {
			String msg = "ContextualHelpParamBaseTag error: requires surrounding message tag of type MessageBaseTag.";
			logError(this, msg);
			throw new JspException(msg);
		}
		ContextualHelpProvider c = getContextualHelpProvider();
		MessageBaseTag messageTag = (MessageBaseTag) parent;
		messageTag.addContextualHelpProvider(c);
		return super.doEndTag();
	}

	/**
	 * Abstract method for getting the contextual help provider from the tag
	 * attributes. Throw a JspException if there was a problem. Different action
	 * depending on the particular style of contextual help, so this is
	 * abstract.
	 * 
	 * @return The contextual help provider containing the contextual help
	 *         parameters.
	 */
	protected abstract ContextualHelpProvider getContextualHelpProvider()
			throws JspException;

	/**
	 * Abstract method for getting a contextual help text string from a message
	 * resource. Should return the content key itself if there was a problem.
	 * Different action for portal and portlet, so this is an abstract method.
	 * The method should return the "raw" message, without any escaping.
	 * 
	 * @param key
	 *            The message key.
	 * @return The message value.
	 */
	protected abstract String getMessage(String key);

	/**
	 * Abstract method for logging a tag error. Different action for portal and
	 * portlet, so this is an abstract method.
	 * 
	 * @param obj
	 *            The object asking to log this error.
	 * @param msg
	 *            The error message.
	 */
	protected abstract void logError(Object obj, String msg);

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
