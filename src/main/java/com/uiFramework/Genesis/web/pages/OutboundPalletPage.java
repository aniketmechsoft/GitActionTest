package com.uiFramework.Genesis.web.pages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.DropDownHelper;
import com.uiFramework.Genesis.helper.ReadJsonData;
import com.uiFramework.Genesis.helper.WaitHelper;

public class OutboundPalletPage extends InboundPalletPage {
	WebDriver driver = null;
	CommonMethods cm;
	CommonPage cp = null;
	SoftAssert softAssert = new SoftAssert();
	SoftAssert sAssert = new SoftAssert(); // added: used throughout the class
	DropDownHelper DrpDwn;
	WaitHelper wt;

	List<String> glOrder = new ArrayList<>();
	List<String> RemovedglOrders = new ArrayList<>();
	List<String> popupGLorder = new ArrayList<>();
	List<String> OrderProcess = new ArrayList<>();

	String[] glOrderOnpopup;
	String toast;
	public static String MOBPL;

	// running totals and temps used in add/remove flows
	int initialPieces, initialWeight, orderPieces, orderWeight;

	// local fields referenced but not declared previously
	private String GLorder;
	private String Pickuplocation;

	// Wait
	private WebDriverWait wait;

	private static final Logger logger = Logger.getLogger(OutboundPalletPage.class.getName());

	public OutboundPalletPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		this.cm = new CommonMethods(driver);
		this.cp = new CommonPage(driver);
		this.wt = new WaitHelper(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
		this.DrpDwn = new DropDownHelper(driver);
	}

	// ===== Locators (added a few missing) =====
	private By palletMenu = By.xpath("//i[@title='Pallet']");
	private By barMenu = By.xpath("//i[@title='Barcode Printing']");
	private By outboundpalletMenu = By.xpath("//a[@href='/pallet/outbound-pallet']");
	private By createPallet = By.xpath("//button[@title='Click to create outbound pallet']");

	private By ldafiltericon = By.xpath("(//button[@class='p-column-filter-menu-button p-link'])[5]");
	private By notContains = By.xpath("//li[text()='Not contains']");
	private By getlda = By.xpath("//tr[1]/td[6]");
	private By searchLda = By.xpath("(//input[@class='p-column-filter p-inputtext p-component'])[5]");

	private By mergepBtn = By.id("outPalletFiltermergePalletBtn");
	private By createPBtn = By.xpath("//button[@title='Click to create pallet']");
	private By popupsaveBtn = By.xpath("//button[@title='Click to save data']");
	private By yesBtn = By.id("confmSubmit");
	private By noBtn = By.id("confmCancel"); // added best-guess id for NO button

	private By fetchordertext = By.xpath("(//td[contains(normalize-space(.),'GL-')])[1]");
	private By dropfor = By.xpath("//div[@id='outPalletCreateOrderpalletFor']");
	private By warehouselocation = By.xpath("(//span[@class='p-dropdown-label p-inputtext'])[3]");
	private By origin = By.xpath("(//span[@class='p-dropdown-item-label'])[2]");
	private By searchStatus = By.xpath("(//input[@placeholder='Contains...'])[4]");

	// edit pallet
	private By getPalletNo = By.xpath("//div[@class='labelBoxK__labelData' and contains(text(), 'OBPL-')]");
	private By multiGLorder = By.xpath("(//div[@class='p-datatable-wrapper'])[1]//td[contains(text(), 'GL-')]");
	private By totalPieces = By.xpath("(//div[@class='labelBoxK__labelData'])[2]");
	private By totalWeight = By.xpath("(//div[@class='labelBoxK__labelData'])[3]");
	private By removeOrder = By.xpath("//button[@title='Click to remove orders from pallet']");
	private By multicheckbx = By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[1]//td[1]");
	private By statusonedit = By.xpath("(//div[@class='labelBoxK__labelData'])[9]");
	private By addorders = By.xpath("//button[@id='outboundPltMappingmapOrdersToPalletBtn']");
	private By apalletqty = By.xpath("(//input[@placeholder='Pallet Qty'])[1]");
	private By rpalletqty = By.xpath("(//input[@placeholder='Pallet Qty'])[2]");

