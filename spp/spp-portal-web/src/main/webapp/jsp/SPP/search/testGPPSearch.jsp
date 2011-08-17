<!-- testGppSearch.jsp -->
<%/*--
	@(#)testGppSearch.jsp 

	Shared Portal Platform.

	Revision History:
	------------------------------------------------------------------------------
	Ver        Modified By               Date                Notes
	------------------------------------------------------------------------------
	v1        ShivaShanker B            11-Sep-2006          Created

	Note :This is a gpp dummy search page
--*/%>

<%@ page import="com.epicentric.common.website.EndUserUtils,
	com.epicentric.common.website.I18nUtils,
	com.epicentric.site.Site,
	com.hp.spp.config.Config,
	com.hp.spp.portal.common.site.SiteManager,
	com.hp.spp.profile.Constants,
	com.hp.spp.search.Result,
	org.apache.log4j.Logger,
	org.w3c.dom.Document,
	org.w3c.dom.Element,
	org.w3c.dom.NodeList,
	org.w3c.dom.Text,
	org.xml.sax.SAXException,
	javax.servlet.jsp.JspWriter,
	javax.xml.parsers.DocumentBuilder,
	javax.xml.parsers.DocumentBuilderFactory,
	javax.xml.parsers.ParserConfigurationException,
	java.io.*,
	java.net.HttpURLConnection,
	java.net.MalformedURLException,
	java.net.URL,
	java.net.URLEncoder,
	java.util.*"
	
	contentType="text/html; charset=UTF-8"
