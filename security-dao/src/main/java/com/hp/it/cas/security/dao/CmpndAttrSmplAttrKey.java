package com.hp.it.cas.security.dao;


/**
 * A Data Access Object manages the connection with the data source.
 * This represents the primary key for the CMPND_ATTR_SMPL_ATTR table.
 *
 * @author CAS Generator v1.0.0
 */
public class CmpndAttrSmplAttrKey {
	
	private String cmpndUserAttrId;
	private String smplUserAttrId;

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
				+ ((cmpndUserAttrId == null) ? 0 : cmpndUserAttrId.hashCode());
		result = prime * result
				+ ((smplUserAttrId == null) ? 0 : smplUserAttrId.hashCode());
		return result;
	}
	
	/**
     * Test the CmpndAttrSmplAttr key for equality.
     *
     * @param   obj  the object to be tested
     *
     * @return  true if the object CmpndAttrSmplAttrKey is equal to this CmpndAttrSmplAttrKey
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
	    if (obj == null)
			return false;
		if (!(obj instanceof CmpndAttrSmplAttrKey))
			return false;
		CmpndAttrSmplAttrKey other = (CmpndAttrSmplAttrKey) obj;

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
        return String.format("%s [ %s, %s ]", getClass().getSimpleName(), cmpndUserAttrId, smplUserAttrId );    
    }
}