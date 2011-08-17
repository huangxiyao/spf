<%@ page import="com.epicentric.common.website.*,
				 java.util.*,
				 com.epicentric.metastore.MetaStoreFolder,
				 com.hp.sesame.navportlet.NavPortletBean,
				 com.epicentric.portalbeans.PortalPageContext,
				 com.epicentric.portalbeans.PortalBean,
  				 com.epicentric.template.Style,
				 com.epicentric.user.User,
				 com.hp.spp.portal.common.helper.LocalizationHelper,
				 com.hp.spp.portal.common.helper.ProfileHelper,
				 com.hp.spp.portal.common.helper.MenuItemHelper"
contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>

<%@taglib uri="module-tags" prefix="mod" %>
<%@taglib uri="vgn-tags" prefix="vgn-portal" %>
<%@page import="com.epicentric.navigation.MenuItem"%>

<vgn-portal:defineObjects/>
<mod:view class="com.epicentric.portalbeans.beans.jspbean.JSPView">

<%
NavPortletBean bean = (NavPortletBean) view.getBean();
boolean underline = false;
boolean thin = false;
boolean noHeader = false;
boolean linespacing = false;
boolean displayDescription = false;
boolean underlineLinks = false;

//Find out if the user session is in simulation mode.
boolean sessionInSimulation = new ProfileHelper().isSimulationMode(session);

int columns = 1;

String description = "";
String bgColorDescription = "";
String securityMessage1 = "";

String menuItem = bean.getMenuItem();
if (menuItem == null) menuItem = "";

String uline = bean.getUnderline();
if (uline == null) uline = "";
if (!uline.equals(""))  underline = true;

String uthin = bean.getThin();
if (uthin == null) uthin = "";
if (!uthin.equals(""))  thin = true;

String noHead = bean.getNoHeader();
if (noHead != null && noHead.length() > 0) {
	noHeader = true;
}

String underlineLinksBeanValue = bean.getUnderlineLinks();
if (underlineLinksBeanValue != null && underlineLinksBeanValue.length() > 0) {
	underlineLinks = true;
}

String lineSpace = bean.getLinespacing();
if (lineSpace == null) lineSpace = "";
if (!lineSpace.equals("")) linespacing = true;

String displayDesc = bean.getDisplayDescription();
if (displayDesc == null) displayDesc = "";
if (!displayDesc.equals("")) displayDescription = true;

String cols = bean.getColumns();
if (cols != null && cols.length() > 0) {
	columns = Integer.parseInt(cols);
}

String fontSize = bean.getFontSize();
String fontSizeStyle;
if (fontSize != null && fontSize.length() > 0)	{
	fontSizeStyle = "font-size: " + fontSize + "pt;";
}
else	{
	fontSizeStyle = "";
}

// set the style for the link section
String linkCellStyle = "padding-bottom:1px;padding-left:0px;text-align:left;vertical-align:top;" 
	+ fontSizeStyle;
String linkStyle = fontSizeStyle;
String textColor = bean.getTextColor();
if (textColor != null && textColor.length() > 0)	{
	String colorStyle = "color: #" + textColor;
	linkCellStyle += colorStyle;
	linkStyle += colorStyle;
}

String linkTableStyle;
String backgroundColor = bean.getBackgroundColor();
if (backgroundColor != null && backgroundColor.length() > 0)	{
	linkTableStyle = "background-color: #" + backgroundColor 
		+ ";border-style: solid; border-width: 4px; border-color: #" + backgroundColor + ";";
}
else	{
	linkTableStyle = "";
}

List allNodes = MenuItemUtils.getAllNodes(portalContext);
List children = new ArrayList();

MenuItemHelper menuItemHelper = new MenuItemHelper();

MenuItemNode thisNode = menuItemHelper.findMenuItemByName(portalContext, menuItem);


String color = MenuItemUtils.getSelectedMenuItemNode(portalContext).getColor();

if(linespacing)
	%><!-- linespace ok --><%
if(!linespacing)
	 %><!-- linespace not ok --><%

