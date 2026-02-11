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

	@Test(priority = 1, alwaysRun = true, groups = { "smoke" })
	public void shouldPalletAvailableToCreateOutboundTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		obt.truckMenu();
		obt.createTruck();
		obt.searchPallet();
		sAssert.assertEquals(obt.getMOBPLCreatingTrcuk(), opp.MOBPL,
				"Main outbound Pallet not available to Create Truck.");
		sAssert.assertAll();
	}

	@Test(priority = 2, alwaysRun = true, groups = { "smoke" })
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
		obt.checkOrderProcessForOutbound();
		sAssert.assertAll();
	}

	@Test(priority = 4,dependsOnMethods="shouldDisplayDefaultCostEstimateAndOrderProcess")
	public void shouldAllowEditingDropLocationDetailsInNonUPSCase() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		obt.selectLTL(DataRepo.VALUE, "Resopnse number-102912 and Instructions");
		obt.selectCheckbox();
		sAssert.assertTrue(obt.checkAddressfield(),
				"Field 'Enter Address 1' is NOT editable.Confirm that LTL multidrop.?");
		sAssert.assertAll();
	}

	@Test(priority = 5,dependsOnMethods="shouldAllowEditingDropLocationDetailsInNonUPSCase")
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

	@Test(priority = 7, alwaysRun = true, groups = { "smoke" })
	public void shouldVerifyAddedPalletWTandPiecesLoadedIntoTruck() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		obt.truckMenu();
		initialCount = obt.getTotalEntriesCount(driver);
		obt.createTruck();
		obt.selectLTLforUPScase(DataRepo.VALUE);
		obt.selectNonMandatefields("Resopnse number-102912 and Instructions");
		obt.creatTruckwithCheckTotalPiecesandpallet();
	}
	
	@Test(priority = 8, alwaysRun = true, groups = { "smoke" }, dependsOnMethods="shouldVerifyAddedPalletWTandPiecesLoadedIntoTruck")
	public void shouldRouteOrderEditable() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
