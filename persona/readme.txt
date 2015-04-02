This multi-module project contains Persona components.

Since Persona is currently used only in SPF and its implementation is not alligned with the latest
Enterprise Data Model-based architecture it was moved into SPF from CAS repository located at
https://code1.corp.hp.com/svn/cas/source/trunk/security/.

The code was moved as-is and is therefore still referencing old CAS classes. Next needed update
to Persona will most likely also require some adjustments to make it truly part of SPF.

Persona modules are currently not part of SPF top-level multi-module project and should not be
until the adjustments mentioned above are done.

Persona 2.0.1 SVN address:
https://code1.corp.hp.com:1181/svn/cas/source/tags/cas-persona-2.0.1/
https://code1.corp.hp.com:1181/svn/cas/source/tags/cas-persona-user-loader-2.0.1/
https://code1.corp.hp.com:1181/svn/cas/source/tags/cas-persona-webservice-2.0.1/
