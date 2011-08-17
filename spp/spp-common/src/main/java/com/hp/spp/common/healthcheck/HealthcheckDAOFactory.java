package com.hp.spp.common.healthcheck;

public class HealthcheckDAOFactory {

    public HealthcheckDAO createHealthcheckDAO () {
        return new JDBCHealthcheckDAOImpl();
    }	
}
