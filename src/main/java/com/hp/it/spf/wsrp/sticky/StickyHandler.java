package com.hp.it.spf.wsrp.sticky;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;

import javax.xml.soap.SOAPException;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;

import com.hp.it.spf.wsrp.misc.Predicates;
import com.hp.it.spf.wsrp.misc.Utils;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

/**
 * Handler providing a stickiness, for the time of the portal user session, between the portal and
 * the (virtual) IP returned for the portal application host name.
 * 
 * @see #invoke(MessageContext) for implementation details
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
@SuppressWarnings("serial")
public class StickyHandler extends BasicHandler {

	private static final LogWrapper LOG = new LogWrapper(StickyHandler.class);
	
	/**
	 * Message context property key associated with the requested WSRP URL. This URL is saved
	 * in this property and it gets replaced with the one containing IP address instead of 
	 * host name.
	 */
	private static final String ORIGINAL_TRANS_URL = MessageContext.TRANS_URL + "_ORIGINAL";
	
	/**
	 * Temporary storage for the host name and the resolved IP used between the time initCookie
	 * and the following markup call is made. Note that this variable is static as a new handler
	 * instance is called for each request but this data must survive between the requests.
	 * The key structure ensures that no conflict will occur for different users, sessions and 
	 * portlet servers. 
	 */
	private static CookieHolder mCookieHolder = new CookieHolder();

	/**
	 * Access object to get DNS cache map.
	 */
	private IDNSCacheStore mStore;

	public StickyHandler()
	{
		this(new HttpSessionDNSCacheStore());
	}

	StickyHandler(IDNSCacheStore store)
	{
		mStore = store;
	}

	/**
	 * Swaps the host name with the resolved and cached IP in the URL used by Axis to call the
	 * web service.
	 * <p>
	 * This method caches the DNS resolution result in a map which is in turn stored in user session.
	 * In WSRP the session, for the stateful portlets, is handled as follows. First portal calls
	 * initCookie method which should return a session cookie that the portal should use in the
	 * subsequent calls to this portlet for that session. Then for each markup call this cookie
	 * is sent to the producer allowing its container to route the request to the appropriate session.
	 * <p>
	 * The DNS cache is stored in the user session. Unfortunately, when initCookie is called
	 * we don't have any handle to the user session. Assuming that initCookie will be followed
	 * by a markup call we store the resolved host name and its IP in {@link CookieHolder}.
	 * The key to store and retrieve data from the cookie holder is the cookie value and requested
	 * host name. Cookie value is sent in the response to the initCookie and that's when it gets
	 * stored in the cookie holder along with the IP (result of DNS lookup for the portlet host name)
	 * used to perform the request.
	 * <p>
	 * In the subsequent markup call the IP is retrieved from cookie holder and is stored in the
	 * session-scoped cache ({@link IDNSCacheStore}). The appropriate entry is also removed from 
	 * the cookie holder. From now on, all the request for this user session will use the resolved
	 * IP as stored in this DNS cache.
	 * <p>
	 * For some faults Axis instead of calling {@link #onFault(MessageContext)} method will call 
	 * this method. Therefore we also check if this is a response containing the fault and perform
	 * the appropriate cleanup as defined in onFault method. 
	 */
	public void invoke(MessageContext messageContext) throws AxisFault {

		if (!Predicates.isWsrp(messageContext)) {
			return;
		}
		
		try {
			if (isInitCookie(messageContext)) {
				if (isRequest(messageContext)) {
					if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
						LOG.debug("Handling initCookie request");
					}
					
					// In initCookie request we don't have any cookie or DNS cache entry
					// so let's lookup the IP.
					
					String targetHost = getTargetHost(messageContext);
					String targetVIP = lookup(targetHost);
					swapTargetHost(messageContext, targetHost, targetVIP);
				} else {
					if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
						LOG.debug("Handling initCookie response");
					}
					
					// In initCookie response we get back the cookie that will be used for
					// subsequent calls from portal to portlet. As we have no access to the
					// session-scoped DNS cache let's temporarily save it along with IP
					// we used.
					
					String originalTargetHost = getOriginalHost(messageContext);
					String targetVIP = getTargetHost(messageContext);
					mCookieHolder.saveCookie(messageContext, originalTargetHost, targetVIP);
				}
			} else if (isWsrpBaseCall(messageContext)) {
				if (isRequest(messageContext)) {
					if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
						LOG.debug("Handling WSRP base request");
					}
					
					Map<String, String> dnsCache = mStore.getCache(messageContext);
					String targetHost = getTargetHost(messageContext);
					String targetVIP = dnsCache.get(targetHost);
					
					// Let's see if we already cached this IP.

					if (targetVIP == null) {
						// It was not cached yet. It may be the first request after initCookie.
						// We should normally have saved that in the cookie holder when handling
						// initCookie response.
						
						targetVIP = mCookieHolder.getVipForCookie(messageContext, targetHost);
						if (targetVIP == null) {
							// This happens if we connected successfuly to a portlet before and
							// now the portlet may be down. VAP will not reinitialize the session
							// in such a case but because of failure we already cleaned up the DNS cache
							// and cookie. Let's do the best we can and simply lookup the IP again.
							if (LOG.willLogAtLevel(LogConfiguration.WARNING)) {
								LOG.warning("Target host not found either for cookie or in DNS cache: " + 
										targetHost + ". Probably recovering from failure.");
							}
							
							targetVIP = lookup(targetHost);
						}
						
						// IP was not in the cache so let's save it.
						dnsCache.put(targetHost, targetVIP);
						mStore.saveCache(messageContext, dnsCache);
						
						// Let's also do the cleanup to make sure we don't accumulate the data in
						// the cookie holder.
						mCookieHolder.removeCookie(messageContext, targetHost);
					}
					swapTargetHost(messageContext, targetHost, targetVIP);
				}
			}

			// For some faults Axis does not call onFault method. Let's do it here to make sure
			// the DNS cache and cookies are cleared if needed.
			if (isSOAPFaultResponse(messageContext)) {
				onFault(messageContext);
			}
		} catch (UnknownHostException e) {
			// Do not rethrow this exception as if we cannot find the host, Axis won't be able
			// to do it either and it will throw this as an exception anyway.
			LOG.error(e);
		} catch (SOAPException e) {
			// Do not rethrow this exception it is probably already a SOAP fault which Axis
			// will report anyway.
			LOG.error(e);
		}
	}


	/**
	 * If a fault occurs we remove the cookie stored in the holder which corresponds to this session
	 * as well as the host entry that we have in the cache.
	 * <p>
	 * This method performs the cleanup for each fault. 
	 */
	@Override
	public void onFault(MessageContext messageContext) {
		if (!Predicates.isWsrp(messageContext)) {
			return;
		}

		if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
			LOG.debug("Handling fault");
		}
		
		// Fault most likely happens in the response. This means that we already
		// replace the URL having the host with the one having IP. However we also saved the
		// original one so let's get the host for there.
		String originalHost = getOriginalHost(messageContext);
		if (originalHost == null) {
			// Looks like we didn't get to the point when we swapped the URL. Let's try to get
			// the host from the default location.
			originalHost = getTargetHost(messageContext);
		}
		// FIXME (slawek) - Should we clean the cache entry on each fault or only on some 
		// specific faults???

		mCookieHolder.removeCookie(messageContext, originalHost);
		
		// Only WSRP base calls allow access to the session
		if (isWsrpBaseCall(messageContext)) {
			Map<String, String> dnsCache = mStore.getCache(messageContext);
			if (dnsCache != null) {
				if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
					LOG.debug("Removing host '" + originalHost + "' from session DNS cache: " + dnsCache);
				}
				dnsCache.remove(originalHost);
				mStore.saveCache(messageContext, dnsCache);
			}
		}
	}


	private boolean isInitCookie(MessageContext messageContext) {
		return Predicates.isAnyOfMethods(messageContext, "initCookie");
	}
	
	private boolean isWsrpBaseCall(MessageContext messageContext) {
		return Predicates.isWsrpBaseCall(messageContext);
	}

	private boolean isRequest(MessageContext messageContext) {
		return !messageContext.getPastPivot();
	}


	/*private*/ boolean isSOAPFaultResponse(MessageContext messageContext) throws SOAPException {
		return messageContext.getPastPivot() && 
			Utils.isSOAPFault(messageContext.getResponseMessage());
	}

	/**
	 * Replaces in the message context the web service target URL containing the targetHost 
	 * with the one containing the corresponding IP address.
	 *  
	 * @param messageContext web service message context containing the WSRP request URL
	 * @param targetHost host present in the URL
	 * @param targetVIP IP corresponding to the host
	 */
	private void swapTargetHost(MessageContext messageContext, String targetHost, String targetVIP) {
		String transportUrl = messageContext.getStrProp(MessageContext.TRANS_URL);
		String newTransUrl = transportUrl.replace(targetHost, targetVIP);
		messageContext.setProperty(ORIGINAL_TRANS_URL, transportUrl);
		messageContext.setProperty(MessageContext.TRANS_URL, newTransUrl);
		if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
			LOG.debug("Host replaced in URL '" + transportUrl + "': " + newTransUrl);
		}
	}


	/**
	 * Performs DNS lookup using {@link InetAddress} class.
	 * @param targetHost host for which we want to find IP
	 * @return IP corresponding to the host
	 * @throws UnknownHostException If the host could not be found
	 */
	/*private*/ String lookup(String targetHost) throws UnknownHostException {
		return InetAddress.getByName(targetHost).getHostAddress();
	}


	private String getHost(String transportUrl) {
		URL url;
		try {
			url = new URL(transportUrl);
		} catch (MalformedURLException e) {
			throw new IllegalStateException("Unable to parse URL set by Axis: " + transportUrl, e);
		}
		return url.getHost();
	}

	
	private String getTargetHost(MessageContext messageContext) {
		return getHost(messageContext.getStrProp(MessageContext.TRANS_URL));
	}

	
	private String getOriginalHost(MessageContext messageContext) {
		String originalTransportUrl = messageContext.getStrProp(ORIGINAL_TRANS_URL);
		if (originalTransportUrl == null) {
			return null;
		}
		return getHost(originalTransportUrl);
	}

	static void setCookieHolder(CookieHolder cookieHolder)
	{
		mCookieHolder = cookieHolder;
	}
}
