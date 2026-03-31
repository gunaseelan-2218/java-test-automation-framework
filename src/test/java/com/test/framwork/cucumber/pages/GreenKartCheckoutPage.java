package com.test.framwork.cucumber.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GreenKartCheckoutPage extends BasePage {

	public GreenKartCheckoutPage(WebDriver driver) {
		super(driver);
	}

	By proceedToCheckout = By.xpath("//button[text()='PROCEED TO CHECKOUT']");
	By productName = By.xpath("//p[@class='product-name']");
	By productQuantity = By.xpath("//p[@class='quantity']");

	public void clickProceedToCheckout() {
		System.out.println("The Checkoutpage is ");
		click(proceedToCheckout);
	}

	public String getProductName() {
		return getText(productName).split("-")[0].strip();

	}

	public String getProductQuantity() {
		return getText(productQuantity).strip();

	}

}
