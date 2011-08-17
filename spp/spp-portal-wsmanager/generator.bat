@ECHO OFF
REM $Id: generator.bat,v 1.1 2006/08/21 14:54:45 cmicoud Exp $
SET cp=%CLASSPATH%
FOR %%i IN (lib\*.jar) DO CALL cp.bat %%i
SET cp="..\spp-repository\vignette\axis\7.3\axis-7.3.jar;..\spp-repository\vignette\commons-discovery\7.3\commons-discovery-7.3.jar;..\spp-repository\vignette\commons-logging\7.3\commons-logging-7.3.jar;..\spp-repository\vignette\jax-qname\7.3\jax-qname-7.3.jar;..\spp-repository\vignette\log4j\7.3\log4j-7.3.jar;..\spp-repository\vignette\saaj\7.3\saaj-7.3.jar;..\spp-repository\vignette\jaxrpc\7.3\jaxrpc-7.3.jar;..\spp-repository\vignette\wsdl4j\7.3\wsdl4j-7.3.jar"
SET cp=%cp%;.

echo %cp%

::SET path=EServiceManager
::SET path=UserGroupManager
::SET path=UPSWebServices
SET path=UGSRuntimeServiceXfireImpl

::SET package=com.hp.spp.webservice.eservice.client
::SET package=com.hp.spp.webservice.ugs.client
::SET package=com.hp.spp.webservice.ups.client
SET package=com.hp.runtime.ugs.asl.service

::SET namespace=http://vdccintitg.houston.hp.com/spp-services-web/services/EServiceManager
::SET namespace=http://vdccintitg.houston.hp.com/spp-services-web/services/UserGroupManager
::SET namespace=http://UPSWeb.hp.com/Services
SET namespace=http://service.asl.ugs.runtime.hp.com

SET src=src/main/java
SET wsdl=%path%.wsdl

ECHO.
ECHO.
ECHO WSDL2Java:
ECHO.
%JAVA_HOME%\bin\java -classpath %CP% org.apache.axis.wsdl.WSDL2Java -s "wsdl/%wsdl%" -p %package% -o "%src%"
