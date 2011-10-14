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
 * SimpleAttributeController is used to handle simple user attribute requests;
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class SimpleAttributeController extends AbstractController {

	private static final String ISADMIN = "isAdmin";

	private static final String SEARCH_BY_NAME = "ByName";

	private static final String INSERT = "insert";

	private static final String QUERY = "query";

	private static final String UPDATE = "update";

	private static final String DELETE = "delete";

	private static final String ADD = "add";

	private static final String EDIT = "edit";

	private static final String ACTIVITY = "activity";

	private String mainViewName;

	private String editViewName;

	private IPersonaAdminService personaService;

	/**
	 * get mainViewName
	 * 
	 * @return String
	 */
	public String getMainViewName() {
		return this.mainViewName;
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
		return this.editViewName;
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
			if (!Utils.isPersonaAdmin(request)
					&& (EDIT.equals(activity) || ADD.equals(activity))) {
				throw new Exception("You are not allowed to do this action");
			}
			if (EDIT.equals(activity)) {
				IUserAttribute iUserAttribute = personaService
						.findSimpleUserAttributeById(userAttributeKey);
				if (iUserAttribute != null) {
					view.addObject("userAttributeName", iUserAttribute
							.getUserAttributeName());
					view.addObject("userAttributeDescription", iUserAttribute
							.getUserAttributeDescription());
					view.addObject("userAttributeDefinition", iUserAttribute
							.getUserAttributeDefinition());
					view.setViewName(editViewName);
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
							.listAllSimpleUserAttributes());
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
			view.addObject("errorMsg", "Invalid Simple Attribute Identifier value!");
		} catch (NullPointerException ex) {
			view.addObject("errorMsg", "There is a error raised.");
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
	 * delete a simple attribute <br/>
	 * add a simple attribute<br/>
	 * edit a simple attribute meta data<br/>
	 * redirect request parmaters from action to render operation.
	 * 
	 * @param request
	 *            a <code>ActionRequest</code>.
	 * @param response
	 *            a <code>ActionResponse</code>.
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
					&& (DELETE.equals(activity) || UPDATE.equals(activity) || INSERT
							.equals(activity))) {
				throw new Exception("You are not allowed to do this action");
			}
			if (DELETE.equals(activity)) {
				// delete specified user attribute
				personaService.removeUserAttribute(userAttributeKey);
				parameterMap.put(ACTIVITY, new String[] { QUERY });
			} else if (UPDATE.equals(activity)) {
				String userAttributeDescription = request
						.getParameter("userAttributeDescription");
				String userAttributeDefinition = request
						.getParameter("userAttributeDefinition");
				// update specified user attribute
				String userAttributeName = request
						.getParameter("userAttributeName");
				personaService.updateUserAttribute(userAttributeKey,
						userAttributeName, userAttributeDescription,
						userAttributeDefinition);
				parameterMap.put(ACTIVITY, new String[] { QUERY });
			} else if (INSERT.equals(activity)) {
				String userAttributeDescription = request
						.getParameter("userAttributeDescription");
				String userAttributeDefinition = request
						.getParameter("userAttributeDefinition");
				// create a new user attribute
				String userAttributeName = request
						.getParameter("userAttributeName");
				IUserAttribute iUserAttribute = personaService
						.createSimpleUserAttribute(userAttributeKey,
						        userAttributeName,
								userAttributeDescription,
								userAttributeDefinition);
				parameterMap.put("userAttributeIdentifier", new String[] { String
						.valueOf(iUserAttribute.getUserAttributeIdentifier()) });
				parameterMap.put(ACTIVITY, new String[] { QUERY });
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
	 * Retrieve simple user attribute with the specified key and search type.
	 * 
	 * @param value
	 *            user attribute key/name value
	 * @param searchType
	 *            search type, by key or by value
	 * @return the simple attribute or null.
	 * @throws NumberFormatException
	 *             if user attribute key is an invalid integer number.
	 * @throws Exception
	 *             if other errors occur
	 */
	private IUserAttribute query(String value, String searchType)
			throws Exception {
		if (SEARCH_BY_NAME.equals(searchType)) {
			return personaService.findSimpleUserAttributeByName(value);
		} else {
			return personaService.findSimpleUserAttributeById(value);
		}
	}
}
