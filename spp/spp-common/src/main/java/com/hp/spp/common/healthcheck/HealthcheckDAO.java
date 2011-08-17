package com.hp.spp.common.healthcheck;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletRequest;

public interface HealthcheckDAO {
	List<HealthcheckServerInfo> getAllServers();
	void updateOutOfRotationFlag(List<HealthcheckServerInfo> info);
	List<HealthcheckServerInfo> findServersByExample(HealthcheckServerInfo infoExample);
}
