/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.tag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.hp.it.spf.xa.help.ContextualHelpProvider;

/**
 * <p>
 * An abstract base class representing a message tag such as the portlet
 * framework's <code>&lt;spf-i18n-portlet:message&gt;</code> tag and the
 * portal framework's <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag. You
 * use these tags to express message resources. Inside the body of these tags,
 * you can define string and contextual help parameter values for the tags,
 * using the <code>&lt;spf-i18n-portlet:param&gt;</code> and
 * <code>&lt;spf-i18n-portlet:contextualHelpParam&gt;</code> tags in the
 * portlet framework, and the similarly-named tags (in
 * <code>spf-i18n-portal</code>) for the portal framework.
 * </p>
 * <p>
 * Every kind of message tag has at least 3 attributes, which are represented in
 * this base class. <code>key="<i>message-key</i>"</code> is the key to
 * lookup in the message resource bundle, and
 * <code>defaultValue="<i>default-value</i>"</code> is an optional default
 * to assign if the key cannot be found (if not provided, then the key itself
 * will be used as a default). <code>escape="<i>escape-html</i>"</code> is
 * an optional switch (default: <code>"false"</code>) which if set to
 * <code>"true"</code> will convert any HTML special characters found in the
 * message string or its parameters into their equivalent HTML character
 * entities.
 * </p>
 * 
 * @author <link href="kuang.cheng@hp.com"> Cheng Kuang </link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.i18n.portal.tag.MessageTag</code><br>
 *      <code>com.hp.it.spf.xa.i18n.portlet.tag.MessageTag</code>
 */
public abstract class MessageBaseTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	/**
	 * The <code>key</code> attribute from the tag.
	 */
	protected String key;

	/**
	 * The <code>defaultValue</code> attribute from the tag.
	 */
	protected String defaultValue;

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
	 * The list of string parameters from any string parameter tags enclosed in
	 * the message tag body.
	 */
	protected List params;

	/**
	 * The list of contextual help providers from any contextual help parameter
	 * tags enclosed in the message tag body.
	 */
	protected List cProviders;

	/**
	 * Initialize the tag values.
	 */
	public MessageBaseTag() {
		key = null;
		defaultValue = null;
		cProviders = null;
		params = null;
		escape = null;
		escapeEnabled = false;
	}

	/**
	 * Get the value of the <code>defaultValue</code> attribute. If there was
	 * no default value defined, return the value of the <code>key</code>
	 * attribute.
	 * 
	 * @return The default value.
	 */
	public String getDefaultValue() {
		return defaultValue != null ? defaultValue : key;
	}

	/**
	 * Set the default value from the <code>defaultValue</code> attribute,
	 * normalizing blank to null.
	 * 
	 * @param defaultValue
	 *            Value of the <code>defaultValue</code> attribute.
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = normalize(defaultValue);
	}

	/**
	 * Set the key from the <code>key</code> attribute, normalizing blank to
	 * null.
	 * 
	 * @param key
	 *            Value of the <code>key</code> attribute.
	 */
	public void setKey(String key) {
		this.key = normalize(key);
	}

	/**
	 * Get the value of the <code>key</code> attribute.
	 * 
	 * @return Value of the <code>key</code> attribute.
	 */
	public String getKey() {
		return key;
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
	 * Empty processing step executed at closure of tag body.
	 * 
	 * @throws JspTagException
	 * @return int int
	 */
	public int doAfterBody() throws JspTagException {
		return SKIP_BODY;
	}

	/**
	 * Add string parameters to the list, for later interpolation into the
	 * message. String parameters come from any string parameter tags contained
	 * inside the message tag body (see {@link ParamBaseTag}).
	 * 
	 * @param o
	 *            A string parameter.
	 */
	public void addParam(Object o) {
		if (params == null) {
			params = new ArrayList();
		}
		params.add(o);
	}

	/**
	 * Add contextual help parameters to the list, for later interpolation into
	 * the message. These come from any contextual help parameter tags contained
	 * inside the message tag body (see {@link ContextualHelpParamBaseTag}).
	 * 
	 * @param c
	 *            A parameter set for one instance of contextual help.
	 */
	public void addContextualHelpProvider(ContextualHelpProvider c) {
		if (cProviders == null) {
			cProviders = new ArrayList();
		}
		cProviders.add(c);
	}

	/**
	 * Clear all buffered parameters.
	 */
	protected void clearParams() {
		key = null;
		defaultValue = null;
		escape = null;
		escapeEnabled = false;
		if (params != null) {
			params.clear();
		}
		if (cProviders != null) {
			Iterator itr = cProviders.iterator();
			while (itr.hasNext()) {
				Object obj = itr.next();
				if (obj != null) {
					if (obj instanceof List) {
						((List) obj).clear();
					} else if (obj instanceof Map) {
						((Map) obj).clear();
					}
				}
			}
			cProviders.clear();
		}
	}

	/**
	 * Release resources after processing this tag.
	 */
	public void release() {
		clearParams();
		super.release();
	}

	/**
	 * Perform tag processing.
	 * 
	 * @return int
	 * @throws JspException
	 */
	public int doEndTag() throws JspException {
		if (key == null) {
			String msg = "MessageBaseTag error: key is a required attribute.";
			logError(this, msg);
			throw new JspException(msg);
		}
		if (defaultValue == null) {
			setDefaultValue(key);
		}
		JspWriter out = pageContext.getOut();
		try {
			String value = getMessage();
			if (value == null) {
				value = defaultValue;
			}
			out.print(value);
		} catch (Exception e) {
			logError(this, "MessageBaseTag error: " + e.getMessage());
			JspException jspE = new JspException(e);
			throw jspE;
		}
		return EVAL_PAGE;
	}

	/**
	 * Abstract method for getting the message string. Different action for
	 * portal and portlet, so this is an abstract method.
	 * 
	 * @return The message string.
	 */
	public abstract String getMessage();

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
