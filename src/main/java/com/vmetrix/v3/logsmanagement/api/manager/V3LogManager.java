package com.vmetrix.v3.logsmanagement.api.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.vmetrix.v3.logsmanagement.enums.LevelType;
import com.vmetrix.v3.logsmanagement.enums.LogType;
import com.vmetrix.v3.logsmanagement.model.LogLevelConf;
import com.vmetrix.v3.logsmanagement.model.LogModule;
import com.vmetrix.v3.logsmanagement.repository.LogModuleRepository;

/**
 * The Class V3LogManager.
 */
public final class V3LogManager {
	private static final Logger LOGGER = LogManager.getLogger("vcubeAppender,v3DatabaseAppender");
	private static final String APPENDERS_SEPARATOR = ",";
	private static final LogModuleRepository logModuleRepository = new LogModuleRepository();

	/**
	 * Instantiates a new v 3 log manager.
	 */
	private V3LogManager() {
		
	}
	
	/**
	 * Apply logger config from DB.
	 */
	public static void applyLoggerConfigFromDB() {
		try {

//			VMetrixLogger.log(VMetrixLogType.DEBUG, VCubeLoggerInitListener.class, "Getting Logger Configuration From Data Base...", null);
			
			//ModuleBO adminModule = ModuleBO.loadModule(ModulesCodes.MODULE_VADMIN_CODE);

			//VMetrixLogger.log(VMetrixLogType.DEBUG, VCubeLoggerInitListener.class, "Applying Logger Configuration From Data Base...", null);

			applyLoggerConfig(/*adminModule*/);

			//VMetrixLogger.log(VMetrixLogType.DEBUG, VCubeLoggerInitListener.class, "Logger Configuration applied From Data Base.", null);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Apply logger config.
	 */
	private static void applyLoggerConfig(/*ModuleBO adminModule*/) {
		Collection<LogModule> logModules = logModuleRepository.findAll();
		Configurator.reconfigure();

		// Loggers configuration
		for (LogModule logModule : logModules) {
			createOrUpdateLoggerConfig(logModule);
		}
		
//		try {
//			for (VCubeLoggerBO cat : VCubeLoggerBO.getList()) {
//					createOrUpdateLoggerConfig(cat);
//			}
//		} catch (VCubeLoggerBOException e) {
//			VMetrixLogger.log(VMetrixLogType.ERROR, VCubeLoggerInitListener.class, "Erro updating logger config", e);
//		}
		
		// Appender file configuration
//		String vcubeLogfile = adminModule.getCfgValue(VCUBE_LOG_FILE);
//		String veltServerLogfile = adminModule.getCfgValue(VELT_SERVER_LOG_FILE);
//
//		Map<String, RollingFileAppender> mapApend = VCubeLogManager.getCurrentVCubeFileAppenders();
//
//		if (vcubeLogfile != null && mapApend.containsKey(VCubeSystemAppender.VCUBEAPPENDER.getName())) {
//			RollingFileAppender rollingFileAppender = mapApend.get(VCubeSystemAppender.VCUBEAPPENDER.getName());
//			File file = new File(vcubeLogfile);
//			Path pathFile = file.toPath();
//
//			if (!Files.isDirectory(pathFile) && FileUtil.getExtension(pathFile.toString()).compareTo("log") == 0) {
//				updateRollingFileAppenderFilePath(pathFile.toString(), rollingFileAppender);
//			}
//		}
//
//		if (veltServerLogfile != null && mapApend.containsKey(VCubeSystemAppender.VELTSERVERAPPENDER.getName())) {
//			RollingFileAppender rollingFileAppender = mapApend.get(VCubeSystemAppender.VELTSERVERAPPENDER.getName());
//			File file = new File(veltServerLogfile);
//			Path pathFile = file.toPath();
//			
//			if (!Files.isDirectory(pathFile) && FileUtil.getExtension(pathFile.toString()).compareTo("log") == 0) {
//				 updateRollingFileAppenderFilePath(pathFile.toString(), rollingFileAppender);
//			}
//		}
	}
	
	/**
	 * Creates the or update logger config.
	 *
	 * @param logModule the log module
	 */
	private static void createOrUpdateLoggerConfig(LogModule logModule) {
		Logger logger = getV3Logger(logModule.getTextPackage());
		
		for (LogLevelConf logLevelConf : logModule.getLevelConfList()) {//FIXME: Confirm If use LevelType or LogType LogConf para pegar o typeLevel
			
			//If logger already exist only update the level
			if (logger != null && logger.getName().equals(logModule.getTextPackage())) {
				Configurator.setLevel(logger.getName(), Level.toLevel(LevelType.of(logLevelConf.getTpLevel()).name()));
			}
		}
		//If logger does'nt exist yet then create a logger config and add to log4j context
		if (logger == null || !logger.getName().equals(logModule.getTextPackage())) {
			createLoggerConfig(logModule);
		}
		
	}
	
	/**
	 * Creates the logger config.
	 *
	 * @param logModule the log module
	 */
	private static void createLoggerConfig(LogModule logModule) {
		LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
		Configuration configuration = loggerContext.getConfiguration();
		Map<String, Appender> allAppenders = configuration.getAppenders();
		List<LogLevelConf> levelConfList = logModule.getLevelConfList();
		
		List<Appender> appenders = null;
		List<AppenderRef> appenderRefList = null;
		Level level = null;
		LoggerConfig loggerConfig  = null;
		String[] appendersFromLevelConf = null;
		
		for (LogLevelConf levelConf : levelConfList) {
			appenders = new ArrayList<>();
			appenderRefList = new ArrayList<>();
			appendersFromLevelConf = levelConf.getAppenders().split(APPENDERS_SEPARATOR);
			
			for (String currentAppender : appendersFromLevelConf){
				appenderRefList.add(AppenderRef.createAppenderRef(currentAppender, null, null));
				appenders.add(allAppenders.get(currentAppender));
			}
			
			level = Level.getLevel(LevelType.of(levelConf.getTpLevel()).name());
			
			// Create new loggerConfig
			AppenderRef[] refs = new AppenderRef[appenderRefList.size()];
			appenderRefList.toArray(refs);
			loggerConfig = LoggerConfig.createLogger(false, level, logModule.getTextPackage(), "true", refs, null, configuration, null);
			
			// Add appenders to the loggerConfig
			for (Appender app : appenders) {
				loggerConfig.addAppender(app, level, null);
			}
			
			loggerConfig.start();
			configuration.addLogger(logModule.getTextPackage() , loggerConfig);
			
			loggerContext.updateLoggers();
		}
		
	}
	
	/**
	 * Gets the v 3 logger.
	 *
	 * @param loggerName the logger name
	 * @return the v 3 logger
	 */
	private static Logger getV3Logger(String loggerName) {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration cfg = ctx.getConfiguration();
		LoggerConfig loggerConfig = cfg.getLoggerConfig(loggerName);

		if (loggerConfig != null && loggerConfig.getName().equals(loggerName)) {
			return LogManager.getLogger(loggerConfig.getName());
		}

		return null;
	}

}