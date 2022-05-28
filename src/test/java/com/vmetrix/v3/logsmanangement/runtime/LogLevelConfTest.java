package com.vmetrix.v3.logsmanangement.runtime;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.vmetrix.v3.logsmanagement.enums.LevelType;
import com.vmetrix.v3.logsmanagement.model.LogLevelConf;
import com.vmetrix.v3.logsmanagement.model.LogModule;
import com.vmetrix.v3.logsmanagement.model.LogSetting;
import com.vmetrix.v3.logsmanagement.repository.LogLevelConfRepository;
import com.vmetrix.v3.persistence.DatabaseConfigException;

import junit.framework.TestCase;

/**
 * The Class LogLevelConfTest.
 */
	@RunWith(JUnitPlatform.class)
	public class LogLevelConfTest extends TestCase {
		
		@BeforeEach
		protected void setUp(){
			
			
		}
		
		
		public LogLevelConf createNewLogLevelConf(LogModule logModule, LogSetting logSetting, String txAppender, LevelType levelType, String nameUser) {
			
			LogLevelConf loglevelConf = new LogLevelConf();
			
			
			loglevelConf.setAppenders(txAppender);
			loglevelConf.setDateCreate(LocalDateTime.now().plusDays(1));
			loglevelConf.setDateUpdate(LocalDateTime.now().plusDays(1));
			loglevelConf.setNameCreateUser(nameUser);
			loglevelConf.setNameUpdateUser(nameUser);
			loglevelConf.setTpLevel(levelType.ordinal());
			
			loglevelConf.setModule(logModule);
			loglevelConf.setSetting(logSetting);
			
			return loglevelConf;
			
		}
		
//		@Test
		public void testRepository() throws DatabaseConfigException{
			
			LogLevelConfRepository logLevelConfRepository = new LogLevelConfRepository();

//			logLevelConfRepository.save(createNewLogLevelConf());

			
		}

}
