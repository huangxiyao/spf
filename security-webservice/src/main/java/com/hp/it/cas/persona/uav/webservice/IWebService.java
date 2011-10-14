package com.hp.it.cas.persona.uav.webservice;

import java.util.Collection;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.hp.it.cas.persona.uav.service.IUserAttributeValue;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;

@Transactional
public interface IWebService {
	Collection<IUserAttributeValue> putUserAttributeValues(Collection<IUserAttributeValue> userAttributeValues);
	Set<IUserAttributeValue> findUserAttributeValues(IUserIdentifier userIdentifier);
	void removeUserAttributeValues(Collection<IUserAttributeValue> userAttributeValues);
}
