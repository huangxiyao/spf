package com.hp.spp.portlets.paramsets;

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

import com.hp.spp.common.exception.BrowserBackException;
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
public class StandardParameterSetExportFormController extends SimpleFormController implements
		InitializingBean {

	private StandardParameterSetManager standardParameterSetManager;

	public void afterPropertiesSet() throws Exception {
		if (this.standardParameterSetManager == null)
			throw new IllegalArgumentException("A StandardParameterSetManager is required");
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response, Object command,
			BindException errors) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("StandardParameterSetExportFormController : onSubmitAction");
		}

		XMLContent xmlContent = (XMLContent) command;

		// get the list of eservices to export
		String[] formContent = request.getParameterValues("standardParameterSetId");
		if (formContent == null || formContent.length == 0)	{
			logger.error("Tried to delete an eservice that does not exist in the Site. Back button pressed.");
			throw new BrowserBackException("Unable to delete the eServices because at least one of the selected eServices no longer exists");
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("The list of checkboxes is [" + formContent + "]");
		}

		Transaction tx = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

			// get the names of the eservices according to the ids
			List standardParameterSetIdsList = new ArrayList();
			for (int i = 0; i < formContent.length; i++) {
				standardParameterSetIdsList.add(new Long(formContent[i]));
			}

			Site site = getSite(request);
			byte[] xmlToDisplay = standardParameterSetManager.getStandardParameterSetDefinitions(standardParameterSetIdsList, site);

			xmlContent.setXmlContent(xmlToDisplay);

			tx.commit();

		} catch (Exception e) {
			logger.error("Catch Exception during exporting the xml file ["
					+ e.getMessage() + "]", e);
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

	protected Object formBackingObject(PortletRequest request) throws Exception {
		return new XMLContent();
	}

	public void setStandardParameterSetManager(StandardParameterSetManager standardParameterSetManager) {
		this.standardParameterSetManager = standardParameterSetManager;
	}

	protected boolean isInvalidSubmission(PortletRequest arg0) {
		return false;
	}

	private Site getSite(ActionRequest request) {
		HashMap UseContextKeys = (HashMap) request
				.getAttribute(SppConstants.SPP_USERCONTEXTKEYS_MAP_NAME);
		return standardParameterSetManager.getSite(UseContextKeys);
	}
}
