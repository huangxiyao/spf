package com.hp.spp.groups;

import org.w3c.dom.Element;

import java.io.Serializable;

public class GroupStatus implements Serializable {
	
	public GroupStatus(String groupName){
		mGroupName = groupName;
	}
	
	public final static int NEW_GROUP = 0;
	
	public final static int EXISTING_GROUP = 1;
	
	
	private String mGroupName;
	
	private Element xmlFragment;
	
	private int existingFlag;
	
	
	public int getExistingFlag() {
		return existingFlag;
	}
	public void setExistingFlag(int existingFlag) {
		this.existingFlag = existingFlag;
	}
	public String getMGroupName() {
		return mGroupName;
	}
	public void setMGroupName(String groupName) {
		mGroupName = groupName;
	}
	public Element getXmlFragment() {
		return xmlFragment;
	}
	public void setXmlFragment(Element xmlFragment) {
		this.xmlFragment = xmlFragment;
	}
	
	
}
