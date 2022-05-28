package com.vmetrix.v3.logsmanangement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.vmetrix.v3.logsmanagement.api.V3LogModule;
import com.vmetrix.v3.logsmanagement.api.V3LogSetting;
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

@Tag("slow")
@RunWith(JUnitPlatform.class)
public class V3LogSettingTest {

	static V3LogSetting v3LogSetting;
	static V3LogModule v3LogModule;
	static Long currentMaxId;
	
	
	@BeforeAll
	public static void init() {
		v3LogSetting = V3LogsManagementFacadeImpl.singleton().newV3LogSetting();
		v3LogModule = V3LogsManagementFacadeImpl.singleton().newV3Module();
	}
	
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
				
//				V3Logger.log(levelT, LogModule.class, new Long(1), LogType.SYSTEM, "user", "127.0.0.1", "Log Test - "+levelT.getDescription(), null);
				logRecordrepository.save(logRecordTest.createNewLLogRecord(levelType, logModule));

			}
		}

	}
	
	
	 @Test
		public void saveTest() {	
		 
				List<V3LogSetting>  v3LogSettingList  = this.v3LogSetting.findAll(null);
				
					if(!v3LogSettingList.isEmpty()) {
						V3LogSetting logSetting = v3LogSettingList.get(0);
						
						List<V3LogModule> v3LogModuleList = logSetting.getModuleList();
						String moduleName = ""; 
						String levelTypeBeforeChange = "";
						String levelTypeAfterChange = "";
						
								if (!v3LogModuleList.isEmpty()) {
									V3LogModule v3LogModule = v3LogModuleList.get(0);
									moduleName = v3LogModule.getModuleName();
									levelTypeBeforeChange = v3LogModule.getLevelType();
									
									v3LogModule.setLevelType(LevelType.OFF.getDescription());
									 
									 this.v3LogModule.updateV3ModuleList(v3LogModuleList);
								}
								
								V3LogModule v3LogModuleAfterSave = this.v3LogModule.findByNameModule(moduleName);
								
								levelTypeAfterChange =  v3LogModuleAfterSave.getLevelType();
								
								if(!levelTypeBeforeChange.equals("")){
									Assert.assertFalse(levelTypeBeforeChange.equals(levelTypeAfterChange));
								}	
					}
			}
	 
	 @Test
	public void logModuleFindAll() {
			List<V3LogModule> v3LogModuleList = new ArrayList<>();
			
			int sizeBefore = 0;
			
			v3LogModuleList = this.v3LogModule.findAll(null);
			
			Assert.assertTrue(sizeBefore < v3LogModuleList.size());

	}
	 
	 
	 @Test
	public void logSettingsFindAll() {
		 	List<V3LogSetting> l3LogSettingList = new ArrayList<>();
		 
		 	int sizeBefore = 0;
		 	
			List<V3LogSetting> vl3LogSettingList;
			vl3LogSettingList = this.v3LogSetting.findAll(null);

			
			Assert.assertTrue(sizeBefore < vl3LogSettingList.size());

	}
	 
	
}
