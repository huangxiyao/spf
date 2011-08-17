package com.hp.spp.common;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;

import com.hp.spp.common.base.BaseResourceHistoryDAO;

public class ResourceHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static int USERGROUP_MODIFICATION = 1;

	public final static int ESERVICE_MODIFICATION = 2;

	public final static int STANDARD_PARAM_SET_MODIFICATION = 3;

	/**
	 * Default constructor
	 */
	public ResourceHistory() {
	}

	// attributes

	private long id;

	private String owner;

	private String comment;

	private Integer modificationType;

	private Date creationDate;

	private long siteId;

	private byte[] dataChange;

	private byte[] backupFile;

	private Blob dataChangeInternal;

	private Blob backupFileInternal;

	// getters

	public byte[] getBackupFile() {
		return backupFile;
	}

	public void setBackupFile(byte[] backupFile) {
		this.backupFile = backupFile;
		backupFileInternal = Hibernate.createBlob(backupFile);
	}

	public long getId() {
		return id;
	}

	public String getOwner() {
		return owner;
	}

	public String getComment() {
		return comment;
	}

	public Integer getModificationType() {
		return modificationType;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public long getSiteId() {
		return siteId;
	}

	public byte[] getDataChange() {
		return dataChange;
	}

	// setters

	public void setId(long id) {
		this.id = id;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setModificationType(Integer modificationType) {
		this.modificationType = modificationType;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setSiteId(long site) {
		this.siteId = site;
	}

	public void setDataChange(byte[] dataChange) {
		this.dataChange = dataChange;
		dataChangeInternal = Hibernate.createBlob(dataChange);
	}

	public boolean equals(Object obj) {
		if ((obj != null) && (obj instanceof ResourceHistory)) {
			ResourceHistory resourceHistory = (ResourceHistory) obj;
			return (resourceHistory.getId() == this.getId());
		}
		return false;
	}

	public int hashCode() {
		return new Long(id).hashCode();
	}

	/**
	 * Method that returns the name of this class
	 */
	public String getComponentType() {
		return "ResourceHistory";
	}

	// inner Class DB

	/**
	 * 
	 * Inner class that is used to interact with the database !!!! WARNING !!!!! This inner
	 * class should not be modified Any modifications made will be lost during regeneration
	 * 
	 */
	public static class DB {

		/**
		 * Method that creates a new ResourceHistory in the database
		 */
		public static Long save(ResourceHistory abstractResourceHistory) throws Exception {
			return BaseResourceHistoryDAO.getInstance().save(abstractResourceHistory);
		}

		/**
		 * Method that deletes the ResourceHistory from the database
		 */
		public static void delete(ResourceHistory abstractResourceHistory) throws Exception {
			BaseResourceHistoryDAO.getInstance().delete(abstractResourceHistory);
		}

		/**
		 * Method that updates an existing ResourceHistory present in the database
		 */
		public static Long update(ResourceHistory abstractResourceHistory) throws Exception {
			return BaseResourceHistoryDAO.getInstance().updateResourceHistory(
					abstractResourceHistory);
		}

		/**
		 * Method that loads the ResourceHistory having an id equal to id passed as parameter
		 */
		public static ResourceHistory load(long id) throws Exception {
			return BaseResourceHistoryDAO.getInstance().load(id);
		}

		/**
		 * Method that loads all the ResourceHistory present in the database
		 */
		public static List findAll() throws Exception {
			return BaseResourceHistoryDAO.getInstance().findAll();
		}

		/**
		 * Method that loads all the ResourceHistory that correspond to the query passed as
		 * parameter
		 */
		public static List findByQuery(String query) throws Exception {
			return BaseResourceHistoryDAO.getInstance().find(query);
		}

	}

	public Blob getBackupFileInternal() {
		return backupFileInternal;
	}

	public void setBackupFileInternal(Blob internalBackupFile) throws SQLException {
		backupFileInternal = internalBackupFile;
		backupFile = internalBackupFile.getBytes(1, (int) internalBackupFile.length());
	}

	public Blob getDataChangeInternal() {
		return dataChangeInternal;
	}

	public void setDataChangeInternal(Blob internalDataChange) throws SQLException {
		this.dataChangeInternal = internalDataChange;
		dataChange = internalDataChange.getBytes(1, (int) internalDataChange.length());
	}

}
