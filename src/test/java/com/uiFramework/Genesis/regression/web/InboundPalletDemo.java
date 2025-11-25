package com.uiFramework.Genesis.regression.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.json.JSONArray;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.ReadJsonData;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.DataRepo;
import com.uiFramework.Genesis.web.pages.InbTruckPage;
import com.uiFramework.Genesis.web.pages.InboundPalletPage;
import com.uiFramework.Genesis.web.pages.OrderPage;

public class InboundPalletDemo extends TestBase {
	InbTruckPage it = null;
	InboundPalletPage ip = null;
	CommonPage cp = null;
	CommonMethods cm;
	OrderPage op = null;
	int initialCount;
	int afterCount;
	String inboundPallet;
	String GLorder;
	SoftAssert sAssert = new SoftAssert();
	private static final Logger logger = Logger.getLogger(InboundPalletDemo.class.getName());

	@DataProvider
	public Object[][] getData() throws IOException {
		List<HashMap<String, String>> data = cm
				.getJsonData(System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\DynamicData.JSON");

		return new Object[][] { { data.get(0) } };
	}

	@BeforeClass(alwaysRun = true)
	public void befreclass() {
		test = extent.createTest(getClass().getSimpleName());
		cm = new CommonMethods(driver);
		op = new OrderPage(driver);
		ip = new InboundPalletPage(driver);
		cp = new CommonPage(driver);
		it = new InbTruckPage(driver);

	}

	@Test(priority = 1) 
	public void verifyInboundPalletCannotMergeWithDifferentFpick() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.inboundMenu();
		initialCount = ip.getTotalEntriesCount(driver);
		ip.createPallet();
		ip.selectCheckbox();
		ip.searchPickupLocation();
		ip.selectCheckbox();
		ip.mergeBtn();
		sAssert.assertEquals(cp.captureToastMessage(), "Can't merge orders, pickup locations are differe",
				"Not displayed error message, pickup locations are differents.");
		sAssert.assertAll();
	}

