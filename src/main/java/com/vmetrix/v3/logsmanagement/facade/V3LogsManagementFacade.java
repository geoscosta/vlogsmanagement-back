package com.vmetrix.v3.logsmanagement.facade;

import java.time.LocalDateTime;
import java.util.List;

import com.vmetrix.v3.logsmanagement.api.V3LogModule;
import com.vmetrix.v3.logsmanagement.api.V3LogSetting;
import com.vmetrix.v3.logsmanagement.api.V3LogViewer;
import com.vmetrix.v3.logsmanagement.api.V3RuntimeLogViewer;
import com.vmetrix.v3.logsmanagement.enums.LogType;
import com.vmetrix.v3.logsmanagement.model.LogSetting;

public interface V3LogsManagementFacade {

	public V3LogModule newV3Module();
	public V3RuntimeLogViewer newV3RuntimeLogViewer(boolean autoScroll, boolean pause, String module, String filter, LogType type, List<String> userNameList, LocalDateTime startLogDateTime);
	public V3LogViewer newV3LogViewer();
	public V3LogSetting newV3LogSetting(LogSetting logSetting);
	public V3LogSetting newV3LogSetting();


}
