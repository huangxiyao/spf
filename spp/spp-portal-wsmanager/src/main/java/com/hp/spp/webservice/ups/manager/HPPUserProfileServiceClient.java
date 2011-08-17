package com.hp.spp.webservice.ups.manager;

import com.hp.spp.portal.common.site.SiteManager;
import com.hp.spp.config.ConfigException;
import com.hp.spp.config.Config;
import com.hp.spp.webservice.ups.client.GenericUPSFault;
import com.hp.spp.webservice.ups.client.UPSServiceRequest;
import com.hp.spp.webservice.ups.client.UPSServiceResponse;
import com.hp.spp.webservice.ups.client.ServiceIdentifier;
import com.hp.spp.webservice.ups.client.AttributeGroup;
import com.hp.spp.webservice.ups.client.RequestHeader;
import com.hp.spp.webservice.ups.client.UPSWebServicesService;
import com.hp.spp.webservice.ups.client.UPSWebServicesServiceLocator;
import com.hp.spp.webservice.ups.client.UPSWebServicesSoapBindingStub;
import com.hp.spp.webservice.ups.client.ResponseHeader;
import com.hp.spp.webservice.ups.client.Attribute;
import com.hp.spp.webservice.ups.client.AttributesRowList;
import com.hp.spp.webservice.ups.client.AttributesRow;
import com.hp.spp.webservice.ups.client.AbstractAttribute;
import com.hp.spp.webservice.ups.client.ConfiguredQueryServiceType;
import com.hp.spp.common.util.DiagnosticContext;
import com.hp.spp.perf.TimeRecorder;
import com.hp.spp.perf.Operation;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.util.Date;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import javax.xml.rpc.ServiceException;

public class HPPUserProfileServiceClient implements UserProfileService {

	private static final Logger mLog = Logger.getLogger(HPPUserProfileServiceClient.class);

	private final static String DEFAULT_LOCALE = "en_US";

	private final static String KEY_SEPARATOR = "";

	private final static String VALUE_SEPARATOR = "";

	private static final String BY_PASS_CACHE = "bypassCache";

	private static final String GET_PROFILE_DATA = "getprofiledata";

	private static final String USER_HEADER_ATTRIBUTES_GROUP = "UserHeaderAttributesGroup";

	private static final String CLIENT_ID = "SPP";

	private static final String DATE_FORMAT = "yyyy-MM-dd";

	//public static final String UPS_TIMEOUT="SPP.UPS.TimeoutMilliseconds";


