package com.hp.spp.common.util.oscache;

import org.apache.log4j.Logger;

import javax.servlet.GenericServlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class CacheNotificationServlet extends GenericServlet {
	private static Logger mLog = Logger.getLogger(CacheNotificationServlet.class);

	private static HttpBroadcastingListener mBroadcastingListener;
	private boolean mSelfUrlSet = false;

	public static void setBroadcastingListener(HttpBroadcastingListener broadcastingListener) {
		mBroadcastingListener = broadcastingListener;
	}

	public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
		if (mBroadcastingListener == null) {
			mLog.warn("HttpBroadcastingListener not set! Check OSCache listener configuration!");
		}
		else {
			String type = servletRequest.getParameter("type");
			String data = servletRequest.getParameter("data");
			String node = servletRequest.getParameter("node");
			if (mLog.isDebugEnabled()) {
				mLog.debug("Received cache cluster notification for type=" + type + ", data=" + data + ", node=" + node);
			}

			if (!mSelfUrlSet) {
				HttpServletRequest request = (HttpServletRequest) servletRequest;
				String selfUrl =
						request.getScheme() + "://" +
						request.getServerName() + ":" + request.getServerPort() +
						request.getContextPath();
				mBroadcastingListener.setSelfBaseUrl(selfUrl);
				mSelfUrlSet = true;
			}

			servletResponse.setContentType("text/plain");
			try {
				mBroadcastingListener.handleNotification(type, data, node);
				servletResponse.getWriter().println("OK");
			}
			catch (Exception e) {
				mLog.error("Error handling notification", e);
				servletResponse.getWriter().println("FAILED: " + e);
			}
		}
	}
}
