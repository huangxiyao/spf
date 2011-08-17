package com.hp.spp.portlets.reports.hppreport.command;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
* The core functionality of this class is to validate the Search form 
*    
* @author Shivashanker B
*/

/** Revision History:
*
* Ver.   Modified By           Date           Notes
*--------------------------------------------------------------------------------------*
* v1     Shivashanker B       27-Oct-2006      Created
*
*/
public class HPPReportValidator implements Validator {	
	public boolean supports(Class clazz) {
		return clazz.equals(HPPReport.class);
	}
	public void validate(Object obj, Errors errors) {
		HPPReport aHPPReport = (HPPReport) obj;				
		validateEntityIdentifier(aHPPReport,errors);	
	}
	
	public void validateEntityIdentifier(HPPReport aHPPReport, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "searchEntityCommand.entityIdentifier", "ENTITYIDENTIFIER_REQUIRED", "Criteria is empty.");		
	}
}