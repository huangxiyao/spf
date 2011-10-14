This multi-module project contains Persona components.

Since Persona is currently used only in SPF and its implementation is not alligned with the latest
Enterprise Data Model-based architecture it was moved into SPF from CAS repository located at
https://code1.corp.hp.com/svn/cas/source/trunk/security/.

The code was moved as-is and is therefore still referencing old CAS classes. Next needed update
to Persona will most likely also require some adjustments to make it truly part of SPF.

Persona modules are currently not part of SPF top-level multi-module project and should not be
until the adjustments mentioned above are done.
