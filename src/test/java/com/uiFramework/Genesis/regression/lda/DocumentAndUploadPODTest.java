package com.uiFramework.Genesis.regression.lda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.LDAPages.DocAndUploadPODPage;
import com.uiFramework.Genesis.LDAPages.LdaQAPage;
import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.NavigationPages;

public class DocumentAndUploadPODTest extends TestBase {

	DocAndUploadPODPage dcp;
	CommonPage cp = null;
	CommonMethods cm;
	NavigationPages np = null;
	JavaScriptHelper js;
	LdaQAPage lp;
	LDAQATest lt;

	@BeforeClass(alwaysRun = true)
	public void befreclass() {
		test = extent.createTest(getClass().getSimpleName());
		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		np = new NavigationPages(driver);
		this.js = new JavaScriptHelper(driver);
		lp = new LdaQAPage(driver);
		lt = new LDAQATest();
		dcp = new DocAndUploadPODPage(driver);
	}

//	@Test(priority = 0)
//	public void createorderForLDA() throws Throwable {
//		SoftAssert sAssert = new SoftAssert();
//		np.endToEndOrder();
//
//	}
//
//	@Test(priority = 1)
//	public void shouldManifestAvailableOnLDAPortal() throws Exception {
//		SoftAssert sAssert = new SoftAssert();
//		js.executeScript("window.open()");
//
//		handles = driver.getWindowHandles();
//		tabs = new ArrayList<>(handles);
//
//		driver.switchTo().window(tabs.get(1));
//
//		driver.get(proObj.getPropertyValueFromFile("ldaportalURL"));
//		cmObj.loginToApplication(driver, proObj.getPropertyValueFromFile("ldausername"),
//				proObj.getPropertyValueFromFile("ldapassword"));
//
//		System.out.println("agent portal open");
//		cp.waitForPopupToDisappear();
//	}

	@Test(priority = 2)
	public void shouldCheckLBOLAvailableToPrintDocument() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		dcp.clickDocumentMenu();
		cp.searchSection();
		System.out.println("LBOL " + np.lbolArray[0]);
		dcp.enterLbolNo(np.lbolArray[0]);
		//dcp.enterLbolNo("4094");
		cp.Search();
		sAssert.assertFalse(cp.checkElementNorecord(), "LBOL Not available on Print Document");
		sAssert.assertAll();
	}

	@Test(priority = 3)
	public void shouldShowErrorMessageIfDocumentNotselect() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		dcp.clickToPrintDocBtn();
		sAssert.assertEquals(cp.captureToastMessage(), "Please select at least one record.", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 4, dependsOnMethods = "shouldCheckLBOLAvailableToPrintDocument")
	public void shouldPrintMultipleDocument() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		dcp.selectDocument();
		cp.deleteExistingFiles();
		dcp.clickToPrintDocBtn();
		sAssert.assertTrue(cp.isFileDownloaded(), "Document not generated from clicking print doc btn");
		sAssert.assertAll();
	}

	@Test(priority = 5, dependsOnMethods = "shouldCheckLBOLAvailableToPrintDocument")
	public void shouldPrintLBOLDocumentOnPortal() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.deleteExistingFiles();
		dcp.clickOnPrintBtn();
		sAssert.assertTrue(cp.isFileDownloaded(), "Document not print On document screen");
		sAssert.assertAll();
	}

	int initialcount;
	@Test(priority = 6, dependsOnMethods = "shouldPrintLBOLDocumentOnPortal")
	public void shouldUploadHCPODSuccessfully() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		dcp.clickHcpodDocMenu();
		initialcount = lp.getTotalEntriesCount(driver);
		dcp.addPDFFile();
		dcp.clickUploadBtn();
		sAssert.assertEquals(cp.captureToastMessage(), "POD uploaded successfully.", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 7, dependsOnMethods = "shouldUploadHCPODSuccessfully")
	public void shouldIncreaseCountByOneAfterPODUpload() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertNotEquals(initialcount, lp.getTotalEntriesCount(driver), "Count not increase by one");
		sAssert.assertAll();
	}

	String podname;
	@Test(priority = 8, dependsOnMethods = "shouldUploadHCPODSuccessfully")
	public void shouldShowUploadedPODForAdmin() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		podname = dcp.getPODName();
		driver.switchTo().window(lt.tabs.get(0));
		dcp.openSplitPODMenu();
		sAssert.assertEquals(podname, dcp.getPODName(), "POD name not match on admin screen.");
		sAssert.assertAll();
	}

	@Test(priority = 9, dependsOnMethods = "shouldUploadHCPODSuccessfully")
	public void shouldViewUploadedPOD() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		driver.switchTo().window(lt.tabs.get(1));
		dcp.viewPOD();
		sAssert.assertAll();
	}

	@Test(priority = 10)
	public void shouldUserDownloadUploadedPOD() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.deleteExistingFiles();
		dcp.downloadPOD();
		sAssert.assertTrue(cp.isFileDownloaded(), "Document not print On document screen");
		sAssert.assertAll();
	}

	@Test(priority = 11, dependsOnMethods = "shouldUploadHCPODSuccessfully")
	public void shouldUserDeleteUploadedPOD() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		dcp.deletePOD();
		sAssert.assertEquals(cp.captureToastMessage(), "HCPOD document deleted successfully", "HCPOD not deleted");
		sAssert.assertAll();
	}
	
	@Test(priority = 12)
	public void shouldRemovedDeletedPODFormAdminPortal() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		driver.switchTo().window(lt.tabs.get(0));
		dcp.searchDocName(podname);;
		sAssert.assertTrue(cp.checkElementNorecord(), "POD should not displayed after deleted.");
		sAssert.assertAll();
	}
	
	@Test(priority = 13)
	public void shouldNotAbleToDeletePODStatusisCompleted() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		driver.switchTo().window(lt.tabs.get(1));
		dcp.clearFilter();
		dcp.searchPodStatus("Completed");
		sAssert.assertTrue(dcp.isDeletePodButtonDisabled(), "Delete button should be Disabled");
		sAssert.assertAll();
	}
	
	@Test(priority = 14)
	public void shouldCheckFilterOnDocumentScreen() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		driver.switchTo().window(lt.tabs.get(1));
		dcp.clickDocumentMenu();
		cp.searchSection();
		cp.clickClearButton();
		cp.waitForLoaderToDisappear();
		List<Integer> indicesToSelect = Arrays.asList(1, 2);
		dcp.selectMultipleDoctypeByIndex(indicesToSelect);
		cp.validateDataInGrid(5);
		List<Integer> indicesToSelect1 = Arrays.asList(22, 2, 52);
		cp.selectMultipleCustomerByIndex(indicesToSelect1);
		cp.validateDataInGrid(6);	
		dcp.searchAndValDocNumber();
		dcp.searchAndValCustOrderNumber();
		cp.clickClearButton();
	}
	
	@Test(priority = 14, enabled = false)
	public void shouldChekCloumnFilterOnDocument() throws Exception {
		lp.checkColoumFilter("DocumentScreen");
	}
	
	@Test(priority = 15, enabled = false)
	public void shouldChekCloumnFilterOnHCPODUpload() throws Exception {
		dcp.clickHcpodDocMenu();
		lp.checkColoumFilter("HCPODupload");
	}
	
}
