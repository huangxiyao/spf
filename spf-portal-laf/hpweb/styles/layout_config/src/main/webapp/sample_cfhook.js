
// This is a sample Central Form integration JS file from the HPP team, and
// serves as demo purpose with the HPWeb VAP layout. 

// This script is a modified version of the cfhook.js file that the HPP team 
// provides, and only contains js functions for loading to the New User
// page, Login page, etc.   It doesn't render any HTML such as for the
// Sign-in link, etc, because it is expected that the HPWeb VAP layout
// will handle the rendering of the links to these pages.

// Portal teams should get the latest cfhook.js file from them, and configure
// the variables to the portals accordingly.

// See the 'sample_hpweb_layout_config.jsp' file on how this file is included
// and how these js functions are configured into the HPWebModel object.

var cfserver = "http://hpp.foo.com";

var language_support = 'Y';
var cookie_name = "c_u";

var strSan="44GV44KT";

var default_url = cfserver + "/cia/home.do";
var application_id = "cia";
var sLang = "lang";
var sCountry = "cc";
var applandingpage = cfserver + "/cia/protected.do";
var signInLandingPage = cfserver + "/cia/protected.do";
var fpProcessLandingPage1=cfserver + "/cia/home.do";
var fpProcessLandingPage2=cfserver + "/cia/protected.do";
var preview = "false";

///////////////////////BEGIN PASSPORT FUNCTION SECTION//////////////////////////////
function newUser() {
	var hppurl;
    var language = getLanguage();
	var country = getCountry();
	
	if( language_support == 'y' || language_support == 'Y') {
        hppurl = cfserver + "/hppcf/createuser.do?lang=" +
        	language + "&cc=" + country + "&hpappid=" + application_id + "&preview=" + preview + "&applandingpage=" +
        escape (fpProcessLandingPage1)+ "&applandingpage2=" + escape (fpProcessLandingPage2);
    }
    else {
        hppurl = cfserver + "/hppcf/createuser.do?lang=en&cc=US&hpappid=" +
        	application_id + "&preview=" + preview + "&applandingpage=" + escape( fpProcessLandingPage1 )+
        	"&applandingpage2=" + escape (fpProcessLandingPage2);
    }

    location.href = hppurl ;
}

function EditProfile() {
	var hppurl;
    var language = getLanguage();
	var country = getCountry();
	
	if( language_support == 'y' || language_support == 'Y') {
        hppurl = cfserver + "/hppcf/modifyuser.do?lang=" +
        	language + "&cc=" + country + "&hpappid=" + application_id + "&preview=" + preview + "&applandingpage=" +
        escape (fpProcessLandingPage1)+ "&applandingpage2=" + escape (fpProcessLandingPage2);
    }
    else {
        hppurl = cfserver + "/hppcf/modifyuser.do?lang=en&cc=US&hpappid=" +
        	application_id + "&preview=" + preview + "&applandingpage=" + escape( fpProcessLandingPage1 )+
        	"&applandingpage2=" + escape (fpProcessLandingPage2);
    }

    location.href = hppurl ;
}

function forgotPassword() {
    var hppurl;
    var language = getLanguage();
	var country = getCountry();
	
    if( language_support == 'y' || language_support == 'Y') {
        hppurl = cfserver + "/hppcf/forgotpwd.do?lang=" +
        	language + "&cc=" + country + "&hpappid=" + application_id + "&preview=" + preview + "&applandingpage=" +
        escape (fpProcessLandingPage1)+ "&applandingpage2=" + escape (fpProcessLandingPage2);
    }
    else {
        hppurl = cfserver + "/hppcf/forgotpwd.do?lang=en&cc=US&hpappid=" +
        	application_id + "&preview=" + preview + "&applandingpage=" + escape( fpProcessLandingPage1 )+
        	"&applandingpage2=" + escape (fpProcessLandingPage2);
    }

    location.href = hppurl ;
}

function recoverUserId() {
    var hppurl;

    var language = getLanguage();
    var country = getCountry();

    if( language_support == 'y' || language_support == 'Y') {
        hppurl = cfserver + "/hppcf/forgotuserid.do?lang=" +
        	language + "&cc=" + country + "&hpappid=" + application_id + "&preview=" + preview + "&applandingpage=" +
        escape( applandingpage );
    }
    else {
        hppurl = cfserver + "/hppcf/forgotuserid.do?lang=en&cc=US&hpappid=" +
        	application_id + "&preview=" + preview + "&applandingpage=" + escape( applandingpage );
    }
    location.href =  hppurl ;
}

