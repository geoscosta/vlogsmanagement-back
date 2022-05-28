package com.vmetrix.v3.logsmanagement.repository;

import com.vmetrix.v3.logsmanagement.model.LogLevelConf;
import com.vmetrix.v3.persistence.repository.V3SimpleDataRepository;

/**
 * 
 * repository of level configuration
 *
 */
public class LogLevelConfRepository extends V3SimpleDataRepository<LogLevelConf, Long> {

	public LogLevelConfRepository(Class<LogLevelConf> beanType) {
		super(beanType);
	}
	
	public LogLevelConfRepository() {
		super(LogLevelConf.class);
	}

}
