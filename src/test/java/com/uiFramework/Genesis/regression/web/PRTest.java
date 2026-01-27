package com.uiFramework.Genesis.regression.web;

import java.util.Arrays;
import java.util.List;

import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.helper.WaitHelper;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.OrderPage;
import com.uiFramework.Genesis.web.pages.OutboundPalletPage;
import com.uiFramework.Genesis.web.pages.productionPage;

public class PRTest extends TestBase {	
	productionPage pr=null;
	CommonPage cp = null;
	OrderPage op = null;
	OutboundPalletPage opp = null;
	
	@BeforeClass(alwaysRun = true)
	public void beforeclass() {
		pr = new productionPage(driver);
		cp = new CommonPage(driver);
		op = new OrderPage(driver);
		opp = new OutboundPalletPage(driver);
	}
	
	@Test(priority = 1, alwaysRun = true, groups = {"smoke"})
	public void shouldNotDisplayedToastafterLogIn() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		driver.manage().window().maximize();
		System.out.println("smoke 1");
		sAssert.assertFalse(cp.isInvalidToastDisplayed());
		sAssert.assertAll();
	}
	
	@Test(priority = 2, alwaysRun = true, groups = {"smoke"})
	public void shouldNotDisplayedToastOnCreateOrderBtn() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("smoke 2");
		cp.createOrder();
		sAssert.assertFalse(cp.isInvalidToastDisplayed());
		sAssert.assertAll();
	}
	
	@Test(priority = 3, alwaysRun = true, groups = {"smoke"})
	public void shouldDisplayErrorForMandatoryFields() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("smoke 3");
		cp.save();
		sAssert.assertEquals(pr.getMandFieldCount(), 9, "Mandatory field count not match on Create Order Page.");
		sAssert.assertAll();	
	}
	
	@Test(priority = 16, alwaysRun = true, groups = {"regression1"})
	public void shouldNotDisplayedToastOnInboundPalletPage() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.inboundMenu();
		System.out.println("Regression 1");
		sAssert.assertFalse(cp.isInvalidToastDisplayed());
		sAssert.assertAll();
	}
	
	@Test(priority = 17, alwaysRun = true, groups = {"regression1"})
	public void shouldNotDisplayedToastOnInboundPalletCreatePage() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("Regression 2");
		pr.clickTocreate();
		sAssert.assertFalse(cp.isInvalidToastDisplayed());
		sAssert.assertAll();
	}
	
//	@Test(priority = 4, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullWarehouseDataOnCreateOrder() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickWarehouse();
//		sAssert.assertTrue(pr.checkDropDownValue("warehouse"), "On Create Order Warehouse list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 5, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullCustomerListOnCreateOrder() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//	//	Thread.sleep(2000);
//		pr.clickCustomerDD();
//		sAssert.assertTrue(pr.checkDropDownValue("customer"), "On Create Order Customer list is null!");
//		sAssert.assertAll();
//	}
	
