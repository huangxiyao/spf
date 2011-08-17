package com.hp.spp.groups.dao;

import java.util.List;

import com.hp.spp.groups.Group;

/**
 * Interface for all the DataAccessObjects related to the SITE.
 * 
 * @author bmollard
 * 
 */
public interface GroupDAO {

	/**
	 * Load the Group.
	 * 
	 * @param id id of the group to load
	 * @return the Group
	 */
	Group load(long id);

	/**
	 * Return a list of Groups corresponding to the query.
	 * 
	 * @param query the Query
	 * @return list of groups
	 */
	List find(String query);

	/**
	 * Save the Group.
	 * 
	 * @param g group to save
	 * @return status
	 */
	Long save(Group g);

	/**
	 * Update the group.
	 * 
	 * @param g group to update
	 */
	void update(Group g);

	/**
	 * Delete the Group.
	 * 
	 * @param g the group to delete
	 */
	void delete(Group g);

}
