

package com.hp.spp.eservice;



import com.hp.spp.eservice.EService ;

import com.hp.spp.eservice.StandardParameterSet ;

import java.util.List;

import java.util.Set;
import java.util.TreeSet;


import java.io.Serializable;


import com.hp.spp.eservice.base.BaseSiteDAO;



public class Site implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	*	Default constructor
	*/
	public Site() {
	}

	//attributes

	private long id;

	private String name;

	private Set  eServiceList;

	private Set  standardParameterSet;


	//getters

	public long getId() {
		return id;
	}

	public String getName(){
    	return name;
    }

	public Set getEServiceList(){
    	return eServiceList;
    }

	public Set getStandardParameterSet(){
    	return standardParameterSet;
    }


	//setters

	public void setId(long id) {
		this.id = id;
	}

	public void  setName( String  name ){
    	this.name = name;
    }

	public void  setEServiceList( Set   eServiceList ){
    	this.eServiceList = eServiceList;
    }

	public void  setStandardParameterSet( Set   standardParameterSet ){
    	this.standardParameterSet = standardParameterSet;
    }



    /*
    *
    * Method that associates a AbstractEService to an Abstract AbstractSite
    */
    public void addEService(EService abstractEService) {

    	if (this.eServiceList == null) {
    		this.eServiceList = new TreeSet();
    	}
    	this.eServiceList.add(abstractEService);


    }

    /*
    *
    * Method that removes the association between a AbstractEService
    * and an Abstract AbstractSite
    */

    public void removeEService(EService eService) {

    	this.eServiceList.remove(eService);


    }

    /*
    *
    * Method that associates a AbstractStandardParameterSet to an Abstract AbstractSite
    */
    public void addStandardParameterSet(StandardParameterSet abstractStandardParameterSet) {

    	if (this.standardParameterSet == null) {
    		this.standardParameterSet = new TreeSet();
    	}
    	this.standardParameterSet.add(abstractStandardParameterSet);


    }

    /*
    *
    * Method that removes the association between a AbstractStandardParameterSet
    * and an Abstract AbstractSite
    */

    public void removeStandardParameterSet(StandardParameterSet standardParameterSet) {

    	this.standardParameterSet.remove(standardParameterSet);


    }

	public boolean equals(Object obj) {
		if ((obj != null) && (obj instanceof Site)) {
			Site site = (Site) obj;
			return (site.getId()== this.getId());
		}
		return false;
	}

	public int hashCode() {
		return new Long(id).hashCode();
	}



	/**
	*	Method that returns the name of this class
	*/
	public String getComponentType() {
		return "Site";
	}



	//inner Class DB

/**
*
* Inner class that is used to interact with the database
* !!!! WARNING !!!!!
* This inner class should not be modified
* Any modifications made will be lost during regeneration
*
**/
public static class DB {


		/**
		* Method that creates a new Site in the database
		*/
		public static Long save(Site abstractSite)throws Exception {
			return BaseSiteDAO.getInstance().save(abstractSite);
		}

		/**
		* Method that deletes the Site from the database
		*/
		public static void delete(Site abstractSite)throws Exception {
			BaseSiteDAO.getInstance().delete(abstractSite);
		}

		/**
		* Method that updates an existing Site present in the database
		*/
		public static Long update(Site abstractSite)throws Exception {
			return BaseSiteDAO.getInstance().updateSite(abstractSite);
		}

		/**
		* Method that loads the Site having an id equal to  id passed as parameter
		*/
		public static Site load(long id)throws Exception {
			return BaseSiteDAO.getInstance().load(id);
		}

		/**
		* Method that loads all the Site present in the database
		*/
		public static List findAll() throws Exception {
			return BaseSiteDAO.getInstance().findAll();
		}

		/**
		* Method that loads all the Site that correspond to the query passed as parameter
		*/
		public static List findByQuery(String query) throws Exception {
			return BaseSiteDAO.getInstance().find(query);
		}

}

}
