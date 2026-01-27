package com.uiFramework.Genesis.regression.web;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.web.pages.AccountsReceivable;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.DataRepo;

public class ARInvoiceTest extends TestBase {

	AccountsReceivable ar = null;
	CommonPage cp = null;
	CommonMethods cm;
	int initialCount;
	int afterCount;
	

	@BeforeClass(alwaysRun = true)
	public void beforeclass() {
		test = extent.createTest(getClass().getSimpleName());
		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		ar = new AccountsReceivable(driver);
		//driver.navigate().refresh();

	}

	@Test(priority = 1, alwaysRun = true)
	public void shouldAllowAllOrderTypesToBeUsedForInvoiceCreation() throws TimeoutException, InterruptedException {
		//ar.ARMenu(); comment while run in suite
		ar.ClickOnInvoiceMenu();
		initialCount = ar.getTotalEntriesCount();
		ar.clickOnCreateInvoicebtn();
		ar.selectCustomer();
		cp.Search();
		//ar.verifyOrderProcess();
	}

	@Test(priority = 2)
	public void shouldClearFieldsWhenClearButtonClicked() {
		SoftAssert sAssert = new SoftAssert();
		cp.clickClearButton();
		cp.Search();
		sAssert.assertEquals(ar.mandatemessage(), "Customer is required.", "Mandatory message Not match for customer");
		sAssert.assertAll();
	}

	@Test(priority = 3)
	public void shouldCheckFuelChargeMandatoryForInvoiceCreation() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.selectCustomer();
		cp.Search();
		ar.clickSaveInvoice();
		Thread.sleep(2000);
		sAssert.assertEquals(ar.mandatemessage(), "Fuel Surcharge is required.",
				"Mandatory message Not match for fuel");
		sAssert.assertAll();
	}

	@Test(priority = 4, alwaysRun = true)
	public void shouldDisplayToastForMandatoryField() {
		SoftAssert sAssert = new SoftAssert();
		ar.addFuelCharge(DataRepo.COST);
		ar.clickSaveInvoice();
		sAssert.assertEquals(cp.captureToastMessage(), "Please select at least one record.",
				"Toast message not displyed.");
		sAssert.assertAll();
	}

