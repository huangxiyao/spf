package com.hp.spp.wsrp.export.exception;

import com.hp.spp.wsrp.export.component.BadComponent;

public class ComponentException extends Exception {

	private static final long serialVersionUID = 9168022911524425566L;
	private BadComponent mComponent = null;

	public ComponentException() {
	}

	public ComponentException(String message) {
		super(message);
	}

	public ComponentException(Throwable cause) {
		super(cause);
	}

	public ComponentException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComponentException(BadComponent component) {
		mComponent  = component ;
	}
	
	public BadComponent getBadComponent() {
		return mComponent ;
	}

}
