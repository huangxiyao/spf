<%@ taglib uri="vgn-tags" prefix="vgn-portal"%>

<%@page import="java.util.List" %>
<%@page import="java.util.Iterator" %>
<%@page import="com.epicentric.common.website.MenuItemNode" %>
<%@page import="com.epicentric.common.website.I18nUtils" %>
<%@page import="com.hp.spp.portal.common.helper.MenuItemHelper"%>
<%@page import="com.hp.spp.portal.common.helper.LocalizationHelper" %>
<%@page import="com.hp.spp.portal.common.helper.ProfileHelper" %>
<%@page import="com.epicentric.navigation.MenuItem"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="com.hp.spp.profile.Constants"%>



<vgn-portal:defineObjects/>

<%

try {
	String portletFriendlyId = new ProfileHelper().getProfileValue(portalContext, Constants.MAP_SITE)+"homepagebannerportlet";
	
	LocalizationHelper localizationHelper = new LocalizationHelper() ;
	String i18nID = localizationHelper.getCommonI18nID(portalContext);

	String securityMessage = I18nUtils.getValue("e52f774b23843eeee13c7ef0b528efa0", "securitymessage", "", session, request);
	securityMessage = localizationHelper.getValueNoSpan(securityMessage) ;
	
	if(securityMessage == null || securityMessage.equalsIgnoreCase(""))	{
	    %><!--security message not ok--><%
	    securityMessage = "" ;
	}

	%>
	<script type="text/javascript" language="javascript">
		var securityMessage = htmlFormat("<%=securityMessage%>");
	</script>
	<%
	
	MenuItemHelper menuItemHelper = new MenuItemHelper();
	MenuItemNode root = menuItemHelper.getSiteRootNode(portalContext);
	
	//	Find out if the user session is in simulation mode.
	boolean sessionInSimulation = new ProfileHelper().isSimulationMode(session);

	// if the root has not yet been created/configured, then nothing is displayed
	// in the left nav. Should not cause error.
	if (root != null)	{
%>
				
<table border="0" cellpadding="0" cellspacing="0" width="170">
    <tr class="colorDCDCDCbg">
    	<td width="10">&nbsp;</td>
    	<td width="10"><h2 class="bold"><font color="#003366">&raquo;</font></h2></td>
        <td >
            <h2 class="bold">
	            <a href="<%=menuItemHelper.getNodeHref(portalContext, root)%>">
	            	<vgn-portal:i18nValue stringID="<%=i18nID%>" key="sitename" defaultValue="" />
	            </a>
            </h2>
        </td>
    </tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="170">
  <tr>
	<td style="padding-top:10px;padding-bottom:10px;text-align:left;vertical-align:top" width="170">
		<table border="0" cellpadding="0" cellspacing="0" width="170">
<%
		List childNodes = menuItemHelper.getChildNodes(portalContext, root);

		Iterator iter = childNodes.iterator();
		while (iter.hasNext())	{
			MenuItemNode node = (MenuItemNode) iter.next();
			String href = null ;
			String title = null ;
			if (!node.isSpacer()) {
				href = menuItemHelper.getNodeHref(portalContext, node);
				if(menuItemHelper.isEService(node)) {
				    href = "#\" onclick=\"return prepareLink(this, '"+href+"','"+node.getMenuItem().getTitle()+"',securityMessage,"+sessionInSimulation+");";
				}
			}
			title = node.getTitle();
			
			String menuItemName = null;
			MenuItem menuItem = node.getMenuItem();
			if (menuItem != null)
			{
				menuItemName = menuItem.getTitle();
			}

			String alt = I18nUtils.getValue(node.getID(), "alt", title, session, request);
			alt = menuItemHelper.stripVignetteSpanTags(alt);
			//FIX ME - Pranav ( BUG 6533 )
			alt=alt.replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
			
%>
		<tr>
			<td align="left" valign="top" width="20">&nbsp;</td>	
<%			if (node.isSpacer()) { %>
				<td align="left" valign="top" width="140" colspan="2"><hr color="#CCCCCC" size="1"></td>
<%			} else if (title != null && !"".equals(title) && href != null && !"".equals(href)) { %>
				<td align="left" valign="top" width="10"><font color="#003366">&raquo;&nbsp;</font></td>
				<td align="left" width="130" ><a href="<%=href%>" title="<%=alt%>" ><%=title%></a></td>
				<!-- Nav-Item-Name: <%= menuItemName %> -->
<%			} else { %>
				<!-- ### BEGIN ### Title is null or empty -->
				<td align="left" valign="top" width="140" colspan="2"><hr color="#CCCCCC" size="1"></td>
				<!-- ### END ### Title is null or empty -->
<%			} %>
			<td align="left" valign="top" width="10">&nbsp;</td>	
		</tr>
<%
		}
%>
		<%-- Logout ### Begin ### --%>
		<tr>
			<td align="left" valign="top" width="20">&nbsp;</td>	
			<td align="left" valign="top" width="140" colspan="2"><hr color="#CCCCCC" size="1"></td>
			<td align="left" valign="top" width="10">&nbsp;</td>	
		</tr>
		<tr>
			<td align="left" valign="top" width="20">&nbsp;</td>	
			<td align="left" valign="top" width="10"><font color="#003366">&raquo;&nbsp;</font></td>
			<td align="left" width="130" >
				<a href="javascript:logout();"><vgn-portal:i18nValue stringID="<%=i18nID%>" key="sign_out" defaultValue="Sign-out" /></a>
			</td>
			<td align="left" valign="top" width="10">&nbsp;</td>	
		</tr>
		<%-- Logout ### End ### --%>
      </table>
	</td>
  </tr>
  	<tr>
	<td>
	<hr color="#CCCCCC" size="2">
	</td>
	</tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="170">
	<tr>
		<td>&nbsp;</td>
		<td align="centre">
			<vgn-portal:renderPortlet portletFriendlyID="<%=portletFriendlyId%>">
				<vgn-portal:onRenderSuccess>
					<vgn-portal:insertPortletContent />
				</vgn-portal:onRenderSuccess>
				<vgn-portal:onRenderFailure>
				</vgn-portal:onRenderFailure>
			</vgn-portal:renderPortlet></td>
	</tr>
	<tr>
		<td colspan="2">
		<hr color="#CCCCCC" size="2">
		</td>
	</tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" height="150">
  <tr>
  <td>&nbsp;</td>
  </tr>
</table>

<%
	} // end if root != null
	else	{
		%>
<!-- The root was not found -->
		<%
	}
} catch (Exception e) {
	out.print("Exception in Left Navigation: " + e);
}
%>
	
<%--
    
 There are three conditions for images: 
    
 1. No image - so title is a key 'title' filtered for user's language.
 2. There is an image so use override nav item with image - alt text is filtered for language
    
 // these two behave the same way
 3. There is an image with text so use custom key 'imagepath' to find image on file system
 4. There is an image with fullpath to external site - filtered for language
    
--%>
