/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.http;

import java.nio.charset.Charset;
import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.hp.it.spf.localeresolver.http.CharacterSetNegotiator;
import com.hp.it.spf.localeresolver.http.NoAcceptableCharacterSetException;

public class CharacterSetNegotiatorTest extends TestCase {
    private CharacterSetNegotiator csn = new CharacterSetNegotiator(
            new Object[] { Charset.forName("UTF-8"),
                    Charset.forName("ISO-8859-1"), });

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    public void testNoAcceptCharset() throws NoAcceptableCharacterSetException {
        // when no accepted charset, negotiated value is first provided charset
        csn.negotiate(request, response);

        assertEquals(Charset.forName("UTF-8"), csn.negotiatedValue(request));
        assertEquals(Charset.forName("UTF-8"), Charset.forName(response
                .getCharacterEncoding()));
        assertEquals("Accept-Charset", response.getHeader("Vary"));
    }

    public void testCommonValue() throws NoAcceptableCharacterSetException {
        // negotiated value is common
        request.addHeader("Accept-Charset", "ISO-8859-1;q=1.0, UTF-8;q=0.8");
        csn.negotiate(request, response);

        assertEquals(Charset.forName("ISO-8859-1"), csn
                .negotiatedValue(request));
        assertEquals(Charset.forName("ISO-8859-1"), Charset.forName(response
                .getCharacterEncoding()));
        assertEquals("Accept-Charset", response.getHeader("Vary"));
    }

    public void testBogusValue() {
        // bogus value
        request.addHeader("Accept-Charset", "foo");
        try {
            csn.negotiate(request, response);
            fail("NoAcceptableCharacterSetException expected.");
        } catch (NoAcceptableCharacterSetException ex) {
        }

        assertNull(csn.negotiatedValue(request));
        assertEquals(new MockHttpServletResponse().getCharacterEncoding(),
                response.getCharacterEncoding());
        assertEquals("Accept-Charset", response.getHeader("Vary"));
    }
}
