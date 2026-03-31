package com.test.framwork.cucumber.constants;

/**
 * Constants class for framework configuration
 * Contains browser names, timeout values, and other constants
 */
public class Constants {

	// ========== BROWSER TYPES ==========
	/**
	 * Browser types supported for cross-browser testing
	 */
	public static final String BROWSER_CHROME = "chrome";
	public static final String BROWSER_FIREFOX = "firefox";
	public static final String BROWSER_EDGE = "edge";
	public static final String BROWSER_SAFARI = "safari";

	// ========== DEFAULT VALUES ==========
	/**
	 * Default browser if not specified
	 */
	public static final String DEFAULT_BROWSER = BROWSER_CHROME;

	/**
	 * Default implicit wait timeout in seconds
	 */
	public static final int IMPLICIT_WAIT_SECONDS = 15;

	/**
	 * Default explicit wait timeout in seconds
	 */
	public static final int EXPLICIT_WAIT_SECONDS = 10;

	// ========== PATHS ==========
	/**
	 * Configuration files path
	 */
	public static final String CONFIG_RESOURCE_PATH = "src/test/resources/";
	public static final String CONFIG_FILE_EXTENSION = "_config.properties";

	/**
	 * Log files path
	 */
	public static final String LOG_FILE_PATH = "target/cucumber-framework.log";

	/**
	 * Report files path
	 */
	public static final String REPORT_PATH = "target/";

	// ========== ENVIRONMENT DEFAULTS ==========
	/**
	 * Default environment if not specified via system property
	 */
	public static final String DEFAULT_ENVIRONMENT = "QA";

	// ========== SCENARIO CONTEXT KEYS ==========
	/**
	 * Keys used in ScenarioContext to store test data
	 */
	public static final String SC_LANDING_PAGE_PRODUCT_NAME = "landingpageProductName";
	public static final String SC_LANDING_PAGE_PRODUCT_COUNT = "landingpageProductCount";
	public static final String SC_CART_PAGE_PRODUCT_NAME = "cartpageProductName";
	public static final String SC_CART_PAGE_QUANTITY = "cartpageQuantity";
	public static final String SC_OFFER_PAGE_PRODUCT_NAME = "offerPageProductName";

	// ========== WAITS AND TIMEOUTS ==========
	/**
	 * Sleep time for page transitions (milliseconds)
	 */
	public static final long PAGE_LOAD_SLEEP_MS = 2000;
	public static final long CART_LOAD_SLEEP_MS = 5000;

	/**
	 * Private constructor to prevent instantiation
	 */
	private Constants() {
		throw new AssertionError("Constants class should not be instantiated");
	}

}
