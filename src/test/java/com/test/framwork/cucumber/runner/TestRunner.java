package com.test.framwork.cucumber.runner;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import com.test.framwork.cucumber.utils.CustomReportGenerator;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * TestRunner - Cucumber TestNG Test Runner Executes Cucumber scenarios in
 * parallel (3 threads) Automatically generates custom report after test
 * completion
 * 
 * Execution Flow: 1. Read feature files from: src/test/resources/features 2.
 * Bind step definitions from: com.test.framwork.cucumber (glue path) 3. Execute
 * scenarios in parallel (see pom.xml: <threadCount>3</threadCount>) 4. Generate
 * reports: Extent Report, Cucumber JSON, Custom HTML Report 5. [AUTOMATIC]
 * Custom report generated via TestSuiteListener.onFinish()
 * 
 * Reports Generated: - target/ExtentReports/ExtentReport.html (Extent Report
 * with screenshots) - target/cucumber-reports.html (Cucumber HTML Report) -
 * target/cucumber.json (Cucumber JSON Report) -
 * target/CustomReports/CustomReport.html (Custom HTML with embedded
 * screenshots)
 */
@Listeners(TestRunner.class)
@CucumberOptions(features = "src/test/resources/features", glue = "com.test.framwork.cucumber", plugin = { "pretty",
		"html:target/cucumber-reports.html", "json:target/cucumber.json",
		"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" }, monochrome = true)
public class TestRunner extends AbstractTestNGCucumberTests implements ISuiteListener {

	@Override
	@DataProvider(parallel = true)
	public Object[][] scenarios() {
		return super.scenarios();
	}

	@Override
	public void onFinish(ISuite suite) {
		try {
			CustomReportGenerator.generateCustomReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}