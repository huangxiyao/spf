package com.hp.it.cas.persona.configuration.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import com.hp.it.cas.config.dao.App;
import com.hp.it.cas.config.dao.AppKey;
import com.hp.it.cas.config.dao.IAppDAO;
import com.hp.it.cas.config.dao.IConfigDAOFactory;
import com.hp.it.cas.persona.configuration.service.IApplication;
import com.hp.it.cas.persona.configuration.service.IMetadataConfigurationService;
import com.hp.it.cas.persona.configuration.service.IPermission;
import com.hp.it.cas.persona.configuration.service.ISecurityConfigurationService;
import com.hp.it.cas.persona.configuration.service.IUserAttribute;
import com.hp.it.cas.persona.dao.IPersonaDaoFactory;
import com.hp.it.cas.security.dao.AppCmpndAttrSmplAttrPrmsn;
import com.hp.it.cas.security.dao.AppCmpndAttrSmplAttrPrmsnKey;
import com.hp.it.cas.security.dao.AppUserAttrPrmsn;
import com.hp.it.cas.security.dao.AppUserAttrPrmsnKey;
import com.hp.it.cas.security.dao.UserAttr;
import com.hp.it.cas.security.dao.UserAttrKey;
import com.hp.it.cas.xa.logging.StopWatch;
import com.hp.it.cas.xa.validate.Verifier;

/**
 * A security configuration service implementation based on a DAO backing store.
 *
 * @author Quintin May
 */
public class SecurityConfigurationService extends AbstractConfigurationService implements ISecurityConfigurationService {

	private final Logger logger = LoggerFactory.getLogger(SecurityConfigurationService.class);
	
	private final IPersonaDaoFactory personaDaoFactory;
	private final IAppDAO appDao;
	private final IMetadataConfigurationService metadataConfigurationService;
	
	/**
	 * Creates a security configuration service.
	 * @param personaDaoFactory the DAO factory that provides Persona specific data.
	 * @param configDaoFactory the DAO factory that provides common configuration data.
	 * @param metadataConfigurationService the service that provides Persona attribute metadata.
	 */
	public SecurityConfigurationService(IPersonaDaoFactory personaDaoFactory, IConfigDAOFactory configDaoFactory, IMetadataConfigurationService metadataConfigurationService) {
		new Verifier()
			.isNotNull(personaDaoFactory, "Persona DAO factory cannot be null.")
			.isNotNull(configDaoFactory, "Configuration DAO factory cannot be null.")
			.isNotNull(metadataConfigurationService, "Metadata configuration service cannot be null.")
			.throwIfError();
		
		this.personaDaoFactory = personaDaoFactory;
		this.appDao = configDaoFactory.getAppDAO();
		this.metadataConfigurationService = metadataConfigurationService;
	}
	
	public IPermission addPermission(IApplication application, IUserAttribute userAttribute) {
		logger.debug("ENTRY ({}, {})", application, userAttribute);

		new Verifier()
			.isNotNull(application, "Application cannot be null.")
			.isNotNull(userAttribute, "User attribute cannot be null.")
			.throwIfError();

		StopWatch sw = new StopWatch().start();
		
		AppUserAttrPrmsnKey key = new AppUserAttrPrmsnKey();
		key.setAppPrtflId(new BigDecimal(application.getApplicationPortfolioIdentifier()));
		key.setUserAttrId(userAttribute.getUserAttributeIdentifier());
		
		AppUserAttrPrmsn appUserAttrPrmsn = new AppUserAttrPrmsn();
		appUserAttrPrmsn.setKey(key);
		
		Date now = new Date();
		String principalName = getAuditPrincipalName();
		appUserAttrPrmsn.setCrtTs(now);
		appUserAttrPrmsn.setCrtUserId(principalName);

		validateForeignKeyConstraints(appUserAttrPrmsn);
		personaDaoFactory.getAppUserAttrPrmsnDAO().insert(appUserAttrPrmsn);
		
		IPermission permission = new Permission(application, userAttribute);
		logger.debug("RETURN {} {}", sw, permission);
		return permission;
	}

