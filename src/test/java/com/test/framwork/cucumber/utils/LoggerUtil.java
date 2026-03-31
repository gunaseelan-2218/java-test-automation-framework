package com.test.framwork.cucumber.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LoggerUtil is a utility class for logging throughout the framework
 * Provides convenient logging methods for different levels
 * 
 * Usage: LoggerUtil.getLogger(ClassName.class).info("message");
 */
public class LoggerUtil {

	/**
	 * Get logger instance for a specific class
	 * @param clazz - the class for which logger is required
	 * @return - Logger instance
	 */
	public static Logger getLogger(Class<?> clazz) {
		return LoggerFactory.getLogger(clazz);
	}

	/**
	 * Log info message with parameters
	 * 
	 * @param logger - logger instance
	 * @param message - message to log
	 * @param args - message parameters
	 */
	public static void info(Logger logger, String message, Object... args) {
		logger.info(message, args);
	}

	/**
	 * Log debug message with parameters
	 * 
	 * @param logger - logger instance
	 * @param message - message to log
	 * @param args - message parameters
	 */
	public static void debug(Logger logger, String message, Object... args) {
		logger.debug(message, args);
	}

	/**
	 * Log warning message with parameters
	 * 
	 * @param logger - logger instance
	 * @param message - message to log
	 * @param args - message parameters
	 */
	public static void warn(Logger logger, String message, Object... args) {
		logger.warn(message, args);
	}

	/**
	 * Log error message with exception
	 * 
	 * @param logger - logger instance
	 * @param message - message to log
	 * @param exception - exception to log
	 */
	public static void error(Logger logger, String message, Exception exception) {
		logger.error(message, exception);
	}

	/**
	 * Log error message with parameters and exception
	 * 
	 * @param logger - logger instance
	 * @param message - message to log
	 * @param exception - exception to log
	 */
	public static void error(Logger logger, String message, Throwable exception) {
		logger.error(message, exception);
	}

}
