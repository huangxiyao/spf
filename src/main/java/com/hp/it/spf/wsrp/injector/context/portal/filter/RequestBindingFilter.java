package com.hp.it.spf.wsrp.injector.context.portal.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.wsrp.injector.context.RequestMap;
import com.hp.it.spf.wsrp.injector.context.RequestWrapper;
import com.vignette.portal.log.LogWrapper;

/**
 * Filter used to add the incoming request to {@link RequestMap} so it's
 * available to {@link UserContextKeysInjector} during WSRP requests.
 */
public class RequestBindingFilter implements Filter {
    private static final LogWrapper LOG = new LogWrapper(
            RequestBindingFilter.class);

    /**
     * Prefix of the request key that will be used in <code>user-agent</code>
     * value.
     */
    public static final String KEY_PREFIX = " __SPP";

    public static final String THREAD_NAME_REQUEST_KEY = RequestBindingFilter.class
            .getName()
            + ".ThreadName";

    /**
     * @param config
     *            FilterConfig
     * @throws ServletException
     *             javax.servlet.ServletException
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException {
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
    }

    /**
     * Adds the incoming request to {@link RequestMap} and creates a
     * {@link RequestWrapper} if this request if this request has not been
     * wrapped yet.
     * 
     * @param servletRequest
     *            incoming request
     * @param servletResponse
     *            incoming response
     * @param filterChain
     *            fitler chain
     * @throws IOException
     *             If an exeption occurs duing the fitler chain processing; this
     *             class doesn't throw any IOException
     * @throws ServletException
     *             If an exception occurs during the filter chain processing;
     *             this class doesn't throw any ServletException
     */
    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        LOG.info("RequestBindingFilter start");
        HttpServletRequest request = (HttpServletRequest)servletRequest;

        String userAgentValue = request.getHeader("user-agent");
        // only create a wrapper if the request is not wrapped yet
        if (userAgentValue == null || userAgentValue.indexOf(KEY_PREFIX) == -1) {
            String requestKey = RequestMap.getInstance().add(request);

            // put in the request the name of the thread so we can associate
            // the
            // WebLogic request
            // thread with the thread running WSRP call
            request.setAttribute(THREAD_NAME_REQUEST_KEY, Thread
                    .currentThread().getName());

            try {
                RequestWrapper wrapper = new RequestWrapper(request, KEY_PREFIX
                        + requestKey);
                filterChain.doFilter(wrapper, servletResponse);
            } finally {
                RequestMap.getInstance().remove(requestKey);
            }
        } else {
            filterChain.doFilter(request, servletResponse);
        }
    }
}
