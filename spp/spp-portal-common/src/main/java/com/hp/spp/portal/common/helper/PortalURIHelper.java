package com.hp.spp.portal.common.helper;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.epicentric.common.website.MenuItemNode;
import com.epicentric.template.Template;
import com.epicentric.template.sqltemplatemanager.SQLTemplate;
import com.vignette.portal.util.StringUtils;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.linking.internal.BasePortalContext;
import com.vignette.portal.website.linking.internal.PortalURIFactory;

/**
 * Helper class for using accessing the vignette PortalURIs.
 * 
 * @author Cyril MICOUD
 * @version $Revision: 1.5 $ $Date: 2006/10/30 16:48:19 $
 */
public class PortalURIHelper {

	private static Logger mLog = Logger.getLogger(PortalURIHelper.class);

	public final static String PORTAL_CONTEXT = "portalContext";

	private MenuItemHelper mMenuItemHelper = null;
	
	private PortalHelper mPortalHelper = null;

	public PortalURIHelper() {
		super();
		mMenuItemHelper = new MenuItemHelper();
		mPortalHelper = new PortalHelper();
	}

	/**
	 * Retrieve the PortalURI from the PortalContext by a simple page name. The
	 * PortalURI is built from the MenuItem or the Template retrieve by the
	 * title or the friendlyId. By default, this function return the PortalURI
	 * of the first MenuItemNode and it is configured to be used for a forward
	 * action (forward parameter value is set to TRUE).
	 * 
	 * @param portalContext
	 *            Needed in order to get all MenuItem and Template
	 * @param pagename
	 *            Page name of the searched element
	 * @return PortalURI whose page name was passed
	 */
	public PortalURI getPortalURI(PortalContext portalContext, String pagename) {
		return getPortalURI(portalContext, pagename, true);
	}

	/**
	 * Retrieve the PortalURI from the Request by a simple page name. The
	 * PortalURI is built from the MenuItem or the Template retrieve by the
	 * title or the friendlyId. By default, this function return the PortalURI
	 * of the first MenuItemNode and it is configured to be used for a forward
	 * action (forward parameter value is set to TRUE).
	 * 
	 * @param request
	 *            Needed in order to get all MenuItem and Template
	 * @param pagename
	 *            Page name of the searched element
	 * @return PortalURI whose page name was passed
	 */
	public PortalURI getPortalURI(HttpServletRequest request, String pagename) {
		return getPortalURI(request, pagename, true);
	}

	/**
	 * Retrieve the PortalURI from the Request and the PortalContext by a simple
	 * page name. The PortalURI is built from the MenuItem or the Template
	 * retrieve by the title or the friendlyId. By default, this function return
	 * the PortalURI of the first MenuItemNode and it is configured to be used
	 * for a forward action (forward parameter value is set to TRUE).
	 * 
	 * @param portalContext
	 *            Needed in order to get all MenuItem and Template
	 * @param request
	 *            Needed in order to build the PortalURI
	 * @param pagename
	 *            Page name of the searched element
	 * @return PortalURI whose page name was passed
	 */
	public PortalURI getPortalURI(PortalContext portalContext, HttpServletRequest request, String pagename) {
		return getPortalURI(portalContext, request, pagename, true);
	}

	/**
	 * Retrieve the PortalURI from the PortalContext by a simple page name. The
	 * PortalURI is built from the MenuItem or the Template retrieve by the
	 * title or the friendlyId. By default, this function return the PortalURI
	 * of the first MenuItemNode. With this function you can choose if you want
	 * t build a simple (normal) PortalURI to redirect or to forward by the
	 * <i>isToForward</i> boolean parameter.
	 * 
	 * @param portalContext
	 *            Needed in order to get all MenuItem and Template
	 * @param pagename
	 *            Page name of the searched element
	 * @return PortalURI whose page name was passed
	 */
	public PortalURI getPortalURI(PortalContext portalContext, String pagename, boolean isToForward) {
		return getPortalURI(portalContext, pagename, isToForward, true);
	}

