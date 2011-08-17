package com.hp.spp.portal.common.user;

public class User {
	private String mName;

	private String mLastName;

	private String mCountry;

	public User(String name, String lastName, String country) {
		mName = name;
		mLastName = lastName;
		mCountry = country;
	}

	public String getCountry() {
		return mCountry;
	}

	public void setCountry(String country) {
		mCountry = country;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getLastName() {
		return mLastName;
	}

	public void setLastName(String lastName) {
		mLastName = lastName;
	}

}
