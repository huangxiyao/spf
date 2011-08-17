package com.hp.spp.groups.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hp.spp.groups.Group;

/**
 * 
 * This class can be modified and will not be regenerated.
 * 
 */

public class GroupDAOHibernateImpl extends RootDAOHibernate implements GroupDAO {

	/**
	 * the instance.
	 */
	private static GroupDAOHibernateImpl instance;

	/**
	 * @return a singleton of the DAO
	 */
	public static GroupDAOHibernateImpl getInstance() {
		if (null == instance) {
			instance = new GroupDAOHibernateImpl();
		}
		return instance;
	}

	/**
	 * @return the class of the current object
	 */
	public Class getReferenceClass() {
		return com.hp.spp.groups.Group.class;
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown
	 * if there is a persistent instance with the same identifier in the current session.
	 * 
	 * @param group - the group to be updated
	 * @throws org.hibernate.HibernateException - if hibernate problems
	 */
	public void update(Group group) throws HibernateException {
		super.update(group);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance
	 * associated with the receiving Session or a transient instance with an identifier
	 * associated with existing persistent state.
	 * 
	 * @param group - the group to be deleted
	 * @throws org.hibernate.HibernateException - if hibernate problems
	 */
	public void delete(Group group) throws HibernateException {
		super.delete(group);
	}

	/**
	 * Used to check that the unicity constraint given in the xml model file
	 * (/module/component/attributes/attribute[@unique='true']) is not violated when a new
	 * GroupImpl is created.
	 * 
	 * @param group : the object that must be created
	 * @return Long : the id of the newly created object
	 * @throws org.hibernate.HibernateException - if hibernate problems
	 */
	public Long save(Group group) throws HibernateException {

		return super.save(group);

	}

	/**
	 * Used to check that the unicity constraint given in the xml model file
	 * (/module/component/attributes/attribute[@unique='true']) is not violated when an
	 * existing GroupImpl is updated.
	 * 
	 * @param group : the object that must be updated
	 * @return Long : the id of the object
	 * @throws org.hibernate.HibernateException - if hibernate problems
	 */
	public Long updateGroup(Group group) throws HibernateException {

		return new Long(group.getId());

	}

	/**
	 * Used to load all the Groups present in the database.
	 * 
	 * @return a list of groups
	 * @throws org.hibernate.HibernateException - if hibernate problems
	 */
	public List findAll() throws HibernateException {
		StringBuffer query = new StringBuffer();
		query.append("select g from com.hp.spp.groups.Group" + " as g ");
		List results = super.find(query.toString());
		return results;
	}

	/**
	 * Used to load the GroupImpl having an ID equal to.
	 * 
	 * @param id the id of the group
	 * @return the corresponding group
	 * @throws org.hibernate.HibernateException - if hibernate problems
	 */
	public Group load(long id) throws HibernateException {
		StringBuffer query = new StringBuffer();
		query.append("select g from com.hp.spp.groups.Group" + " as g where g.id ='" + id
				+ "'");
		Group results = (Group) super.find(query.toString()).get(0);
		results.unmarshallXml();
		return results;
	}
	
	/**
	 * Used to load the GroupImpl having an NAME equal to.
	 * 
	 * @param name the name of the group
	 * @return the corresponding group
	 * @throws org.hibernate.HibernateException - if hibernate problems
	 */
	public Group loadBySiteIdAndName(String name, long siteId) throws HibernateException {
		StringBuffer query = new StringBuffer();
		query.append("select g from com.hp.spp.groups.Group  as g where g.name ='");
		query.append(name);
		query.append("' and g.site.id='").append(Long.toString(siteId)).append("'");
		Group results = (Group) super.find(query.toString()).get(0);
		results.unmarshallXml();
		return results;
	}

}
