package com.hp.it.spf.sso.portal;

import com.epicentric.authentication.AuthenticationException;
import com.epicentric.authentication.Realm;
import com.epicentric.authentication.SSOValidator;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: liuliyey
 * Date: 12-3-5
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSPFSSOValidator implements SSOValidator {
	private static final LogWrapper LOG = AuthenticatorHelper.getLog(AbstractSPFSSOValidator.class);
	protected String realmId = "";

	public void init(Realm realm, Properties properties)
			throws AuthenticationException {
		realmId = realm.getID();
	}

	public abstract boolean canValidate(HttpServletRequest request)
			throws AuthenticationException ;

	public String getValidatedUserId(HttpServletRequest request)
			throws AuthenticationException {
		if (request.getAttribute(AuthenticationConsts.SSO_USERNAME) != null) {
			String userId = (String)request.getAttribute(AuthenticationConsts.SSO_USERNAME);
			return userId;
		} else {
			if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
				LOG.debug("Retrieve null from request for SSO username.");
			}
			return null;
		}
	}

	public boolean isLogoutEnabled() {
		return false;
	}

	public boolean isLoginEnabled() {
		return false;
	}

	public boolean login(PortalContext context)
			throws AuthenticationException {
		throw new UnsupportedOperationException("login has not been implemented");
	}

	public String fetchUserProfile(PortalContext context, Map userProps)
			throws AuthenticationException {
		throw new UnsupportedOperationException("fetchUserProfile has not been implemented");
	}

	public Set<String> getAdditionalFeatures() {
		return new HashSet();
	}
}
