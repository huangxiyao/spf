package com.sun.portal.portletcontainer.admin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

import com.sun.portal.container.EntityID;
import com.sun.portal.container.PortletID;
import com.sun.portal.container.PortletLang;
import com.sun.portal.portletcontainer.admin.registry.PortletRegistryTags;
import com.sun.portal.portletcontainer.admin.registry.database.dao.PortletAppRegistryDao;
import com.sun.portal.portletcontainer.admin.registry.database.dao.PortletDeploymentDescriptorDao;
import com.sun.portal.portletcontainer.admin.registry.database.dao.PortletWindowPreferenceRegistryDao;
import com.sun.portal.portletcontainer.admin.registry.database.dao.PortletWindowRegistryDao;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletApp;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletUserWindow;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletWindow;
import com.sun.portal.portletcontainer.admin.registry.database.utils.PortletRegistryUtils;
import com.sun.portal.portletcontainer.common.PortletDeployConfigReader;
import com.sun.portal.portletcontainer.common.PortletPreferencesUtility;
import com.sun.portal.portletcontainer.common.descriptor.DeploymentDescriptorException;
import com.sun.portal.portletcontainer.common.descriptor.DeploymentDescriptorReader;
import com.sun.portal.portletcontainer.common.descriptor.PortletAppDescriptor;
import com.sun.portal.portletcontainer.common.descriptor.PortletDescriptor;
import com.sun.portal.portletcontainer.common.descriptor.PortletInfoDescriptor;
import com.sun.portal.portletcontainer.common.descriptor.PortletPreferencesDescriptor;
import com.sun.portal.portletcontainer.common.descriptor.PortletsDescriptor;
import com.sun.portal.portletcontainer.common.descriptor.PreferenceDescriptor;
import com.sun.portal.portletcontainer.common.descriptor.SecurityConstraintDescriptor;
import com.sun.portal.portletcontainer.common.descriptor.SecurityRoleRefDescriptor;
import com.sun.portal.portletcontainer.common.descriptor.SupportsDescriptor;
import com.sun.portal.portletcontainer.common.descriptor.UserAttributeDescriptor;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryContext;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryContextAbstractFactory;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryContextFactory;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryException;
import com.sun.portal.portletcontainer.warupdater.PortletWebAppUpdater;

/**
 * PortletRegistryGenerator is responsible for parsing the portlet.xml
 * using the DeploymentDescriptorReader and generating Portlet Registry Elements
 * like PortletAppRegistry, PortletWindowRegistry and PortletWindowPreferenceRegistry
 * 
 * This class should override the one which has the same class name in OpenPortal porltet container
 */
public class PortletRegistryGenerator implements PortletRegistryTags {
	private static final String WEB_INF_PREFIX = "WEB-INF" + "/";
    private static final String WEB_XML = "web.xml";
    
    private static final String PORTLET_XML = "portlet.xml";
    private static final String SUN_PORTLET_XML = "sun-portlet.xml";
    private static final String DD_SUFFIX = "_portlet.xml";
    private static final String WAR_SUFFIX = ".war";
    
    private PortletsDescriptor portletsDescriptor;
    private PortletAppDescriptor portletAppDescriptor;
    private String portletAppName;
    private String warName;
    private Properties configProps = null;

    
    // Create a logger for this class
    private static Logger logger = Logger.getLogger(PortletRegistryGenerator.class.getPackage().getName(), "PALogMessages");
    
    public PortletRegistryGenerator() {
        configProps = new Properties();
    }
    
    // get the portlet.xml as InputStream
    private InputStream getPortletXmlStream(JarFile jar) throws Exception {
        InputStream in = null;
        try {
            ZipEntry portletXMLEntry = jar.getEntry(WEB_INF_PREFIX + PORTLET_XML );
            in = jar.getInputStream(portletXMLEntry);
        } catch (IOException ioe) {
            Object[] tokens = { jar.getName() };
            throw new PortletRegistryException("errorStreamRead",ioe, tokens);
        }catch (Exception ex) {
            String[] tokens = {WEB_INF_PREFIX + PORTLET_XML};
            throw new PortletRegistryException("invalidWar", ex , tokens);
        }
        return in;
    }
    
