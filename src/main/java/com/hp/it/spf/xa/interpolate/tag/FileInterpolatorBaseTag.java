/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.interpolate.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import com.hp.it.spf.xa.interpolate.FileInterpolator;

/**
 * <p>
 * Abstract base class for portal and portlet
 * <code>&lt;spf-help-portal:interpolate&gt;</code> and
 * <code>&lt;spf-help-portlet:interpolate&gt;</code> tags. These tags express
 * interpolated, localized file content, including dynamic token substitution as
 * supported by the {@link com.hp.it.spf.xa.interpolate.FileInterpolator} class
 * hierarchy. The file content, in the portlet case, comes from the named
 * resource bundle of files, in either the portlet resource bundle folder or the
 * portlet WAR. In the portal case, the file content comes from a secondary
 * support file for the current portal component.
 * </p>
 * <p>
 * Both portal and portlet tags have 2 alternative attributes, stored in this
 * base class:
 * </p>
 * <ul>
 * <li>
 * <p>
 * The <code>file="<i>base-filename</i>"</code> attribute is used to pass
 * the name of a base file for a portlet or portal resource bundle.
 * </p>
 * <ul>
 * <li>
 * <p>
 * In the portlet case, this may be a base filename for a bundle of files
 * located in the portlet resource bundle folder or in your portlet WAR. The
 * <i>base-filename</i> may include some leading path relative to that. For
 * example, <code>/images/picture.jpg</code>. In this example, the
 * <code>picture*.jpg</code> bundle of files could be located in the
 * <code>images/</code> subdirectory of the portlet resource bundle folder
 * (the location of this folder is configured in the
 * <code>i18n_portlet_config.properties</code> file). Or, the files could be
 * located in the <code>images/</code> subdirectory of the portlet
 * application.
 * </p>
 * </li>
 * <li>
 * <p>
 * In the portal case, this should be a resource bundle located among the
 * secondary support files for the current portal component (eg style or
 * secondary page).
 * </p>
 * </li>
 * </ul>
 * <p>
 * In either case, the resource files in the bundle should be tagged by locale
 * according to the Java-standard for {@link ResourceBundle}.
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>includeFile="<i>token-filename</i>"</code> attribute can
 * optionally be used to provide the name of a specific token-substitutions
 * property file. The {@link com.hp.it.spf.xa.interpolate.FileInterpolator}
 * supports substitution from this file for the <code>{INCLUDE:<i>key</i>}</code>
 * token. Please see the documentation for more information on how that token
 * works. By default, a file named <code>default_includes.properties</code>
 * is assumed for your token-substitution properties, but you can override that
 * with this tag attribute. Whether you override or accept the default, the
 * actual token-substitution file may be located anywhere that is loadable by
 * the system classloader. You can put some relative path in the
 * <code><i>token-filename</i></code> if needed to help the classloader find
 * it.
 * </p>
 * </li>
 * </ul>
 * 
 * <p>
 * Based on the locale in the current user request, the attributes above, and
 * the available localized files, the expressed file content will come from the
 * proper file. Furthermore, any special tokens supported by the
 * {@link com.hp.it.spf.xa.interpolate.FileInterpolator} class hierarchy will
 * have been substituted with their dynamic values. If no proper file existed,
 * or the file was empty, then an empty string is expressed.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class FileInterpolatorBaseTag extends TagSupport {

	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The value of the <code>file</code> attribute from the tag.
	 */
	protected String file;

	/**
	 * The value of the <code>includeFile</code> attribute from the tag.
	 */
	protected String includeFile;

	/**
	 * Initialize tag attribute values.
	 */
	public FileInterpolatorBaseTag() {
		file = null;
		includeFile = null;
	}

	/**
	 * Perform tag processing.
	 * 
	 * @throws JspException
	 * @return integer
	 */
	public int doEndTag() throws JspException {
		if (file == null) {
			String msg = "FileInterpolatorBaseTag error: the file attribute is required.";
			logError(this, msg);
			throw new JspException(msg);
		}
		JspWriter out = pageContext.getOut();
		try {
			FileInterpolator f = getFileInterpolator();
			String content = null;
			if (f != null) {
				content = f.interpolate();
			}
			if (content == null) {
				content = "";
			}
			out.print(content);
		} catch (Exception e) {
			logError(this, "FileInterpolatorBaseTag error: " + e.getMessage());
			JspException jspE = new JspException(e);
			throw jspE;
		}
		return super.doEndTag();
	}

	/**
	 * Set file from <code>file</code> attribute.
	 * 
	 * @param string
	 *            Value from the <code>file</code> tag attribute.
	 */
	public void setFile(String string) {
		file = normalize(string);
	}

	/**
	 * Get value of the <code>file</code> attribute.
	 * 
	 * @return Value of the <code>file</code> attribute.
	 */
	public String getFile() {
		return file;
	}

	/**
	 * Set token file from <code>includeFile</code> tag attribute.
	 * 
	 * @param string
	 *            Value from the <code>includeFile</code> tag attribute.
	 */
	public void setIncludeFile(String string) {
		includeFile = normalize(string);
	}

	/**
	 * Get value of the <code>includeFile</code> attribute.
	 * 
	 * @return Value of the <code>includeFile</code> attribute.
	 */
	public String getIncludeFile() {
		return includeFile;
	}

	/**
	 * Abstract method for getting the proper <code>FileInterpolator</code> to
	 * use: either a portal
	 * {@link com.hp.it.spf.xa.interpolate.portal.FileInterpolator} or a portlet
	 * {@link com.hp.it.spf.xa.interpolate.portlet.FileInterpolator}. Different
	 * action for portal and portlet, so this is an abstract method.
	 * 
	 * @return The file interpolator to use.
	 */
	public abstract FileInterpolator getFileInterpolator();

	/**
	 * Abstract method for logging a tag error. Different action for portal and
	 * portlet, so this is an abstract method.
	 * 
	 * @param obj
	 *            The object asking to log this error.
	 * @param msg
	 *            The error message.
	 */
	public abstract void logError(Object obj, String msg);

	/**
	 * Normalize blank string values to null - so the return is either a
	 * non-blank string, or null.
	 * 
	 * @param value
	 * @return
	 */
	private String normalize(String value) {
		if (value != null) {
			value = value.trim();
			if (value.equals("")) {
				value = null;
			}
		}
		return value;
	}
}
