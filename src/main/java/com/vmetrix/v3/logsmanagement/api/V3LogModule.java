package com.vmetrix.v3.logsmanagement.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.vmetrix.v3.logsmanagement.api.manager.V3LogManager;
import com.vmetrix.v3.logsmanagement.enums.LevelType;
import com.vmetrix.v3.logsmanagement.enums.LogType;
import com.vmetrix.v3.logsmanagement.model.LogLevelConf;
import com.vmetrix.v3.logsmanagement.model.LogSetting;
import com.vmetrix.v3.logsmanagement.repository.LogLevelConfRepository;
import com.vmetrix.v3.logsmanagement.repository.LogModuleRepository;
import com.vmetrix.v3.pagination.V3Pagination;

/**
 * 
 * Class Used to work whit Log Module Data
 *
 */
public class V3LogModule {

	private LogModuleRepository logModuleRepository;
	private LogLevelConfRepository logLevelConfRepository;
	private String core;
	private String moduleName;
	private String packageName;
	private String levelType;
	private Long idLevelConf;

	/**
	 * 
	 * Create a new Instance empty instance
	 */
	private V3LogModule() {}
	
	public static V3LogModule newInstance() {
		V3LogModule v3LogModule = new V3LogModule();
		v3LogModule.logModuleRepository = new LogModuleRepository();
		v3LogModule.logLevelConfRepository = new LogLevelConfRepository();
		return v3LogModule;
	}

	/**
	 * 
	 * Create a new Instance with LogLevelConf Data
	 */
	public V3LogModule(LogLevelConf logLevelConf) {
		this.core = LogType.of(logLevelConf.getModule().getTpCore()).getDescription();
		this.moduleName = logLevelConf.getModule().getNameModule();
		this.packageName = logLevelConf.getModule().getTextPackage();
		this.levelType = LevelType.of(logLevelConf.getTpLevel()).getDescription();
		this.idLevelConf = logLevelConf.getId();
	}

	/**
	 * 
	 * Create a new Instance
	 */
	public static V3LogModule create() {
		return new V3LogModule();
	}

	/**
	 * 
	 * @param logSetting
	 * @return a List whit all V3LogModule by LogSetting
	 */
	public static List<V3LogModule> findAllByLogSetting(LogSetting logSetting) {
		List<V3LogModule> v3LogModules = new ArrayList<>();

		List<LogLevelConf> logLevelConfList = logSetting.getLevelConfList();
		
		v3LogModules = logLevelConfList.stream().map(V3LogModule::new).collect(Collectors.toList());

		return v3LogModules;
	}

	/**
	 * 
	 * @param moduleName
	 * @return V3LogModule
	 */
	public V3LogModule findByNameModule(String moduleName) {
		return new V3LogModule(logModuleRepository.findByNameModule(moduleName));
	}

	/**
	 * 
	 * @param page Not available by the persistence framework
	 * @return a List whit all V3LogModule
	 */
	public List<V3LogModule> findAll(V3Pagination<V3LogModule> page) {
		List<V3LogModule> v3LogModuleList = new ArrayList<V3LogModule>();

		for (LogLevelConf logLevelConf : logLevelConfRepository.findAll()) {
			v3LogModuleList.add(new V3LogModule(logLevelConf));
		}
		return v3LogModuleList;
	}

	/**
	 * 
	 * Update LevelType changes
	 * 
	 * @param List of v3LogModules
	 */
	public void updateV3ModuleList(List<V3LogModule> v3LogModules) {

		for (V3LogModule v3LogModule : v3LogModules) {
			update(v3LogModule);

		}
		V3LogManager.applyLoggerConfigFromDB();
	}

	/**
	 * 
	 * @param v3LogModule
	 */
	private void update(V3LogModule v3LogModule) {
		
		LogLevelConf logLevelConfModule = logLevelConfRepository.findById("id", v3LogModule.idLevelConf).get();

		logLevelConfModule.setTpLevel(LevelType.ofString(v3LogModule.levelType).ordinal());
		logLevelConfRepository.update(logLevelConfModule);

	}

	public String getCore() {
		return core;
	}

	public void setCore(String core) {
		this.core = core;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getLevelType() {
		return levelType;
	}

	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}

	public Long getIdLevelConf() {
		return idLevelConf;
	}

	public void setIdLevelConf(Long idLevelConf) {
		this.idLevelConf = idLevelConf;
	}

}