%>
<%!

	//Declare variable
	 private static final Logger mLog = Logger.getLogger("testGppSearch.jsp");
	 private static final Logger mTLog = Logger.getLogger("TIME."+ "testGPPSearch.jsp");
	 private static final String error = " ERROR: ";
	 private static final String errorMsg = "Problem in displaying the page...";
	 
	/*
	   a) xmlFileInputStream : Xml file in inputstream format
	   b) tagsToReadList : Tags to be read from the xml file.
	*/
	public Collection parseXML(InputStream xml,String tagName,String callMethodName,JspWriter out) throws IOException{
		Collection aResultList = new ArrayList();
		DocumentBuilderFactory factory =
		DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);

		try {
			 DocumentBuilder builder = factory.newDocumentBuilder();
			 try {
				Document document = builder.parse(xml);
				if("callGetResultElementsInArrayList".equals(callMethodName)){
					aResultList =  getResultElementsInArrayList(tagName,document);
				}				
				if("callGetAllowedNavigationItems".equals(callMethodName)){
					aResultList =  getAllowedNavigationItems(tagName,document);
				}	
			} catch (SAXException e) {
				e.printStackTrace();
				mLog.error(error,e);
				out.println(errorMsg);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			mLog.error(error,e);
			out.println(errorMsg);
		}
		return aResultList;
		
	}//parseXML
	
	
	/*	    	
	<result href="http://search.hp.com/redirect.html?url=http%3A//welcome.hp.com/country/us/en/support.html&qt=&hit=1" url="http://welcome.hp.com/country/us/en/support.html">
	  <title>HP Support & Drivers</title>
	  <summary>Directory of HP support and additional resource information. Get assistance for: drivers, downloads, software updates, patches, find authorized support providers, replacement parts, product registration, hp training ...</summary>
	  <publisher />
	  <extra>spp_page_id</extra>
	  <date>2006-09-24T21:15:12Z</date>
	  <size>56.5K</size>
	  <score>61</score>
	</result>
	
	
	public Collection getResultElementsInArrayList(String resultTag,Document document){
		    Result aResult = null;
		    ArrayList resultList = new ArrayList();
			NodeList resultNodesList =  document.getDocumentElement().getElementsByTagName(resultTag);
			for (int i=0; i<resultNodesList.getLength() ; i ++) {
				aResult = new Result();
				Element resultElement = (Element) resultNodesList.item(i);

				//Get the url attribute of result element
				String urlAttribute = resultElement.getAttribute("href");
				aResult.setUrlAttribute(urlAttribute);

				// Get title
				NodeList titleNodeList = resultElement.getElementsByTagName("title");
				Element titleElement = (Element) titleNodeList.item(0);
				Text aTitleTextNode = (Text)titleElement.getFirstChild();
				aResult.setTitle(aTitleTextNode.getNodeValue());


				//Get navigationItemId
				NodeList navigationItemIdsNodeList = resultElement.getElementsByTagName("date");
				Element navigationItemElement = (Element) navigationItemIdsNodeList.item(0);
				Text aNavigationItemTextNode = (Text)navigationItemElement.getFirstChild();
				aResult.setNavigationItemId(aNavigationItemTextNode.getNodeValue());


				resultList.add(aResult);

			}//for
			return resultList;
	}//getResultElementsInArrayList
	
	
	*/
	
	
	/*
	<result url="url1">
	  <title>HP Support and Drivers1</title>
	  <summary>Directory of HP support and additional resource information. Get assistance for: drivers, downloads, software updates, patches, find authorized support providers, replacement parts, product registration, hp training ...</summary>
	  <publisher />
	  <extra>
	      <name>spp_page_id</name>
	      <value>100</value>
	  </extra>
	  <extra>
	      <name>spp_site</name>
	      <value>testing</value>
	  </extra>
	  <date>2006-09-24T21:15:12Z</date>
	  <size>56.5K</size>
	  <score>61</score>
	</result>
	*/
	
	public Collection getResultElementsInArrayList(String resultTag,Document document){
	    Result aResult = null;
	    ArrayList resultList = new ArrayList();
		NodeList resultNodesList =  document.getDocumentElement().getElementsByTagName(resultTag);
		for (int i=0; i<resultNodesList.getLength() ; i ++) {
			aResult = new Result();
			Element resultElement = (Element) resultNodesList.item(i);

			// Get title
			NodeList titleNodeList = resultElement.getElementsByTagName("title");
			Element titleElement = (Element) titleNodeList.item(0);
			Text aTitleTextNode = (Text)titleElement.getFirstChild();
			aResult.setTitle(aTitleTextNode.getNodeValue());


			//Get navigationItemId and site name
			NodeList extraNodesList = resultElement.getElementsByTagName("extra");
			for (int j=0; j<extraNodesList.getLength() ; j++) {
				Element extraElement = (Element) extraNodesList.item(j);

				NodeList nameNodeList = extraElement.getElementsByTagName("name");
				Element nameElement = (Element) nameNodeList.item(0);
				Text aNameTextNode = (Text)nameElement.getFirstChild();				
				if("spp_page_id:".equals(aNameTextNode.getNodeValue())){
					//Get the <value> element
					NodeList valueNodeList = extraElement.getElementsByTagName("value");
					Element valueElement = (Element) valueNodeList.item(0);
					Text aValueTextNode = (Text)valueElement.getFirstChild();
					aResult.setNavigationItemId(aValueTextNode.getNodeValue());
				}

				if("spp_site:".equals(aNameTextNode.getNodeValue())){
					//Get the <value> element
					NodeList valueNodeList = extraElement.getElementsByTagName("value");
					Element valueElement = (Element) valueNodeList.item(0);
					Text aValueTextNode = (Text)valueElement.getFirstChild();
					aResult.setSiteName(aValueTextNode.getNodeValue());
				}


			}

			resultList.add(aResult);

		}//for
		return resultList;
	}//getResultElementsInArrayList
	
	
	/*
		<NavigationItem>
				<Id>String</Id>
		</NavigationItem>
	*/

	public Collection getAllowedNavigationItems(String navigationItemTag,Document document){
		ArrayList navigationItemList = new ArrayList();
		NodeList navigationItemNodesList =  document.getDocumentElement().getElementsByTagName(navigationItemTag);
		for (int i=0; i<navigationItemNodesList.getLength() ; i ++) {
			Element navigationItemElement = (Element) navigationItemNodesList.item(i);

			// Get Id element
			NodeList idNodeList = navigationItemElement.getElementsByTagName("Id");
			Element idElement = (Element) idNodeList.item(0);
			Text idTextNode = (Text)idElement.getFirstChild();
			String idValue = idTextNode.getNodeValue();
			navigationItemList.add(idValue);
		}//for
		return navigationItemList;
		
	}//getAllowedNavigationItems()
	
	
	public Collection getTitleAndAllowedNaviagtionItems(	        
		Collection allowedNaviagitonItemsList,Collection resultElementsInArrayList){
		
		ArrayList titleAndAllowedNavigationItemsList = new ArrayList();
		Iterator allowedNaviagitonItemsListItr = allowedNaviagitonItemsList.iterator();
		
		
		while(allowedNaviagitonItemsListItr.hasNext()){
			String allowedNavItem = (String)allowedNaviagitonItemsListItr.next();
			if(allowedNavItem!=null && allowedNavItem.length() > 0){
				Iterator resultElementsInArrayListItr = resultElementsInArrayList.iterator();
				while(resultElementsInArrayListItr.hasNext()){
					Result aResult = (Result)resultElementsInArrayListItr.next();
					if(aResult!=null){
						String aNavItemId = aResult.getNavigationItemId();
						if(aNavItemId!=null && aNavItemId.equals(allowedNavItem)){
							titleAndAllowedNavigationItemsList.add(aResult);
						}
					}	
				}
			}	
		}
		return titleAndAllowedNavigationItemsList;
	
	}
	
	/*
		This method retrives xml in InputStream format.
	*/
	public InputStream  getInputStreamXML(String url,String queryParams, String requestMethod,JspWriter out) throws IOException{
		// TODO Auto-generated method stub
		URL urlToService = null;
		InputStream ipStr = null;

		try {
			urlToService = new URL(url);	
			//urlToService = new URL("http","search.hp.com","/query.xml?");
			
						
		} catch (MalformedURLException e) {				
			e.printStackTrace();
			mLog.error(error,e);
			out.println(errorMsg);
		}		
		

		HttpURLConnection urlConnection = null;
		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;								
		try {
			urlConnection = (HttpURLConnection)urlToService.openConnection();
			urlConnection.setRequestMethod(requestMethod);
			urlConnection.setDoOutput(true);
			urlConnection.setAllowUserInteraction(true);				
			//urlConnection.connect();

			outputStream = urlConnection.getOutputStream();
			outputStreamWriter = new OutputStreamWriter(outputStream);
			outputStreamWriter.write(queryParams);
			outputStreamWriter.flush();

			//Thread.sleep(10000);
			ipStr = urlConnection.getInputStream();
			
			/*ipStr.read();
			InputStreamReader ipStrReader = new InputStreamReader(ipStr, "UTF-8");  
			BufferedReader br = new BufferedReader(ipStrReader);

			String respLine = null;
			while ((respLine = br.readLine()) != null) {
				System.out.println("\n"+respLine);	
			}*/

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mLog.error(error,e);
			out.println(errorMsg);

		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mLog.error(error,e);
			out.println(errorMsg);
		}
		finally{
			try {
				//outputStreamWriter.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mLog.error(error,e);
				out.println(errorMsg);
			}

		}
		

		return ipStr;			
	
	}
	
		
