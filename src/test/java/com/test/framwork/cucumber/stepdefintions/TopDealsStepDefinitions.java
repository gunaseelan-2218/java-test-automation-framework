package com.test.framwork.cucumber.stepdefintions;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.Assert;

import com.test.framwork.cucumber.constants.Constants;
import com.test.framwork.cucumber.context.TestContext;
import com.test.framwork.cucumber.exception.TestException;
import com.test.framwork.cucumber.manager.DriverManager;
import com.test.framwork.cucumber.pages.GreenKartLandingPage;
import com.test.framwork.cucumber.pages.GreenKartOffersPage;
import com.test.framwork.cucumber.utils.LoggerUtil;

import io.cucumber.java.en.Then;

public class TopDealsStepDefinitions {

	private static final Logger logger = LoggerUtil.getLogger(TopDealsStepDefinitions.class);
	private TestContext testContext;

	public TopDealsStepDefinitions(TestContext context) {
		this.testContext = context;
		logger.debug("TopDealsStepDefinitions instance created");
	}

	@Then("^User searched for the same (.+) in the offers page to check if products exists$")
	public void user_searched_for_the_same_shortname_in_the_offers_page_to_check_if_products_exists(String productName)
			throws InterruptedException {
		try {
			logger.info("Step: Searching for product in offers page: {}", productName);
			
			GreenKartLandingPage landingpage = testContext.getPageObjectManager().getLandingPage();
			landingpage.clickTopDeals();
			logger.debug("Top Deals link clicked");
			
			GreenKartOffersPage offerspage = testContext.getPageObjectManager().getOffersPage();
			offerspage.searchProduct(productName);
			logger.debug("Product searched in offers page: {}", productName);
			
			String response = offerspage.getProductName();
			logger.debug("Offer page product name extracted: {}", response);
			
			Thread.sleep(Constants.PAGE_LOAD_SLEEP_MS);
			
			// Take screenshot after searching in offers page
			WebDriver driver = DriverManager.getDriver();
			
			testContext.getScenarioContext().set(Constants.SC_OFFER_PAGE_PRODUCT_NAME, response);
			logger.info("✓ Offer page product name stored: {}", response);
		} catch (Exception e) {
			String errorMsg = String.format("Failed to search product in offers page: %s", productName);
			logger.error(errorMsg, e);
			throw new TestException(errorMsg, e);
		}
	}

	@Then("validate the landing page product name matches with offer page product name")
	public void validate_the_landing_page_product_name_matches_with_offer_page_product_name() {
		try {
			logger.info("Step: Validating product names - Landing page vs Offer page");
			
			Object landingPageProductName = testContext.getScenarioContext().get(Constants.SC_LANDING_PAGE_PRODUCT_NAME);
			Object offerPageProductName = testContext.getScenarioContext().get(Constants.SC_OFFER_PAGE_PRODUCT_NAME);
			
			logger.debug("Landing Page Product: {} vs Offer Page Product: {}", landingPageProductName, offerPageProductName);
			
			// Take screenshot before validation
			WebDriver driver = DriverManager.getDriver();
			
			Assert.assertEquals(landingPageProductName, offerPageProductName,
					"Product name mismatch between landing page and offer page");
			logger.info("✓ Product name validation passed - Landing and Offer pages match");
		} catch (AssertionError ae) {
			String errorMsg = "Product name validation failed - Landing page and Offer page mismatch";
			logger.error(errorMsg, ae);
			throw ae;
		} catch (Exception e) {
			String errorMsg = "Failed to validate product names";
			logger.error(errorMsg, e);
			throw new TestException(errorMsg, e);
		}
	}

}
