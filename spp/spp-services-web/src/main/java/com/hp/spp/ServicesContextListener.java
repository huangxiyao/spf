package com.hp.spp;

import com.hp.spp.common.util.Environment;
import com.hp.spp.common.util.hibernate.HibernateUtil;
import com.hp.spp.groups.Site;
import com.hp.spp.groups.dao.SiteDAOCacheImpl;
import com.hp.spp.config.Config;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;

import java.util.List;

public class ServicesContextListener implements ServletContextListener {
	private static Logger mLog = Logger.getLogger(ServicesContextListener.class);

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		if (mLog.isInfoEnabled()) {
			mLog.info("*** Starting spp-services-web ***");
		}

		Config.setPrefix("wsrp");
		// call it to get the info into the log
		Environment.singletonInstance.getType();

		setupProxy(servletContextEvent.getServletContext());
		loadGroupDefinitions();
	}

	private void setupProxy(ServletContext sc) {
		String httpProxyHost = getInitParameter(sc, "http.proxyHost");
		String httpProxyPort = getInitParameter(sc, "http.proxyPort");
		String httpNonProxyHosts = getInitParameter(sc, "http.nonProxyHosts");
		String httpsProxyHost = getInitParameter(sc, "https.proxyHost");
		String httpsProxyPort = getInitParameter(sc, "https.proxyPort");
		String httpsNonProxyHosts = getInitParameter(sc, "https.nonProxyHosts");

		if (httpProxyHost != null && httpProxyPort != null) {
			try {
				// try to parse it to make sure it's a correct integer
				Integer.parseInt(httpProxyPort);

				String sysProxyHost = System.getProperty("http.proxyHost");
				String sysProxyPort = System.getProperty("http.proxyPort");

				System.setProperty("http.proxyHost", httpProxyHost);
				System.setProperty("http.proxyPort", httpProxyPort);
				if (mLog.isInfoEnabled()) {
					if (sysProxyHost != null || sysProxyPort != null) {
						mLog.info("Replacing configured HTTP proxy (" + sysProxyHost + ":" +
								sysProxyPort + ") with " + httpProxyHost + ":" + httpProxyPort);
					}
					else {
						mLog.info("Setting HTTP proxy to " + httpProxyHost + ":" + httpProxyPort);
					}
				}
			}
			catch (NumberFormatException e) {
				mLog.error("HTTP proxy not set as port is not a valid number: " + httpProxyPort);
			}
		}
		if (httpNonProxyHosts != null) {
			String sysNonProxyHosts = System.getProperty("http.nonProxyHosts");
			System.setProperty("http.nonProxyHosts", httpNonProxyHosts);
			if (mLog.isInfoEnabled()) {
				if (sysNonProxyHosts != null) {
					mLog.info("Replacing configured HTTP non-proxy hosts (" + sysNonProxyHosts +
							") with " + httpNonProxyHosts);
				}
			}
		}

		if (httpsProxyHost != null && httpsProxyPort != null) {
			try {
				// try to parse it to make sure it's a correct integer
				Integer.parseInt(httpsProxyPort);

				String sysProxyHost = System.getProperty("https.proxyHost");
				String sysProxyPort = System.getProperty("https.proxyPort");

				System.setProperty("https.proxyHost", httpsProxyHost);
				System.setProperty("https.proxyPort", httpsProxyPort);
				if (mLog.isInfoEnabled()) {
					if (sysProxyHost != null || sysProxyPort != null) {
						mLog.info("Replacing configured HTTPS proxy (" + sysProxyHost + ":" +
								sysProxyPort + ") with " + httpsProxyHost + ":" + httpsProxyPort);
					}
					else {
						mLog.info("Setting HTTPS proxy to " + httpsProxyHost + ":" + httpsProxyPort);
					}
				}
			}
			catch (NumberFormatException e) {
				mLog.error("HTTPS proxy not set as port is not a valid number: " + httpsProxyPort);
			}
		}
		if (httpsNonProxyHosts != null) {
			String sysNonProxyHosts = System.getProperty("https.nonProxyHosts");
			System.setProperty("https.nonProxyHosts", httpsNonProxyHosts);
			if (mLog.isInfoEnabled()) {
				if (sysNonProxyHosts != null) {
					mLog.info("Replacing configured HTTPS non-proxy hosts (" + sysNonProxyHosts +
							") with " + httpsNonProxyHosts);
				}
			}
		}
	}

	private String getInitParameter(ServletContext sc, String name) {
		if (sc.getInitParameter(name) == null || sc.getInitParameter(name).trim().equals("")) {
			return null;
		}
		else {
			return sc.getInitParameter(name).trim();
		}
	}

	private void loadGroupDefinitions() {
		Transaction tx = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

			// we find all sites in database
			List siteList = com.hp.spp.groups.dao.SiteDAOHibernateImpl.getInstance().findAll();

			// for each site, put it into the Cache
			for (int i = 0; i < siteList.size(); i++) {

				Site site = (Site) siteList.get(i);

				SiteDAOCacheImpl.getInstance().load(site.getId());
			}

			tx.commit();

		} catch (Exception e) {
			mLog.error("Error during the initialization of the GroupInitServlet", e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException he) {
					mLog.error("Error during ROLLBACK of Hibernate transaction", he);
				}
				tx = null;
			}
			// Raising exception (will be caught by the framework)
//			throw new ServletException(e);
		} finally {
			try {
				HibernateUtil.getSessionFactory().getCurrentSession().close();
			} catch (HibernateException he) {
				mLog.error("Error during CLOSE of Hibernate transaction", he);
			}
		}

	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
	}
}
