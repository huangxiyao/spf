package com.hp.spp.common.healthcheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.spp.config.Config;
import com.hp.spp.config.ConfigEntry;
import com.hp.spp.config.ConfigException;
import com.hp.spp.common.util.Environment;

public class HealthcheckHelper {

	private static final Logger mLog = Logger.getLogger(HealthcheckHelper.class);

	public static final String CONFIG_KEY_PORTAL_HEALTHCHECK_URL = "SPP.Healthcheck.portalURL";
	public static final String CONFIG_KEY_WSRP_HEALTHCHECK_URL = "SPP.Healthcheck.wsrpURL";
	public static final String CONFIG_KEY_PORTAL_HEALTHCHECK_STRING = "SPP.Healthcheck.portalString";
	public static final String CONFIG_KEY_WSRP_HEALTHCHECK_STRING = "SPP.Healthcheck.wsrpString";
	public static final String CONFIG_KEY_CONNECTION_TIMEOUT = "SPP.Healthcheck.connectionTimeout";

	// A method to access the test page URL and check the output is correct. Returns the result of testing.
	public static String testPageURL(String currentServerName, String currentURL) {

		String healthcheckString;
		InputStream in = null;
		HttpURLConnection con = null;
		URL testURL = null;

		if (mLog.isDebugEnabled()) {
			mLog.debug("JDBCHealthcheckInfo testPageURL(" + currentServerName + ", " + currentURL + ")");
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

	public static List<String> getFilterAllOption(String fieldName) {
		List<String> filterAllOption = new ArrayList<String>();
		List<HealthcheckServerInfo> allServerList = new JDBCHealthcheckDAOImpl().getAllServers();
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
