package com.hp.spp.portal.secondarypagetype.contactHP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.hp.globalops.hppcbl.passport.PassportService;
import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.globalops.hppcbl.passport.beans.Fault;
import com.hp.globalops.hppcbl.webservice.EmailTemplate;
import com.hp.spp.portal.common.sql.SPPSQLManager;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.enduser.components.ActionException;
import com.vignette.portal.website.enduser.components.BaseAction;
import org.apache.commons.lang.StringEscapeUtils;

public class ContactHPProcessAction extends BaseAction {

	private static Logger mLog = Logger.getLogger(ContactHPProcessAction.class);
	
	public PortalURI execute(PortalContext pc) throws ActionException {
		PortalURI portalURI = null ;
		boolean bSuccess = true ;
        ArrayList faults = new ArrayList() ;
        
        HttpServletRequest request = pc.getPortalRequest().getRequest();
        
        // TODO Get the good compliant LangCode for HPP
//		SesameWebServiceUtils wsUtils = SesameWebServiceUtils.getInstance() ;
//		String localeLanguage = wsUtils.getLangCode(LocaleParametersManager.getLangCode(pc));
//		String langCode = wsUtils.getLangCodeHPPCompliant(localeLanguage.toUpperCase());
		String localeLanguage = "en";
		String langCode = "en";

        //instantiate new webservice
        PassportService ws = new PassportService() ;
//		ws.setSystemLangCode(langCode) ;

		// Check form for none HPP fields
        bSuccess &= checkForm(request, faults, langCode) ;
        
        if(bSuccess)
        {
        	ArrayList row = getRow(request.getParameter("email_topic"), langCode) ;
			String userId = (String) row.get(0) ;
			String topicLabel = (String) row.get(1) ;
			String emailMessage = request.getParameter("email_message") ;
			String emailAddress = request.getParameter("email_address") ;
			EmailTemplate[] emailTemplate = retrievEmailTemplateFor(topicLabel, emailMessage, emailAddress) ;
			
			try {
				ws.sendEmail(userId, emailTemplate) ;
			} catch (PassportServiceException e) {
				if(e.getFaults() != null)
				{
					faults.addAll(e.getFaults()) ;
					displayFaults(faults) ;
				}
				else
					mLog.error("[Class]=ContactHPProcessAction [Method]=execute [Parameter]=Exception [Value]=".concat(e.getMessage())) ;
	        	bSuccess &= false ;
			}
        }
		
		if (!bSuccess) {
			checkFaults(faults, localeLanguage, langCode) ;
			request.setAttribute("faults", faults);
		}
		request.setAttribute("forward", "true");
		
		portalURI = pc.getCurrentPageURI();
		portalURI.setForward(true);
		
		return portalURI;
	}

	private boolean checkForm(HttpServletRequest request, ArrayList faults, String langCode) {
		boolean bFailed = false ;
		
		String email_topic = request.getParameter("email_topic") ;
		String email_message = request.getParameter("email_message") ;
		String email_address = request.getParameter("email_address") ;

		//To avoid cross site scripting attack the email_message has been encoded.
		if(email_message != null){
			email_message=StringEscapeUtils.escapeHtml(email_message);
			mLog.debug("Encoded message value: "+ email_message);
		}
		
		ArrayList ruleNumber = new ArrayList() ;
		if(email_topic.equals(""+Integer.MAX_VALUE))
		{
			ruleNumber.add(new Integer(983)) ;
			mLog.error("[Class]=ContactHPProcessAction [Method]=execute [Parameter]=ruleNumber [Value]=983") ;
			bFailed |= email_topic.equals(""+Integer.MAX_VALUE) ;
		}
		if(email_message.equals(""))
		{
			ruleNumber.add(new Integer(982)) ;
			mLog.error("[Class]=ContactHPProcessAction [Method]=execute [Parameter]=ruleNumber [Value]=982") ;
			bFailed |= email_message.equals("") ;
		}	
		
		if(email_address.equals(""))
		{
			ruleNumber.add(new Integer(981)) ;
			mLog.error("[Class]=ContactHPProcessAction [Method]=execute [Parameter]=ruleNumber [Value]=981") ;
			bFailed |= email_address.equals("") ;
		}
		
		boolean testEmail = fieldEmailValidator(email_address);
		
		if(!testEmail)
		{
			ruleNumber.add(new Integer(980)) ;
			mLog.error("[Class]=ContactHPProcessAction [Method]=execute [Parameter]=ruleNumber [Value]=980") ;
			bFailed |= !testEmail;
		}
		
		if(bFailed)
			getFaultsByRuleNumber(faults, ruleNumber, langCode) ;
		
		return !bFailed ;
	}

	private ArrayList getRow(String topicCode, String langCode) {
		ArrayList row = null ;
		
		if(!"".equals(topicCode)) {
			StringBuffer request =  new StringBuffer("");
			request.append("SELECT userid, topiclabel ") ;
			request.append("FROM spp_contact_topics ") ;
			request.append("WHERE topiccode = "+topicCode+" ") ;
			request.append("AND languagecode = '"+langCode.toUpperCase()+"' ") ;
			
			ArrayList result = SPPSQLManager.getInstance().executeSelectQuery(request.toString()) ;
			
			if(result != null && !result.isEmpty()) {
				row = (ArrayList)result.get(0) ;
				
				if(row == null || row.isEmpty()) {
					row.add(null) ;
					row.add(null) ;
				}
			}
		}
		
		return row;
	}

