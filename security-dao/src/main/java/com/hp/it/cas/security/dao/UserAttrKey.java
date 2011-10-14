package com.hp.it.cas.security.dao;


/**
 * A Data Access Object manages the connection with the data source.
 * This represents the primary key for the USER_ATTR table.
 *
 * @author CAS Generator v1.0.0
 */
public class UserAttrKey {
	
	private String userAttrId;

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
				+ ((userAttrId == null) ? 0 : userAttrId.hashCode());
		return result;
	}
	
	/**
     * Test the UserAttr key for equality.
     *
     * @param   obj  the object to be tested
     *
     * @return  true if the object UserAttrKey is equal to this UserAttrKey
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
	    if (obj == null)
			return false;
		if (!(obj instanceof UserAttrKey))
			return false;
		UserAttrKey other = (UserAttrKey) obj;

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
        return String.format("%s [ %s ]", getClass().getSimpleName(), userAttrId );    
    }
}