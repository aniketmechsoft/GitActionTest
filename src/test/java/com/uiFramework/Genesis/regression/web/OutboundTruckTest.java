package com.uiFramework.Genesis.regression.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.DataRepo;
import com.uiFramework.Genesis.web.pages.OutboundPalletPage;
import com.uiFramework.Genesis.web.pages.OutboundTruckPage;

public class OutboundTruckTest extends TestBase {
	OutboundTruckPage obt = null;
	OutboundPalletPage opp = null;
	CommonPage cp = null;
	CommonMethods cm;
	int initialCount;
	int afterCount;
	String OBPL;
	String glorder;
	SoftAssert sAssert = new SoftAssert();

	private static final Logger logger = Logger.getLogger(OutboundTruckTest.class.getName());

	@DataProvider
	public Object[][] getData() throws IOException {
		List<HashMap<String, String>> data = cm
				.getJsonData(System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\DynamicData.JSON");

		return new Object[][] { { data.get(0) } };
	}

	@BeforeClass(alwaysRun = true)
	public void befreclass() {
	//	test = extent.createTest(getClass().getSimpleName());
		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		opp = new OutboundPalletPage(driver);
		obt = new OutboundTruckPage(driver);
	//	driver.navigate().refresh();

	}

	@Test(priority = 1, alwaysRun = true, groups = { "Smoke" })
	public void shouldPalletAvailableToCreateOutboundTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		obt.truckMenu();
		obt.createTruck();
		obt.searchPallet();
		sAssert.assertEquals(obt.getMOBPLCreatingTrcuk(), opp.MOBPL,
				"Main outbound Pallet not available to Create Truck.");
		sAssert.assertAll();
	}

	@Test(priority = 2, alwaysRun = true, groups = { "Smoke" })
	public void shouldDisplayToastMessageOnCreateObtTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		obt.selectLTL(DataRepo.VALUE, "Resopnse number-102912 and Instructions");
		obt.searchPallet();
		obt.selectCheckbox();
		obt.saveObtTruck();
		sAssert.assertEquals(obt.TruckSavemessage(), "Truck created successfully.", "Outbound truck toast not found.");
		obt.getMobtTruckNo();
		sAssert.assertAll();
	}

	@Test(priority = 3)
	public void shouldDisplayDefaultCostEstimateAndOrderProcess() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		obt.truckMenu();
		obt.createTruck();
		sAssert.assertEquals(obt.getCosEstimate(), DataRepo.DEFAULT_COST, "cost Estimate mismatch:");
		obt.checkOrderProcessForOutbound(2);
		sAssert.assertAll();
	}

	@Test(priority = 4)
	public void shouldAllowEditingDropLocationDetailsInNonUPSCase() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		obt.selectLTL(DataRepo.VALUE, "Resopnse number-102912 and Instructions");
		obt.selectCheckbox();
		sAssert.assertTrue(obt.checkAddressfield(),
				"Field 'Enter Address 1' is NOT editable.Confirm that LTL multidrop.?");
		sAssert.assertAll();

	}

	@Test(priority = 5)
	public void shouldValidateMultiDropForNonUPSCase() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		obt.selectDifferentLDA();
		obt.selectCheckbox();
		obt.saveTruck();
		obt.saveTruck();
		sAssert.assertEquals(cp.captureToastMessage(), "Multiple LDA selections are not allowed for the selected LTL",
				"Toast message not displyed for multi LDA selection");
		sAssert.assertAll();
	}

	@Test(priority = 6, dependsOnMethods = "shouldValidateMultiDropForNonUPSCase")
	public void shouldClearFieldsWhenClearButtonClickedOnObtCreateTruck() {
		SoftAssert sAssert = new SoftAssert();
		cp.clickClearButton();
		sAssert.assertTrue(obt.isDropdownCleared(), "Dropdown is not cleared- Clear btn not working.");
		sAssert.assertAll();
	}

	@Test(priority = 7, alwaysRun = true, groups = { "Smoke" })
	public void shouldCreateOutboundTruckAndVerifyTotalPiecesAndPalletQtyPerPalletAdded() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		obt.truckMenu();
		initialCount = obt.getTotalEntriesCount(driver);
		obt.createTruck();
		obt.selectLTLforUPScase(DataRepo.VALUE);
		obt.selectNonMandatefields("Resopnse number-102912 and Instructions");
		obt.creatTruckwithCheckTotalPiecesandpallet();
		obt.saveTruck();
		obt.verifyRouteOrderShouldbeEditable();
		afterCount = obt.getTotalEntriesCount(driver);
		int diffint = afterCount - initialCount;
		String difference = String.valueOf(diffint);
		sAssert.assertEquals(difference, DataRepo.ONE, "Entry count did not increase for Truck by 1.");
		sAssert.assertAll();
	}

	@Test(priority = 8, dependsOnMethods = "shouldCreateOutboundTruckAndVerifyTotalPiecesAndPalletQtyPerPalletAdded", alwaysRun = true, groups = {
			"Smoke" })
	public void shouldUpdateTruckStatusAfterTruckCreation() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(obt.getObtTruckNondStatus(), DataRepo.LOAD_STATUS, "Status not Match for outbound truck");
		sAssert.assertAll();
	}

	@Test(priority = 9, dependsOnMethods = "shouldCreateOutboundTruckAndVerifyTotalPiecesAndPalletQtyPerPalletAdded")
	public void shouldVerifyCreatedTruckDetailsAndPalletQtyMatch() {
		SoftAssert sAssert = new SoftAssert();
		obt.verifyTruckDetailsandPalletOrder();
		sAssert.assertAll();
	}

	@Test(priority = 10)
	public void shouldDisplayLDACodesCorrectlyOnListing() {
		SoftAssert sAssert = new SoftAssert();
		for (String code : obt.LDAcodesOnlist) {
			sAssert.assertTrue(obt.LDAcodes.contains(code), "Missing LDA code in list: " + code);
			sAssert.assertAll();
		}
	}

