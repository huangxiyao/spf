---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- Description: Displays the user groups that have permissions for a given menu item
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- Usage: Change authorizationmanagement.permissionable to the id of your menu item 
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
select usergroups.name 
from usergroups, authorizationmanagement 
where authorizationmanagement.grantee = usergroups.unique_id 
and authorizationmanagement.permissionable = '45d9f996515425edbb7520bbf8039e01'
and authorizationmanagement.permission = 'std:menu_item:enabled'