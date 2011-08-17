package com.hp.spp.common.util.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Creates a sessionFactory at the start of the application from the parameters provided in the
 * hibernate.cfg.xml.
 * 
 * @author klauthan
 * 
 */
public class HibernateUtil {
	
	private static Logger logger = Logger.getLogger(HibernateUtil.class);

	/**
	 * The Hibernate SessionFactory used to manage the Sessions.
	 */
	public static final SessionFactory mSessionFactory;

	static {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			mSessionFactory = new Configuration().configure().buildSessionFactory();

		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			logger.error("Initial SessionFactory creation failed.", ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Returns the SessionFactory.
	 * 
	 * @return SessionFactory
	 */
	public static SessionFactory getSessionFactory() {
		return mSessionFactory;
	}

}
