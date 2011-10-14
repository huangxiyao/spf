package com.hp.it.cas.security.dao;

import java.math.BigDecimal;

/**
 * A Data Access Object manages the connection with the data source.
 * This represents the primary key for the APP_USER_ATTR_PRMSN table.
 *
 * @author CAS Generator v1.0.0
 */
public class AppUserAttrPrmsnKey {
	
	private BigDecimal appPrtflId;
	private String userAttrId;

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
	* Returns the USER_ATTR_ID column.
	* @return  userAttrId.
	*/
	public String getUserAttrId(){
		return this.userAttrId;
	}
	
    /**
	 * Sets the USER_ATTR_ID column.
	 * @param userAttrId a String type value 
	 */
	public void setUserAttrId(String userAttrId){
		this.userAttrId = userAttrId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((appPrtflId == null) ? 0 : appPrtflId.hashCode());
		result = prime * result
				+ ((userAttrId == null) ? 0 : userAttrId.hashCode());
		return result;
	}
	
	/**
     * Test the AppUserAttrPrmsn key for equality.
     *
     * @param   obj  the object to be tested
     *
     * @return  true if the object AppUserAttrPrmsnKey is equal to this AppUserAttrPrmsnKey
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
	    if (obj == null)
			return false;
		if (!(obj instanceof AppUserAttrPrmsnKey))
			return false;
		AppUserAttrPrmsnKey other = (AppUserAttrPrmsnKey) obj;

		if (appPrtflId == null) {
			if (other.appPrtflId != null)
				return false;
		} else if (!appPrtflId.equals(other.appPrtflId))
			return false;
		if (userAttrId == null) {
			if (other.userAttrId != null)
				return false;
		} else if (!userAttrId.equals(other.userAttrId))
			return false;

		return true;
	}
	
	/**
     * A representation of this object.
     *
     * @return  the string representation
     */
	public String toString() {
        return String.format("%s [ %s, %s ]", getClass().getSimpleName(), appPrtflId, userAttrId );    
    }
}