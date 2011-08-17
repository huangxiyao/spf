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
import com.hp.spp.groups.dao.SiteDAOCacheImpl;

/**
 * Controller to reset the Cache.
 * 
 * @author MJULIENS
 * 
 */
public class GroupResetCacheController extends AbstractController implements
		InitializingBean {

	private GroupService groupService;
	
	public void afterPropertiesSet() throws Exception {
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
			
			// reset the cache for the current site
			logger.debug("Remove the site ["+siteName+"] from the cache and load it again from DB");
			SiteDAOCacheImpl.getInstance().removeFromCache(siteName);
			
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
		
		// add the confirmation message in the request
		request.getPortletSession().setAttribute("confirmationMessage",
				"The cache is reset", PortletSession.APPLICATION_SCOPE);
		
		
		return new ModelAndView("groupsView", "model", model);
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

}
