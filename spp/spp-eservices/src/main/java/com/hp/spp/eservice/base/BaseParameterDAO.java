


package com.hp.spp.eservice.base;
import java.util.List;

import com.hp.spp.eservice.dao.ParameterDAO;


public abstract class BaseParameterDAO extends com.hp.spp.eservice.base._BaseRootDAO {

	public static ParameterDAO instance;

	/**
	 * Return a singleton of the DAO
	 */
	public static ParameterDAO getInstance () {
			if (null == instance) instance = new ParameterDAO();
			return instance;
	}
	
	/**
	 * Return the class of the current object
	 */
	public Class getReferenceClass () {
				return com.hp.spp.eservice.Parameter.class;
	}
	

	
	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param person a transient instance containing updated state
	 */
	public void update( com.hp.spp.eservice.Parameter abstractParameter) 
		throws org.hibernate.HibernateException {
			super.update( abstractParameter);
	}

	
	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param person the instance to be removed
	 */
	public void delete( com.hp.spp.eservice.Parameter abstractParameter)
		throws org.hibernate.HibernateException {
				super.delete( abstractParameter);
	}




	/**
	* Used to check that the unicity constraint 
	* given in the xml model file (/module/component/attributes/attribute[@unique='true'])
	* is not violated when a new Parameter is created
	* @param Parameter : the object that must be created
	* @return Long : the id of the newly created object
	*/
	public  Long save(com.hp.spp.eservice.Parameter abstractParameter)throws org.hibernate.HibernateException{
			return super.save(abstractParameter);
	
		
}

	/**
	* Used to check that the unicity constraint 
	* given in the xml model file (/module/component/attributes/attribute[@unique='true'])
	* is not violated when an existing Parameter is updated
	* @param Parameter : the object that must be updated
	* @return Long : the id of the object
	*/
	public  Long updateParameter(com.hp.spp.eservice.Parameter abstractParameter)throws org.hibernate.HibernateException{
			
		return new Long(abstractParameter.getId());
	
	}

	/**
	* Used to load all the Parameters present in the database
	*/
	
	public  List findAll()throws org.hibernate.HibernateException{
				StringBuffer query = new StringBuffer();
		query.append("select parameter from com.hp.spp.eservice.bean.Parameter as parameter ");
				List results = super.find(query.toString());
			return results;
	}

	/**
	* Used to load  the Parameter having an ID equal to 
	* @param long id 
	*/
	public  com.hp.spp.eservice.Parameter load(long id)throws org.hibernate.HibernateException{
				StringBuffer query = new StringBuffer();
		query.append("select parameter from com.hp.spp.eservice.bean.Parameter as parameter where parameter.id ='"+id+"'");
			com.hp.spp.eservice.Parameter  results = (com.hp.spp.eservice.Parameter )super.find(query.toString()).get(0);
				return results;
	}

}

