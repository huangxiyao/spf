package com.hp.it.cas.security.dao;


/**
 * A Data Access Object manages the connection with the data source.
 * This represents the primary key for the USER_ID_TYPE table.
 *
 * @author CAS Generator v1.0.0
 */
public class UserIdTypeKey {
	
	private String userIdTypeCd;

	/**
	* Returns the USER_ID_TYPE_CD column.
	* @return  userIdTypeCd.
	*/
	public String getUserIdTypeCd(){
		return this.userIdTypeCd;
	}
	
    /**
	 * Sets the USER_ID_TYPE_CD column.
	 * @param userIdTypeCd a String type value 
	 */
	public void setUserIdTypeCd(String userIdTypeCd){
		this.userIdTypeCd = userIdTypeCd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((userIdTypeCd == null) ? 0 : userIdTypeCd.hashCode());
		return result;
	}
	
	/**
     * Test the UserIdType key for equality.
     *
     * @param   obj  the object to be tested
     *
     * @return  true if the object UserIdTypeKey is equal to this UserIdTypeKey
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
	    if (obj == null)
			return false;
		if (!(obj instanceof UserIdTypeKey))
			return false;
		UserIdTypeKey other = (UserIdTypeKey) obj;

		if (userIdTypeCd == null) {
			if (other.userIdTypeCd != null)
				return false;
		} else if (!userIdTypeCd.equals(other.userIdTypeCd))
			return false;

		return true;
	}
	
	/**
     * A representation of this object.
     *
     * @return  the string representation
     */
	public String toString() {
        return String.format("%s [ %s ]", getClass().getSimpleName(), userIdTypeCd );    
    }
}