package com.sun.portal.portletcontainer.admin.registry.database.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.portal.portletcontainer.admin.registry.PortletRegistryTags;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletApp;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletAppPropertyCollection;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletAppPropertyMeta;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletUserWindow;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletUserWindowPreference;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryException;

/**
 * This is a util class to deal with the operation on Portlet Registry entity elements
 * like PortletApp, PortletWindow and PortletWindowPreference
 *
 */
public class PortletRegistryUtils {
	
	//------------------------------------PortletApp-------------------------------------------
	/**
	 * Retrieve the values for the Collection specified by the name. This Collection is
     * contained within the registry entity element.
     * <br>
	 * Each Collection element's name is the key of the map and elememt's value
	 * is the value of the map.
	 * <br>
	 * If the specified Collection doesn't exist, then return an empty map.
	 *  
	 * @param portletApp
	 * 					   PortletApp entity
	 * @param collectionName
	 *                     collection name
	 * @param elementName
	 *                     element name of the collection
	 * @return
	 * 					   elements map
	 */
	public static Map<String, String> getCollectionProperty(PortletApp portletApp, String collectionName) {
		Map<String, String> map = new HashMap<String, String>();
		// iterator all collections and set the specified collection's elements into the map
		Set<PortletAppPropertyCollection> collectionSet = portletApp.getPortletAppPropertyCollections();
		for (PortletAppPropertyCollection collection : collectionSet) {
			if (collection.getName().equals(collectionName)) {
				map.put(collection.getElementName(), collection.getElementValue());
			}
		}
		return map.size()>0 ? map : null;
	}
	
	/**
	 * Set the values for the Collection specified by the name. This Collection is
     * contained within the registry entity.
     * <br>
     * For xml type, each value in the list is represented as a value of the "value" attribute in the
     * String tag. This String tag is contained within the Collection tag.
     * 
	 * @param portletApp 
	 * 					  PortletApp entity
	 * @param collectionName
	 *                    collection name
	 * @param elementList
	 *                    collection element value list
	 */
	public static void setCollectionProperty(PortletApp portletApp, String collectionName, List<String> elementList) {
		// convert list<listValue> to Map<listValue, listValue>
		Map<String, String> map = new HashMap<String, String>();
        if(elementList != null) {
            int size = elementList.size();
            for (int i = 0; i < size; i++) {
                String s = (String) elementList.get(i);
                map.put(s, s);
            }
        }
        setCollectionProperty(portletApp, collectionName, map);    
	}
	
	/**
     * Set the values for the Collection specified by the key. This Collection is
     * contained within the registry entity.
     * <br>
     * For xml type, each key in the Map is represented as a value of the "name" attribute in the String tag
     * and value for the key is represented as a value for the "value" attribute in the String tag.
     * This String tag is contained within the Collection tag
     * <br>
     * If the collection already exist in to PortletApp, then this method cannot be used. 
     * For JPA doesn't support delete orphan, So we cannot synchronous the collection elements' value by simply 
     * remove elements from collection set.
     * 
	 * @param portletApp 
	 * 					  PortletApp entity
	 * @param collectionName
	 *                    collection name
	 * @param elementMap
	 *                    collection element value map
	 */
	@SuppressWarnings("unchecked")
	public static void setCollectionProperty(PortletApp portletApp, String collectionName, Map<String, String> elementMap) {
		Set<PortletAppPropertyCollection> existCollectionSet = portletApp.getPortletAppPropertyCollections();
		for (PortletAppPropertyCollection existCollection : existCollectionSet) {
			if(existCollection.getName().equals(collectionName)) {
				//can not set value to an existing collection
				return;
			}
		}
		// only can add none exist collection into the portletApp
		for (String elementName : elementMap.keySet()) {
			PortletAppPropertyCollection collection = new PortletAppPropertyCollection();
			collection.setName(collectionName);
			collection.setElementName(elementName);
			Object elementValue = elementMap.get(elementName);
			if(elementValue instanceof String) {
				collection.setElementValue(elementMap.get(elementName));
			} else if (elementValue instanceof List) {
				// if the element value is a list, then contact all the list value to 
				// string seperated with comma. e.g SUPPORTS_MAP_KEY
				StringBuffer bufferedValue = new StringBuffer();
				int i = ((List<String>)elementValue).size();
				for (Object value : ((List<String>)elementValue).toArray()) {
					bufferedValue.append(value);
					if (--i != 0) { 
						// if current obj is not the last one, then appand comma 
						bufferedValue.append(",");
					}
				}
				collection.setElementValue(bufferedValue.toString());
			}
			collection.setPortletApp(portletApp);
			portletApp.getPortletAppPropertyCollections().add(collection);
		}
	}
	
	/**
	 * Retrieve the String property value with the
	 * given property name in PortletApp object
	 * @param portletApp
	 *  			PortletApp entity
	 * @param propertyName
	 * 				property name
	 * @return porperty value
	 */
	public static String getStringProperty(PortletApp portletApp, String propertyName) {
		for(PortletAppPropertyMeta property : portletApp.getPortletAppPropertyMetas()) {
			if (property.getName().equals(propertyName)) {
        		return property.getValue();
        	}
        }  
        return null;
	}
	
