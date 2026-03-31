package com.test.framwork.cucumber.stepdefintions;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import com.test.framwork.cucumber.constants.Constants;
import com.test.framwork.cucumber.context.TestContext;
import com.test.framwork.cucumber.exception.TestException;
import com.test.framwork.cucumber.manager.DriverManager;
import com.test.framwork.cucumber.pages.GreenKartLandingPage;
import com.test.framwork.cucumber.utils.ConfigReader;
import com.test.framwork.cucumber.utils.LoggerUtil;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class SearchProductStepDefinitions {

	private static final Logger logger = LoggerUtil.getLogger(SearchProductStepDefinitions.class);
	private TestContext testContext;

	public SearchProductStepDefinitions(TestContext context) {
		this.testContext = context;
		logger.debug("SearchProductStepDefinitions instance created");
	}

	@Given("The User is on Greenkart landing page")
	public void the_user_is_on_greenkart_landing_page() {
		try {
			logger.info("Step: User navigating to Greenkart landing page");
			String url = ConfigReader.getBaseUrl();
			logger.debug("Base URL retrieved: {}", url);
			
			testContext.getDriver().get(url);
			
			// Take screenshot after navigating to landing page
			WebDriver driver = DriverManager.getDriver();
		
			
			logger.info("✓ User navigated to: {}", url);
		} catch (Exception e) {
			String errorMsg = "Failed to navigate to Greenkart landing page";
			logger.error(errorMsg, e);
			throw new TestException(errorMsg, e);
		}
	}

	@When("^User searched with shortname (.+) and extracted the actual name of the product$")
	public void user_searched_with_shortname_and_extracted_the_actual_name_of_the_product(String productName)
			throws InterruptedException {
		try {
			logger.info("Step: Searching for product: {}", productName);
			
			GreenKartLandingPage landingpage = testContext.getPageObjectManager().getLandingPage();
			landingpage.searchProduct(productName);
			logger.debug("Product search completed for: {}", productName);
			
			Thread.sleep(Constants.PAGE_LOAD_SLEEP_MS);
			String response = landingpage.getProductName();
			logger.debug("Product name extracted: {}", response);
			
			// Take screenshot after product search
			WebDriver driver = DriverManager.getDriver();
			
			testContext.getScenarioContext().set(Constants.SC_LANDING_PAGE_PRODUCT_NAME, response);
			logger.info("✓ Landing page product name stored: {}", response);
		} catch (Exception e) {
			String errorMsg = String.format("Failed to search and extract product name for: %s", productName);
			logger.error(errorMsg, e);
			throw new TestException(errorMsg, e);
		}
	}
	
	@When("increases the product quantity to {int}")
	public void increases_the_product_quantity_to(Integer quantity) {
		try {
			logger.info("Step: Increasing product quantity to: {}", quantity);
			
			GreenKartLandingPage landingpage = testContext.getPageObjectManager().getLandingPage();
			landingpage.addProductCount(quantity);
			logger.debug("Quantity incremented to: {}", quantity);
			
			// Take screenshot after quantity update
			WebDriver driver = DriverManager.getDriver();
			
			testContext.getScenarioContext().set(Constants.SC_LANDING_PAGE_PRODUCT_COUNT, quantity+1);
			logger.info("✓ Product quantity stored: {}", quantity);
		} catch (Exception e) {
			String errorMsg = String.format("Failed to increase product quantity to: %d", quantity);
			logger.error(errorMsg, e);
			throw new TestException(errorMsg, e);
		}
	}
	
	@When("adds the product to the cart")
	public void adds_the_product_to_the_cart() throws InterruptedException {
		try {
			logger.info("Step: Adding product to cart");
			
			GreenKartLandingPage landingpage = testContext.getPageObjectManager().getLandingPage();
			landingpage.addProductToCart();
			logger.debug("Add to cart button clicked");
			
			Thread.sleep(Constants.CART_LOAD_SLEEP_MS);
			
			// Take screenshot after adding product to cart
			WebDriver driver = DriverManager.getDriver();
			
			logger.info("✓ Product added to cart successfully");
		} catch (Exception e) {
			String errorMsg = "Failed to add product to cart";
			logger.error(errorMsg, e);
			throw new TestException(errorMsg, e);
		}
	}

}
