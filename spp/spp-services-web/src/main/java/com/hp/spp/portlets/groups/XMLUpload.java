package com.hp.spp.portlets.groups;

import java.io.Serializable;
import java.util.List;

public class XMLUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    private String file;
    
    private List updatedGroupMap;

	public XMLUpload() {
    }
    
	public XMLUpload(String file, List updatedGroupMap) {
        setFile(file);
        setUpdatedGroupMap(updatedGroupMap);
    }
    
    public void setFile(String file) {
        this.file = file;
    }
    
    public String getFile() {
        return file;
    }

	public List getUpdatedGroupMap() {
		return updatedGroupMap;
	}

	public void setUpdatedGroupMap(List updatedGroupMap) {
		this.updatedGroupMap = updatedGroupMap;
	}
    
}
