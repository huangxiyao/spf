/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.htmlviewer.portlet.util;

import java.util.HashMap;

/**
 * Container class for common constants shared across multiple artifacts in the
 * <code>html-viewer</code> portlet.
 * 
 * @author <link href="xiao-bing.zuo@hp.com">Zuo Xiaobing</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link
 * @version TBD
 */
public class Consts extends com.hp.it.spf.xa.misc.portlet.Consts {

	/** View filename portlet preference, request parameter, and model attribute. */
	public static final String VIEW_FILENAME = "viewFilename";

	/**
	 * Token substitutions filename portlet preference, request parameter, and
	 * model attribute.
	 */
	public static final String INCLUDES_FILENAME = "includesFilename";

	/**
	 * Default token substitutions filename that will be assumed.
	 */
	public static final String DEFAULT_INCLUDES_FILENAME = "html_viewer_includes.properties";

	/**
	 * Launch-buttonless portlet preference, request parameter, and model
	 * attribute.
	 */
	public static final String LAUNCH_BUTTONLESS = "launchButtonless";

	/**
	 * Default launch-buttonless value that will be assumed.
	 */
	public static final String DEFAULT_LAUNCH_BUTTONLESS = "false";

	/**
	 * Check-seconds portlet preference, request parameter, and model attribute.
	 */
	public static final String CHECK_SECONDS = "checkSeconds";

	/**
	 * Default check-seconds value that will be assumed. Negative means that
	 * view resources are cached forever; zero means there are not cached.
	 */
	public static final String DEFAULT_CHECK_SECONDS = "900";

	/** Error code render parameter. */
	public static final String ERROR_CODE = "errorCode";

	/** Error message model attribute. */
	public static final String ERROR_MESSAGES = "errorMessages";

	/** Status code render parameter. */
	public static final String INFO_CODE = "infoCode";

	/** Status message model attribute. */
	public static final String INFO_MESSAGES = "infoMessages";

	/** Warning code render parameter. */
	public static final String WARN_CODE = "warnCode";

	/** Warning message model attribute. */
	public static final String WARN_MESSAGES = "warnMessages";

	/** View content model attribute. */
	public static final String VIEW_CONTENT = "viewContent";

	/** Error code: internal error. */
	public static final String ERROR_CODE_INTERNAL = "error.internal";

	/** Error code: input error. */
	public static final String ERROR_CODE_INPUT = "error.input";

	/** Error code: view file not found. */
	public static final String ERROR_CODE_VIEW_FILE_NULL = "error.nullFile";

	/** Error code: view filename was blank. */
	public static final String ERROR_CODE_VIEW_FILENAME_NULL = "error.nullFilename";

	/** Error code: view filename contained problem in path. */
	public static final String ERROR_CODE_VIEW_FILENAME_PATH = "error.pathInFilename";

	/** Error code: includes file was not found. */
	public static final String ERROR_CODE_INCLUDES_FILE_NULL = "error.nullSubsFile";

	/** Error code: token substitution file contained problem in path. */
	public static final String ERROR_CODE_INCLUDES_FILENAME_PATH = "error.pathInSubsFilename";

	/** Error code: check seconds value was not an integer. */
	public static final String ERROR_CODE_CHECK_SECONDS_VALUE = "error.checkSecondsValue";

	/** Warning code: view file not found or empty during config mode. */
	public static final String WARN_CODE_VIEW_FILE_NULL = "warn.nullFile";

	/**
	 * Warning code: token substitutions file not found or empty during config
	 * mode.
	 */
	public static final String WARN_CODE_INCLUDES_FILE_NULL = "warn.nullSubsFile";

	/** Status code: portlet preferences saved. */
	public static final String INFO_CODE_PREFS_SAVED = "info.saved";

	/** Status code: portlet preferences not saved. */
	public static final String ERROR_CODE_PREFS_UNSAVED = "error.notSaved";

	/**
	 * Message formats for error, warning, and info codes for which we want to
	 * log messages
	 */
	public static final HashMap<String, String> DIAGNOSTIC_FORMATS = new HashMap<String, String>();
	static {
		DIAGNOSTIC_FORMATS.put(ERROR_CODE_CHECK_SECONDS_VALUE, "invalid "
				+ CHECK_SECONDS + ": %s");
		DIAGNOSTIC_FORMATS.put(ERROR_CODE_INCLUDES_FILENAME_PATH, "invalid "
				+ INCLUDES_FILENAME + ": %s");
		DIAGNOSTIC_FORMATS.put(ERROR_CODE_INCLUDES_FILE_NULL, INCLUDES_FILENAME
				+ " not found or readable: %s");
		DIAGNOSTIC_FORMATS.put(ERROR_CODE_INTERNAL, "system error: %s");
		DIAGNOSTIC_FORMATS.put(ERROR_CODE_INPUT, "input error: %s");
		DIAGNOSTIC_FORMATS.put(ERROR_CODE_VIEW_FILE_NULL, VIEW_FILENAME
				+ " not found or readable: %s");
		DIAGNOSTIC_FORMATS.put(ERROR_CODE_VIEW_FILENAME_NULL, VIEW_FILENAME
				+ " is undefined.");
		DIAGNOSTIC_FORMATS.put(ERROR_CODE_VIEW_FILENAME_PATH, "invalid "
				+ VIEW_FILENAME + ": %s");
		DIAGNOSTIC_FORMATS.put(WARN_CODE_INCLUDES_FILE_NULL, INCLUDES_FILENAME
				+ " not found or readable: %s");
		DIAGNOSTIC_FORMATS.put(WARN_CODE_VIEW_FILE_NULL, VIEW_FILENAME
				+ " not found or readable: %s");
		DIAGNOSTIC_FORMATS.put(INFO_CODE_PREFS_SAVED, "preferences saved");
		DIAGNOSTIC_FORMATS.put(ERROR_CODE_PREFS_UNSAVED,
				"preferences not saved");
	}

	/**
	 * Default folder for view files underneath portlet bundle directory or
	 * portlet application root.
	 */
	public static final String VIEW_FILE_DEFAULT_FOLDER = "html/";
}
