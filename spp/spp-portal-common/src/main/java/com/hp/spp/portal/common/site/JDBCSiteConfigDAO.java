package com.hp.spp.portal.common.site;

import com.hp.spp.db.DB;
import com.hp.spp.db.DatabaseException;
import com.hp.spp.db.DatabaseTransaction;
import com.hp.spp.db.RowMapper;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JDBCSiteConfigDAO implements SiteConfigDAO{

    private static final Logger mLog = Logger.getLogger(JDBCSiteConfigDAO.class);
    private Map<String,Method> mFunctionGettersMap = new HashMap<String,Method>();
    private static final String DEFAULT_SITE_ID = "-10000";

    /*Query to obtain all site specific configuration details. '-10000' for default */
    private static final String querySiteBySiteName = "SELECT a.NAME, a.VALUE FROM SPP_SITE_SETTING a, "+
            " SPP_SITE b where b.NAME = ? and (a.SITE_ID = b.ID or a.SITE_ID = " + DEFAULT_SITE_ID +
            ") ORDER BY a.SITE_ID ASC";

    /*Update a site specific entry to SPP_SITE_SETTING */
    private static final String updateSiteSetting = "UPDATE SPP_SITE_SETTING SET VALUE = ? WHERE NAME = ? AND "+
                                             "SITE_ID = (SELECT ID FROM SPP_SITE WHERE NAME = ?)";

    /*Insert  a site specific entry to SPP_SITE_SETTING */
    private static final String insertSiteSetting = "INSERT INTO  SPP_SITE_SETTING (NAME,VALUE,SITE_ID) VALUES "+
                                               "(?,?,(SELECT ID FROM SPP_SITE WHERE NAME = ?))";

    public JDBCSiteConfigDAO(){
        initialize();
    }

    /**
     * The method retrieves all the site settings for a given site encapsulated in Site object.
     * @param siteName Name of the site for which configuration details is required.
     * @return Site object encapsulating all the site configuration details.
     */
    public Site getSite(String siteName) {
		Site site = new Site(siteName);
		Object[] args = { siteName };
		DB.queryForObject(querySiteBySiteName, new SiteConfigMapper(site) ,args);
        if(mLog.isDebugEnabled()){
            mLog.debug("Site state: "+ site);
        }
        return site;
    }

   /**
     * The method updates all modified site settings attributed in the db
     * @param updatedSite Name of the site for which keyvalue needs to be changed
	 * @key Key Name
	 * @value Value of the key
     */
	public void updateSite(Site updatedSite) {
         String siteName = updatedSite.getSiteName();
         Site siteFromDB = getSite(siteName);
         save(updatedSite, siteFromDB);
    }

    /*This method reads all the getters and setters of Site class and creates Map.*/
    private void initialize(){
		Method[] methods = Site.class.getDeclaredMethods();
		for(Method m: methods){
             if(m.getParameterTypes().length == 0 && m.getName().startsWith("get")){
                    mFunctionGettersMap.put(m.getName().substring(3),m);
             }
        }
    }

    /*
    The method first tries to update site specific setting. If the update fails it means that the setting
    was picked as default value. So now a new entry is created for the site.
     */
    private void save(final Site updatedSite, final Site siteFromDB) {
		DatabaseTransaction<Object> tx = new DatabaseTransaction<Object>() {

            protected Object doInTransaction() throws Exception {
                Method m;
                Object dbValue = null;
                Object updatedValue = null;
                Set<String> getterSet = mFunctionGettersMap.keySet();
                String siteName = updatedSite.getSiteName();
                int status;
                for(String key : getterSet){
                    m = mFunctionGettersMap.get(key);
                    try{
                        dbValue = m.invoke(siteFromDB);
                        updatedValue = m.invoke(updatedSite);
                    }catch(Exception e){
                      mLog.error("Error Occured for "+ m.getName() + " " +dbValue+ " " +updatedValue);
                      throw new RuntimeException(new StringBuilder().append("Error Occured for ").
                              append(m.getName()).append(" ").append(dbValue).append(" ").
                              append(updatedValue).append(e).toString());
                  }
                    
                /*
                Case 1 : Updated object contains a String attribute set to null as there was no default or site
                         specific entry defined. dbValue will also be null for such an object. Here we should not
                         add site specific entry with value null as it serves no purpose.
                Case 2 : Updated object contains a String attribute set to null due to an update by user. There will
                         be a not null dBValue for such an attribute.
                         Case 2.1 : dBValue comes due to default. Here we insert a site specific entry with value null.
                         Case 2.2 : dBValue comes due to site specific entry. Here we update site specific entry with
                                    value null.
                 */

                    if(updatedValue == null && dbValue == null){
                        continue;
                    }

                    if((updatedValue != null && !updatedValue.equals(dbValue)) || (dbValue != null && !dbValue.equals(updatedValue))){
                        updatedValue = updatedValue == null ? null : updatedValue.toString();

                        status = update(updateSiteSetting,new Object[] {updatedValue, key, siteName}, null);
                         if(status > 0){
                            continue;
                         }

                         status =  update(insertSiteSetting,new Object[] {key, updatedValue, siteName},null);
                         if(!(status > 0)){
                            throw new RuntimeException("Failed to update site setting: "+ key +" for site "+ siteName);
                        }
                    }
                }
                return null;
            }
         };
        try {
            tx.execute();
        }
        catch (DatabaseException e) {
            throw e;
        }
		catch (Exception e) {
			// except DatabaseException we don't expect any other exception here
			throw new RuntimeException("Error executing save", e);
		}
	}

    private class SiteConfigMapper implements RowMapper<Site> {

        private static final String KEY_NAME = "NAME";
        private static final String KEY_VALUE = "VALUE";

        private  Map<String,Method> mSetterFunctionMap = new HashMap<String,Method>();

        private Site mSite;

        public SiteConfigMapper(Site site){
            mSite = site;
            initialize();
        }

       /**
         * This methods retrieves all the configuration details for a site given its name.
         * Logic: '-10000' is a dummy site id used to store default site entries. The query
         * used orders the resultset in ascending order so that when the setters are invoked
         * the Site object attributes are first initialized to defalult values and then over-
         * ridden by site specefic value. Please note that the assumption here is that the
         * site id would be greater than value -10000.
         * @param row     Not used
         * @param rowNum  Not used
         * @return Object Not used
         */
        public Site mapRow(ResultSet row, int rowNum){
            String settingName;
            String settingValue;
            Method method;

            try{
                settingName = row.getString(KEY_NAME);
                settingValue = row.getString(KEY_VALUE);
                method = mSetterFunctionMap.get(settingName);
                if(method != null){
                    method.invoke(mSite, convertToSettingType(method.getParameterTypes()[0],settingValue));
                }else{
                    mLog.warn("Setter not found for: "+ settingName);
                }
            //Eating up all the exceptions as separate handling provides no additional benefit.
            }catch(Exception e){
                throw new IllegalStateException("Error :"+ e,e);
            }
            //Since we wish to populate the same Site object the DB code should be returned null.
            return null;
        }

        /* Doing type conversion */
        private Object convertToSettingType(Class klass,String value){
            if("boolean".equals(klass.getName())){
                return Boolean.valueOf(value);
            }else if("int".equals(klass.getName())){
                return Integer.parseInt(value);
            }else if("java.lang.String".equals(klass.getName())){
                return value;
            }
            throw new IllegalStateException("Error while type conversion: "+klass.getName());
        }

        /*This method reads all the method of Site class and creates a Map.*/
        private void initialize(){
            Method[] methods = Site.class.getDeclaredMethods();
            for(Method m: methods){
                if(m.getParameterTypes().length == 1 && isValidType(m.getParameterTypes()[0]) && m.getName().startsWith("set")){
                    mSetterFunctionMap.put(m.getName().substring(3), m);
                }
            }
        }

       /*
        Only the setters and getters having argument type of 'boolean',
        'int' or 'String' are stored in the map
        */
        private boolean isValidType(Class klass){
            return "boolean".equals(klass.getName()) ||
                    "int".equals(klass.getName()) ||
                    "java.lang.String".equals(klass.getName());
        }
    }
}
