package com.hp.spp.portlets.paramsets;

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
public class StandardParameterSetHistoryController extends AbstractController implements
		InitializingBean {
	
	private static Logger logger = Logger.getLogger(StandardParameterSetHistoryController.class);

	private StandardParameterSetManager standardParameterSetManager;

	public void afterPropertiesSet() throws Exception {
		if (this.standardParameterSetManager == null)
			throw new IllegalArgumentException("A StandardParameterSetManager is required");
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

			model.put("history", standardParameterSetManager.getHistoryList(getSite(request)));

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

	public void setStandardParameterSetManager(StandardParameterSetManager standardParameterSetManager) {
		this.standardParameterSetManager = standardParameterSetManager;
	}

	private Site getSite(RenderRequest request) {
		HashMap UseContextKeys = (HashMap) request
				.getAttribute(SppConstants.SPP_USERCONTEXTKEYS_MAP_NAME);
		return standardParameterSetManager.getSite(UseContextKeys);
	}
}