function changeUserId() {
    var language = getLanguage();
    var country = getCountry();
    var hppurl;

    if( language_support == 'y' || language_support == 'Y') {
        hppurl = cfserver + "/hppcf/changeuserid.do?lang=" + language;
        hppurl = hppurl + "&cc=" + country + "&hpappid=" + application_id + 
        	"&preview=" + preview + "&applandingpage=" + escape( applandingpage );
    }

    else {
        hppurl = cfserver + "/hppcf/changeuserid.do?lang=en&cc=US";
        hppurl = hppurl + "&hpappid=" + application_id + 
        	"&preview=" + preview + "&applandingpage=" + escape( applandingpage );
    }
    location.href =  hppurl;
}

function changePassword() {
    var hppurl;
    var language = getLanguage();
    var country = getCountry();

    if( language_support == 'y' || language_support == 'Y') {
        hppurl = cfserver + "/hppcf/changepwd.do?lang=" + language;
        hppurl = hppurl + "&cc=" + country + "&hpappid=" + application_id + "&preview=" + preview + 
        "&applandingpage=" + escape( applandingpage );
    }
    else {
        hppurl = cfserver + "/hppcf/changepwd.do?lang=en&cc=US";
        hppurl = hppurl + "&hpappid=" + application_id + "&preview=" + preview + 
        "&applandingpage=" + escape( applandingpage );
    }
    location.href = hppurl;

}

function expiredPassword() {
    var hppurl;
    var language = getLanguage();
    var country = getCountry();

    if( language_support == 'y' || language_support == 'Y') {
        hppurl = cfserver + "/hppcf/expiredpwd.do?lang=" + language;
        hppurl = hppurl + "&cc=" + country + "&hpappid=" + application_id + 
        "&preview=" + preview + "&applandingpage=" + escape( applandingpage );
    }
    else {
        hppurl = cfserver + "/hppcf/expiredpwd.do?lang=en&cc=US";
        hppurl = hppurl + "&hpappid=" + application_id + "&preview=" + preview + 
        "&applandingpage=" + escape( applandingpage );
    }
    location.href = hppurl;

}

function forcedChangePassword() {
    var hppurl;
    var language = getLanguage();
    var country = getCountry();

    if( language_support == 'y' || language_support == 'Y') {
        hppurl = cfserver + "/hppcf/forcedchangepwd.do?lang=" + language;
        hppurl = hppurl + "&cc=" + country + "&hpappid=" + application_id + 
        "&preview=" + preview + "&applandingpage=" + escape( applandingpage );
    }
    else {
        hppurl = cfserver + "/hppcf/forcedchangepwd.do?lang=en&cc=US";
        hppurl = hppurl + "&hpappid=" + application_id + 
        "&preview=" + preview + "&applandingpage=" + escape( applandingpage );
    }
    location.href = hppurl;

}

function changeSecurityQA() {
    var hppurl;
    var language = getLanguage();
    var country = getCountry();

    if( language_support == 'y' || language_support == 'Y') {
        hppurl = cfserver + "/hppcf/collectsecurityquestionanswers.do?lang=" + language;
        hppurl = hppurl + "&cc=" + country + "&hpappid=" + application_id + 
        "&preview=" + preview + "&applandingpage=" + escape( applandingpage );
    }
    else {
        hppurl = cfserver + "/hppcf/collectsecurityquestionanswers.do?lang=en&cc=US";
        hppurl = hppurl + "&hpappid=" + application_id + 
        "&preview=" + preview + "&applandingpage=" + escape( applandingpage );
    }
    location.href = hppurl;

}

function signIn() {
	var current_href;
    var language = getLanguage();
    var country = getCountry();	
	signIn_href = cfserver + "/hppcf/dologin.do?hpappid=" + 
	               application_id + "&lang=" + language + "&cc=" + country + 
	               "&preview=" + preview + "&applandingpage=" + escape( applandingpage );
	location.href = signIn_href;
}

function signOut() {
    var addresslogout;
    addresslogout = cfserver + "/hppcf/logout.do?hpappid=" + application_id + 
    "&preview=" + preview + "&applandingpage=" + escape( default_url );
    location.href = addresslogout;
}

function activateAccount(guid, uid, email, secLevel) {

	var language = "";
	var country = "";

    if( language_support == 'y' || language_support == 'Y') {
        language = getLanguage();
        country = getCountry();	    
    }
    else {
        language = "en";
        country = "US";
    }    

    link = cfserver + "/hppcf/activateacct.do?guid=" +
	       guid + "&userid=" + 
	       uid + "&s_level=" + 
	       secLevel + "&email=" +
	       email + "&applandingpage=" +
	       escape(applandingpage) + "&preview=" + preview + 
	       "&lang=" + language + "&cc=" + country +
	       "&hpappid=" + application_id;
    location.href = link;
}

