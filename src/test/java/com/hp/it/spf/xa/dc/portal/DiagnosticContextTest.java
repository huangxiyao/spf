package com.hp.it.spf.xa.dc.portal;

import junit.framework.TestCase;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class DiagnosticContextTest extends TestCase
{
	public void testEmpty() {
		DiagnosticContext dc = new DiagnosticContext();
		assertTrue("Is empty before adding data", dc.isEmpty());
		assertFalse("Has errors before adding data", dc.hasErrors());
	}

	public void testSetMethods() {
		DiagnosticContext dc = new DiagnosticContext();
		dc.set("A", "1");
		dc.set("B", new DataCallback() { public String getData() { return "2"; } });
		dc.setError(ErrorCode.AUTH001, "3");

		assertFalse("Is empty after adding data", dc.isEmpty());
		assertTrue("Has error after adding error", dc.hasErrors());
		assertEquals("Data in the context", "A=1;B=2;ERROR=AUTH001=3", dc.toString());
	}

	public void testCopyFrom() {
		DiagnosticContext dc = new DiagnosticContext();
		dc.set("A", "1");
		dc.set("B", new DataCallback() { public String getData() { return "2"; } });
		dc.setError(ErrorCode.AUTH001, "3");

		DiagnosticContext dc2 = new DiagnosticContext();
		dc2.copyFrom(dc);

		assertFalse("Is empty after copying data", dc2.isEmpty());
		assertTrue("Has errors after copying data with errors", dc2.hasErrors());
		assertEquals("Data in the context after copy", "A=1;B=2;ERROR=AUTH001=3", dc2.toString());
	}

	public void testCloneErrorsOnly() {
		DiagnosticContext dc = new DiagnosticContext();
		dc.set("A", "1");
		dc.set("B", new DataCallback() { public String getData() { return "2"; } });
		dc.setError(ErrorCode.AUTH001, "3");

		DiagnosticContext dc2 = dc.cloneErrorsOnly();
		assertFalse("Is empty after clonying errors only as source contained errors", dc2.isEmpty());
		assertTrue("Has errors after cloning", dc2.hasErrors());
		assertEquals("Data in the context after cloning", "ERROR=AUTH001=3", dc2.toString());

	}
}
