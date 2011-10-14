/**
 * The User API exposes user data through Java Collections. It is built upon the User Attribute Value API.
 * 
 * <p>Example usage:</p>
 * <pre>
 * IUser user = userService.createUser(EUserIdentifierType.EXTERNAL_USER, "1234");
 * 
 * Collection&lt;String&gt; emailAddresses = user.getSimpleAttributeValues().get(EMAIL);
 * emailAddresses.remove("j.doe@hp.com");
 * emailAddresses.add("jack.doe@hp.com");
 *
 * if (user.getCompoundAttributeValues().get(ADDRESS).size() &gt; 1) …
 * </pre>
 */

package com.hp.it.cas.persona.user.service;