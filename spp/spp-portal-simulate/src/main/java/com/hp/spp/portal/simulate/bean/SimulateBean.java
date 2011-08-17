package com.hp.spp.portal.simulate.bean;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.hp.spp.portal.crypto.CryptoToolsException;

public class SimulateBean extends AbstractSimulateBean {
	
	// ------------------------------------------------------ Private Variables
	
	private Map mParamMap = null ;
	private String mHash = null ;
	private String mEncrypt = null ;

	// --------------------------------------------- Private Constant Variables
	
	private static final String AMP_CHAR = "&" ;
	private static final String ASK_CHAR = "?" ;
	private static final String EQUAL_CHAR = "=" ;

	// --------------------------------------------------------- Public Methods

	/**
	 * Return a <tt>Map</tt> with all couple key/value to create the hidden
	 * fields if the bean is used to build a form.
	 * @return return the <tt>Map</tt> with all possible hidden fields
	 * @throws SimulateBeanException throw if the Map's build failed
	 */
	public Map getParamMap() throws SimulateBeanException {
		if(mParamMap == null) {
			mParamMap = new HashMap() ;
		}
		
		mParamMap.put(SPP_SiteName, mSiteName) ;
		mParamMap.put(SPP_SimulationKey, getEncryptedParameters()) ;
		mParamMap.put(SPP_RedirectUrl, mRedirectURLName) ;
		mParamMap.put(SPP_ErrorUrl, mErrorURLName) ;
			
		return Collections.unmodifiableMap(mParamMap);
	}

	/**
	 * Return the full URL of the <tt>Simulation Page</tt> with all parameters
	 * concated in the <tt>QueryString</tt>. Used to call the simulation page
	 * by a redirection process.
	 * @return return the full URL of the <tt>Simulation Page</tt>
	 * @throws SimulateBeanException throw if the URL's build failed
	 */
	public String getFullURLWithParameters() throws SimulateBeanException {
		return getServerAddress().concat(getInternalURLWithParameters());
	}

	// ------------------------------------------------------ Protected Methods

	/**
	 * Return the encrypted value of the profileId and the sessionToken.
	 * @return return the encrypted value of the profileId and the sessionToken
	 * @throws SimulateBeanException throw if the encryption failed
	 */
	protected String getEncryptedParameters() throws SimulateBeanException {
		String value = ENCRYPTIONSEPARATOR.concat(mProfileHPPId)
						.concat(ENCRYPTIONSEPARATOR).concat(mSessionId)
						.concat(ENCRYPTIONSEPARATOR);
		
		try {
			if(mHash == null || mCryptoTools.verify(value, mHash))
				mEncrypt = mCryptoTools.encrypt(value) ;
		} catch (CryptoToolsException e) {
			throw new SimulateBeanException(e);
		}
		
		return mEncrypt;
	}

	/**
	 * Return the relative URL of the <tt>Simulation Page</tt> with all 
	 * parameters concated in the <tt>QueryString</tt>. Used to call the 
	 * simulation page by a redirection process.
	 * @return return the full URL of the <tt>Simulation Page</tt>
	 * @throws SimulateBeanException throw if the URL's build failed
	 */
	protected String getInternalURLWithParameters() throws SimulateBeanException {
		String url = getInternalActionURL() ;
		return addParams(url);
	}

	// -------------------------------------------------------- Private Methods

	/**
	 * Add all parameters from the parameters <tt>Map</tt> in the 
	 * <tt>QueryString</tt> of the URL passed to this method.
	 * @param url The URL where the parameters will be added
	 * @return return the original URL with the <tt>QueryString</tt>
	 * @throws SimulateBeanException throw if the <tt>Map</tt> cannot be retrieved
	 */
	private String addParams(String url) throws SimulateBeanException {
		// Test if the url have already a querystring or not
		boolean isFirstParam = (url.indexOf(ASK_CHAR) == -1) ;
		
		for (Iterator iterator = getParamMap().entrySet().iterator(); iterator.hasNext(); ) {
			Map.Entry param = (Map.Entry) iterator.next();
			String key = (String) param.getKey() ;
			String value = (String) param.getValue() ;
			
			if(key != null && value != null) {
				if(isFirstParam) {
					url = url.concat(ASK_CHAR) ;
					isFirstParam = false ;
				} else
					url = url.concat(AMP_CHAR) ;
				
				url = url.concat(key).concat(EQUAL_CHAR).concat(urlEncoder(value)) ;
			}
		}
		return url;
	}
	
	/**
	 * Return the URLEncoded value of the parameter
	 * @param value the parameter to be URLEncoded
	 * @return return the UREncoded value of the parameter
	 * @throws SimulateBeanException throw if a <tt>UnsupportedEncodingException</tt> is catched
	 */
	private String urlEncoder(String value) throws SimulateBeanException {
		try {
			return URLEncoder.encode(value, UTF8) ;
		} catch (UnsupportedEncodingException e) {
			throw new SimulateBeanException(e) ;
		}
	}

}
