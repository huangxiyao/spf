package com.hp.spp.portlets.paramsets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Transaction;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.SimpleFormController;

import com.hp.spp.common.exception.BrowserBackException;
import com.hp.spp.common.util.hibernate.HibernateUtil;
import com.hp.spp.eservice.StandardParameterSet;

/**
 * Controller to delete a list of standardParameterSets
 * 
 * @author PBRETHER
 * 
 */
public class StandardParameterSetDeleteFormController extends SimpleFormController implements
		InitializingBean {

	private static Logger logger = Logger
			.getLogger(StandardParameterSetDeleteFormController.class);

	private StandardParameterSetManager standardParameterSetManager;

	public void afterPropertiesSet() throws Exception {
		if (this.standardParameterSetManager == null)
			throw new IllegalArgumentException("A StandardParameterSetManager is required");
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response, Object command,
			BindException errors) throws Exception {

		StandardParameterSetList standardParameterSetList = (StandardParameterSetList) command;

		String[] formContent = request.getParameterValues("standardParameterSetId");

		if (logger.isDebugEnabled()) {
			logger.debug("The list of checkboxes is [" + formContent + "]");
		}

		// fill the list of standardParameterSet ids to delete
		// fill the list of standardParameterSet names to delete
		List idList = new ArrayList();
		List nameList = getNameList(formContent, idList);

		standardParameterSetList.setStandardParameterSetIdList(idList);
		standardParameterSetList.setStandardParameterSetNameList(nameList);

		if (logger.isDebugEnabled()) {
			logger.debug("delete : Id list ["
					+ standardParameterSetList.getStandardParameterSetIdList() + "]");
			logger.debug("delete : name list ["
					+ standardParameterSetList.getStandardParameterSetNameList() + "]");
		}

		request.getPortletSession().setAttribute("standardParameterSetList",
				standardParameterSetList);

	}

	private List getNameList(String[] formContent, List idList) throws ServletException {
		Transaction tx = null;
		
		ArrayList nameList = new ArrayList();

		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			HashSet referencedParamSets = new HashSet();
			
			for (int i = 0; i < formContent.length; i++) {
				idList.add(formContent[i]);
				// get the name of the standardParameterSet from its Id
				StandardParameterSet standardParameterSet = getStandardParameterSet(Long
						.parseLong(formContent[i]));

				if (standardParameterSetManager.referencedByEService(standardParameterSet)) {
					referencedParamSets.add(standardParameterSet.getName());
				} else {
					nameList.add(standardParameterSet.getName());
				}
			}
			if (referencedParamSets.size() > 0) {
				throw new ServletException("Cannot delete the following parameter "
						+ "sets as they are referenced by at least one eService: "
						+ referencedParamSets);
			}

			tx.commit();
		} catch (Exception e) {
			logger.error("Error getting param sets from database to delete", e);
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
		return nameList;
	}

	private StandardParameterSet getStandardParameterSet(long id) throws ServletException,
			BrowserBackException {

		StandardParameterSet param = null;
		try {
			param = standardParameterSetManager.getStandardParameterSet(id);
		} catch (ObjectNotFoundException e) {
			logger.info("Param set with id [" + id
					+ "] not found, must already have been deleted" +
					" - probably due to browser back being pressed");
			throw new BrowserBackException(
					"At least on parameter set not found, must already have been deleted");
		}

		return param;
	}

	protected Object formBackingObject(PortletRequest request) throws Exception {
		return new StandardParameterSetList();
	}

	protected ModelAndView renderInvalidSubmit(RenderRequest request, RenderResponse response)
			throws Exception {
		return null;
	}

	public void setStandardParameterSetManager(
			StandardParameterSetManager standardParameterSetManager) {
		this.standardParameterSetManager = standardParameterSetManager;
	}

	protected boolean isInvalidSubmission(PortletRequest arg0) {
		return false;
	}

}
