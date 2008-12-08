package com.hp.globalops.hppcbl.passport;

import java.util.ArrayList;
import java.util.Iterator;
import com.hp.globalops.hppcbl.passport.beans.Fault;

public class PassportServiceException extends Exception {

	private ArrayList faults = null;

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
		// For QXCR1000767611: since no Exception message was given, generate
		// one from the faults. DSJ 2008/1/24
		super(faultsToString(faults));
		setFaults(faults);
	}

	public ArrayList getFaults() {
		return faults;
	}

	public void setFaults(ArrayList faults) {
		this.faults = faults;
	}

	// For QXCR1000767611: convert faults to a string that can be used as an
	// Exception message. DSJ 2008/1/24
	private static String faultsToString(ArrayList faults) {
		StringBuffer message = new StringBuffer(16);
		if (faults.size() > 0) {
			Iterator iter = faults.iterator();
			while (iter.hasNext()) {
				Fault aFault = (Fault) iter.next();
				message.append(aFault);
				if (faults.size() > 1)
					message.append("\n");
			}
		}
		return message.toString();
	}
}
