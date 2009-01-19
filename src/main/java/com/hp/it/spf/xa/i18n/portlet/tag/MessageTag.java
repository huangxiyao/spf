/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.portlet.tag;

import javax.portlet.PortletRequest;

import com.hp.it.spf.xa.help.ContextualHelpProvider;
import com.hp.it.spf.xa.i18n.portlet.I18nUtility;
import com.hp.it.spf.xa.i18n.tag.MessageBaseTag;
import com.hp.websat.timber.logging.Log;

/**
 * <p>
 * Tag class for the
 * <code>&lt;spf-i18n-portlet:message key="<i>message-key</i>" defaultValue="<i>default-value</i>" escape="<i>boolean</i>"&gt;</code>
 * tag. This tag expresses the message for the given key from a portlet resource
 * bundle, localized to best-fit the user's locale.
 * </p>
 * <p>
 * All of the message resources configured in your portlet's
 * <code>applicationContext.xml</code> are searched for the given
 * <i>message-key</i>. We recommend you define your message resources using the
 * Spring ReloadableResourceBundleMessageSource class, for hot deployment of
 * message changes (eg upon localization deployment). The message property files
 * themselves should preferably be located in the portlet resource bundle
 * directory (location configured in <code>i18n-portlet-config.properties</code>).
 * The message property files should be tagged by locale according to the
 * Java-standard for ResourceBundle, and the folder where they are located
 * should be configured in the classpath so the system class loader can find
 * them.
 * </p>
 * <p>
 * If a message is found in no applicable resource, then the given
 * <i>default-value</i> is expressed instead. This is an optional attribute;
 * the <i>message-key</i> itself is expressed if no explicit <i>default-value</i>
 * is provided.
 * </p>
 * <p>
 * By default, the expressed message string does not escape any HTML special
 * characters it may contain, such as <code>&lt;</code>. This lets you put
 * HTML markup in your messages and have it render as such in the browser. If
 * you need the HTML special characters to be converted into their corresponding
 * character entities, so that they display literally in the browser instead,
 * use the <code>escape="true"</code> attribute on the tag.
 * </p>
 * <p>
 * If your message value includes general string parameters, use the
 * <code>&lt;spf-i18n-portlet:param&gt;</code> tag inside the
 * <code>&lt;spf-i18n-portlet:message&gt;</code> tag body, in order, for each
 * such parameter. If your message value includes a special contextual-help
 * token (<code>&lt;Contextual_Help&gt;</code>), use the
 * <code>&lt;spf-i18n-portlet:contextualHelpParam&gt;</code> tag inside the
 * <code>&lt;spf-i18n-portlet:message&gt;</code> tag body.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class MessageTag extends MessageBaseTag {

	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Gets the message string for the
	 * <code>&lt;spf-i18n-portlet:message&gt;</code> tag attributes,
	 * substituting any string or contextual help parameters provided in any
	 * child <code>&lt;spf-i18n-portlet:param&gt;</code> or
	 * <code>&lt;spf-i18n-portlet:contextualHelpParam&gt;</code> tags. If the
	 * message string cannot be found, then the key itself is returned.
	 * </p>
	 * <p>
	 * This method uses the full
	 * <code>I18nUtility.getMessage(PortletRequest,String,String,Object[],ContextualHelpProvider[],Locale,boolean)</code>
	 * method to do its work - please see the documentation for that method for
	 * a detailed description of the behavior. The parameters provided to the
	 * I18nUtility are taken from the tag attributes for the current tag and its
	 * child parameter tags (if any).
	 * </p>
	 * 
	 * @return The message string.
	 */
	public String getMessage() {
		PortletRequest portletRequest = (PortletRequest) pageContext
		.getRequest().getAttribute("javax.portlet.request");
		Object[] pArray = null;
		ContextualHelpProvider[] cArray = null;
		if (params != null) {
			pArray = params.toArray();
		}
		if (cProviders != null) {
			cArray = (ContextualHelpProvider[]) cProviders.toArray();
		}
		return I18nUtility.getMessage(portletRequest, key, defaultValue, pArray, cArray, null, escapeEnabled);
	}

	/**
	 * Log a tag error into the portlet error log file.
	 * 
	 * @param obj
	 *            The object asking to log this error.
	 * @param msg
	 *            The error message.
	 */
	public void logError(Object obj, String msg) {
		Log.logError(obj, msg);
	}

}
