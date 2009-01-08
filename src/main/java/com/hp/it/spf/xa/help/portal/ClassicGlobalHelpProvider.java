/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help.portal;

import javax.servlet.http.HttpServletRequest;

import com.vignette.portal.website.enduser.PortalContext;

import com.hp.it.spf.xa.help.portal.GlobalHelpProvider;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.Utils;

/**
 * <p>
 * A concrete global help provider, which produces the "classic"-style global
 * help popup window.
 * </p>
 * <p>
 * This is the style of global-help popup which is rendered by the portal
 * framework's <code>&lt;spf-help-portal:globalHelp&gt;</code> tag. It is also
 * the style rendered by the portal framework's
 * <code>&lt;spf-i18n-portal:i18nGlobalHelpParam&gt;</code> tag. Use that tag
 * inside the <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag body to
 * render this kind of global-help popup into a message with your chosen
 * attributes. (Using the
 * <code>&lt;spf-i18n-portal:i18nGlobalHelpParam&gt;</code> tag instantiates
 * this class.) You can also instantiate this class directly and pass it to the
 * <code>I18nUtility.getValue</code> methods of the portal framework to
 * produce this default-style global-help popup.
 * </p>
 * <p>
 * If you are not happy with the "classic" global-help popup style, you can
 * implement your own. Just extend the abstract base class, GlobalHelpProvider,
 * like this one does. You can even implement a tag for it, similar to the
 * above.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ClassicGlobalHelpProvider extends GlobalHelpProvider {

	/**
	 * Name of a request attribute which tracks how often global help link
	 * markup has been generated during this request lifecycle (ie it tracks how
	 * many times getHTML has been invoked during this request lifecycle).
	 */
	private String CLASSIC_GLOBAL_HELP_COUNTER_ATTR = "ClassicGlobalHelpProvider.count";

	/**
	 * The JavaScript string for the classic global help popup.
	 */
	private String CLASSIC_GLOBAL_HELP_JS = "<script type=\"text/javascript\" language=\"JavaScript\">\n"
			+ "var classicGlobalHelpUtil = \n"
			+ "{ \n"
			+ "    addEvent: function(elm, evType, fn) \n"
			+ "    { \n"
			+ "        if (elm.addEventListener) \n "
			+ "        { \n"
			+ "            elm.addEventListener(evType, fn, false); \n"
			+ "            return true; \n"
			+ "        }  \n"
			+ "        else if (elm.attachEvent) \n"
			+ "        { \n"
			+ "            var r = elm.attachEvent('on' + evType, fn); \n"
			+ "            return r; \n"
			+ "        } \n"
			+ "        else \n"
			+ "        { \n"
			+ "            elm['on' + evType] = fn; \n"
			+ "        }\n"
			+ "    } \n"
			+ "} \n"
			+ "function openClassicGlobalHelpWindow(e) {\n"
			+ "    var el;\n\n"
			// Try to retrieve the element that is the source of the event.
			+ "    if (window.event && window.event.srcElement) {\n"
			// IE
			+ "        el = window.event.srcElement;\n"
			+ "    }\n\n"
			+ "    if (e && e.target) {\n"
			// Firefox
			+ "        el = e.target;\n"
			+ "    }\n\n"
			// If the element can't be retrieved, exit.
			+ "    if (!el) {\n"
			+ "        return;\n"
			+ "    }\n\n"
			+ "    var url;\n"
			+ "    if (el.tagName == 'IMG') {\n"
			+ "        if (el.parentElement != null) {\n"
			+ "            url = el.parentElement.href;\n"
			+ "        }\n"
			+ "        else {\n"
			+ "            url = el.parentNode;\n"
			+ "        }\n"
			+ "    }\n"
			+ "    else {\n"
			+ "        url = el.href;\n"
			+ "    }\n\n"
			// Open the new window
			+ "    var helpWindow = window.open(url, 'spfGlobalHelp', 'height=610,width=410,menubar=no,status=no,toolbar=no,resizable=yes');\n"
			+ "    helpWindow.focus();\n\n"
			// Cancel any further bubbling of the event and disable the
			// default action.
			+ "    if (window.event) {\n"
			+ "        window.event.cancelBubble = true;\n"
			+ "        window.event.returnValue = false;\n"
			+ "    }\n\n"
			+ "    if (e && e.stopPropagation && e.preventDefault) {\n"
			+ "        e.stopPropagation();\n"
			+ "        e.preventDefault();\n"
			+ "    }\n\n"
			+ "}\n\n"
			+ "</script>";

	/**
	 * "Classic" global help has an optional page fragment - this is an anchor
	 * identifier within the global help page that you want the global help URL
	 * to point to.
	 */
	private String fragment = "";

	/**
	 * The request for this classic global help provider.
	 */
	private PortalContext portalContext = null;

	/**
	 * <p>
	 * Constructor for the "classic"-style global help provider for a particular
	 * request. If a null PortalContext is provided, the getHTML methods of this
	 * class will not work (ie will return null).
	 * </p>
	 */
	public ClassicGlobalHelpProvider(PortalContext pContext) {
		portalContext = pContext;
	}

	/**
	 * Setter for the fragment string, which is an anchor name inside the global
	 * help HTML content to which you want the global help window to auto-scroll
	 * when the global help link is clicked. Having a fragment in your global
	 * help is optional. For example, if you set the fragment to
	 * <code>foo</code>, the global help hyperlink returned by the getHTML()
	 * method (see) will, when clicked, open a global-help window that
	 * auto-scrolls to the <code>&lt;a name="foo"&gt;</code> part of the
	 * document.
	 * 
	 * @param pFragment
	 *            The anchor name (the "#" symbol at the beginning is not
	 *            needed).
	 */
	public void setFragment(String pFragment) {
		if (pFragment == null) {
			pFragment = "";
		}
		pFragment = pFragment.trim();
		while (pFragment.startsWith("#")) {
			if (pFragment.length() > 1)
				pFragment = pFragment.substring(1);
			else
				pFragment = "";
		}
		this.fragment = pFragment;
	}

	/**
	 * <p>
	 * Returns the HTML string for the "classic"-style global help, including
	 * the link content surrounded by a hyperlink which, if clicked, will reveal
	 * the global help secondary page in an appropriately-formed popup window.
	 * </p>
	 * <p>
	 * The boolean parameter controls whether or not to escape any HTML special
	 * characters like <code>&lt;</code> (ie convert them to corresponding
	 * HTML character entities so that they display literally) found in the link
	 * content.
	 * </p>
	 * <p>
	 * In this method, any HTML <code>&lt;SPAN&gt;</code> markup in the link
	 * content, which Vignette may have automatically added, is retained.
	 * </p>
	 * <p>
	 * This method requires that a valid HttpServletRequest was given to the
	 * constructor. If not, it returns null.
	 * </p>
	 * 
	 * @param escape
	 *            Whether or not to escape HTML in the link content.
	 * @return The HTML string for a global help hyperlink containing the link
	 *         content that was set.
	 */
	public String getHTML(boolean escape) {
		return getHTML(escape, false);
	}

	/**
	 * <p>
	 * Returns the HTML string for the "classic"-style global help, including
	 * the link content surrounded by a hyperlink which, if clicked, will reveal
	 * the global help secondary page in an appropriately-formed popup window.
	 * </p>
	 * <p>
	 * The first boolean parameter controls whether or not to escape any HTML
	 * special characters like <code>&lt;</code> (ie convert them to
	 * corresponding HTML character entities so that they display literally)
	 * found in the link content.
	 * </p>
	 * <p>
	 * The second boolean parameter controls whether or not to remove any HTML
	 * <code>&lt;SPAN&gt;</code> markup from the link content, which Vignette
	 * may have automatically added.
	 * </p>
	 * <p>
	 * This method requires that a valid HttpServletRequest was given to the
	 * constructor. If not, it returns null.
	 * </p>
	 * 
	 * @param escape
	 *            Whether or not to escape HTML in the link content.
	 * @param filterSpan
	 *            Whether or not to strip <code>&lt;SPAN&gt;</code> from the
	 *            link content.
	 * @return The HTML string for a global help hyperlink containing the link
	 *         content that was set.
	 */
	public String getHTML(boolean escape, boolean filterSpan) {

		if (portalContext == null) {
			return null;
		}
		HttpServletRequest request = portalContext.getHttpServletRequest();
		if (request == null) {
			return null;
		}

		// Get the global help link counter which tracks any previous
		// invocation.
		int count = 0;
		Object countString = request
				.getAttribute(CLASSIC_GLOBAL_HELP_COUNTER_ATTR);
		if (countString == null) {
			count = 0;
		} else {
			try {
				count = Integer.parseInt((String) countString);
			} catch (NumberFormatException e) { // should never happen
				count = 0;
			}
		}

		// If common JavaScript has not previously been returned, then include
		// it now. Bump the counter.
		StringBuffer html = new StringBuffer();
		if (count == 0) {
			html.append(CLASSIC_GLOBAL_HELP_JS);
		}
		++count;

		// Make the anchor ID.
		String id = "classicGlobalHelp" + count;

		// Make the link text.
		String link = this.linkContent;
		if (filterSpan) { // Remove Vignette-introduced <SPAN> tags if needed.
			link = I18nUtility.filterSpan(link);
		}
		if (escape) { // Escape XML meta-characters if needed.
			link = Utils.escapeXml(link);
		}

		// Make the URI for the global help.
		String uri = portalContext.createDisplayURI(Consts.PAGE_GLOBAL_HELP)
				.toString();
		if (this.fragment != null) {
			uri += "#" + fragment;
		}

		// Generate the main HTML and event-handling code by assembling the
		// pieces.
		html.append("<a ");
		html.append("id=\"" + id + "\" ");
		html.append("href=\"" + uri + "\" ");
		html.append(">" + link + "</a>");
		html.append("<script>");
		html.append("classicGlobalHelpUtil.addEvent(document.getElementById('"
				+ id + "'), 'click', openClassicGlobalHelpWindow);");
		html.append("</script>");

		// Store the counter to remember how many links we have done.
		request.setAttribute(CLASSIC_GLOBAL_HELP_COUNTER_ATTR, String
				.valueOf(count));
		return html.toString();
	}

	/**
	 * <p>
	 * Use this method at any time during the request lifecycle, if for some
	 * reason you need the next getHTML invokation to return the common
	 * JavaScript again. This generally is not recommended, because if you
	 * aggregate in the same page multiple getHTML results, which were generated
	 * before and after a resetHTML, a JavaScript runtime conflict will arise.
	 * </p>
	 * 
	 */
	public void resetHTML() {
		if (portalContext != null) {
			HttpServletRequest request = portalContext.getHttpServletRequest();
			if (request != null) {
				request.removeAttribute(CLASSIC_GLOBAL_HELP_COUNTER_ATTR);
			}
		}
	}
}
