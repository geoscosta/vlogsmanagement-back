package com.vmetrix.v3.logsmanagement.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * 
 * Entity representation of TB_SY_MODULE
 *
 */

@Entity
@Table(name = "TB_SY_MODULE")
@SequenceGenerator(name = "CD_SEQ_MODULE", sequenceName = "SQ_TBSYMODULE_CDSEQMODULE", allocationSize = 1)
public class LogModule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1364394434790017442L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CD_SEQ_MODULE")
	@Column(name = "CD_SEQ_MODULE")
	private Long id;

	@OneToMany(mappedBy = "module")
	private List<LogLevelConf> levelConfList;

	@OneToMany(mappedBy = "module")
	private List<LogRecord> logRecordList;

	@Column(name = "TP_CORE")
	private Integer tpCore;

	@Column(name = "NM_MODULE", length = 50)
	private String nameModule;

	@Column(name = "TX_PACKAGE", length = 100)
	private String textPackage;

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

	public List<LogLevelConf> getLevelConfList() {
		return levelConfList;
	}

	public void setLevelConfList(List<LogLevelConf> levelConfList) {
		this.levelConfList = levelConfList;
	}

	public String getNameModule() {
		return nameModule;
	}

	public void setNameModule(String nameModule) {
		this.nameModule = nameModule;
	}

	public String getTextPackage() {
		return textPackage;
	}

	public void setTextPackage(String textPackage) {
		this.textPackage = textPackage;
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

	public Integer getTpCore() {
		return tpCore;
	}

	public void setTpCore(Integer tpCore) {
		this.tpCore = tpCore;
	}

	public LocalDateTime getDateUpdate() {
		return dateUpdate;
	}

	public void setDateUpdate(LocalDateTime dateUpdate) {
		this.dateUpdate = dateUpdate;
	}

	public List<LogRecord> getLogRecordList() {
		return logRecordList;
	}

	public void setLogRecordList(List<LogRecord> logRecordList) {
		this.logRecordList = logRecordList;
	}

}
