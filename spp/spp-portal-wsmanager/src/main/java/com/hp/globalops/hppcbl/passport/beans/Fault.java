/*$Id: Fault.java,v 1.1 2006/09/08 14:32:22 geymonda Exp $*/
package com.hp.globalops.hppcbl.passport.beans;

public class Fault implements java.io.Serializable, java.lang.Comparable  {
    private int ruleNumber;
    private String fieldName;
    private String code;
    private String description;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Fault() {
    }

    public Fault(int ruleNumber) {
        this.ruleNumber = ruleNumber;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Fault))
            return false;
        Fault fault = (Fault) o;
        return fault.getRuleNumber() == ruleNumber;
    }

    public int getRuleNumber() {
        return ruleNumber;
    }

    public void setRuleNumber(int ruleNumber) {
        this.ruleNumber = ruleNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String toString() {
        return "Fault{" +
                "ruleNumber=" + ruleNumber +
                ", fieldName='" + fieldName + "'" +
                ", code='" + code + "'" +
                ", description='" + description + "'" +
                "}";
    }

	public int compareTo(Object o) {
		Fault f = (Fault)o ;
		return ((getRuleNumber() - f.getRuleNumber()) > 0)?1:(((getRuleNumber() - f.getRuleNumber()) < 0)?-1:0) ; 
	}
}