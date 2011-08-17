package com.hp.spp.portlets.eservices;

import java.util.HashMap;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.SimpleFormController;

import com.hp.spp.common.exception.BrowserBackException;
import com.hp.spp.common.util.SppConstants;
import com.hp.spp.common.util.hibernate.HibernateUtil;
import com.hp.spp.eservice.EService;
import com.hp.spp.eservice.Site;
import com.hp.spp.eservice.dao.EServiceDAO;

/**
 * Controller validate the delete of eServices in DB
 * 
 * @author pbrether
 * 
 */
public class EServiceSaveDeleteFormController extends SimpleFormController implements
		InitializingBean {

	private EServiceManager eServiceManager;

	private static Logger logger = Logger.getLogger(EServiceSaveDeleteFormController.class);

	public void afterPropertiesSet() throws Exception {
		if (this.eServiceManager == null)
			throw new IllegalArgumentException("An EServiceManager is required");
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response, Object command,
			BindException errors) throws Exception {

		String changeOwner = request.getParameter("changeOwner");

		String comment = request.getParameter("comment");

		EServiceList eServicesList = (EServiceList) command;

		if(logger.isDebugEnabled()) {
			logger.debug("EServiceSaveDeleteFormController : onSubmitAction");
			logger.debug("changeOwner: " + changeOwner);
			logger.debug("comment: " + comment);
			logger.debug(eServicesList.getEServiceNameList());
		}

		Transaction tx = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

			// create the string to store the changes
			StringBuffer dataChange = new StringBuffer();
			for (int i = 0; i < eServicesList.getEServiceNameList().size(); i++) {
				String serviceName = (String) eServicesList.getEServiceNameList().get(i);
				dataChange.append(serviceName + " - deleted");
				if (i != eServicesList.getEServiceNameList().size() - 1) {
					dataChange.append("<br>");
				}
			}
			if (logger.isDebugEnabled()) {
			logger.debug("Datachange " + dataChange.toString());
			}

		    eServiceManager.updateResourceHistory(changeOwner, comment, dataChange.toString(), getSite(request));

			// browse all the eservices names contained in the list and delete them
			for (int i = 0; i < eServicesList.getEServiceIdList().size(); i++) {
				// delete in database
				long id = Long.parseLong((String) eServicesList.getEServiceIdList().get(i));
				EService currentService = EServiceDAO.getInstance().load(id);
				EServiceDAO.getInstance().delete(currentService);
			}

			tx.commit();

		} catch (Exception e) {
			logger.error("Error during delete of eServices", e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException he) {
					logger.error("Error during ROLLBACK of Hibernate transaction", he);
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

		// add the confirmation message in the request
		request.getPortletSession().setAttribute("confirmationMessage",
				"The eServices have been deleted", PortletSession.APPLICATION_SCOPE);
		response.setRenderParameter("action", "eServices");
	}

	/**
	 * In this controle, the command object (upload information) must be read from the session.
	 */
	protected Object formBackingObject(PortletRequest request) throws Exception {

		EServiceList eServiceList = (EServiceList) request.getPortletSession().getAttribute("eServiceList");
		if (eServiceList == null)	{
			logger.error("Unable to get the list of eServices to delete, it could be due to a back in the browser");
			throw new BrowserBackException("Unable to get the list of eServices to delete, please do not use the \"Back\" button in your browser");
		}
		
		// remove this attribute from the session
		request.getPortletSession().removeAttribute("eServiceList");

		return eServiceList;
	}

	protected ModelAndView renderInvalidSubmit(RenderRequest request, RenderResponse response)
			throws Exception {
		return null;
	}

	protected boolean isInvalidSubmission(PortletRequest arg0) {
		return false;
	}

	public void setEServiceManager(EServiceManager eServiceManager) {
		this.eServiceManager = eServiceManager;
	}

	private Site getSite(ActionRequest request) {
		HashMap UseContextKeys = (HashMap) request
				.getAttribute(SppConstants.SPP_USERCONTEXTKEYS_MAP_NAME);
		return eServiceManager.getSite(UseContextKeys);
	}

}
