/**
 *
 */
package com.hp.spp.search.common;

import java.util.Collection;
import java.util.Locale;
import java.io.Serializable;


/**
 * @author bhujange
 *
 */
public class NavigationItem implements Serializable {
	String mId;
	String mName;
	Locale mLocale;
	String mLocation;
	String mUrl;
	Collection mPortlets;
	String mTitle;
	String mDescription;
	String[] mSearchKeywords;
	Collection mGroupList;
	String mSearchKeywordsString;
	String mDocumentType;
	Long lastUpdate = -1L;

	public NavigationItem() {
		super();
	}

	/**
	 * @return Returns the mDescription.
	 */
	public String getDescription() {
		return mDescription;
	}
	/**
	 * @param description The mDescription to set.
	 */
	public void setDescription(String description) {
		mDescription = description;
	}
	/**
	 * @return Returns the mGroupList.
	 */
	public Collection getGroupList() {
		return mGroupList;
	}
	/**
	 * @param groupList The mGroupList to set.
	 */
	public void setGroupList(Collection groupList) {
		mGroupList = groupList;
	}
	/**
	 * @return Returns the mId.
	 */
	public String getId() {
		return mId;
	}
	/**
	 * @param id The mId to set.
	 */
	public void setId(String id) {
		mId = id;
	}

	/**
	 * @return Returns the mLocation.
	 */
	public String getLocation() {
		return mLocation;
	}
	/**
	 * @param location The mLocation to set.
	 */
	public void setLocation(String location) {
		mLocation = location;
	}
	/**
	 * @return Returns the mName.
	 */
	public String getName() {
		return mName;
	}
	/**
	 * @param name The mName to set.
	 */
	public void setName(String name) {
		mName = name;
	}

	/**
	 * @return Returns the mSearchKeywords.
	 */
	public String[] getSearchKeywords() {
		return mSearchKeywords;
	}
	/**
	 * @param searchKeywords The mSearchKeywords to set.
	 */
	public void setSearchKeywords(String[] searchKeywords) {
		mSearchKeywords = searchKeywords;
	}
	/**
	 * @return Returns the mTitle.
	 */
	public String getTitle() {
		return mTitle;
	}
	/**
	 * @param title The mTitle to set.
	 */
	public void setTitle(String title) {
		mTitle = title;
	}
	/**
	 * @return Returns the mUrl.
	 */
	public String getUrl() {
		return mUrl;
	}
	/**
	 * @param url The mUrl to set.
	 */
	public void setUrl(String url) {
		mUrl = url;
	}

	public int compareTo(Object obj){
		return 0;

	}
	/**
	 * @return Returns the mPortlets.
	 */
	public Collection getPortlets() {
		return mPortlets;
	}
	/**
	 * @param portlets The mPortlets to set.
	 */
	public void setPortlets(Collection portlets) {
		mPortlets = portlets;
	}

	/**
	 * @return Returns the mLocale.
	 */
	public Locale getLocale() {
		return mLocale;
	}
	/**
	 * @param locale The mLocale to set.
	 */
	public void setLocale(Locale locale) {
		mLocale = locale;
	}
	/**
	 * @return Returns the mSearchKeywordsString.
	 */
	public String getSearchKeywordsString() {
		return mSearchKeywordsString;
	}

	/**
	 * @param searchKeywordsString The mSearchKeywordsString to set.
	 */
	public void setSearchKeywordsString(String searchKeywordsString) {
		mSearchKeywordsString = searchKeywordsString;
	}
	
	public String getDocumentType(){
		return mDocumentType;
	}
	
	public void setDocumentType(String documentType){
		mDocumentType = documentType;
	
	}
	
	public Long getLastUpdate(){
		return lastUpdate;
	}
	
	public void setLastUpdate(Long update){
		lastUpdate = update;
	}
}
