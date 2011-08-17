package com.vignette.portal.portlet.management.internal.implementation.provider.wsrp;

import com.vignette.portal.portlet.management.external.PortletPersistenceException;
import com.vignette.portal.portlet.management.external.PortletResourceNotFoundException;


public class WsrpPortletApplicationGetter {
	
	private static WsrpPortletApplicationGetter mApplication = new WsrpPortletApplicationGetter() ;
	
	private WsrpPortletApplicationManagerSpiImpl mManager = null ;
	
	private WsrpPortletApplicationGetter() {
		super() ;
		mManager = WsrpPortletApplicationManagerSpiImpl.getInstance();
	}
	
	public static WsrpPortletApplicationGetter getInstance() {
		return mApplication ;
	}
	
	public String getOriginalServiceURL(String friendlyID) throws Exception {
		return getProducerByFriendlyID(friendlyID).getAppCreationUrl();
	}
	
	private WsrpPortletApplicationSpiImpl getProducerByFriendlyID(String friendlyID) throws Exception {
		try {
			return mManager.getWsrpPortletApplicationByFriendlyID(friendlyID);
		} catch (PortletResourceNotFoundException e) {
			throw new Exception(e) ;
		} catch (PortletPersistenceException e) {
			throw new Exception(e) ;
		}
	}
	
}
