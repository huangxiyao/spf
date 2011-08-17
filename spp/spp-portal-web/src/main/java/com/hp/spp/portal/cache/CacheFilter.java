package com.hp.spp.portal.cache;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.hp.spp.common.util.DiagnosticContext;
import com.hp.spp.config.Config;
import com.hp.spp.portal.common.site.SiteManager;
import com.hp.spp.profile.Constants;
import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.web.ServletCacheAdministrator;
import com.opensymphony.oscache.web.filter.CacheHttpServletResponseWrapper;


/**
 * OSCache-based cache filter caching public pages based on uri, query string, country and language.
 * Note that the overall workflow of this filter is implemented in the parent class. This class
 * overrides only some of the methods that are used in this workflow to tail the filter behavior
 * to our needs.
 */
public class CacheFilter extends com.opensymphony.oscache.web.filter.CacheFilter {

	public static final String CACHE_EXPIRY_CONFIG_KEY_PREFIX = "SPP.CacheFilter.PublicPagesCache.ExpiryPeriodInMins.";
	public static final String CACHE_CAPACITY_CONFIG_KEY = "SPP.CacheFilter.CacheCapacity";

	/**
	 * Path to OSCache config file for page cache. Note that this file is different from the one
	 * used by spp-cache module as it configures 2 different caches.
	 */
	private static final String PAGE_CACHE_CONFIG_RESOURCE_PATH = "/pagecache_oscache.properties";

	private static final Logger mLog = Logger.getLogger(CacheFilter.class);

	private static String PUBLIC_SITE_URI_PREFIX;

	private FilterConfig mConfig;
	
	public void init(FilterConfig filterConfig) {
		mConfig = filterConfig;
		
		// Initialize ServletCacheAdministrator - this must be done prior to calling super.init
		// which will also try to initialized if not done yet.
		ServletCacheAdministrator.getInstance(mConfig.getServletContext(), readCacheConfig());
		
		super.init(filterConfig);
	}

	private Properties readCacheConfig() {
		InputStream is = getClass().getResourceAsStream(PAGE_CACHE_CONFIG_RESOURCE_PATH);
		if (is == null) {
			throw new IllegalStateException("Unable to find in the classpath the cache filter configuration: " + PAGE_CACHE_CONFIG_RESOURCE_PATH);
		}
		Properties cacheConfiguration = new Properties();
		try {
			try {
				cacheConfiguration.load(is);
				int cacheCapacityOverwrite = Config.getIntValue(CACHE_CAPACITY_CONFIG_KEY, -1);
				if (cacheCapacityOverwrite != -1) {
					if (mLog.isDebugEnabled()) {
						mLog.debug("Getting CacheFilter cache.capacity value for configuration table: " + cacheCapacityOverwrite);
					}
					cacheConfiguration.setProperty("cache.capacity", String.valueOf(cacheCapacityOverwrite));
				}
				if (mLog.isDebugEnabled()) {
					mLog.debug("CacheFilter configuration loaded: " + cacheConfiguration);
				}
				return cacheConfiguration;
			}
			finally {
				is.close();
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Error loading filter cache configuration", e);
		}
	}

	/**
	 * Determines whether the given <tt>servletRequest</tt> is cacheable. If this is not the case
	 * the filter will not perform any further action.
	 */
	protected boolean isCacheable(ServletRequest servletRequest) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;

		return
				super.isCacheable(servletRequest) &&
						isGetMethod(request) &&
						isProfileInSession(request) &&
						isPublicPage(request) &&
						isCacheEnabledForSite(request);
	}

	/**
	 * Returns <tt>true</tt> if the cache is enabled for the site requested in given <tt>request</tt>.
	 * The cache is considered enabled if <tt>SPP.CacheFilter.PublicPagesCache.ExpiryPeriodInMins.[site name]</tt>
	 * is defined and its value is greater than 0. To disable the cache it's enough to update the
	 * variable value to 0.
	 */
	private boolean isCacheEnabledForSite(HttpServletRequest request) {
		Map userProfile = (Map) request.getSession().getAttribute(Constants.PROFILE_MAP);
		String siteName = (String) userProfile.get(Constants.MAP_SITE);
        int cacheEntryExpiryPeriod = SiteManager.getInstance().getSite(siteName).getCacheFileterPublicPagesCacheExpiryPeriodInMins();
        return cacheEntryExpiryPeriod > 0;
	}

