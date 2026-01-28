package com.uiFramework.Genesis.regression.web;

import java.util.concurrent.TimeoutException;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.DocQueue;
import com.uiFramework.Genesis.web.pages.productionPage;

public class DocQueueTest extends TestBase {
	DocQueue dc = null;
	CommonPage cp = null;
	CommonMethods cm;
	productionPage pr=null;
	int initialCount;
	int afterCount;

	@BeforeClass(alwaysRun = true)
	public void befreclass() {
	//	test = extent.createTest(getClass().getSimpleName());
		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		dc = new DocQueue(driver);
		pr = new productionPage(driver);
	//	driver.navigate().refresh();

	}

	@Test(priority = 3,alwaysRun = true, groups = {"Smoke"})
	public void shouldShowErrorMessageIfRequiredFieldNotSelected() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		dc.docQueueMenu();
		dc.clickLDABtn();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Plese select one record.");
		sAssert.assertAll();
	}

	@Test(priority = 4)
	public void shouldDisplayRegularRequestsOnRegularCheckbox() throws InterruptedException {
		dc.checkRegularCheckboxBehaviour("Regular");

	}

	@Test(priority = 5)
	public void shouldDisplayReconsignmentRequestsOnReconsignmentCheckbox() throws InterruptedException {
		dc.checkReconsignmentCheckboxBehaviour("Reconsignment");

	}

	@Test(priority = 6)
	public void shouldDisplayCloseoutRequestsOnCloseoutCheckbox() throws InterruptedException {
		dc.checkCloseoutCheckboxBehaviour("Closeout");

	}

	@Test(priority = 7, alwaysRun = true)
	public void shouldCreateDifferentLBOLsForDifferentConsignees() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		     //dc.docQueueMenu();
		dc.clickGeneratedDoc();
		initialCount = dc.ExtrNoCntFrmGenertrPge(driver);
		dc.ClickDocGen();
		dc.createLDAForDiffConsignee();
		dc.clickLDABtn();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Documents have been created for the selected record.");
		sAssert.assertAll();
	}

	@Test(priority = 8, dependsOnMethods = "shouldCreateDifferentLBOLsForDifferentConsignees")
	public void shouldConfirmDifferentRequestsCreatedForDifferentConsignees() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		dc.clickGeneratedDoc();
		afterCount = dc.ExtrNoCntFrmGenertrPge(driver);
		sAssert.assertEquals(initialCount + 3, afterCount, "");
		sAssert.assertAll();
	}

	@Test(priority = 9, alwaysRun = true)
	public void shouldCreateDifferentManifestsForDifferentLDAs() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		dc.clickGeneratedDoc();
		initialCount = dc.ExtrNoCntFrmGenertrPge(driver);
		dc.ClickDocGen();
		dc.createManifestForDiffLDA();
		dc.clickLDABtn();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Documents have been created for the selected record.");
		sAssert.assertAll();
	}

	@Test(priority = 10, dependsOnMethods = "shouldCreateDifferentManifestsForDifferentLDAs")
	public void shouldConfirmDifferentRequestsCreatedForDifferentLDAs() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		dc.clickGeneratedDoc();
		afterCount = dc.ExtrNoCntFrmGenertrPge(driver);
		sAssert.assertNotEquals(initialCount, afterCount, "");
		sAssert.assertAll();
	}

	@Test(priority = 11, alwaysRun = true)
	public void shouldCreateDifferentOrderProcessesForSameLDA() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		   //dc.docQueueMenu();
		initialCount =dc.ExtrNoCntFrmGenertrPge(driver);
		dc.ClickDocGen();
		dc.clickReconsinmntBtn();
		dc.checkDiffTypeOrderMerge();
		dc.clickLDABtn();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Documents have been created for the selected record.");
		sAssert.assertAll();
	}

	@Test(priority = 12, dependsOnMethods = "shouldCreateDifferentOrderProcessesForSameLDA")
	public void shouldConfirmSameRequestCreatedForDifferentLDAs() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		dc.clickGeneratedDoc();
		afterCount = dc.ExtrNoCntFrmGenertrPge(driver);
		sAssert.assertEquals(initialCount + 1, afterCount, "");
		sAssert.assertAll();
	}

	@Test(priority = 13, alwaysRun = true)
	public void shouldCreateDifferentOrderProcessesForDifferentLDAs() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		initialCount = dc.ExtrNoCntFrmGenertrPge(driver);
		dc.ClickDocGen();
		dc.clickReconsinmntBtn();
		dc.clickCloseoutBtn();
		dc.checkDiffTypeOrderMergeDiffLDA();
		dc.clickLDABtn();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Documents have been created for the selected record.");
		sAssert.assertAll();
	}

	@Test(priority = 14, dependsOnMethods = "shouldCreateDifferentOrderProcessesForDifferentLDAs")
	public void shouldConfirmDiffRequestsCreatedForDifferentLDAs() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		dc.clickGeneratedDoc();
		afterCount = dc.ExtrNoCntFrmGenertrPge(driver);
		sAssert.assertEquals(initialCount + 2, afterCount, "");
		sAssert.assertAll();
	}
	
	@Test(priority = 15, alwaysRun = true)
	public void shouldCreateDocumentsForMainOrder() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		dc.ClickDocGen();
		dc.searchOrder();
		dc.checkAndSelectOrder();
		dc.clickLDABtn();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Documents have been created for the selected record.");
		sAssert.assertAll();
	}
	
	@Test(priority = 16,alwaysRun = true, groups = {"Smoke"})
	public void shouldRetrieveDocumentsForMainOrder() throws TimeoutException, InterruptedException {
		SoftAssert sAssert1 = new SoftAssert();
		dc.clickGeneratedDoc();
		dc.searchOrderInGenScreen();
		dc.getDocumentNumber();
		Assert.assertFalse(dc.checkElementNorecord(), "'No record found' is displayed. Document not generated.");
	}
	
	//Split POD
	@Test(priority = 17, alwaysRun = true, groups = {"Smoke"})
	public void shouldCheckColumnFilterOnSplitPOD() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		//dc.docQueueMenu();
		cp.ClickSplitPod();
		pr.clickToRefresh();
		dc.verifyColumnFilterForSplitePOD();
		sAssert.assertAll();
	}
	
	@Test(priority = 18, alwaysRun = true, dependsOnMethods="shouldCheckColumnFilterOnSplitPOD")
	public void shouldPersistColumnFilterOnSplitPOD() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		dc.clickToViewPod();
		cp.clickBackBtn();
		initialCount = dc.ExtrNoCntFrmGenertrPge(driver);
		sAssert.assertTrue(dc.getFilterData(),"Filter data is null or empty");
		sAssert.assertAll();
	}

}
