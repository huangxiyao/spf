package com.hp.spp.portlets.eservices;

import java.io.Serializable;
import java.util.List;

public class EServiceXMLUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    private String file;
    
    private List updatedEServiceMap;

	public EServiceXMLUpload() {
    }
    
	public EServiceXMLUpload(String file, List updatedGroupMap) {
        setFile(file);
        setUpdatedEServiceMap(updatedGroupMap);
    }
    
    public void setFile(String file) {
        this.file = file;
    }
    
    public String getFile() {
        return file;
    }

	public List getUpdatedEServiceMap() {
		return updatedEServiceMap;
	}

	public void setUpdatedEServiceMap(List updatedGroupMap) {
		this.updatedEServiceMap = updatedGroupMap;
	}
    
}
