package com.vmetrix.v3.logsmanagement.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.vmetrix.v3.api.misc.mail.Mail;
import com.vmetrix.v3.api.misc.mail.V3MailGeneralException;
import com.vmetrix.v3.logsmanagement.enums.LevelType;
import com.vmetrix.v3.logsmanagement.enums.LogType;

/**
 * Class used to show Log Message data
 * 
 *
 */
public class V3LogMessage {

	private LocalDateTime dateTime;
	private String remoteAdress;// IP
	private String moduleName;
	private LogType type;
	private LevelType level;
	private String message;
	private String user;
	private String logTrace;
	private V3LogViewer v3LogViewer;

	private V3LogMessage() {}
	
	public static V3LogMessage newIntance() {
		V3LogMessage v3LogMessage = new V3LogMessage();
		return v3LogMessage;
	}

	// get's and set's:
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
	/**
	 * This Method Report a issue by sendding a email
	 * 
	 * @throws Exception
	 */
	public static void reportIssue(V3LogMessage message) throws V3MailGeneralException {
		
		boolean validMessage = message != null;
		
		if (!validMessage) 
			throw new V3MailGeneralException("ERROR: Can´t send e-mail!");
		
		if(validMessage){
			
			String subject = "Logs Management - Issue Report ";
			
			String to = "gustavo.duarte@vmetrix.com";//FIXME: Change to the correct email
			
			Boolean auth = true;
			Boolean tls = true;
			String from = "testsmtpvmetrix@gmail.com";//FIXME: Change to the correct email
			String host = "smtp.gmail.com";
			String port = "587";
			String user = "testsmtpvmetrix@gmail.com";//FIXME: Change to the correct email
			String password = "senhateste";
			
			StringBuilder content = new StringBuilder();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-M-yyyy hh:mm:ss");

			content.append("Date/Time: " + message.getDateTime().format(formatter) + "     Type: "
					+ message.getType().getDescription() + "     Level: " + message.getLevel().getDescription()
					+ System.lineSeparator());
			content.append("Module :" + message.getModuleName() + "     Remote Adress: " + message.getRemoteAdress()
					+ System.lineSeparator());
			content.append(" " + System.lineSeparator());
			content.append(" " + System.lineSeparator());
			content.append("Log Trace:" + System.lineSeparator());
			content.append(" " + System.lineSeparator());
			content.append(message.logTrace);

			Mail.sendEmail(subject + " - " + message.getModuleName() + " - " + message.getLevel().getDescription(),
					content.toString(), to, auth, tls, from, host, port, user, password);

		}

	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getRemoteAdress() {
		return remoteAdress;
	}

	public void setRemoteAdress(String remoteAdress) {
		this.remoteAdress = remoteAdress;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public LogType getType() {
		return type;
	}

	public void setType(LogType type) {
		this.type = type;
	}

	public LevelType getLevel() {
		return level;
	}

	public void setLevel(LevelType level) {
		this.level = level;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public String getLogTrace() {
		return logTrace;
	}

	public void setLogTrace(String logTrace) {
		this.logTrace = logTrace;
	}

	public V3LogViewer getV3LogViewer() {
		return v3LogViewer;
	}

	public void setV3LogViewer(V3LogViewer v3LogViewer) {
		this.v3LogViewer = v3LogViewer;
	}

}
