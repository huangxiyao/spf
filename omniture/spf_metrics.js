// HP Common Metrics Architecture 20060415a


var s_hp_optOut = false // WARNING: IF SET TO TRUE, PAGE WILL NOT BE TRACKED

// Common Dynamic Account structure - please do not use unless authorized by CKM&A Ops
/* Specify the Report Suite ID(s) to track here */
var s_dynamicAccountSelection=false
var s_dynamicAccountList="hphqqatest1=itg.hp.com"
var s_dynamicAccountMatch=window.location.hostname

/* If no s_account is defined then the below reportsuite is taken as default */
if (!(window.s_account && s_account.length>0)){
	var s_account="hphqglobal,hphqwweservicesportal"
}

//Link Tracking Config
var s_trackDownloadLinks=true
var s_trackExternalLinks=true
var s_trackInlineStats=true
var s_linkDownloadFileTypes="exe,zip,wav,mp3,mov,mpg,avi,doc,pdf,xls,cgi,dot,pot,ppt,wmv,asx"
var s_linkInternalFilters="hp,compaq,cpqcorp,javascript:"
var s_linkLeaveQueryString=true
var s_linkTrackVars="s_prop5,s_prop2,s_prop4,s_prop8"
var s_linkTrackEvents="s_prop5,s_prop2,s_prop4,s_prop8"

/* s_prop7, s_prop8, s_prop9 are taken from Meta Tag data - Please follow the Meta Tag Standards */
/* s_prop10 & s_prop13 are generated automatically */

// Common metrics plugin function - do not remove
function s_hp_doMetricsPlugins() { 
} // end function ()

/*** DO NOT MODIFY THIS SECTION ***/
/* Under no circumstances should you modify this code */
//s_hp_includeJavaScriptFile("welcome.hp-ww.com","/cma/metrics/sc/s_code_remote.js");
s_hp_includeJavaScriptFile("welcome.hp-ww.com","/cma/metrics/sc/s_code_remote.js");

function s_hp_includeJavaScriptFile(hp_hostname,hp_path) {
	if(!(window.s_hp_optOut && window.s_hp_optOut == true)) {
		var hp_ssl=(window.location.protocol.toLowerCase().indexOf('https')!=-1)
		if(hp_hostname && hp_hostname.length>0) {
			if(hp_ssl == true && hp_hostname.toLowerCase().indexOf("welcome.") != -1) { hp_hostname = "secure.hp-ww.com"; }
			var fullURL = "http" + (hp_ssl?"s":"") + "://" + hp_hostname + hp_path
		}
		else
			var fullURL=hp_path;
		document.write("<sc" + "ript language=\"JavaScript\" src=\""+fullURL+"\"></sc" + "ript>");
	}
}
// END Clickstream:
