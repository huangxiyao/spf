package com.hp.it.spf.xa.dc.portal;

/**
 * Callback used to retrieve data associated with the diagnostic context component. It is useful
 * if the actual data retrieval should be deferred until the diagnostic context is rendered.
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public interface DataCallback {
	String getData();
}
