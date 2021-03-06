package com.hp.it.spf.xa.log;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;

import com.hp.it.spf.xa.log.LogHelper;

import junit.framework.TestCase;

public class LogHelperTest extends TestCase {

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}
    
	/**
	 * the key and value are both plain string
	 *
	 */
	@Ignore("Temporary")
	public void testFormatParaInfo1() {
		Map map = new HashMap();
		map.put("key1", "value1");
		map.put("key2", "value2");
		map.put("key3", "value3");
		map.put("key4", "value4");
		map.put("key5", "value5");		
		
		String str = LogHelper.formatParaInfo(map);
		assertNotNull(str);

//		Pattern validPattern = Pattern.compile("key[0-9]: value[0-9] \\| key[0-9]: value[0-9] \\| key[0-9]: value[0-9] \\| key[0-9]: value[0-9] \\| key[0-9]: value[0-9]");
//		Matcher m = validPattern.matcher(str);
//		assertTrue(m.matches());
//		assertEquals(str, "key1: value1 | key3: value3 | key5: value5 | key2: value2 | key4: value4");
	}
	
	/**
	 * the key is plain string and the value is plain string or map object
	 *
	 */
	public void testFormatParaInfo2() {
		Map map = new HashMap();
		
		Map map1 = new HashMap();
		map1.put("key11", "value11");
		map1.put("key12", "value12");
		map1.put("key13", "value13");
		map1.put("key14", "value14");
		map.put("key1", map1);
		
		map.put("key2", "value2");
		map.put("key3", "value3");
		
		Map map4 = new LinkedHashMap();
		map4.put("key41", "value41");
		map4.put("key42", "value42");
		map4.put("key43", "value43");
		
		Map map44 = new LinkedHashMap();
		map44.put("key441", "value441");
		map44.put("key442", "value442");
		
		map4.put("key44", map44);		
		map.put("key4", map4);
		
		map.put("key5", "value5");		
		
		String str = LogHelper.formatParaInfo(map);
		assertNotNull(str);
//		
//		Pattern validPattern = Pattern.compile("(key[0-9]+: \\{( key[0-9]+: value[0-9]+ [\\|]?)+\\})|(key[0-9]+: value[0-9]+)");
//		Matcher m = validPattern.matcher(str);
//		assertTrue(m.matches());
//		assertEquals(str, "key1: { key11: value11 | key14: value14 | key12: value12 | key13: value13 } | key3: value3 | key5: value5 | key2: value2 | key4: { key41: value41 | key42: value42 | key43: value43 | key44: { key441: value441 | key442: value442 } }");
	}	
}
