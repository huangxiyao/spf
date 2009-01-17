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
 * <code>&lt;spf-i18n-portlet:classicContextualHelpParam&gt;</code> tag and
 * the portal framework's
 * <code>&lt;spf-i18n-portal:i18nClassicContextualHelpParam&gt;</code> tag.
 * You use these tags to define parameters for surrounding message tags for
 * messages containing
 * <code>&lt;Contextual_Help&gt;...&lt;/Contextual_Help&gt;</code> tokens.
 * These message tags include the portlet framework's
 * <code>&lt;spf-i18n-portlet:message&gt;</code> tag and the portal
 * framework's <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag (see). You
 * place the contextual help parameter tag inside the body of the surrounding
 * message tag, in order to pass parameters in that order into the message.
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
 * <li>
 * <p>
 * The <code>width="<i>width</i>"</code> attribute gives the width to use
 * for the contextual help popup. This is specified in pixels. The default is
 * 300.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>borderStyle="<i>border-style</i>"</code> attribute gives the
 * style to use for the contextual help popup border. This is specified using
 * CSS properties. You may use any properties which are valid border properties
 * for the HTML <code>&lt;TABLE&gt;</code> tag. For example,
 * <code>borderStyle="border-width:1px;border-style:solid;border-color:black"</code>
 * (which is also the default: a thin solid black line).
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>titleBgStyle="<i>title-background-style</i>"</code> attribute
 * gives the style to use for the contextual help title background. This is
 * specified using CSS properties. You may use any properties which are valid
 * background properties for the HTML <code>&lt;TD&gt;</code> tag. For
 * example, <code>titleBgStyle="background-color:blue"</code> (which is also
 * the default: a blue background).
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>titleFontStyle="<i>title-font-style</i>"</code> attribute gives
 * the style to use for the contextual help title content. This is specified
 * using CSS properties. You may use any properties which are valid for the HTML
 * <code>&lt;FONT&gt;</code> tag. For example,
 * <code>titleFontStyle="color:white;font-weight:bold"</code> (which is also
 * the default: bold white text). (<b>Note:</b> This attribute is not the only
 * way to specify title font styles; you can just embed your desired font (and
 * other) effects in the title content string itself.)
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>contentBgStyle="<i>help-content-background-style</i>"</code>
 * attribute gives the style to use for the contextual help content background.
 * This is specified using CSS properties. You may use any properties which are
 * valid background properties for the HTML <code>&lt;TD&gt;</code> tag. For
 * example, <code>titleBgStyle="background-color:white"</code> (which is also
 * the default: a white background).
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>contentFontStyle="<i>help-content-font-style</i>"</code>
 * attribute gives the style to use for the contextual help content. This is
 * specified using CSS properties. You may use any properties which are valid
 * for the HTML <code>&lt;FONT&gt;</code> tag. For example,
 * <code>contentFontStyle="color:black"</code> (which is also the default:
 * black text). (<b>Note:</b> This attribute is not the only way to specify
 * content font styles; you can just embed your desired font (and other) effects
 * in the help content string itself.)
 * </p>
 * </li>
 * </ul>
 * <p>
 * As noted above, this tag is for the classic-style rendering of contextual
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
	 * Stores the value of the <code>width</code> attribute.
	 */
	protected String width;

	/**
	 * Stores the value of the <code>borderStyle</code> attribute.
	 */
	protected String borderStyle;

	/**
	 * Stores the value of the <code>titleBgStyle</code> attribute.
	 */
	protected String titleBgStyle;

	/**
	 * Stores the value of the <code>titleFontStyle</code> attribute.
	 */
	protected String titleFontStyle;

	/**
	 * Stores the value of the <code>contentBgStyle</code> attribute.
	 */
	protected String contentBgStyle;

	/**
	 * Stores the value of the <code>contentFontStyle</code> attribute.
	 */
	protected String contentFontStyle;

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
	 * Get the value of the <code>titleBgStyle</code> attribute.
	 * 
	 * @return The <code>titleBgStyle</code> attribute.
	 */
	public String getTitleBgStyle() {
		return titleBgStyle;
	}

	/**
	 * Set the value from the <code>titleBgStyle</code> attribute. Blank
	 * values are not normalized to null.
	 * 
	 * @param value
	 *            The <code>titleBgStyle</code> attribute.
	 */
	public void setTitleBgStyle(String value) {
		this.titleBgStyle = value;
	}

	/**
	 * Get the value of the <code>titleFontStyle</code> attribute.
	 * 
	 * @return The <code>titleFontStyle</code> attribute.
	 */
	public String getTitleFontStyle() {
		return titleFontStyle;
	}

	/**
	 * Set the value from the <code>titleFontStyle</code> attribute. Blank
	 * values are not normalized to null.
	 * 
	 * @param value
	 *            The <code>titleFontStyle</code> attribute.
	 */
	public void setTitleFontStyle(String value) {
		this.titleFontStyle = value;
	}

	/**
	 * Get the value of the <code>contentBgStyle</code> attribute.
	 * 
	 * @return The <code>contentBgStyle</code> attribute.
	 */
	public String getContentBgStyle() {
		return contentBgStyle;
	}

	/**
	 * Set the value from the <code>contentBgStyle</code> attribute. Blank
	 * values are not normalized to null.
	 * 
	 * @param value
	 *            The <code>contentBgStyle</code> attribute.
	 */
	public void setContentBgStyle(String value) {
		this.contentBgStyle = value;
	}

	/**
	 * Get the value of the <code>contentFontStyle</code> attribute.
	 * 
	 * @return The <code>contentFontStyle</code> attribute.
	 */
	public String getContentFontStyle() {
		return contentFontStyle;
	}

	/**
	 * Set the value from the <code>contentFontStyle</code> attribute. Blank
	 * values are not normalized to null.
	 * 
	 * @param value
	 *            The <code>contentFontStyle</code> attribute.
	 */
	public void setContentFontStyle(String value) {
		this.contentFontStyle = value;
	}

	/**
	 * Initialize the tag attributes.
	 */
	public ClassicContextualHelpParamBaseTag() {
		super();
		title = null;
		titleKey = null;
		noScriptHref = null;
		width = null;
		borderStyle = null;
		titleBgStyle = null;
		titleFontStyle = null;
		contentBgStyle = null;
		contentFontStyle = null;
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
	protected ContextualHelpProvider getContextualHelpProvider()
			throws JspException {
		String actualTitle = getTitleContent();
		if (actualTitle == null) {
			String msg = "ClassicContextualHelpParamBaseTag error: either the title or titleKey are required attributes.";
			logError(this, msg);
			throw new JspException(msg);
		}
		String actualContent = getHelpContent();
		if (actualContent == null) {
			String msg = "ClassicContextualHelpParamBaseTag error: either the content or contentKey are required attributes.";
			logError(this, msg);
			throw new JspException(msg);
		}
		ClassicContextualHelpProvider c = newClassicContextualHelpProvider();
		c.setHelpContent(actualContent);
		c.setTitleContent(actualTitle);
		c.setNoScriptHref(noScriptHref);
		c.setBorderStyle(borderStyle);
		c.setTitleBgStyle(titleBgStyle);
		c.setTitleFontStyle(titleFontStyle);
		c.setContentBgStyle(contentBgStyle);
		c.setContentFontStyle(contentFontStyle);
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
