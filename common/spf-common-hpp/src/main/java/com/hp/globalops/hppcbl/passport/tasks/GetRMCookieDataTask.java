package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.GetRMCookieDataRequestElement;
import com.hp.globalops.hppcbl.webservice.GetRMCookieDataResponseElement;

public class GetRMCookieDataTask extends Task {

    public static final String OPERATION_NAME="getRMCookieData";
    private GetRMCookieDataRequestElement requestElement;

	public GetRMCookieDataTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new GetRMCookieDataResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String profileId = (String) args.get("profileId");

        requestElement = new GetRMCookieDataRequestElement();
        requestElement.setProfileId(profileId);
	}

}
