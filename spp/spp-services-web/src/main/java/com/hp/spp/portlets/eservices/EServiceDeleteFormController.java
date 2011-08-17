package com.hp.spp.portlets.eservices;

import java.util.ArrayList;
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
import com.hp.spp.eservice.EService;

/**
 * Controller to delete a list of eServices
 * 
 * @author PBRETHER
 * 
 */
public class EServiceDeleteFormController extends SimpleFormController implements
		InitializingBean {

	private static Logger logger = Logger.getLogger(EServiceDeleteFormController.class);

	private EServiceManager eServiceManager;

	public void afterPropertiesSet() throws Exception {
		if (this.eServiceManager == null)
			throw new IllegalArgumentException("A EServiceManager is required");
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response, Object command,
			BindException errors) throws Exception {

		EServiceList eServiceList = (EServiceList) command;

		String[] formContent = request.getParameterValues("eServiceId");
		
		if (logger.isDebugEnabled()) {
			logger.debug("The list of checkboxes is [" + formContent + "]");
		}

		// fill the list of eService ids to delete
		// fill the list of eService names to delete
		List idList = new ArrayList();
		List nameList = new ArrayList();
		for (int i = 0; i < formContent.length; i++) {
			idList.add(formContent[i]);
			// get the name of the eService from its Id
			EService eService = getEService(Long.parseLong(formContent[i]));
			nameList.add(eService.getName());
		}
		eServiceList.setEServiceIdList(idList);
		eServiceList.setEServiceNameList(nameList);

		if (logger.isDebugEnabled()) {
			logger.debug("delete : Id list [" + eServiceList.getEServiceIdList() + "]");
			logger.debug("delete : name list [" + eServiceList.getEServiceNameList() + "]");
		}

		request.getPortletSession().setAttribute("eServiceList", eServiceList);

	}

	private EService getEService(long id) throws ServletException, BrowserBackException {
		
		EService eservice = null; 
		Transaction tx = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			eservice = eServiceManager.getEService(id);
			tx.commit();
		}
		catch (ObjectNotFoundException e)	{
			logger.info("EService with id ["+id+"] not found, must already have been deleted" +
					" - probably due to browser back being pressed");
			throw new BrowserBackException("At least one eService not found, must already have been deleted");
		}
		catch (Exception e) {
			logger.error("Error getting eservice with id ["+id+"]", e);
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
			
		return eservice;
	}

	protected Object formBackingObject(PortletRequest request) throws Exception {
		return new EServiceList();
	}

	protected ModelAndView renderInvalidSubmit(RenderRequest request, RenderResponse response)
			throws Exception {
		return null;
	}

	public void setEServiceManager(EServiceManager eServiceManager) {
		this.eServiceManager = eServiceManager;
	}

	protected boolean isInvalidSubmission(PortletRequest arg0) {
		return false;
	}

}
