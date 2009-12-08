package com.hp.it.spf.xa.portletdata.portal;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.epicentric.common.website.MenuItemNode;
import com.epicentric.navigation.MenuItem;
import com.epicentric.page.Page;
import com.epicentric.site.Site;
import com.hp.it.spf.xa.misc.Consts;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
@RunWith(JMock.class)
public class PortletDataCollectorTest
{
	private final Mockery mContext = new JUnit4Mockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
	}};

	private final PortletDataCollector mCollector = new PortletDataCollector();

	@Test
	public void testRetrieveUserProfileWhenNoProfileInSession() {
		final HttpSession session = mContext.mock(HttpSession.class);
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		mContext.checking(new Expectations() {{
			allowing(session).getAttribute(Consts.USER_PROFILE_KEY); will(returnValue(null));
			allowing(session).getAttribute("StandardParameters"); will(returnValue(null));
			allowing(request).getSession(with(true)); will(returnValue(session));
		}});

		assertThat("User Profile map returned is null if none exists in session",
				mCollector.retrieveUserProfile(request), nullValue());
	}

	@Test
	public void testRetrieveUserProfile() {
		final Map<String, String> profile = new HashMap<String, String>();
		profile.put("FirstName", "John");
		profile.put("LastName", "Doe");
		final HttpSession session = mContext.mock(HttpSession.class);
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		mContext.checking(new Expectations() {{
			allowing(session).getAttribute(Consts.USER_PROFILE_KEY); will(returnValue(profile));
			allowing(request).getSession(with(true)); will(returnValue(session));
		}});

		@SuppressWarnings("unchecked") HashMap<String, String> retrievedProfile =
				(HashMap<String, String>) mCollector.retrieveUserProfile(request);
		assertThat("User profile", retrievedProfile, sameInstance(profile));
		assertThat("FirstName still present", retrievedProfile.get("FirstName"), is("John"));
		assertThat("LastName still present", retrievedProfile.get("LastName"), is("Doe"));
	}
	
	@Test
	public void testGetPortalSiteNameEmptyWithoutPortalContext() {
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		mContext.checking(new Expectations() {{
			allowing(request).getAttribute("portalContext"); will(returnValue(null));
		}});

		assertThat("Returns empty string if no portal context found",
				mCollector.getPortalSiteName(request), is(""));
	}

	@Test
	public void testGetPortalSiteNameEmptyWithoutCurrentSite() {
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		final PortalContext portalContext = mContext.mock(PortalContext.class);
		mContext.checking(new Expectations() {{
			allowing(request).getAttribute("portalContext"); will(returnValue(portalContext));
			allowing(portalContext).getCurrentSite(); will(returnValue(null));
		}});

		assertThat("Returns empty string if portal context returns null site",
				mCollector.getPortalSiteName(request), is(""));

	}

	@Test
	public void testGetPortalSiteReturnsDNSName() {
		final Site site = mContext.mock(Site.class);
		final PortalContext portalContext = mContext.mock(PortalContext.class);
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		mContext.checking(new Expectations() {{
			allowing(request).getAttribute("portalContext"); will(returnValue(portalContext));
			allowing(portalContext).getCurrentSite(); will(returnValue(site));
			allowing(site).getDNSName(); will(returnValue("spf-site"));
		}});

		assertThat("Returns site DNS name", mCollector.getPortalSiteName(request), is("spf-site"));
	}

	@Test
	public void testGetHppSessionTokenEmptyWithoutSmsession() {
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		mContext.checking(new Expectations() {{
			allowing(request).getCookies(); will(
					returnValue(new Cookie[] {
							new Cookie("cookie1", "value1"),
							new Cookie("cookie2", "value2")}));
		}});

		assertThat("Returns empty string when no SMSESSION cookie found",
				mCollector.getHppSessionToken(request), is(""));
	}

	@Test
	public void testGetHppSessionToken() {
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		mContext.checking(new Expectations() {{
			allowing(request).getCookies(); will(
					returnValue(new Cookie[] {
							new Cookie("cookie1", "value1"),
			                new Cookie("SMSESSION", "xyz"),
							new Cookie("cookie2", "value2")
					}));
		}});

		assertThat("Returns SMSESSION cookie value if present",
				mCollector.getHppSessionToken(request), is("xyz"));
	}

	@Test
	public void testGetNavigationItemNameEmptyWithoutPoralContext() {
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		mContext.checking(new Expectations() {{
			allowing(request).getAttribute("portalContext"); will(returnValue(null));
		}});

		assertThat("Returns empty string if portalContext not found",
				mCollector.getNavigationItemName(request), is(""));
	}

	@Test
	public void testGetNavigationItemNameEmptyWithoutMenuItemNode() {
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		final PortalContext portalContext = mContext.mock(PortalContext.class);
		mContext.checking(new Expectations() {{
			allowing(request).getAttribute("portalContext"); will(returnValue(portalContext));
		}});

		assertThat("Returns empty string if no menu item node selected",
				new PortletDataCollectorMock(null).getNavigationItemName(request), is(""));
	}

	@Test
	public void testGetNavigationItemEmptyWithoutMenuItem() {
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		final PortalContext portalContext = mContext.mock(PortalContext.class);
		final MenuItemNode menuItemNode = mContext.mock(MenuItemNode.class);
		mContext.checking(new Expectations() {{
			allowing(request).getAttribute("portalContext"); will(returnValue(portalContext));
			allowing(menuItemNode).getMenuItem(); will(returnValue(null));
		}});

		assertThat("Returns empty string if no menu item selected",
				new PortletDataCollectorMock(menuItemNode).getNavigationItemName(request), is(""));
	}

	@Test
	public void testGetNavigationItem() {
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		final PortalContext portalContext = mContext.mock(PortalContext.class);
		final MenuItemNode menuItemNode = mContext.mock(MenuItemNode.class);
		final MenuItem menuItem = mContext.mock(MenuItem.class);
		mContext.checking(new Expectations() {{
			allowing(request).getAttribute("portalContext"); will(returnValue(portalContext));
			allowing(menuItemNode).getMenuItem(); will(returnValue(menuItem));
			allowing(menuItem).getTitle(); will(returnValue("My Super Page"));
		}});

		assertThat("Returns navigation item name",
				new PortletDataCollectorMock(menuItemNode).getNavigationItemName(request), is("My Super Page"));
	}

	@Test
	public void testPageFriendlyIdEmptyWithoutPortalContext() {
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		mContext.checking(new Expectations() {{
			allowing(request).getAttribute("portalContext"); will(returnValue(null));
		}});

		assertThat("Returns empty string if portalContext not found",
				mCollector.getPageFriendlyId(request), is(""));
	}

	@Test
	public void testPageFriendlyIdEmptyWithoutCurrentPage() {
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		final PortalContext portalContext = mContext.mock(PortalContext.class);
		mContext.checking(new Expectations() {{
			allowing(request).getAttribute("portalContext"); will(returnValue(portalContext));
			allowing(portalContext).getResolvedPortletPage(); will(returnValue(null));
		}});

		assertThat("Returns empty string if page not resolved",
				mCollector.getPageFriendlyId(request), is(""));
	}

	@Test
	public void testPageFriendlyId() {
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		final PortalContext portalContext = mContext.mock(PortalContext.class);
		final Page page = mContext.mock(Page.class);
		mContext.checking(new Expectations() {{
			allowing(request).getAttribute("portalContext"); will(returnValue(portalContext));
			allowing(portalContext).getResolvedPortletPage(); will(returnValue(page));
			allowing(page).getFriendlyID(portalContext); will(returnValue("MY_SUPER_PAGE"));
		}});

		assertThat("Returns page friendly ID",
				mCollector.getPageFriendlyId(request), is("MY_SUPER_PAGE"));
	}
	
	@Test
	public void testLastSessionCleanupDate() {
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		final HttpSession session = mContext.mock(HttpSession.class);
		final String lastSessionCleanupDate = String.valueOf(System.currentTimeMillis());
		
		mContext.checking(new Expectations() {{
			allowing(request).getSession(); will(returnValue(session));
			allowing(session).getAttribute(Consts.KEY_LAST_PORTAL_SESSION_CLEANUP_DATE);
				will(returnValue(String.valueOf(lastSessionCleanupDate)));
		}});

		assertThat("Returns last session cleanup date",
				mCollector.getLastSessionCleanupDate(request), is(lastSessionCleanupDate));
	}
	
	private class PortletDataCollectorMock extends PortletDataCollector {
		private MenuItemNode mNode;

		private PortletDataCollectorMock(MenuItemNode node)
		{
			mNode = node;
		}

		@Override
		MenuItemNode getSelectedMenuItemNode(PortalContext portalContext) { return mNode; }
	}
}
