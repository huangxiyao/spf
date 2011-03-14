package com.hp.it.spf.portal.cleansheet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import com.epicentric.common.website.I18nUtils;
import com.epicentric.common.website.Localizer;
import com.epicentric.common.website.MenuItemNode;
import com.epicentric.common.website.MenuItemUtils;
import com.epicentric.common.website.SessionInfo;
import com.epicentric.common.website.SessionUtils;
import com.epicentric.settings.Settings;
import com.epicentric.site.Site;
import com.epicentric.site.SiteSettings;
import com.epicentric.template.Style;
import com.epicentric.user.User;
import com.hp.it.spf.portal.cleansheet.MenuItem;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.vignette.portal.util.StringUtils;
import com.vignette.portal.util.WebUtils;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * This HP Clean sheet utility class contains methods used by the HP Clean sheet layout.
 *
 */

public class Utils {
	
	private static String OPEN_SPAN_REGEX = "(?i:<SPAN.*?>)";
	private static String CLOSE_SPAN_REGEX = "(?i:</SPAN>)";
	
	private static List<List<Locale>> supportedLocales = null;
	//private static List<List<String>> partitionedLocaleCodes = null;
	
	private static String [] americasLocaleCodes = {
		"en-CA", "fr-CA", "es-AR", "es-BO", "es-CL", "es-CO", "es-CR", "es-DO", "es-EC", "es-GT", "es-HN", 
		"es-MX", "es-NI", "es-PA", "es-PE", "es-PR", "es-PY", "es-SV", "es-UY", "es-VE", "pt-BR", "en-US"
		};

	private static String [] europeLocaleCodes = {
		"be-BY", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "de-AT", "de-CH", "de-DE", "de-LU", "el-GR", "en-GB", "en-IE", "es-ES", 
		"et-EE", "fi-FI", "fr-BE", "fr-CH", "fr-FR", "fr-LU", "hr-HR", "hu-HU", "is-IS", "it-CH", "it-IT", "lt-LT", "lv-LV", 
		"mk-MK", "nl-BE", "nl-NL", "no-NO", "pl-PL", "pt-PT", "ro-RO", "ru-RU", "sk-SK", "sl-SI", "sq-AL", "sr-BA", "sr-CS", 
		"sv-SE", "tr-TR", "uk-UA", "ar-AE", "ar-BH", "ar-IQ", "ar-JO", "ar-KW", "ar-LB", "ar-OM", "ar-QA", "ar-SA", "ar-YE", 
		"ar-SY", "iw-IL", "ar-DZ", "ar-EG", "ar-LY", "ar-MA", "ar-SD", "ar-TN", "en-ZA"
		 
	};
	
	private static String [] asiaLocaleCodes = {
		"en-AU", "hi-IN", "ja-JP", "ko-KR", "th-TH", "vi-VN", "zh-CN", "zh-HK", "zh-TW", "en-IN", "en-NZ",
		"en-HK", "en-in", "en-id", "en-my", "en-ph", "en-sg", "en-lk", "en-th", "en-vn"
	};
	
	static {
		if (supportedLocales == null) {
			supportedLocales = new ArrayList< List<Locale>>();
			supportedLocales.add(getLocalesForAZone(americasLocaleCodes));
			supportedLocales.add(getLocalesForAZone(europeLocaleCodes));
			supportedLocales.add(getLocalesForAZone(asiaLocaleCodes));
		}
	}
	
	/**
     * Get {@link java.util.Locale} for current user, whether 
     * she is a anonymous and authenticated user.
     * 
     */
    public static Locale getLocale(HttpServletRequest pReq) {
        if (null == pReq) {
            return getDefaultLocale(pReq);
        }

        Locale rLocale = null;
        try {
        	
            HttpSession session = pReq.getSession();
            User currentUser = SessionUtils.getCurrentUser(session);

            if (currentUser.isGuestUser()) {
                Localizer localizerForGuest = (Localizer)I18nUtils
                        .getLocalizer(session, pReq);
                rLocale = localizerForGuest.getLocale();
            } else {
                rLocale = I18nUtils.getUserLocale(currentUser);
            }            
        } catch (Exception ex) {
            return getDefaultLocale(pReq);
        }
        if (null == rLocale) {
            rLocale = getDefaultLocale(pReq);
        }
        return rLocale;
    }
    
