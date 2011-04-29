<%-----------------------------------------------------------------------------
	sample_athp_layout_config.jsp
	
	The file contains sample @hp layout customizations.
	
-----------------------------------------------------------------------------%>

<jsp:directive.page import="java.util.Properties" />
<jsp:directive.page import="java.util.List" />
<jsp:directive.page import="com.hp.frameworks.wpa.portal.athp.MenuItem" />

<%-- Retrieve request-scoped AtHPModel bean to set layout properties --%>

<jsp:useBean id="AtHPModel" scope="request" 
		class="com.hp.frameworks.wpa.portal.athp.AtHPModel" />

<jsp:scriptlet>

	// Sample additional MetaInfos customization
	
	Properties metaInfos = AtHPModel.getMetaInfos();
	metaInfos.setProperty("robots", "noindex,nofollow");
	
</jsp:scriptlet>

<jsp:setProperty name="AtHPModel" property="greetingMessage"
		value="Welcome, User" />
<jsp:setProperty name="AtHPModel" property="loginUrl"
		value="http://login.foo.com" />
<jsp:setProperty name="AtHPModel" property="logoutUrl"
		value="http://logout.foo.com" />
<jsp:setProperty name="AtHPModel" property="generateBreadcrumbs"
		value="true" />

<jsp:scriptlet>

	// Sample left menu creation and customization
	
	List leftMenu = null;
	
	// set if conditional to 'true' to see sample left menu creation; 
	// otherwise, athp components will create left menu from what's configured 
	// in Vignette.
			
	if (false) {
		leftMenu = AtHPModel.getLeftMenuItems();

		MenuItem menuItem = new MenuItem();

		// Add label
		leftMenu.add(new MenuItem("id1", "text item 1"));

		// Add spacer
		menuItem = new MenuItem();
		menuItem.setSpacer(true);
		leftMenu.add(menuItem);

		// Add link
		leftMenu.add(new MenuItem("id2", "hp support", "http://support.hp.com",
				"_new"));

		// Add link
		leftMenu.add(new MenuItem("id3", "hp shopping", "http://shopping.hp.com", 
				null));
		
		// Add separator
		menuItem = new MenuItem();
		menuItem.setSeparator(true);
		leftMenu.add(menuItem);
	
		// Create a menu item with a submenu of 2 items
		
		menuItem = new MenuItem("id4", "external sites");

		MenuItem subMenuItem = new MenuItem("id5", "espn", "http://espn.com",
				"_new");
		menuItem.getSubMenuItems().add(subMenuItem);

		subMenuItem = new MenuItem("id6", "cnn", "http://cnn.com",
				"_new");
		menuItem.getSubMenuItems().add(subMenuItem);
		
		leftMenu.add(menuItem);

	}

</jsp:scriptlet>

<jsp:setProperty name="AtHPModel" property="feedbackUrl"
		value="http://feedback.foo.com" />