function adminResetPassword(guid, uid, email, secLevel) {
	var language = "";
	var country = "";

    if( language_support == 'y' || language_support == 'Y') {
        language = getLanguage();
        country = getCountry();	    
    }
    else {
        language = "en";
        country = "US";
    }
        
    link = cfserver + "/hppcf/resetpwdadmin.do?guid=" +
           guid + "&userid=" + 
           uid + "&s_level=" + 
           secLevel + "&email=" +
           email + "&hpappid=" + application_id + "&applandingpage=" +
           escape(applandingpage) + "&preview=" + preview + 
           "&lang=" + language + "&cc=" + country;
    location.href = link;
}

///////////////////////END PASSPORT FUNCTION SECTION//////////////////////////////

function trim(strtext) {
   // this will get rid of leading spaces
     while (strtext.substring(0,1) == ' ')
         strtext = strtext.substring(1, strtext.length);

     // this will get rid of trailing spaces
     while (strtext.substring(strtext.length-1,strtext.length) == ' ')
         strtext = strtext.substring(0, strtext.length-1);

    return strtext;
}

function decode(encstr) {

var base64s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
  var bits, decout = '', i = 0;
  for(; i<encstr.length; i += 4){
    bits =
     (base64s.indexOf(encstr.charAt(i))    & 0xff) <<18 |
     (base64s.indexOf(encstr.charAt(i +1)) & 0xff) <<12 |
     (base64s.indexOf(encstr.charAt(i +2)) & 0xff) << 6 |
      base64s.indexOf(encstr.charAt(i +3)) & 0xff;
    decout += String.fromCharCode(
     (bits & 0xff0000) >>16, (bits & 0xff00) >>8, bits & 0xff);
    }
  if(encstr.charCodeAt(i -2) == 61)
    return decout.substring(0, decout.length -2);
  else if(encstr.charCodeAt(i -1) == 61)
    return decout.substring(0, decout.length -1);
  else return decout;
}

function utf8to16(str) {
    var out, i, len, c;
    var char2, char3;

    out = "";
    len = str.length;
    i = 0;
    while(i < len) {
	c = str.charCodeAt(i++);
	switch(c >> 4)
	{
	  case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
	    // 0xxxxxxx
	    out += str.charAt(i-1);
	    break;
	  case 12: case 13:
	    // 110x xxxx   10xx xxxx
	    char2 = str.charCodeAt(i++);
	    out += String.fromCharCode(((c & 0x1F) << 6) | (char2 & 0x3F));
	    break;
	  case 14:
	    // 1110 xxxx  10xx xxxx  10xx xxxx
	    char2 = str.charCodeAt(i++);
	    char3 = str.charCodeAt(i++);
	    out += String.fromCharCode(((c & 0x0F) << 12) |
					   ((char2 & 0x3F) << 6) |
					   ((char3 & 0x3F) << 0));
	    break;
	}
    }

    return out;
}

function getLanguage() {
    var lang = "";
    lang = Get_Cookie(sLang);
    return lang;
}

function getCountry() {
    var cnt = "";
    cnt = Get_Cookie(sCountry);
    return cnt;
}

function Get_Cookie( name ) {
    var cookie_found = false;
    var start;
    var len;
    start = document.cookie.indexOf( "; " + name + "=" );
    if (start == -1) {
	    start = document.cookie.indexOf( name + "=" );
		if (start == 0) {
		    cookie_found = true;
		}
	    len = start + name.length + 1;
	}
	else {
	    cookie_found = true;
	    len = start + name.length + 3;
	}
	if ( ! cookie_found ) return "";
	var end = document.cookie.indexOf( ";", len );
	if ( end == -1 ) end = document.cookie.length;
	return unescape( document.cookie.substring( len, end ) );
}


function getCookieValue(cookiename) {
    var allcookies;
    var cl_start;
    var cl_end;
    allcookies = document.cookie;
    cl_start = allcookies.indexOf( cookiename + "=" );

    if (cl_start != -1) {
        cl_start = cl_start + cookiename.length + 1;
        cl_end = allcookies.indexOf(";", cl_start);
        if (cl_end == -1) cl_end=allcookies.length;
        clvalue = allcookies.substring(cl_start, cl_end);
                if ( (clvalue.substring(clvalue.length, clvalue.length -2) == "?=") && (clvalue.substring(0,2) == "=?")  )
                   clvalue = clvalue.substring( 10, clvalue.length - 2 );
                else
        clvalue = clvalue.substring( 9, clvalue.length );
        clvalue = unescape( decode( clvalue ) );
        return clvalue;
    }
    else {
        return "";
    }
}

