package com.hp.spp.portlets.groups;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletException;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import com.hp.spp.common.util.SppConstants;
import com.hp.spp.common.util.hibernate.HibernateUtil;
import com.hp.spp.cache.Cache;

/**
 * Controller to display the list of groups of a Site.
 * 
 * @author MJULIENS
 * 
 */
public class GroupController extends AbstractController implements
		InitializingBean {

	private GroupService groupService;

	public void afterPropertiesSet() throws Exception {
//		if (this.groupService == null)
//			throw new IllegalArgumentException("A GroupService is required");
	}

	public ModelAndView handleRenderRequestInternal(RenderRequest request,
													RenderResponse response) throws Exception {

		Map model = new HashMap();
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


			model.put("groups", groupService.getGroupList());

		} catch (Exception e) {
			logger.error("Error during display list of groups", e);
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

		request.getPortletSession().setAttribute("backView", "groups", PortletSession.APPLICATION_SCOPE);

		// Set in session the links to refresh the cache
		String urlCache = Cache.getConfig().getProperty("cache.cluster.http.urls");
		if (logger.isDebugEnabled()) {
			if (urlCache != null && !urlCache.trim().equals("")) {
				logger.debug("URLs to notify: " + urlCache);
			}
			else {
				logger.debug("No URLs to notify as 'cache.cluster.http.urls' is empty or not set!");
			}
		}
		request.getPortletSession().setAttribute("urlCache", urlCache, PortletSession.APPLICATION_SCOPE);

		return new ModelAndView("groupsView", "model", model);
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
}
