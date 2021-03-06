/*
 * Project: Shared Portal Framework Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.htmlviewer.portlet.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.io.InputStream;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;

import org.springframework.web.portlet.ModelAndView;

import com.hp.it.spf.xa.i18n.portlet.I18nUtility;
import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;
import com.hp.it.spf.xa.interpolate.portlet.web.FileInterpolatorController;
import com.hp.it.spf.xa.exception.portlet.SPFException;
import com.hp.it.spf.xa.exception.portlet.SystemException;
import com.hp.it.spf.xa.exception.portlet.BusinessException;

import com.hp.frameworks.wpa.portlet.transaction.Transaction;
import com.hp.frameworks.wpa.portlet.transaction.TransactionImpl;
import com.hp.websat.timber.model.StatusIndicator;

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
     * Returns the hardcoded diagnostic message for the given status code and
     * status parameter.
     * 
     * @param statusCode
     *            An error, warning, or info code.
     * @param statusParam
     *            A parameter for the diagnostic message to incorporate.
     * @return diagnostic message
     */
    public static String getDiagnostic(String statusCode, String statusParam) {
	String diagnosticFormat = Consts.DIAGNOSTIC_FORMATS.get(statusCode);
	String diagnostic = statusParam;
	if (diagnosticFormat != null) {
	    diagnostic = String.format(diagnosticFormat, statusParam);
	}
	return diagnostic;
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
     * Returns the view filename exactly as requested in the request (either as
     * a portlet request parameter or a portal URL query parameter). The
     * filename attribute or parameter name is taken from the value of
     * {@link Consts#REQUESTED_VIEW_FILENAME}. Note: This method performs no
     * validation on the filename, but returns it exactly as found in the
     * request (trimmed and normalized to null if blank). Portlet request
     * parameters are checked first. If not found there, portal URL query
     * parameters are checked second (as request attributes; this depends on the
     * SPF request parameter extractor filter having been mapped against this
     * portlet).
     * 
     * @param request
     *            The portlet request.
     * @return The view filename as found in the request (trimmed), or null if
     *         none or blank.
     */
    public static String getRequestedViewFilename(PortletRequest request) {
	String requestedViewFilename = null;
	if (request != null) {
	    // first try getting it from the portlet request parameters
	    requestedViewFilename = request
		    .getParameter(com.hp.it.spf.xa.misc.portlet.Consts.PARAM_HTML_VIEWER_VIEW_FILE);
	    if (requestedViewFilename != null)
		requestedViewFilename = requestedViewFilename.trim();
	    if ((requestedViewFilename == null)
		    || (requestedViewFilename.length() == 0)) {
		// next try getting requested view filename from the portlet
		// request attributes (where the SPF request param extractor
		// filter would have copied it from the portal URL query string)
		requestedViewFilename = null;
		String[] values = (String[]) request
			.getAttribute(com.hp.it.spf.xa.misc.portlet.Consts.PARAM_HTML_VIEWER_VIEW_FILE);
		if ((values != null) && (values.length > 0))
		    requestedViewFilename = values[0];
		if (requestedViewFilename != null)
		    requestedViewFilename = requestedViewFilename.trim();
	    }
	}
	if ((requestedViewFilename != null)
		&& (requestedViewFilename.length() == 0))
	    requestedViewFilename = null;
	return requestedViewFilename;
    }

    /**
     * Returns the includes filename exactly as requested in the request (either
     * as a portlet request parameter or a portal URL query parameter). The
     * filename attribute or parameter name is taken from the value of
     * {@link Consts#REQUESTED_INCLUDES_FILENAME}. Note: This method performs no
     * validation on the filename, but returns it exactly as found in the
     * request (trimmed and normalized to null if blank). Portlet request
     * parameters are checked first. If not found there, portal URL query
     * parameters are checked second (as request attributes; this depends on the
     * SPF request parameter extractor filter having been mapped against this
     * portlet).
     * 
     * @param request
     *            The portlet request.
     * @return The includes filename as found in the request (trimmed), or null
     *         if none or blank.
     */
    public static String getRequestedIncludesFilename(PortletRequest request) {
	String requestedIncludesFilename = null;
	if (request != null) {
	    // first try getting it from the portlet request parameters
	    requestedIncludesFilename = request
		    .getParameter(com.hp.it.spf.xa.misc.portlet.Consts.PARAM_HTML_VIEWER_INCLUDES_FILE);
	    if (requestedIncludesFilename != null)
		requestedIncludesFilename = requestedIncludesFilename.trim();
	    if ((requestedIncludesFilename == null)
		    || (requestedIncludesFilename.length() == 0)) {
		// next try getting requested view filename from the portlet
		// request attributes (where the SPF request param extractor
		// filter would have copied it from the portal URL query string)
		requestedIncludesFilename = null;
		String[] values = (String[]) request
			.getAttribute(com.hp.it.spf.xa.misc.portlet.Consts.PARAM_HTML_VIEWER_INCLUDES_FILE);
		if ((values != null) && (values.length > 0))
		    requestedIncludesFilename = values[0];
		if (requestedIncludesFilename != null)
		    requestedIncludesFilename = requestedIncludesFilename
			    .trim();
	    }
	}
	if ((requestedIncludesFilename != null)
		&& (requestedIncludesFilename.length() == 0))
	    requestedIncludesFilename = null;
	return requestedIncludesFilename;
    }

    /**
     * Sets up the Timber logs for normal processing; meant for use at the
     * beginning of a handler (eg, from the
     * {@link com.hp.it.spf.xa.htmlviewer.portlet.web.TransactionLoggingInterceptor}
     * ). Setup includes:
     * <ul>
     * <li>setting Timber logs' CLIENT_ID column to the SPF diagnostic ID</li>
     * <li>setting Timber logs' PAGE_ID column to the SPF user ID</li>
     * <li>setting Timber logs' PORTLET_NAME column to the SPF portlet friendly
     * ID</li>
     * <li>initializing the status indicator to OK</li>
     * </ul>
     * 
     * <p>
     * Note this method is idempotent: it can be called multiple times on the
     * same request and will always have the same effect.
     * </p>
     * 
     * 
     * @param request
     *            The portlet request.
     */
    public static void setupLogData(PortletRequest request) {
	setupLogData(request, StatusIndicator.OK);
    }

    /**
     * Sets up the Timber logs for normal processing but with the given status
     * indicator; meant for use in a handler or resolver when the status has
     * changed. Setup includes:
     * <ul>
     * <li>setting Timber logs' CLIENT_ID column to the SPF diagnostic ID</li>
     * <li>setting Timber logs' PAGE_ID column to the SPF user ID</li>
     * <li>setting Timber logs' PORTLET_NAME column to the SPF portlet friendly
     * ID</li>
     * <li>initializing the status indicator to OK</li>
     * </ul>
     * <p>
     * Note this method is idempotent: it can be called multiple times with the
     * same request and status indicator, and will always have the same effect.
     * </p>
     * 
     * @param request
     *            The portlet request.
     */
    public static void setupLogData(PortletRequest request,
	    StatusIndicator status) {
	if (request != null) {
	    Transaction trans = TransactionImpl.getTransaction(request);
	    if (trans != null) {
		String portletID = Utils.getPortalPortletID(request);
		String diagID = Utils.getDiagnosticId(request);
		String userID = (String) Utils.getUserProperty(request,
			Consts.KEY_USER_NAME);
		// trans.addContextInfo("diagID", diagID);
		trans.setClientId(diagID);
		trans.setPageId(userID);
		trans.setPortletName(portletID);
		// trans.addContextInfo("portletID", portletID);
		if (status != null)
		    trans.setStatusIndicator(status);
		else
		    trans.setStatusIndicator(StatusIndicator.OK);
	    }
	}
    }

    /**
     * Rewrite all <code>&lt;A&gt;</code> tag <code>HREF</code> attributes in
     * the given content, adding <code>onclick="..."</code> code to open the
     * link in a new child window without buttons.
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
     * Return the <code>onclick</code> event handler string for a link which is
     * to be launched as a buttonless child window.
     * 
     * @param response
     * @return the <code>onclick</code> event handler string - for example,
     * 
     *         <code>callButtonlessWindow_<i>&lt;portlet-namespace&gt;</i>(this);return false;</code>
     */
    private static String getButtonlessChildLauncherOnclick(
	    PortletResponse response) {
	return "callButtonlessWindow_" + response.getNamespace()
		+ "(this);return false;";
    }

    /**
     * Return the <code>script</code> tag and content for a Javascript function
     * which launches a buttonless child window. For example (linefeeds added
     * for readability:
     * 
     * <pre>
     * &lt;code&gt;
     * &lt;script type=&quot;text/javascript&quot; language=&quot;javascript&quot; charset=&quot;utf-8&quot;&gt;
     *   function callButtonlessWindow_&lt;portlet-namespace&gt;(ev) {
     *     window.open (ev,'LinkoutWindow','top=0,left=0,...');
     *   }
     * &lt;/script&gt;
     * &lt;/code&gt;
     * </pre>
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

    /**
     * <p>
     * Sets up the Timber logs; meant for use at the beginning of exception
     * resolution. First delegates to {@link #setupLogData(PortletRequest)} to
     * initialize the log data. Then:
     * </p>
     * <ul>
     * <li>in the case of an SPF <code>BusinessException</code>, initializes the
     * Timber status indicator to ERROR and adds the error code and message as
     * Timber context info</li>
     * <li>in the case of an SPF <code>SystemException</code> or any other
     * non-SPF-<code>BusinessException</code>, initializes the Timber status
     * indicator to FATAL, and adds the error code (or classname) and message as
     * Timber context info, and adds the exception (including its stacktrace) to
     * the Timber error and errortrace logs</li>
     * </ul>
     * 
     * <blockquote>
     * <p>
     * Note this method is <b>not</b> idempotent; each time you call it, it adds
     * context info (and possibly error/errortrace log data) which accumulates.
     * </p>
     * </blockquote>
     * 
     * @param request
     *            The portlet request.
     */
    public static void setupLogData(PortletRequest request, Exception ex) {
	setupLogData(request);
	if (request != null) {
	    Transaction trans = TransactionImpl.getTransaction(request);
	    if (trans != null) {
		// set status indicator
		if (ex instanceof SPFException) {
		    if (ex instanceof SystemException) {
			trans.setStatusIndicator(StatusIndicator.FATAL);
		    } else {
			trans.setStatusIndicator(StatusIndicator.ERROR);
		    }
		} else {
		    trans.setStatusIndicator(StatusIndicator.FATAL);
		}
		// add error context info to business log (in all cases) and
		// add error data to error logs (in system error case only)
		if (ex instanceof SPFException) {
		    SPFException spfEx = (SPFException) ex;
		    String errorCode = spfEx.getErrorCode();
		    String errorDiagnostic = spfEx.getErrorMessage();
		    trans.addContextInfo(errorCode, errorDiagnostic);
		    if (ex instanceof SystemException) { // system error
			Throwable cause = spfEx.getNext();
			if (cause == null) // log cause or else this
			    cause = spfEx;
			trans.addError(cause, errorDiagnostic, errorCode);
		    }
		} else { // assume system error
		    String errorCode = ex.getClass().getName();
		    String errorDiagnostic = ex.getMessage();
		    trans.addContextInfo(errorCode, errorDiagnostic);
		    Throwable cause = ex.getCause();
		    if (cause == null) // log cause or else this
			cause = ex;
		    trans.addError(cause);
		}
	    }
	}
    }

}
