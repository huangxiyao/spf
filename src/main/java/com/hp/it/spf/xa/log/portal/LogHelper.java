/*
 * Project: Shared Portal Framework
 * Copyright (c) 2009 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.xa.log.portal;

import com.vignette.portal.log.LogWrapper;

/**
 * <p>
 * A class to provide helper methods for logging.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class LogHelper extends com.hp.it.spf.xa.log.LogHelper {

	public static int EXCEPTION_LINES_TO_LOG = 5;

	/**
	 * Logs an exception stack trace for the given object to the Vignette log,
	 * using the error log level. Up to {@link #EXCEPTION_LINES_TO_LOG} number
	 * of lines in the trace are included in the log; the remainder are ignored.
	 * 
	 * @param o
	 *            The object for which to log. Generally you pass "this" as this
	 *            parameter.
	 * @param ex
	 *            The exception to log.
	 */
	public static void logStackTrace(Object o, Exception ex) {
		logStackTrace(o, ex, EXCEPTION_LINES_TO_LOG);
	}

	/**
	 * Logs an exception stack trace for the given object to the Vignette log,
	 * using the error log level and recording the given number of lines
	 * maximum. Any remaining lines in the stack trace are ignored.
	 * 
	 * @param o
	 *            The object for which to log. Generally you pass "this" as this
	 *            parameter.
	 * @param ex
	 *            The exception to log.
	 * @param howManyLines
	 *            The maximum number of lines in the exception stack trace to
	 *            log. If non-positive, nothing will be logged.
	 */
	public static void logStackTrace(Object o, Exception ex, int howManyLines) {
		if (o == null || ex == null) {
			return;
		}
		if (howManyLines <= 0) {
			return;
		}
		LogWrapper logger = new LogWrapper(o.getClass());
		StackTraceElement[] lines = ex.getStackTrace();
		for (int i = 0; i < lines.length; i++) {
			if (i == howManyLines - 1) {
				if (i == lines.length) {
					logger.error(lines[i]);
				} else {
					logger.error(lines[i] + "...");
				}
				break;
			} else {
				logger.error(lines[i]);
			}
		}
		return;
	}
}