	/**
	 * Retrieve the PortalURI from the Request by a simple page name. The
	 * PortalURI is built from the MenuItem or the Template retrieve by the
	 * title or the friendlyId. By default, this function return the PortalURI
	 * of the first MenuItemNode. With this function you can choose if you want
	 * t build a simple (normal) PortalURI to redirect or to forward by the
	 * <i>isToForward</i> boolean parameter.
	 * 
	 * @param request
	 *            Needed in order to get all MenuItem and Template
	 * @param pagename
	 *            Page name of the searched element
	 * @return PortalURI whose page name was passed
	 */
	public PortalURI getPortalURI(HttpServletRequest request, String pagename, boolean isToForward) {
		return getPortalURI(request, pagename, isToForward, true);
	}

	/**
	 * Retrieve the PortalURI from the Request and the PortalContext by a simple
	 * page name. The PortalURI is built from the MenuItem or the Template
	 * retrieve by the title or the friendlyId. By default, this function return
	 * the PortalURI of the first MenuItemNode. With this function you can
	 * choose if you want t build a simple (normal) PortalURI to redirect or to
	 * forward by the <i>isToForward</i> boolean parameter.
	 * 
	 * @param portalContext
	 *            Needed in order to get all MenuItem and Template
	 * @param request
	 *            Needed in order to build the PortalURI
	 * @param pagename
	 *            Page name of the searched element
	 * @return PortalURI whose page name was passed
	 */
	public PortalURI getPortalURI(PortalContext portalContext, HttpServletRequest request, String pagename, boolean isToForward) {
		return getPortalURI(portalContext, request, pagename, isToForward, true);
	}

	/**
	 * Retrieve the PortalURI from the PortalContext by a simple page name. The
	 * PortalURI is built from the MenuItem or the Template retrieve by the
	 * title or the friendlyId. With this function, you can choose if you want
	 * to retrieve the first MenuItemNode or not (by the <i>withFirstNode</i>
	 * parameter) and if you want to use the PortalURI to redirect or forward
	 * (by the <i>isToForward</i> parameter).
	 * 
	 * <p>
	 * <b>If <i>withFirstNode</i> equals FALSE, PortalURI can be NULL</b>
	 * </p>
	 * 
	 * @param portalContext
	 *            Needed in order to get all MenuItem and Template
	 * @param pagename
	 *            Page name of the searched element
	 * @return PortalURI whose page name was passed
	 */
	public PortalURI getPortalURI(PortalContext portalContext, String pagename, boolean isToForward, boolean withFirstNode) {
		return getPortalURI(portalContext, portalContext.getPortalRequest().getRequest(), pagename, isToForward, withFirstNode);
	}

	/**
	 * Retrieve the PortalURI from the Request by a simple page name. The
	 * PortalURI is built from the MenuItem or the Template retrieve by the
	 * title or the friendlyId. With this function, you can choose if you want
	 * to retrieve the first MenuItemNode or not (by the <i>withFirstNode</i>
	 * parameter) and if you want to use the PortalURI to redirect or forward
	 * (by the <i>isToForward</i> parameter).
	 * 
	 * <p>
	 * <b>If <i>withFirstNode</i> equals FALSE, PortalURI can be NULL</b>
	 * </p>
	 * 
	 * @param request
	 *            Needed in order to get all MenuItem and Template
	 * @param pagename
	 *            Page name of the searched element
	 * @return PortalURI whose page name was passed
	 */
	public PortalURI getPortalURI(HttpServletRequest request, String pagename, boolean isToForward, boolean withFirstNode) {
		if ((PortalContext) request.getAttribute(PORTAL_CONTEXT) == null)
			throw new NullPointerException("An attribute portalContext is required in the request.");

		return getPortalURI((PortalContext) request.getAttribute(PORTAL_CONTEXT), request, pagename, isToForward, withFirstNode);
	}

