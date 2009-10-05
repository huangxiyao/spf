/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.group.utils;

import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

import com.hp.it.spf.sso.portal.AuthenticationConsts;
import com.hp.it.spf.sso.portal.AuthenticatorHelper;
import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;
import com.vignette.portal.log.LogConfiguration;

/**
 * UGS webservice parameters management class
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class UGSParametersManager {

    private static UGSParametersManager m_UGSParamManager = new UGSParametersManager();
    
    private static ResourceBundle parameters = null;

    private String mode = null;

    private String endPoint = null;        

    private String userName = null;

    private String password = null;

    private String timeout = null;

	private String siteNamePrefix = null;
	
	private Set<String> failOnErrorSites = null;
    
    private UGSParametersManager() {
        super();
        init();
    }

    /**
     * Retrieve a singleton UGSParametersManager class
     * 
     * @return a singleton UGSParametersManager
     */
    public static UGSParametersManager getInstance() {
        if(needRefresh()) {
            m_UGSParamManager = new UGSParametersManager();
        }
        return m_UGSParamManager;
    }    
    
    /**
     * Check if the resourcebundle file is updated.
     * 
     * @return <tt>true</tt> if the file is modified, otherwise, <tt>false</tt>
     */
    private static boolean needRefresh(){
        ResourceBundle currRS = PropertyResourceBundleManager.getBundle(IUGSConstant.UGSPARAMETERSFILENAME);
        // if properties is modified
        return (parameters != currRS) ? true : false;
    }
    
    /**
     * Retrieve parameters from resourcebundle.
     */
    private void init() {        
        parameters = PropertyResourceBundleManager.getBundle(IUGSConstant.UGSPARAMETERSFILENAME);
        try {
            setMode(parameters.getString(IUGSConstant.MODE));
        
            setEndPoint(parameters.getString(IUGSConstant.ENDPOINTS_PREFIX + mode));
            setUserName(parameters.getString(IUGSConstant.USERNAME_PREFIX + mode));
            setPassword(parameters.getString(IUGSConstant.PASSWORD_PREFIX + mode));
            setTimeout(parameters.getString(IUGSConstant.TIMEOUT + mode));
			setSiteNamePrefix(parameters.getString(IUGSConstant.SITE_NAME_PREFIX_PREFIX + mode));
			
			String siteString = parameters.getString(IUGSConstant.FAIL_ON_ERROR_SITES + mode);
			Set<String> siteSet = new HashSet<String>();
			// sites are divided by ,
	        if (siteString != null) {
	            StringTokenizer st = new StringTokenizer(siteString, ",");
	            while (st.hasMoreElements()) {
	                String site = (String)st.nextElement();
	                siteSet.add(site.trim());
	            }
	        }
	        setFailOnErrorSites(siteSet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }    

	public Set<String> getFailOnErrorSites() {
		return failOnErrorSites;
	}

	public void setFailOnErrorSites(Set<String> failOnErrorSites) {
		this.failOnErrorSites = failOnErrorSites;
	}

	public String getEndPoint() {
        return endPoint;
    }

    private void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getMode() {
        return mode;
    }

    private void setMode(String mode) {
        this.mode = mode;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    private void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimeout() {
        return timeout;
    }

    private void setTimeout(String timeout) {
        this.timeout = timeout;
    }

	public String getSiteNamePrefix()
	{
		return siteNamePrefix;
	}

	private void setSiteNamePrefix(String siteNamePrefix)
	{
		this.siteNamePrefix = siteNamePrefix;
	}
}
