package com.hp.spp.portlets.eservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.servlet.ServletException;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.mvc.SimpleFormController;

import com.hp.spp.common.util.SppConstants;
import com.hp.spp.common.util.hibernate.HibernateUtil;
import com.hp.spp.common.util.xml.XMLContent;
import com.hp.spp.eservice.Site;

/**
 * Controller to export the XML file stored in the history table
 * 
 * @author pbrether
 * 
 */
public class EServiceExportFormController extends SimpleFormController implements
		InitializingBean {

	private EServiceManager eServiceManager;

	public void afterPropertiesSet() throws Exception {
		if (this.eServiceManager == null)
			throw new IllegalArgumentException("A EServiceManager is required");
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response, Object command,
			BindException errors) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("EServiceExportFormController : onSubmitAction");
		}

		XMLContent xmlContent = (XMLContent) command;

		// get the list of eservices to export
		String[] formContent = request.getParameterValues("eServiceId");
		
		if (logger.isDebugEnabled()) {
			logger.debug("The list of checkboxes is [" + formContent + "]");
		}

		Transaction tx = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

			// get the names of the eservices according to the ids
			List eServiceIdsList = new ArrayList();
			for (int i = 0; i < formContent.length; i++) {
				eServiceIdsList.add(new Long(formContent[i]));
			}

			byte[] xmlToDisplay = eServiceManager
					.getEServiceDefinitions(eServiceIdsList, getSite(request));

			xmlContent.setXmlContent(xmlToDisplay);

			tx.commit();

		} catch (Exception e) {
			logger.error("Catch Exception during exporting the xml file [" + e.getMessage() + "]", e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException he) {
					logger.error("Exception during ROLLBACK of Hibernate transaction", he);
				}
				tx = null;
			}
			// Raising exception (will be caught by the framework)
			throw new ServletException(e);
		} finally {
			try {
				HibernateUtil.getSessionFactory().getCurrentSession().close();
			} catch (HibernateException he) {
				logger.error("Exception during CLOSE of Hibernate transaction", he);
			}
		}
	}

	private Site getSite(ActionRequest request) {
		HashMap UseContextKeys = (HashMap) request
				.getAttribute(SppConstants.SPP_USERCONTEXTKEYS_MAP_NAME);
		return eServiceManager.getSite(UseContextKeys);
	}

	protected Object formBackingObject(PortletRequest request) throws Exception {
		return new XMLContent();
	}

	public void setEServiceManager(EServiceManager eServiceManager) {
		this.eServiceManager = eServiceManager;
	}

	protected boolean isInvalidSubmission(PortletRequest arg0) {
		return false;
	}

}
