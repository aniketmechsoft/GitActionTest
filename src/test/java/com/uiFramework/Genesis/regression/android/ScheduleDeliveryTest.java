package com.uiFramework.Genesis.regression.android;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

public class ScheduleDeliveryTest {
	
	@BeforeMethod
	public void preSetup() {
//		 Activity activity = new Activity("com.mechsoft.contact_mngmt",
//		 "com.mechsoft.contact_mngmt.MainActivity");
//		((JavascriptExecutor) driver).executeScript("mobile:startActivity",
//				ImmutableMap.of("Intent", "com.mechsoft.contact_mngmt/com.mechsoft.contact_mngmt.MainActivity"));
//		LoginPage formPage = new LoginPage(driver);

	}

//	@Test(priority = 1, groups = "Smoke")
//	public void LoginIntoTheApplication() throws InterruptedException {
//		System.out.println("Nice.....................");
//		LoginPage formPage = new LoginPage(driver);
//		formPage.allow();
//		formPage.clickNext();
//		formPage.clickNext();
//		loginPage.clickLoginNow();
//		loginPage.performLogin(loginPage, "aryaka-64", "gl1234");
//		LDADashboard ldaDashboard = new LDADashboard(driver);
//		Thread.sleep(5000);
//		ldaDashboard.clickScheduled();
//	}
//
//	@Test(priority = 2)
//	public void shouldCheckErrorOnUpdateBtn() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ScheduledPage sp = new ScheduledPage(driver);
//		Thread.sleep(4000);
//		sp.clickonUpdate();
//		sAssert.assertTrue(loginPage.isDisplayedAndroidToast());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 3)
//	public void shouldSearchLBOLNumberCorreclty() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ScheduledPage sp = new ScheduledPage(driver);
//		System.out.println("Lbol is "+sp.getLBOL());
//		sp.searchLBOL(sp.getLBOL());
//		sAssert.assertFalse(sp.isDisplayedNoRecord());
//		sAssert.assertAll();	
//	}
//	
//	@Test(priority = 4)
//	public void shouldDateClearOnClearBtn() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ScheduledPage sp = new ScheduledPage(driver);
//		sp.enterDateForRow(1,sp.getCurrentDateTime()); 
//		sp.enterDateForRow(2,sp.getCurrentDateTime());
//		sp.enterDateForRow(3,sp.getCurrentDateTime());
//		sp.clearDates();
//		sp.clickonUpdate();
//		sAssert.assertTrue(loginPage.isDisplayedAndroidToast());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 5)
//	public void shouldShowErrorForDuplicatedDelDate() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ScheduledPage sp = new ScheduledPage(driver);
//		sp.enterDateForRow(1,sp.getCurrentDateTime()); 
//		sp.enterDateForRow(2,sp.getCurrentDateTime());
//		sp.enterDateForRow(3,sp.getCurrentDateTime());
//		sp.clickonUpdate();
//		sAssert.assertTrue(loginPage.isDisplayedAndroidToast());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 6)
//	public void shouldCheckUserCanScheduledDelDate() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ScheduledPage sp = new ScheduledPage(driver);
//		sp.enterDateForRow(1,sp.getFutureDateTime(1));
//		sp.enterDateForRow(2,sp.getFutureDateTime(2));
//		sp.enterDateForRow(3,sp.getFutureDateTime(3));	
//		sp.clickonUpdate();
//		sAssert.assertTrue(loginPage.isDisplayedAndroidToast());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 7, dependsOnMethods="shouldCheckUserCanScheduledDelDate")
//	public void shouldShowScheduleDateandTime() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ScheduledPage sp = new ScheduledPage(driver);
//		sAssert.assertFalse(sp.isDateBlank());
//		sAssert.assertAll();
//	}
//
//	@Test(priority = 8)
//	public void shouldShowScheduledHistory() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ScheduledPage sp = new ScheduledPage(driver);
//		String schedDate= sp.getSchedDate();
//		System.out.println("SchedDate "+schedDate);
//		sp.clickonHistoryBtn();
//		sp.scrollToBottom();
//		sAssert.assertTrue(sp.isDatePresent(), "Date Not Found on history!");
//	    sAssert.assertAll();
//	}
//
//	@Test(priority = 9)
//	public void shouldPiecesMandatoryForDelivery() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ScheduledPage sp = new ScheduledPage(driver);
//		sp.closeHistory();
//		sp.checkLBOLAvailableForDelivery();
//		sp.confirmOrder();
//		sAssert.assertTrue(loginPage.isDisplayedAndroidToast(), "Should show Toast for Received Pieces.");
//		sAssert.assertAll();
//	}
//
//	@Test(priority = 10)
//	public void shouldMandDeliveryDateOnConfirmBtn() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ScheduledPage sp = new ScheduledPage(driver);
//		sp.clearDelDate();
//		sp.confirmOrder();
//		sAssert.assertTrue(loginPage.isDisplayedAndroidToast(), "Not Show Message On clear Delivery date.");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 11)
//	public void shouldShowPopupOnCancelBtn() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ScheduledPage sp = new ScheduledPage(driver);
//		sp.enterDelDate();
//		sp.cancelOrder();
//		sAssert.assertEquals(sp.getTextFromPopup(), "Are you sure you want to cancel QA?", "Not Match!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 12, dependsOnMethods="shouldShowPopupOnCancelBtn")
//	public void shouldCheckNoBtnOnPopup() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ScheduledPage sp = new ScheduledPage(driver);
//		sp.clickOnNo();
//		sAssert.assertTrue(sp.isDispalyedConfrimBtn(), "No Button not working");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 13)
//	public void shouldShowPopupMsgForDamageConfirm() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ScheduledPage sp = new ScheduledPage(driver);
//		sp.markRecieved();
//		sp.damageCheckBox();
//		sAssert.assertEquals(sp.getDamageConfiMsg(), "Are you sure to mark this order to have damaged pieces?", "Not match!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 14, dependsOnMethods="shouldShowPopupMsgForDamageConfirm")
//	public void shouldWorkNoBtnOnDamageConPopup() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ScheduledPage sp = new ScheduledPage(driver);
//		sp.clickOnNo();
//		sAssert.assertFalse(sp.isDamageOrdFieldDisplayed(), "Should Not displayed DamageField");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 15)
//	public void shouldNotAbletoEnterMoreDamagePiecesThanAvailable() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ScheduledPage sp = new ScheduledPage(driver);
//		sp.damageCheckBox();
//		sp.clickOnYes();
//		//Thread.sleep(3000);
//		System.out.println("more pc "+ sp.getMoreDamageActPc());
//		sp.enterDamagePieces(sp.getMoreDamageActPc());
//		sAssert.assertTrue(sp.isExceedPiecesMsgDisplayed(), "Not Displayed Msg if damage pieces are exceed.");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 16)
//	public void shouldNotAbletoEnterMoreDamageLitPiecesThanAvailable() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ScheduledPage sp = new ScheduledPage(driver);
//		sp.enterDamagePieces(sp.getHalfDamagePc());
//		System.out.println("Half pc "+ sp.getHalfDamagePc());
//		sp.enterDamageLitPieces(sp.getMoreDamageLitPc());
//		sAssert.assertTrue(sp.isExceedPiecesMsgDisplayed(), "Not Displayed Msg damage pieces are exceed.");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 17)
//	public void shouldConfirmDelivery() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ScheduledPage sp = new ScheduledPage(driver);
//		System.out.println("get Pc "+ sp.getHalfDamageLitPc());
//		sp.enterDamageLitPieces(sp.getHalfDamageLitPc());
//		sp.fillReceivedPiecesDynamically();
//		sp.confirmOrder();
//		//sAssert.assertTrue(loginPage.isDisplayedAndroidToast(), "Not Displayed toast For confirm Delivery.");
//		sAssert.assertAll();
//	}
//	
//	
//	@Test(dataProvider = "getData", enabled = false)
//	public void FillForm_ErrorValidation(HashMap<String, String> input) throws InterruptedException {
//		System.out.println("Execution is up to date");
////		LoginPage formPage = new LoginPage(driver);
//		Thread.sleep(5000);
////		formPage.clickAgreenContinue();
////		formPage.clickNext();
////		formPage.clickNext();
////		formPage.loginText();
////		Thread.sleep(2000);
////		formPage.SetPhoneNumber(input.get("number"));
////		formPage.setPassword(input.get("password"));
////		formPage.login();
////		String toastMessage = driver.findElement(By.xpath("(//android.widget.Toast)[1]")).getAttribute("name");
////		AssertJUnit.assertEquals(toastMessage, "Please your name");
//
//	}

//	@DataProvider
//	public Object[][] getData() throws IOException {
//		List<HashMap<String, String>> data = getJsonData(
//				System.getProperty("user.dir") + "\\resources\\loginCredential.json");
//
//		return new Object[][] { { data.get(1) } };
//	}


}
