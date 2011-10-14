package com.hp.it.cas.security.dao;

import java.util.Date;


/**
 * A Data Access Object manages the connection with the data source.
 * This represents the data to/from the APP_USER_ATTR_PRMSN table.
 *
 * @author CAS Generator v1.0.0
 */
public class AppUserAttrPrmsn {

    private AppUserAttrPrmsnKey key;
    private String crtUserId;
    private Date crtTs;
    
    /**
	 * Sets the primary key for the APP_USER_ATTR_PRMSN table.
	 * @param key the primary key - cannot be null
	 */
    public void setKey(AppUserAttrPrmsnKey key) {
        this.key = key;
    }

    /**
	 * Returns the primary key for the APP_USER_ATTR_PRMSN table.
	 * @return key.
	 */
    public AppUserAttrPrmsnKey getKey() {
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
        AppUserAttrPrmsn other = (AppUserAttrPrmsn) obj;
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