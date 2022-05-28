package com.vmetrix.v3.logsmanagement.model;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import com.vmetrix.v3.logsmanagement.repository.LogModuleRepository;

/**
 * 
 * Entity representation of TB_LG_RECORD
 *
 */

@Entity
@Table(name = "TB_LG_RECORD")
@SequenceGenerator(name = "CD_SEQ_LOGREC", sequenceName = "SQ_TBLGRECORD_CDSEQLOGREC", allocationSize = 1)
public class LogRecord implements Serializable {

	private static final long serialVersionUID = 8819060775071119510L;
	private static final String MODULE_ID = "moduleId";
	private static final String IP = "ip";
	private static final String LOGIN = "login";
	private static final String TP_LOG = "tpLog";
	private static final String TP_LEVEL = "tpLevel";

	public LogRecord() {

	}

	/**
	 * Instantiates a new log record.
	 *
	 * @param logEvent the log event
	 * @throws SerialException the serial exception
	 * @throws SQLException    the SQL exception
	 */
	public LogRecord(LogEvent logEvent) throws SerialException, SQLException {
		Objects.requireNonNull(logEvent);
		ReadOnlyStringMap contextData = logEvent.getContextData();
		String strLogin = Objects.isNull(contextData.getValue(LOGIN)) ? "System" : contextData.getValue(LOGIN);
		Integer intTpLog = Objects.isNull(contextData.getValue(TP_LOG)) ? 0 : Integer.parseInt(contextData.getValue(TP_LOG)) ;
		Integer intTpLevel = Objects.isNull(contextData.getValue(TP_LEVEL)) ? null : Integer.parseInt(contextData.getValue(TP_LEVEL)) ;
		Long moduleId = Long.parseLong(Objects.isNull(contextData.getValue(MODULE_ID)) ? "0" : (String) contextData.getValue(MODULE_ID)) ;
		LogModule dummyLogModule = new LogModule();
		dummyLogModule.setId(moduleId);
		
		LogModuleRepository logModuleRepository = new LogModuleRepository();
		Optional<LogModule> opLogModule = logModuleRepository.findById("id", moduleId);
		String moduleName = "";
		
		if (opLogModule.isPresent()) {
			dummyLogModule = opLogModule.get();
			moduleName = dummyLogModule.getNameModule()+" ";
		}else {
			moduleName = "GenericModule ";
		}
		
		String textMEssageTruncated = moduleName+logEvent.getMessage().getFormattedMessage();

		this.textMessage = textMEssageTruncated.substring(0, 99);
		this.textRemoteIP = contextData.getValue(IP);
		this.module = dummyLogModule;
		this.tpLog = intTpLog;
		this.tpLevel = intTpLevel;
		this.nameUser =  strLogin;
		this.nameCreateUser =  strLogin;
		this.nameUpdateUser = strLogin;
		this.dateCreate = LocalDateTime.now();
		this.dateUpdate = LocalDateTime.now();
		this.textBlob = getTextBlobFrom(logEvent);
		this.dateLog = Instant.ofEpochMilli(logEvent.getInstant().getEpochMillisecond()).atZone(ZoneId.systemDefault())
				.toLocalDateTime();
	}

	private SerialBlob getTextBlobFrom(LogEvent logEvent) throws SerialException, SQLException {
		if (null == logEvent.getThrown()) {
			return null;
		}
		StringWriter errors = new StringWriter();
		logEvent.getThrown().printStackTrace(new PrintWriter(errors));
		return new SerialBlob(errors.toString().getBytes(StandardCharsets.UTF_8));
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CD_SEQ_LOGREC")
	@Column(name = "CD_SEQ_LOGREC")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CD_SEQ_MODULE")
	private LogModule module;

	@Column(name = "DT_LOG")
	private LocalDateTime dateLog;

	@Column(name = "TP_LOG")
	private Integer tpLog;

	@Column(name = "NM_USER", length = 15)
	private String nameUser;

	@Column(name = "TX_REMOTEIP", length = 15)
	private String textRemoteIP;

	@Column(name = "TP_LEVEL")
	private Integer tpLevel;

	@Column(name = "TX_MESSAGE", length = 100)
	private String textMessage;

	@Column(name = "TX_LOG")
	private Blob textBlob;

	// FIXME Column size too short
	@Column(name = "NM_CREATE_USER", length = 15)
	private String nameCreateUser;

	@Column(name = "DT_CREATE")
	private LocalDateTime dateCreate;

	// FIXME Column size too short
	@Column(name = "NM_UPDATE_USER", length = 15)
	private String nameUpdateUser;

	@Column(name = "DT_UPDATE")
	private LocalDateTime dateUpdate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LogModule getModule() {
		return module;
	}

	public void setModule(LogModule module) {
		this.module = module;
	}

	public LocalDateTime getDateLog() {
		return dateLog;
	}

	public void setDateLog(LocalDateTime dateLog) {
		this.dateLog = dateLog;
	}

	public Integer getTpLog() {
		return tpLog;
	}

	public void setTpLog(Integer tpLog) {
		this.tpLog = tpLog;
	}

	public String getNameUser() {
		return nameUser;
	}

	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}

	public String getTextRemoteIP() {
		return textRemoteIP;
	}

	public void setTextRemoteIP(String textRemoteIP) {
		this.textRemoteIP = textRemoteIP;
	}

	public Integer getTpLevel() {
		return tpLevel;
	}

	public void setTpLevel(Integer tpLevel) {
		this.tpLevel = tpLevel;
	}

	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	public Blob getTextBlob() {
		return textBlob;
	}

	public void setTextBlob(Blob textBlob) {
		this.textBlob = textBlob;
	}

	public String getNameCreateUser() {
		return nameCreateUser;
	}

	public void setNameCreateUser(String nameCreateUser) {
		this.nameCreateUser = nameCreateUser;
	}

	public LocalDateTime getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(LocalDateTime dateCreate) {
		this.dateCreate = dateCreate;
	}

	public String getNameUpdateUser() {
		return nameUpdateUser;
	}

	public void setNameUpdateUser(String nameUpdateUser) {
		this.nameUpdateUser = nameUpdateUser;
	}

	public LocalDateTime getDateUpdate() {
		return dateUpdate;
	}

	public void setDateUpdate(LocalDateTime dateUpdate) {
		this.dateUpdate = dateUpdate;
	}

}