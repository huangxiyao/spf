package com.hp.spp.config;

import java.util.List;

public class PrefixingConfigEntryDAO implements ConfigEntryDAO {
	private ConfigEntryDAO mDelegate;
	private String mPrefix;

	public PrefixingConfigEntryDAO(ConfigEntryDAO delegate, String prefix) {
		mDelegate = delegate;
		setPrefix(prefix);
	}

	public String getPrefix() {
		return mPrefix;
	}

	public void setPrefix(String prefix) {
		if (prefix == null || prefix.trim().equals("")) {
			mPrefix = null;
		}
		else {
			mPrefix = prefix;
		}
	}

	public ConfigEntryDAO getDelegate() {
		return mDelegate;
	}

	public ConfigEntry load(String name) {
		if (mPrefix == null) {
			return mDelegate.load(name);
		}
		ConfigEntry entry = mDelegate.load(new StringBuffer(mPrefix).append('.').append(name).toString());
		if (entry != null) {
			return entry;
		}
		else {
			return mDelegate.load(name);
		}
	}

	public void save(ConfigEntry entry) {
		mDelegate.save(entry);
	}

	public void delete(String name) {
		mDelegate.delete(name);
	}

	public List getAllEntries() {
		return mDelegate.getAllEntries();
	}
}
