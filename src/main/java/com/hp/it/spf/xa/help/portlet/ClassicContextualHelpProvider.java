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

/**
 * <p>
 * A concrete contextual help provider, which produces the "classic"-style
 * contextual help popup window in a portlet.
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
 * framework to produce this classic-style contextual-help popup.
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
	 * particular request. If a null parameter is provided, the getHTML methods
	 * of this class may not work.
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
	 * (ie its getHTML method has been invoked).
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
	 * generated (ie its getHTML method has been invoked). Different action for
	 * portal and portlet, so this is an abstract method.
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
	 * (ie its getHTML method has been invoked). Different action for portal and
	 * portlet, so this is an abstract method.
	 */
	protected void resetClassicContextualHelpCounter() {
		if (request != null) {
			request.removeAttribute(CLASSIC_CONTEXTUAL_HELP_COUNTER_ATTR);
		}
	}

	/**
	 * A concrete method to generate the hyperlink URL to use for contextual
	 * help in the noscript case. If there was a <code>noscriptHref</code>
	 * attribute, which is not a document fragment (ie does not begin with
	 * <code>#</code>), then the <code>noscriptHref</code> is used as the
	 * noscript URL. Otherwise, if the portlet supports help mode, the noscript
	 * URL is a render URL pointing to that mode (and if the
	 * <code>noscriptHref</code> is a document fragment (ie does begin with
	 * <code>#</code>), then that fragment is passed as a parameter).
	 * Finally, if the portlet does not support help mode, the noscript URL is
	 * null.
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
						url.setParameter("fragment", fragmentName);
					}
					noscriptUrl = url.toString();
				}
			}
		} catch (PortletModeException e) {
		}
		return noscriptUrl;
	}

	/**
	 * A concrete method to generate the image URL for the popup close button.
	 * This close button is presumed to be named
	 * <code>/images/btn_close.gif</code> and located in the portlet resource
	 * bundle directory. This image may be localized if desired; this method
	 * looks for the particular localized image loaded in the portlet resource
	 * bundle directory which is the best-candidate given the current locale. If
	 * no image is found there, then the method assumes it is inside the current
	 * portlet application and returns an encoded URL pointing to it there.
	 */
	protected String getCloseImageURL() {
		String defaultUrl = "/images/" + CLOSE_BUTTON_IMG_NAME;
		String url = defaultUrl;
		if (request != null) {
			url = I18nUtility.getLocalizedFileURL(request, url);
			if (url == null) {
				url = defaultUrl;
				if (response != null) {
					url = response.encodeURL(url);
				}
			}
		}
		return slashify(url);
	}

	/**
	 * A concrete method to get the image alt text for the popup close button.
	 * This text is presumed to be stored in the current portlet's configured
	 * message resources, under a message key named
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
