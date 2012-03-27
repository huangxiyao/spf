package com.hp.it.spf.sso.portal;

import com.epicentric.authentication.AuthenticationException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: liuliyey
 * Date: 12-3-5
 * To change this template use File | Settings | File Templates.
 */
public class SPFDefaultSSOValidator extends AbstractSPFSSOValidator {
	public boolean canValidate(HttpServletRequest request)
			throws AuthenticationException {
		return true;
	}
}
