package com.vmetrix.v3.logsmanangement.api.manager;


import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import com.vmetrix.v3.logsmanagement.api.listener.V3LoggerInitListener;
import com.vmetrix.v3.logsmanagement.model.LogModule;
import com.vmetrix.v3.logsmanagement.repository.LogModuleRepository;


@SpringBootTest
@ContextConfiguration(classes = {V3LoggerInitListener.class})
class V3LogManagerTest {
	private static final LogModuleRepository logModuleRepository = new LogModuleRepository();
	private static final String COM_VMETRIX_V3_CLOUD_VIEW_MAIN = "com.vmetrix.v3.cloud.view.main";
	private static final String ORG_SPRINGFRAMEWORK_BOOT = "org.springframework.boot";
	private static final List<Object> packagesFromDbForTesting = Stream.of(COM_VMETRIX_V3_CLOUD_VIEW_MAIN, ORG_SPRINGFRAMEWORK_BOOT)
		      												             .collect(Collectors.toList());
	
	//*** WARNING! Before running this test please run on your database the file scripts.sql available on src/test/resources folder ***
	@Test
	void shoulSuccessfullyApplyLoggerConfigFromDb() {
		List<LogModule> logModulesFromDb = ((List<LogModule>) logModuleRepository.findAll())
											.stream()
											.filter(p -> packagesFromDbForTesting.contains(p.getTextPackage()))
											.collect(Collectors.toList());
		
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration cfg = ctx.getConfiguration();
		Map<String, LoggerConfig> loggers = cfg.getLoggers();
		
		assertEquals(logModulesFromDb.get(0).getTextPackage(),loggers.get(ORG_SPRINGFRAMEWORK_BOOT).getName());
		assertEquals(logModulesFromDb.get(1).getTextPackage(),loggers.get(COM_VMETRIX_V3_CLOUD_VIEW_MAIN).getName());
	}

}