	public IPermission addPermission(IApplication application, IUserAttribute compoundUserAttribute, IUserAttribute simpleUserAttribute) {
		logger.debug("ENTRY ({}, {}, {})", new Object[] {application, compoundUserAttribute, simpleUserAttribute});
		
		new Verifier()
    		.isNotNull(application, "Application cannot be null.")
    		.isNotNull(compoundUserAttribute, "Compound user attribute cannot be null.")
    		.isNotNull(simpleUserAttribute, "Simple user attribute cannot be null.")
    		.throwIfError();

		StopWatch sw = new StopWatch().start();
		
		AppCmpndAttrSmplAttrPrmsnKey key = new AppCmpndAttrSmplAttrPrmsnKey();
		key.setAppPrtflId(new BigDecimal(application.getApplicationPortfolioIdentifier()));
		key.setCmpndUserAttrId(compoundUserAttribute.getUserAttributeIdentifier());
		key.setSmplUserAttrId(simpleUserAttribute.getUserAttributeIdentifier());
		
		AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = new AppCmpndAttrSmplAttrPrmsn();
		appCmpndAttrSmplAttrPrmsn.setKey(key);
		
		Date now = new Date();
		String principalName = getAuditPrincipalName();
		appCmpndAttrSmplAttrPrmsn.setCrtTs(now);
		appCmpndAttrSmplAttrPrmsn.setCrtUserId(principalName);

		validateForeignKeyConstraints(appCmpndAttrSmplAttrPrmsn);
		personaDaoFactory.getAppCmpndAttrSmplAttrPrmsnDAO().insert(appCmpndAttrSmplAttrPrmsn);
		
		IPermission permission = new Permission(application, compoundUserAttribute, simpleUserAttribute);
		logger.debug("RETURN {} {}", sw, permission);
		return permission;
	}

	public IApplication findApplication(int applicationPortfolioIdentifier) {
		logger.debug("ENTRY ({})", applicationPortfolioIdentifier);
		
		StopWatch sw = new StopWatch().start();
		
		AppKey key = new AppKey();
		key.setAppPrtflId(new BigDecimal(applicationPortfolioIdentifier));
		App app = appDao.selectByPrimaryKey(key);
		
		IApplication application = app == null ? null : new Application(app);
		logger.debug("RETURN {} {}", sw, application);
		return application;
	}

	public Set<IPermission> findPermissions(IApplication application) {
		logger.debug("ENTRY ({})", application);
		
		new Verifier()
			.isNotNull(application, "Application cannot be null.")
			.throwIfError();
		
		StopWatch sw = new StopWatch().start();
		
		Set<IPermission> permissions = new HashSet<IPermission>();
		
		AppUserAttrPrmsnKey appUserAttrPrmsnKey = new AppUserAttrPrmsnKey();
		appUserAttrPrmsnKey.setAppPrtflId(new BigDecimal(application.getApplicationPortfolioIdentifier()));
		List<AppUserAttrPrmsn> appUserAttrPrmsns = personaDaoFactory.getAppUserAttrPrmsnDAO().selectByPrimaryKeyDiscretionary(appUserAttrPrmsnKey);
		
		for (AppUserAttrPrmsn permission : appUserAttrPrmsns) {
			permissions.add(new LazyUserAttributePermission(metadataConfigurationService, application, permission));
		}
		
		AppCmpndAttrSmplAttrPrmsnKey appCmpndAttrSmplAttrPrmsnKey = new AppCmpndAttrSmplAttrPrmsnKey();
		appCmpndAttrSmplAttrPrmsnKey.setAppPrtflId(new BigDecimal(application.getApplicationPortfolioIdentifier()));
		List<AppCmpndAttrSmplAttrPrmsn> appCmpndAttrSmplAttrPrmsns = personaDaoFactory.getAppCmpndAttrSmplAttrPrmsnDAO().selectByPrimaryKeyDiscretionary(appCmpndAttrSmplAttrPrmsnKey);
		
		for (AppCmpndAttrSmplAttrPrmsn permission : appCmpndAttrSmplAttrPrmsns) {
			permissions.add(new LazyCompoundAttributeSimpleAttributePermission(metadataConfigurationService, application, permission));
		}

		logger.debug("RETURN {} {}", sw, permissions);
		return permissions;
	}

	@SuppressWarnings("null")
	public void removePermission(IPermission permission) {
		logger.debug("ENTRY ({})", permission);
		
		new Verifier()
			.isNotNull(permission, "Permission cannot be null.")
			.throwIfError();
		
		StopWatch sw = new StopWatch().start();
		
		IUserAttribute compoundAttribute = permission.getCompoundUserAttribute();
		IUserAttribute simpleAttribute = permission.getSimpleUserAttribute();
		
		if (compoundAttribute == null || simpleAttribute == null) {
			AppUserAttrPrmsnKey key = new AppUserAttrPrmsnKey();
			key.setAppPrtflId(new BigDecimal(permission.getApplication().getApplicationPortfolioIdentifier()));
			key.setUserAttrId(compoundAttribute == null ? simpleAttribute.getUserAttributeIdentifier() : compoundAttribute.getUserAttributeIdentifier());
			
			personaDaoFactory.getAppUserAttrPrmsnDAO().deleteByPrimaryKey(key);
		} else {
			AppCmpndAttrSmplAttrPrmsnKey key = new AppCmpndAttrSmplAttrPrmsnKey();
			key.setAppPrtflId(new BigDecimal(permission.getApplication().getApplicationPortfolioIdentifier()));
			key.setCmpndUserAttrId(compoundAttribute.getUserAttributeIdentifier());
			key.setSmplUserAttrId(simpleAttribute.getUserAttributeIdentifier());
			
			personaDaoFactory.getAppCmpndAttrSmplAttrPrmsnDAO().deleteByPrimaryKey(key);
		}
		
		logger.debug("RETURN {}", sw);
	}

