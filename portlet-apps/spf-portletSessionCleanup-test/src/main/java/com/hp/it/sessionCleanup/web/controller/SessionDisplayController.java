package com.hp.it.sessionCleanup.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hp.it.spf.xa.misc.Consts;

/**
 * Controller responsible for handling requests for the portlet's VIEW mode.
 */
@Controller
@RequestMapping("VIEW")
public class SessionDisplayController 
{
    
	// ------------------------------------------------------ Private Constants   

    protected static Log log = LogFactory.getLog(SessionDisplayController.class);
    private static final String VIEW_NAME = "sessionAttributes";  
    
	// --------------------------------------------------------- Public Methods
    
	
	@SuppressWarnings("unchecked")
	@RequestMapping
    public String doRender(RenderRequest request,RenderResponse response, Model model)
    {
		PortletSession session = request.getPortletSession();

		Map<String, Object> userProfile = (Map<String, Object>) request.getAttribute("SPF_USER_PROFILE");
		String loginId = (String)userProfile.get("LoginId");
		if (loginId == null || loginId.trim().length() == 0) {
			loginId = "sso_guest_user";
		}
		request.setAttribute("user", loginId);
		List<String> spfPortletScopeAttributes = new ArrayList<String>();
		List<String> retainPortletScopeAttributes = new ArrayList<String>();
		List<String> otherPortletScopeAttributes = new ArrayList<String>();
		List<String> spfApplicationScopeAttributes = new ArrayList<String>();
		List<String> retainApplicationScopeAttributes = new ArrayList<String>();
		List<String> otherApplicationScopeAttributes = new ArrayList<String>();

		Set<String> portletScopeAttributes = session.getAttributeMap(PortletSession.PORTLET_SCOPE).keySet();
		for (String key: portletScopeAttributes) {
			if (key != null && key.trim().length() != 0) {
				if ( key.startsWith(Consts.UNSTICKY_SESSION_ATTR_PREFIX) && 
						!key.startsWith(Consts.STICKY_SESSION_ATTR_PREFIX)) {
					spfPortletScopeAttributes.add(key);
				}
				else if ( key.startsWith(Consts.STICKY_SESSION_ATTR_PREFIX)) {
					retainPortletScopeAttributes.add(key);
				}
				else {
					otherPortletScopeAttributes.add(key);
				}
			}
			
		}

		Set<String> applicationScopeAttributes = session.getAttributeMap(PortletSession.APPLICATION_SCOPE).keySet();
		for (String key: applicationScopeAttributes) {
			if (key != null && key.trim().length() != 0) {
				if ( key.startsWith(Consts.UNSTICKY_SESSION_ATTR_PREFIX) && 
						!key.startsWith(Consts.STICKY_SESSION_ATTR_PREFIX)) {
					spfApplicationScopeAttributes.add(key);
				}
				else if ( key.startsWith(Consts.STICKY_SESSION_ATTR_PREFIX)) {
					retainApplicationScopeAttributes.add(key);
				}
				else {
					otherApplicationScopeAttributes.add(key);
				}
			}
			
		}
		
		request.setAttribute("spfPortletScopeAttributes", spfPortletScopeAttributes);
		request.setAttribute("retainPortletScopeAttributes", retainPortletScopeAttributes);
		request.setAttribute("otherPortletScopeAttributes", otherPortletScopeAttributes);

		request.setAttribute("spfApplicationScopeAttributes", spfApplicationScopeAttributes);
		request.setAttribute("retainApplicationScopeAttributes", retainApplicationScopeAttributes);
		request.setAttribute("otherApplicationScopeAttributes", otherApplicationScopeAttributes);
		
        return VIEW_NAME;
    }
    
	@RequestMapping
    public void doAction(ActionRequest request, ActionResponse response) {
		String action = request.getParameter("action");
		PortletSession session = request.getPortletSession();
		if ("Add Session Attributes".equals(action)) {

			session.setAttribute("SPF_KEY1", "truevalue", PortletSession.PORTLET_SCOPE);
			session.setAttribute("SPF_KEY2", "falsevalue", PortletSession.PORTLET_SCOPE);
			session.setAttribute("SPF_RETAIN_KEY1", "John", PortletSession.PORTLET_SCOPE);
			session.setAttribute("SPF_RETAIN_KEY2", "Smith", PortletSession.PORTLET_SCOPE);
			session.setAttribute("SESSION_Key1", "Smith", PortletSession.PORTLET_SCOPE);
			session.setAttribute("SESSION_Key2", "Smith", PortletSession.PORTLET_SCOPE);
	
			session.setAttribute("SPF_KEY3", "truevalue", PortletSession.APPLICATION_SCOPE);
			session.setAttribute("SPF_KEY4", "falsevalue", PortletSession.APPLICATION_SCOPE);
			session.setAttribute("SPF_RETAIN_KEY3", "John", PortletSession.APPLICATION_SCOPE);
			session.setAttribute("SPF_RETAIN_KEY4", "Smith", PortletSession.APPLICATION_SCOPE);
			session.setAttribute("SESSION_Key3", "Smith", PortletSession.APPLICATION_SCOPE);
			session.setAttribute("SESSION_Key4", "Smith", PortletSession.APPLICATION_SCOPE);
		}
    }
}
