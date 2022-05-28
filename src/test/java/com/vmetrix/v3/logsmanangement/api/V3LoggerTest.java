package com.vmetrix.v3.logsmanangement.api;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.skyscreamer.jsonassert.JSONAssert.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.vmetrix.v3.logsmanagement.api.V3Logger;
import com.vmetrix.v3.logsmanagement.api.listener.V3LoggerInitListener;
import com.vmetrix.v3.logsmanagement.api.manager.V3LogManager;
import com.vmetrix.v3.logsmanagement.enums.LevelType;
import com.vmetrix.v3.logsmanagement.enums.LogType;
import com.vmetrix.v3.logsmanagement.model.LogRecord;
import com.vmetrix.v3.logsmanagement.repository.LogRecordRepository;


/**
 * The Class V3LoggerTest.
 */
@SpringBootTest
@ContextConfiguration(classes = {V3LoggerInitListener.class, V3LogManager.class})
class V3LoggerTest {
	private static final String VCUBE_APPENDER = "vcubeAppender";
	private static final LogRecordRepository logRecordRepository = new LogRecordRepository();

	//*** WARNING! Before running any tests please run on your database the file scripts.sql available on src/test/resources folder ***
	/**
	 * Should successfully save log to file and database.
	 *
	 * @throws Exception the exception
	 */
//	@RepeatedTest(100)
	void shouldSuccessfullySaveLogToFileAndDatabaseWithBlob() throws Exception {
		LogFileInfo logFileInfo = getRollingFileLogInfo();
		long lastLogSize = logFileInfo.getSize();
		String lastLogLastLine = logFileInfo.getLastLineContent();
		LogRecord lastLogRecordFromDb = logRecordRepository.getLast();
		
		V3Logger.log(LevelType.ERROR, V3LoggerTest.class, 3L, LogType.SYSTEM, "geovannyribeiro", "127.0.0.1", "V3LoggerTest log with BLOB", new RuntimeException("Testing 1, 2, 3 ..."));
		
		//Wait for logRecordRepository.saveAsync(LogRecord beanInstance) to complete
		sleep(100);
		
		logFileInfo = getRollingFileLogInfo();
		long currentLogSize = logFileInfo.getSize();
		String currentLogLastLine = logFileInfo.getLastLineContent();
		LogRecord currentLastLogRecordFromDb = logRecordRepository.getLast();
		
		if(lastLogRecordFromDb == null)
			assertTrue(currentLastLogRecordFromDb.getId() > 0);
		else
			assertTrue(currentLastLogRecordFromDb.getId() > lastLogRecordFromDb.getId());
		
		assertTrue(currentLastLogRecordFromDb.getTextBlob().length() > 0);
		assertTrue(currentLogSize > lastLogSize);
		assertNotEquals(lastLogLastLine, currentLogLastLine, JSONCompareMode.LENIENT);
	}
	
	@Test
	void shouldSuccessfullySaveLogToFileAndDatabaseWithoutBlob() throws Exception {
		LogFileInfo logFileInfo = getRollingFileLogInfo();
		long lastLogSize = logFileInfo.getSize();
		String lastLogLastLine = logFileInfo.getLastLineContent();
		LogRecord lastLogRecordFromDb = logRecordRepository.getLast();
		
		V3Logger.log(LevelType.ERROR, V3LoggerTest.class, 1L, LogType.SYSTEM, "geovannyribeiro", "127.0.0.1", "V3LoggerTest log without BLOB", null);
		
		//Wait for logRecordRepository.saveAsync(LogRecord beanInstance) to complete
		sleep(3);
		
		logFileInfo = getRollingFileLogInfo();
		long currentLogSize = logFileInfo.getSize();
		String currentLogLastLine = logFileInfo.getLastLineContent();
		LogRecord currentLastLogRecordFromDb = logRecordRepository.getLast();
		
		assertTrue(currentLastLogRecordFromDb.getId() > lastLogRecordFromDb.getId());
		assertNull(currentLastLogRecordFromDb.getTextBlob());
		assertTrue(currentLogSize > lastLogSize);
		assertNotEquals(lastLogLastLine, currentLogLastLine, JSONCompareMode.LENIENT);
	}

	private void sleep(int value) throws InterruptedException {
		TimeUnit.MILLISECONDS.sleep(value);
	}
	
	private static LogFileInfo getRollingFileLogInfo() throws IOException {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(true);
		Configuration config = ctx.getConfiguration();
		RollingFileAppender rollingFileAppender = (RollingFileAppender) config.getAppender(VCUBE_APPENDER);
		File logFile = new File(rollingFileAppender.getFileName());
		return new LogFileInfo(logFile.length(), tailFile(logFile.toPath(), 1).get(0));
	}
	
	private static List<String> tailFile(final Path source, final int noOfLines) throws IOException  {
		try (Stream<String> stream = Files.lines(source)) {
			FileBuffer fileBuffer = new FileBuffer(noOfLines);
			stream.forEach(line -> fileBuffer.collect(line));
			return fileBuffer.getLines();
		}
	}
	
	private static final class FileBuffer {
		private int offset = 0;
		private final int noOfLines;
		private final String[] lines;

		public FileBuffer(int noOfLines) {
			this.noOfLines = noOfLines;
			this.lines = new String[noOfLines];
		}

		public void collect(String line) {
			lines[offset++ % noOfLines] = line;
		}

		public List<String> getLines() {
			return IntStream.range(offset < noOfLines ? 0 : offset - noOfLines, offset)
						    .mapToObj(idx -> lines[idx % noOfLines])
						    .collect(Collectors.toList());
		}
	}
	
	private static final class LogFileInfo {
		private long size;
		private String lastLineContent;
		
		public LogFileInfo(long size, String lastLineContent) {
			this.size = size;
			this.lastLineContent = lastLineContent;
		}
		
		public long getSize() {
			return size;
		}		
		
		public String getLastLineContent() {
			return lastLineContent;
		}
	}

}