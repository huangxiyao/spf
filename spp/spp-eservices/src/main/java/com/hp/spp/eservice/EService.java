package com.hp.spp.eservice;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.hp.spp.eservice.base.BaseEServiceDAO;

public class EService implements Serializable {
	
	private static Logger mLog = Logger.getLogger(EService.class);

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public EService() {
	}

	// attributes

	private long id;

	private String name;

	private String method;

	private String productionUrl;

	private String testUrl;

	private Date creationDate;

	private Date lastModificationDate;

	private StandardParameterSet standardParameterSet;

	private Site site;

	private Set parameterList;
	
	private long navigationMode;
	
	private long securityMode;

	private String standardParameterSetName;
	
	private long simulationMode;
	
	private String windowParameters;
	
	private String characterEncoding;

	// getters

	

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getMethod() {
		return method;
	}

	public String getProductionUrl() {
		return productionUrl;
	}

	public String getTestUrl() {
		return testUrl;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getLastModificationDate() {
		return lastModificationDate;
	}

	public StandardParameterSet getStandardParameterSet() {
		return standardParameterSet;
	}

	public Site getSite() {
		return site;
	}

	public Set getParameterList() {
		return parameterList;
	}

	public long getSimulationMode() {
		return simulationMode;
	}
	
	public String getWindowParameters() {
		return windowParameters;
	}
	
	public String getCharacterEncoding() {
		return characterEncoding;
	}

	// setters

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setProductionUrl(String productionUrl) {
		this.productionUrl = productionUrl;
	}

	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setLastModificationDate(Date lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	public void setStandardParameterSet(StandardParameterSet standardParameterSet) {
		this.standardParameterSet = standardParameterSet;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public void setParameterList(Set parameterList) {
		this.parameterList = parameterList;
	}

	public void setSimulationMode(long simulationMode) {
		this.simulationMode = simulationMode;
	}
	
	public void setWindowParameters(String windowParameters) {
		this.windowParameters = windowParameters;
	}
	
	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}
	
	/*
	 * 
	 * Method that associates a AbstractStandardParameterSet to an Abstract AbstractEService
	 */
	public void addStandardParameterSet(StandardParameterSet abstractStandardParameterSet) {

		this.standardParameterSet = abstractStandardParameterSet;

	}

	/*
	 * 
	 * Method that removes the association between a AbstractStandardParameterSet and an
	 * Abstract AbstractEService
	 */

	public void removeStandardParameterSet(StandardParameterSet standardParameterSet) {

		this.standardParameterSet = null;

	}

	/*
	 * 
	 * Method that associates a AbstractSite to an Abstract AbstractEService
	 */
	public void addSite(Site abstractSite) {

		this.site = abstractSite;

	}

	/*
	 * 
	 * Method that removes the association between a AbstractSite and an Abstract
	 * AbstractEService
	 */

	public void removeSite(Site site) {

		this.site = null;

	}

	/*
	 * 
	 * Method that associates a AbstractParameter to an Abstract AbstractEService
	 */
	public void addParameter(Parameter abstractParameter) {

		if (this.parameterList == null) {
			this.parameterList = new TreeSet();
		}
		this.parameterList.add(abstractParameter);

	}

	/*
	 * 
	 * Method that removes the association between a AbstractParameter and an Abstract
	 * AbstractEService
	 */

	public void removeParameter(Parameter parameter) {

		this.parameterList.remove(parameter);

	}

	public int hashCode() {
		return new Long(id).hashCode();
	}

	/**
	 * Method that returns the name of this class
	 */
	public String getComponentType() {
		return "EService";
	}

	
	public long getSecurityMode() {
		return securityMode;
	}

	public void setSecurityMode(long securityMode) {
		this.securityMode = securityMode;
	}
	
	public long getNavigationMode() {
		return navigationMode;
	}

	public void setNavigationMode(long pNavigationMode) {
		this.navigationMode = pNavigationMode;
	}



	// inner Class DB

	/**
	 * 
	 * Inner class that is used to interact with the database !!!! WARNING !!!!! This inner
	 * class should not be modified Any modifications made will be lost during regeneration
	 * 
	 */
	public static class DB {

		/**
		 * Method that creates a new EService in the database
		 */
		public static Long save(EService abstractEService) throws Exception {
			return BaseEServiceDAO.getInstance().save(abstractEService);
		}

		/**
		 * Method that deletes the EService from the database
		 */
		public static void delete(EService abstractEService) throws Exception {
			BaseEServiceDAO.getInstance().delete(abstractEService);
		}

		/**
		 * Method that updates an existing EService present in the database
		 */
		public static Long update(EService abstractEService) throws Exception {
			return BaseEServiceDAO.getInstance().updateEService(abstractEService);
		}

		/**
		 * Method that loads the EService having an id equal to id passed as parameter
		 */
		public static EService load(long id) throws Exception {
			return BaseEServiceDAO.getInstance().load(id);
		}

		/**
		 * Method that loads all the EService present in the database
		 */
		public static List findAll() throws Exception {
			return BaseEServiceDAO.getInstance().findAll();
		}

		/**
		 * Method that loads all the EService that correspond to the query passed as parameter
		 */
		public static List findByQuery(String query) throws Exception {
			return BaseEServiceDAO.getInstance().find(query);
		}

	}

	public String getStandardParameterSetName() {
		if (standardParameterSetName == null && standardParameterSet != null)	{
			standardParameterSetName = standardParameterSet.getName();
		}
		return standardParameterSetName;
	}

	public void setStandardParameterSetName(String standardParameterSetName) {
		this.standardParameterSetName = standardParameterSetName;
	}


}
