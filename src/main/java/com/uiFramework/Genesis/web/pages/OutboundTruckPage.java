package com.uiFramework.Genesis.web.pages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException; // <-- correct package
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.helper.WaitHelper;

public class OutboundTruckPage extends InbTruckPage {
	WebDriver driver = null;
	CommonMethods cm;
	CommonPage cp = null;
	OutboundPalletPage opp = null;
	WaitHelper wt;
	JavaScriptHelper js;
	SoftAssert sAssert = new SoftAssert();

	Map<String, String> obttruckData = new HashMap<>();
	Map<String, String> obttruckDataforEdit = new HashMap<>();
	int totalOrderPieces = 0;
	int totalOrderWeight = 0;
	public List<String> LDAcodes = new ArrayList<>();
	public String[] LDAcodesOnlist;
	public static String MobtTruckNo;

	private static final Logger logger = Logger.getLogger(OutboundTruckPage.class.getName());

	public OutboundTruckPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		this.cm = new CommonMethods(driver);
		this.wt = new WaitHelper(driver);
		cp = new CommonPage(driver);
		this.js = new JavaScriptHelper(driver);
		opp = new OutboundPalletPage(driver);
	}

	private By obtTruckMenu = By.xpath("//a[@href='/truck/outbound-truck']");
	// listing page xpath
	private By createTruck = By.id("outboundTruckCreateNewOrderBtn");
	private By getliTruckno = By.xpath("(//table/tbody/tr[2]/td[2])[2]");
	private By getliTruckname = By.xpath("(//table/tbody/tr[2]/td[1])[2]");
	private By licheckBox = By.xpath("//tbody[1]/tr[1]/td[1]");
	private By ilibutton = By.xpath("(//div[@title='Click to see pallets present in truck'])[1]");
	private By getliInboundpallet = By.xpath("(//tbody)[2]//tr//td[contains(., 'IBPL-')]");
	private By getliLTL = By.xpath("//tbody[1]/tr[1]/td[5]");
	private By getliTruckstatus = By.xpath("//table/tbody/tr[2]/td[7]");
	private By editBtn = By.xpath("(//*[@title='Click to edit'])[1]");
	private By searchfirstcontains = By.xpath("(//input[@placeholder='Contains...'])[2]");
	private By getliPicklocation = By.xpath("//table/tbody/tr[2]/td[9]");
	private By getliPalletqty = By.xpath("(//span[@class='MuiTypography-root MuiTypography-button css-rscwwo'])[1]");
	private By merkasarrived = By.xpath("//button[@title='Click to mark truck as arrived.']");
	private By deleteBtn = By.xpath("(//button[@title='Click to delete'])[1]");
	// create xpath
	private By getlda = By.xpath("//table/tbody/tr[2]/td[4]");
	private By searchLdacode = By.xpath("//table/thead/tr[2]/th[4]/div/input");
	private By ldacodefiltericon = By.xpath("//table/thead/tr[2]/th[4]/div/span");
	private By notContains = By.xpath("//li[text()='Not contains']");
	private By LTLtext = By.xpath("//span[contains(text(),'Select LTL')]");
	private By getTotalPieces = By.xpath("//input[@id='outboutTruckCreateHeadernumberOfPieces']");
	private By getPalletQty = By.xpath("//input[@id='outboutTruckCreateHeadernoOfPallets']");
	private By truckFrom = By.xpath("//div[@id='outboutTruckCreateHeadertruckFrom']");
	private By pickuplocation = By.xpath("//div[@id='outboutTruckCreateHeaderpickupLocation']");
	private By dropfor = By.xpath("//div[@id='outboutTruckCreateHeaderpalletFor']");
	// edit xpath
	private By getEditTruckno = By.xpath("(//*[@class='labelBoxK__labelData'])[2]");
	private By getEditTotalpieces = By.xpath("(//*[@class='labelBoxK__labelData'])[3]");
	private By getEditTotalwt = By.xpath("(//*[@class='labelBoxK__labelData'])[4]");
	// Note: getEditPickfrom defined in parent (InbTruckPage)
	private By getEditDropfor = By.xpath("(//*[@class='labelBoxK__labelData'])[6]");
	private By getEditPicklocation = By.xpath("(//*[@class='labelBoxK__labelData'])[7]");
	private By getEdit3rdparty = By.xpath("(//*[@class='p-dropdown-label p-inputtext'])[2]");
	private By getEditStatus = By.xpath("(//*[@class='labelBoxK__labelData'])[8]");
	private By getEditLTL = By.xpath("(//*[@class='p-dropdown-label p-inputtext'])[1]");
	private By updateBtn = By.xpath("//*[@title='Click to update the details']");
	private By removePallet = By.xpath("//button[@title='Click to remove pallets from truck']");
	private By addPallets = By.xpath("//button[@title='Click to add pallets into truck']");
	private By updateroute = By.xpath("//button[@title='Click to update route order']");

	/**
	 * Click Outbound Truck menu
	 */
	public void truckMenu() throws TimeoutException {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(barMenu, 10);
		wt.waitToClick(truckMenu, 10);
		driver.findElement(obtTruckMenu).click();
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Click create truck
	 */
	public void createTruck() throws TimeoutException {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		wt.waitToClick(createTruck, 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Search pallet using the last created OBPL
	 */
	public void searchPallet() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		logger.info("Truck create screen OBPL is " + opp.MOBPL);
		cp.searchColoumFilter(searchibpl, opp.MOBPL);
	}

	public void searchPallet1(String outboundPalletNo) throws InterruptedException {
		cp.waitForLoaderToDisappear();
		cp.searchColoumFilter(searchibpl, outboundPalletNo);
	}

	public String getMOBPLCreatingTrcuk() {
		return cp.getMandatoryText(inboundPallet);
	}

	public String getMobtTruckNo() {
		MobtTruckNo = cp.getMandatoryText(By.xpath("(//table/tbody/tr[2]/td[2])[2]"));
		logger.info("MobtTruckNo is " + MobtTruckNo);
		return MobtTruckNo;
	}

	/**
	 * Verify order process types in info grid for outbound (no Return/Closeout)
	 */
	public void checkOrderProcessForOutbound(int j) throws InterruptedException {
		List<WebElement> orderInfo = driver.findElements(iButton);

		for (int i = 0; i < orderInfo.size(); i++) {
			List<WebElement> refreshedButtons = driver.findElements(iButton);
			WebElement orderinfo = refreshedButtons.get(i);
			orderinfo.click();

			List<WebElement> cells = driver
					.findElements(By.xpath("(//tbody[@class='p-datatable-tbody'])[" + j + "]//tr//td[3]"));

			for (WebElement cell : cells) {
				String orderProcess = cell.getText().trim();
				logger.info("Process type: " + orderProcess);
				js.scrollIntoView(orderinfo);

				if (orderProcess.equalsIgnoreCase("Return") || orderProcess.equalsIgnoreCase("Closeout")) {
					Assert.fail("FAIL: GL Order contains 'Return' or 'Closeout' on outbound truck.");
				}
			}
			wt.waitToClick(closeInfo, 30);
		}
		cp.waitForLoaderToDisappear();
	}

	public boolean checkAddressfield() {
		WebElement addressField = driver.findElement(By.xpath("//*[@placeholder='Enter Address 1']"));
		return addressField.isEnabled() && !"true".equals(addressField.getAttribute("readonly"));
	}

	/**
	 * Filter out the current LDA to get a different one
	 */
	public void selectDifferentLDA() throws TimeoutException, InterruptedException {
		String LDA = cp.getMandatoryText(getlda);
		driver.findElement(ldacodefiltericon).click();
		driver.findElement(notContains).click();
		try {
			wt.waitForElement(loader, 3);
			cp.waitForLoaderToDisappear();
		} catch (Exception e) {
			logger.info("Error while applying column filter");
		}
		cp.searchColoumFilter(searchLdacode, LDA);
	}

	public boolean isDropdownCleared() {
		try {
			WebElement dropdownLabel = driver.findElement(LTLtext);
			return dropdownLabel.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void selectLTLforUPScase(String cost) {
		cp.waitForLoaderToDisappear();
		cp.moveToElementAndClick(LTLtext);
		driver.findElement(ddSearch).sendKeys("Rocky");
		driver.findElement(ddSearch).sendKeys(Keys.ARROW_DOWN);
		driver.findElement(ddSearch).sendKeys(Keys.ENTER);
		cp.waitForLoaderToDisappear();
		driver.findElement(costEstimate).clear();
		driver.findElement(costEstimate).sendKeys(cost);
	}

	public void selectNonMandatefields(String data) {
		try {
			cp.waitForLoaderToDisappear();
			driver.findElement(thirdParty).click();
			driver.findElement(selectFirst).click();
			String truckId = "AutomateTruck-" + String.format("%03d", (int) (Math.random() * 1000));
			cp.enterTruckName(truckId);
			driver.findElement(discription).click();
			driver.findElement(selectFirst).click();
			driver.findElement(emrresponseno).sendKeys(data);
			driver.findElement(specialinstruction).sendKeys(data);
		} catch (Exception e) {
			logger.info("3rd party billing & Non mandate data skip");
		}
	}

	/**
	 * Add first 3 pallets, verify totals, collect GL & LDA
	 */
	public void creatTruckwithCheckTotalPiecesandpallet() {
		glOrders.clear();
		checkRemovePalletTotalPiecesndpalletQtyforFirstOrder();
		List<WebElement> glOrderCells = driver.findElements(By.xpath("//*[@class='p-selection-column p-frozen-column']"));
		TotalWeight = 0;
		for (int i = 1; i <= 3 && i < glOrderCells.size(); i++) {
			try {
				iTotalPieces = Integer.parseInt(cp.getAttributeValue(getTotalPieces, "value"));
				iPalletQty = Integer.parseInt(cp.getAttributeValue(getPalletQty, "value"));

				WebElement checkbox = driver.findElement(By.xpath("//table/tbody/tr[" + (i) + "]/td[1]"));
				checkbox.click();
				js.scrollIntoView(checkbox);

				List<WebElement> glOrderElements = driver.findElements(By.xpath("//table/tbody/tr[" + i + "]/td[2]"));
				for (WebElement element : glOrderElements) {
					String key = "OutboundPallet" + i;
					String value = element.getText().trim();
					obttruckData.put(key, value);
				}

				palletAddCalculation(i);

				WebElement orderInfo = driver.findElement(
						By.xpath("(//div[@title='Click to see orders present in pallet'])[" + i + "]"));

				orderInfo.click();
				Thread.sleep(1000);
				List<WebElement> orderElements = driver.findElements(
						By.xpath("(//tbody[@class='p-datatable-tbody'])[2]//*[contains(text(), 'GL-')]"));

				wt.waitForElementVisibleWithPollingTimeForList(orderElements, 9, 500);

				for (WebElement order : orderElements) {
					glOrders.add(order.getText().trim());
				}
				wt.waitToClick(closeInfo, 30);
				logger.info("GL Orders found at index " + i + ": " + glOrders);

				Thread.sleep(1000);
				WebElement lda = driver.findElement(By.xpath("//tbody[1]/tr[" + i + "]/td[5]"));
				wt.waitForElementVisibleWithPollingTime(lda, 9, 500);
				String rawText = cp.getMandatoryText(By.xpath("//tbody[1]/tr[" + i + "]/td[5]"));
				String[] LDAcode = rawText.split("\\s*,\\s*");
				for (String code : LDAcode) {
					LDAcodes.add(code);
				}
			} catch (NoSuchElementException e) {
				logger.info("Truck creation NoSuchElementException: " + e);
			} catch (Exception e) {
				logger.info("General error at index " + i + ": " + e.getMessage());
			}
		}
		obttruckData.put("Totalweight", totalWeight);
		logger.info("LDA codes on creating truck: " + LDAcodes);
		logger.info("Total Weight in array " + obttruckData.get("Totalweight"));
		logger.info("GlOrders on creatTruckwithCheckToatlPiecesandpallet: " + glOrders);
		getObtTruckDetails();
		checkCheckBoxesSelectedAfterPagination();
	}

	public void verifyRouteOrderShouldbeEditable() {
		try {
			WebElement addressField = driver.findElement(By.xpath("//*[@placeholder='Route Order']"));
			boolean isEditable = addressField.isEnabled() && !"true".equals(addressField.getAttribute("readonly"));
			sAssert.assertTrue(isEditable, "Route order should be editable but it is not.");
			cp.waitAndClickWithJS(By.xpath("(//button[@title='Click to save data'])[2]"), 7);
			cp.waitForLoaderToDisappear();
		} catch (Exception e) {
			logger.info("LDA is same; route-order popup not displayed");
		}
	}

	public void getObtTruckDetails() {
		String trimLtl = cp.getMandatoryText(getLTL);
		trimLtl = trimLtl.contains("-") ? trimLtl.split("-", 2)[1].trim() : trimLtl.trim();
		String thirdrdparty = cp.getMandatoryText(getthirdparty);
		String totalPieces = cp.getAttributeValue(getTotalPieces, "value");
		String palletQty = cp.getAttributeValue(getPalletQty, "value");
		String pickupFrom = cp.getMandatoryText(truckFrom);
		wt.waitForElement(pickuplocation, 4);
		String pickupLocation = cp.getMandatoryText(pickuplocation).split(":")[0].trim();
		String dropFor = cp.getMandatoryText(dropfor);

		obttruckData.put("LTL", trimLtl);
		obttruckData.put("thirdparty", thirdrdparty);
		obttruckData.put("Totalpieces", totalPieces);
		obttruckData.put("PalletQty", palletQty);
		obttruckData.put("TruckFrom", pickupFrom);
		obttruckData.put("PickupLocation", pickupLocation);
		obttruckData.put("DropFor", dropFor);

		logger.info("Outbound truck data " + obttruckData);
	}

	public String palletQty() {
		return obttruckData.get("PalletQty");
	}

	public void checkRemovePalletTotalPiecesndpalletQtyforFirstOrder() {
		iTotalPieces = Integer.parseInt(cp.getAttributeValue(getTotalPieces, "value"));
		iPalletQty = Integer.parseInt(cp.getAttributeValue(getPalletQty, "value"));

		WebElement checkbox = driver.findElement(By.xpath("//table/tbody/tr[1]/td[1]"));
		checkbox.click();
		int orderPieces = sumOrderPieces();
		int orderPaletqty = Integer.parseInt(driver.findElement(By.xpath("//table/tbody/tr[1]/td[4]")).getText().trim());

		int ExpectedPieces = orderPieces - iTotalPieces;
		int ExpectedPalletQty = orderPaletqty - iPalletQty;

		iTotalPieces = Integer.parseInt(cp.getAttributeValue(getTotalPieces, "value"));
		iPalletQty = Integer.parseInt(cp.getAttributeValue(getPalletQty, "value"));
		logger.info("Expected Final Pieces when Removed: " + ExpectedPieces + ", Actual: " + iTotalPieces);
		logger.info("Expected Final PQTY when Removed: " + ExpectedPalletQty + ", Actual: " + iPalletQty);

		Assert.assertEquals(iTotalPieces, ExpectedPieces, "Mismatch in Total and final pieces when pallet removed");
		Assert.assertEquals(iPalletQty, ExpectedPalletQty, "Mismatch in Total and final Qty when pallet removed");
		checkbox.click();// unselect
	}

	public int sumOrderPieces() {
		driver.findElement(By.xpath("(//*[@title='Click to see orders present in pallet'])[1]")).click();
		int totalOrderPieces = 0;

		List<WebElement> cells = driver.findElements(By.xpath("(//tbody[@class='p-datatable-tbody'])[2]//tr//td[6]"));

		for (WebElement cell : cells) {
			String orderPieces = cell.getText().trim();
			try {
				int piece = Integer.parseInt(orderPieces);
				totalOrderPieces += piece;
			} catch (NumberFormatException e) {
				logger.info("Unable to parse piece: " + orderPieces);
			}
		}
		wt.waitToClick(closeInfo, 30);
		cp.waitForLoaderToDisappear();
		return totalOrderPieces;
	}

	public void palletAddCalculation(int i) {
		WebElement orderInfo = driver
				.findElement(By.xpath("(//div[@title='Click to see orders present in pallet'])[" + i + "]"));
		orderInfo.click();
		getPiecesandWeight();
		int weight = getWeightCount();

		TotalWeight += weight;
		totalWeight = String.valueOf(TotalWeight);

		int orderPieces = getPiecescount();
		int orderPaletqty = Integer.parseInt(driver.findElement(By.xpath("//table/tbody/tr[" + i + "]/td[4]")).getText().trim());

		int ExpectedPieces = iTotalPieces + orderPieces;
		int ExpectedPalletQty = iPalletQty + orderPaletqty;

		iTotalPieces = Integer.parseInt(cp.getAttributeValue(getTotalPieces, "value"));
		iPalletQty = Integer.parseInt(cp.getAttributeValue(getPalletQty, "value"));
		logger.info("Expected Final Pieces: " + ExpectedPieces + ", Actual: " + iTotalPieces);
		logger.info("Expected Final PQTY: " + ExpectedPalletQty + ", Actual: " + iPalletQty);

		Assert.assertEquals(iTotalPieces, ExpectedPieces, "Mismatch in Total pieces and final pieces!");
		Assert.assertEquals(iPalletQty, ExpectedPalletQty, "Mismatch in Total PQTY and final pallet PQTY!");
	}

	public void getPiecesandWeight() {
		List<WebElement> Pieces = driver.findElements(By.xpath("(//tbody[@class='p-datatable-tbody'])[2]//tr//td[6]"));
		totalOrderPieces = 0;
		totalOrderWeight = 0;

		for (WebElement cell : Pieces) {
			String orderPieces = cell.getText().trim();
			try {
				int piece = Integer.parseInt(orderPieces);
				totalOrderPieces += piece;
			} catch (NumberFormatException e) {
				logger.info("Unable to parse piece: " + orderPieces);
			}
		}

		List<WebElement> weights = driver.findElements(By.xpath("(//tbody[@class='p-datatable-tbody'])[2]//tr//td[7]"));
		for (WebElement cell : weights) {
			String orderWeight = cell.getText().trim();
			try {
				int weight = Integer.parseInt(orderWeight);
				totalOrderWeight += weight;
			} catch (NumberFormatException e) {
				logger.info("Unable to parse weight: " + orderWeight);
			}
		}
		wt.waitToClick(closeInfo, 30);
		cp.waitForLoaderToDisappear();
	}

	public int getPiecescount() {
		return totalOrderPieces;
	}

	public int getWeightCount() {
		return totalOrderWeight;
	}

	public String getObtTruckNondStatus() {
		cp.waitForLoaderToDisappear();
		String truckStatus = cp.getMandatoryText(getliTruckstatus);
		cp.getMandatoryText(getliTruckno); 
		return truckStatus;
	}

	public void verifyTruckDetailsandPalletOrder() {
		getObtTruckNondStatus();
		obttruckListData();
		getPalletandOrders();
		wt.waitToClick(closeInfo, 30);
		for (Map.Entry<String, String> entry : litruckData.entrySet()) {
			String expectedValue = entry.getValue();
			Assert.assertTrue(obttruckData.containsValue(expectedValue),
					"FAIL: Truck data mismatch with Listing page. Missing: " + expectedValue);
		}
		for (String actualOrder : liglOrders) {
			Assert.assertTrue(glOrders.contains(actualOrder),
					"FAIL: Expected list does not contain actual order: " + actualOrder);
		}
	}

	public void obttruckListData() {
		litruckData.clear();
		String liLTL = cp.getMandatoryText(getliLTL);
		String trimliPickup = cp.getMandatoryText(getliPicklocation);
		String liPickup = trimliPickup.split(":")[0].trim();

		litruckData.put("LTL", liLTL);
		litruckData.put("pickupLocation", liPickup);

		String rawText = cp.getMandatoryText(By.xpath("//tbody[1]/tr[1]/td[7]"));
		LDAcodesOnlist = rawText.split("\\s*,\\s*");

		logger.info("TruckData On listing screen: " + litruckData);
		logger.info("LDA codes On listing screen: " + Arrays.toString(LDAcodesOnlist));
	}

	public void editBtn() {
		cp.waitForLoaderToDisappear();
		driver.findElement(editBtn).click();
		cp.waitForLoaderToDisappear();
	}

	public void getObtTruckEditDetails() {
		truckDataforEdit.clear();
		try {
			String truckno = cp.getMandatoryText(getEditTruckno);
			String totalPieces = cp.getMandatoryText(getEditTotalpieces);
			String totalWeight = cp.getMandatoryText(getEditTotalwt);
			String pickupfrom = cp.getMandatoryText(getEditPickfrom);
			String trimPickuploca = cp.getMandatoryText(getEditPicklocation);
			String pickuplocation = trimPickuploca.split(":")[0].trim();
			String trimtruckLTL = cp.getMandatoryText(getEditLTL);
			String truckLTL = trimtruckLTL.contains(" - ") ? trimtruckLTL.split(" - ", 2)[1].trim() : trimtruckLTL;
			String truckStatus = cp.getMandatoryText(getEditStatus);
			String palletqty = cp.getMandatoryText(getEditPalletqty);
			String thridParty = cp.getMandatoryText(getEdit3rdparty);
			String dropfor = cp.getMandatoryText(getEditDropfor);

			truckDataforEdit.put("Truckno", truckno);
			truckDataforEdit.put("Totalpieces", totalPieces);
			truckDataforEdit.put("Totalweight", totalWeight);
			truckDataforEdit.put("Pickupfrom", pickupfrom);
			truckDataforEdit.put("Pickuplocation", pickuplocation);
			truckDataforEdit.put("TruckLTL", truckLTL);
			truckDataforEdit.put("Truckstatus", truckStatus);
			truckDataforEdit.put("ThridParty", thridParty);
			truckDataforEdit.put("DropFor", dropfor);
		} catch (Exception ignored) {}

		List<WebElement> pallets = driver
				.findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//td[contains(text(), 'OBPL-')]"));
		int i = 1;
		for (WebElement element : pallets) {
			String key = "outboundPallet" + i;
			String value = element.getText().trim();
			truckDataforEdit.put(key, value);
			i++;
		}
		logger.info("Truck Data on Edit for outbound truck: " + truckDataforEdit);
	}

	public void validateTruckCreatedDatainEditTruck() {
		getObtTruckEditDetails();
		Assert.assertFalse(obttruckData.isEmpty(), "FAIL: truckData is empty, no data to validate.");
		logger.info("Truck Data on create outbound truck: " + obttruckData);

		for (Map.Entry<String, String> entry : obttruckData.entrySet()) {
			String key = entry.getKey();
			String truckdataValue = entry.getValue();
			if ("PalletQty".equalsIgnoreCase(key)) continue;

			Assert.assertTrue(truckDataforEdit.containsValue(truckdataValue),
					"FAIL: Truck data mismatch on Edit truck page. Missing: " + truckdataValue);
		}
	}

	public boolean checkLTL() {
		WebElement dropdown = driver.findElement(By.id("outboutTruckMappingLTL"));
		return !"true".equalsIgnoreCase(dropdown.getAttribute("data-p-disabled"));
	}

	public boolean check3rdPartyBilling() {
		WebElement dropdown = driver.findElement(By.id("outboutTruckMappingthirdPartyBilling"));
		return !"true".equalsIgnoreCase(dropdown.getAttribute("data-p-disabled"));
	}

	public boolean checkCostEstimateEditable() {
		WebElement costInput = driver.findElement(By.id("outboutTruckMappingcostEstimate"));
		return costInput.isEnabled() && !"true".equalsIgnoreCase(costInput.getAttribute("readonly"));
	}

	public String checkLDAaddress() {
		WebElement LDAaddress = driver.findElement(By.xpath("(//*[@class='labelBoxK__labelData'])[10]"));
		String addressText = LDAaddress.getText().trim();

		if (addressText == null || addressText.isEmpty()) {
			driver.findElement(getEditLTL).click();
			driver.findElement(selectFirst).click();
			cp.waitForLoaderToDisappear();
			driver.findElement(updateBtn).click();
			return cp.captureToastMessage();
		} else {
			throw new SkipException("LDA address is present. Skipping test. UPS case");
		}
	}

	public void checkPiecesanWeightWhenPalletRemoved() throws InterruptedException {
		addedPallets.clear();
		removedPallets.clear();
		List<WebElement> ObplOrderCells = driver
				.findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//td[contains(text(), 'OBPL-')]"));
		if (ObplOrderCells.size() > 1) {
			for (int i = ObplOrderCells.size(); i >= 2; i--) {
				try {
					WebElement checkbox = driver
							.findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[" + i + "]//td[1]"));
					checkbox.click();

					RemovedPalletCalculationforPiecesWeightQTY(i);

					logger.info("Removing pallets from outbound truck: " + addedPallets);
				} catch (NoSuchElementException e) {
					logger.info("Truck edit exception while removing pallets " + e);
				}
			}

			List<WebElement> removedPalleteElement = driver
					.findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[2]//td[contains(text(), 'OBPL-')]"));
			if (removedPalleteElement.isEmpty()) {
				logger.info("No pallets found in removed grid");
			} else {
				for (WebElement ipblpallet : removedPalleteElement) {
					removedPallets.add(ipblpallet.getText());
				}
			}
			logger.info("Pallets available to add in outbound truck: " + removedPallets);
			for (String pallet : addedPallets) {
				Assert.assertTrue(removedPallets.contains(pallet), "Missing pallet: " + pallet);
			}
			RemovelastPalletFromTruck();
		} else {
			logger.info("Pallets not available to remove from Truck");
			RemovelastPalletFromTruck();
		}
	}

	public void RemovelastPalletFromTruck() {
		driver.findElement(firstchekbox).click();
		getPallet();
		int initialPieces = safeParseInt(driver.findElement(getEditTotalpieces).getText());
		int initialWeight = safeParseInt(driver.findElement(getEditTotalwt).getText());

		int palletPieces = Integer.parseInt(
				driver.findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[1]//td[7]")).getText().trim());
		int palletWeight = Integer.parseInt(
				driver.findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[1]//td[8]")).getText().trim());

		int FinalPieces = initialPieces - palletPieces;
		int FinalWeight = initialWeight - palletWeight;

		driver.findElement(removePallet).click();
		cp.waitForLoaderToDisappear();
		cp.captureToastMessage();

		initialPieces = safeParseInt(driver.findElement(getEditTotalpieces).getText());
		initialWeight = safeParseInt(driver.findElement(getEditTotalwt).getText());

		logger.info("Expected Final Pieces: " + FinalPieces + ", Actual: " + initialPieces);
		logger.info("Expected Final Weight: " + FinalWeight + ", Actual: " + initialWeight);

		Assert.assertEquals(initialPieces, FinalPieces, "Mismatch in Total and final pieces!");
		Assert.assertEquals(initialWeight, FinalWeight, "Mismatch in Total and final weight!");
	}

	public int safeParseInt(String value) {
		if (value == null || value.trim().isEmpty()) {
			return 0;
		}
		return Integer.parseInt(value.trim());
	}

	public String getPallet() {
		LastPallet = cp.getMandatoryText(firstpallet);
		logger.info("Removed pallet from truck (last pallet): " + LastPallet);
		return LastPallet;
	}

	public void RemovedPalletCalculationforPiecesWeightQTY(int i) {
		WebElement element = driver
				.findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[" + i + "]//td[2]"));
		addedPallets.add(element.getText());

		int initialPieces = Integer.parseInt(driver.findElement(getEditTotalpieces).getText().trim());
		int initialWeight = Integer.parseInt(driver.findElement(getEditTotalwt).getText().trim());

		int palletPieces = Integer.parseInt(
				driver.findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[" + i + "]//td[7]"))
						.getText().trim());
		int palletWeight = Integer.parseInt(
				driver.findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[" + i + "]//td[8]"))
						.getText().trim());

		int FinalPieces = initialPieces - palletPieces;
		int FinalWeight = initialWeight - palletWeight;

		driver.findElement(removePallet).click();
		cp.waitForLoaderToDisappear();
		cp.captureToastMessage();
		cp.waitForPopupToDisappear();

		initialPieces = Integer.parseInt(driver.findElement(getEditTotalpieces).getText().trim());
		initialWeight = Integer.parseInt(driver.findElement(getEditTotalwt).getText().trim());

		logger.info("Expected Final Pieces: " + FinalPieces + ", Actual: " + initialPieces);
		logger.info("Expected Final Weight: " + FinalWeight + ", Actual: " + initialWeight);

		Assert.assertEquals(initialPieces, FinalPieces, "Mismatch in Total and final pieces!");
		Assert.assertEquals(initialWeight, FinalWeight, "Mismatch in Total and final weight!");
	}

	public String getTruckEditStatus() {
		cp.waitForLoaderToDisappear();
		return cp.getMandatoryText(getEditStatus).trim().toLowerCase();
	}

	public void searchTruckno1(String number) {
		cp.searchColoumFilter(searchfirstcontains, number);
	}

	public String getOBPL() {
		cp.waitForLoaderToDisappear();
		return LastPallet;
	}

	public void checkAddpalletFunctioninObtTruck() {
		addedPallets.clear();
		removedPallets.clear();
		List<WebElement> obplOrderCells = driver
				.findElements(By.xpath("(//div[@id='panel1bh-content'])[3]//td[contains(text(), 'OBPL-')]"));
		if (obplOrderCells.size() > 1) {
			for (int i = 3; i >= 1; i--) {
				try {
					WebElement checkbox = driver
							.findElement(By.xpath("(//div[@id='panel1bh-content'])[3]//tr[" + i + "]//td[1]"));
					checkbox.click();

					AddedPalletCalculationforPiecesWeightQTY(i);

					logger.info("Adding pallets to Outbound truck: " + addedPallets);
				} catch (NoSuchElementException e) {
					logger.info("Truck edit exception while adding " + e);
				}
			}
			List<WebElement> addPalleteElement = driver
					.findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//td[contains(text(), 'OBPL-')]"));
			if (addPalleteElement.isEmpty()) {
				logger.info("No pallets found in added grid");
			} else {
				for (WebElement opblpallet : addPalleteElement) {
					removedPallets.add(opblpallet.getText());
				}
			}
			logger.info("Added pallet into the outbound truck from edit " + removedPallets);
			for (String pallet : addedPallets) {
				Assert.assertTrue(removedPallets.contains(pallet), "Missing pallet: " + pallet);
			}
		} else {
			logger.info("Pallets not available to Add in Truck");
		}
	}

	public void AddedPalletCalculationforPiecesWeightQTY(int i) {
		WebElement element = driver.findElement(By.xpath("(//div[@id='panel1bh-content'])[3]//tr[" + i + "]//td[2]"));
		addedPallets.add(element.getText());

		int initialPieces = safeParseInt(driver.findElement(getEditTotalpieces).getText());
		int initialWeight = safeParseInt(driver.findElement(getEditTotalwt).getText());

		int palletPieces = Integer.parseInt(
				driver.findElement(By.xpath("(//div[@id='panel1bh-content'])[3]//tr[" + i + "]//td[7]")).getText().trim());
		int palletWeight = Integer.parseInt(
				driver.findElement(By.xpath("(//div[@id='panel1bh-content'])[3]//tr[" + i + "]//td[8]")).getText().trim());

		int FinalPieces = initialPieces + palletPieces;
		int FinalWeight = initialWeight + palletWeight;

		wt.waitToClick(addPallets, 10);

		try {
			cp.moveToElementAndClick(saveTruck);
		} catch (Exception ignored) {}
		cp.waitForLoaderToDisappear();
		cp.captureToastMessage();

		initialPieces = Integer.parseInt(driver.findElement(getEditTotalpieces).getText().trim());
		initialWeight = Integer.parseInt(driver.findElement(getEditTotalwt).getText().trim());

		logger.info("Expected Final Pieces: " + FinalPieces + ", Actual: " + initialPieces);
		logger.info("Expected Final Weight: " + FinalWeight + ", Actual: " + initialWeight);

		Assert.assertEquals(initialPieces, FinalPieces, "Mismatch in Total and final pieces!");
		Assert.assertEquals(initialWeight, FinalWeight, "Mismatch in Total and final weight!");
	}

	public boolean searchRemovedPalletsShowNoRecord() throws InterruptedException {
		cp.waitForLoaderToDisappear();

		for (int i = 0; i < 2 && i < removedPallets.size(); i++) {
			try {
				cp.moveToElementAndClick(searchibpl);
				cp.searchColoumFilter(searchibpl, removedPallets.get(i));
				logger.info("Searched for: " + removedPallets.get(i));
			} catch (Exception e) {
				logger.info("On Create truck element not found");
			}
			if (!checkElementNorecord()) {
				return false;
			}
		}
		return true;
	}

	public void updateCostestimate() throws InterruptedException {
		driver.findElement(costEstimate).clear();
		driver.findElement(costEstimate).sendKeys("10");
		Thread.sleep(2000);
		driver.findElement(updateBtn).click();
	}

	public String getEstimatedCost() {
		cp.waitForLoaderToDisappear();
		return cp.getAttributeValue(costEstimate, "value");
	}

	public void clickonUpdateBtn() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		wt.waitToClick(updateroute, 3);
	}

	public boolean isRouteOrderEditable() {
		return driver.findElement(By.xpath("//*[@placeholder='Route Order']")).isEnabled();
	}

	public String getObtTruckNo() {
		cp.waitForLoaderToDisappear();
		return cp.getMandatoryText(getliTruckno);
	}

	public void deleteBtn() {
		logger.info("Deleting Truck  Number:: " + getObtTruckNo());
		cp.waitForLoaderToDisappear();
		driver.findElement(deleteBtn).click();
		cp.waitAndClickWithJS(yesBtn, 5);
		cp.captureToastMessage();
	}

	public String emtyTruckStatsus() {
		WebElement statusElement = driver.findElement(By.xpath("//tbody[1]/tr[1]/td[10]"));
		return Truckstatus = statusElement.getText();
	}

	String captureToast;

	public void saveObtTruck() {
		cp.moveToElementAndClick(saveTruck);
		captureToast = cp.captureToastMessage();
	}

	public String TruckSavemessage() {
		return captureToast;
	}

	public WebElement verifyRemovePalleBtnDisable() {
		return driver.findElement(removePallet);
	}

	public WebElement verifyAddPalleBtnDisable() {
		return driver.findElement(addPallets);
	}

	public void searchAndValidateTruckNo() throws InterruptedException {
		cp.clickClearButton();
		try {
			cp.searchAndValidateTruckNo(getliTruckno);
		} catch (Exception e) {
			logger.info("Truck number filter skip");
		}
	}

	public void searchAndValidateTruckName() throws InterruptedException {
		cp.clickClearButton();
		try {
			cp.searchAndValidateTruckName(getliTruckname);
		} catch (Exception e) {
			logger.info("Truck name filter skip");
		}
	}

	public void paginationOnListingPageforOutboundTruck() {
		cp.paginationTest(By.xpath("//*[@title='Click to edit']"), 4);
		cp.waitForLoaderToDisappear();
	}

	public void paginationOnOutboundCreateTruck() {
		cp.waitForLoaderToDisappear();
		cp.paginationTest(By.xpath("//td[contains(text(), 'OBPL-')]"), 5);
		cp.waitForLoaderToDisappear();
	}

	public void checkColoumFilterforTruck() throws InterruptedException, IOException {
		String commonPath = System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\Locators.JSON";

		String jsonContent = new String(Files.readAllBytes(Paths.get(commonPath)));
		JSONObject root = new JSONObject(jsonContent);

		String pageName = "outboundTruckPage";

		if (root.has("pages") && root.getJSONObject("pages").has(pageName)) {

			JSONObject pageData = root.getJSONObject("pages").getJSONObject(pageName);

			JSONArray filters = pageData.getJSONArray("columns");

			for (int i = 0; i < filters.length(); i++) {
				JSONObject filter = filters.getJSONObject(i);

				String firstRowXPath = filter.getString("firstRowData");
				String filterIconXPath = filter.getString("filterIcon");
				String filterInputXPath = filter.getString("filterInput");
				String tableRowsXPath = filter.getString("tableData");
				int columnIndex = filter.getInt("columnIndex");
				String method = filter.getString("validationType");

				if (method.equals("column")) {
					cp.validateColumnFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath,
							columnIndex);
				} else if (method.equals("numeric")) {
					cp.validateNumericFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath,
							columnIndex);
				} else if (method.equals("date")) {
					cp.validateDateFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath,
							columnIndex);
				}
			}
		} else {
			logger.info("Page not found: " + pageName);
		}
	}

	public void checkColoumFilterforCreateTruck() throws InterruptedException, IOException {
		String commonPath = System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\Locators.JSON";

		String jsonContent = new String(Files.readAllBytes(Paths.get(commonPath)));
		JSONObject root = new JSONObject(jsonContent);

		String pageName = "outboundTruckCreatePage";

		if (root.has("pages") && root.getJSONObject("pages").has(pageName)) {

			JSONObject pageData = root.getJSONObject("pages").getJSONObject(pageName);

			JSONArray filters = pageData.getJSONArray("columns");

			for (int i = 0; i < filters.length(); i++) {
				JSONObject filter = filters.getJSONObject(i);

				String firstRowXPath = filter.getString("firstRowData");
				String filterIconXPath = filter.getString("filterIcon");
				String filterInputXPath = filter.getString("filterInput");
				String tableRowsXPath = filter.getString("tableData");
				int columnIndex = filter.getInt("columnIndex");
				String method = filter.getString("validationType");

				if (method.equals("column")) {
					cp.validateColumnFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath,
							columnIndex);
				} else if (method.equals("numeric")) {
					cp.validateNumericFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath,
							columnIndex);
				} else if (method.equals("date")) {
					cp.validateDateFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath,
							columnIndex);
				}
			}
		} else {
			logger.info("Page not found: " + pageName);
		}
	}
}
