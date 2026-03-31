package com.test.framwork.cucumber.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;

import com.test.framwork.cucumber.constants.Constants;
import com.test.framwork.cucumber.exception.TestException;

/**
 * ConfigReader class manages environment-specific configuration properties
 * Loads properties based on the execution environment (QA, STAGING, PROD, etc.)
 * Uses SLF4J for logging
 * Throws TestException for errors
 */
public class ConfigReader {

	private static final Logger logger = LoggerUtil.getLogger(ConfigReader.class);
	private static Properties properties;
	private static String environment;

	/**
	 * Static method to load properties based on environment
	 * Should be called before any property access from Hooks.setUp()
	 * 
	 * @throws TestException if properties file not found or invalid
	 */
	public static void loadProperties() {
		try {
			logger.debug("Attempting to load configuration properties");
			
			environment = System.getProperty("env");
			if (environment == null) {
				environment = Constants.DEFAULT_ENVIRONMENT;
				logger.debug("No environment specified, defaulting to: {}", environment);
			}
			
			String fileName = environment.toLowerCase() + Constants.CONFIG_FILE_EXTENSION;
			String path = Constants.CONFIG_RESOURCE_PATH + fileName;

			logger.info("Loading configuration file: {}", path);
			
			FileInputStream fis = new FileInputStream(path);
			properties = new Properties();
			properties.load(fis);
			
			logger.info("✓ Configuration loaded successfully for environment: {}", environment.toUpperCase());
			logger.debug("Total properties loaded: {}", properties.size());
			
		} catch (IOException e) {
			String errorMsg = String.format("Configuration file not found for environment: %s", environment);
			logger.error(errorMsg, e);
			throw new TestException(errorMsg, e);
		} catch (Exception e) {
			String errorMsg = "Unexpected error while loading configuration";
			logger.error(errorMsg, e);
			throw new TestException(errorMsg, e);
		}
	}

	/**
	 * Gets the base URL from configuration
	 * 
	 * @return baseUrl property value
	 * @throws TestException if properties not loaded or baseUrl not found
	 */
	public static String getBaseUrl() {
		if (properties == null) {
			String errorMsg = "Properties not loaded. Call loadProperties() first from Hooks.setUp()";
			logger.error(errorMsg);
			throw new TestException(errorMsg);
		}
		
		String baseUrl = properties.getProperty("baseUrl");
		if (baseUrl == null || baseUrl.isEmpty()) {
			String errorMsg = "baseUrl property not found in configuration";
			logger.error(errorMsg);
			throw new TestException(errorMsg);
		}
		
		logger.debug("Retrieved baseUrl: {}", baseUrl);
		return baseUrl;
	}

	/**
	 * Gets the current execution environment
	 * 
	 * @return environment name (QA, STAGING, PROD, etc.)
	 */
	public static String getEnvironment() {
		return environment;
	}

	/**
	 * Get any property from configuration
	 * 
	 * @param key - property key
	 * @return property value or null if not found
	 */
	public static String getProperty(String key) {
		if (properties == null) {
			String errorMsg = "Properties not loaded. Call loadProperties() first";
			logger.error(errorMsg);
			throw new TestException(errorMsg);
		}
		
		String value = properties.getProperty(key);
		if (value == null) {
			logger.warn("Property not found: {}", key);
		}
		return value;
	}

}
