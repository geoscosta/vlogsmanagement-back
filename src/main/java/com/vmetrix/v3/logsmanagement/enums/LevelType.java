package com.vmetrix.v3.logsmanagement.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
/**
 * 
 * enum to manipulate level types
 *
 */
public enum LevelType {

	OFF(0, "Off - No Logs"), FATAL(1, "Fatal"), ERROR(2, "Error"), INFO(3, "Info"), WARN(4, "Warn"), DEBUG(5, "Debug"),
	TRACE(6, "Trace"), ALL(7, "All - All logs");

	private String description;
	private int order;

	LevelType(int order, String description) {
		this.description = description;
		this.order = order;
	}

	/**
	 * 
	 * @return a list of String with all level Type
	 */
	public static List<String> findAll() {
		List<LevelType> levelTypeList = Arrays.asList(LevelType.values());
		List<String> levelType = new ArrayList<>();

		for (LevelType type : levelTypeList) {
			levelType.add(type.getDescription());
		}
		return levelType;
	}

	public String getDescription() {
		return description;
	}

	public int getOrder() {
		return order;
	}

	/**
	 * Convert from int value to a LevelType Enum
	 * 
	 * @param int value
	 * @return LevelType
	 */
	public static LevelType of(int level) {
		return Stream.of(LevelType.values()).filter(l -> l.order == level).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	public static LevelType ofString(String level) {
		return Stream.of(LevelType.values()).filter(l -> l.description == level).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}