function loggedIn() {
	var allcookies = document.cookie;
	var cookiestart = allcookies.indexOf("SMSESSION");
	if (cookiestart != -1) {
		var cookieend = allcookies.indexOf(";", cookiestart);
		if (cookieend == -1) cookieend=allcookies.length;
		var value = allcookies.substring(cookiestart+10, cookieend);
		if (value == "LOGGEDOFF") {
			return false;
		}
        else {
            var next_cookie = allcookies.indexOf( cookie_name );
            if (next_cookie != -1) {
                return true;
            }
            else {
                return false;
            }
		}
	}
    else {
		return false;
	}

}

function getUserName() {
    var message = getCookieValue(cookie_name);
    var index = message.indexOf("|");
    var key = message.substring(0,index);
    message = message.substring(index + 1, message.length);
    message = des (key, hextostring(message),0);
    message = trim(message);
    if( message.length > 30 )
        message = message.substring(0,30) + "...";
    var smlocale = getLanguage();
    if(smlocale.toUpperCase() == "JA") {
        message = message + " " + utf8to16(decode(strSan));
    }
    message = message.replace(/</g,"&lt;");
    return message;
}

///////////////////////////////////////////////////////////////////////////////////////
//paul tero, july 2001
//http://www.shopable.co.uk/des.html
//
//optimised for performance with large blocks by michael hayworth, november 2001
//http://www.netdealing.com
//
//this software is provided "as is" and
//any express or implied warranties, including, but not limited to, the
//implied warranties of merchantability and fitness for a particular purpose
//are disclaimed.  in no event shall the author or contributors be liable
//for any direct, indirect, incidental, special, exemplary, or consequential
//damages (including, but not limited to, procurement of substitute goods
//or services; loss of use, data, or profits; or business interruption)
//however caused and on any theory of liability, whether in contract, strict
//liability, or tort (including negligence or otherwise) arising in any way
//out of the use of this software, even if advised of the possibility of
//such damage.