//	@Test(priority = 6, alwaysRun = true, groups = {"smoke"}, dependsOnMethods="shouldNotNullCustomerListOnCreateOrder")
//	public void shouldNotNullCosigneeList() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.selectEnterValue("Alex");
//		pr.clickConsignee();
//		sAssert.assertTrue(pr.checkDropDownValueForDependsDrpdwn("Consignee"), "On Create Order, Consignee list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 7, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullDistributorList() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		//Thread.sleep(5000);
//		pr.selectOrderSource("Distributor");
//		pr.clickDistibutor();
//		sAssert.assertTrue(pr.checkDropDownValue("Distributor"), "On Create Order, Distributor list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 8, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullPickipLocationList() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		//Thread.sleep(5000);
//		pr.clickPickup();
//		sAssert.assertTrue(pr.checkDropDownValue("pickup"), "On Create Order, Pickip Location list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 9, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullLDACarrierList() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		//Thread.sleep(5000);
//		pr.clickLocalCarrier();
//		sAssert.assertTrue(pr.checkDropDownValue("carrier"), "On Create Order Carrier list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 10, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullOrderTypes() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		//Thread.sleep(5000);
//		pr.clickOrdertype();
//		sAssert.assertTrue(pr.checkDropDownValue("OrderType"), "On Create Order, OrderType list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 11, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullState() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickState();
//		sAssert.assertTrue(pr.checkDropDownValue("State"), "On Create Order, state list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 12, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckFilterOnOrderListing() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickBackBtn();
//		cp.waitForLoaderToDisappear();
//		cp.searchClick();
//		List<Integer> indicesToSelect1 = Arrays.asList(52, 2, 1);
//		cp.selectMultipleCustomerByIndex(indicesToSelect1);
//		cp.validateDataInGridForScroll(3);
//		List<Integer> indicesToSelect = Arrays.asList(13, 2, 8);
//		cp.selectMultipleConsigneesByIndex(indicesToSelect);
//		cp.validateDataInGridForScrollForConsignee(5);
//		List<Integer> indicesToSelect2 = Arrays.asList(6, 9, 8);
//		cp.selectMultipleDistributorByIndex(indicesToSelect2);
//		cp.validateDataInGridForScroll(2);
//		List<Integer> indicesToSelect3 = Arrays.asList(86, 4, 3);
//		cp.selectMultipleLDAByIndex(indicesToSelect3);
//		cp.validateDataInGridForScroll(8);
//		List<Integer> indicesToSelect4 = Arrays.asList(1, 2, 11);
//		cp.selectMultipleLTLByIndex(indicesToSelect4);
//		cp.validateDataInGridForScroll(9);
//		List<Integer> indicesToSelect5 = Arrays.asList(6, 19, 14);
//		cp.selectMultipleUsersByIndex(indicesToSelect5);
//		cp.validateDataInGridForScroll(16);
//		List<Integer> indicesToSelect6 = Arrays.asList(1, 2, 4);
//		cp.selectMultipleWarehouseByIndex(indicesToSelect6);
//		cp.validateDataInGridForScroll(1);
//		List<Integer> indicesToSelect7 = Arrays.asList(1, 2, 3);
//		cp.selectMultipleCreationModeByIndex(indicesToSelect7);
//		cp.validateDataInGridForScroll(13);
//		List<Integer> indicesToSelect8 = Arrays.asList(1, 3, 8);
//		cp.selectMultiplestatusByIndex(indicesToSelect8);
//		cp.validateDataInGridForScroll(15);
//		cp.clickClearButton();
//		//op.searchAndValidateOrderNumber();
//		//op.searchAndValidateCustomerOrderNumber();
//		//cp.searchAndValidateDateByColumn(21);
//		//cp.searchAndValidateToDateByColumn(21);
//		//op.searchAndValidateWaveNum();
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 13, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilter() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.verifyColumnFilter("frozen-pane");
//		pr.verifyColumnFilter("scrollable-pane");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 14, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPDFDownloadedOnOrderPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.deleteExistingFiles();
//		cp.clickToExpPDF();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".pdf"), "FAIL: File Extension not match With Downloaded file.");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 15, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckExcelDownloadedOnOrderPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.deleteExistingFiles();
//		cp.clickToExpExcel();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".xlsx"), "FAIL: File Extension not match With Downloaded file.");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 16, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnInboundPalletPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.inboundMenu();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 17, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnInboundPalletCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickTocreate();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 18, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPicklocationFromCreateInboundPallet() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.searchSection();
//		List<Integer> indicesToSelect1 = Arrays.asList(2, 21, 23);
//		cp.selectMultiplePickuprByIndex(indicesToSelect1);
//		cp.validateDataInGridForScroll(9);	
//	}
//
//	@Test(priority = 19, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullDropLocationFromCreatInboundPallet() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickDroplocation();
//		sAssert.assertTrue(pr.checkDropDownValueForMultiSel("DropLocation"), "On Inbound pallet create page, Drop Locations list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 20, alwaysRun = true, groups = {"smoke"})
//	public void shouldPickFromOnOnInboundPalletCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.pickFrom();
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 21, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilterOnInboundPalletCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.verifyColumnFilter("frozen-pane");
//		pr.verifyColumnFilter("scrollable-pane");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 22, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPicklocationFromInboundPallet() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickBackBtn();
//		cp.searchSection();
//		List<Integer> indicesToSelect1 = Arrays.asList(2, 21, 23);
//		cp.selectMultiplePickuprByIndex(indicesToSelect1);
//		cp.validateDataInGridForScroll(15);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 23, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckDroplocationFromInboundPallet() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		List<Integer> indicesToSelect1 = Arrays.asList(1, 3, 5);
//		cp.selectMultipleDropByIndex(indicesToSelect1);
//		cp.validateDataInGridForScrollForDropLoc(16);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 24, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPickFromAndStatusFromInboundPallet() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.pickFrom();
//		List<Integer> indicesToSelect2 = Arrays.asList(4, 3);
//		pr.selectMultiplestatusByIndex(indicesToSelect2);
//		cp.validateDataInGridForScroll(13);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 25, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilterOnInboundPalletListing() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.verifyColumnFilter("frozen-pane");
//		pr.verifyColumnFilter("scrollable-pane");
//		sAssert.assertAll();
//	}
//	@Test(priority = 26, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPDFDownloadedOnInboundpalletPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.deleteExistingFiles();
//		cp.clickToExpPDF();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".pdf"), "FAIL: File Extension not match With Downloaded file on IBPL page");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 27, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckExcelDownloadedOnInboundpalletPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.deleteExistingFiles();
//		cp.clickToExpExcel();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".xlsx"), "FAIL: File Extension not match With Downloaded file on IBPL page");
//		sAssert.assertAll();
//	}
//	
//	//Outbound pallet
//	@Test(priority = 28, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnOutboundPalletPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.outboundMenu();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 29, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnOutboundPalletCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickTocreate();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 30, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckCustomerDrpdwnOnOutboundPalletCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.searchSection();
//		List<Integer> indicesToSelect1 = Arrays.asList(52, 2, 1);
//		cp.selectMultipleCustomerByIndex(indicesToSelect1);
//		cp.validateDataInGridForScroll(3);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 31, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckConsigneeDrpdwnOnOutboundPalletCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickConsignee();
//		sAssert.assertTrue(pr.checkDropDownValueForDependsDrpdwn("Consignee"), "On outbound pallet create page, consignee list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 32, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckLDADrpdwnOnOutbondPalletCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		List<Integer> indicesToSelect3 = Arrays.asList(86, 4, 3);
//		cp.selectMultipleLDAByIndex(indicesToSelect3);
//		cp.validateDataInGridForScroll(4);
//		opp.checkDropFor();
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 33, alwaysRun = true, groups = {"smoke"})
//	public void shouldChecPickupLocationkDrpdwnOnOutbondPalletCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickOnPickupLocation();
//		sAssert.assertTrue(pr.checkDropDownValue("Pickup location"), "On Create OBPL pickuplocation warehouse list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 34, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckLDAListShouldNotBlankForOutboundPalletcreate() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.palletFromLDA();
//		pr.clickPickup();
//		sAssert.assertTrue(pr.checkDropDownValue("Pickup location"), "On Create OBPL pickuplocation LDA list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 35, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilterOnCreateOutboundPallet() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.verifyColumnFilter("frozen-pane");
//		pr.verifyColumnFilter("scrollable-pane");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 36, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckWarehousePickupListShouldNotNullFromOutboundPallet() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickBackBtn();
//		cp.searchSection();
//		pr.clickOnPickupLocation();
//		sAssert.assertTrue(pr.checkDropDownValue("Pickup location"), "On outbound pallet pickuplocation warehouse list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 37, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckLDAPickupListShouldNotNullFromOutboundPallet() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.palletFromLDA();
//		pr.clickPickup();
//		sAssert.assertTrue(pr.checkDropDownValue("Pickup location"), "On outbound pallet pickuplocation LDA list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 38, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullDropLocationFromOutbondPalletPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.clickDroplocation();
//		sAssert.assertTrue(pr.checkDropDownValueForMultiSel("Drop location"), "On outbound pallet Drop location list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 39, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckStatusDrpdwnFromOutboundListingPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickDroplocation();
//		List<Integer> indicesToSelect2 = Arrays.asList(1, 3);
//		pr.selectMultiplestatusByIndex(indicesToSelect2);
//		cp.validateDataInGridForScroll(10);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 40, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilterOnOutboundPallet() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.verifyColumnFilter("frozen-pane");
//		pr.verifyColumnFilter("scrollable-pane");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 41, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPDFDownloadedOnOutboundpalletPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.deleteExistingFiles();
//		cp.clickToExpPDF();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".pdf"), "FAIL: File Extension not match With Downloaded file on OBPL page");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 42, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckExcelDownloadedOnOutboundpalletPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.deleteExistingFiles();
//		cp.clickToExpExcel();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".xlsx"), "FAIL: File Extension not match With Downloaded file on OBPL page");
//		sAssert.assertAll();
//	}
//	
//	//Truck
//	@Test(priority = 43, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnInboundTrkPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.inboundtruckMenu();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 44, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOninboundTrkCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickTocreate();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 45, alwaysRun = true, groups = {"smoke"})
//	public void shouldShowErrorForMandatoryFieldsOnInboundTrkCreate() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.save();
//		sAssert.assertEquals(pr.getMandFieldCount(), 3, "Mandatory field count not match on Create inbound trcuk Page.");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 46, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullLTLColumnDrpDownOnInboundCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickOnLTL();
//		sAssert.assertTrue(pr.checkDropDownValue("LTL list"), "On inbound truck create LTL list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 47, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNull3rdPartyBillingAndDescriptionDrpDownOnInboundCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickOn3rdPartyBilling();
//		sAssert.assertTrue(pr.checkDropDownValue("3rd party list"), "On inbound truck create 3rd party list is null!");
//		pr.clickOnDescription();
//		sAssert.assertTrue(pr.checkDropDownValue("Description list"), "On inbound truck create Description list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 48, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPickFromAndPickuplocationDrpdwn() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.pickFrom();
//		pr.clickOnPickupLocation();
//		sAssert.assertTrue(pr.checkDropDownValue("Pick up Location"), "On inbound truck create Pickup Location is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 49, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPickFromAndDroplocationDrpdwn() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickDroplocation();
//		sAssert.assertTrue(pr.checkDropDownValue("Drop Location"), "On inbound truck create Drop Location is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 50, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilterOnInboundTrkCreate() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.verifyColumnFilter("frozen-pane");
//		pr.verifyColumnFilter("scrollable-pane");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 51, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPickFromAndPickuplocationFilterOnInboundTrkListing() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickBackBtn();
//		cp.searchSection();
//		cp.pickFrom();
//		List<Integer> indicesToSelect = Arrays.asList(55, 2, 3);
//		cp.selectMultiplePickuprByIndex(indicesToSelect);
//		cp.validateDataInGridForScroll(8);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 52, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckDroplocationFilterOnInboundTrkListing() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		List<Integer> indicesToSelect = Arrays.asList(2,3,5);
//		cp.selectMultipleDropByIndex(indicesToSelect);
//		cp.validateDataInGridForScroll(9);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 53, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckLTLFilterOnInboundTrkListingPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		List<Integer> indicesToSelect2 = Arrays.asList(1, 2, 33);
//		cp.selectMultipleLTLByIndex(indicesToSelect2);
//		cp.validateDataInGridForScroll(4);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 54, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckStatusOnInboundListingTrkPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		List<Integer> indicesToSelect3 = Arrays.asList(5, 3, 2);
//		cp.selectMultiplestatusByIndex(indicesToSelect3);
//		cp.validateDataInGridForScroll(6);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 55, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilterOnInboundTrkListing() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.verifyColumnFilter("frozen-pane");
//		pr.verifyColumnFilter("scrollable-pane");
//		cp.clickClearButton();
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 56, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPDFDownloadedOnInboundTruck() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.deleteExistingFiles();
//		cp.clickToExpPDF();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".pdf"), "FAIL: File Extension not match With Downloaded file on Inbound Truck page");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 57, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckExcelDownloadedOnInboundTruck() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.deleteExistingFiles();
//		cp.clickToExpExcel();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".xlsx"), "FAIL: File Extension not match With Downloaded file on Inbound Truck page");
//		sAssert.assertAll();
//	}
//	
//	//obt...........
//	@Test(priority = 58, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnOutboundTrkPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.outboundtruckMenu();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 59, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnOutboundTrkCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickTocreate();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 60, alwaysRun = true, groups = {"smoke"})
//	public void shouldSHowErrorForMandatoryFieldsForCreateOutboundTruck() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.save();
//		sAssert.assertEquals(pr.getMandFieldCount(), 1, "Mandatory field count not match on Create Order Page.");
//		sAssert.assertAll();	
//	}
//	
//	@Test(priority = 61, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullLTLColumnDrpDownOnOutboundCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickOnLTL();
//		sAssert.assertTrue(pr.checkDropDownValue("LTL list"), "On Outbound truck create LTL list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 62, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNull3rdPartyBillingAndDescriptionDrpDownOnOutboundCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickOn3rdPartyBilling();
//		sAssert.assertTrue(pr.checkDropDownValue("3rd party list"), "On Outbound truck create 3rd party list is null!");
//		pr.clickOnDescription();
//		sAssert.assertTrue(pr.checkDropDownValue("Description list"), "On Outbound truck create Description list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 63, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullPickupLocationWarehouseListOnOutboundCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickOnPickupLocation();
//		sAssert.assertTrue(pr.checkDropDownValue("Pickup Location"), "On outbound truck create Pickup Location(warehouse) is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 64, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullPickupLocationLDAListOnOutboundCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.palletFromLDA();
//		pr.clickOnPickupLocation();
//		sAssert.assertTrue(pr.checkDropDownValue("Pickup Location"), "On outbound truck create Pickup Location(LDA) is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 65, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilterOnOutboundTrkCreate() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.verifyColumnFilter("scrollable-pane");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 66, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckLTLFilterOnOutboundtruckPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickBackBtn();
//		cp.searchSection();
//		List<Integer> indicesToSelect = Arrays.asList(1, 22, 3);
//		cp.selectMultipleLTLByIndex(indicesToSelect);
//		cp.validateDataInGridForScroll(3);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 67, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckDroplocationForLDAOnOutboundTrkListing() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickDroplocation();
//		sAssert.assertTrue(pr.checkDropDownValueForMultiSel("Drop Location"), "On outbound truck Drop Location(LDA) is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 68, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckDroplocationForOriginOnOutboundTrkListing() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.dropforOrigin();
//		pr.clickDroplocation();
//		sAssert.assertTrue(pr.checkDropDownValueForMultiSel("Drop Location"), "On outbound truck Drop Location(origin) is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 69, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullPickupLocationWarehouseListOnOutboundList() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickOnPickupLocation();
//		sAssert.assertTrue(pr.checkDropDownValue("Pickup Location"), "On outbound truck Lsting Pickup Location(warehouse) is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 70, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullPickupLocationLDAListOnOutboundListPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.palletFromLDA();
//		pr.clickOnPickupLocation();
//		sAssert.assertTrue(pr.checkDropDownValue("Pickup Location"), "On outbound truck Lsting Pickup Location(LDA) is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 71, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckStatusOnOutboundListingTrkPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		List<Integer> indicesToSelect3 = Arrays.asList(5, 3, 2);
//		cp.selectMultiplestatusByIndex(indicesToSelect3);
//		cp.validateDataInGridForScroll(7);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 72, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilterOnOutboundTrkListing() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.verifyColumnFilter("scrollable-pane");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 73, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPDFDownloadedOnOutboundTruck() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.deleteExistingFiles();
//		cp.clickToExpPDF();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".pdf"), "FAIL: File Extension not match With Downloaded file on Inbound Truck page");
//		sAssert.assertAll();
//	}
//
//	@Test(priority = 74, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckExcelDownloadedOnObtboundTruck() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.deleteExistingFiles();
//		cp.clickToExpExcel();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".xlsx"), "FAIL: File Extension not match With Downloaded file on Inbound Truck page");
//		sAssert.assertAll();
//	}
//	
//	//Docuements
//	@Test(priority = 75, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnDocumentQueue() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.docQueueMenu();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 76, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullkWarehouseDrpDwnOnDocumentQueue() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.searchSection();
//		pr.clickSelWarehouse();
//		sAssert.assertTrue(pr.checkDropDownValue("Warehouse"), "On Document queue warehouse list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 77, alwaysRun = true, groups = {"smoke"})
//	public void shouldWorkCustomerFilterOnOnDocumentQueue() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		List<Integer> indicesToSelect1 = Arrays.asList(52, 2, 1);
//		cp.selectMultipleCustomerByIndex(indicesToSelect1);
//		cp.validateDataInGrid(5);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 78, alwaysRun = true, groups = {"smoke"})
//	public void shouldWorkConsigneeFilterOnDocumentQueue() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		List<Integer> indicesToSelect = Arrays.asList(20, 3, 9);
//		cp.selectMultipleConsigneesByIndex(indicesToSelect);
//		cp.validateDataInGridForConsignee(7);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 79, alwaysRun = true, groups = {"smoke"})
//	public void shouldWorkLDAandLTLFilterOnDocumentQueue() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();		
//		List<Integer> indicesToSelect3 = Arrays.asList(86, 4, 1);
//		cp.selectMultipleLDAByIndex(indicesToSelect3);
//		cp.validateDataInGrid(10);
//		cp.clickClearButton();
//		List<Integer> indicesToSelect4 = Arrays.asList(1, 2, 11);
//		cp.selectMultipleLTLByIndex(indicesToSelect4);
//		cp.validateDataInGrid(11);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 80, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullkDocumentStatusDrpDwnOnDocumentQueue() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickDocumentStatus();
//		sAssert.assertTrue(pr.checkDropDownValue("Doc. Status"), "On Document queue warehouse list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 81, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilterOnDocumentQueue() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.verifyColumnFilterForFixGrid();
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 82, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnGeneratedDoc() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickDocGen();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 83, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckToastOnGeneratedDoc() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.searchSection();
//		List<Integer> indicesToSelect3 = Arrays.asList(6, 3, 1);
//		cp.selectMultipleDocumentTypeByIndex(indicesToSelect3);
//		cp.validateDataInGrid(5);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 84, alwaysRun = true, groups = {"smoke"})
//	public void shouldWorkCustomerAndLDAFilterOnOnGeneratedDoc() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		List<Integer> indicesToSelect1 = Arrays.asList(52, 2, 1);
//		cp.selectMultipleCustomerByIndex(indicesToSelect1);
//		cp.validateDataInGrid(6);
//		List<Integer> indicesToSelect3 = Arrays.asList(86, 4, 1);
//		cp.selectMultipleLDAByIndex(indicesToSelect3);
//		cp.validateDataInGrid(7);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 85, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullCSRDrpDwnOnGeneratedDoc() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickCustomerServiceRep();
//		sAssert.assertTrue(pr.checkDropDownValueForMultiSel("CSR"), "On Genreated documnet CSR list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 86, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckDocPrintStatusOnGeneratedDoc() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		List<Integer> indicesToSelect3 = Arrays.asList(1);
//		cp.selectMultipleDocPrintStatusByIndex(indicesToSelect3);
//		cp.validateDataInGrid(9);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 87, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilterOnGeneratedDoc() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.verifyColumnFilterForFixGrid();
//		sAssert.assertAll();
//	}
//	
//	//spite pod
//	@Test(priority = 88, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnSplitPOD() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.ClickSplitPod();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 89, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilterOnSplitPOD() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickToRefresh();
//		pr.verifyColumnFilterForFixGrid();
//		sAssert.assertAll();
//	}
//	
//	//QA Menu
//	@Test(priority = 90, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnInboundQAPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.inboundQaMenu();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 91, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPickFromAndPickuplocationFilterOnInboundQAListing() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.searchSection();
//		cp.pickFrom();
//		List<Integer> indicesToSelect = Arrays.asList(55, 2, 3);
//		cp.selectMultiplePickuprByIndex(indicesToSelect);
//		cp.validateDataInGrid(7);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 92, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckDroplocationFilterOnInboundQAListing() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		List<Integer> indicesToSelect = Arrays.asList(1,2,5);
//		cp.selectMultipleDropByIndex(indicesToSelect);
//		cp.validateDataInGrid(8);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 93, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckLTLFilterOnInboundQAPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		List<Integer> indicesToSelect2 = Arrays.asList(1, 2, 33);
//		cp.selectMultipleLTLByIndex(indicesToSelect2);
//		cp.validateDataInGrid(5);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 94, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckStatusOnInboundQAPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		List<Integer> indicesToSelect3 = Arrays.asList(5, 3, 2);
//		cp.selectMultiplestatusByIndex(indicesToSelect3);
//		cp.validateDataInGrid(6);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 95, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilterOnInboundQAListing() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.verifyColumnFilterForFixGrid();
//		cp.clickClearButton();
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 96, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPDFDownloadedOnInboundQAPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.deleteExistingFiles();
//		cp.clickToExpPDF();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".pdf"), "FAIL: File Extension not match With Downloaded file on Inbound QA page");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 97, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckExcelDownloadedOnInboundQAPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.deleteExistingFiles();
//		cp.clickToExpExcel();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".xlsx"), "FAIL: File Extension not match With Downloaded file on Inbound QA page");
//		sAssert.assertAll();
//	}
//	
//	//outbound QA
//	@Test(priority = 98, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnOutboundQAPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.outboundQAMenu();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 99, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckMandatoryFieldOnOutboundQA() {
//		SoftAssert sAssert = new SoftAssert();
//		cp.Search();
//		sAssert.assertEquals(pr.getMandFieldCount(), 2, "Mandatory field count not match on Outbound QA Page.");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 100, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullLDADropDownOnOutboundQAPage() {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickOnLDA();
//		sAssert.assertTrue(pr.checkDropDownValue("LDA"), "On Outbound QA page LDA list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 101, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullManifestDropDownOnOutboundQAPage() {
//		SoftAssert sAssert = new SoftAssert();
//		pr.selectFirstLDA();
//		pr.clickOnManifest();
//		sAssert.assertTrue(pr.checkDropDownValueForDependsDrpdwn("Manifest"), "On Outbound QA page Manifest list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 102, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullWarehouseDropDownOnOutboundQAPage() {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickWarehouse();
//		sAssert.assertTrue(pr.checkDropDownValue("Warehouse"), "On Outbound QA page Warehouse list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 103, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullStatusDropDownOnOutboundQAPage() {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.clickOnStatus();
//		sAssert.assertTrue(pr.checkDropDownValueForMultiSel("Status"), "On Outbound QA page Status list is null!");
//		sAssert.assertAll();
//	}
//	
//	//consginee QA
//	@Test(priority = 104, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnConsigneeQAPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.consigneeQAMenu();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 105, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckMandatoryFieldOnConsigneeQA() {
//		SoftAssert sAssert = new SoftAssert();
//		cp.Search();
//		sAssert.assertEquals(pr.getMandFieldCount(), 1, "Mandatory field count not match on Consignee QA Page.");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 106, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullLBOLListConsigneeQAPage() {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickOnLBOL();
//		sAssert.assertTrue(pr.checkDropDownValueForDependsDrpdwn("Lbol"), "On Consignee QA page LBOL list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 107, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullCustomerListConsigneeQAPage() {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickCustomerDD();
//		sAssert.assertTrue(pr.checkDropDownValue("Customer"), "On Consignee QA page customer list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 108, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullConsigneeListConsigneeQAPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.selectEnterValue("Alex");
//		pr.clicksingleConDrpdwn();
//		sAssert.assertTrue(pr.checkDropDownValue("Consignee"), "On Consignee QA page consignee list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 109, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullWarehouseDropDownOnConsigneeQAPage() {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.clickWarehouse();
//		sAssert.assertTrue(pr.checkDropDownValue("Warehouse"), "On Consignee QA page Warehouse list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 110, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullStatusDropDownOnConsigneeQAPage() {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickOnPendingStatus();
//		sAssert.assertTrue(pr.checkDropDownValueForMultiSel("Status"), "On Consignee QA page Status list is null!");
//		sAssert.assertAll();
//	}
//	
//	//Tracking Page
//	@Test(priority = 111, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnTrackingPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.trackingMenu();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 112, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckMandatoryFieldOnTrackingPage() {
//		SoftAssert sAssert = new SoftAssert();
//		cp.Search();
//		sAssert.assertEquals(pr.getMandFieldCount(), 1, "Mandatory field count not match on Consignee QA Page.");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 113, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullStatusDropDownOnTrackingPage() {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickOnPendingStatus();
//		sAssert.assertTrue(pr.checkDropDownValueForMultiSel("Status"), "On Tracking updated page Status list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 114, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullWarehouseDropDownOnTrackingPage() {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.clickWarehouse();
//		sAssert.assertTrue(pr.checkDropDownValueForMultiSel("Warehouse"), "On Consignee QA page Warehouse list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 115, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilterOnTrackingPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.verifyColumnFilterForFixGrid();
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 116, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckLTLFilterOnTrackingPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		List<Integer> indicesToSelect4 = Arrays.asList(1, 2, 11);
//		cp.selectMultipleLTLByIndex(indicesToSelect4);
//		cp.validateDataInGrid(4);
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 117, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPDFDownloadedOnTrackingPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		cp.deleteExistingFiles();
//		cp.clickToExpPDF();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".pdf"), "FAIL: File Extension not match With Downloaded file on TrackingPage page");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 118, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckExcelDownloadedOnTrackingPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();;
//		cp.deleteExistingFiles();
//		cp.clickToExpExcel();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".xlsx"), "FAIL: File Extension not match With Downloaded file on TrackingPage page");
//		sAssert.assertAll();
//	}
//	
//	//reconsignment 
//	@Test(priority = 119, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnReconsignPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.reconsignMenu();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 120, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnReconsignmentCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickToAddOrd();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 121, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckMandatoryFieldOnReconsignmentPage() {
//		SoftAssert sAssert = new SoftAssert();
//		cp.save();
//		sAssert.assertEquals(pr.getMandFieldCount(), 8, "Mandatory field count not match on ReconsignmentPage.");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 122, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullNewLDAListOnReconsignePage() {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickNewLDA();
//		sAssert.assertTrue(pr.checkDropDownValue("New LDA"), "On Reconsignment page 'New LDA' list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 123, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullStateOnReconsginementPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickState();
//		sAssert.assertTrue(pr.checkDropDownValue("State"), "On Create recon Order, state list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 124, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckStatusFilterOnReconsginementPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickBackBtn();
//		cp.searchSection();
//		List<Integer> indicesToSelect8 = Arrays.asList(1, 3, 8);
//		cp.selectMultiplestatusByIndex(indicesToSelect8);
//		cp.validateDataInGrid(16);
//	}
//	
//	@Test(priority = 125, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckCustomerConsigneeFilterOnReconsginementPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		List<Integer> indicesToSelect1 = Arrays.asList(52, 2, 22);
//		cp.selectMultipleCustomerByIndex(indicesToSelect1);
//		cp.validateDataInGrid(5);
//		List<Integer> indicesToSelect = Arrays.asList(2, 42, 9);
//		cp.selectMultipleOrgConsigneesByIndex(indicesToSelect);
//		cp.validateDataInGridForConsignee(7);
//		List<Integer> indicesToSelect2 = Arrays.asList(30, 29, 2);
//		cp.selectMultipleToConsigneesByIndex(indicesToSelect2);
//		cp.validateDataInGridForConsignee(8);
//	}
//	
//	@Test(priority = 126, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckLDAFilterOnReconsginementPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		List<Integer> indicesToSelect1 = Arrays.asList(1, 11, 22);
//		cp.selectMultipleOriginLDAByIndex(indicesToSelect1);
//		cp.validateDataInGrid(11);
//		List<Integer> indicesToSelect2 = Arrays.asList(1, 11, 3);
//		cp.selectMultipleNewLDAByIndex(indicesToSelect2);
//		cp.validateDataInGrid(12);	
//	}
//	
//	@Test(priority = 127, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckLTLUsersFiltersReconsginementPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		List<Integer> indicesToSelect4 = Arrays.asList(1, 2, 11);
//		cp.selectMultipleLTLByIndex(indicesToSelect4);
//		cp.validateDataInGrid(13);
//		List<Integer> indicesToSelect5 = Arrays.asList(6, 19, 14);
//		cp.selectMultipleUsersByIndex(indicesToSelect5);
//		cp.validateDataInGrid(17);
//	}
//	
//	@Test(priority = 128, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullWarehouseDropDownOnReconsginementPage() {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.clickWarehouse();
//		sAssert.assertTrue(pr.checkDropDownValueForMultiSel("Warehouse"), "On ReconsginementPage Warehouse list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 129, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilterOnReconsingentPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.verifyColumnFilterForFixGrid();
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 130, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPDFDownloadedOnReconsginementPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		cp.deleteExistingFiles();
//		cp.clickToExpPDF();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".pdf"), "FAIL: File Extension not match With Downloaded file on ReconsginementPage");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 131, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckExcelDownloadedOnReconsginementPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		cp.deleteExistingFiles();
//		cp.clickToExpExcel();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".xlsx"), "FAIL: File Extension not match With Downloaded file on ReconsginementPage");
//		sAssert.assertAll();
//	}
//		
//	//Return
//	@Test(priority = 132, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnRecturnPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.returnMenu();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 133, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnReturnCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickToAddOrd();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 134, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckMandatoryFieldOnReturnPage() {
//		SoftAssert sAssert = new SoftAssert();
//		cp.save();
//		sAssert.assertEquals(pr.getMandFieldCount(), 6, "Mandatory field count not match on Return Page.");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 135, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullStateOnReturnPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickState();
//		sAssert.assertTrue(pr.checkDropDownValue("State"), "On Create Return order, state list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 136, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckStatusFilterOnReturnPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickBackBtn();
//		cp.searchSection();
//		List<Integer> indicesToSelect8 = Arrays.asList(1, 3, 8);
//		cp.selectMultiplestatusByIndex(indicesToSelect8);
//		cp.validateDataInGrid(13);
//	}
//	
//	@Test(priority = 137, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckCustomerConsigneeFilterOnReturnPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		List<Integer> indicesToSelect1 = Arrays.asList(52, 2, 22);
//		cp.selectMultipleCustomerByIndex(indicesToSelect1);
//		cp.validateDataInGrid(5);
//		List<Integer> indicesToSelect = Arrays.asList(2, 42, 9);
//		cp.selectMultipleOrgConsigneesByIndex(indicesToSelect);
//		cp.validateDataInGridForConsignee(7);	
//	}
//	
//	@Test(priority = 138, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullLDAListReturnPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickOnLDA();
//		sAssert.assertTrue(pr.checkDropDownValueForMultiSel("LDA"), "On Return page 'LDA' list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 139, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckLTLUsersFiltersReturnPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		List<Integer> indicesToSelect4 = Arrays.asList(1, 2, 11);
//		cp.selectMultipleLTLByIndex(indicesToSelect4);
//		cp.validateDataInGrid(11);
//		List<Integer> indicesToSelect5 = Arrays.asList(6, 19, 14);
//		cp.selectMultipleUsersByIndex(indicesToSelect5);
//		cp.validateDataInGrid(14);
//	}
//	
//	@Test(priority = 140, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullWarehouseDropDownOnReturnPage() {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.clickWarehouse();
//		sAssert.assertTrue(pr.checkDropDownValueForMultiSel("Warehouse"), "On RReturnPage Warehouse list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 141, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilterOnReturnPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.verifyColumnFilterForFixGrid();
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 142, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPDFDownloadedOnReturnPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.deleteExistingFiles();
//		cp.clickToExpPDF();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".pdf"), "FAIL: File Extension not match With Downloaded file on ReturnPage");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 143, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckExcelDownloadedOnReturnPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.deleteExistingFiles();
//		cp.clickToExpExcel();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".xlsx"), "FAIL: File Extension not match With Downloaded file on ReturnPage");
//		sAssert.assertAll();
//	}
//	
//	//closeout
//	@Test(priority = 144, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnCloseoutPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.closeOutMenu();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 145, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotDisplayedToastOnCloseoutCreatePage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickToAddOrd();
//		sAssert.assertFalse(cp.isInvalidToastDisplayed());
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 146, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckMandatoryFieldOnCloseoutPage() {
//		SoftAssert sAssert = new SoftAssert();
//		cp.save();
//		sAssert.assertEquals(pr.getMandFieldCount(), 10, "Mandatory field count not match on Closeout Page.");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 147, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullCustomerListOnCloseoutCreateOrder() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//	//	Thread.sleep(2000);
//		pr.clickCustomerDD();
//		sAssert.assertTrue(pr.checkDropDownValue("customer"), "On closeout Create Order Customer list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 148, alwaysRun = true, groups = {"smoke"}, dependsOnMethods="shouldNotNullCustomerListOnCloseoutCreateOrder")
//	public void shouldNotNullCosigneeListOnCloseoutPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.selectEnterValue("Deccan");
//		pr.clickPickConsignee();
//		sAssert.assertTrue(pr.checkDropDownValueForDependsDrpdwn("consignee"), "On Closeout Create Order, Consignee list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 149, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullOrderTypesOnCloseOutPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickOrdertype();
//		sAssert.assertTrue(pr.checkDropDownValue("OrderType"), "On Closeout Create Order, OrderType list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 150, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullLocalcarrierListOnCloseoutPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickOrgLocalcarrier();
//		sAssert.assertTrue(pr.checkDropDownValue("Local carrier"), "On Closeout Create Order, Local carrier list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 151, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullCloseoutTypeOnCloseoutPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickCloseoutType();
//		sAssert.assertTrue(pr.checkDropDownValue("CloseoutType"), "On Closeout Create Order, CloseoutType list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 152, alwaysRun = true, groups = {"smoke"})
//	public void shouldNotNullStateOnCloseoutPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		pr.clickState();
//		sAssert.assertTrue(pr.checkDropDownValue("State"), "On Closeout Create recon Order, state list is null!");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 153, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckCustomerConsigneeFilterOnCloseoutPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickBackBtn();
//		cp.searchSection();
//		List<Integer> indicesToSelect1 = Arrays.asList(52, 2, 22);
//		cp.selectMultipleCustomerByIndex(indicesToSelect1);
//		cp.validateDataInGrid(4);
//		List<Integer> indicesToSelect = Arrays.asList(2, 42, 9);
//		cp.selectMultiplePickupConsigneesByIndex(indicesToSelect);
//		cp.validateDataInGridForConsignee(6);
//	}
//	
//	@Test(priority = 154, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckStatusPickupLDAUsersFilterOnCloseoutPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		List<Integer> indicesToSelect4 = Arrays.asList(1, 2);
//		cp.selectMultiplestatusByIndex(indicesToSelect4);
//		cp.validateDataInGrid(11);
//		List<Integer> indicesToSelect1 = Arrays.asList(1, 11, 2);
//		cp.selectMultiplePickupLDAByIndex(indicesToSelect1);
//		cp.validateDataInGrid(9);
//		List<Integer> indicesToSelect5 = Arrays.asList(6, 19, 14);
//		cp.selectMultipleUsersByIndex(indicesToSelect5);
//		cp.validateDataInGrid(12);
//	}
//	
//	@Test(priority = 155, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckColumnFilterOnCloseoutPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		pr.verifyColumnFilterForFixGrid();
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 156, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckPDFDownloadedOnCloseoutPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.clickClearButton();
//		cp.deleteExistingFiles();
//		cp.clickToExpPDF();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".pdf"), "FAIL: File Extension not match With Downloaded file on CloseoutPage");
//		sAssert.assertAll();
//	}
//	
//	@Test(priority = 157, alwaysRun = true, groups = {"smoke"})
//	public void shouldCheckExcelDownloadedOnCloseoutPage() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.deleteExistingFiles();
//		cp.clickToExpExcel();
//		sAssert.assertTrue(cp.isFileDownloadWithExtension(".xlsx"), "FAIL: File Extension not match With Downloaded file on CloseoutPage");
//		sAssert.assertAll();
//	}
	
	
}
