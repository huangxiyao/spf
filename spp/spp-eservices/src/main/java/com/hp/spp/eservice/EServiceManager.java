package com.hp.spp.eservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.el.ELException;

import org.apache.log4j.Logger;

import com.hp.spp.common.util.SppConstants;
import com.hp.spp.eservice.el.ExpressionResolver;

/**
 * @author ageymond
 * 
 * Class that manages Eservice to retrieve url, method and parameter
 */
public class EServiceManager {

	private static Logger logger = Logger.getLogger(EServiceManager.class);

	/**
	 * Current EService that is used by the manager
	 */
	private EService mEService = null;

	/**
	 * ExpressionResolver that is uesd to parse Eservice parameter's expression
	 */
	private ExpressionResolver mExpressionResolver = null;

	/**
	 * Parameters coming from the HTTP Request used for deep links
	 */
	private Map mHttpRequestParameters = null;

	/**
	 * Expression that is uesd to retrieve the current platform from the user profile
	 * (mExpressionResolver)
	 */
	public static final String ENVIRONMENT_KEY = "${environmentPlatform}";

	/**
	 * Constant that is used to compare the running platform in order to retrieve the
	 * appropriate url
	 */
	private static final String PRODUCTION_PLATFORM = "PROD";

	/**
	 * The logger to send the emails alerts
	 */
	private static Logger emailLogger = Logger.getLogger(SppConstants.SPP_EMAIL_LOG_NAME);

	/**
	 * Default constructor that can be instantiate with a valid Eservice
	 * 
	 * @param pEservice
	 */
	public EServiceManager(EService pEservice) {
		mEService = pEservice;
	}

	/**
	 * Method that return the url of the Eservice : including test on platform and expression
	 * parsing.
	 * 
	 * @param urlProdFromRequest is the url coming from deeplinking for production.
	 * @param urlTestFromRequest is the url coming from deeplinking for test.
	 * @return the url of the Eservice.
	 * @throws ELException
	 */
	public String getEserviceUrl(String urlProdFromRequest, String urlTestFromRequest) {

		String url = mEService.getTestUrl();
		String urlFromRequest = urlTestFromRequest;

		// GET the exexcution Platform
		String executionPlatform = "TEST";
		try {
			executionPlatform = (String) mExpressionResolver.eval(ENVIRONMENT_KEY,
					String.class);
			logger.debug("Execution platform is :" + executionPlatform);
		} catch (ELException e) {
			logger.error("Error during environment platform evaluation ", e);
		}

		// use production url if execution environement is production
		if (executionPlatform.equalsIgnoreCase(PRODUCTION_PLATFORM)) {
			url = mEService.getProductionUrl();
			urlFromRequest = urlProdFromRequest;
		}

		if (url.indexOf("$") == -1 && urlFromRequest != null) {
			url = urlFromRequest;
		} else {
			// evaluate expression
			try {
				url = (String) mExpressionResolver.eval(url, String.class);
			} catch (ELException elx) {
				logger.error("Error during url expression evaluation", elx);
			}
		}
		return url;
	}

	/**
	 * Method that returns the whole set of parameter of the Eservice : including
	 * standardParameterSet parameters, specific parameters and expression parsing.
	 * 
	 * @param userContext is the map that represent the current User Profile.
	 * @return the map of all parameters of the Eservice.
	 */
	public Map getEserviceParameters() {

		Map parameters = new HashMap();
		String parameterName = null;
		Parameter currentParameter = null;
		Iterator iterator = null;

		// Add Standard Parameters
		if (mEService.getStandardParameterSet() != null
				&& mEService.getStandardParameterSet().getParameterList() != null) {
			iterator = mEService.getStandardParameterSet().getParameterList().iterator();
			while (iterator.hasNext()) {
				currentParameter = (Parameter) iterator.next();
				try {
					parameters.put(currentParameter.getName(), currentParameter
							.getParameterValue(mExpressionResolver));
				} catch (ELException elx) {
					logger.error("Error during parameter expression evaluation", elx);
					emailLogger.error("Exception during EService execution [" + elx
							+ "] for the Eservice[" + mEService.getName() + "] of the site ["
							+ mEService.getSite().getName() + "]");
				}
			}
		}

		// Add Specific Parameters
		if (mEService.getParameterList() != null) {
			iterator = mEService.getParameterList().iterator();
			while (iterator.hasNext()) {
				currentParameter = (Parameter) iterator.next();
				try {
					parameters.put(currentParameter.getName(), currentParameter
							.getParameterValue(mExpressionResolver));
				} catch (ELException elx) {
					logger.error("Error during parameter expression evaluation", elx);
					emailLogger.error("Exception during EService execution [" + elx
							+ "] for the Eservice[" + mEService.getName() + "] of the site ["
							+ mEService.getSite().getName() + "]");
				}
			}
		}

		// Add parameter coming from the request (Deep Link)
		if (mHttpRequestParameters != null) {
			Iterator keyIter = mHttpRequestParameters.keySet().iterator();

			while (keyIter.hasNext()) {
				parameterName = (String) keyIter.next();
				// check if it is already in Specific Parameters

				if (isDeeplinkOk(mEService.getParameterList(),parameterName)
						&& (mEService.getStandardParameterSet() == null || isDeeplinkOk(mEService
								.getStandardParameterSet().getParameterList(),parameterName))) {
					parameters.put(parameterName, mHttpRequestParameters.get(parameterName));
				}

			}
		}

		return parameters;
	}

	private boolean isDeeplinkOk(Set currentSet,String parameterName) {
		boolean okForDeepLink = true;
		if (currentSet != null) {
			Iterator iterator = currentSet.iterator();
			Parameter currentParameter = null;
			while (okForDeepLink && iterator.hasNext()) {
				currentParameter = (Parameter) iterator.next();
				if (currentParameter.getName().equals(parameterName)&& currentParameter.containsDynamicExpression()) {
					logger.info("Reject parameter "+parameterName+" from Deep Link because it contains a dynamic expression");
					okForDeepLink = false;
				}
			}
		}
		return okForDeepLink;
	}

	public String getMethod() {
		return mEService.getMethod();
	}

	public long getSecurityMode() {
		return mEService.getSecurityMode();
	}

	public long getIsNewWindow() {
		return mEService.getNavigationMode();
	}

	public ExpressionResolver getExpressionResolver() {
		return mExpressionResolver;
	}

	public void setExpressionResolver(ExpressionResolver expressionResolver) {
		mExpressionResolver = expressionResolver;
	}

	public Map getHttpRequestParameters() {
		return mHttpRequestParameters;
	}

	public void setHttpRequestParameters(Map httpRequestParameters) {
		mHttpRequestParameters = httpRequestParameters;
	}

	public long getSimulationMode(){
		return mEService.getSimulationMode();
	}
}