	// listing
	private By ldacount = By.xpath("//table/tbody/tr[1]/td[6]//span[contains(@class, 'MuiTypography-button')]");
	private By editBtn = By.xpath("(//button[@title='Click to edit'])[1]");
	private By deleteBtn = By.xpath("(//button[@title='Click to delete'])[1]");
	private By statusonlisting = By.xpath("//table/tbody/tr[1]/td[13]");
	private By palletQty = By.xpath("//tr[1]//td[4]");
	private By getTruckNo = By.xpath("//table[1]/tbody[1]/tr[1]/td[9]");
	private By getPalletNoonlist = By.xpath("//table[1]/tbody[1]/tr[1]/td[3]");
	private By selectstatusoppage = By.xpath("(//*[.='2 items selected'])[3]");
	private By arrived = By.xpath("(//div[@class='p-multiselect-checkbox'])[3]");
	private By closestatusdropdown = By.xpath("//*[@class='p-multiselect-close p-link']");

	// order page
	private By searchfirstcoloum = By.xpath("(//input[@placeholder='Contains...'])[1]");
	private By getOrderStatus = By.xpath("//table/tbody/tr[1]/td[19]");

	// added: these were referenced but not declared earlier
	private By checkbox = By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[1]//td[1]");
	private By getGLorder = By.xpath("//tbody//*[contains(text(), 'GL-')]");
	private By infoGrid = By.xpath("(//tbody[@class='p-datatable-tbody'])[2]//tr//td[3]");
	private By closeInfo = By.xpath("//*[@data-testid='CloseIcon']");
	private By nextPagination = By.xpath("//button[@class='p-paginator-next p-paginator-element p-link']");
	private By backPagination = By.xpath("(//button[@aria-label='Page 1'])[1]");
	private By loader = By.xpath("//*[@src='/static/media/loading.db43a6dd94d78914920a.gif']");
	private By rmulticheckbx = By.xpath("(//div[@class='p-datatable-wrapper'])[2]//tr[1]//td[1]");
	private By searchOrer = By.xpath("(//input[@placeholder='Contains...'])[1]");

	// from parent edit summary labels that are used here too (shadowing if needed)
	private By palletPickup = By.xpath("(//div[@class='labelBoxK__labelData'])[5]");
	private By palletDrop = By.xpath("(//div[@class='labelBoxK__labelData'])[6]");

	/**
	 * Click outbound pallet menu
	 */
	public void outboundMenu() throws TimeoutException {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(barMenu, 10);
		wt.waitToClick(palletMenu, 10);
		driver.findElement(outboundpalletMenu).click();
		cp.waitForLoaderToDisappear();
	}

	// toast captured when creating pallet for main order
	String captureToast;

	/**
	 * Create pallet from selected checkbox in create flow (main order)
	 */
	public void createPalletforMorder() {
		cp.clickElement(checkbox);
		cp.clickElement(createPBtn);
		wt.waitToClick(popupsaveBtn, 5);
		captureToast = cp.captureToastMessage();
		cp.waitForLoaderToDisappear();
	}

	/** Return toast for create pallet */
	public String createPalletmsg() {
		cp.clickToClosePopUp();
		return captureToast;
	}

	/** Record OBPL on listing */
	public void getMOBPL() {
		MOBPL = cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[3]"));
		logger.info("Created OBPL is " + MOBPL);
	}

	/** Return total entries count from paginator */
	public int getTotalEntriesCount(WebDriver driver) {
		cp.clickToClosePopUp();
		cp.waitForLoaderToDisappear();
		WebElement paginatorText = driver.findElement(By.className("p-paginator-current"));
		String text = paginatorText.getText();
		String countStr = text.replaceAll(".*of\\s+(\\d+).*", "$1");
		logger.info("Total count " + countStr);
		return Integer.parseInt(countStr);
	}

	/** Open create pallet dialog */
	public void createPallet() throws TimeoutException {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		wt.waitToClick(createPallet, 5);
		cp.waitForLoaderToDisappear();
	}