	/**
	 * Set the property value into the portlet window object
	 * @param portletApp
	 *                PortletWindow entity
	 * @param propertyName
	 *                property name
	 * @param porpertyValue
	 *                property value
	 *                
	 */
	public static void setStringProperty(PortletApp portletApp, 
										 String propertyName, 
										 String porpertyValue) {
		// if the property already exists, update the value
		for(PortletAppPropertyMeta property : portletApp.getPortletAppPropertyMetas()) {
			if (property.getName().equals(propertyName)) {
				property.setValue(porpertyValue);
				return;
			}
		}
		// the property doesn't exist, add a new property 
		PortletAppPropertyMeta property = new PortletAppPropertyMeta();
		property.setName(propertyName);
		property.setValue(porpertyValue);
		property.setPortletApp(portletApp);		
		portletApp.getPortletAppPropertyMetas().add(property);
	}
	
	/**
	 * Convert Map<String, String> to List<String>
	 * 
	 * @param map
	 *         Map<String, String>
	 * @return
	 *         List<String>
	 */
	public static List<String> mapValuesToList(Map<String, String> map) {
        List<String> list = new ArrayList<String>();
        Set<String> keys = map.keySet();
        Iterator<String> itr = keys.iterator();
        while(itr.hasNext()){
            list.add((String)map.get(itr.next()));
        }
        return list;
    }
	
	/**
	 * Get supported portlet modes, it is stored as String seperated with comma.
	 * <br>
	 * e.g SUPPORTS_MAP_KEY -- 'VIEW,HELP,CONFIG'
	 * 
	 * @param portletApp
	 * @param portletName
	 * @return
	 * @throws PortletRegistryException
	 */
	public static List<String> getSupportedPortletModes(PortletApp portletApp, String portletName) 
										   throws PortletRegistryException {    
        Map<String, String> map = PortletRegistryUtils.getCollectionProperty(portletApp, PortletRegistryTags.SUPPORTS_MAP_KEY);
        List<String> list = new ArrayList<String>();
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for(Map.Entry<String, String> mapEntry : entries) {
            for(String s: mapEntry.getValue().split("[,]")) {
                list.add(s);
            }
        }
        return list;
    }
	//------------------------------------//PortletApp-------------------------------------------
	
	//------------------------------------PortletWindowPreference-------------------------------------------
	/**
	 * Set the values for the Collection tag specified by the key. This Collection tag is
     * contained within the registry element.
     * Each value in the list is represented as a value of the "value" attribute in the
     * String tag. This String tag is contained within the Collection tag.
     * 
	 * @param portletWindowPreference 
	 * 					  PortletWindowPreference entity
	 * @param collectionName
	 *                    collection property name
	 * @param elementList
	 *                    collection element value list
	 */
	public static void setCollectionProperty(PortletUserWindow portletWindowPreference, String collectionName, List<String> elementList) {
		Map<String, String> m = new HashMap<String, String>();
        if(elementList != null) {
            int size = elementList.size();
            for (int i = 0; i < size; i++) {
                String s = (String) elementList.get(i);
                m.put(s, s);
            }
        }
        setCollectionProperty(portletWindowPreference, collectionName, m);    
	}
	
	/**
     * Set the values for the Collection tag specified by the key. This Collection tag is
     * contained within the registry element.
     * Each key in the Map is represented as a value of the "name" attribute in the String tag
     * and value for the key is represented as a value for the "value" attribute in the String tag.
     * This String tag is contained within the Collection tag
     * 
     * <br>
     * If the collection already exist in to PortletApp, then this method cannot be used. 
     * For JPA doesn't support delete orphan, So we cannot synchronous the collection elements' value by simply 
     * remove elements from collection set.
     * <br>
     * we can remove all the collections from the set, then create new collections as the map, when update
     * preference, all the old collections will delete directly first, then the new collections will be 
     * created automatically by JPA
     * 
	 * @param portletWindowPreference 
	 * 					  PortletWindowPreference entity
	 * @param collectionName
	 *                    collection property name
	 * @param elementMap
	 *                    collection element value list
	 */
	public static void setCollectionProperty(PortletUserWindow portletWindowPreference, String collectionName, Map<String, String> elementMap) {
		// check if the collection exist or not
		Set<PortletUserWindowPreference> existCollectionSet = portletWindowPreference.getPortletWindowPreference();
		Set<PortletUserWindowPreference> removeSet = new HashSet<PortletUserWindowPreference>();
		for (PortletUserWindowPreference existCollection : existCollectionSet) {
			if(existCollection.getType().equals(collectionName)) {
				removeSet.add(existCollection);
			}
		}
		existCollectionSet.removeAll(removeSet);
		
		// add collection
		for (String elementName : elementMap.keySet()) {
			PortletUserWindowPreference collection = new PortletUserWindowPreference();
			collection.setType(collectionName);
			collection.setPreferenceName(elementName);
			collection.setPreferenceValue(elementMap.get(elementName));		
			collection.setPortletUserWindow(portletWindowPreference);
			portletWindowPreference.getPortletWindowPreference().add(collection);
		}
	}
	//------------------------------------//PortletWindowPreference-------------------------------------------
}