    /**
     * Get current site's default locale.
     */
    public static Locale getDefaultLocale(HttpServletRequest pReq) {
        if (pReq == null) {
            return null;
        }
        
        Locale defaultLocale = null;
        try {
            SessionInfo sessionInfo = (SessionInfo)pReq.getSession()
                    .getAttribute(SessionInfo.SESSION_INFO_NAME);
            if (sessionInfo != null) {
                Site currentSite = sessionInfo.getSite();
                defaultLocale = getDefaultLocale(currentSite);
            }
        } catch (Exception ex) {
        	// Nothing we can do.
            return null;
        }
        
        return defaultLocale;
    }
    
    /**
     * Get the current site's default locale, or Locale.getDefault() if
     * default locale returns null.
     * 
     */
    private static Locale getDefaultLocale(Site pSite) {
        if (pSite == null) {
            return null;
        }        
        Locale defaultLocale = pSite.getDefaultLocale();        
        if (defaultLocale == null) {
            defaultLocale = Locale.getDefault();
        }
        return defaultLocale;
    }
    
	/**
     * Return RFC 3066 language tag from locale.
     * 
     */
    public static String localeToLanguageTag(Locale pLocale) {
        if (pLocale == null) {
            return null;
        }
        String language = pLocale.getLanguage();
        String country = pLocale.getCountry();
        StringBuffer result = new StringBuffer(4);
        if (language.length() > 0) {
            result.append(language);
            if (country.length() > 0) {
                result.append('-').append(country);    
            }
        } else {
            result.append(country);
        }        
        return result.toString();
    }
    
    /*
     * Method to create MenuItem object from MenuItemNode object.  The friendly URI
     * is stored in the MenuItem.Id field.
     */
     
    public static MenuItem createMenuItem(PortalContext portalContext, MenuItemNode node, 
    		boolean regularNavPage, String spID) {

    	MenuItem menuItem = new MenuItem();
    	
    	menuItem.setId(node.getID());
    	
    	// Get rid of any <span> element in title string
    	menuItem.setTitle(filterSpan(node.getTitle()));
    	menuItem.setVisible(node.isVisible());

    	// If this is a regular nav item, we can set the values 
    	// directly; otherwise, we'll need to calculate the secondary 
    	// page URL and look at the menuID querstring parameter to 
    	// determine whether or not this was the link that was clicked.
    	if (node.getHref().indexOf("template.") == -1)
    	{
    		menuItem.setUrl(node.getHref());
    		menuItem.setHighlighted(node.isSelected() && regularNavPage);
    	}
    	else
    	{
    		String templateID = node.getHref().substring(node.getHref().lastIndexOf(".") + 1);
    		menuItem.setUrl(portalContext.createDisplayURI(templateID).toString());
    		menuItem.setHighlighted(templateID.equals(spID));
    	}
    	return menuItem;
    }

    /*
     * Method to add MenuItem object to menu item list.
     * 
     * This method supports adding a menu item to a menu item list that is
     * up to 3-levels, i.e. if a list is 4-level, additional code needs to be 
     * added.
     */
    private static void addMenuItemToList(List menuItemList, MenuItem menuItem, 
    					MenuItemNode node, int offsetLevel, int[] nodeIndexes) {

    	int currentLevel = node.getLevel();
    	int index;

    	if (currentLevel == offsetLevel) {
    		// level 0 menu item, so just add menu item to menu list
    		menuItemList.add(menuItem);

    		// save level 0 index
    		nodeIndexes[0] = menuItemList.size() - 1;
    	}
    	else if (currentLevel == offsetLevel + 1) {
    		// level 1 menu item
    		// get index of current menu item's parent menu item in menu list, 
    		// and then add current menu item as child to parent menu item.

    		index = nodeIndexes[0];
    		((MenuItem)menuItemList.get(index)).getSubMenuItems().add(menuItem);

    		// save level 1 menu item index
    		nodeIndexes[1] = 
    			((MenuItem)menuItemList.get(index)).getSubMenuItems().size() - 1;
    	}
    	else {
    		// level 2 menu item
    		// get current menu item's level 0 menu item in menu list, 
    		// and level 1 menu item's list of level 2 menu items.
    		// then add current menu item as child to level 1 menu item.

    		index = nodeIndexes[0];
    		MenuItem level0 = (MenuItem)menuItemList.get(index);

    		index = nodeIndexes[1];
    		MenuItem level1 = (MenuItem) level0.getSubMenuItems().get(index);
    		level1.getSubMenuItems().add(menuItem);
    	}
    }
    
