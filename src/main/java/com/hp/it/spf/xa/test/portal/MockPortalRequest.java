package com.hp.it.spf.xa.test.portal;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.vignette.portal.website.enduser.PortalRequest;

public class MockPortalRequest implements PortalRequest {
    private MockHttpServletRequest request = new MockHttpServletRequest();
    private MockHttpSession session = new MockHttpSession();
    
    public Object getAttribute(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public Enumeration getAttributeNames() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getContextPath() {
        // TODO Auto-generated method stub
        return null;
    }

    public Locale getLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getParameter(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public Map getParameterMap() {
        // TODO Auto-generated method stub
        return null;
    }

    public Enumeration getParameterNames() {
        // TODO Auto-generated method stub
        return null;
    }

    public String[] getParameterValues(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getQueryString() {
        // TODO Auto-generated method stub
        return null;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public String getServletPath() {
        // TODO Auto-generated method stub
        return null;
    }

    public HttpSession getSession() {
        return session;
    }

    public String getTemplateFriendlyId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getURIContextAttribute(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isDisplayRequest() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isProcessRequest() {
        // TODO Auto-generated method stub
        return false;
    }

    public void removeAttribute(String arg0) {
        // TODO Auto-generated method stub

    }

    public void setAttribute(String arg0, Object arg1) {
        // TODO Auto-generated method stub

    }

}
