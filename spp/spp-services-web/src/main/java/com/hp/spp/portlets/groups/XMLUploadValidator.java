package com.hp.spp.portlets.groups;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class XMLUploadValidator implements Validator {

    public boolean supports(Class clazz) {
        return XMLUpload.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
    	XMLUpload file = (XMLUpload)obj;
        validateFile(file, errors);
    }

	public void validateFile(XMLUpload file, Errors errors) {
		//ValidationUtils.rejectIfEmpty(errors, "file", "XML_FILE_IS_EMPTY", "XML file is empty");
	}
}
