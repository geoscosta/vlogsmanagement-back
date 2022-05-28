package com.vmetrix.v3.logsmanagement.repository;

import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import com.vmetrix.v3.logsmanagement.api.V3LogMessage;
import com.vmetrix.v3.logsmanagement.api.V3LogModule;
import com.vmetrix.v3.logsmanagement.api.V3LogSetting;
import com.vmetrix.v3.logsmanagement.api.V3LogViewer;
import com.vmetrix.v3.logsmanagement.api.V3LogsManagementFacadeImpl;
import com.vmetrix.v3.logsmanagement.enums.LevelType;
import com.vmetrix.v3.logsmanagement.enums.LogType;
import com.vmetrix.v3.logsmanagement.model.LogRecord;
import com.vmetrix.v3.pagination.V3Pagination;
import com.vmetrix.v3.persistence.database.DatabaseBO;
import com.vmetrix.v3.persistence.repository.V3SimpleDataRepository;
/**
 * 
 * repository of log record
 *
 */
public class LogRecordRepository extends V3SimpleDataRepository<LogRecord, Long> {
	
//	V3LogSetting v3LogSetting = V3LogsManagementFacadeImpl.singleton().newV3LogSetting();
//	V3LogViewer v3LogViewer = V3LogsManagementFacadeImpl.singleton().newV3LogViewer();		

	public LogRecordRepository() {
		super(LogRecord.class);
	}

