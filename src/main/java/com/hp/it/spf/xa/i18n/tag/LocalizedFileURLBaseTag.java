/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * <p>
 * Abstract base class for portal and portlet
 * <code>&lt;spf-i18n-portal:localizedFileURL&gt;</code> and
 * <code>&lt;spf-i18n-portlet:localizedFileURL&gt;</code> tags. These tags
 * express a URL pointing to a particular file, localized to best-fit the user's
 * locale. In the portlet case, this is a file in the portlet resource bundle
 * folder. In the portal case, it is a secondary support file for the current
 * portal component.
 * </p>
 * <p>
 * Both portal and portlet tags have 2 alternative sets of attributes, stored in
 * this base class:
 * </p>
 * <ul>
 * <li>
 * <p>
 * The <code>file="<i>base-filename</i>"</code> attribute is used to pass
 * the name of a base file for a portlet or portal resource bundle.
 * </p>
 * <ul>
 * <li>
 * <p>
 * In the portlet case, this should be a resource bundle located in the portlet
 * resource bundle folder. The <i>base-filename</i> may include some leading
 * path relative to that folder. For example, <code>/images/picture.jpg</code> -
 * in this example, the <code>picture*.jpg</code> bundle of files should be
 * located in the <code>images/</code> subdirectory of the portlet resource
 * bundle folder (the location of this folder is configured in the
 * <code>i18n_portlet_config.properties</code> file).
 * </p>
 * </li>
 * <li>
 * <p>
 * In the portal case, this should be a resource bundle located among the
 * secondary support files for the current portal component (eg style or
 * secondary page).
 * </p>
 * </li>
 * </ul>
 * <p>
 * In either case, the resource files in the bundle should be tagged by locale
 * according to the Java-standard for ResourceBundle.
 * </p>
 * </li>
 * <li>
 * <p>
 * Alternatively, the <code>key="<i>message-key</i>"</code> and optional
 * <code>defaultValue="<i>default-value</i>"</code> attributes can be used.
 * In this event, the <i>message-key</i> is used to lookup the particular
 * localized filename in a message resource bundle for the portal or portlet. If
 * that message is found, its value is used as the localized filename in the
 * URL; if not found, then the optional <i>default-value</i> is used.
 * </p>
 * <ul>
 * <li>
 * <p>
 * In the portlet case, this message should be in a resource bundle located in
 * the portlet resource bundle folder. The message value (ie the filename to
 * include in the URL) can include some leading path relative to that folder.
 * For example, the value in the French message resource bundle could be
 * <code>/images/picture_fr.jpg</code> - and in this example, the
 * <code>picture*.jpg</code> bundle of files should be located in the
 * <code>images/</code> subdirectory of the portlet resource bundle folder
 * (the location of this folder is configured in the
 * <code>i18n_portlet_config.properties</code> file).
 * </p>
 * </li>
 * <li>
 * <p>
 * In the portal case, this message should be included in the message resource
 * bundle for the current portal component. The message value is then the name
 * of a secondary support file (also for that same portal component) to include
 * in the URL.
 * </p>
 * </li>
 * </ul>
 * </li>
 * <li>
 * <p>
 * If both of the above sets of attributes are provided, the
 * <code>file="<i>base-filename</i>"</code> attribute is used and the others
 * are ignored.
 * </p>
 * </li>
 * </ul>
 * 
 * <p>
 * Based on the locale in the current user request, the attributes above, and
 * the available localized files, the expressed URL will point to the proper
 * file. (When the file is expressed as a <i>base-filename</i> instead of
 * identified explicitly via <i>message-key</i> and <i>default-value</i>, the
 * logic for finding the proper file is similar to that practiced by the
 * Java-standard ResourceBundle class.) The expressed URL is already encoded if
 * necessary and can be presented to the user in your response (for example, in
 * a <code>&lt;img&gt;</code> tag). If the file did not exist, however, then
 * an empty string is expressed.
 * </p>
 * 
 * @author <link href="ming.zou@hp.com">Ming</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class LocalizedFileURLBaseTag extends BodyTagSupport {

	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	protected String file;
	protected String key;
	protected String defaultValue;

	/**
	 * Perform tag processing.
	 * 
	 * @throws JspException
	 *             JspException
	 * @return integer
	 */
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		try {
			String url = getLocalizedFileURL();
			if (url == null) {
				url = "";
			}
			url = url.trim();
			out.print(url);
		} catch (Exception e) {
			logError(this, "SPF localized file URL tag error: "
					+ e.getMessage());
		}
		return EVAL_BODY_INCLUDE;
	}

	/**
	 * Set file from <code>file</code> tag attribute.
	 * 
	 * @param string
	 *            Value from the <code>file</code> tag attribute.
	 */
	public void setFile(String string) {
		file = string;
	}

	/**
	 * Set key from <code>key</code> tag attribute.
	 * 
	 * @param string
	 *            Value from the <code>key</code> tag attribute.
	 */
	public void setKey(String string) {
		key = string;
	}

	/**
	 * Set default value from <code>defaultValue</code> tag attribute.
	 * 
	 * @param string
	 *            Value from the <code>defaultValue</code> tag attribute.
	 */
	public void setDefaultValue(String string) {
		defaultValue = string;
	}

	/**
	 * Abstract method for getting the localized file URL. Different action for
	 * portal and portlet, so this is an abstract method.
	 * 
	 * @return The localized file URL to express.
	 */
	public abstract String getLocalizedFileURL();

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
