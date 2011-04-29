<%@ page import="java.util.Map" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<portlet:defineObjects/>
<p>
    The System Performance can be measured on the following aspects.
</p>
<ul>
    <li>
        <b>objectCount : </b>Indicates number of objects [ a single line string object ] created by the portlet instance on 
        single invocation.
        Recommended values [ 500 ]
    </li>
    <li>
        <b>numerands : </b>Indicates number of numerical iterations a single invocation. 
        Recommended values [ multiples of 25000 ]
    </li>
    <li>
        <b>minDelay & maxDelay : </b>This 2 preference values should be used in combination. A random delay in millisecond
        is generated between these 2 preference values.
        Recommended values [ minDelay - 40 And maxDelay - 60 ]        
    </li>
</ul>
<hr/>
<p>
    current portlet instance configuration
</p>
<ul>
    <% PortletPreferences prefs = renderRequest.getPreferences(); %>
    <% for (Map.Entry<String, String[]> pref : prefs.getMap().entrySet()) { %>
    <li>
        <strong><%= pref.getKey() %></strong>
        <%= pref.getValue()[0]%>
    </li>
    <% } %>
</ul>
<form action="<portlet:actionURL />" method="POST">
	<div>
	<b>Set portlet view properties</b>
	</div>
	<div>
	<tr>
		<th>Number of Items:</th>
			<td><input type="text" name="numberOfItems" value="<c:out value="${numberOfItems}" />" /></td>
	</tr>
	<tr>
		<th>Display Mode:</th>
			<td>
				<select name="displayMode">
					<option value="verbose">verbose</option>
					<option value="brief" <c:if test="${displayMode == 'brief'}">selected="selected"</c:if>>brief</option>
				</select>
			</td>
	</tr>
	</div>

	<div>
	<b>Set Portlet Preferences For Performance Test</b>
	</div>

    <div>
        <label for="prefName">Preference Name:</label>
        <input type="text" name="prefName" id="prefName"/>
    </div>
    <div>
        <label for="prefValue">Preference Value:</label>
        <input type="text" name="prefValue" id="prefValue" />
    </div>
    <div>
        <input type="submit" value="Store" />
    </div>
</form>