	private void validateForeignKeyConstraints(AppUserAttrPrmsn appUserAttrPrmsn) {
		logger.debug("ENTRY ({})", appUserAttrPrmsn);
		StopWatch sw = new StopWatch().start();
		
		// APP_PRTFL_ID
		if (appUserAttrPrmsn.getKey().getAppPrtflId() == null) {
			throw new DataIntegrityViolationException("APP_PRTFL_ID cannot be null.");
		}
		
		AppKey appKey = new AppKey();
		appKey.setAppPrtflId(appUserAttrPrmsn.getKey().getAppPrtflId());
		if (appDao.selectByPrimaryKey(appKey) == null) {
			throw new DataIntegrityViolationException(String.format("APP_PRTFL_ID '%s' is invalid.", appUserAttrPrmsn.getKey().getAppPrtflId()));
		}
		
		// USER_ATTR_KY
		if (appUserAttrPrmsn.getKey().getUserAttrId() == null) {
			throw new DataIntegrityViolationException("USER_ATTR_ID cannot be null.");
		}
		
		UserAttrKey userAttrKey = new UserAttrKey();
		userAttrKey.setUserAttrId(appUserAttrPrmsn.getKey().getUserAttrId());
		if (personaDaoFactory.getUserAttrDAO().selectByPrimaryKey(userAttrKey) == null) {
			throw new DataIntegrityViolationException(String.format("USER_ATTR_ID '%s' is invalid.", appUserAttrPrmsn.getKey().getUserAttrId()));
		}
		
		logger.debug("RETURN {}", sw);
	}

	private void validateForeignKeyConstraints(AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn) {
		logger.debug("ENTRY ({})", appCmpndAttrSmplAttrPrmsn);
		StopWatch sw = new StopWatch().start();
		
		// APP_PRTFL_ID
		if (appCmpndAttrSmplAttrPrmsn.getKey().getAppPrtflId() == null) {
			throw new DataIntegrityViolationException("APP_PRTFL_ID cannot be null.");
		}
		
		AppKey appKey = new AppKey();
		appKey.setAppPrtflId(appCmpndAttrSmplAttrPrmsn.getKey().getAppPrtflId());
		if (appDao.selectByPrimaryKey(appKey) == null) {
			throw new DataIntegrityViolationException(String.format("APP_PRTFL_ID '%s' is invalid.", appCmpndAttrSmplAttrPrmsn.getKey().getAppPrtflId()));
		}
		
		// CMPND_USER_ATTR_KY
		if (appCmpndAttrSmplAttrPrmsn.getKey().getCmpndUserAttrId() == null) {
			throw new DataIntegrityViolationException("CMPND_USER_ATTR_ID cannot be null.");
		}
		
		UserAttrKey userAttrKey = new UserAttrKey();
		userAttrKey.setUserAttrId(appCmpndAttrSmplAttrPrmsn.getKey().getCmpndUserAttrId());
		UserAttr userAttr = personaDaoFactory.getUserAttrDAO().selectByPrimaryKey(userAttrKey);
		if (userAttr == null || ! EUserAttributeType.COMPOUND.getUserAttributeTypeCode().equals(userAttr.getUserAttrTypeCd())) {
			throw new DataIntegrityViolationException(String.format("CMPND_USER_ATTR_ID '%s' is invalid.", appCmpndAttrSmplAttrPrmsn.getKey().getCmpndUserAttrId()));
		}

		// SMPL_USER_ATTR_KY
		if (appCmpndAttrSmplAttrPrmsn.getKey().getSmplUserAttrId() == null) {
			throw new DataIntegrityViolationException("SMPL_USER_ATTR_ID cannot be null.");
		}
		
		userAttrKey.setUserAttrId(appCmpndAttrSmplAttrPrmsn.getKey().getSmplUserAttrId());
		userAttr = personaDaoFactory.getUserAttrDAO().selectByPrimaryKey(userAttrKey);
		if (userAttr == null || ! EUserAttributeType.SIMPLE.getUserAttributeTypeCode().equals(userAttr.getUserAttrTypeCd())) {
			throw new DataIntegrityViolationException(String.format("SMPL_USER_ATTR_ID '%s' is invalid.", appCmpndAttrSmplAttrPrmsn.getKey().getSmplUserAttrId()));
		}
		
		logger.debug("RETURN {}", sw);
	}
}
