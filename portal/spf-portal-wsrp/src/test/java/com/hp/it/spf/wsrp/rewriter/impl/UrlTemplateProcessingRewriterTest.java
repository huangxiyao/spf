package com.hp.it.spf.wsrp.rewriter.impl;

import junit.framework.TestCase;
import oasis.names.tc.wsrp.v2.types.PortletDescription;
import oasis.names.tc.wsrp.v2.types.ServiceDescription;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class UrlTemplateProcessingRewriterTest extends TestCase {

	/**
	 * The {@link com.hp.it.spf.wsrp.rewriter.impl.UrlTemplateProcessingRewriter#rewrite(Object)}
	 * should be passed always ServiceDescription object. For all other types it should fail.
	 */
	public void testRewriteFailIfNoServiceDescriptionPassed() {
		try {
			new UrlTemplateProcessingRewriter().rewrite(new Object());
			fail("IllegalArgumentException expected");
		}
		catch (IllegalArgumentException e) {
			assertTrue("IllegalArgumentException expected", true);
		}
	}

	/**
	 * The <tt>doesUrlTemplateProcessing</tt> flag must be rewritten for each offered portlet.
	 */
	public void testRewriteOnPortlets() {
		ServiceDescription serviceDescription = new ServiceDescription();
		PortletDescription portlet1 = new PortletDescription();
		portlet1.setDoesUrlTemplateProcessing(true);
		PortletDescription portlet2 = new PortletDescription();
		portlet2.setDoesUrlTemplateProcessing(true);
		serviceDescription.setOfferedPortlets(new PortletDescription[]{portlet1, portlet2});

		new UrlTemplateProcessingRewriter().rewrite(serviceDescription);
		assertEquals("Portlet 1 url templates de-activated", false, (boolean) portlet1.getDoesUrlTemplateProcessing());
		assertEquals("Portlet 2 url templates de-activated", false, (boolean) portlet2.getDoesUrlTemplateProcessing());
	}
}
