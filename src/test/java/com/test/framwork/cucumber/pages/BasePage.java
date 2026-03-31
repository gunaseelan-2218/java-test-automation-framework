package com.test.framwork.cucumber.pages;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

	private static final Logger LOGGER = LogManager.getLogger(BasePage.class);
	private static final Duration EXPLICIT_WAIT = Duration.ofSeconds(10);

	private WebDriver driver;
	private WebDriverWait wait;
	private Actions actions;

	public BasePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, EXPLICIT_WAIT);
		this.actions = new Actions(driver);
		LOGGER.debug("BasePage initialized for driver: {}", driver);
	}

	// ==================== WINDOW & FRAME MANAGEMENT ====================

	/**
	 * Switch to newly opened child window (useful for single child window)
	 */
	public void switchToChildWindow() {
		try {
			String parentWindow = driver.getWindowHandle();
			Set<String> allWindows = driver.getWindowHandles();

			for (String handle : allWindows) {
				if (!handle.equals(parentWindow)) {
					driver.switchTo().window(handle);
					LOGGER.info("✓ Switched to child window with title: {}", driver.getTitle());
					return;
				}
			}
			LOGGER.warn("⚠ No child window found to switch to");
		} catch (Exception e) {
			LOGGER.error("✗ Error switching to child window: {}", e.getMessage());
		}
	}

	/**
	 * Switch to specific window by title
	 * @param windowTitle - Title of the window to switch to
	 */
	public boolean switchToWindow(String windowTitle) {
		try {
			Set<String> handles = driver.getWindowHandles();
			for (String handle : handles) {
				driver.switchTo().window(handle);
				if (driver.getTitle().equals(windowTitle)) {
					LOGGER.info("✓ Successfully switched to window: {}", windowTitle);
					return true;
				}
			}
			LOGGER.warn("⚠ Window with title '{}' not found", windowTitle);
			return false;
		} catch (Exception e) {
			LOGGER.error("✗ Error switching to window '{}': {}", windowTitle, e.getMessage());
			return false;
		}
	}

	/**
	 * Switch to iframe using locator
	 * @param by - Locator of the iframe
	 */
	public void switchToFrame(By by) {
		try {
			waitForElementToBePresent(by);
			WebElement frameElement = driver.findElement(by);
			driver.switchTo().frame(frameElement);
			LOGGER.info("✓ Successfully switched to iframe: {}", getElementDescription(by));
		} catch (Exception e) {
			LOGGER.error("✗ Error switching to iframe: {}", e.getMessage());
		}
	}

	/**
	 * Switch to iframe using index
	 * @param index - Index of the iframe
	 */
	public void switchToFrame(int index) {
		try {
			driver.switchTo().frame(index);
			LOGGER.info("✓ Successfully switched to iframe at index: {}", index);
		} catch (Exception e) {
			LOGGER.error("✗ Error switching to iframe at index {}: {}", index, e.getMessage());
		}
	}

	/**
	 * Switch back to parent frame
	 */
	public void switchToParentFrame() {
		try {
			driver.switchTo().parentFrame();
			LOGGER.info("✓ Successfully switched to parent frame");
		} catch (Exception e) {
			LOGGER.error("✗ Error switching to parent frame: {}", e.getMessage());
		}
	}

	/**
	 * Switch to default content (exit all frames)
	 */
	public void switchToDefaultContent() {
		try {
			driver.switchTo().defaultContent();
			LOGGER.info("✓ Successfully switched to default content");
		} catch (Exception e) {
			LOGGER.error("✗ Error switching to default content: {}", e.getMessage());
		}
	}

	// ==================== CLICK OPERATIONS ====================

	/**
	 * Click on element using locator
	 * @param by - Locator of the element
	 */
	public void click(By by) {
		try {
			applyBorder(by, "green");
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			LOGGER.info("✓ Successfully clicked on element: {}", getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			LOGGER.error("✗ Error clicking element '{}': {}", getElementDescription(by), e.getMessage());
		}
	}

	/**
	 * Click using JavaScript (useful for hidden elements)
	 * @param by - Locator of the element
	 */
	public void clickByJS(By by) {
		try {
			waitForElementToBePresent(by);
			WebElement element = driver.findElement(by);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", element);
			LOGGER.info("✓ Successfully clicked element using JavaScript: {}", getElementDescription(by));
		} catch (Exception e) {
			LOGGER.error("✗ Error clicking element by JS '{}': {}", getElementDescription(by), e.getMessage());
		}
	}

	/**
	 * Double-click on element
	 * @param by - Locator of the element
	 */
	public void doubleClick(By by) {
		try {
			waitForElementToBeVisible(by);
			WebElement element = driver.findElement(by);
			actions.doubleClick(element).perform();
			LOGGER.info("✓ Successfully double-clicked on element: {}", getElementDescription(by));
		} catch (Exception e) {
			LOGGER.error("✗ Error double-clicking element '{}': {}", getElementDescription(by), e.getMessage());
		}
	}

	/**
	 * Right-click (context click) on element
	 * @param by - Locator of the element
	 */
	public void rightClick(By by) {
		try {
			waitForElementToBeVisible(by);
			WebElement element = driver.findElement(by);
			actions.contextClick(element).perform();
			LOGGER.info("✓ Successfully right-clicked on element: {}", getElementDescription(by));
		} catch (Exception e) {
			LOGGER.error("✗ Error right-clicking element '{}': {}", getElementDescription(by), e.getMessage());
		}
	}

	/**
	 * Hover over element
	 * @param by - Locator of the element
	 */
	public void hover(By by) {
		try {
			waitForElementToBeVisible(by);
			WebElement element = driver.findElement(by);
			actions.moveToElement(element).perform();
			LOGGER.info("✓ Successfully hovered over element: {}", getElementDescription(by));
		} catch (Exception e) {
			LOGGER.error("✗ Error hovering over element '{}': {}", getElementDescription(by), e.getMessage());
		}
	}

	// ==================== TEXT INPUT OPERATIONS ====================

	/**
	 * Enter text into input field
	 * @param by - Locator of the input field
	 * @param value - Text to enter
	 */
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			LOGGER.info("✓ Successfully entered text '{}' in element: {}", value, getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			LOGGER.error("✗ Error entering text '{}' in element '{}': {}", value, getElementDescription(by), e.getMessage());
		}
	}

	/**
	 * Clear input field
	 * @param by - Locator of the input field
	 */
	public void clearField(By by) {
		try {
			waitForElementToBeVisible(by);
			WebElement element = driver.findElement(by);
			element.clear();
			LOGGER.info("✓ Successfully cleared field: {}", getElementDescription(by));
		} catch (Exception e) {
			LOGGER.error("✗ Error clearing field '{}': {}", getElementDescription(by), e.getMessage());
		}
	}

	/**
	 * Get text from element
	 * @param by - Locator of the element
	 * @return Text content of the element
	 */
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			String text = driver.findElement(by).getText();
			LOGGER.debug("✓ Retrieved text from element '{}': {}", getElementDescription(by), text);
			return text;
		} catch (Exception e) {
			applyBorder(by, "red");
			LOGGER.error("✗ Error getting text from element '{}': {}", getElementDescription(by), e.getMessage());
			return "";
		}
	}

	/**
	 * Get attribute value of element
	 * @param by - Locator of the element
	 * @param attributeName - Name of the attribute
	 * @return Attribute value
	 */
	public String getAttribute(By by, String attributeName) {
		try {
			waitForElementToBePresent(by);
			String attributeValue = driver.findElement(by).getAttribute(attributeName);
			LOGGER.debug("✓ Retrieved attribute '{}' from element '{}': {}", attributeName, getElementDescription(by), attributeValue);
			return attributeValue;
		} catch (Exception e) {
			LOGGER.error("✗ Error getting attribute '{}' from element '{}': {}", attributeName, getElementDescription(by), e.getMessage());
			return "";
		}
	}

	/**
	 * Get CSS property value
	 * @param by - Locator of the element
	 * @param propertyName - CSS property name
	 * @return CSS property value
	 */
	public String getCSSValue(By by, String propertyName) {
		try {
			waitForElementToBePresent(by);
			String cssValue = driver.findElement(by).getCssValue(propertyName);
			LOGGER.debug("✓ Retrieved CSS property '{}' from element '{}': {}", propertyName, getElementDescription(by), cssValue);
			return cssValue;
		} catch (Exception e) {
			LOGGER.error("✗ Error getting CSS property '{}' from element '{}': {}", propertyName, getElementDescription(by), e.getMessage());
			return "";
		}
	}

	// ==================== DROPDOWN & SELECT OPERATIONS ====================

	/**
	 * Select option from dropdown by visible text
	 * @param by - Locator of the dropdown
	 * @param optionText - Visible text of the option
	 */
	public void selectByVisibleText(By by, String optionText) {
		try {
			waitForElementToBeVisible(by);
			Select select = new Select(driver.findElement(by));
			select.selectByVisibleText(optionText);
			LOGGER.info("✓ Successfully selected option '{}' from dropdown: {}", optionText, getElementDescription(by));
		} catch (Exception e) {
			LOGGER.error("✗ Error selecting option '{}' from dropdown '{}': {}", optionText, getElementDescription(by), e.getMessage());
		}
	}

	/**
	 * Select option from dropdown by value
	 * @param by - Locator of the dropdown
	 * @param value - Value of the option
	 */
	public void selectByValue(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			Select select = new Select(driver.findElement(by));
			select.selectByValue(value);
			LOGGER.info("✓ Successfully selected option with value '{}' from dropdown: {}", value, getElementDescription(by));
		} catch (Exception e) {
			LOGGER.error("✗ Error selecting value '{}' from dropdown '{}': {}", value, getElementDescription(by), e.getMessage());
		}
	}

	/**
	 * Select option from dropdown by index
	 * @param by - Locator of the dropdown
	 * @param index - Index of the option
	 */
	public void selectByIndex(By by, int index) {
		try {
			waitForElementToBeVisible(by);
			Select select = new Select(driver.findElement(by));
			select.selectByIndex(index);
			LOGGER.info("✓ Successfully selected option at index {} from dropdown: {}", index, getElementDescription(by));
		} catch (Exception e) {
			LOGGER.error("✗ Error selecting index {} from dropdown '{}': {}", index, getElementDescription(by), e.getMessage());
		}
	}

	/**
	 * Get all options from dropdown
	 * @param by - Locator of the dropdown
	 * @return List of WebElements representing options
	 */
	public List<WebElement> getAllDropdownOptions(By by) {
		try {
			waitForElementToBeVisible(by);
			Select select = new Select(driver.findElement(by));
			List<WebElement> options = select.getOptions();
			LOGGER.info("✓ Retrieved {} options from dropdown: {}", options.size(), getElementDescription(by));
			return options;
		} catch (Exception e) {
			LOGGER.error("✗ Error getting dropdown options from '{}': {}", getElementDescription(by), e.getMessage());
			return List.of();
		}
	}

	// ==================== ELEMENT VERIFICATION ====================

	/**
	 * Check if element is present on the page
	 * @param by - Locator of the element
	 * @return true if element is present, false otherwise
	 */
	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			LOGGER.debug("✓ Element is present: {}", getElementDescription(by));
			return true;
		} catch (Exception e) {
			LOGGER.debug("⚠ Element not present: {}", getElementDescription(by));
			return false;
		}
	}

	/**
	 * Check if element is visible
	 * @param by - Locator of the element
	 * @return true if element is visible, false otherwise
	 */
	public boolean isElementVisible(By by) {
		try {
			WebElement element = driver.findElement(by);
			boolean isVisible = element.isDisplayed();
			LOGGER.debug("✓ Element visibility check: {} - {}", getElementDescription(by), isVisible);
			return isVisible;
		} catch (Exception e) {
			LOGGER.debug("⚠ Element not visible: {}", getElementDescription(by));
			return false;
		}
	}

	/**
	 * Check if element is enabled
	 * @param by - Locator of the element
	 * @return true if element is enabled, false otherwise
	 */
	public boolean isElementEnabled(By by) {
		try {
			WebElement element = driver.findElement(by);
			boolean isEnabled = element.isEnabled();
			LOGGER.debug("✓ Element enabled check: {} - {}", getElementDescription(by), isEnabled);
			return isEnabled;
		} catch (Exception e) {
			LOGGER.debug("⚠ Element not enabled: {}", getElementDescription(by));
			return false;
		}
	}

	/**
	 * Check if element is selected (for checkboxes/radio buttons)
	 * @param by - Locator of the element
	 * @return true if element is selected, false otherwise
	 */
	public boolean isElementSelected(By by) {
		try {
			WebElement element = driver.findElement(by);
			boolean isSelected = element.isSelected();
			LOGGER.debug("✓ Element selected check: {} - {}", getElementDescription(by), isSelected);
			return isSelected;
		} catch (Exception e) {
			LOGGER.debug("⚠ Element not selected: {}", getElementDescription(by));
			return false;
		}
	}

	/**
	 * Get count of elements matching the locator
	 * @param by - Locator of the elements
	 * @return Number of elements found
	 */
	public int getElementCount(By by) {
		try {
			List<WebElement> elements = driver.findElements(by);
			LOGGER.info("✓ Found {} elements matching locator: {}", elements.size(), by);
			return elements.size();
		} catch (Exception e) {
			LOGGER.error("✗ Error getting element count for '{}': {}", by, e.getMessage());
			return 0;
		}
	}

	/**
	 * Wait for element to disappear/become invisible
	 * @param by - Locator of the element
	 */
	public void waitForElementToDisappear(By by) {
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
			LOGGER.info("✓ Element disappeared: {}", getElementDescription(by));
		} catch (Exception e) {
			LOGGER.warn("⚠ Element did not disappear within timeout: {}", e.getMessage());
		}
	}

	// ==================== JAVASCRIPT OPERATIONS ====================

	/**
	 * Execute JavaScript and get result
	 * @param script - JavaScript code to execute
	 * @param args - Arguments to pass to the script
	 * @return Result of JavaScript execution
	 */
	public Object executeScript(String script, Object... args) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			Object result = js.executeScript(script, args);
			LOGGER.debug("✓ JavaScript executed successfully");
			return result;
		} catch (Exception e) {
			LOGGER.error("✗ Error executing JavaScript: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * Scroll to element
	 * @param by - Locator of the element
	 */
	public void scrollToElement(By by) {
		try {
			WebElement element = driver.findElement(by);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);", element);
			LOGGER.info("✓ Scrolled to element: {}", getElementDescription(by));
		} catch (Exception e) {
			LOGGER.error("✗ Error scrolling to element '{}': {}", getElementDescription(by), e.getMessage());
		}
	}

	/**
	 * Scroll page up/down
	 * @param pixels - Number of pixels to scroll (positive for down, negative for up)
	 */
	public void scrollPage(int pixels) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollBy(0," + pixels + ");");
			LOGGER.debug("✓ Page scrolled by {} pixels", pixels);
		} catch (Exception e) {
			LOGGER.error("✗ Error scrolling page: {}", e.getMessage());
		}
	}

	// ==================== WAIT OPERATIONS ====================

	/**
	 * Wait for element to be clickable
	 * @param by - Locator of the element
	 */
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
			LOGGER.debug("✓ Element is clickable: {}", getElementDescription(by));
		} catch (Exception e) {
			LOGGER.warn("⚠ Element not clickable within timeout: {}", e.getMessage());
		}
	}

	/**
	 * Wait for element to be visible
	 * @param by - Locator of the element
	 */
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			LOGGER.debug("✓ Element is visible: {}", getElementDescription(by));
		} catch (Exception e) {
			LOGGER.warn("⚠ Element not visible within timeout: {}", e.getMessage());
		}
	}

	/**
	 * Wait for element to be present in DOM
	 * @param by - Locator of the element
	 */
	private void waitForElementToBePresent(By by) {
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
			LOGGER.debug("✓ Element is present in DOM: {}", getElementDescription(by));
		} catch (Exception e) {
			LOGGER.warn("⚠ Element not present within timeout: {}", e.getMessage());
		}
	}

	/**
	 * Wait for custom condition with custom timeout
	 * @param condition - ExpectedCondition to wait for
	 * @param timeoutSeconds - Timeout in seconds
	 * @return true if condition met, false if timeout
	 */
	public boolean waitForCondition(org.openqa.selenium.support.ui.ExpectedCondition<?> condition, long timeoutSeconds) {
		try {
			WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
			customWait.until(condition);
			LOGGER.debug("✓ Custom condition met within {} seconds", timeoutSeconds);
			return true;
		} catch (Exception e) {
			LOGGER.warn("⚠ Custom condition not met within {} seconds: {}", timeoutSeconds, e.getMessage());
			return false;
		}
	}

	// ==================== UI ENHANCEMENT METHODS ====================

	/**
	 * Apply border to element for visual debugging
	 * @param by - Locator of the element
	 * @param color - Border color (green/red/blue/yellow/etc)
	 */
	public void applyBorder(By by, String color) {
		try {
			WebElement element = driver.findElement(by);
			String script = "arguments[0].style.border='3px solid " + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			LOGGER.debug("✓ Applied {} border to element: {}", color, getElementDescription(by));
		} catch (Exception e) {
			LOGGER.debug("⚠ Error applying border: {}", e.getMessage());
		}
	}

	/**
	 * Highlight element with background color
	 * @param by - Locator of the element
	 * @param color - Background color
	 */
	public void highlightElement(By by, String color) {
		try {
			WebElement element = driver.findElement(by);
			String script = "arguments[0].style.backgroundColor='" + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			LOGGER.debug("✓ Highlighted element with color '{}': {}", color, getElementDescription(by));
		} catch (Exception e) {
			LOGGER.debug("⚠ Error highlighting element: {}", e.getMessage());
		}
	}

	// ==================== ELEMENT DESCRIPTION & UTILITY ====================

	/**
	 * Get intelligent description of element based on available attributes
	 * @param locator - Locator of the element
	 * @return Description string
	 */
	public String getElementDescription(By locator) {
		if (driver == null) {
			LOGGER.error("✗ Driver is not initialized");
			return "Driver not initialized";
		}
		if (locator == null) {
			LOGGER.error("✗ Locator is null");
			return "Locator is null";
		}
		try {
			WebElement element = driver.findElement(locator);

			String name = element.getDomProperty("name");
			String id = element.getDomProperty("id");
			String text = element.getText();
			String className = element.getDomProperty("class");
			String placeholder = element.getDomProperty("placeholder");

			if (isNotEmpty(name)) {
				return "Element with name: " + name;
			} else if (isNotEmpty(id)) {
				return "Element with ID: " + id;
			} else if (isNotEmpty(text)) {
				return "Element with text: " + truncate(text, 50);
			} else if (isNotEmpty(className)) {
				return "Element with class: " + className;
			} else if (isNotEmpty(placeholder)) {
				return "Element with placeholder: " + placeholder;
			} else {
				return "Element located using: " + locator.toString();
			}
		} catch (Exception e) {
			LOGGER.debug("⚠ Unable to describe element: {}", e.getMessage());
			return "Element description unavailable";
		}
	}

	/**
	 * Check if string is not null or empty
	 * @param value - String to check
	 * @return true if not empty, false otherwise
	 */
	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	/**
	 * Truncate long strings for logging
	 * @param value - String to truncate
	 * @param maxLength - Maximum length
	 * @return Truncated string
	 */
	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + "...";
	}

}
