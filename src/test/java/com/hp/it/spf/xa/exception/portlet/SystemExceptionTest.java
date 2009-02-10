/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.exception.portlet;

import org.springframework.mock.web.portlet.MockRenderRequest;
import com.hp.it.spf.xa.exception.portlet.SystemException;
import junit.framework.TestCase;

/**
 * The test class: SystemExceptionTest.
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class SystemExceptionTest extends TestCase {

	protected void setUp() throws Exception {
	}

	public void testSystemException() throws Exception {
		MockRenderRequest renderRequest = new MockRenderRequest();
		SystemException e = new SystemException(renderRequest, "errorCode",
				"errorMessage");
		assertEquals("errorCode", e.getErrorCode());
		assertEquals("errorMessage", e.getErrorMessage());
		assertEquals(null, e.getCause());
		assertEquals(e.getMessage(), e.getLocalizedMessage());
		assertEquals("Error code: errorCode; Error message: errorMessage", e
				.getLocalizedMessage());

		e = new SystemException(renderRequest, "errorCode", new Exception(
				"Nested exception"), "errorMessage");
		assertEquals("errorCode", e.getErrorCode());
		assertEquals("errorMessage", e.getErrorMessage());
		assertEquals("Nested exception", e.getCause().getMessage());
		assertEquals(e.getMessage(), e.getLocalizedMessage());
		assertEquals(
				"Error code: errorCode; Error message: errorMessage; Next: java.lang.Exception: Nested exception",
				e.getLocalizedMessage());

		e = new SystemException(renderRequest, "errorCode");
		assertEquals("errorCode", e.getErrorCode());
		assertEquals(null, e.getErrorMessage());
		assertEquals(null, e.getCause());
		assertEquals(e.getMessage(), e.getLocalizedMessage());
		assertEquals("Error code: errorCode", e.getLocalizedMessage());

		e = new SystemException("errorCode", "errorMessage");
		assertEquals("errorCode", e.getErrorCode());
		assertEquals("errorMessage", e.getErrorMessage());
		assertEquals(null, e.getCause());
		assertEquals(e.getMessage(), e.getLocalizedMessage());
		assertEquals("Error code: errorCode; Error message: errorMessage", e
				.getLocalizedMessage());

		e = new SystemException("errorCode", new Exception("Nested exception"), "errorMessage");
		assertEquals("errorCode", e.getErrorCode());
		assertEquals("errorMessage", e.getErrorMessage());
		assertEquals("Nested exception", e.getCause().getMessage());
		assertEquals(e.getMessage(), e.getLocalizedMessage());
		assertEquals(
				"Error code: errorCode; Error message: errorMessage; Next: java.lang.Exception: Nested exception",
				e.getLocalizedMessage());

		e = new SystemException("errorCode");
		assertEquals("errorCode", e.getErrorCode());
		assertEquals(null, e.getErrorMessage());
		assertEquals(null, e.getCause());
		assertEquals(e.getMessage(), e.getLocalizedMessage());
		assertEquals("Error code: errorCode", e.getLocalizedMessage());
	}
}