    /**
     * Retrieve menu items from the navigation tree as configured in 
     * Vignette using the navigation split level and maximum number of levels.
     * 
     * @param portalContext PortalContext object
     * @param navSplit Valid values are 0 to 3, and a value of -1 to use the 
     * configured navigation level in Vignette.  The default value is 0.
     * @param maxLevel Valid values are 2 or 3.  The default value is 3.
     * @return Returns a list of {@link com.hp.it.spf.portal.cleansheet.MenuItem}
     * objects.
     * @throws Exception
     */
	public static List getConfiguredMenuItems(PortalContext portalContext,
			int navSplit, int maxLevel)  throws Exception {
		
		// This code works only with max levels of 2 or 3.
		if (maxLevel < 2 || maxLevel > 3) maxLevel = 3;
		
		List menuItemList = new ArrayList();
		List allNodeList = MenuItemUtils.getAllNodes(portalContext);

		// Retrieve the ID of the current secondary page and determine whether or
		// not it is a "regular" nav page (meaning it is of type PAGE or 
		// JSP_INCLUDE_PAGE.
		String spID = portalContext.getCurrentSecondaryPage().getTemplateFriendlyID();
		boolean regularNavPage = spID.equals("PAGE") || spID.equals("JSP_INCLUDE_PAGE");

		// Retrieve value of navSplit if parameter is -1.  
		// The navSplit value tells us whether we should display all of the 
		// nav items, or just the children of a particular parent.
	
		if (navSplit == -1) {
			Settings siteSettings = portalContext.getCurrentSite().getSettings();
			navSplit = StringUtils.parseInt(
				siteSettings.getSetting(SiteSettings.MENU_NAVIGATION_SPLIT), 0);
		}
		
		// Default to no navigation split if it is an invalid value.
		if (navSplit < 0 || navSplit > 3) navSplit = 0;

		// If navSplit is 0, then we display all items; otherwise we have to find
		// the list of children (branch) to display

		// Retrieve an iterator for all menu items
		Iterator allNodes = allNodeList.iterator();
		MenuItemNode node;
		MenuItem menuItem;
	
		int currentLevel;
		int nodeIndexes[] = new int[maxLevel-1];

		if (navSplit == 0)
		{
			// Copy each MenuItemNode object node to MenuItem object in
			// vertical node list.

			while (allNodes.hasNext())
			{
				node = (MenuItemNode) allNodes.next();
				currentLevel = node.getLevel();

				// ignore menu items that are maxLevel or higher
				if (currentLevel >= maxLevel)
					continue;

				menuItem = createMenuItem(portalContext, node, regularNavPage, spID);
				addMenuItemToList(menuItemList, menuItem, node, 0, nodeIndexes);

			}
		}
		else
		{
			// We want to show 'maxLevel' levels of navigation in the branch 
			// of the current selected menu item.
		
			// The algorithm is to find the branch where the selected menu item
			// is (could be at any level), and put those menu items belonging 
			// to that branch into the menu item list.

			boolean selectedBranchFound = false;

			while (allNodes.hasNext())
			{
				node = (MenuItemNode) allNodes.next();

				// Break loop when new branch with the same level or higher
				// is found, after we already have the selected branch.
			
				if (selectedBranchFound && node.getLevel() < navSplit) 
					break;
					
				if (node.getLevel() <= navSplit - 1 && selectedBranchFound == false)
				{
					// Check for selected branch.
					// If we find a pre-split nav item that is "selected" then 
					// we know that we have the correct branch
					if (node.getHref().indexOf("template.") == -1)
					{
						selectedBranchFound = node.isSelected() && regularNavPage;
					}
					else
					{
						selectedBranchFound = node.getHref().endsWith("." + spID);
					}
					if (selectedBranchFound) {
						navSplit = node.getLevel() + 1;
					
						// Clear menu list of menu items that are not on the 
						// selected branch.
						menuItemList.clear();
					}
				}
			
				// Put 'navSplit' to 'navSplit+maxLevel' level menu items on 
				// menu item list.
			
				else if (node.getLevel() >= navSplit && node.getLevel() < navSplit+maxLevel)
				{
					// Create new menu item
					menuItem = createMenuItem(portalContext, node, regularNavPage, spID);
					if (menuItem.isHighlighted()) selectedBranchFound = true;

					// Add new menu item to list
					addMenuItemToList(menuItemList, menuItem, node, navSplit, 
						nodeIndexes);
				}
			}
		}
		return menuItemList;
	}

