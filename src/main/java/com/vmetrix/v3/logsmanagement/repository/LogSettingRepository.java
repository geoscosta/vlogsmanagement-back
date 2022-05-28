package com.vmetrix.v3.logsmanagement.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import com.vmetrix.v3.logsmanagement.api.V3LogSetting;
import com.vmetrix.v3.logsmanagement.api.V3LogsManagementFacadeImpl;
import com.vmetrix.v3.logsmanagement.enums.LogType;
import com.vmetrix.v3.logsmanagement.model.LogSetting;
import com.vmetrix.v3.pagination.V3Pagination;
import com.vmetrix.v3.persistence.database.DatabaseBO;
import com.vmetrix.v3.persistence.repository.V3SimpleDataRepository;
/**
 * 
 * repository of log settings
 *
 */
public class LogSettingRepository extends V3SimpleDataRepository<LogSetting, Long> {

	public LogSettingRepository(Class<LogSetting> beanType) {
		super(beanType);
	}

	public LogSettingRepository() {
		super(LogSetting.class);
	}

	/**
	 * find all log setting
	 * @param page
	 * @return List<V3LogSetting>
	 */
	public List<V3LogSetting> findAllV3LogSetting(V3Pagination<V3LogSetting> page) {
		List<V3LogSetting> result = new ArrayList<V3LogSetting>();
		Collection<LogSetting> list = findAll();
		for (LogSetting logSetting : list) {
			V3LogSetting v3LogSetting = V3LogsManagementFacadeImpl.singleton().newV3LogSetting(logSetting);
			result.add(v3LogSetting);
		}
		return result;
	}

	@Override
	/**
	 * save the edits in settings
	 * @param v3LogSetting
	 */
	public Long save(LogSetting v3LogSetting) {
		return super.save(v3LogSetting);
	}

	/**
	 * find log setting by runtime viewer
	 * c autoScroll
	 *c pause
	 * @param module
	 * @param filter
	 * @param type
	 * @return
	 */
	public V3LogSetting findLogSettingsByRuntimeViewer(boolean autoScroll, boolean pause, String module, String filter,
			LogType type) {

		V3LogSetting v3LogSetting = null;

		DatabaseBO dbBO = DatabaseBO.getMainInstance();
		Session hibernateSession = dbBO.getConnection(true);
		String objectQuery = "SELECT S FROM LogSetting as S " + "where S in  (select L.setting from LogLevelConf as L "
				+ "where L.module in ( select M from LogModule M where M in "
				+ "(select R.module from LogRecord R where R.tpLog = :logType ";
		if (!module.equalsIgnoreCase("")) {
			objectQuery += "and L.module.nameModule = :module ";
		}
		if (!filter.equalsIgnoreCase("")) {
			objectQuery += "and R.textMessage like :filter ";
		}
		objectQuery += ")))";

		Query query = hibernateSession.createQuery(objectQuery);

		query.setParameter("logType", type.getIntValue());
		if (!module.equalsIgnoreCase("")) {
			query.setParameter("module", module);
		}
		if (!filter.equalsIgnoreCase("")) {
			query.setParameter("filter", filter);
		}

		List<LogSetting> resultList = query.getResultList();

		if (!resultList.isEmpty()) {
			
			
//			v3LogSetting = new V3LogSetting(resultList.get(0));
		}

		return v3LogSetting;
	}

	/**
	 * find log settings by module name
	 * @param moduleName
	 * @return
	 */
	public LogSetting findLogSettingsByModuleName(String moduleName) {

		DatabaseBO dbBO = DatabaseBO.getMainInstance();
		Session hibernateSession = dbBO.getConnection(true);
		String objectQuery = "SELECT S FROM LogSetting as S " + "where S in  (select L.setting from LogLevelConf as L "
				+ "where L.module.nameModule = :module )";

		Query query = hibernateSession.createQuery(objectQuery);

		query.setParameter("module", moduleName);

		List<LogSetting> resultList = query.getResultList();

		if (!resultList.isEmpty()) {
			return resultList.get(0);
		}

		return null;
	}

}
