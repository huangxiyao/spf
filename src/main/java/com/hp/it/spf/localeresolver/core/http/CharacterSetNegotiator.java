/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.core.http;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Negotiates character sets based on the HTTP <code>Accept-Charset</code>
 * header.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class CharacterSetNegotiator implements ContentNegotiator {
    private static final String HEADER = "Accept-Charset";

    private static final HeaderParser HEADER_PARSER = new HeaderParser(HEADER);

    private static final String ACCEPTABLE = CharacterSetNegotiator.class
            .getName()
            + ".ACCEPTABLE";

    private static final String NEGOTIATED_VALUE = CharacterSetNegotiator.class
            .getName()
            + ".NEGOTIATED_VALUE";

    private final List acceptableCharacterSets;

    /**
     * Creates a character set negotiator.
     * 
     * @param acceptableCharacterSets
     *            the acceptable character sets.
     */
    public CharacterSetNegotiator(Object[] acceptableCharacterSets) {
        this.acceptableCharacterSets = new ArrayList(Arrays
                .asList(acceptableCharacterSets));
    }

    public boolean acceptable(HttpServletRequest request) {
        Boolean acceptable = (Boolean) request.getAttribute(ACCEPTABLE);

        if (acceptable == null) {
            Charset negotiatedCharacterSet = null;

            if (request.getHeader(HEADER) == null
                    && acceptableCharacterSets.size() > 0) {
                negotiatedCharacterSet = (Charset) acceptableCharacterSets
                        .get(0);
            } else {
                List acceptedCharacterSets = HEADER_PARSER.values(request);
                for (Iterator c = acceptedCharacterSets.iterator(); c.hasNext()
                        && negotiatedCharacterSet == null;) {
                    try {
                        Charset acceptedCharacterSet = Charset
                                .forName((String) c.next());
                        if (acceptableCharacterSets
                                .contains(acceptedCharacterSet)) {
                            negotiatedCharacterSet = acceptedCharacterSet;
                        }
                    } catch (IllegalArgumentException ex) {
                        // unsupported character set
                    }
                }
            }

            acceptable = Boolean.valueOf(negotiatedCharacterSet != null);
            request.setAttribute(ACCEPTABLE, acceptable);

            if (acceptable.booleanValue()) {
                request.setAttribute(NEGOTIATED_VALUE, negotiatedCharacterSet);
            }
        }

        return acceptable.booleanValue();
    }

    public Object negotiatedValue(HttpServletRequest request) {
        return acceptable(request) ? (Charset) request
                .getAttribute(NEGOTIATED_VALUE) : null;
    }

    // msd: revert to sdk 1.3
    public void negotiate(HttpServletRequest request,
            HttpServletResponse response)
            throws NoAcceptableCharacterSetException {
        Charset negotiatedCharacterSet = null;

        response.addHeader("Vary", HEADER);

        if (acceptable(request)) {
            negotiatedCharacterSet = (Charset) request
                    .getAttribute(NEGOTIATED_VALUE);
            String charset = negotiatedCharacterSet.name();
            response.setContentType("text/html; charset=" + charset);
        } else {
            throw new NoAcceptableCharacterSetException(
                    "No acceptable character set could be negotiated.",
                    acceptableCharacterSets);
        }
    }

}
