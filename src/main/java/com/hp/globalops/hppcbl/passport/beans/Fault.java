/*$Id: Fault.java,v 1.1 2006/11/08 07:33:06 yjie Exp $*/
package com.hp.globalops.hppcbl.passport.beans;

public class Fault implements java.io.Serializable, java.lang.Comparable  {
    private int ruleNumber;
    private String fieldName;
    private String code;
    private String description;
    private String ftype;
    private String localizedMsg;

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
                ", ftype='" + ftype + "'" +
                "}";
    }

	public int compareTo(Object o) {
		Fault f = (Fault)o ;
		return ((getRuleNumber() - f.getRuleNumber()) > 0)?1:(((getRuleNumber() - f.getRuleNumber()) < 0)?-1:0) ; 
	}

	public String getFtype() {
		return ftype;
	}

	public void setFtype(String ftype) {
		this.ftype = ftype;
	}

	public String getLocalizedMsg() {
		return localizedMsg;
	}

	public void setLocalizedMsg(String localizedMsg) {
		this.localizedMsg = localizedMsg;
	}
}