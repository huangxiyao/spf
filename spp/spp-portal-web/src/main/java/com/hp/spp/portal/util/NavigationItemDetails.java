/**
 *
 */
package com.hp.spp.portal.util;
import java.io.Serializable;
import java.util.Collection;


/**
 * @author Srikanth Adepu
 *@version SPP 4.7.2
 *
 */
public class NavigationItemDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mId;	
	private String mTitle;
	private String mType;
	private Collection mGroupList;
	
	public NavigationItemDetails() {
		super();
	}
	
	public<NavigationItemDetails> NavigationItemDetails(String id, String title, String type, Collection groupList) {
		this.mId = id;
		this.mTitle = title;
		this.mType = type;
		this.mGroupList = groupList;		
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
	 * @return Returns the mType.
	 */
	public String getType() {
		return mType;
	}
	/**
	 * @param name The mType to set.
	 */
	public void setType(String name) {
		mType = name;
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
}
