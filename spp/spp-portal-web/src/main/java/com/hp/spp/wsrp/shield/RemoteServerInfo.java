package com.hp.spp.wsrp.shield;

import java.util.List;
import java.util.ArrayList;

public class RemoteServerInfo implements Comparable {
	private String mUrl;
	private List<String> mNames = new ArrayList<String>();
	private boolean mEnabled;

	public RemoteServerInfo(String url, boolean enabled) {
		mUrl = url;
		mEnabled = enabled;
	}

	public void addName(String name) {
		int i = 0;
		while (i < mNames.size() && mNames.get(i).compareToIgnoreCase(name) < 0) {
			i++;
		}

		mNames.add(i, name);
	}

	public String getUrl() {
		return mUrl;
	}

	public List<String> getNames() {
		return mNames;
	}

	public boolean isEnabled() {
		return mEnabled;
	}

	public int compareTo(Object o) {
		return mUrl.compareTo(((RemoteServerInfo) o).getUrl());
	}
}