//	@Test(priority = 11)
//	public void shouldDisplayTruckDetailsInEditOutboundTruckMode() throws TimeoutException, InterruptedException {
//		obt.truckMenu();
//		obt.editBtn();
//		obt.validateTruckCreatedDatainEditTruck();
//	}
//
//	@Test(priority = 12)
//	public void shouldAllowEditingLTL3rdPartyAndCostEstimate() {
//		SoftAssert sAssert = new SoftAssert();
//		sAssert.assertTrue(obt.checkLTL(), "FAIL: LTL should be editable but it is readonly or disabled.");
//		sAssert.assertTrue(obt.checkCostEstimateEditable(),
//				"FAIL: Cost Estimate field should be editable but it's disabled or readonly.");
//		sAssert.assertTrue(obt.check3rdPartyBilling(), "Third Party should be editable");
//		sAssert.assertAll();
//	}
//
//	@Test(priority = 13, dependsOnMethods = "shouldAllowEditingLTL3rdPartyAndCostEstimate")
//	public void shouldNotAllowMultiDropLocationForNonUPS() {
//		SoftAssert sAssert = new SoftAssert();
//		sAssert.assertEquals(obt.checkLDAaddress(), "Multiple drops are not allowed for this LTL.",
//				"Multi drop not allowed toast not displayed.");
//		sAssert.assertAll();
//	}
//
//	@Test(priority = 14,alwaysRun = true, groups = {"Smoke"})
//	public void shouldRemovePalletFromTruckAndUpdateWeightAndEmptyTruckStatus()
//			throws TimeoutException, InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		obt.truckMenu();
//		obt.editBtn();
//		obt.checkPiecesanWeightWhenPalletRemoved();
//		sAssert.assertEquals(obt.getTruckEditStatus(), DataRepo.STATUS,
//				"Truck status not Match for outbound truck when removed all pallets ");
//		sAssert.assertAll();
//	}
//
//	@Test(priority = 15, dependsOnMethods = "shouldRemovePalletFromTruckAndUpdateWeightAndEmptyTruckStatus")
//	public void shouldVerifyOrderProcessDuringOutboundTruckEdit() throws InterruptedException {
//		obt.checkOrderProcessForOutbound(3);
//	}
//
//	@Test(priority = 16, dependsOnMethods = "shouldVerifyOrderProcessDuringOutboundTruckEdit")
//	public void shouldRemovedPalletAvailableToAddInOuboundCreateTruck() throws TimeoutException, InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickBackBtn();
//		obt.createTruck();
//		OBPL = obt.getOBPL();
//		obt.selectLTLforUPScase(DataRepo.LIT_PIECES);
//		obt.searchRemovedPallet(OBPL);
//		sAssert.assertFalse(obt.checkElementNorecord(), "'No record found' is displayed.” Test Failed");
//		sAssert.assertAll();
//	}
//
//	@Test(priority = 17,alwaysRun = true, groups = {"Smoke"})
//	public void shouldAddPalletInEditModeWithCalculationsAndShouldUpdateStatus()
//			throws TimeoutException, InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		obt.truckMenu();
//		obt.editBtn();
//		obt.checkAddpalletFunctioninObtTruck();
//		sAssert.assertEquals(obt.getTruckEditStatus(), DataRepo.LOAD_STATUS,
//				"Outbound Truck status not Match after Adding pallets");
//		sAssert.assertAll();
//	}
//
//	@Test(priority = 18)
//	public void shouldNotAllowAddedPalletToCreateOutboundTruck() throws TimeoutException, InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickBackBtn();
//		obt.createTruck();
//		obt.selectLTLforUPScase(DataRepo.LIT_PIECES);
//		sAssert.assertTrue(obt.searchRemovedPalletsShowNoRecord(), "'No record found' not shown for some pallets");
//		sAssert.assertAll();
//	}
//
//	@Test(priority = 19)
//	public void shouldVerifyCostEstimateOnEditingOutboundTruck() throws TimeoutException, InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		obt.truckMenu();
//		obt.editBtn();
//		obt.updateCostestimate();
//		sAssert.assertEquals(cp.captureToastMessage(), "Cost Estimate Update Successfully",
//				"cost estimated toast not found.");
//		sAssert.assertEquals(obt.getEstimatedCost(), "10.00", "Cost not Updated fot outbound truck");
//		sAssert.assertAll();
//	}
//
//	@Test(priority = 20, dependsOnMethods = "shouldVerifyCostEstimateOnEditingOutboundTruck")
//	public void shouldUpdateRoutesInEditOutboundTruck() throws TimeoutException, InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		obt.clickonUpdateBtn();
//		sAssert.assertTrue(obt.isRouteOrderEditable(), "Route Order input field is not editable.");
//		cp.save();
//		sAssert.assertTrue(cp.toastMsgReceivedSuccessfully(), "Route order Toast not Displyed.");
//		sAssert.assertAll();
//	}
//
//	@Test(priority = 21)
//	public void shouldDeleteTruckAndMakeOrdersAvailableToAdd() throws TimeoutException, InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		obt.truckMenu();
//		String Trcukno = obt.getObtTruckNo();
//		obt.getDeletingPallet();
//		obt.deleteBtn();
//		obt.searchTruckno1(Trcukno);
//		sAssert.assertTrue(obt.checkElementNorecord(), "'No record found' is Not displayed.” Test Failed");
//		obt.createTruck();
//		OBPL = obt.getOBPL();
//		obt.selectLTLforUPScase(DataRepo.LIT_PIECES);
//		obt.searchRemovedPallet(OBPL);
//		sAssert.assertFalse(obt.checkElementNorecord(), "'No record found' is displayed .” Test Failed");
//		sAssert.assertAll();
//	}
//
//	@Test(priority = 22)
//	public void shouldCreateEmptyOutboundTruck() throws TimeoutException, InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		obt.truckMenu();
//		obt.createTruck();
//		obt.selectLTL(DataRepo.VALUE, "Resopnse number-102912 and Instructions");
//		obt.saveObtTruck();
//		sAssert.assertEquals(obt.TruckSavemessage(), "Truck created successfully.", "Outbound truck toast not found.");
//		sAssert.assertAll();
//	}
//
//	@Test(priority = 23, dependsOnMethods = "shouldCreateEmptyOutboundTruck")
//	public void shouldVerifyTruckQuantityAndStatusForEmptyTruck() throws TimeoutException, InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		sAssert.assertEquals(obt.getPalletQTYonli(), DataRepo.ZERO, "Empty Truck PalletQtyShould be Zero.");
//		obt.emtyTruckStatsus();
//		sAssert.assertEquals(obt.emtyTruckStatsus(), DataRepo.STTUS, "Empty Truck Status not Match on listing");
//		sAssert.assertAll();
//	}
//
//	@Test(priority = 24)
//	public void shouldNotAllowTruckDeletionIfTruckHasArrived() throws TimeoutException, InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		obt.truckMenu();
//		obt.searchQAdone(DataRepo.ARRIVED_STATUS);
//		obt.deleteBtn();
//		sAssert.assertEquals(cp.captureToastMessage(), "The tracking number is updated. You cannot delete the truck.",
//				"Not displyed toast When truck not able to delete truck");
//		sAssert.assertAll();
//	}
//
//	@Test(priority = 25)
//	public void shouldNotAllowPalletEditingOnceArrived() throws TimeoutException, InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		obt.truckMenu();
//		obt.searchQAdone(DataRepo.ARRIVED_STATUS);
//		obt.editBtn();
//		sAssert.assertFalse(obt.verifyRemovePalleBtnDisable().isEnabled(),
//				"Remove pallet btn is Editabel when QA done.");
//		sAssert.assertFalse(obt.verifyAddPalleBtnDisable().isEnabled(),
//				"Add order pallet should not be Editabel when QA done.");
//		sAssert.assertAll();
//
//	}
//
//	@Test(priority = 26, dataProvider = "getData", groups = "Smoke", enabled = false)
//	public void shouldApplyFiltersCorrectlyOnOutboundTruck(HashMap<String, String> input)
//			throws InterruptedException, TimeoutException {
//		obt.truckMenu();
//		cp.searchSection();
//		cp.clickClearButton();
//		String jsonPath = System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\xpath.JSON";
//
//		List<Integer> indicesToSelect = Arrays.asList(1);
////	//	cp.selectMultiplePickuprByIndex(indicesToSelect);
////		cp.validateDataInGrid(12);
////
//////		List<Integer> indicesToSelect1 = Arrays.asList(1, 2, 3);
//////		cp.selectMultipleDropByIndex(indicesToSelect1);
//////		cp.validateDataInGrid(11);//same for outbound Pallet
//
//		List<Integer> indicesToSelect2 = Arrays.asList(1, 2, 33);
//		cp.selectMultipleLTLByIndex(indicesToSelect2);
//		cp.validateDataInGrid(5);
//
//		List<Integer> indicesToSelect3 = Arrays.asList(5, 3, 2);
//		cp.selectMultiplestatusByIndex(indicesToSelect3);
//		cp.validateDataInGrid(10);
//
//		obt.searchAndValidateTruckNo();
//		obt.searchAndValidateTruckName();
//
//		Map<String, String> palletNo = CommonMethods.getXPathSet("inboundTruckPalletNo", jsonPath);
//		cp.extractInputFromIconSearchVerify(palletNo);
//
//		Map<String, String> inboundOrderNomap = CommonMethods.getXPathSet("inboundTruckOrderNo", jsonPath);
//		cp.extractInputFromIconSearchVerifyTwoIcons(inboundOrderNomap);
//
//		cp.searchAndValidateDateByColumn(9);
//		cp.searchAndValidateToDateByColumn(9);
//		cp.pickFrom();
//	}
//
//	@Test(priority = 27, enabled = false, groups = "Smoke")
//	public void shouldApplyColumnFilterForOutboundTruck(HashMap<String, String> input)
//			throws InterruptedException, TimeoutException, IOException {
//		obt.truckMenu();
//		obt.checkColoumFilterforTruck();
//	}
//
//	@Test(priority = 28, enabled = false, groups = "Smoke")
//	public void shouldApplyColumnFilterOnOutboundTruckCreatePage(HashMap<String, String> input)
//			throws InterruptedException, TimeoutException, IOException {
//		obt.truckMenu();
//		obt.createTruck();
//		obt.checkColoumFilterforCreateTruck();
//	}
//
//	@Test(priority = 29, groups = "Smoke")
//	public void shouldCheckPaginationOnOutboundTruckListing() throws TimeoutException, InterruptedException {
//		obt.truckMenu();
//		obt.paginationOnListingPageforOutboundTruck();
//	}
//
//	@Test(priority = 30, groups = "Smoke")
//	public void shouldCheckPaginationOnCreateOutboundTruck() throws TimeoutException, InterruptedException {
//		obt.truckMenu();
//		obt.createTruck();
//		obt.paginationOnOutboundCreateTruck();
//	}

//	@AfterClass()
//	public void shouldUpdateOrderStatusAfterOrderLoadInObtTruck() {
//		cp.orderpageMenulanding();
//		Assert.assertEquals(obt.getOrderStatus(obt.MGLOrderno), "Outbound Truck Loaded", "Order Status not Match.");
//
//	}

}
