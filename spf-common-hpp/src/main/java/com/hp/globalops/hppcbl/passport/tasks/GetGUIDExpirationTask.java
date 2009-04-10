package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.GetGUIDExpirationRequestElement;
import com.hp.globalops.hppcbl.webservice.GetGUIDExpirationResponseElement;

public class GetGUIDExpirationTask extends Task {

    public static final String OPERATION_NAME="getGUIDExpiration";
    private GetGUIDExpirationRequestElement requestElement;

	public GetGUIDExpirationTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new GetGUIDExpirationResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String guid = (String) args.get("guid");

        requestElement = new GetGUIDExpirationRequestElement();
        requestElement.setGuid(guid);
	}

}