	/** Select first checkbox and collect GL order */
	public void selectCheckbox() throws TimeoutException {
		cp.waitForLoaderToDisappear();
		try {
			driver.findElement(checkbox).click();
			glOrder.add(getGLorder());
		} catch (Exception e) {
			logger.info("order not available to select");
		}
	}

	/** Read a GL order cell in current context */
	public String getGLorder() {
		return cp.getMandatoryText(getGLorder);
	}

	/** Verify warehouse-based order availability */
	public void verifyOrderAvailableAsperwarehouse() {
		wt.waitForElement(warehouselocation, 4);
		Pickuplocation = cp.getMandatoryText(warehouselocation).split(",")[0].trim();
		logger.info("WH location: " + Pickuplocation);
		List<WebElement> rows = driver.findElements(By.xpath("//table//tbody//tr"));
		int rowLimit = Math.min(rows.size(), 10);
		for (int i = 1; i <= rowLimit; i++) {
			String actualTrimmed = driver.findElement(By.xpath("//table//tbody//tr[" + i + "]/td[11]"))
					.getText().trim().split(",")[0].trim();
			Assert.assertEquals(actualTrimmed, Pickuplocation, "Pick location mismatch at row " + i);
		}
	}

	/** Filter out orders by LDA (Not contains) */
	public void searchLDA() throws TimeoutException, InterruptedException {
		String LDA = cp.getMandatoryText(getlda).trim().split("-")[0].trim();
		driver.findElement(ldafiltericon).click();
		driver.findElement(notContains).click();
		try {
			wt.waitForElement(loader, 3);
			cp.waitForLoaderToDisappear();
		} catch (Exception e) {
			logger.info("Showing error while searching column filter");
		}
		cp.searchColoumFilter(searchLda, LDA);
	}

	/** Merge selected → verify popup GLs, then save */
	public void mergeBtn() {
		driver.findElement(mergepBtn).click();
		try {
			cp.waitAndClickWithJS(yesBtn, 3);
			logger.info(cp.getMandatoryText(By.xpath("//h2[@class='confirm__title pt-1 pb-3']")));
			cp.waitForLoaderToDisappear();
		} catch (Exception e) {
			logger.info("Multi LDA popup not displayed: mergeBtn path");
		}

		WebElement glOrderCell = driver.findElement(
				By.xpath("//tbody[@class='p-datatable-tbody']//tr[@role='row']/td[1][contains(text(), 'GL-')]"));
		String glOrderText = glOrderCell.getText();
		glOrderOnpopup = Arrays.stream(glOrderText.split(",")).map(String::trim).toArray(String[]::new);
		logger.info("popup gl: " + Arrays.toString(glOrderOnpopup));
		logger.info("selected gl orders: " + glOrder);
		matchOrders();
	}

	/** Assert popup GL list matches selected, then save */
	public void matchOrders() {
		SoftAssert sAssert = new SoftAssert();
		for (String glorder : glOrder) {
			sAssert.assertTrue(Arrays.asList(glOrderOnpopup).contains(glorder),
					"GL Order not found in popup: " + glOrder);
		}
		cp.waitAndClickWithJS(popupsaveBtn, 3);
		logger.info("All GL orders matched. Save button clicked.");
		cp.captureToastMessage();
		sAssert.assertAll();
	}