	@SuppressWarnings("unchecked")
	public Integer getUserProfile(Map userProfile, String queryId, String userId, String hppId, String site,
								  String sessionToken, String adminSessionToken,
								  String region, String loginType)
	{
		// Construction of all values parameters
		Map allAttributeValues = new HashMap();
		allAttributeValues.put("UserId", userId);
		allAttributeValues.put("ProfileId", hppId);
		allAttributeValues.put("hppid", hppId);
		allAttributeValues.put("SiteName", site);
		allAttributeValues.put("SessionToken", sessionToken);
		allAttributeValues.put("AdminSessionToken", adminSessionToken);
		allAttributeValues.put("reg", region);
		allAttributeValues.put("loginType", loginType);

		// Get the list of attribute names for this query
		String configKey = "SPP." + queryId + ".AttributeNames";
		if (mLog.isDebugEnabled()) {
			mLog.debug("query id to UPS : [" + queryId + "]");
			mLog.debug("config key  for list of attribute names: [" + configKey
					+ "]");
			mLog.debug("hppid received : [" + hppId + "]");
		}
		List attributeNamesList;
		try {
			attributeNamesList = getQueryAttributeNames(queryId, configKey);
		} catch (ConfigException e) {
			mLog.error("Configuration exception for key [" + configKey + "] "
					+ e.getMessage(), e);
			return new Integer(0);
		}

		// Construct the table of attribute names
		String[] attributeNamesTab = (String[]) attributeNamesList.toArray(new String[0]);

		// Construct the table of attribute values
		if (mLog.isDebugEnabled()) {
			mLog.debug("content of the attributes sent to UPS :");
		}
		String[] attributeValuesTab = new String[attributeNamesTab.length];
		for (int i = 0; i < attributeNamesTab.length; i++) {
			attributeValuesTab[i] = (String) allAttributeValues
					.get(attributeNamesTab[i]);
			if (mLog.isDebugEnabled()) {
				mLog.debug("[" + attributeNamesTab[i] + "] : ["
						+ attributeValuesTab[i] + "]");
			}
		}

		// URL to call UPS
		String upsUrl;
		/*try {
			upsUrl = Config.getValue(mUPSURL);
		} catch (ConfigException e) {
			mLog.error("Configuration exception for [" + mUPSURL + "]", e);
			return new Integer(0);
		} */

		upsUrl = SiteManager.getInstance().getSite(site).getUPSUrl();
		if(upsUrl == null){
			mLog.error("UPS Url is not available for the site: "+site);
			return new Integer(0);
		}

		if (mLog.isDebugEnabled()) {
			mLog.debug("url called for UPS : [" + upsUrl + "]");
		}

		// Call of the WS
		 try{
			 getUserProfile(userProfile, queryId, attributeNamesTab, attributeValuesTab, userId, true, upsUrl);
		 }catch(GenericUPSFault gufe) {
			 	DiagnosticContext.getThreadInstance().add("UPS GenericUPSFault", gufe.getErrorCode() + " : "
						+ gufe.getErrorType() + "GenericUPSFault message : " + gufe.getErrorMessage());
				mLog.error("GenericUPSFault : " + gufe.getErrorCode() + " : "
						+ gufe.getErrorType());
				mLog.error("GenericUPSFault message :  " + gufe.getErrorMessage());
				return new Integer(4);
		 }catch(RemoteException e){
			 	DiagnosticContext.getThreadInstance().add("UPS GenericUPSFault", e.toString() + " : "
						+ e.getMessage());
				mLog.error("RemoteException : " + e.toString() + " : "
						+ e.getMessage(), e);
				return new Integer(4);
		 }
		// Error in UPS invocation
		if (userProfile == null || userProfile.size() == 0) {
			mLog.error("Empty map received from UPS for user [" + hppId + "]");
			return new Integer(0);
		}

		// no error
		return null;
	}

	/**
	 * Retrieve the attribute names of the UPS query.
	 *
	 * @param queryId
	 *            the UPS query
	 * @param configKey
	 *            the name of the config key
	 * @return the attribute names of the UPS query
	 * @throws ConfigException
	 */
	@SuppressWarnings("unchecked")
	private List getQueryAttributeNames(String queryId, String configKey)
			throws ConfigException {
		List attributeNames = new ArrayList();
		String attributeNamesLinked;

		attributeNamesLinked = Config.getValue(configKey);
		StringTokenizer tokenizer = new StringTokenizer(attributeNamesLinked,
				";");
		while (tokenizer.hasMoreTokens()) {
			attributeNames.add(tokenizer.nextToken());
		}
		return attributeNames;
	}


	/**
	 * Obtain the user profile
	 *
	 * @param userProfile
	 *            the map
	 * @param queryId
	 *            the query to UPS
	 * @param userId
	 *            id of the user
	 * @param isBypassCache
	 *            use of UPS cache or no
	 * @param upsUrl
	 *            url to call UPS web service
	 * @return the user profile
	 */
	Map getUserProfile(Map userProfile, String queryId,
							   String[] attributeNames, String[] attributeValues, String userId,
							   boolean isBypassCache, String upsUrl)throws GenericUPSFault,RemoteException  {
		return getUserProfile(userProfile, queryId, attributeNames,
				attributeValues, userId, DEFAULT_LOCALE, isBypassCache, upsUrl);
	}


