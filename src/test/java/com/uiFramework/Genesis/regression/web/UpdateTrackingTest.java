package com.uiFramework.Genesis.regression.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.DocQueue;
import com.uiFramework.Genesis.web.pages.UpdateTracking;

public class UpdateTrackingTest extends TestBase {
	UpdateTracking ut = null;
	CommonPage cp = null;
	DocQueue dc = null;
	CommonMethods cm;
	int rowCount;

	@BeforeClass(alwaysRun = true)
	public void befreclass() {
	//	test = extent.createTest(getClass().getSimpleName());
		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		ut = new UpdateTracking(driver);
		dc = new DocQueue(driver);
	//	driver.navigate().refresh();

	}

	@Test(priority = 1,alwaysRun = true, groups = {"Smoke"})
	public void shouldShowErrorMessageIfReqFieldNotSelected() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ut.updteTrackMenu();
		ut.selectLTL();
		ut.EnterTrkNo();
		ut.clickUpdateBtn();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Please select at least one truck..");
		sAssert.assertAll();
	}

	@Test(priority = 2,alwaysRun = true, groups = {"Smoke"})
	public void shouldShowErrorMessageIfReqSelectedButTrackingNoNotEntered() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ut.selTruckChkbox();
		ut.clickUpdate();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "ETA & Tracking no is Required.");
		sAssert.assertAll();
	}

	@Test(priority = 3,alwaysRun = true, groups = {"Smoke"})
	public void shouldRecErrMsgIfReqTrkNoSelButETAIsNotEnter() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ut.EnterTrkNo();
		ut.clickUpdate();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Please enter the ETA for the selected trucks.");
		sAssert.assertAll();
	}

	@Test(priority = 4,alwaysRun = true, groups = {"Smoke"})
	public void shouldAbleToUpdateTrackinIdOfReq() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ut.selDate();
		ut.clickUpdate();
		cp.waitForPopupToDisappear();
		ut.clickUpdate();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "ETA & Tracking number is updated successfully.");
		sAssert.assertAll();
	}

	@Test(priority = 5)
	public void shouldAllowUserToUpdateMultipleRequestsAtOnce() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ut.enterTrackingDetailsInLoop();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "ETA & Tracking number is updated successfully.");
		sAssert.assertAll();
	}

	@Test(priority = 6,alwaysRun = true, groups = {"Smoke"})
	public void shouldAllowUserToDownloadExcelOfGridData() throws FileNotFoundException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ut.downloadFileFormat();
		Thread.sleep(5000);
	}

	@Test(priority = 7, dependsOnMethods = "shouldAllowUserToDownloadExcelOfGridData")
	public void shouldNotAllowFileUpdateWithoutTrackingNumberDetails() throws InterruptedException, IOException {
		SoftAssert sAssert = new SoftAssert();
		ut.updateFileFormat(true, false);
		Thread.sleep(700);
		ut.excelUpload();
		cp.waitForLoaderToDisappear();
		rowCount = ut.getTableRowCount();
		sAssert.assertEquals(rowCount, 0);
		sAssert.assertAll();
	}

	@Test(priority = 8, dependsOnMethods = "shouldAllowUserToDownloadExcelOfGridData")
	public void shouldNotAllowFileUpdateWithoutETADetails() throws InterruptedException, IOException {
		SoftAssert sAssert = new SoftAssert();
		ut.updateFileFormat(false, true);
		Thread.sleep(700);
		ut.excelUpload();
		cp.waitForLoaderToDisappear();
		rowCount = ut.getTableRowCount();
		sAssert.assertEquals(rowCount, 0);
		sAssert.assertAll();
	}

	@Test(priority = 9, dependsOnMethods = "shouldAllowUserToDownloadExcelOfGridData")
	public void shouldNotAllowUpdateWithoutRequiredDetails() throws InterruptedException, IOException {
		SoftAssert sAssert = new SoftAssert();
		ut.updateFileFormat(false, false);
		Thread.sleep(700);
		ut.excelUpload();
		cp.waitForLoaderToDisappear();
		rowCount = ut.getTableRowCount();
		sAssert.assertEquals(rowCount, 0);
		sAssert.assertAll();
	}

	@Test(priority = 10, alwaysRun = true, groups = {"Smoke"}, dependsOnMethods = "shouldAllowUserToDownloadExcelOfGridData")
	public void shouldAllowUpdateWithValidData() throws InterruptedException, IOException {
		SoftAssert sAssert = new SoftAssert();
		ut.updateFileFormat(true, true);
		ut.excelUpload();
		cp.waitForLoaderToDisappear();
		Thread.sleep(5000);
		rowCount = ut.getTableRowCount();
		sAssert.assertNotEquals(rowCount, 0);
		sAssert.assertAll();
	}

	@Test(priority = 11)
	public void shouldValidateMultipleRequestData() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		ut.selStatus("Done");
		cp.Search();
		ut.TruckDataValidation();
	}

//	@Test(priority = 12, alwaysRun = true, dependsOnMethods = "ToCheckUserIsAbleToDownloadExcelOfGridData")
//	public void shouldUpdateTrackingForMainOrder() throws TimeoutException, InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ut.trackOrderMenu();
//		dc.searchOrder();
//		ut.selTruckChkbox();
//		ut.EnterTrkNo();
//		ut.selDate();
//		ut.clickUpdate();
//		boolean flag = cp.toastMsgReceivedSuccessfully();
//		sAssert.assertTrue(flag, "Tracking update successfully.");
//		sAssert.assertAll();
//	}

}
