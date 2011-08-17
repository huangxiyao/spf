package com.hp.spp.portal.login.model;

public class UPSRule {
	private String mRuleType;
	private String mRuleClasses;
	private String mSiteIdentifier;	
	
	public UPSRule(String ruleType, String ruleClasses, String siteIdentifier) {
		super();
		mRuleType = ruleType;
		mRuleClasses = ruleClasses;
		mSiteIdentifier = siteIdentifier;
	}
	
	public String getRuleType() {
		return mRuleType;
	}
	public void setRuleType(String ruleType) {
		mRuleType = ruleType;
	}
	public String getRuleClasses() {
		return mRuleClasses;
	}
	public void setRuleClasses(String ruleClasses) {
		mRuleClasses = ruleClasses;
	}
	public String getSiteIdentifier() {
		return mSiteIdentifier;
	}
	public void setSiteIdentifier(String siteIdentifier) {
		mSiteIdentifier = siteIdentifier;
	}
}
