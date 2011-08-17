/**
 * 
 */
package com.hp.spp.eservice.services.impl;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import com.hp.spp.eservice.EService;
import com.hp.spp.eservice.Site;
import com.hp.spp.eservice.StandardParameterSet;
import com.hp.spp.eservice.exception.XmlImportException;
import com.hp.spp.eservice.services.EServiceImportValidationService;

/**
 * @author PBRETHER
 * 
 */
public class EServiceImportValidationServiceImpl implements EServiceImportValidationService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.eservice.services.EServiceImportValidationService#validateEServiceImport(com.hp.spp.eservice.Site)
	 */
	public void validateEServiceImport(Site importedSite, Site existingSite)
			throws XmlImportException {

		Set eServices = importedSite.getEServiceList();
		if (eServices != null) {
			ArrayList paramSetNames = getParamSetNames(existingSite);
			ArrayList names = new ArrayList();
			Iterator it = eServices.iterator();
			while (it.hasNext()) {
				EService service = (EService) it.next();
				String name = service.getName();
				if (names.contains(name)) {
					throw new XmlImportException(
							"At least two eServices have the same name in the same import. Name: ["
									+ name + "].");
				}
				checkParamSetExists(service, paramSetNames);
				checkCharset(service);
				
				names.add(name);
			}
		}
	}

	
	

	/**
	 * Checks if the input character-set is supported by java
	 * 
	 * @param service
	 * @throws XmlImportException if the character set is not
	 * 
	 */
	
	private void checkCharset(EService service) throws XmlImportException {
		String characterSet = service.getCharacterEncoding();
		
		if (characterSet != null && !"".equals(characterSet)){
			try{
					Charset.forName(characterSet);
			}catch (UnsupportedCharsetException ex){
				throw new XmlImportException("The character encoding [" + characterSet
						+ "] is not supported");
			}
		}
		
		
	}

	/**
	 * Check that the param sets that are referenced in the eService exists
	 * 
	 * @param service
	 * @param paramSetNames
	 * @throws XmlImportException
	 */
	private void checkParamSetExists(EService service, ArrayList paramSetNames)
			throws XmlImportException {
		String referencedParamSet = service.getStandardParameterSetName();
		if (referencedParamSet != null && !paramSetNames.contains(referencedParamSet)) {
			throw new XmlImportException("The parameter set [" + referencedParamSet
					+ "] does not exist.");
		}
	}

	private ArrayList getParamSetNames(Site existingSite) {
		ArrayList names = new ArrayList();
		Iterator it = existingSite.getStandardParameterSet().iterator();
		while (it.hasNext()) {
			StandardParameterSet param = (StandardParameterSet) it.next();
			names.add(param.getName());
		}
		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.eservice.services.EServiceImportValidationService#validateStandardParameterSetImport(com.hp.spp.eservice.Site)
	 */
	public void validateStandardParameterSetImport(Site importedSite, Site existingSite)
			throws XmlImportException {

		Set paramSets = importedSite.getStandardParameterSet();
		if (paramSets != null) {
			ArrayList names = new ArrayList();
			Iterator it = paramSets.iterator();
			while (it.hasNext()) {
				StandardParameterSet param = (StandardParameterSet) it.next();
				String name = param.getName();
				if (names.contains(name)) {
					throw new XmlImportException(
							"At least two StandardParameterSets have the same name in the same import. Name: ["
									+ name + "].");
				}
				names.add(name);
			}
		}

	}

}
