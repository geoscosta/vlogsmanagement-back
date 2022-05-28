package com.vmetrix.v3.logsmanagement.api;

import java.util.List;

import com.vmetrix.v3.logsmanagement.model.LogSetting;
import com.vmetrix.v3.logsmanagement.repository.LogLevelConfRepository;
import com.vmetrix.v3.logsmanagement.repository.LogModuleRepository;
import com.vmetrix.v3.logsmanagement.repository.LogSettingRepository;
import com.vmetrix.v3.pagination.V3Pagination;

/**
 * 
 * Class used to show Log Setting data
 *
 */
public class V3LogSetting {

	private LogSettingRepository repository;

	private Integer sizeSystem;
	private Integer sizeCustom;
	private List<V3LogModule> moduleList;

	private V3LogSetting() {}

	public static V3LogSetting newInstance() {
		V3LogSetting v3LogSetting = new V3LogSetting();
		v3LogSetting.repository = new LogSettingRepository();
		return v3LogSetting;
	}

	public static V3LogSetting newInstance(LogSetting logSetting) {
		V3LogSetting v3LogSetting = new V3LogSetting();
		v3LogSetting.sizeSystem = logSetting.getSizeSystem();
		v3LogSetting.sizeCustom = logSetting.getSizeCustom();
		v3LogSetting.moduleList = (List<V3LogModule>) V3LogModule.findAllByLogSetting(logSetting);
		return v3LogSetting;
	}

	/**
	 * 
	 * @param page Not available by the persistence framework
	 * @return a List whit all V3LogSetting
	 */
	public List<V3LogSetting> findAll(V3Pagination<V3LogSetting> page) {
		return repository.findAllV3LogSetting(page);
	}

	public Integer getSizeSystem() {
		return sizeSystem;
	}

	public V3LogSetting setSizeSystem(Integer sizeSystem) {
		this.sizeSystem = sizeSystem;
		return this;
	}

	public Integer getSizeCustom() {
		return sizeCustom;
	}

	public V3LogSetting setSizeCustom(Integer sizeCustom) {
		this.sizeCustom = sizeCustom;
		return this;
	}

	public List<V3LogModule> getModuleList() {
		return moduleList;
	}

	public V3LogSetting setModuleList(List<V3LogModule> moduleList) {
		this.moduleList = moduleList;
		return this;
	}

}
