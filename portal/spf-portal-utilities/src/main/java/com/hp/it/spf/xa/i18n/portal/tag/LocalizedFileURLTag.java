/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.portal.tag;

import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.i18n.tag.LocalizedFileURLBaseTag;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * Tag class for the
 * <code>&lt;spf-i18n-portal:localizedFileURL file="<i>base-filename</i>"&gt;</code>
 * tag. This tag expresses a URL pointing to the given secondary support file in
 * the current portal component, localized to best-fit the user's locale. For
 * example, you could use this tag to generate a localized file URL for an
 * image, then embed that tag output into the <code>SRC</code> attribute of an
 * <code>&lt;IMG&gt;</code> tag.
 * </p>
 * <p>
 * This tag has 2 alternative attributes:
 * </p>
 * <ul>
 * <li>
 * <p>
 * The <code>file="<i>base-filename</i>"</code> attribute is used to pass
 * the name of a secondary support file which is a base file for a resource
 * bundle of localized files. These files should all be stored as secondary
 * support files in the current portal component. The files in the bundle should
 * be tagged by locale according to the Java-standard for ResourceBundle (be
 * sure to use lowercase for the language code, and uppercase for the country
 * code).
 * </p>
 * </li>
 * <li>
 * <p>
 * Alternatively, the <code>fileKey="<i>message-key</i>"</code> attribute
 * can be used. In this event, the <i>message-key</i> is used to lookup the
 * particular localized filename in a message resource bundle for the current
 * portal component. That value is then used as the localized filename in the
 * URL. As above, the message resource file itself should be in a bundle of such
 * resources located among the current component's secondary support files, and
 * tagged with locale appropriately.
 * </p>
 * </li>
 * <li>
 * <p>
 * If both of the above attributes are provided, the
 * <code>file="<i>base-filename</i>"</code> attribute is preferred.
 * </p>
 * </li>
 * </ul>
 * <p>
 * Based on the locale in the current user request, the attributes above, and
 * the available localized files, the expressed URL will point to the proper
 * file. (When the file is expressed as a <i>base-filename</i> instead of
 * identified explicitly via <i>message-key</i>, the logic for finding the
 * proper file is similar to that practiced by the Java-standard ResourceBundle
 * class.) The expressed URL is ready to be presented to the user in your
 * response (for example, in a <code>&lt;img&gt;</code> tag). If the file did
 * not exist, however, then the expressed URL points to the base filename
 * anyway. (This URL, if subsequently opened by the browser, will of course
 * trigger an HTTP 404 error since the actual file is missing.)
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

	private final LogWrapper LOG = new LogWrapper(LocalizedFileURLTag.class);

	/**
	 * Get the localized file URL based on the previously-set attributes. Note
	 * that the file attribute takes precedence over the key and default value
	 * attributes.
	 * 
	 * @return The localized file URL to express.
	 */
	public String getLocalizedFileURL() {
		PortalContext portalContext = (PortalContext) pageContext.getRequest()
				.getAttribute("portalContext");
		String url = null;
		if (file != null) {
			url = I18nUtility.getLocalizedFileURL(portalContext, file);
		} else {
			url = I18nUtility.getLocalizedFileURL(portalContext, fileKey, null);
		}
		return url;
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
