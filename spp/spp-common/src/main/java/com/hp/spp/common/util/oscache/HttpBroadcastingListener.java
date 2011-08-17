package com.hp.spp.common.util.oscache;

import com.opensymphony.oscache.plugins.clustersupport.AbstractBroadcastingListener;
import com.opensymphony.oscache.plugins.clustersupport.ClusterNotification;
import com.opensymphony.oscache.base.FinalizationException;
import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.Config;
import com.opensymphony.oscache.base.InitializationException;
import org.apache.log4j.Logger;

import java.util.Date;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.rmi.server.UID;

public class HttpBroadcastingListener extends AbstractBroadcastingListener {

	private static Logger mLog = Logger.getLogger(HttpBroadcastingListener.class);
	private static Logger mEmailLog = Logger.getLogger("sppEmailLog");

	private String mNodeName;
	private URL[] mUrlsToNotify;
	private String mSelfBaseUrl;


	public void initialize(Cache cache, Config config) throws InitializationException {
		super.initialize(cache, config);

		// The use of static method is ugly, but that's the only method to link between the servlet
		// receiving cache notification and this cache listener that has already defined the method
		// handleClusterNotification
		CacheNotificationServlet.setBroadcastingListener(this);

		mNodeName = new UID().toString();
		if (mLog.isDebugEnabled()) {
			mLog.debug("Listener uses the following node name: " + mNodeName);
		}

		String urls = config.getProperty("cache.cluster.http.urls");
		if (mLog.isInfoEnabled()) {
			if (urls != null && !urls.trim().equals("")) {
				mLog.info("Will notify the following URLs: " + urls);
			}
			else {
				mLog.info("No cluster notifications will be sent as 'cache.cluster.http.urls' is empty or not set!");
			}
		}

		if (urls != null && !urls.trim().equals("")) {
			String[] urlArray = urls.split("\\|");
			mUrlsToNotify = new URL[urlArray.length];
			for (int i = 0, len = mUrlsToNotify.length; i < len; ++i) {
				try {
					mUrlsToNotify[i] = new URL(urlArray[i].trim());
				}
				catch (MalformedURLException e) {
					mLog.error("Error creating notification URL", e);
					throw new InitializationException("Error creating notification URL: " + e);
				}
			}
		}
	}

	public void handleNotification(String type, String data, String node) {
		int notificationType = ClusterNotification.FLUSH_CACHE;
		try {
			if (type != null) {
				notificationType = Integer.parseInt(type);
			}
		}
		catch (NumberFormatException e) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Unable to parse ClusterNotification type: " + type + "; will use FLUSH_ALL");
			}
		}

		if (data == null && notificationType != ClusterNotification.FLUSH_CACHE) {
			notificationType = ClusterNotification.FLUSH_CACHE;
			if (mLog.isDebugEnabled()) {
				mLog.debug("ClusterNotification data is null; will use FLUSH_ALL for ClusterNotification type");
			}
		}

		// handle this notification if either node was not provided or it's different from
		// this node
		if (node == null || (node != null && !node.equals(mNodeName))) {
			Serializable notificationData =
					(notificationType == ClusterNotification.FLUSH_CACHE ? (Serializable) new Date() : data);
			ClusterNotification notification = new ClusterNotification(notificationType, notificationData);
			if (mLog.isDebugEnabled()) {
				mLog.debug("Handling cluster notification: " + notification);
			}
			handleClusterNotification(notification);
		}
	}

	protected void sendNotification(ClusterNotification message) {
		if (mUrlsToNotify != null && mUrlsToNotify.length > 0) {
			for (int i = 0, len = mUrlsToNotify.length; i < len; ++i) {
				URL notificationUrl = null;
				try {
					notificationUrl = createNotificationUrl(mUrlsToNotify[i], message);
					if (mSelfBaseUrl == null || !notificationUrl.toString().startsWith(mSelfBaseUrl))
					{
						sendNotificationToUrl(notificationUrl);
					}
				}
				catch (Exception e) {
					mLog.error("Error occured while preparing or sending notification to " + notificationUrl, e);
					mEmailLog.error(
							"Error occured while sending notification to URL: " + notificationUrl +
							". Notification sent from server: " + mSelfBaseUrl + ". Use your browser to access the following URL " +
							notificationUrl + ". If the server is down, its cache will be refreshed upon restart and no additional action is needed.");
				}
			}
		}
		else if (mLog.isDebugEnabled()) {
			mLog.debug("Received sendNotification request but no URLs to notify; ignoring...");
		}
	}

	private void sendNotificationToUrl(URL notificationUrl) throws IOException {
		if (mLog.isDebugEnabled()) {
			mLog.debug("Sending notification to url: " + notificationUrl);
		}
		URLConnection conn = notificationUrl.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(false);
		BufferedReader result = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		String response = result.readLine();
		result.close();
		if (conn instanceof HttpURLConnection) {
			((HttpURLConnection) conn).disconnect();
		}
		if (mLog.isDebugEnabled()) {
			mLog.debug("Received response for notification: " + response);
		}
	}

	private URL createNotificationUrl(URL baseUrl, ClusterNotification notification) throws UnsupportedEncodingException, MalformedURLException {
		String str = baseUrl.toString();
		StringBuffer sb = new StringBuffer(str);
		if (str.indexOf('?') == -1) {
			sb.append('?');
		}
		else {
			sb.append('&');
		}
		sb.append("node=").append(URLEncoder.encode(mNodeName, "UTF-8"));
		sb.append("&type=").append(URLEncoder.encode(String.valueOf(notification.getType()), "UTF-8"));
		if (notification.getType() != ClusterNotification.FLUSH_CACHE) {
			sb.append("&data=").append(notification.getData());
		}
		return new URL(sb.toString());
	}

	public void finialize() throws FinalizationException {
	}

	public void setSelfBaseUrl(String selfBaseUrl) {
		mSelfBaseUrl = selfBaseUrl;
		if (mLog.isInfoEnabled()) {
			mLog.info("Will not send notification to urls starting with: " + mSelfBaseUrl);
		}
	}
}
