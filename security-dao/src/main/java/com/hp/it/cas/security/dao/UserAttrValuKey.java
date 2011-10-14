package com.hp.it.cas.security.dao;


/**
 * A Data Access Object manages the connection with the data source.
 * This represents the primary key for the USER_ATTR_VALU table.
 *
 * @author CAS Generator v1.0.0
 */
public class UserAttrValuKey {
	
	private String userId;
	private String userIdTypeCd;
	private String cmpndUserAttrId;
	private String smplUserAttrId;
	private String userAttrInstncId;

	/**
	* Returns the USER_ID column.
	* @return  userId.
	*/
	public String getUserId(){
		return this.userId;
	}
	
    /**
	 * Sets the USER_ID column.
	 * @param userId a String type value 
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}
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
	/**
	* Returns the USER_ATTR_INSTNC_ID column.
	* @return  userAttrInstncId.
	*/
	public String getUserAttrInstncId(){
		return this.userAttrInstncId;
	}
	
    /**
	 * Sets the USER_ATTR_INSTNC_ID column.
	 * @param userAttrInstncId a String type value 
	 */
	public void setUserAttrInstncId(String userAttrInstncId){
		this.userAttrInstncId = userAttrInstncId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((userId == null) ? 0 : userId.hashCode());
		result = prime * result
				+ ((userIdTypeCd == null) ? 0 : userIdTypeCd.hashCode());
		result = prime * result
				+ ((cmpndUserAttrId == null) ? 0 : cmpndUserAttrId.hashCode());
		result = prime * result
				+ ((smplUserAttrId == null) ? 0 : smplUserAttrId.hashCode());
		result = prime * result
				+ ((userAttrInstncId == null) ? 0 : userAttrInstncId.hashCode());
		return result;
	}
	
	/**
     * Test the UserAttrValu key for equality.
     *
     * @param   obj  the object to be tested
     *
     * @return  true if the object UserAttrValuKey is equal to this UserAttrValuKey
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
	    if (obj == null)
			return false;
		if (!(obj instanceof UserAttrValuKey))
			return false;
		UserAttrValuKey other = (UserAttrValuKey) obj;

		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userIdTypeCd == null) {
			if (other.userIdTypeCd != null)
				return false;
		} else if (!userIdTypeCd.equals(other.userIdTypeCd))
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
		if (userAttrInstncId == null) {
			if (other.userAttrInstncId != null)
				return false;
		} else if (!userAttrInstncId.equals(other.userAttrInstncId))
			return false;

		return true;
	}
	
	/**
     * A representation of this object.
     *
     * @return  the string representation
     */
	public String toString() {
        return String.format("%s [ %s, %s, %s, %s, %s ]", getClass().getSimpleName(), userId, userIdTypeCd, cmpndUserAttrId, smplUserAttrId, userAttrInstncId );    
    }
}