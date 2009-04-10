<%@ page import="java.util.Map" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<portlet:defineObjects/>
<p>
    This portlet allows to test portlet preferences. It can be used with the following scenarios:
</p>
<ul>
    <li>
        Portlet preference values for CONFIG mode. The values defined in CONFIG mode must be
        the same for all the users. Preferences defined in any other mode must specific to each
        user.
    </li>
    <li>
        Portlet application redeployment. After the portlet application is redeployed all
        the portlet windows (instance) must continue to function. In particular any preferences
        defined before the deployment must be still visible after the deployment assuming that
        the portlet still uses them. In addition, any changes to the "portlet-preferences"
        section of the portlet.xml file should also be visible for portlet instances, i.e. read-only
        preference changes (names, values), read/write portlet preference changes (new preferences,
        changed default values, etc...).
    </li>
</ul>
<hr />
<p>
    Current preferences:
</p>
<ul>
    <% PortletPreferences prefs = renderRequest.getPreferences(); %>
    <% for (Map.Entry<String, String[]> pref : prefs.getMap().entrySet()) { %>
    <li>
        <strong><%= pref.getKey() %></strong>
        (<%= prefs.isReadOnly(pref.getKey()) ? "read-only" : "read/write"%>):
        <%= Arrays.asList(pref.getValue())%>
    </li>
    <% } %>
</ul>
<form action="<portlet:actionURL />" method="POST">
    <div>
        <fieldset>
            <label for="prefName">Preference Name:</label>
            <input type="text" name="prefName" id="prefName"/>
        </fieldset>
    </div>
    <div>
        <fieldset>
            <label for="prefValue">Preference Value:</label>
            <input type="text" name="prefValue" id="prefValue" />
            <p style="font-size:small; font-style:italic;">
                Use comma to separate mutli-value preferences. Use empty value to remove/reset
                the preference.
            </p>
        </fieldset>
    </div>
    <div>
        <input type="submit" value="Store" />
    </div>
</form>