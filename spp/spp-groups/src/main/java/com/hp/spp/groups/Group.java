package com.hp.spp.groups;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.hibernate.Hibernate;

import com.hp.spp.common.util.SppConstants;
import com.hp.spp.groups.expression.Expression;

/**
 * Entity Class to represent a Group.
 * 
 * @author bmollard
 * 
 */
public class Group {

	/**
	 * The logger
	 */
	private static Logger logger = Logger.getLogger(Group.class);

	/**
	 * The logger to send the emails alerts
	 */
	private static Logger emailLogger = Logger.getLogger(SppConstants.SPP_EMAIL_LOG_NAME);

	/**
	 * Default constructor.
	 */
	public Group() {
	}

	// attributes

	/**
	 * id of the group.
	 */
	private long mId;

	/**
	 * name of the group.
	 */
	private String mName;

	/**
	 * the Xml defintion of the group used by SPP.
	 */
	private byte[] mRules;

	/**
	 * the Xml defintion of the group use by Hibernate.
	 */
	private Blob mRulesInternal;

	/**
	 * the date of the creation of the group with its definition.
	 */
	private Date mCreationDate;

	/**
	 * the date of modification of the definition of the group.
	 */
	private Date mModificationDate;

	/**
	 * the reference to the site.
	 */
	private Site mSite;

	/**
	 * the principal defintion of the definition of the group.
	 */
	private Expression mExpression;

	// getters

	/**
	 * Return the id attribute.
	 * 
	 * @return the id of the group
	 */
	public long getId() {
		return mId;
	}

	/**
	 * Return the name attribute.
	 * 
	 * @return the name of the group
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Return the rules attribute.
	 * 
	 * @return the rules associated to this group
	 */
	protected Blob getRulesInternal() {
		return mRulesInternal;
	}

	public byte[] getRules() {
		return mRules;
	}

	/**
	 * Return the creationDate attribute.
	 * 
	 * @return the creation date of the group
	 */
	public Date getCreationDate() {
		return mCreationDate;
	}

	/**
	 * Return the modificationDate attribute.
	 * 
	 * @return the modification date of the group
	 */
	public Date getModificationDate() {
		return mModificationDate;
	}

	/**
	 * Return the site attribute.
	 * 
	 * @return the site of the group
	 */
	public Site getSite() {
		return mSite;
	}

	// setters

	/**
	 * Set the id attribute.
	 * 
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.mId = id;
	}

	/**
	 * Set the name attribute.
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.mName = name;
	}

	/**
	 * Set the rules attribute.
	 * 
	 * @param rules the rules to set
	 * @throws SQLException
	 */
	public void setRulesInternal(Blob rules) throws SQLException {
		mRulesInternal = rules;
		if (rules != null) {
			mRules = rules.getBytes(1, (int) rules.length());
		} else {
			mRules = null;
		}
	}

	public void setRules(byte[] rules) {
		mRules = rules;
		mRulesInternal = Hibernate.createBlob(mRules);
	}

	/**
	 * Set the creationDate attribute.
	 * 
	 * @param creationDate creation date
	 */
	public void setCreationDate(Date creationDate) {
		this.mCreationDate = creationDate;
	}

	/**
	 * Set the modificationDate attribute.
	 * 
	 * @param modificationDate modification date
	 */
	public void setModificationDate(Date modificationDate) {
		this.mModificationDate = modificationDate;
	}

	/**
	 * Set the site attribute.
	 * 
	 * @param site the site to set
	 */
	public void setSite(Site site) {
		this.mSite = site;
	}

	/**
	 * Return the Expression attribute. This attribute is computed from the XML file in the
	 * database (stored in rules attribute) The first time tihs method is called, the
	 * unmarshalling process will be called
	 * 
	 * @return the Expression
	 */
	public Expression getExpression() {
		if (mExpression == null) {
			unmarshallXml();
		}
		return mExpression;
	}

	/**
	 * Set the expression attribute.
	 * 
	 * @param expression an instance of expression
	 */
	public void setExpression(Expression expression) {
		mExpression = expression;
	}

	/**
	 * 
	 * Method that associates a Site to aGroup.
	 * 
	 * @param site the site to add
	 */
	public void addSite(Site site) {

		this.mSite = site;

	}

	/**
	 * 
	 * Method that removes the association between a Site and an Group.
	 * 
	 * @param site site to remove
	 */
	public void removeSite(Site site) {
		logger.warn("Removing group [" + mId + "] from site");
		this.mSite = null;
	}

	/**
	 * return true if the userContext is correct for this group.
	 * 
	 * @param userContext user context description
	 * @param siteName name of the site
	 * @return if the user can have this group
	 */
	public boolean isMember(Map userContext, String siteName) {
		boolean member = false;
		try {
			Expression expression = getExpression();
			if (expression != null) {
				member = expression.isTrue(userContext, siteName);
			} else {
				logger.warn("No expression defined for group [" + mId + "]");
			}
		} catch (IllegalArgumentException e) {
			logger.warn("Illegal argument passed in the user context [" + e + "]", e);
			// when an illegal argument is passed in the user context, send an
			// email to alert the support
			emailLogger.error("Exception during UGS execution [" + e + "] for the Group ["
					+ getName() + "] of the site [" + getSite().getName() + "]");
		}
		return member;
	}

	/**
	 * method to parse xml content into java Object.
	 */
	public void unmarshallXml() {
		if (logger.isDebugEnabled()) {
			logger.debug("Unmarshall the group [" + getName() + "]");
		}

		if (mRules != null) {
			InputStream rulesBinaryStream = null;
			try {
				rulesBinaryStream = new ByteArrayInputStream(mRules);
				InputStreamReader is = new InputStreamReader(rulesBinaryStream);
				// UNMARSHALL
				Mapping map = new Mapping();
				try {
					URL u = this.getClass().getResource("GroupCastorMapping.xml");
					map.loadMapping(u);
					Unmarshaller unmarshaller = new Unmarshaller(map);
					try {
						Group group = (Group) unmarshaller.unmarshal(is);
						mExpression = group.getExpression();
					} catch (MarshalException e) {
						logger.error("The XML for the group [" + mId + "] is invalid.", e);
					} catch (ValidationException e) {
						logger.error("The XML for the group [" + mId + "] is invalid.", e);
					}
				} catch (IOException e) {
					logger.error("Could not open the XML stream for the group [" + mId + "]",
							e);
				} catch (MappingException e) {
					logger.error(
							"Problem with the castor mapping for the group [" + mId + "]", e);
				}
			} finally {
				try {
					rulesBinaryStream.close();
				} catch (IOException e) {
					logger.error("Could not close the binary stream on a rule", e);
				}
			}
		} else {
			logger.warn("No rules defined for the group [" + mId + "]");
		}

	}

}
