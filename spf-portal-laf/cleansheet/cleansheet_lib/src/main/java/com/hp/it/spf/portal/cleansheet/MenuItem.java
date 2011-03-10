package com.hp.it.spf.portal.cleansheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <p>
 * This model object represents an an item that might appear as part of
 * navigation menu. Properties are provided for specifying attributes like a
 * unique identifier, title string, and navigation URL. There are also flags
 * that can be used to indicate whether or not the menu item is currently
 * visible and whether the item should be highlighted. For hierarchical menu
 * structures there is also a "subMenuItems" property that can be used to
 * specify child menu items.
 * 
 * <p>
 * Note that this is a general-purpose model used for conveying menu information
 * to the presentation tier -- not all of these properties having meaning in all
 * contexts.
 * </p>
 */
public class MenuItem implements Cloneable
{
    
	// ------------------------------------------------------ Protected Members    
    
    
    /**
     * Unique ID of this menu item.
     */
    protected String id;
    
    
    /**
     * Title text of this menu item.
     */
    protected String title;
    
    
    /**
     * URL for the menu item hyperlink.
     */
    protected String url;
    
    
    /**
     * Flag indicating whether or not this menu item should be rendered in the
     * active or highlighted state. The default value is "false".
     */
    protected boolean highlighted;
    
    
    /**
     * Flag indicating whether or not this menu item is visible. The default
     * value is "true".
     */
    protected boolean visible = true;
    
    
    /**
     * List of child menu items. All items in this list MUST be of type
     * MenuItem.
     */
    protected List<MenuItem> subMenu = new ArrayList<MenuItem>();

    
	// ------------------------------------------------------ Public Properties  
    
    
    /**
     * Returns the unique ID of this menu item.
     */    
    public String getId()
    {
        return id;
    }

    
    /**
     * Sets the unique ID of this menu item.
     */    
    public void setId(String id)
    {
        this.id = id;
    }

    
    /**
     * Returns the title text of this menu item.
     */    
    public String getTitle()
    {
        return title;
    }

    
    /**
     * Sets the title text of this menu item.
     */    
    public void setTitle(String title)
    {
        this.title = title;
    }

    
    /**
     * Returns the URL for the menu item hyperlink.
     */    
    public String getUrl()
    {
        return url;
    }

    
    /**
     * Sets the URL for the menu item hyperlink.
     */    
    public void setUrl(String url)
    {
        this.url = url;
    }

    
    /**
     * Returns the flag indicating whether or not this menu item should be
     * highlighted.
     */    
    public boolean isHighlighted()
    {
        return this.highlighted;
    }
    
    
    /**
     * Sets the flag indicating whether or not this menu item should be
     * highlighted.
     */    
    public void setHighlighted(boolean highlighted)
    {
        this.highlighted = highlighted;
    }
    
    
    /**
     * Returns the flag indicating whether or not this menu item is visible. The
     * default value is "true".
     */    
    public boolean isVisible()
    {
        return visible;
    }


    /**
     * Sets the flag indicating whether or not this menu item is visible.
     */    
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
    
    
    /**
     * Returns the list of child menu items.  All members of the returned
     * list MUST be of type MenuItem.
     */
    public List getSubMenuItems()
    {
        return subMenu;
    }
    
    
	// --------------------------------------------------------- Public Methods
    
    
    /**
     * Recursively searches the children of the current menu item looking for
     * the menu item with the given ID. If an item is found with a matching ID,
     * the "visibility" property of that item is set to "true".
     */    
    public void showChild(String childId)
    {
        this.setChildVisibility(childId, true);
    }
    
    
    /**
     * Recursively searches the children of the current menu item looking for
     * the menu item with the given ID. If an item is found with a matching ID,
     * the "visibility" property of that item is set to "false".
     */        
    public void hideChild(String childId)
    {
        this.setChildVisibility(childId, false);
    }
    
    
    /**
     * Sets the "visibility" property of all child menu items to "false".
     */
    public void hideChildren()
    {
        setChildrenVisibility(this.id, false);
    }
    
    
    /**
     * Recursively searches the children of the current menu item looking for
     * the given parent ID. If an item is found with a matching parent ID, the
     * "visibility" property of all children of that item is set to "false".
     */
    public void hideChildren(String parentId)
    {        
        setChildrenVisibility(parentId, false);
    }
    

    /**
     * Sets the "visibility" property of all child menu items to "true".
     */    
    public void showChildren()
    {
        setChildrenVisibility(this.id, true);
    }    


    /**
     * Recursively searches the children of the current menu item looking for
     * the given parent ID. If an item is found with a matching parent ID, the
     * "visibility" property of all children of that item is set to "true".
     */
    public void showChildren(String parentId)
    {        
        setChildrenVisibility(parentId, true);
    }
    
    
    /**
     * Returns a deep copy of this MenuItem instance.
     */
    @SuppressWarnings("unchecked")
	public MenuItem clone() 
    {
        MenuItem clone = null;
        
        try
        {
            clone = (MenuItem) super.clone();

            clone.subMenu = (List<MenuItem>) ((ArrayList<MenuItem>) this.subMenu).clone();

            for(int i = 0; i < subMenu.size(); i++) 
            {
                clone.subMenu.set(i, (this.subMenu.get(i)).clone());
            }            
        }
        catch (CloneNotSupportedException e) { }

        return clone;
    }

    
	// ------------------------------------------------------ Protected Methods
    
       
    /**
     * Recursively searches the children of the current menu item looking for
     * the menu item with the given ID. If an item is found with a matching ID,
     * the "visibility" property of that item is assigned the value of the given
     * "visible" argument.
     */      
    protected void setChildVisibility(String childId, boolean visible)
    {
        Iterator i = this.subMenu.iterator();
            
        while (i.hasNext())
        {
            MenuItem item = (MenuItem) i.next();
            
            if (childId.equals(item.getId()))
            {
                item.setVisible(visible);
            }
            else
            {
                item.setChildVisibility(childId, visible);
            }
        }
    }
    
    
    /**
     * Recursively searches the children of the current menu item looking for
     * the given parent ID. If an item is found with a matching parent ID, the
     * "visibility" property of all children of that item is assigned the value
     * of the given "visible" argument.
     */    
    protected void setChildrenVisibility(String parentId, boolean visible)
    {
        Iterator i = this.subMenu.iterator();
        
        while (i.hasNext())
        {
            MenuItem item = (MenuItem) i.next();
            
            if (parentId.equals(this.id))
            {
                item.setVisible(visible);
            }
            else
            {
                item.setChildrenVisibility(parentId, visible);
            }
        }
    }    
}
