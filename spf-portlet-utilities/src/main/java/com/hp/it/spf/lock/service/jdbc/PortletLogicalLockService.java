package com.hp.it.spf.lock.service.jdbc;

import java.util.Date;

import javax.portlet.PortletRequest;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.it.cas.lock.custom.dao.ILockCustomDAOFactory;
import com.hp.it.cas.lock.dao.ILockDAOFactory;
import com.hp.it.cas.lock.dao.IOnlnLgclLckDAO;
import com.hp.it.cas.lock.dao.LgclLckKey;
import com.hp.it.cas.lock.dao.OnlnLgclLck;
import com.hp.it.cas.lock.dao.OnlnLgclLckKey;
import com.hp.it.spf.lock.service.IPortletLogicalLockService;
import com.hp.it.cas.lock.service.LockRequest;
import com.hp.it.cas.lock.service.LogicalLockIdentifier;
import com.hp.it.cas.lock.service.UnsuccessfulLockException;
import com.hp.it.cas.lock.service.jdbc.AbstractLogicalLockService;
import com.hp.it.cas.xa.validate.Verifier;
import com.hp.it.spf.xa.misc.portlet.Consts;
import com.hp.it.spf.xa.misc.portlet.Utils;

/**
 * JDBC implementation of {@link IPortletLogicalLockService}
 * 
 * @author paul.truax@hp.com
 * 
 */
public class PortletLogicalLockService extends AbstractLogicalLockService implements IPortletLogicalLockService {

	private final IOnlnLgclLckDAO onlnLgclLckDAO;
	private static final Logger logger = LoggerFactory.getLogger(PortletLogicalLockService.class);

	/**
	 * Construct and initialize the batch logical lock service
	 * 
	 * @param daoFactory
	 *            dao factory
	 * @param customDaoFactory
	 *            custom dao factory
	 * @param dataSource
	 *            data source to use
	 */
	public PortletLogicalLockService(ILockDAOFactory daoFactory, ILockCustomDAOFactory customDaoFactory,
			DataSource dataSource) {
		super(dataSource, daoFactory, customDaoFactory);
		new Verifier().isNotNull(dataSource, "A data source is required.")
				.isNotNull(daoFactory, "A DAO factory is required.").throwIfError("Unable to create the driver.");
		this.onlnLgclLckDAO = daoFactory.getOnlnLgclLckDAO();
		setRunEnvironmentTypeCode("online");
		setApplicationPortfolioIdentifier(123);
		setLockDuration(300000);// 5 minutes default lock duration
		setLockLatency(60000); // 1 minute after expiry before we'll allow other
								// locks to clobber it
		setWaitDuration(0); // wait has no meaning in this impl
		setWaitSleepDuration(0); // sleep has no meaning in this impl
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.hp.it.cas.lock.service.IPortletLogicalLockService#lockExtend(com.hp.it.cas.lock.service.LogicalLockIdentifier)
	 */
	public void lockExtend(LogicalLockIdentifier lockIdentifier) throws UnsuccessfulLockException {
		new Verifier().isNotNull(lockIdentifier, "The lock identifier cannot be null.").throwIfError();
		logger.debug("lockExtend({})", lockIdentifier.toString());
		extendLockExpiry(lockIdentifier);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.hp.it.cas.lock.service.IPortletLogicalLockService#lockImmediate(com.hp.it.cas.lock.service.LockRequest,
	 *      javax.portlet.PortletRequest)
	 */
	public LogicalLockIdentifier lockImmediate(LockRequest lockRequest, PortletRequest portletRequest)
			throws UnsuccessfulLockException {
		verifyLock(lockRequest, portletRequest);
		logger.debug("lockImmediate({}, {})", lockRequest.toString(), portletRequest.toString());
		Date timeout = new Date();
		return lock(lockRequest, timeout, portletRequest);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.hp.it.cas.lock.service.jdbc.AbstractLogicalLockService#handleEnqueueLockRequest(com.hp.it.cas.lock.service.LockRequest,
	 *      java.lang.Object)
	 */
	@Override
	protected void handleEnqueueLockRequest(LockRequest lockRequest, Object param) {
		PortletRequest portletRequest = (PortletRequest) param;
		String profileId = (String) Utils.getUserProperty(portletRequest, Consts.KEY_USER_NAME);

		OnlnLgclLckKey onlineLogicalLockKey = new OnlnLgclLckKey();
		onlineLogicalLockKey.setLckResrcNm(lockRequest.getLockResourceName());
		onlineLogicalLockKey.setLckResrcTypeCd(lockRequest.getLockResourceTypeCode());
		onlineLogicalLockKey.setLckReqTs(lockRequest.getLockRequestTimestamp());
		onlineLogicalLockKey.setMinLckResrcKyValuTx(lockRequest.getMinimumLockResourceKeyValueText());
		onlineLogicalLockKey.setMaxLckResrcKyValuTx(lockRequest.getMaximumLockResourceKeyValueText());

		OnlnLgclLck onlineLogicalLock = new OnlnLgclLck();
		onlineLogicalLock.setKey(onlineLogicalLockKey);
		onlineLogicalLock.setCrtTs(new Date());
		onlineLogicalLock.setCrtUserId(profileId);
		onlineLogicalLock.setLcleCd(portletRequest.getLocale().getCountry());
		onlineLogicalLock.setPrtltModeNm(portletRequest.getPortletMode().toString());
		onlineLogicalLock.setPrtltNm(portletRequest.getPortletSession().getPortletContext().getPortletContextName());
		onlineLogicalLock.setPrtltPgReqNm(PortletRequest.LIFECYCLE_PHASE.toString());
		onlineLogicalLock.setScrnId(portletRequest.getPortletSession().getPortletContext().getPortletContextName()); // one
																														// more
		onlineLogicalLock.setSessId(portletRequest.getPortletSession().getId().toString());
		onlineLogicalLock.setWndwStNm(portletRequest.getWindowState().toString());

		onlnLgclLckDAO.insert(onlineLogicalLock);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.hp.it.cas.lock.service.jdbc.AbstractLogicalLockService#handleSetContext(java.lang.Object)
	 */
	@Override
	protected void handleSetContext(Object param) {
		PortletRequest portletRequest = (PortletRequest) param;
		new Verifier().isNotNull(portletRequest, "The portletRequest cannot be null.").throwIfError();
		String profileId = (String) Utils.getUserProperty(portletRequest, Consts.KEY_USER_NAME);
		setCreateUserId(profileId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.hp.it.cas.lock.service.jdbc.AbstractLogicalLockService#checkForExistingLock(com.hp.it.cas.lock.service.LockRequest,
	 *      java.lang.Object)
	 */
	@Override
	protected LgclLckKey checkForExistingLock(LockRequest lockRequest, Object param) {
		return null;
	}

	private void verifyLock(LockRequest lockRequest, PortletRequest portletRequest) {
		new Verifier().isNotNull(lockRequest, "The lock attempt cannot be null.")
				.isNotNull(portletRequest, "The portlet request cannot be null.").throwIfError();

		new Verifier()
				.isNotEmpty(lockRequest.getLockResourceTypeCode(), "The lock resource type code is null")
				.isNotEmpty(lockRequest.getLockResourceName(), "The lock resource name is null")
				.isNotNull(lockRequest.getLockRequestTimestamp(), "The lock request timestamp is null")
				.isNotEmpty(lockRequest.getMinimumLockResourceKeyValueText(),
						"The lock resource key value text is null").throwIfError();
	}
}