    private List getWebAppRoles(JarFile jar) throws Exception {
        InputStream webXMLStream = null;
        List roles = new ArrayList();
        try {
            ZipEntry webXMLEntry = jar.getEntry(WEB_INF_PREFIX + WEB_XML );
            webXMLStream = (InputStream)jar.getInputStream(webXMLEntry);
            roles = PortletWebAppUpdater.getRoles(webXMLStream);
        } catch (IOException ioe) {
            throw new PortletRegistryException("errorGettingRoles", ioe);
        }  catch (Exception ex) {
            String[] tokens = {WEB_INF_PREFIX + WEB_XML};
            throw new PortletRegistryException("invalidWar", ex , tokens);
        } finally {
            try {
                if(webXMLStream != null) {
                    webXMLStream.close();
                }
            } catch (IOException ignored) {}
        }
        return roles;
    }
    
    
    public void register(File updatedArchiveFile, 
    					 String warFileLocation,
    					 Properties roleProperties, 
    					 Properties userInfoProperties, 
    					 Properties portletWindowProperties,
    					 PortletLang portletLang) throws Exception {
    	
        List<PortletApp> portletAppList = new ArrayList<PortletApp>();
        List<PortletWindow> portletWindowList = new ArrayList<PortletWindow>();
        List<PortletUserWindow> portletWindowPreferenceList = new ArrayList<PortletUserWindow>();
        
        JarFile jar = new JarFile(updatedArchiveFile);
        String configFileLocation = PortletRegistryHelper.getConfigFileLocation();
        DeploymentDescriptorReader ddReader = new DeploymentDescriptorReader(configFileLocation);

        warName = updatedArchiveFile.getName();
        logger.log(Level.FINE, "PSPL_CSPPAM0005", warName);
        portletAppName = warName.substring(0, warName.lastIndexOf('.'));
        
        InputStream portletXmlStream = null;
        try {
            portletXmlStream = getPortletXmlStream(jar);
            portletAppDescriptor = ddReader.loadPortletAppDescriptor(portletAppName, portletXmlStream);
            ddReader.processDeployPortletExtensionDescriptor(updatedArchiveFile, configFileLocation);
        } catch (DeploymentDescriptorException dde) {
            Object[] tokens = {dde.toString()};
            throw new PortletRegistryException("errorReadingPortletDD", tokens);
        } finally {
            try {
                if(portletXmlStream != null) {
                    portletXmlStream.close();
                }
            } catch (IOException ignored) {}
        }
        portletsDescriptor = portletAppDescriptor.getPortletsDescriptor();
//        createPortletRegistryElements( roleProperties, userInfoProperties, getWebAppRoles(jar), portletLang);
        createPortletRegistryElements(portletAppList, portletWindowList, portletWindowPreferenceList, roleProperties, userInfoProperties, portletWindowProperties, getWebAppRoles(jar), portletLang);
        
        //cleanup portlets don't be used anymore
        cleanPortletApps(portletAppList, portletWindowList);
        
        PortletAppRegistryDao portletAppRegistryDao = new PortletAppRegistryDao();
        portletAppRegistryDao.addPortlets(portletAppList);
        logger.log(Level.FINE, "PSPL_CSPPAM0010", "save portletApps");
        
        PortletWindowRegistryDao portletWindowRegistryDao = new PortletWindowRegistryDao();
        portletWindowRegistryDao.addPortletWindows(portletWindowList);
        logger.log(Level.FINE, "PSPL_CSPPAM0010", "save portletWindows");
        
        PortletWindowPreferenceRegistryDao windowPreferenceRegistryDao = new PortletWindowPreferenceRegistryDao();
        windowPreferenceRegistryDao.addPortletWindowPreferences(portletWindowPreferenceList);
        logger.log(Level.FINE, "PSPL_CSPPAM0010", "portlet-window-preference-registry.xml");
        
        try {
            portletXmlStream = getPortletXmlStream(jar);
            copyPortletXML(portletXmlStream, warFileLocation, portletAppName);
        } catch (IOException ioe) {
            throw new PortletRegistryException("errorSavingFile", ioe);
        } finally {
            try {
                if(portletXmlStream != null) {
                    portletXmlStream.close();
                }
            } catch (IOException ignored) {}
        }
    }
    
