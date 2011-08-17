package com.hp.spp.portlets.reports.userageing.contoller;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.SimpleFormController;


import org.apache.log4j.Logger;

import com.hp.spp.portlets.reports.userageing.commands.LastLoginCommand;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;

/**
 * <p> Main control class implemented as Spring's <code>SimpleFormController</code>
 * Flow: This class gets the <code>LastLoginCommand</code> object on jsp (report.jsp)
 * form submisson and invokes the <code>AgegingReportService</code> class to fetch
 * user information finally redirecting to the view page.
 * 
 * @author girishsk
 * @version SPP 2.0 intial
 * @see org.springframework.web.portlet.mvc.SimpleFormController
 * @see org.springframework.web.portlet.ModelAndView
*/

public class AgeingReportController extends SimpleFormController implements
InitializingBean {
	
	private static final Logger mLog = Logger.getLogger(AgeingReportController.class);


	public AgeingReportController(){
	
	}
	
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		///if (hppReportService == null)
		//	throw new IllegalArgumentException("An ReportService instance is required");
	}


	public void onSubmitAction(ActionRequest request, ActionResponse response,
			Object command,	BindException errors) throws Exception {
		mLog.debug("Calling onSubmitAction ============");
		
		LastLoginCommand mLoginCommand = (LastLoginCommand) command;
		mLog.debug("Number of days since last login"+mLoginCommand.getLastLoginNumDays());
		String numDays = mLoginCommand.getLastLoginNumDays();
		//Set the action phase of XLSReport
		response.setRenderParameter("action","XLSReport");
		response.setRenderParameter("lastLoginDate", numDays);
	}
	
	protected ModelAndView onSubmitRender(RenderRequest request, RenderResponse response, Object command, BindException errors) 
	 throws Exception{
		mLog.debug("Render parameter action value" + request.getParameter("action"));
		mLog.debug("Render parameter last login days value" + request.getParameter("lastLoginDate"));
		mLog.debug("Calling onSubmitRendere ============");
		if("XLSReport".equals(request.getParameter("action"))){
			//Pass on model to the success view.
			return new ModelAndView(getSuccessView(),"lastLoginDate", request.getParameter("lastLoginDate"));
		}
		mLog.debug("Calling getFormView ============");
		
		return new ModelAndView(getFormView());
	}
	
}
