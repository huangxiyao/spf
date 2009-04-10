package com.hp.it.spf.localeselector.portal.component.secondarypagetype;

import com.hp.it.spf.localeselector.portal.component.secondarypagetype.SelectLocaleRedirectProcessAction;

import junit.framework.TestCase;

public class SelectLocaleProcessActionTest extends TestCase {
	private SelectLocaleRedirectProcessAction action; 
	
	protected void setUp() {
		this.action = new SelectLocaleRedirectProcessAction();
	}
	
	public void testFilterUrlLocaleParams() {
		System.out.println("perform testFilterUrlLocaleParams");
		String expected = "/portal/site/test?aparam=dd";
		String actual = action.filterUrlLocaleParams("/portal/site/test?aparam=dd&lang=en&cc=US");
		System.out.println("testFilterUrlLocaleParams.1 got: " + actual);
		assertEquals(expected, actual);
		actual = action.filterUrlLocaleParams("/portal/site/test?lang=en&cc=US&aparam=dd");
		System.out.println("testFilterUrlLocaleParams.2 got: " + actual);
		assertEquals(expected, actual);
		actual = action.filterUrlLocaleParams("/portal/site/test?lang=en&aparam=dd");
		System.out.println("testFilterUrlLocaleParams.3 got: " + actual);
		assertEquals(expected, actual);
	}
	
	public void testFilterUrlLocaleParamsWhenNoParams() {
		System.out.println("perform testFilterUrlLocaleParamsWhenNoParams");
		String expected = "/portal/site/test";
		String actual = action.filterUrlLocaleParams("/portal/site/test");
		System.out.println("testFilterUrlLocaleParamsWhenNoParams.1 got: " + actual);
		assertEquals(expected, actual);
	}
}
