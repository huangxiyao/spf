/**
 *
 */
package com.hp.spp.search.common;

/**
 * @author bhujange
 *
 */
public class Portlet {
	String mId;
	String mName;
	String mTitle;
	String mDescription;
	String[] mSearchKeywords;
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
}
