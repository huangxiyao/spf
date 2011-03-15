package com.hp.it.spf.portal.cleansheet;

import java.util.List;

public class MenuItem {

	/**
	 * Unique identifier for the menu item.  This value will be set for default values to represent the Tridian identifier for the Menu Item.
	 */
	private final String id = null;

	
	/**
	 * Text displayed on UI for the link.
	 */
	
	private String linkText;

	
	/**
	 * URL for the link.  If this value is set to null, then the text will become a label.
	 */
	
	private String url;

	
	/**
	 * Alternate text to be displayed for image or link.
	 */
	
	private String alt;

	
	/**
	 * This can be used in situations where a link requires a description.  In places where this is not used it will be ignored.
	 */
	
	private String description;

	
	/**
	 * Image reference for link.  In some cases this image will become a link, in other cases the image will be displayed next to the link.  Check the usage in the model for more details on each specific use.
	 */
	
	private String imageURL;

	
	/**
	 * Currently selected menu item.  This is used in the case of a menu to represent the page the user is currently on.
	 */
	
	private boolean highlighted;

	
	/**
	 * Determine whether a Menu Item should be shown in the UI.  The default value is true.  Can be changed to false to quickly remove a menu item from view.
	 */
	
	private boolean visible;

	
	/**
	 * In cases where a Menu Tree is allowed this List can be used to add additional menu items.
	 */
	
	private List<MenuItem> subMenu;
}
