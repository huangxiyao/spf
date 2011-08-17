package com.hp.spp.portlets.reports.hppreport.service;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.hp.globalops.hppcbl.passport.PassportService;
import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.globalops.hppcbl.passport.beans.Fault;
import com.hp.spp.hpp.admin.HPPAdminPasswordHelper;
import com.hp.spp.hpp.exception.HPPAdminException;
import com.hp.spp.reports.hppreport.exception.HPPReportException;

/**
* Wrapper utility class to access key HPPID and HPP login attributes.
* @author girishsk
* @version 2.0 intial
*/

/** Revision History:
*
* Ver.   Modified By           Date           Notes
*--------------------------------------------------------------------------------------*
* v1     girishsk            30-Oct-2006      Created
* v2	 girishsk			10-Dec-2006		  Deleted method getProfileId
*
*/
public class HPPReportToolkit {
	private Logger mLog = Logger.getLogger(HPPReportToolkit.class);
	private PassportService mPassportService;
	private String mAdminName;
	private String mAdminPassword;
	private String mSiteName;

	public HPPReportToolkit(){
		super();
		mPassportService = new PassportService();
	}

	public  PassportService getPassportService(){
		return mPassportService;
	}
	public String getAdminSessionToken() throws PassportServiceException {
		mLog.debug("mAdminName : "+mAdminName);
		mLog.debug("mAdminPassword : "+mAdminPassword);
		String adminSessionToken =
			(mPassportService.login(mAdminName, mAdminPassword))
				.getSessionToken();
		return adminSessionToken;
	}


	/**
	 * Loads the admin name/password from HPP.
	 * @param siteName
	 */

	public void  loadHPPAdminCredentials(String siteName) throws HPPReportException{
		 mSiteName = siteName;
		 HPPAdminPasswordHelper hppAdminHelper = null;
		 try {
			hppAdminHelper = new HPPAdminPasswordHelper();			
			mAdminName = hppAdminHelper.fetchHPPAdminLoginId(siteName);
			mAdminPassword = hppAdminHelper.fetchHPPAdminPassword(siteName);
			//System.out.println(mAdminPassword);
		} catch (HPPAdminException e) {
			// Simply wrap the HPPAdminException into the application specific exception
			// application specific exception and throw back		
			throw new HPPReportException(e); 
			
		} 
	}

	public static  String getFaultsString(PassportServiceException psex) {
		StringBuffer faultsString = 
			new StringBuffer("\n Passport Service  - Faults : \n");
		ArrayList faults = psex.getFaults();		
		if (faults!=null && faults.size() > 0) {
			Iterator iter = faults.iterator();
			while (iter.hasNext()) {
				Fault afault = (Fault) iter.next();
				faultsString.append(afault.toString());
			}
		}
		return faultsString.toString();
	}
	
	public static  String getFaultsDescription(PassportServiceException psex) {
		StringBuffer faultsString = 
			new StringBuffer("\n Passport Service Description : \n");
		ArrayList faults = psex.getFaults();		
		if (faults!=null && faults.size() > 0) {
			Iterator iter = faults.iterator();
			while (iter.hasNext()) {
				Fault afault = (Fault) iter.next();
				faultsString.append(afault.getDescription()).append("\n");
			}
		}
		return faultsString.toString();
	}
	
	public static String getStackTrace(Exception ex){
		StringBuffer stackTrace = 
				new StringBuffer("\n Stack Trace : \n");		
		StackTraceElement st[] = ex.getStackTrace();		
		for (int i =0; i < st.length; i++) {
			stackTrace.append(st[i].toString()).append("\n");
		}
		return stackTrace.toString(); 
	}
}

