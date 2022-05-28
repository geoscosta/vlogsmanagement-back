package com.vmetrix.v3.logsmanagement.api;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.vmetrix.v3.logsmanagement.enums.LogType;
import com.vmetrix.v3.logsmanagement.repository.LogRecordRepository;

/**
 * 
 * This class is used to show log Trace
 *
 */
public class V3RuntimeLogViewer {

	private LogRecordRepository logRecordRepository;

	private V3LogSetting logSettings;
	private boolean autoScroll = false;
	private boolean pause = !autoScroll;
	private String module;
	private String filter;
	private LogType type;
	private String logTrace = "";
	private List<String> userNameList = new ArrayList<String>();

	private LocalDateTime startLogDateTime = LocalDateTime.now();

	private V3RuntimeLogViewer() {}
	
	/**
	 * 
	 * @param autoScroll used to Freeze the logtrace ( true = Freeze, false = keep
	 *                   listing )
	 * @param pause      ( Stop receiving information )
	 * @param module     ( Choose the Name Module )
	 * @param filter     ( string used to filter the log Trace )
	 * @param type       ( Log Type )
	 */
	public static V3RuntimeLogViewer newInstance(boolean autoScroll, boolean pause, String module, String filter, LogType type,
			List<String> userNameList, LocalDateTime startLogDateTime) {
		V3RuntimeLogViewer v3RuntimeLogViewer = new V3RuntimeLogViewer();
		v3RuntimeLogViewer.logRecordRepository = new LogRecordRepository();
		v3RuntimeLogViewer.autoScroll = autoScroll;
		v3RuntimeLogViewer.pause = pause;
		v3RuntimeLogViewer.module = module;
		v3RuntimeLogViewer.filter = filter;
		v3RuntimeLogViewer.type = type;
		v3RuntimeLogViewer.userNameList = userNameList;
		v3RuntimeLogViewer.startLogDateTime = startLogDateTime;
		return v3RuntimeLogViewer;
	}
	

	/**
	 * Execute a new search using the module, filter and log Type attributes )
	 */
	public void execute() {
		if (!pause)
			this.logTrace = logRecordRepository.findLogTracesByRuntimeViewer(startLogDateTime, this.module, filter, type,
					userNameList);
	}

	/**
	 * enable AutoScroll
	 */
	public void enableAutoScroll() {
		this.autoScroll = true;
		setPause(!this.autoScroll);
	}

	/**
	 * disable AutoScroll
	 */
	public void disableAutoScroll() {
		this.autoScroll = false;
		setPause(!this.autoScroll);
	}

	/**
	 * Start logging
	 */
	public void play() {
		startLogDateTime = LocalDateTime.now();
		setPause(false);
	}

	/**
	 * Stop logging
	 */
	public void stop() {
		setPause(true);
	}

	/**
	 * Create a File to be Downloaded with the 
	 * 
	 * @return PrintWriter
	 */
	public PrintWriter saveFile() {

		PrintWriter printWriter = null;
		try {

			printWriter = new PrintWriter("logTrace.log");

		} catch (FileNotFoundException e) {
			System.out.println("Unable to locate the fileName: " + e.getMessage());
		}
		
		StringBuilder content = new StringBuilder();
		content.append("Log Trace:"+System.lineSeparator());
		content.append(" "+System.lineSeparator());
		content.append(getLogTrace());
		
		
		Objects.requireNonNull(printWriter).println(content.toString());
		printWriter.close();

		return printWriter;

	}

	/**
	 * 
	 * @return List<String>
	 */
	public List<String> userFilterList() {
		return logRecordRepository.getUserNameList();
	}

	/**
	 * clear console
	 */
	public void clearConsole() {
		logTrace = "";
		setStartLogDateTime(LocalDateTime.now());
	}

	public V3LogSetting getLogSettings() {
		return logSettings;
	}

	public void setLogSettings(V3LogSetting logSettings) {
		this.logSettings = logSettings;
	}

	public boolean isAutoScroll() {
		return autoScroll;
	}

	public void setAutoScroll(boolean autoScroll) {
		this.autoScroll = autoScroll;
	}

	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public LogType getType() {
		return type;
	}

	public void setType(LogType type) {
		this.type = type;
	}

	public String getLogTrace() {
		return logTrace;
	}

	public void setLogTrace(String logTrace) {
		this.logTrace = logTrace;
	}

	public List<String> getUserNameList() {
		return userNameList;
	}

	public void setUserNameList(List<String> userNameList) {
		this.userNameList = userNameList;
	}

	public LocalDateTime getStartLogDateTime() {
		return startLogDateTime;
	}

	public void setStartLogDateTime(LocalDateTime startLogDateTime) {
		this.startLogDateTime = startLogDateTime;
	}

}