	/**
	 * Return message string with &lt;span&gt; and &lt;/span&gt; elements,
	 * if any, removed.
	 */
	public static String filterSpan(String msg) {
		if (msg == null)
			return msg;
		
		msg = msg.replaceAll(OPEN_SPAN_REGEX, "");
		return msg.replaceAll(CLOSE_SPAN_REGEX, "");
	}
	
	/**
     * Get localized value from component's resource bundle.  There is
     * no default value parameter, so an empty string is used as the
     * default value.
     * 
     * @param i18nID component's unique ID
     * @param pKey key of the value
     * @param pContext current portal context, from which the resource bundle 
     * message can be obtained
     * @return String localized message
     */
     public static String getI18nValue(String i18nID, String pKey, 
    		 PortalContext pContext) {
    	 return getI18nValue(i18nID, pKey, "", pContext);
     }
	
	/**
     * Get localized value from component's resource bundle.
     * 
     * @param i18nID component's unique ID
     * @param pKey key of the value
     * @param pDefaultValue  default value if key is not existent
     * @param pContext current portal context, from which the resource bundle 
     * message can be obtained
     * @return String localized message
     */
    public static String getI18nValue(String i18nID, String pKey, 
    		 String pDefaultValue, PortalContext pContext) {
    	if (pKey == null || pContext == null) {
    		return null;
    	}
       
    	HttpServletRequest request = pContext.getPortalRequest().getRequest();      
    	String value = filterSpan(I18nUtils.getValue(i18nID, pKey, pDefaultValue, request));
       
    	// I18nUtils.getValue() converts special HTML/XML characters to 
    	// encoded characters, e.g. "&raquo;" to "&amp;raquo;", which is not 
    	// what we found, so use WebUtils.xmlEntitiesToChars to convert 
    	// encoded characters back to special HTML/XML characters.
       
    	return WebUtils.xmlEntitiesToChars(value);          
    }

    public static void initialize(PortalContext portalContext, PageContext pageContext) {
    	 Style currentStyle = portalContext.getCurrentStyle();
    	 String i18nID = currentStyle.getUID();
    	 	
    	 pageContext.setAttribute("stylePath", portalContext.getPortalHttpRoot() + 
    	 		currentStyle.getUrlSafeRelativePath());
    	 	
    	 pageContext.setAttribute("dnsName", portalContext.getCurrentSite().getDNSName());
    	 
    	 HttpServletRequest request = portalContext.getHttpServletRequest();

    	 Locale locale = Utils.getLocale(request);
    	 String languageTag = Utils.localeToLanguageTag(locale);
    	 String countryTag = locale.getCountry();
    	 if (languageTag == null) {
    	     languageTag = "en-US";
    	     countryTag = "US";
    	 }
    	 pageContext.setAttribute("countryTag", countryTag);
    	 pageContext.setAttribute("languageTag", languageTag);  
    	 pageContext.setAttribute("locale", locale);  

    	 HPCSModel hpcsModel = (HPCSModel) request.getAttribute("HPCSModel");
    	 if (hpcsModel == null) {
    		 hpcsModel = new HPCSModel(); 
    		 System.out.println("grid: created new HPCSModel()");
    	 }
    	 else {
    		 System.out.println("grid; used existing HPCSModel()");
    	 }
    	 
    	 hpcsModel.setUsername("John"); // test code, will be removed
		 request.setAttribute("HPCSModel", hpcsModel);
    }
     
