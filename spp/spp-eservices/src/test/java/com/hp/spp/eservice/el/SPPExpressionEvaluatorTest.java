package com.hp.spp.eservice.el;

import junit.framework.TestCase;

import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.FunctionMapper;
import java.lang.reflect.Method;

public class SPPExpressionEvaluatorTest extends TestCase {

	public void testEmbeddedFunctionCalls() throws Exception {
		SPPExpressionEvaluator ee = new SPPExpressionEvaluator();
		String result = (String) ee.evaluate("${concat(a,concat(b,c))}", String.class,
				new VariableResolver() {
					public Object resolveVariable(String name) throws ELException {
						if ("a".equals(name)) {
							return "A";
						}
						else if ("b".equals(name)) {
							return "B";
						}
						else if ("c".equals(name)) {
							return "C";
						}
						else {
							throw new ELException("Variable not found: " + name);
						}
					}
				},
				new SPPFunctionMapper());
		assertEquals("Result of evaluation", "ABC", result);
	}

	public void testEmbeddedFunctionCalls2() throws Exception {
		SPPExpressionEvaluator ee = new SPPExpressionEvaluator();
		String result = (String) ee.evaluate("${spp:digitalSignature(concat(a,concat(b,c)))}", String.class,
				new VariableResolver() {
					public Object resolveVariable(String name) throws ELException {
						if ("a".equals(name)) {
							return "A";
						}
						else if ("b".equals(name)) {
							return "B";
						}
						else if ("c".equals(name)) {
							return "C";
						}
						else {
							throw new ELException("Variable not found: " + name);
						}
					}
				},
				new FunctionMapper() {
					public Method resolveFunction(String prefix, String name) {
						try {
							if ("spp".equals(prefix) && "digitalSignature".equals(name)) {
								return SPPExpressionEvaluatorTest.class.getMethod("digitalSignature", new Class[] {String.class});
							}
							else if ((prefix == null || "".equals(prefix)) && "concat".equals(name)) {
								return SPPExpressionEvaluatorTest.class.getMethod("concat", new Class[] {String.class, String.class});
							}
							else {
								return null;
							}
						}
						catch (NoSuchMethodException e) {
							throw new IllegalStateException("Method not found: " + e);
						}
					}
				});
		assertEquals("Result of evaluation", "x_ABC_x", result);
	}

	public static String concat(String s1, String s2) {
		return s1.concat(s2);
	}

	public static String digitalSignature(String s) {
		return "x_" + s + "_x";
	}
}
