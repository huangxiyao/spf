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

/**
 * <p>
 * An abstract base class representing a string parameter tag such as the
 * portlet framework's <code>&lt;spf-i18n-portlet:param&gt;</code> tag and the
 * portal framework's <code>&lt;spf-i18n-portal:param&gt;</code> tag. You use
 * these tags to define string parameter values for surrounding message tags,
 * such as the portlet frameworks' <code>&lt;spf-i18n-portlet:message&gt;</code>
 * tag and the portal framework's <code>&lt;spf-i18n-portal:i18nValue&gt;</code>
 * tag (see). You place the string parameter tag inside the body of the
 * surrounding message tag, in order, to pass string parameters in that order
 * into the message.
 * </p>
 * <p>
 * <code>&lt;param&gt;</code> tag has one attribute,
 * <code>value="<i>string</i>"</code> where <i>string</i> is the value of a
 * parameter to be substituted.  It is a required attribute.
 * </p>
 * 
 * @author <link href="kuang.cheng@hp.com">Cheng Kuang </link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class ParamBaseTag extends TagSupport {

	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	protected String value;

	/**
	 * Get the value of the <code>value</code> attribute.
	 * 
	 * @return The <code>value</code> attribute.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the value from the <code>value</code> attribute.
	 * 
	 * @param value
	 *            The <code>value</code> attribute.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Initialize tag attribute values.
	 */
	public ParamBaseTag() {
		value = null;
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
			String msg = "SPF string param tag error: requires surrounding message tag.";
			logError(this, msg);
			throw new JspException(msg);
		}
		if (!(parent instanceof MessageBaseTag)) {
			String msg = "SPF string param tag error: requires surrounding message tag.";
			logError(this, msg);
			throw new JspException(msg);
		}
		if (value == null) {
			String msg = "SPF string param tag error: value is a required attribute.";
			logError(this, msg);
			throw new JspException(msg);
		}
		MessageBaseTag messageTag = (MessageBaseTag) parent;
		messageTag.addParam(value);
		return super.doEndTag();
	}

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