    public String getPortletAppName() {
        return portletAppName;
    }
    
    public String getPortletWarName() {
        return warName;
    }
    
    /**
     * Create db portletRegistry elements
     * @param roleProperties
     * @param userInfoProperties
     * @param webAppRoles
     * @param portletLang
     * @throws PortletRegistryException
     */
    private void createPortletRegistryElements(List<PortletApp> portletAppList,
    										   List<PortletWindow> portletWindowList,
    										   List<PortletUserWindow> portletWindowPreferenceList,
    										   Properties roleProperties, 
    										   Properties userInfoProperties, 
    										   Properties portletWindowProperties,
    										   List webAppRoles, 
    										   PortletLang portletLang) throws PortletRegistryException {
        List portletDescriptors = portletsDescriptor.getPortletDescriptors();
        PortletApp portletApp; 
        PortletWindow portletWindow;
        PortletUserWindow portletWindowPreference;
        int size = portletDescriptors.size();
        if(size == 0){
            Object[] tokens = {"portlet.xml"};
            throw new PortletRegistryException("invalidWar", tokens);
        }

        for (int i = 0; i < portletDescriptors.size(); i++) {
            // Instantiate the objects required to write to the registry files
            portletApp = new PortletApp();
            portletWindow = new PortletWindow();
            portletWindowPreference = new PortletUserWindow();
            
            portletAppList.add(portletApp);
            portletWindowList.add(portletWindow);
            portletWindowPreferenceList.add(portletWindowPreference);
            
            PortletDescriptor portletDescriptor = (PortletDescriptor) portletDescriptors.get(i);
            String portletName = portletDescriptor.getPortletName();
            logger.log(Level.FINE, "PSPL_CSPPAM0007", portletName);
            
            PortletID portletID = new PortletID(getPortletAppName(), portletName);
            String portletIDValue = portletID.toString();
            logger.log(Level.FINEST, "PSPL_CSPPAM0008", portletIDValue);
            
            // fill PortletApp
            portletApp.setPortletName(portletIDValue);
            portletApp.setName(portletIDValue);
            
            // set portletApp ARCHIVE_NAME_KEY    
            portletApp.setArchiveName(getPortletWarName());
                        
            // set portletApp ARCHIVE_TYPE_KEY
            portletApp.setArchiveType(getPortletWarName().substring(getPortletWarName().lastIndexOf('.')+1));
                     
            // fill PortletWindow
            portletWindow.setPortletName(portletIDValue);
            portletWindow.setName(portletIDValue);
            portletWindow.setRemote(Boolean.FALSE.toString());
            portletWindow.setLang(portletLang.toString());
            

            
            // fill PortletWindowPreference
            portletWindowPreference.setPortletName(portletIDValue);
            portletWindowPreference.setWindowName(portletIDValue);
            portletWindowPreference.setUserName(PortletRegistryContext.USER_NAME_DEFAULT);
            
            EntityID entityID = new EntityID(portletID);
            String entityIDPrefix = entityID.getPrefix();
            logger.log(Level.FINE, "PSPL_CSPPAM0009", entityIDPrefix);
            // set portletWindow ENTITY_ID_PREFIX_KEY
            portletWindow.setEntityIDPrefix(entityIDPrefix);           
                        
            PortletInfoDescriptor portletInfo = portletDescriptor.getPortletInfoDescriptor();
            String title = portletName;
            String shortTitle = null;
            List keywords = null;
            
            if (portletInfo != null) {
                title = portletInfo.getTitle();
                shortTitle = portletInfo.getShortTitle();
                keywords = portletInfo.getKeywords();
            }
            
            // set PortletApp TITLE_KEY
            portletApp.setTitle(title);
                        
            // set PortletApp SHORT_TITLE_KEY
            portletApp.setShortTitle(shortTitle);
            
            // set PortletApp KEYWORDS_KEY
            PortletRegistryUtils.setCollectionProperty(portletApp, KEYWORDS_KEY, keywords);
                        
            // set portletWindow TITLE_KEY
            portletWindow.setTitle(title);           
            
            // When created through deploy, its visible
            if(portletWindowProperties != null) {
            	//String visible = portletWindowProperties.getProperty(VISIBLE_KEY, Boolean.FALSE.toString());
            	String visible = Boolean.FALSE.toString();
            	portletWindow.setVisible(visible);
            					
			} else {
				portletWindow.setVisible(Boolean.FALSE.toString());
			}
            
            // When created through deploy, its thick
            portletWindow.setWidth("thick");
            
            String description = "";
            if (portletDescriptor.getDescription() != null) {
                description = portletDescriptor.getDescription();
            }
            
            // set PortletApp TITLE_KEY
            portletApp.setTitle(title);
            
            // set PortletApp DESCRIPTION_KEY
            portletApp.setDescription(description);
                        
            PortletPreferencesDescriptor ppd = portletDescriptor.getPortletPreferencesDescriptor();
            List preferenceDescriptors = null;
            if (ppd != null) {
                preferenceDescriptors = ppd.getPreferenceDescriptors();
                if (preferenceDescriptors != null && !preferenceDescriptors.isEmpty()) {
                    Map preferences = new HashMap();
                    Map preferencesReadOnly = new HashMap();
                    for (int j = 0; j < preferenceDescriptors.size(); j++) {
                        PreferenceDescriptor prd = (PreferenceDescriptor) preferenceDescriptors.get(j);
                        String name = prd.getPrefName();
                        List values = prd.getPrefValues();
                        String value = PortletPreferencesUtility.getPreferenceString(values);
                        preferences.put(name, value);
                        String isReadOnly = String.valueOf(prd.getReadOnly());
                        preferencesReadOnly.put(name, isReadOnly);
                    }
                    PortletRegistryUtils.setCollectionProperty(portletWindowPreference, PREFERENCE_PROPERTIES_KEY, preferences);
                    PortletRegistryUtils.setCollectionProperty(portletWindowPreference, PREFERENCE_READ_ONLY_KEY, preferencesReadOnly);
                }
            }
            
            // Create roleMapping collection
            // validate roles
            List securityRoleRefDescriptors = portletDescriptor.getSecurityRoleRefDescriptors();
            if (securityRoleRefDescriptors!=null && securityRoleRefDescriptors.size() > 0) {
                List<String> roles = new ArrayList<String>();
                for (int k = 0; k < securityRoleRefDescriptors.size(); k++) {
                    SecurityRoleRefDescriptor srd = (SecurityRoleRefDescriptor) securityRoleRefDescriptors.get(k);
                    String roleLink = srd.getRoleLink();
                    String portletRole = srd.getRoleName();
                    if (webAppRoles.contains(roleLink)) {
                        roles.add(roleLink);
                    } else if (webAppRoles.contains(portletRole)) {
                        roles.add(portletRole);
                    } else {
                        Object[] tokens = {portletRole};
                        throw new PortletRegistryException("errorRoleValidation", tokens);
                    }
                }
                
                Map<String, String> roleMap = new HashMap<String, String>();
                for(Iterator j = roleProperties.entrySet().iterator();j.hasNext();) {
                    Map.Entry entry = (Map.Entry) j.next();
                    String key = (String) entry.getKey(); // webcontainer role
                    String value = (String) entry.getValue(); // web.xml role
                    roleMap.put(key,value);
                }
                // Check if role mapping is provided for all roles defined in web.xml
                if(!roles.isEmpty()) {
                    for(String role : roles) {
                        if(!roleMap.containsValue(role)) {
                            Object[] tokens = {role};
                            throw new PortletRegistryException("errorReverseRoleMapping", tokens);
                        }
                    }
                }
                if(!roleMap.isEmpty())  {
                	PortletRegistryUtils.setCollectionProperty(portletApp, ROLE_MAP_KEY, roleMap);                    
                }
             
                //create role descriptions
                boolean hasRoleDescriptions = false;
                HashMap roleDescriptions = new HashMap();
                
                for (int k = 0; k < securityRoleRefDescriptors.size(); k++) {
                    SecurityRoleRefDescriptor srd = (SecurityRoleRefDescriptor) securityRoleRefDescriptors.get(k);
                    Map roleDescMap = srd.getDescriptionMap();
                    if (roleDescMap != null && roleDescMap.size() > 0) {
                        hasRoleDescriptions = true;
                        
                        String name = srd.getRoleName();
                        if (!roles.contains(name)) {
                            String[] tokens = {srd.getRoleName()};
                            throw new PortletRegistryException("errorReverseRoleMapping", tokens);
                        }
                        HashMap descriptions = new HashMap();
                        for (Iterator j = roleDescMap.entrySet().iterator(); j.hasNext();) {
                            Map.Entry entry = (Map.Entry) j.next();
                            String lang = (String) entry.getKey();
                            String desc = (String) entry.getValue();
                            descriptions.put(lang, desc);
                        }
                        roleDescriptions.put(name, descriptions);
                    }
                }
                
                if (hasRoleDescriptions) {
                	PortletRegistryUtils.setCollectionProperty(portletApp, ROLE_DESCRIPTIONS_KEY, roleDescriptions);
                }
            }
            
            //create user info collection
            if (userInfoProperties!=null && !userInfoProperties.isEmpty()) {
                HashMap userInfoMap = new HashMap();
                Set keys = userInfoProperties.keySet();
                for (Iterator j = keys.iterator(); j.hasNext();) {
                    String key = (String) j.next();
                    String value = userInfoProperties.getProperty(key);
                    userInfoMap.put(key, value);
                }
                PortletRegistryUtils.setCollectionProperty(portletApp, USER_INFO_MAP_KEY, userInfoMap);
            }else{
                //Check if portlet.xml has user info attributes
                List userAttrDescriptors = portletAppDescriptor.getUserAttributeDescriptors();
                if (userAttrDescriptors!=null && userAttrDescriptors.size() > 0) {
                    HashMap userInfoMap = new HashMap();
                    
                    for (Iterator di = userAttrDescriptors.iterator(); di.hasNext();) {
                        UserAttributeDescriptor uad = (UserAttributeDescriptor) di.next();
                        String attrName = uad.getName();
                        userInfoMap.put(attrName,attrName);
                    }
                    if(userAttrDescriptors.size() > 0)
                    	PortletRegistryUtils.setCollectionProperty(portletApp, USER_INFO_MAP_KEY, userInfoMap);
                }
            }
            //create user info descriptions
            boolean hasUserAttrDescriptions = false;
            List userAttrDescriptors = portletAppDescriptor.getUserAttributeDescriptors();
            if (userAttrDescriptors!=null && userAttrDescriptors.size() > 0) {
                HashMap userAttrDescriptions = new HashMap();
                
                for (Iterator di = userAttrDescriptors.iterator(); di.hasNext();) {
                    UserAttributeDescriptor uad = (UserAttributeDescriptor) di.next();
                    Map userAttrDescMap = uad.getDescriptionMap();
                    if (userAttrDescMap.size() > 0) {
                        HashMap descriptions = new HashMap();
                        for (Iterator j = userAttrDescMap.entrySet().iterator(); j.hasNext();) {
                            Map.Entry entry = (Map.Entry) j.next();
                            String lang = (String) entry.getKey();
                            String desc = (String) entry.getValue();
                            descriptions.put(lang, desc);
                        }
                        userAttrDescriptions.put(uad.getName(), descriptions);
                    }
                }
                
                if (hasUserAttrDescriptions) {
                	PortletRegistryUtils.setCollectionProperty(portletApp, USER_INFO_DESCRIPTIONS_KEY, userAttrDescriptions);
                }
            }
            
            //create supports collection
            List supportsDescriptors = portletDescriptor.getSupportsDescriptors();
            if (supportsDescriptors!=null && supportsDescriptors != null) {
                HashMap supportsMap = new HashMap();
                List contentTypes = new ArrayList();
                for (Iterator j = supportsDescriptors.iterator(); j.hasNext();) {
                    SupportsDescriptor sd = (SupportsDescriptor) j.next();
                    String mimeType = sd.getMimeType();
                    contentTypes.add(mimeType);
                    List portletModes = sd.getPortletModes();
                    supportsMap.put(mimeType, portletModes);
                }
                PortletRegistryUtils.setCollectionProperty(portletApp, SUPPORTS_MAP_KEY, supportsMap);
                PortletRegistryUtils.setCollectionProperty(portletApp, SUPPORTED_CONTENT_TYPES_KEY, contentTypes);
            }
            
            //create display name properties
            Map displayNameMap = portletDescriptor.getDisplayNameMap();
            //TODO Why we are creating HashMap again????
            if (displayNameMap != null) {
                HashMap displayNames = new HashMap();
                
                for (Iterator j = displayNameMap.entrySet().iterator(); j.hasNext();) {
                    Map.Entry entry = (Map.Entry) j.next();
                    String lang = (String) entry.getKey();
                    String dn = (String) entry.getValue();
                    displayNames.put(lang, dn);
                }
                PortletRegistryUtils.setCollectionProperty(portletApp, DISPLAY_NAME_MAP_KEY, displayNames);
            }
            
            //create descriptions properties
            Map descriptionMap = portletDescriptor.getDescriptionMap();
            //TODO Why we are creating HashMap again????
            if (descriptionMap != null) {
                HashMap descriptions = new HashMap();
                
                for (Iterator j = descriptionMap.entrySet().iterator(); j.hasNext();) {
                    Map.Entry entry = (Map.Entry) j.next();
                    String lang = (String) entry.getKey();
                    String desc = (String) entry.getValue();
                    descriptions.put(lang, desc);
                }
                PortletRegistryUtils.setCollectionProperty(portletApp, DESCRIPTION_MAP_KEY, descriptions);
            }
            
            //create supported locales collection
            List supportedLocales = portletDescriptor.getSupportedLocales();
            if (supportedLocales != null) {
            	PortletRegistryUtils.setCollectionProperty(portletApp, SUPPORTED_LOCALES_KEY, supportedLocales);
            }
            
            //create transport-guarantee property, if required
            SecurityConstraintDescriptor scd = portletAppDescriptor.getSecurityConstraintDescriptor();
            if (scd != null) {
                List constrainedPortlets = scd.getConstrainedPortlets();
                if (constrainedPortlets != null && constrainedPortlets.contains(portletName)) {
                    String tgType = scd.getTransportGuaranteeType();
                    if (tgType != null && tgType.length() > 0) {
                    	portletApp.setTransportGuarantee(tgType);
                    }
                }
            }
        }
    }
    