	private boolean isProfileInSession(HttpServletRequest request) {
		return request.getSession(false) != null &&
				request.getSession().getAttribute(Constants.PROFILE_MAP) != null;
	}

	private boolean isGetMethod(HttpServletRequest httpRequest) {
		return "GET".equals(httpRequest.getMethod());
	}

	/**
	 * Create cache entry key for this request markup.
	 * @see #createCacheKey(javax.servlet.http.HttpServletRequest)
	 */
	public String createCacheKey(HttpServletRequest httpRequest, ServletCacheAdministrator scAdmin, Cache cache) {
		return createCacheKey(httpRequest);
	}

	/**
	 * Create cache groups for the public pages. The groups are <tt>allpublicpages</tt> and
	 * <tt>public[site name]</tt>. If the request is for other than public pages this method returns
	 * null.
	 */
	public String[] createCacheGroups(HttpServletRequest request, ServletCacheAdministrator scAdmin, Cache cache) {
		if (isPublicPage(request)) {
			Map userProfile = (Map) request.getSession().getAttribute(Constants.PROFILE_MAP);
			String groupName = new StringBuilder("public").append(userProfile.get(Constants.MAP_SITE)).toString();
			return new String[] {"allpublicpages", groupName};
		}
		return null;
	}

	/**
	 * Create a new cache key based on request's protocol, URI and query string and on the information
	 * available in session-stored profile such as site, country and language for this request.
	 */
	private String createCacheKey(HttpServletRequest request) {
		Map userProfile = (Map) request.getSession().getAttribute(Constants.PROFILE_MAP);
		StringBuilder sb = new StringBuilder();
		sb.append("site:").append(userProfile.get(Constants.MAP_SITE));
		sb.append(",lang:").append(userProfile.get(Constants.MAP_LANGUAGE));
		sb.append(",cc:").append(userProfile.get(Constants.MAP_COUNTRY));

		//FIXME (slawek)
		// Adding scheme to ensure that the redirections done by SecurityFilter are properly handled.
		// The clean solution should be to place SecurityFilter before CacheFilter however we are now
		// pretty late in the testing and this change could present side effects. Thefefore to minimize
		// the risk we will add the protocol to the cache entry key. Normally the cache should always
		// contain entry for a given page only for one protocol.
		sb.append(',').append(request.getScheme());

		sb.append(",uri:").append(request.getRequestURI());
		sb.append(",qs:").append(request.getQueryString());

		return sb.toString();
	}

	private boolean isPublicPage(HttpServletRequest request) {
		return request.getRequestURI().startsWith(getPublicSiteUriPrefix(request));
	}

	private static synchronized String getPublicSiteUriPrefix(HttpServletRequest request) {
		if (PUBLIC_SITE_URI_PREFIX == null) {
			PUBLIC_SITE_URI_PREFIX = new StringBuilder(request.getContextPath()).append("/site/public").toString();
		}
		return PUBLIC_SITE_URI_PREFIX;
	}
	
	
	/** 
	 * 
	 * Determines whether the <tt>ServerletResponse<tt> is cacheable or not
	 * In case the Response is not cacheable then the filter wouldn't cache the content.
	 * 
	 */
	@Override
	protected boolean isCacheable(CacheHttpServletResponseWrapper cacheResponse) {
		return super.isCacheable(cacheResponse) && isValidResponse();
	}
	
	/** 
	 * 
	 * Determines whether the <tt>ServerletResponse<tt> is a valid response or not.
	 * The diagnostic message is set in "OnRenderFailureSPPTag" .. 
	 * This tag overrides the vignette implementation by using customised tag.
	 *  
	 * Returns <tt>true</tt>  - If portlet error diagnostic message is not present.
	 * Returns <tt>false</tt> - If diagnostic error message "PortletRenderFailureSPP" is present
	 * 
	 */
	
	private boolean isValidResponse() {
		
		if(!DiagnosticContext.getThreadInstance().isEmpty() ){
			if(DiagnosticContext.getThreadInstance().getErrorMessage().contains("PortletRenderFailureSPP")){
				if (mLog.isDebugEnabled()) {
					mLog.debug("Portlet Response is errornous , The response would not be cached");
				}				
				return false;
			}
		}
		
		return true;		
	}	
}