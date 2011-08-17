package com.hp.spp.portal.webconnector;

import com.vignette.portlet.buildingblock.webconnector.interpolator.RequestResponseInterpolator;
import com.vignette.portlet.buildingblock.webconnector.interpolator.RequestResponseContext;
import com.vignette.portlet.buildingblock.webconnector.url.ContainedURL;
import com.vignette.portal.rsm.RemoteSession;
import com.vignette.portal.rsm.RemoteResponse;
import com.vignette.portal.rsm.RemoteRequest;
import com.vignette.portal.rsm.http.HttpRemoteSession;
import com.vignette.portal.rsm.http.HttpRemoteRequest;
import com.vignette.portal.rsm.http.HttpRemoteResponse;
import com.vignette.portal.website.enduser.PortalContext;
import com.hp.spp.webservice.eservice.client.EServiceResponse;
import com.hp.spp.webservice.eservice.manager.SPPEServiceWSManager;
import com.hp.spp.profile.Constants;
import com.hp.spp.profile.ProfileHelper;
import com.hp.spp.common.util.DiagnosticContext;
import com.hp.spp.config.ConfigException;
import com.hp.spp.perf.TimeRecorder;
import com.hp.spp.perf.Operation;
import com.hp.spp.portal.TimeRecordingFilter;
import com.hp.spp.portal.diagnosticcontext.DiagnosticContextFilter;
import com.epicentric.portalbeans.beans.webconnectorbean.view.user.ProxyContentView;
import com.epicentric.portalbeans.beans.webconnectorbean.WebConnectorBean;
import com.epicentric.portalbeans.beans.webconnectorbean.session.RemoteSessionManager;
import com.epicentric.portalbeans.PortalBeanPersistificationException;
import com.epicentric.portalbeans.PortalPageContext;
import com.epicentric.common.website.MenuItemNode;
import com.epicentric.common.website.MenuItemUtils;
import org.apache.log4j.Logger;

import javax.xml.rpc.ServiceException;
import javax.servlet.http.HttpSession;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;
import java.rmi.RemoteException;

/**
 * This implementation of Vignette's <tt>RequestResponseInterpolator</tt> is used along with WebConnector
 * during HTML clipping to call E-service Manager and retireve the target URL and the parameters that
 * needs to be sent to the target e-service.
 */
public class EServiceInterpolator implements RequestResponseInterpolator {
	private static final Logger mLog = Logger.getLogger(EServiceInterpolator.class);

	private static final String SESSION_ACCESS_KEY = SessionAccess.class.getName();
	private static final String FIRST_REQUEST_KEY = EServiceInterpolator.class + ".FirstRequest";

	/**
	 * Optional metadata parameter name - if set to <tt>false</tt> e-service host:port won't be added
	 * to the list of contained URLs and therefore the URL rewriting will not occur.
	 */
	private static final String META_ADD_CONTAINED_URL = "AddContainedURL";

	/**
	 * Optional metadata parameter name - if specified the interpolator will track the request times
	 * and perform initial request if the remote session has expired. The value of the parameter contains
	 * the remote session timeout specified in minutes.
	 */
	private static final String META_SESSION_TIMEOUT_IN_MINS = "SessionTimeoutInMinutes";

