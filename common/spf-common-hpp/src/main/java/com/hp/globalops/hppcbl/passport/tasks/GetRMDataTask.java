package com.hp.globalops.hppcbl.passport.tasks;

import java.util.Map;

import com.hp.globalops.hppcbl.webservice.GetRMDataRequestElement;
import com.hp.globalops.hppcbl.webservice.GetRMDataResponseElement;

public class GetRMDataTask extends Task {

    public static final String OPERATION_NAME="getRMData";
    private GetRMDataRequestElement requestElement;

	public GetRMDataTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Object getRequestElement() throws TaskExecutionException {
		return requestElement;
	}

	protected Object getResponseElement() throws TaskExecutionException {
		return new GetRMDataResponseElement() ;
	}

	public void init(Map args) throws TaskExecutionException {
        //initialize
        super.init();
        String rMdataField = (String) args.get("rMdataField");

        requestElement = new GetRMDataRequestElement();
        requestElement.setRMdataField(rMdataField);
	}

}
