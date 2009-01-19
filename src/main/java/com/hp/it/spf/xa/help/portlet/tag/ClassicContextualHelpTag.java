/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help.portlet.tag;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import com.hp.it.spf.xa.help.ContextualHelpProvider;
import com.hp.it.spf.xa.help.ClassicContextualHelpProvider;
import com.hp.it.spf.xa.help.tag.ClassicContextualHelpBaseTag;
import com.hp.it.spf.xa.i18n.portlet.I18nUtility;
import com.hp.websat.timber.logging.Log;

/**
 * <p>
 * A class representing the
 * <code>&lt;spf-help-portlet:classicContextualHelp&gt;</code> tag. You use
 * that tag to define for your portlet, in a single tag, a contextual-help
 * hyperlink and popup window, including the link content, and the popup
 * content.
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
 * title. This message string will be obtained from the resource bundle(s)
 * available to your portlet. One or the other attribute is required. If you
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
 * string will be obtained from the resource bundle(s) available to your
 * portlet.
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
 * provided, the <code>anchorImgKey</code> value should be a message resource
 * key for a message string containing the complete image URL itself (encoded as
 * necessary). Otherwise if provided, the <code>anchorImgKey</code> value
 * should be a base filename for a (potentially localized) bundle of image files
 * stored either in the portlet resource bundle directory, or in your portlet
 * WAR itself.
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

	/**
	 * Concrete method for getting a message from a portlet resource bundle. The
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
		PortletRequest portletRequest = (PortletRequest) pageContext
				.getRequest().getAttribute("javax.portlet.request");
		return I18nUtility.getMessage(portletRequest, key);
	}

	/**
	 * <p>
	 * Concrete method for getting a localized image URL for a portlet, given a
	 * base filename. You can put your bundle of localized images (ie the base
	 * file and its localized variants) in either the portlet resource bundle
	 * directory on the portlet server, or you can package them as static
	 * resources inside your portlet WAR. You can also put the files into a
	 * subdirectory in those locations; if you do so, include the subdirectory
	 * in your given base filename.
	 * </p>
	 * 
	 * @param baseFilename
	 *            The image base filename. For example,
	 *            <code>/images/picture.jpg</code>.
	 * @return A URL, properly built and encoded, for the best-candidate
	 *         localized version of that image for the user.
	 */
	protected String getLocalizedImageURL(String baseFilename) {
		PortletRequest portletRequest = (PortletRequest) pageContext
				.getRequest().getAttribute("javax.portlet.request");
		PortletResponse portletResponse = (PortletResponse) pageContext
				.getRequest().getAttribute("javax.portlet.response");
		return I18nUtility.getLocalizedFileURL(portletRequest, portletResponse,
				baseFilename);
	}

	/**
	 * Concrete method for constructing an empty classic contextual help
	 * provider.
	 * 
	 * @return An empty classic contextual help provider.
	 */
	protected ClassicContextualHelpProvider newClassicContextualHelpProvider() {
		PortletRequest portletRequest = (PortletRequest) pageContext
				.getRequest().getAttribute("javax.portlet.request");
		PortletResponse portletResponse = (PortletResponse) pageContext
				.getRequest().getAttribute("javax.portlet.response");
		return new com.hp.it.spf.xa.help.portlet.ClassicContextualHelpProvider(
				portletRequest, portletResponse);
	}

	/**
	 * Log a tag error into the portlet error log file.
	 * 
	 * @param obj
	 *            The object asking to log this error.
	 * @param msg
	 *            The error message.
	 */
	protected void logError(Object obj, String msg) {
		Log.logError(obj, msg);
	}

}
