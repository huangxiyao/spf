package com.vignette.portal.portlet.management.internal.implementation.provider.wsrp;

public class ProducerDataGetter {

	private ProducerData mProducerData = null ;
	
	public ProducerDataGetter(ProducerData producerData) {
		mProducerData = producerData ;
	}

	public String getMarkupURL() {
		return mProducerData.markupURL;
	}

	public String getOriginalServiceURL() {
		return mProducerData.originalServiceURL;
	}

	public String getPortletManagementURL() {
		return mProducerData.portletManagementURL;
	}

	public byte[] getRegistrationState() {
		return mProducerData.registrationState;
	}

	public String getRegistrationURL() {
		return mProducerData.registrationURL;
	}

	public String getServiceDescriptionURL() {
		return mProducerData.serviceDescriptionURL;
	}

}