    public Boolean unregister(String configFileLocation, String warFileLocation, String warName) throws Exception {
        File warFile = new File(warFileLocation, warName+WAR_SUFFIX);        
        String ddName = warName + DD_SUFFIX;
        PortletDeploymentDescriptorDao dao = new PortletDeploymentDescriptorDao();
        InputStream in = null;
        try {
        	in = dao.loadBeforeRemove(ddName);
            if(in == null) {
                if(logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING, "PSPL_CSPPAM0034", ddName + " doesn't exist");
                }
                return Boolean.FALSE;
            }
            Properties properties = new Properties();
            properties.put(PortletDeployConfigReader.VALIDATE_PROPERTY, "false");
            DeploymentDescriptorReader ddReader = new DeploymentDescriptorReader(properties);
            portletAppDescriptor = ddReader.loadPortletAppDescriptor(warName, in);
            if (warFile.exists()) {
            	ddReader.processUndeployPortletExtensionDescriptor(warFile, configFileLocation);
            }
        } catch (Exception ioe) {
            Object[] tokens = { warFileLocation + File.separator + ddName, warName + WAR_SUFFIX };
            throw new PortletRegistryException("errorStreamReadWhileUndeploy", ioe, tokens);
        }finally{
            try{
                if(in != null){
                    in.close();
                }
            }catch(Exception ignoreit){}
        }
        this.portletAppName = warName;
        
