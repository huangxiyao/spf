package com.hp.spp.portlets.reports.hppreport.controller;

import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.SimpleFormController;
import org.springframework.context.ApplicationContext;
import org.springframework.web.portlet.context.PortletApplicationContextUtils;
import javax.portlet.PortletRequest;

import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.spp.portlets.reports.hppreport.command.HPPReport;
import com.hp.spp.portlets.reports.hppreport.command.SearchEntiyCommand;
import com.hp.spp.portlets.reports.hppreport.common.UserInfo;
import com.hp.spp.portlets.reports.hppreport.model.HPPReportModel;
import com.hp.spp.portlets.reports.hppreport.service.HPPReportService;
import com.hp.spp.portlets.reports.hppreport.service.HPPReportToolkit;
import com.hp.spp.profile.Constants;
import com.hp.spp.reports.hppreport.exception.HPPReportException;

/**
 * <p>The main controller class extends Spring's SimpleFormContoller.
 * This class receives the <code>HPPReport</code> command object from  HPP search,
 * invokes the <code>HPPReportService </code> to load user profile and
 * group information into the <code>HPPReportModel</code>
 * object
 * @author girishsk
 * @version SPP 2.0 intial
 *
 */

/** Revision History:
*
* Ver.   Modified By           Date           Notes
*--------------------------------------------------------------------------------------*
* v1     girishsk           29-Oct-2006      Created
* v1.1     girishsk           20-Apr-2006      Fixed bug related to concurrency
*
*/
public class HPPReportController extends SimpleFormController
	implements	InitializingBean {
	private static final Logger mLog = Logger.getLogger(HPPReportController.class);
	
	private static final String SPACE = " ";
	public void afterPropertiesSet() throws Exception {
		/* if (hppReportService == null)
			throw new IllegalArgumentException("An ReportService instance is required");
		*/
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response,
		Object command,	BindException errors) throws Exception {

		// Get a  command object
		HPPReport aHPPReport = (HPPReport) command;
		HPPReportService hppReportService = new HPPReportService();

		//Cretate (Hack) instance of HPPReportService and bind it to session
		setHppReportService(hppReportService, request);

		// Get the searchEntityName & searchEntityType and construct UserInfo object
		SearchEntiyCommand aSearchEntiyCommand = aHPPReport.getSearchEntityCommand();
		String entityIdentifier =  aSearchEntiyCommand.getEntityIdentifier();
		String entityType = aSearchEntiyCommand.getEntityType();

		UserInfo userInfo = new UserInfo(entityIdentifier,entityType);

		//Get the site
		String site = getSite(request);

		//Intialize the report model class
		boolean status = false;
		
		try{
			status = hppReportService.load(userInfo, site);
		}catch(HPPReportException hppRptEx){
			String error = hppRptEx.getMessage() +
				" The Search criteria : [ "+entityIdentifier+" ] and " +
					"The Search by : [ "+getSearchBy(entityType) +" ]";
			//mLog.error(error + HPPReportToolkit.getFaultsString(psex) +
			//		HPPReportToolkit.getStackTrace(psex));
			throw new HPPReportException(error);
		}catch(PassportServiceException psex){
			String error = "Error in passport service while loading user data for " +
			" search criteria [ "+entityIdentifier+" ] and " +
				" search by [ "+getSearchBy(entityType) +" ]";
			throw new PassportServiceException(error+HPPReportToolkit.getFaultsDescription(psex));
		}

		if(!status){
			String info = "User does not exist in the system for the " +
					" search criteria [ "+entityIdentifier+" ] and " +
					" search by [ "+getSearchBy(entityType) +" ]";
			mLog.info(info);
			throw new Exception(info);
		}
		
		//Get the report summary
		HPPReportModel aHPPReportModel = hppReportService.getReportSummary();

		//if the user exits with given identifier then laod returns true otherwise false
		if(status == false){
			aHPPReportModel.setInvalidSearch(true);
		}

		// Set the model into the HPPReport
		aHPPReport.setHppReportModel(aHPPReportModel);
	}

	protected ModelAndView onSubmitRender(RenderRequest request,
			RenderResponse response, Object command, BindException errors)
				throws Exception{
			HPPReport aHPPReport = (HPPReport) command;
			request.setAttribute("hppReport", aHPPReport);
			return new ModelAndView(getSuccessView(), "reportModel", aHPPReport);
	}

/*
	protected Object formBackingObject(PortletRequest request) throws Exception {
		HPPReport aHPPReport;
		aHPPReport = new HPPReport();
		return aHPPReport;
	}
*/
	private String getSite(ActionRequest request){
		Map userProfile = (Map) request.getAttribute("com.hp.spp.UserProfile");
		String siteID = (String) userProfile.get(Constants.MAP_SITE);
		mLog.debug("Site  : "+siteID);
		return siteID;
	}

	public void setHppReportService(HPPReportService service, PortletRequest request) {
		request.getPortletSession().setAttribute("hppReportService", service);
	}
	
	
	public HPPReportService getHppReportService(PortletRequest request) {
		return (HPPReportService)request.getPortletSession().getAttribute("hppReportService");
	}	

	private String getSearchBy(String searchEntityType){
		String searchEntityTypeName = null;
		if(SearchEntiyCommand.TYPE_USERID.equals(searchEntityType))
			searchEntityTypeName = "User ID";
		else if (SearchEntiyCommand.TYPE_HPPID.equals(searchEntityType))
			searchEntityTypeName = "HPP Passport ID";
		else
			searchEntityTypeName = "Email Address";
		return searchEntityTypeName;
	}
}