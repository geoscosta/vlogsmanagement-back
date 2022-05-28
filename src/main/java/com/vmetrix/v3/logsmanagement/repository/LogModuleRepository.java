package com.vmetrix.v3.logsmanagement.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Query;

import org.hibernate.Session;

import com.vmetrix.v3.logsmanagement.api.V3LogModule;
import com.vmetrix.v3.logsmanagement.model.LogLevelConf;
import com.vmetrix.v3.logsmanagement.model.LogModule;
import com.vmetrix.v3.logsmanagement.model.LogSetting;
import com.vmetrix.v3.pagination.V3Pagination;
import com.vmetrix.v3.persistence.database.DatabaseBO;
import com.vmetrix.v3.persistence.repository.V3SimpleDataRepository;
/**
 * 
 * repository of module
 *
 */
public class LogModuleRepository extends V3SimpleDataRepository<LogModule, Long> {

	public LogModuleRepository(Class<LogModule> beanType) {
		super(beanType);
	}

	public LogModuleRepository() {
		super(LogModule.class);
	}

	/**
	 * List all modules
	 * @param page
	 * @return List<V3LogModule>
	 */
	public List<V3LogModule> findAll(V3Pagination<V3LogModule> page) {
		List<V3LogModule> result = new ArrayList<V3LogModule>();
		Collection<LogModule> moduleList = findAll();
		List<LogLevelConf> logLevelConfList = new ArrayList<>();

		for (LogModule logModule : moduleList) {
			logLevelConfList.addAll(logModule.getLevelConfList());
		}
		
		result = logLevelConfList.stream().map(V3LogModule::new).collect(Collectors.toList());

		return result;
	}

	/**
	 * find module by namemodule
	 * @param moduleName
	 * @return LogLevelConf
	 */
	public LogLevelConf findByNameModule(String moduleName) {

		DatabaseBO dbBO = DatabaseBO.getMainInstance();
		Session hibernateSession = dbBO.getConnection(true);

		String objectQuery = "select c from LogLevelConf as c where c.module.nameModule like : name ";

		Query query = hibernateSession.createQuery(objectQuery);
		query.setParameter("name", moduleName);

		List<LogLevelConf> resultList = query.getResultList();

		if (!resultList.isEmpty()) {
			return resultList.get(0);
		}

		return null;
	}

	/**
	 * find module by log setting
	 * @param logSetting
	 * @return List<LogModule>
	 */
	public List<LogModule> findbyLogSetting(LogSetting logSetting) {

		DatabaseBO dbBO = DatabaseBO.getMainInstance();
		Session hibernateSession = dbBO.getConnection(true);
		String objectQuery = "Select m from LogModule as m where m.id in ( select c.module from LogLevelConf as c where c.setting in (select s.id from LogSetting as s where s.id = :idLevelConf)) ";

		Query query = hibernateSession.createQuery(objectQuery);
		query.setParameter("idLevelConf", logSetting.getId());

		List<LogModule> resultList = query.getResultList();

		return resultList;
	}

}
