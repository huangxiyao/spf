package com.hp.it.spf.xa.portletdata.portal;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.enduser.components.ActionException;
import com.vignette.portal.website.enduser.components.BaseAction;
import com.vignette.portal.portlet.management.external.PortletManager;
import com.vignette.portal.portlet.management.internal.implementation.standard.StandardPortlet;
import com.vignette.portal.portlet.management.internal.managementspi.extension.jsr.JsrPortletTypeSpi;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.log.LogConfiguration;
import com.epicentric.page.PageException;
import com.epicentric.page.Page;
import com.epicentric.page.PageLayout;
import com.epicentric.page.UserModuleSet;
import com.epicentric.page.PageManager;
import com.epicentric.uid.UIDException;
import com.epicentric.common.website.MenuItemNode;
import com.epicentric.common.website.MenuItemUtils;
import com.epicentric.mypage.internal.MyPageNode;
import com.epicentric.mypage.MyPage;
import com.hp.it.spf.xa.misc.Consts;

/**
 * Pre-display action executed for pages rendering portlets, used to share data between portal and
 * local Java portlets.
 *
 * @see #execute(com.vignette.portal.website.enduser.PortalContext)
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class PortletDataPreDisplayAction extends BaseAction {

	private static final LogWrapper LOG = new LogWrapper(PortletDataPreDisplayAction.class);

	/**
	 * Attribute name prefix for request attributes that will be available to the portlets.
	 */
	private static final String VIGNETTE_PORTLET_ATTR_PREFIX = "com.vignette.portal.attribute.portlet.";

	/**
	 * Portlet request attribute name to which the user profile map is bound. Its value is the same
	 * as <code>PortletRequest.USER_INFO</code> so portlets can retrieve the map the same way
	 * as if the map was set by the portlet container.
	 */
	private static final String USER_PROFILE_ATTR_NAME = "javax.portlet.userinfo";

	/**
	 * Portlet request attribute name to which the context map is bound. Its value is the same as
	 * <code>Consts.PORTAL_CONTEXT_KEY </code>.
	 */
	private static final String CONTEXT_MAP_ATTR_NAME = Consts.PORTAL_CONTEXT_KEY;

	/**
	 * Helper class used to collect data shared with portlets.
	 */
	private final PortletDataCollector mDataCollector;


	public PortletDataPreDisplayAction()
	{
		this(new PortletDataCollector());
	}

	/**
	 * Constructor for testing.
	 * 
	 * @param dataCollector helper object used to collect data shared with portlets.
	 */
	PortletDataPreDisplayAction(PortletDataCollector dataCollector)
	{
		mDataCollector = dataCollector;
	}

	/**
	 * Sets the data which should be shared with the local portlets as request attributes.
	 * The data being set is user profile map ({@link com.hp.it.spf.xa.portletdata.portal.PortletDataCollector#retrieveUserProfile(javax.servlet.http.HttpServletRequest)})
	 * and user context map ({@link com.hp.it.spf.xa.portletdata.portal.PortletDataCollector#retrieveUserContextKeys(javax.servlet.http.HttpServletRequest)}).
	 * The attribute names are respectively <code>com.vignette.portal.attribute.portlet.{portlet name}.javax.portlet.userinfo</code>
	 * and <code>com.vignette.portal.attribute.portlet.{portlet name}.ContextMap</code>. Note that
	 * if the portlets use <code>MapAttributeFilter</code> the Vignette namespace prefix as well
	 * as the portlet name will be stripped of the attribute name. 
	 * The data is being set only for local portlets whose names are collected using Vignette API.
	 *
	 * @param portalContext portal request context
	 * @return <code>null</code> or throws ActionException
	 * @throws ActionException If anything goes wrong when retrieving the list of portlet names
	 */
	public PortalURI execute(PortalContext portalContext) throws ActionException {
		HttpServletRequest request = portalContext.getPortalRequest().getRequest();

		try {
			Set<String> portletUIDs = collectCurrentPagePortletUIDs(portalContext);
			Set<String> portletNames = convertToPortletNames(portletUIDs);

			if (!portletNames.isEmpty()) {
				Map<String, String> contextMap = mDataCollector.retrieveUserContextKeys(request);
				Map userProfile = mDataCollector.retrieveUserProfile(request);

				for (String portletName : portletNames) {
					request.setAttribute(
							portletAttributeName(portletName, USER_PROFILE_ATTR_NAME), userProfile);
					request.setAttribute(
							portletAttributeName(portletName, CONTEXT_MAP_ATTR_NAME), contextMap);

					if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
						LOG.debug("Set attribute '" + portletAttributeName(portletName, USER_PROFILE_ATTR_NAME) +
							"' => " + userProfile);
						LOG.debug("Set attribute '" + portletAttributeName(portletName, CONTEXT_MAP_ATTR_NAME) +
							"' => " + contextMap);
					}
				}
			}
		}
		catch (PageException e) {
			ActionException e2 = new ActionException(
					"Error getting list of portlets" +
							(portalContext.getResolvedPortletPage() != null ?
									" present on page: " + portalContext.getResolvedPortletPage().getTitle() : ""));
			// ActionException does not have a constructor taking the root cause exception, therefore
			// setting it manually.
			e2.initCause(e);
			throw e2;
		}

		return null;
	}


	/**
	 * Builds Vignette-namespaced portlet attribute name suited for sharing data between portal
	 * and local Java portlets.
	 *
	 * @param portletName name of the portlet as defined in <tt>portlet.xml</tt> <code>portlet-name</code>
	 * tag
	 * @param attributeName The name under which the attribute should be available in portlet request
	 * @return Vignette-namespaced attribute name
	 */
	private String portletAttributeName(String portletName, String attributeName) {
		return VIGNETTE_PORTLET_ATTR_PREFIX + portletName + "." + attributeName;
	}


	/**
	 * Retrieves the set of UIDs of portlets being present on the currently requested page. This list
	 * contains UIDs for all the portlets, including local and remote portlets.
	 *
	 * @param portalContext portal request context
	 * @return set of portlet UIDs present on the current page
	 * @throws PageException If anything goes wrong when retriving the portlet UIDs
	 */
	/*private*/ Set<String> collectCurrentPagePortletUIDs(PortalContext portalContext) throws PageException
	{
		Set<String> portletUids = new HashSet<String>();

		Page page = portalContext.getResolvedPortletPage();

		// Page would be null for My Pages page.
		if (page == null) {
			page = getMyPage(portalContext);
		}

		if (page != null) {
			PageLayout pageLayout = page.getPageLayout();
			UserModuleSet userModuleSet = page.getPageModuleSet().getUserModuleSet(portalContext.getCurrentUser());
			for (int divider = 0; divider < pageLayout.getDividerCount(); divider++) {
				for (int cell = 0; cell < pageLayout.getCellCount(divider); cell++) {
					String[] uids = userModuleSet.getPortletUIDs(divider, cell);
					if (uids != null && uids.length > 0) {
						portletUids.addAll(Arrays.asList(uids));
					}
				}
			}

			//Sometimes Vignette, in the list of portlet UIDs present on the page, returns "-1".
			//As it's meaningless for us, let's remove it.
			portletUids.remove("-1");
		}

		return portletUids;
	}

	/**
	 * Attempts to look for My Page page definition.
	 * @param portalContext portal request context
	 * @return My Page page or <code>null</code> if this is not a request for My Page or the page
	 * could not be found.
	 */
	private Page getMyPage(PortalContext portalContext)
	{
		// The code below comes from debugging session in which I saw that the selected
		// menu item node is of type MyPageNode and from MyPageBuilderPredisplayAction class
		// which, once having MyPage, retrieves the page definition using PageManager.
		Page result = null;
		MenuItemNode node = MenuItemUtils.getSelectedMenuItemNode(portalContext);
		if (node != null && node instanceof MyPageNode) {
			MyPageNode myPageNode = (MyPageNode) node;
			MyPage myPage = myPageNode.getMyPage();
			if (myPage != null) {
				result = PageManager.getInstance().getPage(myPage.getID());
			}
		}
		return result;
	}


	/**
	 * Converts the incoming set of portlet UIDs to portlet names, keeping only names of local portlets.
	 * The returned portlet names correspond to the value of <code>portlet-name</code> tag defined
	 * in <code>portlet.xml</code> of the given portlet.

	 * @param portletUIDs a set of portlet UIDs; may contain values for local and remote portlets
	 * @return the set of local portlet names corresponding to the given <tt>portletUIDs</tt>; remote
	 * portlets are omitted.
	 * @throws ActionException If anything goes wrong during portlet UID to name conversion.
	 */
	/*private*/ Set<String> convertToPortletNames(Set<String> portletUIDs) throws ActionException
	{
		Set<String> portletNames = new HashSet<String>();

		for (String portletUid : portletUIDs) {
			try {
				StandardPortlet portlet = getPortletForUID(portletUid);
				if (portlet != null && "LOCAL".equals(portlet.getPortletKind())) {
					String portletName = getPortletName(portlet);
					if (portlet != null) {
						portletNames.add(portletName);
					}
				}
			}
			catch (UIDException e) {
				ActionException e2 = new ActionException("Error retrieving portlet with UID " + portletUid);
				// ActionException does not have a constructor taking the root cause exception, therefore
				// setting it manually.
				e2.initCause(e);
				throw e2;
			}
		}

		return portletNames;
	}

	/**
	 * Retrieves the standard portlet for the given <code>portletUid</code>.
	 * 
	 * @param portletUid portlet UID
	 * @return the portlet object or <code>null</code> if the portlet could not be found or type
	 * of the object returned for the given <code>portletUid</code> is not an instance of
	 * <code>StandardPortlet</code>.
	 * @throws UIDException If an error occured when querying Vignette for the given UID.
	 */
	private StandardPortlet getPortletForUID(String portletUid) throws UIDException
	{
		Object portletObj = PortletManager.getInstance().getObjectFromUID(portletUid);
		if (portletObj != null && portletObj instanceof StandardPortlet) {
			return (StandardPortlet) portletObj;
		}
		
		return null;
	}

	/**
	 * Retieves the portlet name for the given portlet.
	 * Portlet name is present in Vignette portlet type's definition specific to local (JSR)
	 * portlets.
	 * @param portlet portlet for which the is to be retrieved
	 * @return portlet name or <code>null</code> if the portlet is not local (JSR) portlet.
	 */
	private String getPortletName(StandardPortlet portlet) {
		if (portlet == null || portlet.getPortletSpi() == null) {
			return null;
		}
		
		// Portlet name is present in Vignette's portlet type definition specific
		// to local (JSR) portlets.
		Object portletTypeSpiObj = portlet.getPortletSpi().getPortletTypeSpi();
		if (portletTypeSpiObj != null && portletTypeSpiObj instanceof JsrPortletTypeSpi) {
			return ((JsrPortletTypeSpi) portletTypeSpiObj).getName();
		}

		return null;
	}
}
