package com.hp.it.spf.wsrp.openportal.producer.markup.impl;

import com.sun.portal.wsrp.producer.markup.CloneBeforeWriteHandler;
import com.sun.portal.wsrp.producer.Producer;
import com.sun.portal.wsrp.producer.ProducerException;
import com.sun.portal.wsrp.producer.WSRPProducerUtility;
import com.sun.portal.wsrp.common.stubs.v2.RegistrationContext;
import com.sun.portal.wsrp.common.stubs.v2.PortletContext;
import com.sun.portal.wsrp.common.stubs.v2.UserContext;
import com.sun.portal.wsrp.common.stubs.v2.ResetProperty;
import com.sun.portal.wsrp.common.stubs.v2.PropertyList;
import com.sun.portal.wsrp.common.stubs.v2.Property;
import com.sun.portal.wsrp.common.encode.Base64;
import com.sun.portal.portletcontainer.common.PortletContainerConstants;
import com.sun.portal.portletcontainer.invoker.WindowInvokerConstants;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements <tt>cloneBeforeWrite</tt> portlet state change behavior.
 * This class clones the portlet and updates its preferences only if the portlet made any
 * preference updates and if the portlet is not already a clone.
 * <p>
 * <b>Note: </b> In order to use this implementation you need to add to <tt>wsrpconfig.properties</tt>
 * file the following property:<br />
 * com.sun.portal.wsrp.producer.markup.clonebeforewritehandler.classname=com.hp.it.spf.wsrp.openportal.producer.markup.impl.CloneBeforeWriteHandlerImpl
 *  
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class CloneBeforeWriteHandlerImpl implements CloneBeforeWriteHandler {

	private Producer mProducer;
	private HttpServletRequest mRequest;
	private RegistrationContext mRegistrationContext;
	private PortletContext mPortletContext;
	private UserContext mUserContext;


	/**
	 * Initializes the internal state of this class. It also sets an empty map as the request's
	 * <tt>PortletContainerConstants.PREFERENCE_STAGING_MAP</tt> attribute to tell the portlet
	 * preferences class implementation that the uplon call to <tt>store</tt> the portlet should
	 * not be saved immediately to the data store but should rather be staged in the provided map
	 * @param producer producer for which this WSRP request is invoked
	 * @param request request associated with the WSRP web service invocation
	 * @param registrationContext consumer registration context
	 * @param portletContext portlet context containing the portlet handle
	 * @param userContext user context
	 */
	public void init(Producer producer, HttpServletRequest request, RegistrationContext registrationContext, PortletContext portletContext, UserContext userContext) {
		boolean shouldClone =
				portletContext != null &&
						portletContext.getPortletHandle() != null &&
						!portletContext.getPortletHandle().startsWith("__WSRP_CLONED__");
		mProducer = producer;
		mRequest = request;
		mRegistrationContext = registrationContext;
		mPortletContext = portletContext;
		mUserContext = userContext;

		if (shouldClone) {
			mRequest.setAttribute(PortletContainerConstants.PREFERENCE_STAGING_MAP, new HashMap());
		}
	}

	/**
	 * Clones the portlet and saves its preferneces if there are any preference updates.
	 * @return new portlet's context or <tt>null</tt> if portlet was not cloned
	 * @throws ProducerException If an error occurs when creating portlet clone or saving its
	 * preferences.
	 */
	public PortletContext handle() throws ProducerException {
		// we don't need to clone the portlet if either this portlet is already a clone (i.e.
		// the map was not set in "init" method), or if the portlet didn't save any preferences.
		Map<String, String> preferencesStagingMap = getPreferenceStagingMap();
		if (preferencesStagingMap == null || preferencesStagingMap.isEmpty()) {
			return null;
		}

		PortletContext clonePortletContext = clonePortlet();
		if (clonePortletContext != null) {
			setPortletPreferences(clonePortletContext, preferencesStagingMap);
		}
		return clonePortletContext;
	}

	/**
	 * @return portlet preferences staging map if it was set in the request; <tt>null</tt> otherwise
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> getPreferenceStagingMap() {
		return (Map<String, String>) mRequest.getAttribute(PortletContainerConstants.PREFERENCE_STAGING_MAP);
	}

	/**
	 * Performs the actual clone operation.
	 * @return cloned portlet context
	 * @throws ProducerException If an error occurs when cloning the portlet
	 */
	protected PortletContext clonePortlet() throws ProducerException{
		String clonePortletHandle = WSRPProducerUtility.getCloneChannelName(
				mPortletContext.getPortletHandle(),
				getUserId(mUserContext),
				mRegistrationContext.getRegistrationHandle());

		mProducer.getPortletRegistry().cloneChannel(
				mUserContext,
				clonePortletHandle,
				mPortletContext.getPortletHandle(),
				mRegistrationContext.getRegistrationHandle());

		PortletContext clonePortletContext = new PortletContext();
		clonePortletContext.setPortletHandle(clonePortletHandle);
		return clonePortletContext;
	}

	/**
	 * Saves portlet preferences.
	 * @param portletContext context of the portlet for which the preferences are to be saved
	 * @param portletPreferences portlet preferences to save
	 * @throws ProducerException If an error occurs when saving the preferences.
	 */
	protected void setPortletPreferences(PortletContext portletContext, Map<String, String> portletPreferences) throws ProducerException {
		PropertyList propertyList = new PropertyList();
		for (Map.Entry<String, String> preference : portletPreferences.entrySet()) {
			String prefName = preference.getKey();
			String prefValue = preference.getValue();
			if (prefValue == null) {
				ResetProperty rp = new ResetProperty();
				rp.setName(new QName(prefName));
				propertyList.getResetProperties().add(rp);
			}
			else {
				Property p = new Property();
				p.setName(new QName(prefName));
				p.setStringValue(prefValue);
				propertyList.getProperties().add(p);
			}
		}
		mProducer.getPortletRegistry().setPortletProperties(
				mUserContext,
				propertyList,
				portletContext.getPortletHandle(),
				mRegistrationContext.getRegistrationHandle());
	}

	//FIXME (slawek) - this method is a copy/paste from PortletRepositoryImpl.
	//Remove it once WSRP producer team exposes it so we can use it.
	private String getUserId(UserContext uc) {

		if (uc == null || uc.getUserContextKey() == null) {
			return WindowInvokerConstants.AUTHLESS_USER_ID;
		} else {
			return Base64.encode(uc.getUserContextKey());
		}
	}
}
