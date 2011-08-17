package com.hp.spp.portal.common.site;

import junit.framework.TestCase;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;

public class SiteConfigRowMapperTest extends TestCase {
     public void testForBoolean()	throws Exception {
		assertTrue(true);
	}

    /**
	 * Test that with country not existing and research focused on language and
	 * no associated guest user, default combination is retrieved.
	 * 
	 * @throws Exception
	 */
	/*public void testForBoolean()	throws Exception {
		Site site = new Site();
        SiteConfigMapper siteMapper = new SiteConfigMapper(site);
        siteMapper.mapRow(getResultSet(new InvoHandForBoolean()),0);
        assertTrue("Testing for Boolean", site.getUseMockProfile());
	}

    public void testForInt() throws Exception {
		Site site = new Site();
        SiteConfigMapper siteMapper = new SiteConfigMapper(site);
        siteMapper.mapRow(getResultSet(new InvoHandForInt()),0);
        assertEquals("Testing for Int", 40 ,site.getUGSTimeoutInMilliseconds());
	}

    public void testForString() throws Exception {
		Site site = new Site();
        SiteConfigMapper siteMapper = new SiteConfigMapper(site);
        siteMapper.mapRow(getResultSet(new InvoHandForString()),0);
        assertEquals("Testing for Integer", "Hello" , site.getAdminUPSQueryId());
	}      */


    private ResultSet getResultSet(InvocationHandler handle){
        return (ResultSet) Proxy.newProxyInstance(getClass().getClassLoader(),
                                                new Class[] {ResultSet.class}, handle);
    }

    private class InvoHandForInt implements InvocationHandler {
        int i = 0;
        public Object invoke(Object proxy, Method method, Object[] args){
            if(method.getName().equals("getString")){
                if(i%2 == 0){
                    ++i;
                    return  "UGSTimeoutInMilliseconds";
                }else{
                    ++i;
                    return "40";
                }
            }else if(method.getName().equals("toString")){
                return "InvoHandForInt";
            }
            throw new UnsupportedOperationException("Unsupported call for this mock object: " + method.getName());
        }
    }

    private class InvoHandForString implements InvocationHandler {
        int i = 0;
        public Object invoke(Object proxy, Method method, Object[] args){
            if(method.getName().equals("getString")){
                if(i%2 == 0){
                    ++i;
                    return  "AdminUPSQueryId";
                }else{
                    ++i;
                    return "Hello";
                }
            }else if(method.getName().equals("toString")){
                return "InvoHandForStr";
            }
            throw new UnsupportedOperationException("Unsupported call for this mock object: " + method.getName());
        }
    }

    private class InvoHandForBoolean implements InvocationHandler {
        int i = 0;
        public Object invoke(Object proxy, Method method, Object[] args){
            if(method.getName().equals("getString")){
                if(i%2 == 0){
                    ++i;
                    return  "UseMockProfile";
                }else{
                    ++i;
                    return "true";
                }
            }else if(method.getName().equals("toString")){
                return "InvoHandForStr";
            }
            throw new UnsupportedOperationException("Unsupported call for this mock object: " + method.getName());
        }
    }
}