//		obt.saveTruck();
//		sAssert.assertTrue(obt.isRouteOrderEditable(), "Route Order input field is not editable.");
//		sAssert.assertAll();
		obt.saveTruck();

		if (obt.isRouteOrderDisplayed()) {
		    sAssert.assertTrue(obt.isRouteOrderEditable(), 
		        "Route Order input field is not editable.");
		} else {
			sAssert.assertEquals(obt.CreateTruckToast(), "Truck created successfully.", "Outbound truck toast not found.");
		}
		sAssert.assertAll();

	}
	
	@Test(priority = 9, alwaysRun = true, groups = { "smoke" })
	public void shouldCreateSuccessfullyAndIncreseCountByOne() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		obt.routeSaveBtn();
		afterCount = obt.getTotalEntriesCount(driver);
		int diffint = afterCount - initialCount;
		String difference = String.valueOf(diffint);
		sAssert.assertEquals(difference, DataRepo.ONE, "Entry count did not increase for Truck by 1.");
		sAssert.assertAll();
	}
	
	@Test(priority = 10, dependsOnMethods="shouldCreateSuccessfullyAndIncreseCountByOne", groups = {
			"smoke" })
	public void shouldUpdateTruckStatusAfterTruckCreation() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(obt.getObtTruckNondStatus(), DataRepo.LOAD_STATUSs, "Status not Match for outbound truck");
		sAssert.assertAll();
	}
	
	@Test(priority = 11, dependsOnMethods = "shouldCreateSuccessfullyAndIncreseCountByOne")
	public void shouldVerifyCreatedTruckDetailsAndPalletQtyMatch() {
		SoftAssert sAssert = new SoftAssert();
		obt.verifyTruckDetailsandPalletOrder();
		sAssert.assertAll();
	}

	@Test(priority = 12, dependsOnMethods = "shouldVerifyCreatedTruckDetailsAndPalletQtyMatch")
	public void shouldDisplayLDACodesCorrectlyOnListing() {
		SoftAssert sAssert = new SoftAssert();
		for (String code : obt.LDAcodesOnlist) {
			sAssert.assertTrue(obt.LDAcodes.contains(code), "Missing LDA code in list: " + code);
			sAssert.assertAll();
		}
	}

	@Test(priority = 13)
	public void shouldDisplayTruckDetailsInEditOutboundTruckMode() throws TimeoutException, InterruptedException {
		obt.truckMenu();
		obt.editBtn();
		obt.validateTruckCreatedDatainEditTruck();
	}

	@Test(priority = 14)
	public void shouldAllowEditingLTL3rdPartyAndCostEstimate() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(obt.checkLTL(), "FAIL: LTL should be editable but it is readonly or disabled.");
		sAssert.assertTrue(obt.checkCostEstimateEditable(),
				"FAIL: Cost Estimate field should be editable but it's disabled or readonly.");
		sAssert.assertTrue(obt.check3rdPartyBilling(), "Third Party should be editable");
		sAssert.assertAll();
	}

	@Test(priority = 15, dependsOnMethods = "shouldAllowEditingLTL3rdPartyAndCostEstimate")
	public void shouldNotAllowMultiDropLocationForNonUPS() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(obt.checkLDAaddress(), "Multiple drops are not allowed for this LTL.",
				"Multi drop not allowed toast not displayed.");
		sAssert.assertAll();
	}

	@Test(priority = 16,alwaysRun = true, groups = {"smoke"})
	public void shouldRemovePalletFromTruckAndUpdateWeightAndEmptyTruckStatus()
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		obt.truckMenu();
		obt.editBtn();
		obt.checkPiecesanWeightWhenPalletRemoved();
		sAssert.assertEquals(obt.getTruckEditStatus(), DataRepo.STATUS,
				"Truck status not Match for outbound truck when removed all pallets ");
		sAssert.assertAll();
	}

	@Test(priority = 17, dependsOnMethods = "shouldRemovePalletFromTruckAndUpdateWeightAndEmptyTruckStatus")//, dependsOnMethods = "shouldRemovePalletFromTruckAndUpdateWeightAndEmptyTruckStatus"
	public void shouldVerifyOrderProcessDuringOutboundTruckEdit() throws InterruptedException, TimeoutException {
		//obt.truckMenu();
		//obt.editBtn();
		obt.checkOrderProcessForOutbound();
	}

	@Test(priority = 18, dependsOnMethods = "shouldRemovePalletFromTruckAndUpdateWeightAndEmptyTruckStatus")
	public void shouldRemovedPalletAvailableToAddInOuboundCreateTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.clickBackBtn();
		obt.createTruck();
		OBPL = obt.getOBPL();
		obt.selectLTLforUPScase(DataRepo.LIT_PIECES);
		obt.searchRemovedPallet(OBPL);
		sAssert.assertFalse(obt.checkElementNorecord(), "'No record found' is displayed.” Test Failed");
		sAssert.assertAll();
	}

	@Test(priority = 19,alwaysRun = true, groups = {"smoke"})
	public void shouldAddPalletInEditModeWithCalculationsAndShouldUpdateStatus()
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		obt.truckMenu();
		obt.editBtn();
		obt.checkAddpalletFunctioninObtTruck();
		sAssert.assertEquals(obt.getTruckEditStatus(), DataRepo.LOAD_STATUS,
				"Outbound Truck status not Match after Adding pallets");
		sAssert.assertAll();
	}

	@Test(priority = 20,dependsOnMethods="shouldAddPalletInEditModeWithCalculationsAndShouldUpdateStatus")
	public void shouldNotAllowAddedPalletToCreateOutboundTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.clickBackBtn();
		obt.createTruck();
		obt.selectLTLforUPScase(DataRepo.LIT_PIECES);
		sAssert.assertTrue(obt.searchRemovedPalletsShowNoRecord(), "'No record found' not shown for some pallets");
		sAssert.assertAll();
	}

	@Test(priority = 21)
	public void shouldVerifyCostEstimateOnEditingOutboundTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		obt.truckMenu();
		obt.editBtn();
		obt.updateCostestimate();
		sAssert.assertEquals(cp.captureToastMessage(), "Truck update successfully",
				"cost estimated toast not found.");
		sAssert.assertEquals(obt.getEstimatedCost(), "10.00", "Cost not Updated fot outbound truck");
		sAssert.assertAll();
	}

	@Test(priority = 22, dependsOnMethods = "shouldVerifyCostEstimateOnEditingOutboundTruck")
	public void shouldUpdateRoutesInEditOutboundTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		obt.clickonUpdateBtn();
		sAssert.assertTrue(obt.isRouteOrderEditable(), "Route Order input field is not editable.");
		cp.save();
		sAssert.assertTrue(cp.toastMsgReceivedSuccessfully(), "Route order Toast not Displyed.");
		sAssert.assertAll();
	}

	@Test(priority = 23)
	public void shouldDeleteTruckAndMakeOrdersAvailableToAdd() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		obt.truckMenu();
		String Trcukno = obt.getObtTruckNo();
		obt.getDeletingPallet();
		obt.deleteBtn();
		obt.searchTruckno1(Trcukno);
		sAssert.assertTrue(obt.checkElementNorecord(), "'No record found' is Not displayed. on listing Test Failed");
		obt.createTruck();
		OBPL = obt.getOBPL();
		obt.selectLTLforUPScase(DataRepo.LIT_PIECES);
		obt.searchRemovedPallet(OBPL);
		sAssert.assertFalse(obt.checkElementNorecord(), "'No record found' is displayed on create truck. Test Failed");
		sAssert.assertAll();
	}

	@Test(priority = 24)
	public void shouldCreateEmptyOutboundTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		obt.truckMenu();
		obt.createTruck();
		obt.selectLTL(DataRepo.VALUE, "Resopnse number-102912 and Instructions");
		obt.saveObtTruck();
		sAssert.assertEquals(obt.TruckSavemessage(), "Truck created successfully.", "Outbound truck toast not found.");
		sAssert.assertAll();
	}

	@Test(priority = 25, dependsOnMethods = "shouldCreateEmptyOutboundTruck")//
	public void shouldVerifyTruckQuantityAndStatusForEmptyTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(obt.getPalletQTYonli(), DataRepo.ZERO, "Empty Truck PalletQtyShould be Zero.");
		obt.emtyTruckStatsus();
		sAssert.assertEquals(obt.emtyTruckStatsus(), DataRepo.STTUS, "Empty Truck Status not Match on listing");
		sAssert.assertAll();
	}

	@Test(priority = 26)
	public void shouldNotAllowTruckDeletionIfTruckHasArrived() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		obt.truckMenu();
		obt.searchQAdone(DataRepo.ARRIVED_STATUS);
		obt.deleteBtn();
		sAssert.assertEquals(cp.captureToastMessage(), "The tracking number is updated. You cannot delete the truck.",
				"Not displyed toast When truck not able to delete truck");
		sAssert.assertAll();
	}

	@Test(priority = 27)
	public void shouldNotAllowPalletEditingOnceArrived() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		obt.truckMenu();
		obt.searchQAdone(DataRepo.ARRIVED_STATUS);
		obt.editBtn();
		sAssert.assertFalse(obt.verifyRemovePalleBtnDisable().isEnabled(),
				"Remove pallet btn is Editabel when QA done.");
		sAssert.assertFalse(obt.verifyAddPalleBtnDisable().isEnabled(),
				"Add order pallet should not be Editabel when QA done.");
		sAssert.assertAll();

	}

	@Test(priority = 28, dataProvider = "getData", groups = {"smoke"}, enabled = true)
	public void shouldApplyFiltersCorrectlyOnOutboundTruck(HashMap<String, String> input)
			throws InterruptedException, TimeoutException {
		obt.truckMenu();
		cp.searchSection();
		cp.clickClearButton();
		String jsonPath = System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\xpath.JSON";

		
		List<Integer> indicesToSelect3 = Arrays.asList(5, 3, 2);
		cp.selectMultiplestatusByIndex(indicesToSelect3);
		cp.validateDataInGridForScroll(7);

		List<Integer> indicesToSelect2 = Arrays.asList(1, 2, 33);
		cp.selectMultipleLTLByIndex(indicesToSelect2);
		cp.validateDataInGridForScroll(3);

		obt.searchAndValidateTruckNo();
		obt.searchAndValidateTruckName();

		Map<String, String> palletNo = CommonMethods.getXPathSet("inboundTruckPalletNo", jsonPath);
		cp.extractInputFromIconSearchVerify(palletNo);

		Map<String, String> inboundOrderNomap = CommonMethods.getXPathSet("inboundTruckOrderNo", jsonPath);
		cp.extractInputFromIconSearchVerifyTwoIcons(inboundOrderNomap);

		cp.searchAndValidateDateByColumn(6);
		cp.searchAndValidateToDateByColumn(6);
		cp.pickFrom();
	}

	@Test(priority = 29, enabled = true, groups = {"smoke"})
	public void shouldApplyColumnFilterForOutboundTruck()
			throws InterruptedException, TimeoutException, IOException {
		SoftAssert sAssert = new SoftAssert();
		obt.truckMenu();
		cp.verifyColumnFilter("scrollable-pane");
		sAssert.assertAll();
	}

	@Test(priority = 30, enabled = true, groups = {"smoke"})
	public void shouldApplyColumnFilterOnOutboundTruckCreatePage()
			throws InterruptedException, TimeoutException, IOException {
		SoftAssert sAssert = new SoftAssert();
		obt.truckMenu();
		obt.createTruck();
		cp.verifyColumnFilter("frozen-pane");
		cp.verifyColumnFilter("scrollable-pane");
		sAssert.assertAll();
	}

	//Removed pagination button new grid
//	@Test(priority = 29, groups = "smoke")
//	public void shouldCheckPaginationOnOutboundTruckListing() throws TimeoutException, InterruptedException {
//		obt.truckMenu();
//		obt.paginationOnListingPageforOutboundTruck();
//	}
//
//	@Test(priority = 30, groups = "smoke")
//	public void shouldCheckPaginationOnCreateOutboundTruck() throws TimeoutException, InterruptedException {
//		obt.truckMenu();
//		obt.createTruck();
//		obt.paginationOnOutboundCreateTruck();
//	}

//	@AfterClass()
//	public void shouldUpdateOrderStatusAfterOrderLoadInObtTruck() throws InterruptedException {
//		cp.orderpageMenulanding();
//		Assert.assertEquals(cp.getOrderStatus(obt.MGLOrderno), "Outbound Truck Loaded", "Order Status not Match.");
//	}
	
}
