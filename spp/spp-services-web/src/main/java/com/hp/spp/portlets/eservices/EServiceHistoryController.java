package com.hp.spp.portlets.eservices;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import com.hp.spp.common.util.SppConstants;
import com.hp.spp.common.util.hibernate.HibernateUtil;
import com.hp.spp.eservice.Site;

/**
 * Controller to display the history of changes on the Site.
 * 
 * @author pbrether
 * 
 */
public class EServiceHistoryController extends AbstractController implements
		InitializingBean {
	
	private static Logger logger = Logger.getLogger(EServiceHistoryController.class);

	private EServiceManager eServiceManager;

	public void afterPropertiesSet() throws Exception {
		if (this.eServiceManager == null)
			throw new IllegalArgumentException("A EServiceManager is required");
	}

	public ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Group History Controller");
		}

		Map model = new HashMap();

		Transaction tx = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession()
					.beginTransaction();

			model.put("history", eServiceManager.getHistoryList(getSite(request)));

			tx.commit();

		} catch (Exception e) {
			logger.error("Error during get history list for the site", e);
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
				logger.error("Error during CLOSE of Hibernate transaction", he);
			}
		}
		return new ModelAndView("historyView", "model", model);
	}

	private Site getSite(RenderRequest request) {
		HashMap UseContextKeys = (HashMap) request
				.getAttribute(SppConstants.SPP_USERCONTEXTKEYS_MAP_NAME);
		return eServiceManager.getSite(UseContextKeys);
	}

	public void setEServiceManager(EServiceManager eServiceManager) {
		this.eServiceManager = eServiceManager;
	}

}
