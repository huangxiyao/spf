/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.exception.portlet;

import org.springframework.mock.web.portlet.MockRenderRequest;
import com.hp.it.spf.xa.exception.portlet.SystemException;
import com.hp.it.spf.xa.exception.portlet.BusinessException;
import com.hp.it.spf.xa.exception.portlet.ExceptionUtil;
import junit.framework.TestCase;

/**
 * The test class: SystemExceptionTest.
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ExceptionUtilTest extends TestCase {

	protected void setUp() throws Exception {
	}

	public void testExceptionUtil() throws Exception {
		MockRenderRequest request = new MockRenderRequest();
		ExceptionUtil.setException(request, new BusinessException("errorCode", new Exception("Nested exception"), "errorMessage"));
		BusinessException e = (BusinessException) ExceptionUtil.getException(request);
		assertEquals("errorCode", e.getErrorCode());
		assertEquals("errorMessage", e.getErrorMessage());
		assertEquals("Error code: errorCode; Error message: errorMessage; Cause: java.lang.Exception: Nested exception",
				e.getLocalizedMessage());
		assertEquals(true, ExceptionUtil.containsBusinessException(request));
		assertEquals(false, ExceptionUtil.containsSystemException(request));
		assertEquals(true, ExceptionUtil.containsOtherException(request));
		String[] errorCodes = ExceptionUtil.getErrorCodes(request, "defaultErrorCode");
		assertEquals(2, errorCodes.length);
		assertEquals("errorCode", errorCodes[0]);
		assertEquals("defaultErrorCode", errorCodes[1]);
		String[] messages = ExceptionUtil.getLocalizedMessages(request, "defaultMessage");
		assertEquals(2, messages.length);
		assertEquals("defaultMessage", messages[0]);
		assertEquals("defaultMessage", messages[1]);
		errorCodes = ExceptionUtil.getErrorCodes(request);
		assertEquals(2, errorCodes.length);
		assertEquals("errorCode", errorCodes[0]);
		assertEquals(null, errorCodes[1]);
		messages = ExceptionUtil.getLocalizedMessages(request);
		assertEquals(2, messages.length);
		assertEquals(null, messages[0]);
		assertEquals(null, messages[1]);
		String errorCode = ExceptionUtil.getErrorCode(request, "defaultCode");
		assertEquals("errorCode", errorCode);
		String message = ExceptionUtil.getLocalizedMessage(request, "defaultMessage");
		assertEquals("defaultMessage", message);

		ExceptionUtil.setException(request, new Exception("Not an SPFException"));
		Exception ex = ExceptionUtil.getException(request);
		assertEquals(false, ExceptionUtil.containsBusinessException(request));
		assertEquals(false, ExceptionUtil.containsSystemException(request));
		assertEquals(true, ExceptionUtil.containsOtherException(request));
		errorCodes = ExceptionUtil.getErrorCodes(request, "defaultErrorCode");
		assertEquals(1, errorCodes.length);
		assertEquals("defaultErrorCode", errorCodes[0]);
		messages = ExceptionUtil.getLocalizedMessages(request, "defaultMessage");
		assertEquals(1, messages.length);
		assertEquals("defaultMessage", messages[0]);
	}
}