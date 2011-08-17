
package com.hp.spp.portlets.reports.hppreport.command;
/**
* The SearchEntiyCommand class is data binding object and bound to
* the Search Form in HppSupportReport view.
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

public class SearchEntiyCommand extends BaseCommand {
	
	private static final long serialVersionUID = 1L;	
	public static final String TYPE_USERID = "1";
	public static final String TYPE_HPPID = "2";
	public static final String TYPE_EMAILID = "3";	
	private String entityIdentifier;
	private String entityType;
	
	public SearchEntiyCommand(){
		entityIdentifier="";
		entityType=TYPE_USERID;
	}
	
	public String getEntityIdentifier() {
		return entityIdentifier;
	}
	public void setEntityIdentifier(String entityIdentifier) {
		this.entityIdentifier = entityIdentifier;
	}
	
	/**
	 * It will return the current entity type that is stored, default
	 * will return the userID entity type
	 * @return
	 */
	public String getEntityType() {
		return entityType;
	}
	
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	
}
