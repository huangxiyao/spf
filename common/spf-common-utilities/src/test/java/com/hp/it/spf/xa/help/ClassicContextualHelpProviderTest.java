/**
 * 
 */
package com.hp.it.spf.xa.help;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author joey.sebestyen@hp.com
 *
 */
public class ClassicContextualHelpProviderTest {

	private static final String HELP_CONTENT = "This is the classic contextual help provider contect";

	/**
	 * Test method for {@link com.hp.it.spf.xa.help.ClassicContextualHelpProvider#getHTML(boolean)}.
	 */
	@Test
	public void thatGetHtmlReturnsSomething() {
		TestAbstractClassicContetualHelpProvider tacchp = new TestAbstractClassicContetualHelpProvider();
		String helpHtml = tacchp.getHTML(false);
		assertNotNull(helpHtml);
	}

	/**
	 * Test method for {@link com.hp.it.spf.xa.help.ClassicContextualHelpProvider#getHTML(boolean)}.
	 */
	@Test
	public void thatGetHtmlReturnsExpectedContent(){
		TestAbstractClassicContetualHelpProvider tacchp = new TestAbstractClassicContetualHelpProvider();
		tacchp.setHelpContent(HELP_CONTENT);
		String helpHtml = tacchp.getHTML(false);
		assertTrue(helpHtml.contains(HELP_CONTENT));
	}
	
	/**
	 * Test method for {@link com.hp.it.spf.xa.help.ClassicContextualHelpProvider#getHTML(boolean)}.
	 */
	@Test
	public void thatGetHtmlReturnsDistinctIdsOnSubsequentCallsUsingIndependentCounter(){
		TestAbstractClassicContetualHelpProvider tacchpFirst = new TestAbstractClassicContetualHelpProvider();
		TestAbstractClassicContetualHelpProvider tacchpSecond = new TestAbstractClassicContetualHelpProvider();
		TestAbstractClassicContetualHelpProvider tacchpThird = new TestAbstractClassicContetualHelpProvider();

		assertTrue(tacchpFirst.getClassicContextualHelpCounter() == 0);
		assertTrue(tacchpSecond.getClassicContextualHelpCounter() == 0);
		assertTrue(tacchpThird.getClassicContextualHelpCounter() == 0);
		
		tacchpFirst.setHelpContent(HELP_CONTENT);
		String helpHtml = tacchpFirst.getHTML(false);
		assertTrue(helpHtml.contains(HELP_CONTENT));
		assertTrue(tacchpFirst.getClassicContextualHelpCounter() == 1);
		assertTrue(tacchpSecond.getClassicContextualHelpCounter() == 0);
		assertTrue(tacchpThird.getClassicContextualHelpCounter() == 0);
		
		tacchpSecond.setHelpContent(HELP_CONTENT);
		String helpHtmlSecond = tacchpSecond.getHTML(false);
		assertTrue(helpHtmlSecond.contains(HELP_CONTENT));
		assertTrue(tacchpFirst.getClassicContextualHelpCounter() == 1);
		assertTrue(tacchpSecond.getClassicContextualHelpCounter() == 1);
		assertTrue(tacchpThird.getClassicContextualHelpCounter() == 0);
		
		tacchpThird.setHelpContent(HELP_CONTENT);
		String helpHtmlThird = tacchpThird.getHTML(false);
		assertTrue(helpHtmlThird.contains(HELP_CONTENT));
		assertTrue(tacchpFirst.getClassicContextualHelpCounter() == 1);
		assertTrue(tacchpSecond.getClassicContextualHelpCounter() == 1);
		assertTrue(tacchpThird.getClassicContextualHelpCounter() == 1);
		
		
		assertTrue(!helpHtml.equalsIgnoreCase(helpHtmlSecond));
		assertTrue(!helpHtml.equalsIgnoreCase(helpHtmlThird));
		assertTrue(!helpHtmlSecond.equalsIgnoreCase(helpHtmlThird));
		
	}
	
	private class TestAbstractClassicContetualHelpProvider extends ClassicContextualHelpProvider{
		
		private int counter = 0;
		
		protected TestAbstractClassicContetualHelpProvider(){
			
		}

		@Override
		protected void bumpClassicContextualHelpCounter() {
			counter++;
		}

		@Override
		protected int getClassicContextualHelpCounter() {
			return counter;
		}

		@Override
		protected String getCloseImageAlt() {
			return("closeImageAlt");
		}

		@Override
		protected String getCloseImageURL() {
			return("closeImageURL");
		}

		@Override
		protected String getNoScriptURL() {
			return("noScriptURL");
		}

		@Override
		protected void resetClassicContextualHelpCounter() {
			counter = 0;
		}
	}

}
