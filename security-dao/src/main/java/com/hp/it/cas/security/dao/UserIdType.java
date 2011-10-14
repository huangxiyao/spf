package com.hp.it.cas.security.dao;

import java.util.Date;


/**
 * A Data Access Object manages the connection with the data source.
 * This represents the data to/from the USER_ID_TYPE table.
 *
 * @author CAS Generator v1.0.0
 */
public class UserIdType {

    private UserIdTypeKey key;
    private String crtUserId;
    private Date crtTs;
    private String lastMaintUserId;
    private Date lastMaintTs;
    private String userIdTypeNm;
    private String userIdTypeDn;
    
    /**
	 * Sets the primary key for the USER_ID_TYPE table.
	 * @param key the primary key - cannot be null
	 */
    public void setKey(UserIdTypeKey key) {
        this.key = key;
    }

    /**
	 * Returns the primary key for the USER_ID_TYPE table.
	 * @return key.
	 */
    public UserIdTypeKey getKey() {
        return key;
    }

    /**
	 * Returns the CRT_USER_ID column.
	 * @return crtUserId.
	 */
    public String getCrtUserId(){
        return this.crtUserId;
    }

    /**
	 * Sets the CRT_USER_ID column.
	 * @param crtUserId  a String type value 
	 */
    public void setCrtUserId(String crtUserId){
        this.crtUserId = crtUserId;
    }
    /**
	 * Returns the CRT_TS column.
	 * @return crtTs.
	 */
    public Date getCrtTs(){
        return this.crtTs;
    }

    /**
	 * Sets the CRT_TS column.
	 * @param crtTs  a Date type value 
	 */
    public void setCrtTs(Date crtTs){
        this.crtTs = crtTs;
    }
    /**
	 * Returns the LAST_MAINT_USER_ID column.
	 * @return lastMaintUserId.
	 */
    public String getLastMaintUserId(){
        return this.lastMaintUserId;
    }

    /**
	 * Sets the LAST_MAINT_USER_ID column.
	 * @param lastMaintUserId  a String type value 
	 */
    public void setLastMaintUserId(String lastMaintUserId){
        this.lastMaintUserId = lastMaintUserId;
    }
    /**
	 * Returns the LAST_MAINT_TS column.
	 * @return lastMaintTs.
	 */
    public Date getLastMaintTs(){
        return this.lastMaintTs;
    }

    /**
	 * Sets the LAST_MAINT_TS column.
	 * @param lastMaintTs  a Date type value 
	 */
    public void setLastMaintTs(Date lastMaintTs){
        this.lastMaintTs = lastMaintTs;
    }
    /**
	 * Returns the USER_ID_TYPE_NM column.
	 * @return userIdTypeNm.
	 */
    public String getUserIdTypeNm(){
        return this.userIdTypeNm;
    }

    /**
	 * Sets the USER_ID_TYPE_NM column.
	 * @param userIdTypeNm  a String type value 
	 */
    public void setUserIdTypeNm(String userIdTypeNm){
        this.userIdTypeNm = userIdTypeNm;
    }
    /**
	 * Returns the USER_ID_TYPE_DN column.
	 * @return userIdTypeDn.
	 */
    public String getUserIdTypeDn(){
        return this.userIdTypeDn;
    }

    /**
	 * Sets the USER_ID_TYPE_DN column.
	 * @param userIdTypeDn  a String type value 
	 */
    public void setUserIdTypeDn(String userIdTypeDn){
        this.userIdTypeDn = userIdTypeDn;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
			return false;
        if (getClass() != obj.getClass())
            return false;
        UserIdType other = (UserIdType) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }
    
    /**
     * A representation of this object's key.
     *
     * @return  the string representation of the key
     */
    public String toString() {
        return String.format("%s [ %s ]", getClass().getSimpleName(), key);
    } 
}