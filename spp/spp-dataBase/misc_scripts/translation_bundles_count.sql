---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- Description: Displays the amount of translation bundles associated to each category
-- within the Vignette database (includes all sites)
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- Usage: Run the query on a regular basis. If any categories have more than
-- 5000 budles associated there is probably something wrong. If there is no
-- way to account for such a large number of bundles then the 
-- delete_translation_bundles.sql script can be used.
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
select a.CATEGORY,
(select title from CATEGORIES where uniqueid = a.CATEGORY),
count(a.CATEGORIZABLE) thecount 
from CategoryMap a 
where a.CATEGORY 
in (select uniqueid from CATEGORIES) 
group by a.CATEGORY 
order by thecount desc