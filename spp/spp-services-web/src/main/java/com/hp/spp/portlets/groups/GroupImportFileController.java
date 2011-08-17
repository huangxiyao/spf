package com.hp.spp.portlets.groups;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.multipart.MultipartActionRequest;
import org.springframework.web.portlet.mvc.SimpleFormController;

/**
 * Controller import an XML file as a list of groups
 * 
 * @author MJULIENS
 * 
 */
public class GroupImportFileController extends SimpleFormController implements InitializingBean {

	private static Logger logger = Logger.getLogger(GroupImportFileController.class);

	private GroupService groupService;

	public void afterPropertiesSet() throws Exception {
//		if (this.groupService == null)
//			throw new IllegalArgumentException("A GroupService is required");
	}

	public void onSubmitAction(ActionRequest request, ActionResponse response,
            Object command, BindException errors) throws Exception {

        if (request instanceof MultipartActionRequest) {
        	logger.debug("The request is an instance of MultipartActionRequest");
            MultipartActionRequest multipartRequest = (MultipartActionRequest) request;
            MultipartFile multipartFile = multipartRequest.getFile("file");
            if (multipartFile != null) {
                logger.info("isEmpty: " + multipartFile.isEmpty());
                logger.info("contentType: " + multipartFile.getContentType());
                logger.info("name: " + multipartFile.getName());
                logger.info("size: " + multipartFile.getSize());
                logger.info("originalFilename: " + multipartFile.getOriginalFilename());
                if (!"text/plain".equals(multipartFile.getContentType())) {
                    throw new PortletException("File is of type '" + 
                            multipartFile.getContentType() + 
                            "', not 'text/plain'");
                }
                logger.info("content: " + multipartFile.getBytes().toString());
            } else {
                logger.info("MultipartFile returned NULL");
            }
        }
        else
        {
        	logger.debug("The request is NOT an instance of MultipartActionRequest, this a ["+request.getClass()+"]");
        }
        
        /**
        Upload upload = (Upload)command;
        if (upload != null) {
            byte[] file = upload.getFile();
            if (file != null) {
                String uploadString = new String(file);
                response.setRenderParameter("xmlUpload",uploadString);
            }
        }
        **/
    }

    protected ModelAndView onSubmitRender(RenderRequest request, RenderResponse response,
            Object command, BindException errors) throws Exception {
        return this.showNewForm(request, response);
    }

    protected void initBinder(PortletRequest request, PortletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        //binder.registerCustomEditor(String.class, new StringMultipartFileEditor());
    }
    
    /*
	 * protected void handleInvalidSubmit(ActionRequest request, ActionResponse response)
	 * throws Exception { response.setRenderParameter("action", "groups"); }
	 */

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	protected boolean isInvalidSubmission(PortletRequest arg0) {
		return false;
	}

}
