package com.uiFramework.Genesis.regression.lda;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.LDAPages.DriverPage;
import com.uiFramework.Genesis.LDAPages.LdaQAPage;
import com.uiFramework.Genesis.LDAPages.ScheduleDeliveryPage;
import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.NavigationPages;

public class DriverTest extends TestBase {

	DriverPage dp;
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
		this.js = new JavaScriptHelper(driver);
		lp = new LdaQAPage(driver);
		lt = new LDAQATest();
		dp = new DriverPage(driver);

	}

	@Test(priority = 0,enabled=false)
	public void createorderForLDA() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		np.endToEndOrder();

	}

	@Test(priority = 1,enabled=false)
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

	String username;

	@Test(priority = 2)
	public void shouldCheckMandatoryFieldForCreateDriver() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		dp.clickDriverMenu();
		username = dp.getUsername();
		dp.clickAddDriver();
		cp.save();
		sAssert.assertEquals(dp.getMandFieldCount(), 8, "Mandatory field count not match save btn.");
		sAssert.assertAll();
	}

	@Test(priority = 3)
	public void shouldCheckClearBtnWork() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.clickClearButton();
		sAssert.assertEquals(dp.getMandFieldCount(), 0, "clear button not working on creation of driver.");
		sAssert.assertAll();
	}

	@Test(priority = 4)
	public void shouldCheckUsernameNotDuplicate() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		dp.enterUsername(username);
		dp.enterMandatoryFields("Phonix");
		cp.save();
		sAssert.assertEquals(cp.captureToastMessage(), "Username already exist", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 5)
	public void shouldUploadDriverPhoto() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		dp.uploadDriverPhoto();
		sAssert.assertTrue(dp.isRemoveButtonDisplayed(), "Driver photo should be Uploaded.");
		sAssert.assertAll();
	}

	@Test(priority = 6)
	public void shouldCreateDiverSuccessfully() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		dp.enterUsername(cm.getcharacterString(9));
		dp.enterNonMandateFiled();
		cp.save();
		sAssert.assertEquals(cp.captureToastMessage(), "Driver save successfully", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 7)
	public void shouldResetPassowordSuccessfully() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		lp.editBtn();
		username = dp.getPassword();
		dp.clickOnResetBtn();
		sAssert.assertEquals(cp.captureToastMessage(), "Password reset successfully.", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 8, dependsOnMethods = "shouldResetPassowordSuccessfully")
	public void shouldPassowordChangedSuccessfully() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertNotEquals(username, dp.getPassword(), "Password not changed after reset");
		sAssert.assertAll();
	}

	@Test(priority = 9)
	public void shouldRemovedDiverPhoto() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		dp.clickOnRemovedBtn();
		sAssert.assertFalse(dp.isRemoveButtonDisplayed(), "Driver photo should be removed.");
		sAssert.assertAll();
	}

	@Test(priority = 10)
	public void shouldResetPasswordOnListing() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.clickBackBtn();
		dp.clickOnResetBtnOnListing();
		sAssert.assertEquals(cp.captureToastMessage(), "Password reset successfully.", "Not match!");
		sAssert.assertAll();
	}

/**
 * LBOL-Assigned driver page
 */
	int count;
	@Test(priority = 11)
	public void shouldAvailabletoAssigneDriverForLBOL() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		cp.searchSection();
		cp.clickClearButton();
		count = lp.getTotalEntriesCount(driver);
		dp.LbolAssignmentMenu();
		dp.selectDriverDrpDwn();
		sAssert.assertEquals(count, dp.getDriver(), "Created Driver not show to Assigned");
		sAssert.assertAll();
	}

	@Test(priority = 12, dependsOnMethods = "shouldAvailabletoAssigneDriverForLBOL")
	public void shouldAssignedDriverToLBOL() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		dp.selectDriver();
		username = dp.getLBOL();
		dp.clickToAssignDriver();
		sAssert.assertEquals(cp.captureToastMessage(), "Driver details save successfully.",
				"Should be Displayed Toast After Assigne Driver.");
		sAssert.assertAll();
	}
	
	@Test(priority = 13, dependsOnMethods = "shouldAssignedDriverToLBOL")
	public void shouldUpdateLBOLStatusAsAssigned() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.searchSection();
		dp.selectAssignStatus();
		dp.enterlbol(username);
		cp.Search();
		sAssert.assertFalse(cp.checkElementNorecord(), "Driver Assigned LBOL not Found.");
		sAssert.assertAll();
	}
	
	@Test(priority = 14, dependsOnMethods = "shouldAssignedDriverToLBOL")
	public void shouldAssigneDriverForMultipleLBOL() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.clickClearButton();
		dp.assigneDriverForMultiLBOL();
		dp.clickToAssignDriver();
		sAssert.assertEquals(cp.captureToastMessage(), "Driver details save successfully.",
				"Should be Displayed Toast After Assign Driver for multi lbol");
		sAssert.assertAll();
	}
	
	@Test(priority = 15)
	public void shouldExportPdfOnDriverAssignPage() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.deleteExistingFiles();
		cp.clickToExpPDF();
		sAssert.assertTrue(cp.isFileDownloaded(), "PDF not download on driver assign page.");
		sAssert.assertAll();
	}
	
	@Test(priority = 16)
	public void shouldExportExcelOnDriverAssignPage() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.deleteExistingFiles();
		cp.clickToExpExcel();
		sAssert.assertTrue(cp.isFileDownloaded(), "Excel not download on driver assign page.");
		sAssert.assertAll();
	}
	
	@Test(priority = 17,enabled= true)
	public void shouldChekCloumnFilterOnDriverAssignePage() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.clickClearButton();
		lp.checkColoumFilter("DriverAssignment");
	}
	
	@Test(priority = 18)
	public void shouldUserInactiveDriver() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		dp.clickDriverMenu();
		count = lp.getTotalEntriesCount(driver);
		lp.editBtn();
		dp.inactiveDriver();
		cp.save();
		sAssert.assertEquals(cp.captureToastMessage(), "Driver save successfully", "Not match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 19)
	public void shouldNotMatchDriverCountAfterDriverInactive() throws Exception {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForLoaderToDisappear();
		count = lp.getTotalEntriesCount(driver);
		sAssert.assertNotEquals(count, lp.getTotalEntriesCount(driver) , "Count Should decrese after driver inactive");
		sAssert.assertAll();
	}
	
	@Test(priority = 20, enabled= false)
	public void shouldChekCloumnFilterOnCreateDriver() throws Exception {
		lp.checkColoumFilter("CreateDriver");
	}
	
}