     public static void initHorzNav(PortalContext portalContext, PageContext pageContext) throws Exception {
    	 Style currentStyle = portalContext.getCurrentStyle();
    	 String i18nID = currentStyle.getUID();
    	 
    	 pageContext.setAttribute("stylePath", portalContext.getPortalHttpRoot() + 
    	 		currentStyle.getUrlSafeRelativePath());

    	 String whiteCaretImg = 
    	 	"http://welcome.hp-ww.com/img/hpweb_1-2_tbl_sort_wht.gif";
    	 String blackCaretImg = 
    	 	"http://welcome.hp-ww.com/img/hpweb_1-2_tbl_sort_blk.gif";
    	 String spacerImg =
    	 	"http://welcome.hp-ww.com/img/s.gif";

    	 if (pageContext.getRequest().getScheme().equals("https")) {
    	 	whiteCaretImg = 
    	 		"https://secure.hp-ww.com/img/hpweb_1-2_tbl_sort_wht.gif";
    	 	blackCaretImg = 
    	 		"https://secure.hp-ww.com/img/hpweb_1-2_tbl_sort_blk.gif";
    	 	spacerImg =
    	 		"https://secure.hp-ww.com/img/s.gif";
    	 }

    	 pageContext.setAttribute("whiteCaretImg", whiteCaretImg);
    	 pageContext.setAttribute("blackCaretImg", blackCaretImg);
    	 pageContext.setAttribute("spacerImg", spacerImg);

    	 pageContext.setAttribute("helpTextDef", Utils.getI18nValue(i18nID, "hpweb.helpText",
    	 		portalContext));
    	 pageContext.setAttribute("helpUrlDef",  Utils.getI18nValue(i18nID, "hpweb.helpUrl",
    	 		portalContext));

    	 HttpServletRequest request = portalContext.getHttpServletRequest();
    	 HPCSModel hpcsModel = (HPCSModel) request.getAttribute("HPCSModel");
    	 if (hpcsModel == null) {
    		 hpcsModel = new HPCSModel(); 
    		 request.setAttribute("HPCSModel", hpcsModel);
    		 System.out.println("hnav: created new HPCSModel()");
    	 }
    	 else {
    		 System.out.println("hnav; used existing HPCSModel()");
    	 }

    	 List menuItemList = hpcsModel.getTopMenuItems();

    	 // if the HPCSModel bean has top menu items, use it for hpweb layout
    	 // top nav, i.e. don't need to generate it from vap configured nav menu.

    	 if (menuItemList == null || menuItemList.size() == 0) {
    	 	
    	 	int navSplit = 0;
    	 	int maxLevel = 3;
    	 	menuItemList = Utils.getConfiguredMenuItems(portalContext, navSplit, maxLevel);
    	 }

    	 // We should expect that only one MenuItem node has its highLighted flag 
    	 // set to true.   Find that selected node and save the tab index it
    	 // belongs under.   Also if it is a 3rd level node, set the highlight flag
    	 // of its parent node (a button item) to true.

    	 int selectedIndex = 0;
    	 boolean done = false;
    	 for (int tabIndex=0; tabIndex<menuItemList.size(); tabIndex++) {
    	 	MenuItem tab = (MenuItem) menuItemList.get(tabIndex);
    	 	if (tab.isVisible() == false) {
    	 		continue;
    	 	}
    	 	if (tab.isHighlighted()) {
    	 		// found selected node in tab item, don't need to do anymore
    	 		selectedIndex = tabIndex;
    	 		break;
    	 	}
    	 	int buttonCount = tab.getSubMenuItems().size();
    	 	if (buttonCount > 0) {
    	 		for (int buttonIndex=0; buttonIndex<buttonCount; buttonIndex++) {
    	 			MenuItem button = 
    	 				(MenuItem) tab.getSubMenuItems().get(buttonIndex);
    	 			if (button.isVisible() == false) {
    	 				continue;
    	 			}
    	 			if (button.isHighlighted()) {
    	 				// found selected node in button item, save tab index
    	 				selectedIndex = tabIndex;
    	 				done = true;
    	 				break;
    	 			}
    	 			int flyoutItemCount = button.getSubMenuItems().size();
    	 			if (flyoutItemCount > 0) {
    	 				for (int flyoutIndex=0; flyoutIndex<flyoutItemCount; flyoutIndex++) {
    	 					MenuItem flyoutItem = 
    	 						(MenuItem) button.getSubMenuItems().get(flyoutIndex);
    	 					if (button.isVisible() == false) {
    	 						continue;
    	 					}
    	 					if (flyoutItem.isHighlighted()) {
    	 						// found selected node in flyout item, save tab
    	 						// index and button (parent) item's  highlight flag 
    	 						// to true.
    	 						selectedIndex = tabIndex;
    	 						button.setHighlighted(true);
    	 						done = true;
    	 						break;
    	 					}
    	 				}
    	 			}
    	 		}
    	 	}
    	 	if (done)
    	 		break;
    	 }

    	 pageContext.setAttribute("menuItemList", menuItemList);
    	 pageContext.setAttribute("selectedIndex", selectedIndex); 
    	 
     }
     
