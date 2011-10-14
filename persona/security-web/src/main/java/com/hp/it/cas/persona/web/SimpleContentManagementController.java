package com.hp.it.cas.persona.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import com.hp.it.cas.persona.service.IPersonaAdminService;
import com.hp.it.cas.persona.uav.service.EUserIdentifierType;

/**
 * SimpleContentManagementController is to handle simple content browsing
 * requests for user
 * 
 * @author Ding Kai-Jian
 * 
 */
public class SimpleContentManagementController extends AbstractController {

	private static final String ISADMIN = "isAdmin";

	private static final String QUERY = "query";

	private static final String JUMPADDSIMPLE = "jumpAddSimple";

	private static final String JUMPEDITCOMPOUND = "jumpEditCompound";

	private static final String ADD = "add";

	private static final String SAVE = "save";

	private static final String DELETE = "delete";

	private static final String ACTIVITY = "activity";

	private String mainViewName;

	private String compAttributeEditViewName;

	private String simpleAttributeAddViewName;

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
	 * get simpleAttributeAddViewName
	 * 
	 * @return String
	 */
	public String getSimpleAttributeAddViewName() {
		return simpleAttributeAddViewName;
	}

	/**
	 * set simpleAttributeAddViewName
	 * 
	 * @param String
	 *            simpleAttributeAddViewName
	 */
	public void setSimpleAttributeAddViewName(String simpleAttributeAddViewName) {
		this.simpleAttributeAddViewName = simpleAttributeAddViewName;
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
	 * get compAttributeEditViewName
	 * 
	 * @return String
	 */
	public String getCompAttributeEditViewName() {
		return compAttributeEditViewName;
	}

	/**
	 * set compAttributeEditViewName
	 * 
	 * @param String
	 *            compAttributeEditViewName
	 */
	public void setCompAttributeEditViewName(String compAttributeEditViewName) {
		this.compAttributeEditViewName = compAttributeEditViewName;
	}

	/**
	 * jump to add simple content browsing page.
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

		// retrieve activity value to indicate what currect operation is.
		String activity = request.getParameter(ACTIVITY);
		// use simple attribute key to search
		String userIdentifier = request.getParameter("userIdentifier");
		String userIdentifierTypeCode = request
				.getParameter("userIdentifierTypeCode");
		if (userIdentifierTypeCode == null) {
			userIdentifierTypeCode = EUserIdentifierType.EXTERNAL_USER
					.getUserIdentifierTypeCode();
		}
		if (Utils.isPersonaAdmin(request)) {
			view.addObject(ISADMIN, true);
		}
		try {
			// normal user can only query or go home page
			if (!Utils.isPersonaAdmin(request)
					&& (JUMPADDSIMPLE.equals(activity))) {
				throw new Exception("You are not allowed to do this action");
			}
			if (JUMPADDSIMPLE.equals(activity)) {
				// jump to add simple attribute page
				view.addObject("availableUserAttributeList", personaService
						.listAllSimpleUserAttributes());
				view.setViewName(simpleAttributeAddViewName);
			}
			// query operation
			if (QUERY.equals(activity)) {
				if (userIdentifier != null && !"".equals(userIdentifier.trim())) {
					view.addObject("attributeList", personaService
							.listSimpleUserAttributesForUser(userIdentifier,
									userIdentifierTypeCode));
				}
			}
		} catch (IllegalStateException ex) {
			view.addObject("errorMsg", ex.getMessage());
		} catch (Exception ex) {
			view.addObject("errorMsg", ex.getMessage());
		} finally {
			view.addObject("userIdentifierTypes", Arrays
					.asList(EUserIdentifierType.values()));
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
	 * handle add, save, delete of simple user content attributes.
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
		Map parameterMap = new HashMap();
		parameterMap.putAll(request.getParameterMap());
		// retrieve activity value to indicate what currect operation is.
		String activity = request.getParameter(ACTIVITY);
		// use simple attribute key to search
		String userIdentifier = request.getParameter("userIdentifier");
		String userIdentifierTypeCode = request
				.getParameter("userIdentifierTypeCode");
		String userAttributeIdentifier = request.getParameter("userAttributeIdentifier");
		try {
			if (!Utils.isPersonaAdmin(request)
					&& (ADD.equals(activity) || SAVE.equals(activity) || DELETE
							.equals(activity))) {
				throw new Exception("You are not allowed to do this action");
			}
			if (ADD.equals(activity)) {
				String userAttributeValue = request
						.getParameter("userAttributeValue");
				if (userAttributeValue != null
						&& !"".equals(userAttributeValue)) {
					personaService.addSimpleUserAttributeToUser(userIdentifier,
							userIdentifierTypeCode, userAttributeIdentifier,
							userAttributeValue);
					parameterMap.put(ACTIVITY, new String[] { QUERY });
				}
			} else if (SAVE.equals(activity)) {
				for (String parameter : request.getParameterMap().keySet()) {
					if (parameter.startsWith("simple_")) {
						// format is like:
						// simple_attributeKey__instanceIdentifier
						String[] tmp = parameter.substring(7).split("__");
						String simpleAttributeIdentifier = tmp[0];
						String instanceIdentifier = tmp[1];
						personaService.updateSimpleUserAttributeForUser(
								userIdentifier, userIdentifierTypeCode,
								instanceIdentifier, simpleAttributeIdentifier, request
										.getParameter(parameter));
					}
				}
				parameterMap.put(ACTIVITY, new String[] { QUERY });
			} else if (DELETE.equals(activity)) {
				personaService.removeSimpleUserAttributeForUser(userIdentifier,
						userIdentifierTypeCode, request
								.getParameter("deletedInstanceIdentifier"));
				parameterMap.put(ACTIVITY, new String[] { QUERY });
			}
		} catch (IllegalStateException ex) {
			parameterMap.put("errorMsg", new String[] { ex.getMessage() });
		} catch (Exception ex) {
			parameterMap.put("errorMsg", new String[] { ex.getMessage() });
		}
		if (JUMPEDITCOMPOUND.equals(activity)) {
			// jump to edit compound attribute page
			parameterMap.put("userIdentifierTypeCode", new String[] { request
					.getParameter("userIdentifierTypeCode") });
			parameterMap.put("userIdentifier", new String[] { userIdentifier });
			if (userIdentifier != null && !"".equals(userIdentifier.trim())) {
				parameterMap.put("action",
						new String[] { "compoundContentManagement" });
			}
		}
		response.setRenderParameters(parameterMap);
	}
}
