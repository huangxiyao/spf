package com.hp.globalops.hppcbl.passport;

import java.util.ArrayList;

public class PassportServiceException extends Exception {
	
	private ArrayList faults = null ;

	public PassportServiceException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PassportServiceException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public PassportServiceException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public PassportServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public PassportServiceException(ArrayList faults) {
		setFaults(faults) ;
	}

	public ArrayList getFaults() {
		return faults;
	}

	public void setFaults(ArrayList faults) {
		this.faults = faults;
	}

}
