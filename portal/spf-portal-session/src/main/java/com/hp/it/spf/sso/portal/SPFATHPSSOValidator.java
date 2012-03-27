package com.hp.it.spf.sso.portal;

import com.epicentric.authentication.AuthenticationException;
import javax.servlet.http.HttpServletRequestWrapper;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: liuliyey
 * Date: 12-3-5
 * To change this template use File | Settings | File Templates.
 */
public class SPFATHPSSOValidator extends AbstractSPFSSOValidator {
	public boolean canValidate(HttpServletRequest request)
			throws AuthenticationException {
		/*
		HttpServletRequestWrapper requestWrapper = (HttpServletRequestWrapper)request;
		String accessType = requestWrapper.getHeader(AuthenticationConsts.HEADER_ACCESS_TYPE);
		if (AuthenticationConsts.ACCESS_TYPE_INTRANET.equals(accessType)) {
			return true;
		}      */
		if (AuthenticatorHelper.loggedIntoAtHP(request)) {
			return true;
		}
		return false;
	}
}