%>

<html>

<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>GPP - Search Dummy Page</title>



</head>

<body>
<%

Map userProfile = (Map) session.getAttribute("com.hp.spp.UserProfile");	
if (mLog.isDebugEnabled()) {
	mLog.debug("UserProfile : "+userProfile);	
}
// Construct encoded url 
String enc = "UTF-8";
//String url = "http://search.hp.com/query.xml?";
//String url = " http://dnt604.den.hp.com:8765/query.xml?";

//String url = url = "http://g3w0738.americas.hpqcorp.net/query.xml?";


//Get the site id from the profile
//String siteID = (String) userProfile.get("SiteId");

//This validation checks needs 
if(userProfile == null){
//If the user profile is null then, this page may have been invoked 
// after logout.
	out.println("Error: User data missing in session. Please re-login");
	return;
}


//Get the site id and encode it.
String spp_site_dns = (String)request.getParameter("spp_site");

if(spp_site_dns==null || spp_site_dns.length()==0){
	spp_site_dns = (String) userProfile.get(Constants.MAP_SITE);
	//spp_site_dns = (String)userProfile.get("SiteId");
	//out.println("spp_site_dns===="+spp_site_dns);
}
if (mLog.isDebugEnabled()) {
	mLog.debug("spp_site_dns : "+spp_site_dns);
}