	/**
	 * Obtain the user profile
	 *
	 * @param userProfile
	 *            the map
	 * @param queryId
	 *            the query to UPS
	 * @param userId
	 *            id of the user
	 * @param localeCode
	 *            the language locale for UPS
	 * @param isBypassCache
	 *            use of UPS cache or no
	 * @param upsUrl
	 *            url to call UPS web service
	 * @return the user profile
	 */
	private Map getUserProfile(Map userProfile, String queryId,
									 String[] attributeNames, String[] attributeValues, String userId,
									 String localeCode, boolean isBypassCache, String upsUrl)throws GenericUPSFault, RemoteException {

		UPSServiceRequest serviceRequest = getServiceRequest(queryId,
				attributeNames, attributeValues, userId, localeCode,
				isBypassCache);
		UPSServiceResponse serviceResponse;
		try {
			if (mLog.isDebugEnabled()) {
				mLog.debug("invokeService");
			}
			serviceResponse = invokeService(serviceRequest, upsUrl);
		} catch (ServiceException e) {
			DiagnosticContext.getThreadInstance().add("UPS ServiceException", e.getMessage());
			mLog.error("ServiceException : " + e.toString() + " : "
					+ e.getMessage(), e);
			return null;
		} catch (MalformedURLException e) {
			DiagnosticContext.getThreadInstance().add("UPS MalformedURLException", e.getMessage());
			mLog.error("MalformedURLException : " + e.toString() + " : "
					+ e.getMessage(), e);
			return null;
		} catch (ConfigException e) {
			DiagnosticContext.getThreadInstance().add("UPS ConfigException", e.getMessage());
			mLog.error("ConfigException : " + e.toString() + " : "
					+ e.getMessage(), e);
			return null;
		}
		if (mLog.isDebugEnabled()) {
			mLog.debug("checkResponseWithRequest");
		}
		if (checkResponseWithRequest(serviceRequest, serviceResponse)) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("processServiceResponse");
			}
		}
		processServiceResponse(userProfile, serviceResponse);

		return userProfile;
	}


	private UPSServiceRequest getServiceRequest(String queryId,
													   String[] attributeNames, String[] attributeValues, String userId,
													   String localeCode, boolean isBypassCache) {
		// retrieve all ServiceRequest parameters
		ServiceIdentifier serviceIdentifier = getServiceIdentifier(queryId,
				localeCode, isBypassCache);
		AttributeGroup[] attributeGroups = getAttributeGroups(attributeNames,
				attributeValues);
		RequestHeader requestHeader = getRequestHeader(userId);

		// build the ServiceRequest object
		UPSServiceRequest serviceRequest = new UPSServiceRequest();
		serviceRequest.setServiceIdentifier(serviceIdentifier);
		serviceRequest.setRequestHeader(requestHeader);
		serviceRequest.setAttributeGroups(attributeGroups);

		return serviceRequest;
	}

	private UPSServiceResponse invokeService(
			UPSServiceRequest serviceRequest, String upsUrl)
			throws GenericUPSFault, RemoteException, ServiceException,
			MalformedURLException, ConfigException {
		// Init the webservice
		UPSWebServicesService pws = new UPSWebServicesServiceLocator();
		UPSWebServicesSoapBindingStub binding = (UPSWebServicesSoapBindingStub)pws.getUPSWebServices(new URL(upsUrl));

		//Get the site name from the serviceRequest for getting the site specific time out setting
		//for the UPS web service call
		String siteName = getSiteName(serviceRequest);

		// Process the call
		//binding.setTimeout(Config.getIntValue(UPS_TIMEOUT+"."+siteName,Config.getIntValue(UPS_TIMEOUT, 60000)));
        binding.setTimeout(SiteManager.getInstance().getSite(siteName).getUPSTimeoutInMilliseconds());
        if(mLog.isDebugEnabled()){
			mLog.debug("Timeout after setting: "+binding.getTimeout());
		}

		TimeRecorder.getThreadInstance().recordStart(Operation.UPS_CALL);
		UPSServiceResponse uPSServiceResponse = null;
		try {
			uPSServiceResponse = binding.invokeService(serviceRequest);
			TimeRecorder.getThreadInstance().recordEnd(Operation.UPS_CALL);
		}
		catch (RemoteException e) {
			TimeRecorder.getThreadInstance().recordError(Operation.UPS_CALL, e);
			throw e;
		}

		return uPSServiceResponse;
	}


	private boolean checkResponseWithRequest(
			UPSServiceRequest serviceRequest, UPSServiceResponse serviceResponse) {
		RequestHeader requestHeader = serviceRequest.getRequestHeader();
		ResponseHeader responseHeader = serviceResponse.getResponseHeader();

		return requestHeader.getRequestDate().equals(
				responseHeader.getRequestDate())
				&& requestHeader.getRequestId().equals(
						responseHeader.getRequestId());
	}


	private Map processServiceResponse(Map userProfile,
											  UPSServiceResponse serviceResponse) {
		String prefix = "";

		AttributeGroup[] attributeGroups = serviceResponse.getAttributeGroups();
		AttributeGroup attributeGroup = null;
		for (int i = 0; i < attributeGroups.length; i++) {
			attributeGroup = attributeGroups[i];
			if (mLog.isDebugEnabled())
				mLog.debug("attributeGroup [" + i + "] : [" + attributeGroup
						+ "]");

			List attributes = arrayToList(attributeGroup.getAttributes());
			if (mLog.isDebugEnabled()) {
				mLog.debug("recurseOnAttributes");
			}
			recurseOnAttributes(userProfile, attributes, prefix);

			List attributesRowLists = arrayToList(attributeGroup
					.getAttributesList());
			recurseOnAttributesRowList(userProfile, attributesRowLists, prefix);
		}

		return userProfile;
	}

	private List arrayToList(Object[] o) {
		if (o == null) {
			return null;
		}

		// IMPORTANT:
		// When the UPS client was regenerated with Axis 1.4 (provided by VAP 7.3), the empty arrays
		// that previously (Axis 1.1) were returned as "null" now come as single element arrays
		// with the element value being null.
		// This method has been modified to add only non-null values to the resulting list. To keep
		// backward compatibility if the resulting array is empty, this method will return "null".

		List list = new ArrayList();
		for (int i = 0, len = o.length; i < len; ++i) {
			if (o[i] != null) {
				list.add(o[i]);
			}
		}

		if (list.isEmpty()) {
			return null;
		}
		else {
			return list;
		}
	}

	private void recurseOnAttributes(Map userProfile, List attributes,
											String prefix) {
		if (mLog.isDebugEnabled()) {
			mLog.debug("userProfile : " + userProfile);
			mLog.debug("attributes : " + attributes);
		}
		if (userProfile != null && attributes != null) {

			Attribute attribute = null;
			String attributeId = null;
			List attributeValues = null;

			Iterator iterator = attributes.iterator();
			while (iterator.hasNext()) {
				attribute = (Attribute) iterator.next();
				attributeId = prefix.concat(KEY_SEPARATOR).concat(
						attribute.getAttributeId());
				attributeValues = arrayToList(attribute.getAttributeValues());
				Object value = recurseOnAttributeValues(attributeValues);
				if (value == null) {
					value = "";
				}
				if (mLog.isDebugEnabled()) {
					mLog.debug("attributeId : " + attributeId);
					mLog.debug("value : " + value);
				}
				userProfile.put(attributeId, value);
			}
		}
	}

	private void recurseOnAttributesRowList(Map userProfile,
												   List attributesRowLists, String prefix) {
		if (userProfile != null && attributesRowLists != null) {
			Iterator iterator = attributesRowLists.iterator();

			AttributesRowList attributesRowList = null;
			String attributesRowListId = null;

			while (iterator.hasNext()) {
				attributesRowList = (AttributesRowList) iterator.next();
				attributesRowListId = attributesRowList.getAttributeId();
				if (attributesRowListId != null) {
					List attributesRows = arrayToList(attributesRowList
							.getRows());
					recurseOnAttributesRows(userProfile, attributesRows, prefix
							.concat(KEY_SEPARATOR).concat(attributesRowListId));
				}
			}
		}
	}

	private Object recurseOnAttributeValues(List attributeValues) {
		String values = null;
		boolean isMultiple = false;

		if (attributeValues != null) {
			Iterator iterator = attributeValues.iterator();

			while (iterator.hasNext()) {
				if (!isMultiple) {
					if (values != null) {
						isMultiple = !isMultiple;
					}
					else {
						values = "";
					}
				}

				values = values.concat(VALUE_SEPARATOR).concat(
						(String) iterator.next());
			}
		}

		return values;
	}

	private void recurseOnAttributesRows(Map userProfile,
												List attributesRows, String prefix) {
		if (userProfile != null && attributesRows != null) {
			Iterator iterator = attributesRows.iterator();

			AttributesRow attributesRow = null;
			String attributeId = null;

			while (iterator.hasNext()) {
				attributesRow = (AttributesRow) iterator.next();
				attributeId = attributesRow.getAttributeId();

				AbstractAttribute[] abstractAttributes = attributesRow
						.getAttributes();
				if (abstractAttributes instanceof AttributesRowList[]) {
					recurseOnAttributesRowList(userProfile,
							arrayToList(abstractAttributes), prefix.concat(
									KEY_SEPARATOR).concat(attributeId));
				}
				else if (abstractAttributes instanceof Attribute[]) {
					recurseOnAttributes(userProfile,
							arrayToList(abstractAttributes), prefix.concat(
									KEY_SEPARATOR).concat(attributeId));
				}
			}
		}
	}

	private ServiceIdentifier getServiceIdentifier(String queryId,
														  String localeCode, boolean isBypassCache) {
		String operation = null;

		if (isBypassCache) {
			operation = BY_PASS_CACHE;
		}
		else {
			operation = GET_PROFILE_DATA;
		}

		// Init IServiceType
		ConfiguredQueryServiceType serviceType = new ConfiguredQueryServiceType();
		serviceType.setConfiguredQueryId(queryId);
		serviceType.setOperation(operation); // String: getprofiledata or
												// bypassCache
		// serviceType.setRevisionVersion(revisionVersion) ; // String: ???
		// serviceType.setTransactionId(transactionId) ; // String: ???

		// Init ServiceIdentifier
		ServiceIdentifier serviceIdentifier = new ServiceIdentifier();
		serviceIdentifier.setLocale(localeCode);
		serviceIdentifier.setServiceType(serviceType);

		return serviceIdentifier;
	}

	AttributeGroup[] getAttributeGroups(String[] attributeNames,
											   String[] attributeValues) {
		String attributeGroupName = USER_HEADER_ATTRIBUTES_GROUP;

		Attribute[] attributes = new Attribute[attributeNames.length];
		for (int i = 0; i < attributeNames.length; i++) {
			attributes[i] = getAttribute(attributeNames[i], attributeValues[i]);
		}

		// Init AttributeGroups
		AttributeGroup[] attributeGroups = null;
		attributeGroups = new AttributeGroup[1];
		attributeGroups[0] = new AttributeGroup();
		attributeGroups[0].setAttributeGroupName(attributeGroupName);
		attributeGroups[0].setAttributes(attributes);

		return attributeGroups;
	}

	private RequestHeader getRequestHeader(String userId) {
		String clientId = CLIENT_ID;

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String requestDate = sdf.format(new Date());

		String requestId = userId + Long.toString(System.currentTimeMillis());

		// Init RequestHeader
		RequestHeader requestHeader = new RequestHeader();
		requestHeader.setClientId(clientId); // String: SPP
		requestHeader.setRequestDate(requestDate); // String: current date
		requestHeader.setRequestId(requestId); // String: userId + current date
												// in ms

		return requestHeader;
	}

	private String getSiteName(UPSServiceRequest serviceRequest) {
		String siteName = "";
		AttributeGroup attrGroups = serviceRequest.getAttributeGroups(0);
		Attribute[] attr = attrGroups.getAttributes();
		int attrSize = attr.length;
		for(int i = 0; i< attrSize ; ++i){
			if("SiteName".equalsIgnoreCase(attr[i].getAttributeId())){
				siteName = attr[i].getAttributeValues(0);
				break;
			}
		}
	return siteName;

	}

	private Attribute getAttribute(String key, String value) {
		String attributeId = key;
		String[] attributeValues = new String[1];
		attributeValues[0] = value;

		Attribute attribute = new Attribute();
		attribute.setAttributeId(attributeId);
		attribute.setAttributeValues(attributeValues);

		return attribute;
	}

}
