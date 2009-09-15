/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.htmlviewer.portlet.util;

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
	 * Default view filename that will be assumed.
	 */
	public static final String DEFAULT_VIEW_FILENAME = "";

	/**
	 * Token substitutions filename portlet preference, request parameter, and
	 * model attribute.
	 */
	public static final String INCLUDES_FILENAME = "includesFilename";

	/**
	 * Default token substitutions filename that will be assumed.
	 */
	public static final String DEFAULT_INCLUDES_FILENAME = "html_viewer_includes";

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
	public static final String DEFAULT_CHECK_SECONDS = "0";

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

	/** Error code: view file not found or empty. */
	public static final String ERROR_CODE_VIEW_FILE_NULL = "error.nullFile";

	/** Error code: view filename was blank. */
	public static final String ERROR_CODE_VIEW_FILENAME_NULL = "error.nullFilename";

	/** Error code: view filename contained problem in path. */
	public static final String ERROR_CODE_VIEW_FILENAME_PATH = "error.pathInFilename";

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

	/**
	 * Default folder for view files underneath portlet bundle directory or
	 * portlet application root.
	 */
	public static final String VIEW_FILE_DEFAULT_FOLDER = "html/";
}
