package com.hp.it.spf.xa.mock.portal;

import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.MockHttpServletResponse;

import com.vignette.portal.website.enduser.PortalResponse;

public class MockPortalResponse implements PortalResponse {
    MockHttpServletResponse response = new MockHttpServletResponse();
    
    public HttpServletResponse getResponse() {
        return response;
    }

}
