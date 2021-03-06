********************************************************************************
readme.txt

Welcome to the WPA @hp Layout VAP Components for use in Vignette Application 
Portal (VAP) product.

Questions about this file or the software it is associated with should be
directed to help.wpa@hp.com.
********************************************************************************

CAR PACKAGE
-----------
AtHPComponents-<version>.car


DEPENDENCIES
------------
The WPA @hp Layout VAP Components works on VAP product 7.4 and above.


FEATURES
--------
The WPA @hp Layout VAP Components provides the styles, grids,  and theme that 
produces the @hp look-and-feel C-Frame for a page.   The C-Frame styles
include the @hp header, @hp top navigation, @hp vertical navigation, and the
@hp footer.  The @hp portlet chrome style is also included.


USAGE
-----
Usage information for the product can be found at:

http://intranet.hp.com/Sites/SOA/TechnicalDocs/WPAPortlet/Pages/products.aspx


INSTALLATION INSTRUCTIONS
-------------------------
1) Download the CAR file, and then use the Vignette Portal console to import 
the CAR file and its components.  Since the CAR contains Java classes,
you have to shutdown and restart the VAP portal application twice to have 
the classes be in effect.

2) Use the VAP Admin Console to share all of the  @hp components (styles, 
grids, theme) to the portal site where they will be used.

3) Use the VAP Admin console to assign the @hp Theme and the appropriate
@hp grid to the navigation items where you want to apply the @hp layout.


VERSION HISTORY
---------------

1.1.2 (2009/07/20):

	- Added browser window title to layout configuration.
	- Added include of athp_layout_config_head.jsp to allow portals to add 
		code to <head> section.

1.1.1 (2009/01/21):

	- Added HP Layout Config Style Type and AtHP Layout Config Style.
		- Removed athp_model.jsp layout configuration from grid styles.

1.1.0 (2009/01/12):

	- Changed requirement to run on VAP 7.4 or later.
	- Changed programmatic configuration with AtHPModel class.
	- Added declarative configuration using component properties files.
	- Added ability to auto-generate left menu.
	- Added ability to auto-generate breadcrumbs and Generate Breadcrumbs
	  property.
	- New sample files: sample_athp_model.jsp and blank_athp_model.jsp.

1.0.2 (2007/11/06):

	- Changed atHPModel.metaInfos property to be of java.util.Properties object
		of name-value pairs (from ArrayList of MetaInfo objects).
	- Added atHPModel.bodyStyleDisabled property.
	- Changed atHPModel.verticalMenu to be of VerticalMenu object
		(instead of VerticalMenu.items() objects).
	- Breadcrumbs is automatically generated by default, so there is no
		customization code needed.

1.0.0 (2007/10/25):

	- Initial release.

