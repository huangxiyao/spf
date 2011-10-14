package com.hp.it.cas.security.dao;

import com.ibatis.sqlmap.client.SqlMapClient;
import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.ibatis.SqlMapClientFactoryBean;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * A DAO Factory class that provides a single point of access for all the DAOs
 * in the security environment.
 *
 * @author CAS Generator v1.0.0
 */
public class SecurityDAOFactoryImpl implements ISecurityDAOFactory {
	private final IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDAO;
	private final IAppUserAttrPrmsnDAO appUserAttrPrmsnDAO;
	private final ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDAO;
	private final IUserAttrDAO userAttrDAO;
	private final IUserAttrTypeDAO userAttrTypeDAO;
	private final IUserAttrValuDAO userAttrValuDAO;
	private final IUserIdTypeDAO userIdTypeDAO;

     /**
     * Creates the Factory
     * @param dataSource  a DataSource object
     * @throws Exception 
     */	
    public SecurityDAOFactoryImpl(DataSource dataSource) throws Exception {

        SqlMapClientFactoryBean sqlMapClientFactoryBean = new SqlMapClientFactoryBean();
		sqlMapClientFactoryBean.setDataSource(dataSource);
		sqlMapClientFactoryBean.setConfigLocation(new ClassPathResource("com/hp/it/cas/security/dao/sqlMapConfig.xml"));
		sqlMapClientFactoryBean.afterPropertiesSet();
		
		SqlMapClient sqlMapClient = (SqlMapClient) sqlMapClientFactoryBean.getObject();
		SqlMapClientTemplate sqlMapClientTemplate = new SqlMapClientTemplate(sqlMapClient);
		sqlMapClientTemplate.afterPropertiesSet();
		
		appCmpndAttrSmplAttrPrmsnDAO = new AppCmpndAttrSmplAttrPrmsnDAOImpl(sqlMapClient);
		appUserAttrPrmsnDAO = new AppUserAttrPrmsnDAOImpl(sqlMapClient);
		cmpndAttrSmplAttrDAO = new CmpndAttrSmplAttrDAOImpl(sqlMapClient);
		userAttrDAO = new UserAttrDAOImpl(sqlMapClient);
		userAttrTypeDAO = new UserAttrTypeDAOImpl(sqlMapClient);
		userAttrValuDAO = new UserAttrValuDAOImpl(sqlMapClient);
		userIdTypeDAO = new UserIdTypeDAOImpl(sqlMapClient);
	}

	public IAppCmpndAttrSmplAttrPrmsnDAO getAppCmpndAttrSmplAttrPrmsnDAO() {
		return appCmpndAttrSmplAttrPrmsnDAO;
	}	
	public IAppUserAttrPrmsnDAO getAppUserAttrPrmsnDAO() {
		return appUserAttrPrmsnDAO;
	}	
	public ICmpndAttrSmplAttrDAO getCmpndAttrSmplAttrDAO() {
		return cmpndAttrSmplAttrDAO;
	}	
	public IUserAttrDAO getUserAttrDAO() {
		return userAttrDAO;
	}	
	public IUserAttrTypeDAO getUserAttrTypeDAO() {
		return userAttrTypeDAO;
	}	
	public IUserAttrValuDAO getUserAttrValuDAO() {
		return userAttrValuDAO;
	}	
	public IUserIdTypeDAO getUserIdTypeDAO() {
		return userIdTypeDAO;
	}	

}
