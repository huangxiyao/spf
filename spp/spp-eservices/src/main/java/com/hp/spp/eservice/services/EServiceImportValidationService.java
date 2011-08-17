package com.hp.spp.eservice.services;

import com.hp.spp.eservice.Site;
import com.hp.spp.eservice.exception.XmlImportException;

public interface EServiceImportValidationService {
	
	public void validateEServiceImport(Site importedSite, Site existingSite) throws XmlImportException;
	
	public void validateStandardParameterSetImport(Site importedSite, Site existingSite) throws XmlImportException;
}
