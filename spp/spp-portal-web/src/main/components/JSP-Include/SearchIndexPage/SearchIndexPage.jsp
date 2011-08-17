<!-- SearchIndexPage.jsp -->
<%/*--
	@(#)SearchIndexPage.jsp

	Shared Portal Platform.

	Revision History:
	------------------------------------------------------------------------------
	Ver        Modified By               Date                Notes
	------------------------------------------------------------------------------
	v1        ShivaShanker B            11-Sep-2006          Created
	v2        Akash Srivastava          23-Mar-2007	         Modified to include multiple nodes indexing

	Note :This is a jsp include page which will serve as the interface for the
	search spider.

--*/%>

<%@ page import="java.util.Iterator,
	java.util.List,
	java.util.ArrayList,
	java.util.Collection,
	java.net.URLEncoder,
	com.hp.spp.search.common.NavigationItem,
	com.epicentric.common.website.I18nUtils,
	com.epicentric.common.website.MenuItemNode,
	com.epicentric.common.website.MenuItemUtils,
	com.epicentric.common.website.EndUserUtils,
	com.epicentric.site.Site,
	com.epicentric.navigation.MenuItem,
	com.epicentric.template.Style,
	com.epicentric.page.Page,
	java.util.Locale,
	com.epicentric.i18n.EditableLocalizedBundle,
	com.hp.spp.config.Config,
	com.epicentric.navigation.display.MenuItemTreeManager,
	com.epicentric.navigation.display.MenuItemTreeRoot,
	com.epicentric.navigation.display.MenuItemTreeNode,
	com.epicentric.site.SiteManager,
	com.epicentric.site.SiteException,
	com.epicentric.user.User,
	com.vignette.portal.website.enduser.PortalContext,
	com.epicentric.metastore.MetaStoreFolder,
	com.epicentric.metastore.MetaStoreDocument,
	com.epicentric.common.website.EntityUtils,
	com.epicentric.i18n.locale.LocaleManager"

        contentType="text/html; charset=UTF-8"
%>


