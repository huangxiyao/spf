package com.hp.it.cas.persona.mock.dao;

import com.hp.it.cas.config.dao.IAlmDAO;
import com.hp.it.cas.config.dao.IAppDAO;
import com.hp.it.cas.config.dao.IAppSysParmDAO;
import com.hp.it.cas.config.dao.IBusTxnObjVersDAO;
import com.hp.it.cas.config.dao.IBusTxnObjVersSrchFldDAO;
import com.hp.it.cas.config.dao.IBusTxnTypeDAO;
import com.hp.it.cas.config.dao.ICmputPrcsDAO;
import com.hp.it.cas.config.dao.ICmputPrcsRunCnfgDAO;
import com.hp.it.cas.config.dao.ICmputPrcsRunStDAO;
import com.hp.it.cas.config.dao.ICmputPrcsTypeDAO;
import com.hp.it.cas.config.dao.IConfigDAOFactory;
import com.hp.it.cas.config.dao.IDrvrModDAO;
import com.hp.it.cas.config.dao.IDrvrModParmDAO;
import com.hp.it.cas.config.dao.IDrvrModTypeDAO;
import com.hp.it.cas.config.dao.IJobAlmDAO;
import com.hp.it.cas.config.dao.IJobDAO;
import com.hp.it.cas.config.dao.IJobInstncRunCnfgDAO;
import com.hp.it.cas.config.dao.IJobInstncTxdrvDrvrRunCnfgDAO;
import com.hp.it.cas.config.dao.IJobParmDAO;
import com.hp.it.cas.config.dao.IJobRunCnfgDAO;
import com.hp.it.cas.config.dao.ILcleDAO;
import com.hp.it.cas.config.dao.ILlMsgDAO;
import com.hp.it.cas.config.dao.IMsgDAO;
import com.hp.it.cas.config.dao.IOsClsDAO;
import com.hp.it.cas.config.dao.IPhysclSrvrDAO;
import com.hp.it.cas.config.dao.IPltfrmDAO;
import com.hp.it.cas.config.dao.ISrvrDAO;
import com.hp.it.cas.config.dao.ISrvrEnvrmtDAO;
import com.hp.it.cas.config.dao.ISrvrGrpDAO;
import com.hp.it.cas.config.dao.ISrvrGrpSrvrDAO;
import com.hp.it.cas.config.dao.ISrvrGrpUseDAO;
import com.hp.it.cas.config.dao.ISrvrGrpgRsnDAO;
import com.hp.it.cas.config.dao.ISrvrStatDAO;
import com.hp.it.cas.config.dao.ISrvrTypeDAO;
import com.hp.it.cas.config.dao.ITblDataExtrcCtrlDAO;
import com.hp.it.cas.config.dao.IVrtSrvrInstncDAO;

public class MockConfigDAOFactory implements IConfigDAOFactory {

	private final IAppDAO appDao;
	
	public MockConfigDAOFactory(MockDatabase database) {
		appDao = new MockAppDAO(database);
	}
	
	public IAlmDAO getAlmDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IAppDAO getAppDAO() {
		return appDao;
	}

	public IAppSysParmDAO getAppSysParmDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IBusTxnObjVersDAO getBusTxnObjVersDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IBusTxnTypeDAO getBusTxnTypeDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public ICmputPrcsDAO getCmputPrcsDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public ICmputPrcsRunCnfgDAO getCmputPrcsRunCnfgDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public ICmputPrcsRunStDAO getCmputPrcsRunStDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public ICmputPrcsTypeDAO getCmputPrcsTypeDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IDrvrModDAO getDrvrModDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IDrvrModParmDAO getDrvrModParmDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IDrvrModTypeDAO getDrvrModTypeDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IJobAlmDAO getJobAlmDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IJobDAO getJobDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IJobInstncRunCnfgDAO getJobInstncRunCnfgDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IJobInstncTxdrvDrvrRunCnfgDAO getJobInstncTxdrvDrvrRunCnfgDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IJobParmDAO getJobParmDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IJobRunCnfgDAO getJobRunCnfgDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public ILlMsgDAO getLlMsgDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IMsgDAO getMsgDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IOsClsDAO getOsClsDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPhysclSrvrDAO getPhysclSrvrDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPltfrmDAO getPltfrmDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public ISrvrDAO getSrvrDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public ISrvrEnvrmtDAO getSrvrEnvrmtDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public ISrvrGrpDAO getSrvrGrpDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public ISrvrGrpSrvrDAO getSrvrGrpSrvrDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public ISrvrGrpUseDAO getSrvrGrpUseDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public ISrvrGrpgRsnDAO getSrvrGrpgRsnDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public ISrvrStatDAO getSrvrStatDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public ISrvrTypeDAO getSrvrTypeDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	public IVrtSrvrInstncDAO getVrtSrvrInstncDAO() {
		// TODO Auto-generated method stub
		return null;
	}

    public ILcleDAO getLcleDAO() {
        // TODO Auto-generated method stub
        return null;
    }

    public ITblDataExtrcCtrlDAO getTblDataExtrcCtrlDAO() {
        // TODO Auto-generated method stub
        return null;
    }

    public IBusTxnObjVersSrchFldDAO getBusTxnObjVersSrchFldDAO() {
        // TODO Auto-generated method stub
        return null;
    }

}
