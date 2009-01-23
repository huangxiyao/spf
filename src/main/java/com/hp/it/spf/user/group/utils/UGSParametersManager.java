package com.hp.it.spf.user.group.utils;

import java.util.ResourceBundle;

import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;

public class UGSParametersManager {

    private static UGSParametersManager m_UGSParamManager = new UGSParametersManager();
    
    private static ResourceBundle parameters = null;

    private String mode = null;

    private String endPoint = null;        

    private String userName = null;

    private String password = null;

    private String timeout = null;   
    

    private UGSParametersManager() {
        super();
        init();
    }

    public static UGSParametersManager getInstance() {
        if(needRefresh()) {
            m_UGSParamManager = new UGSParametersManager();
        }
        return m_UGSParamManager;
    }    
    
    private static boolean needRefresh(){
        ResourceBundle currRS = PropertyResourceBundleManager.getBundle(IUGSConstant.UGSPARAMETERSFILENAME);
        // if properties is modified
        return (parameters != currRS) ? true : false;
    }
    
    private void init() {        
        parameters = PropertyResourceBundleManager.getBundle(IUGSConstant.UGSPARAMETERSFILENAME);
        try {
            setMode(parameters.getString(IUGSConstant.MODE));
        
            setEndPoint(parameters.getString(IUGSConstant.ENDPOINTS_PREFIX + mode));
            setUserName(parameters.getString(IUGSConstant.USERNAME_PREFIX + mode));
            setPassword(parameters.getString(IUGSConstant.PASSWORD_PREFIX + mode));
            setTimeout(parameters.getString(IUGSConstant.TIMEOUT + mode));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
}
