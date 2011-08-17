<%@ page session="false" buffer="none" %>
<%@ page import="org.apache.pluto.portalImpl.core.PortalURL" %>
<%@ page import="org.apache.pluto.portalImpl.core.PortalEnvironment" %>
<%@ page import="org.apache.pluto.portalImpl.aggregation.navigation.Navigation" %>
<%@ page import="org.apache.pluto.portalImpl.aggregation.navigation.NavigationTreeBean" %>
<jsp:useBean id="fragment" type="org.apache.pluto.portalImpl.aggregation.navigation.TabNavigation" scope="request" />
<%
	PortalURL url = PortalEnvironment.getPortalEnvironment(request).getRequestedPortalURL();
	NavigationTreeBean[] tree = fragment.getNavigationView(url);
	for (int i=0; i<tree.length; i++)
	{
		Navigation nav = tree[i].navigation;
%>
<tr>
	<td align="left" valign="top" width="10" class="color003366">&raquo;</td>
	<td align="left" width="140" colspan="3"><h3><a href="<%=new PortalURL(request, nav.getLinkedFragment()).toString()%>"><%=nav.getTitle()%></a></h3></td>
</tr>
<%
	}
%>

