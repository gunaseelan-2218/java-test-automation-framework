package com.test.framwork.cucumber.stepdefintions;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.Assert;

import com.test.framwork.cucumber.constants.Constants;
import com.test.framwork.cucumber.context.TestContext;
import com.test.framwork.cucumber.exception.TestException;
import com.test.framwork.cucumber.manager.DriverManager;
import com.test.framwork.cucumber.pages.GreenKartCheckoutPage;
import com.test.framwork.cucumber.utils.LoggerUtil;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddtoCartStepDefintions {

	private static final Logger logger = LoggerUtil.getLogger(AddtoCartStepDefintions.class);
	private TestContext testContext;

	public AddtoCartStepDefintions(TestContext context) {
		this.testContext = context;
		logger.debug("AddtoCartStepDefintions instance created");
	}

	@When("proceeds to checkout")
	public void proceeds_to_checkout() throws InterruptedException {
		try {
			logger.info("Step: Proceeding to checkout");
			GreenKartCheckoutPage checkoutpage = testContext.getPageObjectManager().getCheckoutPage();
			checkoutpage.clickProceedToCheckout();
			logger.debug("Proceed to checkout button clicked");
			Thread.sleep(Constants.CART_LOAD_SLEEP_MS);
			// Take screenshot after navigating to checkout
			WebDriver driver = DriverManager.getDriver();
			logger.info("✓ Checkout page loaded successfully");
		} catch (Exception e) {
			String errorMsg = "Failed to proceed to checkout";
			logger.error(errorMsg, e);
			throw new TestException(errorMsg, e);
		}
	}

	@Then("the product name in the checkout page should match the landing page")
	public void the_product_name_in_the_checkout_page_should_match_the_landing_page() {
		try {
			logger.info("Step: Validating product name in checkout page");
			GreenKartCheckoutPage checkoutpage = testContext.getPageObjectManager().getCheckoutPage();
			String response = checkoutpage.getProductName();
			logger.debug("Checkout page product name: {}", response);
			testContext.getScenarioContext().set("cartpageProductName", response);
			Object landingPageProductName = testContext.getScenarioContext().get("landingpageProductName");
			logger.debug("Comparing - Landing Page: {} vs Checkout Page: {}", landingPageProductName, response);
			// Take screenshot before assertion
			WebDriver driver = DriverManager.getDriver();
			Assert.assertEquals(landingPageProductName, response,
					"Product name mismatch between landing page and checkout page");
			logger.info("✓ Product name validation passed");
		} catch (AssertionError ae) {
			String errorMsg = "Product name validation failed";
			logger.error(errorMsg, ae);
			throw ae;
		} catch (Exception e) {
			String errorMsg = "Failed to validate product name in checkout page";
			logger.error(errorMsg, e);
			throw new TestException(errorMsg, e);
		}
	}

	@Then("the selected quantity should match the checkout quantity")
	public void the_selected_quantity_should_match_the_checkout_quantity() {
		try {
			logger.info("Step: Validating product quantity in checkout page");
			GreenKartCheckoutPage checkoutpage = testContext.getPageObjectManager().getCheckoutPage();
			String response = checkoutpage.getProductQuantity();
			Integer cartQuantity = Integer.parseInt(response);
			logger.debug("Checkout page quantity: {}", cartQuantity);
			testContext.getScenarioContext().set(Constants.SC_CART_PAGE_QUANTITY, cartQuantity);
			Integer landingPageQuantity = (Integer) testContext.getScenarioContext()
					.get(Constants.SC_LANDING_PAGE_PRODUCT_COUNT);
			logger.debug("Comparing - Landing Page Quantity: {} vs Checkout Page Quantity: {}", landingPageQuantity,
					cartQuantity);
			// Take screenshot before assertion
			WebDriver driver = DriverManager.getDriver();
			Assert.assertEquals(landingPageQuantity, cartQuantity,
					"Quantity mismatch between landing page and checkout page");
			logger.info("✓ Quantity validation passed");
		} catch (AssertionError ae) {
			String errorMsg = "Quantity validation failed";
			logger.error(errorMsg, ae);
			throw ae;
		} catch (Exception e) {
			String errorMsg = "Failed to validate quantity in checkout page";
			logger.error(errorMsg, e);
			throw new TestException(errorMsg, e);
		}
	}

}
