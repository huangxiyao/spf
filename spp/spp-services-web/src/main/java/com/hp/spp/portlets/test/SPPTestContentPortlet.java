package com.hp.spp.portlets.test;


import org.apache.log4j.Logger;

import javax.portlet.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;



public class SPPTestContentPortlet extends GenericPortlet
{
		
	private static final String LABEL = "Lorem ipsum dolor sit amet";
	private static final String DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
	private static final String DEFAULT_EXEC_TIME = "500";
	private static final String DEFAULT_NUM_ITEMS = "10";
	private static final String DEFAULT_DISP_MODE = "verbose";
      private static final String IMAGE_LOC = "/images/project-logo.jpg";
//	private static final String USER_CONTEXT_KEY = "StandardParameters";
	private static final Logger mTLog = Logger.getLogger("TIME." + SPPTestContentPortlet.class.getName());
	private static final Logger mLog = Logger.getLogger(SPPTestContentPortlet.class);
	private static final Random RANDOM = new Random();
	private File mConfigFile = null;
	private int mGlobalExecutionTime = Integer.MIN_VALUE;
	private String mUserContextKey = null;

	public void init(PortletConfig portletConfig) throws PortletException {
		super.init(portletConfig);
		if (portletConfig.getInitParameter("ConfigFile") != null) {
			mConfigFile = new File(portletConfig.getInitParameter("ConfigFile"));
			if (mLog.isInfoEnabled()) {
				mLog.info("Will use config file " + mConfigFile.getAbsolutePath());
			}
			if (mConfigFile.exists()) {
				readConfigFile();
			}
			else {
				mLog.warn("ConfigFile specified (" + mConfigFile.getAbsolutePath() + ") but file could not be found!");
			}
		}
		else {
			if (mLog.isInfoEnabled()) {
				mLog.info("No config file will be used!");
			}
		}
		if (portletConfig.getInitParameter("UserContextAttributeName") != null && !"".equals(portletConfig.getInitParameter("UserContextAttributeName"))) {
			mUserContextKey = portletConfig.getInitParameter("UserContextAttributeName").trim();
			if (mLog.isInfoEnabled()) {
				mLog.info("The following UserContext session attribute name will be used: " + mUserContextKey);
			}
		}
		else {
			if (mLog.isInfoEnabled()) {
				mLog.info("UserContext will not be retrieved from session as not UserContextAttributeName parameter has been specified");
			}
		}
	}

	private void readConfigFile() {
		try {
			if (mConfigFile == null) {
				mLog.warn("readConfigFile called but config file init param not specified");
				return;
			}
			if (!mConfigFile.exists()) {
				mLog.warn("Config file not found: " + mConfigFile.getCanonicalPath());
				mGlobalExecutionTime = Integer.MIN_VALUE;
				return;
			}

			if (mLog.isDebugEnabled()) {
				mLog.debug("Reading config file: " + mConfigFile.getAbsolutePath());
			}
			Properties props = new Properties();
			InputStream is = new FileInputStream(mConfigFile);
			props.load(is);
			is.close();
			if (props.containsKey("globalExecTime")) {
				mGlobalExecutionTime = Integer.parseInt(props.getProperty("globalExecTime"));
				mLog.info("Global portlet execution time is " + mGlobalExecutionTime + "ms");
			}
			else {
				mLog.info("Global portlet execution time not specified");
			}
		}
		catch (Exception e) {
			mLog.error("Error reading config file", e);
		}
	}

	protected void doDispatch(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
		PortletMode mode = renderRequest.getPortletMode();
		if ("config".equalsIgnoreCase(mode.toString())) {
			doConfig(renderRequest, renderResponse);
		}
		else {
			super.doDispatch(renderRequest, renderResponse);
		}
	}

	private void doConfig(RenderRequest request, RenderResponse response) throws IOException, PortletException {
		if (!"true".equals(request.getParameter("preview"))) {
			response.setContentType("text/html");
			PortletPreferences prefs = request.getPreferences();
			request.setAttribute("isUsingConfigFile", String.valueOf(mConfigFile != null));
			request.setAttribute("isUsingGlobalExecutionTime", Boolean.valueOf(mGlobalExecutionTime != Integer.MIN_VALUE));

			request.setAttribute("minExecutionTime", String.valueOf(getExecTime(prefs, true)));
            request.setAttribute("maxExecutionTime", String.valueOf(getExecTime(prefs, false)));

            request.setAttribute("numberOfItems", prefs.getValue("numberOfItems", DEFAULT_NUM_ITEMS));
			request.setAttribute("displayMode", prefs.getValue("displayMode", DEFAULT_DISP_MODE));
			getPortletContext().getRequestDispatcher("/WEB-INF/jsp/perf/config.jsp").include(request, response);
		}
		else {
			request.setAttribute("preview", Boolean.TRUE);
			doView(request, response);
		}
	}

	protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		long start = 0;
		if (mTLog.isDebugEnabled()) {
			start = System.currentTimeMillis();
		}
		response.setContentType("text/html");

		boolean showingItem = request.getParameter("id") != null;

		PortletPreferences prefs = request.getPreferences();

        int minExecTime = getExecTime(prefs, true);
        int maxExecTime = getExecTime(prefs, false);

        try {
            int randomTime = generateRandomDelay(minExecTime, maxExecTime);
            if (mLog.isInfoEnabled()) {
				mLog.info("Random delay of " + randomTime + " used");
			}

            Thread.sleep(randomTime);
		}
		catch (InterruptedException e) {
			throw new IOException(e.toString());
		}

