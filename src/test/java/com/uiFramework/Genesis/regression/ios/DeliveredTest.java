package com.uiFramework.Genesis.regression.ios;

import java.io.IOException;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.android.pages.AvailablePage;
import com.uiFramework.Genesis.android.pages.DelieveredPage;
import com.uiFramework.Genesis.android.pages.LDADashboard;
import com.uiFramework.Genesis.android.pages.LoginPage;
import com.uiFramework.Genesis.android.pages.PieceCountPendingPage;

public class DeliveredTest {
	
//	@Test(priority = 1)
//	public void UserIsableToSearchLbol() throws InterruptedException {
//		System.out.println("Nice.....................");
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
//
//	}
//	
//	@Test(priority = 3)
//	public void UserIsNotAbleToSavePODUploadWithoutFileUpload() {
//		SoftAssert sAssert = new SoftAssert();
//		AvailablePage ap = new AvailablePage(driver);
//		ap.clickAvailableMenu();
//		DelieveredPage dp = new DelieveredPage(driver);
//		dp.clickDelMenu();
//		dp.clickPopUpload();
//		dp.clickSaveBtn();
//
//	}
//	
//	@Test(priority = 4)
//	public void UserIsAbleToUploadPOD() throws IOException {
//		SoftAssert sAssert = new SoftAssert();
//		DelieveredPage dp = new DelieveredPage(driver);
//		dp.uploadPOD();
//		dp.clickSaveBtn();
//		sAssert.assertEquals(dp.getPODUploadSuccessfullyMsg(), "POD uploaded successfully!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 5)
//	public void UserIsAbleToSeeDetails() {
//		DelieveredPage dp = new DelieveredPage(driver);
//        dp.clickOkBtn();
//        dp.getLBOL();
//        dp.getConsignee();
//        dp.getCustName();
//
//	}

}
