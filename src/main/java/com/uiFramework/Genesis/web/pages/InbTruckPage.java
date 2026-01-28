package com.uiFramework.Genesis.web.pages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.helper.WaitHelper;

public class InbTruckPage extends InboundPalletPage {
	WebDriver driver = null;
	CommonMethods cm;
	CommonPage cp = null;
	WaitHelper wt;
	JavaScriptHelper js;
	Map<String, String> truckData = new HashMap<>();
	Map<String, String> litruckData = new HashMap<>();
	Map<String, String> truckDataforEdit = new HashMap<>();
	int iTotalPieces;
	int iPalletQty;
	int icount;
	int TotalWeight;
	String Truckstatus;
	String totalWeight;
	List<String> glOrders = new ArrayList<>();
	List<String> liglOrders = new ArrayList<>();
	List<String> addedPallets = new ArrayList<>();
	List<String> removedPallets = new ArrayList<>();
	String LastPallet;
	String glorder;
	public static String MTruckNo;
	private static final Logger logger = Logger.getLogger(InbTruckPage.class.getName());

	// Cross-browser helpers
	private final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
	private final boolean isSafari;
	private final WebDriverWait wait;

	public InbTruckPage(WebDriver driver) {
		super(driver); // add when extend page class
		this.driver = driver;
		this.cm = new CommonMethods(driver);
		this.wt = new WaitHelper(driver);
		this.cp = new CommonPage(driver);
		this.js = new JavaScriptHelper(driver);
		this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(40));
		this.isSafari = getBrowserName(driver).contains("safari");
	}

	private static String getBrowserName(WebDriver d) {
		try {
			Capabilities caps = ((RemoteWebDriver) d).getCapabilities();
			return (caps.getBrowserName() != null) ? caps.getBrowserName().toLowerCase() : "";
		} catch (Exception e) {
			return "";
		}
	}

	private Keys selectAllModifier() {
		return isMac ? Keys.COMMAND : Keys.CONTROL;
	}

	private void clearElement(WebElement el) {
		try {
			el.sendKeys(Keys.chord(selectAllModifier(), "a"));
			el.sendKeys(Keys.DELETE);
		} catch (Exception ignore) {
			try {
				el.clear();
			} catch (Exception ignored) {
			}
		}
	}

	private void clearAndType(By locator, String text) {
		WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		el.click();
		clearElement(el);
		el.sendKeys(text);
	}

	private void safeClick(By locator) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
		} catch (Exception e) {
			// fallback to JS click (helps Safari / overlay cases)
			try {
				WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
			} catch (Exception ignored) {
			}
		}
	}

	private boolean isDisplayed(By locator, int seconds) {
		try {
			new WebDriverWait(driver, java.time.Duration.ofSeconds(seconds))
					.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return driver.findElement(locator).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	// create Screen Xpath
	By barMenu = By.xpath("//i[@title='Barcode Printing']");
	By truckMenu = By.xpath("//i[@title='Truck']");
	private By inboundTruckMenu = By.xpath("//a[@href='/truck/inbound-truck']");
	private By createTruck = By.id("inboundTruckCreateNewTruckBtn");
	private By enterTruckname = By.id("inboundTruckCreateInboundPallettruckName");
	private By savebtn = By.xpath("//button[@id='inboundTruckSubmitPalletBtn']");
	private By LTLtext = By.xpath("(//*[contains(text(),'Select LTL')])[2]");
	By thirdParty = By.xpath("//span[contains(text(),'Select 3rd Party Billing')]");
	By discription = By.xpath("//span[contains(text(),'Select Description')]");
	By emrresponseno = By.xpath("//input[@placeholder='Enter Emergency Response Number']");
	By specialinstruction = By.xpath("//*[@placeholder='Enter Special Instructions']");
	private By truckName = By.xpath("//input[@placeholder='Enter Truck Name']");
	private By pickuptext = By.xpath("//span[contains(text(),'Select Pickup Location')]");
	private By droptext = By.xpath("//span[contains(text(),'Select Drop Location')]");
	private By pickupFrom = By.xpath("//div[@id='inboundTruckCreateInboundPalletpickupFrom']");
	private By getTotalPieces = By.xpath("//input[@id='inboundTruckCreateInboundPallettotalPieces']");
	private By getPalletQty = By.xpath("//input[@id='inboundTruckCreateInboundPallettotalPallets']");
	By costEstimate = By.xpath("//input[@placeholder='Enter Cost Estimate']");
	By selectFirst = By.xpath("(//li[@class='p-dropdown-item'])[1]");// select random fist data/getText
	By ddSearch = By.xpath("//input[@class='p-dropdown-filter p-inputtext p-component']");// search dd value
	private By ddnoResult = By.xpath("//li[@class='p-dropdown-empty-message']");// search dd value
	// Grid xpath
	private By checkBoxfirst = By.xpath("//table/tbody/tr[2]/td[1]");
	By inboundPallet = By.xpath("(//table/tbody/tr[2]/td)[2]");
	private By palletQty = By.xpath("(//table/tbody/tr[2]/td[1])[2]");
	private By pickupLocation = By.xpath("//table/tbody/tr[2]/td[6]");// d
	private By dropLocation = By.xpath("//table/tbody/tr[2]/td[7]");
	private By infoGrid = By.xpath("(//tbody[@class='p-datatable-tbody'])[2]//tr//td[3]");
	By iButton = By.xpath("//div[@title='Click to see orders present in pallet']");
	By closeInfo = By.xpath("//*[@data-testid='CloseIcon']");
	private By closeInfo2 = By.xpath("(//*[@data-testid='CloseIcon'])[2]");
	By saveTruck = By.xpath("//button[@title='Click to save data']");
	// searchfilter
	By searchibpl = By.xpath("(//input[@placeholder='Contains...'])[1]");
	By getLTL = By.xpath("(//span[@class='p-dropdown-label p-inputtext'])[1]");
	By getthirdparty = By.xpath("(//span[@class='p-dropdown-label p-inputtext'])[2]");
	private By nextPagination = By.xpath("//button[@class='p-paginator-next p-paginator-element p-link']");
	private By backPagination = By.xpath("(//button[@aria-label='Page 1'])[1]");
	private By getliTruckno = By.xpath("(//tbody[1]/tr[2]/td[1])[2]");
	private By getliTruckname = By.xpath("(//tbody[1]/tr[2]/td[2])[2]");
	private By licheckBox = By.xpath("//tbody[1]/tr[2]/td[1]");
	private By ilibutton = By.xpath("(//div[@title='Click to see pallets present in truck'])[1]");
	private By getliInboundpallet = By.xpath("(//tbody)[2]//tr//td[contains(., 'IBPL-')]");
	private By getliLTL = By.xpath("//tbody[1]/tr[2]/td[4]");
	private By getliTruckstatus = By.xpath("//tbody[1]/tr[2]/td[6]");
	private By getliPickfrom = By.xpath("//tbody[1]/tr[2]/td[7]");
	private By getliPicklocation = By.xpath("//tbody[1]/tr[2]/td[8]");
	private By getliDroplocation = By.xpath("//tbody[1]/tr[2]/td[9]");
	private By getliPalletqty = By.xpath("(//span[@class='MuiTypography-root MuiTypography-button css-rscwwo'])[1]");
	private By merkasarrived = By.xpath("//button[@title='Click to mark truck as arrived.']");
	// edit xpath
	private By editBtn = By.xpath("(//button[@id='inboundTruckEditBtn'])[1]");
	private By getEditTruckno = By.xpath("(//*[@class='labelBoxK__labelData'])[1]");
	private By getEditTotalpieces = By.xpath("(//*[@class='labelBoxK__labelData'])[3]");
	private By getEditTotalwt = By.xpath("(//*[@class='labelBoxK__labelData'])[4]");
	By getEditPickfrom = By.xpath("(//*[@class='labelBoxK__labelData'])[5]");
	private By getEditPicklocation = By.xpath("(//*[@class='labelBoxK__labelData'])[6]");
	private By getEditDroplocation = By.xpath("(//*[@class='labelBoxK__labelData'])[7]");
	private By getEdit3rdparty = By.xpath("(//*[@class='labelBoxK__labelData'])[11]//following::span[1]");
	private By getEditStatus = By.xpath("(//*[@class='labelBoxK__labelData'])[2]");
	By getEditPalletqty = By.xpath("(//*[@class='labelBoxK__labelData'])[8]");
	private By removePallet = By.xpath("//button[@title='Click to remove Pallets from Truck']");
	private By addPallets = By.xpath("//button[@title='Click to add Pallet into Truck']");
	By firstchekbox = By.xpath("(//div[@class='p-checkbox p-component'])[1]");
	By firstpallet = By.xpath("(//div[@class='datatable-wrapper'])[1]//tr[1]//td[2]");
	By loader = By.xpath("//*[@src='/static/media/loading.db43a6dd94d78914920a.gif']");
	private By Norecord = By.xpath("//*[@class='noRcords']");
	private By searchstatus = By.xpath("(//input[@placeholder='Contains...'])[4]");
	private By searchfirstcoloum = By.xpath("(//input[@placeholder='Contains...'])[1]");
	private By deleteBtn = By.xpath("(//button[@title='Delete Truck'])[1]");
	By yesBtn = By.id("confmSubmit");
	private By getIbpl = By.xpath("(//tbody[@class='p-datatable-tbody'])//tr//td[2]");
	private By getIbplfromedit = By.xpath("(//div[@id='panel1bh-content'])[3]//tr[1]//td[2]");
	private By getglorder = By.xpath("((//*[@class='p-datatable-tbody']))[2]//tr[1]//td[2]");
	private By getOrderStatus = By.xpath("//table/tbody/tr/td[15]");

	/**
	 * Click on truck menu for inbound truck
	 */
	public void truckMenu() throws TimeoutException {
		cp.waitForLoaderToDisappear();
		safeClick(barMenu);
		safeClick(truckMenu);
		safeClick(inboundTruckMenu);
		cp.waitForLoaderToDisappear();
	}

	/** Get inbound pallet for end to end */
	public String getMIBPLCreatingTrcuk() {
		return cp.getMandatoryText(inboundPallet);
	}

	/** Search pallet to create truck */
	public void searchPallet() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		logger.info("Truck create screen ibpl is " + MIBPL);
		cp.searchColoumFilter(searchibpl, MIBPL);
	}

	/** Add truck mandatory fields */
	public void addTruckDetails(String ibpl) throws InterruptedException {
		getPickndDrop();
		selectPickupLocation();
		SelectDropLocation();
		cp.searchColoumFilter(searchibpl, ibpl);
		safeClick(checkBoxfirst);
		saveTruck();
	}

	/** Get Truck number where main order is available */
	public String getMTruckNo() {
		MTruckNo = cp.getMandatoryText(By.xpath("(//table/tbody/tr[2]/td[1])[2]"));
		logger.info("MTruckNo is " + MTruckNo);
		return MTruckNo;
	}

	/** Click create truck */
	public void createTruck() throws TimeoutException {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		wt.waitToClick(createTruck, 10);
		cp.waitForLoaderToDisappear();
	}

	/** Get cost estimate value */
	public String getCosEstimate() {
		return cp.getAttributeValue(costEstimate, "value");
	}

	/** Validate order process not Reconsignment */
	public void checkOrderProcessforinboundTruck() throws InterruptedException {
	try {	
		List<WebElement> orderInfo = driver.findElements(iButton);

		for (int i = 0; i < orderInfo.size(); i++) {
			List<WebElement> refreshedButtons = driver.findElements(iButton);
			WebElement orderinfo = refreshedButtons.get(i);
			orderinfo.click();

			List<WebElement> cells = driver
					.findElements(By.xpath("(//tbody[@class='p-datatable-tbody'])//td[3]"));

			for (WebElement cell : cells) {
				String orderProcess = cell.getText().trim();
				logger.info("Process type: " + orderProcess);
				js.scrollIntoView(orderinfo);

				if (orderProcess.equalsIgnoreCase("Reconsignment")) {
					Assert.fail("'Reconsignment' Process Type shown in Inbound Truck: Test Failed");
				}
			}
			Thread.sleep(500);
			wt.waitToClick(closeInfo, 30);
		}
		
		cp.waitForLoaderToDisappear();
	} catch (Exception e) {
		// TODO: handle exception
	}
		
	}

	/** Get pickup and drop from create truck grid */
	public void getPickndDrop() {
		String trimpickup = cp.getMandatoryText(pickupLocation);
		String pickup = trimpickup.substring(0, trimpickup.indexOf(",") + 1);
		String drop = cp.getMandatoryText(dropLocation);
		String DropWH = drop.split(":")[0].trim();

		truckData.put("pickupFrom", pickup);
		truckData.put("DropTo", DropWH);
		logger.info("order data " + truckData);
	}

	/** Select LTL and non-mandatory fields */
	public void selectLTL(String cost, String data) {
		cp.waitForLoaderToDisappear();
		cp.moveToElementAndClick(LTLtext);
		safeClick(selectFirst);
		cp.waitForLoaderToDisappear();
		WebElement ce = driver.findElement(costEstimate);
		clearElement(ce);
		ce.sendKeys(cost);
		selectNonMandatefields(data);
	}

	/** Select 3rd party & other non-mandatory fields */
	public void selectNonMandatefields(String data) {
		try {
			safeClick(thirdParty);
			safeClick(selectFirst);
			String truckId = "AutomateTruck-" + String.format("%03d", (int) (Math.random() * 1000));
			cp.enterTruckName(truckId);
			safeClick(discription);
			safeClick(selectFirst);
			driver.findElement(emrresponseno).sendKeys(data);
			driver.findElement(specialinstruction).sendKeys(data);
		} catch (Exception e) {
			logger.info("3rd party billing & Non-mandatory data skipped");
		}
	}

	public void searchndSelect(String value) {
		WebElement box = driver.findElement(ddSearch);
		clearElement(box);
		box.sendKeys("Distributor");
		box.sendKeys(Keys.ARROW_DOWN);
		box.sendKeys(Keys.ENTER);
	}

	/** Select drop location */
	public void SelectDropLocation() throws InterruptedException {
		String DropLocation = truckData.get("DropTo");
		safeClick(droptext);
		WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(ddSearch));
		clearElement(searchBox);
		Thread.sleep(700);
		searchBox.sendKeys(DropLocation);
		searchBox.sendKeys(Keys.ARROW_DOWN);
		searchBox.sendKeys(Keys.ENTER);
		cp.waitForLoaderToDisappear();
	}

	/** Select pickup location with fallback to Distributor/LDA */
	public void selectPickupLocation() throws InterruptedException {
		String location = truckData.get("pickupFrom");
		if (tryToSelectLocation(location))
			return;

		logger.info("Customer Location not found, checking Distributor");
		Thread.sleep(500);
		switchPickupSource("Distributor");
		if (tryToSelectLocation(location))
			return;

		logger.info("Distributor Location not found, checking LDA");
		Thread.sleep(500);
		switchPickupSource("LDA");
		if (tryToSelectLocation(location))
			return;

		logger.info("Location not found in Customer, Distributor or LDA");
	}

	private boolean tryToSelectLocation(String location) {
		try {
			safeClick(pickuptext);
			WebElement box = wait.until(ExpectedConditions.visibilityOfElementLocated(ddSearch));
			clearElement(box);
			box.sendKeys(location);

			List<WebElement> dropdownItems = driver.findElements(
					By.xpath("//li[contains(@class,'dropdown-item') or contains(@class,'p-dropdown-item')]"));
			if (dropdownItems.isEmpty()) {
				logger.info("Dropdown empty after typing: " + location);
				return false;
			}
			box.sendKeys(Keys.ARROW_DOWN);
			box.sendKeys(Keys.ENTER);
			cp.waitForLoaderToDisappear();
			return true;
		} catch (Exception e) {
			logger.info("Exception in tryToSelectLocation: " + e.getMessage());
			return false;
		}
	}

	private void switchPickupSource(String sourceName) {
		try {
			Thread.sleep(300);
			wt.waitToClick(pickupFrom, 5);
			WebElement box = wait.until(ExpectedConditions.visibilityOfElementLocated(ddSearch));
			clearElement(box);
			box.sendKeys(sourceName);
			wt.waitToClick(By.xpath("//*[@class='p-dropdown-item-label']"), 10);
			cp.waitForLoaderToDisappear();
		} catch (Exception e) {
			logger.info("Failed to switch source to: " + sourceName + " | " + e.getMessage());
		}
	}
	

	/** Create truck + check totals */
	public void creatTruckwithCheckTotalPiecesandpallet() {
		checkRemovePalletTotalPiecesndpalletQtyforFirstOrder();
		List<WebElement> glOrderCells = driver.findElements(By.xpath(
				"//table/tbody/tr//td[1]//*[@type='checkbox']"));
		TotalWeight = 0;
		for (int i = 1; i <= 3 && i < glOrderCells.size(); i++) {
			try {
				iTotalPieces = Integer.parseInt(cp.getAttributeValue(getTotalPieces, "value"));
				iPalletQty = Integer.parseInt(cp.getAttributeValue(getPalletQty, "value"));
				
				WebElement checkbox = driver.findElement(By.xpath("//table/tbody/tr[" + (i+1) + "]/td[1]"));
				checkbox.click();
				js.scrollIntoView(checkbox);

				List<WebElement> glOrderElements = driver.findElements(By.xpath("(//table/tbody/tr["+( i+1 )+"]/td[2])[1]"));
				for (WebElement element : glOrderElements) {
					String key = "inboundPallet" + i;
					String value = element.getText().trim();
					truckData.put(key, value);
				}

				palletAddCalculation(i);

				WebElement orderInfo = driver
						.findElement(By.xpath("(//div[@title='Click to see orders present in pallet'])[" + i + "]"));
				orderInfo.click();

				List<WebElement> orderElements = driver
						.findElements(By.xpath("//tbody[@class='p-datatable-tbody']//td[2]"));

				for (WebElement order : orderElements) {
					glOrders.add(order.getText().trim());
				}
				wt.waitToClick(closeInfo, 30);
				logger.info("GL Orders found at index " + i + ": " + glOrders);

			} catch (NoSuchElementException e) {
				logger.info("Truck creation NoSuchElementException: " + e);
			} catch (Exception e) {
				logger.info("General error at index " + i + ": " + e.getMessage());
			}
		}
		truckData.put("Totalweight", totalWeight);
		logger.info("Total Weight in array " + truckData.get("Totalweight"));
		logger.info("GlOrders on create: " + glOrders);
		getTruckDetails();
	}

	/** Click save truck */
	String ToastOnTruckCreate;

	public void saveTruck() {
		wt.waitToClickWithAction(saveTruck, 10);
		ToastOnTruckCreate = cp.captureToastMessage();
		cp.waitForLoaderToDisappear();
	}

	public String CreateTruckToast() {
		return ToastOnTruckCreate;
	}

	/** Capture truck details after create */
	public void getTruckDetails() {
		String trimLtl = cp.getMandatoryText(getLTL);
		trimLtl = trimLtl.contains("-") ? trimLtl.split("-", 2)[1].trim() : trimLtl.trim();
		String thirdrdparty = cp.getMandatoryText(getthirdparty);
		String totalPieces = cp.getAttributeValue(getTotalPieces, "value");
		String palletQty = cp.getAttributeValue(getPalletQty, "value");
		String pickupfrom = cp.getMandatoryText(pickupFrom);

		truckData.put("LTL", trimLtl);
		truckData.put("thirdparty", thirdrdparty);
		truckData.put("Totalpieces", totalPieces);
		truckData.put("PalletQty", palletQty);
		truckData.put("PickFrom", pickupfrom);
		logger.info("order data " + truckData);
	}

	public String palletQty() {
		return truckData.get("PalletQty");
	}

	public String thrirdParty() {
		return truckData.get("thirdparty");
	}

	public String totalPieces() {
		return truckData.get("Totalpieces");
	}

	/** Check weight & pieces while adding pallet */
	public void palletAddCalculation(int i) {
		String weightStr = driver.findElement(By.xpath("//table/tbody/tr[" + (i+1) + "]/td[4]")).getText().trim();
		int weight = Integer.parseInt(weightStr);
		TotalWeight += weight;
		totalWeight = String.valueOf(TotalWeight);

		int orderPieces = Integer
				.parseInt(driver.findElement(By.xpath("//table/tbody/tr[" + (i+1) + "]/td[3]")).getText().trim());
		int orderPaletqty = Integer
				.parseInt(driver.findElement(By.xpath("(//table/tbody/tr[" + (i+1) + "]//td[1])[2]")).getText().trim());

		int ExpectedPieces = iTotalPieces + orderPieces;
		int ExpectedPalletQty = iPalletQty + orderPaletqty;

		iTotalPieces = Integer.parseInt(cp.getAttributeValue(getTotalPieces, "value"));
		iPalletQty = Integer.parseInt(cp.getAttributeValue(getPalletQty, "value"));

		logger.info("Expected Final Pieces: " + ExpectedPieces + ", Actual: " + iTotalPieces);
		logger.info("Expected Final PQTY: " + ExpectedPalletQty + ", Actual: " + iPalletQty);

		Assert.assertEquals(iTotalPieces, ExpectedPieces, "Mismatch in Total and final pieces!");
		Assert.assertEquals(iPalletQty, ExpectedPalletQty, "Mismatch in Total and final pallet qty!");
	}

	/** Check selected after pagination */
