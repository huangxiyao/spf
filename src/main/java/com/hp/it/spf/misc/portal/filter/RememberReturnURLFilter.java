package com.hp.it.spf.misc.portal.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.epicentric.template.Style;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.website.enduser.PortalContext;

public class RememberReturnURLFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
			// save the current URL to session
			PortalContext context = (PortalContext) req
					.getAttribute("portalContext");
			if (context != null) {
				Style thisPage = context.getCurrentSecondaryPage();
				if (thisPage != null) {
					if (!Consts.PAGE_FRIENDLY_ID_RETURN.equals(thisPage
							.getFriendlyID())) {
						String currentURL = Utils.getRequestURL(req);
						if (session != null) {
							session.setAttribute(
									Consts.SESSION_ATTR_RETURN_URL, currentURL);
						}
					}
				}
			}
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}
}
