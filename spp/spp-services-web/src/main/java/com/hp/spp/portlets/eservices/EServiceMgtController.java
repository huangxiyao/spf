package com.hp.spp.portlets.eservices;

import java.util.HashMap;
import java.util.Set;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import com.hp.spp.common.util.SppConstants;
import com.hp.spp.common.util.hibernate.HibernateUtil;
import com.hp.spp.eservice.Site;

/**
 * 
 * @author PBRETHER
 * 
 */
public class EServiceMgtController extends AbstractController implements InitializingBean {

	private EServiceManager eServiceManager;

	public EServiceManager getEServiceManager() {
		return eServiceManager;
	}

	public void setEServiceManager(EServiceManager serviceManager) {
		eServiceManager = serviceManager;
	}

	public void afterPropertiesSet() throws Exception {
		if (this.eServiceManager == null)
			throw new IllegalArgumentException("A EServiceManager is required");
	}

	public ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {

		Set eServicesList = getEServicesList(request);
		
		request.getPortletSession().setAttribute("backView", "eservices", PortletSession.APPLICATION_SCOPE);

		return new ModelAndView("eServicesMgtView", "eServiceList", eServicesList);
	}

	private Set getEServicesList(RenderRequest request) throws ServletException {
		
		// TODO: this is horrible!! Look at using Spring templates.
		Transaction tx = null;
		Set eServicesList;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			eServicesList = getSite(request).getEServiceList();
			Hibernate.initialize(eServicesList);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error getting eservices", e);
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
		return eServicesList;
	}

	private Site getSite(RenderRequest request) {
		HashMap UseContextKeys = (HashMap) request
				.getAttribute(SppConstants.SPP_USERCONTEXTKEYS_MAP_NAME);
		return eServiceManager.getSite(UseContextKeys);
	}

}
