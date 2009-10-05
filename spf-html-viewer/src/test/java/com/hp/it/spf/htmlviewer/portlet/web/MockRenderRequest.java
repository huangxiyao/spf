package com.hp.it.spf.htmlviewer.portlet.web;

/**
 * The Spring JSR-286 release is not final yet as of this writing - and we need
 * a JSR-286 Spring mock method, getWindowID - so rather than introduce a
 * dependency on Spring Beta code, we just have our own wrapper class for this.
 */
public class MockRenderRequest extends
		org.springframework.mock.web.portlet.MockRenderRequest {

	public String getWindowID() {
		return "12345";
	}
}