//Prepare the broker url
String url = null;
String BrokerUrlkey = "SPP."+spp_site_dns.trim()+".BrokerUrl";
if (mLog.isDebugEnabled()) {
	mLog.debug("BrokerUrlkey : "+BrokerUrlkey);
}
//try{
	//url = (String)Config.getValue(BrokerUrlkey);
    url = SiteManager.getInstance().getSite(spp_site_dns).getSearchBrokerUrl();
    if(url == null){
        mLog.error("Search Broker URL not found for site: "+spp_site_dns);
        return;
    }
    //url = "http://dnt604.den.hp.com:8765/query.xml?";
/*}catch(Exception ex){
	out.println("Problem in getting the Key :  " + BrokerUrlkey);
	mLog.error(error,ex);	
	return;
}*/
if (mLog.isDebugEnabled()) {
	mLog.debug("url : "+url);
}

//Prepare the HpvcKey and values
/*String HpvcKey = "SPP."+spp_site_dns.trim()+".HpvcKey";
if (mLog.isDebugEnabled()) {
	mLog.debug("HpvcKey : "+HpvcKey);
}          */
String HpvcValue  = null;

//try{
	
	//HpvcValue = (String)Config.getValue(HpvcKey);
    HpvcValue = SiteManager.getInstance().getSite(spp_site_dns).getSearchHpvcKey();
    if(HpvcValue == null){
        mLog.error("Hpvc Value not found for site: "+spp_site_dns);
        return;
    }
    //HpvcValue="SPP_QA_ft";
/*}catch(Exception ex){
	out.println("Problem in getting the Key :  " + HpvcKey);
	mLog.error(error,ex);	
	return;
}*/

if (mLog.isDebugEnabled()) {
	mLog.debug("HpvcValue : "+HpvcValue);
}

String HpvcKeyValue = "hpvc="+HpvcValue;
if (mLog.isDebugEnabled()) {
	mLog.debug("HpvcKeyValue : "+HpvcKeyValue);
}

// String HpvcKeyValue = "hpvc=SPPNavItems";
//String HpvcKeyValue = "hpvc=SPP_QA_itg";
String DetailKeyValue = "detail=1";
String debugKeyValue = "debug=2";



//Get the query text and encode it
String QueryText = (String)request.getParameter("qt");
if(QueryText==null || QueryText.length()==0){
	QueryText="";
	//out.println("QueryText===="+QueryText);
}

if (mLog.isDebugEnabled()) {
	mLog.debug("QueryText : "+QueryText);
}

String QueryTextEncocoded = URLEncoder.encode(QueryText,enc);
if (mLog.isDebugEnabled()) {
	mLog.debug("QueryTextEncocoded : "+QueryTextEncocoded);
}
String QueryTextKeyValue = "qt="+QueryTextEncocoded;
if (mLog.isDebugEnabled()) {
	mLog.debug("QueryTextKeyValue : "+QueryTextKeyValue);
}
//Get the language and encode it.
String Language = (String)request.getParameter("lang");
// Get locale from current locale
if(Language == null || Language.length()==0) {
        Site aSite =  EndUserUtils.getSite(pageContext);
	if (mLog.isDebugEnabled()) {
		mLog.debug("aSite : "+aSite);
	}
        if(aSite!=null){
		Locale	aCurrentLocale= I18nUtils.getCurrentLocale(aSite);
		Language = aCurrentLocale.getLanguage();		
	}	
	if(Language==null || Language.length()==0){
		Language="";
	}
	//out.println("Language===="+Language);
}

if (mLog.isDebugEnabled()) {
	mLog.debug("Language : "+Language);
}
String LanguageEncoded = URLEncoder.encode(Language,enc);
if (mLog.isDebugEnabled()) {
	mLog.debug("LanguageEncoded : "+LanguageEncoded);
}
String LanguageKeyValue = "lang="+LanguageEncoded; 
if (mLog.isDebugEnabled()) {
	mLog.debug("LanguageKeyValue : "+LanguageKeyValue);
}


//Get the groups and encode it.
String groups = (String)request.getParameter("groups");

if(groups==null || groups.length() == 0 ){
	groups = (String)userProfile.get(Constants.MAP_USERGROUPS);
	
	//After fetching groups from the profile, check again for null.
	if(groups==null || groups.length() == 0 ){
		//If user is not Logged in.
		groups = "";
	}
	//out.println("groups===="+groups);
}

if (mLog.isDebugEnabled()) {
	mLog.debug("groups : "+groups);
}

