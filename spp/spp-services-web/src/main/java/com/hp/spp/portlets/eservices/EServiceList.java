package com.hp.spp.portlets.eservices;

import java.util.List;

public class EServiceList {
	
	List eServiceIdList;
	List eServiceNameList;

	public void setEServiceIdList(List idList) {
		eServiceIdList = idList;
	}

	public void setEServiceNameList(List nameList) {
		eServiceNameList = nameList;
	}

	public List getEServiceIdList() {
		return eServiceIdList;
	}

	public List getEServiceNameList() {
		return eServiceNameList;
	}

}
