/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.xa.exception.portlet;

import javax.portlet.PortletRequest;
import com.hp.it.spf.xa.misc.portlet.Consts;
import com.hp.it.spf.xa.i18n.portlet.I18nUtility;
import com.hp.it.spf.xa.exception.portlet.SPFException;
import com.hp.it.spf.xa.exception.portlet.SystemException;
import com.hp.it.spf.xa.exception.portlet.BusinessException;

/**
 * <p>
 * This class provides exception utility methods for portlets using the SPF
 * <code>SimpleMappingExceptionResolver</code> and the
 * <code>SPFException</code> class hierarchy.
 * </p>
 * 
 * @author Scott Jorgenson
 * @version TBD
 */
public class ExceptionUtil {

	// ------------------------------------------------------------- Constants

	/**
	 * The name of the request attribute holding the exception.
	 */
	private static final String REQUEST_ATTR_EXCEPTION = "SPF_EXCEPTION_DATA";

	// ------------------------------------------------------------- Attributes

	// ------------------------------------------------------------- Methods

	/**
	 * Stores the given exception into the given request.
	 * 
	 * @param request
	 *            The portlet request.
	 * @param exception
	 *            Some exception.
	 */
	public static void setException(PortletRequest request, Exception e) {
		if (request != null) {
			request.setAttribute(REQUEST_ATTR_EXCEPTION, e);
		}
	}

	/**
	 * Gets the stored exception from the given request. This method returns
	 * null if there is none.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The exception, or null if none.
	 */
	public static Exception getException(PortletRequest request) {
		if (request != null) {
			return (Exception) request.getAttribute(REQUEST_ATTR_EXCEPTION);
		}
		return null;
	}

	/**
	 * Returns true if the given request contains an SPF
	 * <code>SystemException</code>, false otherwise.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return True if the request contains an SPF <code>SystemException</code>
	 *         or false otherwise.
	 */
	public static boolean systemException(PortletRequest request) {
		Exception e = getException(request);
		if ((e != null) && (e instanceof SystemException))
			return true;
		else
			return false;
	}

	/**
	 * Returns true if the given request contains an SPF
	 * <code>BusinessException</code>, false otherwise.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return True if the request contains an SPF
	 *         <code>BusinessException</code> or false otherwise.
	 */
	public static boolean businessException(PortletRequest request) {
		Exception e = getException(request);
		if ((e != null) && (e instanceof BusinessException))
			return true;
		else
			return false;
	}

	/**
	 * Returns true if the given request contains some other exception (not an
	 * <code>SPFException</code>), false otherwise.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return True if the request contains some kind of exception besides an
	 *         <code>SPFException</code> or false otherwise.
	 */
	public static boolean otherException(PortletRequest request) {
		Exception e = getException(request);
		if ((e != null) && !(e instanceof SPFException))
			return true;
		else
			return false;
	}

	/**
	 * <p>
	 * Returns the error code of any <code>SPFException</code> contained in
	 * the given request, or returns the given default if there is no
	 * <code>SPFException</code> in the request. For example, if there is no
	 * exception in the request, or if the exception in the request is not an
	 * <code>SPFException</code>, the given default is returned. Similarly,
	 * if there is an <code>SPFException</code> in the request, but it has a
	 * null error code, the default is also returned in that case.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param defaultValue
	 *            A default value.
	 * @return The error code as described above.
	 */
	public static String getErrorCode(PortletRequest request,
			String defaultValue) {

		if (defaultValue != null)
			defaultValue = defaultValue.trim();
		if (request == null)
			return defaultValue;
		String errorCode = null;

		// check for an SPF exception first
		if (systemException(request) || businessException(request)) {
			SPFException e = (SPFException) getException(request);
			errorCode = e.getErrorCode();
		}
		if (errorCode == null)
			errorCode = defaultValue;
		return errorCode;
	}

	/**
	 * <p>
	 * Returns the error code of any <code>SPFException</code> contained in
	 * the given request. If there is no <code>SPFException</code> in the
	 * request, or it has a null error code, then null is returned.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The error code as described above.
	 */
	public static String getErrorCode(PortletRequest request) {
		return getErrorCode(request, null);
	}

	/**
	 * <p>
	 * Returns the error message for any exception contained in the given
	 * request, or returns the given default if there is no exception in the
	 * request. Similarly, if there is an exception in the request, but it has a
	 * null error message, the default is also returned in that case.
	 * </p>
	 * <p>
	 * The returned value is the internal message string for the exception, not
	 * the localized error message appropriate for display to the user. Use
	 * <code>getDisplayMessage(PortletRequest request, String defaultValue)</code>
	 * for that.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param defaultValue
	 *            A default value.
	 * @return The error message as described above.
	 */
	public static String getErrorMessage(PortletRequest request,
			String defaultValue) {

		if (defaultValue != null)
			defaultValue = defaultValue.trim();
		if (request == null)
			return defaultValue;
		String errorMessage = null;

		// check for an SPF exception first
		if (systemException(request) || businessException(request)) {
			SPFException e = (SPFException) getException(request);
			errorMessage = e.getErrorMessage();
			if (errorMessage == null)
				errorMessage = e.getMessage();
		} else if (otherException(request)) {
			Exception e = (Exception) getException(request);
			errorMessage = e.getMessage();
		}
		if (errorMessage == null)
			errorMessage = defaultValue;
		return errorMessage;
	}