String groupsEncoded = URLEncoder.encode(groups,enc); 
if (mLog.isDebugEnabled()) {
	mLog.debug("groupsEncoded : "+groupsEncoded);
}

String groupsKeyValue="spp_groups="+groupsEncoded;
if (mLog.isDebugEnabled()) {
	mLog.debug("groupsKeyValue : "+groupsKeyValue);
}




String spp_site_dnsEncoded  =URLEncoder.encode(spp_site_dns,enc); 
if (mLog.isDebugEnabled()) {
	mLog.debug("spp_site_dnsEncoded : "+spp_site_dnsEncoded);
}

String spp_site_dnsKeyValue = "spp_site="+spp_site_dnsEncoded;
if (mLog.isDebugEnabled()) {
	mLog.debug("spp_site_dnsKeyValue : "+spp_site_dnsKeyValue);
}


//String queryParams = HpvcKeyValue+"&"+DetailKeyValue; 
String queryParams = debugKeyValue+"&"+HpvcKeyValue+"&"+DetailKeyValue +"&"+QueryTextKeyValue+"&"+LanguageKeyValue+"&"+groupsKeyValue+"&"+spp_site_dnsKeyValue;
if (mLog.isDebugEnabled()) {
	mLog.debug("queryParams : "+queryParams);
}

//String queryParams = QueryTextKeyValue+"&"+LanguageKeyValue+"&"+groupsKeyValue+"&"+spp_site_dnsKeyValue;

url = url + queryParams + "&" + "nh=500";
if (mLog.isDebugEnabled()) {
	mLog.debug("Final Broker URL : "+url);
}

//url = url+queryParams;
//response.sendRedirect(url);

//out.println("url======="+url);
//out.println("queryParams======="+queryParams);

long searchStartTime = System.currentTimeMillis();
//Get the xml input stream by callin getInputStreamXML() method
InputStream xmlInputStream = getInputStreamXML(url,queryParams,"GET",out);
//out.println("xmlInputStream==="+xmlInputStream);
long searchEndTime = System.currentTimeMillis();
if (mTLog.isDebugEnabled()){
	mTLog.debug("Fetch Result Duration :" + (searchEndTime - searchStartTime));
	}




/* if (mLog.isDebugEnabled()) {
        xmlInputStream.read();
	InputStreamReader ipStrReader = new InputStreamReader(xmlInputStream, "UTF-8");

	BufferedReader br1 = new BufferedReader(ipStrReader);

	StringBuffer respLine = new StringBuffer();
	String line = null;
	while ((line = br1.readLine()) != null) {
		respLine.append(line).append("\n");	
	}
	mLog.debug("The Xml file from the broker url (xmlInputStream) :  "+respLine.toString());
	//out.println(respLine.toString());
}*/



//Create a collection containing name of the tags to be read
 
//Pass xml input stream to a parser and  tag to read
ArrayList aValues = (ArrayList)parseXML(xmlInputStream,"result","callGetResultElementsInArrayList",out);

if (mLog.isDebugEnabled()) {
        if(aValues!=null){
		mLog.debug("After parsing the xml from the broker url the values stored in the array list, the size :  "+aValues.size());
	}else{
		mLog.debug("After parsing the xml from the broker url the values stored in the array list, the array list object is : null");
	}
}
Iterator aValuesItr = null;
if(aValues!=null && aValues.size()>0){
	 aValuesItr = aValues.iterator();
}	 
String audience = (String)request.getParameter("Audience");
if(audience ==null || audience.length()==0){
	audience="yes";
	//out.println("audience===="+audience);
}

if (mLog.isDebugEnabled()) {
	mLog.debug("Audience Flag: "+audience);
}

