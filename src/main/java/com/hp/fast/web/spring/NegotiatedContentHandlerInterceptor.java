/* $Id: NegotiatedContentHandlerInterceptor.java,v 1.1 2007/04/03 10:05:16 marcd Exp $ */

package com.hp.fast.web.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.hp.fast.web.http.ContentNegotiator;
import com.hp.fast.web.http.NotAcceptableException;

/**
 * Adjusts the response object to match negotiated content settings. Commonly
 * used in conjuction with a
 * {@link com.hp.fast.web.spring.ContentNegotiatingHandlerMapping} in a
 * negotiated content scenario.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class NegotiatedContentHandlerInterceptor extends
        HandlerInterceptorAdapter {
    private final Collection negotiators = new ArrayList();

    /**
     * Sets the negotiators that are used to adjust the response negotiated
     * content settings.
     * 
     * @param negotiators
     *            the negotiators that adjust the response.
     */
    public void setNegotiators(Object[] negotiators) {
        this.negotiators.clear();
        this.negotiators.addAll(Arrays.asList(negotiators));
    }

    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws NotAcceptableException {
        Iterator itr = negotiators.iterator();
        while (itr.hasNext()) {
            ContentNegotiator negotiator = (ContentNegotiator) itr.next();
            negotiator.negotiate(request, response);
        }

        // inform the Spring locale resolver of locale changes
        LocaleResolver localeResolver = RequestContextUtils
                .getLocaleResolver(request);
        if (localeResolver == null) {
            throw new IllegalStateException(
                    "No LocaleResolver found: not in a DispatcherServlet request?");
        }
        localeResolver.setLocale(request, response, response.getLocale());

        return true;
    }
}
