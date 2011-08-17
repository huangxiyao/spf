<!-- IndexListView.jsp -->
<%/*--
	@(#)IndexListView.jsp 

	Shared Portal Platform.

	Revision History:
	------------------------------------------------------------------------------
	Ver        Modified By               Date                Notes
	------------------------------------------------------------------------------
	v1        ShivaShanker B            11-Sep-2006          Created

	Note :This JSP page is responsible for rendering the list of navigation items to
	be indexed. This JSP page will be used by the HP Spider.
	
	This expects the NaigationItem list in the request. It also expects a boolean which 
	instructs it to display the <next> link. The complete list of NavigationItems is put 
	in the session and the next link will retrieve the next batch and display.
--*/%>
<%@ page import="java.util.Iterator,
	java.util.Collection,
	java.util.ArrayList, 
	com.hp.spp.search.common.NavigationItem,
	com.hp.spp.config.Config"
	contentType="text/html; charset=UTF-8"
%>

<%!
        /*
           This method returns the total number of rows to be displayed per page.
           The page size value is retrived from the spp database.
        */       	int getPageSize(){
       		int pageSize = 500;
       	        try{
       			pageSize = Integer.parseInt(Config.getValue("SPP.search.PageSize"));       			
       		}catch(Exception ex){
       		        //Default the page size to 500
       			pageSize = 500;
       		}	
       		return pageSize;
       	}
       	
       	/* This method determines the total number of pages.
       	   - Devide the total number of records (totalLineItems) by page sige (getPageSize())
       	     and ceil the resultant value which gives the total number of pages.
       	    	
       	*/
       	double getNextPage(int totalLineItems){
       		double nextPage = Math.ceil((double)totalLineItems/getPageSize());
       		return nextPage;
       	}
%>
<html>
        <%
         
         HttpSession httpSession = request.getSession();         
         //httpSession.setMaxInactiveInterval(5);
         /* 
            Check the wheather the http session alive or not, 
            - At the beginning of the http session set a variable like 
              httpSession.setAttribute("expired","false"); This value
              is set in SearchIndexPage.jsp page.
            - To check wheather the http session is alive or not, access the
              httpSession.getAttribute("expired"), if it returns null then
              sesson is not alive.

         */
         String sessionExpired=(String)httpSession.getAttribute("expired");
         if (sessionExpired == null ) {
          out.println("Session is expired.....");
          out.flush();
         }
         String siteTitle=(String)httpSession.getAttribute("SiteTitle");
        %> 
	<head>	   
		<meta HTTP-EQUIV="content-type" CONTENT="text/html; charset=UTF-8"/>
		<meta NAME="ROBOTS" CONTENT="NOINDEX" />
		<meta NAME="keywords" CONTENT="Shared Portal Platform index Page for <%=siteTitle%>,
			spp index page for <%=siteTitle%>"/>
		<meta NAME="Description" CONTENT="Shared Portal Platform index page for 
			<%=siteTitle%>" />
	      	<title>Shared Portal Platform - index page for <%=siteTitle%> </title>  
	</head>
	<body>
	<%      
		
		//Get the navigation item list from the request.		
		Collection aNavigationItemList = (ArrayList)
			httpSession.getAttribute("NavigationItemList");
		
		//Determine wheather next page is available or not
		double nextPage = getNextPage(aNavigationItemList.size());		
		// Construct the links to agent page for each navigation item. 	
		String serverPath = request.getScheme() +
				"://"+ request.getServerName()+
				":"+ request.getServerPort() + request.getContextPath();
		String path = null;		
		String siteDNS = (String) httpSession.getAttribute("SiteDNS");		
		Iterator aNavigationItemListItr = aNavigationItemList.iterator();
		Collection elementToBeRemovedList = new ArrayList();
		for(int i=0;i< getPageSize();i++){
		        try{
		                
				NavigationItem aNavigationItem = 
					(NavigationItem)aNavigationItemListItr.next();				
				String langCode = aNavigationItem.getLocale().getLanguage();
				//Fix to differentiate between chinese and taiwanese links.
				String tempLangCode = langCode;
				if("zh".equalsIgnoreCase(aNavigationItem.getLocale().getLanguage())){
					if("tw".equalsIgnoreCase(aNavigationItem.getLocale().getCountry())){
						tempLangCode = "zh-tw";
					}else if("cn".equalsIgnoreCase(aNavigationItem.getLocale().getCountry())){
						tempLangCode = "zh-cn";
					}
				}
				
				
				String titleAndLangCode = aNavigationItem.getTitle()+"_"+tempLangCode;
				path = serverPath +
					"/SPP/SearchAudiencingServlet/ItemIndexingRequest?"+
					"SiteDNS="+siteDNS+
					"&NavItemId="+aNavigationItem.getId()+
					"&LanguageCode="+langCode+
					"&CountryCode="+aNavigationItem.getLocale().getCountry();
				out.println("<br><a href="+path+">"+titleAndLangCode+"</a>");					
				elementToBeRemovedList.add(aNavigationItem);
			}catch(Exception ex){}
		}//while
		
		Iterator aElementToBeRemovedListItr = elementToBeRemovedList.iterator();
		while(aElementToBeRemovedListItr.hasNext()){
			NavigationItem aNavigationItm= (NavigationItem) aElementToBeRemovedListItr.next();
			aNavigationItemList.remove(aNavigationItm);
		}
		
		String indexListView = null;

		String nextLinkPath  = (String)request.getAttribute("nextLinkPath");
		
		if(nextLinkPath != null){
			out.println("<br><a href="+nextLinkPath+" target=_self ><< Next >></a>");
		}
		
		
		/*if(nextPage > 1){		
			String indexListView = null;
			try{
				indexListView = (String)Config.getValue("SPP.search.IndexListView");       			
			}catch(Exception ex){ }
			if(indexListView == null || indexListView.length() == 0){
				indexListView="jsp/SPP/search/IndexListView.jsp";
			}
			indexListView = "/"+indexListView;			
		        path= serverPath + indexListView;
			out.println("<br><a href="+path+" target=_self ><< Next >></a>");	 
		}*/
	%>
	</body>
</html>
