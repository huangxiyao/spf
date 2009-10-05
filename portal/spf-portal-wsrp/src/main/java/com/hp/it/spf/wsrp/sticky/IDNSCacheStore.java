package com.hp.it.spf.wsrp.sticky;

import java.util.Map;

import org.apache.axis.MessageContext;

/**
 * Interface providing access to DNS cache map.
 * Implementations of this interface should be able to find the information required to locate 
 * the cache in the message context present in the methods signature.
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public interface IDNSCacheStore {
	
	/**
	 * Retrieves the DNS cache map based on the content of the message context.
	 * Note that the changes to the returned map are not automatically persisted. Instead 
	 * {@link #saveCache(MessageContext, Map)} method must be explicitly called.
	 * 
	 * @param messageContext web service call message context
	 * @return DNS map cache associated with this web service call. If no cache is present
	 * this method should return an empty map.
	 */
	Map<String, String> getCache(MessageContext messageContext);

	/**
	 * Saves the DNS cache map. 
	 * @param messageContext web service call message context
	 * @param dnsCache DNS map cache to save
	 */
	void saveCache(MessageContext messageContext, Map<String, String> dnsCache);
}
