package com.hp.it.spf.htmlviewer.portlet.util;

import java.util.Map;
import java.util.Set;
import java.util.Locale;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import javax.portlet.PortletRequest;

import com.hp.it.spf.xa.misc.portlet.Utils;
import com.hp.it.spf.xa.i18n.portlet.I18nUtility;
import com.hp.it.spf.htmlviewer.portlet.util.ViewData;

/**
 * <p>
 * The <code>ViewDataCache</code> implements a memory cache for
 * {@link com.hp.it.spf.htmlviewer.portlet.util.ViewData} objects. View data may
 * be different for each portlet window (ie portlet instance) and user locale;
 * but for any given portlet instance and locale, it is always the same. Thus
 * this cache maps portlet instance and locale to <code>ViewData</code> and
 * maintains that cache over time (expiring and refreshing <code>ViewData</code>
 * from portlet preferences as needed).
 * </p>
 * <p>
 * The get method obtains view data from the cache for the current portlet
 * instance and locale, or if the cache does not have the data yet, it returns
 * it from the portlet preferences and backfills into the cache. The rest method
 * clears view data from the cache for the current portlet instance and locale.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @since SPF 1.2
 */
public class ViewDataCache {

	// in-memory cache of ViewData objects -- use ConcurrentHashMap because it
	// is internally synchronized so none of the logic in this class needs to be
	// explicitly synchronized
	private static final Map<String, ViewData> cache = new ConcurrentHashMap<String, ViewData>();

	// text format for a cache key: portlet ID followed by locale
	private static final String CACHE_KEY_FORMAT = "%s-%s";

	/**
	 * <p>
	 * Return the {@link com.hp.it.spf.htmlviewer.portlet.util.ViewData} object
	 * for the portlet instance (ie, window) and locale indicated in the given
	 * portlet request, using the cache. If the object is already in the cache
	 * and is still valid (ie not yet expired), it is simply returned. Otherwise
	 * it is created (this fetches it from portlet preferences and sets the
	 * expiration time accordingly) and backfilled into the cache as a
	 * side-effect. Returns null if a null request is provided.
	 * </p>
	 * <p>
	 * <b>Note:</b> If there was an error or warning when loading the data from
	 * portlet preferences and filesystem, it will not be placed into the cache.
	 * Each subsequent call to this method will attempt reloading it each time,
	 * until the reload succeeds - then it will be placed into the cache.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request for the particular portlet instance (ie
	 *            window) and locale.
	 * @return The <code>ViewData</code> for the portlet instance and locale.
	 */
	public static ViewData getViewData(PortletRequest request) {
		// request must not be null
		if (request == null) {
			return null;
		}
		// get cache key and lookup data in cache
		String cacheKey = getCacheKey(request);
		ViewData data = cache.get(cacheKey);
		// return from cache if the data exists, was loaded OK, and is still
		// fresh
		if ((data != null) && (data.ok()) && (!data.expired())) {
			return data;
		}
		// if the data did not exist, had an error or warning when loaded, or is
		// stale, try loading again - backfill into cache if successful - this
		// does not need to be synchronized (if concurrent execution occurs, no
		// serious problem will result)
		data = new ViewData(request);
		if ((data != null) && (data.ok())) {
			cache.put(cacheKey, data);
		}
		return data;
	}

	/**
	 * <p>
	 * Flushes any cached {@link com.hp.it.spf.htmlviewer.portlet.util.ViewData}
	 * objects for the portlet instance (ie, window) indicated in the given
	 * request. All cached objects for all locales for that instance are
	 * dropped, not just the current locale.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request for the particular portlet instance (ie
	 *            window).
	 */
	public static void resetViewData(PortletRequest request) {
		// request must not be null
		if (request == null) {
			return;
		}
		// iterate through cache and remove all entries which are for this
		// portlet instance - this does not need to be synchronized (if
		// concurrent execution occurs, no serious problem will result)
		Set<String> cacheKeys = cache.keySet();
		Iterator<String> i = cacheKeys.iterator();
		String cacheKey;
		while (i.hasNext()) {
			cacheKey = i.next();
			if (isCacheKey(request, cacheKey)) {
				cache.remove(cacheKey);
			}
		}
		return;
	}

	/**
	 * Make the key for the cached view data for the given portlet request. This
	 * is the portlet ID for this portlet instance (either the
	 * Vignette/SPF-supplied friendly ID, or the JSR-standard window ID by
	 * default), followed by the locale. For the portlet ID, we prefer to use
	 * the friendly ID because it is more human-readable (in case the cache keys
	 * are ever printed, eg for debugging).
	 */
	private static String getCacheKey(PortletRequest request) {
		String portletID = getPortletID(request);
		String locale = I18nUtility.localeToLanguageTag(request.getLocale());
		// cache key is portlet ID then language tag
		String cacheKey = String.format(CACHE_KEY_FORMAT, portletID, locale);
		return cacheKey;
	}

	/**
	 * Returns true if the given cache key is a relevant cache key for this
	 * portlet instance, and false otherwise.
	 */
	private static boolean isCacheKey(PortletRequest request, String cacheKey) {
		String portletID = getPortletID(request);
		String cacheKeyPrefix = String.format(CACHE_KEY_FORMAT, portletID, "");
		return cacheKey.startsWith(cacheKeyPrefix);
	}

	/**
	 * Get the ID for the portlet instance targeted by the given portlet
	 * request. This is either the Vignette/SPF-provided friendly ID for the
	 * portlet, or its JSR 286-standard window ID. The friendly ID is preferred
	 * because it is human-readable (in case the portlet ID is every output, eg
	 * for debug purposes).
	 */
	private static String getPortletID(PortletRequest request) {
		// look for portlet friendly ID
		String portletID = Utils.getPortalPortletID(request);
		if (portletID != null) {
			portletID = portletID.trim();
		}
		// since portlet friendly ID is not guaranteed to exist, use window ID
		// by default.
		if ((portletID == null) || (portletID.length() == 0)) {
			portletID = request.getWindowID();
		}
		return portletID;
	}
}
