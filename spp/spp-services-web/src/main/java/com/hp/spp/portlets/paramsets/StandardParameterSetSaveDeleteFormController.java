package com.hp.spp.portlets.paramsets;

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
import com.hp.spp.eservice.Site;
import com.hp.spp.eservice.StandardParameterSet;
import com.hp.spp.eservice.dao.StandardParameterSetDAO;

/**
 * Controller validate the delete of standardParameterSets in DB
 * 
 * @author pbrether
 * 
 */
public class StandardParameterSetSaveDeleteFormController extends SimpleFormController implements
		InitializingBean {

	private StandardParameterSetManager standardParameterSetManager;

	private static Logger logger = Logger.getLogger(StandardParameterSetSaveDeleteFormController.class);

	public void afterPropertiesSet() throws Exception {
		if (this.standardParameterSetManager == null)
			throw new IllegalArgumentException("An StandardParameterSetManager is required");
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response, Object command,
			BindException errors) throws Exception {

		String changeOwner = request.getParameter("changeOwner");

		String comment = request.getParameter("comment");

		StandardParameterSetList standardParameterSetsList = (StandardParameterSetList) command;

		if(logger.isDebugEnabled()) {
			logger.debug("StandardParameterSetSaveDeleteFormController : onSubmitAction");
			logger.debug("changeOwner: " + changeOwner);
			logger.debug("comment: " + comment);
			logger.debug(standardParameterSetsList.getStandardParameterSetNameList());
		}

		Transaction tx = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

			// create the string to store the changes
			StringBuffer dataChange = new StringBuffer();
			for (int i = 0; i < standardParameterSetsList.getStandardParameterSetNameList().size(); i++) {
				String serviceName = (String) standardParameterSetsList.getStandardParameterSetNameList().get(i);
				dataChange.append(serviceName + " - deleted");
				if (i != standardParameterSetsList.getStandardParameterSetNameList().size() - 1) {
					dataChange.append("<br>");
				}
			}
			if (logger.isDebugEnabled()) {
			logger.debug("Datachange " + dataChange.toString());
			}

			// create a backup in the database
			standardParameterSetManager.updateResourceHistory(changeOwner, comment, dataChange
					.toString(), getSite(request));

			// browse all the eservices names contained in the list and delete them
			for (int i = 0; i < standardParameterSetsList.getStandardParameterSetIdList().size(); i++) {
				// delete in database
				long id = Long.parseLong((String) standardParameterSetsList.getStandardParameterSetIdList().get(i));
				StandardParameterSet currentService = StandardParameterSetDAO.getInstance().load(id);
				StandardParameterSetDAO.getInstance().delete(currentService);
				
				// don't think we have to do this... 
//				// remove it from the StandardParameterSet object
//				Iterator it = standardParameterSetManager.getSite().getStandardParameterSetList().iterator();
//				while (it.hasNext()) {
//					Group localGroup = (Group) it.next();
//					if (localGroup.getId() == id) {
//						standardParameterSetManager.getSite().getStandardParameterSetList().remove(localGroup);
//						break;
//					}
//				}

			}

			tx.commit();

		} catch (Exception e) {
			logger.error("Error during delete of standardParameterSets", e);
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
				"The parameter sets have been deleted", PortletSession.APPLICATION_SCOPE);
		response.setRenderParameter("action", "standardParameterSets");
	}

	/**
	 * In this controle, the command object (upload information) must be read from the session.
	 */
	protected Object formBackingObject(PortletRequest request) throws Exception {

		StandardParameterSetList standardParameterSetList = (StandardParameterSetList) request.getPortletSession().getAttribute("standardParameterSetList");

		if (standardParameterSetList == null) {
			logger.error("Unable to get the list of param sets to delete, it could be due to a back in the browser");
			throw new BrowserBackException(
					"Unable to get the list of parameter sets to delete, please do not use the \"Back\" button in your browser");
		}
		// remove this attribute from the session
		request.getPortletSession().removeAttribute("standardParameterSetList");

		return standardParameterSetList;
	}

	protected ModelAndView renderInvalidSubmit(RenderRequest request, RenderResponse response)
			throws Exception {
		return null;
	}

	protected boolean isInvalidSubmission(PortletRequest arg0) {
		return false;
	}

	public void setStandardParameterSetManager(StandardParameterSetManager standardParameterSetManager) {
		this.standardParameterSetManager = standardParameterSetManager;
	}

	private Site getSite(ActionRequest request) {
		HashMap UseContextKeys = (HashMap) request
				.getAttribute(SppConstants.SPP_USERCONTEXTKEYS_MAP_NAME);
		return standardParameterSetManager.getSite(UseContextKeys);
	}
}
