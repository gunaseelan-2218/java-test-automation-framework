package com.test.framwork.cucumber.hooks;

import java.io.File;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import com.test.framwork.cucumber.context.TestContext;
import com.test.framwork.cucumber.manager.DriverManager;
import com.test.framwork.cucumber.utils.ConfigReader;
import com.test.framwork.cucumber.utils.LoggerUtil;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Hooks class for Cucumber lifecycle management Manages test setup and teardown
 * operations with proper logging
 * 
 * Execution Order: 1. Context Creation (DI Container) - TestContext +
 * ScenarioContext created 2. @Before Hook - Runs before any steps 3. Background
 * Steps (optional) 4. Scenario Steps - Given, When, Then 5. @After Hook - Runs
 * after all steps
 */
public class Hooks {

	private static final Logger logger = LoggerUtil.getLogger(Hooks.class);
	private TestContext testContext;

	/**
	 * Constructor - receives TestContext via dependency injection Called during
	 * context creation (Step 1)
	 * 
	 * @param testContext - injected by Cucumber's PicoContainer
	 */
	public Hooks(TestContext testContext) {
		this.testContext = testContext;
		logger.debug("Hooks instance created with TestContext");
	}

	/**
	 * @Before hook - runs after context creation but before any steps Executes once
	 *         per scenario
	 * 
	 *         Execution: Step 2
	 */
	@Before
	public void setUp() {
		try {
			// Thread information for parallel execution tracking
			String threadName = Thread.currentThread().getName();
			long threadId = Thread.currentThread().getId();

			logger.info("═══════════════════════════════════════════════════════════════");
			logger.info("SCENARIO START [Thread: {}] [ID: {}]", threadName, threadId);
			logger.info("═══════════════════════════════════════════════════════════════");

			logger.info("[{}] Loading configuration properties...", threadName);
			ConfigReader.loadProperties();

			logger.info("[{}] Initializing WebDriver...", threadName);
			DriverManager.initDriver();

			logger.info("[{}] TestContext and ScenarioContext ready", threadName);
			logger.debug("[{}] ScenarioContext size: {}", threadName, testContext.getScenarioContext().size());
		} catch (Exception e) {
			String threadName = Thread.currentThread().getName();
			logger.error("[{}] Setup failed - Scenario execution aborted", threadName, e);
			throw e;
		}
	}

	/**
	 * @After hook - runs after all steps complete Executes regardless of scenario
	 *        pass/fail Takes screenshot on failure for debugging
	 * 
	 *        Execution: Step 5
	 */
	@After
	public void tearDown(Scenario scenario) {
		try {
			String threadName = Thread.currentThread().getName();
			long threadId = Thread.currentThread().getId();

			logger.info("═══════════════════════════════════════════════════════════════");
			logger.info("SCENARIO TEARDOWN [Thread: {}] [ID: {}]", threadName, threadId);
			logger.info("═══════════════════════════════════════════════════════════════");

			logger.debug("[{}] Cleaning up scenario context...", threadName);
			testContext.getScenarioContext().clear();
			logger.debug("[{}] ScenarioContext cleared - size: {}", threadName,
					testContext.getScenarioContext().size());

			logger.info("[{}] Closing WebDriver...", threadName);
			DriverManager.quitDriver();

			logger.info("[{}] SCENARIO COMPLETED SUCCESSFULLY", threadName);
		} catch (Exception e) {
			String threadName = Thread.currentThread().getName();
			logger.error("[{}] Teardown error: {}", threadName, e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * @AfterStep hook - runs after each step completes Captures screenshot for
	 *            every step (pass/fail) Embeds screenshot in Extent Report Saves
	 *            screenshot to file system for custom report
	 * 
	 *            Execution: After each Given, When, Then step
	 */
	@AfterStep
	public void afterStep(Scenario scenario) {
		try {
			WebDriver driver = DriverManager.getDriver();
			byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

			// Embed in Cucumber JSON (for Extent Report)
			scenario.attach(screenshot, "image/png", "step");

			// Save to file system (for custom report generation)
			String screenshotFileName = saveScreenshotToFile(screenshot, scenario);

			logger.debug("Screenshot captured for step - File: {}", screenshotFileName);
		} catch (Exception e) {
			logger.warn("Error capturing screenshot after step: {}", e.getMessage());
			// Don't throw exception - continue test execution
		}
	}

	/**
	 * Save screenshot to target/screenshots directory Creates timestamped file
	 * names for easy tracking
	 */
	private String saveScreenshotToFile(byte[] screenshot, Scenario scenario) throws Exception {
		File screenshotDir = new File("target/screenshots");
		screenshotDir.mkdirs();
		// Generate unique filename with timestamp
		String timestamp = java.time.LocalDateTime.now()
				.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
		String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_");
		String fileName = String.format("%s_%s_%d.png", scenarioName, timestamp, Thread.currentThread().getId());

		File screenshotFile = new File(screenshotDir, fileName);
		java.nio.file.Files.write(screenshotFile.toPath(), screenshot);

		logger.debug("Screenshot saved to file system: {}", screenshotFile.getAbsolutePath());
		return fileName;
	}

}