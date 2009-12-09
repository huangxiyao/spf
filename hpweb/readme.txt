********************************************************************************
readme.txt

Welcome to the WPA HPWeb Layout VAP Components for use in Vignette Application 
Portal (VAP) product.

Questions about this file or the software it is associated with should be
directed to help.wpa@hp.com.
********************************************************************************

CAR FILE
--------
HPWebComponents-<version>.car


DEPENDENCIES
------------
The WPA HPWeb Layout VAP Components runs on VAP product 7.4 and above.


FEATURES
--------
The WPA HPWeb Layout VAP Components provides the styles, grids, and theme that 
produces the HPWeb look-and-feel C-Frame for a page.


USAGE
-----
Usage information for the product can be found at:

http://intranet.hp.com/Sites/SOA/TechnicalDocs/WPAPortlet/Pages/hpweb_vap.aspx


INSTALLATION INSTRUCTIONS
-------------------------
1) Download the CAR file, and then use the Vignette Server Console to import 
the CAR file and its components.  Since the CAR contains Java classes,
you have to shutdown and restart the VAP portal application twice to have 
the classes be in effect.

2) Use the VAP Admin Console to share all of the  HPWeb components (styles, 
grids, theme) to the portal site where they will be used.

3) Use the VAP Admin console to assign the HPWeb Theme and the appropriate
HPWeb grid to the navigation items where you want to apply the HPWeb layout.


VERSION HISTORY
---------------

1.1.8 (2009/8/19):
	- Shrink vertical spacing between flyout menu items.
	- Fixed tab spacing so that tab text doesn't span two lines.
	
1.1.7 (2009/8/5):
	- Provide language-named properties files, Messages_xx.properties.
	  Renamed Messages_xx_XX.properties files to Message_xx.properties,
	  or Messages_xx_YY.properties to Messages_xx.properties where 
	  YY is the default country for the xx language (in hpweb-resources.jar).
	
1.1.6 (2009/7/30):
	- Use Empty Page Controls in hpweb theme.
	- Updated translation for the ko-KR privacy statement string 
	- Fixed stretch logo bar positioning with Japanese and Korean locales.
	- Fixed flyout menu items color and font size.
	- Shrinked tab menu item width.
	- Added underline decoration to help link when hovered.

1.1.5 (2009/7/1):

	- Displayed &raquo character in horizontal menu's selected tab.
	- Split out static css styles from hpweb_extensions_css.jsp to new file
	   hpweb_extensions.css for improved performance.
	- Fixed wrong zh_TW signout string.
	- Added compellation character to Japanese welcome string.
	
1.1.4 (2009/6/1):

	- Additional fix for IE6 missing right border in portlet bounding box.
	
1.1.3 (2009/5/28):

	- Modified code to not escape html strings from hpweb model object and 
	  component property files.
	- Modified code to display default portlet error message like Vignette 
	  portlet error message.
	- Fixed IE6 missing right border in portlet bounding box (dependency
	  	on Page Display component to specify "lastColumnPortlet" css class).
	- Fixed rtl display in horizontal navigation.
	- Included hpweb-resources.jar 4.4.1, containing new translation strings.
	- Minor tweaks in portlet chromes padding spacing.
		
1.1.2 (2009/5/11):

	- Fixed the following defects:
		- Default window title flashing briefly before configured title 
			is displayed.
		- Horizontal navigation button bar is not always showing on
		   some pages.
		- Horizontal tab is too wide.
		- Use https images in horizontal navigation with https request.
	
1.1.1 (2009/03/20):

	- Fixed defect with non-visible menu items showing in menu.
	- Added topMenuItems getter/setter method to HPWebModel class.
	- Specified initial jsr-286 portlet css class properties 
		(hpweb_portlet_css.jsp).

1.1.0 (2009/03/11):

	- Added three-level horizontal navigation consisting of primary 
		navigation of tabs, secondary navigation of buttons, and third
		level of flyout menus.
	- Added portlet chrome styles:
		- Introduction box, alpha level header, beta level header, beveled
		   beta level header, gamma level header, callout/chromeless
	- Added capability for developers to include layout config JSP file
		for js script and css styles into <head> section.
	- Added <meta content="target_country"> element based on locale.
	- Added <meta content="hp_design_version"> element.
	
1.0.1 (2009/01/21):

	- Added HP Layout Config Style Type and HPWeb Layout Config Style.
		- Removed hpweb_model.jsp layout configuration from grid styles.
	- Added HPWebModel locale property and default logic to get locale.
		- Added capability to set <html> language attribute value.
		- Added logic to set locale on HPWeb layout message bundle.
	- Added HPWebModel metaInfos property.
	
1.0.0 (2009/01/05):

	- Initial release.

