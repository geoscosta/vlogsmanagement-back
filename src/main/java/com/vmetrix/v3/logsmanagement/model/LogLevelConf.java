package com.vmetrix.v3.logsmanagement.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * Entity representation of TB_LG_LEVELCONF
 *
 */

@Entity
@Table(name = "TB_LG_LEVELCONF")
@SequenceGenerator(name = "CD_SEQ_LEVELCONF", sequenceName = "SQ_TBLGLEVELCONF_CDSEQLEVELCON", allocationSize = 1)
public class LogLevelConf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6536296546468836726L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CD_SEQ_LEVELCONF")
	@Column(name = "CD_SEQ_LEVELCONF")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "CD_SEQ_LOGSET")
	private LogSetting setting;

	@ManyToOne
	@JoinColumn(name = "CD_SEQ_MODULE")
	private LogModule module;

	@Column(name = "TX_APPENDER", length = 50)
	private String appenders;

	@Column(name = "TP_LEVEL")
	private Integer tpLevel;

	@Column(name = "NM_CREATE_USER", length = 15)
	private String nameCreateUser;

	@Column(name = "DT_CREATE")
	private LocalDateTime dateCreate;

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

	public LogSetting getSetting() {
		return setting;
	}

	public void setSetting(LogSetting setting) {
		this.setting = setting;
	}

	public LogModule getModule() {
		return module;
	}

	public void setModule(LogModule module) {
		this.module = module;
	}

	public String getAppenders() {
		return appenders;
	}

	public void setAppenders(String appender) {
		this.appenders = appender;
	}

	public Integer getTpLevel() {
		return tpLevel;
	}

	public void setTpLevel(Integer tpLevel) {
		this.tpLevel = tpLevel;
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
