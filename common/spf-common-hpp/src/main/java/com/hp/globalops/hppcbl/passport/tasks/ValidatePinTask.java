package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.ValidatePinRequestElement;
import com.hp.globalops.hppcbl.webservice.ValidatePinResponseElement;

public class ValidatePinTask extends Task {

    public static final String OPERATION_NAME="validatePin";
    private ValidatePinRequestElement requestElement;

	public ValidatePinTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new ValidatePinResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String guid = (String) args.get("guid");
        String newPassword = (String) args.get("newPassword");
        String newPasswordConfirm = (String) args.get("newPasswordConfirm");
        String securityAnswer = (String) args.get("securityAnswer");
        String securityQuestion = (String) args.get("securityQuestion");
        String pin = (String) args.get("pin");

        requestElement = new ValidatePinRequestElement();
        requestElement.setGuid(guid);
        requestElement.setNewPassword(newPassword);
        requestElement.setNewPasswordConfirm(newPasswordConfirm);
        requestElement.setSecurityAnswer(securityAnswer);
        requestElement.setSecurityQuestion(securityQuestion);
        requestElement.setPin(pin);
	}

}
