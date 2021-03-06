/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.portal.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import com.hp.it.spf.xa.help.ContextualHelpProvider;
import com.hp.it.spf.xa.help.ClassicContextualHelpProvider;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.i18n.tag.ClassicContextualHelpParamBaseTag;
import com.hp.it.spf.xa.i18n.tag.MessageBaseTag;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * A class representing the
 * <code>&lt;spf-i18n-portal:i18nClassicContextualHelpParam&gt;</code> tag. You use
 * that tag to define parameters for surrounding
 * <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tags which contain
 * <code>&lt;Contextual_Help&gt;...&lt;/Contextual_Help&gt;</code> tokens. You
 * place the <code>&lt;spf-i18n-portal:i18nClassicContextualHelpParam&gt;</code> tag
 * inside the body of the surrounding
 * <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag, in order to pass
 * contextual-help parameters (in that order) into the message.
 * </p>
 * <p>
 * The style of contextual help rendered by this tag is the SPF-provided
 * "classic" style. It has 3 attributes:
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
 * tag for it. Like the above tags, your custom tag would be used inside a
 * message tag body. Implement a ContextualHelpProvider concrete subclass for
 * that custom style. Then implement a tag class for it, like this one. Have the
 * tag construct the appropriate kind of ContextualHelpProvider subclass
 * corresponding to that custom style, and have the tag add it into the parent
 * message tag's list.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ClassicContextualHelpParamTag extends
		ClassicContextualHelpParamBaseTag {

	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	private final LogWrapper LOG = new LogWrapper(ClassicContextualHelpParamTag.class);

	/**
	 * <p>
	 * Get a string from a message resource using the
	 * current locale. The message resource bundle files for the current portal
	 * component are the ones searched. If the message is not found, then
	 * the key is returned instead.
	 * </p>
	 * <p>
	 * <b>Note:</b> The following functionality is currently disabled and the
	 * string will be returned "raw" (but this documentation has been left
	 * in case we ever re-enable the functionality):
	 * </p>
	 * <p>
	 * In deciding whether to escape HTML in the title string, this method
	 * refers to the parent tag (which should be a
	 * <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag indicating one way
	 * or the other in its <code>escape="<i>true-or-false</i>"</code>
	 * attribute).
	 * </p>
	 * <p>
	 * Likewise, in deciding whether to filter HTML <code>&lt;SPAN&gt;</code>
	 * markup out of the title string, which Vignette may have put there
	 * automatically, this method refers to the parent tag's
	 * <code>filterSpan="<i>true-or-false</i>"</code> attribute.
	 * </p>
	 * 
	 * @param key The message key.
	 * @return The message value.
	 */
	protected String getMessage(String key) {
		PortalContext portalContext = (PortalContext) pageContext.getRequest()
				.getAttribute("portalContext");
		boolean escape = false;
		boolean filterSpan = false;
/*
 * Disabled for now - DSJ 20090107
 * 
		Tag parent = getParent();
		if (parent != null && parent instanceof MessageBaseTag) {
			MessageBaseTag messageTag = (MessageBaseTag) parent;
			escape = messageTag.isEscapeEnabled();
		}
		if (parent != null && parent instanceof MessageTag) {
			MessageTag messageTag = (MessageTag) parent;
			filterSpan = messageTag.isFilterSpanEnabled();
		}
*/
		return I18nUtility.getValue(key, null, portalContext,
				null, null, null, escape, filterSpan);
	}

	/**
	 * This method gets the ClassicContextualHelpProvider from the superclass
	 * and then resets the title and help content if filter-span is required, 
	 * before returning it.  This override of the parent method is necessary
	 * in order to support the filter-span functionality.
	 */
	protected ContextualHelpProvider getContextualHelpProvider()
		throws JspException {
		ClassicContextualHelpProvider c = (ClassicContextualHelpProvider) super.getContextualHelpProvider();
		Tag parent = getParent();
		if (parent != null && parent instanceof MessageTag) {
			MessageTag messageTag = (MessageTag) parent;
			if (messageTag.isFilterSpanEnabled()) {
				String actualTitle = getTitleContent();
				String actualContent = getHelpContent();
				c.setHelpContent(I18nUtility.filterSpan(actualContent));
				c.setTitleContent(I18nUtility.filterSpan(actualTitle));
			}
		}
		return c;
	}
	
	/**
	 * Concrete method for constructing an empty classic contextual help provider.
	 * 
	 * @return An empty classic contextual help provider.
	 */
	protected ClassicContextualHelpProvider newClassicContextualHelpProvider() {
		PortalContext portalContext = (PortalContext) pageContext.getRequest()
		.getAttribute("portalContext");
		return new com.hp.it.spf.xa.help.portal.ClassicContextualHelpProvider(portalContext);
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
