package com.hp.it.spf.portal.cleansheet;

public class HyperLink {
	private String displayName;
	private String name;
	private String url;
	
	/**
	 * default constructor
	 */
	public HyperLink() {}
	
	/**
	 * Constructor
	 * @param displayName
	 * @param name
	 * @param url
	 */
	public HyperLink(String displayName, String name, String url) {
		super();
		this.displayName = displayName;
		this.name = name;
		this.url = url;
	}


	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
