package com.hp.spp.portlets.paramsets;

import java.util.HashMap;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
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

import com.hp.spp.common.util.SppConstants;
import com.hp.spp.common.util.format.Formatter;
import com.hp.spp.common.util.hibernate.HibernateUtil;
import com.hp.spp.eservice.Site;
import com.hp.spp.eservice.exception.XmlImportException;

/**
 * Controller import an XML file as a list of standardParameterSets
 * 
 * @author pbrether
 * 
 */
public class StandardParameterSetImportFormController extends SimpleFormController implements
		InitializingBean {

	private static Logger logger = Logger
			.getLogger(StandardParameterSetImportFormController.class);

	private StandardParameterSetManager standardParameterSetManager;

	public void afterPropertiesSet() throws Exception {
		if (this.standardParameterSetManager == null)
			throw new IllegalArgumentException("An StandardParameterSetManager is required");
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response, Object command,
			BindException errors) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("StandardParameterSetManager : onSubmitAction");
		}

		StandardParameterSetXMLUpload standardParameterSetXMLUpload = (StandardParameterSetXMLUpload) command;

		String file = request.getParameter("file");
		file = new Formatter().escapeAmpersand(file);
		standardParameterSetXMLUpload.setFile(file);

		List statusList = getStatusList(file, request);

		standardParameterSetXMLUpload.setUpdatedStandardParameterSetMap(statusList);

		request.getPortletSession().setAttribute("xmlUpload", standardParameterSetXMLUpload);
	}

	private List getStatusList(String file, ActionRequest request) throws Exception {
		Transaction tx = null;
		List list = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			list = standardParameterSetManager.importXML(file.getBytes(), getSite(request));
		} catch (XmlImportException e) {
			// the import exception can be rethrown to be caught by spring
			throw e;
		} catch (Exception e) {
			logger.error("Error getting eservice importing the XML: [" + file + "]", e);
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

		return list;
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