	/**
	 * <p>
	 * Returns the error message of any exception contained in the given
	 * request. If there is no exception in the request, or it has a null error
	 * message, then null is returned.
	 * </p>
	 * <p>
	 * The returned value is the internal message string for the exception, not
	 * the localized error message appropriate for display to the user. Use
	 * <code>getDisplayMessage(PortletRequest request, String defaultValue)</code>
	 * for that.
	 * </p>
	 * 
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The error message as described above.
	 */
	public static String getErrorMessage(PortletRequest request) {
		return getErrorMessage(request, null);
	}

	/**
	 * <p>
	 * Returns the message to display to the user for any
	 * <code>SPFException</code> contained in the given request, or returns a
	 * message based upon the given default when there is no
	 * <code>SPFException</code> in the request or the message cannot be
	 * obtained.
	 * </p>
	 * <p>
	 * If the given request contains an <code>SPFException</code> with a
	 * non-null error code, then this method uses that error code as a message
	 * key, to retrieve a localized message, appropriate for display to the
	 * user, from your portlet's resource bundles:
	 * </p>
	 * <ul>
	 * <li>
	 * <p>
	 * Selection of the particular localized message properties file is as per
	 * the Java standard behavior for ResourceBundle. Selection is based on the
	 * current locale (indicated in the given portlet request).
	 * </p>
	 * </li>
	 * <li>
	 * <p>
	 * The resource bundles searched are the ones configured for your portlet in
	 * the Spring application context (ie in your portlet's
	 * <code>applicationContext.xml</code>). In your
	 * <code>applicationContext.xml</code>, we recommend you define your
	 * message <code>&lt;bean&gt;</code>'s using Spring's
	 * ReloadableResourceBundleMessageSource class, because then your messages
	 * will be hot-refreshed at runtime should they change (eg during a
	 * localization deployment, no restart will be needed).
	 * </p>
	 * </li>
	 * <li>
	 * <p>
	 * Your resource bundle files must be located somewhere findable by the
	 * class loader. For example, you could put them in the usual location,
	 * inside your portlet WAR. But for ease of administration (eg to permit
	 * localization by the administrator without having to touch the portlet
	 * WAR), we recommend you put them in the portlet resource bundle folder
	 * dedicated for this purpose. The location of the portlet resource bundle
	 * folder is configured in the <code>i18n_portlet_config.properties</code>
	 * file; this folder should also be specified on the classpath so that the
	 * class loader can find it.
	 * </p>
	 * </li>
	 * <li>
	 * <p>
	 * This method assumes the message string it returns does not need any
	 * parameter substitution, escaping HTML meta-characters, etc. If your
	 * message string does require these things, you should use
	 * <code>getException</code> to get the exception directly. Then you can
	 * use the portlet I18nUtility <code>getMessage</code> methods directly
	 * with it.
	 * </p>
	 * </li>
	 * </ul>
	 * <p>
	 * If your resource bundles do not contain any message string for the error
	 * code in the <code>SPFException</code> - or if the given request does
	 * not contain any <code>SPFException</code> with a non-null error code -
	 * then this method attempts to obtain a default message string as follows:
	 * </p>
	 * <ul>
	 * <li>
	 * <p>
	 * First, your given default value (if it is not null) is used as a default
	 * message key. The method looks into your portlet's message resources once
	 * more, using that key this time. All the above stipulations about the
	 * message retrieval apply.
	 * </p>
	 * </li>
	 * <li>
	 * <p>
	 * If, after this, still no message string has been found, then your given
	 * default is returned.
	 * </p>
	 * </li>
	 * </ul>
	 * <p>
	 * <b>Note:</b> This method is supposed to return a message appropriate for
	 * display to the user. You might wonder why we don't return the value of
	 * <code>Exception.getLocalizedMessage()</code> for that. The reason is
	 * that we cannot be sure that value is appropriate for the user - it might
	 * be a localized version of the internal error message, for example. If you
	 * need to have access to the <code>getLocalizedMessage</code> value
	 * anyway, use <code>getException</code> and then call
	 * <code>getLocalizedMessage</code> directly.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param defaultValue
	 *            A default value.
	 * @return The error message as described above.
	 */
	public static String getDisplayMessage(PortletRequest request,
			String defaultValue) {

		if (defaultValue != null)
			defaultValue = defaultValue.trim();
		if (request == null)
			return defaultValue;
		String displayMessage = null;

		// check for an SPF exception first
		if (systemException(request) || businessException(request)) {
			SPFException e = (SPFException) getException(request);
			String errorCode = e.getErrorCode();
			if (errorCode != null)
				displayMessage = I18nUtility.getMessage(request, errorCode,
						(String) null);
		}
		if (displayMessage == null) {
			String errorCode = defaultValue;
			if (errorCode != null)
				displayMessage = I18nUtility.getMessage(request, errorCode,
						(String) null);
		}
		if (displayMessage == null)
			displayMessage = defaultValue;
		return displayMessage;
	}

	/**
	 * <p>
	 * Returns the message to display to the user for any
	 * <code>SPFException</code> contained in the given request, or returns
	 * null if there is no <code>SPFException</code> in the request or no
	 * message can be found for its error code.
	 * </p>
	 * <p>
	 * This method works the same as
	 * <code>getDisplayMessage(PortletRequest,String,String)</code> (see) with
	 * a null default value.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param defaultValue
	 *            A default value.
	 * @return The error message as described above.
	 */
	public static String getDisplayMessage(PortletRequest request) {
		return getDisplayMessage(request, null);
	}
}
