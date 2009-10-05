package com.hp.it.spf.xa.axis.portal;

import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.log.LogConfiguration;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

import org.apache.axis.client.Stub;

import java.lang.reflect.Method;

/**
 * Initializes Axis type descriptors for web service stub defined in <tt>axisStubClass</tt>
 * class.
 * <p>
 * Axis type descriptor initialization occurs the first a web service client classes are used and
 * its result is stored in static variables of the stub. Unfortunatley Axis does not properly
 * initialization which may result in SAXException thrown during web service call,
 * saying that an request or response element is not valid even though the content of
 * the SOAP message was created using Axis stub.
 * <p>
 * Initializing the web service type descriptors in this listener ensures that it happens
 * in a single thread and prevents this issue from happening.
 *
 * @see #initAxisWebServiceTypeDescriptors(Class<? extends org.apache.axis.client.Stub>)
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class AxisInitializationListener implements ServletContextListener
{
	private static final LogWrapper LOG = new LogWrapper(AxisInitializationListener.class);

	public void contextInitialized(ServletContextEvent servletContextEvent)
	{
		initAxisWebServiceTypeDescriptors(
				oasis.names.tc.wsrp.v1.bind.WSRP_v1_Markup_Binding_SOAPStub.class);
		initAxisWebServiceTypeDescriptors(
				oasis.names.tc.wsrp.v1.bind.WSRP_v1_PortletManagement_Binding_SOAPStub.class);
		initAxisWebServiceTypeDescriptors(
				oasis.names.tc.wsrp.v1.bind.WSRP_v1_Registration_Binding_SOAPStub.class);
		initAxisWebServiceTypeDescriptors(
				oasis.names.tc.wsrp.v1.bind.WSRP_v1_ServiceDescription_Binding_SOAPStub.class);
		initAxisWebServiceTypeDescriptors(
				oasis.names.tc.wsrp.v2.bind.WSRP_v2_Markup_Binding_SOAPStub.class);
		initAxisWebServiceTypeDescriptors(
				oasis.names.tc.wsrp.v2.bind.WSRP_v2_PortletManagement_Binding_SOAPStub.class);
		initAxisWebServiceTypeDescriptors(
				oasis.names.tc.wsrp.v2.bind.WSRP_v2_Registration_Binding_SOAPStub.class);
		initAxisWebServiceTypeDescriptors(
				oasis.names.tc.wsrp.v2.bind.WSRP_v2_ServiceDescription_Binding_SOAPStub.class);
		//FIXME (slawek) - Add code for UGS stub		
	}

	/**
	 * Initializes Axis type descriptors for the given web service client stub.
	 * The initialization is done by invoking a private stub method <tt>createCall</tt> which
	 * when called the first time will instantiate all the descriptors and store them in its
	 * static member.
	 *
	 * @param axisStubClass Axis web service client stub class
	 */
	private void initAxisWebServiceTypeDescriptors(Class<? extends Stub> axisStubClass)
	{
		if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
			LOG.debug("Initializing type descriptors for " + axisStubClass.getName());
		}
		try {
			Method createCallMethod = axisStubClass.getDeclaredMethod("createCall", (Class[]) null);
			createCallMethod.setAccessible(true);
			Object axisStubInstance = axisStubClass.newInstance();
			createCallMethod.invoke(axisStubInstance, (Object[]) null);
			createCallMethod.setAccessible(false);
		}
		catch (Exception e) {
			// do not rethrow this exception - if initialization above fails it may work
			// the first time the actual web service is called.
			LOG.error("Error occured hile initializing type descriptors for " +
					axisStubClass.getName(), e);
		}
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent)
	{
	}
}
