package com.test.framwork.cucumber.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GreenKartOffersPage extends BasePage {

	public GreenKartOffersPage(WebDriver driver) {
		super(driver);
	}

	By searchBox = By.xpath("//input[@type='search']");
	By productName = By.cssSelector("tr td:nth-child(1)");


	public void searchProduct(String product) {
		enterText(searchBox, product);
	}

	public String getProductName() {
		return getText(productName);

	}
}