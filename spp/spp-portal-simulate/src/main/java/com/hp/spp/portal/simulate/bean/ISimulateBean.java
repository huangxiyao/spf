package com.hp.spp.portal.simulate.bean;

import java.util.Map;

import javax.portlet.PortletRequest;

public interface ISimulateBean {

	public static final String SPP_SiteName = "sn";

	public static final String SPP_SimulationKey = "sk";

	public static final String SPP_RedirectUrl = "ru";

	public static final String SPP_ErrorUrl = "eu";

	/**
	 * Return the action URL wihout any parameter to use this bean to build a 
	 * form to send all parameters by a POST method. 
	 * @return return the action URL
	 */
	public abstract String getActionURL();

	/**
	 * Return a <tt>Map</tt> with all couple key/value to create the hidden
	 * fields if the bean is used to build a form.
	 * @return return the <tt>Map</tt> with all possible hidden fields
	 * @throws SimulateBeanException throw if the Map's build failed
	 */
	public abstract Map getParamMap() throws SimulateBeanException;

	/**
	 * Return the full URL of the <tt>Simulation Page</tt> with all parameters
	 * concated in the <tt>QueryString</tt>. Used to call the simulation page
	 * by a redirection process.
	 * @return return the full URL of the <tt>Simulation Page</tt>
	 * @throws SimulateBeanException throw if the URL's build failed
	 */
	public abstract String getFullURLWithParameters() throws SimulateBeanException;

	/**
	 * Used to retrieve some information like <tt>ServerName</tt>, 
	 * <tt>SiteName</tt>, etc.
	 * @param request the <tt>request</tt> of the portlet
	 */
	public abstract void setPortletRequest(PortletRequest request);

	/**
	 * Used to be encrypted and pass to the simulation page.
	 * @param profileId the <tt>profileId</tt> of the <b>simulated</b> user
	 */
	public abstract void setSimulatedProfileHPPId(String profileId);

	/**
	 * Used to be encrypted and pass to the simulation page.
	 * @param sessionToken the <tt>sessionToken</tt> of the <b>simulator</b> user
	 */
	public abstract void setSessionId(String sessionToken);

	/**
	 * Used to redirect on the <b>success</b> page at the end of the simulation.
	 * By default, if the value is not set, the succes page is the home page
	 * of the site.
	 * @param redirectURLName the <i>menuItem name</i> of the success page
	 */
	public abstract void setRedirectURLName(String redirectURLName);

	/**
	 * Used to redirect on the <b>error</b> page at the end of the simulation.
	 * @param errorURLName the <i>menuItem name</i> of the error page
	 */
	public abstract void setErrorURLName(String errorURLName);

}
