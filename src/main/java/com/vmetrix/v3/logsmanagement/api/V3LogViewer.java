package com.vmetrix.v3.logsmanagement.api;

import java.util.ArrayList;
import java.util.List;

import com.vmetrix.v3.logsmanagement.model.LogRecord;
import com.vmetrix.v3.logsmanagement.repository.LogRecordRepository;
import com.vmetrix.v3.logsmanagement.repository.LogSettingRepository;
import com.vmetrix.v3.pagination.V3Pagination;
/**
 * 
 * class to view log data
 *
 */
public class V3LogViewer {

	private LogRecordRepository logRecordRepository;
	private LogSettingRepository logSettingRepository;

	// Passar logSetting como parametro mas pode ser passado null
	private V3LogSetting logSettings;
	private List<V3LogMessage> messageList;

	private V3LogViewer(){}
	
	public static V3LogViewer newInstance() {
		V3LogViewer v3LogViewer = new V3LogViewer();
		v3LogViewer.logRecordRepository = new LogRecordRepository();
		v3LogViewer.logSettingRepository = new LogSettingRepository();
		return v3LogViewer;
	}
	
	/**
	 * Returns a List<V3LogViewer>, if logSettings != null logSettings is used as
	 * parameter
	 * 
	 * @param logSettings
	 * @param page        not Available by the framework
	 * @return List<V3LogViewer>
	 */
	public List<V3LogViewer> findAll(V3LogSetting logSettings, V3Pagination<V3LogViewer> page) {
		return findAllV3LogView(logSettings, page);
	}
	
	public V3LogSetting getLogSettings() {
		return logSettings;
	}

	public void setLogSettings(V3LogSetting logSettings) {
		this.logSettings = logSettings;
	}

	public List<V3LogMessage> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<V3LogMessage> messageList) {
		this.messageList = messageList;
	}
	
	
	/**
	 * 
	 * @param v3LogSettings
	 * @return List<V3LogViewer>
	 */
	private List<V3LogViewer> createV3LogViewerList(V3LogSetting v3LogSettings) {
		List<V3LogViewer> v3LogViewList = new ArrayList<>();

		if (v3LogSettings != null) {

			for (V3LogModule v3logModule : v3LogSettings.getModuleList()) {

				V3LogViewer v3LogViewer = new V3LogViewer();
				
				v3LogViewer.setMessageList(new ArrayList<>());

				List<LogRecord> logReList = logRecordRepository.findLogRecordByModuleName(v3logModule.getModuleName());

				v3LogViewer.setLogSettings(v3LogSettings);

				for (LogRecord logRecord : logReList) {

					V3LogMessage v3LogMessage = logRecordRepository.createV3LogMessageByLogRecord(logRecord);
					v3LogMessage.setV3LogViewer(v3LogViewer);
					v3LogViewer.getMessageList().add(v3LogMessage);

				}

				v3LogViewList.add(v3LogViewer);
			}

		}

		return v3LogViewList;
	}

	/**
	 * 
	 * @param v3LogSettings if null FindAll()
	 * @param page
	 * @return List<V3LogViewer>
	 */
	public List<V3LogViewer> findAllV3LogView(V3LogSetting v3LogSettings, V3Pagination<V3LogViewer> page) {
		List<V3LogViewer> v3LogViewList = new ArrayList<>();

		// LogSetting available
		if (v3LogSettings != null) {

			v3LogViewList = createV3LogViewerList(v3LogSettings);

			// LogSetting not available
		} else {

			
			List<V3LogSetting> v3LogSettingList = logSettingRepository.findAllV3LogSetting(null);

			for (V3LogSetting v3LogSetting : v3LogSettingList) {
				v3LogViewList.addAll(createV3LogViewerList(v3LogSetting));
			}

		}

		return v3LogViewList;
	}


}
