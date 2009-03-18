package com.hp.it.spf.wsrp.sticky;

import org.apache.axis.MessageContext;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assume.assumeThat;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.HashMap;

import javax.xml.soap.SOAPException;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class StickyHandlerTest
{
	private Map<String, String> mDnsCache;
	private Map<String, String> mCookieHolderMap;
	private TestStickyHandler mHandler;

	@Before
	public void setUp() {
		mDnsCache = new HashMap<String, String>();
		mCookieHolderMap = new HashMap<String, String>();
		StickyHandler.setCookieHolder(new CookieHolder(mCookieHolderMap));
		mHandler = new TestStickyHandler(new TestCache(mDnsCache));
	}

	@Test
	public void testInitCookieSavesSessionId() throws Exception {
		MessageContext messageContext = new MessageContext(null);
		messageContext.setSOAPActionURI("urn:oasis:names:tc:wsrp:v2:initCookie");
		messageContext.setProperty(MessageContext.TRANS_URL, "http://one.two.three.four:12345/someportletapp");

		assumeThat(mCookieHolderMap.get("JSESSIONID=abcdef|one.two.three.four"), nullValue());
		assumeThat(mDnsCache.get("one.two.three.four"), nullValue());

		messageContext.setPastPivot(false);
	    mHandler.invoke(messageContext);

		assertThat("Transport URL",
				(String) messageContext.getProperty(MessageContext.TRANS_URL), 
				is("http://1.2.3.4:12345/someportletapp"));

		messageContext.setPastPivot(true);
		messageContext.setProperty("Cookie", "JSESSIONID=abcdef");
		mHandler.invoke(messageContext);

		assertThat("Saved cookie entry",
				mCookieHolderMap.get("JSESSIONID=abcdef|one.two.three.four"),
				is("1.2.3.4"));
		assertThat("Nothing saved in DNS cache",
				mDnsCache.get("one.two.three.four"), nullValue());
	}

	@Test
	public void testGetMarkupCallJustAfterInitCookie() throws Exception {
		// simulate initCookie call
		MessageContext messageContext = new MessageContext(null);
		messageContext.setSOAPActionURI("urn:oasis:names:tc:wsrp:v2:initCookie");
		messageContext.setProperty(MessageContext.TRANS_URL, "http://one.two.three.four:12345/someportletapp");
		messageContext.setPastPivot(false);
	    mHandler.invoke(messageContext);
		messageContext.setPastPivot(true);
		messageContext.setProperty("Cookie", "JSESSIONID=abcdef");
		mHandler.invoke(messageContext);

		// do getMarkup
		assertThat("Saved cookie entry",
				mCookieHolderMap.get("JSESSIONID=abcdef|one.two.three.four"),
				is("1.2.3.4"));
		assumeThat(mDnsCache.get("one.two.three.four"), nullValue());

		messageContext = new MessageContext(null);
		messageContext.setSOAPActionURI("urn:oasis:names:tc:wsrp:v2:getMarkup");
		messageContext.setProperty(MessageContext.TRANS_URL, "http://one.two.three.four:12345/someportletapp");
		messageContext.setProperty("Cookie", "JSESSIONID=abcdef");
		messageContext.setPastPivot(false);
		mHandler.invoke(messageContext);

		assertThat("Transport URL",
				(String) messageContext.getProperty(MessageContext.TRANS_URL),
				is("http://1.2.3.4:12345/someportletapp"));
		assertThat("Cookie value removed",
				mCookieHolderMap.get("JSESSIONID=abcdef|one.two.three.four"), nullValue());
		assertThat("Value saved in DNS cache",
				mDnsCache.get("one.two.three.four"), is("1.2.3.4"));
	}

	@Test
	public void testPerformBlockingInteractionWithinSession() throws Exception {
		mDnsCache.put("one.two.three.four", "1.2.3.4");
		assumeThat(mCookieHolderMap.get("JSESSIONID=abcdef|one.two.three.four"), nullValue());

		MessageContext messageContext = new MessageContext(null);
		messageContext.setSOAPActionURI("urn:oasis:names:tc:wsrp:v2:getMarkup");
		messageContext.setProperty(MessageContext.TRANS_URL, "http://one.two.three.four:12345/someportletapp");
		messageContext.setProperty("Cookie", "JSESSIONID=abcdef");
		messageContext.setPastPivot(false);
		mHandler.invoke(messageContext);

		assertThat("Transport URL",
				(String) messageContext.getProperty(MessageContext.TRANS_URL),
				is("http://1.2.3.4:12345/someportletapp"));
		assertThat("No cookie value hold",
				mCookieHolderMap.get("JSESSIONID=abcdef|one.two.three.four"), nullValue());
	}


	@Test
	public void testFault() throws Exception {
		mDnsCache.put("one.two.three.four", "1.2.3.4");
		assumeThat(mCookieHolderMap.get("JSESSIONID=abcdef|one.two.three.four"), nullValue());

		// first invoke the handler for request
		MessageContext messageContext = new MessageContext(null);
		messageContext.setSOAPActionURI("urn:oasis:names:tc:wsrp:v2:handleEvents");
		messageContext.setProperty(MessageContext.TRANS_URL, "http://one.two.three.four:12345/someportletapp");
		messageContext.setProperty("Cookie", "JSESSIONID=abcdef");
		messageContext.setPastPivot(false);
		mHandler.invoke(messageContext);

		// setup a messageContext for response which has fault
		messageContext.setPastPivot(true);
		mHandler.setHasFault(true);
		mHandler.invoke(messageContext);

		assertThat("DNS cache entry removed",
				mDnsCache.get("one.two.three.four"), nullValue());
		assertThat("No cookie value hold",
				mCookieHolderMap.get("JSESSIONID=abcdef|one.two.three.four"), nullValue());
	}

	@Test
	public void testHandleEventsAfterFault() throws Exception {
		// after fault the cookies and DNS cache for the session should have been removed
		assumeThat(mDnsCache.get("one.two.three.four"), nullValue());
		assumeThat(mCookieHolderMap.get("JSESSIONID=abcdef|one.two.three.four"), nullValue());

		// invoke the handler to force it to run DNS lookup
		MessageContext messageContext = new MessageContext(null);
		messageContext.setSOAPActionURI("urn:oasis:names:tc:wsrp:v2:handleEvents");
		messageContext.setProperty(MessageContext.TRANS_URL, "http://one.two.three.four:12345/someportletapp");
		messageContext.setProperty("Cookie", "JSESSIONID=abcdef");
		messageContext.setPastPivot(false);
		mHandler.invoke(messageContext);


		assertThat("Transport URL",
				(String) messageContext.getProperty(MessageContext.TRANS_URL),
				is("http://1.2.3.4:12345/someportletapp"));
		assertThat("No cookie value hold",
				mCookieHolderMap.get("JSESSIONID=abcdef|one.two.three.four"), nullValue());
		assertThat("Value saved in DNS cache",
				mDnsCache.get("one.two.three.four"), is("1.2.3.4"));
	}

	private class TestStickyHandler extends StickyHandler {
		private boolean mHasFault = false;

		private TestStickyHandler(IDNSCacheStore store)
		{
			super(store);
		}

		@Override
		String lookup(String targetHost) throws UnknownHostException
		{
			if ("one.two.three.four".equals(targetHost)) {
				return "1.2.3.4";
			}
			throw new UnknownHostException(targetHost);
		}

		@Override
		boolean isSOAPFaultResponse(MessageContext messageContext) throws SOAPException
		{
			return mHasFault;
		}

		public void setHasFault(boolean hasFault)
		{
			mHasFault = hasFault;
		}
	}


	private class TestCache implements IDNSCacheStore {

		private Map<String, String> mCache;

		private TestCache(Map<String, String> cache)
		{
			mCache = cache;
		}

		public Map<String, String> getCache(MessageContext messageContext)
		{
			return mCache;
		}

		public void saveCache(MessageContext messageContext, Map<String, String> dnsCache)
		{
			mCache = dnsCache;
		}
	}

}
