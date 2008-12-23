package com.sun.portal.portletcontainer.admin.registry.database;

import java.util.ArrayList;
import java.util.List;

import com.sun.portal.container.EntityID;
import com.sun.portal.container.PortletLang;
import com.sun.portal.container.PortletType;
import com.sun.portal.portletcontainer.admin.registry.PortletRegistryConstants;
import com.sun.portal.portletcontainer.admin.registry.PortletRegistryTags;
import com.sun.portal.portletcontainer.admin.registry.PortletWindowRegistryContext;
import com.sun.portal.portletcontainer.admin.registry.database.dao.PortletWindowRegistryDao;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletWindow;
import com.sun.portal.portletcontainer.admin.registry.database.utils.PortletRegistryUtils;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryException;

public class PortletWindowRegistryContextDBImpl implements PortletWindowRegistryContext{
	
	PortletWindowRegistryDao windowRegistryDao = null;
	
	public PortletWindowRegistryContextDBImpl() {
		windowRegistryDao = new PortletWindowRegistryDao();
	}
	
	public void createPortletWindow(String portletName, String portletWindowName)
			throws PortletRegistryException {
		createPortletWindow(portletName, portletWindowName, null, null);		
	}

	public void createPortletWindow(String portletName,
			                        String portletWindowName, 
			                        String title, 
			                        String locale)
			throws PortletRegistryException {
		// when OpenPortal fisrt deploy a portlet, it will create a portlet window
		// and set its portletWindowName as the value of the portlet name
		// all the windows need to be created should copy same meta data from this orignial type 
		PortletWindow existPortletWindow = windowRegistryDao.getPortletWindow(portletName);
		if (existPortletWindow == null) {
			return;
		}
		
		PortletWindow portletWindow = windowRegistryDao.getPortletWindow(portletWindowName);		
		if (portletWindow == null) {
			PortletWindow newPortletWindow = new PortletWindow();
			newPortletWindow.setName(portletWindowName);
			newPortletWindow.setPortletName(existPortletWindow.getPortletName());
			newPortletWindow.setUserName(existPortletWindow.getUserName());
			// additional relative to PortletWindowRegistryContextImpl
			newPortletWindow.setRemote(Boolean.toString(false));
			newPortletWindow.setVersion("1.0");
			// If title is not present use title of the existing portlet window name
            if(title == null){
            	title = PortletRegistryUtils.getStringProperty(existPortletWindow, PortletRegistryTags.TITLE_KEY);
            }
            PortletRegistryUtils.setStringProperty(newPortletWindow, PortletRegistryTags.TITLE_KEY, title);
            
            String entityIDPrefix = PortletRegistryUtils.getStringProperty(existPortletWindow, PortletRegistryTags.ENTITY_ID_PREFIX_KEY);
            PortletRegistryUtils.setStringProperty(newPortletWindow, PortletRegistryTags.ENTITY_ID_PREFIX_KEY, entityIDPrefix);
            
            PortletRegistryUtils.setStringProperty(newPortletWindow, PortletRegistryTags.WIDTH_KEY, PortletRegistryConstants.WIDTH_THIN);
            PortletRegistryUtils.setStringProperty(newPortletWindow, PortletRegistryTags.VISIBLE_KEY, Boolean.toString(false));
            
            // row
            int row = windowRegistryDao.getMaxRow();
            PortletRegistryUtils.setStringProperty(newPortletWindow, PortletRegistryTags.ROW_KEY, String.valueOf(row+1));
                        
            windowRegistryDao.addPortletWindow(newPortletWindow);
            
		} else {
			// If the portlet window is already present, update the title
            if(title != null){
            	this.setPortletWindowTitle(portletWindowName, title);
            }
		}		
	}

	public List<String> getAllPortletWindows(PortletType portletType) throws PortletRegistryException {
		return getAllPortletWindows(portletType, true);
	}

	public List<String> getVisiblePortletWindows(PortletType portletType)
			throws PortletRegistryException {
		// Return only visible portlet windows
        return getAllPortletWindows(portletType, false);		
	}
	
