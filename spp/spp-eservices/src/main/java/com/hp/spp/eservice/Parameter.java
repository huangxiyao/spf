package com.hp.spp.eservice;

import java.io.Serializable;
import java.util.List;

import javax.servlet.jsp.el.ELException;

import com.hp.spp.eservice.base.BaseParameterDAO;
import com.hp.spp.eservice.el.ExpressionResolver;

public class Parameter implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public Parameter() {
	}

	// attributes

	private long id;

	private EService eService;

	private StandardParameterSet standardParameterSet;

	private String name;

	private String expression;

	// getters

	public long getId() {
		return id;
	}

	public EService getEService() {
		return eService;
	}

	public StandardParameterSet getStandardParameterSet() {
		return standardParameterSet;
	}

	public String getName() {
		return name;
	}

	public String getExpression() {
		return expression;
	}

	// setters

	public void setId(long id) {
		this.id = id;
	}

	public void setEService(EService eService) {
		this.eService = eService;
	}

	public void setStandardParameterSet(StandardParameterSet standardParameterSet) {
		this.standardParameterSet = standardParameterSet;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public boolean containsDynamicExpression(){
		if (expression.indexOf('$')>-1) return true;
		return false;
	}
	/*
	 *
	 * Method that associates a AbstractEService to an Abstract AbstractParameter
	 */
	public void addEService(EService abstractEService) {

		this.eService = abstractEService;

	}

	/*
	 *
	 * Method that removes the association between a AbstractEService and an Abstract
	 * AbstractParameter
	 */

	public void removeEService(EService eService) {

		this.eService = null;

	}

	/*
	 *
	 * Method that associates a AbstractStandardParameterSet to an Abstract AbstractParameter
	 */
	public void addStandardParameterSet(StandardParameterSet abstractStandardParameterSet) {

		this.standardParameterSet = abstractStandardParameterSet;

	}

	/*
	 *
	 * Method that removes the association between a AbstractStandardParameterSet and an Abstract
	 * AbstractParameter
	 */

	public void removeStandardParameterSet(StandardParameterSet standardParameterSet) {

		this.standardParameterSet = null;

	}

	public int hashCode() {
		return new Long(id).hashCode();
	}

	/**
	 * Method that returns the name of this class
	 */
	public String getComponentType() {
		return "Parameter";
	}

	//SPP Specific Modification
	
	public String getParameterValue(ExpressionResolver resolver) throws ELException{
		return (String)resolver.eval(getExpression(), String.class);
	}
	
	// inner Class DB
	public static class DB {

		/**
		 * Method that creates a new Parameter in the database
		 */
		public static Long save(Parameter abstractParameter) throws Exception {
			return BaseParameterDAO.getInstance().save(abstractParameter);
		}

		/**
		 * Method that deletes the Parameter from the database
		 */
		public static void delete(Parameter abstractParameter) throws Exception {
			BaseParameterDAO.getInstance().delete(abstractParameter);
		}

		/**
		 * Method that updates an existing Parameter present in the database
		 */
		public static Long update(Parameter abstractParameter) throws Exception {
			return BaseParameterDAO.getInstance().updateParameter(abstractParameter);
		}

		/**
		 * Method that loads the Parameter having an id equal to id passed as parameter
		 */
		public static Parameter load(long id) throws Exception {
			return BaseParameterDAO.getInstance().load(id);
		}

		/**
		 * Method that loads all the Parameter present in the database
		 */
		public static List findAll() throws Exception {
			return BaseParameterDAO.getInstance().findAll();
		}

		/**
		 * Method that loads all the Parameter that correspond to the query passed as parameter
		 */
		public static List findByQuery(String query) throws Exception {
			return BaseParameterDAO.getInstance().find(query);
		}

	}

}