     /**
      * lazy initialization of locale selector
      * @param portalContext PortalContext
      * @return
      */               
    public static void initializeLocaleSelector(PortalContext portalContext, PageContext pageContext) {
    	 
    	if (portalContext == null || pageContext == null ) return;
    	
    	Style currentStyle = portalContext.getCurrentStyle();
	 
    	pageContext.setAttribute("stylePath", portalContext.getPortalHttpRoot() + currentStyle.getUrlSafeRelativePath());
    	
    	HttpServletRequest request = portalContext.getHttpServletRequest();

    	Locale currentLocale = I18nUtility.getLocale(request);
    	if (currentLocale == null) currentLocale = Locale.US;
    	pageContext.setAttribute("selectedCountry", currentLocale.getDisplayCountry(currentLocale));

    	HttpSession session = request.getSession();
    	if (session.getAttribute("americas") == null) {
    	
        	String siteName = portalContext.getCurrentSite().getDNSName();
        	System.out.println("dns name "+ siteName);
    		final String urlFormat = "/portal/site/" + siteName + "/template.PUBLIC_SPF_SELECT_LOCALE/action.process/?spfSelectedLocale=%s-%s";
	    	List<HyperLink> countryHyperlinks = null;
	    	List<Locale> localesInZone = null;
	    	 
	    	// locales supported by a site
	    	Collection availableLocales = I18nUtility.getAvailableLocales(request);
	    	
	    	// debugging
	    	Iterator it = availableLocales.iterator();
	    	
	    	while (it.hasNext()) {
	    		System.out.println("Available Locale: " + ((Locale)it.next()).toString());
	    	}
	    		
	    	// need to refactor
	    	if (availableLocales.size() > 1) {
	    		countryHyperlinks =  new ArrayList<HyperLink>();
	    		localesInZone = getLocalesForZone(0, availableLocales);
	    		for (Locale locale: localesInZone) {
	    			countryHyperlinks.add(
	    				new HyperLink(locale.getDisplayCountry(locale), 
	    					locale.getDisplayCountry(), 
	    					(String.format(urlFormat, locale.getLanguage(), locale.getCountry()))
	    			));
	    		}
	    		session.setAttribute("americas", countryHyperlinks);
	    		System.out.println("set in session for americas: " + countryHyperlinks.size()); 

	    		localesInZone = getLocalesForZone(1, availableLocales);
	    		countryHyperlinks =  new ArrayList<HyperLink>();
	    		for (Locale locale: localesInZone) {
	    			countryHyperlinks.add(
	    				new HyperLink(locale.getDisplayCountry(locale), 
	    					locale.getDisplayCountry(), 
	    					(String.format(urlFormat, locale.getLanguage(), locale.getCountry()))
	    			));
	    		}
	    		session.setAttribute("europe", countryHyperlinks);
	    		System.out.println("set in session for europe and: " + countryHyperlinks.size()); 

	    		localesInZone = getLocalesForZone(2, availableLocales);
	    		countryHyperlinks =  new ArrayList<HyperLink>();
	    		for (Locale locale: localesInZone) {
	    			countryHyperlinks.add(
	    				new HyperLink(locale.getDisplayCountry(locale), 
	    					locale.getDisplayCountry(), 
	    					(String.format(urlFormat, locale.getLanguage(), locale.getCountry()))
	    			));
	    		}
	    		session.setAttribute("asia", countryHyperlinks);
	    		System.out.println("set in session for asia AND: " + countryHyperlinks.size()); 
	    	}
    	}
    }
     
    /**
     * filter out locales/countries in a zone 
     * @param zone
     * @param availableLocales
     * @return
     */
    private static List<Locale> getLocalesForZone(int zone, Collection availableLocales) {
    	List<Locale> list = new ArrayList<Locale>();
    	 
    	List<Locale> supportedLocalesInZone = supportedLocales.get(zone);
    	Iterator it = availableLocales.iterator();
    	Locale availLocale;
    	while (it.hasNext()) {
    		availLocale = (Locale)it.next();
    		for (Locale supportedLocale: supportedLocalesInZone) {
    			if (availLocale.getCountry().equalsIgnoreCase(supportedLocale.getCountry()) &&
    					availLocale.getLanguage().equalsIgnoreCase(supportedLocale.getLanguage())) {
    				list.add(availLocale);
    				break;
    			}
    		}
    	}
    	System.out.println(String.format("available locales in zone %d: %d", zone+1, list.size()));
    	return list;
    }
     
    private static List<Locale> getLocalesForAZone(String [] localeCodes) {
    	List<Locale> locales = new ArrayList<Locale>();
    	 
    	String [] langCnty;
		for (String code : localeCodes) {
			langCnty = code.split("-"); 
			if (langCnty.length == 1) {
				locales.add(new Locale(langCnty[0]));;
			}
			else if(langCnty.length == 2) {
				locales.add(new Locale(langCnty[0], langCnty[1]));;
			}
		}
		System.out.println("supported locales in a zone " + locales.size());
		return locales;
		
     }
}
