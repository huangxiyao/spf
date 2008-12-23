package com.sun.portal.portletcontainer.admin.registry.database.dao;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletDeplymentDescriptor;

public class PortletDeplymentDescriptorDao {
	private static Logger LOG = Logger.getLogger(PortletDeplymentDescriptorDao.class.toString());
	
	private EntityManagerFactory emFactory;
	
	public PortletDeplymentDescriptorDao() {
		emFactory = EntityManagerFactoryManager.getInstance().getFactory();;
	}
	
	public InputStream loadBeforeRemove(String descriptorName) {
	    EntityManager em = emFactory.createEntityManager();
	    EntityTransaction tran = em.getTransaction();
	    
	    try {	    	
	    	PortletDeplymentDescriptor descriptor = em.find(PortletDeplymentDescriptor.class, descriptorName);
	    	if (descriptor != null) {
		    	ByteArrayInputStream bais = new ByteArrayInputStream(descriptor.getContent());
		    	tran.begin();
		    	em.remove(descriptor);
		    	tran.commit();
		    	return bais;
	    	}	    		    	
		} catch (Exception ex) {
			if(tran!=null && tran.isActive()) tran.rollback();
			LOG.warning("get portlet deployment descriptor error, descriptorName: " + descriptorName);
			LOG.warning("error message: " + ex.getMessage());
		} finally {
			em.close();
		}	
        return null;        
	}
		
	public void SaveOrUpdate(String descriptorName, InputStream content) {
		EntityManager em = emFactory.createEntityManager();
		EntityTransaction tran = em.getTransaction();
		try {
	    	PortletDeplymentDescriptor descriptor = new PortletDeplymentDescriptor();
	    	
	    	// read inputstream into byte array
	    	int len;
	    	BufferedInputStream bis = new BufferedInputStream(content);	    	
	    	byte[] buffer = new byte[1024];
	    	
	    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    	while ( (len=bis.read(buffer))!= -1 ) {
	    		bos.write(buffer, 0, len);
	    	} 
	    	
	    	descriptor.setDescriptorName(descriptorName);
	    	descriptor.setContent(bos.toByteArray());
	    	tran.begin();
	    	
	    	// for descriptorName is the id of entity, so it will override
	    	// the value of entity as the specified name 
	    	em.merge(descriptor);
	    	
	    	tran.commit();	    	
		} catch (Exception ex) {
			if(tran!=null && tran.isActive()) tran.rollback();
			LOG.warning("save portlet deployment descriptor error, descriptorName: " + descriptorName);
			LOG.warning("error message: " + ex.getMessage());
		} finally {
			em.close();
		}	
		
	}
}
