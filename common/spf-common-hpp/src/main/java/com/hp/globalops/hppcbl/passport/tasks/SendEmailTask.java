package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.EmailTemplate;
import com.hp.globalops.hppcbl.webservice.SendEmailRequestElement;
import com.hp.globalops.hppcbl.webservice.SendEmailResponseElement;

public class SendEmailTask extends Task {

    public static final String OPERATION_NAME="sendEmail";
    private SendEmailRequestElement requestElement;

	public SendEmailTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new SendEmailResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String userId = (String) args.get("userId");
        EmailTemplate[] emailTemplate = (EmailTemplate[]) args.get("emailTemplate");

        requestElement = new SendEmailRequestElement();
        requestElement.setUserId(userId);
        requestElement.setEmailTemplate(emailTemplate);
	}

}
