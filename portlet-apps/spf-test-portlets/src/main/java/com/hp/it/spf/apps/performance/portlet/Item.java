package com.hp.it.spf.apps.performance.portlet;

import java.util.Date;

public class Item {
	private int mId;
	private String mLabel;
	private Date mDate;
	private String mDescription;

	public Item(int id, Date date, String label, String description) {
		mId = id;
		mDate = date;
		mLabel = label;
		mDescription = description;
	}


	public int getId() {
		return mId;
	}

	public Date getDate() {
		return mDate;
	}

	public String getLabel() {
		return mLabel;
	}

	public String getDescription() {
		return mDescription;
	}

}
