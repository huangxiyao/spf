function findDOM(objectID,withStyle) {
	if (withStyle == 1) {
		if (isID) { return (document.getElementById(objectID).style) ; }
		else {
			if (isAll) { return (document.all[objectID].style); }
		else {
			if (isLayers) { return (document.layers[objectID]); }
		};}
	}
	else {
		if (isID) { return (document.getElementById(objectID)) ; }
		else {
			if (isAll) { return (document.all[objectID]); }
		else {
			if (isLayers) { return (document.layers[objectID]); }
		};}
	}
}

function htmlFormat(message)
{
	while(message.indexOf("&#39;") > -1)
		message = message.replace("&#39;", "'") ;
	while(message.indexOf("&#34;") > -1)
		message = message.replace("&#34;", "\"") ;
	while(message.indexOf("&amp;") > -1)
		message = message.replace("&amp;", "&") ;
	while(message.indexOf("&quot;") > -1)
		message = message.replace("&quot;", "\"") ;
	return message ;
}

/*
 * Prepares the opening of a link by calling a URL and finding out whether
 * the eservice should be opened in the same page or not and whether the
 * user needs to be warned that they are exiting the HP domain.
 * Call the funtion prepareLink passing the link and the service name.
 */

function prepareLink(linkObj, link, serviceName, confirmMsg, sessionInSimulation)
{
	linkObj.href = link;

	// handle different browser cases and if browser not supported just return TRUE
	if (window.XMLHttpRequest) { // Mozilla, Safari,...
    	http_request = new XMLHttpRequest();
		if (http_request.overrideMimeType) {
			http_request.overrideMimeType('text/xml');
		}
	} else if (window.ActiveXObject) { // IE
		try {
			http_request = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try {
				http_request = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e) {
				// ERROR: popup and staying in HP
				linkObj.target = '_blank';
				return true;
			}
		}
	}
	else	{
		// ERROR: popup and staying in HP
		linkObj.target = '_blank';
		return true;
	}

	var protocol = location.protocol;
	var hostname = location.hostname;

	var href = location.href;

	var requestParams = '_EService_=' + serviceName;
	var url = encodeURI(getEServiceInformationURL() + '&' + requestParams);

	http_request.open('GET', url, false);
	http_request.send(null);

	responseText = findResponse(http_request.responseText);
	var windowResponseText = findWindowParameterResponse(responseText);
	var characterEncoding = findCharacterEncoding(responseText);
	responseText = findResponseText(http_request.responseText);

	//Find out if the eService supports simulation mode.
	if(!isEserviceSimulationMode(responseText) && sessionInSimulation){
		alert('This eService does not support simulation. You cannot access it while simulating another user.');
		return false;

	}

	//Now that we know that the eService supports the simulation mode,
	//we do not need the last character in the response string anymore.
	//So we get rid of it.
	responseText = responseText.substring(0, responseText.length - 1);
	
	if("" != characterEncoding){
			 if(link.indexOf('?') == -1 ){
			 link = link + '?_spp_charset='+characterEncoding; 
		 }
		 else{
			 link = link + '&_spp_charset='+characterEncoding;
		 }
	}

	// YY: popup and leaving HP
	if ('YY' == responseText) {
		if(confirm(confirmMsg))
			window.open(link,'_blank',windowResponseText);
		return false;
	}
	// YN: popup and staying in HP
	else if ('YN' == responseText) {
			window.open(link,'_blank',windowResponseText);
		return false;
	}
	// NY: same window and leaving HP
	else if ('NY' == responseText) {
		linkObj.target = '_self';
		return confirm(confirmMsg);
	}
	// NN: same window and staying in HP
	else if ('NN' == responseText) {
		linkObj.target = '_self';
		return true;
	}
	// ERROR: popup and staying in HP
	else {
		linkObj.target = '_blank';
		return true;
	}
}

/*
 * Finds the response in the HTML that is returned. Searches for text
 * surrounded by '*', e.g. '*YN*'.
 */
function findResponse(sString)
{
	var index = sString.indexOf("*");
	sString = sString.substring(index + 1);
	index = sString.indexOf("*");
	sString = sString.substring(0, index);
	return sString;
}

/*
 * Finds the response in the HTML that is returned. Searches for text
 * surrounded by ' *' ', e.g. '*YN''. This excludes the windowParameters
 */
function findResponseText(sString)
{
	var index = sString.indexOf("*");
	sString = sString.substring(index + 1);
	index = sString.indexOf("'");
	sString = sString.substring(0, index);
	return sString;
}

/*
 * Finds the response in the HTML that is returned. Searches for text
 * surrounded by ''', e.g. 'toolbar=yes,scrollbar=no'. 
 * From the whole responseText String it finds only windowParameters passed
 */

function findWindowParameterResponse(sString)
{
	var index = sString.indexOf("'");
	sString = sString.substring(index + 1);
	index = sString.indexOf("'");
	sString = sString.substring(0, index);
	return sString;
}

function findCharacterEncoding(sString)
{
	var index = sString.indexOf("$");
	sString = sString.substring(index + 1);
	index = sString.indexOf("$");
	sString = sString.substring(0, index);
	return sString;
}

/*
 * Builds the friendly URL for the EServiceResponse JSP-include page
 */
function getEServiceInformationURL()
{
	//Getting the complete url
	var href = location.href;
	//Retrieve the inext of first occurance of '/site/'
	var index = href.indexOf('/site/');
	//Fetching the url till the first occurance of '/site/.
	//6 is the length of string '/site/'
	var urlTillSiteString = href.substring(0,index+6);	
	//Fetching the url fragment after the string '/site/'
	var urlAfterSiteString = href.substring(index+6);
	//Fetching the site name
	var siteName = urlAfterSiteString.substring(0,urlAfterSiteString.indexOf('/'));
	//Creating url with sitename at the end.
	href = urlTillSiteString + siteName+'/';
	//Returning final url that invokes EServiceInformation.jsp page.
	return href + '?page=EServiceInformation';
}

/*
 * Returns true if the eService supports simulation mode, false otherwise.
 * Takes the response string from the EserviceInformation.jsp, finds out
 * if the last character is a 'Y' or 'N'. Returns true if it is 'Y'
 * ie eService supports simulation mode, false otherwise.
 */
function isEserviceSimulationMode(sString)
{
	var lastChar = sString.substring(sString.length - 1);

	if ('Y' == lastChar) {
		return true;

	} else {
		return false;
	}
}

/*
 * Pops the window out in case the the new window option is selected for URL type
 * Navigation item in vignette console.
 * This function doesn't pop the security message in case of external sites
 * In case the security message is required , we have to create the navigation item 
 * as Eservice and use that functionality
 */

function popupLink(linkObj, link, serviceName, confirmMsg, popupValue)
{
	if (popupValue) {
		window.open(link,'_blank');
		return false;
	}
	// popup and staying in HP
	else {
		linkObj.target = '_blank';
		return true;
	}
}