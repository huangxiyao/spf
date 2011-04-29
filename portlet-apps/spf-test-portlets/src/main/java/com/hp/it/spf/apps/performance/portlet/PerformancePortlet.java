package com.hp.it.spf.apps.performance.portlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * 
 * This Performance Test Portlet is used to measure the system performance by simulating portlet behavior.
 * <p>
 * The PerformancePortlet is created based on the VAP Performance Guide. The simulated portlet behavior is in-line 
 * with Vignette Tests. Each portlet instance behaves according to the portlet preference settings. 
 * <p>
 * We can simulate the portlet behaviors in terms of 3 aspects. i/o operations , memory intensive operations,
 * computation intensive operations.
 * 
 * @author pranav
 *
 */
public class PerformancePortlet extends GenericPortlet {
	
	private static final Logger mLog = Logger.getLogger(PerformancePortlet.class.getName());	

	private static final int MIN_DEFAULT = 1;
	private static final int MAX_DEFAULT = 1;
	private static final int NUMERANDS_DEFAULT = 1;
	private static final int OBJECTS_DEFAULT = 1;
	private static final int DEFAULT_NUM_ITEMS = 3;
	
	private static final String LABEL = "Lorem ipsum dolor sit amet";
	private static final String DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
	private static final String DEFAULT_DISP_MODE = "verbose";
	private static final String IMAGE_LOC = "/images/project-logo.jpg";
	
	@Override
	@RenderMode(name = "view")
	protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
	throws PortletException, IOException {

		long start = 0;
		start = System.currentTimeMillis();
		PortletPreferences preferences = renderRequest.getPreferences();
		
		// Computation delay generation 
		int numerands = Integer.parseInt(preferences.getValue("numerands", String.valueOf(NUMERANDS_DEFAULT)));
		if( numerands > 1){
			generateComputationDelay(numerands);
		}
		renderRequest.setAttribute("compOperation", numerands );
		
		// Object Generation Block
		int objectCount = Integer.parseInt(preferences.getValue("objectCount", String.valueOf(OBJECTS_DEFAULT)));
		if( objectCount > 1){
			generateObject(objectCount);
		}
		renderRequest.setAttribute("objectCount", objectCount );

		// Delay Generation Block
		try {
			int delay = getRandomDelayTime(
					Integer.parseInt(preferences.getValue("minDelay", String.valueOf(MIN_DEFAULT))), 
					Integer.parseInt(preferences.getValue("maxDelay", String.valueOf(MAX_DEFAULT))));
			renderRequest.setAttribute("generatedDelay", delay);
			if(delay > 1){
				Thread.sleep(delay);
			}
		} catch (InterruptedException e) {
			mLog.log(Level.SEVERE, "Time Delay Block Caused an exception");
		}

		// Encode URL
		boolean showingItem = renderRequest.getParameter("id") != null;
		String encodedImageURL = renderResponse.encodeURL(getContextPath(renderRequest) + IMAGE_LOC);
		//Encode the imageURL
		renderRequest.setAttribute("EncodedImageURL", encodedImageURL);

		String includePage = null;

		if (showingItem) {
			int id = Integer.parseInt(renderRequest.getParameter("id"));
			Item item = new Item(id, new Date(), LABEL + " (" + renderRequest.getParameter("id") + ")", DESCRIPTION);
			renderRequest.setAttribute("item", item);
			includePage = "/WEB-INF/jsp/performance/viewItem.jsp";
		}
		else {
			int numOfItems = Integer.parseInt(preferences.getValue("numberOfItems", String.valueOf(DEFAULT_NUM_ITEMS)));
			List result = new ArrayList(numOfItems);
			Date now = new Date();
			for(int i = 0; i < numOfItems; ++i) {
				int id = i + 1;
				result.add(new Item(id, now, LABEL + " (" + id + ")", DESCRIPTION));
			}
			renderRequest.setAttribute("listOfItems", result);
			if ("brief".equals(preferences.getValue("displayMode", DEFAULT_DISP_MODE))) {
				includePage = "/WEB-INF/jsp/performance/viewBrief.jsp";
			}
			else {
				includePage = "/WEB-INF/jsp/performance/viewVerbose.jsp";
			}
		}

		long processingEnd = 0;			
		processingEnd = System.currentTimeMillis();
		
		getPortletContext().getRequestDispatcher(includePage).include(renderRequest, renderResponse);

		long renderingEnd = System.currentTimeMillis();
		
		mLog.info("processing [" + includePage + "] | " + (processingEnd-start));
		mLog.info("rendering [" + includePage + "] | " + (renderingEnd-processingEnd));
	}

