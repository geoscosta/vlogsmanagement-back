package com.vmetrix.v3.logsmanagement.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 
 * enum to manipulate log types
 *
 */
public enum LogType {

	SYSTEM(0, "System"), CUSTOM(1, "Custom");

	private String description;
	private Integer intValue;

	LogType(int i, String description) {
		this.description = description;
		this.intValue = i;
	}

	public String getDescription() {
		return description;
	}

	public Integer getIntValue() {
		return intValue;
	}

	public List<LogType> findAll() {
		return Arrays.asList(LogType.values());
	}

	/**
	 * @return a list of String with all level Type
	 */
	public static LogType of(int value) {
		return Stream.of(LogType.values()).filter(l -> l.intValue == value).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	/**
	 * @return a list of String with all level Type
	 */
	public static LogType of(String value) {
		return Stream.of(LogType.values()).filter(l -> value.equals(l.getDescription())).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}
}
