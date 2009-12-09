
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
	var img = topmenuitem.getElementsByTagName("img").item(1);
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
	var img = topmenuitem.getElementsByTagName("img").item(1);
	if (active)
		img.src = blackCaretImg;
	else
		img.src = whiteCaretImg;

	/** Change style class back to menuNormal **/
	topmenuitem.className = 
		topmenuitem.className.replace(/menuHover/, 'menuNormal');

}

