package com.hp.spp.portlets.reports.userageing.validator;


import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.hp.spp.portlets.reports.userageing.commands.LastLoginCommand;

public class AgeingReportValidator implements Validator {

	    public boolean supports(Class clazz) {
	        return LastLoginCommand.class.isAssignableFrom(clazz);
	    }

	    public void validate(Object obj, Errors errors) {
	    	LastLoginCommand loginCommand = (LastLoginCommand)obj;
	    	validateLastLoginCount(loginCommand, errors);
	    	//Enter some dummy value
	    	int lastLoginNum = 0;
	    	try{
	    		lastLoginNum = Integer.parseInt(loginCommand.getLastLoginNumDays());
	    	}catch(NumberFormatException  ex){
	 	         errors.rejectValue("lastLoginNumDays", "invalidNumber", "Last login date is an invalid number");
	 		}
	    		
	    		
	    	if (lastLoginNum < 0) {
   	         errors.rejectValue("lastLoginNumDays", "negativevalue", "Last login date is negative");
    		}
	    }

		public void validateLastLoginCount(LastLoginCommand loginCommand, Errors errors) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastLoginNumDays", "ENTITY_REQUIRED", "Last login date is empty or invalid.");
		}
	    
}


