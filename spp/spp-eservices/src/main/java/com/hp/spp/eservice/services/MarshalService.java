package com.hp.spp.eservice.services;

import com.hp.spp.eservice.Site;

public interface MarshalService {
	
	/**
	 * Creates XML containing eservices from a Site object.
	 * @param site
	 * @return XML
	 */
	public byte[] marshalEServices(Site site);

	/**
	 * Creates XML containing standard param set from a Site object.
	 * @param site
	 * @return XML
	 */
	public byte[] marshalStandardParameterSets(Site site);
}
