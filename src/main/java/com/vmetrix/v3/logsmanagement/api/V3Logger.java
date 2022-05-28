package com.vmetrix.v3.logsmanagement.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import com.vmetrix.v3.logsmanagement.enums.LevelType;
import com.vmetrix.v3.logsmanagement.enums.LogType;

/**
 * The Class V3Logger.
 */
public final class V3Logger {
	private static Map<Class, Logger> loggers = (Map) new HashMap<Class, Logger>();

	/**
	 * Instantiates a new v 3 logger.
	 */
	private V3Logger() {

	}

	/**
	 * Log.
	 *
	 * @param logType     the log type
	 * @param loggerClass the logger class
	 * @param moduleId    the module id
	 * @param login       the login
	 * @param ip          the ip
	 * @param msg         the msg
	 * @param error       the error
	 */
	// FIXME Decide about moduleId argument
	public static void log(LevelType levelType, Class loggerClass, Long moduleId, LogType logType, String login, String ip, String msg,
			Throwable error) {
		ThreadContext.put("tpLevel", ""+levelType.getOrder());
		ThreadContext.put("tpLog", ""+logType.getIntValue());
		ThreadContext.put("login", login);
		ThreadContext.put("ip", ip);
		ThreadContext.put("moduleId", moduleId.toString());

		Logger logger = getLogger(loggerClass);
		switch (levelType) {
		case OFF:
			return;
		case FATAL:
			logger.fatal(msg, error);
			return;
		case ERROR:
			logger.error(msg, error);
			return;
		case INFO:
			logger.info(msg, error);
			return;
		case DEBUG:
			logger.debug(msg, error);
			return;
		case WARN:
			logger.warn(msg, error);
			return;
		case TRACE:
		case ALL:
			logger.trace(msg, error);
			return;
		}

		ThreadContext.clearAll();
	}

	/**
	 * Gets the logger.
	 *
	 * @param loggerClass the logger class
	 * @return the logger
	 */
	private static Logger getLogger(Class loggerClass) {
		if (!loggers.containsKey(loggerClass)) {
			Logger logger = LogManager.getLogger(loggerClass.getCanonicalName());
			loggers.put(loggerClass, logger);
		}
		return loggers.get(loggerClass);
	}

	/**
	 * Gets the log level.
	 *
	 * @param loggerClass the logger class
	 * @return the log level
	 */
	public static Level getLogLevel(Class loggerClass) {
		Logger loggerInstance = getLogger(loggerClass);
		return loggerInstance.getLevel();
	}

	/**
	 * Checks if is in debug mode.
	 *
	 * @param loggerClass the logger class
	 * @return true, if is in debug mode
	 */
	public static boolean isInDebugMode(Class loggerClass) {
		Logger loggerInstance = getLogger(loggerClass);
		return loggerInstance.getLevel().equals(Level.DEBUG);
	}

//	/**
//	 * Off.
//	 *
//	 * @param logType     the log type
//	 * @param loggerClass the logger class
//	 * @param moduleId    the module id
//	 * @param login       the login
//	 * @param ip          the ip
//	 * @param msg         the msg
//	 * @param error       the error
//	 */
//	public static void off(LevelType logType, Class loggerClass, Long moduleId, String login, String ip, String msg,
//			Throwable error) {
//		log(LevelType.OFF, loggerClass, moduleId, login, ip, msg, error);
//	}
//
//	/**
//	 * Fatal.
//	 *
//	 * @param logType     the log type
//	 * @param loggerClass the logger class
//	 * @param moduleId    the module id
//	 * @param login       the login
//	 * @param ip          the ip
//	 * @param msg         the msg
//	 * @param error       the error
//	 */
//	public static void fatal(LevelType logType, Class loggerClass, Long moduleId, String login, String ip, String msg,
//			Throwable error) {
//		log(LevelType.FATAL, loggerClass, moduleId, login, ip, msg, error);
//	}
//
//	/**
//	 * Error.
//	 *
//	 * @param logType     the log type
//	 * @param loggerClass the logger class
//	 * @param moduleId    the module id
//	 * @param login       the login
//	 * @param ip          the ip
//	 * @param msg         the msg
//	 * @param error       the error
//	 */
//	public static void error(LevelType logType, Class loggerClass, Long moduleId, String login, String ip, String msg,
//			Throwable error) {
//		log(LevelType.ERROR, loggerClass, moduleId, login, ip, msg, error);
//	}
//
//	/**
//	 * Info.
//	 *
//	 * @param logType     the log type
//	 * @param loggerClass the logger class
//	 * @param moduleId    the module id
//	 * @param login       the login
//	 * @param ip          the ip
//	 * @param msg         the msg
//	 * @param error       the error
//	 */
//	public static void info(LevelType logType, Class<?> loggerClass, Long moduleId, String login, String ip, String msg,
//			Throwable error) {
//		log(LevelType.INFO, loggerClass, moduleId, login, ip, msg, error);
//	}
//
//	/**
//	 * Debug.
//	 *
//	 * @param logType     the log type
//	 * @param loggerClass the logger class
//	 * @param moduleId    the module id
//	 * @param login       the login
//	 * @param ip          the ip
//	 * @param msg         the msg
//	 * @param error       the error
//	 */
//	public static void debug(LevelType logType, Class loggerClass, Long moduleId, String login, String ip, String msg,
//			Throwable error) {
//		log(LevelType.DEBUG, loggerClass, moduleId, login, ip, msg, error);
//	}
//
//	/**
//	 * Warn.
//	 *
//	 * @param logType     the log type
//	 * @param loggerClass the logger class
//	 * @param moduleId    the module id
//	 * @param login       the login
//	 * @param ip          the ip
//	 * @param msg         the msg
//	 * @param error       the error
//	 */
//	public static void warn(LevelType logType, Class loggerClass, Long moduleId, String login, String ip, String msg,
//			Throwable error) {
//		log(LevelType.WARN, loggerClass, moduleId, login, ip, msg, error);
//	}
//
//	/**
//	 * Trace.
//	 *
//	 * @param logType     the log type
//	 * @param loggerClass the logger class
//	 * @param moduleId    the module id
//	 * @param login       the login
//	 * @param ip          the ip
//	 * @param msg         the msg
//	 * @param error       the error
//	 */
//	public static void trace(LevelType logType, Class loggerClass, Long moduleId, String login, String ip, String msg,
//			Throwable error) {
//		log(LevelType.TRACE, loggerClass, moduleId, login, ip, msg, error);
//	}
//
//	/**
//	 * All.
//	 *
//	 * @param logType     the log type
//	 * @param loggerClass the logger class
//	 * @param moduleId    the module id
//	 * @param login       the login
//	 * @param ip          the ip
//	 * @param msg         the msg
//	 * @param error       the error
//	 */
//	public static void all(LevelType logType, Class loggerClass, Long moduleId, String login, String ip, String msg,
//			Throwable error) {
//		log(LevelType.ALL, loggerClass, moduleId, login, ip, msg, error);
//	}
//	
}