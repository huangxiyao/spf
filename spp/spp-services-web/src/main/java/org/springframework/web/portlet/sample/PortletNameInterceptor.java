package org.springframework.web.portlet.sample;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.HandlerInterceptor;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.context.PortletApplicationObjectSupport;
import org.springframework.web.portlet.context.PortletContextAware;

public class PortletNameInterceptor extends PortletApplicationObjectSupport
        implements HandlerInterceptor, PortletContextAware {

    PortletContext portletContext;
    
	public void setPortletConfig(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

	public boolean preHandle(PortletRequest request, PortletResponse response, Object handler)
			throws Exception {
		if(logger.isInfoEnabled()) {
			logger.info("portletContextName : " +(portletContext == null ? "<no PortletContext!>" : portletContext.getPortletContextName()));
		}
        return true;
	}

	public void postHandle(
			RenderRequest request, RenderResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
	}

	public void afterCompletion(
			PortletRequest request, PortletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
