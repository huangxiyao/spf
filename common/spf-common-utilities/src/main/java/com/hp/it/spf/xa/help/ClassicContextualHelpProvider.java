/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help;

import java.util.StringTokenizer;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.xa.help.ContextualHelpProvider;
import com.hp.it.spf.xa.i18n.I18nUtility;
import com.hp.it.spf.xa.misc.Utils;

/**
 * <p>
 * An abstract contextual help provider, which produces a "classic"-rendition
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
 * If you are not happy with the "classic" contextual-help popup rendition, you
 * can implement your own. Just extend the abstract base class like this one
 * does. You can even implement a tag for it, similar to the ones mentioned
 * above.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.help.ContextualHelpProvider</code><br>
 *      <code>com.hp.it.spf.xa.help.portal.ClassicContextualHelpProvider</code><br>
 *      <code>com.hp.it.spf.xa.help.portlet.ClassicContextualHelpProvider</code>
 */
public abstract class ClassicContextualHelpProvider extends
		ContextualHelpProvider {

	/**
	 * Name of a request attribute which tracks how often contextual help link
	 * markup has been generated during this request lifecycle (ie it tracks how
	 * many times getHTML has been invoked during this request lifecycle).
	 */
	protected static String CLASSIC_CONTEXTUAL_HELP_COUNTER_ATTR = "ClassicContextualHelpProvider.count";

	/**
	 * Constant for close image file in the classic popup.
	 */
	protected static String CLOSE_BUTTON_IMG_NAME = "btn_close.gif";

	/**
	 * Constant for message key for close image alt in the classic popup.
	 */
	protected static String CLOSE_BUTTON_IMG_ALT = "contextualHelp.close.alt";

    /**
     * Constant for message key for close image URL in the classic popup.
     */
    protected static String CLOSE_BUTTON_IMG_URL = "contextualHelp.close.url";

    /**
	 * Default width for classic popup.
	 */
	protected static int DEFAULT_WIDTH = 300;

	/**
	 * Default border style for classic popup.
	 */
	protected static String DEFAULT_BORDER_STYLE = "border-width:1px;border-style:solid;border-color:black";

	/**
	 * Default title style for classic popup.
	 */
	protected static String DEFAULT_TITLE_STYLE = "background-color:blue;color:white;font-weight:bold";

	/**
	 * Default help content style for classic popup.
	 */
	protected static String DEFAULT_HELP_STYLE = "background-color:white;color:black;font-weight:normal";

	/**
	 * The CSS string for static CSS classes used by the classic contextual
	 * help.
	 */
	protected static String CLASSIC_CONTEXTUAL_HELP_STYLE = "<style>\n"
			+ "/* Workaround for IE6 <SELECT> bug */\n" + ".select-ie6 {\n"
			+ "    position: absolute;\n" + "    z-index: 10;\n"
			+ "    cursor: move;\n" + "    overflow: hidden;\n" + "}\n"
			+ ".select-ie6 iframe {\n" + "    display: none;\n"
			+ "    display: block;\n" + "    position:absolute;\n"
			+ "    top: 0;\n" + "    left: 0;\n" + "    z-index: -1;\n"
			+ "    filter: mask();\n" + "    height: 9999px;\n" + "}\n"
			+ "</style>";

	/**
	 * The JavaScript string for the classic contextual help popup open, close,
	 * and drag-and-drop behavior. The popup itself is not defined in this
	 * JavaScript.
	 * 
	 * Fix for CR# 308: now all global variable and function names in this
	 * JavaScript are namespaced with "cch" if they were not adequately
	 * namespaced previously.  This is to avoid conflicts with other DHTML
	 * code that may be on the same page, such as MooTools (used by Cleansheet)
	 * - DSJ 2011/7/28
	 */
	protected static String CLASSIC_CONTEXTUAL_HELP_JS = "<script type=\"text/javascript\" language=\"JavaScript\">\n"
			+ "if (typeof(loadedClassicContextualHelpJS) == 'undefined') \n"
			+ "{ \n"
			+ "    var loadedClassicContextualHelpJS = true; \n"
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
			+ "    function cchIsMSIE() {\n"
			+ "        if (navigator.appName == 'Microsoft Internet Explorer') \n"
			+ "            return true; \n"
			+ "        else \n"
			+ "            return false; \n"
			+ "    } \n"
			// Used to block cascading events
			+ "    function cchFalse() { return false; } \n"

			// Used to call cchGetMouseXY();
			+ "    function cchUpdate(e) { cchGetMouseXY(e); } \n"

            // Used to cancel onclick propagation.
			+ "    function cchNoBubble(e) \n"
			+ "    { \n"
			+ "        if (!e) e = window.event; \n"
            + "        e.cancelBubble = true; \n"
            + "        e.returnValue = false; \n"
            + "        if (e.stopPropagation) e.stopPropagation(); \n"
            + "        if (e.preventDefault) e.preventDefault(); \n"
            + "    } \n"

			// Used to get position of mouse, and work on IE6,FireFox.
			+ "    function cchGetMouseXY(e) \n"
			+ "    { \n"
			+ "        if (!e) e = window.event; \n"
			+ "        if (e) \n"
			+ "        { //Work on FF \n"
			+ "            if (e.pageX || e.pageY) \n"
			+ "            { \n"
			+ "                cchMouseX = e.pageX + window.pageXOffset; \n"
			+ "                cchMouseY = e.pageY + window.pageYOffset; \n"
			+ "            } \n"
			+ "            else if (e.clientX || e.clientY) \n"
			+ "            { \n"
			+ "                cchMouseX = e.clientX + document.body.scrollLeft; \n"
			+ "                cchMouseY = e.clientY + document.body.scrollTop; \n"
			+ "            } \n"
			+ "        } \n"
			+ "    } \n"

			// Used to calculate div dimensions
			+ "    function cchGetBoxSize(w, b) \n"
			+ "    { \n"
			+ "        var origPosition = w.style.position; \n"
			+ "        var origVisibility = w.style.visibility; \n"
			+ "        var origDisplay = w.style.display; \n"
			+ "        w.style.position = 'absolute'; \n"
			+ "        w.style.visibility = 'hidden'; \n"
			+ "        w.style.display = 'block'; \n"
			+ "        cchBoxHeight = b.offsetHeight; \n"
			+ "        cchBoxWidth = b.offsetWidth; \n"
			+ "        w.style.position = origPosition; \n"
			+ "        w.style.visibility = origVisibility; \n"
			+ "        w.style.display = origDisplay; \n"
			+ "    } \n"

			// Set this method to mousedown event of div
			+ "    function cchGrab(e, context) \n"
			+ "    { \n"
			+ "        cchDragObj = context;  \n"
            + "        cchUpdate(e); \n"
			+ "        cchGrabX = cchMouseX; \n"
			+ "        cchGrabY = cchMouseY; \n"
			+ "        cchElemX = cchOrigX = cchDragObj.offsetLeft; \n"
			+ "        cchElemY = cchOrigY = cchDragObj.offsetTop; \n"
            + "        cchMouseDownWas = document.onmousedown; \n"
            + "        cchMouseMoveWas = document.onmousemove; \n"
            + "        cchMouseUpWas = document.onmouseup; \n"
            + "        document.onmousedown = cchFalse; \n"
            + "        document.onmousemove = cchDrag; \n"
            + "        document.onmouseup = cchDrop; \n"
			+ "    } \n"

			+ "    function cchMoveFrame(o) \n"
			+ "    { \n"
			+ "        if (cchIsMSIE() == true && o != null) { \n"
			+ "            var wframe = document.getElementById(o.id+'HelpFrame'); \n"
			+ "            if (wframe != null) { \n"
			+ "                wframe.style.top = o.offsetTop; \n"
			+ "                wframe.style.left = o.offsetLeft; \n"
			+ "            } \n"
			+ "        } \n"
			+ "    } \n"

			// When mouse drags the div, this method is called to update the div
			// position
			+ "    function cchDrag(e) \n"
			+ "    { \n"
            + "        cchUpdate(e); \n"
			+ "        if (cchDragObj) \n"
			+ "        { \n"
			+ "            cchElemX = cchOrigX + (cchMouseX-cchGrabX); \n"
			+ "            cchElemY = cchOrigY + (cchMouseY-cchGrabY); \n"
			+ "            cchDragObj.style.position = 'absolute'; \n"
			+ "            cchDragObj.style.left = (cchElemX).toString(10) + 'px'; \n"
			+ "            cchDragObj.style.top  = (cchElemY).toString(10) + 'px'; \n"
			+ "            cchMoveFrame(cchDragObj); \n"
			+ "        } \n"
			+ "        return false; \n"
			+ "    } \n"

			// When mouse drop the div, this method is called to clear cchDragObj
			// and some event handler
			+ "    function cchDrop(e) \n"
			+ "    { \n"
			+ "        if (cchDragObj) \n "
			+ "            cchDragObj = null; \n"
			+ "        cchUpdate(e); \n"
			+ "        document.onmousemove = cchMouseMoveWas; \n"
			+ "        document.onmouseup = cchMouseUpWas; \n"
			+ "        document.onmousedown = cchMouseDownWas; \n"
			+ "    } \n"

			+ "    function cchHideFrame(o) {\n"
			+ "        if (cchIsMSIE() == true && o != null) {\n"
			+ "            var wframe = document.getElementById(o.id+'HelpFrame'); \n"
			+ "            if (wframe != null) {\n"
			+ "                wframe.style.display = \"none\"; \n"
			+ "                wframe.style.zIndex = 0; \n"
			+ "            } \n"
			+ "        } \n"
			+ "    } \n"
			+ "    function cchHideObject(ev) {\n"
			+ "        var e = window.event ? window.event : ev; \n"
			+ "        var t = e.target ? e.target : e.srcElement; \n"

			// set help div element to none display, extract div element
			// name from close image id by removing 'Close' string from it.
			+ "        var w = document.getElementById(t.id.substr(0, t.id.lastIndexOf('Close'))); \n"
			+ "        if (w != null) { \n "
			+ "            w.style.display = \"none\"; \n"
			+ "            w.style.zIndex=0; \n"
			+ "            cchHideFrame(w); \n"
			+ "        } \n"
			+ "    } \n"
			+ "    function cchShowFrame(o) { \n"
			+ "        if (cchIsMSIE() == true && o != null) { \n"
			+ "            var wframe = document.getElementById(o.id+'HelpFrame'); \n"
			+ "            if (wframe != null) { \n"
			+ "                wframe.style.top = o.offsetTop; \n"
			+ "                wframe.style.left = o.offsetLeft; \n"
			+ "                wframe.style.width = cchBoxWidth; \n"
			+ "                wframe.style.height = cchBoxHeight; \n"
			+ "                wframe.style.display = \"block\"; \n"
			+ "                wframe.style.zIndex = 4999; \n"
			+ "            } \n"
			+ "        } \n"
			+ "    } \n"
			+ "    function cchShowObject(ev) { \n"
			+ "        var e = window.event ? window.event : ev; \n"
			+ "        var t = e.target ? e.target : e.srcElement; \n"
			+ "        var w = document.getElementById(t.id + 'Help'); \n"
			+ "        var b = document.getElementById(t.id + 'Box'); \n"
			// Fix for QC CR#65 - use clientX and clientY in all circumstances -
			// they are the coordinates relative to the viewport while pageX and
			// pageY are non-IE and are coordinates relative to the top of the
			// page - it is viewport-relative that we want. DSJ 2009/6/3
			+ "        // var mouseX = e.pageX ? e.pageX : e.clientX; \n"
			+ "        // var mouseY = e.pageY ? e.pageY : e.clientY; \n"
			+ "        var mouseX = e.clientX; \n"
			+ "        var mouseY = e.clientY; \n"

			// use the cchLastShow variable to close the last popup window, if any,
			// before opening the new popup window.
			+ "        if (cchLastShow != null) { \n"
			+ "            cchLastShow.style.display = \"none\"; \n"
			+ "            cchLastShow.style.zIndex = 0; \n"
			+ "            cchHideFrame(cchLastShow); \n"
			+ "        } \n"

			// set help div element to block display, get div element
			// name by concatenating 'Close' string to anchor id.
			+ "        if (w != null) { \n"
			+ "            cchLastShow = w; \n"
			+ "            w.style.left = mouseX + \"px\"; \n"
			+ "            w.style.zIndex = 4999; \n"
			+ "            var spaceNeeded, spaceAvail; \n"
			+ "            var frameWidth, frameHeight; \n"
			+ "            var scrollTop, scrollLeft; \n"
			+ "            if (document.documentElement && document.documentElement.clientWidth) { \n"

			// Non-IE5
			+ "                frameWidth = document.documentElement.clientWidth; \n"
			+ "                frameHeight = document.documentElement.clientHeight; \n"
			+ "                if (document.documentElement.scrollTop) { \n"
			// IE and FF
			+ "                    scrollTop = document.documentElement.scrollTop; \n"
			+ "                    scrollLeft = document.documentElement.scrollLeft; \n"
			+ "                } else { \n"
			// Chrome and Safari
			+ "                    scrollTop = document.body.scrollTop; \n"
			+ "                    scrollLeft = document.body.scrollLeft; \n"
			+ "                } \n"
			+ "            } \n"
			+ "            else if (document.body) { \n"

			// IE5
			+ "                frameWidth = document.body.clientWidth; \n"
			+ "                frameHeight = document.body.clientHeight; \n"
			+ "                scrollTop = document.body.scrollTop; \n"
			+ "                scrollLeft = document.body.scrollLeft; \n"
			+ "            } \n"

			// Get popup dimensions
			// Fix for CR #694 - now get popup dimensions correctly
			+ "            cchGetBoxSize(w, b); \n"
			
			// Calculate Y position to put popup
			+ "            spaceAvail = frameHeight - mouseY; \n"
			+ "            if (spaceAvail > cchBoxHeight) { \n"

			// display top of popup at mouse pointer if there's enough space
			// below pointer position to display entire popup
			+ "                w.style.top = mouseY + scrollTop + \"px\"; \n"
			+ "            } else { \n"

			// not enough space to display entire popup starting at pointer
			+ "                spaceNeeded = cchBoxHeight - spaceAvail; \n"
			+ "                if (spaceNeeded < mouseY) { \n"

			// there's room in the window above the pointer to display popup;
			// so position popup higher up as needed to fit
			+ "                    w.style.top = mouseY + scrollTop - spaceNeeded + \"px\"; \n"
			+ "                } else { \n"
			
			// popup won't fit window, so just position popup at top of window
			+ "                    w.style.top = scrollTop + \"px\"; \n"
			+ "                } \n"
			+ "            } \n"

			// Calculate X position to put popup - same algorithm as
			// displaying Y position above
			+ "            spaceAvail = frameWidth - mouseX; \n"
			+ "            if (spaceAvail > cchBoxWidth) { \n"
			+ "                w.style.left = mouseX + scrollLeft + \"px\"; \n"
			+ "            } else { \n"
			+ "                spaceNeeded = cchBoxWidth - spaceAvail; \n"
			+ "                if (spaceNeeded < mouseX) { \n"
			+ "                    w.style.left = mouseX + scrollLeft - spaceNeeded + \"px\"; \n"
			+ "                } else { \n"
			+ "                    w.style.left = scrollLeft + \"px\"; \n"
			+ "                } \n"
			+ "            } \n"
			
			// Popup is properly positioned - now display it.
            + "            w.style.display = \"block\"; \n"
			+ "            cchShowFrame(w); \n"
			+ "        } \n"
			+ "        cchNoBubble(e); \n"
			+ "    } \n"

			// Append the JavaScript code for div moving
			// These variables are used to get mouse position, calculate and
			// change div position.
			+ "    var cchMouseX = 0, cchMouseY = 0, cchGrabX = 0, cchGrabY = 0, cchOrigX = 0, cchOrigY = 0; \n"
			+ "    var cchBoxHeight = 0, cchBoxWidth = 0, cchElemX = 0, cchElemY = 0; \n"
			+ "    var cchDragObj = null; \n" + "    var cchLastShow = null;\n"

			// These variables are used to remember document event handlers.
			+ "    var cchMouseMoveWas = null, cchMouseUpWas = null, cchMouseDownWas = null; \n"

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
	 * This is the width set to use for the classic popup.
	 */
	protected int width = 0;

	/**
	 * This is the style of border set to use for the classic popup.
	 */
	protected String borderStyle = null;

	/**
	 * This is the style to use for the title in the classic popup.
	 */
	protected String titleStyle = null;

	/**
	 * This is the style to use for the help content in the classic popup.
	 */
	protected String helpStyle = null;

	/**
	 * This is the CSS class to use for the border of the classic popup.
	 */
	protected String borderClass = null;

	/**
	 * This is the CSS class to use for the title in the classic popup.
	 */
	protected String titleClass = null;

	/**
	 * This is the CSS class to use for the help content in the classic popup.
	 */
	protected String helpClass = null;

	/**
	 * Protected to prevent external construction except by subclasses. Use an
	 * appropriate subclass instead.
	 */
	protected ClassicContextualHelpProvider() {

	}

	/**
	 * Setter for the title content string: any string of text or HTML which you
	 * want to display as the title of the "classic"-rendition contextual-help
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
	 * "Classic"-rendition contextual help requires the use of JavaScript, so
	 * this lets you provide an alternative experience for
	 * non-JavaScript-enabled browsers.
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
	 * Setter for the pixel width of the "classic"-rendition contextual help
	 * popup. If you never call this method, or you pass a non-positive number
	 * to it, the default width will be assumed (300 pixels).
	 * 
	 * @param pWidth
	 *            The width of the popup, in pixels.
	 */
	public void setWidth(int pWidth) {
		this.width = pWidth;
	}

	/**
	 * Setter for the table border CSS style to use for the "classic"-rendition
	 * contextual help popup. Provide a CSS string of properties as you would
	 * for a standard inline HTML <code>style</code> attribute. The properties
	 * should be CSS border properties like <code>border-width</code> and
	 * <code>border-color</code>, valid for styling an HTML
	 * <code>&lt;TABLE&gt;</code> tag border. If you never call this method,
	 * or pass null, and you never call the <code>setBorderClass</code>
	 * method, or pass null, then the default style will be used (a solid black
	 * 1-pixel border). You can pass a blank string if you just want to clear
	 * that default.
	 * 
	 * @param pBorderStyle
	 *            The CSS style for the popup border. For example,
	 *            <code>border-width:1px;border-style:solid;border-color:black</code>
	 *            (the default). Pass blank to just clear the default.
	 */
	public void setBorderStyle(String pBorderStyle) {
		if (pBorderStyle != null)
			pBorderStyle = pBorderStyle.trim();
		this.borderStyle = pBorderStyle;
	}

	/**
	 * Setter for the table border CSS class to use for the "classic"-rendition
	 * contextual help popup. Provide the name of a CSS class as you would for a
	 * standard HTML <code>class</code> attribute. This should be a class
	 * which you are planning to include in the same page as the HTML from this
	 * <code>ClassicContextualHelpProvider</code>. That class can define any
	 * CSS properties valid for the HTML <code>&lt;TABLE&gt;</code> tag
	 * border, like <code>border-color</code>. If you never call this method,
	 * or pass null, and you never call the <code>setBorderStyle</code>
	 * method, or pass null, then the default style will be used (a solid black
	 * 1-pixel border). You can pass a blank string if you just want to clear
	 * that default.
	 * 
	 * @param pBorderClass
	 *            The name of the CSS class for the popup border. Pass blank to
	 *            just clear the default style.
	 */
	public void setBorderClass(String pBorderClass) {
		if (pBorderClass != null)
			pBorderClass = pBorderClass.trim();
		this.borderClass = pBorderClass;
	}

	/**
	 * Setter for the CSS style to use for the title in the "classic"-rendition
	 * contextual help popup. Provide a CSS string of properties as you would
	 * for a standard inline HTML <code>style</code> attribute. The properties
	 * should be CSS properties like <code>background-color</code> and
	 * <code>color</code>, valid for styling the contents of an HTML
	 * <code>&lt;TD&gt;</code> tag. If you never call this method, or pass
	 * null, and you never call the <code>setTitleClass</code> method, or pass
	 * null, then the default style will be used (bold white font on a blue
	 * background). You can pass a blank string if you just want to clear that
	 * default.
	 * 
	 * @param pTitleStyle
	 *            The CSS style for the popup title. For example,
	 *            <code>background-color:blue;color:white;font-weight:bold</code>
	 *            (the default). Pass blank to just clear the default.
	 */
	public void setTitleStyle(String pTitleStyle) {
		if (pTitleStyle != null)
			pTitleStyle = pTitleStyle.trim();
		this.titleStyle = pTitleStyle;
	}

	/**
	 * Setter for the CSS class to use for the title in the "classic"-rendition
	 * contextual help popup. Provide the name of a CSS class as you would for a
	 * standard HTML <code>class</code> attribute. This should be a class
	 * which you are planning to include in the same page as the HTML from this
	 * <code>ClassicContextualHelpProvider</code>. That class can define any
	 * CSS properties valid for the contents of an HTML <code>&lt;TD&gt;</code>
	 * tag, like <code>background-color</code>. If you never call this
	 * method, or pass null, and you never call the <code>setTitleStyle</code>
	 * method, or pass null, then the default style will be used (bold white
	 * font on a blue background). You can pass a blank string if you just want
	 * to clear that default.
	 * 
	 * @param pTitleClass
	 *            The name of the CSS class for the popup title. Pass blank to
	 *            just clear the default style.
	 */
	public void setTitleClass(String pTitleClass) {
		if (pTitleClass != null)
			pTitleClass = pTitleClass.trim();
		this.titleClass = pTitleClass;
	}

	/**
	 * Setter for the CSS style to use for the help content in the
	 * "classic"-rendition contextual help popup. Provide a CSS string of
	 * properties as you would for a standard inline HTML <code>style</code>
	 * attribute. The properties should be CSS properties like
	 * <code>background-color</code> and <code>color</code>, valid for
	 * styling the contents of an HTML <code>&lt;TD&gt;</code> tag. If you
	 * never call this method, or pass null, and you never call the
	 * <code>setContentClass</code> method, or pass null, then the default
	 * style will be used (normal black font on a white background). You can
	 * pass a blank string if you just want to clear that default.
	 * 
	 * @param pHelpStyle
	 *            The CSS style for the popup help content. For example,
	 *            <code>background-color:white;color:blank;font-weight:normal</code>
	 *            (the default). Pass blank to just clear the default.
	 */
	public void setHelpStyle(String pHelpStyle) {
		if (pHelpStyle != null)
			pHelpStyle = pHelpStyle.trim();
		this.helpStyle = pHelpStyle;
	}

	/**
	 * Setter for the CSS class to use for the help content in the
	 * "classic"-rendition contextual help popup. Provide the name of a CSS
	 * class as you would for a standard HTML <code>class</code> attribute.
	 * This should be a class which you are planning to include in the same page
	 * as the HTML from this <code>ClassicContextualHelpProvider</code>. That
	 * class can define any CSS properties valid for the contents of an HTML
	 * <code>&lt;TD&gt;</code> tag, like <code>background-color</code>. If
	 * you never call this method, or pass null, and you never call the
	 * <code>setContentStyle</code> method, or pass null, then the default
	 * style will be used (normal black font on a white background). You can
	 * pass a blank string if you just want to clear that default.
	 * 
	 * @param pHelpClass
	 *            The name of the CSS class for the popup help content. Pass
	 *            blank to just clear the default style.
	 */
	public void setHelpClass(String pHelpClass) {
		if (pHelpClass != null)
			pHelpClass = pHelpClass.trim();
		this.helpClass = pHelpClass;
	}

	/**
	 * <p>
	 * Returns the HTML string for the "classic"-rendition contextual help,
	 * including the link content surrounded by a hyperlink which, if clicked,
	 * will reveal the help title and content in an appropriately-formed DHTML
	 * popup layer. The link, title, and content are taken from the attributes
	 * previously set via the respective methods (like
	 * {@link #setTitleContent(String)}. The popup displays with the width and
	 * style/class attributes which were previously set via methods like
	 * {@link #setWidth(int)} and {@link #setHelpStyle(String)}.
	 * </p>
	 * <p>
	 * The boolean parameter controls whether or not to escape any HTML special
	 * characters like <code>&lt;</code> (ie convert them to corresponding
	 * HTML character entities so that they display literally) found in the
	 * content.
	 * </p>
	 * <p>
	 * The DHTML click-and-drag JavaScript will only be included the first time
	 * you invoke this method for this request. To reset that, use the
	 * {@link #resetHTML()} method. (Actually, tracking this depends on proper
	 * implementation of the counter methods like
	 * {@link #getClassicContextualHelpCounter()}.)
	 * </p>
	 * <p>
	 * The close image for the popup; its <code>ALT</code> text, if any; and
	 * the no-script URL for the contextual help link, if any, are provided via
	 * implementation of the respective abstract methods (like
	 * {@link #getCloseImageURL()}).
	 * </p>
	 * 
	 * @param escape
	 *            Whether to escape the HTML in the link content (and any other
	 *            content contained in the returned HTML)
	 * @return A string containing all of the HTML for the help hyperlink.
	 */
    public String getHTML(boolean escape) {

        // Get the contextual help link counter which tracks any previous
        // invocation.
        int count = getClassicContextualHelpCounter();

        // If common JavaScript has not previously been returned, then include
        // it now. Bump the counter.
        // DSJ 2009/6/3 - add common style definition too, if not already set.
        // Fix for QC CR# 64.
        StringBuffer html = new StringBuffer();
        if (count == 0) {
            html.append(CLASSIC_CONTEXTUAL_HELP_JS);
            html.append(CLASSIC_CONTEXTUAL_HELP_STYLE);
        }
        ++count;

        // Make the element ID.
        String id = "classicContextualHelp" + UUID.randomUUID().toString();

        // Make the link, title, and help contents.
        String link = this.linkContent;
        String title = this.titleContent;
        String help = this.helpContent;
        
        // Add element ID to <img> (it does not already have it and the script
        // needs it).
        if (isImage(link)) {
            link = "<img id=\"" + id + "\" " + link.substring(5);
        }
        
        // Escape XML meta-characters if needed.
        if (escape) {
            // Do not escape the link if it is an <img> tag.
            if (!isImage(link)) {
                link = Utils.escapeXml(link);
            }
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
        String closeButtonAlt = normalize(getCloseImageAlt());
        if (escape) {
            closeButtonAlt = Utils.escapeXml(closeButtonAlt);
        }
        closeButtonAlt = I18nUtility.filterNoLocalizationTokens(closeButtonAlt);

        // Make the width.
        String widthAttr = "";
        String widthStyleAttr = "";
        if (this.width > 0) {
            widthAttr = "width=\"" + this.width + "\" ";
            widthStyleAttr = "style=\"width: " + this.width + "px;\" ";
        }
        if ("".equals(widthAttr) && (DEFAULT_WIDTH > 0)) {
            widthAttr = "width=\"" + DEFAULT_WIDTH + "\" ";
            widthStyleAttr = "style=\"width: " + DEFAULT_WIDTH + "px;\" ";
        }

        // Make the border style.
        // Add width to the style to workaround Cleansheet-provoked issue.
        // DSJ 2011/8/24
        String borderStyleAttr = "";
        if (this.borderStyle != null && this.borderClass != null) {
            borderStyleAttr += "class=\"" + Utils.escapeXml(this.borderClass)
                    + "\" ";
            borderStyleAttr += "style=\""
                    + Utils.escapeXml(addWidth(this.borderStyle)) + "\" ";
        } else if (this.borderStyle != null) {
            borderStyleAttr += "style=\""
                    + Utils.escapeXml(addWidth(this.borderStyle)) + "\" ";
        } else if (this.borderClass != null) {
            borderStyleAttr += "class=\"" + Utils.escapeXml(this.borderClass)
                    + "\" ";
            borderStyleAttr += widthStyleAttr;
        } else if (DEFAULT_BORDER_STYLE != null) {
            borderStyleAttr += "style=\""
                    + Utils.escapeXml(addWidth(DEFAULT_BORDER_STYLE)) + "\" ";
        } else {
            borderStyleAttr += widthStyleAttr;
        }

        // Make the title style.
        String titleStyleAttr = "";
        if (this.titleStyle != null)
            titleStyleAttr += "style=\"" + Utils.escapeXml(this.titleStyle)
                    + "\" ";
        if (this.titleClass != null)
            titleStyleAttr += "class=\"" + Utils.escapeXml(this.titleClass)
                    + "\" ";
        if ("".equals(titleStyleAttr) && (DEFAULT_TITLE_STYLE != null))
            titleStyleAttr += "style=\"" + Utils.escapeXml(DEFAULT_TITLE_STYLE)
                    + "\" ";

        // Make the help content style.
        String helpStyleAttr = "";
        if (this.helpStyle != null)
            helpStyleAttr += "style=\"" + Utils.escapeXml(this.helpStyle)
                    + "\" ";
        if (this.helpClass != null)
            helpStyleAttr += "class=\"" + Utils.escapeXml(this.helpClass)
                    + "\" ";
        if ("".equals(helpStyleAttr) && (DEFAULT_HELP_STYLE != null))
            helpStyleAttr += "style=\"" + Utils.escapeXml(DEFAULT_HELP_STYLE)
                    + "\" ";

        // Generate the link HTML.
        String href = noscriptUrl;
        if (href == null) {
            href = "javascript:void(0);";
        }
        String anchor = "<a href=\"" + href + "\"";
        if (!isImage(link)) {
            // If the anchor text is not an image tag, put the ID on the anchor
            // (otherwise the image already has the ID on it so do nothing)
            anchor += " id=\"" + id + "\"";
        }
        anchor += ">" + link + "</a>";
        html.append(anchor);
        
        // Generate the main HTML and event-handling code by assembling the
        // pieces. First, assemble the event-handler script.

        html.append("<script>\n");
        // Comment this out - the link is not written from the script anymore.
        //if (isImage(link)) {
            // If the anchor text is an image tag, then don't need an
            // anchor tag to surround it.
        //  html.append("document.write('" + escapeQuotes(link) + "');\n");
        //} else {
        //  html.append("document.write('<a href=\"javascript:noop\" id=\""
        //          + id + "\">" + escapeQuotes(link) + "</a>');\n");
        //}
        // Write html to call javascript popup function
        html.append("classicContextualHelpUtil.addEvent(document.getElementById('"
                        + id + "'), 'click', cchShowObject);\n");
        // Add iframe to avoid IE select bug
        // html.append("if (cchIsMSIE() == true) \n");
        // html.append(" document.write('<iframe id=\"" + id + "HelpFrame\"
        // src=\"javascript:false;\"
        // style=\"position:absolute;background-color:white;display:none;\"></iframe>');\n");
        html.append("</script>");

        // Next, add the noscript for unscripted browsers. Use the noscript URL
        // if one was returned.
        // Comment this out - noscript handling is now done through the link href instead.
        //html.append("<noscript>");
        //if (noscriptUrl != null) {
        //  html.append("<a href=\"" + noscriptUrl + "\" target=\"_self\">"
        //          + link + "</a>");
        //} else {
        //  html.append(link);
        //}
        //html.append("</noscript>");

        // Next, write the popup window (layer) itself.

        html.append("<div ");
        html.append("id=\"" + id + "Help\" ");
        html.append("onmousedown=\"cchGrab(event,this)\" ");
        html.append("onclick=\"cchNoBubble(event)\" ");
        html
                .append("style=\"cursor:move;position:absolute;background-color:white;display:none;top:200px;left:200px\">\n");
        // Next line is a workaround for IE6 <SELECT> bug. Fix for QC CR# 64.
        // DSJ
        // 2009/6/3
        html.append("<div class=\"select-ie6\">\n");
        html.append("<table ");
        html.append("id=\"" + id + "Box\" ");
        html.append(borderStyleAttr + widthAttr
                + "cellpadding=10 cellspacing=0>\n");
        // Write title bar with title string and close button
        html.append("<tr>\n");
        html.append("<td align=left " + titleStyleAttr + "valign=middle>");
        html.append(title);
        html.append("</td>\n");
        html.append("<td align=right " + titleStyleAttr + "valign=middle>");
        html.append("<img id=\"" + id + "HelpClose\" ");
        html.append("src=\"" + closeButtonUrl + "\" style=\"cursor:pointer\" ");
        if (closeButtonAlt != null) {
            // Add alt text for the close image if there is any.
            html.append("alt=\"" + closeButtonAlt + "\" ");
            // Duplicate the alt text into the title attribute.
            html.append("title=\"" + closeButtonAlt + "\" ");
        }
        // Don't specify size since we can't know the size of the image
        // html.append("width=\"15\" height=\"15\" ");
        html.append("align=right border=\"0\"/ ></td>\n");
        html.append("</tr>\n");
        html.append("<tr valign=top height=\"100%\">\n");
        html.append("<td " + helpStyleAttr + "colspan=2>\n");
        // Write popup content
        // Add width to inner table to workaround Cleansheet-provoked issue -
        // DSJ 2011/8/24
        html.append("<table " + widthStyleAttr + widthAttr
                + "cellpadding=10 cellspacing=0>\n");
        html.append("<tr><td align=left>");
        html.append(help);
        html.append("</td></tr>\n");
        html.append("</table></td>\n");
        html.append("</tr></table>\n");
        // Next line is a workaround for IE6 <SELECT> bug. Fix for QC CR# 64.
        // DSJ 2009/6/3
        html.append("<!--[if lte IE 6.5]><iframe " + widthStyleAttr
                + "src=\"javascript:false;\"></iframe><![endif]--></div>\n");
        html.append("</div>");

        // Finally, write script which adds event for close button.

        html.append("<script>\n");
        html
                .append("classicContextualHelpUtil.addEvent(document.getElementById('"
                        + id + "HelpClose'), 'click', cchHideObject);\n");
        html.append("</script>");

        // Bump counter and return.
        bumpClassicContextualHelpCounter();
        return html.toString();
    }

	/**
	 * <p>
	 * Use this method at any time during the request lifecycle, if for some
	 * reason you need the next {@link #getHTML(boolean)} invocation to return
	 * the common JavaScript again. This generally is not recommended.
	 * </p>
	 * 
	 */
	public void resetHTML() {
		resetClassicContextualHelpCounter();
	}

	/**
	 * An abstract method to get a counter of how many times in this request
	 * lifecycle a classic contextual help provider has been generated (ie its
	 * {@link #getHTML(boolean)} method has been invoked). Different action for
	 * portal and portlet, so this is an abstract method.
	 * 
	 * @return The counter.
	 */
	protected abstract int getClassicContextualHelpCounter();

	/**
	 * An abstract method to increment the counter of how many times in this
	 * request lifecycle a classic contextual help provider has been generated
	 * (ie its {@link #getHTML(boolean)} method has been invoked). Different
	 * action for portal and portlet, so this is an abstract method.
	 */
	protected abstract void bumpClassicContextualHelpCounter();

	/**
	 * An abstract method to reset the counter of how many times in this request
	 * lifecycle a classic contextual help provider has been generated (ie its
	 * {@link #getHTML(boolean)} method has been invoked). Different action for
	 * portal and portlet, so this is an abstract method.
	 */
	protected abstract void resetClassicContextualHelpCounter();

	/**
	 * An abstract method to generate the hyperlink URL to use for contextual
	 * help in the no-script case. Different action for portal and portlet, so
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
	 * Returns true if the given string appears to be an HTML
	 * <code>&lt;IMG&gt;</code> tag, false otherwise.
	 */
	private boolean isImage(String s) {
		if (s != null) {
			s = s.trim().toLowerCase();
			if (s.startsWith("<img ") && s.endsWith(">"))
				return true;
		}
		return false;
	}

	/**
	 * Normalize blank string values to null - so the return is either a
	 * non-blank string, or null.
	 * 
	 * @param value
	 * @return
	 */
	protected String normalize(String value) {
		if (value != null) {
			value = value.trim();
			if (value.equals("")) {
				value = null;
			}
		}
		return value;
	}
	
	/**
	 * Add a CSS width property for the current contextual-help width, if 
	 * defined, to any string of CSS properties passed.
	 * 
	 * @param style
	 * @return
	 */
	private String addWidth(String style) {
	    String widthProp = "width:";
	    if (this.width > 0) {
	        widthProp += this.width;
	    } else if (DEFAULT_WIDTH > 0) {
	        widthProp += DEFAULT_WIDTH;
	    } else {
	        return style;
	    }
	    widthProp += "px;";
	    if (style == null) {
	        return widthProp;
	    }
	    style = style.trim();
	    if ("".equals(style)) {
	        return widthProp;
	    }
	    if (style.endsWith(";")) {
	        style += widthProp;
	    } else {
	        style += ";" + widthProp;
	    }
	    return style;
    }
}
