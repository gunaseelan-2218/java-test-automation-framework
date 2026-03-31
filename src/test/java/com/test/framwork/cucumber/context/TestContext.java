package com.test.framwork.cucumber.context;

import org.openqa.selenium.WebDriver;

import com.test.framwork.cucumber.manager.DriverManager;
import com.test.framwork.cucumber.manager.PageObjectManager;
import com.test.framwork.cucumber.utils.ConfigReader;

/**
 * TestContext class serves as a centralized context for sharing data 
 * and objects across step definitions within a scenario
 * 
 * Initialization Flow:
 * 1. Hooks.setUp() is called -> ConfigReader.loadProperties() -> DriverManager.initDriver()
 * 2. TestContext is created -> ScenarioContext initialized
 * 3. PageObjectManager is lazily initialized when first accessed
 */
public class TestContext {

	private ScenarioContext scenarioContext;
	private PageObjectManager pageObjectManager;

	/**
	 * Constructor initializes scenario-specific context
	 * Note: ConfigReader is initialized in Hooks.setUp() before this
	 * Note: DriverManager is initialized in Hooks.setUp() before this
	 */
	public TestContext() {
		System.out.println("Test context first");
		this.scenarioContext = new ScenarioContext();
	}

	/**
	 * Gets the WebDriver instance managed by DriverManager
	 * Driver is initialized in Hooks.setUp()
	 * 
	 * @return WebDriver instance
	 */
	public WebDriver getDriver() {
		return DriverManager.getDriver();
	}

	/**
	 * Gets the PageObjectManager instance with lazy initialization
	 * Lazily instantiated on first access to ensure driver is available
	 * 
	 * @return PageObjectManager instance
	 */
	public PageObjectManager getPageObjectManager() {
		if (pageObjectManager == null) {
			pageObjectManager = new PageObjectManager(getDriver());
		}
		return pageObjectManager;
	}

	/**
	 * Gets the ConfigReader instance for accessing configuration properties
	 * ConfigReader is initialized in Hooks.setUp() as a static singleton
	 * 
	 * @return ConfigReader class (static methods)
	 */
	public ConfigReader getConfigReader() {
		return ConfigReader.class.cast(ConfigReader.class);
	}

	/**
	 * Alternative method to get base URL directly from ConfigReader
	 * 
	 * @return baseUrl property value
	 */
	public String getBaseUrl() {
		return ConfigReader.getBaseUrl();
	}

	/**
	 * Gets the ScenarioContext for storing scenario-specific data
	 * Used to pass data between steps within a scenario
	 * 
	 * @return ScenarioContext instance
	 */
	public ScenarioContext getScenarioContext() {
		return scenarioContext;
	}

}
