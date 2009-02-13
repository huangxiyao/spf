/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.portal.tag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hp.it.spf.xa.help.ContextualHelpProvider;
import com.hp.it.spf.xa.help.portal.GlobalHelpProvider;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.i18n.tag.MessageBaseTag;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * Tag class for the <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag. This
 * tag expresses a message from a portal resource bundle, localized to best-fit
 * the user's locale. This tag takes the following attributes:
 * </p>
 * <ul>
 * <li>
 * <p>
 * The <code>key="<i>message-key</i>"</code> attribute specifies the key for
 * the message to lookup in the resource bundle. It is a required attribute.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>stringID="<i>component-id</i>"</code> attribute specifies the
 * UID for the Vignette portal component containing the resource bundle to
 * search. This is an optional attribute. If not provided, then the current
 * portal component (ie the one in whose JSP the current tag is embedded) is
 * assumed.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>defaultValue="<i>default-message</i>"</code> attribute provides
 * a default message value to express in case no message for the given
 * <i>message-key</i> can be found. This is an optional attribute. If not
 * provided, then the <i>message-key</i> itself is used.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>escape="<i>true-or-false</i>"</code> attribute indicates
 * whether to escape any HTML special characters found in the message - ie,
 * converting special characters like <code>&lt;</code> into their equivalent
 * character entities. By default this is not done (so any HTML markup contained
 * in your message is rendered as such by the browser), but if the attribute
 * value is <code>true</code> then it is performed.
 * </p>
 * </li>
 * <li>
 * <p>
 * Similarly, the <code>filterSpan="<i>true-or-false</i>"</code> attribute
 * indicates whether to remove any <code>&lt;SPAN&gt;</code> tags which
 * Vignette may have inserted. Vignette does this automatically, to help
 * assistive devices such as readers. Normally the <code>&lt;SPAN&gt;</code>
 * tags are invisible, but some browsers in some contexts (eg the HTML
 * <code>&lt;TITLE&gt;</code> tag) will display these erroneously. The
 * <code>filterSpan="true"</code> attribute setting lets you suppress those
 * <code>&lt;SPAN&gt;</code> tags. By default they are not suppressed.
 * </p>
 * </li>
 * </ul>
 * 
 * <p>
 * If your message value includes general string parameters, use the
 * <code>&lt;spf-i18n-portal:i18nParam&gt;</code> tag inside the
 * <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag body. List them with the
 * proper values in order, for each such parameter.
 * </p>
 * <p>
 * If your message value includes a special contextual-help token (<code>&lt;Contextual_Help&gt;</code>),
 * use the <code>&lt;spf-i18n-portal:i18nContextualHelpParam&gt;</code> tag
 * inside the <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag body. List
 * them with the proper values in order, for each such
 * <code>&lt;Contextual_Help&gt;...&lt;/Contextual_Help&gt;</code> token
 * pairing in the message string.
 * </p>
 * <p>
 * Finally, if your message value includes a special global-help token (<code>&lt;Global_Help&gt;</code>),
 * use the <code>&lt;spf-i18n-portal:i18nGlobalHelpParam&gt;</code> tag inside
 * the <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag body. List as many
 * of them as there are
 * <code>&lt;Contextual_Help&gt;...&lt;/Contextual_Help&gt;</code> token
 * pairings in the message string.
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

	private final LogWrapper LOG = new LogWrapper(MessageTag.class);

	/**
	 * The <code>stringID</code> attribute (ie the portal component ID) from
	 * the tag.
	 */
	protected String stringID;

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
	 * The list of global help providers from any global help parameter tags
	 * enclosed in the message tag body.
	 */
	protected List gProviders;

	/**
	 * Initialize the tag values.
	 */
	public MessageTag() {
		super();
		stringID = null;
		filterSpan = null;
		filterSpanEnabled = false;
		gProviders = null;
	}

	/**
	 * Set the component ID from the <code>stringID</code> attribute,
	 * normalizing blank to null.
	 * 
	 * @param key
	 *            Value of the <code>stringID</code> attribute.
	 */
	public void setStringID(String stringID) {
		this.stringID = normalize(stringID);
	}

	/**
	 * Get the value of the <code>stringID</code> attribute.
	 * 
	 * @return Value of the <code>stringID</code> attribute.
	 */
	public String getStringID() {
		return stringID;
	}

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
	 * Add global help parameters to the list, for later interpolation into the
	 * message. These come from any global help parameter tags contained inside
	 * the message tag body (see GlobalHelpParamBaseTag).
	 * 
	 * @param g
	 *            A parameter set for one instance of global help.
	 */
	public void addGlobalHelpProvider(GlobalHelpProvider g) {
		if (gProviders == null) {
			gProviders = new ArrayList();
		}
		gProviders.add(g);
	}

	/**
	 * Clear all buffered parameters.
	 */
	protected void clearParams() {
		super.clearParams();
		stringID = null;
		filterSpan = null;
		filterSpanEnabled = false;
		if (gProviders != null) {
			Iterator itr = gProviders.iterator();
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
			gProviders.clear();
		}
	}

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
		PortalContext portalContext = (PortalContext) pageContext.getRequest()
				.getAttribute("portalContext");
		Object[] pArray = null;
		ContextualHelpProvider[] cArray = null;
		GlobalHelpProvider[] gArray = null;
		if (params != null) {
			pArray = params.toArray();
		}
		if (cProviders != null) {
			cArray = (ContextualHelpProvider[]) cProviders
					.toArray(new ContextualHelpProvider[] {});
		}
		if (gProviders != null) {
			gArray = (GlobalHelpProvider[]) gProviders
					.toArray(new GlobalHelpProvider[] {});
		}
		if (stringID == null) {
			return I18nUtility.getValue(key, defaultValue, portalContext,
					pArray, gArray, cArray, escapeEnabled, filterSpanEnabled);
		} else {
			return I18nUtility.getValue(stringID, key, defaultValue,
					portalContext, pArray, gArray, cArray, escapeEnabled,
					filterSpanEnabled);
		}
	}

	/**
	 * Log a tag error into the portal error log file.
	 * 
	 * @param obj
	 *            The object asking to log this error.
	 * @param msg
	 *            The error message.
	 */
	public void logError(Object obj, String msg) {
		LOG.error(msg);
	}

}