//des
//this takes the key, the message, and whether to encrypt or decrypt
//encrypt boolean value
//mode = 1 or 2
function des (key, message, encrypt, mode, iv) {
  //declaring this locally speeds things up a bit
  var spfunction1 = new Array (0x1010400,0,0x10000,0x1010404,0x1010004,0x10404,0x4,0x10000,0x400,0x1010400,0x1010404,0x400,0x1000404,0x1010004,0x1000000,0x4,0x404,0x1000400,0x1000400,0x10400,0x10400,0x1010000,0x1010000,0x1000404,0x10004,0x1000004,0x1000004,0x10004,0,0x404,0x10404,0x1000000,0x10000,0x1010404,0x4,0x1010000,0x1010400,0x1000000,0x1000000,0x400,0x1010004,0x10000,0x10400,0x1000004,0x400,0x4,0x1000404,0x10404,0x1010404,0x10004,0x1010000,0x1000404,0x1000004,0x404,0x10404,0x1010400,0x404,0x1000400,0x1000400,0,0x10004,0x10400,0,0x1010004);
  var spfunction2 = new Array (-0x7fef7fe0,-0x7fff8000,0x8000,0x108020,0x100000,0x20,-0x7fefffe0,-0x7fff7fe0,-0x7fffffe0,-0x7fef7fe0,-0x7fef8000,-0x80000000,-0x7fff8000,0x100000,0x20,-0x7fefffe0,0x108000,0x100020,-0x7fff7fe0,0,-0x80000000,0x8000,0x108020,-0x7ff00000,0x100020,-0x7fffffe0,0,0x108000,0x8020,-0x7fef8000,-0x7ff00000,0x8020,0,0x108020,-0x7fefffe0,0x100000,-0x7fff7fe0,-0x7ff00000,-0x7fef8000,0x8000,-0x7ff00000,-0x7fff8000,0x20,-0x7fef7fe0,0x108020,0x20,0x8000,-0x80000000,0x8020,-0x7fef8000,0x100000,-0x7fffffe0,0x100020,-0x7fff7fe0,-0x7fffffe0,0x100020,0x108000,0,-0x7fff8000,0x8020,-0x80000000,-0x7fefffe0,-0x7fef7fe0,0x108000);
  var spfunction3 = new Array (0x208,0x8020200,0,0x8020008,0x8000200,0,0x20208,0x8000200,0x20008,0x8000008,0x8000008,0x20000,0x8020208,0x20008,0x8020000,0x208,0x8000000,0x8,0x8020200,0x200,0x20200,0x8020000,0x8020008,0x20208,0x8000208,0x20200,0x20000,0x8000208,0x8,0x8020208,0x200,0x8000000,0x8020200,0x8000000,0x20008,0x208,0x20000,0x8020200,0x8000200,0,0x200,0x20008,0x8020208,0x8000200,0x8000008,0x200,0,0x8020008,0x8000208,0x20000,0x8000000,0x8020208,0x8,0x20208,0x20200,0x8000008,0x8020000,0x8000208,0x208,0x8020000,0x20208,0x8,0x8020008,0x20200);
  var spfunction4 = new Array (0x802001,0x2081,0x2081,0x80,0x802080,0x800081,0x800001,0x2001,0,0x802000,0x802000,0x802081,0x81,0,0x800080,0x800001,0x1,0x2000,0x800000,0x802001,0x80,0x800000,0x2001,0x2080,0x800081,0x1,0x2080,0x800080,0x2000,0x802080,0x802081,0x81,0x800080,0x800001,0x802000,0x802081,0x81,0,0,0x802000,0x2080,0x800080,0x800081,0x1,0x802001,0x2081,0x2081,0x80,0x802081,0x81,0x1,0x2000,0x800001,0x2001,0x802080,0x800081,0x2001,0x2080,0x800000,0x802001,0x80,0x800000,0x2000,0x802080);
  var spfunction5 = new Array (0x100,0x2080100,0x2080000,0x42000100,0x80000,0x100,0x40000000,0x2080000,0x40080100,0x80000,0x2000100,0x40080100,0x42000100,0x42080000,0x80100,0x40000000,0x2000000,0x40080000,0x40080000,0,0x40000100,0x42080100,0x42080100,0x2000100,0x42080000,0x40000100,0,0x42000000,0x2080100,0x2000000,0x42000000,0x80100,0x80000,0x42000100,0x100,0x2000000,0x40000000,0x2080000,0x42000100,0x40080100,0x2000100,0x40000000,0x42080000,0x2080100,0x40080100,0x100,0x2000000,0x42080000,0x42080100,0x80100,0x42000000,0x42080100,0x2080000,0,0x40080000,0x42000000,0x80100,0x2000100,0x40000100,0x80000,0,0x40080000,0x2080100,0x40000100);
  var spfunction6 = new Array (0x20000010,0x20400000,0x4000,0x20404010,0x20400000,0x10,0x20404010,0x400000,0x20004000,0x404010,0x400000,0x20000010,0x400010,0x20004000,0x20000000,0x4010,0,0x400010,0x20004010,0x4000,0x404000,0x20004010,0x10,0x20400010,0x20400010,0,0x404010,0x20404000,0x4010,0x404000,0x20404000,0x20000000,0x20004000,0x10,0x20400010,0x404000,0x20404010,0x400000,0x4010,0x20000010,0x400000,0x20004000,0x20000000,0x4010,0x20000010,0x20404010,0x404000,0x20400000,0x404010,0x20404000,0,0x20400010,0x10,0x4000,0x20400000,0x404010,0x4000,0x400010,0x20004010,0,0x20404000,0x20000000,0x400010,0x20004010);
  var spfunction7 = new Array (0x200000,0x4200002,0x4000802,0,0x800,0x4000802,0x200802,0x4200800,0x4200802,0x200000,0,0x4000002,0x2,0x4000000,0x4200002,0x802,0x4000800,0x200802,0x200002,0x4000800,0x4000002,0x4200000,0x4200800,0x200002,0x4200000,0x800,0x802,0x4200802,0x200800,0x2,0x4000000,0x200800,0x4000000,0x200800,0x200000,0x4000802,0x4000802,0x4200002,0x4200002,0x2,0x200002,0x4000000,0x4000800,0x200000,0x4200800,0x802,0x200802,0x4200800,0x802,0x4000002,0x4200802,0x4200000,0x200800,0,0x2,0x4200802,0,0x200802,0x4200000,0x800,0x4000002,0x4000800,0x800,0x200002);
  var spfunction8 = new Array (0x10001040,0x1000,0x40000,0x10041040,0x10000000,0x10001040,0x40,0x10000000,0x40040,0x10040000,0x10041040,0x41000,0x10041000,0x41040,0x1000,0x40,0x10040000,0x10000040,0x10001000,0x1040,0x41000,0x40040,0x10040040,0x10041000,0x1040,0,0,0x10040040,0x10000040,0x10001000,0x41040,0x40000,0x41040,0x40000,0x10041000,0x1000,0x40,0x10040040,0x1000,0x41040,0x10001000,0x40,0x10000040,0x10040000,0x10040040,0x10000000,0x40000,0x10001040,0,0x10041040,0x40040,0x10000040,0x10040000,0x10001000,0x10001040,0,0x10041040,0x41000,0x41000,0x1040,0x1040,0x40040,0x10000000,0x10041000);

  //create the 16 or 48 subkeys we will need
  var keys = des_createkeys (key);
  var m=0, i, j, temp, temp2, right1, right2, left, right, looping;
  var cbcleft, cbcleft2, cbcright, cbcright2
  var endloop, loopinc;
  var len = message.length;
  var chunk = 0;
  //set up the loops for single and triple des
  var iterations = keys.length == 32 ? 3 : 9; //single or triple des
  if (iterations == 3) {looping = encrypt ? new Array (0, 32, 2) : new Array (30, -2, -2);}
  else {looping = encrypt ? new Array (0, 32, 2, 62, 30, -2, 64, 96, 2) : new Array (94, 62, -2, 32, 64, 2, 30, -2, -2);}

  message += "\0\0\0\0\0\0\0\0"; //pad the message out with null bytes
  //store the result here
  result = "";
  tempresult = "";

  if (mode == 1) { //cbc mode
    cbcleft = (iv.charCodeAt(m++) << 24) | (iv.charCodeAt(m++) << 16) | (iv.charCodeAt(m++) << 8) | iv.charCodeAt(m++);
    cbcright = (iv.charCodeAt(m++) << 24) | (iv.charCodeAt(m++) << 16) | (iv.charCodeAt(m++) << 8) | iv.charCodeAt(m++);
    m=0;
  }

  //loop through each 64 bit chunk of the message
  while (m < len) {
    left = (message.charCodeAt(m++) << 24) | (message.charCodeAt(m++) << 16) | (message.charCodeAt(m++) << 8) | message.charCodeAt(m++);
    right = (message.charCodeAt(m++) << 24) | (message.charCodeAt(m++) << 16) | (message.charCodeAt(m++) << 8) | message.charCodeAt(m++);

    //for cipher block chaining mode, xor the message with the previous result
    if (mode == 1) {if (encrypt) {left ^= cbcleft; right ^= cbcright;} else {cbcleft2 = cbcleft; cbcright2 = cbcright; cbcleft = left; cbcright = right;}}

    //first each 64 but chunk of the message must be permuted according to ip
    temp = ((left >>> 4) ^ right) & 0x0f0f0f0f; right ^= temp; left ^= (temp << 4);
    temp = ((left >>> 16) ^ right) & 0x0000ffff; right ^= temp; left ^= (temp << 16);
    temp = ((right >>> 2) ^ left) & 0x33333333; left ^= temp; right ^= (temp << 2);
    temp = ((right >>> 8) ^ left) & 0x00ff00ff; left ^= temp; right ^= (temp << 8);
    temp = ((left >>> 1) ^ right) & 0x55555555; right ^= temp; left ^= (temp << 1);

    left = ((left << 1) | (left >>> 31));
    right = ((right << 1) | (right >>> 31));

    //do this either 1 or 3 times for each chunk of the message
    for (j=0; j<iterations; j+=3) {
      endloop = looping[j+1];
      loopinc = looping[j+2];
      //now go through and perform the encryption or decryption
      for (i=looping[j]; i!=endloop; i+=loopinc) { //for efficiency
        right1 = right ^ keys[i];
        right2 = ((right >>> 4) | (right << 28)) ^ keys[i+1];
        //the result is attained by passing these bytes through the s selection functions
        temp = left;
        left = right;
        right = temp ^ (spfunction2[(right1 >>> 24) & 0x3f] | spfunction4[(right1 >>> 16) & 0x3f]
              | spfunction6[(right1 >>>  8) & 0x3f] | spfunction8[right1 & 0x3f]
              | spfunction1[(right2 >>> 24) & 0x3f] | spfunction3[(right2 >>> 16) & 0x3f]
              | spfunction5[(right2 >>>  8) & 0x3f] | spfunction7[right2 & 0x3f]);
      }
      temp = left; left = right; right = temp; //unreverse left and right
    } //for either 1 or 3 iterations

    //move then each one bit to the right
    left = ((left >>> 1) | (left << 31));
    right = ((right >>> 1) | (right << 31));

    //now perform ip-1, which is ip in the opposite direction
    temp = ((left >>> 1) ^ right) & 0x55555555; right ^= temp; left ^= (temp << 1);
    temp = ((right >>> 8) ^ left) & 0x00ff00ff; left ^= temp; right ^= (temp << 8);
    temp = ((right >>> 2) ^ left) & 0x33333333; left ^= temp; right ^= (temp << 2);
    temp = ((left >>> 16) ^ right) & 0x0000ffff; right ^= temp; left ^= (temp << 16);
    temp = ((left >>> 4) ^ right) & 0x0f0f0f0f; right ^= temp; left ^= (temp << 4);

    //for cipher block chaining mode, xor the message with the previous result
    if (mode == 1) {if (encrypt) {cbcleft = left; cbcright = right;} else {left ^= cbcleft2; right ^= cbcright2;}}
    tempresult += String.fromCharCode ((left>>>24), ((left>>>16) & 0xff), ((left>>>8) & 0xff), (left & 0xff), (right>>>24), ((right>>>16) & 0xff), ((right>>>8) & 0xff), (right & 0xff));

    chunk += 8;
    if (chunk == 512) {result += tempresult; tempresult = ""; chunk = 0;}
  } //for every 8 characters, or 64 bits in the message

  //return the result as an Array
  return result + tempresult;
} //end of des



