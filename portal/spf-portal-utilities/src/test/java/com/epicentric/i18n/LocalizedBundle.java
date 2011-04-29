package com.epicentric.i18n;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class LocalizedBundle extends ResourceBundle{
		@SuppressWarnings("unchecked")
		Hashtable resources = new Hashtable<String, Object>(){{
			put("test", "test");
			put("key", "Test & Test");
			put("clean_key", "Test and Test");
			put("lt_key", "Test < Test");
		}};

		@SuppressWarnings("unchecked")
		@Override
		public Enumeration<String> getKeys() {
			return (Enumeration<String>)resources.keys();
		}

		@Override
		public Object handleGetObject(String key) {
			if( resources.containsKey(key)){
				return resources.get(key);
			}
			return null;
		}
		
		public String getString(String key, String defaultValue){
			String value = (String)handleGetObject(key);
			if( null == value){
				return defaultValue;
			}
			return value;
		}
		
	}	

