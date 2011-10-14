package com.hp.it.cas.persona.uav.webservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.hp.it.cas.persona.uav.service.IUserAttributeValue;
import com.hp.it.cas.persona.uav.service.IUserAttributeValueService;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;

/**
 * An implementation of the transactional business service layer.
 *
 * @author Quintin May
 */
@Transactional
public class WebService implements IWebService {

	private final IUserAttributeValueService userAttributeValueService;
	
	/**
	 * Creates the service.
	 * @param userAttributeValueService the underlying non-transactional service.
	 */
	public WebService(IUserAttributeValueService userAttributeValueService) {
		this.userAttributeValueService = userAttributeValueService;
	}
	
	public Set<IUserAttributeValue> findUserAttributeValues(IUserIdentifier userIdentifier) {
		return userAttributeValueService.findUserAttributeValues(userIdentifier);
	}

	public Collection<IUserAttributeValue> putUserAttributeValues(Collection<IUserAttributeValue> userAttributeValues) {
		Collection<IUserAttributeValue> results = new ArrayList<IUserAttributeValue>();
		
		for (IUserAttributeValue userAttributeValue : userAttributeValues) {
			IUserAttributeValue result = userAttributeValueService.putUserAttributeValue(
					userAttributeValue.getUserIdentifier(),
					userAttributeValue.getUserAttribute().getCompoundUserAttributeIdentifier(),
					userAttributeValue.getInstanceIdentifier(),
					userAttributeValue.getUserAttribute().getSimpleUserAttributeIdentifier(),
					userAttributeValue.getValue()
			);
			results.add(result);
		}
		
		return results;
	}

	public void removeUserAttributeValues(Collection<IUserAttributeValue> userAttributeValues) {
		for (IUserAttributeValue userAttributeValue : userAttributeValues) {
			userAttributeValueService.remove(userAttributeValue);
		}
	}
}
