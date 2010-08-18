package com.hp.it.spf.lock.service.jdbc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mock.web.portlet.MockPortletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.hp.it.cas.lock.custom.dao.LockCustomDAOFactoryImpl;
import com.hp.it.cas.lock.dao.ILgclLckDAO;
import com.hp.it.cas.lock.dao.ILockDAOFactory;
import com.hp.it.cas.lock.dao.IOnlnLgclLckDAO;
import com.hp.it.cas.lock.dao.LgclLck;
import com.hp.it.cas.lock.dao.LgclLckKey;
import com.hp.it.cas.lock.dao.LockDAOFactoryImpl;
import com.hp.it.cas.lock.dao.OnlnLgclLck;
import com.hp.it.cas.lock.dao.OnlnLgclLckKey;
import com.hp.it.cas.lock.service.LockRequest;
import com.hp.it.cas.lock.service.LogicalLockIdentifier;
import com.hp.it.cas.lock.service.UnsuccessfulLockException;
import com.hp.it.spf.lock.service.jdbc.PortletLogicalLockService;
import com.hp.it.cas.xa.logging.StopWatch;
import com.hp.it.cas.xa.security.SecurityContextHolder;

/**
 * 
 * 
 * @author roger.spotts@hp.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/datasource-config.xml" })
public class PortletLogicalLockServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(PortletLogicalLockServiceTest.class);

	ILockDAOFactory lockDaoFactory;
	IOnlnLgclLckDAO onlnLgclLckDAO;
	ILgclLckDAO lgclLckDAO;
	PortletLogicalLockService portletLogicalLockService;
	DataSourceTransactionManager transactionManager;

	String new1Min = "0000000050";
	String new1Max = "0000000125";
	String new2Max = "0000000215";

	String existingMin = "0000000100";
	String existingMax = "0000000200";

	StopWatch sw;

	static final Date LOCK_ACTIVE = new Date(System.currentTimeMillis() + 60000);
	static final String LOCK_RESOURCE_TYPE_CODE = "file";
	static final String LOCK_RESOURCE_NAME = "myLock";
	static final Date NOW = new Date();
	static final Date LOCK_BEGINS = new Date(System.currentTimeMillis() - 600000);
	static final Date LOCK_EXPIRES = new Date(System.currentTimeMillis() - 300000);

	@Autowired
	DataSource dataSource;

	/**
	 * Run prior to each test case
	 * 
	 * @throws Exception
	 *             of type Exception
	 */
	@Before
	public void setUp() throws Exception {
		SecurityContextHolder.getInstance().removeAuditPrincipal();
		lockDaoFactory = new LockDAOFactoryImpl(dataSource);
		onlnLgclLckDAO = lockDaoFactory.getOnlnLgclLckDAO();
		lgclLckDAO = lockDaoFactory.getLgclLckDAO();
		LockCustomDAOFactoryImpl lockCustomDaoFactory = new LockCustomDAOFactoryImpl(dataSource);
		portletLogicalLockService = new PortletLogicalLockService(lockDaoFactory, lockCustomDaoFactory, dataSource);

		transactionManager = new DataSourceTransactionManager(dataSource);

		cleanup();
	}

	@Test(expected = IllegalArgumentException.class)
	public void lockExtendWithNullIdentifierThrowsException() throws UnsuccessfulLockException {
		LogicalLockIdentifier identifier = null;
		portletLogicalLockService.lockExtend(identifier);
	}

	@Test(expected = IllegalArgumentException.class)
	public void lockImmediateWithNullLockAttemptThrowsException() throws UnsuccessfulLockException {
		portletLogicalLockService.lockImmediate(null, null);
	}

	@Test
	public void thatUnlockWithNullLockIdentifierIsIgnored() throws UnsuccessfulLockException {
		portletLogicalLockService.unLock(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void lockImmediateWithNullPortletlRequestThrowsException() throws UnsuccessfulLockException {
		LockRequest lockAttempt = createLock(new1Min);
		portletLogicalLockService.lockImmediate(lockAttempt, null);
	}

	@Test
	public void lockExtendWithExpiredLockExtendsLock() throws UnsuccessfulLockException {
		TransactionStatus transactionStatus = begin("lockExtendWithExpiredLockExtendsLock");
		setExpiredLogicalLock();
		setExpiredOnlnLogicalLock();
		commit(transactionStatus);

		LogicalLockIdentifier identifier = new LogicalLockIdentifier("thisLock", LOCK_ACTIVE);
		portletLogicalLockService.lockExtend(identifier);
		List<LgclLck> locks = lgclLckDAO.findRangeByLckId("thisLock", "thisLock", null, null);
		assertTrue(locks.size() == 1);
		LgclLck lock = locks.get(0);
		assertTrue(lock.getLckExpTs().after(new Date(System.currentTimeMillis() + 150000)));
		portletLogicalLockService.unLock(identifier);
		LgclLck lock2 = lgclLckDAO.selectByPrimaryKey(lock.getKey());
		assertNull(lock2);
	}

	@Test
	public void lockExtendWithUnexpiredLockWorksAsExpected() throws UnsuccessfulLockException {
		TransactionStatus transactionStatus = begin("lockExtendWithUnexpiredLockWorksAsExpected");
		setActiveLogicalLock();
		setExpiredOnlnLogicalLock();
		commit(transactionStatus);

		LogicalLockIdentifier identifier = new LogicalLockIdentifier("thisLock", LOCK_ACTIVE);
		portletLogicalLockService.lockExtend(identifier);
		List<LgclLck> locks = lgclLckDAO.findRangeByLckId("thisLock", "thisLock", null, null);
		assertTrue(locks.size() == 1);
		LgclLck lock = locks.get(0);
		assertTrue(lock.getLckExpTs().after(new Date(System.currentTimeMillis() + 150000)));
		portletLogicalLockService.unLock(identifier);
		LgclLck lock2 = lgclLckDAO.selectByPrimaryKey(lock.getKey());
		assertNull(lock2);
	}

	@Test
	public void lockImmediateWorksAsExpected() throws UnsuccessfulLockException {
		LockRequest lockAttempt = createLock(new1Max);
		PortletRequest request = createPortletRequest();
		LogicalLockIdentifier identifier = portletLogicalLockService.lockImmediate(lockAttempt, request);

		LgclLckKey lgclLckKey = createLogicalLockKey(new1Max, new1Max);
		OnlnLgclLckKey onlnLgclLckKey = createOnlnLogicalLockKey(new1Max, new1Max);
		LgclLck lock = lgclLckDAO.selectByPrimaryKey(lgclLckKey);
		assertNotNull(lock);
		assertTrue(lock.getLckId().equals(identifier.getIdentifier()));
		OnlnLgclLck onlineLock = onlnLgclLckDAO.selectByPrimaryKey(onlnLgclLckKey);
		assertNotNull(onlineLock);
		portletLogicalLockService.unLock(identifier);
		LgclLck lock2 = lgclLckDAO.selectByPrimaryKey(lgclLckKey);
		assertNull(lock2);
	}

	@Test(expected = UnsuccessfulLockException.class)
	public void lockImmediateWithExistingLockThrowsException() throws UnsuccessfulLockException {
		LockRequest lockAttempt = createLock(new1Max);
		PortletRequest request = createPortletRequest();
		LogicalLockIdentifier identifier = portletLogicalLockService.lockImmediate(lockAttempt, request);

		LockRequest lockAttempt2 = createLock(new1Max);
		LogicalLockIdentifier identifier2 = portletLogicalLockService.lockImmediate(lockAttempt2, request);
	}

	@Test
	public void lockImmediateWithExpiredExistingLockWorksAsExpected() throws UnsuccessfulLockException {
		TransactionStatus transactionStatus = begin("lockImmediateWithExpiredExistingLockWorksAsExpected");
		setActiveLogicalLock();
		setExpiredOnlnLogicalLock();
		commit(transactionStatus);

		LockRequest lockAttempt = createLock(new2Max);
		PortletRequest request = createPortletRequest();
		LogicalLockIdentifier identifier = portletLogicalLockService.lockImmediate(lockAttempt, request);

		LgclLckKey lgclLckKey = createLogicalLockKey(new2Max, new2Max);
		OnlnLgclLckKey onlnLgclLckKey = createOnlnLogicalLockKey(new2Max, new2Max);
		LgclLck lock = lgclLckDAO.selectByPrimaryKey(lgclLckKey);
		assertNotNull(lock);
		assertTrue(lock.getLckId().equals(identifier.getIdentifier()));
		OnlnLgclLck onlineLock = onlnLgclLckDAO.selectByPrimaryKey(onlnLgclLckKey);
		assertNotNull(onlineLock);
		portletLogicalLockService.unLock(identifier);
	}

	@After
	public void cleanup() {
		logger.debug("\n\nENTERING CLEANUP\n\n");
		if (null != sw) {
			System.out.println(sw.toString());
		}
		// loop and delete all logical locks
		List<LgclLck> logicalLocks = lgclLckDAO.findRangeByLckId("0", null, null, null);
		logger.debug("found " + logicalLocks.size() + " logical locks.");
		for (LgclLck lock : logicalLocks) {
			lgclLckDAO.deleteByPrimaryKey(lock.getKey());
		}

		// loop and delete all online locks
		List<OnlnLgclLck> onlineLocks = onlnLgclLckDAO
				.findRangeByLckResrcTypeCdByLckResrcNmByMinLckResrcKyValuTxByMaxLckResrcKyValuTxByLckReqTs(
						LOCK_RESOURCE_TYPE_CODE, null, null, null, null, null, null, null, null, null, null, null);
		logger.debug("found " + onlineLocks.size() + " online locks.");
		for (OnlnLgclLck lock : onlineLocks) {
			onlnLgclLckDAO.deleteByPrimaryKey(lock.getKey());
		}
		logger.debug("\n\nDONE WITH CLEANUP\n\n");
	}

	class MutableLockRequest extends LockRequest {
		public MutableLockRequest(LockRequest lockRequest) {
			super(lockRequest.getLockResourceTypeCode(), lockRequest.getLockResourceName(), lockRequest
					.getMinimumLockResourceKeyValueText(), lockRequest.getMaximumLockResourceKeyValueText());
			lockRequestTimestamp = lockRequest.getLockRequestTimestamp();
		}

		/**
		 * Increments the timestamp value to the next possible value. Useful only to the lock manager.
		 */
		public void setLockRequestTimestamp(Date date) {
			lockRequestTimestamp = date;
		}
	}

	private LockRequest createLock(String min) {
		LockRequest lock = new LockRequest(LOCK_RESOURCE_TYPE_CODE, LOCK_RESOURCE_NAME, min, min);
		MutableLockRequest lockRequest = new MutableLockRequest(lock);
		lockRequest.setLockRequestTimestamp(NOW);
		return lockRequest;
	}

	private PortletRequest createPortletRequest() {
		MockPortletRequest request = new MockPortletRequest();
		request.getPortletSession(true);
		request.setPortletMode(PortletMode.VIEW);
		request.setWindowState(WindowState.NORMAL);
		return request;
	}

	private void setExpiredLogicalLock() {
		LgclLckKey key = createLogicalLockKey(existingMin, existingMax);
		LgclLck lock = new LgclLck();
		lock.setKey(key);
		lock.setAppPrtflId(BigDecimal.ZERO);
		lock.setCrtTs(LOCK_BEGINS);
		lock.setCrtUserId("PortletLogicalLockServiceTest");
		lock.setLckBgnTs(LOCK_BEGINS);
		lock.setLckExpTs(LOCK_EXPIRES);
		lock.setLckId("thisLock");
		lock.setLckUserId("me");
		lock.setRunEnvrmtTypeCd("run");

		lgclLckDAO.insert(lock);
	}

	private void setActiveLogicalLock() {
		LgclLckKey key = createLogicalLockKey(existingMin, existingMax);
		LgclLck lock = new LgclLck();
		lock.setKey(key);
		lock.setAppPrtflId(BigDecimal.ZERO);
		lock.setCrtTs(NOW);
		lock.setCrtUserId("PortletLogicalLockServiceTest");
		lock.setLckBgnTs(NOW);
		lock.setLckExpTs(LOCK_ACTIVE);
		lock.setLckId("thisLock");
		lock.setLckUserId("me");
		lock.setRunEnvrmtTypeCd("run");

		lgclLckDAO.insert(lock);
	}

	private void setExpiredOnlnLogicalLock() {
		OnlnLgclLckKey key = createOnlnLogicalLockKey(existingMin, existingMax);
		OnlnLgclLck lock = new OnlnLgclLck();
		lock.setKey(key);
		lock.setCrtTs(NOW);
		lock.setCrtUserId("PortletLogicalLockServiceTest");
		lock.setLcleCd("lcle");
		lock.setPrtltModeNm("View");
		lock.setPrtltNm("portlet name");
		lock.setPrtltPgReqNm("portlet name");
		lock.setScrnId("screen");
		lock.setSessId("session");
		lock.setWndwStNm("maximized");

		onlnLgclLckDAO.insert(lock);
	}

	private LgclLckKey createLogicalLockKey(String min, String max) {
		LgclLckKey key = new LgclLckKey();
		key.setLckResrcNm(LOCK_RESOURCE_NAME);
		key.setLckResrcTypeCd(LOCK_RESOURCE_TYPE_CODE);
		key.setLckReqTs(NOW);
		key.setMinLckResrcKyValuTx(min);
		key.setMaxLckResrcKyValuTx(max);
		return key;
	}

	private OnlnLgclLckKey createOnlnLogicalLockKey(String min, String max) {
		OnlnLgclLckKey key = new OnlnLgclLckKey();
		key.setLckResrcNm(LOCK_RESOURCE_NAME);
		key.setLckResrcTypeCd(LOCK_RESOURCE_TYPE_CODE);
		key.setLckReqTs(NOW);
		key.setMinLckResrcKyValuTx(min);
		key.setMaxLckResrcKyValuTx(max);
		return key;
	}

	void commit(TransactionStatus transactionStatus) {
		StopWatch sw = new StopWatch().start();
		logger.debug("ENTRY");

		if (transactionStatus.isCompleted()) {
			logger.debug("Commit requested but no active transaction, no commit performed.");
		} else {
			transactionManager.commit(transactionStatus);
		}

		logger.debug("RETURN {}", sw);
	}

	TransactionStatus begin(String name) {
		DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
		transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
		/*
		 * transactionStatus = transactionManager .getTransaction(transactionDefinition);
		 */
		transactionDefinition.setName(name);
		return transactionManager.getTransaction(transactionDefinition);
	}
}
