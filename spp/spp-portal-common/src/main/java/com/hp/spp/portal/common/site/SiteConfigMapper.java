package com.hp.spp.portal.common.site;

import com.hp.spp.db.RowMapper;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class SiteConfigMapper implements RowMapper<Object> {

    private final String KEY_NAME = "NAME";
    private final String KEY_VALUE = "VALUE";

    private  Map<String,Method> mFunctionMap = new HashMap<String,Method>();
    
    private Site mSite;
	
	public SiteConfigMapper(Site site){
		mSite = site;
		initialize();
	}
	
	public Object mapRow(ResultSet row, int rowNum){
        String settingName;
        String settingValue;
        Method method;

        try{
			settingName = row.getString(KEY_NAME);
			settingValue = row.getString(KEY_VALUE);
			method = mFunctionMap.get(settingName);
            if(method != null){
                method.invoke(mSite, convertToSettingType(method.getParameterTypes()[0],settingValue));
            }
        //Eating up all the exceptions as separate handling provides no additional benefit.
        }catch(Exception e){
            throw new IllegalStateException("Error :",e);
        }
		//Since we wish to populate the same Site object the DB code should be returned null.
        return null;
	}

    /* Doing type conversion */
    private static Object convertToSettingType(Class klass,String value){
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
            if(m.getName().startsWith("set")){
			    mFunctionMap.put(m.getName().substring(3), m);
		    }
        }
    }
}
