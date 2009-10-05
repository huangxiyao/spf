package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.GetSecurityQuestionRequestElement;
import com.hp.globalops.hppcbl.webservice.GetSecurityQuestionResponseElement;

public class GetSecurityQuestionTask extends Task {

    public static final String OPERATION_NAME="getSecurityQuestion";
    private GetSecurityQuestionRequestElement requestElement;

	public GetSecurityQuestionTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new GetSecurityQuestionResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String guid = (String) args.get("guid");

        requestElement = new GetSecurityQuestionRequestElement();
        requestElement.setGuid(guid);
	}

}
