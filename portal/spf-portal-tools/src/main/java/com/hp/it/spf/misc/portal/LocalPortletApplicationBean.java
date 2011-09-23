package com.hp.it.spf.misc.portal;

import java.util.List;

/**
 * @author Liu, Ye (HPIT-GADSC) (ye.liu@hp.com)
 */
class LocalPortletApplicationBean
{
    private String name;
    private String description;
    private String contextRoot;
    private List<String> typeList;

    public LocalPortletApplicationBean(String name, String description, String contextRoot,
                                       List<String> typeList)
    {
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("Portlet application name cannot be null or empty");
        }
        if (contextRoot == null || contextRoot.trim().equals("")) {
            throw new IllegalArgumentException("Portlet application contextRoot cannot be null or empty");
        }
        if (typeList == null || typeList.isEmpty()) {
            throw new IllegalArgumentException("Portlet application's portlet list cannot be empty");
        }
        for (String type : typeList) {
            if (type == null || type.trim().equals("")) {
                throw new IllegalArgumentException("Portlet name cannot be null or empty");
            }
        }

        this.name = name;
        this.description = description;
        this.contextRoot = contextRoot;
        this.typeList = typeList;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getContextRoot()
    {
        return contextRoot;
    }

    public List<String> getTypeList()
    {
        return typeList;
    }

    @Override
    public String toString()
    {
        return (" name = [" + name + "] ; contextRoot = [" + contextRoot + "] ; description =[" + description
                + "] ; typeList = [" + typeList + "]");
    }
}
