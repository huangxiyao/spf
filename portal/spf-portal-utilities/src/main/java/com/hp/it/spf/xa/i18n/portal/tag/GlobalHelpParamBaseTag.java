/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.portal.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.hp.it.spf.xa.help.portal.GlobalHelpProvider;
import com.hp.it.spf.xa.i18n.tag.MessageBaseTag;
import com.vignette.portal.log.LogWrapper;

/**
 * <p>
 * An abstract base class for all global help parameter tags, including the tags
 * for "classic"-style global help parameters (ie the
 * <code>&lt;spf-i18n-portal:i18nClassicGlobalHelpParam&gt;</code> tag). If
 * you create another style of rendering global help, and would like to render
 * that style by embedding
 * <code>&lt;Global_Help&gt;...&lt;/Global_Help&gt;</code> tokens into your
 * message string, then you should develop a global help parameter tag class by
 * subclassing from this one. Implement the abstract method which should
 * construct and return your new kind of GlobalHelpProvider; this base class
 * will set that into the parent message tag for output.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class GlobalHelpParamBaseTag extends TagSupport {

	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	private final LogWrapper LOG = new LogWrapper(GlobalHelpParamBaseTag.class);

	/**
	 * Do the tag processing. An error is thrown if the tag discovers it is not
	 * contained inside the body of a surrounding message tag (any subclass of
	 * MessageBaseTag).
	 * 
	 * @return int int
	 * @throws JspException
	 */
	public int doEndTag() throws JspException {
		Tag parent = getParent();
		if (parent == null) {
			String msg = "GlobalHelpParamBaseTag error: requires surrounding MessageTag.";
			logError(this, msg);
			throw new JspException(msg);
		}
		if (!(parent instanceof MessageBaseTag)) {
			String msg = "GlobalHelpParamBaseTag error: requires surrounding MessageTag.";
			logError(this, msg);
			throw new JspException(msg);
		}
		GlobalHelpProvider c = getGlobalHelpProvider();
		MessageTag messageTag = (MessageTag) parent;
		messageTag.addGlobalHelpProvider(c);
		return super.doEndTag();
	}

	/**
	 * Abstract method for getting the global help provider from the tag
	 * attributes. Throw a JspException if there was a problem. Different action
	 * depending on the particular style of contextual help, so this is
	 * abstract.
	 * 
	 * @return The global help provider containing the global help parameters.
	 */
	protected abstract GlobalHelpProvider getGlobalHelpProvider()
			throws JspException;

	/**
	 * Log a tag error into the portal error log file.
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
