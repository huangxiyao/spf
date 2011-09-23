package com.hp.it.spf.misc.portal;

import com.vignette.portal.portlet.management.external.PortletPersistenceException;
import com.vignette.portal.portlet.management.internal.implementation.provider.jsr.JsrPortletApplicationSpiImpl;
import com.vignette.portal.portlet.management.internal.implementation.provider.jsr.JsrPortletTypeManagerSpiImpl;
import com.vignette.portal.portlet.management.internal.implementation.provider.jsr.JsrPortletTypeSpiImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Liu, Ye (HPIT-GADSC) (ye.liu@hp.com)
 * @author Xu, Ke-Jun (Daniel,HPIT-GADSC) (ke-jun.xu@hp.com)
 */
class PortletTypeHelper
{
    private static final int MAX_PORTLET_TYPES_TO_RETRIEVE = 1000;


    Set<String> createApplicationPortletTypes(String portletApplicationName, List<String> portletTypeNames) throws PortletPersistenceException
    {
        Set<String> locales = new HashSet<String>();
        locales.add("English");

        JsrPortletTypeManagerSpiImpl typeManager = JsrPortletTypeManagerSpiImpl.getInstance();

        Set<String> portletTypeIDs = new HashSet<String>();
        for (String typeName : portletTypeNames) {
            JsrPortletTypeSpiImpl type = typeManager.createJsrPortletType(portletApplicationName, typeName, typeName, "", null, false, null, locales);
            portletTypeIDs.add(type.getID());
        }

        return portletTypeIDs;
    }


    void createMissingPortletTypes(JsrPortletApplicationSpiImpl existingPortletApplication, LocalPortletApplicationBean application) throws PortletPersistenceException
    {

        @SuppressWarnings("unchecked")
        List<String> portletTypesToAdd = getNamesOfPortletTypesToAdd(
                existingPortletApplication.getPortletTypes(MAX_PORTLET_TYPES_TO_RETRIEVE),
                application.getTypeList());
        createApplicationPortletTypes(application.getName(), portletTypesToAdd);
    }


    private List<String> getNamesOfPortletTypesToAdd(
            Iterator<JsrPortletTypeSpiImpl> existingPortletTypes, List<String> targetPortletTypeNames)
    {
        List<String> namesOfPortletTypesToAdd = new ArrayList<String>(targetPortletTypeNames);

        while (existingPortletTypes.hasNext()) {
            JsrPortletTypeSpiImpl existingPortletType = existingPortletTypes.next();
            String existingPortletTypeName = existingPortletType.getName();

            if (targetPortletTypeNames.contains(existingPortletTypeName)) {
                namesOfPortletTypesToAdd.remove(existingPortletTypeName);
            }
        }

        return namesOfPortletTypesToAdd;
    }


    void deleteObsoletePortletTypes(JsrPortletApplicationSpiImpl existingPortletApplication, LocalPortletApplicationBean application) throws PortletPersistenceException
    {
        @SuppressWarnings("unchecked")
        Iterator<JsrPortletTypeSpiImpl> existingPortletTypes =
                existingPortletApplication.getPortletTypes(MAX_PORTLET_TYPES_TO_RETRIEVE);
        List<String> targetPortletTypeNames = application.getTypeList();

        while (existingPortletTypes.hasNext()) {
            JsrPortletTypeSpiImpl existingPortletType = existingPortletTypes.next();
            String existingPortletTypeName = existingPortletType.getName();

            if (!targetPortletTypeNames.contains(existingPortletTypeName)) {
                existingPortletType.delete();
            }
        }
    }
}
