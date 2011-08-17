package com.hp.spp.common.exception;

/**
 * Exception that must be thown when an forbidden back in the browser has been performed
 * @author MJULIENS
 *
 */
public class BrowserBackException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BrowserBackException (){
		super();
	}
	
	public BrowserBackException(String message){
		super(message);
	}

}
