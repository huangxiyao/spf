package com.hp.spp.cache;

import org.apache.log4j.Logger;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class CacheNotificationServlet extends GenericServlet {
	private static Logger mLog = Logger.getLogger(CacheNotificationServlet.class);

	private static List mBroadcastingListeners = new ArrayList();

	public static void register(HttpBroadcastingListener listener) {
		mBroadcastingListeners.add(listener);
	}


	public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
		if (CacheNotificationServlet.mBroadcastingListeners.isEmpty()) {
			CacheNotificationServlet.mLog.warn("No HttpBroadcastingListener registered! Check OSCache listener configuration!");
		}
		else {
			String type = servletRequest.getParameter("type");
			String data = servletRequest.getParameter("data");
			String node = servletRequest.getParameter("node");
			if (CacheNotificationServlet.mLog.isDebugEnabled()) {
				CacheNotificationServlet.mLog.debug("Received cache cluster notification for type=" + type + ", data=" + data + ", node=" + node);
			}

			for (Iterator it = CacheNotificationServlet.mBroadcastingListeners.iterator(); it.hasNext(); ) {
				HttpBroadcastingListener listener = (HttpBroadcastingListener) it.next();
				if (listener.getSelfBaseUrl() == null) {
					HttpServletRequest request = (HttpServletRequest) servletRequest;
					String selfUrl =
							request.getScheme() + "://" +
							request.getServerName() + ":" + request.getServerPort() +
							request.getContextPath();
					listener.setSelfBaseUrl(selfUrl);
				}

				servletResponse.setContentType("text/plain");
				try {
					listener.handleNotification(type, data, node);
					servletResponse.getWriter().println("OK");
				}
				catch (Exception e) {
					CacheNotificationServlet.mLog.error("Error handling notification", e);
					servletResponse.getWriter().println("FAILED: " + e);
				}
			}
		}
	}

	public void destroy() {
		super.destroy();
		CacheNotificationServlet.mBroadcastingListeners.clear();
	}
}
