---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- Description: Script used in order to delete categories translation bundles that are
-- not used
-- WARNING: only use if you are VERY sure that the bundles are not used!
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- Usage: use the translation_bundles_count.sql script in order to find out if any 
-- categories have a worrying number of translation bundles (over 5000). 
-- Change a.CATEGORY to the category id identified in the results of 
-- translation_bundles_count.sql. You may need to execute the query several
-- times if more than one category is implicated. 
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
DELETE FROM 
    CategoryMap a
WHERE 
    a.CATEGORY = '9f60f280baf849837c95bb8fa9854d60'