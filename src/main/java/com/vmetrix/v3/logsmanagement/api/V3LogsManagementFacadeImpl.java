package com.vmetrix.v3.logsmanagement.api;

import java.time.LocalDateTime;
import java.util.List;

import com.vmetrix.v3.logsmanagement.enums.LogType;
import com.vmetrix.v3.logsmanagement.facade.V3LogsManagementFacade;
import com.vmetrix.v3.logsmanagement.model.LogSetting;
import com.vmetrix.v3.logsmanagement.repository.LogLevelConfRepository;
import com.vmetrix.v3.logsmanagement.repository.LogModuleRepository;
import com.vmetrix.v3.logsmanagement.repository.LogRecordRepository;
import com.vmetrix.v3.logsmanagement.repository.LogSettingRepository;
/**
 * 
 * instance of log management
 *
 */
public class V3LogsManagementFacadeImpl implements V3LogsManagementFacade {
	
	private static V3LogsManagementFacadeImpl instance = null;

	private V3LogsManagementFacadeImpl() {}
	
	/**
	 * Single instance return method V3LogsManagement
	 * @return a instance of V3LogsManagementFacadeImpl
	 */
	public static V3LogsManagementFacadeImpl singleton() {
		if(instance == null) {
			instance = new V3LogsManagementFacadeImpl();
		}
		return instance;
	}

	public V3LogModule newV3Module(){
		return V3LogModule.newInstance();
	}
	public V3RuntimeLogViewer newV3RuntimeLogViewer(boolean autoScroll, boolean pause, String module, String filter, LogType type, List<String> userNameList, LocalDateTime startLogDateTime) {
		return V3RuntimeLogViewer.newInstance(autoScroll, pause, module, filter, type, userNameList, startLogDateTime);
	}
	public V3LogViewer newV3LogViewer(){
		return V3LogViewer.newInstance();
	}

	public V3LogSetting newV3LogSetting(LogSetting logSetting){
		return V3LogSetting.newInstance(logSetting);
	}
	
	public V3LogSetting newV3LogSetting(){
		return V3LogSetting.newInstance();
	}

	public V3LogMessage newV3LogMessage() {
		return V3LogMessage.newIntance();
	}
}
