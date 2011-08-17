package com.hp.spp.common.util.hibernate;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

/**
 * The HibernanteSessionFilter is used as a J2EE filter to manage the hibernante connexion
 * (Session).
 * 
 * @author MJULIENS
 * 
 */
public class HibernateSessionFilter implements Filter {
	
	private static Logger logger = Logger.getLogger(HibernateSessionFilter.class);

	/**
	 * @author klauthan Filters are Java classes that can : intercept requests from a client
	 *         before they access a resource manipulate requests from clients intercept
	 *         responses from resources before they are sent back to the client and manipulate
	 *         responses before they are sent to the client. In our case, the filter starts a
	 *         transaction at the beginning of every request and commits the transaction if no
	 *         exception is thrown. In the latter case, a rollback is done
	 */
	public void destroy() {
	}

	/**
	 * Start a transaction for every request caught Commit the transaction if no exception is
	 * thrown otherwise rollback.
	 * 
	 * @param req the HTTP request
	 * @param resp the HTTP response
	 * @param chain THe filter chain
	 * @throws javax.servlet.ServletException, java.io.IOException the IO exception that can be
	 *         trown
	 */
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws ServletException, IOException {

		Transaction tx = null;
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			chain.doFilter(req, resp);
			tx.commit();

		} catch (Exception e) {
			logger.error("Error commiting the transaction", e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException he) {
					logger.error("Error rolling back the transaction", e);
				}
				tx = null;
			}
			// Raising exception (may be caught by struts)
			throw new ServletException(e);
		} finally {
			try {
				HibernateUtil.getSessionFactory().getCurrentSession().close();
			} catch (HibernateException he) {
				logger.error("Error closing the transaction", he);
			}
		}
	}

	/**
	 * Method init.
	 * 
	 * @param config
	 * @throws javax.servlet.ServletException
	 */
	public void init(FilterConfig config) throws ServletException {

	}

}