	private List<String> getAllPortletWindows(PortletType portletType, boolean allPortlets) throws PortletRegistryException {			 
		List<PortletWindow> portletWindows = windowRegistryDao.getAllPortletWindows();
		List<String> visiblePortletWindows = new ArrayList<String>();
		if(portletType == null){
            portletType = PortletType.ALL;
        }
		
        for(PortletWindow portletWindow : portletWindows){        	
        	String portletWindowName = portletWindow.getName();
            boolean isRemote = "true".equals(portletWindow.getRemote());
            
            // check is visible
            String visibleValue = PortletRegistryUtils.getStringProperty(portletWindow, PortletRegistryTags.VISIBLE_KEY);
            boolean isVisible = PortletRegistryConstants.VISIBLE_TRUE.equals(visibleValue);
                                    
            if ( portletType.equals(PortletType.ALL)
               ||(!isRemote && portletType.equals(PortletType.LOCAL))
               ||(isRemote && (portletType.equals(PortletType.REMOTE)))){
            	
                if (allPortlets || isVisible) {
                    visiblePortletWindows.add(portletWindowName);
                }
            }
        }
        return visiblePortletWindows;
	}
	
	public String getConsumerID(String portletWindowName) throws PortletRegistryException {
		PortletWindow portletWindow = windowRegistryDao.getPortletWindow(portletWindowName);
		String consumerId = PortletRegistryUtils.getStringProperty(portletWindow, PortletRegistryTags.CONSUMER_ID);
		return consumerId;
	}

	public EntityID getEntityId(String portletWindowName) throws PortletRegistryException {
		PortletWindow portletWindow = windowRegistryDao.getPortletWindow(portletWindowName);
		portletWindowName = portletWindow.getName();
        EntityID entityID = new EntityID();
        String entityIDPrefix = PortletRegistryUtils.getStringProperty(portletWindow, PortletRegistryTags.ENTITY_ID_PREFIX_KEY);
        entityID.setPrefix(entityIDPrefix);
        entityID.setPortletWindowName(portletWindowName);
		return entityID;
	}

	public List<EntityID> getEntityIds() throws PortletRegistryException {
		List<PortletWindow> portletWindows = windowRegistryDao.getAllPortletWindows();
		List<EntityID> entityIds = new ArrayList<EntityID>();
    
        for(PortletWindow portletWindow : portletWindows){ 
            String portletWindowName = portletWindow.getName();
            EntityID entityID = new EntityID();
            String entityIDPrefix = PortletRegistryUtils.getStringProperty(portletWindow, PortletRegistryTags.ENTITY_ID_PREFIX_KEY);
            entityID.setPrefix(entityIDPrefix);
            entityID.setPortletWindowName(portletWindowName);
            entityIds.add(entityID);
        }
        return entityIds;
	}
	
	
	public String getPortletID(String portletWindowName) throws PortletRegistryException {
		PortletWindow portletWindow = windowRegistryDao.getPortletWindow(portletWindowName);
		String portletId = PortletRegistryUtils.getStringProperty(portletWindow, PortletRegistryTags.PORTLET_ID);
		return portletId;
	}

	public PortletLang getPortletLang(String portletWindowName) throws PortletRegistryException {
		PortletWindow portletWindow = windowRegistryDao.getPortletWindow(portletWindowName);
		String lang = portletWindow.getLang();
        PortletLang portletLang = new PortletLang(lang);
        return portletLang;
	}

	public String getPortletName(String portletWindowName) throws PortletRegistryException {
		PortletWindow portletWindow = windowRegistryDao.getPortletWindow(portletWindowName);
		return portletWindow.getPortletName();
	}

	public String getPortletWindowTitle(String portletWindowName) throws PortletRegistryException {
		PortletWindow portletWindow = windowRegistryDao.getPortletWindow(portletWindowName);
		String title = PortletRegistryUtils.getStringProperty(portletWindow, PortletRegistryTags.TITLE_KEY);
		return title;
	}

	public List<String> getPortletWindows(String portletName) throws PortletRegistryException {		
		List<PortletWindow> portletWindows = windowRegistryDao.getPortletWindowsByPortletName(portletName);
		List<String> list = new ArrayList<String>();
		for (PortletWindow portletWindow : portletWindows) {
            list.add(portletWindow.getName());            
        }
        return list;
	}

