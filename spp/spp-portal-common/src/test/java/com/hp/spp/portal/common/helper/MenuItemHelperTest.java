package com.hp.spp.portal.common.helper;

import junit.framework.TestCase;

import com.hp.spp.portal.common.helper.MenuItemHelper;

/**
 * @author Girish Keshavamurthy (girish.keshavamurthy@hp.com)
 */
public class MenuItemHelperTest extends TestCase {
	private MenuItemHelper mHelper = new MenuItemHelper();


	public void testStripVignetteSpanTagsForNullString(){
		assertNull(mHelper.stripVignetteSpanTags(null));
	}

	public void testStripVignetteSpanTagsForEmptyString() {
		String emptyString = "";
		assertEquals(emptyString, mHelper.stripVignetteSpanTags(emptyString));
	}

	public void testStripVignetteSpanTagsForStringWithoutSpan() {
		String stringWithoutSpan = "Product Catalog";
		assertEquals(stringWithoutSpan, mHelper.stripVignetteSpanTags(stringWithoutSpan));
	}

	public void testStripVignetteSpanTagsForUnescapedSingleLineSpan() {
		String textWithUnescapedSpan = "<SPAN lang='en' xml:lang='en'>Standard Pricing Viewer </SPAN>";
		assertEquals("Standard Pricing Viewer", mHelper.stripVignetteSpanTags(textWithUnescapedSpan));

		String textWithUnescapedSpan2 = "<SPAN lang=\"en\" xml:lang=\"en\"'>Standard Pricing Viewer </SPAN>";
		assertEquals("Standard Pricing Viewer", mHelper.stripVignetteSpanTags(textWithUnescapedSpan2));
	}

	public void testStripVignetteSpanTagsForUnescapedMultiLineSpan() {
		String textWithUnescapedSpan = "<SPAN lang='en' xml:lang='en'>Standard \nPricing\n Viewer\n </SPAN>";
		assertEquals("Standard \nPricing\n Viewer", mHelper.stripVignetteSpanTags(textWithUnescapedSpan));

		String textWithUnescapedSpan2 = "<SPAN lang=\"en\" xml:lang=\"en\"'>Standard \n Pricing\n Viewer\n </SPAN>";
		assertEquals("Standard \n Pricing\n Viewer", mHelper.stripVignetteSpanTags(textWithUnescapedSpan2));
	}

	public void testStripVignetteSpanTagsForEscapedSingleLineSpan() {
		String textWithSpanInside = "Excellent &lt;SPAN lang=&#034;en&#034; xml:lang=&#034;en&#034;&gt;Standard Pricing Viewer &lt;/SPAN&gt; provided by HP";
		assertEquals("Excellent Standard Pricing Viewer  provided by HP", mHelper.stripVignetteSpanTags(textWithSpanInside));

		String textWithSpanInside2 = "Excellent &lt;SPAN lang=&quot;en&quot; xml:lang=&quot;en&quot;&gt;Standard Pricing Viewer &lt;/SPAN&gt; provided by HP";
		assertEquals("Excellent Standard Pricing Viewer  provided by HP", mHelper.stripVignetteSpanTags(textWithSpanInside2));
	}
	
	public void testStripVignetteSpanTagsForEscapedMultiLineSpan(){
		String vignetteTagText = "&lt;SPAN lang=&#034;en&#034; xml:lang=&#034;en&#034;&gt;Standard Pricing Viewer" + "\n" +" &lt;/SPAN&gt;";
		assertEquals("Standard Pricing Viewer", mHelper.stripVignetteSpanTags(vignetteTagText));

		String vignetteTagText2 = "&lt;SPAN lang=&quot;en&quot; xml:lang=&quot;en&quot;&gt;Standard Pricing Viewer" + "\n" +" &lt;/SPAN&gt;";
		assertEquals("Standard Pricing Viewer", mHelper.stripVignetteSpanTags(vignetteTagText2));
	}

}