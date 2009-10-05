/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.portal.tag;

import com.hp.it.spf.xa.i18n.tag.ParamBaseTag;
import com.vignette.portal.log.LogWrapper;

/**
 * <p>
 * A class representing the portal framework's
 * <code>&lt;spf-i18n-portal:i18nParam&gt;</code> tag. You use that tag to define
 * string parameter values for surrounding
 * <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tags (see). You place the
 * string parameter tag inside the body of the surrounding message tag, in
 * order, to pass string parameters in that order into the message.
 * </p>
 * <p>
 * The <code>&lt;spf-i18n-portal:i18nParam&gt;</code> tag has one attribute,
 * <code>value="<i>param</i>"</code> where <i>param</i> is the value of a
 * parameter to be substituted. It is a required attribute.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ParamTag extends ParamBaseTag {

	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	private final LogWrapper LOG = new LogWrapper(ParamTag.class);

	/**
	 * Log a tag error into the portlet error log file.
	 * 
	 * @param obj
	 *            The object asking to log this error.
	 * @param msg
	 *            The error message.
	 */
	public void logError(Object obj, String msg) {
		LOG.error(msg);
	}

}
