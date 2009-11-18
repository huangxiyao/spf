package com.epicentric.i18n;

import java.util.Locale;


public class LocalizedBundleManager {
	
	private static LocalizedBundleManager mgr = new LocalizedBundleManager();

	public static LocalizedBundleManager getInstance() {
		return mgr;

	}
	
	public static LocalizedBundle getBundle(String uid, Locale locale){
		return new LocalizedBundle();
	}


}
