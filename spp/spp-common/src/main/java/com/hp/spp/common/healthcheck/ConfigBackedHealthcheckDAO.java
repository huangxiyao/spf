package com.hp.spp.common.healthcheck;

import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.ArrayList;
import java.net.InetAddress;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;

import org.apache.log4j.Logger;

import com.hp.spp.config.Config;
import com.hp.spp.config.ConfigEntry;
import com.hp.spp.config.ConfigException;
import com.hp.spp.common.util.Environment;

public class ConfigBackedHealthcheckDAO implements HealthcheckDAO {
	
	private static final Logger mLog = Logger.getLogger(ConfigBackedHealthcheckDAO.class);
	public static final String CONFIG_KEY_OUT_OF_ROTATION_SERVERS = "SPP.Healthcheck.shutdownServers";
	public static final String CONFIG_KEY_ALL_WEB_SERVERS = "SPP.Healthcheck.allWSNames";
	public static final String CONFIG_KEY_PORTAL_HEALTHCHECK_URL = "SPP.Healthcheck.portalURL";
	public static final String CONFIG_KEY_WSRP_HEALTHCHECK_URL = "SPP.Healthcheck.wsrpURL";
	public static final String CONFIG_KEY_PORTAL_HEALTHCHECK_STRING = "SPP.Healthcheck.portalString";
	public static final String CONFIG_KEY_WSRP_HEALTHCHECK_STRING = "SPP.Healthcheck.wsrpString";
	public static final String CONFIG_KEY_CONNECTION_TIMEOUT = "SPP.Healthcheck.connectionTimeout";
	public static final String CONFIG_KEY_DOMAIN_NAME = "SPP.Healthcheck.domainName";
		
	private Set<String> configVariableAsSet(String variableName) {
		String configValue = Config.getValue(variableName, null);
		if (configValue == null) {
			return new TreeSet<String>();
		}
		return new TreeSet<String>(Arrays.asList(configValue.split(",")));
	}
	
