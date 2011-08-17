


package com.hp.spp.eservice.base;
import java.util.List;

import com.hp.spp.eservice.EService;
import com.hp.spp.eservice.StandardParameterSet;
import com.hp.spp.eservice.dao.StandardParameterSetDAO;


public abstract class BaseStandardParameterSetDAO extends com.hp.spp.eservice.base._BaseRootDAO {

	public static StandardParameterSetDAO instance;

	/**
	 * Return a singleton of the DAO
	 */
	public static StandardParameterSetDAO getInstance () {
				if (null == instance) instance = new StandardParameterSetDAO();
				return instance;
	}
	
	/**
	 * Return the class of the current object
	 */
	public Class getReferenceClass () {
				return com.hp.spp.eservice.StandardParameterSet.class;
	}
	

	
	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param person a transient instance containing updated state
	 */
	public void update( com.hp.spp.eservice.StandardParameterSet abstractStandardParameterSet) 
		throws org.hibernate.HibernateException {
				super.update( abstractStandardParameterSet);
	}

	
	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param person the instance to be removed
	 */
	public void delete( com.hp.spp.eservice.StandardParameterSet abstractStandardParameterSet)
		throws org.hibernate.HibernateException {
				super.delete( abstractStandardParameterSet);
	}




	/**
	* Used to check that the unicity constraint 
	* given in the xml model file (/module/component/attributes/attribute[@unique='true'])
	* is not violated when a new StandardParameterSet is created
	* @param StandardParameterSet : the object that must be created
	* @return Long : the id of the newly created object
	*/
	public  Long save(com.hp.spp.eservice.StandardParameterSet abstractStandardParameterSet)throws org.hibernate.HibernateException{
			return super.save(abstractStandardParameterSet);
	
		
}

	/**
	* Used to check that the unicity constraint 
	* given in the xml model file (/module/component/attributes/attribute[@unique='true'])
	* is not violated when an existing StandardParameterSet is updated
	* @param StandardParameterSet : the object that must be updated
	* @return Long : the id of the object
	*/
	public  Long updateStandardParameterSet(com.hp.spp.eservice.StandardParameterSet abstractStandardParameterSet)throws org.hibernate.HibernateException{
			
		return new Long(abstractStandardParameterSet.getId());
	
	}

	/**
	* Used to load all the StandardParameterSets present in the database
	*/
	
	public  List findAll()throws org.hibernate.HibernateException{
				StringBuffer query = new StringBuffer();
		query.append("select standardParameterSet from com.hp.spp.eservice.StandardParameterSet as standardParameterSet ");
				List results = super.find(query.toString());
				return results;
	}

	/**
	* Used to load  the StandardParameterSet having an ID equal to 
	* @param long id 
	*/
	public  com.hp.spp.eservice.StandardParameterSet load(long id)throws org.hibernate.HibernateException{
		return (StandardParameterSet) load(StandardParameterSet.class, new Long(id));
	}

}

