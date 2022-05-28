package com.vmetrix.v3.logsmanagement.api.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.vmetrix.v3.logsmanagement.api.manager.V3LogManager;

/**
 * The listener interface for receiving v3LoggerInit events.
 * The class that is interested in processing a v3LoggerInit
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addV3LoggerInitListener<code> method. When
 * the v3LoggerInit event occurs, that object's appropriate
 * method is invoked.
 *
 * @see V3LoggerInitEvent
 */
@Component
public class V3LoggerInitListener implements ApplicationListener<ApplicationReadyEvent> {
	private static final Logger LOGGER = LogManager.getLogger("V3LoggerInitListener");

	/**
	 * On application event.
	 *
	 * @param ApplicationReadyEvent the event
	 */
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		try {
			V3LogManager.applyLoggerConfigFromDB();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

}