	/**
	 * Retrieve the PortalURI from the Request and the PortalContext by a simple
	 * page name. The PortalURI is built from the MenuItem or the Template
	 * retrieve by the title or the friendlyId. With this function, you can
	 * choose if you want to retrieve the first MenuItemNode or not (by the
	 * <i>withFirstNode</i> parameter) and if you want to use the PortalURI to
	 * redirect or forward (by the <i>isToForward</i> parameter). <br>
	 * <br>
	 * If <i>withFirstNode</i> equals <b>FALSE</b>, PortalURI can be <b>NULL</b><br>
	 * If <i>isToForward</i> equals <b>FALSE</b>, <i>request</i> is not
	 * required <i>(can be <b>NULL</b>)</i><br>
	 * 
	 * @param portalContext
	 *            Needed in order to get all MenuItem and Template
	 * @param request
	 *            Needed in order to build the PortalURI
	 * @param pagename
	 *            Page name of the searched element
	 * @return PortalURI whose page name was passed
	 */
	public PortalURI getPortalURI(PortalContext portalContext, HttpServletRequest request, String pagename, boolean isToForward, boolean withFirstNode) {
		PortalURI portalURI = null;

		// Only perform comparison if there is a value for channel otherwise
		// default to standard display
		if (!StringUtils.isEmpty(pagename)) {
			HashMap nodeMap = mMenuItemHelper.findMenuItemByNameInternal(portalContext, pagename);
			MenuItemNode menuItemNode = (MenuItemNode) nodeMap.get(MenuItemHelper.SEARCHED_NODE);

			portalURI = getMenuItemPortalURI(portalContext, menuItemNode);

			if (portalURI == null && withFirstNode) {
				menuItemNode = (MenuItemNode) nodeMap.get(MenuItemHelper.FIRST_NODE);

				portalURI = getMenuItemPortalURI(portalContext, menuItemNode);
				if (mLog.isDebugEnabled()) {
					mLog.debug("[Class]=PortalURIHelper [Method]=getPortalURI [Parameter]=MenuItem found [Value]=" + portalURI);
				}
			} else {
				if (mLog.isDebugEnabled()) {
					mLog.debug("[Class]=PortalURIHelper [Method]=getPortalURI [Parameter]=MenuItem found [Value]=" + portalURI);
				}
			}

			if (isToForward) {
				if (request == null) {
					if (mLog.isDebugEnabled()) {
						mLog.debug("[Class]=PortalURIHelper [Method]=getPortalURI [Comment]=request parameter is null and forward is requested.");
					}
				}

				if (mLog.isDebugEnabled()) {
					mLog.debug("[Class]=PortalURIHelper [Method]=getPortalURI [Comment]=Use to forward.");
				}

				if (portalURI != null) {
					portalURI.setForward(true);
				}
			}
		}

		return portalURI;
	}

	public String getFullPortalURL(PortalContext portalContext, String pagename) {

		if (StringUtils.isEmpty(pagename)) {
			return null;
		}

		String url = null;

		HashMap nodeMap = mMenuItemHelper.findMenuItemByNameInternal(portalContext, pagename);

		if (mLog.isDebugEnabled()) {
			mLog.debug("Node map: "+nodeMap);
		}
		
		MenuItemNode menuItemNode = (MenuItemNode) nodeMap.get(MenuItemHelper.SEARCHED_NODE);

		if (mLog.isDebugEnabled()) {
			mLog.debug("Menu item node: "+menuItemNode);
		}
		
		if (menuItemNode != null) {
			url = mMenuItemHelper.getNodeHref(portalContext, menuItemNode);
		}
		
		if (mLog.isDebugEnabled()) {
			mLog.debug("Returning URL: "+url);
		}

		return url;
	}
	
