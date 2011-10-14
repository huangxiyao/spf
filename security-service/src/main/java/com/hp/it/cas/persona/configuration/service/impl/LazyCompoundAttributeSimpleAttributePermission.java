package com.hp.it.cas.persona.configuration.service.impl;

import java.util.concurrent.atomic.AtomicReference;

import com.hp.it.cas.persona.configuration.service.IApplication;
import com.hp.it.cas.persona.configuration.service.IMetadataConfigurationService;
import com.hp.it.cas.persona.configuration.service.IPermission;
import com.hp.it.cas.persona.configuration.service.IUserAttribute;
import com.hp.it.cas.security.dao.AppCmpndAttrSmplAttrPrmsn;

class LazyCompoundAttributeSimpleAttributePermission implements IPermission {

	private final IMetadataConfigurationService metadataConfigurationService;
	private final IApplication application;
	private final AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn;
	private final AtomicReference<IUserAttribute> compoundUserAttribute = new AtomicReference<IUserAttribute>();
	private final AtomicReference<IUserAttribute> simpleUserAttribute = new AtomicReference<IUserAttribute>();
	
	public LazyCompoundAttributeSimpleAttributePermission(IMetadataConfigurationService metadataConfigurationService, IApplication application, AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn) {
		this.metadataConfigurationService = metadataConfigurationService;
		this.application = application;
		this.appCmpndAttrSmplAttrPrmsn = appCmpndAttrSmplAttrPrmsn;
	}

	public IApplication getApplication() {
		return application;
	}

	public IUserAttribute getCompoundUserAttribute() {
		synchronized (compoundUserAttribute) {
			if (compoundUserAttribute.get() == null) {
				compoundUserAttribute.set(metadataConfigurationService.findUserAttribute(appCmpndAttrSmplAttrPrmsn.getKey().getCmpndUserAttrId()));
			}
		}
		return compoundUserAttribute.get();
	}

	public IUserAttribute getSimpleUserAttribute() {
		synchronized (simpleUserAttribute) {
			if (simpleUserAttribute.get() == null) {
				simpleUserAttribute.set(metadataConfigurationService.findUserAttribute(appCmpndAttrSmplAttrPrmsn.getKey().getSmplUserAttrId()));
			}
		}
		return simpleUserAttribute.get();
	}

}
