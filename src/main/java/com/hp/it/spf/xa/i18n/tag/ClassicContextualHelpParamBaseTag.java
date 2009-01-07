/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.xa.i18n.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import com.hp.it.spf.xa.i18n.tag.ContextualHelpParamBaseTag;
import com.hp.it.spf.xa.help.ContextualHelpProvider;
import com.hp.it.spf.xa.help.ClassicContextualHelpProvider;

/**
 * <p>
 * An abstract base class representing a "classic"-style contextual help
 * parameter tag such as the portlet framework's
 * <code>&lt;spf-i18n-portlet:classicContextualHelpParam&gt;</code> tag and the
 * portal framework's
 * <code>&lt;spf-i18n-portal:i18nClassicContextualHelpParam&gt;</code> tag. You use
 * these tags to define parameters for surrounding message tags for messages
 * containing <code>&lt;Contextual_Help&gt;...&lt;/Contextual_Help&gt;</code>
 * tokens. These message tags include the portlet framework's
 * <code>&lt;spf-i18n-portlet:message&gt;</code> tag and the portal
 * framework's <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag (see). You
 * place the contextual help parameter tag inside the body of the surrounding
 * message tag, in order to pass parameters in that order into the message.
 * </p>
 * <p>
 * The style of contextual help rendered by this tag is the SPF-provided default
 * style. For both portal and portlet frameworks, this tag has the following
 * attributes:
 * </p>
 * <ul>
 * <li>
 * <p>
 * The <code>title="<i>title</i>"</code> and
 * <code>titleKey="<i>title-key</i>"</code> attributes are alternative ways
 * of providing the title in the contextual help popup. If you provide the
 * <code>title</code> attribute, then its value is used as the title.
 * Alternatively, if you provide the <code>titleKey</code> attribute, then its
 * value is used as a message resource key for a message string containing the
 * title. This message string will be obtained from the resource bundle(s)
 * available to your portlet or portal component. One or the other attribute is
 * required. If you specify both, then <code>title</code> will take
 * precedence.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>content="<i>content</i>"</code> and
 * <code>contentKey="<i>content-key</i>"</code> attributes are alternative
 * ways of providing the help content for the popup. If you provide the
 * <code>content</code> attribute, then its value is used directly.
 * Alternatively, if you provide the <code>contentKey</code> attribute, then
 * its value is used as a message resource key for a message string containing
 * the content. One or the other attribute is required. If you specify both,
 * then <code>content</code> will take precedence.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>noScriptHref="<i>url</i>"</code> attribute gives an alternative
 * URL to offer the browser to use in case JavaScript is disabled. Because the
 * default-style contextual help requires JavaScript, it will not work if
 * JavaScript is not enabled in the browser. But you can specify an alternate
 * URL for a page to open when the contextual-help hyperlink is clicked, via
 * this attribute.
 * </p>
 * </li>
 * </ul>
 * <p>
 * As noted above, this tag is for the default-style rendering of contextual
 * help. If you would like a custom style, you must implement your own custom
 * tag for it. Like the above tags, your custom tag would be used inside a
 * message tag body. Implement a ContextualHelpProvider concrete subclass for
 * that custom style. Then implement a tag class for it, like this one. Have the
 * tag construct the appropriate kind of ContextualHelpProvider subclass
 * corresponding to that custom style, and have the tag add it into the parent
 * message tag's list.
 * </p>
 * 
 * @author <link href="kuang.cheng@hp.com">Cheng Kuang</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class ClassicContextualHelpParamBaseTag extends
		ContextualHelpParamBaseTag {
	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Stores the value of the <code>title</code> attribute.
	 */
	protected String title;

	/**
	 * Stores the value of the <code>titleKey</code> attribute.
	 */
	protected String titleKey;

	/**
	 * Stores the value of the <code>noScriptHref</code> attribute.
	 */
	protected String noScriptHref;

	/**
	 * Stores the value of the <code>content</code> attribute.
	 */
	protected String content;

	/**
	 * Stores the value of the <code>contentKey</code> attribute.
	 */
	protected String contentKey;

	/**
	 * Get the value of the <code>title</code> attribute.
	 * 
	 * @return The <code>title</code> attribute.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the value from the <code>title</code> attribute, with blank values
	 * normalized to null.
	 * 
	 * @param value
	 *            The <code>title</code> attribute.
	 */
	public void setTitle(String value) {
		this.title = normalize(value);
	}

	/**
	 * Get the value of the <code>titleKey</code> attribute.
	 * 
	 * @return The <code>titleKey</code> attribute.
	 */
	public String getTitleKey() {
		return titleKey;
	}

	/**
	 * Set the value from the <code>titleKey</code> attribute, with blank
	 * values normalized to null.
	 * 
	 * @param value
	 *            The <code>titleKey</code> attribute.
	 */
	public void setTitleKey(String value) {
		this.titleKey = normalize(value);
	}

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
	 * Get the value of the <code>noScriptHref</code> attribute.
	 * 
	 * @return The <code>noScriptHref</code> attribute.
	 */
	public String getNoScriptHref() {
		return noScriptHref;
	}

	/**
	 * Set the value from the <code>noScriptHref</code> attribute.
	 * 
	 * @param value
	 *            The <code>noScriptHref</code> attribute.
	 */
	public void setNoScriptHref(String value) {
		this.noScriptHref = normalize(value);
	}

	/**
	 * Initialize the tag attributes.
	 */
	public ClassicContextualHelpParamBaseTag() {
		super();
		title = null;
		titleKey = null;
		noScriptHref = null;
		contentKey = null;
		content = null;
	}

	/**
	 * Return an instance of the "classic"-style contextual help provider,
	 * populated with the parameters from the current tag. This throws a
	 * JspException if the required parameters for that provider (ie the
	 * <code>titleKey</code> and <code>contentKey</code> attributes) were
	 * not specified in the tag. This method leaves the link content
	 * unpopulated; the surrounding tag will populate that.
	 * 
	 * @return ContextualHelpProvider
	 * @throws JspException
	 */
	public ContextualHelpProvider getContextualHelpProvider()
			throws JspException {
		if (title == null && titleKey == null) {
			String msg = "ClassicContextualHelpParamBaseTag error: either the title or titleKey are required attributes.";
			logError(this, msg);
			throw new JspException(msg);
		}
		if (content == null && contentKey == null) {
			String msg = "ClassicContextualHelpParamBaseTag error: either the content or contentKey are required attributes.";
			logError(this, msg);
			throw new JspException(msg);
		}
		String actualTitle = title;
		if (actualTitle == null) {
			actualTitle = getTitleMessage();
		}
		String actualContent = content;
		if (actualContent == null) {
			actualContent = getContentMessage();
		}
		ClassicContextualHelpProvider c = new ClassicContextualHelpProvider();
		c.setHelpContent(actualContent);
		c.setTitleContent(actualTitle);
		c.setNoScriptHref(noScriptHref);
		return c;
	}

	/**
	 * Abstract method for getting the contextual help title string from a
	 * message resource. Should return the title key itself if there was a
	 * problem. Different action for portal and portlet, so this is an abstract
	 * method.
	 * 
	 * @return The help title message.
	 */
	public abstract String getTitleMessage();

	/**
	 * Abstract method for getting the contextual help content string from a
	 * message resource. Should return the content key itself if there was a
	 * problem. Different action for portal and portlet, so this is an abstract
	 * method.
	 * 
	 * @return The help content message.
	 */
	public abstract String getContentMessage();

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
	private String normalize(String value) {
		if (value != null) {
			value = value.trim();
			if (value.equals("")) {
				value = null;
			}
		}
		return value;
	}
}