	/**
	 * Prepares the request calling E-service Manager and retring the target URL and set of parameters
	 * used for the first request to the E-service.
	 * It has also a capability to track the target web site session. This happens but setting a metadata
	 * value {@link %META_SESSION_TIMEOUT_IN_MINS}. If this value is specified the interpolator will
	 * track the request times and invoke the initial request if the remote session expired since
	 * the last request.
	 * By default, the remote host and port are added to the contained URL set (they resource references
	 * referring them will be rewritten). This behavior can be changed by setting a meta parameter
	 * {@link #META_ADD_CONTAINED_URL} to <tt>false</tt>.
	 * @param requestResponseContext conext used between request and response handing calls.
	 * @param remoteSession remote session
	 * @param remoteRequest remote request
	 */
	public void processRequest(RequestResponseContext requestResponseContext,
							   RemoteSession remoteSession, RemoteRequest remoteRequest)
	{
		if (remoteRequest == null || !(remoteRequest instanceof HttpRemoteRequest) ||
			  remoteSession == null || !(remoteSession instanceof HttpRemoteSession))
		{
		   return;
		}
		HttpRemoteRequest request = (HttpRemoteRequest) remoteRequest;
		String siteName = null;
		String eServiceName = null;

		try {
			URL url = request.getUrl();
			PortalContext portalContext = getPortalContext(requestResponseContext);
			HttpSession session = portalContext.getPortalRequest().getSession();
			eServiceName = getEServiceName(portalContext);
			siteName = getSiteName(portalContext);
			long now = System.currentTimeMillis();
			SessionAccess sessionAccess =
					(SessionAccess) session.getAttribute(SESSION_ACCESS_KEY + "." + eServiceName);

			// if this is the first request we have to call ESM to get the request information
			if ("ESERVICE".equalsIgnoreCase(url.getHost()) ||
					(sessionAccess != null && sessionAccess.isExpired(now)))
			{
				Map metadata = getMetaData(requestResponseContext);

				// setup the request based on the information from ESM
				setupEserviceRequest(portalContext, siteName, eServiceName, request);

				// add the server to the contained URL list
				if (!"false".equals(metadata.get(META_ADD_CONTAINED_URL))) {
					addEserviceUrlAsContained(requestResponseContext, request);
				}

				// add SessionAccess object if SessionTimeout specified in metadata
				if (metadata.containsKey(META_SESSION_TIMEOUT_IN_MINS) &&
						(sessionAccess == null || sessionAccess.isExpired(now)))
				{
					sessionAccess = new SessionAccess(
							Integer.parseInt((String) metadata.get(META_SESSION_TIMEOUT_IN_MINS)) * 60 * 1000);
					// I don't set the sessionAccess object in the session yet - I'll do it below
					// when tracking the last access.
				}

				// save the fact that this is initial request for this Eservice so we can properly
				// handle response cookies
				portalContext.getPortalRequest().getRequest().setAttribute(
						FIRST_REQUEST_KEY, request.getUrl());
			}

			// if we track the remote session, update last access
			if (sessionAccess != null) {
				sessionAccess.setLastAccess(now);
				// reset the object in the session so if it's clustered it will get updated in
				// other cluster nodes
				session.setAttribute(SESSION_ACCESS_KEY + "." + eServiceName, sessionAccess);

				if (mLog.isDebugEnabled()) {
					mLog.debug("Tracking access to remote session: " + sessionAccess);
				}
			}

			TimeRecorder timeRecorder = (TimeRecorder) portalContext.getPortalRequest().getRequest().getAttribute(TimeRecordingFilter.TIME_RECORDER_REQUEST_KEY);
			if (timeRecorder != null) {
				timeRecorder.recordStart(Operation.WEB_CONNECTOR, request.getUrl());
			}
		}
		catch (Throwable e) {
			// I have to log the exception before rethrowing it as Vignette will log only a
			// useless generic message
			mLog.error(
					"Error occured for site " + (siteName != null ? "'" + siteName + "'" : "(site unknown)") +
					" and eService " + (eServiceName != null ? "'" + eServiceName + "'" : "(eService unknown)"),
					e);
			// rethrow exception to let Vignette know that something went wrong
			throw new RuntimeException("Error initializing remote session", e);
		}


		if (mLog.isDebugEnabled()) {
			debugRequest(request);
		}
	}

