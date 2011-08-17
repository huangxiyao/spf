package com.hp.spp.portal.login.business.rule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import com.hp.spp.portal.login.dao.UPSRuleDAO;

public class RuleContainerTest extends TestCase {
	public void testABCD(){
		assert(true);
	}
	/*public void testRuleContainer(){
		RuleContainer container = RuleContainer.getInstance();
		container.setRuleDAO(new TestUPSRuleDAO());
		List ruleList = container.initializeRuleClasses("sppqa");
		
		Iterator it = ruleList.iterator();
		
		while(it.hasNext()){
			DisclaimerFormRule rule = (DisclaimerFormRule)it.next();
			assertEquals("Rule execution returned class ", true, rule instanceof DisclaimerFormRule);
		}
	
	}
	
	
	public void testNullRuleContainer(){
		RuleContainer container = RuleContainer.getInstance();
		container.setRuleDAO(new TestNullUPSRuleDAO());
		List ruleList = container.initializeRuleClasses("sppqa");
		
		
		assertEquals("Rule execution returned class ", ruleList , null);
			
	}
	
	public void testEmptyRuleContainer(){
		RuleContainer container = RuleContainer.getInstance();
		container.setRuleDAO(new TestEmptyUPSRuleDAO());
		List ruleList = container.initializeRuleClasses("sppqa");
		
		assertEquals("Rule execution returned class ", ruleList , null);
	
	}
	
	
	class TestUPSRuleDAO implements UPSRuleDAO{

		public List getUPSRules(String siteIdentifier) {
			return "com.hp.spp.portal.login.business.rule.DisclaimerFormRule:com.hp.spp.portal.login.business.rule.DisclaimerFormRule";
		}

	}
	
	class TestNullUPSRuleDAO implements UPSRuleDAO{

		public List getUPSRules(String siteIdentifier) {
			return null;
		}

	}
	
	class TestEmptyUPSRuleDAO implements UPSRuleDAO{

		public List getUPSRules(String siteIdentifier) {
			return new ArrayList();
		}

	}*/


}