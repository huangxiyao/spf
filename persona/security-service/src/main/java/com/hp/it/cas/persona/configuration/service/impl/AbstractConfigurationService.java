package com.hp.it.cas.persona.configuration.service.impl;

import java.security.Principal;

import org.springframework.security.context.SecurityContextHolder;

abstract class AbstractConfigurationService {

	protected String getAuditPrincipalName() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return principal instanceof Principal ? ((Principal) principal).getName() : "";
	}
}
