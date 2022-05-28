package com.vmetrix.v3.logsmanagement.api.appenders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import com.vmetrix.v3.logsmanagement.model.LogRecord;
import com.vmetrix.v3.logsmanagement.repository.LogRecordRepository;

/**
 * Log4j2 Custom Appender responsible for saving log to database
 * 
 */
@Plugin(name = V3DatabaseAppender.V3_DATABASE_APPENDER, category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class V3DatabaseAppender extends AbstractAppender {
	private static final Logger LOGGER = LogManager.getLogger("V3DatabaseAppender");
	static final String V3_DATABASE_APPENDER = "V3DatabaseAppender";
	private static final LogRecordRepository logRecordRepository = new LogRecordRepository();

	/**
	 * Instantiates a new v 3 database appender.
	 *
	 * @param name the name
	 * @param filter the filter
	 */
	protected V3DatabaseAppender(String name, Filter filter) {
		super(name, filter, null);
	}

	/**
	 * Creates the appender.
	 *
	 * @param name the name
	 * @param filter the filter
	 * @return the v 3 database appender
	 */
	@PluginFactory
	public static V3DatabaseAppender createAppender(@PluginAttribute("name") String name,
			@PluginElement("Filter") Filter filter) {

		return new V3DatabaseAppender(name, filter);
	}

	/**
	 * Append.
	 *
	 * @param event the event
	 */
	@Override
	public void append(LogEvent event) {
			try {//TODO: Ricardo quer que todos os logs sejam salvos, com e sem module
				logRecordRepository.saveAsync(new LogRecord(event));
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
	}
		
	/**
	 * Check if is valid.
	 * FIXME In case of change of argument moduleId necessary change implementation
	 * @param event the event
	 */
	private boolean isValid(LogEvent event) {
		return null != event.getContextData().getValue("moduleId") && null != event.getContextData().getValue("login");
	}

}