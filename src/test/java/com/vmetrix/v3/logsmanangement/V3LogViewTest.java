package com.vmetrix.v3.logsmanangement;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.vmetrix.v3.logsmanagement.api.V3LogMessage;
import com.vmetrix.v3.logsmanagement.api.V3LogModule;
import com.vmetrix.v3.logsmanagement.api.V3LogViewer;
import com.vmetrix.v3.logsmanagement.api.V3LogsManagementFacadeImpl;
import com.vmetrix.v3.logsmanagement.enums.LevelType;
import com.vmetrix.v3.logsmanagement.enums.LogType;
import com.vmetrix.v3.logsmanagement.model.LogLevelConf;
import com.vmetrix.v3.logsmanagement.model.LogModule;
import com.vmetrix.v3.logsmanagement.model.LogRecord;
import com.vmetrix.v3.logsmanagement.model.LogSetting;
import com.vmetrix.v3.logsmanagement.repository.LogLevelConfRepository;
import com.vmetrix.v3.logsmanagement.repository.LogModuleRepository;
import com.vmetrix.v3.logsmanagement.repository.LogRecordRepository;
import com.vmetrix.v3.logsmanagement.repository.LogSettingRepository;
import com.vmetrix.v3.logsmanangement.runtime.LogLevelConfTest;
import com.vmetrix.v3.logsmanangement.runtime.LogModuleTest;
import com.vmetrix.v3.logsmanangement.runtime.LogRecordTest;
import com.vmetrix.v3.logsmanangement.runtime.LogSettingTest;

@RunWith(JUnitPlatform.class)
public class V3LogViewTest {
	
	static V3LogViewer v3LogViewer;
	static V3LogMessage v3LogMessage;
	static Long currentMaxId;
	
	/**
	 * Init()
	 * 
	 */
	@BeforeAll
	public static void init() {
		v3LogViewer = V3LogsManagementFacadeImpl.singleton().newV3LogViewer();
	}
	
	/**
	 * 
	 * 
	 */
	@AfterAll
	public static void destroy() {
		LogRecordRepository repositoryR = new LogRecordRepository();
		Collection<LogRecord> resultR = repositoryR.findAll();
		resultR.stream().forEach(o -> repositoryR.delete(o));

		LogLevelConfRepository repositoryC = new LogLevelConfRepository();
		Collection<LogLevelConf> resultC = repositoryC.findAll();
		resultC.stream().forEach(o -> repositoryC.delete(o));

		LogSettingRepository repositoryS = new LogSettingRepository();
		Collection<LogSetting> resultS = repositoryS.findAll();
		resultS.stream().forEach(o -> repositoryS.delete(o));

		LogModuleRepository repositoryM = new LogModuleRepository();
		Collection<LogModule> resultM = repositoryM.findAll();
		resultM.stream().forEach(o -> repositoryM.delete(o));
	}

	@BeforeEach
	public void addRecord() {

		createAllData(true, LogType.SYSTEM, "User.Name", "Hibernate", "org.hibernate", 100, 100, 30,
				null, LevelType.ERROR);

	}

	public void createAllData(boolean withLogRecords, LogType logType, String userName, String moduleName,
			String moduleTxPackage, int sizeSys, int sizeCustome, int delDays, String appender, LevelType levelType) {

		LogModuleTest logModuleTest = new LogModuleTest();

		LogRecordRepository logRecordrepository = new LogRecordRepository();

		LogModuleRepository logModuleRepository = new LogModuleRepository();

		LogLevelConfRepository logLevelConfRepository = new LogLevelConfRepository();

		LogSettingRepository settingRepository = new LogSettingRepository();

		//Creating Log Module
		LogModule logModule = logModuleTest.createNewLogModule(0, "Hibernate", "org.hibernate",
				userName);

		logModuleRepository.save(logModule);
		
		LogModule logModule2 = logModuleTest.createNewLogModule(0, "Hibernate SQL", "org.hibernate.SQL",
				userName);

		logModuleRepository.save(logModule2);
		
		LogModule logModule3 = logModuleTest.createNewLogModule(0, "SpringBoot", "org.springframework",
				userName);

		logModuleRepository.save(logModule3);
		
		LogModule logModule4 = logModuleTest.createNewLogModule(0, "V3 Core", "com.vmetrix.v3",
				userName);

		logModuleRepository.save(logModule4);
		
		
		LogModule logModule5 = logModuleTest.createNewLogModule(1, "V3 Custom", "com.vmetrix.v3.custom",
				userName);

		logModuleRepository.save(logModule5);

		//Creating Log Setting
		LogSettingTest logSettingTest = new LogSettingTest();
		LogSetting logSetting = logSettingTest.createNewLogSetting(sizeSys, sizeCustome, delDays, userName);

		settingRepository.save(logSetting);

		//Creating Long Conf
		LogLevelConfTest logLevelConfTest = new LogLevelConfTest();

		LogLevelConf logLevelConf = logLevelConfTest.createNewLogLevelConf(logModule, logSetting, "vcubeAppender,v3DatabaseAppender", LevelType.ERROR,
				userName);

		logLevelConfRepository.save(logLevelConf);
	

		LogLevelConf logLevelConf2 = logLevelConfTest.createNewLogLevelConf(logModule2, logSetting, "vcubeAppender,v3DatabaseAppender", LevelType.ERROR,
				userName);

		logLevelConfRepository.save(logLevelConf2);
		
		LogLevelConf logLevelConf3 = logLevelConfTest.createNewLogLevelConf(logModule3, logSetting, "vcubeAppender,v3DatabaseAppender", LevelType.ERROR,
				userName);

		logLevelConfRepository.save(logLevelConf3);
		
		LogLevelConf logLevelConf4 = logLevelConfTest.createNewLogLevelConf(logModule4, logSetting, "vcubeAppender,v3DatabaseAppender", LevelType.INFO,
				userName);

		logLevelConfRepository.save(logLevelConf4);
		
		
		LogLevelConf logLevelConf5 = logLevelConfTest.createNewLogLevelConf(logModule5, logSetting, "vcubeAppender,v3DatabaseAppender", LevelType.INFO,
				userName);

		logLevelConfRepository.save(logLevelConf5);
		

		if (withLogRecords) {
			//Creating Log Record
			LogRecordTest logRecordTest = new LogRecordTest();

			for (LevelType levelT : Arrays.asList(LevelType.values())) {
				
				logRecordrepository.save(logRecordTest.createNewLLogRecord(levelType, logModule));

			}
		}

	}
	
//	 	@Test
		public void executeTestLogView() {
	
			List<V3LogViewer> v3LogViewerList = this.v3LogViewer.findAllV3LogView(null, null);

			Assert.assertTrue(v3LogViewerList.size() > 0); 
		}
		
//		 @Test
		public void V3LogModuleFindallTest() {
			LogModuleRepository repository = new LogModuleRepository();
			List<V3LogModule> logMList = repository.findAll(null);
			
			Assert.assertTrue(logMList.size() > 0);

		}
		 
		 
		 @Test
		public void reportIssueTest() {
			 
			 V3LogViewer v3LogViewerTest = this.v3LogViewer.findAll(null, null).get(0);
			 
				V3LogMessage v3LogMessage = v3LogViewerTest.getMessageList().get(0);
				 try {
					this.v3LogMessage.reportIssue(v3LogMessage);
				} catch (Exception e) {
					Assert.assertTrue(false);
					e.printStackTrace();
				}
			
			Assert.assertTrue(true);

		}

}
