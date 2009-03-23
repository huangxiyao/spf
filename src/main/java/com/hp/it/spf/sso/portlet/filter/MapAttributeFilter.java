/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portlet.filter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.EventFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;
import javax.portlet.filter.ResourceFilter;
import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.xa.misc.Consts;


/**
 * <p>
 * A filter transfers user profile map from http request to portlet request.
 * </p>
 * <p>
 * Vignette Portal does not allow sending different sets of profile attributes
 * for different sites, and the profile structure supported by Vignette Portal
 * must be predefined. In SPF, this issue was addressed by injecting user
 * profile information into the WSRP SOAP request. The injection occurred in the
 * consumer by a project called wsrp-injector. On the producer side, the profile
 * was extracted from WSRP SOAP request by another project called
 * wsrp-extractor. This project provides a filter to transfer the user profile
 * from http request to portlet request and made available to the portlets as a
 * map.
 * </p>
 * 
 * @author Oliver, Kaijian Ding, Ye Liu
 * @version TBD
 */
public class MapAttributeFilter
                               implements
                               ActionFilter,
                               RenderFilter,
                               EventFilter,
                               ResourceFilter {
    private FilterConfig filterConfig;

    private static boolean mIsLocalPortlet = false;
    static {
        try {
            // local if vap class found
            String classVignette = "com.vignette.portal.portlet.jsrcontainer.PortletCommandServlet";
            Class.forName(classVignette);
            mIsLocalPortlet = true;
        } catch (ClassNotFoundException e) {
            mIsLocalPortlet = false;
        }
    }

    // prefix of the attribute passed by VAP
    private final String VIGNETTE_PREFIX = "com.vignette.portal.attribute.portlet.";

    /**
     * init filter config for filter
     */
    public void init(FilterConfig filterConfig) throws PortletException {
        this.filterConfig = filterConfig;
    }

    public void destroy() {

    }

    /**
     * map Atttribute from portal To portlet for action request
     */
    public void doFilter(ActionRequest actionRequest,
                         ActionResponse actionResponse,
                         FilterChain filterChain) throws IOException,
                                                 PortletException {
        mapAtttributeFromPortalToPortlet(actionRequest);
        filterChain.doFilter(actionRequest, actionResponse);
    }

    /**
     * map Atttribute from portal To portlet for render request
     */
    public void doFilter(RenderRequest renderRequest,
                         RenderResponse renderResponse,
                         FilterChain filterChain) throws IOException,
                                                 PortletException {
        mapAtttributeFromPortalToPortlet(renderRequest);
        filterChain.doFilter(renderRequest, renderResponse);
    }

    /**
     * map Atttribute from portal To portlet for resource request
     */
    public void doFilter(ResourceRequest resourceRequest,
                         ResourceResponse resourceResponse,
                         FilterChain filterChain) throws IOException,
                                                 PortletException {
        mapAtttributeFromPortalToPortlet(resourceRequest);
        filterChain.doFilter(resourceRequest, resourceResponse);
    }

    /**
     * map Atttribute from portal To portlet for event request
     */
    public void doFilter(EventRequest eventRequest,
                         EventResponse eventResponse,
                         FilterChain filterChain) throws IOException,
                                                 PortletException {
        mapAtttributeFromPortalToPortlet(eventRequest);
        filterChain.doFilter(eventRequest, eventResponse);
    }

    /**
     * map all attribute passed by Portal to portlet Portal will dispatch each
     * attributes to the related destination portlets, So each individual
     * portlet will receive its own attributes
     * 
     * @param PortletRequest ActionRequest, RenderRequest
     */
    private void mapAtttributeFromPortalToPortlet(PortletRequest request) {

        if (mIsLocalPortlet) {
            forVAP(request);
        } else {
            forOpenPortal(request);
        }
    }

    /**
     * map all attribute passed by Portal to portlet in vignette
     * 
     * @param PortletRequest ActionRequest, RenderRequest
     */
    private void forVAP(PortletRequest request) {
        // retrieve all attribute names start with VIGNETTE_PREFIX
        Enumeration enums = request.getAttributeNames();
        while (enums.hasMoreElements()) {
            String attKey = (String)enums.nextElement();
            if (attKey.startsWith(VIGNETTE_PREFIX)) {
                String localKey = attKey.replaceFirst(VIGNETTE_PREFIX, "");
                // ignore the portlet name, as the portlet will receive
                // its own attributes dispatched by VAP
                int start = localKey.indexOf(".");
                localKey = localKey.substring(start + 1);

                request.setAttribute(localKey, request.getAttribute(attKey));
            }
        }
    }

    /**
     * map all attribute passed by Portal to portlet in OpenPortal
     * 
     * @param PortletRequest ActionRequest, RenderRequest
     */
    private void forOpenPortal(PortletRequest request) {
        HttpServletRequest rq = (HttpServletRequest)request.getAttribute("javax.portlet.portletc.httpServletRequest");
        if (rq.getAttribute(Consts.USER_PROFILE_KEY) instanceof Map) {
            Object obj = rq.getAttribute("com.sun.portal.portletcontainer.portlet_container_request");
            if (obj != null) {
                try {
                    Method method = obj.getClass()
                                       .getMethod("setUserInfo",
                                                  new Class[] {Map.class});
                    
                    Map userProfile = (Map)rq.getAttribute(Consts.USER_PROFILE_KEY);
                    if (userProfile != null) {
                    	userProfile = convertDataTypes(userProfile);
    				}
                    method.invoke(obj,
                                  new Object[] {userProfile});
                } catch (SecurityException e) {
                    System.out.println(e.getMessage());
                } catch (NoSuchMethodException e) {
                    System.out.println(e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                } catch (IllegalAccessException e) {
                    System.out.println(e.getMessage());
                } catch (InvocationTargetException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        // set UserContextKeys into PortletRequest with PORTAL_CONTEXT_KEY key
        request.setAttribute(Consts.PORTAL_CONTEXT_KEY,
                             rq.getAttribute(Consts.PORTAL_CONTEXT_KEY));
    }

	private Map convertDataTypes(Map userProfile) {
		// convert last change date and last login date to java.util.Date type
		DateFormat format = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
		String lastChangeDateString = (String) userProfile.get(Consts.KEY_LAST_CHANGE_DATE);
		if ((lastChangeDateString != null)
				&& !("".equals(lastChangeDateString.trim()))) {
			try {
				userProfile.put(Consts.KEY_LAST_CHANGE_DATE, format.parse(lastChangeDateString));
			} catch (ParseException e) {
				userProfile.put(Consts.KEY_LAST_CHANGE_DATE, null);
			}
		} else {
			userProfile.put(Consts.KEY_LAST_CHANGE_DATE, null);
		}
		String lastLoginDateString = (String) userProfile.get(Consts.KEY_LAST_LOGIN_DATE);
		if ((lastLoginDateString != null)
				&& !("".equals(lastLoginDateString.trim()))) {
			try {
				userProfile.put(Consts.KEY_LAST_LOGIN_DATE, format.parse(lastLoginDateString));
			} catch (ParseException e) {
				userProfile.put(Consts.KEY_LAST_LOGIN_DATE, null);
			}
		} else {
			userProfile.put(Consts.KEY_LAST_LOGIN_DATE, null);
		}
		
		return userProfile;
	}
}
