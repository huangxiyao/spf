package com.hp.spp.webservice.eservice;

import java.util.Map;

/** This is a JavaBean which represents an Eservice response.
 * 
 * @author Adrien GeyMond
 */
public class EServiceResponse
{
	private String mUrl;
    
    private String mMethod;
    
    private Map mParameters;
    
    private boolean openInNewWindow;
    
    private boolean securityMode;

    private long mSimulationMode;
    
    private String windowParameters;
    
    private String characterEncoding;
    
	public boolean isSecurityMode() {
		return securityMode;
	}

	public void setSecurityMode(boolean securityMode) {
		this.securityMode = securityMode;
	}

	public String getMethod() {
		return mMethod;
	}

	public void setMethod(String method) {
		mMethod = method;
	}

	public Map getParameters() {
		return mParameters;
	}

	public void setParameters(Map parameters) {
		mParameters = parameters;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	public boolean isOpenInNewWindow() {
		return openInNewWindow;
	}

	public void setOpenInNewWindow(boolean openInNewWindow) {
		this.openInNewWindow = openInNewWindow;
	}

	public long getSimulationMode() {
		return mSimulationMode;
	}

	public void setSimulationMode(long simulationMode) {
		this.mSimulationMode = simulationMode;
	}

	public String getWindowParameters() {
		return windowParameters;
	}

	public void setWindowParameters(String windowParameters) {
		this.windowParameters = windowParameters;
	}
	
	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}

}
