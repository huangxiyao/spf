package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.UpdateCredentialsRequestElement;
import com.hp.globalops.hppcbl.webservice.UpdateCredentialsResponseElement;

public class UpdateCredentialsTask extends Task {

    public static final String OPERATION_NAME="updateCredentials";
    private UpdateCredentialsRequestElement requestElement;

	public UpdateCredentialsTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new UpdateCredentialsResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String guid = (String) args.get("guid");
        String newPassword = (String) args.get("newPassword");
        String newPasswordConfirm = (String) args.get("newPasswordConfirm");
        String securityAnswer = (String) args.get("securityAnswer");
        String securityQuestion = (String) args.get("securityQuestion");
        String userId = (String) args.get("userId");

        requestElement = new UpdateCredentialsRequestElement();
        requestElement.setGuid(guid);
        requestElement.setNewPassword(newPassword);
        requestElement.setNewPasswordConfirm(newPasswordConfirm);
        if(securityQuestion != null)
       		requestElement.setSecurityQuestion(securityQuestion);
        if(securityAnswer != null)
       		requestElement.setSecurityAnswer(securityAnswer);
        requestElement.setUserId(userId);
	}

}
