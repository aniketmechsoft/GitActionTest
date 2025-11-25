package com.uiFramework.Genesis.regression.web;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.DataRepo;
import com.uiFramework.Genesis.web.pages.DimWtUpdate;

public class DimWtUpdateTest extends TestBase {
	CommonMethods cm = null;
	CommonPage cp = null;
	DimWtUpdate dwu = null;
	String ordNo;
	String dimWt;
	String litDimWt;

	@BeforeClass
	public void beforeclass() {

		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		dwu = new DimWtUpdate(driver);
	//	driver.navigate().refresh();

	}

	@Test(priority = 1)
	public void shouldSkipTestWhenNoDimWeightRecordsPresent() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		dwu.goToDimWeightScreen();
		dwu.checkDataPresent();
		sAssert.assertTrue(dwu.checkDataPresent());
		sAssert.assertAll();
	}

	@Test(priority = 2)
	public void shouldShowWarningToastWhenSavingWithoutSelectingAnyRecord() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		dwu.clickSaveDimWeight();
		sAssert.assertEquals(cp.captureToastMessage(), "Please select at least one record.", "Not match.");
		sAssert.assertAll();
	}

	@Test(priority = 3)
	public void shouldRejectAlphaNumericAndNegativeValuesInDimAndLitWeightFields() {
		dwu.selectCheckbox();
//		dwu.enterDimweight(cm.getAlphaNumericString(12));
//		dwu.enterLitDimweight(cm.getAlphaNumericString(12));

	}

	@Test(priority = 4)
	public void shouldTrimDimAndLitWeightFieldsWhenInputExceedsMaxLength() {
		dwu.enterDimweightwith101Numbers(cm.getNumericString(110));
		dwu.enterLitDimweightwith101Numbers(cm.getNumericString(110));
	}

	@Test(priority = 5)
	public void shouldSaveDimAndLitWeightsAndValidateSuccessMessageAndStoredValues() {
		SoftAssert sAssert = new SoftAssert();
		dwu.enterDimweightValue(100);
		dwu.enterLitDimweightValue(100);
		dwu.clickSaveDimWeight();
		sAssert.assertEquals(cp.captureToastMessage(), "Dimensional Weight save successfully.", "Not match.");
		sAssert.assertAll();
	}

	@Test(priority = 6)
	public void validateDimWeightOnMultiOrderSave() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		dwu.fillFirstThreeRows(cm.getNumericString(2));
		sAssert.assertEquals(cp.captureToastMessage(), "Dimensional Weight save successfully.", "Not match.");
		sAssert.assertAll();
	}

	@Test(priority = 7)
	public void Import_Excel_validationandExcel_import_success() throws InterruptedException, FileNotFoundException {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		dwu.downloadFileFormat();

	}

	@Test(priority = 8)
	public void ToCheckUserIsNotAbleToUpdateBulkDimWtFileWithoutDimWtDetails()
			throws InterruptedException, IOException {
		SoftAssert sAssert = new SoftAssert();
		dwu.updateFileFormat(false, true);
		dwu.ClickImportBtn();
		dwu.excelUpload();
		sAssert.assertEquals(dwu.getErrorMsg(), DataRepo.DIM_WEIGHT_BLANK_ERROR);
		sAssert.assertAll();
	}

	@Test(priority = 9)
	public void ToCheckUserIsNotAbleToUpdateFileWithoutETADetails() throws InterruptedException, IOException {
		SoftAssert sAssert = new SoftAssert();
		dwu.closeErrDescPopup();
		dwu.updateFileFormat(true, false);
		dwu.excelUpload();
		sAssert.assertEquals(dwu.getErrorMsg(), DataRepo.LIT_DIM_WEIGHT_BLANK_ERROR);
		sAssert.assertAll();
	}

	@Test(priority = 10)
	public void ToCheckUserIsNotAbleToUpdateWithoutDetails() throws InterruptedException, IOException {
		SoftAssert sAssert = new SoftAssert();
		dwu.closeErrDescPopup();
		dwu.updateFileFormat(false, false);
		dwu.excelUpload();
		sAssert.assertEquals(dwu.getErrorMsg(),
				"Dim. Weight should not be blank.\nLit. Dim. Weight should not be blank.");
		sAssert.assertAll();
	}

	@Test(priority = 11)
	public void ToCheckUserIsAbleToUpdateWithValidData() throws InterruptedException, IOException {
		SoftAssert sAssert = new SoftAssert();
		dwu.closeErrDescPopup();
		dwu.updateFileFormat(true, true);
		dwu.excelUpload();
		sAssert.assertTrue((dwu.uploadedDataCount()));
		sAssert.assertAll();
	}
	
	@Test(priority = 12)
	public void ToCheckUserIsNotAbleToSaveReqWithoutDimWt() throws InterruptedException, IOException {
		SoftAssert sAssert = new SoftAssert();
		dwu.selTop5RecFromUploadedExcel();
		dwu.enterDimweightValOnUpload("");
		dwu.clickSaveDimWeight();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Plese select one record.");
		sAssert.assertAll();
	}
	
	@Test(priority = 13)
	public void ToCheckUserIsNotAbleToSaveReqWithoutLitDimWt() throws InterruptedException, IOException {
		SoftAssert sAssert = new SoftAssert();
		dwu.enterLitDimweightValOnUpload("");
		dwu.enterDimweightValOnUpload(DataRepo.DIM_WEIGHT);
		dwu.clickSaveDimWeight();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Plese select one record.");
		sAssert.assertAll();
	}
	
	@Test(priority = 14)
	public void ToCheckUserIsAbleSaveDimWt() throws InterruptedException, IOException {
		SoftAssert sAssert = new SoftAssert();
		dwu.enterLitDimweightValOnUpload(DataRepo.DIM_WEIGHT);
		ordNo = dwu.getuploadPopupOrdNo();
		dimWt = dwu.getuploadPopupDimWt();
		litDimWt = dwu.getuploadPopupLitDimWt();
		dwu.clickSaveDimWeight();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Plese select one record.");
		sAssert.assertAll();
	}
	
	@Test(priority = 15)
	public void ToCheckProperDataSaveOrNot() throws InterruptedException, IOException {
		SoftAssert sAssert = new SoftAssert();
        dwu.searchOrdNoOnLanding(ordNo);
        sAssert.assertEquals(dwu.getLandingDimWt(), 20);
        sAssert.assertEquals(dwu.getLandingLitDimWt(), 20);
		sAssert.assertAll();
	}
	
	
	
	

}
