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
import com.hp.it.spf.xa.misc.portal.Utils;

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
	 * The name of the submit button image for the classic locale selector form.
	 */
	protected static String SUBMIT_BUTTON_IMG_NAME = "btn_submit.gif";

	/**
	 * The <code>label</code> attribute from the tag.
	 */
	protected String label;

	/**
	 * The <code>labelKey</code> attribute from the tag.
	 */
	protected String labelKey;

	protected static final LogWrapper LOGGER = new LogWrapper(
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
	 * the form which is visible to the user), using the given parameters. The
	 * classic locale selector consists of a label (indicated by the tag
	 * attributes - at least one is required, although blank is permissible, or
	 * else a JspException is thrown) and an HTML <code>&lt;SELECT&gt;</code>
	 * element listing all the available locales for the current portal site.
	 * The current effective locale is the default. The available locales from
	 * which to choose and the current locale are passed in by the superclass.
	 * </p>
	 * <p>
	 * <b>Note:</b> The contract here is that the returned HTML must name the
	 * <code>&lt;SELECT&gt;</code> with the given widget name and available
	 * locales, and the selectable values must be RFC 3066 language tags (eg
	 * <code>zh-CN</code> for Simplified Chinese). That is what the locale
	 * selector secondary page type is expecting. Also, there is no need for
	 * this method to return the <code>&lt;FORM&gt;</code> tag or any hidden
	 * inputs to the form (eg the locale selector redirect target URL) because
	 * the superclass provides those.
	 * </p>
	 * 
	 * @param widgetName
	 *            The name to use for the <code>&lt;SELECT&gt;</code> element.
	 * @param availableLocales
	 *            An unsorted collection of all of the available locales (one or
	 *            more) from which the user may select.
	 * @param currentLocale
	 *            The user's current locale.
	 * @throws JspException
	 * @return The HTML string for "classic"-style locale selector.
	 */
	protected String getWidgetHTML(String widgetName,
			Collection availableLocales, Locale currentLocale)
			throws JspException {

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
			html += "<select id=\"" + widgetName + "\" name=\"" + widgetName
					+ "\">\n";
			if (availableLocales != null) {
				// sort available locales by the current locale - note that
				// HPWeb standard is to sort and display by country first,
				// language second, alphabetically by the current locale
				availableLocales = I18nUtility.sortLocales(availableLocales,
						currentLocale, I18nUtility.LOCALE_BY_COUNTRY);
				Iterator atts = availableLocales.iterator();
				int i = 1;
				while (atts.hasNext()) {
					Locale locale = (Locale) atts.next();
					// get display name in same locale (not necessarily current
					// locale) - note that HPWeb standard is to display country
					// first, language second, in the same locale
					String dispName = I18nUtility.getLocaleDisplayName(locale,
							locale, I18nUtility.LOCALE_BY_COUNTRY);
					String value = I18nUtility.localeToLanguageTag(locale);
					// both the display name and value need to be HTML-escaped
					// just in case
					html += "<option value=" + "\"" + Utils.escapeXml(value)
							+ "\"";
					// make the current locale selected if it is not empty
					if ((currentLocale != null)
							&& (locale.equals(currentLocale))) {
						html += " selected";
					}
					html += ">" + Utils.escapeXml(dispName) + "</option>\n";
					i++;
				}
			}
			html += "</select>\n";
			html += "</td>\n";

			// button column
			String imgLink = getSubmitImageURL(portalContext);
			html += "<td valign=\"middle\">\n";
			html += "<input type=\"image\" name=\"btn_" + widgetName
					+ "\" src=\"" + imgLink + "\">\n";
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
	 * A method to generate the image URL for the classic locale selector's
	 * submit button. This button is presumed to be named
	 * <code>btn_submit.gif</code> and located in the current portal
	 * component. This image may be localized if desired; this method looks for
	 * the particular localized image (loaded in the current portal component's
	 * secondary support files) which is the best-candidate given the current
	 * locale. If the image is not found there, then a URL pointing to
	 * <code>/images/btn_submit.gif</code> under the portal root path is
	 * assumed and returned.
	 */
	private String getSubmitImageURL(PortalContext portalContext) {
		String url = "/images/" + SUBMIT_BUTTON_IMG_NAME;
		if (portalContext != null) {
			if (I18nUtility.getLocalizedFileAsStream(portalContext,
					SUBMIT_BUTTON_IMG_NAME) != null) {
				url = I18nUtility.getLocalizedFileURL(portalContext,
						SUBMIT_BUTTON_IMG_NAME);
			} else {
				url = portalContext.getPortalHttpRoot() + "/" + url;
				// make sure the path includes the portal application context
				// root
				String contextPath = portalContext.getPortalRequest()
						.getContextPath();
				if (!url.startsWith(contextPath)) {
					return slashify(contextPath + "/" + url);
				}
			}
		}
		return slashify(url);
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

	/**
	 * <p>
	 * Returns the given path, with any consecutive file separators ("/" for
	 * Java) reduced to just one. The given path is also trimmed of whitespace.
	 * </p>
	 * 
	 * @param pPath
	 *            The file path to clean-up.
	 * @return The cleaned-up file path.
	 */
	private String slashify(String pPath) {
		if (pPath == null) {
			return null;
		}
		pPath = pPath.trim();
		return pPath.replaceAll("/+", "/");
	}
}