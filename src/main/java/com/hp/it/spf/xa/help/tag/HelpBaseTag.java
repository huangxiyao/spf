/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.xa.help.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.hp.it.spf.xa.help.ContextualHelpProvider;
import com.hp.it.spf.xa.i18n.tag.MessageBaseTag;
import com.hp.it.spf.xa.misc.Utils;

/**
 * <p>
 * An abstract base class for all help tags, including both contextual and
 * global help tags, such as the "classic"-style portal global help tag (<code>&lt;spf-help-portal:classicGlobalHelp&gt;</code>)
 * and the "classic"-style portlet contextual help tag (<code>&lt;spf-help-portlet:classicContextualHelp&gt;</code>).
 * If you create another style of rendering either kind of help, and would like
 * to render that help with a custom tag, then you should develop your custom
 * help tag class by subclassing from this one.
 * </p>
 * <p>
 * All help includes a help hyperlink. The content of the help hyperlink can be
 * specified through a proper mix of the following attributes:
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
 * string will be obtained from the resource bundle(s) available to your portlet
 * or portal component.
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
 * a (potentially localized) image file (See the concrete subclasses for how
 * this works.)
 * </p>
 * </li>
 * </ul>
 * <p>
 * At least one of the above attributes must be specified. <code>anchor</code>
 * takes priority, followed by <code>anchorKey</code>, <code>anchorImg</code>,
 * and lastly <code>anchorImgKey</code>.
 * </p>
 * <p>
 * In addition, the following attributes are supported:
 * </p>
 * <ul>
 * <li>
 * <p>
 * If an image was specified, then an <code>alt</code> text string for that
 * image can be specified with
 * <code>anchorImgAlt="<i>anchor-img-alt-text</i>"</code> or
 * <code>anchorImgAltKey="<i>anchor-img-alt-text-key</i>"</code>. The
 * former takes priority over the latter.
 * </p>
 * </li>
 * <li>
 * <p>
 * Finally, <code>escape="<i>escape-html</i>"</code> is an optional switch
 * (default: <code>"false"</code>) which if set to <code>"true"</code> will
 * convert any HTML special characters found in the any of the above attributes
 * into their equivalent HTML character entities.
 * </p>
 * </li>
 * </ul>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class HelpBaseTag extends TagSupport {
	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Stores the value of the <code>anchor</code> attribute.
	 */
	protected String anchor;

	/**
	 * Stores the value of the <code>anchorKey</code> attribute.
	 */
	protected String anchorKey;

	/**
	 * Stores the value of the <code>anchorImg</code> attribute.
	 */
	protected String anchorImg;

	/**
	 * Stores the value of the <code>anchorImgKey</code> attribute.
	 */
	protected String anchorImgKey;

	/**
	 * Stores the value of the <code>anchorImgAlt</code> attribute.
	 */
	protected String anchorImgAlt;

	/**
	 * Stores the value of the <code>anchorImgAltKey</code> attribute.
	 */
	protected String anchorImgAltKey;

	/**
	 * The <code>escape</code> attribute from the tag.
	 */
	protected String escape;

	/**
	 * A boolean switch representing the <code>escape</code> attribute in
	 * boolean form.
	 */
	protected boolean escapeEnabled;

	/**
	 * Set the value from the <code>anchor</code> attribute.
	 * 
	 * @param value
	 *            The <code>anchor</code> attribute.
	 */
	public void setAnchor(String value) {
		this.anchor = normalize(value);
	}

	/**
	 * Get the value of the <code>anchor</code> attribute.
	 * 
	 * @return The <code>anchor</code> attribute.
	 */
	public String getAnchor() {
		return anchor;
	}

	/**
	 * Set the value from the <code>anchorKey</code> attribute.
	 * 
	 * @param value
	 *            The <code>anchorKey</code> attribute.
	 */
	public void setAnchorKey(String value) {
		this.anchorKey = normalize(value);
	}

	/**
	 * Get the value of the <code>anchorKey</code> attribute.
	 * 
	 * @return The <code>anchorKey</code> attribute.
	 */
	public String getAnchorKey() {
		return anchorKey;
	}

	/**
	 * Set the value from the <code>anchorImg</code> attribute.
	 * 
	 * @param value
	 *            The <code>anchorImg</code> attribute.
	 */
	public void setAnchorImg(String value) {
		this.anchorImg = normalize(value);
	}

	/**
	 * Get the value of the <code>anchorImg</code> attribute.
	 * 
	 * @return The <code>anchorImg</code> attribute.
	 */
	public String getAnchorImg() {
		return anchorImg;
	}

	/**
	 * Set the value from the <code>anchorImgKey</code> attribute.
	 * 
	 * @param value
	 *            The <code>anchorImgKey</code> attribute.
	 */
	public void setAnchorImgKey(String value) {
		this.anchorImgKey = normalize(value);
	}

	/**
	 * Get the value of the <code>anchorImgKey</code> attribute.
	 * 
	 * @return The <code>anchorImgKey</code> attribute.
	 */
	public String getAnchorImgKey() {
		return anchorImgKey;
	}

	/**
	 * Set the value from the <code>anchorImgAlt</code> attribute.
	 * 
	 * @param value
	 *            The <code>anchorImgAlt</code> attribute.
	 */
	public void setAnchorImgAlt(String value) {
		this.anchorImgAlt = normalize(value);
	}

	/**
	 * Get the value of the <code>anchorImgAlt</code> attribute.
	 * 
	 * @return The <code>anchorImgAlt</code> attribute.
	 */
	public String getAnchorImgAlt() {
		return anchorImgAlt;
	}

	/**
	 * Set the value from the <code>anchorImgAltKey</code> attribute.
	 * 
	 * @param value
	 *            The <code>anchorImgAltKey</code> attribute.
	 */
	public void setAnchorImgAltKey(String value) {
		this.anchorImgAltKey = normalize(value);
	}

	/**
	 * Get the value of the <code>anchorImgAltKey</code> attribute.
	 * 
	 * @return The <code>anchorImgAltKey</code> attribute.
	 */
	public String getAnchorImgAltKey() {
		return anchorImgAltKey;
	}

	/**
	 * Set the escape-HTML switch from the <code>escape</code> attribute,
	 * normalizing blank to null.
	 * 
	 * @param value
	 *            Value of the <code>escape</code> attribute.
	 */
	public void setEscape(String value) {
		this.escape = normalize(value);
		this.escapeEnabled = false;
		if (value != null) {
			value = value.trim();
			if ("true".equalsIgnoreCase(value)) {
				this.escapeEnabled = true;
			}
		}
	}

	/**
	 * Get the value of the <code>escape</code> attribute.
	 * 
	 * @return Value of the <code>escape</code> attribute.
	 */
	public String getEscape() {
		return escape;
	}

	/**
	 * Returns true if <code>escape="true"</code> (case-insensitive) and false
	 * otherwise.
	 * 
	 * @return Whether escape-HTML behavior is enabled.
	 */
	public boolean isEscapeEnabled() {
		return escapeEnabled;
	}

	/**
	 * Initialize the tag attributes.
	 */
	public HelpBaseTag() {
		super();
		escape = null;
		escapeEnabled = false;
		anchorKey = null;
		anchor = null;
		anchorImgKey = null;
		anchorImg = null;
		anchorImgAltKey = null;
		anchorImgAlt = null;
	}

	/**
	 * Releases tag resources.
	 */
	public void release() {

		super.release();
		anchorKey = null;
		anchor = null;
		anchorImgKey = null;
		anchorImg = null;
		anchorImgAltKey = null;
		anchorImgAlt = null;
		escape = null;
		escapeEnabled = false;
	}

	/**
	 * Do the tag processing. An error is thrown if the tag is missing a
	 * required attribute. Otherwise, it gets the help hyperlink HTML from the
	 * concrete subclass and writes it out.
	 * 
	 * @return int
	 * @throws JspException
	 */
	public int doEndTag() throws JspException {
		if (anchor == null && anchorKey == null && anchorImg == null
				&& anchorImgKey == null) {
			String msg = "HelpBaseTag error: one of the following attributes is required: anchor, anchorKey, anchorImg, or anchorImgKey.";
			logError(this, msg);
			throw new JspException(msg);
		}
		JspWriter out = pageContext.getOut();
		try {
			String value = getHTML(getLinkContent());
			if (value == null) {
				value = "";
			}
			out.print(value);
		} catch (Exception e) {
			logError(this, "HelpBaseTag error: " + e.getMessage());
			JspException jspE = new JspException(e);
			throw jspE;
		}
		return EVAL_PAGE;
	}

	/**
	 * Assembles the content of the global help hyperlink (ie the anchor content
	 * itself) from the various tag attributes, and returns it. This content is
	 * not yet escaped; the concrete subclass will do that when its getHTML
	 * method is called.
	 */
	protected String getLinkContent() {

		String linkContent = null;
		if (anchor != null) {
			// If anchor attribute was provided, take that as the link content.
			linkContent = anchor;
		} else if (anchorKey != null) {
			// Else if anchorKey attribute was provided, take that message as
			// the link content.
			linkContent = normalize(getMessage(anchorKey));
		} else if (anchorImg != null || anchorImgKey != null) {
			// Else if an image was indicated, build an <img> tag for the link
			// content.
			String imgUrl = null;
			if (anchorImg != null) {
				// If an anchorImg attribute was provided, take that value as
				// the image URL.
				imgUrl = anchorImg;
			} else {
				// If an anchorImgKey attribute was provided, take that value as
				// a base filename of an external image, and get a localized
				// file URL for it and use that as the image URL.
				imgUrl = normalize(getLocalizedImageURL(anchorImgKey));
			}
			String imgAlt = null;
			if (anchorImgAlt != null) {
				// If an anchorImgAlt attribute was provided, take that value as
				// the image alt.
				imgAlt = anchorImgAlt;
			} else if (anchorImgAltKey != null) {
				// If an anchorImgAltKey attribute was provided, take that
				// message as the image alt.
				imgAlt = normalize(getMessage(anchorImgAltKey));
			}
			linkContent = "<img src=\"" + imgUrl + "\" ";
			if (imgAlt != null) {
				linkContent += "alt=\"" + imgAlt + "\" ";
			}
			linkContent += "style=\"cursor:pointer\" border=\"0\">";
		}
		return linkContent;
	}

	/**
	 * Abstract method for getting the tag markup (ie the help hyperlink
	 * markup). The action is different depending on the type of help (global or
	 * contextual), so this method is abstract. This method should apply any
	 * needed escaping/filtering to the markup.
	 * 
	 * @param linkContent
	 *            The markup to put inside the hyperlink.
	 * @return The total help hyperlink markup.
	 * @throws JspException
	 */
	protected abstract String getHTML(String linkContent) throws JspException;

	/**
	 * Abstract method for getting a message from a resource bundle. Different
	 * action for portal and portlet so this is an abstract method. The message
	 * should be returned unescaped and unfiltered; any necessary
	 * escaping/filtering will happen later, in the getHTML method.
	 * 
	 * @param key
	 *            The message key.
	 * @return The message value (unescaped and unfiltered), localized for the
	 *         user.
	 */
	protected abstract String getMessage(String key);

	/**
	 * Abstract method for getting a localized image URL. Different action for
	 * portal and portlet so this is an abstract method.
	 * 
	 * @param baseFilename
	 *            The image basefilename (eg <code>picture.jpg</code>).
	 * @return A URL, properly built and encoded, for the best-candidate
	 *         localized version of that image for the user.
	 */
	protected abstract String getLocalizedImageURL(String baseFilename);

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
