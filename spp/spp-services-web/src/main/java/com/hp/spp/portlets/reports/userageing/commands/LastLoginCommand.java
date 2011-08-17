package com.hp.spp.portlets.reports.userageing.commands;


import java.io.Serializable;
/**
 * Command class for user ageing report
 * @author girishsk
 *
 */
public class LastLoginCommand implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Number of days since user last logged into SPP
	 */
	String lastLoginNumDays;

	
	
	public String getLastLoginNumDays() {
		return lastLoginNumDays;
	}

	public void setLastLoginNumDays(String lastLoginNumDays) {
		this.lastLoginNumDays = lastLoginNumDays;
	}
}