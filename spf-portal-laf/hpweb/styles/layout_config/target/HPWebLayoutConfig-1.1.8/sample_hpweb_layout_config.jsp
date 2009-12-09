<%-----------------------------------------------------------------------------
	sample_hpweb_layout_config.jsp
	
	This is a sample hpweb_layout_config.jsp to demonstrate how to customize 
	the HPWeb layout through the use of the HPWebModel request bean
	(com.hp.frameworks.wpa.portal.hpweb.model.HPWebModel class).

	See the HPWeb Layout VAP Components Usage Guide for information on 
	HPWeb layout customizations.
	
-----------------------------------------------------------------------------%>

<%@ taglib prefix="vgn-portal" uri="vgn-tags" %>

<jsp:directive.page import="java.util.List" />
<jsp:directive.page import="com.hp.frameworks.wpa.hpweb.MenuItem" />

<vgn-portal:defineObjects/>

<%-- Retrieve request-scoped HPWebModel bean to set layout properties --%>

<jsp:useBean id="HPWebModel" scope="request" 
		class="com.hp.frameworks.wpa.portal.hpweb.HPWebModel" />
		
<%-----------------------------------------------------------------------------
	Sample HPP Central Forms integration
-----------------------------------------------------------------------------%>

<jsp:scriptlet>

//Build username string from Vignette user object for 'welcome string'.

String firstName = (String) portalContext.getCurrentUser().getProperty("firstname");
String lastName = (String) portalContext.getCurrentUser().getProperty("lastname");
String displayName = "";
if (firstName != null && lastName != null)
	displayName = firstName + " " + lastName;
else if (firstName != null)
	displayName = firstName;
else if (lastName == null)
	displayName = lastName;

</jsp:scriptlet>

<jsp:setProperty name="HPWebModel" property="username" 
		value="<%= displayName %>" />
		
<%-- These HPP functions, e.g. newUser(), are defined in the sample_cfhook.js 
		which is included by the sample_hpweb_layout_config_head.jsp file. 
--%>

<jsp:setProperty name="HPWebModel" property="registerUrl" 
		value="javascript:newUser()" />
<jsp:setProperty name="HPWebModel" property="signInUrl" 
		value="javascript:signIn()" />
<jsp:setProperty name="HPWebModel" property="signOutUrl" 
		value="javascript:signOut()" />
<jsp:setProperty name="HPWebModel" property="profileUrl" 
		value="javascript:EditProfile()" />

<%-----------------------------------------------------------------------------
	Sample code to add <meta> element
-----------------------------------------------------------------------------%>

<jsp:scriptlet>

	Properties metaInfos = HPWebModel.getMetaInfos();
	metaInfos.setProperty("robots", "noindex,nofollow");
	
</jsp:scriptlet>

<%-----------------------------------------------------------------------------
	Sample Grid properties
-----------------------------------------------------------------------------%>

<jsp:setProperty name="HPWebModel" property="themeColor" value="#0066FF" />

<%-----------------------------------------------------------------------------
	Sample Account Controls properties
-----------------------------------------------------------------------------%>

<%--
<jsp:setProperty name="HPWebModel" property="localeSelector"
	value='<select name="locale"><option>US - English</option><option>Canada - English</option></select>' />
--%>

<%-----------------------------------------------------------------------------
	Sample Header properties
-----------------------------------------------------------------------------%>

<jsp:setProperty name="HPWebModel" property="tagline"
    value="For demonstration purposes only" />
<jsp:setProperty name="HPWebModel" property="cartText" 
		value="Shopping cart" />
<jsp:setProperty name="HPWebModel" property="cartUrl" 
		value="http://www.hp.com" />
<jsp:setProperty name="HPWebModel" property="cartItemText" 
		value="{0} items in cart" />
<jsp:setProperty name="HPWebModel" property="cartItemCount" 
		value="3" />
<jsp:setProperty name="HPWebModel" property="topPromotion" 
		value="This is a top promotion" />

