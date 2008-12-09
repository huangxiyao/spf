package com.hp.globalops.hppcbl.passport.manager;

import java.util.ResourceBundle;

import com.hp.it.spf.xa.common.PropertyResourceBundleManager;

public class PassportParametersManager {

    private static PassportParametersManager M_wsManager = new PassportParametersManager();

    private String mode = null;

    private String endPoint = null;

    private String nonProxyHosts = null;

    private String userName = null;

    private String password = null;

    private String defaultLangCode = null;

    private String proxyHost = null;

    private String proxyPort = null;

    private String adminUser = null;

    private String adminPassword = null;

    private PassportParametersManager() {
        super();
        // init();
    }

    public static PassportParametersManager getInstance() {
        return M_wsManager;
    }

    private void init() {
        ResourceBundle parameters = PropertyResourceBundleManager
                .getBundle(IConstantPassportService.WSPARAMETERSFILENAME);
        setMode(parameters.getString(IConstantPassportService.MODE));
        // System.out.println("Mode=" + getMode());

        // Get the configuration information based on mode
        setDefaultLangCode(parameters
                .getString(IConstantPassportService.DEFAULTLANGCODE_PREFIX
                        + getMode()));
        setEndPoint(parameters
                .getString(IConstantPassportService.ENDPOINTS_PREFIX + getMode()));
        setUserName(parameters
                .getString(IConstantPassportService.USERNAME_PREFIX + getMode()));
        setPassword(parameters
                .getString(IConstantPassportService.PASSWORD_PREFIX + getMode()));

        setProxyHost(parameters
                .getString(IConstantPassportService.PROXYHOST_PREFIX + getMode()));
        setProxyPort(parameters
                .getString(IConstantPassportService.PROXYPORT_PREFIX + getMode()));
        setNonProxyHosts(parameters
                .getString(IConstantPassportService.NONPROXYHOSTS_PREFIX + getMode()));

        setAdminUser(parameters
                .getString(IConstantPassportService.ADMINUSER_PREFIX + getMode()));
        setAdminPassword(parameters
                .getString(IConstantPassportService.ADMINPASSWORD_PREFIX + getMode()));
    }

    public static PassportParametersManager getM_wsManager() {
        return M_wsManager;
    }

    public static void setM_wsManager(PassportParametersManager manager) {
        M_wsManager = manager;
    }

    public String getAdminPassword() {
        return PropertyResourceBundleManager.getString(
                IConstantPassportService.WSPARAMETERSFILENAME,
                IConstantPassportService.ADMINPASSWORD_PREFIX + getMode());
    }

    private void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminUser() {
        return PropertyResourceBundleManager.getString(
                IConstantPassportService.WSPARAMETERSFILENAME,
                IConstantPassportService.ADMINUSER_PREFIX + getMode());
    }

    private void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    public String getDefaultLangCode() {
        return PropertyResourceBundleManager.getString(
                IConstantPassportService.WSPARAMETERSFILENAME,
                IConstantPassportService.DEFAULTLANGCODE_PREFIX + getMode());
    }

    private void setDefaultLangCode(String defaultLangCode) {
        this.defaultLangCode = defaultLangCode;
    }

    public String getEndPoint() {
        return PropertyResourceBundleManager.getString(
                IConstantPassportService.WSPARAMETERSFILENAME,
                IConstantPassportService.ENDPOINTS_PREFIX + getMode());
    }

    private void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getMode() {
        return PropertyResourceBundleManager.getString(
                IConstantPassportService.WSPARAMETERSFILENAME,
                IConstantPassportService.MODE);
    }

    private void setMode(String mode) {
        this.mode = mode;
    }

    public String getNonProxyHosts() {
        return PropertyResourceBundleManager.getString(
                IConstantPassportService.WSPARAMETERSFILENAME,
                IConstantPassportService.NONPROXYHOSTS_PREFIX + getMode());
    }

    private void setNonProxyHosts(String nonProxyHosts) {
        this.nonProxyHosts = nonProxyHosts;
    }

    public String getPassword() {
        return PropertyResourceBundleManager.getString(
                IConstantPassportService.WSPARAMETERSFILENAME,
                IConstantPassportService.PASSWORD_PREFIX + getMode());
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public String getProxyHost() {
        return PropertyResourceBundleManager.getString(
                IConstantPassportService.WSPARAMETERSFILENAME,
                IConstantPassportService.PROXYHOST_PREFIX + getMode());
    }

    private void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPort() {
        return PropertyResourceBundleManager.getString(
                IConstantPassportService.WSPARAMETERSFILENAME,
                IConstantPassportService.PROXYPORT_PREFIX + getMode());
    }

    private void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getUserName() {
        return PropertyResourceBundleManager.getString(
                IConstantPassportService.WSPARAMETERSFILENAME,
                IConstantPassportService.USERNAME_PREFIX + getMode());
    }

    private void setUserName(String userName) {
        this.userName = userName;
    }
}
