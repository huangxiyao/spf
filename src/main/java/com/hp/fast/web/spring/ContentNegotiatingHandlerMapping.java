/* $Id: ContentNegotiatingHandlerMapping.java,v 1.1 2007/04/03 10:05:16 marcd Exp $ */

package com.hp.fast.web.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import com.hp.fast.web.http.ContentNegotiator;

/**
 * A handler mapping that in addition to mapping based solely on URL, also maps
 * based on specified
 * {@link com.hp.fast.web.http.ContentNegotiator ContentNegotiators}.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class ContentNegotiatingHandlerMapping extends SimpleUrlHandlerMapping {
    private final Collection negotiators = new ArrayList();

    /**
     * Sets the negotiators that are used to select a mapping. All negotiators
     * must return true from their
     * {@link com.hp.fast.web.http.ContentNegotiator#acceptable(HttpServletRequest)}
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
        Object handler = super.lookupHandler(urlPath, request);

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