        portletsDescriptor = portletAppDescriptor.getPortletsDescriptor();
        List portletDescriptors = portletsDescriptor.getPortletDescriptors();
        PortletRegistryContext portletRegistryContext = getPortletRegistryContext();
        for (int i = 0; i < portletDescriptors.size(); i++) {
            PortletDescriptor portletDescriptor = (PortletDescriptor) portletDescriptors.get(i);
            String portletName = portletDescriptor.getPortletName();
            logger.log(Level.FINE, "PSPL_CSPPAM0007", portletName);
            PortletID portletID = new PortletID(getPortletAppName(), portletName);
            if(logger.isLoggable(Level.FINEST)) {
                logger.log(Level.FINEST, "PSPL_CSPPAM0008", portletID);
            }
            portletRegistryContext.removePortlet(portletID.toString());
        }
        return Boolean.TRUE;
    }
    
    public void removePortletWar(String warFileLocation, String warName) throws Exception {
        String ddName = warName + DD_SUFFIX;
        // Remove the portlet war and portlet xml created in pc.home/war directory
        File portletFile = new File(warFileLocation, ddName);
        boolean portletFileRemoved = portletFile.delete();
        if(logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "PSPL_CSPPAM0018",
                    new String[]{ portletFile.getAbsolutePath(), String.valueOf(portletFileRemoved) } );
        }
        File warFile = new File(warFileLocation, warName+WAR_SUFFIX);
        boolean warFileRemoved = warFile.delete();
        if(logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "PSPL_CSPPAM0019",
                    new String[]{ warFile.getAbsolutePath(), String.valueOf(warFileRemoved) } );
        }
    }
    
    public void registerRemote(String portletWindowName, String consumerId, String producerEntityId,
            String portletHandle, String portletId) throws Exception{
            	
        List portletWindowList = new ArrayList();
        List portletWindowPreferenceList = new ArrayList();
        
    	PortletWindow portletWindow = new PortletWindow();        
        
        portletWindow.setName(portletWindowName);
        portletWindow.setPortletName(portletId);
        portletWindow.setRemote(Boolean.TRUE.toString());
        
        portletWindow.setConsumerId(consumerId);
        portletWindow.setProducerEntityID(producerEntityId);
        portletWindow.setPortletHandle(portletHandle);
        portletWindow.setPortletID(portletId);
        
        portletWindowList.add(portletWindow);
        PortletWindowRegistryDao portletWindowRegistryDao = new PortletWindowRegistryDao();
        portletWindowRegistryDao.addPortletWindows(portletWindowList);
        logger.log(Level.FINE, "PSPL_CSPPAM0010", "save portletWindows");
        
        PortletUserWindow portletWindowPreference = new PortletUserWindow();
        portletWindowPreferenceList.add(portletWindowPreference);
        portletWindowPreference.setPortletName(portletId);
        portletWindowPreference.setWindowName(portletWindowName);
        Map preferences = new HashMap();
        preferences.put(PORTLET_HANDLE,portletHandle);
        PortletRegistryUtils.setCollectionProperty(portletWindowPreference, PREFERENCE_PROPERTIES_KEY, preferences);
        
        PortletWindowPreferenceRegistryDao windowPreferenceRegistryDao = new PortletWindowPreferenceRegistryDao();
        windowPreferenceRegistryDao.addPortletWindowPreferences(portletWindowPreferenceList);
        logger.log(Level.FINE, "PSPL_CSPPAM0010", "portlet-window-preference-registry.xml");
    }
    
    public void unregisterRemote(String portletWindowName) throws Exception{    	
    	getPortletRegistryContext().removePortletWindow(portletWindowName);
    }
    
    private void copyPortletXML(InputStream portletXMLStream, String warFileLocation,
            String portletAppName) throws Exception {
    	String descriptorName = portletAppName + DD_SUFFIX;
        try{            
        	PortletDeploymentDescriptorDao dao = new PortletDeploymentDescriptorDao();
        	dao.SaveOrUpdate(descriptorName, portletXMLStream);
        }catch(Exception ioe){
            throw new PortletRegistryException("errorStreamRead",ioe);
            
        }
        logger.log(Level.FINE, "PSPL_CSPPAM0011", "save " + descriptorName + " into database.");
    }							

    private PortletRegistryContext getPortletRegistryContext() throws PortletRegistryException {
        PortletRegistryContextAbstractFactory afactory = new PortletRegistryContextAbstractFactory();
        PortletRegistryContextFactory factory = afactory.getPortletRegistryContextFactory();
        return factory.getPortletRegistryContext();
    }
    
    private void cleanPortletApps(List<PortletApp> portletAppList,
			   List<PortletWindow> portletWindowList) {
    	/*
    	 * 1. get all portlets belongs to this app (query by archive name);
    	 * 2. compare with newAppList, and find the one doesn't be used anymore;
    	 * 3. call getPortletRegistryContext().removePortlet() method to cleanup the whole portlet doesn't be used
    	 * 4. for each portletapp, remove old registration information;
    	 * 5. for each portlet window, remove old registration information except the cloned ones;
    	 * 6. for each portlet window preference, remove old registration information except the cloned ones;
    	 * */
    	PortletAppRegistryDao portletAppRegistryDao = new PortletAppRegistryDao();
    	List<PortletApp> oldPortlets = portletAppRegistryDao.getPortletsForArchiveName(portletAppList.get(0).getArchiveName());
    	for (PortletApp oldPortlet : oldPortlets) {
			if (!portletAppList.contains(oldPortlet)) {
				try {
					//delete the portlet doesn't be used anymore
					getPortletRegistryContext().removePortlet(oldPortlet.getName());
				} catch (PortletRegistryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    	// retrieve the portlets will be overwritten
    	oldPortlets = portletAppRegistryDao.getPortletsForArchiveName(portletAppList.get(0).getArchiveName());
    	PortletWindowRegistryDao portletWindowRegistryDao = new PortletWindowRegistryDao();
    	PortletWindowPreferenceRegistryDao windowPreferenceRegistryDao = new PortletWindowPreferenceRegistryDao();
    	for (PortletApp oldPortlet : oldPortlets) {
    		portletAppRegistryDao.removePortlet(oldPortlet.getName());
    		portletWindowRegistryDao.removeNonClonedPortletWindows(oldPortlet.getName());
            windowPreferenceRegistryDao.removeNonClonedPortletWindowPreferences(oldPortlet.getName());
            //windowPreferenceRegistryDao.removeReadOnlyPortletWindowPreferences(oldPortlet.getName());
		}
    }
}
