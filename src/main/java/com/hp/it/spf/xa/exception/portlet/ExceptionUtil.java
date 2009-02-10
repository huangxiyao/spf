/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.xa.exception.portlet;

import java.util.ArrayList;
import javax.portlet.PortletRequest;
import com.hp.it.spf.xa.misc.portlet.Consts;
import com.hp.it.spf.xa.i18n.portlet.I18nUtility;
import com.hp.it.spf.xa.exception.portlet.SPFException;
import com.hp.it.spf.xa.exception.portlet.SystemException;
import com.hp.it.spf.xa.exception.portlet.BusinessException;

/**
 * <p>
 * This class provides exception utility methods for portlets that are using the
 * SPF
 * {@link com.hp.it.spf.xa.exception.portlet.handler.SimpleMappingExceptionResolver}
 * and the {@link SPFException} class hierarchy. Currently the class provides:
 * </p>
 * <ul>
 * <li> Methods to access the exception that was saved by the
 * <code>SimpleMappingExceptionResolver</code></li>
 * <li> Methods to inspect what kind of exception(s) are in that exception
 * (there could be a chain)</li>
 * <li> Methods to return the error code(s) for each exception in that chain</li>
 * <li> Methods to get user-displayable, localized messages for that exception
 * from your message resources for each exception in that chain</li>
 * </ul>
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
	 * Stores the given exception into the given request. If you need to store
	 * multiple exceptions, chain them one inside the other, and then call this
	 * method to just store the outermost exception.
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
	 * null if there is none. If there are multiple exceptions in the request,
	 * this method returns the outermost one, you can get the chained
	 * exception(s) by calling each one's <code>getCause</code> method to get
	 * the next.
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
	 * <code>SystemException</code>, false otherwise. If multiple exceptions
	 * have been chained, then all of them are checked.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return True if the request contains an SPF <code>SystemException</code>
	 *         or false otherwise.
	 */
	public static boolean containsSystemException(PortletRequest request) {
		Throwable e = getException(request);
		while (e != null) {
			if (e instanceof SystemException) {
				return true;
			}
			if (e.equals(e.getCause()))
				break;
			e = e.getCause();
		}
		return false;
	}

	/**
	 * Returns true if the given request contains an SPF
	 * <code>BusinessException</code>, false otherwise. If multiple
	 * exceptions have been chained, then all of them are checked.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return True if the request contains an SPF
	 *         <code>BusinessException</code> or false otherwise.
	 */
	public static boolean containsBusinessException(PortletRequest request) {
		Throwable e = getException(request);
		while (e != null) {
			if (e instanceof BusinessException) {
				return true;
			}
			if (e.equals(e.getCause()))
				break;
			e = e.getCause();
		}
		return false;
	}

	/**
	 * Returns true if the given request contains some other exception (not an
	 * <code>SPFException</code>), false otherwise. If multiple exceptions
	 * have been chained, then all of them are checked.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return True if the request contains some kind of exception besides an
	 *         <code>SPFException</code> or false otherwise.
	 */
	public static boolean containsOtherException(PortletRequest request) {
		Throwable e = getException(request);
		while (e != null) {
			if (!(e instanceof SPFException)) {
				return true;
			}
			if (e.equals(e.getCause()))
				break;
			e = e.getCause();
		}
		return false;
	}

	/**
	 * <p>
	 * Returns all of the error code(s) of any {@link SPFException}(s)
	 * contained in the given request, substituting the given default for any
	 * non-<code>SPFException</code>(s) in the request. This method looks
	 * into the request at where the
	 * {@link #setException(PortletRequest,Exception)} method stores the
	 * exception, and checks all the chained exceptions it finds there. If none
	 * are found, then an empty array is returned.
	 * </p>
	 * <p>
	 * For example, if there is no exception in the request, then an empty array
	 * is returned. If there are exception(s) in the request, then the error
	 * code for each one is returned, in order, in an array. Each element in the
	 * array is either the error code if that one was an
	 * <code>SPFException</code> (or the default if the error code for that
	 * <code>SPFException</code> was null), or it is the default if that one
	 * was not an <code>SPFException</code>.
	 * </p>
	 * <p>
	 * Note that null is permissible as a default value.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param defaultValue
	 *            A default value.
	 * @return The error codes as described above.
	 */
	public static String[] getErrorCodes(PortletRequest request,
			String defaultValue) {

		ArrayList<String> errorCodes = new ArrayList<String>();
		Throwable t = getException(request);
		while (t != null) {
			if (t instanceof SPFException) {
				SPFException e = (SPFException) t;
				String errorCode = e.getErrorCode();
				if (errorCode == null)
					errorCode = defaultValue;
				errorCodes.add(errorCode);
			} else {
				errorCodes.add(defaultValue);
			}
			if (t.equals(t.getCause()))
				break;
			t = t.getCause();
		}
		return (String[]) errorCodes.toArray(new String[0]);
	}

	/**
	 * <p>
	 * Returns the error code of the outermost {@link SPFException} contained in
	 * the given request, substituting the given default for any non-<code>SPFException</code>
	 * found there. This method looks into the request at where the
	 * {@link #setException(PortletRequest,Exception)} method stores the
	 * exception, and checks the exception it finds there. If none is found,
	 * then the given default is returned.
	 * </p>
	 * <p>
	 * For example, if there is no exception in the request, then the default is
	 * returned. If there are exception(s) in the request, then the error code
	 * of the outermost one in the chain is returned: which is the error code
	 * from <code>SPFException</code> (or the default if that error code is
	 * null), or the default if it is not an <code>SPFException</code>.
	 * </p>
	 * <p>
	 * Note that null is permissible as a default value.
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
		String[] errorCodes = getErrorCodes(request, defaultValue);
		if ((errorCodes != null) && (errorCodes.length > 0))
			return errorCodes[0];
		else
			return defaultValue;
	}

	/**
	 * <p>
	 * Returns all of the error code(s) of any {@link SPFException}(s)
	 * contained in the given request, substituting null for any non-<code>SPFException</code>(s)
	 * in the request. This method looks into the request at where the
	 * {@link #setException(PortletRequest,Exception)} method stores the
	 * exception, and checks all the chained exceptions it finds there. If none
	 * are found, then an empty array is returned.
	 * </p>
	 * <p>
	 * For example, if there is no exception in the request, then an empty array
	 * is returned. If there are exception(s) in the request, then the error
	 * code for each one is returned, in order, in an array. Each element in the
	 * array is either the error code from an <code>SPFException</code>, or
	 * null if that one was not an <code>SPFException</code>.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param defaultValue
	 *            A default value.
	 * @return The error codes as described above.
	 */
	public static String[] getErrorCodes(PortletRequest request) {
		return getErrorCodes(request, null);
	}

	/**
	 * <p>
	 * Returns the error code of the outermost {@link SPFException} contained in
	 * the given request. This method looks into the request at where the
	 * {@link #setException(PortletRequest,Exception)} method stores the
	 * exception, and returns the error code of the outermost
	 * <code>SPFException</code> it finds there. Otherwise, null is returned.
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
	 * Returns all of the real localized error message(s) of any
	 * {@link SPFException}(s) and/or other {@link java.lang.Throwable}(s)
	 * contained in the given request, substituting a default message for any
	 * that are not real. This method looks into the request at where the
	 * {@link #setException(PortletRequest,Exception)} method stores the
	 * exception, and returns the real (or default) localized error messages for
	 * each one found in that chain. An empty array is returned if there was no
	 * exception in the request.
	 * </p>
	 * <p>
	 * For this purpose, a "real" localized error message is one that is not
	 * null and not equal to the throwable's message itself (ie not equal to the
	 * {@link java.lang.Throwable#getMessage()} value of the throwable). That
	 * message is typically not localized or suitable for display to a user, yet
	 * it is used by default when the <code>SPFException</code> (or any other
	 * throwable) was not provided with a real localized message. So this method
	 * discards those non-real values and substitutes the default for them
	 * instead.
	 * </p>
	 * <p>
	 * The default is either a message from your resource bundles for error code
	 * found inside the <code>SPFException</code> (if applicable); or a
	 * message from your resource bundles for the given default key; or finally
	 * the default key itself if such a message is not found.
	 * </p>
	 * <p>
	 * For example, if there is no exception in the request, then an empty array
	 * is returned. If there are exception(s) in the request, then the localized
	 * message for each one is returned, in order, in an array: where each
	 * element of the array is the value of <code>getLocalizedMessage()</code>
	 * for that throwable, unless that matches the value of
	 * <code>getMessage</code> (in which case the default is used instead).
	 * </p>
	 * <p>
	 * Note that null is permissible as a default value.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param defaultKey
	 *            A default value: either a message key or a literal value.
	 * @return The localized messages as described above.
	 */
	public static String[] getLocalizedMessages(PortletRequest request,
			String defaultKey) {

		String defaultMessage = null;
		String message = null;
		if (defaultKey != null) {
			defaultMessage = I18nUtility.getMessage(request, defaultKey);
		}
		ArrayList<String> messages = new ArrayList<String>();
		Throwable t = getException(request);
		while (t != null) {
			if (t instanceof SPFException) {
				SPFException e = (SPFException) t;
				message = e.getLocalizedMessage();
				if ((message == null) || (message.equals(e.getMessage()))) {
					String errorCode = e.getErrorCode();
					message = I18nUtility.getMessage(request, errorCode);
					if ((message == null) || (message.equals(errorCode)))
						message = defaultMessage;
				}
			} else {
				message = t.getLocalizedMessage();
				if ((message == null) || (message.equals(t.getMessage())))
					message = defaultMessage;
			}
			messages.add(message);
			if (t.equals(t.getCause()))
				break;
			t = t.getCause();
		}
		return (String[]) messages.toArray(new String[0]);
	}

	/**
	 * <p>
	 * Returns all of the real localized error message(s) of any
	 * {@link SPFException}(s) and/or other {@link java.lang.Throwable}(s)
	 * contained in the given request, using null for any that are not real.
	 * </p>
	 * <p>
	 * This method works like
	 * {@link #getLocalizedMessages(PortletRequest, String)} where null is the
	 * default given.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The localized messages as described above.
	 */
	public static String[] getLocalizedMessages(PortletRequest request) {
		return getLocalizedMessages(request, null);
	}

	/**
	 * <p>
	 * Returns the real localized error message for the outermost
	 * {@link SPFException} or other {@link java.lang.Throwable} contained in
	 * the given request, substituting a default message if it is not real. This
	 * method looks into the request at where the
	 * {@link #setException(PortletRequest,Exception)} method stores the
	 * exception, and returns the real (or default) localized message of the
	 * outermost exception there.
	 * </p>
	 * <p>
	 * For this purpose, a "real" localized error message is one that is not
	 * null and not equal to the throwable's message itself (ie the
	 * {@link java.lang.Throwable#getMessage()} value of the throwable). That
	 * message is typically not localized or suitable for display to a user, yet
	 * it is used by default when the <code>SPFException</code> (or any other
	 * throwable) was not provided with a real localized message. So this method
	 * discards that non-real value and substitutes a default for it instead.
	 * </p>
	 * <p>
	 * The default is either a message from your resource bundles for error code
	 * found inside the <code>SPFException</code> (if applicable); or a
	 * message from your resource bundles for the given default key; or finally
	 * the default key itself if such a message is not found.
	 * </p>
	 * <p>
	 * For example, if there is no exception in the request, then the default is
	 * returned. If there are exception(s) in the request, then the localized
	 * message for the outermost one is returned: either the value of
	 * <code>getLocalizedMessage()</code>, or the default if
	 * <code>getLocalizedMessage</code> matched <code>getMessage</code>.
	 * </p>
	 * <p>
	 * Note that null is permissible as a default value.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param defaultKey
	 *            A default value: either a message key or a literal value.
	 * @return The localized messages as described above.
	 */
	public static String getLocalizedMessage(PortletRequest request,
			String defaultKey) {
		String[] messages = getLocalizedMessages(request, defaultKey);
		if ((messages != null) && (messages.length > 0))
			return messages[0];
		else
			return defaultKey;
	}

	/**
	 * <p>
	 * Returns the real localized error message for the outermost
	 * {@link SPFException} or other {@link java.lang.Throwable} contained in
	 * the given request. Null is returned if there is none.
	 * </p>
	 * <p>
	 * This method works like
	 * {@link #getLocalizedMessage(PortletRequest, String)} where null is the
	 * default given.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The localized messages as described above.
	 */
	public static String getLocalizedMessage(PortletRequest request) {
		return getLocalizedMessage(request, null);
	}
}
