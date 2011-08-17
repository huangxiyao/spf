@ECHO OFF
REM $Id: generator.bat,v 1.1 2006/08/21 14:54:45 cmicoud Exp $
SET JAVA=%JAVA_HOME%\bin\java
SET cp=%CLASSPATH%
FOR %%i IN (lib\*.jar) DO CALL cp.bat %%i
::SET cp=%cp%;"c:\Documents and Settings\mvidal\.m2\repository\axis\axis\1.3\axis-1.3.jar";"c:\Documents and Settings\mvidal\.m2\repository\commons-logging\commons-logging\1.0.4\commons-logging-1.0.4.jar";"c:\Documents and Settings\mvidal\.m2\repository\commons-discovery\commons-discovery\0.2\commons-discovery-0.2.jar";"c:\Documents and Settings\mvidal\.m2\repository\axis\axis-jaxrpc\1.3\axis-jaxrpc-1.3.jar";"c:\Documents and Settings\mvidal\.m2\repository\axis\axis-saaj\1.3\axis-saaj-1.3.jar"
SET REPO="c:\Documents and Settings\mvidal\.m2\repository\
SET cp=%REPO%axis\axis\1.3\axis-1.3.jar";%REPO%commons-logging\commons-logging\1.0.4\commons-logging-1.0.4.jar";%REPO%commons-discovery\commons-discovery\0.2\commons-discovery-0.2.jar";%REPO%axis\axis-jaxrpc\1.3\axis-jaxrpc-1.3.jar";%REPO%axis\axis-saaj\1.3\axis-saaj-1.3.jar";%REPO%wsdl4j\wsdl4j\1.5.1\wsdl4j-1.5.1.jar"
SET cp=%cp%;.


SET wsdl=D:\mat\workspaces\spp\spp-portal-wsmanager\wsdl\UPSWebServices.wsdl

ECHO.
ECHO.
ECHO WSDL2Java:
ECHO.
ECHO %JAVA% -classpath %CP% org.apache.axis.wsdl.WSDL2Java %wsdl%
%JAVA% -classpath %CP% org.apache.axis.wsdl.WSDL2Java %wsdl% 
