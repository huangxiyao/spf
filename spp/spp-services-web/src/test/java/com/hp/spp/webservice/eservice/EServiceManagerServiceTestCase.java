/**
 * EServiceManagerServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.eservice;


public class EServiceManagerServiceTestCase extends junit.framework.TestCase {
	public EServiceManagerServiceTestCase(java.lang.String name) {
		super(name);
	}
	
	
	public void testOk(){
		assertTrue(true);
	}
	
	/*
	 * THE FOLLOWING TEST CASE WORK ONLY IF THE WEB APPLICATION SERVER IS WORKING ON 
	 * server LOCALHOST & port 7001
	 * 
	 */
/*	public void testEServiceManagerWSDL() throws Exception {
		javax.xml.rpc.ServiceFactory serviceFactory = javax.xml.rpc.ServiceFactory
				.newInstance();
		java.net.URL url = new java.net.URL(
				new EServiceManagerServiceLocator()
						.getEServiceManagerAddress()
						+ "?WSDL");
		javax.xml.rpc.Service service = serviceFactory.createService(url,
				new EServiceManagerServiceLocator()
						.getServiceName());
		assertTrue(service != null);
	}

	public void test1EServiceManagerGetEserviceInformation() throws Exception {
		EServiceManagerSoapBindingStub binding;
		try {
			binding = (EServiceManagerSoapBindingStub) new EServiceManagerServiceLocator()
					.getEServiceManager();
		} catch (javax.xml.rpc.ServiceException jre) {
			if (jre.getLinkedCause() != null)
				jre.getLinkedCause().printStackTrace();
			throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: "
					+ jre);
		}
		assertNotNull("binding is null", binding);

		// Time out after a minute
		binding.setTimeout(60000);

		EserviceRequest request = new com.hp.spp.webservice.eservice.client.EserviceRequest();
		request.setSiteName("SMART PORTAL");
		request.setEServiceName("EasyCatalogue");
		java.util.Map userContext = new java.util.HashMap();
		userContext.put("environmentPlatform", "PROD");
		userContext.put("username", "jdoe");
		userContext.put("firstname", "Jhon");
		userContext.put("lastname", "Doe");
		userContext.put("language", "FR");
		request.setUserContext(userContext);

		
		// Test operation
		com.hp.spp.webservice.eservice.client.EServiceResponse value = null;
		value = binding
				.getEserviceInformation(request);
		assertNotNull(value);
		assertEquals("Test on method",value.getMethod(),"POST");
		assertEquals("Test on url ",value.getUrl(),"https://www.hp-easycatalogue.com/FR/home.aspx");
	}
*/
	
}
