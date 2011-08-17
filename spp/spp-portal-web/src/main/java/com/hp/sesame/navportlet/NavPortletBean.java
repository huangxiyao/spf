// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JSPIncludeBean.java

package com.hp.sesame.navportlet;

import com.epicentric.portalbeans.beans.jspbean.JSPBean;

// Referenced classes of package com.epicentric.portalbeans.beans.jspincludebean:
//            JSPIncludeBeanPageView, JSPIncludeBeanEditView, JSPIncludeBeanAdminEditView, JSPIncludeBeanAdminEditProcessView

public class NavPortletBean extends JSPBean
{
	static final long serialVersionUID = 1L;
	
    public NavPortletBean()
    {
    }

	private String underline;

	private String menuItem;

	private String thin;

	private String bold;

	private String noHeader;

	private String linespacing;

	private String displayDescription;

	private String columns;

	private String backgroundColor;

	private String textColor;
	
	private String underlineLinks;
	
	private String fontSize;

	/**
	 * @return Renvoie linespacing.
	 */
	public String getLinespacing() {
		return linespacing;
	}

	/**
	 * @param linespacing linespacing à définir.
	 */
	public void setLinespacing(String linespacing) {
		this.linespacing = linespacing;
	}

	public String getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(String menuItem) {
		this.menuItem = menuItem;
	}

	public String getUnderline() {
		return underline;
	}

	public void setUnderline(String underline) {
		this.underline = underline;
	}

	public String getThin() {
		return thin;
	}

	public void setThin(String thin) {
		this.thin = thin;
	}

	public String getBold() {
		return bold;
	}

	public void setBold(String bold) {
		this.bold = bold;
	}

	public String getDisplayDescription() {
		return displayDescription;
	}

	public void setDisplayDescription(String displayDescription) {
		this.displayDescription = displayDescription;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getNoHeader() {
		return noHeader;
	}

	public void setNoHeader(String noheader) {
		this.noHeader = noheader;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public String getUnderlineLinks() {
		return underlineLinks;
	}

	public void setUnderlineLinks(String underlineLinks) {
		this.underlineLinks = underlineLinks;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}
}
