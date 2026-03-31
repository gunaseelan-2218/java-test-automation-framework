package com.test.framwork.cucumber.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GreenKartLandingPage extends BasePage {

	public GreenKartLandingPage(WebDriver driver) {
		super(driver);
	}

	By searchBox = By.xpath("//input[@type='search']");
	By productName = By.cssSelector("h4.product-name");
	By topDealsLink = By.linkText("Top Deals");
	By increasePdtCount = By.xpath("//a[@class='increment']");
	By addToCart = By.xpath("//button[text()='ADD TO CART']");
	By cart = By.xpath("//img[@alt='Cart']");

	public void searchProduct(String product) {
		enterText(searchBox, product);
	}

	public String getProductName() {
		return getText(productName).split("-")[0].strip();
	}

	public void clickTopDeals() {
		click(topDealsLink);
		switchToChildWindow();
	}

	public void addProductCount(int n) {
		for (int i = 1; i <= n; i++) {
			click(increasePdtCount);
		}
	}

	public void addProductToCart() throws InterruptedException {
		click(addToCart);
		Thread.sleep(5000);
		click(cart);
	}
}