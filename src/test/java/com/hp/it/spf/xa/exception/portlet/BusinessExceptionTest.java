/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.exception.portlet;

import org.springframework.mock.web.portlet.MockRenderRequest;
import com.hp.it.spf.xa.exception.portlet.BusinessException;
import junit.framework.TestCase;

/**
 * The test class: BusinessExceptionTest.
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class BusinessExceptionTest extends TestCase {

	protected void setUp() throws Exception {
	}

	public void testBusinessException() throws Exception {
		MockRenderRequest renderRequest = new MockRenderRequest();
		BusinessException e = new BusinessException(renderRequest, "errorCode",
				"errorMessage");
		assertEquals("errorCode", e.getErrorCode());
		assertEquals("errorMessage", e.getErrorMessage());
		assertEquals(null, e.getCause());
		assertEquals(e.getMessage(), e.getLocalizedMessage());
		assertEquals("Error code: errorCode; Error message: errorMessage", e
				.getLocalizedMessage());

		e = new BusinessException(renderRequest, "errorCode", new Exception(
				"Nested exception"), "errorMessage");
		assertEquals("errorCode", e.getErrorCode());
		assertEquals("errorMessage", e.getErrorMessage());
		assertEquals("Nested exception", e.getCause().getMessage());
		assertEquals(e.getMessage(), e.getLocalizedMessage());
		assertEquals(
				"Error code: errorCode; Error message: errorMessage; Cause: java.lang.Exception: Nested exception",
				e.getLocalizedMessage());

		e = new BusinessException(renderRequest, "errorCode");
		assertEquals("errorCode", e.getErrorCode());
		assertEquals(null, e.getErrorMessage());
		assertEquals(null, e.getCause());
		assertEquals(e.getMessage(), e.getLocalizedMessage());
		assertEquals("Error code: errorCode", e.getLocalizedMessage());

		e = new BusinessException("errorCode", "errorMessage");
		assertEquals("errorCode", e.getErrorCode());
		assertEquals("errorMessage", e.getErrorMessage());
		assertEquals(null, e.getCause());
		assertEquals(e.getMessage(), e.getLocalizedMessage());
		assertEquals("Error code: errorCode; Error message: errorMessage", e
				.getLocalizedMessage());

		e = new BusinessException("errorCode", new Exception("Nested exception"), "errorMessage");
		assertEquals("errorCode", e.getErrorCode());
		assertEquals("errorMessage", e.getErrorMessage());
		assertEquals("Nested exception", e.getCause().getMessage());
		assertEquals(e.getMessage(), e.getLocalizedMessage());
		assertEquals(
				"Error code: errorCode; Error message: errorMessage; Cause: java.lang.Exception: Nested exception",
				e.getLocalizedMessage());

		e = new BusinessException("errorCode");
		assertEquals("errorCode", e.getErrorCode());
		assertEquals(null, e.getErrorMessage());
		assertEquals(null, e.getCause());
		assertEquals(e.getMessage(), e.getLocalizedMessage());
		assertEquals("Error code: errorCode", e.getLocalizedMessage());
	}
}