	/**
	 * Processes the response logging the error information if the error occured.
	 *
	 * @param requestResponseContext context used between request and response handling calls
	 * @param remoteSession remote session
	 * @param remoteResponse remote response
	 */
	public void processResponse(RequestResponseContext requestResponseContext,
								RemoteSession remoteSession, RemoteResponse remoteResponse)
	{
		if (remoteResponse == null || !(remoteResponse instanceof HttpRemoteResponse)) {
			return;
		}
		HttpRemoteResponse response = (HttpRemoteResponse) remoteResponse;
		// statusCode is < 0 if a timeout occurs.
		boolean timeoutOccured = response.getStatusCode() < 0;
		boolean httpErrorOccured = response.getStatusCode() >= 400;
		boolean errorOccured = timeoutOccured || httpErrorOccured;
		
		PortalContext portalContext = getPortalContext(requestResponseContext);
		DiagnosticContext diagnosticContext = (DiagnosticContext) portalContext.getPortalRequest().getRequest().getAttribute(DiagnosticContextFilter.DIAGNOSTIC_CONTEXT_REQUEST_KEY);

		if (errorOccured) {
		 	diagnosticContext.add("Webconnector Remote Server response error", response.getStatusMessage());
			mLog.error("Remote server responded with an error: " + response.getStatusCode() + " " + response.getStatusMessage());
		 	diagnosticContext.add("Webconnector Remote Server response header", convertHeaders(response).toString());
			mLog.error("Remote server response headers: " + convertHeaders(response));
		}
		else if(mLog.isDebugEnabled()) {
		 	diagnosticContext.add("Webconnector received response", response.getStatusMessage());
			mLog.debug("Received response: " + response.getStatusCode() + " " + response.getStatusMessage());
		 	diagnosticContext.add("Webconnector received response header", convertHeaders(response).toString());
			mLog.debug("Received response headers: " + convertHeaders(response));
		}

		TimeRecorder timeRecorder = (TimeRecorder) portalContext.getPortalRequest().getRequest().getAttribute(TimeRecordingFilter.TIME_RECORDER_REQUEST_KEY);
		if (timeRecorder != null) {
			if (errorOccured) {
				if (timeoutOccured) {
					timeRecorder.recordError(Operation.WEB_CONNECTOR, "Timeout occured");
				}
				else {
					timeRecorder.recordError(Operation.WEB_CONNECTOR, response.getStatusCode() + " " + response.getStatusMessage());
				}
			}
			else {
				timeRecorder.recordEnd(Operation.WEB_CONNECTOR);
			}
		}

		// If this is the first request it was done with a symbolic URL, like http://ESERVICE.
		// We have to save the response cookies in the remote session corresponding to the actual
		// eservice URL.
		URL eServiceURL =
				(URL) portalContext.getPortalRequest().getRequest().getAttribute(FIRST_REQUEST_KEY);
		// This eServiceURL is only set during the initial request
		if (eServiceURL != null) {
			// In the "processRequest" the RemoteSession parameter is a session associated with the
			// symbolic URL like http://ESERVICE. We replaced this URL with the actual URL of the
			// eservice. Now we have to save this session but for the URL of the eService.
			RemoteSessionManager.saveRemoteSession(
					(ProxyContentView) requestResponseContext.getContextAttribute("vap.portalbeanview"),
					eServiceURL,
					remoteSession);
		}
	}

	private PortalContext getPortalContext(RequestResponseContext requestResponseContext) {
		Object vapPageContextObj = requestResponseContext.getContextAttribute("vap.pagecontext");
		if (vapPageContextObj == null) {
			throw new IllegalStateException("vap.pagecontext attribute not found in RequestResponseContext");
		}
		if (!(vapPageContextObj instanceof PortalPageContext)) {
			throw new IllegalStateException("The attribute in RequestResponseContext bound to vap.pagecontext is not of type PortalPageContext: " + vapPageContextObj.getClass().getName());
		}
		PortalPageContext vapPageContext = (PortalPageContext) vapPageContextObj;
		return (PortalContext) vapPageContext.getRequest().getAttribute("portalContext");
	}

	/**
	 * Retrieves the metadata as map from the metadata string specified in the WebConnector portlet's
	 * interpolator configuration. The format of the meta data is as follows:
	 * <pre>
	 * name1=value1, name2=value2, name3=value3
	 * </pre>
	 * It's a comma-separated list of name-value pairs. Names become map keys and values - map values.
	 * @param requestResponseContext context containing metadata string
	 * @return map containing specified parameters or empty map if no metadata was specified.
	 */
	private Map getMetaData(RequestResponseContext requestResponseContext) {
		String metadata = (String) requestResponseContext.getContextAttribute(RequestResponseContext.ATTRIBUTE_KEY_INTERPOLATOR_META_DATA);
		if (metadata == null || metadata.trim().equals("")) {
			return Collections.EMPTY_MAP;
		}
		metadata = metadata.trim();
		Map result = new HashMap();
		String[] params = metadata.split("\\s*,\\s*");
		for (int i = 0, len = params.length; i < len; ++i) {
			String[] param = params[i].split("\\s*=\\s*");
			if (param != null && param.length == 2) {
				result.put(param[0].trim(), param[1].trim());
			}
		}
		if (mLog.isDebugEnabled()) {
			mLog.debug("Interpolator metadata: " + result);
		}
		return result;
	}