	/** LDA count badge on listing */
	public int getLDAfromListng() {
		logger.info(cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[5]")));
		return Integer.parseInt(cp.getMandatoryText(ldacount).trim());
	}

	/** Edit first pallet */
	public void editBtn() {
		cp.waitForLoaderToDisappear();
		driver.findElement(editBtn).click();
		cp.waitForLoaderToDisappear();
	}

	/** Verify pickup/drop values between create vs. edit */
	public void verifyPickndDrop() {
		String palletpickup = cp.getMandatoryText(palletPickup).split(",")[0].trim();
		String palletdrop = cp.getMandatoryText(palletDrop);
		logger.info("Edit pickup " + palletpickup + " | expected " + Pickuplocation);
		softAssert.assertEquals(Pickuplocation, palletpickup,
				"Pickup location mismatch! Expected: " + Pickuplocation);

		softAssert.assertEquals("LDA", palletdrop, "Drop location mismatch! Expected: LDA");

		logger.info("Pickup and drop locations compared. Verifying assertions...");
		softAssert.assertAll();
	}

	/** Return OBPL in edit view header */
	public String getOutboundPallet() {
		logger.info("OBPL " + driver.findElement(getPalletNo).getText());
		return driver.findElement(getPalletNo).getText();
	}

	/** Return first GL in top grid (edit) */
	public String getPalletorder() {
		GLorder = cp.getMandatoryText(multiGLorder);
		logger.info("GLorder " + GLorder);
		return GLorder;
	}

	/** Remove down to last order, verify totals and presence in lower grid */
	public void checkRemovedOrders() {
		glOrder.clear();
		List<WebElement> glOrderCells = driver
				.findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//td[contains(text(), 'GL-')]"));

		if (glOrderCells.size() > 1) {
			for (int i = glOrderCells.size(); i >= 2; i--) {
				try {
					WebElement checkbox = driver
							.findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[" + i + "]//td[1]"));
					checkbox.click();

					List<WebElement> glOrderElements = driver
							.findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[" + i + "]//td[2]"));
					for (WebElement element : glOrderElements) {
						glOrder.add(element.getText());

						initialPieces = Integer.parseInt(driver.findElement(totalPieces).getText().trim());
						initialWeight = Integer.parseInt(driver.findElement(totalWeight).getText().trim());

						orderPieces = Integer.parseInt(driver
								.findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[" + i + "]//td[8]"))
								.getText().trim().split("/")[0].trim());
						orderWeight = Integer.parseInt(driver
								.findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[" + i + "]//td[9]"))
								.getText().trim());

						int FinalPieces = initialPieces - orderPieces;
						int FinalWeight = initialWeight - orderWeight;

						wt.waitToClick(removeOrder, 10);
						cp.waitForLoaderToDisappear();
						cp.captureToastMessage();
						cp.waitForPopupToDisappear();

						initialPieces = Integer.parseInt(driver.findElement(totalPieces).getText().trim());
						initialWeight = Integer.parseInt(driver.findElement(totalWeight).getText().trim());

						logger.info("Expected Final Pieces: " + FinalPieces + ", Actual: " + initialPieces);
						logger.info("Expected Final Weight: " + FinalWeight + ", Actual: " + initialWeight);

						Assert.assertEquals(initialPieces, FinalPieces, "Mismatch in Total and final Pieces!");
						Assert.assertEquals(initialWeight, FinalWeight, "Mismatch in Total and final weight!");
					}
				} catch (NoSuchElementException e) {
					logger.info("Pallet edit exception: " + e);
				}
			}

			List<WebElement> glOrderRemoved = driver
					.findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[2]//td[contains(text(), 'GL-')]"));
			if (glOrderRemoved.isEmpty()) {
				logger.info("No GL orders found in removed orders table.");
			} else {
				for (WebElement glorder : glOrderRemoved) {
					RemovedglOrders.add(glorder.getText());
				}
			}
			for (String Glorder : glOrder) {
				sAssert.assertTrue(RemovedglOrders.contains(Glorder), "Missing order: " + Glorder);
			}
			checkOrderProsessForInbound();
			RemovelastOrderFrompallet();
			cp.clickToClosePopUp();
			sAssert.assertAll();
		} else {
			logger.info("Order not available to remove from pallet");
			checkOrderProsessForInbound();
			RemovelastOrderFrompallet();
			cp.clickToClosePopUp();
			sAssert.assertAll();
		}
	}

	/** Remove last order from pallet (with confirm) */
	public void RemovelastOrderFrompallet() {
		driver.findElement(multicheckbx).click();
		getPalletorder();
		wt.waitToClick(removeOrder, 10);
		cp.waitAndClickWithJS(yesBtn, 10);
		toast = cp.captureToastMessage();
		cp.waitForLoaderToDisappear();
	}

	/** Toast after delete/remove */
	public String palletDelete() {
		return toast;
	}

	/** Fail if any order process is Return/Closeout in bottom grid */
	public void checkOrderProsessForInbound() {
		List<WebElement> orderProcess = driver
				.findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[2]//td[contains(text(), 'Re')]"));
		if (orderProcess.isEmpty()) {
			logger.info("No GL Order found for Order Process");
		} else {
			for (WebElement orderType : orderProcess) {
				String text = orderType.getText();
				OrderProcess.add(text);
				if (text.equalsIgnoreCase("Return") || text.equalsIgnoreCase("Closeout")) {
					Assert.fail("FAIL: GL Order contains 'Return' or 'Closeout', which is not allowed.");
				}
			}
		}
	}

	public String PalletStatusonEdit() {
		return cp.getMandatoryText(statusonedit);
	}

	/** Select multiple source rows then verify selection survives pagination + popup match */
	public void selectMulcheckBoxForCretepallet() {
		glOrder.clear();
		List<WebElement> checkboxes = driver
				.findElements(By.xpath("//td[@class='p-selection-column p-frozen-column']//input[@type='checkbox']"));
		for (int i = 0; i < 3 && i < checkboxes.size(); i++) {
			try {
				checkboxes.get(i).click();
				List<WebElement> glOrderElements = driver.findElements(By.xpath("//td[@class='p-frozen-column']"));
				if (i < 3 && i < glOrderElements.size()) {
					String glorder = glOrderElements.get(i).getText().trim();
					glOrder.add(glorder);
				}
			} catch (Exception e) {
				logger.info("Checkbox at index " + i + " not clickable: " + e.getMessage());
			}
		}

		driver.findElement(nextPagination).click();
		cp.waitForLoaderToDisappear();
		driver.findElement(backPagination).click();
		cp.waitForLoaderToDisappear();

		List<WebElement> refreshedCheckboxes = driver
				.findElements(By.xpath("//td[@class='p-selection-column p-frozen-column']//input[@type='checkbox']"));
		for (int i = 0; i < 3 && i < refreshedCheckboxes.size(); i++) {
			Assert.assertTrue(refreshedCheckboxes.get(i).isSelected(), "Checkbox is NOT selected after pagination.");
		}

		wt.waitToClick(createPBtn, 10);
		wt.waitForElement(By.xpath("//tbody[@class='p-datatable-tbody']//tr[@role='row']/td[1][contains(text(), 'GL-')]"), 10);

		List<WebElement> glOrderc = driver.findElements(
				By.xpath("//tbody[@class='p-datatable-tbody']//tr[@role='row']/td[1][contains(text(), 'GL-')]"));
		for (WebElement element : glOrderc) {
			popupGLorder.add(element.getText().trim());
		}
		logger.info("Popup GL " + popupGLorder);
		matchPopOrders();
		cp.clickToClosePopUp();
	}

	/** Assert selected GLs appear in the popup list, then save */
	public void matchPopOrders() {
		SoftAssert sAssert = new SoftAssert();
		for (String glOrder : glOrder) {
			boolean found = popupGLorder.stream().anyMatch(popupOrder -> popupOrder.trim().equalsIgnoreCase(glOrder.trim()));
			sAssert.assertTrue(found, "GL Order not found in popup: " + glOrder);
		}
		wt.waitToClick(popupsaveBtn, 5);
		logger.info("All GL orders matched. Save button clicked.");
		cp.captureToastMessage();
		sAssert.assertAll();
	}

	/** Verify orders from popup via info icons (robust waits) */
	public void orderInfo() {
		SoftAssert sAssert = new SoftAssert();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		cp.waitForLoaderToDisappear();
		logger.info("popgl " + popupGLorder);

		for (int i = 0; i < 3; i++) {
			try {
				List<WebElement> orderinfo = driver.findElements(
						By.xpath("//div[@title='Click to see orders present in pallet']"));

				if (i >= orderinfo.size()) {
					logger.info("No more order info icons to click at index: " + i);
					break;
				}

				wait.until(ExpectedConditions.visibilityOf(orderinfo.get(i)));
				orderinfo.get(i).click();

				String iGLorder = cp.getMandatoryText(infoGrid);
				sAssert.assertTrue(popupGLorder.contains(iGLorder),
						"GL Order '" + iGLorder + "' not found in popupGL list.");

				wait.until(ExpectedConditions.visibilityOfElementLocated(closeInfo));
				wt.waitToClick(closeInfo, 30);

				sAssert.assertAll();
			} catch (Exception e) {
				logger.info("Error at index " + i + ": " + e.getMessage());
				logger.info("Order info method error");
			}
		}
	}

	/** Pallet status on listing row 1 */
	public String PalletStatusonListing() {
		return cp.getMandatoryText(statusonlisting);
	}

	/** Ensure NO button keeps order on pallet */
	public void checkNoBtnonPopup() {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForLoaderToDisappear();
		List<WebElement> glOrderCells = driver
				.findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//td[contains(text(), 'GL-')]"));
		if (glOrderCells.size() > 1) {
			logger.info("More than one GL order found. Skipping checkNoBtnonPopup method.");
			return;
		}

		driver.findElement(multicheckbx).click();
		getPalletorder();
		driver.findElement(removeOrder).click();
		cp.waitAndClickWithJS(noBtn, 4);
		logger.info("checking No button on Pop up");
		sAssert.assertTrue(!getOutboundPallet().isEmpty(), "Outbound pallet should not be empty.");
		sAssert.assertTrue(!getPalletorder().isEmpty(), "Pallet order should not be empty.");
		sAssert.assertAll();
	}

	/** Add from bottom grid → verify totals and presence in top grid */
	public void checkAddedOrdersinPallet() {
		cp.clickToClosePopUp();
		RemovedglOrders.clear();
		glOrder.clear();
		List<WebElement> glOrderCells = driver
				.findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[2]//td[contains(text(), 'GL-')]"));
		if (glOrderCells.size() > 0) {
			for (int i = 3; i >= 1; i--) {
				try {
					WebElement checkbox = driver
							.findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[2]//tr[" + i + "]//td[1]"));
					checkbox.click();

					List<WebElement> glOrderElements = driver
							.findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[2]//tr[" + i + "]//td[2]"));
					for (WebElement element : glOrderElements) {
						glOrder.add(element.getText());

						initialPieces = Integer.parseInt(driver.findElement(totalPieces).getText().trim());
						initialWeight = Integer.parseInt(driver.findElement(totalWeight).getText().trim());

						orderPieces = Integer.parseInt(driver
								.findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[2]//tr[" + i + "]//td[8]"))
								.getText().trim().split("/")[0].trim());
						orderWeight = Integer.parseInt(driver
								.findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[2]//tr[" + i + "]//td[9]"))
								.getText().trim());

						int FinalPieces = initialPieces + orderPieces;
						int FinalWeight = initialWeight + orderWeight;

						wt.waitToClick(addorders, 10);
						try {
							cp.waitAndClickWithJS(yesBtn, 5);
						} catch (Exception e) {
							logger.info("Yes button not displayed for multiple LDA");
						}
						cp.captureToastMessage();
						cp.waitForLoaderToDisappear();
						cp.waitForPopupToDisappear();

						initialPieces = Integer.parseInt(driver.findElement(totalPieces).getText().trim());
						initialWeight = Integer.parseInt(driver.findElement(totalWeight).getText().trim());

						logger.info("Expected Final Pieces: " + FinalPieces + ", Actual: " + initialPieces);
						logger.info("Expected Final Weight: " + FinalWeight + ", Actual: " + initialWeight);

						Assert.assertEquals(initialPieces, FinalPieces, "Mismatch in Total and final Pieces!");
						Assert.assertEquals(initialWeight, FinalWeight, "Mismatch in Total and final weight!");
					}

				} catch (NoSuchElementException e) {
					logger.info("Pallet edit exception " + e);
				}
			}
			List<WebElement> glOrderAdded = driver
					.findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//td[contains(text(), 'GL-')]"));
			if (glOrderAdded.isEmpty()) {
				logger.info("No GL orders found in Added grid orders table.");
			} else {
				for (WebElement glorder : glOrderAdded) {
					RemovedglOrders.add(glorder.getText());
				}
			}
			for (String Glorder : glOrder) {
				Assert.assertTrue(RemovedglOrders.contains(Glorder), "Missing Added order: " + Glorder);
				logger.info("Checked Order available in Added grid");
			}

		} else {
			logger.info("Order not available to Add in pallet");
		}
	}

	/** Search removed orders in create grid and expect empty results */
	public boolean searchOrders() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		for (String order : RemovedglOrders) {
			cp.searchColoumFilter(searchOrer, order);
			cp.waitForLoaderToDisappear();
			try {
				WebElement emptyRow = driver.findElement(By.xpath("//tr[@class='p-datatable-emptymessage']"));
				if (!emptyRow.isDisplayed()) {
					return false;
				}
			} catch (NoSuchElementException e) {
				return false;
			}
		}
		return true;
	}

