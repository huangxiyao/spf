package com.hp.it.cas.persona.configuration.service.impl;

import java.util.concurrent.atomic.AtomicReference;

import com.hp.it.cas.persona.configuration.service.IApplication;
import com.hp.it.cas.persona.configuration.service.IMetadataConfigurationService;
import com.hp.it.cas.persona.configuration.service.IPermission;
import com.hp.it.cas.persona.configuration.service.IUserAttribute;
import com.hp.it.cas.security.dao.AppUserAttrPrmsn;

class LazyUserAttributePermission implements IPermission {

	private final IMetadataConfigurationService metadataConfigurationService;
	private final IApplication application;
	private final AppUserAttrPrmsn appUserAttrPrmsn;
	private final AtomicReference<IUserAttribute> userAttribute = new AtomicReference<IUserAttribute>();
	
	LazyUserAttributePermission(IMetadataConfigurationService metadataConfigurationService, IApplication application, AppUserAttrPrmsn appUserAttrPrmsn) {
		this.metadataConfigurationService = metadataConfigurationService;
		this.application = application;
		this.appUserAttrPrmsn = appUserAttrPrmsn;
	}

	public IApplication getApplication() {
		return application;
	}

	public IUserAttribute getCompoundUserAttribute() {
		IUserAttribute userAttribute = getUserAttribute();
		return userAttribute.isCompoundUserAttribute() ? userAttribute : null;
	}

	public IUserAttribute getSimpleUserAttribute() {
		IUserAttribute userAttribute = getUserAttribute();
		return userAttribute.isSimpleUserAttribute() ? userAttribute : null;
	}

	private IUserAttribute getUserAttribute() {
		synchronized (userAttribute) {
			if (userAttribute.get() == null) {
				userAttribute.set(metadataConfigurationService.findUserAttribute(appUserAttrPrmsn.getKey().getUserAttrId()));
			}
		}
		return userAttribute.get();
	}
}