<%!

        private static final String TITLE_NOT_DEFINED = "title-not-defined";
        private static final String EMPTY_STRING = "";
		private static final String PAGE_NO = "pageNo";

        /*
           This method returns the total number of rows to be displayed per page.
           The page size value is retrived from the spp database.
        */ 
		int getPageSize(){
       		int pageSize = 500;
       	        try{
       			pageSize = Integer.parseInt(Config.getValue("SPP.search.PageSize"));       			
       		}catch(Exception ex){
       		        //Default the page size to 500
       			pageSize = 500;
       		}	
       		return pageSize;
       	}

	/*

	//This method will get all the MenuItemTreeNode list of a particular site
	private List getMenuItemTreeNodesList(Site aSite){
	        List menuItemList = new ArrayList();
		//Get the menu item tree manager instance and tree root node
		// i.e start node of the navigation tree.
		MenuItemTreeManager menuItemTreeManager = MenuItemTreeManager.getInstance();
		MenuItemTreeRoot rootMenuItem = menuItemTreeManager.getMenuItemTree(aSite);

		//Get all the menu item tree nodes  of the tree
		List menuItemTreeNodeList = getAllNodes(rootMenuItem);
		Iterator menuItemTreeNodeListItr = menuItemTreeNodeList.iterator();
		while(menuItemTreeNodeListItr.hasNext()){
			MenuItemTreeNode aMenuItemTreeNode = (MenuItemTreeNode) menuItemTreeNodeListItr.next();
			MenuItem aMenuItem = aMenuItemTreeNode.getMenuItem();
			menuItemList.add(aMenuItem);

		}
        	return menuItemList;
        }
        */

        /*
           This method returns all the child nodes(MenuItemTreeNode)
           of the passed node (startNode)
        */
	private  List getAllNodes(MenuItemTreeNode startNode)
	{
		//This method returns all the nodes of the site, i.e in the first level
		List nodeListToReturn = new ArrayList();
			//getAllNodesHelper(startNode, nodeListToReturn);
			
			//Get the first level nodes for the site.
			getFirstLevelChildNodes(startNode, nodeListToReturn);

		List childrenOfFirstNode = new ArrayList();
		if (nodeListToReturn != null && nodeListToReturn.size() >0) {
		        // The 0th element will be rootNode of the site
		        // The 1st element is the first Node in the tree node.
		        // This value is important. It is the 1st element which makes us get ALL
		        // the navigation items that should be indexed. The value 1 should be taken from
		        // the config data table. This change should be made nxt release.
		        MenuItemTreeNode aMenuItemTreeNode = (MenuItemTreeNode)nodeListToReturn.get(1);
		      	
		      	//FIX - Fix done to get ALL the nodes under the root node. Earlier it was getting only the 
		      	//1st level nodes. Now we are getting ALL the nodes, regardless of level, from under the
		      	//root node.
		      	//getFirstLevelChildNodes(aMenuItemTreeNode,childrenOfFirstNode);
		      	
		      	//Get all the nodes regardless of level
		      	getAllNodesHelper(aMenuItemTreeNode,childrenOfFirstNode);

		}
		return childrenOfFirstNode;
	}


	/*
	   This method is helper method for the getAllNodes.
	   It gets all children nodes of the passed node.
	*/

	private  void getFirstLevelChildNodes(MenuItemTreeNode startNode, List nodesToReturn)
	{

		if(nodesToReturn != null && startNode != null)
		{
		    nodesToReturn.add(startNode);
		    MenuItemTreeNode children[] = startNode.getChildNodes();
		    if(children != null)
		    {
			for(int i = 0; i < children.length; i++)
			{
			    nodesToReturn.add(children[i]);

			}

		    }
		}
	}


	/*
	   This method is helper method for the getAllNodes.
	   It gets all children nodes of the passed node.
        */

	private  void getAllNodesHelper(MenuItemTreeNode startNode, List nodesToReturn)
	{
		if(nodesToReturn != null && startNode != null)
		{
		    nodesToReturn.add(startNode);
		    MenuItemTreeNode children[] = startNode.getChildNodes();
		    if(children != null)
		    {
			for(int i = 0; i < children.length; i++)
			{
			    getAllNodesHelper(children[i], nodesToReturn);
			}

		    }
		}
	}

        /*
              Retrives the Site object from SiteManager based on site domain name passed to it.
              The DNS Name (dnsName) is the friendly url of the site.
        */
	private Site getVignetteSite(String dnsName) {
	        Site aSite = null;
		if (dnsName == null || dnsName.length() == 0) {
			// Need to log
			throw new IllegalArgumentException(
					"The dns name passed in the site object is null or blank");
		}

		SiteManager aSiteManager = SiteManager.getInstance();
		try {
			aSite = aSiteManager.getSiteFromDNSName(dnsName);
		} catch (SiteException e) {
			// Log
			throw new IllegalArgumentException(
					"The site object passed in cannot be resolved");
		}

		return aSite;
	}

	/*
	   This method checks for wheather MenuItem is associated with page.
	*/
	private boolean isPage(MenuItem aMenuItem)
	{
		boolean isPageFlag = false;
		String linkType = aMenuItem.getLinkType();
		if(aMenuItem.LINKTYPE_PAGE.equals(linkType)){
			isPageFlag = true;
		}
		return isPageFlag;
	}

	/*
	   This method checks for wheather MenuItem is associated with jsp include page
	*/
	private boolean isJSPPage(MenuItem aMenuItem)
	{

		boolean isJSPPageFlag = false;
		String linkType = aMenuItem.getLinkType();
		if(aMenuItem.LINKTYPE_INCLUDE.equals(linkType)){
			isJSPPageFlag = true;
		}
		return isJSPPageFlag;
	}

	/*
	   This method populates the NavigationItem Object
	*/
	private NavigationItem getNavigationItem(MenuItem firstNode,Locale locale){
		//OVSD: 604,876,212: This fix is to supress hongkong chinese links.
		if("hk".equalsIgnoreCase(locale.getCountry())){
			return null;
		}
		NavigationItem aNavigationItem = aNavigationItem = new NavigationItem();
		//Set the Navigation Item ID
		aNavigationItem.setId(firstNode.getUID());
		//Retrieve the title from MenuItemNode and
		//set this title to the navigation item.
		aNavigationItem.setTitle(firstNode.getTitle());
		//Set the language code & Country code
		aNavigationItem.setLocale(locale);
		return aNavigationItem;
	}


	private String replaceNullValue(String value,String stringToBeReplaced){
	   String retValue = "";
	   if(value ==null || EMPTY_STRING.equals(value.trim())){
	      	retValue=stringToBeReplaced;
	   }else{
	   	retValue = value.trim();
	   }
	   return retValue;
	}

