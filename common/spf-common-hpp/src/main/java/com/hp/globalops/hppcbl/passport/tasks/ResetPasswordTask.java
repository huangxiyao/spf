package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.EmailTemplate;
import com.hp.globalops.hppcbl.webservice.ResetPasswordRequestElement;
import com.hp.globalops.hppcbl.webservice.ResetPasswordResponseElement;

public class ResetPasswordTask extends Task {

    public static final String OPERATION_NAME="resetPassword";
    private ResetPasswordRequestElement requestElement;

	public ResetPasswordTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new ResetPasswordResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String userId = (String) args.get("userId");
        String email = (String) args.get("email");
        EmailTemplate[] emailTemplate = (EmailTemplate[]) args.get("emailTemplate");

        requestElement = new ResetPasswordRequestElement();
        requestElement.setUserId(userId);
        requestElement.setEmail(email);
        requestElement.setEmailTemplate(emailTemplate);
	}

}
