package com.hp.it.spf.wsrp.sticky;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.axis.MessageContext;

import com.hp.it.spf.wsrp.misc.Utils;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

/**
 * {@link IDNSCacheStore} implementation persisting the DNS cache map in the user session.
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class HttpSessionDNSCacheStore implements IDNSCacheStore {

	private static final LogWrapper LOG = new LogWrapper(HttpSessionDNSCacheStore.class);

	/**
	 * Cache key used to which the cache map is bound in user session.
	 */
	private static final String DNS_CACHE_KEY = HttpSessionDNSCacheStore.class.getName() + ".DNSCache";

	/**
	 * Key under which the session will be kept in the message context.
	 */
	private static final String SESSION_MC_KEY = HttpSessionDNSCacheStore.class.getName() + ".HttpSession";

	/**
	 * Retrieves the cache from user session.
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getCache(MessageContext messageContext) {
		HttpSession session = getSession(messageContext);
		Map<String, String> dnsCache = (Map<String, String>) session.getAttribute(DNS_CACHE_KEY);

		if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
			LOG.debug("DNS cache found in session: " + dnsCache);
		}
		if (dnsCache == null) {
			dnsCache = new HashMap<String, String>();
		}

		return dnsCache;
	}

	/**
	 * Saves the cache into user session.
	 */
	public void saveCache(MessageContext messageContext, Map<String, String> dnsCache) {
		if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
			LOG.debug("Saving DNS cache in session: " + dnsCache);
		}
		getSession(messageContext).setAttribute(DNS_CACHE_KEY, dnsCache);
	}


	/**
	 * Retrieves the user session based on the content of messageContext.
	 * This method relies on {@link Utils#retrieveSession(org.apache.axis.MessageContext)} to get access
	 * to the portal session.
	 * 
	 * @param messageContext web service call message context. 
	 * @return user session
	 * @throws IllegalStateException If an error occurs when retrieving user request associated 
	 * with the user session
	 */
	private HttpSession getSession(MessageContext messageContext) {
		try {
			return Utils.retrieveSession(messageContext);
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Unable to retrieve session or request object. " +
					"RequestBindingFilter is probably not setup properly or this method is called " +
					"in the context where request is not available.", e);
		}
	}

}
