/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.htmlviewer.portlet.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.io.InputStream;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import org.springframework.web.portlet.ModelAndView;

import com.hp.it.spf.xa.i18n.portlet.I18nUtility;
import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;
import com.hp.it.spf.xa.interpolate.portlet.web.FileInterpolatorController;

/**
 * Container class for common utility methods used within the
 * <code>html-viewer</code> portlet.
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @version TBD
 */
public class Utils extends com.hp.it.spf.xa.misc.portlet.Utils {

	private static String TOKEN_HREF_START = "<a ";
	private static String TOKEN_HREF_END = "</a>";

	protected Utils() {
	}

	/**
	 * Checks the indicated view file name for error conditions given the
	 * particular request, returning the particular error code for the first
	 * error condition found, or null if no errors are found. The checked error
	 * conditions are a null or blank filename, or a filename containing a
	 * parent directory reference.
	 * 
	 * @param request
	 *            The portlet request.
	 * @param viewFilename
	 *            The view file name.
	 * @return error code (or null if no errors found).
	 */
	public static String checkViewFilenameForErrors(PortletRequest request,
			String viewFilename) {
		if (viewFilename == null) {
			return Consts.ERROR_CODE_VIEW_FILENAME_NULL;
		}
		viewFilename = viewFilename.trim();
		if (viewFilename.length() == 0) {
			return Consts.ERROR_CODE_VIEW_FILENAME_NULL;
		}
		if (viewFilename.indexOf("..") != -1) {
			return Consts.ERROR_CODE_VIEW_FILENAME_PATH;
		}
		return null;
	}

	/**
	 * Checks the indicated includes file name for error conditions given the
	 * particular request, returning the particular error code for the first
	 * error condition found, or null if no errors are found. The only checked
	 * error condition is a filename containing a parent directory reference.
	 * 
	 * @param request
	 *            The portlet request.
	 * @param includesFilename
	 *            The includes file name.
	 * @return error code (or null if no errors found).
	 */
	public static String checkIncludesFilenameForErrors(PortletRequest request,
			String includesFilename) {
		if ((includesFilename != null)
				&& (includesFilename.indexOf("..") != -1)) {
			return Consts.ERROR_CODE_INCLUDES_FILENAME_PATH;
		}
		return null;
	}

	/**
	 * Checks the indicated check-seconds option for error conditions given the
	 * particular request, returning the particular error code for the first
	 * error condition found, or null if no errors are found. The only checked
	 * error condition is a non-integer value.
	 * 
	 * @param request
	 *            The portlet request.
	 * @param checkSeconds
	 *            The check-seconds option.
	 * @return error code (or null if no errors found).
	 */
	public static String checkSecondsForErrors(PortletRequest request,
			String checkSeconds) {
		if ((checkSeconds != null) && (checkSeconds.trim().length() > 0)) {
			try {
				Integer.parseInt(checkSeconds);
			} catch (NumberFormatException e) {
				return Consts.ERROR_CODE_CHECK_SECONDS_VALUE;
			}
		}
		return null;
	}

	/**
	 * Checks the indicated view file name for warning conditions given the
	 * particular request, returning the particular warning code for the first
	 * warning condition found, or null if no warnings are found. The checked
	 * warning conditions are a non-existent or unreadable file. Note the method
	 * does not check if the file is empty.
	 * 
	 * @param request
	 *            The portlet request.
	 * @param viewFilename
	 *            The view file name.
	 * @return warning code (or null if no warnings found).
	 */
	public static String checkViewFilenameForWarnings(PortletRequest request,
			String viewFilename) {
		if ((viewFilename != null) && (viewFilename.trim().length() > 0)) {
			// treat as base file and check only for its existence/readability
			// check first for filename as-is
			viewFilename = slashify(viewFilename);
			InputStream is = I18nUtility.getLocalizedFileStream(request,
					viewFilename, false);
			if (is == null) {
				// if not found, check for filename in default folder -
				// DSJ 2009/3/30
				viewFilename = Consts.VIEW_FILE_DEFAULT_FOLDER + viewFilename;
				viewFilename = slashify(viewFilename);
				is = I18nUtility.getLocalizedFileStream(request, viewFilename,
						false);
				if (is == null) {
					return Consts.WARN_CODE_VIEW_FILE_NULL;
				}
			}
		}
		return null;
	}

