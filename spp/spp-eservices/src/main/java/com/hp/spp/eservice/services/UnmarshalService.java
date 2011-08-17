package com.hp.spp.eservice.services;

import com.hp.spp.eservice.Site;
import com.hp.spp.eservice.exception.XmlImportException;

public interface UnmarshalService {
	
	/**
	 * Creates an Site object from the xml that contains a collection of
	 * EService objects.
	 * @param xml XML that complies with EServiceDefinition.xsd
	 * @return Site object
	 */
	public Site unmarshalEServices(byte[] xml) throws XmlImportException;

	/**
	 * Creates an Site object from the xml that contains a collection of
	 * StandardParameterSet objects.
	 * @param xml XML that complies with StandardParameterSet.xsd
	 * @return Site object
	 */
	public Site unmarshalStandardParameterSets(byte[] xml) throws XmlImportException;
}
