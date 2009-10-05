/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.i18n.portal.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.xa.help.portal.ClassicGlobalHelpProvider;
import com.hp.it.spf.xa.help.portal.GlobalHelpProvider;
import com.hp.it.spf.xa.i18n.portal.tag.GlobalHelpParamBaseTag;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * A class representing the
 * <code>&lt;spf-i18n-portal:i18nClassicGlobalHelpParam&gt;</code> tag. You
 * use that tag to define parameters for surrounding
 * <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tags which contain
 * <code>&lt;Global_Help&gt;...&lt;/Global_Help&gt;</code> tokens. You place
 * the <code>&lt;spf-i18n-portal:i18nClassicGlobalHelpParam&gt;</code> tag
 * inside the body of the surrounding
 * <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag, in order to pass
 * global-help parameters (in that order) into the message.
 * </p>
 * The style of global help rendered by this tag is the SPF-provided "classic"
 * style, which opens a child window. It has the following optional attributes:
 * </p>
 * <ul>
 * <li>
 * <p>
 * <code>fragment="<i>fragment-name</i>"</code> can optionally be used to
 * specify a fragment name in the global help page. If that attribute is
 * provided, the global help hyperlink expressed by the parent tag will include
 * an anchor to that fragment (otherwise it will just point to the top of the
 * global help page).
 * </p>
 * </li>
 * <li>
 * <p>
 * The <code>windowFeatures="<i>window-features</i>"</code> attribute gives
 * the set of JavaScript window properties to use for the global help child
 * window. This is specified using any valid JavaScript <code>window.open</code>
 * features name/value pairs properties. For example,
 * <code>windowFeatures="height=610,width=410,menubar=no,status=no,toolbar=no,resizable=yes"</code>
 * (which is also the default: a 610-by-410 pixel child window with no tools or
 * controls).
 * </p>
 * </li>
 * </ul>
 * <p>
 * Note this tag is for the "classic"-style rendering of global help. If you
 * would like a custom style, you must implement your own custom tag for it.
 * Like the above tags, your custom tag would be used inside a message tag body.
 * Implement a GlobalHelpProvider concrete subclass for that custom style. Then
 * implement a tag class for it, like this one. Have the tag construct the
 * appropriate kind of GlobalHelpProvider subclass corresponding to that custom
 * style, and have the tag add it into the parent message tag's list.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ClassicGlobalHelpParamTag extends GlobalHelpParamBaseTag {

	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Stores the value of the <code>fragment</code> attribute.
	 */
	private String fragment;

	/**
	 * Stores the value of the <code>windowFeatures</code> attribute.
	 */
	protected String windowFeatures;

	/**
	 * Get the value of the <code>fragment</code> attribute.
	 * 
	 * @return The <code>fragment</code> attribute.
	 */
	public String getFragment() {
		return fragment;
	}

	/**
	 * Set the value from the <code>fragment</code> attribute, with blank
	 * values normalized to null.
	 * 
	 * @param value
	 *            The <code>fragment</code> attribute.
	 */
	public void setFragment(String value) {
		this.fragment = normalize(value);
	}

	/**
	 * Set the value from the <code>windowFeatures</code> attribute. Blank is
	 * not normalized to null.
	 * 
	 * @param value
	 *            Value of the <code>windowFeatures</code> attribute.
	 */
	public void setWindowFeatures(String value) {
		this.windowFeatures = value;
	}

	/**
	 * Get the value of the <code>windowFeatures</code> attribute.
	 * 
	 * @return Value of the <code>windowFeatures</code> attribute.
	 */
	public String getWindowFeatures() {
		return windowFeatures;
	}

	/**
	 * Initialize the tag attributes.
	 */
	public ClassicGlobalHelpParamTag() {
		super();
		fragment = null;
		windowFeatures = null;
	}

	/**
	 * Return an instance of the "classic"-style global help provider, populated
	 * with the parameters from the current tag. This throws a JspException if
	 * the required parameters for that provider (currently, none) were not
	 * specified in the tag. This method leaves the link content unpopulated;
	 * the surrounding tag will populate that.
	 * 
	 * @return GlobalHelpProvider
	 * @throws JspException
	 */
	protected GlobalHelpProvider getGlobalHelpProvider() throws JspException {
		PortalContext portalContext = (PortalContext) pageContext.getRequest()
				.getAttribute("portalContext");
		ClassicGlobalHelpProvider g = new ClassicGlobalHelpProvider(
				portalContext);
		g.setFragment(fragment);
		g.setWindowFeatures(windowFeatures);
		return g;
	}

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
