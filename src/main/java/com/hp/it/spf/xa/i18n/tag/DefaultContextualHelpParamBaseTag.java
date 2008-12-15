/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.xa.i18n.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.hp.it.spf.xa.i18n.tag.MessageBaseTag;
import com.hp.it.spf.xa.help.DefaultContextualHelpProvider;

/**
 * <p>
 * An abstract base class representing a default-style contextual help parameter
 * tag such as the portlet framework's
 * <code>&lt;spf-i18n-portlet:contextualHelpParam&gt;</code> tag and the
 * portal framework's <code>&lt;spf-i18n-portal:contextualHelpParam&gt;</code>
 * tag. You use these tags to define parameters for surrounding message tags for
 * messages containing
 * <code>&lt;Contextual_Help&gt;...&lt;/Contextual_Help&gt;</code> tokens.
 * These message tags include the portlet framework's
 * <code>&lt;spf-i18n-portlet:message&gt;</code> tag and the portal
 * framework's <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag (see). You
 * place the contextual help parameter tag inside the body of the surrounding
 * message tag, in order to pass parameters in that order into the message.
 * </p>
 * <p>
 * The style of contextual help rendered by this tag is the SPF-provided default
 * style. For both portal and portlet frameworks, the default-style
 * <code>&lt;contextualHelpParam&gt;</code> tag has 3 attributes:
 * </p>
 * <ul>
 * <li>
 * <p>
 * The <code>titleKey="<i>title-key</i>"</code> attribute gives a message
 * resource key for a message string containing the string to use for the
 * contextual help title. This message string will be obtained from the resource
 * bundle(s) available to your portlet or portal component. This is a required
 * attribute.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>contentKey="<i>content-key</i>"</code> attribute gives a
 * message resource key for a message string to be used for the help content
 * itself. This message string will likewise be looked-up in your resource
 * bundle(s) by this class. This is a required attribute.
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
public abstract class DefaultContextualHelpParamBaseTag extends TagSupport {
	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	private String titleKey;

	private String noScriptHref;

	private String contentKey;

	/**
	 * Get the value of the <code>titleKey</code> attribute.
	 * 
	 * @return The <code>titleKey</code> attribute.
	 */
	public String getTitleKey() {
		return titleKey;
	}

	/**
	 * Set the value from the <code>titleKey</code> attribute.
	 * 
	 * @param value
	 *            The <code>titleKey</code> attribute.
	 */
	public void setTitleKey(String value) {
		this.titleKey = value;
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
		this.contentKey = value;
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
		this.noScriptHref = value;
	}

	/**
	 * Initialize the tag attributes.
	 */
	public DefaultContextualHelpParamBaseTag() {
		titleKey = null;
		noScriptHref = null;
		contentKey = null;
	}

	/**
	 * Do the tag processing. An error is thrown if the tag discovers it is not
	 * contained inside the body of a surrounding message tag (any subclass of
	 * MessageBaseTag).
	 * 
	 * @return int int
	 * @throws JspException
	 */
	public int doEndTag() throws JspException {
		javax.servlet.jsp.tagext.Tag parent = getParent();
		if (parent == null) {
			String msg = "SPF contextual help param tag error: requires surrounding message tag.";
			logError(this, msg);
			throw new JspException(msg);
		}
		if (!(parent instanceof MessageBaseTag)) {
			String msg = "SPF contextual help param tag error: requires surrounding message tag.";
			logError(this, msg);
			throw new JspException(msg);
		}
		if (titleKey == null || contentKey == null) {
			String msg = "SPF contextual help param tag error: both the titleKey and contentKey are required attributes.";
			logError(this, msg);
			throw new JspException(msg);
		}
		String title = getTitle();
		String content = getContent();
		DefaultContextualHelpProvider c = new DefaultContextualHelpProvider();
		c.setHelpContent(content);
		c.setTitleContent(title);
		c.setNoScriptHref(noScriptHref);
		MessageBaseTag messageTag = (MessageBaseTag) parent;
		messageTag.addCParam(c);
		return super.doEndTag();
	}

	/**
	 * Abstract method for getting the contextual help title string from a
	 * message resource. Different action for portal and portlet, so this is an
	 * abstract method.
	 * 
	 * @return The help title message.
	 */
	public abstract String getTitle();

	/**
	 * Abstract method for getting the contextual help content string from a
	 * message resource. Different action for portal and portlet, so this is an
	 * abstract method.
	 * 
	 * @return The help content message.
	 */
	public abstract String getContent();

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

}