if (thisNode != null) {
	LocalizationHelper localizationHelper = new LocalizationHelper() ;
	
	children = menuItemHelper.getChildNodes(portalContext, thisNode);
	description = I18nUtils.getValue(thisNode.getID(), "description", "", session, request);
	description = localizationHelper.getValueNoSpan(description) ;
	bgColorDescription = I18nUtils.getValue(thisNode.getID(), "bgcolor", "", session, request);
	bgColorDescription = localizationHelper.getValueNoSpan(bgColorDescription) ;
	
	// set up the menu item name (not the displayed title) to display as comment to help find
	// the friendly URL of the menu item
	String thisNodeItemName = null;
	MenuItem thisNodeItem = thisNode.getMenuItem();
	if (thisNodeItem != null)
	{
		thisNodeItemName = thisNodeItem.getTitle();
	}
	
	securityMessage1 = I18nUtils.getValue("e52f774b23843eeee13c7ef0b528efa0", "securitymessage", "", session, request);
	
	if(securityMessage1 == null || securityMessage1.equalsIgnoreCase(""))	{
	    %><!--security message not ok--><%
	}
	else	{
	    %>
	    <vgn-portal:i18nElement>
	    <SCRIPT>
	    var securityMessage = "<vgn-portal:i18nValue stringID="e52f774b23843eeee13c7ef0b528efa0" key="securitymessage" defaultValue="" />";
	    </SCRIPT>
	    </vgn-portal:i18nElement>
	    <% 
	} 
	if (description==null) description="";
	
	if (underline) {%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td>
					<table border="0" cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<td bgcolor="#FFFFFF" style="height:4px;"><spacer type="block" width="1" height="1"></td>
						</tr>
						<tr>
							<td>
								<h2 class="bold" style="<%= fontSizeStyle %>"><%=thisNode.getTitle()%></h2>
								<!-- Nav-Item-Name: <%= thisNodeItemName %> -->
							</td>
						</tr>
						<tr>
							<td bgcolor="#<%=color%>" style="height:4px;"><spacer type="block" width="1" height="1"></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	<%} else if (thin) {%>
		<table>
			<tr>
				<td bgcolor="#FFFFFF" style="height:4px;"><spacer type="block" width="1" height="1"></td>
			</tr>
			<tr>
				<td>
					<h2 class="bold" style="<%= fontSizeStyle %>"><%=thisNode.getTitle()%></h2>
					<!-- Nav-Item-Name: <%= thisNodeItemName %> -->
				</td>
			</tr>
			<tr>
				<td bgcolor="#<%=color%>"	style="height:2px;"><spacer type="block" width="1" height="1"></td>
			</tr>
		</table>
	<%}
	// if neither thin nor underline and 'no header' has not been specified, then standard header
	else if (! noHeader) {%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr class="theme">
				<td style="padding-left:10px;padding-right:10px;padding-top:2px;padding-bottom:2px;<%= fontSizeStyle %>"><span class="themeheader"><%=thisNode.getTitle()%></span></td>
				<!-- Nav-Item-Name: <%= thisNodeItemName %> -->
			</tr>
		</table>
	<%}
	
	if (!description.equals("")) {%>
	
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
		
			<%if (bgColorDescription.equals("")) {%>
			<tr class="colorDCDCDCbg">
				<td style="padding-left:8px;padding-top:8px;text-align:left;vertical-align:bottom;"><%=description.replaceAll("\r\n", "<br>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")%></td>
			<%}else{ %>
			<tr class="<%=bgColorDescription%>">
				<td style="padding-left:1px;padding-top:8px;text-align:left;vertical-align:bottom;"><%=description.replaceAll("\r\n", "<br>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")%></td>	
			<%}%>
				
			</tr>
			<%if (bgColorDescription.equals("")) {%>
			<tr class="colorDCDCDCbg">	
			<%}else{ %>
			<tr class="<%=bgColorDescription%>">
			<%}%>
				<td style="height:4px;"><spacer type="block" width="1" height="1"></td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" style="height:3px;"><spacer type="block" width="1" height="1"></td>
			</tr>
		</table>
	<%}else{%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td bgcolor="#FFFFFF" style="height:6px;"><spacer type="block" width="1" height="1"></td>
			</tr>
		</table>	
	<%}%>
	
	<!-- Start table containing links -->
	<table  style="<%= linkTableStyle %>" border="0" cellpadding="0" cellspacing="0" width="100%"> 
	
	<%
	int i = 0;
	for (i = 0; i < children.size(); i++) {
		MenuItemNode node = (MenuItemNode) children.get(i);
		String childTitle = node.getTitle();

		String childDescription = node.getDescription();
		
		String childMenuItemName = null;
		MenuItem childMenuItem = node.getMenuItem();
		if (childMenuItem != null)	
		{
			childMenuItemName = childMenuItem.getTitle();
		}
		
		// alt text displayed for the navigation portlets.
		// Vignette adds the <SPAN> tag arround the name for displaying purpose.
		// Displaying the alt text we need to remove the Tags
		String alt = I18nUtils.getValue(node.getID(), "alt", childTitle, session, request);
		alt = menuItemHelper.stripVignetteSpanTags(alt);
		
		//FIX Me - Pranav ( BUG 6533 )
		alt=alt.replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
				
		// if we are on the first row or we have reached the max number of columns
		if (i%columns == 0)	{
			// close the previous row unless we are on the first row
			if (i != 0)	{
				%>
	</tr>
				<%
			}
		%>
	<tr>
		<%
		}
		%>
		<td width="<%= 100/columns %>%">

		<%
		if (node.inPopup()){
				String href = menuItemHelper.getNodeHref(portalContext, node);
				if (!node.isPage() && !node.isJSPPage()) {
					    href = "#\" onclick=\"return popupLink(this, '"+href+"','"+childMenuItemName+"',htmlFormat(securityMessage),"+node.inPopup()+");";
				}else if (menuItemHelper.isEService(node)) {
			    href = "#\" onclick=\"return prepareLink(this, '"+href+"','"+childMenuItemName+"',htmlFormat(securityMessage),"+sessionInSimulation+");";
				} 
				%>
		
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<%
				if (linespacing)	{ 
			%>
				<tr>
					<td colspan="2" style="font-size: 10pt;"><p>&nbsp;</td>
				</tr>
			<%
				}
			 %>
				<tr>
					<td style="<%= linkCellStyle %>" nowrap>&raquo;&nbsp;</td>
					<td style="<%= linkCellStyle %>" width="100%"><a href="<%=href%>" style="<%= linkStyle %>" title="<%=alt%>"><%=childTitle%></a></td>
					<!-- Nav-Item-Name: <%= childMenuItemName %> -->
				</tr>
				<%
				if (underlineLinks)	{
				%>
				<tr>
					<td colspan="2" bgcolor="#<%=color%>" style="height:4px;"><spacer type="block" width="1" height="1"></td>
				</tr>
				<%
				}
				if (displayDescription && childDescription != null && childDescription.length() != 0)	{
				%>
				<tr>
					<td colspan="2" style="font-size: 2pt;">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2" style="<%= linkCellStyle %>" width="100%"><%=childDescription%></td>
				</tr>
				<tr>
					<td colspan="2" style="font-size: 10pt;"><p>&nbsp;</td>
				</tr>
				<%
				}
				%>
			</table>
			
		<%}else{
	
		if (node.isLeaf()) {%>
			<%  String href = menuItemHelper.getNodeHref(portalContext, node);
			if (menuItemHelper.isEService(node)) {
			    href = "#\" onclick=\"return prepareLink(this, '"+href+"','"+childMenuItemName+"',htmlFormat(securityMessage),"+sessionInSimulation+");";
			} 
			%>	
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<%
				if (linespacing)	{ 
			%>
				<tr>
					<td colspan="2" style="font-size: 10pt;"><p>&nbsp;</td>
				</tr>
			<%
				}
			 %>
				<tr>
					<td style="<%= linkCellStyle %>" nowrap>&raquo;&nbsp;</td>
					<td style="<%= linkCellStyle %>" width="100%"><a href="<%=href%>" style="<%= linkStyle %>" title="<%=alt%>"><%=childTitle%></a></td>
					<!-- Nav-Item-Name: <%= childMenuItemName %> -->
				</tr>
				<%
				if (underlineLinks)	{
				%>
				<tr>
					<td colspan="2" bgcolor="#<%=color%>" style="height:4px;"><spacer type="block" width="1" height="1"></td>
				</tr>
				<%
				}
				if (displayDescription && childDescription != null && childDescription.length() != 0)	{
				%>
				<tr>
					<td colspan="2" style="font-size: 2pt;">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2" style="<%= linkCellStyle %>" width="100%"><%=childDescription%></td>
				</tr>
				<tr>
					<td colspan="2" style="font-size: 10pt;"><p>&nbsp;</td>
				</tr>
				<%
				}
				%>
			</table>
		<%}else{
			if (!node.isEmpty()){
				String href = menuItemHelper.getNodeHref(portalContext, node);
				if (menuItemHelper.isEService(node)) {
				    href = "#\" onclick=\"return prepareLink(this, '"+href+"','"+childMenuItemName+"',htmlFormat(securityMessage),"+sessionInSimulation+");";
				} 
				%>
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<%
					if (linespacing)	{
				%>
					<tr>
						<td colspan="2" style="font-size: 10pt;"><p>&nbsp;</td>
					</tr>
				<%
					}
				 %>
					<tr>
						<td style="<%= linkCellStyle %>" nowrap>&raquo;&nbsp;</td>
						<td style="<%= linkCellStyle %>" width="100%"><a href="<%=href%>" style="<%= linkStyle %>" title="<%=alt%>"><%=childTitle%></a></td>
						<!-- Nav-Item-Name: <%= childMenuItemName %> -->
					</tr>
					<%
					if (underlineLinks)	{
					%>
					<tr>
						<td colspan="2" bgcolor="#<%=color%>" style="height:4px;"><spacer type="block" width="1" height="1"></td>
					</tr>
					<%
					}
					if (displayDescription && childDescription != null && childDescription.length() != 0)	{
					%>
					<tr>
						<td colspan="2" style="font-size: 2pt;">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2" style="<%= linkCellStyle %>" width="100%"><%=childDescription%></td>
					</tr>
					<tr>
						<td colspan="2" style="font-size: 10pt;"><p>&nbsp;</td>
					</tr>
					<%
					}
					%>
				</table>
			<%}else{%>
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr><td>
					<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td bgcolor="#FFFFFF" style="height:4px;"><spacer type="block" width="1" height="1"></td>
					</tr>
					<tr>
					<td>
					<h2 class="bold" style="<%= fontSizeStyle %>"><%=node.getTitle()%></h2>
					<!-- Nav-Item-Name: <%= childMenuItemName %> -->
					</td>
					</tr>
					<tr>
						<td bgcolor="#<%=color%>"	style="height:2px;"><spacer type="block" width="1" height="1"></td>
					</tr>
					</table>
					<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td bgcolor="#FFFFFF" style="height:4px;"><spacer type="block" width="1" height="1"></td>
					</tr>
					</table>	
					</td></tr>
				</table>
				<%List grandChildren = menuItemHelper.getChildNodes(portalContext, node);
				for (int j = 0; j < grandChildren.size(); j++) {
					MenuItemNode node_bis = (MenuItemNode) grandChildren.get(j);
					if (node.getID().equals(node_bis.getParentID())) {
						String href = menuItemHelper.getNodeHref(portalContext, node_bis);
						
						String grandchildMenuItemName = null;
						MenuItem grandchildMenuItem = node_bis.getMenuItem();
						if (grandchildMenuItem != null)	
						{
							grandchildMenuItemName = grandchildMenuItem.getTitle();
						}
					
						String alt2 = I18nUtils.getValue(node_bis.getID(), "alt", node_bis.getTitle(), session, request);
						alt2= menuItemHelper.stripVignetteSpanTags(alt2);
						
						// FIX Me - Pranav ( Bug 6533 )
						alt2=alt2.replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
															
						if (menuItemHelper.isEService(node_bis)) {
						    href = "#\" onclick=\"return prepareLink(this, '"+href+"','"+grandchildMenuItemName+"',htmlFormat(securityMessage),"+sessionInSimulation+");";
						}
						%>
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<%
								if (linespacing)	{
							%>
								<tr>
									<td colspan="2" style="font-size: 10pt;"><p>&nbsp;</td>
								</tr>
							<%
								}
							 %>
								<tr>
									<td style="<%= linkCellStyle %>" nowrap>&raquo;&nbsp;</td>
									<td style="<%= linkCellStyle %>" width="100%"><a href="<%=href%>" style="<%= linkStyle %>" title="<%=alt2%>"><%=node_bis.getTitle()%></a></td>
									<!-- Nav-Item-Name: <%= grandchildMenuItemName %> -->
								</tr>
								<%
								if (underlineLinks)	{
								%>
								<tr>
									<td bgcolor="#<%=color%>" style="height:4px;"><spacer type="block" width="1" height="1"></td>
								</tr>
								<%
								}
								String grandchildDescription = node_bis.getDescription();
								if (displayDescription && grandchildDescription != null
										 && grandchildDescription.length() != 0)	{
								%>
								<tr>
									<td colspan="2" style="font-size: 2pt;">&nbsp;</td>
								</tr>
								<tr>
									<td colspan="2" style="<%= linkCellStyle %>" width="100%"><%=grandchildDescription%></td>
								</tr>
								<tr>
									<td colspan="2" style="font-size: 10pt;"><p>&nbsp;</td>
								</tr>
								<%
								}
								%>
							</table>
					<%}
				}
			}
		  }
		}
		%>
		</td>
		<%
	}
	while (i%columns != 0)	{
		%>
		<td width="<%= 100/columns %>%">&nbsp;</td>
		<%
		i++;
	}
	%>
		</tr>
	<%
	%>
	<!-- End table containing links -->
	</table>
	<%
}%>

<%!
		//Checks if the url is an EService URL or not.
		boolean isEService(Style eService){

		String eServiceTitle = eService.getTitle();

		String eServiceFriendlyId =  eService.getFriendlyID();

		//Check if the EService pattern ever occurs.
		String eServicePattern = "SPP EserviceProxy";

		//Check if the EService pattern ever occurs.
		String eServiceFriendlyIdPattern = "eserviceproxy1";

		int index = eServiceTitle.indexOf(eServicePattern);

		if ((eServiceTitle.indexOf(eServicePattern) >= 0) || (eServiceFriendlyId.indexOf(eServiceFriendlyIdPattern) >= 0)) {
			return true;
		}
		else{
			return false;
		}

	}
%>

</mod:view>