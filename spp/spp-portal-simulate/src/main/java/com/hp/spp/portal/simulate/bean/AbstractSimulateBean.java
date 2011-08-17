package com.hp.spp.portal.simulate.bean;

import java.util.Map;

import javax.portlet.PortletRequest;

import com.hp.spp.portal.crypto.CryptoTools;
import com.hp.spp.portal.crypto.CryptoToolsImpl;
import com.hp.spp.profile.Constants;
import com.hp.spp.wsrp.context.ContextHelper;

public abstract class AbstractSimulateBean implements ISimulateBean {

	// ---------------------------------------------------- Protected Variables
	
	protected CryptoTools mCryptoTools = null ;

	protected String mErrorURLName = null ;
	protected String mProfileHPPId = null ;
	protected String mRedirectURLName = null ;
	protected String mSessionId = null ;
	protected String mSiteName = null ;
	protected String mServerScheme = null ;
	protected String mServerName = null ;
	protected String mServerPort = null ;
	
	// ---------------------------------------------- Public Constant Variables
	
	public static final String ENCRYPTIONSEPARATOR = "***" ;
	
	// ------------------------------------------- Protected Constant Variables
	
	protected static final String UTF8 = CryptoToolsImpl.UTF8 ;
	protected static final String SIMULATEPAGENAME = "STARTSIMULATION" ;

	// --------------------------------------------- Private Constant Variables
	
	private static final String SECRETKEY = "Pmjq3PcyPb+UyB8CE/2/eT5o6tz3Mj2/" ;

	private static final String HTTPS = "https" ;
	private static final String DOT = "." ;
	private static final String DOUBLEDOT = ":" ;
	private static final String SLASH = "/" ;
	private static final String DOUBLESLASH = SLASH.concat(SLASH) ;
	private static final String PORTAL = "portal" ;
	private static final String SITE = "site" ;
	private static final String TEMPLATE = "template" ;
	
	private static final String DEFAULTSECUREPORT = "443" ;
	private static final String EMPTY = "" ;
	
	// ------------------------------------------------------------ Construtors
	
	public AbstractSimulateBean() {
		init() ;
	}
	
	private void init() {
		mCryptoTools = new CryptoToolsImpl(SECRETKEY) ;
	}

	// --------------------------------------------------------- Public Methods

	/**
	 * Return the action URL wihout any parameter to use this bean to build a 
	 * form to send all parameters by a POST method. 
	 * @return return the action URL
	 */
	public String getActionURL() {
		return getServerAddress().concat(getInternalActionURL()) ;
	}

	/**
	 * Used to retrieve some information like <tt>ServerName</tt>, 
	 * <tt>SiteName</tt>, etc.
	 * @param request the <tt>request</tt> of the portlet
	 */
	public void setPortletRequest(PortletRequest request) {
		if(request == null)
			throw new NullPointerException(Constants.PROFILE_MAP.concat(" cannot be NULL!")) ;
		
		Map userProfile = getUserProfile(request) ;
		Map userContextKeys = getUserContextKeys(request) ;
		
		mSiteName = getSiteName(userProfile) ;
		mServerName = getServerName(userProfile) ;
		mServerPort = getServerPort(userContextKeys) ;
	}

	/**
	 * Used to be encrypted and pass to the simulation page.
	 * @param profileId the <tt>profileId</tt> of the <b>simulated</b> user
	 */
	public void setSimulatedProfileHPPId(String profileId) {
		mProfileHPPId = profileId ;
	}

	/**
	 * Used to be encrypted and pass to the simulation page.
	 * @param sessionToken the <tt>sessionToken</tt> of the <b>simulator</b> user
	 */
	public void setSessionId(String sessionToken) {
		mSessionId = sessionToken ;
	}

	/**
	 * Used to redirect on the <b>success</b> page at the end of the simulation.
	 * By default, if the value is not set, the succes page is the home page
	 * of the site.
	 * @param redirectURLName the <i>menuItem name</i> of the success page
	 */
	public void setRedirectURLName(String redirectURLName) {
		mRedirectURLName = redirectURLName ;
	}

	/**
	 * Used to redirect on the <b>error</b> page at the end of the simulation.
	 * @param errorURLName the <i>menuItem name</i> of the error page
	 */
	public void setErrorURLName(String errorURLName) {
		mErrorURLName = errorURLName ;
	}

	// ------------------------------------------------------ Protected Methods

	/**
	 * Return the full server address like 
	 * scheme://servername:serverport/
	 * @return return the full server address
	 */
	protected String getServerAddress() {
		String serverAddress = HTTPS.concat(DOUBLEDOT).concat(DOUBLESLASH).concat(mServerName) ;
		
		if(mServerPort != null && !DEFAULTSECUREPORT.equals(mServerPort))
			serverAddress = serverAddress.concat(DOUBLEDOT).concat(mServerPort) ;
		
		serverAddress = serverAddress.concat(SLASH) ;
		return serverAddress ;
	}
	
