package com.hp.spp.reports.hppreport;
import java.math.BigDecimal;
import java.util.Date;

/**
* Entity Class which represents an Users table information available
* in the vignette database.
*
* @author Shivashanker B
*/

/** Revision History:
*
* Ver.   Modified By           Date           Notes
*--------------------------------------------------------------------------------------*
* v1     Akash               03-Nov-2006      Created
*
*/

public class User {
	BigDecimal mId;
	String mUserName;
	String mFirstName;
	String mLastName;
	Date mLastLoginDate;	
	String mHppId;
	String mEmailId;

	
	public User() {
		super();
	}
	public String getFirstName() {
		return mFirstName;
	}
	public void setFirstName(String firstName) {
		mFirstName = firstName;
	}

	public String getLastName() {
		return mLastName;
	}
	public void setLastName(String lastName) {
		mLastName = lastName;
	}
	public String getUserName() {
		return mUserName;
	}
	public void setUserName(String userName) {
		mUserName = userName;
	}
	public BigDecimal getId() {
		return mId;
	}
	public void setId(BigDecimal id) {
		this.mId = id;
	}
	public Date getLastLoginDate() {
		return mLastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		mLastLoginDate = lastLoginDate;
	}
	public String getEmailId() {
		return mEmailId;
	}
	public void setEmailId(String emailId) {
		mEmailId = emailId;
	}
	public String getHppId() {
		return mHppId;
	}
	public void setHppId(String hppId) {
		mHppId = hppId;
	}	
}
