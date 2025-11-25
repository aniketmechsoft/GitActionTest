package com.uiFramework.Genesis.regression.lda;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.LDAPages.LdaQAPage;
import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.NavigationPages;

public class LDAQATest extends TestBase {
	LdaQAPage lp;
	CommonPage cp = null;
	CommonMethods cm;
	NavigationPages np = null;
	JavaScriptHelper js;
	public static Set<String> handles;
	public static List<String> tabs;

	@BeforeClass(alwaysRun = true)
	public void befreclass() {
		test = extent.createTest(getClass().getSimpleName());
		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		np = new NavigationPages(driver);
		lp = new LdaQAPage(driver);
		this.js = new JavaScriptHelper(driver);

	}

	@Test(priority = 1)
	public void createorderForLDA() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		np.endToEndOrder();
		
	}

	@Test(priority = 2)
	public void shouldNotDisplayedManifestBeforeTrackingUpdate() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		js.executeScript("window.open()");

		handles = driver.getWindowHandles();
		tabs = new ArrayList<>(handles);

		driver.switchTo().window(tabs.get(1));

//		driver.get(proObj.getPropertyValueFromFile("ldaportalURL"));
//		cmObj.loginToApplication(driver, proObj.getPropertyValueFromFile("ldausername"),
//				proObj.getPropertyValueFromFile("ldapassword"));

		System.out.println("agent portal open");
		cp.waitForPopupToDisappear();
		cp.searchSection();
	    //lp.selectStatus("Pending");
		System.out.println("manifest " + np.manifestArray[0]);
		System.out.println("Lbol" +  np.lbolArray[0]);
		lp.searchManifest(np.manifestArray[0]);
		//lp.searchManifest("3536");
		cp.Search();
		sAssert.assertTrue(cp.checkElementNorecord(), "Manifest should Not available on LDA portal, before tracking update");
		sAssert.assertAll();
	}
	
	@Test(priority = 3)
	public void shouldManifestAvailableOnLDAPortal() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		driver.switchTo().window(tabs.get(0));
		np.UpdateTrackingFromOrder();
		driver.switchTo().window(tabs.get(1));
		cp.Search();
		sAssert.assertFalse(cp.checkElementNorecord(), "Manifest should be available on LDA portal.");
		sAssert.assertAll();
	}
	
	@Test(priority = 4, dependsOnMethods = "shouldManifestAvailableOnLDAPortal")
	public void shouldDisplayedTrackingNoAndETA() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(lp.isTrackingNoDisplayed(), "Tracking number No show for Manifest");
		sAssert.assertAll();
	}

	@Test(priority = 5, dependsOnMethods = "shouldManifestAvailableOnLDAPortal")
	public void shouldDisplayedTotalPiecesSumCorretly() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		lp.editManifest();
		sAssert.assertEquals(lp.getTotalPieces(), lp.getTotalPiecesSum(), "Total Pieces sum not match on LDA QA.");
		sAssert.assertAll();
	}

	@Test(priority = 6)
	public void shouldDisplayedErrorIfNotEnterPieces() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		lp.clickOnUpdatePieces();
		sAssert.assertEquals(cp.captureToastMessage(), "Please enter received pieces & received literature pieces.",
				"Not Match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 7)
	public void shouldWorkMarkRecQtySameAsOrdQtyBtn() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		lp.clickMarkAsRecPiecesBtn();
		sAssert.assertEquals(lp.getOrdPieces(),lp.getEnetrPieces() , "Received Order Pieces not match.");
		sAssert.assertEquals(lp.getLitOrdPieces(),lp.getEnetrLitPieces() , "Received Order Pieces not match.");
		sAssert.assertAll();
	}
	
	@Test(priority = 8)
	public void shouldAcceptManifestFrmLDA() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		lp.clickOnUpdatePieces();
		sAssert.assertEquals(cp.captureToastMessage(),"QA details update successfully", "Not match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 9)
	public void shouldCheckManifestStatusShouldQAPassed() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		//cp.searchSection();
		//cp.clickClearButton();
		sAssert.assertEquals(lp.getQAStatus(),"QA Passed", "Manifest Status Not match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 10)
	public void shouldCheckOrderStatusAfterAcceptedManifest() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		driver.switchTo().window(tabs.get(0));
		lp.navigateOrderMenu();
		sAssert.assertEquals(lp.getOrderStatus(lp.MGLOrderno), "Consignee Delivery Schedule Pending","Not match!" );
	//	sAssert.assertEquals(lp.getOrderStatus("10024"), "Consignee Delivery Schedule Pending","Not match!" );
		sAssert.assertAll();
	}
	
	@Test(priority = 11)
	public void shouldEnterMorePiecesThanAvailable() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		driver.switchTo().window(tabs.get(1));
		cp.searchSection();
		cp.clickClearButton();
		lp.selectStatus("Pending");
		cp.Search();
		lp.editPendingManifest();
		lp.clickMarkAsRecPiecesBtn();
		lp.enterActualPieces(lp.getMoreOrLessPiece(1));
		lp.enterLitPieces(lp.getMoreOrLessLitPiece(1));
		lp.clickOnUpdatePieces();
		sAssert.assertTrue(lp.isPopDisplayed(), "POP up not displayed for Mismatch Pieces." );
		sAssert.assertAll();
	}
	
	@Test(priority = 12)
	public void shouldCheckReceivedDateMandatory() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		lp.clickOnNo();
		lp.clearDates();
		lp.clickOnUpdatePieces();
		sAssert.assertEquals(cp.captureToastMessage(),"Received Date is required.","Toast message not Displyed for when Received date blank.");
		sAssert.assertAll();
	}

	@Test(priority = 13)
	public void shouldCheckVoiceMailDateMandatory() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		lp.ReceivedDateSelection();
		lp.clickOnUpdatePieces();
		sAssert.assertEquals(cp.captureToastMessage(),"Voice Mail Date is required.", "Toast message not Displyed for when voice mail date blank.");
		sAssert.assertAll();
	}

	String Manifest;
	@Test(priority = 14)
	public void shouldEnterLessPiecesThanAvailable() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		lp.voiceMailDateSelection(lp.getCurrentDateTime());
		lp.clickMarkAsRecPiecesBtn();
		Manifest=lp.getManifestFromQA();
		lp.enterActualPieces(lp.getMoreOrLessPiece(-2));
		lp.enterLitPieces(lp.getMoreOrLessLitPiece(-1));
		lp.clickOnUpdatePieces();
		lp.clickOnYes();
		sAssert.assertEquals(cp.captureToastMessage(),"QA details update successfully", "Not match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 15)
	public void shouldCheckManifestAvailableInGrid() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.searchSection();
		cp.clickClearButton();
		lp.searchOrd(Manifest);
		sAssert.assertFalse(cp.checkElementNorecord(), "Manifest Not available on LDA portal");
		sAssert.assertAll();
	}
	
	@Test(priority = 16)//shouldCheckManifestAvailableInGrid
	public void shouldManifestStatusQAPending() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(lp.getQAStatus(), "QA Pending", "Manifets Status Should be QA pending.");
		sAssert.assertAll();
	}
	
	@Test(priority = 17)//shouldManifestStatusQAPending
	public void shouldNotEditabelManifestToLDA() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		lp.editManifest();
		sAssert.assertFalse(lp.isUpdatePiecsBtnDisable().isEnabled(), "Update btn should bedisable in complete status.");
		sAssert.assertAll();
	}
	
	@Test(priority = 18)//shouldManifestStatusQAPending
	public void shouldExportPdfOnLDAQA() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.clickBackBtn();
		cp.deleteExistingFiles();
		cp.clickToExpPDF();
		sAssert.assertTrue(cp.isFileDownloaded(), "PDF not download on LDA QA page.");
		sAssert.assertAll();
	}
	
	@Test(priority = 19)
	public void shouldExportExcelOnLDAQA() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.deleteExistingFiles();
		cp.clickToExpExcel();
		sAssert.assertTrue(cp.isFileDownloaded(), "Excel not download on LDA QA page.");
		sAssert.assertAll();
	}
	
	@Test(priority = 20, enabled=false)
	public void shouldChekCloumnFilterOnLDAQApage() throws Exception {
		cp.clickClearButton();
		lp.checkColoumFilter("LdaQaPage");
	}
	
}