	public String getFullPortalTemplateURL(PortalContext portalContext, String pagename)	{
		String url = null;
		PortalURI portalURI = portalContext.createDisplayURI(pagename);
		
		if (mLog.isDebugEnabled()) {
			mLog.debug("portalURI: "+portalURI);
		}

		if (portalURI != null) {
			url = mPortalHelper.buildFullHref(portalURI.toString(), portalContext);
		}
		
		if (mLog.isDebugEnabled()) {
			mLog.debug("Returning URL: "+url);
		}

		return url;
	}

	/**
	 * Build a PortalURI from a MenuItemNode
	 * 
	 * @param request
	 *            Needed in order to build the PortalURI
	 * @param menuItemNode
	 *            Needed in order to know which specific PortalURI it needs to
	 *            build
	 * @return PortalURI build with the MenuItemNode
	 */
	public PortalURI getMenuItemPortalURI(HttpServletRequest request, MenuItemNode menuItemNode) {
		if ((PortalContext) request.getAttribute(PORTAL_CONTEXT) == null)
			throw new NullPointerException("An attribute portalContext is required in the request.");

		return getMenuItemPortalURI((PortalContext) request.getAttribute(PORTAL_CONTEXT), menuItemNode);
	}

	/**
	 * Build a PortalURI from a MenuItemNode
	 * 
	 * @param portalContext
	 *            Needed in order to build the PortalURI
	 * @param menuItemNode
	 *            Needed in order to know which specific PortalURI it needs to
	 *            build
	 * @return PortalURI build with the MenuItemNode
	 */
	public PortalURI getMenuItemPortalURI(PortalContext portalContext, MenuItemNode menuItemNode) {
		PortalURI portalURI = null;

		if (menuItemNode != null) {
			portalURI = PortalURIFactory.createMenuItemURI((BasePortalContext) portalContext, menuItemNode.getID());
		}

		return portalURI;
	}

	/**
	 * Build a PortalURI from a Template
	 * 
	 * @param portalContext
	 *            Needed in order to build the PortalURI
	 * @param template
	 *            Needed in order to know which specific PortalURI it needs to
	 *            build
	 * @return PortalURI build with the Template
	 */
	public PortalURI getTemplatePortalURI(HttpServletRequest request, Template template) {
		if ((PortalContext) request.getAttribute(PORTAL_CONTEXT) == null)
			throw new NullPointerException("An attribute portalContext is required in the request.");

		return getTemplatePortalURI((PortalContext) request.getAttribute(PORTAL_CONTEXT), template, false);
	}

	/**
	 * Build a PortalURI from a Template
	 * 
	 * @param portalContext
	 *            Needed in order to build the PortalURI
	 * @param template
	 *            Needed in order to know which specific PortalURI it needs to
	 *            build
	 * @return PortalURI build with the Template
	 */
	public PortalURI getTemplatePortalURI(PortalContext portalContext, Template template) {
		return getTemplatePortalURI(portalContext, template, false);
	}

	/**
	 * Build a PortalURI from a Template
	 * 
	 * @param portalContext
	 *            Needed in order to build the PortalURI
	 * @param template
	 *            Needed in order to know which specific PortalURI it needs to
	 *            build
	 * @param getProcess
	 *            By default the value must be FALSE to retrieve the predisplay
	 *            action in the first case and the process action in the second
	 *            case. But if the value is TRUE, the portalURI is build only on
	 *            the process action.
	 * @return PortalURI build with the Template
	 */
	public PortalURI getTemplatePortalURI(PortalContext portalContext, Template template, boolean getProcess) {
		SQLTemplate sqlTemplate = (SQLTemplate) template;

		PortalURI portalURI = null;
		if (sqlTemplate != null) {
			if (sqlTemplate.getPreDisplayActions().length > 0 && !getProcess) {
				portalURI = portalContext.createDisplayURI(sqlTemplate.getFriendlyID());
			} else if (sqlTemplate.getProcessActions().length > 0) {
				portalURI = portalContext.createProcessURI(sqlTemplate.getFriendlyID());
			} else {
				sqlTemplate = null;
			}

			if (portalURI != null)
				portalURI.setForward(true);
		}

		return portalURI;
	}

}
