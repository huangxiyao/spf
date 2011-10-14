/**
 * The User Attribute Value API is the lowest level Persona API. It represents information about a user as a collection of triples consisting of the user, an
 * attribute and a value.
 * 
 * <table cellpadding="3px" rules="cols" style="border: 3px solid gray; border-collapse: collapse">
 * 	<caption>User Attribute Value data representation</caption>
 * 	<thead align="left">
 * 		<tr>
 * 			<th style="border-right: 2px solid gray">User</th>
 * 			<th style="border-right: 2px solid gray" colspan="2">Attribute</th>
 * 			<th colspan="2">Value</th>
 * 		</tr>
 * 		<tr style="border-bottom:2px solid gray">
 * 			<th style="border-right: 2px solid gray"></th>
 * 			<th style="border: 1px solid gray; font-weight: normal; font-style: italic">Compound attribute</th>
 * 			<th style="border: 1px solid gray; font-weight: normal; font-style: italic">Simple attribute</th>
 * 			<th style="border: solid gray; font-weight: normal; font-style: italic; border-width: 1px 1px 0 2px">Instance</th>
 * 			<th style="font-weight: normal; font-style: italic">Value</th>
 * 		</tr>
 * 	</thead>
 * 	<tbody>
 * 		<tr>
 * 			<td>hpp:1234</td>
 * 			<td></td>
 * 			<td>email</td>
 * 			<td></td>
 * 			<td>john.doe@hp.com</td>
 * 		</tr>
 * 		<tr>
 * 			<td>hpp:1234</td>
 * 			<td></td>
 * 			<td>email</td>
 * 			<td></td>
 * 			<td>j.doe@hp.com</td>
 * 		</tr>
 * 		<tr>
 * 			<td>hpp:1234</td>
 * 			<td></td>
 * 			<td>company</td>
 * 			<td></td>
 * 			<td>Hewlett-Packard</td>
 * 		</tr>
 * 		<tr>
 * 			<td>hpp:1234</td>
 * 			<td>name</td>
 * 			<td>givenName</td>
 * 			<td>A</td>
 * 			<td>John</td>
 * 		</tr>
 * 		<tr>
 * 			<td>hpp:1234</td>
 * 			<td>name</td>
 * 			<td>familyName</td>
 * 			<td>A</td>
 * 			<td>Doe</td>
 * 		</tr>
 * 		<tr>
 * 			<td>hpp:1234</td>
 * 			<td>address</td>
 * 			<td>city</td>
 * 			<td>B</td>
 * 			<td>Atlanta</td>
 * 		</tr>
 * 		<tr>
 * 			<td>hpp:1234</td>
 * 			<td>address</td>
 * 			<td>state</td>
 * 			<td>B</td>
 * 			<td>GA</td>
 * 		</tr>
 * 		<tr>
 * 			<td>hpp:1234</td>
 * 			<td>address</td>
 * 			<td>city</td>
 * 			<td>C</td>
 * 			<td>Tampa</td>
 * 		</tr>
 * 		<tr>
 * 			<td>hpp:1234</td>
 * 			<td>address</td>
 * 			<td>state</td>
 * 			<td>C</td>
 * 			<td>FL</td>
 * 		</tr>
 * 	</tbody>
 * </table>
 */

package com.hp.it.cas.persona.uav.service;