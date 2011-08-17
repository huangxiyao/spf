package com.hp.spp.portlets.eservices;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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
import com.hp.spp.eservice.EServiceStatus;
import com.hp.spp.eservice.Parameter;
import com.hp.spp.eservice.Site;
import com.hp.spp.eservice.StandardParameterSet;
import com.hp.spp.eservice.dao.EServiceDAO;
import com.hp.spp.eservice.dao.ParameterDAO;
import com.hp.spp.eservice.dao.StandardParameterSetDAO;
import com.hp.spp.eservice.exception.XmlImportException;

/**
 * Controller import an XML file as a list of eServices
 * 
 * @author pbrether
 * 
 */
public class EServiceSaveImportFormController extends SimpleFormController implements
		InitializingBean {

	private static Logger logger = Logger.getLogger(EServiceSaveImportFormController.class);

	private EServiceManager eServiceManager;

	public void afterPropertiesSet() throws Exception {
		if (this.eServiceManager == null)
			throw new IllegalArgumentException("An EServiceManager is required");
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response, Object command,
			BindException errors) throws Exception {

		String changeOwner = request.getParameter("changeOwner");

		String comment = request.getParameter("comment");

		EServiceXMLUpload xmlUpload = (EServiceXMLUpload) command;

		if (logger.isDebugEnabled()) {
			logger.debug("EServiceSaveImportFormController : onSubmitAction");
			logger.debug("changeOwner : " + changeOwner);
			logger.debug("comment : " + comment);
			logger.debug(xmlUpload.getFile());
			logger.debug(xmlUpload.getUpdatedEServiceMap());
		}

		Date currentDate = new Date();

		Transaction tx = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

			// create the string to store the changes
			StringBuffer dataChange = new StringBuffer();
			for (int i = 0; i < xmlUpload.getUpdatedEServiceMap().size(); i++) {
				EServiceStatus status = (EServiceStatus) xmlUpload.getUpdatedEServiceMap()
						.get(i);
				EService eService = status.getEService();
				if (status.getExistingFlag() == EServiceStatus.EXISTING_ESERVICE) {
					dataChange.append(eService.getName() + " - updated");
				} else {
					dataChange.append(eService.getName() + " - created");
				}
				if (i != xmlUpload.getUpdatedEServiceMap().size() - 1) {
					dataChange.append("<br>");
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Datachange " + dataChange.toString());
			}

			Site site = getSite(request);

			// create a backup in the database
			eServiceManager.updateResourceHistory(changeOwner, comment, dataChange.toString(),
					site);

			EServiceDAO eServiceDAO = EServiceDAO.getInstance();

			// manage the upload information
			for (int i = 0; i < xmlUpload.getUpdatedEServiceMap().size(); i++) {
				// for each eService in the list, treat the data
				EServiceStatus eServiceStatus = (EServiceStatus) xmlUpload
						.getUpdatedEServiceMap().get(i);
				// if its a creation, create the eService in the DB
				if (eServiceStatus.getExistingFlag() == EServiceStatus.NEW_ESERVICE) {
					if(logger.isDebugEnabled()) {
						logger.debug("create eservice ["+eServiceStatus.getEService().getName()+"] for site ["+site.getName()+"]");
					}

					// add a new eService
					EService newEService = eServiceStatus.getEService();
					setParamSet(newEService, newEService.getStandardParameterSetName(),site.getName());
					newEService.setCreationDate(currentDate);
					newEService.setLastModificationDate(currentDate);
					newEService.setSite(site);
					// save the parameters
					Set paramSet = newEService.getParameterList();
					if (paramSet != null) {
						Iterator it = paramSet.iterator();
						while (it.hasNext()) {
							Parameter param = (Parameter) it.next();
							param.setEService(newEService);
							ParameterDAO.getInstance().save(param);
						}
					}
					eServiceDAO.save(newEService);
					// add the new eService to the eServiceService
					site.getEServiceList().add(newEService);
				} else if (eServiceStatus.getExistingFlag() == EServiceStatus.EXISTING_ESERVICE) {
					if(logger.isDebugEnabled()) {
						logger.debug("update eservice ["+eServiceStatus.getEService().getName()+"] for site ["+site.getName()+"]");
					}

					// update an existing eService
					EService eService = eServiceDAO.loadBySiteIdAndName(eServiceStatus.getEService().getName(),site.getId());
					if (eService != null) {
						copyEServiceData(eService, eServiceStatus.getEService(),site.getName());
						eService.setLastModificationDate(currentDate);
						eServiceDAO.update(eService);
					} else {
						logger.warn("The eService with name ["
								+ eServiceStatus.getEService().getName()
								+ "] is not found in the DB");
					}
				} else {
					logger.warn("EService status incorrect ["
							+ eServiceStatus.getExistingFlag() + "]");
				}

				// else, update the existing eService
			}

			tx.commit();

		} catch (Exception e) {
			logger.error("Error during update of eServices", e);
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
				"The eServices have been updated", PortletSession.APPLICATION_SCOPE);
		response.setRenderParameter("action", "eServices");
	}

	private void copyEServiceData(EService serviceToUpdate, EService serviceFromXml,String siteName)
			throws XmlImportException {
		serviceToUpdate.setName(serviceFromXml.getName());
		serviceToUpdate.setMethod(serviceFromXml.getMethod());
		serviceToUpdate.setProductionUrl(serviceFromXml.getProductionUrl());
		serviceToUpdate.setTestUrl(serviceFromXml.getTestUrl());
		serviceToUpdate.setNavigationMode(serviceFromXml.getNavigationMode());
		serviceToUpdate.setSecurityMode(serviceFromXml.getSecurityMode());
		serviceToUpdate.setSimulationMode(serviceFromXml.getSimulationMode());
		serviceToUpdate.setWindowParameters(serviceFromXml.getWindowParameters());
		serviceToUpdate.setCharacterEncoding(serviceFromXml.getCharacterEncoding());
		setParamSet(serviceToUpdate, serviceFromXml.getStandardParameterSetName(),siteName);
		HashMap xmlParamsMap = createNameParamMap(serviceFromXml);
		updateParameters(xmlParamsMap, serviceToUpdate.getParameterList(), serviceToUpdate);
	}

	private void setParamSet(EService serviceToUpdate, String paramSetName, String siteName)
			throws XmlImportException {
		if (paramSetName != null && paramSetName.length() > 0) {
			StandardParameterSet paramSet = StandardParameterSetDAO.getInstance().loadByName(
					paramSetName,siteName);
			if (paramSet != null) {
				serviceToUpdate.setStandardParameterSet(paramSet);
			} else {
				// should not happen, check already made
				logger.info("The StandardParameterSet indicated [" + paramSetName
						+ "] does not exist, the user will be informed");
				throw new XmlImportException(
						"The following StandardParameterSet was in the XML "
								+ "but does not exist for this site: " + paramSetName);
			}
		} else {
			// see if there is a parameter set associated, if so remove
			StandardParameterSet paramSet = serviceToUpdate.getStandardParameterSet();
			if (paramSet != null) {
				serviceToUpdate.removeStandardParameterSet(paramSet);
			}
		}
	}

	private void updateParameters(HashMap xmlParamsMap, Set parameterList,
			EService serviceToUpdate) {
		Iterator it = new ArrayList(parameterList).iterator();
		while (it.hasNext()) {
			Parameter param = (Parameter) it.next();
			String paramName = param.getName();
			// it is contained, should be updated
			if (xmlParamsMap.containsKey(paramName)) {
				String expression = ((Parameter) xmlParamsMap.get(paramName)).getExpression();
				param.setExpression(expression);
				ParameterDAO.getInstance().update(param);
				// remove the param from the map so as to only have the
				// new parameters left at the end
				xmlParamsMap.remove(paramName);
			}
			// should be removed
			else {
				serviceToUpdate.removeParameter(param);
				ParameterDAO.getInstance().delete(param);
			}
		}
		// add the new parameters
		it = xmlParamsMap.values().iterator();
		while (it.hasNext()) {
			Parameter param = (Parameter) it.next();
			param.setEService(serviceToUpdate);
			serviceToUpdate.addParameter(param);
			ParameterDAO.getInstance().save(param);
		}
	}

	private HashMap createNameParamMap(EService serviceFromXml) {
		Set paramsList = serviceFromXml.getParameterList();
		HashMap paramsMap = new HashMap();
		if (paramsList != null) {
			Iterator it = paramsList.iterator();
			while (it.hasNext()) {
				Parameter param = (Parameter) it.next();
				paramsMap.put(param.getName(), param);
			}
		}
		return paramsMap;
	}

	/**
	 * In this controle, the command object (upload information) must be read from the session.
	 */
	protected Object formBackingObject(PortletRequest request) throws Exception {

		EServiceXMLUpload xmlUpload = (EServiceXMLUpload) request.getPortletSession()
				.getAttribute("xmlUpload");

		if (xmlUpload == null)	{
			logger.info("Unable to get the list of eServices to save/update, it could be due to a back in the browser");
			throw new BrowserBackException("Unable to get the list of eServices to save/update, please do not use the \"Back\" button in your browser");
		}
		
		// remove this attribute from the session
		request.getPortletSession().removeAttribute("xmlUpload");

		return xmlUpload;
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
