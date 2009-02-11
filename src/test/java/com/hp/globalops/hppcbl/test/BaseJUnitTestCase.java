package com.hp.globalops.hppcbl.test;

import com.hp.globalops.hppcbl.passport.PassportService;

import junit.framework.TestCase;

public class BaseJUnitTestCase extends TestCase {

    protected PassportService ws = null;
	public BaseJUnitTestCase() {

    	ws = new PassportService();
    }
	
	protected void setUp() throws Exception {
        super.setUp();
        
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        
    }
}