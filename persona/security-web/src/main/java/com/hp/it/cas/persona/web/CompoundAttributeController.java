package com.hp.it.cas.persona.web;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import com.hp.it.cas.persona.configuration.service.IUserAttribute;
import com.hp.it.cas.persona.service.IPersonaAdminService;

/**
 * CompoundAttributeController is used to render and handle compound attribute
 * persona requests;
 *
 * @author <link href="kaijian.ding@hp.com">Ding Kai-Jian</link>
 * @version 1.0
 */
public class CompoundAttributeController extends AbstractController {

	private static final String ISADMIN = "isAdmin";

	private static final String SEARCH_BY_NAME = "ByName";

	private static final String INSERT = "insert";

	private static final String QUERY = "query";

	private static final String UPDATE = "update";

	private static final String DELETE = "delete";

	private static final String ADD = "add";

	private static final String EDIT = "edit";

	private static final String ADDSIMPLE = "addSimple";

	private static final String DELETESIMPLE = "deleteSimple";

	private static final String ACTIVITY = "activity";

	private IPersonaAdminService personaService;

	private String mainViewName;

	private String editViewName;

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
	 *            mainViewName
	 */
	public void setPersonaService(IPersonaAdminService personaService) {
		this.personaService = personaService;
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
	 * get editViewName
	 *
	 * @return String
	 */
	public String getEditViewName() {
		return editViewName;
	}

	/**
	 * set editViewName
	 *
	 * @param String
	 *            editViewName
	 */
	public void setEditViewName(String editViewName) {
		this.editViewName = editViewName;
	}

	/**
	 * jump to edit or main page according to special situation
	 *
	 * @param request
	 *            a <code>RenderRequest</code>.
	 * @param response
	 *            a <code>RenderResponse</code>.
	 * @return a <code>ModelAndView</code>.
	 * @throws Exception
	 *             if any error occur.
	 */
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		ModelAndView view = new ModelAndView(mainViewName);

		// retrieve activity value to indicate what currect operation is.
		String activity = request.getParameter(ACTIVITY);
		if (Utils.isPersonaAdmin(request)) {
			view.addObject(ISADMIN, true);
		}
		// use user attribute key to search
		String userAttributeKey = request.getParameter("userAttributeIdentifier");
		try {
			// normal user can not edit or add
			if (!Utils.isPersonaAdmin(request) && (ADD.equals(activity))) {
				throw new Exception("You are not allowed to do this action");
			}
			if (EDIT.equals(activity)) {
				IUserAttribute iUserAttribute = personaService
						.findCompoundUserAttributeById(userAttributeKey);
				if (iUserAttribute != null) {
					view.addObject("userAttributeName", iUserAttribute
							.getUserAttributeName());
					view.addObject("userAttributeDescription", iUserAttribute
							.getUserAttributeDescription());
					view.addObject("userAttributeDefinition", iUserAttribute
							.getUserAttributeDefinition());
					view.setViewName(editViewName);
					view
							.addObject(
									"simpleAttributeList",
									personaService
											.listSimpleUserAttributesForCompound(userAttributeKey));
					view
							.addObject(
									"availableSimpleAttributeList",
									personaService
											.listAvailableSimpleUserAttributes(userAttributeKey));
				}
			} else if (ADD.equals(activity)) {
				// do not pass userAttributeKey to page
				userAttributeKey = null;
				view.setViewName(editViewName);
			} else if (QUERY.equals(activity)) {
				String searchType = request.getParameter("searchType");
				// searchKey is user attribute key or name
				String searchKey = userAttributeKey;
				// if user attribute key/name is not specified, then query all
				if (searchKey == null || "".equals(searchKey.trim())) {
					view.addObject("attributeList", personaService
							.listAllCompoundUserAttributes());
				} else {
					IUserAttribute iUserAttribute = query(searchKey, searchType);
					Set<IUserAttribute> set = new HashSet<IUserAttribute>();
					if (iUserAttribute != null) {
						set.add(iUserAttribute);
						view.addObject("attributeList", set);
					}
				}
				view.addObject("searchType", searchType);
			}
		} catch (IllegalStateException ex) {
			view.addObject("errorMsg", ex.getMessage());
		} catch (NumberFormatException ex) {
			view.addObject("errorMsg", "Invalid Compound Attribute Identifier value!");
		} catch (Exception ex) {
			view.addObject("errorMsg", ex.getMessage());
		} finally {
			if (request.getParameter("errorMsg") != null) {
				view.addObject("errorMsg", request.getParameter("errorMsg"));
			}
			view.addObject("userAttributeIdentifier", userAttributeKey);
		}
		return view;
	}

