/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.htmlviewer.portlet.util;

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
	 * Launch-buttonless portlet preference, request parameter, and model
	 * attribute.
	 */
	public static final String LAUNCH_BUTTONLESS = "launchButtonless";

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

	/**
	 * Portlet resource bundle directory sub-folder containing the HTML files to
	 * view.
	 */
	public static final String HTML_FILE_FOLD = "/html/";

	/** Error code: internal error. */
	public static final String ERROR_CODE_INTERNAL = "error.internal";
	
	/** Error code: view file not found or empty. */
	public static final String ERROR_CODE_VIEW_FILE_NULL = "error.nullFile";

	/** Error code: view filename was blank. */
	public static final String ERROR_CODE_VIEW_FILENAME_NULL = "error.nullFilename";

	/** Error code: view filename contained path information. */
	public static final String ERROR_CODE_VIEW_FILENAME_PATH = "error.pathInFilename";

	/** Warning code: view file or token substitutions file not found or empty during config mode. */
	public static final String WARN_CODE_VIEW_FILE_NULL = "warn.nullFile";
	
	/** Status code: portlet preferences saved. */
	public static final String INFO_CODE_PREFS_SAVED = "info.saved";
	
}
