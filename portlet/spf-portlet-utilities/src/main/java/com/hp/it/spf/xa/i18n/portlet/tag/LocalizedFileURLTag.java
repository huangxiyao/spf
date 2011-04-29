/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.portlet.tag;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import com.hp.it.spf.xa.i18n.portlet.I18nUtility;
import com.hp.it.spf.xa.i18n.tag.LocalizedFileURLBaseTag;
import com.hp.websat.timber.logging.Log;

/**
 * <p>
 * Tag class for the
 * <code>&lt;spf-i18n-portlet:localizedFileURL file="<i>base-filename</i>"&gt;</code>
 * tag. This tag expresses a URL pointing to the given file, localized to
 * best-fit the user's locale. For example, you could use this tag to generate a
 * localized file URL for an image, then embed that tag output into the
 * <code>SRC</code> attribute of an <code>&lt;IMG&gt;</code> tag.
 * </p>
 * <p>
 * This tag has 2 alternative attributes:
 * </p>
 * <ul>
 * <li>
 * <p>
 * The <code>file="<i>base-filename</i>"</code> attribute is used to pass
 * the name of a base file in a portlet resource bundle of files. These files
 * may all be located in the portlet resource bundle folder on the portlet
 * server (eg, for easy administrator access). Or, you can place these files
 * inside your portlet WAR. The <i>base-filename</i> may include some leading
 * path relative to the location you choose (ie, the portlet resource bundle
 * folder, or the portlet application root), so you can put them into a
 * subfolder if desired.
 * </p>
 * <p>
 * For example, <code>/images/picture.jpg</code> - in this example, the
 * <code>picture*.jpg</code> bundle of files could be located in the
 * <code>images/</code> subdirectory of the portlet resource bundle folder
 * (the location of this folder is configured in the
 * <code>i18n_portlet_config.properties</code> file). Or you could place that
 * bundle of files inside the <code>images/</code> subdirectory of your
 * portlet application. In either case, the resource files in the bundle should
 * be tagged by locale according to the Java-standard for ResourceBundle (be
 * sure to use lowercase for the language code, and uppercase for the country
 * code - on a case-sensitive filesystem, this may matter).
 * </p>
 * </li>
 * <li>
 * <p>
 * Alternatively, the <code>fileKey="<i>message-key</i>"</code> attribute
 * can be used. In this event, the <i>message-key</i> is used to lookup the
 * particular localized filename in a message resource bundle for the portlet.
 * If that message is found, its value is used as the localized filename in the
 * URL. As above, this message resource file itself should be in a bundle of
 * such resources located in the portlet resource bundle folder or your portlet
 * WAR (ie where it is accessible to the classloader) and tagged with locale
 * appropriately.
 * </p>
 * <p>
 * Note that the message value (ie the filename to include in the URL) can
 * include some leading path relative to that folder. For example, the value in
 * the French message resource bundle could be
 * <code>/images/picture_fr.jpg</code> - and in this example, the
 * <code>picture*.jpg</code> bundle of files should be located in the
 * <code>images/</code> subdirectory of the portlet resource bundle folder or
 * the portlet WAR.
 * </p>
 * </li>
 * <li>
 * <p>
 * If both of the above attributes are provided, the
 * <code>file="<i>base-filename</i>"</code> attribute is used and the other
 * is ignored.
 * </p>
 * </li>
 * </ul>
 * <p>
 * Based on the locale in the current user request, the attributes above, and
 * the available localized files, the expressed URL will point to the proper
 * file. (When the file is expressed as a <i>base-filename</i> instead of
 * identified explicitly via <i>message-key</i> and <i>default-file</i>, the
 * logic for finding the proper file is similar to that practiced by the
 * Java-standard ResourceBundle class.) The expressed URL is already encoded if
 * necessary and can be presented to the user in your portlet response (for
 * example, in a <code>&lt;img&gt;</code> tag). If the file did not exist,
 * however, then a URL referencing your base filename inside the portlet WAR
 * will be expressed. (This URL, if subsequently opened by the browser, will of
 * course trigger an HTTP 404 error since the actual file is missing.)
 * </p>
 * 
 * @author <link href="ming.zou@hp.com">Ming</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class LocalizedFileURLTag extends LocalizedFileURLBaseTag {

	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Get the localized file URL based on the previously-set attributes. Note
	 * that the file attribute takes precedence over the key and default value
	 * attributes.
	 * 
	 * @return The localized file URL to express.
	 */
	public String getLocalizedFileURL() {
		PortletRequest portletRequest = (PortletRequest) pageContext
				.getRequest().getAttribute("javax.portlet.request");
		PortletResponse portletResponse = (PortletResponse) pageContext
				.getRequest().getAttribute("javax.portlet.response");
		String url = null;
		if (file != null) {
			url = I18nUtility.getLocalizedFileURL(portletRequest,
					portletResponse, file);
		} else {
			url = I18nUtility.getLocalizedFileURL(portletRequest,
					portletResponse, fileKey, null);
		}
		return url;
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
