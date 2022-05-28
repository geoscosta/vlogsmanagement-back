package com.vmetrix.v3.logsmanangement.runtime;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.vmetrix.v3.logsmanagement.enums.LevelType;
import com.vmetrix.v3.logsmanagement.model.LogModule;
import com.vmetrix.v3.logsmanagement.model.LogRecord;
import com.vmetrix.v3.logsmanagement.repository.LogRecordRepository;
import com.vmetrix.v3.persistence.DatabaseConfigException;

import junit.framework.TestCase;

/**
 * The Class LogRecordTest.
 */
	@RunWith(JUnitPlatform.class)
	public class LogRecordTest extends TestCase {
		
		@BeforeEach
		protected void setUp(){
			
			
		}
		
		public LogRecord createNewLLogRecord(LevelType levelType, LogModule logModule) {
			
			LogRecord logRecord = new LogRecord();
			
			String moduleName = "";
			
			if (logModule != null) {
				moduleName = logModule.getNameModule()+" ";
			}else {
				moduleName = "GenericModule ";
			}
			
			String textMEssageTruncated = moduleName+" "+levelType.getDescription()+" java.lang.Exception at V3.testException1(File.java:36)";
			
			
			logRecord.setDateCreate(LocalDateTime.now());
			logRecord.setModule(logModule);
			logRecord.setDateLog(LocalDateTime.now().plusDays(1));
			logRecord.setDateUpdate(LocalDateTime.now().plusDays(1));
			logRecord.setNameCreateUser("Create Name");
			logRecord.setNameUpdateUser("Update Name");
			logRecord.setNameUser("Name User");
			StringWriter errors = new StringWriter();
			errors.append("java.lang.Exception\r\n"
					+ "    at V3.testException1(File.java:36)\r\n"
					+ "    at V3.main(File.java:15)\r\n"
					+ "Caused by: java.lang.ArrayIndexOutOfBoundsException\r\n"
					+ "    at V3.testException1(File.java:32)");
			
			Blob blob = null;
					
			try {
				blob = new SerialBlob(errors.toString().getBytes(StandardCharsets.UTF_8));
			} catch (SerialException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} 	
					
			logRecord.setTextBlob(blob);
			logRecord.setTextMessage(textMEssageTruncated);
			logRecord.setTextRemoteIP("200.192.0.20");
			logRecord.setTpLevel(levelType.ordinal());
			logRecord.setTpLog(logModule.getTpCore());
			
			return logRecord;
			
		}
		
		
		public void testRepository() throws DatabaseConfigException{
			
			LogRecordRepository repository = new LogRecordRepository();

//			repository.save(createNewLLogRecord(LevelType.INFO));

			
		}

}