//	@Test(priority = 5)
//	public void validateOrdersAndFuelChargesOnPaginationChange() throws TimeoutException, InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ar.selectCheckBox();
//		ar.changePagination();
//		sAssert.assertTrue(ar.checkGridData(),
//				"FAIL: Fuel charges changed or checkbox state not preserved after pagination.");
//		ar.selectCheckBox();
//		sAssert.assertAll();
//
//	}

	@Test(priority = 6, alwaysRun = true)
	public void shouldAllowDimWeightToBeEditable() {
		SoftAssert sAssert = new SoftAssert();
		//ar.selectFiftyRecord();
		Assert.assertTrue(ar.selectRegularOrder(), "FAIL: Dimensional Weight field is not editable for Regular order.");
	}

	@Test(priority = 7, alwaysRun = true)
	public void shouldCalculateFuelSurchargesCorrectly() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.addFuelCharge(DataRepo.COST);
		ar.selectMultipleOrders();
		sAssert.assertTrue(ar.verifyFuelSurcharges(0.02), "Fuel surcharge calculations failed.");
		sAssert.assertAll();

	}

	@Test(priority = 8, alwaysRun = true)
	public void shouldCalculateTotalPriceCorrectly() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.addAccessorialPrice();
		ar.TotalPriceFirstOrder = ar.getTotalPrice(1);
		System.out.println("Total " + ar.TotalPriceFirstOrder);
		ar.TotalPriceSecondOrder = ar.getTotalPrice(2);
		ar.TotalPriceThirdOrder = ar.getTotalPrice(3);
		sAssert.assertEquals(ar.TotalPriceFirstOrder, ar.PriceSum(1), "Total price mismatch in row 1");
		sAssert.assertEquals(ar.TotalPriceSecondOrder, ar.PriceSum(2), "Total price mismatch in row 2");
		sAssert.assertEquals(ar.TotalPriceThirdOrder, ar.PriceSum(3), "Total price mismatch in row 3");
		sAssert.assertAll();
	}

	@Test(priority = 9, alwaysRun = true)
	public void shouldCalculateSubTotalCorrectly() {
		SoftAssert sAssert = new SoftAssert();
		ar.SubTotal = ar.getSubTotal();
		double calSubTotal = ar.TotalPriceFirstOrder + ar.TotalPriceSecondOrder + ar.TotalPriceThirdOrder;
		calSubTotal = Math.round(calSubTotal * 100.0) / 100.0;
		sAssert.assertEquals(ar.SubTotal, calSubTotal, "SubTotal not calculated correctly.");
		sAssert.assertAll();
	}

	@Test(priority = 10, alwaysRun = true)
	public void shouldAddNewRowForDiscountWhenAddButtonClicked() {
		SoftAssert sAssert = new SoftAssert();
		ar.clickOnAddnewRowForDiscount();
		sAssert.assertTrue(ar.isRadioButtonDisplayed(), "FAIL: Discount row not added.");
		sAssert.assertAll();
	}

	@Test(priority = 11, dependsOnMethods = "shouldAddNewRowForDiscountWhenAddButtonClicked")
	public void shouldAllowPercentageToBeEditableAndAmountNonEditableWhenPercentageRadioButtonActive() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(ar.verifyPercentageFieldEditabel(),
				"Percentage field is not editabel or amount field is editabel.");
		sAssert.assertAll();
	}

	@Test(priority = 12)
	public void shouldAllowAmountFieldEditableAndPercentageNonEditableWhenAmountRadioButtonSelected() {
		SoftAssert sAssert = new SoftAssert();
		ar.clickAmountRadioBtnFirst();
		sAssert.assertTrue(ar.verifyAmountEditableAndPercentageNotEditable(),
				"Percentage field is editabel or amount field non editabel.");
		sAssert.assertAll();
	}

	@Test(priority = 13, alwaysRun = true)
	public void shouldAllowUserToDeleteDiscount() {
		SoftAssert sAssert = new SoftAssert();
		ar.clickAmountRadioBtnFirst();
		ar.addDiscountAmount(DataRepo.VALUE);
		ar.clickOnDeleteBtn();
		sAssert.assertTrue(ar.isRecordNotFoundDisplayed(), "Discount row not delete after click on delete");
		sAssert.assertAll();
	}

	@Test(priority = 14, dependsOnMethods = "shouldAllowUserToDeleteDiscount")
	public void shouldRemoveDiscountAmountWhenDiscountIsDeleted() {
		SoftAssert sAssert = new SoftAssert();
		ar.TotalDiscount = ar.getTotalDiscount();
		sAssert.assertEquals(ar.TotalDiscount, 0.00, "Discount not zero after row delete.");
		sAssert.assertAll();
	}

	@Test(priority = 15, alwaysRun = true)
	public void shouldAllowUserToAddDiscountInPercentage() {
		SoftAssert sAssert = new SoftAssert();
		ar.clickOnAddnewRowForDiscount();
		ar.addPercent("1.02");
		ar.TotalDiscount = ar.getTotalDiscount();
		sAssert.assertNotEquals(ar.TotalDiscount, 0.00, "Discount not zero after row delete.");
		sAssert.assertAll();
	}

	@Test(priority = 16, dependsOnMethods = "shouldAllowUserToAddDiscountInPercentage")
	public void shouldCalculateDiscountAmountCorrectlyOnSubTotal() {
		SoftAssert sAssert = new SoftAssert();
		double actualAmountFirstRow = ar.getAmountFromFirstRow();
		sAssert.assertEquals(actualAmountFirstRow, ar.calculateAmountOnPercenatage(1.02),
				"Discound amount calculated wrong on percentage");
		sAssert.assertAll();
	}

	@Test(priority = 17)
	public void shouldNotAllowAddingDiscountGreaterThanSubTotal() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.clickOnAddnewRowForDiscount();
		ar.clickAmountRadioBtnSecond();
		double addMorePrice = ar.getSubTotal() + 10;
		String priceAsString = String.format("%.2f", addMorePrice);
		ar.addDiscountAmountSecondRow(priceAsString);
		sAssert.assertEquals(cp.captureToastMessage(), "Discount percentage exceeds max limit.",
				"Discount exeed pop up not displyed.");
		sAssert.assertAll();
	}

	@Test(priority = 18)
	public void shouldAllowUserToAddDiscountDirectlyInAmountAndShowCorrectPercentage() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.addDiscountAmountSecondRow(DataRepo.VALUE);
		double actualPercentSecondRow = ar.getPercentFromSecondRow();
		sAssert.assertEquals(actualPercentSecondRow, ar.calculatePercentageFromAmount(10),
				"Percentage calculate wrong on amount");
		sAssert.assertAll();
	}

	@Test(priority = 19)
	public void shouldCalculateTotalDiscountCorrectlyFromSumOfDiscountRows() {
		SoftAssert sAssert = new SoftAssert();
		ar.TotalDiscount = ar.getTotalDiscount();
		System.out.println("Discount " + ar.TotalDiscount);
		double SumOfTheDiscount = ar.getAmtSumOnDiscountRow();
		sAssert.assertEquals(ar.TotalDiscount, SumOfTheDiscount, "Total discount calculation mismatch");
		sAssert.assertAll();
	}

	@Test(priority = 20)
	public void shouldDeductDiscountAmountFromInvoiceTotal() {
		SoftAssert sAssert = new SoftAssert();
		ar.InvoiceAmount = ar.getInvoiceAmount();
		ar.SubTotal = ar.getSubTotal();
		ar.TotalDiscount = ar.getTotalDiscount();
		double calInvoiceAmt = ar.SubTotal - ar.TotalDiscount;
		calInvoiceAmt = Math.round(calInvoiceAmt * 100.0) / 100.0;
		sAssert.assertEquals(ar.InvoiceAmount, calInvoiceAmt, "Discount amount not deduct from Invoice Amount.");
		sAssert.assertAll();
	}

	@Test(priority = 21)
	public void shouldMakeDescriptionMandatoryForAddOnAndDeduction() {
		SoftAssert sAssert = new SoftAssert();
		ar.clickOnAddnewRowForAddOns();
		ar.enterwt();
		ar.clickSaveInvoice();
		sAssert.assertEquals(cp.captureToastMessage(), "Please select add on deduction description",
				"Toast message not displyed for discription.");
		sAssert.assertAll();
	}

	@Test(priority = 22)
	public void shouldMakeDateMandatoryForAddOn() {
		SoftAssert sAssert = new SoftAssert();
		ar.selectDiscription();
		ar.clickSaveInvoice();
		sAssert.assertEquals(cp.captureToastMessage(), "Please select add on deduction date",
				"Toast message not displyed for date.");
		sAssert.assertAll();
	}

	@Test(priority = 23)
	public void shouldMakeAmountMandatoryForAddOns() {
		SoftAssert sAssert = new SoftAssert();
		ar.selectDate();
		ar.clickSaveInvoice();
		sAssert.assertEquals(cp.captureToastMessage(), "Please enter add on deduction amount",
				"Toast message not displyed for amount.");
		sAssert.assertAll();
	}

	@Test(priority = 24)
	public void shouldMakeQuantityMandatoryForAddOns() {
		SoftAssert sAssert = new SoftAssert();
		ar.addAmountIntoAddon("1.5");
		ar.clickSaveInvoice();
		sAssert.assertEquals(cp.captureToastMessage(), "Please enter add on deduction quantity",
				"Toast message not displyed for quantity.");
		sAssert.assertAll();
	}

	@Test(priority = 25)
	public void shouldNotAllowDecimalValuesInQuantity() {
		SoftAssert sAssert = new SoftAssert();
		ar.addQTYforAddon("1.1");
		String addedQty = ar.getQTY();
		sAssert.assertNotEquals(addedQty, "1.1", "Failed: Qty accepted decimal value.");
		sAssert.assertAll();
	}

	@Test(priority = 26)
	public void shouldCalculateTotalBasedOnAmtAndQty() {
		SoftAssert sAssert = new SoftAssert();
		double totalAddonForRow = ar.getAddOnTotalFromFirstRow();
		sAssert.assertNotEquals(totalAddonForRow, ar.calculateTotal(), "Total for row not calculate correctly.");
		sAssert.assertAll();
	}

	@Test(priority = 27)
	public void shouldAllowUserToDeleteAddOrDeduction() {
		SoftAssert sAssert = new SoftAssert();
		ar.deleteAddon();
		sAssert.assertTrue(ar.isRecordNotFoundDisplayed(), "Add on row not delete after click on delete");
		sAssert.assertAll();
	}

	@Test(priority = 28, dependsOnMethods = "shouldAllowUserToDeleteAddOrDeduction")
	public void shouldRemoveAddAndDeductionAmountAfterAddOnDeleted() {
		SoftAssert sAssert = new SoftAssert();
		ar.TotalAddandDeduction = ar.getTotalAddOns();
		sAssert.assertEquals(ar.TotalAddandDeduction, 0.00, "Discount not zero after row delete.");
		sAssert.assertAll();
	}

	@Test(priority = 29)
	public void shouldCalculateTotalAddOnDeductionCorrectly() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.clickOnAddnewRowForAddOns();
		ar.selectDiscription();
		ar.selectDate();
		ar.addAmountIntoAddon("1.5");
		ar.addQTYforAddon("1.1");
		ar.enterwt();
		ar.clickOnAddnewRowForAddOns();
		ar.clickOnAddnewRowForAddOns();// system error need to click twice
		ar.selectDiscription();
		ar.selectDate2ndRow();
		ar.addAmountIntoAddon2ndRow("-1");
		ar.addQTYforAddon2ndRow(DataRepo.COST);
		ar.enterwt();
		ar.TotalAddandDeduction = ar.getTotalAddOns();
		ar.calculataionOnTotal();
		sAssert.assertEquals(ar.TotalAddandDeduction, ar.calculataionOnTotal(),
				"Add on sum or difference not match with TotalAddonsDeduction amt");
		sAssert.assertAll();
	}

	@Test(priority = 30, alwaysRun = true)
	public void shouldDisplayTotalInvoiceAmountCorrectly() {
		SoftAssert sAssert = new SoftAssert();
		ar.InvoiceAmount = ar.getInvoiceAmount();
		sAssert.assertEquals(ar.InvoiceAmount, ar.calculateTotalInvoiceAmt(), "Final Invoice Amount not match");
		sAssert.assertAll();

	}

	@Test(priority = 31, alwaysRun = true)
	public void shouldAllowUserToCreateInvoice() {
		SoftAssert sAssert = new SoftAssert();
		ar.clickSaveInvoice();
		sAssert.assertEquals(cp.captureToastMessage(), "Invoice Details save successfully",
				"Invoice save toast not displayed.");
		sAssert.assertAll();
	}

	@Test(priority = 32, alwaysRun = true, dependsOnMethods = "shouldAllowUserToCreateInvoice")
	public void shouldIncrementCountAfterInvoiceCreation() {
		SoftAssert sAssert = new SoftAssert();
		afterCount = ar.getTotalEntriesCount();
		int diffint = afterCount - initialCount;
		ar.InvoiceNumber = ar.getInvoiceNumber();
		System.out.println("invoice no " + ar.InvoiceNumber);
		sAssert.assertEquals(diffint, 1, "Entry count did not increase by One for Invoice");
		sAssert.assertAll();
	}

	@Test(priority = 33, dependsOnMethods = "shouldIncrementCountAfterInvoiceCreation")
	public void shouldDisplaySelectedOrdersOnListingPage() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.clickOnExpandBtn();
		sAssert.assertTrue(ar.CheckGLOrdersOnAvailable(), "Created invoice orders not match on listing");
		sAssert.assertAll();
	}

	@Test(priority = 34, alwaysRun = true, dependsOnMethods = "shouldDisplaySelectedOrdersOnListingPage")
	public void shouldDisplayOrderDetailsOnViewButtonClick() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.clickOnviewBtn();
		sAssert.assertTrue(ar.CheckGLOrdersOnAvailable(), "Created invoice orders not match on view btn");
		sAssert.assertAll();
	}

	@Test(priority = 35, dependsOnMethods = "shouldDisplayOrderDetailsOnViewButtonClick")
	public void shouldDisplaySubTotalOnViewInvoice() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("Sub Total " + ar.SubTotal);
		sAssert.assertEquals(ar.getSubTotal(), ar.SubTotal, "Sub total MismatchOn view invoice");
		sAssert.assertAll();
	}

	@Test(priority = 36)
	public void shouldShowTotalInvoiceAmountWhenViewingInvoice() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("Total invoice on view" + ar.InvoiceAmount);
		sAssert.assertEquals(ar.getInvoiceAmount(), ar.InvoiceAmount, "Total invoice amount MismatchOn view invoice");
		sAssert.assertAll();
	}

	@Test(priority = 37)
	public void shouldShowTotalDiscountAndAddOnDeductionWhenViewingInvoice() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("Total discount " + ar.TotalDiscount);
		System.out.println("Total addons " + ar.TotalAddandDeduction);
		sAssert.assertEquals(ar.getTotalDiscount(), ar.TotalDiscount, "Total discount amount MismatchOn view invoice");
		sAssert.assertEquals(ar.getTotalAddOns(), ar.TotalAddandDeduction,
				"Total AddonDeduction amount MismatchOn view invoice");
		sAssert.assertAll();
	}

	@Test(priority = 38, dependsOnMethods = "shouldDisplaySubTotalOnViewInvoice")
	public void shouldNotAllowEditingSaveInvoiceButton() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(ar.isSaveInvoiceButtonDisabled(), "Save invoice Btn Should be non editabel");
		sAssert.assertAll();
	}

	@Test(priority = 39, alwaysRun = true, dependsOnMethods = "shouldDisplayOrderDetailsOnViewButtonClick")
	public void shouldNotAllowEditingCustomerAndSearchButtons() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.clickBackBtn();
		ar.clickOnEditInvoiceBtn();
		sAssert.assertTrue(ar.isCustomerndSearchBtnDisabled(), "Customer and SearchBtn Should Be Non editabel");
		sAssert.assertAll();
	}

	@Test(priority = 40)
	public void shouldDisplayInvoiceAmountWhileEditingInvoice() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("Total invoice edit " + ar.InvoiceAmount);
		sAssert.assertEquals(ar.getInvoiceAmount(), ar.InvoiceAmount, "Total invoice amount MismatchOn view invoice");
		sAssert.assertAll();
	}

	@Test(priority = 41, dependsOnMethods = "shouldDisplayInvoiceAmountWhileEditingInvoice")
	public void shouldMaintainGLOrdersSelectedWhileEditing() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(ar.checkGLOrdersAreProperlySelected(), "Invoice Created Order should be Selected");
		sAssert.assertAll();
	}

	@Test(priority = 42, dependsOnMethods = "shouldDisplayInvoiceAmountWhileEditingInvoice")
	public void shouldEnableEditingOfFuelChargeAndAccessorialPrice() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(ar.checkFiedlShouldBeEditabel(), "Fuel,Price,AccePrice should be Editabel");
		sAssert.assertAll();
	}

	@Test(priority = 43, dependsOnMethods = "shouldDisplayInvoiceAmountWhileEditingInvoice")
	public void shouldRecalculateInvoiceSubTotalOnOrderRemoval() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.SubTotal = ar.getSubTotal();
		ar.RemovedFirstorder(1);
		sAssert.assertEquals(ar.getSubTotal(), ar.minusAmountfromTotal(),
				"Total invoice amount MismatchOn view invoice");
		sAssert.assertAll();
	}

	@Test(priority = 44)
	public void shouldRecalculateDiscountInPercentageOnOrderRemoval() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		double actualAmountFirstRow = ar.getAmountFromFirstRow();
		double getPercetange = ar.getPercentage();
		System.out.println("get percentage " + getPercetange);
		sAssert.assertEquals(actualAmountFirstRow, ar.calculateAmountOnPercenatage(getPercetange),
				"Discound amount calculated wrong on percentage");
		sAssert.assertAll();
	}

	@Test(priority = 45, dependsOnMethods = "shouldDisplayInvoiceAmountWhileEditingInvoice")
	public void shouldShowCorrectTotalDiscountAfterDeletionOnEditPage() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.TotalDiscount = ar.getTotalDiscount();
		double getDiscountAmt = ar.getAmountFromFirstRow();
		ar.clickOnDeleteBtn();
		sAssert.assertEquals(ar.getTotalDiscount(), ar.TotalDiscount - getDiscountAmt,
				"Discount amount not deducted after discount delete");
		sAssert.assertAll();
	}

	@Test(priority = 46, dependsOnMethods = "shouldDisplayInvoiceAmountWhileEditingInvoice")
	public void shouldRecalculateTotalAddOnAfterDeletingDeductionOnEditPage() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.TotalAddandDeduction = ar.getTotalAddOns();
		double getAddOnAmtAmt = ar.getAddOnTotalFromFirstRow();
		ar.deleteAddon();
		sAssert.assertEquals(ar.getTotalAddOns(), ar.TotalAddandDeduction - getAddOnAmtAmt,
				"After delete addOns Total displyed wrong.");
		sAssert.assertAll();
	}

	@Test(priority = 47)
	public void shouldMakeRemovedOrderAvailableForInvoiceCreation() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.getCustomerName();
		System.out.println("customer name is: " + ar.customer);
		ar.clickSaveInvoice();
		ar.clickOnCreateInvoicebtn();
		ar.selectEditInvoiceCustomer();
		cp.Search();
	//	ar.selectFiftyRecord();
		sAssert.assertTrue(ar.checkOrderAvailability(), "Removed GL order not found in createdInvoice.");
		sAssert.assertAll();
	}

	@Test(priority = 48)
	public void shouldEnableAddingOrderFromEditInvoice() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.clickBackBtn();
		ar.clickOnEditInvoiceBtn();
		ar.selectOneOrderAndGetAmt();
		sAssert.assertEquals(ar.getSubTotal(), ar.getSumOfSelectedOrderAmounts(),
				"Sum of the selected order not match with Sub total.");
		sAssert.assertAll();
	}

	@Test(priority = 49, dependsOnMethods = "shouldEnableAddingOrderFromEditInvoice")
	public void shouldAllowUserToAddDiscountInEditInvoice() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.clickOnAddnewRowForDiscount();
		ar.TotalDiscount = ar.getTotalDiscount();
		ar.addPercentforSecondRow("1.05");
		sAssert.assertNotEquals(ar.getTotalDiscount(), ar.TotalDiscount,
				"Added disount amount not added in Total Discount");
		sAssert.assertAll();
	}

	@Test(priority = 50, dependsOnMethods = "shouldEnableAddingOrderFromEditInvoice")
	public void shouldAllowUserToAddAddOnDeductionsInEditInvoice() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.clickOnAddnewRowForAddOns();
		ar.TotalAddandDeduction = ar.getTotalAddOns();
		ar.selectDiscription();
		ar.selectDate2ndRow();
		ar.addAmountIntoAddon2ndRow("1.25");
		ar.addQTYforAddon2ndRow(DataRepo.COST);
		ar.enterwt();
		sAssert.assertNotEquals(ar.getTotalAddOns(), ar.TotalAddandDeduction,
				"Added addon/deduction amt not added in Total AddOns.");
		sAssert.assertAll();
	}

	@Test(priority = 51, alwaysRun = true)
	public void shouldAllowUserToUpdateInvoice() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.SubTotal = ar.getSubTotal();
		ar.TotalDiscount = ar.getTotalDiscount();
		ar.TotalAddandDeduction = ar.getTotalAddOns();
		ar.InvoiceAmount = ar.getInvoiceAmount();
		ar.clickSaveInvoice();
		sAssert.assertEquals(cp.captureToastMessage(), "Invoice Details save successfully",
				"Invoice save toast not displayed.");
		sAssert.assertAll();
	}

	@Test(priority = 52, dependsOnMethods = "shouldAllowUserToUpdateInvoice")
	public void shouldDisplayEditedInvoiceAmountOnViewPage() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.clickOnviewBtn();
	   
		sAssert.assertTrue(ar.CheckGLOrdersOnAvailable(), "Edit invoice orders not match on view btn");

		System.out.println("Sub Total " + ar.SubTotal);
		sAssert.assertEquals(ar.getSubTotal(), ar.SubTotal, "Edit invoice:Sub total MismatchOn view invoice");

		System.out.println("Total invoice on view" + ar.InvoiceAmount);
		sAssert.assertEquals(ar.getInvoiceAmount(), ar.InvoiceAmount,
				"Edit invoice:Total invoice amount MismatchOn view invoice");

		System.out.println("Total discount " + ar.TotalDiscount);
		System.out.println("Total addons " + ar.TotalAddandDeduction);
		sAssert.assertEquals(ar.getTotalDiscount(), ar.TotalDiscount,
				"Edit invoice:Total discount amount MismatchOn view invoice");
		sAssert.assertEquals(ar.getTotalAddOns(), ar.TotalAddandDeduction,
				"Edit invoice:Total AddonDeduction amount MismatchOn view invoice");
		 ar.assignAmountForEDICheck();
		sAssert.assertAll();
	}

	@Test(priority = 53)
	public void shouldNotAllowCreatingInvoiceForAlreadyAddedOrder() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.clickBackBtn();
		ar.clickOnCreateInvoicebtn();
		ar.selectEditInvoiceCustomer();
		cp.Search();
	//	ar.selectFiftyRecord();
		sAssert.assertTrue(ar.areSelectedOrdersAbsentInGrid(), "FAIL: Selected orders are still present in the grid.");
		sAssert.assertAll();
	}
	
	@Test(priority = 54, alwaysRun=true)
	public void shouldAllowUserToDownloadInvoiceDetailsInExcel() throws InterruptedException, FileNotFoundException {
		SoftAssert sAssert = new SoftAssert();
		   // ar.ARMenu(); //remove click on AR menu
		ar.ClickOnInvoiceMenu();
		ar.deleteExistingFiles();
		ar.exportDetailXLSfile();
		sAssert.assertTrue(ar.isFileDownloaded(), "FAIL: File was not downloaded. when export detaild XLS");
		sAssert.assertAll();
	}

}