%>

<%



	//Get the site using site dns name, site dns name is nothing but friendly url of the site.
	//String dnsName = (String) request.getParameter("Site_Name");
	//Site aSite = getVignetteSite(dnsName);
	Site aSite= EndUserUtils.getSite(pageContext);
	//Get the menu item tree manager instance and tree root node
	// i.e start node of the navigation tree.
	MenuItemTreeManager menuItemTreeManager = MenuItemTreeManager.getInstance();
	MenuItemTreeRoot rootMenuItem = menuItemTreeManager.getMenuItemTree(aSite);

	//Get all the menu item tree nodes  of the tree
	List menuItemNodesList = getAllNodes(rootMenuItem);


	// Get the current locale for the site.
	Locale	aCurrentLocale= I18nUtils.getCurrentLocale(aSite);
	//Collection supportedLocalesList =  I18nUtils.getLocalesToDisplay(session);
	Collection supportedLocalesList =  I18nUtils.getRegisteredLocales();

	/*
	Apply below rules for each navigation item.

	1>Only the navigation items which are  available  within the first  node in the
	  navigation tree of the site will be  considered  for indexing.  This is based
	  on the  assumption that  the first  node will  be the  root  of navigation for
	  the logged in user.

	2>Only the navigation items which are linked to a  "page"   or a  "jsppage"  will
	  be considered for indexing. This covers the eService links which are navigation
	  items linked to a "jsppage".

	3>A navigation item which has defined a property "IndexForSearch" with a value of
	 "false" will not be considered for indexing. Note that a navigation  item  which
	  does not  define  this  property OR  defines  it with a value of "true" will be
	  considered  for indexing.

	4>.Check for the title for navigation item is defined or not, if the title for the
	   Navigation item is not defined, in such case navigation item will not be indexed
	*/

	EditableLocalizedBundle aEditableLocalizedBundle = null;
	String indexForSearch = null;

	String currentLocaleTitle = null;
	if(menuItemNodesList != null) {
		List navigationItemList = new ArrayList();
		Iterator menuItemNodesListItr = menuItemNodesList.iterator();
		NavigationItem aNavigationItem = null;
		while(menuItemNodesListItr.hasNext()){
			boolean siteLocaleIndexed = false;
			MenuItemTreeNode aMenuItemTreeNode = (MenuItemTreeNode)menuItemNodesListItr.next();
			MenuItem firstNode = aMenuItemTreeNode.getMenuItem();
			
			/*
			if(aMenuItemTreeNode.getLevel() == 1 &&
				(isPage(firstNode)||isJSPPage(firstNode))){*/

			if(isPage(firstNode)||isJSPPage(firstNode)) {



				//To get MenuItemNode resource bundle properties
				aEditableLocalizedBundle =
					EditableLocalizedBundle.getBundle(firstNode.getUID(), aCurrentLocale);
				/*
				aEditableLocalizedBundle =
					EditableLocalizedBundle.getBundle(firstNode.getMenuItem().getStyleID(), aCurrentLocale);
				*/
				indexForSearch = aEditableLocalizedBundle.getString("IndexForSearch","IndexForSearch_Not_Defined");
				if(indexForSearch==null || (indexForSearch.trim()).equals(EMPTY_STRING) ||
					(indexForSearch.trim()).equalsIgnoreCase("true") ||
						indexForSearch.equals("IndexForSearch_Not_Defined")	){
					indexForSearch = "true";
				}

				// The below code is added to resolve the Defect #2021

				/*
				//Get the title from the default property file of the resource bundle
				EditableLocalizedBundle defaultResourceBundle =
					EditableLocalizedBundle.getBundle(firstNode.getUID());
				String titleFromDefaultResourceBundle =
					defaultResourceBundle.getString("title",TITLE_NOT_DEFINED);
				titleFromDefaultResourceBundle = replaceNullValue(titleFromDefaultResourceBundle,TITLE_NOT_DEFINED);
				*/

				//Get the title of the current locale.
				EditableLocalizedBundle currentLocaleResourceBundle =
					EditableLocalizedBundle.getBundle(firstNode.getUID(),aCurrentLocale);

				if(currentLocaleResourceBundle!=null &&
					indexForSearch.equals("true") && !siteLocaleIndexed){
					currentLocaleTitle =
						currentLocaleResourceBundle.getString("title",TITLE_NOT_DEFINED);
					currentLocaleTitle = replaceNullValue(currentLocaleTitle,TITLE_NOT_DEFINED);
					aNavigationItem = getNavigationItem(firstNode,aCurrentLocale);
					//Add the NavigationItem in list.
					//OVSD: 604,876,212: This fix is to supress hongkong chinese links. Fix
					//included null check.
					if(!TITLE_NOT_DEFINED.equals(currentLocaleTitle) && aNavigationItem != null){
						navigationItemList.add(aNavigationItem);
					}
					siteLocaleIndexed = true;
				}

				//End of Defect #2021

				if(supportedLocalesList!=null && supportedLocalesList.size()>0){
					//Create a NavigationItem Object for all supported locales
					Iterator supportedLocalesListItr = supportedLocalesList.iterator();
					while (supportedLocalesListItr.hasNext()) {
						 Locale supportedLocale = (Locale)supportedLocalesListItr.next();
						 try{
						 	 //To get MenuItemNode resource bundle properties
						 	  EditableLocalizedBundle elb =
						 	  	EditableLocalizedBundle.getBundle(
							 		firstNode.getUID(),supportedLocale);

							 /* EditableLocalizedBundle elb =
								EditableLocalizedBundle.getBundle(
									firstNode.getMenuItem().getStyleID(), supportedLocale);  */


							// The below code is added to resolve the Defect #2021
							//Get the title from the language specific property file of the resource bundle
							 String titleFromLanguageSpecificResourceBundle =
								elb.getString("title",TITLE_NOT_DEFINED);
							 titleFromLanguageSpecificResourceBundle =
							 	replaceNullValue(titleFromLanguageSpecificResourceBundle,TITLE_NOT_DEFINED);
							 //End of Defect #2021

							 if(elb!=null && indexForSearch.equals("true")){
								 if(!currentLocaleTitle.equals(titleFromLanguageSpecificResourceBundle)){
								       if(!TITLE_NOT_DEFINED.equals(titleFromLanguageSpecificResourceBundle)){
										aNavigationItem = getNavigationItem(firstNode,supportedLocale);
										//Add the NavigationItem in list.
										//OVSD: 604,876,212: This fix is to supress hongkong chinese links. Null
										//check added.
										if(aNavigationItem != null){
											navigationItemList.add(aNavigationItem);
										}
								 	}
								 }
							 }

						 }catch(Exception mEx){
						 }
					}//while

				}else{
					if(indexForSearch.equals("true")){
						aNavigationItem = getNavigationItem(firstNode,aCurrentLocale);
						//OVSD: 604,876,212: This fix is to supress hongkong chinese links.
						//Added null checks.
						if(aNavigationItem != null){
						//Add the NavigationItem in list.
							navigationItemList.add(aNavigationItem);
						}
					}
				}//if
			}//if
		}//while


		//Page size
		int configuredPageSize = 0;
		boolean nextLinkRequired = false;
		int pageNumber = 0;

		ArrayList trimmedNavigationItemList = null;

		if(navigationItemList!=null & navigationItemList.size()>0){
			HttpSession httpSession = request.getSession();
			httpSession.setAttribute("expired","false");
			httpSession.setAttribute("SiteDNS",aSite.getDNSName());
			httpSession.setAttribute("SiteTitle",aSite.getTitle());
			//httpSession.setAttribute("NavigationItemList",navigationItemList);

			out.println("Total items picked = "+navigationItemList.size());
			//We do the following:
			/*
			1. Check for the param PAGE_NO in the request. Default is set to '0'.
			2. Check if the <<Next>> link is required
			3. Trim the navigation item list
			4. Compute the <<Next>> link url and put it in request scope.
			*/

			// Get the 'PAGE_NO' parameter from the request.
			String pageNumberString =	request.getParameter(PAGE_NO);

			try{
				pageNumber = (pageNumberString == null)?0:Integer.parseInt(pageNumberString);
       		}catch(Exception ex){
       		    //pageNumber is set to '0' if it is not present in request.
       			pageNumber = 0;
       		}	
			

			//determines number of links to be displayed on index page.
			configuredPageSize = getPageSize();


			// Trim the navigation item list
			int startIndex = pageNumber == 0?0:((pageNumber * configuredPageSize) - 1);

			
			//Condition for ensuring that a higer page number does not lead to a start index which is greater than the size
			//of the list
			if (startIndex > navigationItemList.size()) {
					startIndex = 0;
					// if page number exceeds maximum possible for given list of navigation item then it is reset to 0
					pageNumber = 0;
			}

			
			//Check if the <<Next>> link is required.
			if ((navigationItemList.subList(startIndex, (navigationItemList.size() - 1)).size() > configuredPageSize)) {
				nextLinkRequired = true;
			}

			//determines index of the last link to be displayed from the list.
			int endIndex = startIndex + configuredPageSize;

			try{
				trimmedNavigationItemList = new ArrayList(navigationItemList.subList(startIndex, endIndex));
			}catch(IndexOutOfBoundsException indExp) {

				trimmedNavigationItemList = new ArrayList(navigationItemList.subList(startIndex, (navigationItemList.size() - 1)));
			}
			

			String searchIndexPage = null;
			/*try{
			    	searchIndexPage = (String)Config.getValue("SPP.search.SearchIndexPage."+aSite.getDNSName());
			}catch(Exception ex){
				searchIndexPage = "Search Index Page(http)";
			}  */
            searchIndexPage = com.hp.spp.portal.common.site.SiteManager.getInstance().getSite(aSite.getDNSName()).getSearchIndexPage();
            if(searchIndexPage == null){
                searchIndexPage = "Search Index Page(http)";
            }
            //Generating <<Next>> link url
			if(nextLinkRequired){
				String serverPath = request.getScheme() + "://"+ request.getServerName()+ ":"+ request.getServerPort() + request.getContextPath();
				String nextLinkPath = serverPath + "/site/"+aSite.getDNSName()+"/?page="+URLEncoder.encode(searchIndexPage,"UTF-8")+"&pageNo="+URLEncoder.encode(""+(pageNumber+1),"UTF-8");
			    request.setAttribute("nextLinkPath",nextLinkPath);
			}

			// Finally set it in the session/request to the IndexListView
			httpSession.setAttribute("NavigationItemList",trimmedNavigationItemList);


			String indexListView = null;
			try{
			    	indexListView = (String)Config.getValue("SPP.search.IndexListView");
			}catch(Exception ex){ }

			if(indexListView == null || indexListView.length() == 0){
				//indexListView = "IndexListView.jsp";
				indexListView = "jsp/SPP/search/IndexListView.jsp";
			}
			indexListView = "/"+indexListView;
			String path= indexListView;
			RequestDispatcher rd = application.getRequestDispatcher(path);
			rd.include(request,response);
		}
	}//if

%>