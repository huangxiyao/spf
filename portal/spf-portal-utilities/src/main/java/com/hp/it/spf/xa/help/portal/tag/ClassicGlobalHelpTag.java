/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.xa.help.portal.tag;

import javax.servlet.jsp.JspException;

import com.hp.it.spf.xa.help.portal.ClassicGlobalHelpProvider;
import com.hp.it.spf.xa.help.portal.GlobalHelpProvider;
import com.hp.it.spf.xa.help.portal.tag.GlobalHelpBaseTag;
import com.hp.it.spf.xa.i18n.portal.tag.LocalizedFileURLTag;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * The concrete tag class for the "classic"-style portal global help tag (<code>&lt;spf-help-portal:classicGlobalHelp&gt;</code>).
 * <p>
 * The style of global help rendered by this tag is the SPF-provided "classic"
 * style, which opens a child window. It has the following attributes (most of
 * them implemented in the superclass hierarchy), at least one of which is
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
 * provide them but not an image.
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
 * <code>fragment="<i>fragment-name</i>"</code> can optionally be used to
 * specify a fragment name in the global help page. If that attribute is
 * provided, the global help hyperlink expressed through the getHTML method will
 * include an anchor to that fragment (otherwise it will just point to the top
 * of the global help page).
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>windowFeatures="<i>window-features</i>"</code> attribute gives
 * the set of JavaScript window properties to use for the global help child
 * window. This is specified using any valid JavaScript <code>window.open</code>
 * features name/value pairs properties. For example,
 * <code>windowFeatures="height=610,width=410,menubar=no,status=no,toolbar=no,resizable=yes"</code>
 * (which is also the default: a 610-by-410 pixel child window with no tools or
 * controls).
 * </p>
 * </li>
 * </ul>
 * <p>
 * As noted above, this tag is for the classic-style rendering of global help.
 * If you would like a custom style, you must implement your own custom tag for
 * it. First, implement a GlobalHelpProvider concrete subclass for that custom
 * style. Then implement a tag class for it, like this one. Have the tag
 * construct the appropriate kind of GlobalHelpProvider subclass corresponding
 * to that custom style, then return it so the parent can use it in the getHTML
 * method.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ClassicGlobalHelpTag extends GlobalHelpBaseTag {
	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The <code>fragment</code> attribute from the tag.
	 */
	protected String fragment;

	/**
	 * The <code>windowFeatures</code> attribute from the tag.
	 */
	protected String windowFeatures;

	/**
	 * Set the value from the <code>fragment</code> attribute, normalizing
	 * blank to null.
	 * 
	 * @param value
	 *            Value of the <code>fragment</code> attribute.
	 */
	public void setFragment(String value) {
		this.fragment = normalize(value);
	}

	/**
	 * Get the value of the <code>fragment</code> attribute.
	 * 
	 * @return Value of the <code>fragment</code> attribute.
	 */
	public String getFragment() {
		return fragment;
	}

	/**
	 * Set the value from the <code>windowFeatures</code> attribute. Blank is
	 * not normalized to null.
	 * 
	 * @param value
	 *            Value of the <code>windowFeatures</code> attribute.
	 */
	public void setWindowFeatures(String value) {
		this.windowFeatures = value;
	}

	/**
	 * Get the value of the <code>windowFeatures</code> attribute.
	 * 
	 * @return Value of the <code>windowFeatures</code> attribute.
	 */
	public String getWindowFeatures() {
		return windowFeatures;
	}

	/**
	 * Initialize the tag attributes.
	 */
	public ClassicGlobalHelpTag() {
		super();
		fragment = null;
		windowFeatures = null;
	}

	/**
	 * Releases tag resources.
	 */
	public void release() {
		super.release();
		fragment = null;
		windowFeatures = null;
	}

	/**
	 * Creates and sets attributes corresponding to the tag attributes in a
	 * ClassicGlobalHelpProvider, then returns it to the superclass to be
	 * expressed in the getHTML method.
	 * 
	 * @param linkContent
	 *            The content to be embedded inside the hyperlink.
	 * @return The ClassicGlobalHelpProvider corresponding to the attributes of
	 *         this tag.
	 * @throws JspException
	 */
	protected GlobalHelpProvider getGlobalHelpProvider(String linkContent)
			throws JspException {
		PortalContext portalContext = (PortalContext) pageContext.getRequest()
				.getAttribute("portalContext");
		// Get the link content (exception if none was defined or found)
		if (linkContent == null) {
			String msg = "ClassicGlobalHelpTag error: no link content was found.";
			logError(this, msg);
			throw new JspException(msg);
		}
		// Make and express a classic-style global help hyperlink using
		// ClassicGlobalHelpProvider.
		ClassicGlobalHelpProvider g = new ClassicGlobalHelpProvider(
				portalContext);
		g.setLinkContent(linkContent);
		g.setFragment(fragment);
		g.setWindowFeatures(windowFeatures);
		return g;
	}

}
