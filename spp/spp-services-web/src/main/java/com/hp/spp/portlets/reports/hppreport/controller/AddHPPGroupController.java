package com.hp.spp.portlets.reports.hppreport.controller;

import java.util.ArrayList;
import java.util.Iterator;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.SimpleFormController;
import javax.portlet.PortletRequest;

import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.globalops.hppcbl.passport.beans.Fault;
import com.hp.spp.portlets.reports.hppreport.command.HPPReport;
import com.hp.spp.portlets.reports.hppreport.model.HPPReportModel;
import com.hp.spp.portlets.reports.hppreport.service.HPPReportService;
import com.hp.spp.portlets.reports.hppreport.service.HPPReportToolkit;

/*
 * This controller is called when user clicks add hpp groups button in the view.
 * It will add a user to a group and prepares the model object and updated model
 * object will be set in to the command object. 
 *  
 * @author girishsk
 * 
 */

/** Revision History:
*
* Ver.   Modified By           Date           Notes
*--------------------------------------------------------------------------------------*
* v1     girishsk           29-Oct-2006      Created
* v1.1     girishsk           20-Apr-2006      Fixed bug related to concurrency
*/
public class AddHPPGroupController extends SimpleFormController 
	implements InitializingBean {
	private static final Logger mLog = Logger.getLogger(AddHPPGroupController.class);
	private static final String SPACE = " ";
	
	private HPPReportService hppReportService = null;

	public void afterPropertiesSet() throws Exception {
		/* if (hppReportService == null)
			throw new IllegalArgumentException("An ReportService instance is required");
		*/	
	}
	
	public void onSubmitAction(ActionRequest request, ActionResponse response,
			Object command,	BindException errors) throws Exception {
		 
		//Get the command object		
		HPPReport aHPPReport = (HPPReport) command;
		HPPReportService hppReportService = getHppReportService(request);

		//Get the group for which membership for the user needs to be added
		String groupToAdd = aHPPReport.getGroupCommand();
		//Add the user to the group 
		try{
			//Add the user to the group 
			hppReportService.addUserToGroup(groupToAdd);		 
		}catch(PassportServiceException psex){			
			String error = "Error in passport service while adding a " +
					"user to the group : [ "+ groupToAdd +" ]" ;
			//mLog.error(error + HPPReportToolkit.getFaultsString(psex) + 
			//		HPPReportToolkit.getStackTrace(psex));		
			throw new PassportServiceException(error+HPPReportToolkit.getFaultsDescription(psex));
		}
			
		//Populate the EntityIdentifier & EntityType into the SearchEntityCommand
		aHPPReport.getSearchEntityCommand().setEntityIdentifier(
				hppReportService.getUserInfo().getEntityIdentifier());
		aHPPReport.getSearchEntityCommand().setEntityType(
				hppReportService.getUserInfo().getEntityType());
		
		//Get the updated HPPReportModel object.
		HPPReportModel aHPPReportModel = hppReportService.getReportSummary();
		
		//Set the updated HPPReportModel into the  command object reference.
		aHPPReport.setHppReportModel(aHPPReportModel);
	}
	
	protected ModelAndView onSubmitRender(RenderRequest request, 
		RenderResponse response, Object command, BindException errors)throws Exception{		
		HPPReport aHPPReport = (HPPReport) command;	
		HPPReportService hppReportService = getHppReportService(request);
		//Get the report summary directly from the service stored in session
		HPPReportModel aHPPReportModel = hppReportService.getReportSummary();
		//Set the  HPPReportModel into the HPPReport command
		aHPPReport.setHppReportModel(aHPPReportModel);

		request.setAttribute("hppReport", aHPPReport);
		return new ModelAndView(getSuccessView(), "reportModel", aHPPReport);
	}
	
	public HPPReportService getHppReportService(PortletRequest request) {
		return (HPPReportService)request.getPortletSession().getAttribute("hppReportService");
	}	
}