	/**
	 * delete a compound attribute <br/>
	 * delete a simple attribute from a compound attribte<br/>
	 * add a compound attribute<br/>
	 * add a simple attribute to a compound attribute<br/>
	 * edit a compound attribute meta data<br/>
	 * redirect request parmaters from action to render operation
	 *
	 * @param ActionRequest
	 *            request
	 * @param ActionResponse
	 *            response
	 * @throws Exception
	 *             if any error occur.
	 */
	protected void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		Map parameterMap = new HashMap();
		parameterMap.putAll(request.getParameterMap());
		// retrieve activity value to indicate what currect operation is.
		String activity = request.getParameter(ACTIVITY);
		String userAttributeKey = request.getParameter("userAttributeIdentifier");
		try {
			if (!Utils.isPersonaAdmin(request)
					&& (DELETE.equals(activity) || UPDATE.equals(activity)
							|| INSERT.equals(activity)
							|| ADDSIMPLE.equals(activity) || DELETESIMPLE
							.equals(activity))) {
				throw new Exception("You are not allowed to do this action");
			}
			if (DELETE.equals(activity)) {
				// delete specified user attribute
				personaService.removeUserAttribute(userAttributeKey);
				parameterMap.put(ACTIVITY, new String[] { QUERY });
			} else if (UPDATE.equals(activity)) {
				// update specified user attribute
				String userAttributeName = request
						.getParameter("userAttributeName");
				String userAttributeDescription = request
						.getParameter("userAttributeDescription");
				String userAttributeDefinition = request
						.getParameter("userAttributeDefinition");
				personaService.updateUserAttribute(userAttributeKey,
						userAttributeName, userAttributeDescription,
						userAttributeDefinition);
				parameterMap.put(ACTIVITY, new String[] { QUERY });
			} else if (INSERT.equals(activity)) {
				// create a new user attribute
				String userAttributeName = request
						.getParameter("userAttributeName");
				String userAttributeDescription = request
						.getParameter("userAttributeDescription");
				String userAttributeDefinition = request
						.getParameter("userAttributeDefinition");
				IUserAttribute iUserAttribute = personaService
						.createCompoundUserAttribute(userAttributeKey,
						        userAttributeName,
								userAttributeDescription,
								userAttributeDefinition);
				parameterMap.put("userAttributeIdentifier", new String[] { String
						.valueOf(iUserAttribute.getUserAttributeIdentifier()) });
				parameterMap.put(ACTIVITY, new String[] { QUERY });
			} else if (ADDSIMPLE.equals(activity)) {
				// add a simple attribute to current compound attribute
				personaService.addSimpleUserAttributeToCompound(request
						.getParameter("addedUserAttributeIdentifier"),
						userAttributeKey);
				parameterMap.put(ACTIVITY, new String[] { EDIT });
			} else if (DELETESIMPLE.equals(activity)) {
				// add delete simple attribute from current compound attribute
				personaService.removeSimpleUserAttributeFromCompound(request
						.getParameter("deletedUserAttributeIdentifier"),
						userAttributeKey);
				parameterMap.put(ACTIVITY, new String[] { EDIT });
			}
		} catch (IllegalStateException ex) {
			parameterMap.put("errorMsg", new String[] { ex.getMessage() });
		} catch (NumberFormatException ex) {
			parameterMap.put("errorMsg",
					new String[] { "Invalid user Attribute Identifier value!" });
		} catch (Exception ex) {
			parameterMap.put("errorMsg", new String[] { ex.getMessage() });
		}
		response.setRenderParameters(parameterMap);
	}

	/**
	 * Retrieve compound user attribute with the specified key and search type.
	 *
	 * @param value
	 *            user attribute key/name value
	 * @param searchType
	 *            search type, by key or by name
	 * @return the compound attribute or null.
	 * @throws NumberFormatException
	 *             if user attribute key is an invalid integer number.
	 * @throws Exception
	 *             if other errors occur
	 */
	private IUserAttribute query(String value, String searchType)
			throws Exception {
		if (SEARCH_BY_NAME.equals(searchType)) {
			return personaService.findCompoundUserAttributeByName(value);
		} else {
			return personaService.findCompoundUserAttributeById(value);
		}
	}
}
