package com.test.framwork.cucumber.manager;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;

import com.test.framwork.cucumber.constants.Constants;
import com.test.framwork.cucumber.exception.TestException;
import com.test.framwork.cucumber.utils.LoggerUtil;

/**
 * DriverManager handles WebDriver lifecycle management
 * Supports cross-browser testing (Chrome, Firefox, Edge, Safari)
 * Provides static methods for driver initialization and cleanup
 * Uses ThreadLocal to support parallel test execution
 * Throws TestException for initialization failures
 * 
 * Browser Selection:
 * - Default: Chrome
 * - Override via: -Dbrowser=firefox (or edge, safari)
 */
public class DriverManager {

	private static final Logger logger = LoggerUtil.getLogger(DriverManager.class);
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	/**
	 * Get the WebDriver instance for current thread
	 * 
	 * @return WebDriver instance or null if not initialized
	 */
	public static WebDriver getDriver() {
		return driver.get();
	}

	/**
	 * Initialize WebDriver instance based on browser type
	 * Browser can be specified via system property: -Dbrowser=browserName
	 * Supported browsers: chrome, firefox, edge, safari
	 * Default: chrome
	 * 
	 * @throws TestException if driver initialization fails
	 */
	@SuppressWarnings("null")
	public static void initDriver() {
		try {
			String browserName = System.getProperty("browser", Constants.DEFAULT_BROWSER).toLowerCase();
			logger.info("Initializing WebDriver for browser: {}", browserName.toUpperCase());

			WebDriver webDriver = createDriver(browserName);

			// Set implicit wait
			webDriver.manage().timeouts()
				.implicitlyWait(Duration.ofSeconds(Constants.IMPLICIT_WAIT_SECONDS));
			logger.debug("Implicit wait set to {} seconds", Constants.IMPLICIT_WAIT_SECONDS);

			// Maximize window
			webDriver.manage().window().maximize();
			logger.debug("Browser window maximized");

			// Store in ThreadLocal
			driver.set(webDriver);
			logger.info("✓ WebDriver initialized successfully for browser: {}", browserName.toUpperCase());

		} catch (TestException e) {
			logger.error("Failed to initialize WebDriver", e);
			throw e;
		} catch (Exception e) {
			String errorMsg = "Unexpected error during WebDriver initialization";
			logger.error(errorMsg, e);
			throw new TestException(errorMsg, e);
		}
	}

	/**
	 * Create WebDriver instance based on browser type
	 * 
	 * @param browserName - browser type (chrome, firefox, edge, safari)
	 * @return WebDriver instance
	 * @throws TestException if browser type is not supported
	 */
	private static WebDriver createDriver(String browserName) {
		logger.debug("Creating WebDriver instance for browser: {}", browserName);

		switch (browserName.toLowerCase()) {
		case Constants.BROWSER_CHROME:
			logger.info("Launching ChromeDriver");
			return new ChromeDriver();

		case Constants.BROWSER_FIREFOX:
			logger.info("Launching FirefoxDriver");
			return new FirefoxDriver();

		case Constants.BROWSER_EDGE:
			logger.info("Launching EdgeDriver");
			return new EdgeDriver();

		case Constants.BROWSER_SAFARI:
			logger.info("Launching SafariDriver");
			return new SafariDriver();

		default:
			String errorMsg = String.format(
				"Unsupported browser type: %s. Supported browsers: chrome, firefox, edge, safari",
				browserName);
			logger.error(errorMsg);
			throw new TestException(errorMsg);
		}
	}

	/**
	 * Quit WebDriver and cleanup resources
	 * Safely removes driver from ThreadLocal
	 */
	public static void quitDriver() {
		try {
			WebDriver webDriver = driver.get();
			if (webDriver != null) {
				logger.info("Closing WebDriver");
				webDriver.quit();
				logger.debug("WebDriver quit successfully");
				driver.remove();
				logger.debug("ThreadLocal reference removed");
				logger.info("✓ WebDriver closed successfully");
			} else {
				logger.warn("WebDriver already closed or not initialized");
			}
		} catch (Exception e) {
			logger.error("Error while closing WebDriver", e);
		}
	}

}
