package com.uiFramework.Genesis.regression.web;

import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.web.pages.ARManualPage;
import com.uiFramework.Genesis.web.pages.AccountsReceivable;
import com.uiFramework.Genesis.web.pages.CommonPage;

public class ARManualInputTest extends TestBase {
	ARManualPage arm = null;
	AccountsReceivable ar = null;
	protected static final Logger logger = Logger.getLogger(ARManualInputTest.class.getName());
	CommonPage cp = null;
	CommonMethods cm;

	@BeforeClass(alwaysRun = true)
	public void beforeclass() {
		test = extent.createTest(getClass().getSimpleName());
		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		arm = new ARManualPage(driver);
		ar = new AccountsReceivable(driver);
		//driver.navigate().refresh();

	}

	@Test(priority = 1, alwaysRun = true)
	public void shouldMakeCustomerFieldMandatory() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		   // ar.ARMenu();// Hide while run suite
		arm.ARInputMenu();
		cp.Search();
		sAssert.assertEquals(ar.mandatemessage(), "Customer is required.", "Mandatory message Not match for customer");
		sAssert.assertAll();
	}

	@Test(priority = 2, alwaysRun = true)
	public void shouldDisplaySaveAndClearButtonsForEachOrder() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.selectCustomer();
		cp.Search();
		sAssert.assertTrue(arm.isSaveBtnDisplayedForEachRow(), "save button not displayed for each row");
		sAssert.assertAll();
	}

	@Test(priority = 3,dependsOnMethods="shouldDisplaySaveAndClearButtonsForEachOrder")
	public void shouldMakeAmountFieldMandatoryForSave() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		arm.clickOnsaveBtnForEmptyAmt();
		sAssert.assertEquals(cp.captureToastMessage(), "AR Manual Amount is Required.",
				" Amount required toast not found.");
		sAssert.assertAll();
	}

	@Test(priority = 4, alwaysRun = true,dependsOnMethods="shouldDisplaySaveAndClearButtonsForEachOrder")
	public void shouldAllowUserToEnterAndSaveAmount() {
		SoftAssert sAssert = new SoftAssert();
		arm.enterAmount("101");
		arm.clickOnfirstSaveBtn();
		sAssert.assertEquals(cp.captureToastMessage(), "AR Manual Amount Save Successfully.",
				"Toast msg not show for Amount save");
		sAssert.assertAll();
	}

	@Test(priority = 5, alwaysRun = true ,dependsOnMethods="shouldDisplaySaveAndClearButtonsForEachOrder")
	public void shouldShowCorrectEnteredAmount() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(arm.getAmount(), 101.00, "Enter Amount not match in Manual invoice amount");
		sAssert.assertAll();
	}

	@Test(priority = 6, alwaysRun = true ,dependsOnMethods="shouldDisplaySaveAndClearButtonsForEachOrder")
	public void shouldClearFieldsWhenClearButtonClicked() {
		SoftAssert sAssert = new SoftAssert();
		arm.clickOnClearBtn();
		sAssert.assertTrue(arm.checkAmount(), "Clear Button not working.");
		sAssert.assertAll();
	}

	@Test(priority = 7, alwaysRun = true ,dependsOnMethods="shouldDisplaySaveAndClearButtonsForEachOrder")
	public void shouldAllowUserToEnterNegativeAmount() {
		SoftAssert sAssert = new SoftAssert();
		arm.enterAmount("-105");
		arm.clickOnfirstSaveBtn();
		sAssert.assertEquals(cp.captureToastMessage(), "AR Manual Amount Save Successfully.",
				"Toast msg not show for Amount save");
		sAssert.assertEquals(arm.getAmount(), -105.00, "Enter minus Amount not match.");
		sAssert.assertAll();
	}

	@Test(priority = 8, alwaysRun = true ,dependsOnMethods="shouldDisplaySaveAndClearButtonsForEachOrder")
	public void shouldAllowUserToEditAmount() {
		SoftAssert sAssert = new SoftAssert();
		double beforeUpdate = arm.getAmount();
		arm.enterAmount("7009005");
		arm.clickOnfirstSaveBtn();
		sAssert.assertEquals(cp.captureToastMessage(), "AR Manual Amount Save Successfully.",
				"Toast msg not show for Amount save");
		sAssert.assertNotEquals(arm.getAmount(), beforeUpdate, "Enter minus Amount not match.");
		sAssert.assertAll();
	}

	@Test(priority = 9, alwaysRun = true ,dependsOnMethods="shouldDisplaySaveAndClearButtonsForEachOrder")
	public void shouldAllowUserToUpdateManualAmountForMultipleOrders() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(arm.enterAmountForMultipleOrder(), "When enter an amount toast Not displyed.");
		sAssert.assertAll();
	}

}
