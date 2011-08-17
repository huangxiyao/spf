---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- Description: Displays ALL the menuitems and the user groups that have permissions for each menu item
-- sorted by the menuitem name
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- Usage: Just run the query
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------

select menuitem.string_id AS MenuItem, usergroups.name As GroupName 
from usergroups, authorizationmanagement, menuitem 
where menuitem.id = authorizationmanagement.permissionable
and usergroups.unique_id = authorizationmanagement.grantee
and authorizationmanagement.permission = 'std:menu_item:enabled' order by menuitem.string_id