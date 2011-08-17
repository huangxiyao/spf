package com.hp.spp.portlets.groups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hp.spp.groups.exception.XmlImportException;

/**
 * Controller import an XML file as a list of groups
 * 
 * @author MJULIENS
 * 
 */
public class GroupImportFormController extends SimpleFormController implements
		InitializingBean {

	private static Logger logger = Logger
			.getLogger(GroupImportFormController.class);

	private GroupService groupService;

	public void afterPropertiesSet() throws Exception {
		// if (this.groupService == null)
		// throw new IllegalArgumentException("A GroupService is required");
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response,
			Object command, BindException errors) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("GroupImportFormController : onSubmitAction");
		}

		XMLUpload xmlUpload = (XMLUpload) command;

		String file = request.getParameter("file");
		file = new Formatter().escapeAmpersand(file);
		xmlUpload.setFile(file);

		Transaction tx = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession()
					.beginTransaction();

			// get the name of the site from the passing parameters
			// and load it from the cache
			String siteName = null;

			HashMap UseContextKeys = (HashMap) request
					.getAttribute(SppConstants.SPP_USERCONTEXTKEYS_MAP_NAME);
			if ((UseContextKeys!=null) && (UseContextKeys
					.get(SppConstants.SPP_USERCONTEXTKEYS_KEY_SITE_NAME) != null) ) {
				siteName = (String) UseContextKeys
						.get(SppConstants.SPP_USERCONTEXTKEYS_KEY_SITE_NAME);
			}
			groupService = new GroupService();
			groupService.init(siteName);

			Map userContextKeys = (Map) request.getAttribute("com.hp.spp.UserProfile");
			List list = groupService.importXML(userContextKeys, file.getBytes());

			xmlUpload.setUpdatedGroupMap(list);
		}
		catch(XmlImportException e)	{
			throw e;
		}
		catch (Exception e) {
			logger.error("Error during display list of selected groups", e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException he) {
					logger.error(
							"Error during ROLLBACK of Hibernate transaction",
							he);
				}
				tx = null;
			}
			// Raising exception (will be caught by the framework)
			throw new ServletException(e);
		} finally {
			try {
				HibernateUtil.getSessionFactory().getCurrentSession().close();
			} catch (HibernateException he) {
				logger.error("Exception during CLOSE of Hibernate transaction",
						he);
			}
		}

		request.getPortletSession().setAttribute("xmlUpload", xmlUpload);
	}

	protected ModelAndView renderInvalidSubmit(RenderRequest request,
			RenderResponse response) throws Exception {
		return null;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	protected boolean isInvalidSubmission(PortletRequest arg0) {
		return false;
	}

}
