package com.hp.it.cas.security.dao;


/**
 * A Data Access Object manages the connection with the data source.
 * This represents the primary key for the USER_ATTR_TYPE table.
 *
 * @author CAS Generator v1.0.0
 */
public class UserAttrTypeKey {
	
	private String userAttrTypeCd;

	/**
	* Returns the USER_ATTR_TYPE_CD column.
	* @return  userAttrTypeCd.
	*/
	public String getUserAttrTypeCd(){
		return this.userAttrTypeCd;
	}
	
    /**
	 * Sets the USER_ATTR_TYPE_CD column.
	 * @param userAttrTypeCd a String type value 
	 */
	public void setUserAttrTypeCd(String userAttrTypeCd){
		this.userAttrTypeCd = userAttrTypeCd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((userAttrTypeCd == null) ? 0 : userAttrTypeCd.hashCode());
		return result;
	}
	
	/**
     * Test the UserAttrType key for equality.
     *
     * @param   obj  the object to be tested
     *
     * @return  true if the object UserAttrTypeKey is equal to this UserAttrTypeKey
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
	    if (obj == null)
			return false;
		if (!(obj instanceof UserAttrTypeKey))
			return false;
		UserAttrTypeKey other = (UserAttrTypeKey) obj;

		if (userAttrTypeCd == null) {
			if (other.userAttrTypeCd != null)
				return false;
		} else if (!userAttrTypeCd.equals(other.userAttrTypeCd))
			return false;

		return true;
	}
	
	/**
     * A representation of this object.
     *
     * @return  the string representation
     */
	public String toString() {
        return String.format("%s [ %s ]", getClass().getSimpleName(), userAttrTypeCd );    
    }
}