<%-----------------------------------------------------------------------------
	Sample Footer properties
-----------------------------------------------------------------------------%>

<jsp:setProperty name="HPWebModel" property="feedbackUrl" 
		value="http://www.foo.com" />
<jsp:setProperty name="HPWebModel" property="feedbackText" 
		value="Feedback to webmaster" />

<jsp:scriptlet>

<%-----------------------------------------------------------------------------
	The following sample code shows how to create a sample left menu 
	programmatically.
	
	The vertical navigation menu can be created programmatically by the 
	application, or by default, created by the HPWeb Vertical Navigation 
	component.
-----------------------------------------------------------------------------%>

List leftMenu = null;
boolean createVertNav = false;

if (createVertNav) {

leftMenu = HPWebModel.getLeftMenuItems();

// Create heading 1, link 1.1, link 1.1.1 menu

MenuItem item1 = new MenuItem();
item1.setTitle("item 1");
leftMenu.add(item1);

MenuItem item1_1 = new MenuItem();
item1_1.setTitle("item 1.1");
item1_1.setUrl("http://www.foo.com");
item1.getSubMenuItems().add(item1_1);

MenuItem item1_2 = new MenuItem();
item1_2.setTitle("item 1.2");
item1_2.setUrl("http://www.foo.com");
item1.getSubMenuItems().add(item1_2);

// Separator

leftMenu.add(new MenuItem());

// Create item 2, link 2.1 and link 2.2, and (invisible) link 2.3 menu

MenuItem item2 = new MenuItem();
item2.setTitle("item 2");
item2.setUrl("http://www.foo.com");
leftMenu.add(item2);

MenuItem item2_1 = new MenuItem();
item2_1.setTitle("item 2.1");
item2_1.setUrl("http://www.foo.com");
item2.getSubMenuItems().add(item2_1);

MenuItem item2_2 = new MenuItem();
item2_2.setTitle("item 2.2");
item2_2.setUrl("http://www.foo.com");
item2.getSubMenuItems().add(item2_2);

MenuItem item2_3 = new MenuItem();
item2_3.setTitle("item 2.3");
item2_3.setUrl("http://www.foo.com");
item2_3.setVisible(false);
item2.getSubMenuItems().add(item2_3);

}

</jsp:scriptlet>

<%-----------------------------------------------------------------------------
	Sample Vertical Navigation properties
-----------------------------------------------------------------------------%>

<jsp:setProperty name="HPWebModel" property="leftMenuTitle" 
		value="Sample Site" />
<jsp:setProperty name="HPWebModel" property="leftMenuTitleUrl" 
		value="http://www.foo.com" />
<jsp:setProperty name="HPWebModel" property="leftPromotion" 
		value="This is my promotion" />

<%-----------------------------------------------------------------------------
	Sample code to generate breadcrumbs
-----------------------------------------------------------------------------%>

<%-- 
There are two ways to create breadcrumbs:
1) Set the following property to true, which will auto-generate the breadcrumbs
based on the currently selected page.
--%>

<jsp:setProperty name="HPWebModel" property="generateBreadcrumbs" 
		value="true" />

<%-- 
2) Create the List of breadcrumb MenuItem objects, to the 'breadcrumbsItems' 
HPWebModel property. 
--%>

<jsp:scriptlet>

List breadcrumbs = null;
boolean createBreadcrumbs = false;

if (createBreadcrumbs) {
	
	breadcrumbs = HPWebModel.getBreadcrumbItems();
	
	// Breadcrumb 1
	MenuItem item = new MenuItem();
	item.setTitle("Breadcrumb 1");
	item.setUrl("http://www.hp.com");
	breadcrumbs.add(item);

	// Breadcrumb 2
	item = new MenuItem();
	item.setTitle("Breadcrumb 2");
	item.setUrl("http://www.hp.com");
	breadcrumbs.add(item);
}

</jsp:scriptlet>