	/**
	 * find log trace by runtime viewer
	 * @param startLogDateTime
	 * @param module
	 * @param filter
	 * @param type
	 * @param userNameList
	 * @return String
	 */
	public String findLogTracesByRuntimeViewer(LocalDateTime startLogDateTime, String module, String filter,
			LogType type, List<String> userNameList) {
		StringBuilder result = new StringBuilder();

		DatabaseBO dbBO = DatabaseBO.getMainInstance();
		Session hibernateSession = dbBO.getConnection(true);
		String objectQuery = "SELECT l FROM LogRecord as l " + "where 1=1 ";
		if (!userNameList.isEmpty()) {
			int i = 0;
			objectQuery += " and ( ";
			for (String userName : userNameList) {
				if (i > 0)
					objectQuery += " or ";
				objectQuery += " l.nameUser like :userName" + i + " ";
				i++;
			}
			objectQuery += " ) ";

		}
		if (type != null)
			objectQuery += "and l.tpLog = :type ";
		if (!module.equalsIgnoreCase("")) {
			objectQuery += "and l.module.nameModule = :module ";
		}
		objectQuery += "and l.dateLog >= :startLogDateTime ";
		if (!filter.equalsIgnoreCase("")) {
			objectQuery += "and l.textMessage like :filter ";
		}

		objectQuery += "order by l.dateLog ";

		Query query = hibernateSession.createQuery(objectQuery);

		if (!userNameList.isEmpty()) {
			int i = 0;
			for (String userName : userNameList) {
				query.setParameter("userName" + i, userName);
				i++;
			}

		}
		if (type != null)
			query.setParameter("type", type.getIntValue());
		if (!module.equalsIgnoreCase("")) {
			query.setParameter("module", module);
		}
		if (!filter.equalsIgnoreCase("")) {
			query.setParameter("filter", "%" + filter + "%");
		}
		query.setParameter("startLogDateTime", startLogDateTime);

		List<LogRecord> resultList = query.getResultList();

		for (LogRecord logRecord : resultList) {
			
			if(logRecord.getTextBlob() != null) {
				 Blob blob = logRecord.getTextBlob();
				 byte[] logTrace;
				try {
					logTrace = blob.getBytes(1, (int) blob.length());
					result.append(new String(logTrace));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			result.append(System.lineSeparator());
		}

		return result.toString();

	}

	/**
	 * find log record by module name
	 * @param moduleName
	 * @return List<LogRecord>
	 */
	public List<LogRecord> findLogRecordByModuleName(String moduleName) {

		DatabaseBO dbBO = DatabaseBO.getMainInstance();
		Session hibernateSession = dbBO.getConnection(true);
		String objectQuery = "SELECT l FROM LogRecord as l " + "where l.module.nameModule = :module  "
				+ "order by l.dateLog";

		Query query = hibernateSession.createQuery(objectQuery);

		query.setParameter("module", moduleName);

		List<LogRecord> result = query.getResultList();

		return result;

	}

	/**
	 * find log record by module name and type level
	 * @param moduleName
	 * @param levelType
	 * @return List<LogRecord>
	 */
	public List<LogRecord> findLogRecordByModuleNameAndTypeLevel(String moduleName, LevelType levelType) {
	
		
		DatabaseBO dbBO = DatabaseBO.getMainInstance();
		Session hibernateSession = dbBO.getConnection(true);
		String objectQuery = "SELECT l FROM LogRecord as l "
				+ "where l.module.nameModule = :module  "
				+ "where l.tpLevel = :levelType  "
				+ "order by l.dateLog";
		
		Query query = hibernateSession.createQuery(objectQuery);

		query.setParameter("module", moduleName);
		query.setParameter("levelType", levelType);
		
		List<LogRecord> result = query.getResultList();
		
		return result;

		
	}

	/**
	 * Gets the last record.
	 *
	 * @return the last LogRecord
	 */
	public LogRecord getLast() {
		DatabaseBO dbBO = DatabaseBO.getMainInstance();
		Session hibernateSession = dbBO.getConnection(true);
		String objectQuery = "SELECT l FROM LogRecord as l where l.id = (select max(lr.id) from LogRecord as lr)";
		Query query = hibernateSession.createQuery(objectQuery);

		List<LogRecord> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		}

		return resultList.get(0);
	}

	/**
	 * Save async LogRecord.
	 *
	 * @param beanInstance the bean instance
	 */
	public void saveAsync(LogRecord beanInstance) {
		Runnable task = () -> {
			super.save(beanInstance);
		};
		new Thread(task).start();
	}

	/**
	 * create a log message by log record
	 * @param logRecord
	 * @return V3LogMessage
	 */
	public V3LogMessage createV3LogMessageByLogRecord(LogRecord logRecord) {

		V3LogMessage v3LogMessage = V3LogsManagementFacadeImpl.singleton().newV3LogMessage();

		v3LogMessage.setDateTime(logRecord.getDateLog());
		if (logRecord.getTpLevel() != null) {
			v3LogMessage.setLevel(LevelType.of(logRecord.getTpLevel()));
		}
		if ( logRecord.getTextMessage() != null) {
			v3LogMessage.setMessage(logRecord.getTextMessage());
		}
		if (logRecord.getModule() != null) {
			v3LogMessage.setModuleName(logRecord.getModule().getNameModule());
		}
		if (logRecord.getTextRemoteIP() != null) {
			v3LogMessage.setRemoteAdress(logRecord.getTextRemoteIP());
		}
		if (logRecord.getTpLog() != null) {
			v3LogMessage.setType(LogType.of(logRecord.getTpLog()));
		}
		if (logRecord.getNameUser() != null) {
			v3LogMessage.setUser(logRecord.getNameUser());
		}
		if (logRecord.getTextBlob() != null) {
			Blob blob = logRecord.getTextBlob();
			byte[] logTrace;
			try {
				logTrace = blob.getBytes(1, (int) blob.length());
				v3LogMessage.setLogTrace(new String(logTrace));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			v3LogMessage.setLogTrace("");
		}

		return v3LogMessage;
	}

	/**
	 * get user list
	 * @return List<String>
	 */
	public List<String> getUserNameList() {
		DatabaseBO dbBO = DatabaseBO.getMainInstance();
		Session hibernateSession = dbBO.getConnection(true);
		String objectQuery = "SELECT DISTINCT l.nameUser FROM LogRecord as l";
		Query query = hibernateSession.createQuery(objectQuery);

		List<String> resultList = query.getResultList();

		return resultList;
	}

}