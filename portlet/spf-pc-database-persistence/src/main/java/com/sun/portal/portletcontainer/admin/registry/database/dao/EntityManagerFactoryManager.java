package com.sun.portal.portletcontainer.admin.registry.database.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.sun.portal.portletcontainer.admin.database.exception.PortletRegistryDBException;


public class EntityManagerFactoryManager {
	private static Logger LOG = Logger.getLogger(EntityManagerFactoryManager.class.toString());
	
	private static EntityManagerFactoryManager manager = null;
	private static EntityManagerFactory emf;
	
	static {
		try {
			emf = Persistence.createEntityManagerFactory("portletregistry_datasource");
		} catch (Exception ex){			
			throw new PortletRegistryDBException("Create entity manager factory error!", ex);
		}
	}
	
	public static EntityManagerFactoryManager getInstance() {
	    if (manager == null || emf == null){
	    	manager = new EntityManagerFactoryManager();
	    }
	    return manager;
	}
	
	public EntityManagerFactory getFactory(){
		if (LOG.isLoggable(Level.INFO)) {
			LOG.log(Level.FINE, "Retrieved entity manager facotry: " + emf.toString());
		}
		return emf;
	}
}