	@RenderMode(name = "vignette:config")
	public void showConfig(RenderRequest request, RenderResponse response) throws PortletException, IOException
	{
		PortletPreferences prefs = request.getPreferences();

		request.setAttribute("numberOfItems", prefs.getValue("numberOfItems", String.valueOf(DEFAULT_NUM_ITEMS)));
		request.setAttribute("displayMode", prefs.getValue("displayMode", DEFAULT_DISP_MODE));

		getPortletContext().getRequestDispatcher("/WEB-INF/jsp/performance/config.jsp").include(request, response);
	}

	@RenderMode(name = "config")
	public void showConfigLocal(RenderRequest request, RenderResponse response) throws PortletException, IOException
	{
		showConfig(request, response);
	}
	
	@Override
	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
	throws PortletException, IOException {

		String prefName = actionRequest.getParameter("prefName");
		String prefValue = actionRequest.getParameter("prefValue");
		PortletPreferences prefs = actionRequest.getPreferences();
		if (prefName != null && prefValue != null) {
			if (prefValue.trim().equals("")) {
				prefs.reset(prefName.trim());
			}
			else {
				prefs.setValue(prefName.trim(), prefValue);
			}
			prefs.store();
		}

		String numOfItems = (actionRequest.getParameter("numberOfItems") != null ? 
				actionRequest.getParameter("numberOfItems") : String.valueOf(DEFAULT_NUM_ITEMS));

		try {
			Integer.parseInt(numOfItems);
			prefs.setValue("numberOfItems", numOfItems.trim());
		}
		catch (NumberFormatException e) {
			prefs.setValue("numberOfItems", String.valueOf(DEFAULT_NUM_ITEMS));
		}
		if ("brief".equals(actionRequest.getParameter("displayMode"))) {
			prefs.setValue("displayMode", "brief");
		}
		else {
			prefs.setValue("displayMode", DEFAULT_DISP_MODE);
		}
		prefs.store();

	}

	/**
	 * This method calculates the random time in between the mindelay and maxdelay range.
	 */
	private static int getRandomDelayTime(int minDelay, int maxDelay){
		if(minDelay == maxDelay){
			return minDelay;
		}
		int random = new Random().nextInt();
		int delay = minDelay + (int) Math.abs(random%(maxDelay - minDelay));
		return delay;
	}

	/**
	 * This method does the arithmetic operations. 
	 * The number of operations is based on the value passed as numrands.
	 */
	private void generateComputationDelay(int numRands){
		if(numRands > 0){
			Random rgen = new Random();
			int[] randNum = new int[numRands];
			for (int r=0; r < numRands; r++) randNum[r]=rgen.nextInt()%100000;

			int sum=0;
			int mean=0;
			int max=0;
			int min=100000;
			for (int n=0; n < numRands; n++) {
				sum +=randNum[n];
				if (randNum[n] < min) min=randNum[n];
				if (randNum[n] > max) max=randNum[n];
			}
			mean = Math.abs(mean = sum/numRands);
		}
	}

	/**
	 * This method generates number of string objects, based on the objectCount value
	 */
	private void generateObject(int objectCount){
		for(int i= 0 ; i < objectCount ; i++ ){    	
			String s = "The quick brown fox jumps over the lazy dog " + i;
		}
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

}
