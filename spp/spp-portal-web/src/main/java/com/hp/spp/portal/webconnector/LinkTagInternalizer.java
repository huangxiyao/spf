package com.hp.spp.portal.webconnector;

import com.vignette.portal.text.processor.ProcessingContext;
import com.vignette.portal.text.processor.ProcessingException;
import com.vignette.portal.text.processor.html.HTMLSegment;
import com.vignette.portal.text.processor.html.HTMLElementToken;
import com.vignette.portal.text.processor.html.HTMLElementAttribute;

/**
 * In addition to the class provided by Vignette this class is able to handle properly CSS <tt>link</tt>
 * tag if it's part of HTML body and not head as it should be. This is the case for GPP Java Modules.
 * In such a case Vignette <tt>LinkTagInternalizer</tt> implementation rewrites the value of <tt>href</tt>
 * attribute twice, which generates incorrect url. This implemenation will ignore the href attribute
 * if it has already been rewritten.
 */
public class LinkTagInternalizer extends com.vignette.portlet.buildingblock.webconnector.transform.mutators.html.LinkTagInternalizer {

	public void handle(ProcessingContext processingContext, HTMLSegment htmlSegment, HTMLElementToken htmlElementToken) throws ProcessingException {
		if (htmlElementToken == null) {
			return;
		}

		if ("link".equals(htmlElementToken.getElementName())) {
			HTMLElementAttribute href = htmlElementToken.getAttribute("href");
			if (href != null) {
				String hrefValue = href.getValue();

				// If the hrefValue starts with "/portal" this means that the link has already been
				// rewritten. Let's just ignore it in this case.
				String contextPath = this.view.getPortalPageContext().getRequest().getContextPath();
				if (hrefValue != null && hrefValue.startsWith(contextPath)) {
					// Return as we don't wont to process it more.
					return;
				}
			}
		}

		super.handle(processingContext, htmlSegment, htmlElementToken);
	}
}
