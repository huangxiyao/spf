package com.hp.spp.portlets.paramsets;

import java.io.Serializable;
import java.util.List;

public class StandardParameterSetXMLUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    private String file;
    
    private List updatedStandardParameterSetMap;

	public StandardParameterSetXMLUpload() {
    }
    
	public StandardParameterSetXMLUpload(String file, List updatedGroupMap) {
        setFile(file);
        setUpdatedStandardParameterSetMap(updatedGroupMap);
    }
    
    public void setFile(String file) {
        this.file = file;
    }
    
    public String getFile() {
        return file;
    }

	public List getUpdatedStandardParameterSetMap() {
		return updatedStandardParameterSetMap;
	}

	public void setUpdatedStandardParameterSetMap(List updatedGroupMap) {
		this.updatedStandardParameterSetMap = updatedGroupMap;
	}
    
}
