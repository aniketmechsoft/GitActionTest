package com.uiFramework.Genesis.regression.lda;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.LDAPages.LdaQAPage;
import com.uiFramework.Genesis.LDAPages.ScheduleDeliveryPage;
import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.NavigationPages;

public class ScheduleDeliveryTest extends TestBase {
	ScheduleDeliveryPage sd;
	CommonPage cp = null;
	CommonMethods cm;
	NavigationPages np = null;
	JavaScriptHelper js;
	LdaQAPage lp;
	LDAQATest lt;
	//Set<String> handles;
	//List<String> tabs;

	@BeforeClass(alwaysRun = true)
	public void befreclass() {
		test = extent.createTest(getClass().getSimpleName());
		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		np = new NavigationPages(driver);
		sd = new ScheduleDeliveryPage(driver);
		this.js = new JavaScriptHelper(driver);
		lp = new LdaQAPage(driver);
		lt = new LDAQATest();

	}

	@Test(priority = 0,enabled=false)
	public void createorderForLDA() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		np.endToEndOrder();

	}

	@Test(priority = 1, enabled=false)
	public void shouldManifestAvailableOnLDAPortal() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		js.executeScript("window.open()");

		lt.handles = driver.getWindowHandles();
		lt.tabs = new ArrayList<>(lt.handles);

		driver.switchTo().window(lt.tabs.get(1));

//		driver.get(proObj.getPropertyValueFromFile("ldaportalURL"));
//		cmObj.loginToApplication(driver, proObj.getPropertyValueFromFile("ldausername"),
//				proObj.getPropertyValueFromFile("ldapassword"));

		System.out.println("agent portal open");
		cp.waitForPopupToDisappear();
	}
	
/**
 * Manual Schedule delivery
 * 
 */

	@Test(priority = 2)
	public void shouldCheckMandatoryMessageOnManualSchedDate() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		sd.clickManualScheDel();
		sd.clickUpdateScheDel();
		sAssert.assertEquals(cp.captureToastMessage(), "Please select at least one record.", "Not Match!.");
		sAssert.assertAll();
	}

	@Test(priority = 3)
	public void shouldCheckLBOLAvailableToShceduleDelivery() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.searchSection();
		System.out.println("lbol " + np.lbolArray[0]);
		 sd.enterLbolNo(np.lbolArray[0]);
		//sd.enterLbolNo("3863");
		cp.Search();
		sAssert.assertFalse(cp.checkElementNorecord(), "LBOL Not available on LDA portal");
		sAssert.assertAll();
	}

	@Test(priority = 4,dependsOnMethods="shouldCheckLBOLAvailableToShceduleDelivery")
	public void shouldScheduleManualDateForLBOL() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		sd.selectManualScheDate();
		sd.clickUpdateScheDel();
		sAssert.assertEquals(cp.captureToastMessage(), "Schedule delivery detaile update successfully.");
		sAssert.assertAll();
	}

	@Test(priority = 5,dependsOnMethods="shouldScheduleManualDateForLBOL")
	public void shouldNotDisplayedLBOlAfterDeliverySchedule() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.Search();
		sAssert.assertTrue(cp.checkElementNorecord(), "LBOL available after schedule date");
		sAssert.assertAll();
	}

	@Test(priority = 6)
	public void shouldScheduleDeliveryForMultiLBOL() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.clickClearButton();
		sd.selectManualScheDate();
		sd.enterDateManually(lp.getCurrentDateTime());
		sd.clickUpdateScheDel();
		sAssert.assertEquals(cp.captureToastMessage(), "Schedule delivery detaile update successfully.");
		sAssert.assertAll();
	}

	@Test(priority = 7)
	public void shouldExportPdfOnLDAQA() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.deleteExistingFiles();
		cp.clickToExpPDF();
		sAssert.assertTrue(cp.isFileDownloaded(), "PDF not download on manual sched. page.");
		sAssert.assertAll();
	}

	@Test(priority = 8)
	public void shouldExportExcelOnLDAQA() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.deleteExistingFiles();
		cp.clickToExpExcel();
		sAssert.assertTrue(cp.isFileDownloaded(), "Excel not download on manual sched. page.");
		sAssert.assertAll();
	}
	
	@Test(priority = 9)
	public void shouldCheckOrderStatusAfterAcceptedManifest() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		driver.switchTo().window(lt.tabs.get(0));
		lp.clearFilter();
		sAssert.assertEquals(lp.getOrderStatus(lp.MGLOrderno), "Consignee Delivery Pending","Not match!" );
	//	sAssert.assertEquals(lp.getOrderStatus("10024"), "Consignee Delivery Schedule Pending","Not match!" );
		sAssert.assertAll();
	}
	
