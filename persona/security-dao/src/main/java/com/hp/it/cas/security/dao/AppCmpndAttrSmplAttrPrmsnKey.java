package com.hp.it.cas.security.dao;

import java.math.BigDecimal;

/**
 * A Data Access Object manages the connection with the data source.
 * This represents the primary key for the APP_CMPND_ATTR_SMPL_ATTR_PRMSN table.
 *
 * @author CAS Generator v1.0.0
 */
public class AppCmpndAttrSmplAttrPrmsnKey {
	
	private BigDecimal appPrtflId;
	private String cmpndUserAttrId;
	private String smplUserAttrId;

	/**
	* Returns the APP_PRTFL_ID column.
	* @return  appPrtflId.
	*/
	public BigDecimal getAppPrtflId(){
		return this.appPrtflId;
	}
	
    /**
	 * Sets the APP_PRTFL_ID column.
	 * @param appPrtflId a BigDecimal type value 
	 */
	public void setAppPrtflId(BigDecimal appPrtflId){
		this.appPrtflId = appPrtflId;
	}
	/**
	* Returns the CMPND_USER_ATTR_ID column.
	* @return  cmpndUserAttrId.
	*/
	public String getCmpndUserAttrId(){
		return this.cmpndUserAttrId;
	}
	
    /**
	 * Sets the CMPND_USER_ATTR_ID column.
	 * @param cmpndUserAttrId a String type value 
	 */
	public void setCmpndUserAttrId(String cmpndUserAttrId){
		this.cmpndUserAttrId = cmpndUserAttrId;
	}
	/**
	* Returns the SMPL_USER_ATTR_ID column.
	* @return  smplUserAttrId.
	*/
	public String getSmplUserAttrId(){
		return this.smplUserAttrId;
	}
	
    /**
	 * Sets the SMPL_USER_ATTR_ID column.
	 * @param smplUserAttrId a String type value 
	 */
	public void setSmplUserAttrId(String smplUserAttrId){
		this.smplUserAttrId = smplUserAttrId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((appPrtflId == null) ? 0 : appPrtflId.hashCode());
		result = prime * result
				+ ((cmpndUserAttrId == null) ? 0 : cmpndUserAttrId.hashCode());
		result = prime * result
				+ ((smplUserAttrId == null) ? 0 : smplUserAttrId.hashCode());
		return result;
	}
	
	/**
     * Test the AppCmpndAttrSmplAttrPrmsn key for equality.
     *
     * @param   obj  the object to be tested
     *
     * @return  true if the object AppCmpndAttrSmplAttrPrmsnKey is equal to this AppCmpndAttrSmplAttrPrmsnKey
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
	    if (obj == null)
			return false;
		if (!(obj instanceof AppCmpndAttrSmplAttrPrmsnKey))
			return false;
		AppCmpndAttrSmplAttrPrmsnKey other = (AppCmpndAttrSmplAttrPrmsnKey) obj;

		if (appPrtflId == null) {
			if (other.appPrtflId != null)
				return false;
		} else if (!appPrtflId.equals(other.appPrtflId))
			return false;
		if (cmpndUserAttrId == null) {
			if (other.cmpndUserAttrId != null)
				return false;
		} else if (!cmpndUserAttrId.equals(other.cmpndUserAttrId))
			return false;
		if (smplUserAttrId == null) {
			if (other.smplUserAttrId != null)
				return false;
		} else if (!smplUserAttrId.equals(other.smplUserAttrId))
			return false;

		return true;
	}
	
	/**
     * A representation of this object.
     *
     * @return  the string representation
     */
	public String toString() {
        return String.format("%s [ %s, %s, %s ]", getClass().getSimpleName(), appPrtflId, cmpndUserAttrId, smplUserAttrId );    
    }
}