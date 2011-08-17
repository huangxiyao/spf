package com.hp.spp.portlets.groups;

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

/**
 * Controller to display the history of changes on the Site.
 * 
 * @author MJULIENS
 * 
 */
public class GroupHistoryController extends AbstractController implements
		InitializingBean {
	
	private static Logger logger = Logger.getLogger(GroupHistoryController.class);

	private GroupService groupService;

	public void afterPropertiesSet() throws Exception {
//		if (this.groupService == null)
//			throw new IllegalArgumentException("A GroupService is required");
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

			// get the name of the site from the passing parameters
			// and load it from the cache
			String siteName = null;
			
			HashMap UseContextKeys = (HashMap) request.getAttribute(SppConstants.SPP_USERCONTEXTKEYS_MAP_NAME);
			if ((UseContextKeys!=null) && (UseContextKeys
					.get(SppConstants.SPP_USERCONTEXTKEYS_KEY_SITE_NAME) != null) ) {
				siteName = (String) UseContextKeys
						.get(SppConstants.SPP_USERCONTEXTKEYS_KEY_SITE_NAME);
			}
			groupService = new GroupService();
			groupService.init(siteName);
			
			model.put("history", groupService.getHistoryList());

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

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
}
