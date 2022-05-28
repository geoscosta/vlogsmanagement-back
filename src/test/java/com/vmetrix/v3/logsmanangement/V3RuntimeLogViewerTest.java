package com.vmetrix.v3.logsmanangement;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

import com.vmetrix.v3.api.misc.string.StringUtil;
import com.vmetrix.v3.logsmanagement.api.V3LogsManagementFacadeImpl;
import com.vmetrix.v3.logsmanagement.api.V3RuntimeLogViewer;
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
public class V3RuntimeLogViewerTest {
	static V3RuntimeLogViewer v3RuntimeLogViewer;
	static Long currentMaxId;

	@BeforeAll
	public static void init() {
		v3RuntimeLogViewer = V3LogsManagementFacadeImpl.singleton().newV3RuntimeLogViewer(false, false, "", "", null,
				new ArrayList<String>(), LocalDateTime.now().minusDays(1));
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
	public void executeTest() {
		String logTraceBefore = v3RuntimeLogViewer.getLogTrace();
		this.v3RuntimeLogViewer.execute();
		String logTraceAfter = v3RuntimeLogViewer.getLogTrace();
		
		Assert.assertTrue(logTraceBefore.length() < logTraceAfter.length());
	}

	@Test
	public void nameUserLogRecordTest() {
		addRecord();
		LogRecordRepository repository = new LogRecordRepository();
		List<String> userNameList = repository.getUserNameList();

		Assert.assertTrue(userNameList.size() > 0);
	}

//	@Test
	public void filterByUser() {
		addRecord();

		this.v3RuntimeLogViewer.setFilter("");
		this.v3RuntimeLogViewer.setModule("");
		this.v3RuntimeLogViewer.setType(LogType.CUSTOM);

		String beforeLogTrace = this.v3RuntimeLogViewer.getLogTrace();

		List<String> nemUserList = new ArrayList<>();
		nemUserList.add("Name User");
		nemUserList.add("Name User2");

		this.v3RuntimeLogViewer.setUserNameList(nemUserList);
		this.v3RuntimeLogViewer.execute();
		String afterLogTrace = this.v3RuntimeLogViewer.getLogTrace();

		Assert.assertTrue(!beforeLogTrace.equalsIgnoreCase(afterLogTrace));
	}

	@Test
	public void enableAutoScrollTest() {
		String beforeLogTrace = this.v3RuntimeLogViewer.getLogTrace();
		this.v3RuntimeLogViewer.enableAutoScroll();
		addRecord();
		this.v3RuntimeLogViewer.execute();
		String afterLogTrace = this.v3RuntimeLogViewer.getLogTrace();

		Assert.assertFalse(beforeLogTrace.equals(afterLogTrace));

	}

	@Test
	public void disableAutoScrollTest() {
		String beforeLogTrace =this. v3RuntimeLogViewer.getLogTrace();
		this.v3RuntimeLogViewer.disableAutoScroll();
		addRecord();
		this.v3RuntimeLogViewer.execute();
		String afterLogTrace = this.v3RuntimeLogViewer.getLogTrace();

		Assert.assertTrue(beforeLogTrace.equals(afterLogTrace));
	}

	@Test
	public void pauseTest() {
		String beforeLogTrace = this.v3RuntimeLogViewer.getLogTrace();
		this.v3RuntimeLogViewer.stop();
		addRecord();
		this.v3RuntimeLogViewer.execute();
		String afterLogTrace = this.v3RuntimeLogViewer.getLogTrace();

		Assert.assertTrue(beforeLogTrace.equals(afterLogTrace));
	}

	@Test
	public void filter() {
		addRecord();

		String beforeLogTrace = this.v3RuntimeLogViewer.getLogTrace();
		this.v3RuntimeLogViewer.setFilter("Hibernate");
		this.v3RuntimeLogViewer.execute();
		String afterLogTrace = this.v3RuntimeLogViewer.getLogTrace();

		Assert.assertTrue(!beforeLogTrace.equalsIgnoreCase(afterLogTrace));
		this.v3RuntimeLogViewer.setFilter("");
	}

	@Test
	public void clearConsole() {
		this.v3RuntimeLogViewer.setPause(false);
		addRecord();
		this.v3RuntimeLogViewer.execute();

		String beforeLogTrace = this.v3RuntimeLogViewer.getLogTrace();
		Assert.assertTrue(!StringUtil.isEmpty(beforeLogTrace));

		this.v3RuntimeLogViewer.clearConsole();
		String afterLogTrace = this.v3RuntimeLogViewer.getLogTrace();
		Assert.assertTrue(StringUtil.isEmpty(afterLogTrace));
	}
//
//	@Test
	public void playTest() {
		String beforeLogTrace = this.v3RuntimeLogViewer.getLogTrace();
		this.v3RuntimeLogViewer.play();
		addRecord();
		this.v3RuntimeLogViewer.execute();
		String afterLogTrace = this.v3RuntimeLogViewer.getLogTrace();

		Assert.assertFalse(beforeLogTrace.equals(afterLogTrace));
	}

	@Test
	public void saveFileDownload() {

		this.v3RuntimeLogViewer.saveFile();
	}

}