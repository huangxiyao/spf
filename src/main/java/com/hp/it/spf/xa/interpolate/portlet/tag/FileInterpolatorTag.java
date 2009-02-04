/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.interpolate.portlet.tag;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import com.hp.it.spf.xa.interpolate.portlet.FileInterpolator;
import com.hp.it.spf.xa.interpolate.tag.FileInterpolatorBaseTag;
import com.hp.websat.timber.logging.Log;

/**
 * <p>
 * Tag class for the <code>&lt;spf-file-portlet:interpolate&gt;</code> tag.
 * This tag expresses interpolated, localized file content, including dynamic
 * token substitution as supported by the
 * {@link com.hp.it.spf.xa.interpolate.portlet.FileInterpolator} class. The file
 * content comes either from the <i>portlet resource bundle folder</i> outside
 * of the portlet WAR, or from inside the portlet WAR itself.
 * </p>
 * <p>
 * This tag has 2 attributes:
 * </p>
 * <ul>
 * <li>
 * <p>
 * The <code>file="<i>base-filename</i>"</code> attribute is used to pass
 * the name of a file which is a base file for a resource bundle of localized
 * files. The tag will interpolate and express the content found in the best-fit
 * localized file it finds for that base file, given the current user locale.
 * These files may all be located in the portlet resource bundle folder on the
 * portlet server (eg, for easy administrator access). Or, you can place these
 * files inside your portlet WAR. The <i>base-filename</i> may include some
 * leading path relative to the location you choose (ie, the portlet resource
 * bundle folder, or the portlet application root), so you can put them into a
 * subfolder if desired.
 * </p>
 * <p>
 * For example, <code>/html/file.html</code> - in this example, the
 * <code>file*.html</code> bundle of files could be located in the
 * <code>html/</code> subdirectory of the portlet resource bundle folder (the
 * location of this folder is configured in the
 * <code>i18n_portlet_config.properties</code> file). Or you could place that
 * bundle of files inside the <code>html/</code> subdirectory of your portlet
 * application. In either case, the resource files in the bundle should be
 * tagged by locale according to the Java-standard for {@link ResourceBundle}
 * (be sure to use lowercase for the language code, and uppercase for the
 * country code - on a case-sensitive filesystem, this may matter).
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
 * Based on the locale in the current request, the attributes above, and
 * the available localized files, the expressed file content will come from the
 * proper file for the given base filename. Furthermore, any special tokens
 * supported by the {@link com.hp.it.spf.xa.interpolate.portlet.FileInterpolator} class
 * hierarchy will have been substituted with their dynamic values (including
 * <code>{TOKEN:<i>key</i>}</code> replacement with the property values from
 * the given token file or from <code>default_token_subs.properties</code> by
 * default). If no proper file existed, or the file was empty, then an empty
 * string is expressed.
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

	/**
	 * Make and return a portlet
	 * {@link com.hp.it.spf.xa.interpolate.portlet.FileInterpolator} for the
	 * previously-set attributes.
	 * 
	 * @return The file interpolator to use.
	 */
	public FileInterpolator getFileInterpolator() {
		PortletRequest portletRequest = (PortletRequest) pageContext
				.getRequest().getAttribute("javax.portlet.request");
		PortletResponse portletResponse = (PortletResponse) pageContext
				.getRequest().getAttribute("javax.portlet.response");
		if (tokenFile != null) {
			return new FileInterpolator(portletRequest, portletResponse, file, tokenFile);
		} else {
			return new FileInterpolator(portletRequest, portletResponse, file);
		}
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