        String encodedImageURL = response.encodeURL(getContextPath(request) + IMAGE_LOC);
        //Encode the imageURL
        request.setAttribute("EncodedImageURL", encodedImageURL);


        String includePage = null;

		if (showingItem) {
			int id = Integer.parseInt(request.getParameter("id"));
			Item item = new Item(id, new Date(), LABEL + " (" + request.getParameter("id") + ")", DESCRIPTION);
			request.setAttribute("item", item);
			includePage = "/WEB-INF/jsp/perf/viewItem.jsp";
		}
		else {
			if (mUserContextKey != null) {
				Map profile = (Map) request.getPortletSession().getAttribute(mUserContextKey, PortletSession.APPLICATION_SCOPE);
				if (profile == null) {
					if (profile == null) {
						profile = (Map) request.getAttribute(mUserContextKey);
					}
					if (profile == null) {
						throw new PortletException("User context required but not found neither in the session nor in the request under " + mUserContextKey);
					}
				}
				request.setAttribute("greetings", "Welcome " + profile.get("FirstName") + " " + profile.get("LastName") + "!");
			}
			else {
				request.setAttribute("greetings", "Welcome!");
			}

			//view list
			int numOfItems = Integer.parseInt(prefs.getValue("numberOfItems", DEFAULT_NUM_ITEMS));
			List result = new ArrayList(numOfItems);
			Date now = new Date();
			for(int i = 0; i < numOfItems; ++i) {
				int id = i + 1;
				result.add(new Item(id, now, LABEL + " (" + id + ")", DESCRIPTION));
			}
			request.setAttribute("listOfItems", result);
			if ("brief".equals(prefs.getValue("displayMode", DEFAULT_DISP_MODE))) {
				includePage = "/WEB-INF/jsp/perf/viewBrief.jsp";
			}
			else {
				includePage = "/WEB-INF/jsp/perf/viewVerbose.jsp";
			}
		}
		long processingEnd = 0;
		if (mTLog.isDebugEnabled()) {
			processingEnd = System.currentTimeMillis();
		}
		getPortletContext().getRequestDispatcher(includePage).include(request, response);
		if (mTLog.isDebugEnabled()) {
			long renderingEnd = System.currentTimeMillis();
			mTLog.debug("processing [" + includePage + "] | " + (processingEnd-start));
			mTLog.debug("rendering [" + includePage + "] | " + (renderingEnd-processingEnd));
		}
	}

	private int getExecTime(PortletPreferences prefs, boolean isMinExecTime) {
        int execTime = Integer.MIN_VALUE;

        if(isMinExecTime){
             execTime = (mGlobalExecutionTime != Integer.MIN_VALUE ?
				mGlobalExecutionTime :
				Integer.parseInt(prefs.getValue("minExecutionTime", DEFAULT_EXEC_TIME)));
        }else{
              execTime = (mGlobalExecutionTime != Integer.MIN_VALUE ?
                mGlobalExecutionTime :
                Integer.parseInt(prefs.getValue("maxExecutionTime", DEFAULT_EXEC_TIME)));
        }

        return execTime;
	}


    private int generateRandomDelay(int minExecTime, int maxExecTime) {
        
        if(minExecTime == maxExecTime){
            return minExecTime;
        }

        int random = RANDOM.nextInt();
        int delay = minExecTime + (int) Math.abs(random%(maxExecTime - minExecTime));

        return delay;
	}


    private String getContextPath(RenderRequest request){
        String scheme = request.getScheme();
        String server = request.getServerName();
        int port = request.getServerPort();
        String context = request.getContextPath();
        String hostContext = scheme + "://" + server;

        if (port != 80){
         hostContext += ":" + port;
        }

        hostContext += context;
        return hostContext;

    }

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
		if ("true".equals(request.getParameter("refreshConfig"))) {
			readConfigFile();
		}
		else {
			PortletPreferences prefs = request.getPreferences();
			String minExecutionTime = (request.getParameter("minExecutionTime") != null ? request.getParameter("minExecutionTime") : DEFAULT_EXEC_TIME);
            String maxExecutionTime = (request.getParameter("maxExecutionTime") != null ? request.getParameter("maxExecutionTime") : DEFAULT_EXEC_TIME);

            try {
				int minExecTime  = Integer.parseInt(minExecutionTime);
			    int maxExecTime = Integer.parseInt(maxExecutionTime);

                 //Check if maxExecTime is less than minExecTime
                if (maxExecTime < minExecTime)     {
                    maxExecutionTime = minExecutionTime;
                }

                prefs.setValue("minExecutionTime", minExecutionTime);
                prefs.setValue("maxExecutionTime", maxExecutionTime);
            }
			catch (NumberFormatException e) {
                prefs.setValue("minExecutionTime", DEFAULT_EXEC_TIME);
	             prefs.setValue("maxExecutionTime", DEFAULT_EXEC_TIME);
			}


            
            String numOfItems = (request.getParameter("numberOfItems") != null ? request.getParameter("numberOfItems") : DEFAULT_NUM_ITEMS);

            try {
				Integer.parseInt(numOfItems);
				prefs.setValue("numberOfItems", numOfItems);
			}
			catch (NumberFormatException e) {
				prefs.setValue("numberOfItems", DEFAULT_NUM_ITEMS);
			}
			if ("brief".equals(request.getParameter("displayMode"))) {
				prefs.setValue("displayMode", "brief");
			}
			else {
				prefs.setValue("displayMode", DEFAULT_DISP_MODE);
			}
			prefs.store();
		}
	}
	
}