	private EmailTemplate[] retrievEmailTemplateFor(String subject, String body, String emailAddress) {
		EmailTemplate[] emailTemplate = new EmailTemplate[1] ;
		emailTemplate[0] = new EmailTemplate() ; 
        emailTemplate[0].setSubject(subject) ;
        emailTemplate[0].setBody(body) ;
        emailTemplate[0].setTemplateType(EmailTemplate.WELCOME) ;
        emailTemplate[0].setFromAddress(emailAddress) ;
		
		return emailTemplate ;
	}
	
	private boolean fieldEmailValidator(String stringValidation){
		return stringValidation.matches("[a-z0-9._-]+[@]{1}[a-z0-9.-]+[.][a-z]{2,4}") ;
	}

	private void getFaultsByRuleNumber(ArrayList faults, ArrayList array, String langCode) {
		StringBuffer request = new StringBuffer("") ;
		request.append("select rulenumber, code, fieldname, max(description1), max(description2)   ") ;
		request.append("from(   ") ;
		request.append("SELECT  h.rulenumber, h.code, h.fieldname, h.description as description1, null as description2   ") ;
		request.append("FROM    spp_contact_faults h   ") ;
		request.append("WHERE   h.languagecode ='"+langCode.toUpperCase()+"'   ") ;
		request.append("		and (");
		for(int i = 0; i < array.size(); i++)
		{
			Integer value = (Integer)array.get(i) ;
			request.append("h.rulenumber = ") ;
			request.append(value.intValue()) ;
			request.append(" OR ") ;
		}
		request.append(" 1 <> 1)   ") ;
		request.append("union all         ") ;
		request.append("SELECT  h.rulenumber, h.code, h.fieldname, null as description1, h.description as description2   ") ;
		request.append("FROM    spp_contact_faults h   ") ;
		request.append("WHERE   h.languagecode ='en'    ") ;
		request.append("		and (");
		for(int i = 0; i < array.size(); i++)
		{
			Integer value = (Integer)array.get(i) ;
			request.append("h.rulenumber = ") ;
			request.append(value.intValue()) ;
			request.append(" OR ") ;
		}
		request.append(" 1 <> 1)   ") ;
		request.append(")   ") ;
		request.append("group by rulenumber, code, fieldname  ") ;
		
		ArrayList result = SPPSQLManager.getInstance().executeSelectQuery(request.toString()) ;
		
		if(result != null && !result.isEmpty()) {
			for(int i = 0; i < result.size();  i++) {
				ArrayList row = (ArrayList)result.get(i) ;
				Fault fault = new Fault() ;
				fault.setRuleNumber(Integer.parseInt(row.get(0).toString())) ;
				fault.setCode((String)row.get(1)) ;
				fault.setFieldName((String)row.get(2)) ;
				
				if (row.get(3)!=null && !"".equals(row.get(3))) {
					fault.setDescription((String)row.get(3)) ;
				} else{
					fault.setDescription((String)row.get(4)) ;
				}
				
				faults.add(fault) ;
			}
		}
		
		displayFaults(faults);
	}
	
	/**
	 * 
	 * @param faults list of some fault return by WebServices
	 * @param localeLanguage browser's language code
	 * @param langCode language code return by 'getLangCode' method
	 */
	private void checkFaults(ArrayList faults, String localeLanguage, String langCode) {
		if(faults != null 
		&& !faults.isEmpty() 
		&& localeLanguage != null 
		&& langCode != null 
		&& !langCode.equalsIgnoreCase(localeLanguage))
		{
			Collections.sort(faults) ;
			
			StringBuffer request = new StringBuffer("") ;
			request.append("SELECT h.description, h.rulenumber ") ;
			request.append("FROM spp_contact_faults h, spp_contact_language l ") ;
			request.append("WHERE l.languagecode = h.languagecode ") ;
			request.append("AND l.ishppcompliant = 0 ") ;
			request.append("AND UPPER(h.languagecode) = '") ;
			request.append(localeLanguage.toUpperCase()) ;
			request.append("' AND ( ") ;
			
			for(int i = 0; i < faults.size(); i++) {
				Fault fault = (Fault)faults.get(i) ;
				request.append("( h.rulenumber = ") ;
				request.append(fault.getRuleNumber()) ;
				request.append(" AND h.code = '") ;
				request.append(fault.getCode()) ;
				request.append("' AND h.fieldname = '") ;
				request.append(fault.getFieldName()) ;
				request.append("') OR ") ;
			}

			request.append("1 <> 1 )  ") ;
			request.append("ORDER BY rulenumber  ") ;
			
			ArrayList result = SPPSQLManager.getInstance().executeSelectQuery(request.toString()) ;

			boolean found = false;
			if(result != null && !result.isEmpty()) {
				for(int i = 0; i < faults.size(); i++) {
					found = false;
					Fault fault = (Fault)faults.get(i) ;
					for(int j = 0; j < faults.size() && !found; j++) {
						ArrayList row = (ArrayList)result.get(j) ;
						if ( Integer.parseInt((row.get(1).toString())) == fault.getRuleNumber() ){
							fault.setDescription((String)row.get(0)) ;
							found = true;
						}
					}
				}
			}
		}
	}

	private void displayFaults(ArrayList faults) {
		if (faults != null && faults.size() > 0) {
			Iterator iter = faults.iterator();
			while (iter.hasNext()) {
				Fault afault = (Fault) iter.next();
				mLog.error(afault.toString());
			}
		}
	}

	
}
