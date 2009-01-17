package com.hp.it.spf.localeselector.portal.component.secondarypagetype;

import com.hp.it.spf.localeselector.portal.component.secondarypagetype.SelectLocaleRedirectProcessAction;

import junit.framework.TestCase;

public class SelectLocaleProcessActionTest extends TestCase {
	private SelectLocaleRedirectProcessAction action; 
	
	protected void setUp() {
		this.action = new SelectLocaleRedirectProcessAction();
	}
	
	public void testFilterUrlLocaleParams() {
		String expected = "/portal/site/test?aparam=dd";
		String actual = action.filterUrlLocaleParams("/portal/site/test?aparam=dd&lang=en&cc=US");
		assertEquals(expected, actual);
		actual = action.filterUrlLocaleParams("/portal/site/test?lang=en&cc=US&aparam=dd");
		assertEquals(expected, actual);
		actual = action.filterUrlLocaleParams("/portal/site/test?lang=en&aparam=dd");
		assertEquals(expected, actual);
	}
	
	public void testFilterUrlLocaleParamsWhenNoParams() {
		String expected = "/portal/site/test";
		String actual = action.filterUrlLocaleParams("/portal/site/test");
		assertEquals(expected, actual);
	}

}