	public String getProducerEntityID(String portletWindowName) throws PortletRegistryException {
		PortletWindow portletWindow = windowRegistryDao.getPortletWindow(portletWindowName);
		String producerEntityID = PortletRegistryUtils.getStringProperty(portletWindow, PortletRegistryTags.PRODUCER_ENTITY_ID);
		return producerEntityID;
	}

	public List<String> getRemotePortletWindows() throws PortletRegistryException {
		List<PortletWindow> portletWindows = windowRegistryDao.getAllPortletWindows();
		List<String> remotePortletWindows = new ArrayList<String>();
		for (PortletWindow portletWindow : portletWindows) {
			boolean isRemote = Boolean.valueOf(portletWindow.getRemote()).booleanValue();
            if(isRemote) {
                remotePortletWindows.add(portletWindow.getName());
            }
		}
		return remotePortletWindows;
	}

	public Integer getRowNumber(String portletWindowName) throws PortletRegistryException {
		PortletWindow portletWindow = windowRegistryDao.getPortletWindow(portletWindowName);
		String rowNumber = PortletRegistryUtils.getStringProperty(portletWindow, PortletRegistryTags.ROW_KEY);
		return new Integer(rowNumber);
	}

	public String getWidth(String portletWindowName) throws PortletRegistryException {
		PortletWindow portletWindow = windowRegistryDao.getPortletWindow(portletWindowName);
		String width = PortletRegistryUtils.getStringProperty(portletWindow, PortletRegistryTags.WIDTH_KEY);
		return width;
	}

	public boolean isRemote(String portletWindowName) throws PortletRegistryException {
		PortletWindow portletWindow = windowRegistryDao.getPortletWindow(portletWindowName);
		boolean isRemote = false;
        if(portletWindow.getRemote()!=null && !portletWindow.getRemote().trim().equals("")){
            isRemote = Boolean.valueOf(portletWindow.getRemote()).booleanValue();
        }
		return isRemote;
	}

	public boolean isVisible(String portletWindowName)
			throws PortletRegistryException {		
		// retreive the portlet window
		PortletWindow portletWindow = windowRegistryDao.getPortletWindow(portletWindowName);
		String visibleValue = PortletRegistryUtils.getStringProperty(portletWindow, PortletRegistryTags.VISIBLE_KEY);
		boolean isVisible = PortletRegistryConstants.VISIBLE_TRUE.equals(visibleValue);
		
		return isVisible;
	}

	public void removePortletWindow(String portletWindowName) throws PortletRegistryException {
		windowRegistryDao.removePortletWindow(portletWindowName);
	}

	public void removePortletWindows(String portletName) throws PortletRegistryException {
		windowRegistryDao.removePortletWindows(portletName);		
	}

	/**
	 * this func is different with PortletWindowRegistryContextImpl.setPortletWindowTitle
	 * for that function only change the title value in the portlet window object stored 
	 * in the JVM (collectionStringTable)
	 */
	public void setPortletWindowTitle(String portletWindowName, String title) throws PortletRegistryException {
		PortletWindow portletWindow = windowRegistryDao.getPortletWindow(portletWindowName);
		PortletRegistryUtils.setStringProperty(portletWindow, PortletRegistryTags.TITLE_KEY, title);
		windowRegistryDao.updatePortletWindow(portletWindow);		
	}

	public void setWidth(String portletWindowName, String width) throws PortletRegistryException {
		PortletWindow portletWindow = windowRegistryDao.getPortletWindow(portletWindowName);
		PortletRegistryUtils.setStringProperty(portletWindow, PortletRegistryTags.WIDTH_KEY, width);
		windowRegistryDao.updatePortletWindow(portletWindow);			
	}

	public void showPortletWindow(String portletWindowName, boolean visible) throws PortletRegistryException {
		PortletWindow portletWindow = windowRegistryDao.getPortletWindow(portletWindowName);
		PortletRegistryUtils.setStringProperty(portletWindow, PortletRegistryTags.VISIBLE_KEY, String.valueOf(visible));
		windowRegistryDao.updatePortletWindow(portletWindow);		
	}	
}
