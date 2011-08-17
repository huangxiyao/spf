


package com.hp.spp.eservice.base;
import java.util.List;

import com.hp.spp.eservice.EService;
import com.hp.spp.eservice.dao.EServiceDAO;

public abstract class BaseEServiceDAO extends com.hp.spp.eservice.base._BaseRootDAO {

	public static EServiceDAO instance;

	/**
	 * Return a singleton of the DAO
	 */
	public static EServiceDAO getInstance () {
			if (null == instance) instance = new EServiceDAO();
			return instance;
	}
	
	/**
	 * Return the class of the current object
	 */
	public Class getReferenceClass () {
			return com.hp.spp.eservice.EService.class;
	}
	

	
	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param person a transient instance containing updated state
	 */
	public void update( com.hp.spp.eservice.EService abstractEService) 
		throws org.hibernate.HibernateException {
				super.update( abstractEService);
	}

	
	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param person the instance to be removed
	 */
	public void delete( com.hp.spp.eservice.EService abstractEService)
		throws org.hibernate.HibernateException {
				super.delete( abstractEService);
	}




	/**
	* Used to check that the unicity constraint 
	* given in the xml model file (/module/component/attributes/attribute[@unique='true'])
	* is not violated when a new EService is created
	* @param EService : the object that must be created
	* @return Long : the id of the newly created object
	*/
	public  Long save(com.hp.spp.eservice.EService abstractEService)throws org.hibernate.HibernateException{
		return super.save(abstractEService);
	
		
}

	/**
	* Used to check that the unicity constraint 
	* given in the xml model file (/module/component/attributes/attribute[@unique='true'])
	* is not violated when an existing EService is updated
	* @param EService : the object that must be updated
	* @return Long : the id of the object
	*/
	public  Long updateEService(com.hp.spp.eservice.EService abstractEService)throws org.hibernate.HibernateException{
			
		return new Long(abstractEService.getId());
	
	}

	/**
	* Used to load all the EServices present in the database
	*/
	
	public  List findAll()throws org.hibernate.HibernateException{
				StringBuffer query = new StringBuffer();
		query.append("select eService from com.hp.spp.eservice.EService as eService ");
			List results = super.find(query.toString());
				return results;
	}

	/**
	* Used to load  the EService having an ID equal to 
	* @param long id 
	*/
	public  EService load(long id)throws org.hibernate.HibernateException{
		return (EService) load(EService.class, new Long(id));
	}

}

