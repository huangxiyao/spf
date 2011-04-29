// This file just demonstrates how you can reference JavaScript from
// your global help HTML file.  Currently it is not used.  See the
// global help HTML file for more information.

var localAnchor = window.location.href;
if (localAnchor.lastIndexOf("#") != -1) {
   window.navigate(localAnchor);
}

// End of file