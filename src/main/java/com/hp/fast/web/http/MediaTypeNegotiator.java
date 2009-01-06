/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.fast.web.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Negotiates media types based on the HTTP <code>Accept</code> header.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class MediaTypeNegotiator implements ContentNegotiator {
    private static final String HEADER = "Accept";

    private static final HeaderParser HEADER_PARSER = new HeaderParser(HEADER);

    private static final String ACCEPTABLE = MediaTypeNegotiator.class
            .getName()
            + ".ACCEPTABLE";

    private static final String NEGOTIATED_VALUE = MediaTypeNegotiator.class
            .getName()
            + ".NEGOTIATED_VALUE";

    private final List acceptabledMediaTypes;

    /**
     * Creates a media type negotiator.
     * 
     * @param acceptabledMediaTypes
     *            the acceptable media types.
     */
    public MediaTypeNegotiator(Object[] acceptabledMediaTypes) {
        this.acceptabledMediaTypes = new ArrayList(Arrays
                .asList(acceptabledMediaTypes));
    }

    public boolean acceptable(HttpServletRequest request) {
        Boolean acceptable = (Boolean) request.getAttribute(ACCEPTABLE);

        if (acceptable == null) {
            String negotiatedMediaType = null;

            if (request.getHeader(HEADER) == null
                    && acceptabledMediaTypes.size() > 0) {
                negotiatedMediaType = (String) acceptabledMediaTypes.get(0);
            } else {
                List acceptedMediaTypes = HEADER_PARSER.values(request);
                for (Iterator m = acceptedMediaTypes.iterator(); m.hasNext()
                        && negotiatedMediaType == null;) {
                    String acceptedMediaType = (String) m.next();
                    if (acceptabledMediaTypes.contains(acceptedMediaType)) {
                        negotiatedMediaType = acceptedMediaType;
                    }
                }
            }

            acceptable = Boolean.valueOf(negotiatedMediaType != null);
            request.setAttribute(ACCEPTABLE, acceptable);

            if (acceptable.booleanValue()) {
                request.setAttribute(NEGOTIATED_VALUE, negotiatedMediaType);
            }
        }

        return acceptable.booleanValue();
    }

    public Object negotiatedValue(HttpServletRequest request) {
        return acceptable(request) ? (String) request
                .getAttribute(NEGOTIATED_VALUE) : null;
    }

    public void negotiate(HttpServletRequest request,
            HttpServletResponse response) throws NoAcceptableMediaTypeException {
        String negotiatedMediaType = null;

        response.addHeader("Vary", HEADER);

        if (acceptable(request)) {
            negotiatedMediaType = (String) request
                    .getAttribute(NEGOTIATED_VALUE);
            response.setContentType(negotiatedMediaType);
        } else {
            throw new NoAcceptableMediaTypeException(
                    "No acceptable media type could be negotiated.",
                    acceptabledMediaTypes);
        }
    }

}