	/**
	 * Checks the indicated token substitutions file name for warning conditions
	 * given the particular request, returning the particular warning code for
	 * the first warning condition found, or null if no warnings are found. The
	 * checked warning conditions are a non-existent or unloadable file. Note
	 * the method does not check if the file is empty.
	 * 
	 * @param request
	 *            The portlet request.
	 * @param includesFilename
	 *            The token substitutions filename.
	 * @return warning code (or null if no warnings found).
	 */
	public static String checkIncludesFilenameForWarnings(
			PortletRequest request, String includesFilename) {
		// includes file is optional, so return if not defined
		if (includesFilename == null
				|| (includesFilename.trim().length() == 0)
				|| (includesFilename.trim()
						.equals(Consts.DEFAULT_INCLUDES_FILENAME))) {
			return null;
		}
		// see if we can load a property resource bundle for it off the
		// classpath, or from the internal or external resource files
		try {
			includesFilename = slashify(includesFilename);
			ResourceBundle resBundle = PropertyResourceBundleManager
					.getBundle(includesFilename);
			if (resBundle != null) {
				return null;
			}
			PropertyResourceBundle propBundle = new PropertyResourceBundle(
					I18nUtility.getLocalizedFileStream(request,
							includesFilename, false));
			if (propBundle != null) {
				return null;
			}
			return Consts.WARN_CODE_INCLUDES_FILE_NULL;
		} catch (Exception e) {
			return Consts.WARN_CODE_INCLUDES_FILE_NULL;
		}
	}

	/**
	 * Rewrite all <code>&lt;A&gt;</code> tag <code>HREF</code> attributes
	 * in the given content, adding <code>onclick="..."</code> code to open
	 * the link in a new child window without buttons.
	 * 
	 * @param response
	 *            The portlet response
	 * @param content
	 *            old HTML content
	 * @return String new HTML content after parsing
	 */
	public static String addButtonlessChildLauncher(PortletResponse response,
			String content) {

		String regEx = "(" + TOKEN_HREF_START + ".*?" + TOKEN_HREF_END + ")";
		Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE
				| Pattern.UNICODE_CASE | Pattern.DOTALL);
		Matcher m = p.matcher(content);
		boolean mustAddJavascript = true;

		while (m.find()) {
			String s = m.group();// found string
			String slow = s.toLowerCase();

			// Add onclick when href tag exists and onclick does not exist
			if (slow.indexOf("href") > -1 && slow.indexOf("onclick") == -1) {
				String news = s.substring(3);
				news = TOKEN_HREF_START + "onclick=\""
						+ getButtonlessChildLauncherOnclick(response) + "\" "
						+ news;
				if (mustAddJavascript) {
					news += getButtonlessChildLauncherJavascript(response);
				}
				int starti = content.indexOf(s);
				int endi = starti + s.length();
				String a = content.substring(0, starti);
				String b = content.substring(endi);
				content = a + news + b;
				mustAddJavascript = false;
			}
		}

		return content;
	}

	/**
	 * No longer used.
	 * 
	 * @param content
	 *            old HTML content
	 * @param className
	 *            name of the style, to use in the class="..." attribute
	 * @return String new content after parsing
	 * @deprecated
	 */
	public static String addLinkStyle(String content, String className) {

		String regEx = "(" + TOKEN_HREF_START + ".*?" + TOKEN_HREF_END + ")";
		Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE
				| Pattern.UNICODE_CASE | Pattern.DOTALL);
		Matcher m = p.matcher(content);

		while (m.find()) {
			String s = m.group();// found string
			String slow = s.toLowerCase();

			// Add class when href tag exists and class does not exist
			if (slow.indexOf("href") > -1 && slow.indexOf("class") == -1) {
				String news = s.substring(3);
				news = TOKEN_HREF_START + " class=\"" + className + "\" "
						+ news;
				int starti = content.indexOf(s);
				int endi = starti + s.length();
				String a = content.substring(0, starti);
				String b = content.substring(endi);
				content = a + news + b;
			}
		}

		return content;
	}

	/**
	 * Return the <code>onclick</code> event handler string for a link which
	 * is to be launched as a buttonless child window.
	 * 
	 * @param response
	 * @return the <code>onclick</code> event handler string - for example,
	 *         <code>callButtonlessWindow_<i>&lt;portlet-namespace&gt;</i>(this);return false;</code>
	 */
	private static String getButtonlessChildLauncherOnclick(
			PortletResponse response) {
		return "callButtonlessWindow_" + response.getNamespace()
				+ "(this);return false;";
	}

	/**
	 * Return the <code>script</code> tag and content for a Javascript
	 * function which launches a buttonless child window. For example (linefeeds
	 * added for readability:
	 * 
	 * <pre><code>
	 * &lt;script type=&quot;text/javascript&quot; language=&quot;javascript&quot; charset=&quot;utf-8&quot;&gt;
	 *   function callButtonlessWindow_&lt;portlet-namespace&gt;(ev) {
	 *     window.open (ev,'LinkoutWindow','top=0,left=0,...');
	 *   }
	 * &lt;/script&gt;
	 * </code></pre>
	 * 
	 * @param response
	 * @return the <code>script</code> string
	 */
	private static String getButtonlessChildLauncherJavascript(
			PortletResponse response) {

		String javascript = "";
		javascript += "<script type=\"text/javascript\" language=\"javascript\" charset=\"utf-8\">";
		javascript += "function callButtonlessWindow_"
				+ response.getNamespace() + "(ev) { ";
		javascript += "window.open (ev,'LinkoutWindow','top=0,left=0,location=no,toolbar=no,menubar=no,status=no,resizable=yes,scrollbars=yes');";
		javascript += " }</script>";
		return javascript;

	}

}
