package com.hp.globalops.hppcbl.client;

import java.util.ArrayList;
import java.util.Iterator;

import com.hp.globalops.hppcbl.passport.PassportService;
import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.globalops.hppcbl.passport.beans.Fault;
import com.hp.globalops.hppcbl.passport.manager.IConstantPassportService;
import com.hp.globalops.hppcbl.passport.manager.PassportParametersManager;
import com.hp.globalops.hppcbl.webservice.AdminViewUserResponseElement;
import com.hp.globalops.hppcbl.webservice.EmailTemplate;
import com.hp.globalops.hppcbl.webservice.GetUserGroupsResponseElement;
import com.hp.globalops.hppcbl.webservice.GroupRole;
import com.hp.globalops.hppcbl.webservice.ProfileCore;
import com.hp.globalops.hppcbl.webservice.ProfileCredentials;
import com.hp.globalops.hppcbl.webservice.ProfileExtended;
import com.hp.globalops.hppcbl.webservice.ProfileIdentity;

/**
 * Created by IntelliJ IDEA. User: millerand Date: Aug 23, 2004 Time: 11:00:01
 * AM To change this template use File | Settings | File Templates.
 */
public class DocLiteralClient {

	public static void main(String[] args) {

		try {
			// instantiate new webservice
			PassportService ws = new PassportService();
//			ws.setSystemLangCode("fr");
			PassportParametersManager wsManager = PassportParametersManager.getInstance() ;

			String userId = "sppuser" ;
			String password = "Azerty38" ;
			String profileId = "09ab3760101b49692f3983b419bd484d" ;
			String email = "" ;
			

			
//			String newPassword= "E$kenl8P";
			
			
//			ws.changePassword(userId,password,newPassword,newPassword);
//			if (true){
//				throw new Exception();
//			}
			
			
			

			ProfileCore profileCore = null ;
			ProfileCredentials profileCredentials = null ;
			ProfileExtended profileExtended = null ;
			ProfileIdentity profileIdentity = null ;
			
			String oldPassword = password ;
//			String newPassword = null ;
			
//			newPassword = "Tmp_Pwd_1" ;
//			ws.changePassword(userId, password, newPassword, newPassword) ;
//			password = newPassword ;
//			
//			newPassword = "Tmp_Pwd_2" ;
//			ws.changePassword(userId, password, newPassword, newPassword) ;
//			password = newPassword ;
//			
//			newPassword = "Tmp_Pwd_3" ;
//			ws.changePassword(userId, password, newPassword, newPassword) ;
//			password = newPassword ;
//			
//			newPassword = "Tmp_Pwd_4" ;
//			ws.changePassword(userId, password, newPassword, newPassword) ;
//			password = newPassword ;
//			
//			newPassword = "Tmp_Pwd_5" ;
//			ws.changePassword(userId, password, newPassword, newPassword) ;
//			password = newPassword ;
//			
//			newPassword = oldPassword ;
//			ws.changePassword(userId, password, newPassword, newPassword) ;
//			password = newPassword ;
//			

			String adminSessionToken = null ;
			System.out.println(wsManager.getMode());
			if(wsManager.getMode().equals(IConstantPassportService.SYSTEST_MODE))
				adminSessionToken = (ws.login("sppgrpadmin", "E$kenl8P")).getSessionToken() ;
			//	adminSessionToken = (ws.login("littleAdmin", "little1Admin")).getSessionToken() ;
//				adminSessionToken = (ws.login("adminvgn", "YN5TsHQ/")).getSessionToken() ;
			else if(wsManager.getMode().equals(IConstantPassportService.STAGING_MODE))
//				adminSessionToken = (ws.login("littleAdmin", "little1Admin")).getSessionToken() ;
				adminSessionToken = (ws.login("sppgrp", "gR#45X!$")).getSessionToken() ;
			else if(wsManager.getMode().equals(IConstantPassportService.PRODUCTION_MODE))
				adminSessionToken = (ws.login("sppgrp", "Xj#LBSbE")).getSessionToken() ;
//				adminSessionToken = (ws.login("adminvgn", "passport1")).getSessionToken() ;
//			System.out.println(adminSessionToken);
			
//			String sessionToken = null ;
//			if(!"".equals(userId) && !"".equals(password)) {
//				sessionToken = ws.login(userId, password).getSessionToken() ;
//				System.out.println("sessionToken: "+sessionToken);
//			}
			
			AdminViewUserResponseElement adminview = ws.adminViewUser(adminSessionToken, userId, "userId") ;
//			AdminViewUserResponseElement adminview = ws.adminViewUser(adminSessionToken, profileId, "profileId") ;
//			AdminViewUserResponseElement adminview = ws.adminViewUser(adminSessionToken, email, "email") ;
			System.out.println("UserID: "+adminview.getProfileIdentity().getUserId());
			System.out.println("ProfileId: "+adminview.getProfileIdentity().getProfileId());
			System.out.println("") ;
			System.out.println("FirstName: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getFirstName()) ;
			System.out.println("LastName: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getLastName()) ;
			System.out.println("CountryCode: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getResidentCountryCode()) ;
			System.out.println("SecurityQuestion: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getSecurityQuestion()) ;
			System.out.println("SecurityAnswer: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getSecurityAnswer()) ;
			System.out.println("Email: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getEmail()) ;
			System.out.println("Securitylevel: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getSecurityLevel()) ;
			System.out.println("") ;
			System.out.println("LastSuccessfulLoginDate: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getSystemData().getLastSuccessfulLoginDate()) ;
			System.out.println("LastAttemptedLoginDate: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getSystemData().getLastAttemptedLoginDate()) ;
			System.out.println("PasswordCreationDate: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getSystemData().getPasswordCreationDate()) ;
			System.out.println("") ;
			
			/**
			 * TODO UserId exist and if yes, get profileId
			 */
			profileId = ws.getProfileId(userId).getProfileId() ;
			System.out.println("profileId: "+profileId) ;
//				
//			/**
//			 * TODO profileId exist and if yes, get UserId
//			 */
//			profileIdentity = new ProfileIdentity() ;
//			profileIdentity.setProfileId(profileId) ;
//			userId = ws.getUserId(profileIdentity).getUserId() ;
//			System.out.println(userId) ;
//
//			/**
//			 * TODO get privacy statment
//			 */
//			GetUserResponseElement guResponse = ws.getUser(sessionToken) ;
//			profileIdentity = guResponse.getProfileIdentity() ;
//			profileCore = guResponse.getProfileCore() ;
//			System.out.println("email: " + profileCore.getEmail());
//			profileCore.setEmail("cyril.micoud@capgemini.com") ;
//			profileCore.setSecurityLevel(null) ;
//			profileCore.setSecurityQuestion(null) ;
//			profileCore.setSecurityAnswer(null) ;
//			System.out.println("PrefEmail: " + profileCore.getContactPrefEmail());
//			System.out.println("PrefPost: " + profileCore.getContactPrefPost());
//			System.out.println("PrefTele: " + profileCore.getContactPrefTelephone());
			
//			ws.adminModifyUser(adminSessionToken, profileIdentity.getProfileId(), null, profileCore, null) ;
//			profileCore = new ProfileCore() ;
//			profileCore.setSecurityQuestion("Of who are you the master?") ;
//			profileCore.setSecurityAnswer("daniel DOT saier AT vignette DOT com") ;
//			
//			ws.modifyUser(sessionToken, profileCore, null) ;
//			
//			/**
//			 * TODO addUserToGroup with USER status
//			 */
//			profileIdentity = new ProfileIdentity() ;
//			profileIdentity.setUserId(userId) ;
//			ws.addUserToGroup(adminSessionToken, profileIdentity, "SPPQA", "user", null) ;
/////			ws.addUserToGroup(adminSessionToken, profileIdentity, "SESAME_TRANSFER", "user", null) ;
//			
//			/**
//			 * TODO removeUserFromGroup with USER status
//			 */
//			profileIdentity = new ProfileIdentity() ;
//			profileIdentity.setUserId(userId) ;
//			ws.removeUserFromGroup(adminSessionToken, profileIdentity, "SESAME", "user", null) ;
//			ws.removeUserFromGroup(adminSessionToken, profileIdentity, "SESAME_TRANSFER", "user", null) ;
//			
//			/**
//			 * TODO addUserToGroup with ADMIN status
//			 */
//			profileIdentity = new ProfileIdentity() ;
//			profileIdentity.setUserId(userId) ;
//			ws.addUserToGroup(adminSessionToken, profileIdentity, "SESAME", "admin", null) ;
//			
//			/**
//			 * TODO removeUserFromGroup with ADMIN status
//			 */
//			profileIdentity = new ProfileIdentity() ;
//			profileIdentity.setUserId(userId) ;
//			ws.removeUserFromGroup(adminSessionToken, profileIdentity, "SESAME", "admin", null) ;
			
			/**
			 * TODO check if user is in SESAME group
			 */
			profileIdentity = new ProfileIdentity();
			profileIdentity.setUserId(userId);
			GetUserGroupsResponseElement response = ws.getUserGroups(adminSessionToken, profileIdentity);
			System.out.println("Count: "+response.getGroupRoleCount());
			for (int i = 0; i < response.getGroupRoleCount(); i++) 
			{
				GroupRole groupRole = response.getGroupRole(i) ;
				System.out.println("Group: "+groupRole.getGroupName());
				System.out.println("Role: "+groupRole.getRoleName());
			}
			
//			GetMembersOfGroupResponseElement res = ws.getMembersOfGroup(adminSessionToken, "SESAME", "admin") ;
//			Enumeration enumerate = res.enumerateUserId() ;
//			while(enumerate.hasMoreElements()) {
//				System.out.println(enumerate.nextElement());
//			}
			
//			profileCore = new ProfileCore() ;
//			profileCore.setEmail("cyril.micoud@capgemini.com") ;
//			profileExtended = new ProfileExtended() ;
//			ws.adminModifyUser(adminSessionToken, profileId, null, profileCore, profileExtended) ;
//			
			
//			#################################################################################################################################			
//			#################################################################################################################################			
//			#################################################################################################################################			
//			#################################################################################################################################			
//			#################################################################################################################################			
//			#################################################################################################################################			
//			#################################################################################################################################			
//			#################################################################################################################################			
//			#################################################################################################################################			
//			#################################################################################################################################			
//			#################################################################################################################################			
			
			/*
			String link = "https://prmnt20.bbn.hp.com/portal/site/smartportal/template.sesameResetPwd/";
			link = link.concat("\\?").concat("userId").concat("=\\$userId\\$");
			link = link.concat("\\&").concat("email").concat("=\\$email\\$");
			link = link.concat("\\&").concat("GUID").concat("=\\$guid\\$");
			
			String replace ;
			String currentBody ;

			EmailTemplate[] emailTemplate = new EmailTemplate[3] ;
			emailTemplate[0] = new EmailTemplate() ;
			emailTemplate[0].setBody("Cher(e) $firstName$ $lastName$, \r\n\r\n* Merci de ne pas répondre à cet e-mail * \r\n\r\nVous avez demandé la réinitialisation de votre mot de passe pour le site HP Smart Portal. Merci de cliquer sur le lien suivant dans les prochaines 24 heures pour changer votre mot de passe. \r\n\r\n#Countrylink# \r\n\r\nVous serez amené à saisir votre login, adresse e-mail, et à répondre à votre question sécurité. Vous pourrez alors saisir votre nouveau mot de passe. \r\n\r\nSi vous avez besoin d'aide, merci de contacter Infoline.pts@hp.com .\r\n\r\nCordialement, \r\n\r\nL'équipe HP Smart Portal\r\n\r\n     Anglais :   +49 7031 468 3288\r\n     Français :     +49 7031 468 3286\r\n     Allemand :   +49 7031 468 3287\r\n      Italien :      +49 7031 468 3291\r\n     Espagnol :   +49 7031 468 3285 \r\n") ;
			emailTemplate[0].setSubject("Réinitialisation de votre mot de passe pour le site HP Smart Portal") ;
			emailTemplate[0].setIsHTML(false) ;
			emailTemplate[0].setFromAddress("support@hp.com") ;
			emailTemplate[0].setTemplateType(EmailTemplate.RESET_PWD_NO_QA) ;
			
			replace = link.concat("\\&sLevel=").concat(Integer.toString(1)) ;
			currentBody = emailTemplate[0].getBody();
			emailTemplate[0].setBody(currentBody.replaceAll("#Countrylink#", replace));
			
			emailTemplate[1] = new EmailTemplate() ;
			emailTemplate[1].setBody("Cher(e) $firstName$ $lastName$, \r\n\r\n* Merci de ne pas répondre à cet e-mail * \r\n\r\nVous avez demandé la réinitialisation de votre mot de passe pour le site HP Smart Portal. Merci de cliquer sur le lien suivant dans les prochaines 24 heures pour changer votre mot de passe. \r\n\r\n#Countrylink# \r\n\r\nVous serez amené à saisir votre login, adresse e-mail, et à répondre à votre question sécurité. Vous pourrez alors saisir votre nouveau mot de passe. \r\n\r\nSi vous avez besoin d'aide, merci de contacter Infoline.pts@hp.com .\r\n\r\nCordialement, \r\n\r\nL'équipe HP Smart Portal\r\n\r\n     Anglais :   +49 7031 468 3288\r\n     Français :     +49 7031 468 3286\r\n     Allemand :   +49 7031 468 3287\r\n      Italien :      +49 7031 468 3291\r\n     Espagnol :   +49 7031 468 3285 \r\n") ;
			emailTemplate[1].setSubject("Réinitialisation de votre mot de passe pour le site HP Smart Portal") ;
			emailTemplate[1].setIsHTML(false) ;
			emailTemplate[1].setFromAddress("support@hp.com") ;
			emailTemplate[1].setTemplateType(EmailTemplate.RESET_PWD_QA_EXCEEDZERO_LEVEL) ;
			
			replace = link.concat("\\&sLevel=").concat(Integer.toString(0)) ;
			currentBody = emailTemplate[1].getBody();
			emailTemplate[1].setBody(currentBody.replaceAll("#Countrylink#", replace));
			
			emailTemplate[2] = new EmailTemplate() ;
			emailTemplate[2].setBody("Cher(e) $firstName$ $lastName$, \r\n\r\n* Merci de ne pas répondre à cet e-mail * \r\n\r\nVous avez demandé la réinitialisation de votre mot de passe pour le site HP Smart Portal. Merci de cliquer sur le lien suivant dans les prochaines 24 heures pour changer votre mot de passe. \r\n\r\n#Countrylink# \r\n\r\nVous serez amené à saisir votre login, adresse e-mail, et à répondre à votre question sécurité. Vous pourrez alors saisir votre nouveau mot de passe. \r\n\r\nSi vous avez besoin d'aide, merci de contacter Infoline.pts@hp.com .\r\n\r\nCordialement, \r\n\r\nL'équipe HP Smart Portal\r\n\r\n     Anglais :   +49 7031 468 3288\r\n     Français :     +49 7031 468 3286\r\n     Allemand :   +49 7031 468 3287\r\n      Italien :      +49 7031 468 3291\r\n     Espagnol :   +49 7031 468 3285 \r\n") ;
			emailTemplate[2].setSubject("Réinitialisation de votre mot de passe pour le site HP Smart Portal") ;
			emailTemplate[2].setIsHTML(false) ;
			emailTemplate[2].setFromAddress("support@hp.com") ;
			emailTemplate[2].setTemplateType(EmailTemplate.RESET_PWD_QA_ZERO_LEVEL) ;
			
			replace = link.concat("\\&sLevel=").concat(Integer.toString(-1)) ;
			currentBody = emailTemplate[2].getBody();
			emailTemplate[2].setBody(currentBody.replaceAll("#Countrylink#", replace));
			
			ws.resetPassword(userId, email, emailTemplate) ;
			
			ws.updateCredentials("MRHCVIP7RTAXCD0GOXVPHKPVVJM3VKIA", "3;9zTky;", "3;9zTky;", null, null, userId);
			*/

			
			/*
			EmailTemplate[] emailTemplate = new EmailTemplate[2] ;
			emailTemplate[0] = new EmailTemplate() ;
			emailTemplate[0].setBody("<html><body><p><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>Cher(e) $firstName$ $lastName$, </span></p><p><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>* Merci de ne pas répondre à cet e-mail * </span></p><p><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>Vous avez demandé la réinitialisation de votre mot de passe pour le site HP Smart Portal. Merci de cliquer sur le lien suivant dans les prochaines 24 heures pour changer votre mot de passe. </span></p><p><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>  #Countrylink# </span></p><p><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>Vous serez amené à saisir votre login, adresse e-mail, et à répondre à votre question sécurité. Vous pourrez alors saisir votre nouveau mot de passe. </span></p><p><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>Si vous avez besoin d'aide, merci de contacter</span><span  style='font-size:10.0pt;font-family:\"Futura Bk\";\"Futura Bk\"'><a href=\"mailto:Infoline.pts@hp.com\">Infoline.pts@hp.com</a></span><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>.</span></p><p><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>Cordialement, </span></p><p ><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>L'équipe HP Smart Portal</span></p><p><span style='font-size:7.5pt;font-family:\"Futura Bk\";\"Arial Unicode MS\";color:blue'>&nbsp;<strong>&nbsp;&nbsp;</strong>&nbsp; </span><span  style='font-size:7.5pt;font-family:\"Futura Bk\";\"Arial Unicode MS\"'>Anglais :&nbsp;&nbsp;&nbsp;+49 7031 468 3288<br>&nbsp;&nbsp; &nbsp;Français :&nbsp;&nbsp;&nbsp;&nbsp; +49 7031 468 3286<br>&nbsp;&nbsp;&nbsp;&nbsp;Allemand :&nbsp;&nbsp;&nbsp;+49 7031 468 3287<br>&nbsp;&nbsp;&nbsp; &nbsp;Italien :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; +49 7031 468 3291<br>&nbsp;&nbsp;&nbsp;&nbsp;Espagnol :&nbsp;&nbsp;&nbsp;+49 7031 468 3285</span></p><br /><a href=\"http://www.blab.com/?guid=$guid$\">link</a></body></html>") ;
			emailTemplate[0].setSubject("Réinitialisation de votre mot de passe pour le site HP Smart Portal") ;
			emailTemplate[0].setIsHTML(true) ;
			emailTemplate[0].setFromAddress("support@hp.com") ;
			emailTemplate[0].setTemplateType(EmailTemplate.ADMIN_RESET_PWD_LINK) ;
			
			emailTemplate[1] = new EmailTemplate() ;
			emailTemplate[1].setBody("<html><body><p><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>Cher(e) $firstName$ $lastName$, </span></p><p><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>* Merci de ne pas répondre à cet e-mail * </span></p><p><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>Vous avez demandé la réinitialisation de votre mot de passe pour le site HP Smart Portal. Merci de cliquer sur le lien suivant dans les prochaines 24 heures pour changer votre mot de passe. </span></p><p><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>  #Countrylink# </span></p><p><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>Vous serez amené à saisir votre login, adresse e-mail, et à répondre à votre question sécurité. Vous pourrez alors saisir votre nouveau mot de passe. </span></p><p><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>Si vous avez besoin d'aide, merci de contacter</span><span  style='font-size:10.0pt;font-family:\"Futura Bk\";\"Futura Bk\"'><a href=\"mailto:Infoline.pts@hp.com\">Infoline.pts@hp.com</a></span><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>.</span></p><p><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>Cordialement, </span></p><p ><span style='font-size:10.0pt;font-family:\"Futura Bk\"'>L'équipe HP Smart Portal</span></p><p><span style='font-size:7.5pt;font-family:\"Futura Bk\";\"Arial Unicode MS\";color:blue'>&nbsp;<strong>&nbsp;&nbsp;</strong>&nbsp; </span><span  style='font-size:7.5pt;font-family:\"Futura Bk\";\"Arial Unicode MS\"'>Anglais :&nbsp;&nbsp;&nbsp;+49 7031 468 3288<br>&nbsp;&nbsp; &nbsp;Français :&nbsp;&nbsp;&nbsp;&nbsp; +49 7031 468 3286<br>&nbsp;&nbsp;&nbsp;&nbsp;Allemand :&nbsp;&nbsp;&nbsp;+49 7031 468 3287<br>&nbsp;&nbsp;&nbsp; &nbsp;Italien :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; +49 7031 468 3291<br>&nbsp;&nbsp;&nbsp;&nbsp;Espagnol :&nbsp;&nbsp;&nbsp;+49 7031 468 3285</span></p><br />Use the following PIN:<br />$pin$</body></html>") ;
			emailTemplate[1].setSubject("Réinitialisation de votre mot de passe pour le site HP Smart Portal") ;
			emailTemplate[1].setIsHTML(true) ;
			emailTemplate[1].setFromAddress("support@hp.com") ;
			emailTemplate[1].setTemplateType(EmailTemplate.ADMIN_RESET_PWD_PIN) ;
			
			ws.adminResetPassword(adminSessionToken, (ws.getProfileId(userId)).getProfileId(), emailTemplate) ;
			
			ws.validatePin("VUONK9WDGACJ9WZIBTGHLYEBFTQJNUGC", "", "", "", "", "fw9ere") ;
			*/

//			userId = "Sesame6_GPP1.0_SIT2" ;
//			profileCore = new ProfileCore() ;
//			profileExtended = new ProfileExtended() ;
//			profileCore.setContactPrefEmail("N") ;
//			profileCore.setEmail("Sesame6_GPP1.0_SIT2@hotmail.com") ;
//			profileCore.setFirstName("Test") ;
//			profileCore.setLangCode("en") ;
//			profileCore.setLastName("User") ;
//			profileCore.setResidentCountryCode("VC") ;
//			profileCore.setSecurityQuestion("SecurityQuestion") ;
//			profileCore.setSecurityAnswer("SecurityAnswer") ;
//			profileCore.setSecurityLevel("2") ;
//			profileExtended = null ;
//			profileCredentials = new ProfileCredentials() ;
//			profileCredentials.setPassword("Sesame6_GPP1") ;
//			profileCredentials.setPasswordConfirm("Sesame6_GPP1") ;
//			
//			ws.createUser(userId, profileCore, profileExtended, profileCredentials) ;
			
//			EmailTemplate[] emailTemplates = new EmailTemplate[1] ;
//			emailTemplates[0] = new EmailTemplate() ;
//			emailTemplates[0].setTemplateType(EmailTemplate.WELCOME) ;
//			emailTemplates[0].setFromAddress("ta@tou.mi") ;
//			emailTemplates[0].setBody("test") ;
//			emailTemplates[0].setSubject("test") ;
//			String[] ccAddress = new String[1] ;
//			ccAddress[0] = "cyril.micoud@hp.com" ;
//			String[] bccAddress = new String[1] ;
//			bccAddress[0] = "cyril.micoud@capgemini.com" ;
//			emailTemplates[0].setCcAddress(ccAddress) ;
//			emailTemplates[0].setBccAddress(bccAddress) ;
//			ws.sendEmail(userId, emailTemplates) ;
			
		} catch (PassportServiceException pse) {
			if(pse.getFaults() != null)
				displayFaults(pse.getFaults()) ;
			else
				System.out.println(pse.getMessage());
		} catch (Exception Exp) {
			System.out.println(Exp.getMessage());
		}
	}

	private static void displayFaults(ArrayList faults) {
		if (faults.size() > 0) {
			Iterator iter = faults.iterator();
			while (iter.hasNext()) {
				Fault afault = (Fault) iter.next();
				System.out.println(afault);
			}
		}
	}
}
