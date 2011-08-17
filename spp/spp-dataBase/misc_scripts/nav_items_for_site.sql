---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- Description: Displays all the navigation items below a site's homepage
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- Usage: Change name to site's name
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
select 
 (select value_small 
  from settingsdictionary 
  where uniqueid = 
  (select value1_dictionary 
   from settingscontents 
   where 
    uniqueid = a.settings_id and 
    settings_key = 'menuitem_setting_title')) as "title",
 a.id as "id"
from 
 menuitem a, 
 menuitemassociation b 
where a.id = b.menu_item_id
start with menu_id = 
(select menu_id from menuitemassociation where menu_item_id = 
 (select home_page from spp_site 
   where name = 'smartportal'))
connect by prior menu_item_id = menu_id
order by 1,2
