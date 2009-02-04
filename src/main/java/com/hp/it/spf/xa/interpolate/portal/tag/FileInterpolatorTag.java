/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.interpolate.portal.tag;

import com.hp.it.spf.xa.interpolate.portal.FileInterpolator;
import com.hp.it.spf.xa.interpolate.tag.FileInterpolatorBaseTag;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * Tag class for the <code>&lt;spf-file-portal:interpolate&gt;</code> tag.
 * This tag expresses interpolated, localized file content, including dynamic
 * token substitution as supported by the
 * {@link com.hp.it.spf.xa.interpolate.portal.FileInterpolator} class. The file
 * content comes from a secondary support file for the current portal component.
 * </p>
 * <p>
 * This tag has 2 attributes:
 * </p>
 * <ul>
 * <li>
 * <p>
 * The <code>file="<i>base-filename</i>"</code> attribute is used to pass
 * the name of a secondary support file which is a base file for a resource
 * bundle of localized files. The tag will interpolate and express the content
 * found in the best-fit localized file it finds for that base file, given the
 * current user locale. These files should all be stored as secondary support
 * files in the current portal component. The files in the bundle should be
 * tagged by locale according to the Java-standard for {@link ResourceBundle}
 * (be sure to use lowercase for the language code, and uppercase for the
 * country code).
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>tokenFile="<i>token-filename</i>"</code> attribute can
 * optionally be used to provide the name of a specific token-substitutions
 * property file. The
 * {@link com.hp.it.spf.xa.interpolate.portal.FileInterpolator} supports
 * substitution from this file for the <code>{TOKEN:<i>key</i>}</code>
 * token. Please see the class documentation for more information on how that
 * token works. By default, a file named
 * <code>default_token_subs.properties</code> is assumed for your
 * token-substitution properties, but you can override that with this tag
 * attribute. Whether you override or accept the default, the actual
 * token-substitution file may be located anywhere that is loadable by the
 * system classloader. You can put some relative path in the
 * <code><i>token-filename</i></code> if needed to help the classloader find
 * it.
 * </p>
 * </li>
 * </ul>
 * <p>
 * Based on the locale in the current user request, the attributes above, and
 * the available localized files, the expressed file content will come from the
 * proper file for the given base filename. Furthermore, any special tokens
 * supported by the {@link com.hp.it.spf.xa.interpolate.portal.FileInterpolator}
 * class hierarchy will have been substituted with their dynamic values
 * (including <code>{TOKEN:<i>key</i>}</code> replacement with the property
 * values from the given token file or from
 * <code>default_token_subs.properties</code> by default). If no proper file
 * existed, or the file was empty, then an empty string is expressed.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class FileInterpolatorTag extends FileInterpolatorBaseTag {

	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	private final LogWrapper LOG = new LogWrapper(FileInterpolatorTag.class);

	/**
	 * Make and return a portal
	 * {@link com.hp.it.spf.xa.interpolate.portal.FileInterpolator} for the
	 * previously-set attributes.
	 * 
	 * @return The file interpolator to use.
	 */
	public FileInterpolator getFileInterpolator() {
		PortalContext portalContext = (PortalContext) pageContext.getRequest()
				.getAttribute("portalContext");
		if (tokenFile != null) {
			return new FileInterpolator(portalContext, file, tokenFile);
		} else {
			return new FileInterpolator(portalContext, file);
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
