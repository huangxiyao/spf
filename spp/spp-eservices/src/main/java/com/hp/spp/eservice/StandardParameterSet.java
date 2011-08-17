

package com.hp.spp.eservice;



import com.hp.spp.eservice.Site ;

import com.hp.spp.eservice.Parameter ;

import java.util.List;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;


import java.io.Serializable;


import com.hp.spp.eservice.base.BaseStandardParameterSetDAO;



public class StandardParameterSet implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	*	Default constructor
	*/
	public StandardParameterSet() {
	}

	//attributes

	private long mId;

	private String mName;

	private Site mSite;

	private Set mParameterList;

	private Date mCreationDate;

	private Date mLastModificationDate;


	//getters

	public long getId() {
		return mId;
	}

	public String getName(){
    	return mName;
    }

	public Site getSite(){
    	return mSite;
    }

	public Set getParameterList(){
    	return mParameterList;
    }


	//setters

	public void setId(long id) {
		this.mId = id;
	}

	public void  setName( String  name ){
    	this.mName = name;
    }

	public void  setSite( Site  site ){
    	this.mSite = site;
    }

	public void  setParameterList( Set   parameterList ){
    	this.mParameterList = parameterList;
    }


    /*
    *
    * Method that associates a AbstractSite to an Abstract AbstractStandardParameterSet
    */
    public void addSite(Site abstractSite) {

    	this.mSite = abstractSite;


    }

    /*
    *
    * Method that removes the association between a AbstractSite
    * and an Abstract AbstractStandardParameterSet
    */

    public void removeSite(Site site) {

    	this.mSite = null;


    }

    /*
    *
    * Method that associates a AbstractParameter to an Abstract AbstractStandardParameterSet
    */
    public void addParameter(Parameter abstractParameter) {

    	if (this.mParameterList == null) {
    		this.mParameterList = new TreeSet();
    	}
    	this.mParameterList.add(abstractParameter);


    }

    /*
    *
    * Method that removes the association between a AbstractParameter
    * and an Abstract AbstractStandardParameterSet
    */

    public void removeParameter(Parameter parameter) {

    	this.mParameterList.remove(parameter);


    }

	public int hashCode() {
		return new Long(mId).hashCode();
	}



	/**
	*	Method that returns the name of this class
	*/
	public String getComponentType() {
		return "StandardParameterSet";
	}



	//inner Class DB


public static class DB {


		/**
		* Method that creates a new StandardParameterSet in the database
		*/
		public static Long save(StandardParameterSet abstractStandardParameterSet)throws Exception {
			return BaseStandardParameterSetDAO.getInstance().save(abstractStandardParameterSet);
		}

		/**
		* Method that deletes the StandardParameterSet from the database
		*/
		public static void delete(StandardParameterSet abstractStandardParameterSet)throws Exception {
			BaseStandardParameterSetDAO.getInstance().delete(abstractStandardParameterSet);
		}

		/**
		* Method that updates an existing StandardParameterSet present in the database
		*/
		public static Long update(StandardParameterSet abstractStandardParameterSet)throws Exception {
			return BaseStandardParameterSetDAO.getInstance().updateStandardParameterSet(abstractStandardParameterSet);
		}

		/**
		* Method that loads the StandardParameterSet having an id equal to  id passed as parameter
		*/
		public static StandardParameterSet load(long id)throws Exception {
			return BaseStandardParameterSetDAO.getInstance().load(id);
		}

		/**
		* Method that loads all the StandardParameterSet present in the database
		*/
		public static List findAll() throws Exception {
			return BaseStandardParameterSetDAO.getInstance().findAll();
		}

		/**
		* Method that loads all the StandardParameterSet that correspond to the query passed as parameter
		*/
		public static List findByQuery(String query) throws Exception {
			return BaseStandardParameterSetDAO.getInstance().find(query);
		}

}



public Date getCreationDate() {
	return mCreationDate;
}

public void setCreationDate(Date creationDate) {
	mCreationDate = creationDate;
}

public Date getLastModificationDate() {
	return mLastModificationDate;
}

public void setLastModificationDate(Date lastModificationDate) {
	mLastModificationDate = lastModificationDate;
}

}
