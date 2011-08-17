package com.hp.spp.db;

import java.sql.Blob;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SqlTypeMap {
    private static SqlTypeMap mInstance = new SqlTypeMap();

    private Map mMap;
	private List mSupportedClassNames;

	private SqlTypeMap() {
        mMap = new HashMap();
        mMap.put(String.class, new Integer(Types.VARCHAR));
        mMap.put(Integer.class, new Integer(Types.INTEGER));
		mMap.put(Date.class, new Integer(Types.DATE));
		mMap.put(Blob.class, new Integer(Types.BLOB));

		mMap = Collections.unmodifiableMap(mMap);

		mSupportedClassNames = new ArrayList(mMap.size());
		for (Iterator it = mMap.keySet().iterator(); it.hasNext();) {
			Class klass = (Class) it.next();
			mSupportedClassNames.add(klass.getName());
		}
		Collections.sort(mSupportedClassNames);
	}

    public static int getType(Class klass) {
        if (mInstance.mMap.containsKey(klass)) {
            return ((Integer) mInstance.mMap.get(klass)).intValue();
        }
        throw new IllegalArgumentException("No SQL type mapping found for " + klass.getName() + ". " +
                "If this type needs to be supported you need to update SqlTypeMap constructor! Currently supported types are " +
				mInstance.mSupportedClassNames);
    }
}
