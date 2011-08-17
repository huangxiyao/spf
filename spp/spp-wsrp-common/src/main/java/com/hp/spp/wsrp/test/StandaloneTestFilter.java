package com.hp.spp.wsrp.test;

import com.hp.spp.wsrp.context.UserContextExtractor;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class StandaloneTestFilter implements Filter {
	private static final String FILTER_APPLIED = StandaloneTestFilter.class.getName() + ".Applied";
	private static final Logger mLog = Logger.getLogger(StandaloneTestFilter.class);
	private ServletContext mServletContext;
	private boolean mRequiresSession = false;

	public void init(FilterConfig filterConfig) throws ServletException {
		mServletContext = filterConfig.getServletContext();
		mRequiresSession = checkIfRequiresSession(filterConfig.getServletContext());
	}

	private boolean checkIfRequiresSession(ServletContext servletContext) throws ServletException {
		final String resourcePath = "/WEB-INF/persistence/descriptions/org.apache.wsrp4j.commons.persistence.driver.WSRPServiceDescription.xml";
		InputStream is = servletContext.getResourceAsStream(resourcePath);
		if (is == null) {
			mLog.warn("Unable to find resource '" + resourcePath + "'. Filter will not create session!");
			return false;
		}
		try {
			boolean requiresSession = false;
			try {
				Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
				final String requiresInitCookie = doc.getDocumentElement().getAttribute("requiresInitCookie");
				requiresSession = !"none".equals(requiresInitCookie);
				if (mLog.isInfoEnabled()) {
					if (requiresSession) {
						mLog.info("Filter will ensure session is created.");
					}
					else {
						mLog.info("Filter will not create session.");
					}
				}
			}
			finally {
				is.close();
			}
			return requiresSession;
		}
		catch (Exception e) {
			throw new ServletException("Error occured while reading information from '" + resourcePath + "'", e);
		}
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		if (servletRequest.getAttribute(FILTER_APPLIED) == null) {
			if (mRequiresSession) {
				((HttpServletRequest) servletRequest).getSession(true);
			}

			Map profile = readTestUserProfile();
			if (profile != null) {
				servletRequest.setAttribute(UserContextExtractor.USER_PROFILE_KEY, profile);
			}
			Map contextKeys = readTestUserContextKeys();
			if (contextKeys != null) {
				servletRequest.setAttribute(UserContextExtractor.USER_CONTEXT_KEYS_KEY, contextKeys);
			}

			servletRequest.setAttribute(FILTER_APPLIED, Boolean.TRUE);
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}

	private Map readTestUserContextKeys() throws IOException {
		return readProperties("/WEB-INF/data/test_UserContextKeys.properties");
	}

	private Map readTestUserProfile() throws IOException {
		return readProperties("/WEB-INF/data/test_UserProfile.properties");
	}

	private Map readProperties(String resourcePath) throws IOException {
		InputStream is = mServletContext.getResourceAsStream(resourcePath);
		if (is == null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Unable to read properties from resource: " + resourcePath);
			}
			return null;
		}
		try {
			Properties props = new Properties();
			props.load(is);
			if (mLog.isDebugEnabled()) {
				mLog.debug("Read properties from resource '" + resourcePath + "': " + props);
			}
			return new HashMap(props);
		}
		finally {
			is.close();
		}
	}

	public void destroy() {
	}
}
