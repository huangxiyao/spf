package com.hp.it.spf.wsrp.rewriter.impl;

import com.hp.it.spf.wsrp.rewriter.IRewriter;
import com.hp.it.spf.wsrp.rewriter.Predicates;
import oasis.names.tc.wsrp.v2.types.PortletDescription;
import oasis.names.tc.wsrp.v2.types.ServiceDescription;
import org.apache.axis.MessageContext;

/**
 * Rewrites the <tt>getServiceDescription</tt> response changing the flag inidicating whether
 * the portlets support URL template processing to true.
 * The reason for this change is that Vignette sends to the producer URL templates which do not
 * result in valid URLs and require further processing by Vignette. Disabling the templates
 * we prevent Vignette from sending templates with each WSRP markup request and we force the producer
 * to generate its own URLs. This way the generated URLs still need to be processed but the templates
 * are not sent with each request making the request ligher.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class UrlTemplateProcessingRewriter implements IRewriter {

	/**
	 * @param messageContext web service call message context
	 * @return <tt>true</tt> if this is a V2 getServiceDescription call
	 */
	public boolean shouldApply(MessageContext messageContext) {
		return Predicates.isAnyOfMethods(messageContext, "v2:getServiceDescription");
	}

	/**
	 * Changes <tt>doesUrlTemplateProcessing</tt> to <tt>false</tt> for each offered portlet
	 *
	 * @param data WSRP request or reponse object
	 * @throws IllegalArgumentException if the given parameter is not of type {@link oasis.names.tc.wsrp.v2.types.ServiceDescription}
	 */
	public void rewrite(Object data) throws IllegalArgumentException {
		if (!(data instanceof ServiceDescription)) {
			throw new IllegalArgumentException("Expected argument of type '" +
					ServiceDescription.class.getName() +
					"' but found: " + (data == null ? "null" : data.getClass().getName()));
		}

		ServiceDescription serviceDescription = (ServiceDescription) data;
		if (serviceDescription != null) {
			PortletDescription[] offeredPortlets = serviceDescription.getOfferedPortlets();
			if (offeredPortlets != null) {
				for (PortletDescription portletDescription : offeredPortlets) {
					portletDescription.setDoesUrlTemplateProcessing(false);
				}
			}
		}
	}
}
