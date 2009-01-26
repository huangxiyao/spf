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
 * <code>&lt;spf-i18n-portal:classicLocaleSelector&gt;</code> tag). The
 * locales expressed are the ones currently available for the portal site,
 * sorted by locale. The locale currently resolved for the user is marked as the
 * pre-selected default.
 * </p>
 * <p>
 * This tag takes the following tag attributes:
 * </p>
 * <ul>
 * <li>
 * <p>
 * This tag requires a label string to insert next to the selector widget
 * itself, which can be passed via either the <code>label="<i>string</i>"</code>
 * attribute or the <code>labelKey="<i>message-key</i>"</code> attribute. If
 * the former, the given <i>string</i> is used as the label text directly. If
 * the latter, the given <i>message-key</i> is used to lookup an appropriate
 * localized message for the user's current locale in the current portal
 * component's resource bundle (if not found, then the <i>message-key</i>
 * itself is used for this purpose). If both <code>label</code> and
 * <code>labelKey</code> are provided, the <code>label</code> attribute
 * takes precedence. And if <code>label</code> and <code>defaultLabel</code>
 * are provided, the <code>defaultLabel</code> is ignored.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>labelStyle="<i>title-style</i>"</code> and
 * <code>labelClass="<i>title-class</i>"</code> attributes are alternate
 * ways of specifying a CSS style for the locale selector's label. With the
 * <code>labelStyle</code> attribute, you pass an inline CSS style. This
 * should be a string of any CSS properties which are valid properties for your
 * label and/or the HTML <code>&lt;TD&gt;</code> tag. For example,
 * <code>labelStyle="background-color:black;color:white;font-weight:bold"</code>.
 * With the <code>labelClass</code> attribute, you pass the name of a CSS
 * class you have already included in your JSP. That class should specify the
 * same kind of properties as you would in <code>labelStyle</code> - for
 * example, <code>labelClass="my-label-style"</code> where you have elsewhere
 * defined the following CSS class:
 * <code>.my-label-style { background-color:black; ... }</code>. There is no
 * default applied.
 * </p>
 * </li>
 * The <code>labelStyle="<i>label-style</i>"</code> and
 * <code>labelClass="<i>label-class</i>"</code> attributes are alternate
 * ways of specifying a CSS style for the locale selector's label. With the
 * <code>labelStyle</code> attribute, you pass an inline CSS style. This
 * should be a string of any CSS properties which are valid properties for your
 * label and/or the HTML <code>&lt;TD&gt;</code> tag. For example,
 * <code>labelStyle="background-color:black;color:white;font-weight:bold"</code>.
 * With the <code>labelClass</code> attribute, you pass the name of a CSS
 * class you have already included in your JSP. That class should specify the
 * same kind of properties as you would in <code>labelStyle</code> - for
 * example, <code>labelClass="my-label-style"</code> where you have elsewhere
 * defined the following CSS class:
 * <code>.my-label-style { background-color:black; ... }</code>. There is no
 * default applied.
 * <li>
 * <p>
 * The <code>listStyle="<i>list-style</i>"</code> and
 * <code>listClass="<i>list-class</i>"</code> attributes are alternate ways
 * of specifying a CSS style for the locale selector's label. With the
 * <code>listStyle</code> attribute, you pass an inline CSS style. This should
 * be a string of any CSS properties which are valid properties for locale list
 * (ie, the HTML <code>&lt;SELECT&gt;</code> tag. For example,
 * <code>labelStyle="background-color:yellow;color:black;font-style:italic"</code>.
 * With the <code>listClass</code> attribute, you pass the name of a CSS class
 * you have already included in your JSP. That class should specify the same
 * kind of properties as you would in <code>listStyle</code> - for example,
 * <code>listClass="my-list-style"</code> where you have elsewhere defined the
 * following CSS class:
 * <code>.my-list-style { background-color:yellow; ... }</code>. There is no
 * default applied.
 * </p>
 * </li>
 * </ul>
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
	 * The default style to apply to the label. Currently null.
	 */
	protected static String DEFAULT_LABEL_STYLE = null;

	/**
	 * The default style to apply to the list. Currently null.
	 */
	protected static String DEFAULT_LIST_STYLE = null;

	/**
	 * The <code>label</code> attribute from the tag.
	 */
	protected String label;

	/**
	 * The <code>labelKey</code> attribute from the tag.
	 */
	protected String labelKey;

	/**
	 * Stores the value of the <code>labelStyle</code> attribute.
	 */
	protected String labelStyle;

	/**
	 * Stores the value of the <code>labelClass</code> attribute.
	 */
	protected String labelClass;

	/**
	 * Stores the value of the <code>listStyle</code> attribute.
	 */
	protected String listStyle;

	/**
	 * Stores the value of the <code>listClass</code> attribute.
	 */
	protected String listClass;

	protected static final LogWrapper LOGGER = new LogWrapper(
			ClassicLocaleSelectorTag.class);

	/**
	 * Constructor to initialize tag attributes.
	 * 
	 */
	public ClassicLocaleSelectorTag() {
		labelKey = null;
		label = null;
		labelClass = null;
		labelStyle = null;
		listClass = null;
		listStyle = null;
	}

	/**
	 * Release resources after processing this tag.
	 */
	public void release() {
		super.release();
		labelKey = null;
		label = null;
		labelClass = null;
		labelStyle = null;
		listClass = null;
		listStyle = null;
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
			widgetName = Utils.escapeXml(widgetName.trim());
		}
		if ((widgetName == null) || (widgetName.length() == 0)) {
			String msg = "ClassicLocaleSelectorTag error: the base tag did not provide a widget name.";
			LOGGER.error(msg);
			throw new JspException(msg);
		}
		try {
			PortalContext portalContext = (PortalContext) pageContext
					.getRequest().getAttribute("portalContext");

			// Get the label to use.
			String actualLabel = label;
			if (actualLabel == null) {
				actualLabel = I18nUtility.getValue(labelKey, portalContext);
			}
			if (actualLabel != null) {
				actualLabel = actualLabel.trim();
			} else {
				actualLabel = "";
			}

			// Get the label style to use.
			String labelStyleAttr = "";
			if (this.labelStyle != null)
				labelStyleAttr += "style=\"" + Utils.escapeXml(this.labelStyle)
						+ "\" ";
			if (this.labelClass != null)
				labelStyleAttr += "class=\"" + Utils.escapeXml(this.labelClass)
						+ "\" ";
			if ("".equals(labelStyleAttr) && (DEFAULT_LABEL_STYLE != null))
				labelStyleAttr += "style=\""
						+ Utils.escapeXml(DEFAULT_LABEL_STYLE) + "\" ";

			// Get the list style to use.
			String listStyleAttr = "";
			if (this.listStyle != null)
				listStyleAttr += "style=\"" + Utils.escapeXml(this.listStyle)
						+ "\" ";
			if (this.listClass != null)
				listStyleAttr += "class=\"" + Utils.escapeXml(this.listClass)
						+ "\" ";
			if ("".equals(listStyleAttr) && (DEFAULT_LIST_STYLE != null))
				listStyleAttr += "style=\""
						+ Utils.escapeXml(DEFAULT_LIST_STYLE) + "\" ";

			// begin selector table layout
			html += "<table cellpadding=\"2\" cellspacing=\"0\">\n";
			html += "<tr>\n";

			// label column
			html += "<td " + labelStyleAttr + "valign=\"middle\">" + actualLabel + "</td>\n";

			// drop down list column
			html += "<td " + labelStyleAttr + ">\n";
			html += "<select " + listStyleAttr + "id=\"" + widgetName + "\" name=\"" + widgetName
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
			html += "<td " + labelStyleAttr + "valign=\"middle\">\n";
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
	 * Get the value of the <code>labelStyle</code> attribute.
	 * 
	 * @return The <code>labelStyle</code> attribute.
	 */
	public String getLabelStyle() {
		return labelStyle;
	}

	/**
	 * Set the value from the <code>labelStyle</code> attribute. Blank values
	 * are not normalized to null.
	 * 
	 * @param value
	 *            The <code>labelStyle</code> attribute.
	 */
	public void setLabelStyle(String value) {
		this.labelStyle = value;
	}

	/**
	 * Get the value of the <code>labelClass</code> attribute.
	 * 
	 * @return The <code>labelClass</code> attribute.
	 */
	public String getLabelClass() {
		return labelClass;
	}

	/**
	 * Set the value from the <code>labelClass</code> attribute. Blank values
	 * are not normalized to null.
	 * 
	 * @param value
	 *            The <code>labelClass</code> attribute.
	 */
	public void setLabelClass(String value) {
		this.labelClass = value;
	}

	/**
	 * Get the value of the <code>listStyle</code> attribute.
	 * 
	 * @return The <code>listStyle</code> attribute.
	 */
	public String getListStyle() {
		return listStyle;
	}

	/**
	 * Set the value from the <code>listStyle</code> attribute. Blank values
	 * are not normalized to null.
	 * 
	 * @param value
	 *            The <code>listStyle</code> attribute.
	 */
	public void setListStyle(String value) {
		this.listStyle = value;
	}

	/**
	 * Get the value of the <code>listClass</code> attribute.
	 * 
	 * @return The <code>listClass</code> attribute.
	 */
	public String getListClass() {
		return listClass;
	}

	/**
	 * Set the value from the <code>listClass</code> attribute. Blank values
	 * are not normalized to null.
	 * 
	 * @param value
	 *            The <code>listClass</code> attribute.
	 */
	public void setListClass(String value) {
		this.listClass = value;
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