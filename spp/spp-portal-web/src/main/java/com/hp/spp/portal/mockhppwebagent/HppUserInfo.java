package com.hp.spp.portal.mockhppwebagent;

public class HppUserInfo {
	private String mUsername;
	private String mProfileId;

	HppUserInfo(String username, String profileId) {
		mUsername = username;
		mProfileId = profileId;
	}

	public String getUsername() {
		return mUsername;
	}

	public String getProfileId() {
		return mProfileId;
	}

	public String toString() {
		return mUsername + "--" + mProfileId;
	}

	public static HppUserInfo parse(String s) {
		if (s == null || s.trim().equals("")) {
			return null;
		}
		String[] data = s.split("--");
		if (data == null || data.length != 2 || data[0] == null || data[1] == null) {
			return null;
		}
		return new HppUserInfo(data[0], data[1]);
	}
}
