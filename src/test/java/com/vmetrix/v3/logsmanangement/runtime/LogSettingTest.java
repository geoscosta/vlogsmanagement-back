package com.vmetrix.v3.logsmanangement.runtime;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.vmetrix.v3.logsmanagement.model.LogSetting;
import com.vmetrix.v3.logsmanagement.repository.LogSettingRepository;
import com.vmetrix.v3.persistence.DatabaseConfigException;

import junit.framework.TestCase;

/**
 * 
 * the LogSettingTest Class
 *
 */
	@RunWith(JUnitPlatform.class)
	public class LogSettingTest extends TestCase {
		
		@BeforeEach
		protected void setUp(){
			
			
		}
		
		
		public LogSetting createNewLogSetting(int sizeSys, int sizeCustome, int delDays, String NameUser) {
			
			LogSetting logSetting = new LogSetting();
			
			
			logSetting.setDateCreate(LocalDateTime.now().plusDays(1));
			logSetting.setDateUpdate(LocalDateTime.now().plusDays(1));
			logSetting.setDeldays(delDays);

			logSetting.setNameCreateUser(NameUser);
			logSetting.setNameUpdateUser(NameUser);
			logSetting.setSizeCustom(sizeCustome);
			logSetting.setSizeSystem(sizeSys);
			
			logSetting.setLevelConfList(null);
			
			return logSetting;
			
		}
		
		@Test
		public void testRepository() throws DatabaseConfigException{
			
			LogSettingRepository logSettingRepository = new LogSettingRepository();

			logSettingRepository.save(createNewLogSetting(8, 10, 30, "User.Name"));

			
		}

}
