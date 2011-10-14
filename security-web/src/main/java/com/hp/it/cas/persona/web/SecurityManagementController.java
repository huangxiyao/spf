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

import com.hp.it.cas.persona.configuration.service.IApplication;
import com.hp.it.cas.persona.configuration.service.IPermission;
import com.hp.it.cas.persona.service.IPersonaAdminService;

/**
 * This controller is used for security management.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class SecurityManagementController extends AbstractController {

	private static final String ISADMIN = "isAdmin";

	private static final String COMPOUNDDELETE = "compounddelete";

	private static final String SIMPLEDELETE = "simpledelete";

	private static final String COMPOUNDGRANT = "compoundgrant";

	private static final String CASCADE_SIMPLE_ATTRIBUTES = "cascadeSimpleAttributes";

	private static final String SIMPLEGRANT = "simplegrant";

	private static final String COMPOUNDEDIT = "compoundedit";

	private static final String SIMPLEEDIT = "simpleedit";

	private static final String QUERY = "query";

	private static final String ACTIVITY = "activity";

	private String mainViewName;

	private String simpleEditViewName;

	private String compoundEditViewName;

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
	 * get simpleEditViewName
	 * 
	 * @return String
	 */
	public String getSimpleEditViewName() {
		return this.simpleEditViewName;
	}

	/**
	 * set simpleEditViewName
	 * 
	 * @param String
	 *            simpleEditViewName
	 */
	public void setSimpleEditViewName(String simpleEditViewName) {
		this.simpleEditViewName = simpleEditViewName;
	}

	/**
	 * get compoundEditViewName
	 * 
	 * @return String
	 */
	public String getCompoundEditViewName() {
		return this.compoundEditViewName;
	}

	/**
	 * set compoundEditViewName
	 * 
	 * @param String
	 *            compoundEditViewName
	 */
	public void setCompoundEditViewName(String compoundEditViewName) {
		this.compoundEditViewName = compoundEditViewName;
	}

	/**
	 * Handle render request
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

		String activity = request.getParameter("activity");
		String appIdentifier = request
				.getParameter("applicationPortfolioIdentifier");

		if (Utils.isPersonaAdmin(request)) {
			view.addObject(ISADMIN, true);
		}
		try {
			// retrieve all simple user attributes under selected compound
			// attribute
			if (CASCADE_SIMPLE_ATTRIBUTES.equals(activity)) {
				String compoundKey = request
						.getParameter("grantCompoundUserAttributeIdentifier");
				if (compoundKey != null && !"".equals(compoundKey.trim())) {
					if (Utils.isPersonaAdmin(request)) {
						view
								.addObject(
										"avaliableSimpleAttributeList",
										personaService
												.listUnGrantedSimpleUserAttributesByCompoundForApplication(
														appIdentifier,
														compoundKey));
					}
					view
							.addObject("grantCompoundUserAttributeIdentifier",
									compoundKey);
				}
				activity = COMPOUNDEDIT;
			}

			// edit simple attribute permissions of the specified application
			if (SIMPLEEDIT.equals(activity)) {
				if (appIdentifier == null || "".equals(appIdentifier.trim())) {
					throw new IllegalStateException(
							"Application identifier doesn't be specified.");
				}
				// retrieve all simple attribute permissions
				Set<IPermission> set = personaService
						.listSimplePermissionsForApplication(appIdentifier);
				view.addObject("permissionList", set);

				if (Utils.isPersonaAdmin(request)) {
					// retrieve all ungranted simple user attributes
					view
							.addObject(
									"avaliableSimpleAttributeList",
									personaService
											.listUnGrantedSimpleUserAttributesForApplication(appIdentifier));
				}
				// redirect to edit page
				view.setViewName(simpleEditViewName);
			} else if (COMPOUNDEDIT.equals(activity)) {
				if (appIdentifier == null || "".equals(appIdentifier.trim())) {
					throw new IllegalStateException(
							"Application identifier doesn't be specified.");
				}
				// retrieve all compound attribute permissions
				Set<IPermission> set = personaService
						.listCompoundPermissionsForApplication(appIdentifier);
				view.addObject("permissionList", set);

				if (Utils.isPersonaAdmin(request)) {
					// retrieve all ungranted compound user attributes
					view
							.addObject(
									"avaliableCompoundAttributeList",
									personaService
											.listUnGrantedCompoundUserAttributesForApplication(appIdentifier));
				}
				// direct to edit page
				view.setViewName(compoundEditViewName);
			}

			// query specified application with portfolio identifier
			if (QUERY.equals(activity)) {
				if (appIdentifier != null && !"".equals(appIdentifier.trim())) {
					IApplication iApplication = personaService
							.findApplication(appIdentifier);
					Set<IApplication> set = new HashSet<IApplication>();
					if (iApplication != null) {
						set.add(iApplication);
					}
					view.addObject("applicationList", set);
				}
			}
		} catch (NumberFormatException ex) {
			view.addObject("errorMsg", "Invalid Application Key value!");
		} catch (NullPointerException ex) {
			view.addObject("errorMsg", "There is an error raised.");
		} catch (Exception ex) {
			view.addObject("errorMsg", ex.getMessage());
		} finally {
			if (request.getParameter("errorMsg") != null) {
				view.addObject("errorMsg", request.getParameter("errorMsg"));
			}
			view.addObject("applicationPortfolioIdentifier", appIdentifier);
		}

		return view;
	}

	/**
	 * Redirect request parmaters from action to render operation.
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
		String activity = request.getParameter("activity");
		String appIdentifier = request
				.getParameter("applicationPortfolioIdentifier");
		try {
			if (!Utils.isPersonaAdmin(request)
					&& (SIMPLEDELETE.equals(activity)
							|| COMPOUNDDELETE.equals(activity)
							|| COMPOUNDGRANT.equals(activity) || SIMPLEGRANT
							.equals(activity))) {
				throw new Exception("You are not allowed to do this action");
			}
			// delete simple user attribute permission
			if (SIMPLEDELETE.equals(activity)) {
				personaService.removePermission(appIdentifier, request
						.getParameter("simpleAttributeIdentifier"));
				parameterMap.put(ACTIVITY, new String[] { SIMPLEEDIT });
			} else if (COMPOUNDDELETE.equals(activity)) {
				// delete compound user attribute permission
				personaService.removePermission(appIdentifier, request
						.getParameter("compoundAttributeIdentifier"), request
						.getParameter("simpleAttributeIdentifier"));
				parameterMap.put(ACTIVITY, new String[] { COMPOUNDEDIT });
			} else if (COMPOUNDGRANT.equals(activity)) {
				// add compound user attribute permission choosed by user
				String compoundKey = request
						.getParameter("grantCompoundUserAttributeIdentifier");
				String simpleKey = request
						.getParameter("grantSimpleUserAttributeIdentifier");

				personaService.addCompoundPermission(appIdentifier,
						compoundKey, simpleKey);
				parameterMap.put(ACTIVITY,
						new String[] { CASCADE_SIMPLE_ATTRIBUTES });
			} else if (SIMPLEGRANT.equals(activity)) {
				// add simple user attribute permission choosed by user
				String key = request
						.getParameter("grantSimpleUserAttributeIdentifier");
				personaService.addPermission(appIdentifier, key);
				activity = SIMPLEEDIT;
				parameterMap.put(ACTIVITY, new String[] { SIMPLEEDIT });
			}
		} catch (IllegalStateException ex) {
			parameterMap.put("errorMsg", new String[] { ex.getMessage() });
		} catch (NullPointerException ex) {
			parameterMap.put("errorMsg", "There is an error raised.");
		} catch (NumberFormatException ex) {
			parameterMap.put("errorMsg",
					new String[] { "Invalid Application Key value!" });
		} catch (Exception ex) {
			parameterMap.put("errorMsg", new String[] { ex.getMessage() });
		}
		response.setRenderParameters(parameterMap);
	}
}