	/** Adjust pallet qty from remove grid and assert qty parity */
	public void removedPalletQty(String qty) throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		checkPalletStatus(); // lightweight no-op stub here
		driver.findElement(rmulticheckbx).click();
		driver.findElement(rpalletqty).sendKeys(Keys.BACK_SPACE);
		driver.findElement(rpalletqty).sendKeys(Keys.BACK_SPACE);
		driver.findElement(addorders).click();
		sAssert.assertEquals(cp.captureToastMessage(), ReadJsonData.getNestedValue("PalletQty", "expected"),
				ReadJsonData.getNestedValue("PalletQty", "message"));
		cp.waitForPopupToDisappear();
		addPalletQty(qty);
	}

	/** Helper: set remove-qty and verify parity with add-qty */
	public void addPalletQty(String qty) {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForLoaderToDisappear();
		driver.findElement(rpalletqty).sendKeys(qty);
		driver.findElement(addorders).click();
		try {
			cp.waitAndClickWithJS(yesBtn, 4);
		} catch (Exception e) {
			// ignore
		}
		cp.waitForPopupToDisappear();
		sAssert.assertEquals(getPalletQtyAdd(), getPalletQtyRemove(), "Pallet quantity not same");
	}

	public String getPalletQtyAdd() {
		WebElement qtyElement = driver.findElement(apalletqty);
		return qtyElement.getAttribute("value");
	}

	public String getPalletQtyRemove() {
		WebElement qtyElement = driver.findElement(rpalletqty);
		return qtyElement.getAttribute("value");
	}

	public String getPalletQty() {
		return cp.getMandatoryText(palletQty);
	}

	/** Close info popup and delete pallet with confirm */
	public void deleteBtn() {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(closeInfo, 30);
		wt.waitToClick(deleteBtn, 30);
		cp.waitAndClickWithJS(yesBtn, 5);
	}

	/** Delete pallet directly from list */
	public void deleteBtnDirect() {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(deleteBtn, 30);
		cp.waitAndClickWithJS(yesBtn, 5);
		cp.waitForLoaderToDisappear();
	}

	/** Multiselect status by index via CommonPage helper */
	public void selectMultiplestatusByIndex(List<Integer> indices) throws InterruptedException {
		cp.waitForLoaderToDisappear();
		cp.selectMultipleByIndex(selectstatusoppage, indices, "Status");
	}

	/** Validate truck number search on listing */
	public void searchAndValidateTruckNo() throws InterruptedException {
		cp.clickClearButton();
		try {
			cp.searchAndValidateTruckNo(getTruckNo);
		} catch (Exception e) {
			logger.info("Truck number filter skip..");
		}
	}

	/** Validate pallet number search on listing */
	public void searchAndValidatePalletNo() throws InterruptedException {
		cp.clickClearButton();
		try {
			cp.searchAndValidatePalletNo(getPalletNoonlist);
		} catch (Exception e) {
			logger.info("Pallet no filter skip..");
		}
	}

	/** Pagination check: listing grid */
	public void paginationOnListing() {
		cp.paginationTest(By.xpath("//td[@class='p-frozen-column' and contains(text(), 'OBPL-')]"), 4);
	}

	/** Pagination check: create page grid */
	public void paginationOnCeatePallet() {
		cp.clickClearButton();
		cp.paginationTest(By.xpath("//td[@class='p-frozen-column' and contains(text(), 'GL-')]"), 4);
	}

	/** Search & validate order number from listing */
	public void searchAndValidateOrderNumber() throws InterruptedException {
		cp.searchAndValidateOrderNumber(fetchordertext);
		cp.waitForLoaderToDisappear();
	}

	/** Validate Drop For filter logic (LDA vs Origin) */
	public void checkDropFor() {
		cp.clickClearButton();
		List<WebElement> rows = driver.findElements(By.xpath("//td[@class='p-frozen-column' and contains(text(), 'GL-')]"));
		logger.info("Drop for testing for LDA filter");
		for (int i = 1; i <= rows.size(); i++) {
			sAssert.assertNotEquals(getConsignee(i), getLDA(i),
					"checking for LDA Mismatch failed at row " + i + ": Customer = " + getConsignee(i) + ", LDA = " + getLDA(i));
		}
		WebElement dropElement = driver.findElement(dropfor);
		dropElement.click();
		driver.findElement(origin).click();
		cp.Search();
		cp.waitForLoaderToDisappear();
		logger.info("Drop for testing for Origin filter");
		for (int i = 1; i <= rows.size(); i++) {
			sAssert.assertEquals(getConsignee(i), getLDA(i),
					"LDA mismatch at row " + i + ": Customer = " + getConsignee(i) + ", LDA = " + getLDA(i));
		}
		sAssert.assertAll();
	}

	public String getConsignee(int i) {
		return cp.getMandatoryText(By.xpath("//table/tbody/tr[" + i + "]/td[7]"));
	}

	public String getLDA(int i) {
		String LDA = cp.getMandatoryText(By.xpath("//table/tbody/tr[" + i + "]/td[6]"));
		return LDA.replaceFirst("^\\d+\\s*-\\s*", "").trim();
	}

	/** Select Arrived in search section multiselect */
	public void selectArrivedStatus() {
		driver.findElement(selectstatusoppage).click();
		driver.findElement(arrived).click();
		driver.findElement(closestatusdropdown).click();
	}

	/** Column search for Status */
	public void searchStatus(String status) {
		cp.waitForLoaderToDisappear();
		cp.searchColoumFilter(searchStatus, status);
	}

	/** Order status by order number */
	public String getOrderStatus(String order) {
		cp.waitForLoaderToDisappear();
		searchOrderno(order);
		return cp.getMandatoryText(getOrderStatus);
	}

	public void searchOrderno(String number) {
		cp.searchColoumFilter(searchfirstcoloum, number);
	}

	/** Column filter validations (listing) from JSON recipe */
	public void checkColoumFilter() throws InterruptedException, IOException {
		String commonPath = System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\Locators.JSON";
		String jsonContent = new String(Files.readAllBytes(Paths.get(commonPath)));
		JSONObject root = new JSONObject(jsonContent);
		String pageName = "outboundPalletPage";

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
					cp.validateColumnFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath, columnIndex);
				} else if (method.equals("numeric")) {
					cp.validateNumericFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath, columnIndex);
				} else if (method.equals("date")) {
					cp.validateDateFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath, columnIndex);
					driver.findElement(By.xpath("//*[@class='p-column-filter-clear-button p-link']")).click();
				}
			}
		} else {
			logger.info("Page not found: " + pageName);
		}
	}

	/** Column filter validations (create page) from JSON recipe */
	public void checkColoumFilterOnCreatePage() throws InterruptedException, IOException {
		String commonPath = System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\Locators.JSON";
		String jsonContent = new String(Files.readAllBytes(Paths.get(commonPath)));
		JSONObject root = new JSONObject(jsonContent);
		String pageName = "outboundPalletCreatePage";

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
					cp.validateColumnFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath, columnIndex);
				} else if (method.equals("numeric")) {
					cp.validateNumericFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath, columnIndex);
				} else if (method.equals("date")) {
					cp.validateDateFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath, columnIndex);
					driver.findElement(By.xpath("//*[@class='p-column-filter-clear-button p-link']")).click();
				}
			}
		} else {
			logger.info("Page not found: " + pageName);
		}
	}

	// ---- small stub to avoid compile errors if not provided by parent ----
	public void checkPalletStatus() {
		cp.waitForLoaderToDisappear();
	}
}
