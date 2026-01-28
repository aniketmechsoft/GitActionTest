package com.uiFramework.Genesis.regression.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.DataRepo;
import com.uiFramework.Genesis.web.pages.InbTruckPage;
import com.uiFramework.Genesis.web.pages.InboundPalletPage;

public class InboundTruckTest extends TestBase {
	InbTruckPage it = null;
	InboundPalletPage ip = null;
	CommonPage cp = null;
	CommonMethods cm;
	int initialCount;
	int afterCount;
	String iIBPL;
	String glorder;
	SoftAssert sAssert = new SoftAssert();
	private static final Logger logger = Logger.getLogger(InboundTruckTest.class.getName());

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
		it = new InbTruckPage(driver);
		cp = new CommonPage(driver);
		ip = new InboundPalletPage(driver);
	//	driver.navigate().refresh();

	}

	@Test(priority = 1,alwaysRun = true, groups = {"smoke"})
	public void shouldVerifyPalletAvailableToAddInTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.truckMenu();
		it.createTruck();
		it.searchPallet();
		sAssert.assertEquals(it.getMIBPLCreatingTrcuk(), it.MIBPL, "Main Pallet not available to Create Truck.");
		sAssert.assertAll();
	}

	@Test(priority = 2,alwaysRun = true, groups = {"smoke"})
	public void shouldDisplayToastMessageOnCreateTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.selectLTL(DataRepo.VALUE, "Resopnse number-102912 and Instructions");
		it.addTruckDetails(ip.MIBPL);
		sAssert.assertEquals(it.CreateTruckToast(), "Inbound Truck is created successfully.",
				"Inbound Truck create message not found.");
		it.getMTruckNo();
		it.truckMarkasArrived();
		sAssert.assertAll();
	}

	@Test(priority = 3,alwaysRun = true, groups = {"smoke"} )
	public void shouldCheckDefaultCostEstimate()
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.truckMenu();
		initialCount = ip.getTotalEntriesCount(driver);
		it.createTruck();
		sAssert.assertEquals(it.getCosEstimate(), DataRepo.DEFAULT_COST, "cost Estimate mismatch:");
		sAssert.assertAll();
	}
	
	@Test(priority = 4, dependsOnMethods="shouldCheckDefaultCostEstimate")
	public void shouldCheckOrderProcessShowcorrect()
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.checkOrderProcessforinboundTruck();
		sAssert.assertAll();
	}

	@Test(priority = 5,alwaysRun = true, groups = {"smoke"},dependsOnMethods="shouldCheckDefaultCostEstimate")
	public void shouldCreateTruckSuccessfullyAndVerifyTotalPiecesAndPalletQty()
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.selectLTL(DataRepo.VALUE, "Resopnse number-102912 and Instructions");
		it.getPickndDrop();
		it.selectPickupLocation();
		it.SelectDropLocation();
		it.creatTruckwithCheckTotalPiecesandpallet();
		cp.addpalletWt(DataRepo.LIT_WEIGHT);
		it.saveTruck();
		afterCount = ip.getTotalEntriesCount(driver);
		int diffint = afterCount - initialCount;
		String difference = String.valueOf(diffint);
		sAssert.assertEquals(difference, DataRepo.ONE, "Entry count did not increase for Truck by 1.");
		sAssert.assertAll();

	}

	@Test(priority = 6,dependsOnMethods="shouldCreateTruckSuccessfullyAndVerifyTotalPiecesAndPalletQty",alwaysRun = true, groups = {"smoke"})
	public void shouldVerifyTruckStatusAfterTruckCreation() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		String Status = it.getTruckNondStatus();
		sAssert.assertEquals(Status, DataRepo.LOAD_STATUS, "Truck Loaded Status not Match ");
		sAssert.assertAll();
	}

	@Test(priority = 7)
	public void shouldMatchCreatedTruckDetails() throws TimeoutException, InterruptedException {
		it.verifyTruckDetailsandPalletOrder();
	}

	@Test(priority = 8,alwaysRun = true, groups = {"smoke"})
	public void shouldUpdateTruckStatusOnMarkAsArrived() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.truckMenu();
		it.truckMarkasArrived();
		sAssert.assertEquals(it.getTruckStatus(), DataRepo.ARRIVED_STATUS, "Truck Status Arrived not Match ");
		sAssert.assertAll();
	}

	@Test(priority = 9,alwaysRun = true, groups = {"smoke"})
	public void shouldDisplayTruckDetailsInEditMode() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.truckMenu();
		it.editBtn();
		it.validateTruckCreatedDatainEditTruck();
		sAssert.assertEquals(cp.getPalletWt(),DataRepo.LIT_WEIGHT,
				"added pallet weight not match with truck");
		sAssert.assertAll();
	}

	@Test(priority = 10,alwaysRun = true, groups = {"smoke"} )
	public void shouldRemovePalletFromTruckAndUpdateWeightAndEmptyStatus()
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.checkPiecesanWeightWhenpalletRemoved();
		sAssert.assertEquals(it.getTruckEditStatus(), DataRepo.STATUS,
				"Truck status not Match after removed all pallets ");
		sAssert.assertAll();
	}

	@Test(priority = 11,dependsOnMethods="shouldRemovePalletFromTruckAndUpdateWeightAndEmptyStatus")
	public void shouldMakeRemovedPalletAvailableToAddInInboundCreateTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.clickBackBtn();
		it.createTruck();
		iIBPL = it.getIBPL();
		it.searchRemovedPallet(iIBPL);
		sAssert.assertFalse(it.checkElementNorecord(), "'No record found' is displayed.” Test Failed");
		sAssert.assertAll();
	}

	@Test(priority = 12, alwaysRun = true, groups = {"smoke"})
	public void shouldAddPalletInEditModeWithCalculations() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.truckMenu();
		it.editBtn();
		it.checkAddpalletFunctioninTruck();
		sAssert.assertEquals(it.getTruckEditStatus(), DataRepo.LOAD_STATUS, "Truck status not Match after Adding pallets ");
		sAssert.assertAll();
	}

	@Test(priority = 13, dependsOnMethods="shouldAddPalletInEditModeWithCalculations")
	public void shouldNotAllowAddingPalletToCreateTruck() throws TimeoutException, InterruptedException {
		cp.clickBackBtn();
		it.createTruck();
		it.searchAddedPalletinCreateTruck();
	}

	@Test(priority = 14)//
	public void shouldDeleteTruckAndMakePalletAvailableToAddInInboundTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.truckMenu();
		String Trcukno = it.getTruckNo();
		it.getDeletingPallet();
		it.deleteBtn();
		it.searchTruckno(Trcukno);
		sAssert.assertTrue(it.checkElementNorecord(), "'No record found' is Not displayed.” Test Failed");
		it.createTruck();
		iIBPL = it.getIBPL();
		it.searchRemovedPallet(iIBPL);
		sAssert.assertFalse(it.checkElementNorecord(), "'No record found' is displayed .” Test Failed");
		sAssert.assertAll();
	}

	@Test(priority = 15)
	public void shouldCreateEmptyInboundTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.truckMenu();
		it.createTruck();
		it.selectLTL(DataRepo.VALUE, "Resopnse number-102912 and Instructions");
		it.getPickndDrop();
		it.selectPickupLocation();
		it.SelectDropLocation();
		it.saveTruck();
		sAssert.assertEquals(it.CreateTruckToast(), "Inbound Truck is created successfully.",
				"Truck create success msg not found.");
		sAssert.assertAll();

	}

	@Test(priority = 16,dependsOnMethods="shouldCreateEmptyInboundTruck")
	public void shouldVerifyEmptyTruckQtyAndStatus() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(it.getPalletQTYonli(), DataRepo.ZERO, "Empty Truck PalletQtyShould be Zero.");
		it.emtyTruckStatsus();
		sAssert.assertEquals(it.emtyTruckStatsus(), DataRepo.STATUS, "Empty Truck Status not Match on listing");
		sAssert.assertAll();
	}

	@Test(priority = 17)
	public void shouldUpdateOrderStatusWhenTruckLoaded() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.truckMenu();
		it.editBtn();
		it.AddpalletinTruck();
		cp.clickBackBtn();
		glorder = it.getOrderno();
		it.truckMarkasArrived();
		cp.orderpageMenulanding();
		sAssert.assertEquals(it.getOrderStatus(glorder), "Inbound QA Pending", "Not Match");
		sAssert.assertAll();
	}

	@Test(priority = 18)
	public void shouldNotDeleteTruckIfQADone() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.truckMenu();
		it.searchQAdone(DataRepo.QA_DONE);
		it.deleteBtn();
		sAssert.assertEquals(cp.captureToastMessage(), "Truck inbound QA is done.", "Truck QA done message not found.");
		sAssert.assertAll();
	}

	@Test(priority = 19)
	public void shouldNotAllowEditPalletIfQADone() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.truckMenu();
		it.searchQAdone(DataRepo.QA_DONE);
		it.editBtn();
		sAssert.assertFalse(it.verifyRemovePalleBtnDisable().isEnabled(),
				"Remove pallet btn is Editabel when QA done.");
		sAssert.assertFalse(it.verifyAddPalleBtnDisable().isEnabled(),
				"Add order pallet should not Editabel when QA done.");
		sAssert.assertAll();

	}

	@Test(priority = 20, dataProvider = "getData", groups = {"smoke"}, enabled = true)
	public void shouldApplyFiltersCorrectlyOnInboundTruck(HashMap<String, String> input)
			throws InterruptedException, TimeoutException {
		it.truckMenu();
		cp.searchSection();
		cp.clickClearButton();
		String jsonPath = System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\xpath.JSON";

		List<Integer> indicesToSelect = Arrays.asList(55, 2, 3);
		cp.selectMultiplePickuprByIndex(indicesToSelect);
		cp.validateDataInGridForScroll(8);
		
//		List<Integer> indicesToSelect = Arrays.asList(55, 2, 3);
//		cp.selectMultiplePickuprByIndex(indicesToSelect);
//		cp.validateDataInGrid(10);
		
		List<Integer> indicesToSelect1 = Arrays.asList(2,3,5);
		cp.selectMultipleDropByIndex(indicesToSelect1);
		cp.validateDataInGridForScroll(9);

//		List<Integer> indicesToSelect1 = Arrays.asList(1, 2, 3);
//		cp.selectMultipleDropByIndex(indicesToSelect1);
//		cp.validateDataInGrid(11);

		List<Integer> indicesToSelect2 = Arrays.asList(1, 2, 33);
		cp.selectMultipleLTLByIndex(indicesToSelect2);
		cp.validateDataInGridForScroll(4);
		
//		List<Integer> indicesToSelect2 = Arrays.asList(1, 2, 33);
//		cp.selectMultipleLTLByIndex(indicesToSelect2);
//		cp.validateDataInGrid(6);
		
		cp.clickClearButton();
		List<Integer> indicesToSelect3 = Arrays.asList(5, 3, 2);
		cp.selectMultiplestatusByIndex(indicesToSelect3);
		cp.validateDataInGridForScroll(6);

//		List<Integer> indicesToSelect3 = Arrays.asList(5, 3, 2);
//		cp.selectMultiplestatusByIndex(indicesToSelect3);
//		cp.validateDataInGrid(8);

		it.searchAndValidateTruckNo();
		it.searchAndValidateTruckName();

		Map<String, String> palletNo = CommonMethods.getXPathSet("inboundTruckPalletNo", jsonPath);
		cp.extractInputFromIconSearchVerify(palletNo);

		Map<String, String> inboundOrderNomap = CommonMethods.getXPathSet("inboundTruckOrderNo", jsonPath);
		cp.extractInputFromIconSearchVerifyTwoIcons(inboundOrderNomap);

		cp.searchAndValidateDateByColumn(5);
		cp.searchAndValidateToDateByColumn(5);
		cp.pickFrom();
	}

	@Test(priority = 21, groups = {"smoke"})
	public void shouldApplyColumnFilterCorrectlyOnInboundTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.truckMenu();
		cp.verifyColumnFilter("frozen-pane");
		cp.verifyColumnFilter("scrollable-pane");
		sAssert.assertAll();
	}

	@Test(priority = 22, enabled = true, groups = {"smoke"})
	public void shouldApplyColumnFilterCorrectlyOnInboundTruckCreate()
			throws InterruptedException, TimeoutException{
		SoftAssert sAssert = new SoftAssert();
		it.truckMenu();
		it.createTruck();
		cp.verifyColumnFilter("frozen-pane");
		cp.verifyColumnFilter("scrollable-pane");
		sAssert.assertAll();
	}
	
	@Test(priority = 24, groups = {"smoke"})
	public void shouldCheckMandateFiledForImportTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.truckMenu();
		initialCount = ip.getTotalEntriesCount(driver);
		it.importIbtTrucks();
		cp.save();
		sAssert.assertEquals(it.getMandFieldCount(), 3, "Mandatory field count not match On import inbound Truck.");
		sAssert.assertAll();
	}
	
	@Test(priority = 25, groups = {"smoke"}, dependsOnMethods="shouldCheckMandateFiledForImportTruck")
	public void shouldCreateBulkInboundTruck() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		it.selectPickup();
		it.selectDrop();
		it.selectLtl();
		it.enterNooftrucks();
		cp.save();
		afterCount = ip.getTotalEntriesCount(driver);
		int diffint = afterCount - initialCount;
		String difference = String.valueOf(diffint);
		sAssert.assertEquals(difference, DataRepo.TWO, "Entry count did not increase for Truck by 2.");
		sAssert.assertAll();
	}

	@AfterClass()
	public void shouldUpdateOrderStatusAfterPalletLoaded() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.orderpageMenulanding();
		sAssert.assertEquals(cp.getOrderStatus(it.MGLOrderno), "Inbound QA Pending", "Not match");
		sAssert.assertAll();
	}

}
