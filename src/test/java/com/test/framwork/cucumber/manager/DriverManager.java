package com.test.framwork.cucumber.manager;

import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;

import com.test.framwork.cucumber.constants.Constants;
import com.test.framwork.cucumber.exception.TestException;
import com.test.framwork.cucumber.utils.ConfigReader;
import com.test.framwork.cucumber.utils.LoggerUtil;

/**
 * DriverManager handles WebDriver lifecycle management Supports cross-browser
 * testing (Chrome, Firefox, Edge, Safari) Provides static methods for driver
 * initialization and cleanup Uses ThreadLocal to support parallel test
 * execution Throws TestException for initialization failures
 * 
 * Browser Selection: - Default: Chrome - Override via: -Dbrowser=firefox (or
 * edge, safari)
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
	 * Initialize WebDriver instance based on browser type Supports both local and
	 * Selenium Grid execution Browser can be specified via system property:
	 * -Dbrowser=browserName Grid usage via: -DseleniumGrid=true and
	 * -DgridURL=http://localhost:4444/wd/hub Supported browsers: chrome, firefox,
	 * edge, safari Default: chrome, local execution
	 * 
	 * @throws TestException if driver initialization fails
	 */
	@SuppressWarnings("null")
	public static void initDriver() {
		try {
			String browserName = System.getProperty("browser", Constants.DEFAULT_BROWSER).toLowerCase();
			boolean useGrid = Boolean.parseBoolean(System.getProperty("usegrid", "false"));
			String gridURL = ConfigReader.getProperty("gridUrl");

			logger.info("Initializing WebDriver for browser: {} | Grid Mode: {} ", browserName.toUpperCase(), useGrid);

			WebDriver webDriver = createDriver(browserName, useGrid, gridURL);

			// Set implicit wait
			webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constants.IMPLICIT_WAIT_SECONDS));
			logger.debug("Implicit wait set to {} seconds", Constants.IMPLICIT_WAIT_SECONDS);

			// Maximize window
			webDriver.manage().window().maximize();
			logger.debug("Browser window maximized");

			// Store in ThreadLocal
			driver.set(webDriver);
			logger.info("✓ WebDriver initialized successfully for browser: {} | Mode: {}", browserName.toUpperCase(),
					useGrid ? "GRID" : "LOCAL");

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
	 * Create WebDriver instance based on browser type and execution mode Supports
	 * both local and Selenium Grid execution
	 * 
	 * @param browserName - browser type (chrome, firefox, edge, safari)
	 * @param useGrid     - if true, creates RemoteWebDriver for Grid; else local
	 *                    WebDriver
	 * @param gridURL     - Selenium Grid hub URL (required when useGrid=true)
	 * @return WebDriver instance
	 * @throws TestException if browser type is not supported or Grid connection
	 *                       fails
	 */
	private static WebDriver createDriver(String browserName, boolean useGrid, String gridURL) {
		logger.debug("Creating WebDriver instance for browser: {} | Grid: {}", browserName, useGrid);

		if (useGrid) {
			return createRemoteDriver(browserName, gridURL);
		} else {
			return createLocalDriver(browserName);
		}
	}

	/**
	 * Create RemoteWebDriver for Selenium Grid execution
	 * 
	 * @param browserName - browser type
	 * @param gridURL     - Selenium Grid hub URL
	 * @return RemoteWebDriver instance
	 * @throws TestException if browser not supported or Grid connection fails
	 */
	private static RemoteWebDriver createRemoteDriver(String browserName, String gridURL) {
		try {
			URL hubURL = new URL(gridURL);
			logger.info("Connecting to Selenium Grid at: {}", gridURL);

			RemoteWebDriver remoteDriver = null;

			switch (browserName.toLowerCase()) {
			case Constants.BROWSER_CHROME:
				logger.info("Launching RemoteWebDriver for Chrome on Grid");
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--disable-gpu", "--window-size=1920,1080");
				chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
				remoteDriver = new RemoteWebDriver(hubURL, chromeOptions);
				break;

			case Constants.BROWSER_FIREFOX:
				logger.info("Launching RemoteWebDriver for Firefox on Grid");
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				firefoxOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
				remoteDriver = new RemoteWebDriver(hubURL, firefoxOptions);
				break;

			case Constants.BROWSER_EDGE:
				logger.info("Launching RemoteWebDriver for Edge on Grid");
				EdgeOptions edgeOptions = new EdgeOptions();
				edgeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
				remoteDriver = new RemoteWebDriver(hubURL, edgeOptions);
				break;

			case Constants.BROWSER_SAFARI:
				logger.info("Launching RemoteWebDriver for Safari on Grid");
				SafariOptions safariOptions = new SafariOptions();
				safariOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
				remoteDriver = new RemoteWebDriver(hubURL, safariOptions);
				break;

			default:
				String errorMsg = String.format(
						"Unsupported browser type on Grid: %s. Supported: chrome, firefox, edge, safari", browserName);
				logger.error(errorMsg);
				throw new TestException(errorMsg);
			}

			logger.info("✓ RemoteWebDriver connected to Grid: {} | Browser: {}", gridURL, browserName.toUpperCase());
			return remoteDriver;

		} catch (Exception e) {
			String errorMsg = String.format("Failed to create RemoteWebDriver for Grid. URL: %s, Browser: %s", gridURL,
					browserName);
			logger.error(errorMsg, e);
			throw new TestException(errorMsg, e);
		}
	}

	/**
	 * Create local WebDriver instance with proper browser options
	 * 
	 * @param browserName - browser type (chrome, firefox, edge, safari)
	 * @return WebDriver instance
	 * @throws TestException if browser type is not supported
	 */
	private static WebDriver createLocalDriver(String browserName) {
		logger.debug("Creating WebDriver instance for browser: {}", browserName);

		switch (browserName.toLowerCase()) {
		case Constants.BROWSER_CHROME:
			logger.info("Launching ChromeDriver (Local)");
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("--disable-gpu", "--window-size=1920,1080");
			chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			return new ChromeDriver(chromeOptions);

		case Constants.BROWSER_FIREFOX:
			logger.info("Launching FirefoxDriver (Local)");
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			return new FirefoxDriver(firefoxOptions);

		case Constants.BROWSER_EDGE:
			logger.info("Launching EdgeDriver (Local)");
			EdgeOptions edgeOptions = new EdgeOptions();
			edgeOptions.addArguments("--disable-gpu", "--window-size=1920,1080");
			edgeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			return new EdgeDriver(edgeOptions);

		case Constants.BROWSER_SAFARI:
			logger.info("Launching SafariDriver (Local)");
			SafariOptions safariOptions = new SafariOptions();
			safariOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			return new SafariDriver(safariOptions);

		default:
			String errorMsg = String.format(
					"Unsupported browser type: %s. Supported browsers: chrome, firefox, edge, safari", browserName);
			logger.error(errorMsg);
			throw new TestException(errorMsg);
		}
	}

	/**
	 * Quit WebDriver and cleanup resources Safely removes driver from ThreadLocal
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