//des_createkeys
//this takes as input a 64 bit key (even though only 56 bits are used)
//as an Array of 2 integers, and returns 16 48 bit keys
function des_createkeys (key) {
  //declaring this locally speeds things up a bit
  pc2bytes0  = new Array (0,0x4,0x20000000,0x20000004,0x10000,0x10004,0x20010000,0x20010004,0x200,0x204,0x20000200,0x20000204,0x10200,0x10204,0x20010200,0x20010204);
  pc2bytes1  = new Array (0,0x1,0x100000,0x100001,0x4000000,0x4000001,0x4100000,0x4100001,0x100,0x101,0x100100,0x100101,0x4000100,0x4000101,0x4100100,0x4100101);
  pc2bytes2  = new Array (0,0x8,0x800,0x808,0x1000000,0x1000008,0x1000800,0x1000808,0,0x8,0x800,0x808,0x1000000,0x1000008,0x1000800,0x1000808);
  pc2bytes3  = new Array (0,0x200000,0x8000000,0x8200000,0x2000,0x202000,0x8002000,0x8202000,0x20000,0x220000,0x8020000,0x8220000,0x22000,0x222000,0x8022000,0x8222000);
  pc2bytes4  = new Array (0,0x40000,0x10,0x40010,0,0x40000,0x10,0x40010,0x1000,0x41000,0x1010,0x41010,0x1000,0x41000,0x1010,0x41010);
  pc2bytes5  = new Array (0,0x400,0x20,0x420,0,0x400,0x20,0x420,0x2000000,0x2000400,0x2000020,0x2000420,0x2000000,0x2000400,0x2000020,0x2000420);
  pc2bytes6  = new Array (0,0x10000000,0x80000,0x10080000,0x2,0x10000002,0x80002,0x10080002,0,0x10000000,0x80000,0x10080000,0x2,0x10000002,0x80002,0x10080002);
  pc2bytes7  = new Array (0,0x10000,0x800,0x10800,0x20000000,0x20010000,0x20000800,0x20010800,0x20000,0x30000,0x20800,0x30800,0x20020000,0x20030000,0x20020800,0x20030800);
  pc2bytes8  = new Array (0,0x40000,0,0x40000,0x2,0x40002,0x2,0x40002,0x2000000,0x2040000,0x2000000,0x2040000,0x2000002,0x2040002,0x2000002,0x2040002);
  pc2bytes9  = new Array (0,0x10000000,0x8,0x10000008,0,0x10000000,0x8,0x10000008,0x400,0x10000400,0x408,0x10000408,0x400,0x10000400,0x408,0x10000408);
  pc2bytes10 = new Array (0,0x20,0,0x20,0x100000,0x100020,0x100000,0x100020,0x2000,0x2020,0x2000,0x2020,0x102000,0x102020,0x102000,0x102020);
  pc2bytes11 = new Array (0,0x1000000,0x200,0x1000200,0x200000,0x1200000,0x200200,0x1200200,0x4000000,0x5000000,0x4000200,0x5000200,0x4200000,0x5200000,0x4200200,0x5200200);
  pc2bytes12 = new Array (0,0x1000,0x8000000,0x8001000,0x80000,0x81000,0x8080000,0x8081000,0x10,0x1010,0x8000010,0x8001010,0x80010,0x81010,0x8080010,0x8081010);
  pc2bytes13 = new Array (0,0x4,0x100,0x104,0,0x4,0x100,0x104,0x1,0x5,0x101,0x105,0x1,0x5,0x101,0x105);

  //how many iterations (1 for des, 3 for triple des)
  var iterations = key.length >= 24 ? 3 : 1;
  //stores the return keys
  var keys = new Array (32 * iterations);
  //now define the left shifts which need to be done
  var shifts = new Array (0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0);
  //other variables
  var lefttemp, righttemp, m=0, n=0, temp;

  for (var j=0; j<iterations; j++) { //either 1 or 3 iterations
    left = (key.charCodeAt(m++) << 24) | (key.charCodeAt(m++) << 16) | (key.charCodeAt(m++) << 8) | key.charCodeAt(m++);
    right = (key.charCodeAt(m++) << 24) | (key.charCodeAt(m++) << 16) | (key.charCodeAt(m++) << 8) | key.charCodeAt(m++);

    temp = ((left >>> 4) ^ right) & 0x0f0f0f0f; right ^= temp; left ^= (temp << 4);
    temp = ((right >>> -16) ^ left) & 0x0000ffff; left ^= temp; right ^= (temp << -16);
    temp = ((left >>> 2) ^ right) & 0x33333333; right ^= temp; left ^= (temp << 2);
    temp = ((right >>> -16) ^ left) & 0x0000ffff; left ^= temp; right ^= (temp << -16);
    temp = ((left >>> 1) ^ right) & 0x55555555; right ^= temp; left ^= (temp << 1);
    temp = ((right >>> 8) ^ left) & 0x00ff00ff; left ^= temp; right ^= (temp << 8);
    temp = ((left >>> 1) ^ right) & 0x55555555; right ^= temp; left ^= (temp << 1);

    //the right side needs to be shifted and to get the last four bits of the left side
    temp = (left << 8) | ((right >>> 20) & 0x000000f0);
    //left needs to be put upside down
    left = (right << 24) | ((right << 8) & 0xff0000) | ((right >>> 8) & 0xff00) | ((right >>> 24) & 0xf0);
    right = temp;

    //now go through and perform these shifts on the left and right keys
    for (i=0; i < shifts.length; i++) {
      //shift the keys either one or two bits to the left
      if (shifts[i]) {left = (left << 2) | (left >>> 26); right = (right << 2) | (right >>> 26);}
      else {left = (left << 1) | (left >>> 27); right = (right << 1) | (right >>> 27);}
      left &= -0xf; right &= -0xf;

      //now apply pc-2, in such a way that e is easier when encrypting or decrypting
      //this conversion will look like pc-2 except only the last 6 bits of each byte are used
      //rather than 48 consecutive bits and the order of lines will be according to
      //how the s selection functions will be applied: s2, s4, s6, s8, s1, s3, s5, s7
      lefttemp = pc2bytes0[left >>> 28] | pc2bytes1[(left >>> 24) & 0xf]
              | pc2bytes2[(left >>> 20) & 0xf] | pc2bytes3[(left >>> 16) & 0xf]
              | pc2bytes4[(left >>> 12) & 0xf] | pc2bytes5[(left >>> 8) & 0xf]
              | pc2bytes6[(left >>> 4) & 0xf];
      righttemp = pc2bytes7[right >>> 28] | pc2bytes8[(right >>> 24) & 0xf]
                | pc2bytes9[(right >>> 20) & 0xf] | pc2bytes10[(right >>> 16) & 0xf]
                | pc2bytes11[(right >>> 12) & 0xf] | pc2bytes12[(right >>> 8) & 0xf]
                | pc2bytes13[(right >>> 4) & 0xf];
      temp = ((righttemp >>> 16) ^ lefttemp) & 0x0000ffff;
      keys[n++] = lefttemp ^ temp; keys[n++] = righttemp ^ (temp << 16);
    }
  } //for each iterations
  //return the keys we've created
  return keys;
} //end of des_createkeys

function stringtohex (s) {
  var r = "0x";
  var hexes = new Array ("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f");
  for (var i=0; i<s.length; i++) {r += hexes [s.charCodeAt(i) >> 4] + hexes [s.charCodeAt(i) & 0xf];}
  return r;
}

function hextostring (h) {
  var r = "";
  for (var i= (h.substr(0, 2)=="0x")?2:0; i<h.length; i+=2) {r += String.fromCharCode (parseInt (h.substr (i, 2), 16));}
  return r;
}