	private String setAsConfigVariableValue(Set<String> set) {
		StringBuilder sb = new StringBuilder();
		for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
			String s = it.next();
			sb.append(s);
			if (it.hasNext()) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
// Save a new/updated shutdown server listing	
	
	public void updateOutOfRotationFlag(List<HealthcheckServerInfo> listServerInfo) {

		if (mLog.isDebugEnabled()) {
			mLog.debug("ConfigBackedHealthcheckInfo saveServers calls");
		}
		try {
			// Collect server names need to be put into the shutdown server listing
			Set<String> outOfRotationServers = configVariableAsSet(CONFIG_KEY_OUT_OF_ROTATION_SERVERS);
			for (HealthcheckServerInfo serverInfo : listServerInfo) {
				if (serverInfo.getOutOfRotation()) {
					outOfRotationServers.add(serverInfo.getServerName());
				}
				else {
					outOfRotationServers.remove(serverInfo.getServerName());
				}
			}

			String value = setAsConfigVariableValue(outOfRotationServers);
			ConfigEntry configEntry = 
				new ConfigEntry(CONFIG_KEY_OUT_OF_ROTATION_SERVERS, value, ConfigEntry.TYPE_STRING, false, false, null); 
			
		    // Save the list of shutdown servers into DB spp_config_entry

			if (mLog.isDebugEnabled()) {
				mLog.debug("ConfigBackedHealthcheckInfo: Save the new shutdown listing (" + configEntry + ")");
			}
			Config.set(configEntry);	
		} catch (ConfigException e) {
			throw new IllegalStateException("Error saving Layer 7 health check configuration", e);
		}
			
	}

// Method to return a list of HealthcheckInfo objects for all servers
	
	public List<HealthcheckServerInfo> getAllServers() {
		if (mLog.isDebugEnabled()) {
			mLog.debug("ConfigBackedHealthcheckInfo getAllServers calls");
		}
		
		HealthcheckServerInfo queryExampleInfo = new HealthcheckServerInfo(null, null, null, null);
		return findServersByExample(queryExampleInfo);
	}	

// Method to return a list of servers filtered by a input HealthcheckInfo example object	
	
	public List<HealthcheckServerInfo> findServersByExample(HealthcheckServerInfo infoExample) {
		List<HealthcheckServerInfo> allserverInfo = new ArrayList<HealthcheckServerInfo>();
		
		if (mLog.isDebugEnabled()) {
			mLog.debug("ConfigBackedHealthcheckInfo findServersByExample calls");
		}

		// get all server information from Config object and parse it
		try {
			String allServerNames = Config.getValue(CONFIG_KEY_ALL_WEB_SERVERS);
			Set<String> outOfRotationServers = configVariableAsSet(CONFIG_KEY_OUT_OF_ROTATION_SERVERS);
			
			Set<String> serverList = new TreeSet<String>(Arrays.asList(allServerNames.split(";")));
			for (String serverInfoStr : serverList) {
				String[] serverDetails = serverInfoStr.split(",");
				if (serverDetails.length < 4) {
					throw new IllegalStateException("Layer 7 health check server details are incorrect: " + serverInfoStr);
				}
				HealthcheckServerInfo currentServer = new HealthcheckServerInfo(
						serverDetails[0], serverDetails[1], serverDetails[2], serverDetails[3]);
				if (outOfRotationServers.contains(currentServer.getServerName())) {
					currentServer.setOutOfRotation(true);
				}
				else {
					currentServer.setOutOfRotation(false);
				}
			   // Add the currentServerInfo into the list to be returned
			   if (compareHealthcheckInfo(infoExample, currentServer)) {
				   allserverInfo.add(currentServer);
			   }
			}
			
			return allserverInfo;
		} catch (ConfigException e) {
			throw new IllegalStateException("Error querying Layer 7 health check configuration", e);
		}
		
	}
	
// A method to compare two HealthcheckInfo objects
	
	private boolean compareHealthcheckInfo(HealthcheckServerInfo infoExample, HealthcheckServerInfo infoSource) {
		
		if (mLog.isDebugEnabled()) {
			mLog.debug("ConfigBackedHealthcheckInfo compareHealthcheckInfo(" + infoExample + ", " + infoSource + ")");
		}

		if ((infoExample.getServerName() != null) && (!infoExample.getServerName().equals(infoSource.getServerName()))) {
			return false;
		}
		if ((infoExample.getApplicationName() != null) && (!infoExample.getApplicationName().equals(infoSource.getApplicationName()))) {
			return false;
		}
		if ((infoExample.getServerType() != null) && (!infoExample.getServerType().equals(infoSource.getServerType()))) {
			return false;
		}
		if ((infoExample.getSiteName() != null) && (!infoExample.getSiteName().equals(infoSource.getSiteName()))) {
			return false;
		}

		return true;		
	}

// A method to access the test page URL and check the output is correct. Returns the result of testing.

	public String testPageURL(String currentServerName, String currentURL) {

		String healthcheckString;
		InputStream in = null;
		HttpURLConnection con = null;
		URL testURL = null;

		if (mLog.isDebugEnabled()) {
			mLog.debug("ConfigBackedHealthcheckInfo testPageURL(" + currentServerName + ", " + currentURL + ")");
		}

		try {
			// Invoke the test page URL
			int currentServerPort = Environment.singletonInstance.getManagedServerPort();
			String currentServerNameURL;
			
			if ("UNKNOWN".equals(currentServerName)) {
				mLog.warn("ConfigBackedHealthcheckInfo Cannot get the web server name from HTTP header. So, localhost will be used with port number.");
			} 
			
			currentServerNameURL = InetAddress.getLocalHost().getHostName() + ":" + currentServerPort;
						
			if (currentURL.indexOf("spp-services-web/jsp/SPP") >= 0) {
				testURL = new URL("http://" + currentServerNameURL + Config.getValue(CONFIG_KEY_WSRP_HEALTHCHECK_URL));
				healthcheckString = Config.getValue(CONFIG_KEY_WSRP_HEALTHCHECK_STRING);
			}
			else {
				testURL = new URL("http://" + currentServerNameURL + Config.getValue(CONFIG_KEY_PORTAL_HEALTHCHECK_URL));
				healthcheckString = Config.getValue(CONFIG_KEY_PORTAL_HEALTHCHECK_STRING);
			}

			if (mLog.isDebugEnabled()) {
				mLog.debug("ConfigBackedHealthcheckInfo testURL = " + testURL.toString());
				mLog.debug("ConfigBackedHealthcheckInfo healthcheck String = " + healthcheckString);
			}

			int connectionTimeout = Integer.parseInt(Config.getValue(CONFIG_KEY_CONNECTION_TIMEOUT));
			URLConnection url = testURL.openConnection();
			url.setConnectTimeout(connectionTimeout); 
			con=(HttpURLConnection)url;
			con.setReadTimeout(connectionTimeout);
			in = con.getInputStream();

			//If there is any error, then return fail message.
			if (con.getResponseCode() > 400) {
				return "<p>Failed - HTTP error (" + con.getResponseCode() + "): " + con.getResponseMessage();
			}

			//get the HTML output string and check the output string contains a specfic string that we are looking for
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] b = new byte[8192];
			for (int n; (n = in.read(b)) != -1;) {
				outStream.write(b);
			}
			in.close();

			String outputStr = outStream.toString();
			if (outputStr.indexOf(healthcheckString) < 0) {
				return "<p>Failed - Cannot find the string to validate the page (" + healthcheckString + ")";
			}

			// health check successful and output "SUCCESS".
			return "<p>SUCCESS - Portal server name: " + Environment.singletonInstance.getManagedServerName() + "<p>Web server name: " + currentServerName;
		}
		catch (ConfigException e) {			
			throw new IllegalStateException("Error retrieving layer 7 health check configuration", e);
		}
		catch (IOException e) {
			throw new IllegalStateException("Error reading layer 7 health check test page (" + testURL + ")", e);			
		}
		finally {
			if (con != null) {
				con.disconnect();
			}
		}
		
	}

	public List<String> getFilterAllOption(String fieldName) {
		List<String> filterAllOption = new ArrayList<String>();
		List<HealthcheckServerInfo> allServerList = getAllServers();
		String optionStr = null;
		boolean duplicate;
		
		for (Iterator<HealthcheckServerInfo> i = allServerList.iterator(); i.hasNext(); ) {
			HealthcheckServerInfo serverInfo = i.next();
			if ("serverName".equals(fieldName)) {
				optionStr = serverInfo.getServerName();
			}
			else if ("applicationName".equals(fieldName)) {
				optionStr = serverInfo.getApplicationName();				
			}
			else if ("serverType".equals(fieldName)) {
				optionStr = serverInfo.getServerType();				
			}
			else if ("siteName".equals(fieldName)) {
				optionStr = serverInfo.getSiteName();				
			}
			else {
				break;
			}

			// if duplicate, then don't add in the list
			duplicate = false;
			for (Iterator<String> it = filterAllOption.iterator(); it.hasNext(); ) {
					if (optionStr.equals(it.next())) {
						duplicate = true;
						break;
					}
			}
			if (!duplicate) {
				filterAllOption.add(optionStr);
			}							
		}
		return filterAllOption;
	}	
}