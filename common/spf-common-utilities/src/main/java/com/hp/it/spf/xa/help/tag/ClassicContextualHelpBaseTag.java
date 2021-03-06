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
 * style, which produces a DHTML popup layer. For both portal and portlet
 * frameworks, this tag has the following attributes (plus the attributes in the
 * superclass):
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
 * <li>
 * <p>
 * The <code>width="<i>width</i>"</code> attribute gives the width to use
 * for the contextual help popup. This is specified in pixels. The default is
 * 300.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>borderStyle="<i>border-style</i>"</code> and
 * <code>borderClass="<i>border-class</i>"</code> attributes are alternate
 * ways of specifying a CSS style for the contextual help popup border. With the
 * <code>borderStyle</code> attribute, you pass an inline CSS style. This
 * should be a string of any CSS properties which are valid border properties
 * for the HTML <code>&lt;TABLE&gt;</code> tag. For example,
 * <code>borderStyle="border-width:1px;border-style:solid;border-color:black"</code>.
 * With the <code>borderClass</code> attribute, you pass the name of a CSS
 * class you have already included in your JSP page. That class should specify
 * the same kind of properties as you would in <code>borderStyle</code> - for
 * example, <code>borderClass="my-help-border"</code> where you have elsewhere
 * defined the following CSS class:
 * <code>.my-help-border { border-width:1px; ... }</code>. If you do not
 * provide either the <code>borderClass</code> or <code>borderStyle</code>
 * attributes, then the default border style is a think solid black line. (If
 * you just want to clear this default, without positively providing a
 * replacement style, just pass an empty string to <code>borderStyle</code>
 * and/or <code>borderClass</code>.)
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>titleStyle="<i>title-style</i>"</code> and
 * <code>titleClass="<i>title-class</i>"</code> attributes are alternate
 * ways of specifying a CSS style for the contextual help popup title. With the
 * <code>titleStyle</code> attribute, you pass an inline CSS style. This
 * should be a string of any CSS properties which are valid properties for your
 * title and/or the HTML <code>&lt;TD&gt;</code> tag. For example,
 * <code>titleStyle="background-color:blue;color:white;font-weight:bold"</code>.
 * With the <code>titleClass</code> attribute, you pass the name of a CSS
 * class you have already included in your JSP page. That class should specify
 * the same kind of properties as you would in <code>titleStyle</code> - for
 * example, <code>titleClass="my-help-title"</code> where you have elsewhere
 * defined the following CSS class:
 * <code>.my-help-title { background-color:blue; ... }</code>. If you do not
 * provide either the <code>titleClass</code> or <code>titleStyle</code>
 * attributes, then the default title is a bold white font on blue background.
 * (If you just want to clear this default, without positively providing a
 * replacement style, just pass an empty string to <code>titleStyle</code>
 * and/or <code>titleClass</code>.)
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>contentStyle="<i>content-style</i>"</code> and
 * <code>contentClass="<i>content-class</i>"</code> attributes are alternate
 * ways of specifying a CSS style for the contextual help popup content. With
 * the <code>contentStyle</code> attribute, you pass an inline CSS style. This
 * should be a string of any CSS properties which are valid properties for your
 * content and/or the HTML <code>&lt;TD&gt;</code> tag. For example,
 * <code>contentStyle="background-color:white;color:black;font-weight:normal"</code>.
 * With the <code>contentClass</code> attribute, you pass the name of a CSS
 * class you have already included in your JSP page. That class should specify
 * the same kind of properties as you would in <code>contentStyle</code> - for
 * example, <code>contentClass="my-help-content"</code> where you have
 * elsewhere defined the following CSS class:
 * <code>.my-help-content { background-color:white; ... }</code>. If you do
 * not provide either the <code>contentClass</code> or
 * <code>contentStyle</code> attributes, then the default style for the help
 * content is a normal black font on white background. (If you just want to
 * clear this default, without positively providing a replacement style, just
 * pass an empty string to <code>contentStyle</code> and/or
 * <code>contentClass</code>.)
 * </p>
 * </li>
 * </ul>
 * <p>
 * As noted above, this tag is for the classic-style rendering of contextual
 * help. If you would like a custom style, you must implement your own custom
 * tag for it. Implement a {@link com.hp.it.spf.xa.help.ContextualHelpProvider}
 * concrete subclass for that custom style. Then implement a tag class for it,
 * like this one, extending {@link ContextualHelpBaseTag} like this one does.
 * Have the tag construct your new kind of <code>ContextualHelpProvider</code>,
 * and return its HTML.
 * </p>
 * 
 * @author <link href="kuang.cheng@hp.com">Cheng Kuang</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.help.tag.ContextualHelpBaseTag</code><br>
 *      <code>com.hp.it.spf.xa.help.portal.tag.ClassicContextualHelpTag</code><br>
 *      <code>com.hp.it.spf.xa.help.portlet.tag.ClassicContextualHelpTag</code>
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
	 * Stores the value of the <code>width</code> attribute.
	 */
	protected String width;

	/**
	 * Stores the value of the <code>borderStyle</code> attribute.
	 */
	protected String borderStyle;

	/**
	 * Stores the value of the <code>borderClass</code> attribute.
	 */
	protected String borderClass;

	/**
	 * Stores the value of the <code>titleStyle</code> attribute.
	 */
	protected String titleStyle;

	/**
	 * Stores the value of the <code>titleClass</code> attribute.
	 */
	protected String titleClass;

	/**
	 * Stores the value of the <code>contentStyle</code> attribute.
	 */
	protected String contentStyle;

	/**
	 * Stores the value of the <code>contentClass</code> attribute.
	 */
	protected String contentClass;

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
	 * Set the value from the <code>noScriptHref</code> attribute, with blank
	 * values normalized to null.
	 * 
	 * @param value
	 *            The <code>noScriptHref</code> attribute.
	 */
	public void setNoScriptHref(String value) {
		this.noScriptHref = normalize(value);
	}

	/**
	 * Get the value of the <code>width</code> attribute.
	 * 
	 * @return The <code>width</code> attribute.
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * Set the value from the <code>width</code> attribute. Blank values are
	 * not normalized to null.
	 * 
	 * @param value
	 *            The <code>width</code> attribute.
	 */
	public void setWidth(String value) {
		this.width = value;
	}

	/**
	 * Get the value of the <code>borderStyle</code> attribute.
	 * 
	 * @return The <code>borderStyle</code> attribute.
	 */
	public String getBorderStyle() {
		return borderStyle;
	}

	/**
	 * Set the value from the <code>borderStyle</code> attribute. Blank values
	 * are not normalized to null.
	 * 
	 * @param value
	 *            The <code>borderStyle</code> attribute.
	 */
	public void setBorderStyle(String value) {
		this.borderStyle = value;
	}

	/**
	 * Get the value of the <code>borderClass</code> attribute.
	 * 
	 * @return The <code>borderClass</code> attribute.
	 */
	public String getBorderClass() {
		return borderClass;
	}

	/**
	 * Set the value from the <code>borderClass</code> attribute. Blank values
	 * are not normalized to null.
	 * 
	 * @param value
	 *            The <code>borderClass</code> attribute.
	 */
	public void setBorderClass(String value) {
		this.borderClass = value;
	}

	/**
	 * Get the value of the <code>titleStyle</code> attribute.
	 * 
	 * @return The <code>titleStyle</code> attribute.
	 */
	public String getTitleStyle() {
		return titleStyle;
	}

	/**
	 * Set the value from the <code>titleStyle</code> attribute. Blank values
	 * are not normalized to null.
	 * 
	 * @param value
	 *            The <code>titleStyle</code> attribute.
	 */
	public void setTitleStyle(String value) {
		this.titleStyle = value;
	}

	/**
	 * Get the value of the <code>titleClass</code> attribute.
	 * 
	 * @return The <code>titleClass</code> attribute.
	 */
	public String getTitleClass() {
		return titleClass;
	}

	/**
	 * Set the value from the <code>titleClass</code> attribute. Blank values
	 * are not normalized to null.
	 * 
	 * @param value
	 *            The <code>titleClass</code> attribute.
	 */
	public void setTitleClass(String value) {
		this.titleClass = value;
	}

	/**
	 * Get the value of the <code>contentStyle</code> attribute.
	 * 
	 * @return The <code>contentStyle</code> attribute.
	 */
	public String getContentStyle() {
		return contentStyle;
	}

	/**
	 * Set the value from the <code>contentStyle</code> attribute. Blank
	 * values are not normalized to null.
	 * 
	 * @param value
	 *            The <code>contentStyle</code> attribute.
	 */
	public void setContentStyle(String value) {
		this.contentStyle = value;
	}

	/**
	 * Get the value of the <code>contentClass</code> attribute.
	 * 
	 * @return The <code>contentClass</code> attribute.
	 */
	public String getContentClass() {
		return contentClass;
	}

	/**
	 * Set the value from the <code>contentClass</code> attribute. Blank
	 * values are not normalized to null.
	 * 
	 * @param value
	 *            The <code>contentClass</code> attribute.
	 */
	public void setContentClass(String value) {
		this.contentClass = value;
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
		return normalize(actualTitle);
	}

	/**
	 * Initialize the tag attributes.
	 */
	public ClassicContextualHelpBaseTag() {
		super();
		title = null;
		titleKey = null;
		noScriptHref = null;
		width = null;
		borderStyle = null;
		borderClass = null;
		titleStyle = null;
		titleClass = null;
		contentStyle = null;
		contentClass = null;
	}

	/**
	 * Releases tag resources.
	 */
	public void release() {

		super.release();
		title = null;
		titleKey = null;
		noScriptHref = null;
		width = null;
		borderStyle = null;
		borderClass = null;
		titleStyle = null;
		titleClass = null;
		contentStyle = null;
		contentClass = null;
	}

	/**
	 * Return an instance of the "classic"-style contextual help provider,
	 * populated with the parameters from the current tag. This throws a
	 * {@link javax.servlet.jsp.JspException} if the required parameters for
	 * that provider (ie the link, title, and help content) were not specified
	 * in the tag.
	 * 
	 * @param linkContent
	 * @return The {@link com.hp.it.spf.xa.help.ClassicContextualHelpProvider}
	 *         for this tag.
	 * @throws JspException
	 */
	protected ContextualHelpProvider getContextualHelpProvider(
			String linkContent) throws JspException {
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
		c.setBorderStyle(borderStyle);
		c.setBorderClass(borderClass);
		c.setTitleStyle(titleStyle);
		c.setTitleClass(titleClass);
		c.setHelpStyle(contentStyle);
		c.setHelpClass(contentClass);
		try {
			c.setWidth(Integer.parseInt(width));
		} catch (NumberFormatException e) {
			// leave unset in case of bad value
		}
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
