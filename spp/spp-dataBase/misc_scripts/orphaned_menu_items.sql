---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- Description: Displays menu items that have been dissociated from the navigation tree
-- of any site
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- Usage: Simply run the query
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
select id, string_id, type 
from menuitem 
where 
id not in (select menu_item_id from menuitemassociation)
and id not in (select menu from sites) 
and id not like '%menu%' 
and string_id not like '%My Pages Menu%';