package com.hp.it.cas.security.dao;

import java.util.Date;


/**
 * A Data Access Object manages the connection with the data source.
 * This represents the data to/from the USER_ATTR table.
 *
 * @author CAS Generator v1.0.0
 */
public class UserAttr {

    private UserAttrKey key;
    private String crtUserId;
    private Date crtTs;
    private String lastMaintUserId;
    private Date lastMaintTs;
    private String userAttrTypeCd;
    private String userAttrNm;
    private String userAttrDn;
    private String userAttrDefnTx;
    
    /**
	 * Sets the primary key for the USER_ATTR table.
	 * @param key the primary key - cannot be null
	 */
    public void setKey(UserAttrKey key) {
        this.key = key;
    }

    /**
	 * Returns the primary key for the USER_ATTR table.
	 * @return key.
	 */
    public UserAttrKey getKey() {
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
	 * Returns the USER_ATTR_TYPE_CD column.
	 * @return userAttrTypeCd.
	 */
    public String getUserAttrTypeCd(){
        return this.userAttrTypeCd;
    }

    /**
	 * Sets the USER_ATTR_TYPE_CD column.
	 * @param userAttrTypeCd  a String type value 
	 */
    public void setUserAttrTypeCd(String userAttrTypeCd){
        this.userAttrTypeCd = userAttrTypeCd;
    }
    /**
	 * Returns the USER_ATTR_NM column.
	 * @return userAttrNm.
	 */
    public String getUserAttrNm(){
        return this.userAttrNm;
    }

    /**
	 * Sets the USER_ATTR_NM column.
	 * @param userAttrNm  a String type value 
	 */
    public void setUserAttrNm(String userAttrNm){
        this.userAttrNm = userAttrNm;
    }
    /**
	 * Returns the USER_ATTR_DN column.
	 * @return userAttrDn.
	 */
    public String getUserAttrDn(){
        return this.userAttrDn;
    }

    /**
	 * Sets the USER_ATTR_DN column.
	 * @param userAttrDn  a String type value 
	 */
    public void setUserAttrDn(String userAttrDn){
        this.userAttrDn = userAttrDn;
    }
    /**
	 * Returns the USER_ATTR_DEFN_TX column.
	 * @return userAttrDefnTx.
	 */
    public String getUserAttrDefnTx(){
        return this.userAttrDefnTx;
    }

    /**
	 * Sets the USER_ATTR_DEFN_TX column.
	 * @param userAttrDefnTx  a String type value 
	 */
    public void setUserAttrDefnTx(String userAttrDefnTx){
        this.userAttrDefnTx = userAttrDefnTx;
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
        UserAttr other = (UserAttr) obj;
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