//	public void checkCheckBoxesSelectedAfterPagination() {
//		safeClick(nextPagination);
//		cp.waitForLoaderToDisappear();
//		safeClick(backPagination);
//		cp.waitForLoaderToDisappear();
//		WebElement checkbox = driver.findElement(By.xpath("//div[@class='p-checkbox p-component p-highlight']"));
//		String classValue = checkbox.getAttribute("class");
//		Assert.assertTrue(classValue.contains("p-highlight"), "Checkbox is NOT highlighted (selected).");
//	}

	/** Remove pallet calculation */
	public void checkRemovePalletTotalPiecesndpalletQtyforFirstOrder() {
		iTotalPieces = Integer.parseInt(cp.getAttributeValue(getTotalPieces, "value"));
		iPalletQty = Integer.parseInt(cp.getAttributeValue(getPalletQty, "value"));

		WebElement checkbox = driver.findElement(By.xpath("//table/tbody/tr[2]/td[1]"));
		checkbox.click();

		int orderPieces = Integer.parseInt(driver.findElement(By.xpath("//table/tbody/tr[2]/td[3]")).getText().trim());
		int orderPaletqty = Integer
				.parseInt(driver.findElement(By.xpath("(//table/tbody/tr[2]/td[1])[2]")).getText().trim());

		int ExpectedPieces = orderPieces - iTotalPieces;
		int ExpectedPalletQty = orderPaletqty - iPalletQty;

		iTotalPieces = Integer.parseInt(cp.getAttributeValue(getTotalPieces, "value"));
		iPalletQty = Integer.parseInt(cp.getAttributeValue(getPalletQty, "value"));

		logger.info("Expected Final Pieces when removed: " + ExpectedPieces + ", Actual: " + iTotalPieces);
		logger.info("Expected Final PQTY when removed: " + ExpectedPalletQty + ", Actual: " + iPalletQty);

		Assert.assertEquals(iTotalPieces, ExpectedPieces, "Mismatch in total pieces when pallet removed");
		Assert.assertEquals(iPalletQty, ExpectedPalletQty, "Mismatch in total qty when pallet removed");
		checkbox.click(); // unselect after calculation check
	}

	/** Get truck status (listing) */
	public String getTruckNondStatus() {
		String truckStatus = cp.getMandatoryText(getliTruckstatus).toLowerCase();
		String truckNo = cp.getMandatoryText(getliTruckno);
		logger.info("Truck number: " + truckNo);
		logger.info("Truck status: " + truckStatus);
		return truckStatus;
	}

	/** Capture listing truck data */
	public void truckListData() {
		String liLTL = cp.getMandatoryText(getliLTL);
		String liPickFrom = cp.getMandatoryText(getliPickfrom);
		String trimliPickup = cp.getMandatoryText(getliPicklocation);
		String liPickup = trimliPickup.substring(0, trimliPickup.indexOf(",") + 1);
		String liDrop = cp.getMandatoryText(getliDroplocation);
		String liDropWH = liDrop.split(":")[0].trim();

		litruckData.put("LTL", liLTL);
		litruckData.put("PickFrom", liPickFrom);
		litruckData.put("pickupLocation", liPickup);
		litruckData.put("DropLocation", liDropWH);
		logger.info("TruckData On listing screen: " + litruckData);
	}

	/** Pallets & orders from listing */
	public void getPalletandOrders() {
		safeClick(ilibutton);

		List<WebElement> palletElements = driver.findElements(getliInboundpallet);
		logger.info("Total pallet elements size: " + palletElements.size());

		for (int i = 0; i < palletElements.size(); i++) {
			WebElement element = palletElements.get(i);
			String value = element.getText().trim();
			litruckData.put("liInboundPallet" + (i + 1), value);
		}
		List<WebElement> orderInfo = driver
				.findElements(By.xpath("//div[@title='Click to see orders present in pallet']"));

		for (int j = 0; j < orderInfo.size(); j++) {
			WebElement infoIcon = orderInfo.get(j);
			try {
				js.scrollIntoView(infoIcon);
				infoIcon.click();
				List<WebElement> orderElements = driver.findElements(By.xpath("//tbody//*[contains(text(), 'GL-')]"));

				for (WebElement order : orderElements) {
					liglOrders.add(order.getText().trim());
				}
				logger.info("GL Orders on listing found at index " + j + ": " + liglOrders);
				wt.waitToClick(closeInfo2, 30);

			} catch (Exception e) {
				logger.info("Error while processing order info at index " + j);
			}
		}
		logger.info("getPalletandOrders final: " + litruckData);
	}

	/** Validate create vs listing truck details & orders */
	public void verifyTruckDetailsandPalletOrder() {
		getTruckNondStatus();
		truckListData();
		getPalletandOrders();
		wt.waitToClick(closeInfo, 30);

		for (Map.Entry<String, String> entry : litruckData.entrySet()) {
			String expectedValue = entry.getValue();
			Assert.assertTrue(truckData.containsValue(expectedValue),
					"FAIL: Truck data mismatch (listing vs create). Missing: '" + expectedValue + "'");
		}
		for (String actualOrder : liglOrders) {
			Assert.assertTrue(glOrders.contains(actualOrder),
					"FAIL: Expected list does not contain order: '" + actualOrder + "'");
		}
	}

	/** Pallet qty on listing chip */
	public String lispalletQty() {
		return cp.getMandatoryText(getliPalletqty);
	}

	/** Mark truck arrived */
	public void truckMarkasArrived() throws InterruptedException {
		cp.waitForPopupToDisappear();
		safeClick(firstchekbox);
		cp.moveToElementAndClick(merkasarrived);
		cp.waitForLoaderToDisappear();
		cp.captureToastMessage();
		cp.waitForPopupToDisappear();
		WebElement statusElement = driver.findElement(By.xpath("//tbody[1]/tr[2]/td[6]"));
		Truckstatus = statusElement.getText();
		logger.info("TruckStatus::  " + Truckstatus);
	}

	public String getTruckStatus() {
		return Truckstatus;
	}

	/** Edit truck */
	public void editBtn() {
		cp.waitForLoaderToDisappear();
		safeClick(editBtn);
		cp.waitForLoaderToDisappear();
	}

	/** Get truck details from edit */
	public void getTruckEditDetails() {
		String truckno = cp.getMandatoryText(getEditTruckno);
		String totalPieces = cp.getMandatoryText(getEditTotalpieces);
		String totalWeight = cp.getMandatoryText(getEditTotalwt);
		String pickupfrom = cp.getMandatoryText(getEditPickfrom);
		String trimPickuploca = cp.getMandatoryText(getEditPicklocation);
		String pickuplocation = trimPickuploca.substring(0, trimPickuploca.indexOf(",") + 1);
		String trimDroploca = cp.getMandatoryText(getEditDroplocation);
		String droplocation = trimDroploca.split(":")[0].trim();
		String thirdrdparty = cp.getMandatoryText(getEdit3rdparty);
		String truckStatus = cp.getMandatoryText(getEditStatus);
		String palletqty = cp.getMandatoryText(getEditPalletqty);

		truckDataforEdit.put("Truckno", truckno);
		truckDataforEdit.put("Totalpieces", totalPieces);
		truckDataforEdit.put("Totalweight", totalWeight);
		truckDataforEdit.put("Pickupfrom", pickupfrom);
		truckDataforEdit.put("Pickuplocation", pickuplocation);
		truckDataforEdit.put("Droplocation", droplocation);
		truckDataforEdit.put("Thirdparty", thirdrdparty);
		truckDataforEdit.put("Truckstatus", truckStatus);
		truckDataforEdit.put("Palletqty", palletqty);

		List<WebElement> pallets = driver
				.findElements(By.xpath("(//div[@class='datatable-wrapper'])[1]//td[contains(text(), 'IBPL-')]"));
		int i = 1;
		for (WebElement element : pallets) {
			String key = "inboundPallet" + i;
			String value = element.getText().trim();
			truckDataforEdit.put(key, value);
			i++;
		}
		logger.info("Truck Data on Edit:: " + truckDataforEdit);
	}

	/** Validate created vs edit truck details */
	public void validateTruckCreatedDatainEditTruck() {
		SoftAssert sAssert = new SoftAssert();
		getTruckEditDetails();
		for (Map.Entry<String, String> entry : truckData.entrySet()) {
			String key = entry.getKey();
			String truckdataValue = entry.getValue();
			if ("LTL".equalsIgnoreCase(key))
				continue;
			sAssert.assertTrue(truckDataforEdit.containsValue(truckdataValue),
					"FAIL: Edit truck mismatch. Missing: " + truckdataValue);
			sAssert.assertAll();	
			}
	}

	/** Remove pallets and verify calculations */
	public void checkPiecesanWeightWhenpalletRemoved() {
		addedPallets.clear();
		removedPallets.clear();
		List<WebElement> ibplOrderCells = driver
				.findElements(By.xpath("(//div[@class='datatable-wrapper'])[1]//td[contains(text(), 'IBPL-')]"));
		if (ibplOrderCells.size() > 1) {
			for (int i = ibplOrderCells.size(); i >= 2; i--) {
				try {
					WebElement checkbox = driver
							.findElement(By.xpath("(//div[@class='datatable-wrapper'])[1]//tr[" + i + "]//td[1]"));
					checkbox.click();

					RemovedPalletCalculationforPiecesWeightQTY(i);
					logger.info("Removing pallets from truck: " + addedPallets);
				} catch (NoSuchElementException e) {
					logger.info("Truck edit exception while removing pallets " + e);
				}
			}

			List<WebElement> removedPalleteElement = driver
					.findElements(By.xpath("(//div[@class='datatable-wrapper'])[2]//td[contains(text(), 'IBPL-')]"));
			for (WebElement ipblpallet : removedPalleteElement) {
				removedPallets.add(ipblpallet.getText());
			}
			logger.info("Pallets available to add back: " + removedPallets);
			for (String pallet : addedPallets) {
				Assert.assertTrue(removedPallets.contains(pallet), "Missing pallet in removed grid: " + pallet);
			}
			RemovelastPalletFromTruck();
		} else {
			logger.info("No pallets available to remove from Truck");
			RemovelastPalletFromTruck();
		}
	}

	/** Remove last pallet and verify totals */
	public void RemovelastPalletFromTruck() {
		safeClick(firstchekbox);
		getPallet();
		int initialPieces = Integer.parseInt(driver.findElement(getEditTotalpieces).getText().trim());
		int initialWeight = Integer.parseInt(driver.findElement(getEditTotalwt).getText().trim());
		int initialPalletqty = Integer.parseInt(driver.findElement(getEditPalletqty).getText().trim());

		int palletPieces = Integer.parseInt(driver
				.findElement(By.xpath("(//div[@class='datatable-wrapper'])[1]//tr[1]//td[6]")).getText().trim());
		int palletWeight = Integer.parseInt(driver
				.findElement(By.xpath("(//div[@class='datatable-wrapper'])[1]//tr[1]//td[7]")).getText().trim());
		int palletQTY = Integer.parseInt(driver
				.findElement(By.xpath("(//div[@class='datatable-wrapper'])[1]//tr[1]//td[3]")).getText().trim());

		int FinalPieces = initialPieces - palletPieces;
		int FinalWeight = initialWeight - palletWeight;
		int FinalPalletQty = initialPalletqty - palletQTY;

		safeClick(removePallet);
		cp.waitForLoaderToDisappear();
		cp.captureToastMessage();

		initialPieces = Integer.parseInt(driver.findElement(getEditTotalpieces).getText().trim());
		initialWeight = Integer.parseInt(driver.findElement(getEditTotalwt).getText().trim());
		initialPalletqty = Integer.parseInt(driver.findElement(getEditPalletqty).getText().trim());

		logger.info("Expected Final Pieces: " + FinalPieces + ", Actual: " + initialPieces);
		logger.info("Expected Final Weight: " + FinalWeight + ", Actual: " + initialWeight);
		logger.info("Expected Final QTY: " + FinalPalletQty + ", Actual: " + initialPalletqty);

		Assert.assertEquals(initialPieces, FinalPieces, "Mismatch in total pieces after remove!");
		Assert.assertEquals(initialWeight, FinalWeight, "Mismatch in total weight after remove!");
		Assert.assertEquals(initialPalletqty, FinalPalletQty, "Mismatch in total pallet qty after remove!");
	}

	/** Get last pallet text */
	public String getPallet() {
		LastPallet = cp.getMandatoryText(firstpallet);
		logger.info(LastPallet);
		return LastPallet;
	}

	/** Remove pallet calculation helper */
	public void RemovedPalletCalculationforPiecesWeightQTY(int i) {
		WebElement element = driver
				.findElement(By.xpath("(//div[@class='datatable-wrapper'])[1]//tr[" + i + "]//td[2]"));
		addedPallets.add(element.getText());

		int initialPieces = Integer.parseInt(driver.findElement(getEditTotalpieces).getText().trim());
		int initialWeight = Integer.parseInt(driver.findElement(getEditTotalwt).getText().trim());
		int initialPalletqty = Integer.parseInt(driver.findElement(getEditPalletqty).getText().trim());

		int palletPieces = Integer
				.parseInt(driver.findElement(By.xpath("(//div[@class='datatable-wrapper'])[1]//tr[" + i + "]//td[6]"))
						.getText().trim());
		int palletWeight = Integer
				.parseInt(driver.findElement(By.xpath("(//div[@class='datatable-wrapper'])[1]//tr[" + i + "]//td[7]"))
						.getText().trim());
		int palletQTY = Integer
				.parseInt(driver.findElement(By.xpath("(//div[@class='datatable-wrapper'])[1]//tr[" + i + "]//td[3]"))
						.getText().trim());

		int FinalPieces = initialPieces - palletPieces;
		int FinalWeight = initialWeight - palletWeight;
		int FinalPalletQty = initialPalletqty - palletQTY;

		safeClick(removePallet);
		cp.waitForLoaderToDisappear();
		cp.captureToastMessage();
		cp.waitForPopupToDisappear();

		initialPieces = Integer.parseInt(driver.findElement(getEditTotalpieces).getText().trim());
		initialWeight = Integer.parseInt(driver.findElement(getEditTotalwt).getText().trim());
		initialPalletqty = Integer.parseInt(driver.findElement(getEditPalletqty).getText().trim());

		logger.info("Expected Final Pieces: " + FinalPieces + ", Actual: " + initialPieces);
		logger.info("Expected Final Weight: " + FinalWeight + ", Actual: " + initialWeight);
		logger.info("Expected Final QTY: " + FinalPalletQty + ", Actual: " + initialPalletqty);

		Assert.assertEquals(initialPieces, FinalPieces, "Mismatch in total pieces!");
		Assert.assertEquals(initialWeight, FinalWeight, "Mismatch in total weight!");
		Assert.assertEquals(initialPalletqty, FinalPalletQty, "Mismatch in total pallet qty!");
	}

	/** Truck status in edit */
	public String getTruckEditStatus() {
		cp.waitForLoaderToDisappear();
		return cp.getMandatoryText(getEditStatus).trim().toLowerCase();
	}

	/** Search removed pallet */
	public void searchRemovedPallet(String pallet) throws InterruptedException {
		cp.waitForLoaderToDisappear();
		cp.moveToElementAndClick(searchibpl);
		cp.searchColoumFilter(searchibpl, pallet);
	}

	/** Check 'no record' cell */
	public boolean checkElementNorecord() {
		try {
			WebElement norecord = driver.findElement(Norecord);
			return norecord.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/** Add pallets in edit and verify totals */
	public void checkAddpalletFunctioninTruck() {
		addedPallets.clear();
		removedPallets.clear();
		List<WebElement> ibplOrderCells = driver
				.findElements(By.xpath("(//div[@id='panel1bh-content'])[3]//td[contains(text(), 'IBPL-')]"));
		if (ibplOrderCells.size() > 1) {
			for (int i = 3; i >= 1; i--) {
				try {
					WebElement checkbox = driver
							.findElement(By.xpath("(//div[@id='panel1bh-content'])[3]//tr[" + i + "]//td[1]"));
					checkbox.click();

				AddedPalletCalculationforPiecesWeightQTY(i);
				
				System.out.println("Adding pallets in truck: " + addedPallets);	
				logger.info("Adding pallets in truck: " + addedPallets);	
				
				} catch (NoSuchElementException e) {
					logger.info("Truck edit exception while adding " + e);
				}
			}
			List<WebElement> addPalleteElement = driver
					.findElements(By.xpath("(//div[@class='datatable-wrapper'])[1]//td[contains(text(), 'IBPL-')]"));
			for (WebElement ipblpallet : addPalleteElement) {
				removedPallets.add(ipblpallet.getText());
			}
			logger.info("Added pallet in edit " + removedPallets);
			for (String pallet : addedPallets) {
				Assert.assertTrue(removedPallets.contains(pallet), "Missing pallet: " + pallet);
			}
		} else {
			logger.info("Pallets not available to Add in Truck");
		}
	}

	/** Add pallet calculation */
	public void AddedPalletCalculationforPiecesWeightQTY(int i) {
		WebElement element = driver.findElement(By.xpath("(//div[@id='panel1bh-content'])[3]//tr[" + i + "]//td[2]"));
		addedPallets.add(element.getText());

		int initialPieces = Integer.parseInt(driver.findElement(getEditTotalpieces).getText().trim());
		int initialWeight = Integer.parseInt(driver.findElement(getEditTotalwt).getText().trim());
		int initialPalletqty = Integer.parseInt(driver.findElement(getEditPalletqty).getText().trim());

		int palletPieces = Integer.parseInt(driver
				.findElement(By.xpath("(//div[@id='panel1bh-content'])[3]//tr[" + i + "]//td[5]")).getText().trim());
		int palletWeight = Integer.parseInt(driver
				.findElement(By.xpath("(//div[@id='panel1bh-content'])[3]//tr[" + i + "]//td[6]")).getText().trim());
		int palletQTY = Integer.parseInt(driver
				.findElement(By.xpath("(//div[@id='panel1bh-content'])[3]//tr[" + i + "]//td[3]")).getText().trim());

		int FinalPieces = initialPieces + palletPieces;
		int FinalWeight = initialWeight + palletWeight;
		int FinalPalletQty = initialPalletqty + palletQTY;

		safeClick(addPallets);
		cp.waitForLoaderToDisappear();
		cp.captureToastMessage();
		cp.waitForLoaderToDisappear();

		initialPieces = Integer.parseInt(driver.findElement(getEditTotalpieces).getText().trim());
		initialWeight = Integer.parseInt(driver.findElement(getEditTotalwt).getText().trim());
		initialPalletqty = Integer.parseInt(driver.findElement(getEditPalletqty).getText().trim());

		logger.info("Expected Final Pieces: " + FinalPieces + ", Actual: " + initialPieces);
		logger.info("Expected Final Weight: " + FinalWeight + ", Actual: " + initialWeight);
		logger.info("Expected Final QTY: " + FinalPalletQty + ", Actual: " + initialPalletqty);

		Assert.assertEquals(initialPieces, FinalPieces, "Mismatch in total pieces!");
		Assert.assertEquals(initialWeight, FinalWeight, "Mismatch in total weight!");
		Assert.assertEquals(initialPalletqty, FinalPalletQty, "Mismatch in total pallet qty!");
	}

	/** Verify added pallet not available in create truck */
	public void searchAddedPalletinCreateTruck() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		for (int i = 0; i < 2 && i < removedPallets.size(); i++) {
			try {
				String ibpl = removedPallets.get(i);
				cp.moveToElementAndClick(searchibpl);
				cp.searchColoumFilter(searchibpl, ibpl);
				logger.info("Searched for: " + ibpl);
			} catch (Exception e) {
				logger.info("On Create truck element not found");
			}
			Assert.assertTrue(checkElementNorecord(), "'No record found' is not displayed. Test Failed.");
		}
	}

	/** Search QA done 
	 * @throws InterruptedException */
	public void searchQAdone(String done) {
		cp.waitForLoaderToDisappear();
		cp.searchColoumFilter(searchstatus, done);
	}

	public WebElement verifyRemovePalleBtnDisable() {
		return driver.findElement(removePallet);
	}

	public WebElement verifyAddPalleBtnDisable() {
		return driver.findElement(addPallets);
	}

	public String getTruckNo() {
		cp.waitForLoaderToDisappear();
		return cp.getMandatoryText(getliTruckno);
	}

	public void searchTruckno(String number) {
		try {
			clearAndType(searchfirstcoloum, number);
			cp.waitForLoaderToDisappear();
		} catch (StaleElementReferenceException e) {
			clearAndType(searchfirstcoloum, number);
		}
	}

	/** Delete truck */
	public void deleteBtn() {
		logger.info("Deleting Truck Number:: " + getTruckNo());
		cp.waitForLoaderToDisappear();
		safeClick(deleteBtn);
		cp.waitAndClickWithJS(yesBtn, 10);
		cp.captureToastMessage();
	}

	/** Get pallet before deleting */
	public void getDeletingPallet() {
		WebElement orderinfo = driver
				.findElement(By.xpath("(//div[@title='Click to see pallets present in truck'])[1]"));
		orderinfo.click();
		LastPallet = cp.getMandatoryText(getIbpl);
		wt.waitToClick(closeInfo, 30);
	}

	public String getIBPL() {
		cp.waitForLoaderToDisappear();
		return LastPallet;
	}

	public String getPalletQTYonli() {
		return cp.getMandatoryText(getliPalletqty);
	}

	public String emtyTruckStatsus() {
		WebElement statusElement = driver.findElement(By.xpath("//tbody[1]/tr[2]/td[6]"));
		return Truckstatus = statusElement.getText().toLowerCase();
	}

	/** Add pallet in empty truck */
	public void AddpalletinTruck() {
		List<WebElement> ibplOrderCells = driver
				.findElements(By.xpath("(//div[@id='panel1bh-content'])[3]//td[contains(text(), 'IBPL-')]"));
		if (ibplOrderCells.size() > 1) {
			WebElement checkbox = driver.findElement(By.xpath("(//div[@id='panel1bh-content'])[3]//tr[1]//td[1]"));
			checkbox.click();
			logger.info("Adding pallet in empty truck :: " + cp.getMandatoryText(getIbplfromedit));
			safeClick(addPallets);
			cp.captureToastMessage();
			cp.waitForLoaderToDisappear();
		} else {
			logger.info("Pallets not available to Add in Truck");
		}
	}

	/** Get GL order number from info popups 
	 * @throws InterruptedException */
	public String getOrderno() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		try {
			WebElement orderinfo = driver
					.findElement(By.xpath("(//div[@title='Click to see pallets present in truck'])[1]"));
			orderinfo.click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//div[@title='Click to see orders present in pallet']")).click();
			glorder = cp.getMandatoryText(getglorder);
			wt.waitToClick(closeInfo2, 30);
			wt.waitToClick(closeInfo, 30);
			return glorder;
			
		} catch (Exception e) {
			// TODO: handle exception
			wt.waitToClick(closeInfo2, 30);
			wt.waitToClick(closeInfo, 30);
		}
		return glorder;
		
	}

	/** Get order status from listing */
	public String getOrderStatus(String order) {
		cp.waitForLoaderToDisappear();
		searchTruckno(order);
		return cp.getMandatoryText(getOrderStatus);
	}

	/** Search & validate truck no */
	public void searchAndValidateTruckNo() throws InterruptedException {
		cp.clickClearButton();
		try {
			cp.searchAndValidateTruckNo(getliTruckno);
		} catch (Exception e) {
			logger.info("Truck number filter skip");
		}
	}

	/** Search & validate truck name */
	public void searchAndValidateTruckName() throws InterruptedException {
		cp.clickClearButton();
		try {
			cp.searchAndValidateTruckName(getliTruckname);
		} catch (Exception e) {
			logger.info("Truck name filter skip");
		}
	}

	/** Pagination in listing */
	public void paginationOnListing() {
		cp.paginationTest(By.xpath("//*[@title='Edit Truck']"), 2);
		cp.waitForLoaderToDisappear();
	}

	/** Pagination on create truck */
	public void paginationOnCreateTruck() {
		cp.waitForLoaderToDisappear();
		cp.paginationTest(By.xpath("//td[@class='p-frozen-column' and contains(text(), 'IBPL-')]"), 2);
		cp.waitForLoaderToDisappear();
	}

	/** Column filters (listing) */
	public void checkColoumFilterforTruck() throws InterruptedException, IOException {
		Path commonPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "configfile",
				"Locators.JSON");

		String jsonContent = Files.readString(commonPath);
		JSONObject root = new JSONObject(jsonContent);

		String pageName = "inboundTruckPage";

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

	/** Column filters (create truck) */
	public void checkColoumFilterforCreateTruck() throws InterruptedException, IOException {
		Path commonPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "configfile",
				"Locators.JSON");

		String jsonContent = Files.readString(commonPath);
		JSONObject root = new JSONObject(jsonContent);

		String pageName = "inboundTruckCreatePage";

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
	
    
    By importtruckBtn=By.xpath("//*[@title='Click to import trucks.']");
    By nooftruck=By.xpath("//*[@placeholder='Enter No of trucks']");
    
    public void importIbtTrucks() {
    	wt.waitToClick(importtruckBtn, 10);
    	cp.waitForLoaderToDisappear();
    }
    
	/**
	 * Returns the count of mandatory field validation messages.
	 * @return number of mandatory field errors
	 */
	public int getMandFieldCount() {
		return driver.findElements(By.xpath("//*[contains(text(), 'is required')]")).size();
	}
	
	/**
	 * This method used to select value from drop down
	 * @param locator
	 * @param i
	 */
	public void selectSignleValue(By locator, int i) {
		wt.waitToClick(locator, 10);
		wt.waitToClick(By.xpath("(//ul[contains(@class,'p-dropdown-items')]/li)["+ i +"]"), 10);
	}
	
	/**
	 * This method is used to select pickup location from import
	 */
	public void selectPickup() {
		selectSignleValue(pickuptext, 2);
	}
	
	/**
	 * This method is used to select Drop location from import 
	 */
	public void selectDrop() {
		selectSignleValue(droptext, 3);
	}
	
	/**
	 * This method is used to select LTL from import truck
	 */
	public void selectLtl() {
		selectSignleValue(LTLtext, 1);
	}
	
	/**
	 * This method used to enter truck count
	 */
	public void enterNooftrucks() {
		cp.clickAndSendKeys1(nooftruck, "2");
	}
}
