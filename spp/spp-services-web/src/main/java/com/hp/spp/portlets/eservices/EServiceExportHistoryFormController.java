package com.hp.spp.portlets.eservices;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.mvc.SimpleFormController;

import com.hp.spp.common.ResourceHistory;
import com.hp.spp.common.dao.ResourceHistoryDAO;
import com.hp.spp.common.util.hibernate.HibernateUtil;
import com.hp.spp.common.util.xml.XMLContent;

/**
 * Controller to export the XML file stored in the history table
 * 
 * @author pbrether
 * 
 */
public class EServiceExportHistoryFormController extends SimpleFormController
		implements InitializingBean {

	private static Logger logger = Logger.getLogger(EServiceExportHistoryFormController.class);
	
	private EServiceManager eServiceManager;

	public void afterPropertiesSet() throws Exception {
		if (this.eServiceManager == null)
			throw new IllegalArgumentException("A EServiceManager is required");
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response,
			Object command, BindException errors) throws Exception {

		XMLContent xmlContent = (XMLContent) command;

		// get the id of the ressource history entry in the table
		long id = Long.parseLong(request.getParameter("backupId"));
		Transaction tx = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession()
					.beginTransaction();

			ResourceHistory history = ResourceHistoryDAO.getInstance().load(id);

			xmlContent.setXmlContent(history.getBackupFile());
			
			tx.commit();

		} catch (Exception e) {
			logger.error("Catch Exception during exporting the xml file", e);
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

		XMLContent xmlContent;

		xmlContent = new XMLContent();

		return xmlContent;
	}

	public void setEServiceManager(EServiceManager eServiceManager) {
		this.eServiceManager = eServiceManager;
	}

	protected boolean isInvalidSubmission(PortletRequest arg0) {
		return false;
	}

}
