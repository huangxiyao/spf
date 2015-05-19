package com.hp.it.cas.persona.web;

import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import com.hp.it.cas.persona.configuration.service.IUserAttribute;
import com.hp.it.cas.persona.service.IPersonaAdminService;
import com.hp.it.cas.persona.uav.service.EUserIdentifierType;

/**
 * CompoundContentManagementController is to deal compund content browsing and
 * editing requests
 * 
 * @author Ding Kai-Jian
 * 
 */
public class CompoundContentManagementController extends AbstractController {

	private static final String ISADMIN = "isAdmin";

	private static final String ADD = "add";

	private static final String SAVE = "save";

	private static final String ACTIVITY = "activity";

	private String mainViewName;

	private String contentViewName;

	private IPersonaAdminService personaService;

	/**
	 * get personaService
	 * 
	 * @return IPersonaAdminService
	 */
	public IPersonaAdminService getPersonaService() {
		return personaService;
	}

	/**
	 * set personaService
	 * 
	 * @param IPersonaAdminService
	 *            personaService
	 */
	public void setPersonaService(IPersonaAdminService personaService) {
		this.personaService = personaService;
	}

	/**
	 * get contentViewName
	 * 
	 * @return String
	 */
	public String getContentViewName() {
		return contentViewName;
	}

	/**
	 * set contentViewName
	 * 
	 * @param String
	 *            contentViewName
	 */
	public void setContentViewName(String contentViewName) {
		this.contentViewName = contentViewName;
	}

	/**
	 * get mainViewName
	 * 
	 * @return String
	 */
	public String getMainViewName() {
		return mainViewName;
	}

	/**
	 * set mainViewName
	 * 
	 * @param String
	 *            mainViewName
	 */
	public void setMainViewName(String mainViewName) {
		this.mainViewName = mainViewName;
	}

	/**
	 * Find out all compound user attribute attached in this user. List all
	 * simple user attributes, if some simple user attribute is not in the user,
	 * leave the value blank
	 * 
	 * @param request
	 *            a <code>RenderRequest</code>.
	 * @param response
	 *            a <code>RenderResponse</code>.
	 * @return a <code>ModelAndView</code>.
	 * @throws Exception
	 *             exception.
	 */
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		ModelAndView view = new ModelAndView(mainViewName);

