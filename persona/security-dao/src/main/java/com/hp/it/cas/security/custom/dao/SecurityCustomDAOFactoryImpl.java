package com.hp.it.cas.security.custom.dao;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.ibatis.SqlMapClientFactoryBean;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Roger Spotts
 *
 */
public class SecurityCustomDAOFactoryImpl implements ISecurityCustomDAOFactory {

	private final IUserAttrCustomDAO userAttrCustomDAO;
	private final ICmpndAttrSmplAttrCustomDAO cmpndAttrSmplAttrCustomDAO;
	private final IUserAttrValuCustomDAO userAttrValuCustomDAO;
	private final IAppUserAttrPrmsnCustomDAO appUserAttrPrmsnCustomDAO;
	private final IAppCmpndAttrSmplAttrPrmsnCustomDAO appCmpndAttrSmplAttrPrmsnCustomDAO;
	
	/**
     * Creates the Factory
     * @param dataSource  a DataSource object
     * @throws Exception
     */	
	public SecurityCustomDAOFactoryImpl(DataSource dataSource) throws Exception {

		SqlMapClientFactoryBean sqlMapClientFactoryBean = new SqlMapClientFactoryBean();
		sqlMapClientFactoryBean.setDataSource(dataSource);
		sqlMapClientFactoryBean.setConfigLocation(new ClassPathResource("com/hp/it/cas/security/custom/dao/sqlMapConfig.xml"));
		sqlMapClientFactoryBean.afterPropertiesSet();
		
		SqlMapClient sqlMapClient = (SqlMapClient) sqlMapClientFactoryBean.getObject();
		SqlMapClientTemplate sqlMapClientTemplate = new SqlMapClientTemplate(sqlMapClient);
		sqlMapClientTemplate.afterPropertiesSet();
		
		userAttrCustomDAO = new UserAttrCustomDAOImpl(sqlMapClient);
		cmpndAttrSmplAttrCustomDAO = new CmpndAttrSmplAttrCustomDAOImpl(sqlMapClient);
		userAttrValuCustomDAO = new UserAttrValuCustomDAOImpl(sqlMapClient);
		appUserAttrPrmsnCustomDAO = new AppUserAttrPrmsnCustomDAOImpl(sqlMapClient);
		appCmpndAttrSmplAttrPrmsnCustomDAO = new AppCmpndAttrSmplAttrPrmsnCustomDAOImpl(sqlMapClient);
		
	}
	
	public IUserAttrCustomDAO getUserAttrCustomDAO() {
		return userAttrCustomDAO;
	}
	
	public ICmpndAttrSmplAttrCustomDAO getCmpndAttrSmplAttrCustomDAO() {
		return cmpndAttrSmplAttrCustomDAO;
	}
	
	public IUserAttrValuCustomDAO getUserAttrValuCustomDAO() {
		return userAttrValuCustomDAO;
	}
	
	public IAppUserAttrPrmsnCustomDAO getAppUserAttrPrmsnCustomDAO() {
		return appUserAttrPrmsnCustomDAO;
	}
	
	public IAppCmpndAttrSmplAttrPrmsnCustomDAO getAppCmpndAttrSmplAttrPrmsnCustomDAO() {
		return appCmpndAttrSmplAttrPrmsnCustomDAO;
	}
}
