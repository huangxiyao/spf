package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.GetUserIdListRequestElement;
import com.hp.globalops.hppcbl.webservice.GetUserIdListResponseElement;

public class GetUserIdListTask extends Task {

    public static final String OPERATION_NAME="getUserIdList";
    private GetUserIdListRequestElement requestElement;

	public GetUserIdListTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new GetUserIdListResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String[] profileId = (String[]) args.get("ProfileId");
        String[] applicationRefId = (String[]) args.get("ApplicationRefId");

        requestElement = new GetUserIdListRequestElement();
        if(profileId != null)
        	requestElement.setProfileId(profileId);
        else
        	requestElement.setApplicationRefId(applicationRefId);

	}

}