	@Test(priority = 2)
	public void shouldPerformMergeWhenButtonClicked() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.clickonContains();
		ip.selectmulcheckBox();
		afterCount = ip.getTotalEntriesCount(driver);
		int diffint = afterCount - initialCount;
		String difference = String.valueOf(diffint);
		sAssert.assertEquals(difference, DataRepo.ONE, "Entry count did not increase by 1.");
		sAssert.assertAll();

	}

	@Test(priority = 3)
	public void verifyRemovedOrdersInEditPallet() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.inboundMenu();
		ip.editBtn();
		ip.verifyPickndDrop();
		inboundPallet = ip.getInboundPallet();
		GLorder = ip.getPalletorder();
		ip.checkRemovedOrders();
		sAssert.assertEquals(cp.captureToastMessage(), "Pallet deleted ",
				"Message not found for auto deleted Pallet");
		sAssert.assertAll();

	}

	@Test(priority = 4, dependsOnMethods="verifyRemovedOrdersInEditPallet")
	public void verifyAutoDeletedPalletAndOrderAvailableToAdd() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.searchData(inboundPallet);
		sAssert.assertTrue(ip.checkElementNorecord(), "'No record found' is Not displayed.” Test Failed");
		ip.createPallet();
		ip.searchData(GLorder);
		sAssert.assertFalse(ip.checkElementNorecord(), "'No record found' is displayed. Test Failed");
		sAssert.assertAll();
	}

	@Test(priority = 5)
	public void verifyMultiPalletCreationAndPaginationOnCheckBoxSelection()
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		ip.inboundMenu();
		initialCount = ip.getTotalEntriesCount(driver);
		ip.createPallet();
		ip.selectMulcheckBoxForCretepallet();
		afterCount = ip.getTotalEntriesCount(driver);
		int diffint = afterCount - initialCount;
		String difference = String.valueOf(diffint);
		sAssert.assertEquals(difference, DataRepo.ONE, "Entry count did not increase by 3.");
		sAssert.assertAll();
	}

	@Test(priority = 6,dependsOnMethods="verifyMultiPalletCreationAndPaginationOnCheckBoxSelection")
	public void verifyOrderAvailableInPalletListing() throws TimeoutException, InterruptedException {
		ip.orderInfo();
	}

	@Test(priority = 7)
	public void verifyAddPalletInEditModeWithPiecesWeightAndNoPopup() throws TimeoutException, InterruptedException {
		ip.inboundMenu();
		ip.editBtn();
		inboundPallet = ip.getInboundPallet();
		ip.checkNoBtnonPopup();
		ip.checkAddedOrdersinPallet();
	}

	@Test(priority = 8)
	public void verifyAddedOrdersNotAvailableInAnotherPallet() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		cp.clickBackBtn();
		ip.createPallet();
		sAssert.assertTrue(ip.searchOrders(), "One or more removed orders are still displayed in the grid.");
		sAssert.assertAll();
	}

	@Test(priority = 9, dataProvider = "getData")
	public void verifyPalletQuantityAndStatus(HashMap<String, String> input)
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.inboundMenu();
		ip.editBtn();
		ip.removedPalletQty(input.get("palletQty"));
		ip.backBtn();
		sAssert.assertEquals(ip.getPalletQty(), input.get("palletQty"),
				"Pallet quantity not same on create and listing");
		sAssert.assertAll();
	}

	@Test(priority = 10)
	public void verifyOrderAvailableAfterPalletDeletion() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.inboundMenu();
		ip.checkDeletedOrderAvailable();
		GLorder = ip.iGLorder();
		ip.deleteBtn();
		sAssert.assertEquals(cp.captureToastMessage(), "Pallet deleted successfully.",
				"Message not found for auto deleted Pallet");
		ip.createPallet();
		ip.search1Order(GLorder);
		sAssert.assertEquals(ip.getGLorder(), GLorder, "Deleted pallet Order Not available to Add");
		sAssert.assertAll();
	}

	@Test(priority = 11)
	public void verifyPalletNotDeletableAfterQA()
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.inboundMenu();
		cp.searchSection();
		ip.selectQAdoneStatus();
		cp.Search();
		ip.searchQAdone(DataRepo.QA_DONE);
		ip.deleteBtnDirect();
		sAssert.assertEquals(cp.captureToastMessage(), "This pallet QA is already done.You can not delete it.",
				"Pallet QA done. Toast not Displayed.");
		sAssert.assertAll();
	}

	@Test(priority = 12)
	public void verifyPalletCannotBeEditedAfterQA()
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.searchQAdone(DataRepo.QA_DONE);
		ip.editBtn();
		sAssert.assertFalse(ip.verifyRemovePalleBtnDisable().isEnabled(), "Remove order btn is Editabel when QA done.");
		sAssert.assertFalse(ip.verifyAddPalleBtnDisable().isEnabled(), "Add order btn is Editabel when QA done.");
		sAssert.assertAll();
	}

	@Test(priority = 13, dataProvider = "getData",groups = "Smoke", timeOut = 20000, enabled=true)
	public void verifyFiltersOnInboundPallet(HashMap<String, String> input) throws InterruptedException, TimeoutException {
		ip.inboundMenu();
		cp.searchSection();
		cp.clickClearButton();
		String jsonPath = System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\xpath.JSON";

		List<Integer> indicesToSelect = Arrays.asList(55, 2, 3);
		cp.selectMultiplePickuprByIndex(indicesToSelect);
		cp.validateDataInGrid(17);

		List<Integer> indicesToSelect1 = Arrays.asList(2, 1, 3);
		cp.selectMultipleDropByIndex(indicesToSelect1);
		cp.validateDataInGrid(18);
		List<Integer> indicesToSelect2 = Arrays.asList(4, 3);
		ip.selectMultiplestatusByIndex(indicesToSelect2);
		cp.validateDataInGrid(15);
		ip.searchAndValidateTruckNo();
		ip.searchAndValidatePalletNo();
		Map<String, String> customerOrderMap = CommonMethods.getXPathSet("inboundCustomerOrderNo", jsonPath);
		cp.extractInputFromIconSearchVerify(customerOrderMap);
		Map<String, String> inboundOrderNomap = CommonMethods.getXPathSet("inboundOrderNo", jsonPath);
		cp.extractInputFromIconSearchVerify(inboundOrderNomap);
		cp.searchAndValidateDateByColumn(10);
		cp.searchAndValidateToDateByColumn(10);
		cp.pickFrom();

	}

	@Test(priority = 14,groups = "Smoke")
	public void verifyPaginationOnInboundPalletListing() throws TimeoutException, InterruptedException {
		ip.inboundMenu();
		ip.paginationOnListing();

	}

	@Test(priority = 15)
	public void verifyPaginationOnCreatePallet() throws TimeoutException, InterruptedException {
		ip.inboundMenu();
		ip.createPallet();
		ip.paginationOnCeatePallet();
	}

	@Test(priority = 16, groups = "Smoke", timeOut = 20000)
	public void verifyColumnfiltersFunctionOnInboundListing() throws InterruptedException, IOException, TimeoutException {
		ip.inboundMenu();
		ip.checkColoumFilter();
	}

	@Test(priority = 17, groups = "Smoke",timeOut = 20000)
	public void verifyColumnfiltersonCreateInboundPallet() throws InterruptedException, IOException, TimeoutException {
		ip.inboundMenu();
		ip.createPallet();
		ip.checkColoumFilterCreatePage();
	}

	@AfterClass()
	public void checkOrderStatusAfterorderLoad() {
		SoftAssert sAssert = new SoftAssert();
//		cp.orderpageMenulanding();
//		sAssert.assertEquals(it.getOrderStatus(op.getMGLOrderno()), ReadJsonData.getNestedValue("ILoadPending", "expected"),
//				ReadJsonData.getNestedValue("ILoadPending", "message"));
		sAssert.assertAll();
	}

}
