/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A content negotiator examines attributes of the incoming request to determine
 * whether the application can provide content in the requested form.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public interface ContentNegotiator {
    /**
     * Returns an indication whether the application can provide content in the
     * requested form.
     * 
     * @param request
     *            the servlet request.
     * @return true if the content can be provided in an acceptable form, false
     *         otherwise.
     */
    boolean acceptable(HttpServletRequest request);

    /**
     * Returns the best value for which the application can meet the request
     * specified requirements.
     * 
     * @param request
     *            the servlet request.
     * @return the best negotiated value, or null if no value could be
     *         negotiated.
     */
    Object negotiatedValue(HttpServletRequest request);

    /**
     * Performs whatever steps are necessary to adjust the response to the
     * negotiated value.
     * 
     * @param request
     *            the servlet request.
     * @param response
     *            the servlet response.
     * @throws NotAcceptableException
     */
    void negotiate(HttpServletRequest request, HttpServletResponse response)
            throws NotAcceptableException;
}