/**
 * Schedule Delivery Page
 */

	@Test(priority = 10)
	public void shouldCheckMandatoryMessageOnSchedDelDate() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		driver.switchTo().window(lt.tabs.get(1));
		sd.clickScheduleDel();
		sd.clickUpdateScheDel();
		sAssert.assertEquals(cp.captureToastMessage(), "Please select at least one record.", "Not Match!.");
		sAssert.assertAll();
	}

	@Test(priority = 11)
	public void shouldAcceptScheduleDeliveryByConsignee() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		sd.clickOnAcceptedBtnIfPresent();
		sAssert.assertEquals(cp.captureToastMessage(), "Schedule accepted", "Not Match!.");
		sAssert.assertAll();
	}

	@Test(priority = 12)
	public void shouldCheckMandatoryMsgForSchedDate() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		sd.selectStatus();
		cp.Search();
		sd.clickUpdateScheDel();
		sAssert.assertEquals(cp.captureToastMessage(), "Please select at least one record.", "Not Match!.");
		sAssert.assertAll();
	}

	@Test(priority = 13)
	public void shouldLBOLAvailableOnScheduleStatus() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		//sd.searchlbol("3863");
		sd.searchlbol(np.lbolArray[0]);
		cp.Search();
		sd.selectLbol();
		sd.clickUpdateScheDel();
		sAssert.assertTrue(
				cp.captureToastMessage().contains("Schedule delivery date & time is required for given LBOL No"),
				"Toast message not matching!");
		sAssert.assertAll();
	}
	
	@Test(priority = 14, dependsOnMethods="shouldLBOLAvailableOnScheduleStatus")
	public void shouldScheduleDeliveryDateByLDA() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		sd.schedDeliveryDates();
		sd.clickUpdateScheDel();
		sAssert.assertEquals(cp.captureToastMessage(), "Schedule delivery successfully","Not Match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 15, dependsOnMethods="shouldScheduleDeliveryDateByLDA")
	public void shouldDisplayedSchedDatebyLDA() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(sd.isDateAvailable(), "Schedule date should not blank");
		sAssert.assertAll();
	}
	
	@Test(priority = 16)
	public void shouldShowErrorInSchedDateDuplicate() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		sd.selectLbol();
		sd.chooseSchedDates();
		sd.clickUpdateScheDel();
		sAssert.assertTrue(cp.captureToastMessage().contains("Duplicate schedule date found against LBOL"),
				"Toast message not matching!");
		sAssert.assertAll();
	}
	
	@Test(priority = 17)
	public void shouldAddNoteForLBOL() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		lp.editManifest();
		sd.addNote(cm.getcharacterString(20));
		cp.save();
		sAssert.assertEquals(cp.captureToastMessage(),"Note Update Successfully.","Not Match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 18,dependsOnMethods="shouldAddNoteForLBOL")
	public void shouldDisplyedNoteOnGrid() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(sd.isNoteDisplayed(), "Added note should not be blank");
		sAssert.assertAll();
	}
	
	@Test(priority = 19)
	public void shouldOpenViewBtnForScheduleHistory() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		sd.clickOnViewBtn();
		sAssert.assertTrue(sd.isSchedHistoryDisplayed(), "Schedule History Pop up not displayed");
		sAssert.assertAll();
	}
	
	@Test(priority = 20 ,enabled= false)
	public void shouldChekCloumnFilterOnSchedDelpage() throws Exception {
		cp.clickClearButton();
		lp.checkColoumFilter("ScheduleDeliveryPage");
	}
	
	@Test(priority = 21 ,enabled= false)
	public void shouldChekCloumnFilterOnManualSchedDelpage() throws Exception {
		sd.clickManualScheDel();
		lp.checkColoumFilter("ManualSchedDeliveryPage");
	}

}
