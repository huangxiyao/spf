<%@page import="com.vignette.portal.log.Log"%><%//get parameter value HPPUserId and PortalSessionId
    String _HPPUserId  = (String)request.getParameter("HPPUserId");
    String _PortalSessionId  = (String)request.getParameter("PortalSessionId");
    Log.info(this.getClass(), "[SPP] checkSession: HPPUserId="+_HPPUserId);
    Log.info(this.getClass(), "[SPP] checkSession: PortalSessionId="+_PortalSessionId);
    if(		(_HPPUserId !=  null )&& 
            (!_HPPUserId.equalsIgnoreCase("")) &&
            (_HPPUserId.trim().length() > 0) &&
            (_PortalSessionId !=  null )&& 
            (!_PortalSessionId.equalsIgnoreCase("")) &&
            (_PortalSessionId.trim().length() > 0))
    {
        Log.info(this.getClass(), "/C:checkSession: evaluateCheckSession");
        out.print(com.hp.spp.portal.common.util.CheckSession.evaluateCheckSession(_HPPUserId,_PortalSessionId));
    }
    else
    {
		out.print("VALID=FALSE");
    }
%>