	private void addEserviceUrlAsContained(RequestResponseContext requestResponseContext, HttpRemoteRequest request) throws PortalBeanPersistificationException {
		ProxyContentView view = (ProxyContentView) requestResponseContext.getContextAttribute("vap.portalbeanview");
		WebConnectorBean bean = view.getMyBean();
		if (bean.getContainedURLSetting() != 0) {
			URL requestUrl = request.getUrl();
			String baseUrl =
					requestUrl.getProtocol() + "://" + requestUrl.getHost() +
					(requestUrl.getPort() != -1 ? ":" + requestUrl.getPort() : "");
			ContainedURL containedURL = bean.getContainedURL(baseUrl);
			// add url only if it's not there yet
			if (containedURL == null) {
				if (bean.getContainedURLSetting() != 2) {
					bean.setContainedURLSetting(2);
				}
				bean.addContainedURL(new ContainedURL(baseUrl));
				bean.save();
			}
		}
	}

	private void setupEserviceRequest(PortalContext portalContext, String siteName, String eServiceName, HttpRemoteRequest request) throws MalformedURLException, RemoteException, ConfigException, ServiceException {
		HashMap userContext = getUserContext(portalContext);
		if (mLog.isDebugEnabled()) {
			mLog.debug("Performing the session initialization request for site '" + siteName + "' and eService '" + eServiceName + "' and user context " + userContext);
		}
		EServiceResponse eServiceInfo = SPPEServiceWSManager.getEServiceResponse(siteName, eServiceName, userContext);
		if (eServiceInfo == null) {
			throw new IllegalStateException("Eservice not defined in ESM for site '" + siteName + "': " + eServiceName);
		}
		setupRequest(siteName, eServiceName, request, eServiceInfo);
	}

	private HashMap getUserContext(PortalContext portalContext) {
		HashMap profileMap = (HashMap) portalContext.getPortalRequest().getSession().getAttribute(Constants.PROFILE_MAP);
		return (HashMap) new ProfileHelper().toLegacyProfile(profileMap);
	}

	private String getSiteName(PortalContext portalContext) {
		return portalContext.getCurrentSite().getDNSName();
	}

	private String getEServiceName(PortalContext portalContext) {
		MenuItemNode menuItemNode = MenuItemUtils.getSelectedMenuItemNode(portalContext);
		String eServiceName = null;
		if (menuItemNode != null) {
			eServiceName = menuItemNode.getMenuItem().getTitle();
		}
		else {
			throw new IllegalStateException("Unable to retrieve eService name from menu item title!");
		}
		return eServiceName;
	}

	private void setupRequest(String siteName, String eServiceName, HttpRemoteRequest request, EServiceResponse eServiceInfo) {
		request.setRequestMethod(eServiceInfo.getMethod());
		try {
			request.setUrl(new URL(eServiceInfo.getUrl()));
		}
		catch (MalformedURLException e) {
			throw new IllegalArgumentException("The URL of the eService [" + eServiceName + "] declared in [" + siteName + "] is malformed: " + eServiceInfo.getUrl());
		}
		for (Iterator it = eServiceInfo.getParameters().entrySet().iterator(); it.hasNext(); ) {
			Map.Entry param = (Map.Entry) it.next();
			request.addBodyParameter((String) param.getKey(), (String) param.getValue());
		}
	}

	private void debugRequest(HttpRemoteRequest request) {
		mLog.debug("Sending request: " + request.getRequestMethod() + " " + request.getUrl());
		mLog.debug("Sending request headers: " + convertParamValuesToList(request.getRequestHeaders()));
		mLog.debug("Sending request cookies: " + request.getRequestCookies().getCookieRequestHeaderValue(request.getUrl()));
		mLog.debug("Sending parameters: " + convertParamValuesToList(request.getBodyParameters()));
	}

	private Map convertParamValuesToList(Map params) {
		Map result = new HashMap();
		for (Iterator it = params.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), Arrays.asList((String[]) entry.getValue()));
		}
		return result;
	}

	private Map convertHeaders(HttpRemoteResponse response) {
		Map headers = new HashMap();
		for (Iterator it = response.getHeaderNames().iterator(); it.hasNext(); ) {
			String name = (String) it.next();
			headers.put(name, Arrays.asList((String[]) response.getHeaderValues(name)));
		}
		return headers;
	}
}
