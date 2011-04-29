package com.hp.frameworks.wpa.portal.athp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This model object represents an an item that might appear as part of
 * navigation menu: label, link, spacer, separator. 
 * Properties are provided for specifying attributes like a
 * unique identifier, title string, and navigation URL. There are boolean
 * properties to indicate whether or not the menu item is currently
 * visible and whether the item should marked as active. For hierarchical menu
 * structures there is also a "subMenuItems" property that can be used to
 * specify child menu items of <code>MenuItem</code> objects.
 * 
 * <p>
 * Note that this is a general-purpose model used for conveying menu information
 * to the presentation tier -- not all of these properties having meaning in all
 * contexts.
 * </p>
 */

public class MenuItem {
	
	/**
	 * Default constructor
	 */
	public MenuItem() {
	}
	
	/**
	 * Menu item link convenience constructor
	 */
	public MenuItem(String id, String title, String href, String target) {
		this.id = id;
		this.title = title;
		this.href = href;
		this.target = target;
	}

	/**
	 * Menu item label convenience constructor
	 */
	public MenuItem(String id, String title) { 
		this.id = id;
		this.title = title;
	}

	private String id;
	/**
     * Get the unique ID of this menu item.
     */
	public String getId() { return this.id ; }
	/**
     * Set the unique ID of this menu item.
     */
	public void setId(String id) { this.id = id; }

	private String parentId;
	/**
     * Get the parent ID of this menu item.
     */
	public String getParentId() { return this.parentId ; }
	/**
     * Set the parent ID of this menu item.
     */
	public void setParentId(String parentId) { this.parentId = parentId; }

	private String title;
	/**
     * Get the title text.
     */
	public String getTitle() { return this.title; }
	/**
     * Set the title text.
     */
	public void setTitle(String title) { this.title = title; }

	private String target;
	/**
	 * Get the window name to use when opening url.
	 */
	public String getTarget() { return this.target; }
	/**
	 * Set the window name to use when opening url.
	 */
	public void setTarget(String target) { this.target = target; }

	private boolean selectedFlag = false;
	/**
     * Get the boolean value indicating whether or not this menu item should be 
     * rendered in the active/highlighted state.
     */
	public boolean isSelected() { return this.selectedFlag; }
	/**
     * Set the boolean value indicating whether or not this menu item should be 
     * rendered in the active/highlighted state. The default value is 
     * <code>false</code>.
     */
	public void setSelected(boolean selectedFlag) { this.selectedFlag = selectedFlag; }

	private String href;
	/**
     * Get the URL for the menu item hyperlink.
     */
	public String getHref() { return this.href; }
	/**
     * Set the URL for the menu item hyperlink.
     */
	public void setHref(String href) { this.href = href; }

	private List subMenuItems = new ArrayList();
	/**
	 * Get the list of submenu items of {@link MenuItem} objects.
	 */
	public List getSubMenuItems() {
		return this.subMenuItems;
	}	
	/**
	 * Set the list of submenu items of {@link MenuItem} objects.
	 */
	public void setSubMenuItems(List subMenuItems) {
		this.subMenuItems = subMenuItems;
	}	
	
	private boolean spacer = false;
	/**
	 * Get the boolean value to indicate whether it is a spacer menu item or not.
	 */
	public boolean isSpacer() {
		return spacer;	
	}
	/**
	 * Set the boolean value to indicate whether it is a spacer menu item or not.
	 * The default is <code>false</code>.
	 */
	public void setSpacer(boolean spacer) {
		this.spacer = spacer;
	}

	private boolean separator = false;
	/**
	 * Get the boolean value to indicate whether it is a separator menu item or not.
	 */
	public boolean isSeparator() {
		return separator;	
	}
	/**
	 * Set the boolean value to indicate whether it is a separator menu item or not.
	 * The default is <code>false</code>.
	 */
	public void setSeparator(boolean separator) {
		this.separator = separator;
	}

	private boolean open = true;
	/**
	 * Get the boolean value to indicate whether this submenu item should be 
	 * open (collapse) or not.
	 */
	public boolean isOpen() {
		return open;
	}
	/**
	 * Set the boolean value to indicate whether this submenu item should be 
	 * open (collapse) or not. The default is <code>true</code>.
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	private boolean leaf = false;
	/**
	 * Get the boolean value to indicate whether this menu item is a leaf menu item 
	 * or not. This is a property that is used internally by the &#64;hp layout.
	 */
	public boolean isLeaf() {
	    return leaf;
	}
	/**
	 * Set the boolean value to indicate whether this menu item is a leaf menu item 
	 * or not. The default is <code>false</code>.  This is a property that 
	 * is used internally by the &#64;hp layout.
	 */
	public void setLeaf(boolean leaf) {
	    this.leaf = leaf;
	}

	private boolean empty = false;
	/**
	 * Get the boolean value to indicate whether this menu item is empty or not.
	 * This is a property that is used internally by the &#64;hp layout.
	 */
	public boolean isEmpty() {
	    return empty;
	}
	/**
	 * Set the boolean value to indicate whether this menu item is empty or not.
	 * The default is <code>false</code>.  This is a property that is
	 * used internally by the &#64;hp layout.
	 */
	public void setEmpty(boolean empty) {
	    this.empty = empty;
	}
	
	private boolean visible = true;
	/**
	 * Get the boolean value to indicate whether this menu item should be visible or 
	 * not.
	 */
	public boolean isVisible() {
		return visible;
	}
	/**
	 * Set the boolean value to indicate whether this menu item should be visible or 
	 * not. The default is <code>true</code>.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