if("no".equals(audience) && aValuesItr!=null){
	long renderingNoAudStartTime = System.currentTimeMillis();
	out.println("<div align=center>");
		out.println("<table align=center border=1 width=78% bordercolor=#000080 id=table1>");
			out.println("<tr>");
				out.println("<td colspan = 2  align=center bgcolor=#808080>");
				out.println("<font color=#FFFF00><b>Query Broker - Naviagtion Items</b></font></td>");					
			out.println("</tr>");
			out.println("<tr>");
				out.println("<td width=171 align=center bgcolor=#808080>");
				out.println("<font color=#FFFF00><b>Navigation Item Id</b></font></td>");
				out.println("<td width=226 align=center bgcolor=#808080>");
				out.println("<font color=#FFFF00><b>&nbsp;Title</b></font></td>");
				//out.println("<td align=center bgcolor=#808080><font color=#FFFF00><b>&nbsp;Site</b></font></td>"); 
			out.println("</tr>");

		while(aValuesItr.hasNext()){
			Result aResult = (Result)aValuesItr.next();
			
			out.println("<tr>");
				out.println("<td width=171 bgcolor=#C0C0C0>"+aResult.getNavigationItemId()+"</td>");
				out.println("<td width=226 bgcolor=#C0C0C0>"+aResult.getTitle()+"</td>");
				//out.println("<td bgcolor=#C0C0C0>"+aResult.getSiteName()+"</td>");
				
			out.println("</tr>");
		}
		out.println("</table>");
	out.println("</div>");	
	long renderingNoAudEndTime = System.currentTimeMillis();
	if (mTLog.isDebugEnabled()){
		mTLog.debug("Rendering Result Duration :" + (renderingNoAudEndTime - renderingNoAudStartTime));
	}	

}


