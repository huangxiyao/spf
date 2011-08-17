package com.hp.spp.portal.common.site;

public class JDBCSiteConfigDAOTest extends TestFixture {

    public void testForDefault() throws Exception {
        JDBCSiteConfigDAO dao = new JDBCSiteConfigDAO();
        Site site = dao.getSite("TestSite");
        System.out.println(site.getSearchPortletID());
        assertEquals("defaultPortletID",site.getSearchPortletID());
    }

    public void testForSiteSpecific() throws Exception {
        JDBCSiteConfigDAO dao = new JDBCSiteConfigDAO();
        Site site = dao.getSite("TestSite");
        assertEquals(10000,site.getESMTimeoutInMilliseconds());
    }

     public void testForUpdate() throws Exception{
        JDBCSiteConfigDAO dao = new JDBCSiteConfigDAO();
        Site site = dao.getSite("TestSite");
        site.setSearchPortletID("xyz");
        dao.updateSite(site);
        site = dao.getSite("TestSite");
        assertEquals("xyz",site.getSearchPortletID()); 
    }

    public void testForNullUpdate() throws Exception{
        JDBCSiteConfigDAO dao = new JDBCSiteConfigDAO();
        Site site = dao.getSite("TestSite");
        site.setSearchPortletID(null);
        dao.updateSite(site);
        site = dao.getSite("TestSite");
        assertEquals(null,site.getSearchPortletID()); 
    }
}
