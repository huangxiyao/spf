package com.hp.spp.portlets.paramsets;

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
public class StandardParameterSetController extends AbstractController implements InitializingBean {

	private StandardParameterSetManager standardParameterSetManager;

	public StandardParameterSetManager getStandardParameterSetManager() {
		return standardParameterSetManager;
	}

	public void setStandardParameterSetManager(StandardParameterSetManager paramManager) {
		standardParameterSetManager = paramManager;
	}

	public void afterPropertiesSet() throws Exception {
		if (this.standardParameterSetManager == null)
			throw new IllegalArgumentException("A StandardParameterSetManager is required");
	}

	public ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {

		Set standardParameterSetsList = getStandardParameterSetsList(request);
		
		request.getPortletSession().setAttribute("backView", "standardParameterSets", PortletSession.APPLICATION_SCOPE);

		return new ModelAndView("standardParameterSetsView", "standardParameterSetList", standardParameterSetsList);
	}

	private Set getStandardParameterSetsList(RenderRequest request) throws ServletException {
		
		// TODO: this is horrible!! Look at using Spring templates.
		Transaction tx = null;
		Set standardParameterSetsList;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			standardParameterSetsList = getSite(request).getStandardParameterSet();
			Hibernate.initialize(standardParameterSetsList);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error getting standard parameter sets", e);
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
		return standardParameterSetsList;
	}

	private Site getSite(RenderRequest request) {
		HashMap UseContextKeys = (HashMap) request
				.getAttribute(SppConstants.SPP_USERCONTEXTKEYS_MAP_NAME);
		return standardParameterSetManager.getSite(UseContextKeys);
	}

}
