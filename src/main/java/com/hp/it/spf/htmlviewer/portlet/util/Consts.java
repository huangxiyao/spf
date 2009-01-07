/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.htmlviewer.portlet.util;

/**
 * Container class for common constants shared across multiple artifacts in the
 * HTMLViewer portlet.
 * 
 * @author <link href="xiao-bing.zuo@hp.com">Zuo Xiaobing</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link
 * @version TBD
 */
public class Consts {

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
	public static final String ERROR_MESSAGE = "errorMessage";

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
	public static final String ERROR_CODE_FILE_NULL = "error.nullFile";

	/** Error code: view filename was blank. */
	public static final String ERROR_CODE_VIEW_FILENAME_NULL = "error.nullFilename.";

	/** Error code: view filename contained path information. */
	public static final String ERROR_CODE_VIEW_FILENAME_PATH = "error.pathInFilename";

}
