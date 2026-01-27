package com.uiFramework.Genesis.regression.android;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.android.pages.AvailablePage;
import com.uiFramework.Genesis.android.pages.LDADashboard;
import com.uiFramework.Genesis.android.pages.LoginPage;
import com.uiFramework.Genesis.android.pages.PieceCountPendingPage;
import com.uiFramework.Genesis.base.BaseTest;

import io.appium.java_client.android.AndroidDriver;

public class PieceCountPendingTest extends BaseTest {

	@Test(priority = 1)
	public void UserIsableToSearchManifest() throws InterruptedException {
		System.out.println("Nice.....................");
		Thread.sleep(10000);
		
		LoginPage formPage = new LoginPage((AndroidDriver)driver);
		formPage.allow();
		formPage.clickNext();
		formPage.clickNext();
		formPage.clickLoginNow();//current we change navigate driver to current object(formPage)
		formPage.performLogin(formPage, "aryaka-64", "gl1234");
		LDADashboard ldaDashboard = new LDADashboard(driver);
		Thread.sleep(3000);
		ldaDashboard.clickPieceCountPending();
		PieceCountPendingPage pcp = new PieceCountPendingPage((AndroidDriver)driver);
		//pcp.enterManifest("abc");

	}

//	@Test(priority = 2)
//	public void UserReceivedNoRecFoundMsgForInvalidSearch() {
//		SoftAssert sAssert = new SoftAssert();
//		PieceCountPendingPage pcp = new PieceCountPendingPage(driver);
//		sAssert.assertEquals(pcp.getNoRecFoundMsg(), "No Record Found");
//		sAssert.assertAll();
//
//	}
//
//	@Test(priority = 3)
//	public void UserIsAbleToSeeOrderDetailsWithClickOnManifest() {
//		SoftAssert sAssert = new SoftAssert();
//		AvailablePage ap = new AvailablePage(driver);
//		ap.clickAvailableMenu();
//		PieceCountPendingPage pcp = new PieceCountPendingPage(driver);
//		pcp.clickPieceCntPendingMenu();
//		pcp.assertForManifestData();
//	}
//
//	@Test(priority = 4)
//	public void UserIsAbleToSeeRecOrdAndLitDisableForNonMatchedPieces() {
//		PieceCountPendingPage pcp = new PieceCountPendingPage(driver);
//		pcp.recOrderDisableValid();
//
//	}
//
//	@Test(priority = 5)
//	public void UserIsNotAbleToProcessWithoutTrackingNumberUpdate() {
//		PieceCountPendingPage pcp = new PieceCountPendingPage(driver);
//		pcp.TrkingNoNotUpdatedValid();
//
//	}
//
//	@Test(priority = 6)
//	public void UserNotAbleToSaveOrderWithoutDelieveryDate() {
//		PieceCountPendingPage pcp = new PieceCountPendingPage(driver);
//		pcp.enterManifest("");
//		pcp.enterRecOrdPieces("");
//		pcp.enterLitOrdPieces("");
//		pcp.clickConfirmBtn();
//	}
//
//	@Test(priority = 7)
//	public void UserAbleToSAveOrderWithValidData() {
//		PieceCountPendingPage pcp = new PieceCountPendingPage(driver);
//		pcp.clickConfirmBtn();
//	}
	
	@Test(priority = 8)
	public void UserAbleToDoPartialQA() throws InterruptedException {
		PieceCountPendingPage pcp = new PieceCountPendingPage((AndroidDriver)driver);
		//pcp.clickConfirmBtn();
		pcp.checkManifestAvailableForDelivery();
		pcp.enterRecOrderPieces("10");
		pcp.enterLitOrderPieces("1");
		pcp.closeKeyboard();
		pcp.confirmOrder();
		
	}
	
	@Test(priority = 9, dependsOnMethods="UserAbleToDoPartialQA")
	public void VerifyManifestStatusShouldBeUnderGenesisReview() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		PieceCountPendingPage pcp = new PieceCountPendingPage((AndroidDriver)driver);
		pcp.viewDetails();
		System.out.println("M sta "+pcp.getmanifestStatus());
		sAssert.assertEquals(pcp.getmanifestStatus(),"Under Genesis Review", "Not match!");
		sAssert.assertAll();
	}
	
	

}
