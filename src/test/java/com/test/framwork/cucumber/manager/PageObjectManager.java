package com.test.framwork.cucumber.manager;

import org.openqa.selenium.WebDriver;

import com.test.framwork.cucumber.pages.GreenKartCheckoutPage;
import com.test.framwork.cucumber.pages.GreenKartLandingPage;
import com.test.framwork.cucumber.pages.GreenKartOffersPage;

public class PageObjectManager {

	private WebDriver driver;
	private GreenKartLandingPage landingPage;
	private GreenKartOffersPage offersPage;
	private GreenKartCheckoutPage checkoutpage;

	public PageObjectManager(WebDriver driver) {
		this.driver = driver;
	}

	public GreenKartLandingPage getLandingPage() {
		if (landingPage == null) {
			landingPage = new GreenKartLandingPage(driver);
		}
		return landingPage;
	}

	public GreenKartOffersPage getOffersPage() {
		if (offersPage == null) {
			offersPage = new GreenKartOffersPage(driver);
		}
		return offersPage;
	}

	public GreenKartCheckoutPage getCheckoutPage() {
		if (checkoutpage == null) {
			checkoutpage = new GreenKartCheckoutPage(driver);
		}
		return checkoutpage;
	}
}
