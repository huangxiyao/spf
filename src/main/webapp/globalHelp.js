// This file contains some JavaScript functions which you can reference in
// your global help HTML.  See the global help HTML template file for 
// instructions how to reference this file.

function globalHelpPrint() {
   var divContainer = document.getElementById('contentContainer');
   window.onresize = null;
   divContainer.style.overflow = "visible";
   window.print();
   window.onresize = updateContainer;
   divContainer.style.overflow = "auto";
}

function updateContainer() {
   if (document.documentElement && document.documentElement.clientWidth) {
      frameHeight = document.documentElement.clientHeight;
   }
   else if (document.body) {
      frameHeight = document.body.clientHeight;
   }
   var divContainer = document.getElementById('contentContainer');
   var divHearder = document.getElementById('header');
   divContainer.style.height = frameHeight-divHearder.scrollHeight>10?frameHeight-divHearder.scrollHeight+'px':'10px';
   divContainer.style.overflow = "auto";
}

window.onresize = updateContainer;

updateContainer();

var localAnchor = window.location.href;
if (localAnchor.lastIndexOf("#") != -1) {
   window.navigate(localAnchor);
}

// End of file