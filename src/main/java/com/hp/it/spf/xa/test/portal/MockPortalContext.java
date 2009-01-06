package com.hp.it.spf.xa.test.portal;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.epicentric.authorization.PrincipalSet;
import com.epicentric.page.Page;
import com.epicentric.site.Site;
import com.epicentric.template.Style;
import com.epicentric.user.User;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalRequest;
import com.vignette.portal.website.enduser.PortalResponse;
import com.vignette.portal.website.enduser.PortalURI;

public class MockPortalContext implements PortalContext {
    private SurrogateSite currentSite = new SurrogateSite();
    private MockHttpServletRequest request = new MockHttpServletRequest();
    private MockHttpServletResponse response = new MockHttpServletResponse();    
    
    private MockPortalRequest portalRequest = new MockPortalRequest();
    private MockPortalResponse portalResponse = new MockPortalResponse();

    public PortalURI createDisplayURI(String arg0) {
        return new MockPortalURI();
    }

    public String createMenuItemURI(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public PortalURI createPageURI(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    public PortalURI createPortletURI(String arg0, String arg1, Map arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    public PortalURI createProcessURI(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public String createTemplateProcessURI() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getBaseContextPath() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCurrentBasePortalURI() {
        // TODO Auto-generated method stub
        return null;
    }

    public PortalURI getCurrentPageURI() {
        // TODO Auto-generated method stub
        return null;
    }

    public PortalURI getCurrentPageURI(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public PortalURI getCurrentPortalURI() {
        // TODO Auto-generated method stub
        return null;
    }

    public Style getCurrentSecondaryPage() {
        return new SurrogateStyle();
    }

    public Site getCurrentSite() {
        return this.currentSite;
    }

    public Style getCurrentStyle() {
        return new SurrogateStyle();
    }

    public User getCurrentUser() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getPortalHttpRoot() {
        // TODO Auto-generated method stub
        return null;
    }

    public PortalRequest getPortalRequest() {
        return portalRequest;
    }

    public PortalResponse getPortalResponse() {
        return portalResponse;
    }

    public PortalURI getPortalURI() {
        // TODO Auto-generated method stub
        return null;
    }

    public Page getResolvedPortletPage() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSelectedMenuItemId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSiteURI(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getContextPath() {
        // TODO Auto-generated method stub
        return null;
    }

    public HttpServletRequest getHttpServletRequest() {
        return request;
    }

    public HttpServletResponse getHttpServletResponse() {
        return response;
    }

    public String getServletPath() {
        // TODO Auto-generated method stub
        return null;
    }

    public PortalURI createMenuItemURI(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    public PortalURI createTemplateProcessURIAsPortalURI() {
        // TODO Auto-generated method stub
        return null;
    }

    public PrincipalSet getCurrentUserPrincipals() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isPartialPageRequest() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setPartialPageRequest(boolean arg0) {
        // TODO Auto-generated method stub
        
    }

    public void setPartialPageResponseHeader() {
        // TODO Auto-generated method stub
        
    }

}
