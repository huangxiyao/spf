<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.regex.Matcher"%>
<%@ page import="java.util.regex.Pattern"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.epicentric.navigation.MenuItem"%>
<%@ page import="com.epicentric.common.website.MenuItemUtils" %>
<%@ page import="com.epicentric.common.website.MenuItemNode" %>
<%@ page import="com.epicentric.common.website.I18nUtils" %>
<%@ page import="com.hp.it.spf.navportlet.portal.component.portalbean.NavPortletBean" %>
<%@ page import="com.hp.it.spf.xa.misc.portal.MenuItemHelper" %> 

<%@ taglib uri="module-tags" prefix="mod" %>
<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>

<%! private static final Pattern SPAN_PATTERN = Pattern.compile("(&lt;SPAN lang=&quot;..&quot; xml:lang=&quot;..&quot;&gt;|&lt;/SPAN&gt;|<SPAN lang=\"..\" xml:lang=\"..\">|</SPAN>)", Pattern.CASE_INSENSITIVE); %>

<vgn-portal:defineObjects/>

<mod:view class="com.epicentric.portalbeans.beans.jspbean.JSPView">

<%
	//FIXME (slawek) The look and feel of this page does not comply with the latest standards.
	//This needs to be fixed and we will manage that once people start using. Right now it is an
	//as-is port from SPP

	NavPortletBean bean = (NavPortletBean) view.getBean();
//	final String beanLocaleKey = "c56d32feb0fce0e2743292844c6f0101";
	final String beanLocaleKey = bean.getStringID();
	boolean underline = false;
	boolean thin = false;
	boolean noHeader = false;
	boolean linespacing = false;
	boolean displayDescription = false;
	boolean underlineLinks = false;

	//Find out if the user session is in simulation mode.
	//FIXME (Slawek) - below line is commented as at this point the SPP simulation has not been
	// moved to SPF yet
	//boolean sessionInSimulation = new ProfileHelper().isSimulationMode(session);
	boolean sessionInSimulation = false;

	int columns = 1;

	String description = "";

	String bgColorDescription = bean.getDescriptionBackgroundColor();
	if (bgColorDescription == null) bgColorDescription = "";
	String bgColorDescriptionStyle = 
		("".equals(bgColorDescription) ? "" :
			"background-color: " + 
			(bgColorDescription.startsWith("#") ? "" : "#") + 
			bgColorDescription + ";");

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
		children = menuItemHelper.getChildNodes(portalContext, thisNode);
		description = I18nUtils.getValue(thisNode.getID(), "description", "", session, request);
		description = menuItemHelper.getValueNoSpan(description) ;

		// set up the menu item name (not the displayed title) to display as comment to help find
		// the friendly URL of the menu item
		String thisNodeItemName = null;
		MenuItem thisNodeItem = thisNode.getMenuItem();
		if (thisNodeItem != null)
		{
			thisNodeItemName = thisNodeItem.getTitle();
		}

		securityMessage1 = I18nUtils.getValue(beanLocaleKey, "securitymessage", "", session, request);

		if(securityMessage1 == null || securityMessage1.equalsIgnoreCase(""))	{
			%><!--security message not ok--><%
		}
		else	{
	    %>
	    <vgn-portal:i18nElement>
	    <SCRIPT>
	    var securityMessage = "<vgn-portal:i18nValue stringID="<%=beanLocaleKey%>" key="securitymessage" defaultValue="" />";
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
			<tr style="<%=bgColorDescriptionStyle%>">
				<td style="padding-left:1px;padding-top:8px;text-align:left;vertical-align:bottom;"><%=description.replaceAll("\r\n", "<br>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")%></td>	
			<%}%>
				
			</tr>
			<%if (bgColorDescription.equals("")) {%>
			<tr class="colorDCDCDCbg">	
			<%}else{ %>
			<tr style="<%=bgColorDescriptionStyle%>">
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
		Matcher matcher = SPAN_PATTERN.matcher(alt);
		alt=matcher.replaceAll("");
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
			//FIXME (slawek) Uncomment the line below once we have a common place for SPF javascript
			href = href + "\" target=\"_blank";
//				    href = "#\" onclick=\"return popupLink(this, '"+href+"','"+childMenuItemName+"',htmlFormat(securityMessage),"+node.inPopup()+");";
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
				//FIXME (slawek) the commented code relies on ESM which is not migrated yet from SPP
//			if (menuItemHelper.isEService(node)) {
//			    href = "#\" onclick=\"return prepareLink(this, '"+href+"','"+childMenuItemName+"',htmlFormat(securityMessage),"+sessionInSimulation+");";
//			}
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
				//FIXME (slawek) The commented code relies on ESM which is not migrated yet from SPP
//				if (menuItemHelper.isEService(node)) {
//				    href = "#\" onclick=\"return prepareLink(this, '"+href+"','"+childMenuItemName+"',htmlFormat(securityMessage),"+sessionInSimulation+");";
//				}
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
						matcher = SPAN_PATTERN.matcher(alt2);
						alt2=matcher.replaceAll("");
						// FIX Me - Pranav ( Bug 6533 )
						alt2=alt2.replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");

						//FIXME (slawek) The commented code relies on ESM which has not been migrated yet from SPP
//						if (menuItemHelper.isEService(node_bis)) {
//						    href = "#\" onclick=\"return prepareLink(this, '"+href+"','"+grandchildMenuItemName+"',htmlFormat(securityMessage),"+sessionInSimulation+");";
//						}
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

</mod:view>