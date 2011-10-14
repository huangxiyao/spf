package com.hp.it.cas.persona.uav.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;

import com.hp.it.cas.persona.dao.IPersonaDaoFactory;
import com.hp.it.cas.persona.uav.service.IUserAttribute;
import com.hp.it.cas.security.dao.AppCmpndAttrSmplAttrPrmsnKey;
import com.hp.it.cas.security.dao.AppUserAttrPrmsnKey;
import com.hp.it.cas.security.dao.IAppCmpndAttrSmplAttrPrmsnDAO;
import com.hp.it.cas.security.dao.IAppUserAttrPrmsnDAO;
import com.hp.it.cas.xa.security.IApplicationPrincipal;

final class Authorizer {

	private static final GrantedAuthority PERSONA_ADMINISTRATION = new GrantedAuthorityImpl("ROLE_ADMIN-PERSONA");

	private final IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao;
	private final IAppUserAttrPrmsnDAO appUserAttrPrmsnDao;

	Authorizer(IPersonaDaoFactory daoFactory) {
		this.appCmpndAttrSmplAttrPrmsnDao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		this.appUserAttrPrmsnDao = daoFactory.getAppUserAttrPrmsnDAO();
	}

	void authorize(IUserAttribute userAttribute) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();

		// users are not authorized, only applications acting on their behalf are
		if (principal instanceof IApplicationPrincipal) {
			GrantedAuthority[] authorities = authentication.getAuthorities();
			
			// administrators are fully authorized
			if (!Arrays.asList(authorities).contains(PERSONA_ADMINISTRATION)) {
				
				// applications must have specific attribute permission
				IApplicationPrincipal applicationPrincipal = (IApplicationPrincipal) principal;
				int applicationPortfolioIdentifier = applicationPrincipal.getApplicationPortfolioIdentifier();
				boolean authorized = false;

				if (userAttribute.isSimpleUserAttribute()) {
					authorized = isAuthorized(applicationPortfolioIdentifier, userAttribute.getSimpleUserAttributeIdentifier());
				} else {
					// permission for all simple attributes in a compound attribute
					authorized = isAuthorized(applicationPortfolioIdentifier, userAttribute.getCompoundUserAttributeIdentifier());

					if (!authorized) {
						// permission for a specific simple attribute in a compound attribute
						authorized = isAuthorized(applicationPortfolioIdentifier, userAttribute.getCompoundUserAttributeIdentifier(), userAttribute.getSimpleUserAttributeIdentifier());
					}
				}

				if (!authorized) {
					throw new PermissionDeniedDataAccessException(String.format("Application '%s' is not authorized to write user attribute '%s'.",
							applicationPrincipal.getName(), userAttribute), null);
				}
			}
		} else {
			throw new PermissionDeniedDataAccessException(String.format("Principal %s is not authorized to write user attribute '%s'.", principal,
					userAttribute), null);
		}
	}
	
	private boolean isAuthorized(int applicationPortfolioIdentifier, String compoundUserAttributeIdentifier, String simpleUserAttributeIdentifier) {
		AppCmpndAttrSmplAttrPrmsnKey compoundSimpleKey = new AppCmpndAttrSmplAttrPrmsnKey();
		compoundSimpleKey.setAppPrtflId(new BigDecimal(applicationPortfolioIdentifier));
		compoundSimpleKey.setCmpndUserAttrId(compoundUserAttributeIdentifier);
		compoundSimpleKey.setSmplUserAttrId(simpleUserAttributeIdentifier);
		return appCmpndAttrSmplAttrPrmsnDao.selectByPrimaryKey(compoundSimpleKey) != null;
	}
	
	private boolean isAuthorized(int applicationPortfolioIdentifier, String userAttributeIdentifier) {
		AppUserAttrPrmsnKey key = new AppUserAttrPrmsnKey();
		key.setAppPrtflId(new BigDecimal(applicationPortfolioIdentifier));
		key.setUserAttrId(userAttributeIdentifier);
		return appUserAttrPrmsnDao.selectByPrimaryKey(key) != null;
	}
}
