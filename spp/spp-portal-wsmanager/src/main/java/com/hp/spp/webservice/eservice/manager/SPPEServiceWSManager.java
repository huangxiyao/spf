package com.hp.spp.webservice.eservice.manager;

import com.hp.spp.config.Config;
import com.hp.spp.config.ConfigException;
import com.hp.spp.perf.Operation;
import com.hp.spp.perf.TimeRecorder;
import com.hp.spp.portal.common.site.SiteManager;
import com.hp.spp.webservice.eservice.client.*;
import org.apache.log4j.Logger;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;

public class SPPEServiceWSManager {

	private static final Logger mLog = Logger.getLogger(SPPEServiceWSManager.class);

	public static final String mEserviceURL = "SPP.eservice.url";

	public static final String ESM_TIMEOUT="SPP.ESM.TimeoutMilliseconds";

	public static EServiceResponse getEServiceResponse(String siteName, String eServiceName,
													   HashMap userContext)
			throws MalformedURLException, RemoteException, ConfigException, ServiceException
	{
		EServiceResponse serviceResponse = null;
			EserviceRequest serviceRequest = getEserviceRequest(eServiceName, null, siteName,
					null, null, userContext);
			serviceResponse = invokeService(serviceRequest);

		return serviceResponse;
	}

	public static EServiceResponse getEServiceResponse(String siteName, String eServiceName,
													   HashMap userContext, HashMap httpRequestParameters, String urlProdFromRequest,
													   String urlTestFromRequest) throws RemoteException, MalformedURLException,
			ServiceException, ConfigException {
		EServiceResponse serviceResponse = null;

		EserviceRequest serviceRequest = getEserviceRequest(eServiceName,
				httpRequestParameters, siteName, urlProdFromRequest, urlTestFromRequest,
				userContext);
		serviceResponse = invokeService(serviceRequest);

		return serviceResponse;
	}

	public static EServiceResponse getEServiceResponse(EserviceRequest serviceRequest)
			throws RemoteException, MalformedURLException, ServiceException, ConfigException {
		EServiceResponse serviceResponse = null;
		serviceResponse = invokeService(serviceRequest);

		return serviceResponse;
	}

	private static EserviceRequest getEserviceRequest(String eServiceName,
													  HashMap httpRequestParameters, String siteName, String urlProdFromRequest,
													  String urlTestFromRequest, HashMap userContext) {
		EserviceRequest serviceRequest = new EserviceRequest();

		if (eServiceName != null)
			serviceRequest.setEServiceName(eServiceName);
		if (httpRequestParameters != null)
			serviceRequest.setHttpRequestParameters(httpRequestParameters);
		if (siteName != null)
			serviceRequest.setSiteName(siteName);
		if (urlProdFromRequest != null)
			serviceRequest.setUrlProdFromRequest(urlProdFromRequest);
		if (urlTestFromRequest != null)
			serviceRequest.setUrlTestFromRequest(urlTestFromRequest);
		if (userContext != null)
			serviceRequest.setUserContext(userContext);

		return serviceRequest;
	}

	private static EServiceResponse invokeService(EserviceRequest serviceRequest)
			throws ServiceException, RemoteException, MalformedURLException, ConfigException {
		String siteName = serviceRequest.getSiteName();
		EServiceManagerWSService esms = new EServiceManagerWSServiceLocator();
		String eserviceURL = Config.getValue(mEserviceURL);
		EServiceManagerSoapBindingStub binding = (EServiceManagerSoapBindingStub) esms.getEServiceManager(new URL(eserviceURL));

		String timeOutKeyName =  siteName == null?ESM_TIMEOUT:ESM_TIMEOUT+"."+siteName;

		//binding.setTimeout(Config.getIntValue(timeOutKeyName,Config.getIntValue(ESM_TIMEOUT, 60000)));
        binding.setTimeout(SiteManager.getInstance().getSite(siteName).getESMTimeoutInMilliseconds());
        
        if(mLog.isDebugEnabled()){
			mLog.debug("Timeout after setting: "+binding.getTimeout());
		}
		EServiceResponse serviceResponse = null;
		TimeRecorder.getThreadInstance().recordStart(Operation.ESM_CALL);
		try {
			serviceResponse = binding.getEserviceInformation(serviceRequest);
			TimeRecorder.getThreadInstance().recordEnd(Operation.ESM_CALL);
		}
		catch (RemoteException e) {
			TimeRecorder.getThreadInstance().recordError(Operation.ESM_CALL, e);
			throw e;
		}
		return serviceResponse;
	}
}