	/**
	 * Return the action URL wihout any parameter to use this bean to build a 
	 * form to send all parameters by a POST method. This internal method 
	 * return a relative URL without any <tt>Scheme</tt>, <tt>ServerName</tt> 
	 * or <tt>ServerPort</tt> values.
	 * @return return the action URL
	 */
	protected String getInternalActionURL() {
		return PORTAL.concat(SLASH).concat(SITE).concat(SLASH)
				.concat(mSiteName).concat(SLASH).concat(TEMPLATE)
				.concat(DOT).concat(SIMULATEPAGENAME);
	}

	// -------------------------------------------------------- Private Methods

	/**
	 * Return the <tt>userProfile</tt> from the <tt>portletRequest</tt>
	 * @param request the <tt>PortletRequest</tt> where the <tt>userProfile</tt> is stored
	 * @return return the <tt>userProfile</tt> from the <tt>portletRequest</tt>
	 */
	private Map getUserProfile(PortletRequest request) {
		Map userProfile = ContextHelper.getUserProfile(request);

		if(userProfile == null)
			throw new NullPointerException(Constants.PROFILE_MAP.concat(" cannot be NULL!")) ;
		
		return userProfile ;
	}

	/**
	 * Return the <tt>userContextKeys</tt> from the <tt>portletRequest</tt>
	 * @param request the <tt>PortletRequest</tt> where the <tt>userContextKeys</tt> is stored
	 * @return return the <tt>userContextKeys</tt> from the <tt>portletRequest</tt>
	 */
	private Map getUserContextKeys(PortletRequest request) {
		return ContextHelper.getUserContextKeys(request);
	}

	/**
	 * Return the <tt>siteName</tt> from the <tt>userProfile</tt>
	 * @param userProfile the <tt>Map</tt> where the <tt>siteName</tt> is stored
	 * @return return the <tt>siteName</tt> from the <tt>userProfile</tt>
	 */
	private String getSiteName(Map userProfile) {
		if(!userProfile.containsKey(Constants.MAP_SITE))
			throw new NullPointerException(Constants.MAP_SITE.concat(" cannot be NULL!")) ; 
		
		return (String) userProfile.get(Constants.MAP_SITE) ;
	}
	
	/**
	 * Return the <tt>serverName</tt> from the <tt>userProfile</tt>
	 * @param userProfile the <tt>Map</tt> where the <tt>serverName</tt> is stored
	 * @return return the <tt>serverName</tt> from the <tt>userProfile</tt>
	 */
	private String getServerName(Map userProfile) {
		if(!userProfile.containsKey(Constants.MAP_HOMEPAGE))
			throw new NullPointerException(Constants.MAP_HOMEPAGE.concat(" cannot be NULL!")) ;
		
		String serverName = (String) userProfile.get(Constants.MAP_HOMEPAGE) ;
		
		String pattern = DOUBLEDOT.concat(DOUBLESLASH);
		mServerScheme = serverName.substring(0, serverName.indexOf(pattern)) ;
		serverName = serverName.substring(serverName.indexOf(pattern) + pattern.length(), 
				serverName.indexOf(SLASH.concat(PORTAL))) ;
		
		if(serverName.indexOf(DOUBLEDOT) != -1) {
			mServerPort = serverName.substring(serverName.indexOf(DOUBLEDOT) + 1) ;
			serverName = serverName.substring(0, serverName.indexOf(DOUBLEDOT) + 1) ;
		}

		return serverName;
	}

	/**
	 * Return the <tt>serverPort</tt> from the <tt>userContextKeys</tt>
	 * @param userContextKeys the <tt>Map</tt> where the <tt>serverPort</tt> is stored
	 * @return return the <tt>serverPort</tt> from the <tt>userContextKeys</tt>
	 */
	private String getServerPort(Map userContextKeys) {
		String serverPort = (String) userContextKeys.get("HttpsPort") ;
		
		if(serverPort == null || EMPTY.equals(serverPort)) {
			if(HTTPS.equals(mServerScheme))
				serverPort = mServerPort ;
			else
				serverPort = DEFAULTSECUREPORT ;
		}
		
		return serverPort;
	}

	// --------------------------------------------- Abstract Protected Methods

	/**
	 * Return the encrypted value of the profileId and the sessionToken.
	 * @return return the encrypted value of the profileId and the sessionToken
	 * @throws SimulateBeanException throw if the encryption failed
	 */
	protected abstract String getEncryptedParameters() throws SimulateBeanException ;
	
	/**
	 * Return the relative URL of the <tt>Simulation Page</tt> with all 
	 * parameters concated in the <tt>QueryString</tt>. Used to call the 
	 * simulation page by a redirection process.
	 * @return return the full URL of the <tt>Simulation Page</tt>
	 * @throws SimulateBeanException throw if the URL's build failed
	 */
	protected abstract String getInternalURLWithParameters() throws SimulateBeanException ;
	
}
