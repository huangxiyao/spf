package com.hp.it.spf.lock.service;

import javax.portlet.PortletRequest;

import com.hp.it.cas.lock.service.LockRequest;
import com.hp.it.cas.lock.service.LogicalLockIdentifier;
import com.hp.it.cas.lock.service.UnsuccessfulLockException;

/**
 * The Logical LockRequest Service (LLS) is responsible for placing a lock on a resource in order to prevent concurrent
 * access to that resource from more than one consumer. It is called logical because it is placed with the knowledge
 * that its adherence is enforced through the use of a gentleman's agreement for those consumers of the LLS API. A
 * logical lock is different from a database lock since it may span longer than a database transaction. DB locks only
 * span a single transaction; however, a logical lock may span multiple transactions. The LLS locks a specific entity
 * until a request is made to unlock it, or the lock is removed via programmatic means (e.g. a session listener). Use
 * this API to lock from a portlet application.
 * 
 * @author paul.truax@hp.com
 */
public interface IPortletLogicalLockService {

	/**
	 * Attempt to place a lock immediately.
	 * 
	 * @param lockAttempt
	 *            the lock attempt
	 * @param portletRequest
	 *            the portlet request
	 * 
	 * @return the lock identifier
	 * 
	 * @throws UnsuccessfulLockException
	 *             thrown if lock was unsuccessful
	 */
	LogicalLockIdentifier lockImmediate(LockRequest lockAttempt, PortletRequest portletRequest)
			throws UnsuccessfulLockException;

	/**
	 * Attempt to lock a list of resources immediately.
	 * 
	 * @param lockAttempt
	 *            the lock attempt
	 * @param portletRequest
	 *            the portlet request
	 * 
	 * @return the lock identifier
	 * 
	 * @throws UnsuccessfulLockException
	 *             the unsuccessful lock exception
	 */
	// List<LogicalLockIdentifier> lockImmediate(List<LockRequest> lockAttempt, PortletRequest portletRequest)
	// throws UnsuccessfulLockException;

	/**
	 * Extend a lock that you already have.
	 * 
	 * @param lockIdentifier
	 *            the lock identifier
	 * 
	 * @throws UnsuccessfulLockException
	 *             the unsuccessful lock exception
	 */
	void lockExtend(LogicalLockIdentifier lockIdentifier) throws UnsuccessfulLockException;

	/**
	 * Unlock the resource/resources that have previously been locked.
	 * 
	 * @param lockIdentifier
	 *            the lock identifier
	 */
	void unLock(LogicalLockIdentifier lockIdentifier);

	/**
	 * Unlock by lock.
	 * 
	 * @param lock
	 *            the lock
	 * 
	 * @return true, if successful
	 */
	// boolean unLock(List<LogicalLockIdentifier> lockIdentifiers);

}
