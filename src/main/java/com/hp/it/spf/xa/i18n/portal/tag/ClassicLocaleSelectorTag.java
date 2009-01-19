/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.portal.tag;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;
import com.hp.it.spf.xa.i18n.portal.tag.LocaleSelectorBaseTag;
import com.hp.it.spf.xa.misc.portal.Consts;

/**
 * <p>
 * The tag class for the "classic"-style locale selector (ie the
 * <code>&lt;spf-i18n-portal:classicLocaleSelector&gt;</code> tag). This tag
 * requires a label string to insert next to the selector widget itself, which
 * can be passed via either the <code>label="<i>string</i>"</code> attribute
 * or the <code>labelKey="<i>message-key</i>"</code> attribute. If the
 * former, the given <i>string</i> is used as the label text directly. If the
 * latter, the given <i>message-key</i> is used to lookup an appropriate
 * localized message for the user's current locale in the current portal
 * component's resource bundle (if not found, then the <i>message-key</i>
 * itself is used for this purpose). If both <code>label</code> and
 * <code>labelKey</code> are provided, the <code>label</code> attribute
 * takes precedence. And if <code>label</code> and <code>defaultLabel</code>
 * are provided, the <code>defaultLabel</code> is ignored.
 * </p>
 * <p>
 * The locales expressed are the ones currently available for the portal site,
 * sorted by locale. The locale currently resolved for the user is marked as the
 * pre-selected default.
 * </p>
 * 
 * @author <link href="maomao.guan@hp.com">Aaron</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ClassicLocaleSelectorTag extends LocaleSelectorBaseTag {
	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The <code>label</code> attribute from the tag.
	 */
	private String label;

	/**
	 * The <code>labelKey</code> attribute from the tag.
	 */
	private String labelKey;

	private static final LogWrapper LOGGER = new LogWrapper(
			ClassicLocaleSelectorTag.class);

	/**
	 * Constructor to initialize tag attributes.
	 * 
	 */
	public ClassicLocaleSelectorTag() {
		labelKey = null;
		label = null;
	}

	/**
	 * Release resources after processing this tag.
	 */
	public void release() {
		super.release();
		labelKey = null;
		label = null;
	}

	/**
	 * <p>
	 * Get the HTML for the "classic"-style locale selector widget (the part of
	 * the form which is visible to the user). The classic locale selector
	 * consists of a label (indicated by the tag attributes - at least one is
	 * required, although blank is permissible, or else a JspException is
	 * thrown) and an HTML <code>&lt;SELECT&gt;</code> element listing all the
	 * available locales for the current portal site. The current effective
	 * locale is the default. The available locales from which to choose and the
	 * current locale are gotten from the portal I18nUtility.
	 * </p>
	 * <p>
	 * <b>Note:</b> The contract here is that the returned HTML must name the
	 * <code>&lt;SELECT&gt;</code> with the given widget name, and the
	 * selectable values must be RFC 3066 language tags (eg <code>zh-CN</code>
	 * for Simplified Chinese). That is what the locale selector secondary page
	 * type is expecting.
	 * </p>
	 * 
	 * @param widgetName
	 *            The name to use for the <code>&lt;SELECT&gt;</code> element.
	 * @throws JspException
	 * @return The HTML string for "classic"-style locale selector.
	 */
	protected String getWidgetHTML(String widgetName) throws JspException {

		String html = "";
		if (label == null && labelKey == null) {
			String msg = "ClassicLocaleSelectorTag error: either the label or labelKey are required attributes.";
			LOGGER.error(msg);
			throw new JspException(msg);
		}
		if (widgetName != null) {
			widgetName = widgetName.trim();
		}
		if ((widgetName == null) || (widgetName.length() == 0)) {
			String msg = "ClassicLocaleSelectorTag error: the base tag did not provide a widget name.";
			LOGGER.error(msg);
			throw new JspException(msg);
		}
		try {
			PortalContext portalContext = (PortalContext) pageContext
					.getRequest().getAttribute("portalContext");
			HttpServletRequest request = portalContext.getHttpServletRequest();
			Collection locales = I18nUtility.getAvailableLocales(request);
			locales = I18nUtility.sortLocales(locales);
			Locale defaultLocale = I18nUtility.getLocale(request);
			String actualLabel = label;
			if (actualLabel == null) {
				actualLabel = I18nUtility.getValue(labelKey, portalContext);
			}
			if (actualLabel != null) {
				actualLabel = actualLabel.trim();
			} else {
				actualLabel = "";
			}

			// begin selector table layout
			html += "<table>\n";
			html += "<tr>\n";

			// label column
			html += "<td valign=MIDDLE>" + actualLabel + "</td>\n";

			// drop down list column
			html += "<td>\n";
			html += "<select id=selector name=\"" + widgetName + "\">\n";
			if (locales != null) {
				Iterator atts = locales.iterator();
				int i = 1;
				while (atts.hasNext()) {
					Locale locale = (Locale) atts.next();
					String dispName = I18nUtility.getLocaleDisplayName(locale);
					String value = I18nUtility.localeToLanguageTag(locale);
					html += "<option value=" + "\"" + value + "\"";
					// make the default locale selected if it is not empty
					if ((defaultLocale != null)
							&& (locale.equals(defaultLocale))) {
						html += " selected";
					}
					html += ">" + dispName + "</option>\n";
					i++;
				}
			}
			html += "</select>\n";
			html += "</td>\n";

			// button column
			String imgLink = portalContext.getPortalRequest().getContextPath()
					+ "/images/i18n/hpweb_1-2_arrw_sbmt.gif";
			html += "<td valign=\"middle\">\n";
			html += "<input type=\"image\" name=\"locale_selector_btn\" src=\""
					+ imgLink + "\">\n";
			html += "</td>\n";

			// end selector table layout
			html += "</tr>\n";
			html += "</table>\n";
		} catch (Exception ex) {
			String errMsg = "ClassicLocaleSelectorTag error: "
					+ ex.getMessage();
			LOGGER.error(errMsg);
			throw new JspException(errMsg, ex);
		}
		return html;
	}

	/**
	 * Set the label from the <code>label</code> attribute in the tag.
	 * 
	 * @param label
	 *            The label text.
	 */
	public void setLabel(String label) {
		this.label = normalize(label);
	}

	/**
	 * Get the value of the <code>label</code> attribute.
	 * 
	 * @return String
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set the label key from the <code>labelKey</code> attribute in the tag.
	 * 
	 * @param labelKey
	 *            The label key.
	 */
	public void setLabelKey(String labelKey) {
		this.labelKey = normalize(labelKey);
	}

	/**
	 * Get the value of the <code>labelKey</code> attribute.
	 * 
	 * @return String
	 */
	public String getLabelKey() {
		return label;
	}

	/**
	 * Normalize blank string values to null - so the return is either a
	 * non-blank string, or null.
	 * 
	 * @param value
	 * @return
	 */
	private String normalize(String value) {
		if (value != null) {
			value = value.trim();
			if (value.equals("")) {
				value = null;
			}
		}
		return value;
	}

}