<%--
Copyright 2004 The Apache Software Foundation
Licensed  under the  Apache License,  Version 2.0  (the "License");
you may not use  this file  except in  compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed  under the  License is distributed on an "AS IS" BASIS,
WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
implied.

See the License for the specific language governing permissions and
limitations under the License.
--%>
<jsp:useBean id="portletInfo" type="org.apache.pluto.portalImpl.aggregation.PortletFragment$PortletInfo" scope="request" />
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr class="theme">
		<td align="left" width="10"><img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="1" alt=""></td>
		<td align="left" valign="top" width="100%"><h2 class="themeheader"><%= portletInfo.getTitle() %></h2></td>
		<td align="right" valign="top" nowrap="nowrap">
			<span class="themeheader">
			<%
				java.util.List modeList = portletInfo.getAvailablePortletModes();
				java.util.Collections.sort(modeList);
				for (java.util.Iterator iter = modeList.iterator(); iter.hasNext();) {
					org.apache.pluto.portalImpl.aggregation.PortletFragment.PortletModeInfo modeInfo = (org.apache.pluto.portalImpl.aggregation.PortletFragment.PortletModeInfo) iter.next();
					if (modeInfo.isCurrent()) {
						out.println(modeInfo.getName());
					}
					else {
			%>
				<a href="<%=modeInfo.getUrl()%>" class="themeheaderlink">&raquo;<%=modeInfo.getName()%></a>&nbsp;
			<%
					}
				}
			%>
			<%
				java.util.List windowStateList = portletInfo.getAvailablePortletWindowStates();
				java.util.Collections.sort(windowStateList);

				if (modeList.size() > 0 && windowStateList.size() > 0) {
			%>
					&nbsp;|&nbsp;
			<%
				}
				for (java.util.Iterator iter = windowStateList.iterator(); iter.hasNext();) {
					org.apache.pluto.portalImpl.aggregation.PortletFragment.PortletWindowStateInfo stateInfo = (org.apache.pluto.portalImpl.aggregation.PortletFragment.PortletWindowStateInfo) iter.next();
					if (stateInfo.isCurrent()) {
						out.println(stateInfo.getLabel());
					}
					else {
			%>
				<a href="<%=stateInfo.getUrl()%>" class="themeheaderlink">&nbsp;&raquo;<%=stateInfo.getLabel()%></a>&nbsp;
			<%
					}
				}
			%>
			</span>
		</td>
		<td align="left" width="10"><img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="1" alt=""></td>
	</tr>
	<tr class="decoration">
		<td colspan="4"><img src="http://welcome.hp-ww.com/img/s.gif" width="1" height="10" alt=""></td>
	</tr>
	<tr>
		<td align="left" width="10"><img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="1" alt=""></td>
		<td align="left" valign="top" width="100%" colspan="2">
