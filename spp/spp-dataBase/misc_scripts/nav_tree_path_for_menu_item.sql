---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- Description: For a given menu item id, displays the navigation tree path to the item
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- Usage: Change menu_item_id to the your menu item's id
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
start with menu_item_id = 'b5d942745977dfa003ee48b3f4439e01'
connect by prior menu_id = menu_item_id