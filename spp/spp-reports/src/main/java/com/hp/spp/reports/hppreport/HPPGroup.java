package com.hp.spp.reports.hppreport;

/**
 * Entity Class which represents an HPP group
 * @author Akash
 */

/** Revision History:
*
* Ver.   Modified By           Date           Notes
*--------------------------------------------------------------------------------------*
* v1     Akash            03-Nov-2006      Created
*
*/
public class HPPGroup {	
	private String mHppGroup = null;
	private String mPortal = null;

	public HPPGroup() {
		super();
	}

	/**
	 * @return Returns the mHPPGroup.
	 */
	public String getHppGroup() {
		return mHppGroup;
	}

	/**
	 * @param group The mHPPGroup to set.
	 */
	public void setHppGroup(String group) {
		mHppGroup = group;
	}

	/**
	 * @return Returns the mPortal.
	 */
	public String getPortal() {
		return mPortal;
	}

	/**
	 * @param portal The mPortal to set.
	 */
	public void setPortal(String portal) {
		mPortal = portal;
	}

}
