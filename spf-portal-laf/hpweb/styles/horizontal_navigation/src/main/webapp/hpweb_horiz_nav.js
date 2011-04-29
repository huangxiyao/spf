
/*
 * When there is a mouseover event in a secondary nav menu item this function
 * is called to show its dropdown menu (see html in hpweb_horiz_nav.jsp).  
 * The div style class name and the caret image are changed.
 */

function expand(div, blackCaretImg)
{
	var d = div.getElementsByTagName("div").item(0);
	d.className = d.className.replace(/menuNormal/, 'menuHover');

	var topmenuitem = div.getElementsByTagName("a").item(0);

	/** Change to black caret image **/
	//var img = topmenuitem.getElementsByTagName("img").item(1);
	//img.src = blackCaretImg;

	var idStr = div.id;
	var idIndex = idStr.substring((idStr.indexOf('menuTD_')+'menuTD_'.length), idStr.length);
	var img = document.getElementById('img_'+idIndex);
	var imgTD = document.getElementById('imgTD_'+idIndex);
	if(imgTD.className.indexOf('active')<0){
		imgTD.className = imgTD.className.concat(' active');	
	}
	img.src = blackCaretImg;
	
	/** Change style class to menuHover **/
	topmenuitem.className = 
		topmenuitem.className.replace(/menuNormal/, 'menuHover');
}

/*
 * When there is a mouseout event in a secondary nav menu item this function
 * is called to not show its dropdown menu (see html in hpweb_horiz_nav.jsp).  
 * The div style class name and the caret image are changed.
 */

function collapse(div, active, blackCaretImg, whiteCaretImg)
{
	var d = div.getElementsByTagName("div").item(0);
	d.className = d.className.replace(/menuHover/, 'menuNormal');

	var topmenuitem = div.getElementsByTagName("a").item(0);

	/** if active button, change back to white caret image;
		otherwise, change back to black caret image.
	 **/
	var idStr = div.id;
	var idIndex = idStr.substring((idStr.indexOf('menuTD_') + 'menuTD_'.length), idStr.length);
	var img = document.getElementById('img_' + idIndex);
	var imgTD = document.getElementById('imgTD_' + idIndex);
	if (active){
		img.src = blackCaretImg;
		if(imgTD.className.indexOf('active') < 0){
			imgTD.className = imgTD.className.contact(' active');
		}
	}else{
		img.src = whiteCaretImg;
		imgTD.className = imgTD.className.replace(/active/, '');
	}
		
	/** Change style class back to menuNormal **/
	topmenuitem.className = 
		topmenuitem.className.replace(/menuHover/, 'menuNormal');

}

/*
 * When there is a mouseover event in a nav menu which has no flyout secondart menus, this function
 * is called to show the highlight effect (see hpweb_horiz_nav.jsp).  
 * The div style class name and the caret image are changed.
 */

function mouseOver(el){
	var className = el.className;
	var parentNode = el.parentNode;
	if(className.indexOf('menuHover') < 0){
		el.className = className.concat(' menuHover');
	}
	if(parentNode.className.indexOf('active') < 0){
		parentNode.className = parentNode.className.concat(' active');
	}
}

/*
 * When there is a mouseout event in a nav menu which has no flyout secondart menus, this function
 * is called to not show the highlight effect (see hpweb_horiz_nav.jsp).  
 * The td style class name and the caret image are changed.
 */
function mouseOut(el, active){
	el.className = el.className.replace(/menuHover/,'');
	var parentNode = el.parentNode;
	if (active){
		if(parentNode.className.indexOf('active') < 0){
			parentNode.className = parentNode.className.contact(' active');
		}
	}else{
		parentNode.className = parentNode.className.replace(/active/, '');
	}
}
