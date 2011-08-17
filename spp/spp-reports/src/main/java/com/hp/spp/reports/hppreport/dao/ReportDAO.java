package com.hp.spp.reports.hppreport.dao;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hp.spp.common.exception.BrowserBackException;
import com.hp.spp.common.util.hibernate.HibernateUtil;
import com.hp.spp.reports.hppreport.User;
import com.hp.spp.reports.hppreport.exception.HPPReportException;

/*
 * It is an interface to a database and retrives
 * - the user information who did not login for a period of time
 * - hpp groups belonging to perticular site.
 *
 * @author Shivashanker B
 *
 */

/** Revision History:
*
* Ver.   Modified By           Date           Notes
*--------------------------------------------------------------------------------------*
* v1     Shiva           03-Nov-2006      Created
* v2     Akash           03-Nov-2006      added findAllHppGroups() method.
*
*/
public class ReportDAO{
	private static final Logger mLog = Logger.getLogger(ReportDAO.class);
	/**
	* This method retrives all the hpp groups specific to site.
	*/
	public List findAllHppGroups(String siteName)
		throws HPPReportException {
		String error = "Error while getting the hpp groups for the site "+ siteName;
		List hppGroups = null;
		Transaction tx = null;
		Session session = null;

		String findAllHppGroupsHSQL =
		 "  SELECT " +
		 " 		hppGroup.hppGroup " +
		 "	FROM " +
		 "		com.hp.spp.reports.hppreport.HPPGroup as hppGroup " +
		 "	WHERE hppGroup.portal =? ";

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query hppQuery = session.createQuery(findAllHppGroupsHSQL);
			hppQuery.setString(0, siteName);
			hppGroups = hppQuery.list();
			tx.commit();
		}catch (ObjectNotFoundException e)	{
			String info = "HPP Groups not found for the site ["+siteName+"]";
			mLog.info(info +" \n Stack Trace " + e.getStackTrace().toString());
			throw new HPPReportException(info);
		} catch (Exception e) {
	        mLog.error(error, e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException he) {
					mLog.error("Error rolling back the transaction", he);
				}
				tx = null;
			}
			throw new HPPReportException(error);
		} finally {
			try {
				if(session.isOpen()){
					session.close();
				}
			} catch (HibernateException he) {
				mLog.error("Error closing the transaction", he);
				throw new HPPReportException(error);
			}
		}

		return hppGroups;
	}

	/**
	* This method retrives all the aged users present in the database
	*/
	public List findAllAgedUsers(int days)
		throws HPPReportException {
		String error = "Error while getting the aged users for the given criteria";
		List users = null;
		Transaction tx = null;
		Session session = null;

		String findAllAgedUsersHSQL =
		" SELECT "+
		"	user "+
		" FROM  "+
		"	com.hp.spp.reports.hppreport.User as user "+
		" WHERE "+
		"   user.lastLoginDate <= ( SYSDATE - ?) ";

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery(findAllAgedUsersHSQL);
			query.setInteger(0, days);
			users = query.list();
			tx.commit();
		}catch (ObjectNotFoundException e)	{
			String info = "Users not found for the given criteria";
			mLog.info(info +" \n Stack Trace " + e.getStackTrace().toString());
			throw new HPPReportException(info);
		} catch (Exception e) {
	        mLog.error(error, e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException he) {
					mLog.error("Error rolling back the transaction", he);
				}
				tx = null;
			}
			throw new HPPReportException(error);
		} finally {
			try {
				if(session.isOpen()){
					session.close();
				}
			} catch (HibernateException he) {
				mLog.error("Error closing the transaction", he);
				throw new HPPReportException(error);
			}
		}
		return users;
	}


	/**
	* This method retrives all the aged users present in the database
	*/
	public String findUserHppId(String emailId,String userId)
		throws HPPReportException {
		String error = "Error while getting the aged users for the given criteria";
		List users = null;
		Transaction tx = null;
		Session session = null;
		String hppId = null;

		String findAllAgedUsersHSQL =
		" SELECT "+
		"	user.hppId "+
		" FROM  "+
		"	com.hp.spp.reports.hppreport.User as user "+
		" WHERE "+
		"   upper(trim(user.emailId)) = ? " + " or " + " upper(trim(user.userName)) = ? " ;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery(findAllAgedUsersHSQL);
			query.setString(0, emailId.toUpperCase());
			query.setString(1, userId.toUpperCase());
			users = query.list();
			if(users!=null && users.size()>0){
				hppId = (String)users.get(0);
			}
			tx.commit();
		}catch (ObjectNotFoundException e)	{
			String info = "User profile id does not exist for the given criteria";
			mLog.info(info, e);
			throw new HPPReportException(info);
		} catch (Exception e) {
		        mLog.error(error, e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException he) {
					mLog.error("Error rolling back the transaction.", e);
				}
				tx = null;
			}
		throw new HPPReportException(error);
		} finally {
			try {
				if(session.isOpen()){
					session.close();
				}
			} catch (HibernateException he) {
				mLog.error("Error closing the transaction", he);
				throw new HPPReportException(error);
			}
		}
		return hppId;
	}

}

