package com.hp.spp.cache;

import com.opensymphony.oscache.plugins.clustersupport.AbstractBroadcastingListener;
import com.opensymphony.oscache.plugins.clustersupport.ClusterNotification;
import com.opensymphony.oscache.base.InitializationException;
import com.opensymphony.oscache.base.Config;
import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.FinalizationException;

import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.rmi.server.UID;
import java.io.Serializable;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.log4j.Logger;

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
		CacheNotificationServlet.register(this);

		mNodeName = new UID().toString();
		if (HttpBroadcastingListener.mLog.isDebugEnabled()) {
			HttpBroadcastingListener.mLog.debug("Listener uses the following node name: " + mNodeName);
		}

		String urls = config.getProperty("cache.cluster.http.urls");
		if (HttpBroadcastingListener.mLog.isInfoEnabled()) {
			if (urls != null && !urls.trim().equals("")) {
				HttpBroadcastingListener.mLog.info("Will notify the following URLs: " + urls);
			}
			else {
				HttpBroadcastingListener.mLog.info("No cluster notifications will be sent as 'cache.cluster.http.urls' is empty or not set!");
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
					HttpBroadcastingListener.mLog.error("Error creating notification URL", e);
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
			if (HttpBroadcastingListener.mLog.isDebugEnabled()) {
				HttpBroadcastingListener.mLog.debug("Unable to parse ClusterNotification type: " + type + "; will use FLUSH_ALL");
			}
		}

		if (data == null && notificationType != ClusterNotification.FLUSH_CACHE) {
			notificationType = ClusterNotification.FLUSH_CACHE;
			if (HttpBroadcastingListener.mLog.isDebugEnabled()) {
				HttpBroadcastingListener.mLog.debug("ClusterNotification data is null; will use FLUSH_ALL for ClusterNotification type");
			}
		}

		// handle this notification if either node was not provided or it's different from
		// this node
		if (node == null || (node != null && !node.equals(mNodeName))) {
			Serializable notificationData =
					(notificationType == ClusterNotification.FLUSH_CACHE ? (Serializable) new Date() : data);
			ClusterNotification notification = new ClusterNotification(notificationType, notificationData);
			if (HttpBroadcastingListener.mLog.isDebugEnabled()) {
				HttpBroadcastingListener.mLog.debug("Handling cluster notification: " + notification);
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
					HttpBroadcastingListener.mLog.error("Error occured while preparing or sending notification to " + notificationUrl, e);
					HttpBroadcastingListener.mEmailLog.error(
							"Error occured while sending notification to URL: " + notificationUrl +
							". Notification sent from server: " + mSelfBaseUrl + ". Use your browser to access the following URL " +
							notificationUrl + ". If the server is down, its cache will be refreshed upon restart and no additional action is needed.");
				}
			}
		}
		else if (HttpBroadcastingListener.mLog.isDebugEnabled()) {
			HttpBroadcastingListener.mLog.debug("Received sendNotification request but no URLs to notify; ignoring...");
		}
	}

	private void sendNotificationToUrl(URL notificationUrl) throws IOException {
		if (HttpBroadcastingListener.mLog.isDebugEnabled()) {
			HttpBroadcastingListener.mLog.debug("Sending notification to url: " + notificationUrl);
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
		if (HttpBroadcastingListener.mLog.isDebugEnabled()) {
			HttpBroadcastingListener.mLog.debug("Received response for notification: " + response);
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
		if (HttpBroadcastingListener.mLog.isInfoEnabled()) {
			HttpBroadcastingListener.mLog.info("Will not send notification to urls starting with: " + mSelfBaseUrl);
		}
	}

	public String getSelfBaseUrl() {
		return mSelfBaseUrl;
	}
}