//Get the audience flag. If true then call audience service
if("yes".equals(audience) && aValuesItr!=null){
	//String audienceServiceUrl = "http://bbnapppro02.bbn.hp.com:27001/portal/SPP/SearchAudiencingServlet/ItemAudiencingRequest";
	
	
	String audienceServiceUrl = 
		request.getScheme() 
		+ "://"  
		+ request.getServerName() 
		+ ":"
		+ request.getServerPort() 
		+ request.getContextPath()
		+ "/SPP/SearchAudiencingServlet/ItemAudiencingRequest";
		
		//out.println("audienceServiceUrl======="+audienceServiceUrl);
	if (mLog.isDebugEnabled()) {
		mLog.debug("audienceServiceUrl : "+audienceServiceUrl);
	}
	
	String valuesToPass = "xmlRequest";
	try {
	
	        String hpp_id = (String)request.getParameter("hpp_id");
	        
	        if(hpp_id==null || hpp_id.length()==0){
	        	hpp_id= (String)userProfile.get(Constants.MAP_HPPID);
	        	//out.println("hpp_id===="+hpp_id);
	        }
		valuesToPass = URLEncoder.encode(valuesToPass, "UTF-8")+"=";		
		StringBuffer inputXml = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");                
		
				
		inputXml.append("<AudiencingServiceRequest>");		
		inputXml.append("<User><hppId>");
		inputXml.append(hpp_id);		
		inputXml.append("</hppId></User>");				
		inputXml.append("<Site><Name>");
		inputXml.append(spp_site_dns);
		inputXml.append("</Name></Site>");		
		inputXml.append("<ItemList>");
		while(aValuesItr.hasNext()){
			Result aResult = (Result)aValuesItr.next();
			
			String navItemId = aResult.getNavigationItemId();			
			inputXml.append("<NavigationItem><Id>");
			inputXml.append(navItemId);
			inputXml.append("</Id></NavigationItem>");
		}
		inputXml.append("</ItemList></AudiencingServiceRequest>"); 
		if (mLog.isDebugEnabled()) {
			mLog.debug("The XML file needs to be passed to audiencing service(inputXml) "+inputXml);
		}
		
		
		String temp = URLEncoder.encode(inputXml.toString(), "UTF-8");
		valuesToPass = valuesToPass+temp;		
		if (mLog.isDebugEnabled()) {
			mLog.debug("Values to be passed for the audiencing service is(valuesToPass) : "+valuesToPass);
		}



		long audienceStartTime = System.currentTimeMillis();
		
		InputStream xmlInputStreamForAudience = getInputStreamXML(audienceServiceUrl,valuesToPass,"POST",out);
		
		long audienceEndTime = System.currentTimeMillis();
		if (mTLog.isDebugEnabled()){
			mTLog.debug("Audiencing Duration :" + (audienceEndTime - audienceStartTime));
		}





		/* if (mLog.isDebugEnabled()) {
			InputStreamReader ipStrReader = new InputStreamReader(xmlInputStreamForAudience, "UTF-8");		
			BufferedReader br = new BufferedReader(ipStrReader);                
                        StringBuffer respLine =  new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {			
				respLine.append(line).append("\n");
			}
			mLog.debug("The xml file after audiencing(xmlInputStreamForAudience) : "+respLine.toString()); 
		}*/	
		
		//Pass xml input stream to a parser and  tag to read
		ArrayList aNavigationItemValues = (ArrayList)parseXML(xmlInputStreamForAudience,"NavigationItem","callGetAllowedNavigationItems",out);
		if (mLog.isDebugEnabled()) {
			if(aNavigationItemValues!=null){
				mLog.debug("After parsing the xml file after audiencing service  the values stored in the array list, the size :  "+aNavigationItemValues.size());
			}else{
				mLog.debug("After parsing the xml file after audiencing service  the values stored in the array list, the array list object is : null ");
			}	
		}
		/* Iterator aNavigationItemValuesItr = aNavigationItemValues.iterator();
		if(aNavigationItemValues!=null && aNavigationItemValues.size() > 0){
			out.println("<table border=1 width=100% id=table1>");
					out.println("<tr>");
						out.println("<td colspan=2 bgcolor=#81180>");
						out.println("<p align=center><b><font size=6 color=#F12F00>Allowed ");
						out.println("Navigation Items List</font></b></td>");
					out.println("</tr>");

					out.println("<tr>");
						out.println("<td bgcolor=#C111C0>NAVIGATION ITEM ID</td>");
						out.println("<td bgcolor=#C111C0>TITLE</td>");
					out.println("</tr>");

					while(aNavigationItemValuesItr.hasNext()){
						String navigationItemId= (String)aNavigationItemValuesItr.next();
						out.println("<tr>");
							out.println("<td bgcolor=#C0C0C0>"+navigationItemId+"</td>");
							out.println("<td bgcolor=#C0C0C0>&nbsp;</td>");
						out.println("</tr>");

					}
			out.println("</table>");
		}
		
		*/
		
		
		
		long renderingAudStartTime = System.currentTimeMillis();
		//To get the title and navigation item id list
		Collection titleAndAllowedNaviagtionItemsList = getTitleAndAllowedNaviagtionItems(aNavigationItemValues,aValues);
		Iterator titleAndAllowedNaviagtionItemsListItr = 
			titleAndAllowedNaviagtionItemsList.iterator();
		
		if(titleAndAllowedNaviagtionItemsList!=null && titleAndAllowedNaviagtionItemsList.size() > 0){
				out.println("<table align=center border=1 width=78% bordercolor=#000080 id=table1>");
				out.println("<tr>");
					out.println("<td colspan = 2  align=center bgcolor=#808080>");
					out.println("<font color=#FFFF00><b>Audience - Allowed Naviagtion Items</b></font></td>");					
				out.println("</tr>");
				out.println("<tr>");
					out.println("<td width=171 align=center bgcolor=#808080>");
					out.println("<font color=#FFFF00><b>Navigation Item Id</b></font></td>");
					out.println("<td width=226 align=center bgcolor=#808080>");
					out.println("<font color=#FFFF00><b>&nbsp;Title</b></font></td>");
					//out.println("<td align=center bgcolor=#808080><font color=#FFFF00><b>&nbsp;Site</b></font></td>"); 
				out.println("</tr>");
				
				
				
				while(titleAndAllowedNaviagtionItemsListItr.hasNext()){
					Result aResult = (Result) titleAndAllowedNaviagtionItemsListItr.next();
					String navigationItemId = aResult.getNavigationItemId();
					String title = aResult.getTitle();
					out.println("<tr>");
						out.println("<td bgcolor=#C0C0C0>"+navigationItemId+"</td>");
						out.println("<td bgcolor=#C0C0C0>"+title+"</td>");
					out.println("</tr>");
					
				}
				

			out.println("</table>");	
		}
		long renderingAudEndTime = System.currentTimeMillis();
		if (mTLog.isDebugEnabled()){
			mTLog.debug("Rendering Result Duration :" + (renderingAudEndTime - renderingAudStartTime));
		}	

	} catch (UnsupportedEncodingException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		mLog.error(error,e1);
		out.println(errorMsg);
	} catch(Exception ex){
		ex.printStackTrace();
		mLog.error(error,ex);
		out.println(errorMsg);
	}
	
} 
%>

</body>

</html>

