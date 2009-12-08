package com.hp.it.spf.request.portlet.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.filter.FilterConfig;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.hamcrest.core.Is.*;

import com.hp.it.spf.xa.misc.Consts;

/**
 * This test class checks RequestParameterExtractor class functionalities.
 * Test cases cover the following aspects.
 * <p>paramOverwrite - filter configuration to allow overwrite capability.
 * <p>parameterExtraction - parameters are extracted from the portalURL. 
 * <p>mal-formed URL cases- incase user has manually added in PortalURL , this case can occur.
 * <p>empty parameters 
 * 
 * @author pranav
 *
 */
@RunWith(JMock.class)
public class RequestParamExtractorFilterTest {
    
	private final Mockery mContext = new JUnit4Mockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
	}};    

    /**
     * Ensures the parameters retrieved from the portal URL are as expected. 
     */
    @Test
    public void testParameterExtractionFromPortalURL(){

    	final PortletRequest request = mContext.mock(PortletRequest.class);
    	final PortletResponse response = mContext.mock(PortletResponse.class);
    	final RequestParamExtractorFilter filter = new RequestParamExtractorFilter();
    	final Map<String,String> contextMap = new HashMap<String,String>();

    	//given
    	contextMap.put(Consts.KEY_PORTAL_REQUEST_URL,"/portal/site/test/?param1=value1&param2=value2&param3=value3");
    	mContext.checking(new Expectations(){{
    		allowing(request).getAttribute(Consts.PORTAL_CONTEXT_KEY) ; will(returnValue(contextMap));	    
    	}});

    	//when
    	Map<String, String[]> paramMap = filter.extractParametersFromPortalURL(request);

    	//then
    	assertEquals("1. Extracted param not present", true, paramMap.containsKey("param1"));
    	assertEquals("2. Extracted param not present", true, paramMap.containsKey("param2"));
    	assertEquals("3. Extracted param not present", true, paramMap.containsKey("param3"));

    	//given
    	contextMap.put(Consts.KEY_PORTAL_REQUEST_URL,"/portal/site/test/?");
    	mContext.checking(new Expectations(){{
    		allowing(request).getAttribute(Consts.PORTAL_CONTEXT_KEY) ; will(returnValue(contextMap));	    
    	}});

    	//when
    	paramMap = filter.extractParametersFromPortalURL(request);	

    	//then
    	assertEquals("4. Empty map should be returned test failed ", true, paramMap.isEmpty());

    	//given
    	contextMap.put(Consts.KEY_PORTAL_REQUEST_URL,"/portal/site/test/?color=red&color=blue");
    	mContext.checking(new Expectations(){{
    		allowing(request).getAttribute(Consts.PORTAL_CONTEXT_KEY) ; will(returnValue(contextMap));	    
    	}});

    	//when
    	paramMap = filter.extractParametersFromPortalURL(request);	

    	//then
    	assertEquals("5. Same parameter name test failed", 1 , paramMap.keySet().size());
    	assertEquals("6. First parameter value should be returned failed " , "red", paramMap.get("color")[0]);
    	assertNotSame("7. First parameter value should be returned failed", "blue", paramMap.get("color")[0]);

    }
    /**
     * This method ensures that in-case of mal-formed URL, The map returned would be empty.
     * We do not add/overwrite any of the attributes in this case.
     * 
     */
    @Test
    public void testParameterExtractionFromPortalURLMalformedCases(){
    	
    	final PortletRequest request = mContext.mock(PortletRequest.class);
    	final PortletResponse response = mContext.mock(PortletResponse.class);
    	final RequestParamExtractorFilter filter = new RequestParamExtractorFilter();
    	final Map<String,String> contextMap = new HashMap<String,String>();
    	
    	//when
    	contextMap.put(Consts.KEY_PORTAL_REQUEST_URL,"/portal/site/test/??");
    	mContext.checking(new Expectations(){{
    		allowing(request).getAttribute(Consts.PORTAL_CONTEXT_KEY) ; will(returnValue(contextMap));	    
    	}});
    	Map<String, String[]> paramMap = filter.extractParametersFromPortalURL(request);
    	//then
    	assertEquals("1. MalFormed URL case failed , Map should be empty : ", true, paramMap.isEmpty());
    	
    	//when
    	contextMap.put(Consts.KEY_PORTAL_REQUEST_URL,"/portal/site/test/?param=value&param&");
    	mContext.checking(new Expectations(){{
    		allowing(request).getAttribute(Consts.PORTAL_CONTEXT_KEY) ; will(returnValue(contextMap));	    
    	}});
    	paramMap = filter.extractParametersFromPortalURL(request);	
    	//then
    	assertEquals("2. MalFormed URL case failed , Map should be empty : ", true, paramMap.isEmpty());
    	
    	//when
    	contextMap.put(Consts.KEY_PORTAL_REQUEST_URL,"/portal/site/test/?param&");
    	mContext.checking(new Expectations(){{
    		allowing(request).getAttribute(Consts.PORTAL_CONTEXT_KEY) ; will(returnValue(contextMap));	    
    	}});
    	paramMap = filter.extractParametersFromPortalURL(request);	
    	//then
    	assertEquals("3. MalFormed URL case failed , Map should be empty : ", true, paramMap.isEmpty());

    	
    }
    
    
    @SuppressWarnings("cast")
	@Test
    public void testAddParametersToAttribute(){
    	
    	final PortletRequest request = mContext.mock(PortletRequest.class);
    	final PortletResponse response = mContext.mock(PortletResponse.class);
    	final RequestParamExtractorFilter filter = new RequestParamExtractorFilter();
    	final FilterConfig filterConfig = mContext.mock(FilterConfig.class);
    	final Map<String,String> contextMap = new HashMap<String,String>();
    	contextMap.put(Consts.KEY_PORTAL_REQUEST_URL,"/portal/site/test/?param1=value1&param2=value2&param3=value3");
    	
    	try{
    	    filter.init(filterConfig);
    	}catch(PortletException e){
    	    
    	}		
    	
    	mContext.checking(new Expectations(){{
    	    allowing(filterConfig).getInitParameter("paramOverwrite") ; will(returnValue("true"));
    	    allowing(request).getAttribute(Consts.PORTAL_CONTEXT_KEY) ; will(returnValue(contextMap));
    	    exactly(3).of(request).setAttribute( with(any(String.class)), with(any(Object.class)));
    	}});
    	
    	filter.addParametersToAttribute(request);
    	
    }
    
    /**
     * This method ensures that overwrite capabilities are working as expected.
     * First we populate the request object with "param".
     * "paramOverwrite" is false - the parameter is not overwritten.
     * "paramOverwrite" is true - the parameter is overwritten.[ setAttribute is invoked in that case ] 
     */
    @SuppressWarnings("unchecked")
	@Test
    public void testAddParametersToAttributeOverwriteCapability(){
    	
    	final PortletRequest request = mContext.mock(PortletRequest.class);
    	final PortletResponse response = mContext.mock(PortletResponse.class);
    	final RequestParamExtractorFilter filter = new RequestParamExtractorFilter();
    	final FilterConfig filterConfig = mContext.mock(FilterConfig.class);
    	final Collection<String> c = new ArrayList<String>();
    	c.add("param");
    	final Map<String,String> contextMap = new HashMap<String,String>();
    	contextMap.put(Consts.KEY_PORTAL_REQUEST_URL,"/portal/site/test/?param=overwritten");
    	
    	try{
    	    filter.init(filterConfig);
    	}catch(PortletException e){
    	    
    	}		
    	
    	//given
    	mContext.checking(new Expectations(){{
    	    allowing(filterConfig).getInitParameter("paramOverwrite") ; will(returnValue("false"));
    	    allowing(request).getAttribute(Consts.PORTAL_CONTEXT_KEY) ; will(returnValue(contextMap));
    	    allowing(request).getAttributeNames(); will(returnValue(Collections.enumeration(c)));
    	    never(request).setAttribute( with(any(String.class)), with(any(Object.class)));
    	}});
    	
    	//when
    	filter.addParametersToAttribute(request);
    	
    	//given
    	mContext.checking(new Expectations(){{
    	    allowing(filterConfig).getInitParameter("paramOverwrite") ; will(returnValue("true"));
    	    allowing(request).getAttribute(Consts.PORTAL_CONTEXT_KEY) ; will(returnValue(contextMap));
    	    exactly(1).of(request).setAttribute( with("param"), with(new String[]{"overwritten"}));
    	}});
    	
    	//when
    	filter.addParametersToAttribute(request);  	
    	
    }
    
}
