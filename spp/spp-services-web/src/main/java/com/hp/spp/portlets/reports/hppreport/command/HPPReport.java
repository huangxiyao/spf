package com.hp.spp.portlets.reports.hppreport.command;

import com.hp.spp.portlets.reports.hppreport.model.HPPReportModel;

/**
* This class is a composite command  and composed of SearchEntiyCommand command
* It stores the search details in SearchEntiyCommand 
* It holds the group details in groupCommand attribute
* It is also composed of HPPReportModel object. This object is populated in 
* the controller and set in to the command class and the same model will be used 
* in the view
*  
* @author girishsk
*/

/** Revision History:
*
* Ver.   Modified By           Date           Notes
*--------------------------------------------------------------------------------------*
* v1     girishsk           25-Oct-2006      Created
*
*/

public class HPPReport extends BaseCommand{	
	private static final long serialVersionUID = 1L;
	private SearchEntiyCommand searchEntityCommand;
	private String groupCommand;
	private HPPReportModel hppReportModel;
	public HPPReport(){
		searchEntityCommand = new SearchEntiyCommand();
		groupCommand = "";
		
	}
	public String getGroupCommand() {
		return groupCommand;
	}
	public void setGroupCommand(String groupCommand) {
		this.groupCommand = groupCommand;
	}
	
	public SearchEntiyCommand getSearchEntityCommand() {
		return searchEntityCommand;
	}
	public void setSearchEntityCommand(SearchEntiyCommand searchEntityCommand) {
		this.searchEntityCommand = searchEntityCommand;
	}
	/**
	 * @return Returns the hppReportModel.
	 */
	public HPPReportModel getHppReportModel() {
		return hppReportModel;
	}
	/**
	 * @param hppReportModel The hppReportModel to set.
	 */
	public void setHppReportModel(HPPReportModel hppReportModel) {
		this.hppReportModel = hppReportModel;
	}

}
