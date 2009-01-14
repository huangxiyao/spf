/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.xa.help.ContextualHelpProvider;
import com.hp.it.spf.xa.i18n.I18nUtility;
import com.hp.it.spf.xa.misc.Utils;

/**
 * <p>
 * An abstract contextual help provider, which produces a "classic"-style
 * contextual help popup window. Although this class is abstract, it does most
 * of the work in producing the markup for that popup window. It delegates to
 * the concrete subclass only the work of state tracking (since the
 * implementation of that varies for portal and portlet).
 * </p>
 * <p>
 * <b>In the portlet framework:</b> This is the style of contextual-help popup
 * which is rendered by the
 * <code>&lt;spf-help-portlet:classicContextualHelp&gt;</code> tag. This is
 * also the style of contextual-help popup which is rendered by the
 * <code>&lt;spf-i18n-portlet:classicContextualHelpParam&gt;</code> tag. Use
 * that tag inside the <code>&lt;spf-i18n-portlet:message&gt;</code> tag body
 * to render this kind of contextual-help popup into a message with your chosen
 * attributes. (Using the
 * <code>&lt;spf-i18n-portlet:classicContextualHelpParam&gt;</code> tag
 * instantiates this class.) You can also instantiate this class directly, and
 * pass it to the portlet <code>I18nUtility.getMessage</code> methods to
 * produce a message containing this style of contextual-help popup.
 * </p>
 * <p>
 * <b>In the portal framework:</b> This is the style of contextual-help popup
 * which is rendered by the
 * <code>&lt;spf-help-portal:classicContextualHelp&gt;</code> tag. This is
 * also the style of contextual-help popup which is rendered by the
 * <code>&lt;spf-i18n-portal:i18nClassicContextualHelpParam&gt;</code> tag.
 * Use this tag inside the <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag
 * body to render this kind of contextual-help popup into a message with your
 * chosen attributes. (Using the
 * <code>&lt;spf-i18n-portal:i18nClassicContextualHelpParam&gt;</code> tag
 * instantiates this class.) You can also instantiate this class directly and
 * pass it to the portal <code>I18nUtility.getValue</code> methods to produce
 * a message containing this style of contextual-help popup.
 * </p>
 * <p>
 * If you are not happy with the "classic" contextual-help popup style, you can
 * implement your own. Just extend the abstract base class like this one does.
 * You can even implement a tag for it, similar to the ones mentioned above.
 * </p>
 * <p>
 * TODO: Need to finish implementing this class. The logic for the
 * implementation should largely come from the ContextualHelpBaseTag.todo file
 * (taken from the ContextualHelpBaseTag.java file in the Service Portal (OS)
 * code).
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class ClassicContextualHelpProvider extends
		ContextualHelpProvider {

	/**
	 * Name of a request attribute which tracks how often contextual help link
	 * markup has been generated during this request lifecycle (ie it tracks how
	 * many times getHTML has been invoked during this request lifecycle).
	 */
	protected static final String CLASSIC_CONTEXTUAL_HELP_COUNTER_ATTR = "ClassicContextualHelpProvider.count";

	/**
	 * Constant for close image file on the div.
	 */
	protected static final String CLOSE_BUTTON_IMG_NAME = "btn_close.gif";

	/**
	 * Constant for message key for close image alt on the div.
	 */
	protected static final String CLOSE_BUTTON_IMG_ALT = "contextualHelp.close.alt";

	/**
	 * The JavaScript string for the classic contextual help popup.
	 */
	private static String CLASSIC_CONTEXTUAL_HELP_JS = "<script type=\"text/javascript\" language=\"JavaScript\">\n"
			+ "if (typeof(loadedClassicContextualHelpJS) == 'undefined') \n"
			+ "{ \n"
			+ "    var loadedContextualHelpJS = true; \n"
			+ "    var classicContextualHelpUtil = \n"
			+ "    { \n"
			+ "        addEvent: function(elm, evType, fn) \n"
			+ "        { \n"
			+ "            if (elm.addEventListener) \n "
			+ "            { \n"
			+ "                elm.addEventListener(evType, fn, false); \n"
			+ "                return true; \n"
			+ "            }  \n"
			+ "            else if (elm.attachEvent) \n"
			+ "            { \n"
			+ "                var r = elm.attachEvent('on' + evType, fn); \n"
			+ "                return r; \n"
			+ "            } \n"
			+ "            else \n"
			+ "            { \n"
			+ "                elm['on' + evType] = fn; \n"
			+ "            }\n"
			+ "        } \n"
			+ "    } \n"
			+ "    function isMSIE() {\n"
			+ "        if (navigator.appName == 'Microsoft Internet Explorer') \n"
			+ "            return true; \n"
			+ "        else \n"
			+ "            return false; \n"
			+ "    } \n"
			+ "    var lastShow = null;\n"
			+ "    function hideFrame(o) {\n"
			+ "        if (isMSIE() == true && o != null) {\n"
			+ "            var wframe = document.getElementById(o.id+'Frame'); \n"
			+ "            if (wframe != null) {\n"
			+ "                wframe.style.display = \"none\"; \n"
			+ "                wframe.style.zIndex = 0; \n"
			+ "            } \n"
			+ "        } \n"
			+ "    } \n"
			+ "    function hideObject(ev) {\n"
			+ "        var e = window.event ? window.event : ev; \n"
			+ "        var t = e.target ? e.target : e.srcElement; \n"

			// set help div element to none display, extract div element
			// name from close image id by removing 'Close' string from it.
			+ "        var w = document.getElementById(t.id.substr(0, t.id.lastIndexOf('Close'))); \n"
			+ "        if (w != null) { \n "
			+ "            w.style.display = \"none\"; \n"
			+ "            w.style.zIndex=0; \n"
			+ "            hideFrame(w); \n"
			+ "        } \n"
			+ "    } \n"
			+ "    function showFrame(o) { \n"
			+ "        if (isMSIE() == true && o != null) { \n"
			+ "            var wframe = document.getElementById(o.id+'Frame'); \n"
			+ "            if (wframe != null) { \n"
			+ "                wframe.style.top = o.offsetTop; \n"
			+ "                wframe.style.left = o.offsetLeft; \n"
			+ "                wframe.style.width=o.offsetWidth; \n"
			+ "                wframe.style.height=o.offsetHeight; \n"
			+ "                wframe.style.display = \"block\"; \n"
			+ "                wframe.style.zIndex=4999; \n"
			+ "            } \n"
			+ "        } \n"
			+ "    } \n"
			+ "    function showObject(ev) { \n"
			+ "        var e = window.event ? window.event : ev; \n"
			+ "        var t = e.target ? e.target : e.srcElement; \n"

			// use the lastShow variable to close the last popup window, if any,
			// before opening the new popup window.
			+ "        if (lastShow != null) { \n"
			+ "            lastShow.style.display = \"none\"; \n"
			+ "            lastShow.style.zIndex=0; \n"
			+ "            hideFrame(lastShow); \n"
			+ "        } \n"

			// set help div element to block display, get div element
			// name by concatenating 'Close' string to anchor id.
			+ "        var w = document.getElementById(t.id + 'Help'); \n"
			+ "        if (w != null) { \n"
			+ "            lastShow = w; \n"
			+ "            w.style.display = \"block\"; \n"
			+ "            w.style.left = e.clientX + \"px\"; \n"
			+ "            w.style.zIndex=5999; \n"
			+ "            var spaceNeeded; \n"
			+ "            var spaceAvail; \n"
			+ "            if (document.documentElement && document.documentElement.clientWidth) { \n"

			// IE6 and Firefox
			+ "                frameWidth = document.documentElement.clientWidth; \n"
			+ "                frameHeight = document.documentElement.clientHeight; \n"
			+ "                scrollTop = document.documentElement.scrollTop; \n"
			+ "                scrollLeft = document.documentElement.scrollLeft; \n"
			+ "            } \n"
			+ "            else if (document.body) { \n"

			// IE5
			+ "                frameWidth = document.body.clientWidth; \n"
			+ "                frameHeight = document.body.clientHeight; \n"
			+ "                scrollTop = document.body.scrollTop; \n"
			+ "                scrollLeft = document.body.scrollLeft; \n"
			+ "            } \n"

			// Calculate Y position to put popup
			+ "            spaceAvail = frameHeight - e.clientY; \n"
			+ "            if (spaceAvail > w.offsetHeight) { \n"

			// display top of popup at cursor if there's enough space
			// below cursor position to display entire popup
			+ "                w.style.top = e.clientY + scrollTop + \"px\"; \n"
			+ "            } else { \n"

			// top window is big enough to display popup
			+ "                spaceNeeded = w.offsetHeight - spaceAvail; \n"
			+ "                if (spaceNeeded < e.clientY) { \n"

			// there's room in top window to display popup, position
			// popup as high as possible in top window to fit
			+ "                    w.style.top = e.clientY + scrollTop - spaceNeeded + \"px\"; \n"
			+ "                } else { \n"
			+ "                    if (e.clientY > 100) { \n"

			// popup won't fit top window, so just position popup
			// at 25 pixel for margin top space, as long as top
			// window has has more than 100 pixels,
			+ "                        w.style.top = 25 + scrollTop + \"px\"; \n"
			+ "                    } else { \n"

			// no margin top space, just display top of popup
			// at cursor position
			+ "                        w.style.top = e.clientY + scrollTop + \"px\"; \n"
			+ "                    } \n"
			+ "                } \n"
			+ "            } \n"

			// Calculate X position to put popup - same algorithm as
			// displaying Y position above
			+ "            spaceAvail = frameWidth - e.clientX; \n"
			+ "            if (spaceAvail > w.offsetWidth) { \n"
			+ "                w.style.left = e.clientX + scrollLeft + \"px\"; \n"
			+ "            } else { \n"
			+ "                spaceNeeded = w.offsetWidth - spaceAvail; \n"
			+ "                if (spaceNeeded < e.clientX) { \n"
			+ "                    w.style.left = e.clientX + scrollLeft - spaceNeeded + \"px\"; \n"
			+ "                } else { \n"
			+ "                    if (e.clientX > 100) { \n"
			+ "                        w.style.left = 25 + scrollLeft + \"px\"; \n"
			+ "                    } else { \n"
			+ "                        w.style.left = e.clientX + scrollLeft + \"px\"; \n"
			+ "                    } \n"
			+ "                } \n"
			+ "            } \n"
			+ "            showFrame(w); \n"
			+ "        } \n"
			+ "        if (window.event) { \n"
			+ "            window.event.cancelBubble = true; \n"
			+ "            window.event.returnValue = false; \n"
			+ "        } \n"
			+ "        if (e && e.stopPropagation && e.preventDefault) { \n"
			+ "            e.stopPropagation(); \n"
			+ "            e.preventDefault(); \n"
			+ "        } \n"
			+ "    } \n"

			// Append the JavaScript code for div moving
			// These variables are used to get mouse position, calculate and
			// change div position.
			+ "    var mousex = 0,mousey = 0,grabx = 0,graby = 0,orix = 0,oriy = 0,elex = 0,eley = 0; \n"
			+ "    var dragobj = null; \n"

			// Update the mousex,mousey when mouse is moving
			+ "    document.onmousemove = update; \n"
			+ "    update(); \n\n"

			// Used to block cascading events
			+ "    function falsefunc() { return false; } \n"

			// Used to call getMouseXY();
			+ "    function update(e) { getMouseXY(e); } \n"

			// Used to get position of mouse, and work on IE6,FireFox.
			+ "    function getMouseXY(e) \n"
			+ "    { \n"
			+ "        if (!e) e = window.event; \n"
			+ "        if (e) \n"
			+ "        { //Work on FF \n"
			+ "            if (e.pageX || e.pageY) \n"
			+ "            { \n"
			+ "                mousex = e.pageX; \n"
			+ "                mousey = e.pageY; \n"
			+ "            } \n"
			+ "            else if (e.clientX || e.clientY) \n"
			+ "            { \n"
			+ "                mousex = e.clientX + document.body.scrollLeft; \n"
			+ "                mousey = e.clientY + document.body.scrollTop; \n"
			+ "            } \n"
			+ "        } \n"
			+ "    } \n"

			// Set this method to mousedown event of div
			+ "    function grab(context) \n"
			+ "    { \n"
			+ "        document.onmousedown = falsefunc; \n"
			+ "        dragobj = context;  \n"
			+ "        //dragobj.style.zIndex = 10; \n"
			// move it to the top
			+ "        document.onmousemove = drag; \n"
			+ "        document.onmouseup = drop; \n"
			+ "        grabx = mousex; \n"
			+ "        graby = mousey; \n"
			+ "        elex = orix = dragobj.offsetLeft; \n"
			+ "        eley = oriy = dragobj.offsetTop; \n"
			+ "        update(); \n"
			+ "    } \n\n"

			+ "    function moveFrame(o) \n"
			+ "    { \n"
			+ "        if (isMSIE() == true && o != null) { \n"
			+ "            var wframe = document.getElementById(o.id+'Frame'); \n"
			+ "            if (wframe != null) { \n"
			+ "                wframe.style.top = o.offsetTop; \n"
			+ "                wframe.style.left = o.offsetLeft; \n"
			+ "            } \n"
			+ "        } \n"
			+ "    } \n"

			// When mouse drags the div, this method is called to update the div
			// position
			+ "    function drag(e) \n"
			+ "    { \n"
			+ "        if (dragobj) \n"
			+ "        { \n"
			+ "            elex = orix + (mousex-grabx); \n"
			+ "            eley = oriy + (mousey-graby); \n"
			+ "            dragobj.style.position = 'absolute'; \n"
			+ "            dragobj.style.left = (elex).toString(10) + 'px'; \n"
			+ "            dragobj.style.top  = (eley).toString(10) + 'px'; \n"
			+ "            moveFrame(dragobj); \n"
			+ "        } \n"
			+ "        update(e); \n"
			+ "        return false; \n"
			+ "    } \n"

			// When mouse drop the div, this method is called to clear dragobj
			// and some event handler
			+ "    function drop() \n"
			+ "    { \n"
			+ "        if (dragobj) \n "
			+ "            dragobj = null; \n"
			+ "        update(); \n"
			+ "        document.onmousemove = update; \n"
			+ "        document.onmouseup = null; \n"
			+ "        document.onmousedown = null; \n" + "    } \n"

			+ "} \n" + "</script>";

	/**
	 * "Classic" contextual help has a title. This is the title content.
	 */
	protected String titleContent = "";

	/**
	 * "Classic" contextual help can put an alternate URL in the contextual-help
	 * hyperlink which the browser will open if JavaScript is not supported.
	 * ("Classic" contextual help style requires JavaScript, so this allows for
	 * a noscript alternative.)
	 */
	protected String noScriptHref = "";

	/**
	 * Protected to prevent external construction except by subclasses. Use an
	 * appropriate subclass instead.
	 */
	protected ClassicContextualHelpProvider() {

	}

	/**
	 * Setter for the title content string: any string of text or HTML which you
	 * want to display as the title of the "classic"-style contextual-help
	 * popup. Depending on how the contextual help is invoked, your title
	 * content may or may not later be escaped (ie conversion of HTML special
	 * characters like <code>&lt;</code> inside the title content to their
	 * corresponding HTML character entities). When using this method, you
	 * should pass unescaped content.
	 * 
	 * @param pTitleContent
	 *            The title content.
	 */
	public void setTitleContent(String pTitleContent) {
		if (pTitleContent == null) {
			pTitleContent = "";
		}
		pTitleContent = pTitleContent.trim();
		this.titleContent = pTitleContent;
	}

	/**
	 * Setter for the noscript URL: any URL you want to be opened instead of the
	 * contextual-help popup in a non-JavaScript-enabled browser.
	 * "Classic"-style contextual help requires the use of JavaScript, so this
	 * lets you provide an alternative experience for non-JavaScript-enabled
	 * browsers.
	 * 
	 * @param pNoScriptHref
	 *            A URL to fallback upon in the noscript case.
	 */
	public void setNoScriptHref(String pNoScriptHref) {
		if (pNoScriptHref == null) {
			pNoScriptHref = "";
		}
		pNoScriptHref = pNoScriptHref.trim();
		this.noScriptHref = pNoScriptHref;
	}

	/**
	 * <p>
	 * Returns the HTML string for the "classic"-style contextual help,
	 * including the link content surrounded by a hyperlink which, if clicked,
	 * will reveal the help content in an appropriately-formed DHTML popup
	 * layer.
	 * </p>
	 * <p>
	 * The boolean parameter controls whether or not to escape any HTML special
	 * characters like <code>&lt;</code> (ie convert them to corresponding
	 * HTML character entities so that they display literally) found in the
	 * content.
	 * </p>
	 * 
	 * TODO: Must implement this method, see notes above.
	 * 
	 * @param escape
	 * @return
	 */
	public String getHTML(boolean escape) {

		// Get the contextual help link counter which tracks any previous
		// invocation.
		int count = getClassicContextualHelpCounter();

		// If common JavaScript has not previously been returned, then include
		// it now. Bump the counter.
		StringBuffer html = new StringBuffer();
		if (count == 0) {
			html.append(CLASSIC_CONTEXTUAL_HELP_JS);
		}
		++count;

		// Make the element ID.
		String id = "classicContextualHelp" + count;

		// Make the link, title, and help contents.
		String link = this.linkContent;
		String title = this.titleContent;
		String help = this.helpContent;
		// Add element ID to <img> (it does not already have it and the script
		// needs it).
		if (link.startsWith("<img ")) {
			link = "<img id=\"" + id + "\" " + link.substring(5);
		}
		// Escape XML meta-characters if needed.
		if (escape) {
			link = Utils.escapeXml(link);
			title = Utils.escapeXml(title);
			help = Utils.escapeXml(help);
		}
		// Remove special <NO_LOCALIZATION> markup.
		link = I18nUtility.filterNoLocalizationTokens(link);
		title = I18nUtility.filterNoLocalizationTokens(title);
		help = I18nUtility.filterNoLocalizationTokens(help);

		// Make the noscript URL.
		String noscriptUrl = getNoScriptURL();

		// Make the close button image URL and alt message.
		String closeButtonUrl = getCloseImageURL();
		String closeButtonAlt = getCloseImageAlt();

		// Generate the main HTML and event-handling code by assembling the
		// pieces. First, assemble the event-handler script.

		html.append("<script>\n");
		if (link.startsWith("<img ")) {
			// If the anchor text is an image tag, then don't need an
			// anchor tag to surround it.
			html.append("document.write('" + link + "');\n");
		} else {
			html.append("document.write('<a href=\"javascript:noop\" id=\""
					+ id + "\">" + link + "</a>');\n");
		}
		// Write html to call javascript popup function
		html
				.append("classicContextualHelpUtil.addEvent(document.getElementById('"
						+ id + "'), 'click', showObject);\n");
		// Add iframe to avoid IE select bug
		html.append("if (isMSIE() == true) \n");
		html
				.append("    document.write('<iframe id=\""
						+ id
						+ "HelpFrame\" src=\"javascript:false;\" style=\"position:absolute;background-color:white;display:none;\"></iframe>');\n");
		html.append("</script>");

		// Next, add the noscript for unscripted browsers. Use the noscript URL
		// if one was returned.
		html.append("<noscript>");
		if (noscriptUrl != null) {
			html.append("<a href=\"" + noscriptUrl + "\" target=\"_self\">"
					+ link + "</a>");
		} else {
			html.append(link);
		}
		html.append("</noscript>");

		// Next, write the popup window (layer) itself.

		html.append("<div ");
		html.append("id=\"" + id + "Help\" ");
		html.append("onmousedown=\"grab(this)\" ");
		html
				.append("style=\"cursor:pointer;position:absolute;background-color:white;display:none;top:200px;left:200px;border: 1px solid black;\">\n");
		html.append("<table width=300 cellpadding=2 cellspacing=0 border=0>\n");
		html.append("<tr height=2 bgcolor=\"#0000FF\"><td colspan=3 /></tr>\n");
		html.append("<tr valign=middle height=20>\n");
		// Write title bar with title string and close button, with surrounding
		// top and bottom cells for padding.
		html.append("<td align=left bgcolor=\"#0000FF\" height=20>");
		html.append("<font color=\"#FFFFFF\"><strong>&nbsp;"
				+ escapeQuotes(title) + "</strong></font>");
		html.append("</td>\n");
		html.append("<td bgcolor=\"#0000FF\">&nbsp;</td>");
		html.append("<td bgcolor=\"#0000FF\">");
		html.append("<img id=\"" + id + "HelpClose\" ");
		html.append("src=\"" + closeButtonUrl + "\" ");
		html.append("style=\"cursor:pointer\" alt=\"" + closeButtonAlt + "\" ");
		html
				.append("width=\"15\" height=\"15\" align=right border=\"0\"/ ></td>\n");
		// Right padding for button
		html.append("</tr>\n");
		html.append("<tr height=2 bgcolor=\"#0000FF\"><td colspan=3 /></tr>\n");
		html.append("<tr valign=top height=\"100%\">\n");
		html.append("<td colspan=3>\n");
		html.append("<table width=300 cellpadding=10 cellspacing=10>\n");
		html.append("<tr><td align=left>\n");
		// Write popup content
		html.append("<p>");
		html.append(escapeQuotes(help));
		html.append("</p></td></tr>\n");
		html.append("</table></td>\n");
		html.append("</tr></table>\n");
		html.append("</div>");

		// Finally, write script which adds event for close button.

		html.append("<script>\n");
		html
				.append("classicContextualHelpUtil.addEvent(document.getElementById('"
						+ id + "HelpClose'), 'click', hideObject);\n");
		html.append("</script>");

		// Bump counter and return.
		bumpClassicContextualHelpCounter();
		return html.toString();
	}

	/**
	 * <p>
	 * Use this method at any time during the request lifecycle, if for some
	 * reason you need the next getHTML invocation to return the common
	 * JavaScript again. This generally is not recommended.
	 * </p>
	 * 
	 */
	public void resetHTML() {
		resetClassicContextualHelpCounter();
	}

	/**
	 * An abstract method to get a counter of how many times in this request
	 * lifecycle a classic contextual help provider has been generated (ie its
	 * getHTML method has been invoked). Different action for portal and
	 * portlet, so this is an abstract method.
	 * 
	 * @return The counter.
	 */
	protected abstract int getClassicContextualHelpCounter();

	/**
	 * An abstract method to increment the counter of how many times in this
	 * request lifecycle a classic contextual help provider has been generated
	 * (ie its getHTML method has been invoked). Different action for portal and
	 * portlet, so this is an abstract method.
	 */
	protected abstract void bumpClassicContextualHelpCounter();

	/**
	 * An abstract method to reset the counter of how many times in this request
	 * lifecycle a classic contextual help provider has been generated (ie its
	 * getHTML method has been invoked). Different action for portal and
	 * portlet, so this is an abstract method.
	 */
	protected abstract void resetClassicContextualHelpCounter();

	/**
	 * An abstract method to generate the hyperlink URL to use for contextual
	 * help in the noscript case. Different action for portal and portlet, so
	 * this is an abstract method.
	 */
	protected abstract String getNoScriptURL();

	/**
	 * An abstract method to generate the image URL for the popup close button.
	 * Different action for portal and portlet, so this is an abstract method.
	 */
	protected abstract String getCloseImageURL();

	/**
	 * An abstract method to get the image alt text for the popup close button.
	 * Different action for portal and portlet, so this is an abstract method.
	 */
	protected abstract String getCloseImageAlt();

	/**
	 * Returns the input string with all occurrences of the double-quote
	 * character <code>"</code> replaced with the <code>\"</code> character
	 * sequence.
	 * 
	 * @param input
	 *            String
	 * @return The string with escaped quotes
	 */
	private String escapeQuotes(String input) {

		if (input == null)
			return null;
		StringBuffer buffer = new StringBuffer();
		StringTokenizer st = new StringTokenizer(input, "\"", true);

		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.equals("\"")) {
				buffer.append("\\\"");
			} else {
				buffer.append(token);
			}
		}
		return buffer.toString();
	}

	/**
	 * <p>
	 * Returns the given path, with any consecutive file separators ("/" for
	 * Java) reduced to just one.
	 * </p>
	 * 
	 * @param pPath
	 *            The file path to clean-up.
	 * @return The cleaned-up file path.
	 */
	protected String slashify(String pPath) {
		if (pPath == null) {
			return null;
		}
		return pPath.replaceAll("/+", "/");
	}
}
