/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.fast.web.http;

import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class MediaTypeNegotiatorTest extends TestCase {
    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    public void testMatchesPreferred() throws NoAcceptableMediaTypeException {
        MediaTypeNegotiator mn = new MediaTypeNegotiator(new Object[] {
                "text/xml", "application/xhtml+xml" });
        request.addHeader("Accept",
                "text/html;q=0.8,application/xhtml+xml;q=1.0");
        mn.negotiate(request, response);

        assertEquals("application/xhtml+xml", mn.negotiatedValue(request));
        assertEquals("application/xhtml+xml", response.getContentType());
    }

    public void testMatchesNonPreferred() throws NoAcceptableMediaTypeException {
        MediaTypeNegotiator mn = new MediaTypeNegotiator(new Object[] {
                "text/xml", "application/xhtml+xml" });
        request.addHeader("Accept",
                "text/html;q=1.0,application/xhtml+xml;q=0.5");
        mn.negotiate(request, response);

        assertEquals("application/xhtml+xml", mn.negotiatedValue(request));
        assertEquals("application/xhtml+xml", response.getContentType());
    }

    public void testNotSpecified() throws NoAcceptableMediaTypeException {
        MediaTypeNegotiator mn = new MediaTypeNegotiator(new Object[] {
                "application/xhtml+xml", "text/xml" });
        mn.negotiate(request, response);

        assertEquals("application/xhtml+xml", mn.negotiatedValue(request));
        assertEquals("application/xhtml+xml", response.getContentType());
    }

    public void testNotMatches() {
        MediaTypeNegotiator mn = new MediaTypeNegotiator(new Object[] {
                "text/xml", "application/xhtml+xml" });
        request.addHeader("Accept", "text/html");
        try {
            mn.negotiate(request, response);
            fail("NoAcceptableMediaTypeException expected.");
        } catch (NoAcceptableMediaTypeException ex) {
        }

        assertNull(mn.negotiatedValue(request));
        assertEquals(new MockHttpServletResponse().getContentType(), response
                .getContentType());
    }

}
