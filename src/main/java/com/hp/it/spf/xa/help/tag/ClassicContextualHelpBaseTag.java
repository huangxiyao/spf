/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.xa.help.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import com.hp.it.spf.xa.help.tag.ContextualHelpBaseTag;
import com.hp.it.spf.xa.help.ContextualHelpProvider;
import com.hp.it.spf.xa.help.ClassicContextualHelpProvider;

/**
 * <p>
 * An abstract base class representing a "classic"-style contextual help tag
 * such as the portlet framework's
 * <code>&lt;spf-help-portlet:classicContextualHelp&gt;</code> tag and the
 * portal framework's <code>&lt;spf-help-portal:classicContextualHelp&gt;</code>
 * tag.
 * </p>
 * <p>
 * The style of contextual help rendered by this tag is the SPF-provided classic
 * style. For both portal and portlet frameworks, this tag has the following
 * attributes (plus the attributes in the superclass):
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
 * As noted above, this tag is for the classic-style rendering of contextual
 * help. If you would like a custom style, you must implement your own custom
 * tag for it. Implement a ContextualHelpProvider concrete subclass for that
 * custom style. Then implement a tag class for it, like this one, extending
 * ContextualHelpBaseTag. Have the tag construct the appropriate kind of
 * ContextualHelpProvider subclass corresponding to that custom style, and
 * return its HTML.
 * </p>
 * 
 * @author <link href="kuang.cheng@hp.com">Cheng Kuang</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class ClassicContextualHelpBaseTag extends
		ContextualHelpBaseTag {
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
	 * Returns the title content, from either the <code>title</code> attribute
	 * or the <code>titleKey</code> message.
	 * 
	 * @return The string to use as the title content.
	 */
	public String getTitleContent() {
		String actualTitle = title;
		if (actualTitle == null) {
			actualTitle = getMessage(titleKey);
		}
		return actualTitle;
	}

	/**
	 * Initialize the tag attributes.
	 */
	public ClassicContextualHelpBaseTag() {
		super();
		title = null;
		titleKey = null;
		noScriptHref = null;
	}

	/**
	 * Return an instance of the "classic"-style contextual help provider,
	 * populated with the parameters from the current tag. This throws a
	 * JspException if the required parameters for that provider (ie the
	 * <code>titleKey</code> and <code>contentKey</code> attributes) were
	 * not specified in the tag.
	 * 
	 * @param linkContent
	 * @return ContextualHelpProvider
	 * @throws JspException
	 */
	protected ContextualHelpProvider getContextualHelpProvider(String linkContent)
			throws JspException {
		if (linkContent == null) {
			String msg = "ClassicContextualHelpBaseTag error: one of the following attributes is required: anchor, anchorKey, anchorImg, anchorImgKey.";
			logError(this, msg);
			throw new JspException(msg);
		}
		String titleContent = getTitleContent();
		if (titleContent == null) {
			String msg = "ClassicContextualHelpBaseTag error: either the title or titleKey are required attributes.";
			logError(this, msg);
			throw new JspException(msg);
		}
		String helpContent = getHelpContent();
		if (helpContent == null) {
			String msg = "ClassicContextualHelpBaseTag error: either the content or contentKey are required attributes.";
			logError(this, msg);
			throw new JspException(msg);
		}
		ClassicContextualHelpProvider c = newClassicContextualHelpProvider();
		c.setLinkContent(linkContent);
		c.setHelpContent(helpContent);
		c.setTitleContent(titleContent);
		c.setNoScriptHref(noScriptHref);
		return c;
	}

	/**
	 * Abstract method for getting an empty classic contextual help provider.
	 * Different action for portal and portlet, so this is an abstract method.
	 * 
	 * @return An empty classic contextual help provider.
	 */
	protected abstract ClassicContextualHelpProvider newClassicContextualHelpProvider();

}