		// use simple attribute key to search
		String userIdentifier = request.getParameter("userIdentifier");
		String userIdentifierTypeCode = request
				.getParameter("userIdentifierTypeCode");
		if (userIdentifierTypeCode == null) {
			userIdentifierTypeCode = EUserIdentifierType.EXTERNAL_USER
					.getUserIdentifierTypeCode();
		}
		try {
			if (userIdentifier != null && !"".equals(userIdentifier.trim())) {
				if (Utils.isPersonaAdmin(request)) {
					view.addObject("availableUserAttributeList", personaService
							.listAllCompoundUserAttributes());
					view.addObject(ISADMIN, true);
				}

				Set<UserAttribute> simpleUserAttributes = personaService
						.listSimpleUserAttributesInCompoundForUser(
								userIdentifier, userIdentifierTypeCode);
				// retrieve activity value
				String activity = request.getParameter(ACTIVITY);
				if (ADD.equals(activity)) {
					String compoundUserAttributeIdentifier = request
							.getParameter("userAttributeIdentifier");
					Set<IUserAttribute> simpleAttributesInCompound = personaService
							.listSimpleUserAttributesForCompound(compoundUserAttributeIdentifier);
					String compoundUserAttributeName = personaService
							.findCompoundUserAttributeById(
									compoundUserAttributeIdentifier)
							.getUserAttributeName();
					for (IUserAttribute userAttribute : simpleAttributesInCompound) {
						UserAttribute userAttr = new UserAttribute(
								compoundUserAttributeName, userAttribute
										.getUserAttributeName(), null);
						userAttr.setCompoundUserAttributeIdentifier(compoundUserAttributeIdentifier);
						userAttr.setSimpleUserAttributeIdentifier(userAttribute
								.getUserAttributeIdentifier());
						simpleUserAttributes.add(userAttr);
					}
				}
				view.addObject("attributeList", simpleUserAttributes);
			}
		} catch (IllegalStateException ex) {
			view.addObject("errorMsg", ex.getMessage());
		} catch (Exception ex) {
			view.addObject("errorMsg", ex.getMessage());
		} finally {
			view.addObject("userIdentifierTypeCode", userIdentifierTypeCode);
			view.addObject("userIdentifierTypeName", EUserIdentifierType
					.valueOfUserIdentifierTypeCode(userIdentifierTypeCode)
					.getUserIdentifierName());
			view.addObject("userIdentifier", userIdentifier);
			if (request.getParameter("errorMsg") != null) {
				view.addObject("errorMsg", request.getParameter("errorMsg"));
			}
		}
		return view;
	}

	/**
	 * to add, save or delete attributes
	 * 
	 * @param request
	 *            a <code>ActionRequest</code>.
	 * @param response
	 *            a <code>ActionResponse</code>.
	 * @throws Exception
	 *             exception.
	 */
	protected void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		// retrieve activity value to indicate what currect operation is.
		String activity = request.getParameter(ACTIVITY);
		String userIdentifier = request.getParameter("userIdentifier");
		String userIdentifierTypeCode = request
				.getParameter("userIdentifierTypeCode");
		try {
			if (!Utils.isPersonaAdmin(request) && SAVE.equals(activity)) {
				throw new Exception("You are not allowed to do this action");
			}
			if (SAVE.equals(activity)) {
				// for add compound&simple user attribute
				String tmpInstanceIdentifier = "";
				for (String parameter : request.getParameterMap().keySet()) {
					if (parameter.startsWith("compound_")) {
						// format is like:
						// compound_compoundAttributeKey__simpleAttributeKey__instanceIdentifier
						String[] tmp = parameter.substring(9).split("__");
						String compoundAttributeIdentifier = tmp[0];
						String simpleAttributeIdentifier = tmp[1];
						String instanceIdentifier = "";
						if (tmp.length == 3) {
							instanceIdentifier = tmp[2];
						}
						String attributeValue = request.getParameter(parameter);
						// if instanceIdentifier is not defined, this attribute
						// should be added
						if ("".equals(instanceIdentifier)) {
							if ("".equals(tmpInstanceIdentifier)) {
								tmpInstanceIdentifier = personaService
										.addSimpleUserAttributeToCompoundForUser(
												userIdentifier,
												userIdentifierTypeCode,
												compoundAttributeIdentifier,
												simpleAttributeIdentifier,
												attributeValue);
							} else if (!"".equals(attributeValue)) {
								personaService
										.updateSimpleUserAttributeToCompoundForUser(
												userIdentifier,
												userIdentifierTypeCode,
												tmpInstanceIdentifier,
												compoundAttributeIdentifier,
												simpleAttributeIdentifier,
												attributeValue);
							}
						} else {
							if ("".equals(attributeValue)) {
								personaService
										.removeSimpleUserAttributeFromCompoundForUser(
												userIdentifier,
												userIdentifierTypeCode,
												instanceIdentifier,
												compoundAttributeIdentifier,
												simpleAttributeIdentifier);
							} else {
								personaService
										.updateSimpleUserAttributeToCompoundForUser(
												userIdentifier,
												userIdentifierTypeCode,
												instanceIdentifier,
												compoundAttributeIdentifier,
												simpleAttributeIdentifier,
												attributeValue);
							}
						}
					}
				}
			}
		} catch (IllegalStateException ex) {
			response.setRenderParameter("errorMsg", ex.getMessage());
		} catch (Exception ex) {
			response.setRenderParameter("errorMsg", ex.getMessage());
		}
		response.setRenderParameters(request.getParameterMap());
	}
}
