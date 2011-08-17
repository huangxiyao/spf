package com.hp.spp.filters.gzip;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GZIPFilter implements Filter {
	private static final String GZIPFILTER_PROCESSED = "com.hp.spp.GZIPFilterProcessed";
//	private static ThreadLocal mThreadLocal = new ThreadLocal();

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request.getAttribute(GZIPFILTER_PROCESSED) == null/*mThreadLocal.get() == null*/) {
			if(request instanceof HttpServletRequest)
			{
				HttpServletRequest req = (HttpServletRequest)request;
				String contentEncoding = req.getHeader("Content-Encoding");
				if(contentEncoding != null && contentEncoding.toLowerCase().indexOf("gzip") > -1) {
					request = new GZIPRequestWrapper((HttpServletRequest)request);
//					mThreadLocal.set(Boolean.TRUE);
					request.setAttribute(GZIPFILTER_PROCESSED, Boolean.TRUE);
				}
			}
			if(response instanceof HttpServletResponse)
			{
				HttpServletRequest req = (HttpServletRequest)request;
				String acceptEncoding = req.getHeader("Accept-Encoding");
				if(acceptEncoding != null && acceptEncoding.toLowerCase().indexOf("gzip") > -1) {
					response = new GZIPResponseWrapper((HttpServletResponse)response);
//					mThreadLocal.set(Boolean.TRUE);
					request.setAttribute(GZIPFILTER_PROCESSED, Boolean.TRUE);
				}
			}
		}
		try {
			chain.doFilter(request, response);
		}
		finally {
			if(request.getAttribute(GZIPFILTER_PROCESSED) != null /*mThreadLocal.get() != null*/ && response instanceof GZIPResponseWrapper) {
				((GZIPResponseStream)response.getOutputStream()).finish();
//				mThreadLocal.set(null);
				request.removeAttribute(GZIPFILTER_PROCESSED);
			}
		}
	}

	public void destroy() {
	}
}
