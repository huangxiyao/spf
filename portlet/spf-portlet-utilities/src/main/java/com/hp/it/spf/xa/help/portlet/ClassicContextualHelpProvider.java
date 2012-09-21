/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help.portlet;

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderResponse;
import javax.portlet.PortletURL;

import com.hp.it.spf.xa.i18n.portlet.I18nUtility;
import com.hp.it.spf.xa.misc.portlet.Consts;
import com.hp.it.spf.xa.misc.portlet.Utils;

/**
 * <p>
 * A concrete contextual help provider, which produces the "classic"-rendition
 * of a contextual help popup window (table) in a portlet.
 * </p>
 * <p>
 * This is the style of contextual-help popup which is rendered by the portlet
 * framework's <code>&lt;spf-help-portlet:classicContextualHelp&gt;</code>
 * tag. It is also the style rendered by the portlet framework's
 * <code>&lt;spf-i18n-portlet:classicContextualHelpParam&gt;</code> tag. Use
 * that tag inside the <code>&lt;spf-i18n-portlet:message&gt;</code> tag body
 * to render this kind of contextual-help popup into a message with your chosen
 * attributes. (Using the
 * <code>&lt;spf-i18n-portlet:classicContextualHelpParam&gt;</code> tag
 * instantiates this class.) You can also instantiate this class directly and
 * pass it to the <code>I18nUtility.getMessage</code> methods of the portlet
 * framework (such as
 * {@link com.hp.it.spf.xa.i18n.portlet.I18nUtility#getMessage(PortletRequest, String, com.hp.it.spf.xa.help.ContextualHelpProvider[])})
 * to produce this classic-style contextual-help popup.
 * </p>
 * <p>
 * This class just implements some portlet-specific concrete methods which the
 * generic superclass cannot. The main logic for generating the contextual-help
 * popup is in the superclass.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ClassicContextualHelpProvider extends
		com.hp.it.spf.xa.help.ClassicContextualHelpProvider {

	/**
	 * The request for this classic contextual help provider.
	 */
	private PortletRequest request = null;

	/**
	 * The response for this classic contextual help provider.
	 */
	private PortletResponse response = null;

	/**
	 * <p>
	 * Constructor for the "classic"-style contextual help provider for a
	 * particular request. If a null parameter is provided, the
	 * <code>getHTML</code> method of this class may not work.
	 * </p>
	 */
	public ClassicContextualHelpProvider(PortletRequest pRequest,
			PortletResponse pResponse) {
		request = pRequest;
		response = pResponse;
	}

	/**
	 * A concrete implementation, to get a counter of how many times in this
	 * request lifecycle a classic contextual help provider has been generated
	 * (ie its <code>getHTML</code> method has been invoked). If a null
	 * portlet request was given to the constructor, this method always returns
	 * 0.
	 * 
	 * @return The counter.
	 */
	protected int getClassicContextualHelpCounter() {

		int count = 0;
		if (request != null) {
			Object countString = request
					.getAttribute(CLASSIC_CONTEXTUAL_HELP_COUNTER_ATTR);
			if (countString == null) {
				count = 0;
			} else {
				try {
					count = Integer.parseInt((String) countString);
				} catch (NumberFormatException e) { // should never happen
					count = 0;
				}
			}
		}
		return count;
	}

	/**
	 * A concrete implementation, to increment the counter of how many times in
	 * this request lifecycle a classic contextual help provider has been
	 * generated (ie its <code>getHTML</code> method has been invoked). If a
	 * null portlet request was given to the constructor, this method does
	 * nothing.
	 */
	protected void bumpClassicContextualHelpCounter() {

		int count = 0;
		if (request != null) {
			count = getClassicContextualHelpCounter() + 1;
			request.setAttribute(CLASSIC_CONTEXTUAL_HELP_COUNTER_ATTR, String
					.valueOf(count));
		}
	}

	/**
	 * A concrete implementation, to reset the counter of how many times in this
	 * request lifecycle a classic contextual help provider has been generated
	 * (ie its <code>getHTML</code> method has been invoked). Different action
	 * for portal and portlet, so this is an abstract method. If a null portlet
	 * request was given to the constructor, this method does nothing.
	 */
	protected void resetClassicContextualHelpCounter() {
		if (request != null) {
			request.removeAttribute(CLASSIC_CONTEXTUAL_HELP_COUNTER_ATTR);
		}
	}

	/**
	 * A concrete method to generate the hyperlink URL to use for contextual
	 * help in the no-script case. If a no-script HREF was set to the class,
	 * which is not a document fragment (ie does not begin with <code>#</code>),
	 * then that no-script HREF is used as the no-script URL. Otherwise, if the
	 * portlet supports help mode, the no-script URL is a render URL pointing to
	 * that mode (and if the no-script HREF is a document fragment - ie does
	 * begin with <code>#</code> - then that fragment name is passed as a
	 * parameter). Finally, if the portlet does not support help mode, the
	 * no-script URL is null.
	 */
	protected String getNoScriptURL() {

		String noscriptUrl = null;
		try {
			if (noScriptHref != null && !noScriptHref.startsWith("#")) {
				noscriptUrl = noScriptHref;
			} else if (request != null && response != null
					&& response instanceof RenderResponse) {
				if (request.isPortletModeAllowed(PortletMode.HELP)) {
					RenderResponse render = (RenderResponse) response;
					PortletURL url = render.createRenderURL();
					url.setPortletMode(PortletMode.HELP);

					// pass current mode
					String mode = request.getPortletMode().toString()
							.toLowerCase();
					if (mode != null) {
						url.setParameter("mode", mode);
					}

					// pass fragment name
					if (noScriptHref != null && noScriptHref.startsWith("#")) {
						String fragmentName = noScriptHref.substring(1);
						url.setParameter(Consts.PARAM_FRAGMENT, fragmentName);
					}
					noscriptUrl = url.toString();
				}
			}
		} catch (PortletModeException e) {
		}
		return noscriptUrl;
	}

    /**
     * A concrete method to generate the image URL for the popup close button in
     * a portlet. The URL is presumed to be stored in the message resources for
     * the current portlet, under a message key named
     * <code>contextualHelp.close.url</code>; the URL may refer to a resource
     * file in either the portlet resource bundle directory or your portlet WAR,
     * or it may refer to an actual URL. If no such message exists, then a file
     * named <code>/images/btn_close.gif</code> is assumed in your portlet
     * resource bundle directory or portlet WAR, and a URL for that is generated
     * and returned. Note: Both message lookup, and file lookup lookup, search
     * for the best localized candidate, if any, for the user's current locale.
     */
	protected String getCloseImageURL() {
        String url = "/images/" + CLOSE_BUTTON_IMG_NAME;
        if (request != null && response != null) {
            // Look for the message.
            String msg = I18nUtility.getMessage(request, CLOSE_BUTTON_IMG_URL,
                    "");
            if (msg.length() > 0) {
                // If the message is found, use and return it. Either use and
                // return the message itself, or if the message refers to a file
                // in the current portlet bundle directory or WAR, return a URL
                // for that file.
                url = msg;
                if (I18nUtility.getLocalizedFileName(request, url) != null) {
                    url = I18nUtility.getLocalizedFileURL(request, response,
                            url);
                }
            } else {
                // If the message is not found, assume btn_close.gif. If that
                // file exists in the current portlet bundle directory, return a
                // URL for that. Otherwise assume it exists in the portlet WAR
                // and return a URL for that.
                url = I18nUtility.getLocalizedFileURL(request, response, url);
            }
        }
        return url;
    }

	/**
	 * A concrete method to get the image alt text for the popup close button in
	 * a portlet. This text is presumed to be stored in the current portlet's
	 * configured message resources, under a message key named
	 * <code>contextualHelp.close.alt</code>. If it is not found there, then
	 * an empty string is returned. <b>Note:</b> the returned message is the
	 * best-candidate localized version found in the resource bundle for the
	 * user's current locale.
	 */
	protected String getCloseImageAlt() {
		String alt = "";
		if (request != null) {
			alt = I18nUtility.getMessage(request, CLOSE_BUTTON_IMG_ALT, "");
		}
		return alt;
	}
}
