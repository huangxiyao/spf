<%-----------------------------------------------------------------------------
	sample_cleansheet_layout_config.jsp
	
	This is a sample cleansheet_layout_config.jsp to demonstrate how to customize 
	the HP Clean sheet layout through the use of the HPCSModel request bean
	(com.hp.it.spf.portal.cleansheet.HPCSModel class).

	See the HP Cleansheet Layout VAP Components Usage Guide for information on 
	HP Cleansheet layout customizations.
	
-----------------------------------------------------------------------------%>

<%@ taglib prefix="vgn-portal" uri="vgn-tags" %>

<jsp:directive.page import="java.util.List" />
<jsp:directive.page import="com.hp.it.spf.portal.cleansheet.MenuItem" />

<vgn-portal:defineObjects/>

<%-- Retrieve request-scoped HPCSModel bean to set layout properties --%>

<jsp:useBean id="HPCSModel" scope="request" class="com.hp.it.spf.portal.cleansheet.HPCSModel" />
		
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

<jsp:setProperty name="HPCSModel" property="username" 
		value="<%= displayName %>" />
		
<%-- These HPP functions, e.g. newUser(), are defined in the sample_cfhook.js 
		which is included by the sample_hpweb_layout_config_head.jsp file. 
--%>

<jsp:setProperty name="HPCSModel" property="registerUrl" 
		value="javascript:newUser()" />
<jsp:setProperty name="HPCSModel" property="signInUrl" 
		value="javascript:signIn()" />
<jsp:setProperty name="HPCSModel" property="signOutUrl" 
		value="javascript:signOut()" />
<jsp:setProperty name="HPCSModel" property="profileUrl" 
		value="javascript:EditProfile()" />

<%-----------------------------------------------------------------------------
	Sample code to add <meta> element
-----------------------------------------------------------------------------%>

<jsp:scriptlet>

	Properties metaInfos = HPCSModel.getMetaInfos();
	metaInfos.setProperty("robots", "noindex,nofollow");
	
</jsp:scriptlet>

<%-----------------------------------------------------------------------------
	Sample Grid properties
-----------------------------------------------------------------------------%>

<jsp:setProperty name="HPCSModel" property="themeColor" value="#0066FF" />

<%-----------------------------------------------------------------------------
	Sample Account Controls properties
-----------------------------------------------------------------------------%>

<%--
<jsp:setProperty name="HPCSModel" property="localeSelector"
	value='<select name="locale"><option>US - English</option><option>Canada - English</option></select>' />
--%>

<%-----------------------------------------------------------------------------
	Sample Header properties
-----------------------------------------------------------------------------%>

<jsp:setProperty name="HPCSModel" property="tagline"
    value="For demonstration purposes only" />
<jsp:setProperty name="HPCSModel" property="cartText" 
		value="Shopping cart" />
<jsp:setProperty name="HPCSModel" property="cartUrl" 
		value="http://www.hp.com" />
<jsp:setProperty name="HPCSModel" property="cartItemText" 
		value="{0} items in cart" />
<jsp:setProperty name="HPCSModel" property="cartItemCount" 
		value="3" />
<jsp:setProperty name="HPCSModel" property="topPromotion" 
		value="This is a top promotion" />

<%-----------------------------------------------------------------------------
	Sample Footer properties
-----------------------------------------------------------------------------%>

<jsp:setProperty name="HPCSModel" property="feedbackUrl" 
		value="http://www.foo.com" />
<jsp:setProperty name="HPCSModel" property="feedbackText" 
		value="Feedback to webmaster" />


<%-----------------------------------------------------------------------------
	Sample code to generate breadcrumbs
-----------------------------------------------------------------------------%>

<%-- 
There are two ways to create breadcrumbs:
1) Set the following property to true, which will auto-generate the breadcrumbs
based on the currently selected page.
--%>

<jsp:setProperty name="HPCSModel" property="generateBreadcrumbs" 
		value="true" />

<%-- 
2) Create the List of breadcrumb MenuItem objects, to the 'breadcrumbsItems' 
HPCSModel property. 
--%>

<jsp:scriptlet>

List breadcrumbs = null;
boolean createBreadcrumbs = false;

if (createBreadcrumbs) {
	
	breadcrumbs = HPCSModel.getBreadcrumbItems();
	
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
