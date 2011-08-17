package com.hp.spp.portlets.paramsets;

import java.util.List;

public class StandardParameterSetList {
	
	List standardParameterSetIdList;
	List standardParameterSetNameList;

	public void setStandardParameterSetIdList(List idList) {
		standardParameterSetIdList = idList;
	}

	public void setStandardParameterSetNameList(List nameList) {
		standardParameterSetNameList = nameList;
	}

	public List getStandardParameterSetIdList() {
		return standardParameterSetIdList;
	}

	public List getStandardParameterSetNameList() {
		return standardParameterSetNameList;
	}

}
