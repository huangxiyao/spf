<%@ include file="wpa-tlds.jsp" %>

<html:errors/>
<br><br>

<logic:present name="errorInfoBean">
	Error Code: <bean:write name="errorInfoBean" property="errorCode"/>
</logic:present>