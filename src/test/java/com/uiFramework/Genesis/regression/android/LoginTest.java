package com.uiFramework.Genesis.regression.android;

import org.testng.annotations.Test;

import com.uiFramework.Genesis.android.pages.AvailablePage;
import com.uiFramework.Genesis.android.pages.LDADashboard;
import com.uiFramework.Genesis.android.pages.LoginPage;
import com.uiFramework.Genesis.android.pages.LoginPagee;
import com.uiFramework.Genesis.android.pages.PieceCountPendingPage;
import com.uiFramework.Genesis.base.BaseTest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

public class LoginTest extends BaseTest {
	
	@BeforeMethod
	public void preSetup() {
//		 Activity activity = new Activity("com.mechsoft.contact_mngmt",
//		 "com.mechsoft.contact_mngmt.MainActivity");
//		((JavascriptExecutor) driver).executeScript("mobile:startActivity",
//				ImmutableMap.of("Intent", "com.mechsoft.contact_mngmt/com.mechsoft.contact_mngmt.MainActivity"));
//		LoginPage formPage = new LoginPage(driver);
//		formPage = new LoginPage((AppiumDriver) driver);

	}

	@Test(priority = 1, groups = "Smoke")
	public void saveNUpdateContact() throws InterruptedException {
//		System.out.println("Nice.....................");
////		formPage.acceptPopup();
//		Thread.sleep(19000);
////		LoginPage formPage = new LoginPage(driver);
////		System.out.println("Current page source: " + driver.getPageSource());
//		formPage.clickNext();
//		Thread.sleep(2000);
//		formPage.clickNext();
//		Thread.sleep(2000);
//		loginPage.clickLoginNow();
//		Thread.sleep(2000);
//		loginPage.performLogin(loginPage, "aryaka-64", "gl1234");
//		Thread.sleep(4000);
//		LDADashboard ldaDashboard = new LDADashboard(driver);
////		ldaDashboard.clickPieceCountPending();
//		ldaDashboard.clickAvailable();
//		Thread.sleep(5000);
//		PieceCountPendingPage pieceCntPending = new PieceCountPendingPage(driver);
//		pieceCntPending.getManifestNumberText();
//		Thread.sleep(2000);
//		pieceCntPending.getConsigneeNameText();
//		Thread.sleep(2000);
//		pieceCntPending.getCreatedDateText();
//		Thread.sleep(2000);
//		pieceCntPending.getTrkingNoText();
//		Thread.sleep(2000);
//		pieceCntPending.getetaText();
//		Thread.sleep(2000);
//		pieceCntPending.clickManifest();
//		Thread.sleep(2000);
//		pieceCntPending.clickCancelBtn();
		
//		AvailablePage available = new AvailablePage(driver);
//		available.getLBOLNoText();
//		Thread.sleep(2000);
//		available.getConsignNameText();
//		Thread.sleep(2000);
//		available.getConsignCntactText();
//		Thread.sleep(2000);
//		available.getCustomerNameText();
//		Thread.sleep(2000);
//		available.getWtText();
//		Thread.sleep(2000);
//		available.getDelieveryLocationText();
//		Thread.sleep(2000);
//		available.getReqDateTimeText();
//		Thread.sleep(2000);
		
//		available.clickDelConfirmationBtn();
//		Thread.sleep(2000);
//		available.getlboText();
		
//		formPage.clickAgreenContinue();
//		formPage.clickNext();
//		formPage.clickNext();
//		formPage.loginText();
//		formPage.selectCountry();
//		Thread.sleep(2000);
//		formPage.SetPhoneNumber(input.get("number"));
//		formPage.setPassword(input.get("password"));
//		formPage.login();
	}

	@Test(enabled = false)
	public void FillForm_ErrorValidation(HashMap<String, String> input) throws InterruptedException {
		System.out.println("Execution is up to date");
//		LoginPage formPage = new LoginPage(driver);
		Thread.sleep(18000);
//		formPage.clickAgreenContinue();
//		formPage.clickNext();
//		formPage.clickNext();
//		formPage.loginText();
//		Thread.sleep(2000);
//		formPage.SetPhoneNumber(input.get("number"));
//		formPage.setPassword(input.get("password"));
//		formPage.login();
//		String toastMessage = driver.findElement(By.xpath("(//android.widget.Toast)[1]")).getAttribute("name");
//		AssertJUnit.assertEquals(toastMessage, "Please your name");

	}

	@DataProvider
	public Object[][] getData() throws IOException {
		List<HashMap<String, String>> data = getJsonData(
				System.getProperty("user.dir") + "\\resources\\loginCredential.json");

		return new Object[][] {  { data.get(1) }  };
	}

}



