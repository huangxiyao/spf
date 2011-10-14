package com.hp.it.cas.security.dao;

import java.util.Date;


/**
 * A Data Access Object manages the connection with the data source.
 * This represents the data to/from the USER_ATTR_VALU table.
 *
 * @author CAS Generator v1.0.0
 */
public class UserAttrValu {

    private UserAttrValuKey key;
    private String crtUserId;
    private Date crtTs;
    private String lastMaintUserId;
    private Date lastMaintTs;
    private String userAttrValuTx;
    
    /**
	 * Sets the primary key for the USER_ATTR_VALU table.
	 * @param key the primary key - cannot be null
	 */
    public void setKey(UserAttrValuKey key) {
        this.key = key;
    }

    /**
	 * Returns the primary key for the USER_ATTR_VALU table.
	 * @return key.
	 */
    public UserAttrValuKey getKey() {
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
	 * Returns the USER_ATTR_VALU_TX column.
	 * @return userAttrValuTx.
	 */
    public String getUserAttrValuTx(){
        return this.userAttrValuTx;
    }

    /**
	 * Sets the USER_ATTR_VALU_TX column.
	 * @param userAttrValuTx  a String type value 
	 */
    public void setUserAttrValuTx(String userAttrValuTx){
        this.userAttrValuTx = userAttrValuTx;
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
        UserAttrValu other = (UserAttrValu) obj;
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