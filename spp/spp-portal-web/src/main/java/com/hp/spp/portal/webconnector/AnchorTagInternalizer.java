package com.hp.spp.portal.webconnector;

import org.apache.log4j.Logger;
import com.vignette.portal.text.processor.ProcessingException;
import com.vignette.portal.text.processor.ProcessingContext;
import com.vignette.portal.text.processor.html.HTMLElementToken;
import com.vignette.portal.text.processor.html.HTMLSegment;
import com.vignette.portal.text.processor.html.HTMLElementAttribute;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * In addition to the class provided by Vignette, this class is able to handle urls having format
 * <tt>PORTAL:[menu item uid]</tt> used for example by GPP Java modules. The link would be rewritten
 * to a standard Vignette URI.
 */
public class AnchorTagInternalizer extends com.vignette.portlet.buildingblock.webconnector.transform.mutators.html.AnchorTagInternalizer
{
	private static Logger mLog = Logger.getLogger(AnchorTagInternalizer.class);
	private static final String PORTAL_URL_MARKER = "PORTAL:";
	private static final int PORTAL_URL_MARKER_LEN = PORTAL_URL_MARKER.length();


	public void handle(ProcessingContext processingContext, HTMLSegment htmlSegment, HTMLElementToken token) throws ProcessingException {
		if (token == null) {
			return;
		}

		if (!"a".equalsIgnoreCase(token.getElementName())) {
			super.handle(processingContext, htmlSegment, token);
			return;
		}

		HTMLElementAttribute href = token.getAttribute("href");
		if (href != null && href.getValue() != null) {
			String hrefValue = href.getValue();
			int pos = hrefValue.toUpperCase().indexOf(PORTAL_URL_MARKER);
			if (pos != -1) {
				Object portalContextObj = this.view.getPortalPageContext().getRequest().getAttribute("portalContext");
				if (portalContextObj == null || !(portalContextObj instanceof PortalContext)) {
					mLog.error("Cannot find PortalContext object: " + portalContextObj);
				}
				else {
					PortalContext portalContext = (PortalContext) portalContextObj;
					String menuItemId = href.getValue().substring(pos + PORTAL_URL_MARKER_LEN);
					if (menuItemId != null) {
						int pos2 = menuItemId.indexOf('/');
						if (pos2 != -1) {
							menuItemId = menuItemId.substring(0, pos2);
						}
						String newHrefValue = portalContext.createMenuItemURI(menuItemId);
						if (mLog.isDebugEnabled()) {
							mLog.debug("Rewriting [" + href.getValue() + "] with [" + newHrefValue + "]");
						}
						href.setValue(newHrefValue);
						token.setAttribute(href);
						htmlSegment.write(token);
						return;
					}
				}
			}
		}

		// If we get here, this means that PORTAL:[menu item uid] link was not found - let's
		// the parent class handle this link
		super.handle(processingContext, htmlSegment, token);
	}

}
