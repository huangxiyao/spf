package com.hp.spp.portlets.paramsets;

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
import com.hp.spp.eservice.Parameter;
import com.hp.spp.eservice.Site;
import com.hp.spp.eservice.StandardParameterSet;
import com.hp.spp.eservice.StandardParameterSetStatus;
import com.hp.spp.eservice.dao.ParameterDAO;
import com.hp.spp.eservice.dao.StandardParameterSetDAO;

/**
 * Controller import an XML file as a list of standardParameterSets
 * 
 * @author pbrether
 * 
 */
public class StandardParameterSetSaveImportFormController extends SimpleFormController
		implements InitializingBean {

	private static Logger logger = Logger
			.getLogger(StandardParameterSetSaveImportFormController.class);

	private StandardParameterSetManager standardParameterSetManager;

	public void afterPropertiesSet() throws Exception {
		if (this.standardParameterSetManager == null)
			throw new IllegalArgumentException("An StandardParameterSetManager is required");
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response, Object command,
			BindException errors) throws Exception {

		String changeOwner = request.getParameter("changeOwner");

		String comment = request.getParameter("comment");

		StandardParameterSetXMLUpload xmlUpload = (StandardParameterSetXMLUpload) command;

		if (logger.isDebugEnabled()) {
			logger.debug("StandardParameterSetSaveImportFormController : onSubmitAction");
			logger.debug("changeOwner : " + changeOwner);
			logger.debug("comment : " + comment);
			logger.debug(xmlUpload.getFile());
			logger.debug(xmlUpload.getUpdatedStandardParameterSetMap());
		}

		Date currentDate = new Date();

		Transaction tx = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

			// create the string to store the changes
			StringBuffer dataChange = new StringBuffer();
			for (int i = 0; i < xmlUpload.getUpdatedStandardParameterSetMap().size(); i++) {
				StandardParameterSetStatus status = (StandardParameterSetStatus) xmlUpload
						.getUpdatedStandardParameterSetMap().get(i);
				StandardParameterSet standardParameterSet = status.getStandardParameterSet();
				if (status.getExistingFlag() == StandardParameterSetStatus.EXISTING_PARAM_SET) {
					dataChange.append(standardParameterSet.getName() + " - updated");
				} else {
					dataChange.append(standardParameterSet.getName() + " - created");
				}
				if (i != xmlUpload.getUpdatedStandardParameterSetMap().size() - 1) {
					dataChange.append("<br>");
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Datachange " + dataChange.toString());
			}

			Site site = getSite(request);

			// create a backup in the database
			standardParameterSetManager.updateResourceHistory(changeOwner, comment, dataChange
					.toString(), site);

			StandardParameterSetDAO standardParameterSetDAO = StandardParameterSetDAO
					.getInstance();

			// manage the upload information
			for (int i = 0; i < xmlUpload.getUpdatedStandardParameterSetMap().size(); i++) {
				// for each standardParameterSet in the list, treat the data
				StandardParameterSetStatus standardParameterSetStatus = (StandardParameterSetStatus) xmlUpload
						.getUpdatedStandardParameterSetMap().get(i);
				// if its a creation, create the standardParameterSet in the DB
				if (standardParameterSetStatus.getExistingFlag() == StandardParameterSetStatus.NEW_PARAM_SET) {
					// add a new standardParameterSet
					StandardParameterSet newStandardParameterSet = standardParameterSetStatus
							.getStandardParameterSet();
					newStandardParameterSet.setCreationDate(currentDate);
					newStandardParameterSet.setLastModificationDate(currentDate);
					newStandardParameterSet.setSite(site);
					// save the parameters
					Set paramSet = newStandardParameterSet.getParameterList();
					if (paramSet != null) {
						Iterator it = paramSet.iterator();
						while (it.hasNext()) {
							Parameter param = (Parameter) it.next();
							param.setStandardParameterSet(newStandardParameterSet);
							ParameterDAO.getInstance().save(param);
						}
					}
					standardParameterSetDAO.save(newStandardParameterSet);
					// add the new standardParameterSet to the standardParameterSetService
					site.getStandardParameterSet().add(newStandardParameterSet);
				} else if (standardParameterSetStatus.getExistingFlag() == StandardParameterSetStatus.EXISTING_PARAM_SET) {
					// update an existing standardParameterSet
					StandardParameterSet standardParameterSet = standardParameterSetDAO
							.loadByName(standardParameterSetStatus.getStandardParameterSet()
									.getName(),site.getName());
					if (standardParameterSet != null) {
						copyStandardParameterSetData(standardParameterSet,
								standardParameterSetStatus.getStandardParameterSet());
						standardParameterSet.setLastModificationDate(currentDate);
						standardParameterSetDAO.update(standardParameterSet);
					} else {
						logger.warn("The standardParameterSet with name ["
								+ standardParameterSetStatus.getStandardParameterSet()
										.getName() + "] is not found in the DB");
					}
				} else {
					logger.warn("StandardParameterSet status incorrect ["
							+ standardParameterSetStatus.getExistingFlag() + "]");
				}

				// else, update the existing standardParameterSet
			}

			tx.commit();

		} catch (Exception e) {
			logger.error("Error during update of standardParameterSets", e);
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
				"The standardParameterSets have been updated",
				PortletSession.APPLICATION_SCOPE);
		response.setRenderParameter("action", "standardParameterSets");
	}

	private void copyStandardParameterSetData(StandardParameterSet paramToUpdate,
			StandardParameterSet paramFromXml) {
		paramToUpdate.setName(paramFromXml.getName());
		HashMap xmlParamsMap = createNameParamMap(paramFromXml);
		updateParameters(xmlParamsMap, paramToUpdate.getParameterList(), paramToUpdate);
	}

	private void updateParameters(HashMap xmlParamsMap, Set parameterList,
			StandardParameterSet paramSetToUpdate) {
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
				paramSetToUpdate.removeParameter(param);
				ParameterDAO.getInstance().delete(param);
			}
		}
		// add the new parameters
		it = xmlParamsMap.values().iterator();
		while (it.hasNext()) {
			Parameter param = (Parameter) it.next();
			param.setStandardParameterSet(paramSetToUpdate);
			paramSetToUpdate.addParameter(param);
			ParameterDAO.getInstance().save(param);
		}
	}

	private HashMap createNameParamMap(StandardParameterSet paramSetFromXml) {
		Set paramsList = paramSetFromXml.getParameterList();
		HashMap paramsMap = new HashMap(paramsList.size());
		Iterator it = paramsList.iterator();
		while (it.hasNext()) {
			Parameter param = (Parameter) it.next();
			paramsMap.put(param.getName(), param);
		}
		return paramsMap;
	}

	/**
	 * In this controle, the command object (upload information) must be read from the session.
	 */
	protected Object formBackingObject(PortletRequest request) throws Exception {

		StandardParameterSetXMLUpload xmlUpload = (StandardParameterSetXMLUpload) request
				.getPortletSession().getAttribute("xmlUpload");

		if (xmlUpload == null) {
			logger.info("Unable to get the list of param sets to save/update, it could be due to a back in the browser");
			throw new BrowserBackException(
					"Unable to get the list of parameter sets to save/update, please do not use the \"Back\" button in your browser");
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

	public void setStandardParameterSetManager(
			StandardParameterSetManager standardParameterSetManager) {
		this.standardParameterSetManager = standardParameterSetManager;
	}

	private Site getSite(ActionRequest request) {
		HashMap UseContextKeys = (HashMap) request
				.getAttribute(SppConstants.SPP_USERCONTEXTKEYS_MAP_NAME);
		return standardParameterSetManager.getSite(UseContextKeys);
	}
}
