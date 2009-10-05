/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help.portal.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import com.hp.it.spf.xa.help.ContextualHelpProvider;
import com.hp.it.spf.xa.help.ClassicContextualHelpProvider;
import com.hp.it.spf.xa.help.tag.ClassicContextualHelpBaseTag;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * A class representing the
 * <code>&lt;spf-help-portal:classicContextualHelp&gt;</code> tag. You use
 * that tag to define, in a single tag, a contextual-help hyperlink and popup
 * window, including the link content, and the popup content.
 * </p>
 * <p>
 * The style of contextual help rendered by this tag is the SPF-provided
 * "classic" style. It has the following attributes (most of them implemented in
 * the superclass hierarchy):
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
 * title. This message string will be obtained from the resource bundle(s) in
 * your current portal component. One or the other attribute is required. If you
 * specify both, then <code>title</code> will take precedence.
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
 * </ul>
 * <p>
 * In addition, there are the following attributes, at least one of which is
 * required:
 * </p>
 * <ul>
 * <li>
 * <p>
 * The <code>anchor="<i>anchor-text</i>"</code> and
 * <code>anchorKey="<i>anchor-text-key</i>"</code> attributes are
 * alternative ways of providing some arbitrary text content for the help
 * hyperlink. If you provide the <code>anchor</code> attribute, then its value
 * is used as the hyperlink content. Alternatively, if you provide the
 * <code>anchorKey</code> attribute, then its value is used as a message
 * resource key for a message string containing the link content. This message
 * string will be obtained from the resource bundle(s) available to your portal
 * component.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>anchorImg="<i>anchor-img-src</i>"</code> and
 * <code>anchorImgKey="<i>anchor-img-src-key</i>"</code> attributes are
 * alternative ways of indicating that an image should be the content of the
 * help hyperlink, and providing the <code>src</code> URL for the image. As
 * with the above keys, <code>anchorImg</code> takes precedence over
 * <code>anchorImgKey</code>. If provided, the <code>anchorImg</code> value
 * should be the complete image URL itself (encoded as necessary). Otherwise if
 * provided, the <code>anchorImgKey</code> value should be a base filename for
 * a (potentially localized) bundle of secondary support files which are the
 * images.
 * </p>
 * </li>
 * </ul>
 * <p>
 * Finally, there are the following optional attributes:
 * </p>
 * <ul>
 * <li>
 * <p>
 * If an image was specified for the link content, then an <code>alt</code>
 * text string for that image can be specified with
 * <code>anchorImgAlt="<i>anchor-img-alt-text</i>"</code> or
 * <code>anchorImgAltKey="<i>anchor-img-alt-text-key</i>"</code>. The
 * former takes priority over the latter. These attributes are ignored if you
 * provide them but an image was not indicated.
 * </p>
 * </li>
 * <li>
 * <p>
 * <code>escape="<i>true-or-false</i>"</code> is an optional switch
 * (default: <code>"false"</code>) which if set to <code>"true"</code> will
 * convert any HTML special characters found in the any of the above attributes
 * into their equivalent HTML character entities.
 * </p>
 * </li>
 * <li>
 * <p>
 * <code>filterSpan="<i>true-or-false</i>"</code> is an optional switch
 * (default: <code>"false"</code>) which if set to <code>"true"</code> will
 * remove any <code>&lt;SPAN&gt;</code> tags found in the any of the above
 * attributes. (Vignette sometimes injects <code>&lt;SPAN&gt;</code> tags into
 * messages returned from the resource bundle, and in some contexts that might
 * be problematic, so this is a way around that.)
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>noScriptHref="<i>url</i>"</code> attribute gives an alternative
 * URL to offer the browser to use in case JavaScript is disabled. Because the
 * classic-style contextual help requires JavaScript, it will not work if
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
 * tag for it. First, implement a ContextualHelpProvider concrete subclass for
 * that custom style. Then implement a tag class for it, like this one. Have the
 * tag construct the appropriate kind of ContextualHelpProvider subclass
 * corresponding to that custom style, then return it so the parent can use it
 * in the getHTML method.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ClassicContextualHelpTag extends ClassicContextualHelpBaseTag {

	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	private final LogWrapper LOG = new LogWrapper(
			ClassicContextualHelpTag.class);

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
	public ClassicContextualHelpTag() {
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
	 * escaping/filtering will happen later, in the concrete getHTML method. If
	 * the message is not found, the key is returned instead.
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
	 * Put your bundle of base file and localized files into the portal
	 * component as secondary support files.
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
	 * This method gets the ClassicContextualHelpProvider from the superclass
	 * and then resets the title and help content if filter-span is required,
	 * before returning it. This override of the parent method is necessary in
	 * order to support the filter-span functionality.
	 */
	protected ContextualHelpProvider getContextualHelpProvider()
			throws JspException {
		String actualLink = getLinkContent();
		ClassicContextualHelpProvider c = (ClassicContextualHelpProvider) super
				.getContextualHelpProvider(actualLink);
		if (isFilterSpanEnabled()) {
			String actualTitle = getTitleContent();
			String actualContent = getHelpContent();
			c.setHelpContent(I18nUtility.filterSpan(actualContent));
			c.setTitleContent(I18nUtility.filterSpan(actualTitle));
			c.setLinkContent(I18nUtility.filterSpan(actualLink));
		}
		return c;
	}

	/**
	 * Concrete method for constructing an empty classic contextual help
	 * provider.
	 * 
	 * @return An empty classic contextual help provider.
	 */
	protected ClassicContextualHelpProvider newClassicContextualHelpProvider() {
		PortalContext portalContext = (PortalContext) pageContext.getRequest()
				.getAttribute("portalContext");
		return new com.hp.it.spf.xa.help.portal.ClassicContextualHelpProvider(
				portalContext);
	}

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
