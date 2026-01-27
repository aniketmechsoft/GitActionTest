package com.uiFramework.Genesis.regression.ios;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.android.pages.AvailablePage;
import com.uiFramework.Genesis.android.pages.DelieveredPage;
import com.uiFramework.Genesis.android.pages.LDADashboard;
import com.uiFramework.Genesis.android.pages.LoginPage;
import com.uiFramework.Genesis.android.pages.PieceCountPendingPage;

public class AvailableTest {
	
//	@Test(priority = 1)
//	public void UserIsableToSearchLbol() throws InterruptedException {
//	//	System.out.println("Nice.....................");
//		Thread.sleep(19000);
//		LoginPage formPage = new LoginPage(driver);
//		formPage.clickNext();
//		formPage.clickNext();
//		loginPage.clickLoginNow();
//		loginPage.performLogin(loginPage, "aryaka-64", "gl1234");
//		LDADashboard ldaDashboard = new LDADashboard(driver);
//		ldaDashboard.clickPieceCountPending();
//		DelieveredPage dp = new DelieveredPage(driver);
//		dp.enterLbol("");
//
//	}
//    
//	@Test(priority = 2)
//	public void UserReceivedNoRecFoundMsgForInvalidSearch() {
//		SoftAssert sAssert = new SoftAssert();
//		PieceCountPendingPage pcp = new PieceCountPendingPage(driver);
//		sAssert.assertEquals(pcp.getNoRecFoundMsg(), "No Record Found");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 3)
//	public void UserIsAbleToSeeHistoryWithClickOnHistory() {
//		SoftAssert sAssert = new SoftAssert();
//		AvailablePage ap = new AvailablePage(driver);
//		ap.clickHistoryBtn();
//		sAssert.assertEquals(ap.isScheduledHistoryNoDataFoundVisible(), true);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 4)
//	public void UserIsAbleToClickOnDelieveryConfirmationBtn() {
//		SoftAssert sAssert = new SoftAssert();
//		AvailablePage ap = new AvailablePage(driver);
//        ap.backBtn();
//        number = ap.getLBOLNoText();
//        ap.clickDelConfirmationBtn();
//        sAssert.assertEquals(number, ap.getLBOLNoText());
//		sAssert.assertAll();
//		
//	}
//	
//	@Test(priority = 5)
//	public void UserIsAbleToSeeDamageOrdersWithCheckOnDamageCheckbox() {
//		SoftAssert sAssert = new SoftAssert();
//		AvailablePage ap = new AvailablePage(driver);
//		ap.clickDamageCheckbox();
//		ap.clickYesBtn();
//		sAssert.assertEquals(ap.isDamagedOrderVisible(), true);
//		sAssert.assertEquals(ap.isDamagedLitVisible(), true);
//		sAssert.assertAll(); 
//	}
//	
//	@Test(priority = 6)
//	public void UserIsAbleToNotAbleToSeeDamageOrdersWithUncheckOnDamageCheckbox() {
//		SoftAssert sAssert = new SoftAssert();
//		AvailablePage ap = new AvailablePage(driver);
//		ap.clickDamageCheckbox();
//		sAssert.assertEquals(ap.isDamagedOrderVisible(), true);
//		sAssert.assertEquals(ap.isDamagedLitVisible(), true);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 7)
//	public void UserIsAbleToClickOnMarkReceivedBtn() {
//		SoftAssert sAssert = new SoftAssert();
//		AvailablePage ap = new AvailablePage(driver);
//		ap.clickMarkRecBtn();
//		sAssert.assertNotEquals(ap.getRecOrder(), 0);
//		sAssert.assertAll();
//
//	}
//	
//	@Test(priority = 8)
//	public void UserCanSeeSameRecAndActualOrderPiecesWhenClicksOnMarkReceived() {
//		SoftAssert sAssert = new SoftAssert();
//		AvailablePage ap = new AvailablePage(driver);
//		sAssert.assertEquals(ap.getActOrdPiecesText(), ap.getRecOrder());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 9)
//	public void UserNotAbleToSaveOrderWithoutDelieveryDate() {
//		SoftAssert sAssert = new SoftAssert();
//		AvailablePage ap = new AvailablePage(driver);
//		ap.clickConfirmBtn();
//
//	}
//	
//	@Test(priority = 10)
//	public void UserAbleToConfirmOrderWithValidDetails() {
//		SoftAssert sAssert = new SoftAssert();
//		AvailablePage ap = new AvailablePage(driver);
//		ap.clickConfirmBtn();
//
//	}

}
