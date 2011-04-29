package com.hp.it.spf.navportlet.portal.component.portalbean;

import com.epicentric.portalbeans.beans.jspbean.JSPBean;

public class NavPortletBean extends JSPBean {
	static final long serialVersionUID = 1L;

	private String underline;
	private String menuItem;
	private String thin;
	private String bold;
	private String noHeader;
	private String linespacing;
	private String displayDescription;
	private String columns;
	private String backgroundColor;
	private String descriptionBackgroundColor;
	private String textColor;
	private String underlineLinks;
	private String fontSize;

	public NavPortletBean() {
	}

	public String getUnderline() {
		return underline;
	}

	public void setUnderline(String underline) {
		this.underline = underline;
	}

	public String getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(String menuItem) {
		this.menuItem = menuItem;
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

	public String getNoHeader() {
		return noHeader;
	}

	public void setNoHeader(String noHeader) {
		this.noHeader = noHeader;
	}

	public String getLinespacing() {
		return linespacing;
	}

	public void setLinespacing(String linespacing) {
		this.linespacing = linespacing;
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

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getDescriptionBackgroundColor() {
		return descriptionBackgroundColor;
	}

	public void setDescriptionBackgroundColor(String descriptionBackgroundColor) {
		this.descriptionBackgroundColor = descriptionBackgroundColor;
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
