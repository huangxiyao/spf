package com.hp.spf.misc.portal.component.secondarypage;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.enduser.components.BaseAction;

/**
 * Redirect to spf error page instead of default site picker page.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @author <link href="ye.liu@hp.com">Ye Liu</link>
 * @version TBD
 * @see com.vignette.portal.website.enduser.components.BaseAction
 */

public class SitePickerDisplayAction extends BaseAction {
	/**
	 * The LOG for this display action class.
	 */
	private static final LogWrapper LOG = new LogWrapper(
			SitePickerDisplayAction.class);

	/**
	 * This method will redirect to the SPF system error page instead of default
	 * site picker page.
	 * 
	 * @param portalContext
	 *            The encapsulated PortalContext object of current secondary
	 *            page.
	 * @return PortalURI The address of the system error page.
	 * @see com.vignette.portal.website.enduser.components.BaseAction#execute(com.vignette.portal.website.enduser.PortalContext)
	 */
	public PortalURI execute(PortalContext portalContext) {
		StringBuilder errorPageURI = new StringBuilder();
		errorPageURI.append(portalContext.getPortalHttpRoot());
		errorPageURI.append("/site/");
		errorPageURI.append(Consts.SPF_CORE_SITE);
		errorPageURI.append("/");
		errorPageURI.append(Consts.PAGE_FRIENDLY_URI_SYSTEM_ERROR);

		HttpServletResponse response = portalContext.getPortalResponse()
				.getResponse();

		try {
			String errorPage = Utils.slashify(errorPageURI.toString());
			response.sendRedirect(errorPage);
			if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
				LOG.debug("Redirect to: " + errorPage);
			}
		} catch (IOException ex) {
			LOG.error(ex);
		}

		return null;
	}

}