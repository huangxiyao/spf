/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import com.hp.it.spf.localeresolver.http.ContentNegotiator;

/**
 * A handler mapping that in addition to mapping based solely on URL, also maps
 * based on specified
 * {@link com.hp.it.spf.localeresolver.http.ContentNegotiator ContentNegotiators}.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class ContentNegotiatingHandlerMapping extends SimpleUrlHandlerMapping {
    private final Collection negotiators = new ArrayList();

    /**
     * Sets the negotiators that are used to select a mapping. All negotiators
     * must return true from their
     * {@link com.hp.it.spf.localeresolver.http.ContentNegotiator#acceptable(HttpServletRequest)}
     * methods in order for a mapping to apply.
     * 
     * @param negotiators
     *            the negotiators to query for content acceptability.
     */
    public void setNegotiators(Object[] negotiators) {
        this.negotiators.clear();
        this.negotiators.addAll(Arrays.asList(negotiators));
    }

    protected Object lookupHandler(String urlPath, HttpServletRequest request) {
        Object handler;
        try { // added try-catch when porting to Spring 2.5.6
        	handler = super.lookupHandler(urlPath, request);
        } catch (Exception e) {
        	handler = null;
        }

        // if the URL matches, check the negotiators for acceptability
        if (!(handler == null || negotiators.isEmpty())) {
            for (Iterator n = negotiators.iterator(); n.hasNext()
                    && handler != null;) {
                if (!((ContentNegotiator) n.next()).acceptable(request)) {
                    handler = null;
                }
            }
        }

        return handler;
    }

}
