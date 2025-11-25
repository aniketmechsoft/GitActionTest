package com.uiFramework.Genesis.web.pages;
import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.DropDownHelper;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.helper.WaitHelper;

public class CloseOutPage {
	WebDriver driver = null;
	CommonPage cp;
	DropDownHelper DrpDwn;
	WaitHelper wt;
	JavaScriptHelper js;
	CommonMethods cm = null;
	private WebDriverWait wait;
	protected static final Logger logger = Logger.getLogger(OrderPage.class.getName());
	public static String pickupLocation;
	public static String iOriginOrderNo;
	public static String pickupLocationObpl;
	public static String OriginOrderNoObpl;
	public static String iLBOL;
	public static String OLBOL;
	public static String SameLDAOrderNo;
	public static String DiffLDAOrderNo;
	public static String LDAPickuplocation;
	
	public String MCusorderNo;
	
	public CloseOutPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
		this.cp = new CommonPage(driver);
		this.wt = new WaitHelper(driver);
		this.DrpDwn = new DropDownHelper(driver);
		this.js = new JavaScriptHelper(driver);
		cm = new CommonMethods(driver);
	}

	private By OutMenu = By.xpath("//i[@title='Reconsignment']");
	private By closeOutMenu = By.xpath("//i[@title='Closeout']");
	private By subMenu = By.xpath("//a[@href='/closeout']");
	private By createOrder = By.xpath("//*[@id='closeOutbtnAddNewCloseout']");
	private By confirm = By.xpath("//*[@title='Click to confirm/tender']");
	private By confirmLine = By.xpath("(//*[@title='Click to confirm/tender'])[2]");
	private By selectconsingee = By.id("closeOutlineOrderDetailspickupConsignee");
	private By selectorderType = By.id("closeOutlineOrderDetailsorderType");
	private By searchcustomer = By.xpath("//*[@id='closeOutlineOrderDetailscustomer']");
	private By list = By
			.xpath("//div[contains(@class,'p-dropdown-items-wrapper')]//ul[contains(@class,'p-dropdown-items')]/li");	
	private By clist = By.xpath("//div[contains(@class,'d-flex align-items-center mt-1 pitem ')]");
	private By selectordertype = By.xpath("//*[@id='closeOutlineOrderDetailsorderType']");
	private By enterordernum = By.xpath("//input[@placeholder='Enter Customer Order No']");
	private By getOrignalLocCarrier = By.xpath("//*[@id='closeOutlineOrderDetailslocalCarrier']/span");
	private By selectCloseOutType = By.xpath("//span[.='Select Closeout Type']");
	private By repReturn = By.xpath("//li[@role='option' and .='Rep Return']");
	private By consTerm = By.xpath("//li[@role='option' and .='Consignee Termination']");
	private By tranSample = By.xpath("//li[@role='option' and .='Transfer of Samples']");
	private By enterSpecialInst = By.xpath("//*[@placeholder='Enter Special Instructions']");
	private By originstatus = By.id("closeOutlineOrderDetailsorderStatus");
	private By scheddate = By.xpath("(//*[@class='p-datepicker-trigger p-button p-component p-button-icon-only'])[1]");
	private By pickupdate = By.xpath("(//*[@class='p-datepicker-trigger p-button p-component p-button-icon-only'])[2]");
	private By dateSel = By.xpath("//td[@data-p-today='true']");
	private By repname = By.xpath("//*[@placeholder='Enter Rep Name']");
	private By signby = By.xpath("//*[@placeholder='Enter Pickup signed by']");
	private By fetchaddress1 = By.xpath("//*[@id='reconsignmentPickupAddresaddress1']");
	// private By address2 = By.xpath("//*[@id='address2']");
	// private By address3 = By.xpath("//*[@id='address3']");
	private By fetchstate = By.xpath("//*[@id='reconsignmentPickupAddresstate']/span");
	private By fetchcity = By.xpath("//*[@id='reconsignmentPickupAddrescity']");
	// private By citypresent = By.xpath("//input[@id='city']");
	private By fetchzipcode = By.xpath("//input[@id='reconsignmentPickupAddreszip']");
	private By enteractweight = By.xpath("//*[@placeholder='Enter Actual Weight']");
	private By enetrdimweight = By.xpath("//input[@placeholder='Enter Dimensional Weight']");
	private By enetrpieces = By.xpath("//input[@placeholder='Enter Number Of Pieces']");
	private By enterLitweight = By.xpath("//input[@placeholder='Enter Literature Actual Weight']");
	private By enetrLitdimweight = By.xpath("//input[@placeholder='Enter Literature Dimensional Weight']");
	private By enetrLitpieces = By.xpath("//input[@placeholder='Enter Literature Number Of Pieces']");
	private By ratedweight = By.xpath("//input[@placeholder='Rated Weight']");
	private By ratedweightline = By.xpath("(//input[@placeholder='Rated Weight'])[2]");
	private By ratedlitweight = By.xpath("(//input[contains(@placeholder,'Literature Rated Weight')])[1]");
	private By ratedlitweightline = By.xpath("(//input[contains(@placeholder,'Literature Rated Weight')])[2]");
	private By noofpallet = By.xpath("//*[@placeholder='Enter Pallet Qty']");
	private By custaccess = By.xpath("//button[@title='Click to add customer accessorial']");
	private By addcarr = By.xpath("//button[@title='Click to add carrier accessorial']");
	private By custaccesstext = By.xpath("//span[text()='Customer Accessorial is required.']");
	private By carrieraccesstext = By.xpath("//span[text()='Carrier Accessorial is required.']");
	private By pricetext = By.xpath("//span[text()='Price is required.']");
	private By selectcustaccess = By.xpath("//span[normalize-space()='Select Customer Accessorial']");
	private By selectcarrtaccess = By.xpath("//span[normalize-space()='Select Carrier Accessorial']");
	private By selectcarrtaccesstype = By.xpath("//span[normalize-space()='Select Carrier Accessorial Type']");
	private By input = By.xpath("//input[@class='p-dropdown-filter p-inputtext p-component']");
	private By enterprice = By.xpath("//input[@placeholder='Enter Price ($)']");
	private By enterreason = By.id("closeOutaddCarrierAccessorialreason");
	private By saveaccess = By.cssSelector("button[title='Click to save data'][type='button']");
	private By delete = By.xpath("//*[name()='svg' and @data-testid='DeleteIcon']");
	private By editbutton = By.xpath("(//*[name()='svg' and @data-testid='EditIcon'])[1]");
	private By editbuttoncarr = By.xpath("(//button[@aria-label='edit'])[2]");
	private By deletecarr = By
			.xpath("(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeInherit css-1amtie4'])[13]");
	private By yesbutton = By.xpath(
			"//div[@class='MuiDialog-root LoginForm MuiModal-root css-126xj0f']//button[@id='confmSubmit']");
	private By edit = By.xpath("//button[@title='Click to edit']");
	private By comment = By.xpath("//button[contains(@class, 'MuiButtonBase-root') and @title='Click to add comment']");
	private By entercomment = By.xpath("//textarea[@placeholder='Enter Comment']");
	private By editcomment = By.xpath("(//button[@title='Click to edit'])[1]");
	private By subjecttext = By.xpath("//span[text()='Subject is required.']");
	private By entersubject = By.xpath("//input[@placeholder='Enter Subject']");
	private By closecommnet = By.xpath("//button[@title='Click to close comment']");
	private By deletecomment = By.xpath("//button[@title='Click to delete']");
	private By selecttype = By.xpath("//span[normalize-space()='Select Type']");
	private By yescomment = By.xpath("(//button[@id='confmSubmit'])[5]");
	private By typetext = By.xpath("//span[text()='Type is required.']");
	private By deletefile = By.xpath("(//*[@data-testid='DeleteIcon'])[4]");
	private By documnet = By.xpath("//input[@placeholder='Enter Document Name']");
	private By doctext = By.xpath("//span[text()='Document Name is required.']");
	private By filetext = By.xpath("//span[text()='File is required.']");
	private By addfile = By.xpath("//button[@title='Click to add file']");
	private String downloadDir = System.getProperty("user.dir") + "\\downloadedFiles";
	private By ormanifest = By.xpath("(//*[@title='Click to print'])[1]");
	private By orlbol = By.xpath("(//*[@title='Click to print'])[2]");
	private By addcloseoutline = By.xpath("//*[@title='Click to add Closeout Line']");
	private By linePieces = By.xpath("(//*[@class='p-inputgroup-addon'])[1]");
	private By lineLitPieces = By.xpath("(//*[@class='p-inputgroup-addon'])[2]");
	private By getConsigneeto = By.xpath("//*[@id='closeOutlineDetconsignTo']//span[@class='p-dropdown-label p-inputtext']");
	private By saveBtnOnline = By.xpath("(//button[@title='Click to save data'])[2]");
	private By warehouseprocess = By.xpath("//*[@id='closeOutlineDetwarehouseProcessed']");
	private By selectconsigneetype = By.id("closeOutlineDetoriginSource");
	private By contactnumber = By.xpath("//*[@placeholder='Enter Contact Phone']");
	private By selectwarehouse = By.xpath("//*[@id='closeOutlineDetwarehouse']");
	private By firstvalue = By.xpath("//*[@class='p-dropdown-item-label']");
	private By contactemail = By.xpath("//*[@placeholder='Enter Contact Email']");
	private By enterLinepieces = By.xpath("//input[@id='closeOutlineDetnoOfPieces']");
	private By enterLineDimwt = By.xpath("//input[@id='closeOutlineDetdimensionalWeight']");
	private By enterLineActualwt =By.xpath("//input[@id='closeOutlineDetsampleWeight']");
	private By selectLineOrdertype = By.xpath("(//*[@class='p-dropdown-trigger' and @aria-label='Select Order Type'])[2]");
	private By enterLineLitpieces =By.xpath("//input[@id='closeOutlineDetliteratureNoOfPieces']");;	
	private By enterLineLitwt = By.xpath("//input[@id='closeOutlineDetliteratureActualWeight']");
	private By enterLineLitDimwt =By.xpath("//input[@id='closeOutlineDetliteratureDimensionalWeight']");
	private By ordernumber =By.xpath("//*[@id='closeOutlineDetglOrderNumber']");
	private By lbolnumber =By.xpath("//*[@class='p-datatable-tbody']//td[7]");
	private By closepopupBtn =By.xpath("(//*[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-sizeMedium css-3b9s7z'])[8]");
	private By deleteBtn =By.xpath("//*[contains(@id,'closeOutlineDeleteBtn')]");
	private By getstatus =By.xpath("//*[@class='p-datatable-tbody']//td[8]");
	private By auditorname=By.id("closeOutauditorDetauditorName");
	private By auditorNumber=By.id("closeOutauditorDetmobileNumber");
	private By firstcheckBox=By.xpath("//*[@class='p-selection-column p-frozen-column']");
	private By printtempbtn=By.xpath("//*[@id='closeOutprintCloseoutBtn']");
	private By confrimbtn=By.xpath("//*[@title='Click to confirm/tender selected order']");
	private By okBtn= By.id("closeOutconfirmOKBTN");
	private By selectconsigneeto = By.id("closeOutlineDetconsignTo");
	private By selectconsignee= By.xpath("//*[@id='closeOutlineDetconsignee']");
	private By selectLDA= By.xpath("//*[@id='closeOutlineDetconsigneeLDA']");
	private By searchconsignee= By.xpath("//*[@placeholder='Search...']");
	private By getorgorder= By.xpath("//*[@id='closeOutlineOrderDetailscloseoutOrderNo']");
	private By getcustomerordno= By.xpath("//*[@id='closeOutlineOrderDetailscustomerOrderNumber']");
	private By getnoofpieces= By.xpath("//*[@placeholder='Enter Number Of Pieces']");
	private By getactualwt= By.xpath("//*[@placeholder='Enter Actual Weight']");
	private By getlitnoofoieces= By.xpath("//*[@placeholder='Enter Literature Number Of Pieces']");
	private By getlitactualwt= By.xpath("//*[@placeholder='Enter Literature Actual Weight']");
	private By getpickconsingee= By.xpath("//*[@placeholder='Select Pickup Consignee']");
	private By expandBtn=By.xpath("//*[@title='Click to expand row']");
	private By clearBtn=By.xpath("//*[@id='closeOutlineClearButton']");
	private By selectoriginLDA =By.xpath("//*[@id='closeOutlineOrderDetailslocalCarrier']/span");
	private By selectpickupLda =By.xpath("(//*[.='Select Pickup LDA'])[3]");
	private By pickupLdaCode =By.xpath("//*[@placeholder='Enter Pickup LDA Code']");
	private By pickupConsignee =By.xpath("//*[@placeholder='Select Pickup Consignee']");
	private By fetchCloseoutOrderTextLocator =By.xpath("//td[contains(text(), 'GL-')]");
	private By fetchcustordertext =By.xpath("//table/tbody/tr[1]/td[5]");
	private By ordernum =By.xpath("//*[@placeholder='Enter Closeout Order Number']");
	private By fetchpickupldacode =By.xpath("//table/tbody/tr[2]/td[10]");
	
	/**
	 * This method used to open closeout menu
	 * @throws TimeoutException
	 * @throws InterruptedException 
	 */
	public void closeOutMenu() throws TimeoutException, InterruptedException {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		wt.waitToClickWithAction(OutMenu, 10);
	    Thread.sleep(1500);
		wt.waitToClickWithAction(closeOutMenu, 10);
		wt.waitToClick(subMenu, 10);
		cp.waitForLoaderToDisappear();

	}
	/**
	 * This method used to open closeout sub menu
	 * @throws TimeoutException
	 */
	public void closeSubMenu() throws TimeoutException {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		wt.waitToClick(subMenu, 10);
		cp.waitForLoaderToDisappear();
	}
	
	/**
	 * This method use to click add new close out order btn
	 * @throws InterruptedException 
	 */
	public void AddNewOrderBtn() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		wt.waitToClick(createOrder, 10);
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
	 * Clicks on the confirm button.
	 */
	public void clickOnConfirmBtn() {
		wt.waitToClickWithAction(confirm, 10);
	
	}

	/**
	 * Selects the consignee from the list.
	 */
	public void checkConsingee() {
		wt.waitToClick(selectconsingee, 5);
	}

	/**
	 * Checks if the consignee list is not found.
	 * @return true if list is not found, false otherwise
	 */
	public boolean isConsigneeListNotFound() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//*[@class='d-flex align-items-center mt-1 pitem ']")));
			return false;
		} catch (Exception e) {
			return true;
		}
	}

	/**
	 * Selects the order type from the dropdown.
	 */
	public void checkOrderType() {
		wt.waitToClick(selectorderType, 5);
	}
	
	/**
	 * Checks if a dropdown is empty (shows 'No available options').
	 * @return true if dropdown has no options, false otherwise
	 */
	public boolean isDropDownEmpty() {
		return driver.findElement(By.xpath("//li[.='No available options']")).isDisplayed();
	}

	/**
	 * This methods is used to select customer and verify consignee and order type
	 * available
	 * @throws InterruptedException
	 */
	public void selectAnyValidCustomerForCloseOutOrFail() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		for (int i = 0; i < 25; i++) {
			WebElement selectcustomer = wait.until(ExpectedConditions.elementToBeClickable(searchcustomer));
			selectcustomer.click();
			WebElement List = driver.findElement(list);
			wait.until(ExpectedConditions.visibilityOfElementLocated(list));
			wt.waitForElementClickable(list, 10);

			List<WebElement> customerslist = driver.findElements(list);

			if (customerslist.isEmpty()) {
				logger.info("Not custome found method Will failed..");
			}
			WebElement customer = customerslist.get(i);
			System.out.println("customer " + i + ": " + customer.getText());
			customer.click();
			cp.waitForLoaderToDisappear();
			try {
				driver.findElement(selectconsingee).click();
				List<WebElement> options = driver.findElements(By.xpath("//*[text()='No available options']"));

				if (!options.isEmpty() && options.get(0).isDisplayed()) {
					logger.warning("Consignee dropdown is empty for customer index: " + i + ".Trying next customer.");
					continue;
				}
			} catch (Exception e) {
				logger.info("some error while checking consignee");
			}
			driver.findElement(By.xpath("//input[@placeholder='Enter Auditor Name']")).click();
			// MPicklocation = driver.findElement(pickupadd).getText().trim();
//			if (MPicklocation.isEmpty()) {
//				logger.warning("Pickup location is empty for customer index: " + i + ". Trying next customer.");
//				continue;
//			}			
			try {
				driver.findElement(selectorderType).click();
				WebElement optionsOrderT = driver.findElement(By.xpath("//*[text() ='No available options']"));
				if (optionsOrderT.isDisplayed()) {
					logger.warning("Consignee dropdown is empty for customer index: " + i + ". Trying next customer.");
					continue;
				}
			} catch (Exception e) {
				logger.info("some error while checking Order type");
			}

//			logger.info(
//					"Valid customer found with populated consignee and pickup location & order type : " + MPicklocation);
			System.out.println("check consignee drop close for close out:");

			return;

		}
		cp.waitForLoaderToDisappear();
		logger.severe("No customer with valid consignee and pickup location found. Failing test.");
		Assert.fail("No valid customer found.");
	}
	
	/**
	 * This methods is used to select consignee
	 * 
	 * @throws InterruptedException
	 */
	public void selectAnyConsigneeOrSkip() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		wait.until(ExpectedConditions.elementToBeClickable(selectconsingee)).click();

		wait.until(driver -> {
			List<WebElement> options = driver.findElements(clist);
			return options.size() > 1;
		});

		driver.findElement(By.xpath("//input[@placeholder='Enter Auditor Name']")).click();
		wait.until(ExpectedConditions.elementToBeClickable(selectconsingee)).click();
		List<WebElement> consignee = driver.findElements(clist);
		int consigneeCount = consignee.size();

		logger.info("Total consignee found in list: " + consigneeCount);

		if (consigneeCount == 1) {
			logger.warning("No Consignee found in dropdown. Skipping test.");
			throw new SkipException("No Consignee available in the dropdown.");
		}

		WebElement selectConsignee = consignee.get(0);
		logger.info("Seleted consignee is " + selectConsignee);
		wait.until(ExpectedConditions.visibilityOfElementLocated(clist));
		try {
			driver.findElements(clist).get(0).click();
		} catch (StaleElementReferenceException e) {
			logger.warning("StaleElementReferenceException caught. Retrying click...");
			wait.until(ExpectedConditions.elementToBeClickable(selectconsingee)).click();// try it work or not its add
																							// for outbox index
			driver.findElements(clist).get(0).click();
		}
		cp.waitForLoaderToDisappear();
	}

	/**
	 * This method is used to select first order type from drop down
	 * 
	 * @throws InterruptedException
	 */
	public void selectFirstOrderType() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(selectordertype));
		dropdown.click();

		By options = By
				.xpath("//div[contains(@class,'p-dropdown-items-wrapper')]//li[not(contains(@class,'p-disabled'))]");
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(options));

		List<WebElement> orderTypes = driver.findElements(options);
		if (orderTypes.isEmpty()) {
			logger.warning("No order types found in dropdown.");
			throw new SkipException("No order types available.");
		}
		orderTypes.get(0).click();
		cp.waitForLoaderToDisappear();
	}

	/**
	 * This method is used to enter customer order number. Generate Random number
	 * 
	 * @param orderNumber
	 * @throws InterruptedException
	 */
	public void enterCustOrdNum(String orderNumber) throws InterruptedException {
		cp.waitForLoaderToDisappear();
		wait.until(ExpectedConditions.elementToBeClickable(enterordernum));
		driver.findElement(enterordernum).clear();
		driver.findElement(enterordernum).sendKeys(orderNumber);
	}

	/**
	 * This method is used to return true if carrier available
	 * 
	 * @return
	 */
	public boolean isCarrierDefaultSelected() {
		String value = cp.getMandatoryText(getOrignalLocCarrier);
		return value != null && !value.trim().isEmpty();
	}

	/**
	 * This method is use to click closeout type
	 */
	public void clickOnCloseOutType() {
		wt.waitToClick(selectCloseOutType, 10);
	}

	/**
	 * This method is used to select Rep return close out order type
	 */
	public void selectRepReturn() {
		wt.waitToClick(repReturn, 10);
	}

	/**
	 * This method is used to select transfer sample close out order type
	 */
	public void selectTrasferSample() {
		wt.waitToClick(tranSample, 10);
	}

	/**
	 * This method is used to select consignee termination close out order type
	 */
	public void selectConsigneeTer() {
		wt.waitToClick(consTerm, 10);
	}

	/**
	 * Verifies that the CloseOut Type dropdown contains all required options. This
	 * method locates all dropdown items using the provided XPath and checks whether
	 * the following options are present:
	 * 
	 * @return true if all the required options are available in the CloseOut Type
	 *         dropdown, false otherwise.
	 */
	public boolean checkCloseOutTypeOption() {
		List<WebElement> closeOutTypeList = driver.findElements(By.xpath("//span[@class='p-dropdown-item-label']"));

		boolean hasRepReturn = false;
		boolean hasConsigneeTermination = false;
		boolean hasTransferOfSamples = false;

		for (WebElement option : closeOutTypeList) {
			String text = option.getText().trim();
			if (text.contains("Rep Return")) {
				hasRepReturn = true;
			}
			if (text.contains("Consignee Termination")) {
				hasConsigneeTermination = true;
			}
			if (text.contains("Transfer of Samples")) {
				hasTransferOfSamples = true;
			}
		}

		return hasRepReturn && hasConsigneeTermination && hasTransferOfSamples;
	}

	/**
	 * This method is used to Add special instructions
	 * 
	 * @param instuction
	 */
	public void addSpecialInstuction(String instuction) {
		try {
			wt.waitToClick(enterSpecialInst, 5);
			driver.findElement(enterSpecialInst).sendKeys(instuction);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * THis method is used to get origin order status
	 * 
	 * @return
	 */
	public String getOriginOrderStatus() {
		cp.waitForLoaderToDisappear();
		return cp.getAttributeValue(originstatus, "value");
	}

	/**
	 * This method is use to return mandatory message on pick up details
	 * 
	 * @param i
	 * @return
	 */
	public String getManTextOnPickupDetails(int i) {
		return cp.getMandatoryText(By.xpath("(//*[contains(text(), 'is required')])[" + i + "]"));
	}

	/**
	 * This method is use to select pick up details
	 * 
	 * @throws InterruptedException
	 */
	public void addPickUpDetails() throws InterruptedException {
		wt.waitToClick(scheddate, 10);
		Thread.sleep(1000);
		wt.waitToClick(dateSel, 10);
		driver.findElement(repname).sendKeys("jack");
		wt.waitToClick(pickupdate, 10);
		Thread.sleep(1000);
		wt.waitToClick(dateSel, 10);
		driver.findElement(signby).sendKeys("Jorge");

	}

	/**
	 * This method is used to get address if null then send address
	 * 
	 * @param Address
	 * @return
	 * @throws InterruptedException
	 */
	public String getAdrees1(String Address) throws InterruptedException {
		String address = cp.getMandatoryText(fetchaddress1);

		if (address.isEmpty()) {
			driver.findElement(fetchaddress1).sendKeys(Address);

		} else {
			logger.info("consignee adress availble");
		}
		address = cp.getMandatoryText(fetchaddress1);
		return address;
	}

	/**
	 * This method is used to get state if null then send state
	 * 
	 * @param st
	 * @return
	 * @throws InterruptedException
	 */
	public String state(String st) throws InterruptedException {
		String state = cp.getMandatoryText(fetchstate);

		if (state.isEmpty()) {
			WebElement selectdropdown = driver.findElement(fetchstate);
			cp.selectSearchableDropdown(selectdropdown, st);

		} else {
			logger.info("State is available availble");
		}

		state = cp.getMandatoryText(fetchstate);
		return state;

	}

	/**
	 * This method is used to get city if null then enter city
	 * 
	 * @param city
	 * @return
	 * @throws InterruptedException
	 */
	public String getCity(String city) throws InterruptedException {
		String City = cp.getAttributeValue(fetchcity, "value");

		if (City.isEmpty()) {
			driver.findElement(fetchcity).sendKeys(city);
		} else {
			logger.info("City is availble");
		}
		City = cp.getAttributeValue(fetchcity, "value");
		return City;
	}

	/**
	 * This method is used to get zipcode if null then send zipcode
	 */
	public String getZipcode(String zip) throws InterruptedException {
		String zipcode = cp.getAttributeValue(fetchzipcode, "value");

		if (zipcode.isEmpty()) {
			driver.findElement(fetchzipcode).sendKeys(zip);
		} else {
			logger.info("Zipcode is availble");
		}
		zipcode = cp.getAttributeValue(fetchzipcode, "value");
		return zipcode;
	}

	/**
	 * This method is used to enter dim weight
	 * 
	 * @param dimWeight
	 */
	public void enterDimWeight(String dimWeight) {
		cp.clickAndSendKeys(enetrdimweight, dimWeight);
	}

	/**
	 * This method is used to enter pieces
	 * 
	 * @param pieces
	 */
	public void enterPieces(String pieces) {
		cp.clickAndSendKeys(enetrpieces, pieces);
	}

	/**
	 * This method is used to enter lit dim weight
	 * 
	 * @param litWeight
	 */
	public void enterLitWeight(String litWeight) {
		cp.clickAndSendKeys(enterLitweight, litWeight);
	}

	/**
	 * This method is used to enter Lit dim weight
	 * 
	 * @param litDimWeight
	 */
	public void enterLitDimWeight(String litDimWeight) {
		cp.clickAndSendKeys(enetrLitdimweight, litDimWeight);
	}

	/**
	 * This method is used to enter lit pieces
	 * 
	 * @param litPieces
	 */
	public void enterLitPieces(String litPieces) {
		cp.clickAndSendKeys(enetrLitpieces, litPieces);
	}

	/**
	 * This method is used to enter Actual weight
	 * 
	 * @param actWeight
	 */
	public void enterActWeight(String actWeight) {
		logger.info("Entering Actual Weight: " + actWeight);
		wait.until(ExpectedConditions.elementToBeClickable(enteractweight));
		driver.findElement(enteractweight).clear();
		driver.findElement(enteractweight).sendKeys(actWeight);
	}

	/**
	 * This method is used to get ratedWeight 
	 * @return
	 */
	public String ratedWeight() {
		return cp.getAttributeValue(ratedweight, "value");
	}

	/**
	 * This method is used to get ratedLitWeight
	 * 
	 * @return
	 */
	public String ratedLitweight() {
		return cp.getAttributeValue(ratedlitweight, "value");
	}

	/**
	 * THis method use to get max weight from dim and actual weight
	 * @return
	 */
	public String getMaxWt() {
		String acrwt = cp.getAttributeValue(enteractweight, "value");
		String dimwt = cp.getAttributeValue(enetrdimweight, "value");

		int actWeight = Integer.parseInt(acrwt);
		int dimWeight = Integer.parseInt(dimwt);

		return String.valueOf(Math.max(actWeight, dimWeight));
	}
	
	/**
	 * This method use to get max weight from literature dim and literature actual weight 
	 * @return
	 */
	public String getMaxLitWt() {
		String acrwt = cp.getAttributeValue(enterLitweight, "value");
		String dimwt = cp.getAttributeValue(enetrLitdimweight, "value");

		int actWeight = Integer.parseInt(acrwt);
		int dimWeight = Integer.parseInt(dimwt);

		return String.valueOf(Math.max(actWeight, dimWeight));
	}

	/**
	 * This method is Used to click accessorial button
	 */
	public void clickAccessorialButton() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		logger.info("Attempting to click the accessorial button");
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(custaccess));
			WebElement accessorialButton = driver.findElement(custaccess);
			js.scrollIntoView(accessorialButton);
			wait.until(ExpectedConditions.elementToBeClickable(accessorialButton));

			cp.moveToElementAndClick(custaccess);
			accessorialButton.click();

			logger.info("Clicked on Accessorial button successfully.");

		} catch (Exception e) {
		}
		logger.info("Unable to click Accessorial button");
	}

	/**
	 * This method is used to add accessorial
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public String addAccessorial() throws InterruptedException {
		logger.info("Attempting to add accessorial with empty values to capture validation messages");
		WebElement Saveacces = driver.findElement(saveaccess);
		cp.clickElement(saveaccess);
		String accessorialText = driver.findElement(custaccesstext).getText();
		String priceText = driver.findElement(pricetext).getText();
		logger.info("Validation messages received - Accessorial: " + accessorialText + ", Price: " + priceText);
		return accessorialText + " | " + priceText;
	}

	/**
	 * This method is used to click on first edit order btn
	 */
	public void editFirstorder() {
		cp.waitForLoaderToDisappear();
		WebElement editButton = wait.until(ExpectedConditions.visibilityOfElementLocated(edit));
		editButton.click();
		cp.waitForLoaderToDisappear();
	}

	/**
	 * This method is used to save customer accessorial
	 */
	public void saveCustAccess() {
		wt.waitToClick(selectcustaccess, 10);
		WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(input));
		logger.info("save accessorial success...");
		searchBox.sendKeys(Keys.ARROW_DOWN);
		searchBox.sendKeys(Keys.ENTER);
		wt.waitToClick(saveaccess, 10);
		logger.info("Customer Access saved successfully.");
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
	}

	/**
	 * This method is used to edit and customer accessorial
	 * 
	 * @param price2
	 */
	public void editCustAccess(String price2) {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		wait.until(ExpectedConditions.elementToBeClickable(editbutton)).click();
		WebElement priceField = wait.until(ExpectedConditions.visibilityOfElementLocated(enterprice));
		priceField.clear();
		priceField.sendKeys(price2);
		wait.until(ExpectedConditions.elementToBeClickable(saveaccess)).click();
		logger.info("Customer Access edited and saved.");
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
	}

	/**
	 * This method is used to Delete Customer Accessorial
	 * 
	 */
	public void deleteCustAccess() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		wait.until(ExpectedConditions.elementToBeClickable(delete)).click();
		WebElement yesButton = wait.until(ExpectedConditions.elementToBeClickable(yesbutton));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", yesButton);
		logger.info("Customer Access deleted successfully.");

	}

	/**
	 * This method is used to Re-save Customer Access After Delete
	 */
	public void saveCustAccessAfterDelete() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		cp.waitForLoaderToDisappear();
		wait.until(ExpectedConditions.elementToBeClickable(custaccess)).click();
		driver.findElement(selectcustaccess).click();
		WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(input));
		logger.info("saving accessorial");
		searchBox.sendKeys(Keys.ARROW_DOWN);
		searchBox.sendKeys(Keys.ENTER);

		wait.until(ExpectedConditions.elementToBeClickable(saveaccess)).click();
		logger.info("Customer Access re-saved successfully.");

	}

	/**
	 * This method is used to add carrier accessorial
	 * 
	 * @return
	 */
	public String addCarrAccessorial() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		WebElement addCarrBtn = wait.until(ExpectedConditions.elementToBeClickable(addcarr));
		addCarrBtn.click();

		try {
			wait.until(ExpectedConditions.elementToBeClickable(saveaccess)).click();
		} catch (ElementNotInteractableException e) {
			logger.warning("Save button not interactable; using JavaScript click as fallback.");
			WebElement saveBtn = wait.until(ExpectedConditions.presenceOfElementLocated(saveaccess));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
		}

		WebElement accessorialTextElement = wait
				.until(ExpectedConditions.visibilityOfElementLocated(carrieraccesstext));
		String accessorialText = accessorialTextElement.getText();

		WebElement priceTextElement = wait.until(ExpectedConditions.visibilityOfElementLocated(pricetext));
		String priceText = priceTextElement.getText();

		logger.info("Carrier Accessorial validation message: " + accessorialText + ", Price message: " + priceText);
		return accessorialText + " | " + priceText;
	}

	/**
	 * This method is used to Save Carrier accessorial
	 * 
	 * @param price
	 * @param text
	 */
	public void saveCarrAccess(String price, String text) {
		wt.waitToClick(selectcarrtaccess, 10);
		WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(input));
		searchInput.sendKeys(Keys.ARROW_DOWN);
		searchInput.sendKeys(Keys.ENTER);
		wt.waitToClick(selectcarrtaccesstype, 10);
		WebElement searchInputType = wait.until(ExpectedConditions.presenceOfElementLocated(input));
		searchInputType.sendKeys(Keys.ARROW_DOWN);
		searchInputType.sendKeys(Keys.ENTER);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(enterprice)).clear();
			driver.findElement(enterprice).sendKeys(price);
			wait.until(ExpectedConditions.presenceOfElementLocated(enterreason)).sendKeys(text);
		} catch (Exception e) {
			// TODO: handle exception
		}
		wait.until(ExpectedConditions.elementToBeClickable(saveaccess)).click();
		logger.info("Carrier Access saved successfully.");
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
	}

	/**
	 * This Method is used to Edit Carrier Access
	 * 
	 * @param price2
	 */
	public void editCarrAccess(String price2) {
		wait.until(ExpectedConditions.elementToBeClickable(editbuttoncarr)).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(enterprice)).clear();
		driver.findElement(enterprice).sendKeys(price2);
		wait.until(ExpectedConditions.elementToBeClickable(saveaccess)).click();
		logger.info("Carrier Access edited and saved");
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
	}

	/**
	 * This Method is used to Delete Carrier Access
	 */
	public void deleteCarrAccess() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		wait.until(ExpectedConditions.elementToBeClickable(deletecarr)).click();
		WebElement yesButton = wait.until(ExpectedConditions.elementToBeClickable(yesbutton));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", yesButton);
		logger.info("Carrier Access deleted successfully");
	}

	/**
	 * This Method is used to Re-save Carrier Access After Delete
	 * 
	 * @param price
	 * @param text
	 */
	public void saveCarrAccessAfterDelete(String price, String text) {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		wait.until(ExpectedConditions.elementToBeClickable(addcarr)).click();
		wt.waitToClick(selectcarrtaccess, 10);
		WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(input));
		searchInput.sendKeys(Keys.ARROW_DOWN);
		searchInput.sendKeys(Keys.ENTER);

		wt.waitToClick(selectcarrtaccesstype, 10);
		WebElement searchInputType = wait.until(ExpectedConditions.presenceOfElementLocated(input));
		searchInputType.sendKeys(Keys.ARROW_DOWN);
		searchInputType.sendKeys(Keys.ENTER);

		wait.until(ExpectedConditions.presenceOfElementLocated(enterprice)).clear();
		driver.findElement(enterprice).sendKeys(price);
		wait.until(ExpectedConditions.presenceOfElementLocated(enterreason)).sendKeys(text);

		wait.until(ExpectedConditions.elementToBeClickable(saveaccess)).click();
		logger.info("Carrier Access re-saved successfully");

	}

	/**
	 * This method id used to add comment
	 * 
	 * @return
	 */
	public String addComment() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(comment));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();

		WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(saveaccess));
		saveButton.click();

		String accessorialText = wait.until(ExpectedConditions.visibilityOfElementLocated(typetext)).getText();
		String priceText = wait.until(ExpectedConditions.visibilityOfElementLocated(subjecttext)).getText();
		logger.info("Captured validation: Type = " + accessorialText + ", Subject = " + priceText);

		return accessorialText + " | " + priceText;

	}

	/**
	 * This Method is used to save a comment
	 * 
	 * @param commentType
	 * @param subject
	 * @param comments
	 */
	public void saveComment(String subject, String comments) {
	    try {
	        wait.until(ExpectedConditions.elementToBeClickable(selecttype)).click();

	        WebElement inputTypeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(input));
	        wt.waitToClick(By.xpath("//*[@class='p-dropdown-item-label']"), 10);

	        WebElement subjectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(entersubject));
	        subjectElement.sendKeys(subject);

	        WebElement commentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(entercomment));
	        commentElement.sendKeys(comments);

	      //  wait.until(ExpectedConditions.elementToBeClickable(saveaccess)).click();
	        logger.info("Comment saved successfully");
	        cp.waitForLoaderToDisappear();
	        cp.waitForPopupToDisappear();

	    } catch (Exception e) {
	        logger.info("Failed to save comment: " + e.getMessage());
	    } finally {
	    	
	    	 cp.closePopupIfPresent("(//*[@data-testid='HighlightOffIcon'])[8]");
	    }
	    
	}


	/**
	 * This method is used to edit a comment
	 * 
	 * @param newSubject
	 */
	public void editComment(String newSubject) {
		wait.until(ExpectedConditions.elementToBeClickable(editcomment)).click();

		WebElement subjectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(entersubject));
		subjectElement.clear();
		subjectElement.sendKeys(newSubject);

		wait.until(ExpectedConditions.elementToBeClickable(closecommnet)).click();
		logger.info("Comment edited and closed");
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
	}

	/**
	 * This Method is used to delete a comment
	 * 
	 * @throws InterruptedException
	 */
	public void deleteComment() throws InterruptedException {
		cp.waitForPopupToDisappear();
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(deletecomment)).click();
		WebElement yesButton = wait.until(ExpectedConditions.elementToBeClickable(yescomment));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", yesButton);
		logger.info("Comment deleted");

	}

	/**
	 * This Method is used to re-save a comment after deletion
	 * 
	 * @param commentType
	 * @param subject
	 * @param comments
	 */
	public void saveCommentAfterDelete(String subject, String comments) {
	    try {
	        cp.waitForLoaderToDisappear();
	        cp.waitForPopupToDisappear();

	        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(comment));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	        element.click();

	        wait.until(ExpectedConditions.elementToBeClickable(selecttype)).click();
	        WebElement inputTypeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(input));
	        wt.waitToClick(By.xpath("//*[@class='p-dropdown-item-label']"), 10);

	        WebElement subjectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(entersubject));
	        subjectElement.sendKeys(subject);

	        WebElement commentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(entercomment));
	        commentElement.sendKeys(comments);

	        wait.until(ExpectedConditions.elementToBeClickable(saveaccess)).click();
	        logger.info("Comment re-saved successfully");

	    } catch (Exception e) {
	        logger.info("Failed to re-save comment: " + e.getMessage());
	    } finally {
	        cp.closePopupIfPresent("(//*[@data-testid='HighlightOffIcon'])[8]");
	    }
	}


	/**
	 * This method is used to add File 
	 * @return
	 */
	public String addFile() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		WebElement addFileButton = wait.until(ExpectedConditions.elementToBeClickable(addfile));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addFileButton);
		addFileButton.click();

		wait.until(ExpectedConditions.elementToBeClickable(saveaccess)).click();

		WebElement docTextElement = wait.until(ExpectedConditions.visibilityOfElementLocated(doctext));
		WebElement fileTextElement = wait.until(ExpectedConditions.visibilityOfElementLocated(filetext));
		logger.info("Validation messages captured - Document: " + docTextElement.getText() + ", File: "
				+ fileTextElement.getText());

		return docTextElement.getText() + " | " + fileTextElement.getText();
	}

	/**
	 * This method is used to save file
	 * @param file
	 * @return
	 */
	public String savefile(String file) {
		try {
			WebElement docField = driver.findElement(documnet);
			docField.click();
			docField.sendKeys(file);

			By fileInputLocator = By.xpath("//input[@type='file']");
			WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(fileInputLocator));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fileInput);

			String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\attachments\\demo.txt";
			fileInput.sendKeys(filePath);

			WebElement saveButton = driver.findElement(saveaccess);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);
			logger.info("Save button clicked for file upload");

			WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[@role='alert']/div[contains(text(),'File save successfully')]")));

			logger.info("File Upload Message: " + successMessage.getText());

			if (successMessage.getText().contains("File save successfully")) {
				return "File saved successfully";
			} else {
				logger.warning("File not saved");
				return "File not saved";
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error during file upload", e);
			return "Error during file upload: " + e.getMessage();
		} finally {
	    	
	    	 cp.closePopupIfPresent("(//*[@data-testid='HighlightOffIcon'])[8]");
	    }

	}

	/**
	 * This method is used to delete file
	 * @throws InterruptedException
	 */
	public void deleteFile() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		wait.until(ExpectedConditions.elementToBeClickable(deletefile)).click();
		WebElement yesButton = wait.until(ExpectedConditions.elementToBeClickable(yesbutton));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", yesButton);
		logger.info("file deleted");
		cp.clickToClosePopUp();

	}
	/**
	 * This method checks whether a file exists at the given download location. If
	 * the file is found, it deletes the existing file to ensure a clean state for
	 * the next download.
	 */
	public void deleteExistingFiles() {
		File dir = new File(downloadDir);
		if (dir.isDirectory()) {
			Arrays.stream(dir
					.listFiles((d, name) -> name.endsWith(".xlsx") || name.endsWith(".xls") || name.endsWith(".pdf")))
					.forEach(file -> {
						System.out.println("Deleted file: " + file.getName());
						file.delete();
					});
		}
	}

	/**
	 * This method waits for a file to be downloaded in the specified directory. If
	 * a new file appears within the timeout, it returns true; otherwise, false.
	 *
	 * @return true if file is downloaded successfully, false otherwise
	 * @throws InterruptedException
	 */
	public boolean isFileDownloaded() throws InterruptedException {

		File dir = new File(downloadDir);
		File[] filesBefore = dir.listFiles();
		int initialCount = filesBefore != null ? filesBefore.length : 0;
		int timeoutSec = 30;
		int waited = 0;

		while (waited < timeoutSec) {
			Thread.sleep(1000);
			File[] filesAfter = dir.listFiles();

			if (filesAfter != null && filesAfter.length > initialCount) {
				System.out.println("File downloaded: " + filesAfter[filesAfter.length - 1].getName());
				return true;
			}
			waited++;
		}
		System.out.println("No file downloaded within timeout.");
		return false;
	}
	
	/**
	 * This method used to click file download icon
	 */
	public void downloadUploadedFile() {
		wt.waitToClick(By.xpath("//*[@data-testid='FileDownloadIcon']"), 10);
	}
	
	String toastMsg;
	/**
	 * This method is used to click on confirm/tender btn and Yes btn from Pop up
	 */
	public void clickToTenderOrder() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		wt.waitToClick(confirm, 10);
		cp.waitAndClickWithJS(yesbutton, 10);
		toastMsg=cp.captureToastMessage();
	}
	/**
	 * This method used to return message from toast
	 * @return
	 */
	public String getToastText() {
		return toastMsg;
	}
	
	/**
	 * method is click On manifest print
	 */
	public void printManifest() {
		wt.waitToClickWithAction(ormanifest, 10);
	}
	
	/**
	 * method is click On LBOL print
	 */
	public void printLBOL() {
		wt.waitToClickWithAction(orlbol, 10);
	}
	
	/**
	 * This method is used to get QA status
	 * @return
	 */
	public String getQAStatus() {
		return cp.getMandatoryText(By.xpath("//*[.='QA Passed']"));	
	}
	

	/**
	 * This method is used to click on close out line
	 */
	public void clickOnCloseOutLine() {
		cp.waitForLoaderToDisappear();
		wt.waitToClickWithAction(addcloseoutline, 20);
		cp.waitForLoaderToDisappear();
	}
	
	/**
	 * Retrieves the number of pieces from the input field.
	 * @return the pieces count as an integer
	 */
	public int getPieces() {
		return Integer.parseInt(cp.getAttributeValue(enetrpieces, "value").trim());
	}
	
	/**
	 * Retrieves the number of  literature pieces from the input field.
	 * @return the pieces count as an integer
	 */
	public int getLitPieces() {
		return Integer.parseInt(cp.getAttributeValue(enetrLitpieces, "value").trim());
	}
	
	
	/**
	 * Retrieves the line pieces count from the element text.
	 * @return the line pieces as an integer
	 */
	public int getLinePieces() {
	    return Integer.parseInt(cp.getMandatoryText(linePieces).replace("/", "").trim());
	}
	
	/**
	 * Retrieves the line pieces count from the element text.
	 * @return the line pieces as an integer
	 */
	public int getLineLitPieces() {
	    return Integer.parseInt(cp.getMandatoryText(lineLitPieces).replace("/", "").trim());
	}
	
	/**
	 * THis method used to get consingee to value
	 * @return
	 * @throws InterruptedException 
	 */
	public String getConsingeeTo() throws InterruptedException {
		Thread.sleep(1000);
		return cp.getMandatoryText(getConsigneeto);
	}
	
	/**
	 * This method used to click on save btn on closeout line
	 */
	public void saveLine() {
		cp.waitForPopupToDisappear();
		wt.waitToClick(saveBtnOnline, 20);
		toastMsg=cp.captureToastMessage();
		
	}
	
	/**
	 * Clicks on the warehouse process checkbox.
	 */
	public void clickWHcheckBox() {
		driver.findElement(warehouseprocess).click();
	}
	
	/**
	 * Selects a consignee type from the dropdown based on the given value.
	 * @param value the consignee type to select
	 */
	public void selectConsigneeType(String value) {
		wt.waitToClick(selectconsigneetype, 10);
		cp.DrpDwnValueSel(input, value);
		cp.waitForLoaderToDisappear();
	}
	
	/**
	 * Checks if the Consignee Address field is editable and not empty.
	 * @return true if editable and has value, false otherwise
	 */
	public boolean isConsigneeAddressEditableAndNotEmpty() {
	    WebElement field = driver.findElement(By.id("closeOutlineDetconsigneeAddress"));
	    WebElement field1 = driver.findElement(By.xpath("//input[@id='closeOutlineDetconsigneeAddress']"));
	    
	    boolean isEditable = field.isEnabled();

	    String value = field1.getAttribute("value").trim();
	    boolean isNotEmpty = !value.isEmpty();

	    return isEditable && isNotEmpty;
	}
	
	/**
	 * This method used to  enter contact number
	 * @param value
	 */
	public void enterContactNumber(String value) {
		try {
			driver.findElement(contactnumber).sendKeys(value);
		} catch (Exception e) {
			// TODO: handle exception
		}	
	}
	
	/**
	 * This method is used to enter email id
	 * @param value
	 */
	public void enterContactEmail(String value) {
		try {
			driver.findElement(contactemail).sendKeys(value);
		} catch (Exception e) {
			// TODO: handle exception
		}	
	}
	
	/**
	 * This method is select first warehouse from list
	 */
	public void selectWarehouse() {
		wt.waitToClick(selectwarehouse, 10);
		driver.findElement(firstvalue).click();
	}
	

	/**
	 * This method is use to eneter actual pieces on line item
	 * @param pieces
	 */
	public void enterActualPiecesForlineLine(String pieces) {
		cp.clickAndSendKeys(enterLinepieces, pieces);
	}
	
	/**
	 * Returns half of the line pieces.
	 * Example: if 12 → 6, if 11 → 5
	 * @return half of line pieces as integer
	 */
	public String getHalfPieces() {
	    int pieces = getLinePieces();
	    return String.valueOf(pieces / 2);
	}


	/**
	 * This method used to click and enter dim weight for line 
	 * @param dimwt
	 */
	public void enterDimWeightForLine(String dimwt) {
		cp.clickAndSendKeys(enterLineDimwt, dimwt);
	}
	
	/**
	 * This method used to click and enter actual weight for line 
	 * @param actwt
	 */
	public void enterActualWtForLine(String actwt) {
		cp.clickAndSendKeys(enterLineActualwt, actwt);
	}
	
	
	/**
	 * This method is used to select first order type from drop down
	 * 
	 * @throws InterruptedException
	 */
	public void selectFirstOrderTypeForline() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(selectLineOrdertype));
		dropdown.click();

		By options = By
				.xpath("//div[contains(@class,'p-dropdown-items-wrapper')]//li[not(contains(@class,'p-disabled'))]");
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(options));

		List<WebElement> orderTypes = driver.findElements(options);
		if (orderTypes.isEmpty()) {
			logger.warning("No order types found in dropdown.");
			throw new SkipException("No order types available.");
		}
		orderTypes.get(0).click();
		cp.waitForLoaderToDisappear();
	}

	/**
	 * This method used to enter literature pieces
	 * @param pieces
	 */
	public void eneterLitPiecesForLine(String pieces) {
		cp.clickAndSendKeys(enterLineLitpieces, pieces);
	}
	
	/**
	 * Returns half of the line pieces.
	 * Example: if 12 → 6, if 11 → 5
	 * @return half of line pieces as integer
	 */
	public String getHalfLitPieces() {
	    int pieces = getLineLitPieces();
	    return String.valueOf(pieces / 2);
	}
	
	/**
	 * This mehod is used to enter literature actual weight
	 * @param actwt
	 */
	public void enterLiteratureWeightForLine(String actwt) {
		cp.clickAndSendKeys(enterLineLitwt, actwt);
	}
	
	/**
	 * This method used to enter dim dim weight for line
	 * @param dimwt
	 */
	public void enterLiterDimWeightForLine(String dimwt) {
		cp.clickAndSendKeys(enterLineLitDimwt, dimwt);
	}
	
	/**
	 * This method is used to get ratedWeight from line
	 * @return
	 */
	public String ratedWeightOnLine() {
		return cp.getAttributeValue(ratedweightline, "value");
	}

	/**
	 * This method is used to get ratedLitWeight from line
	 * @return
	 */
	public String ratedLitweightOnLine() {
		return cp.getAttributeValue(ratedlitweightline, "value");
	}
	
	/**
	 * This method use to get max weight from dim and actual weight on closeout line
	 * @return
	 */
	public String getMaxWtFromLine() {
		String acrwt = cp.getAttributeValue(enterLineActualwt, "value");
		String dimwt = cp.getAttributeValue(enterLineDimwt, "value");

		int actWeight = Integer.parseInt(acrwt);
		int dimWeight = Integer.parseInt(dimwt);

		return String.valueOf(Math.max(actWeight, dimWeight));
	}
	
	/**
	 * This method use to get max weight from literature dim and literature actual weight closeout line
	 * @return
	 */
	public String getMaxLitWtFromLine() {
		String acrwt = cp.getAttributeValue(enterLineLitwt, "value");
		String dimwt = cp.getAttributeValue(enterLineLitDimwt, "value");

		int actWeight = Integer.parseInt(acrwt);
		int dimWeight = Integer.parseInt(dimwt);

		return String.valueOf(Math.max(actWeight, dimWeight));
	}

	/**
	 * this method will return origin location
	 * @return
	 */
	public String getOriginLocation() {
		return cp.getAttributeValue(By.xpath("//*[@id='closeOutlineDetorigin']"), "value");
	}
	/**
	 * this method will return current location of the order
	 * @return
	 */
	public String getCurrentLocation() {
		return cp.getAttributeValue(By.xpath("//*[@id='closeOutlineDetcurrentLocation']"), "value");
	}
	
	/**
	 * This method is used to get current location of the order and spite that state and city
	 * @return
	 */
	public String getSplitCurrentLocation() {
	    String location = cp.getAttributeValue(By.xpath("//input[@id='closeOutlineDetcurrentLocation']"), "value");
	    if (location != null && location.contains(",")) {
	        return location.substring(0, location.indexOf(",")).trim();
	    }
	    return location;
	}
	
	public String getWarehouse() {
	    return cp.getMandatoryText(By.xpath("//*[@id='closeOutlineDetwarehouse']//*[@class='p-dropdown-label p-inputtext']"));
	}

	/**
	 * This method is used to get close out line status
	 * @return
	 */
	public String getLineStatus() {
		cp.waitForLoaderToDisappear();
		return cp.getAttributeValue(By.xpath("//*[@id='closeOutlineDetdeliveryStatus']"), "value");
	}

	/**
	 * Verifies that the Schedule Date, Delivery Date, and Delivery Signed By fields
	 * are non-editable (disabled) on the Delivery screen.
	 * @return true if all fields are disabled, false if any field is editable
	 */
	public boolean isDeliveryFieldNonEditable() {
	    WebElement schedule = driver.findElement(By.xpath("//*[@id='closeOutlineDetscheduledDateAndTime']//input"));
	    WebElement deliveryDate = driver.findElement(By.xpath("//*[@id='closeOutlineDetdeliveryDateAndTime']//input"));
	    WebElement delSignBy = driver.findElement(By.xpath("//*[@id='closeOutlineDetdeliverySignedBy']"));
	    
	    return !schedule.isEnabled() && !deliveryDate.isEnabled() && !delSignBy.isEnabled();
	}

	/**
	 * This method used to click on confirm close out line
	 */
	public void clickOnConfirmBtnOnLine() {
		wt.waitToClick(confirmLine, 10);
		cp.waitAndClickWithJS(yesbutton, 10);
		toastMsg=cp.captureToastMessage();
	}
	
	/**
	 * Checks whether the POD (Proof of Delivery) checkbox is selected.
	 * @return true if the POD checkbox is selected, false if it is not selected
	 */
	public boolean isPODChecked() {
	    WebElement podCheckbox = driver.findElement(By.xpath("//*[@id='closeOutlineDetpodReceived']//input[@type='checkbox']"));
	    return podCheckbox.isSelected();
	}
	
	/**
	 * Gets the total number of pieces and adds one extra piece.
	 * For example, if pieces = 12, this will return 13.
	 *
	 * @return pieces count incremented by one (as String)
	 */
	public String getMorePieces() {
	    int pieces = getLinePieces();
	    return String.valueOf(pieces + 1);
	}

	/**
	 * Gets the total number of lit pieces and adds one extra piece.
	 * For example, if pieces = 15, this will return 16.
	 *
	 * @return pieces count incremented by one (as String)
	 */
	public String getMoreLitPieces() {
		cp.waitForPopupToDisappear();
	    int pieces = getLineLitPieces();
	    return String.valueOf(pieces + 1);
	}
	/**
	 * This method is used to get order number
	 * @return
	 */
	public String getOrderNumber() {
		return cp.getAttributeValue(ordernumber, "value");
	}
	
	/**
	 * This method is used to get LBOL number for to origin order 
	 * @return
	 */
	public String getLBOLNumber() {
		return cp.getMandatoryText(lbolnumber);
	}
	
	/**
	 * This method is used to click on close closout line
	 * @throws InterruptedException 
	 */
	public void CloseLinePopup() throws InterruptedException {
		cp.waitForPopupToDisappear();
		Thread.sleep(3000);
		wt.waitToClick(closepopupBtn, 10);
	}
	
	/**
	 * This method is used to create line order for without warehouse process
	 * @throws InterruptedException
	 */
	public void createCloseOutLineWithoutWHProcess() throws InterruptedException {
		clickOnCloseOutLine();
		selectConsigneeType("Customer");
		enterContactNumber(cm.getNumericString(5));
		enterContactEmail("automate@email.com");
		enterActualPiecesForlineLine(getHalfPieces());
		enterDimWeightForLine(cm.getNumericString(2));
		selectFirstOrderTypeForline();
		enterActualWtForLine(cm.getNumericString(2));
		eneterLitPiecesForLine(getHalfLitPieces());
		enterLiteratureWeightForLine(cm.getNumericString(2));
		enterLiterDimWeightForLine(cm.getNumericString(2));
		clickOnConfirmBtnOnLine();
	}
	
	/**
	 * This method is used to create line for with warehoue process
	 * @throws InterruptedException
	 */
	public void createCloseOutLineWitWHProcess() throws InterruptedException {
		clickOnCloseOutLine();
		selectConsigneeType("Customer");
		clickWHcheckBox();
		enterContactNumber(cm.getNumericString(5));
		enterContactEmail("automate@email.com");
		selectWarehouse();
		enterActualPiecesForlineLine(getHalfPieces());
		enterDimWeightForLine(cm.getNumericString(2));
		selectFirstOrderTypeForline();
		enterActualWtForLine(cm.getNumericString(2));
		eneterLitPiecesForLine(getHalfLitPieces());
		enterLiteratureWeightForLine(cm.getNumericString(2));
		enterLiterDimWeightForLine(cm.getNumericString(2));
		//clickOnConfirmBtnOnLine();
		saveLine();
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
	}
	
	/**
	 * this method use to delete closeout line
	 */
	public void deleteFirstLine() {
		wt.waitToClick(deleteBtn, 10);
		cp.waitAndClickWithJS(yesbutton, 10);
	}
	
	/**
	 * Deletes an order only if its status is "Outbound Pallet Pending".
	 * If the status does not match, the test case will be skipped.
	 */
	public void deleteObtPendingOrder() {
		cp.waitForPopupToDisappear();
	    String status = cp.getMandatoryText(getstatus);
	    if (status.equals("Outbound Pallet Pending")) {
	        wt.waitToClick(deleteBtn, 10);
	        cp.waitAndClickWithJS(yesbutton, 10);
	        
	    } else {
	        throw new SkipException("Skipping test: Order status is not 'Outbound Pallet Pending'");
	    }
	}
	
	public void deleteObtQAPending() {
		cp.waitForPopupToDisappear();
	    String status = cp.getMandatoryText(getstatus);
	    if (status.equals("Outbound QA Pending")) {
	        wt.waitToClick(deleteBtn, 10);
	        cp.waitAndClickWithJS(yesbutton, 10);
	        
	    } else {
	        throw new SkipException("Skipping test: Order status is not 'Outbound Pallet Pending'");
	    }
	}
	
	/**
	 * Adds auditor details by entering name and number.
	 * @param name   the auditor's name
	 * @param number the auditor's contact number
	 */
	public void addAuditorDetails(String name, String number) {
		cp.clickAndSendKeys(auditorname, name);
		cp.clickAndSendKeys(auditorNumber, number);
		
	}
	
	/**
	 * Selects the first order from the listing.
	 */
	public void selectFirstOrder() {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(firstcheckBox, 10);
	}
	
	/**
	 * Clicks on the print template button.
	 */
	public void clickOnPrintTem() {
		wt.waitToClick(printtempbtn, 10);
	}
	
	/**
	 * Clicks the confirm button on the listing and confirms the action.
	 */
	public void confirmBtnOnListing() {
		wt.waitToClick(confrimbtn, 10);
		cp.waitAndClickWithJS(yesbutton, 10);
		toastMsg=cp.captureToastMessage();
	}
	
	/**
	 * Checks whether the "Ok" button is displayed on the page.
	 * @return true if the Ok button is visible, false otherwise
	 */
	public void clickOkBtn() {
	    wt.waitToClick(okBtn, 10);
	}
	
	/**
	 * Verifies if the given error message is displayed on the page.
	 *
	 * @param expectedMessage The message text (or part of it) to search for.
	 * @return true if the message is found and visible, otherwise false.
	 */
	
	public boolean isErrorMessagePresent(String expectedMessage) {
	    List<WebElement> elements = driver.findElements(
	        By.xpath("//*[contains(text(),'" + expectedMessage + "')]")
	    );
	    return !elements.isEmpty() && elements.get(0).isDisplayed();
	}

	/**
	 * This method used to add matrics detail for origin order
	 */
	public void addMetricsForOriginOrder() {
		enterActWeight(cm.getNumericString(2));
		enterDimWeight(cm.getNumericString(2));
		enterPieces(cm.getNumericString(2));
		enterLitWeight(cm.getNumericString(2));
		enterLitDimWeight(cm.getNumericString(2));
		enterLitPieces(cm.getNumericString(2));

	}
	/**
	 * This method use to enter no of pieces and literature weight
	 */
	public void addNoOfPiecesAndLitWtOriginOrder() {
		//enterActWeight(cm.getNumericString(2));
		//enterDimWeight(cm.getNumericString(2));
		enterPieces(cm.getNumericString(2));
		enterLitWeight(cm.getNumericString(2));
		//enterLitDimWeight(cm.getNumericString(2));
		//enterLitPieces(cm.getNumericString(2));
		
	}
	/**
	 * This method is used to get mandatory msg from actual wt
	 * @return
	 */
	public String getMandatoryTextActWt() {
		return cp.getMandatoryText(By.xpath("//div[contains(text(),'Actual Weight should be')]"));
	}
	
	/**
	 * This method is used to get mandatory msg from literature pieces
	 * @return
	 */
	public String getMandatoryTextActLitPiece() {
		return cp.getMandatoryText(By.xpath("//div[contains(text(),'Literature No Of Pieces should be')]"));
	}
	
	/**
	 * This method used to get pallet count from order page
	 * @return
	 */
	public String getNoOfPallet() {
		return cp.getAttributeValue(noofpallet, "value");
	}
	/**
	 * Selects a consignee (To) from the dropdown by value.
	 * @param value consignee to select
	 */
	public void selectConsigneeTo(String value) {
		wt.waitToClick(selectconsigneeto, 10);
		cp.DrpDwnValueSel(input, value);
		cp.waitForLoaderToDisappear();
	}
	
	/**
	 * Checks if the warehouse checkbox is not editable.
	 * @return true if warehouse checkbox is disabled, false otherwise
	 */
	public boolean isWarehouseNotEditabel() {
		WebElement checkbox = driver.findElement(warehouseprocess);
	    return !checkbox.isEnabled();
	}

	/**
	 * Selects a consignee for a line.
	 */
	public void selectCosigneeForLine() {
		wt.waitToClick(selectconsignee, 10);
		wt.waitToClick(clist, 10);
		cp.waitForLoaderToDisappear();
	}
	
	/**
	 * Selects an LDA for a line using the given value.
	 * @param value LDA to select
	 */
	public void selectSameLDAForLine(String value) {
		wt.waitToClick(selectLDA, 10);
		System.out.println("lda is "+ value);
		cp.clickAndSendKeys(input, value);
		wt.waitToClick(By.xpath("//*[@class='p-dropdown-items']"), 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Gets the LDA value before the colon (:) from current location field.
	 * @return trimmed LDA value or full value if colon not found
	 */
	public String getSameLDA() {
	    String sameLda = cp.getAttributeValue(By.xpath("//*[@id='closeOutlineDetcurrentLocation']"), "value");
	    if (sameLda != null && sameLda.contains(":")) {
	        return sameLda.substring(0, sameLda.indexOf(":")).trim();
	    }
	    return sameLda;
	}
	
	/**
	 * Gets the consignee from the pickup consignee field. rom origin order
	 * @return consignee value
	 */
	public String getConsignee() {
		return cp.getAttributeValue(By.xpath("//*[@id='closeOutlineOrderDetailspickupConsignee']"), "value");
	}
	
	/**
	 * Checks if the given consignee is not displayed in the list.
	 * @param value consignee to search
	 * @return true if consignee is not displayed, false otherwise
	 * @throws InterruptedException
	 */
	public boolean isConsigneeNotDisplayed(String value) throws InterruptedException {
		wt.waitToClick(selectconsignee, 10);
		cp.clickAndSendKeys(searchconsignee, value);
		Thread.sleep(1000);
		cp.waitForLoaderToDisappear();
	    List<WebElement> elements = driver.findElements(By.xpath("//div[contains(@class,'d-flex align-items-center mt-1 pitem ')]"));
		return elements.isEmpty();
	}
	
	/**
	 * Clears the consignee data from the field.
	 */
	public void clearConsigneeData() {
		wt.waitToClick(By.xpath("//*[@class='pi pi-times']"), 10);
		cp.waitForLoaderToDisappear();
	}
	
	/**
	 * Deletes an order only if its status is "Outbound Pallet Pending".
	 * If the status does not match, the test case will be skipped.
	 */
	public void deleteObtQAPendingOrder() {
		cp.waitForPopupToDisappear();
	    String status = cp.getMandatoryText(getstatus);
	    if (status.equals("Outbound QA Pending")) {
	        wt.waitToClick(deleteBtn, 10);
	        cp.waitAndClickWithJS(yesbutton, 10);
	        
	    } else {
	        throw new SkipException("Skipping test: Order status is not 'Outbound Pallet Pending'");
	    }
	}
	/**
	 * This method will return order status on listing page
	 * @return
	 */
	public String getOriginOrderStatusOnListing() {
		cp.waitForLoaderToDisappear();
		return cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[11]"));
	}
	
	/**
	 * This method will check if document numbers are present in the listing.
	 * It returns false if any document number is empty, otherwise true.
	 * 
	 * @return boolean - true if all document numbers are present, false otherwise
	 */
	public boolean checkDocNoPresent() {
		cp.waitForLoaderToDisappear();
		cp.moveToElementAndClick(By.xpath("//button[@title='Click to print']/ancestor::tr/td[3]"));
	    List<WebElement> docList = driver.findElements(
	        By.xpath("//button[@title='Click to print']/ancestor::tr/td[3]"));

	    for (WebElement cell : docList) {
	        String docNo = cell.getText().trim();
	        if (docNo.isEmpty()) {
	            return false;
	        }
	    }   
	    return !docList.isEmpty();
	}
	

	/**
	 * This method used to click on clear btn
	 */
	public void clickOnClearBtn() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		wt.waitToClick(clearBtn, 10);
		cp.waitForLoaderToDisappear();
	}
	
	/**
	 * Gets the origin LDA number (before the dash).
	 * @return origin LDA number
	 */
	public String getOriginLDACode() {
	    return cp.getMandatoryText(selectoriginLDA).split("-")[0].trim();
	}
	
	/**
	 * Selects a different LDA from the dropdown list (not equal to the given LDA).
	 * Uses a while loop to check each element until a match is found.
	 * 
	 * @param LDA the LDA code to avoid selecting
	 */
	public void selectDifferentLDA(String LDA) {
	    wt.waitToClick(selectLDA, 10);

	    List<WebElement> ldaList = driver.findElements(By.xpath("//*[@class='p-dropdown-item-label']"));

	    int i = 0;
	    while (i < ldaList.size()) {
	        WebElement cell = ldaList.get(i);
	        String ldaCode = cell.getText().split("-")[0].trim();

	        if (!LDA.equals(ldaCode)) {
	            cell.click();
	            cp.waitForLoaderToDisappear();
	            return;
	        }
	        i++;
	    }
	    throw new RuntimeException("No different LDA found to select!");
	}
	
	/**
	 * This method will return true if the "Save and Confirm" button is disabled.
	 * @return true if disabled, false otherwise
	 */
	public boolean isSaveAndConfirmBtnDisableOnLine() {
		WebElement confirmbtn = driver.findElement(confirmLine);
		  WebElement savebtn = driver.findElement(By.id("closeOutlineSaveButton"));
		return !confirmbtn.isEnabled() && !savebtn.isEnabled();
	}
	
	 /**
     * This methods is used to get Order data
     * @throws InterruptedException
     */
	public String MGLOrderno;
	public String PickupConsignee;
	public String MLDACode;
	public String Noofpieces;
	public String Actualwt;
	public String Litnoofpieces;
	public String Litactualwt;
	
	public void getOrderData() throws InterruptedException {
		try {
			cp.waitForLoaderToDisappear();
			MGLOrderno=driver.findElement(getorgorder).getAttribute("value");
	        logger.info("GL order no--MGLOrderno-- " + MGLOrderno);
	        
	        PickupConsignee = cp.getAttributeValue(getpickconsingee, "value");
			logger.info("Pick up consingee-- " + PickupConsignee);
	               
			MCusorderNo = cp.getAttributeValue(getcustomerordno, "value");
			logger.info(" CusorderNo-- " + MCusorderNo);
			MLDACode = getOriginLDACode();
			logger.info(" LDA-- " + MLDACode);
			Noofpieces = cp.getAttributeValue(getnoofpieces, "value");
			logger.info(" Noofpieces-- " + Noofpieces);	
			Actualwt = cp.getAttributeValue(getactualwt, "value");
			logger.info(" Actualwt-- " + Actualwt);	
			Litnoofpieces = cp.getAttributeValue(getlitnoofoieces, "value");
			logger.info(" Litnoofpieces-- " + Litnoofpieces);
			Litactualwt = cp.getAttributeValue(getlitactualwt, "value");
			logger.info(" Litactualwt-- " + Litactualwt);
		} catch (Exception e) {
			logger.info("Some error while get order details.");
		}
	}
	
	/**
	 * IN this method we select first 3 check boxex on listing
	 */
	public void selectMultiOrderOnListing() {
	    List<WebElement> checkBoxes = driver.findElements(firstcheckBox);
	    for (int i = 0; i < 3 && i < checkBoxes.size(); i++) {
	        checkBoxes.get(i).click();
	    }
	}
	
	/**
	 * This method is used to get Order Number from the listing page.
	 * @return Order Number
	 */
	public String getOrderNo() {
		return cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[3]"));
	}
	
	/**
	 * This method is used to get Customer Order Number from the listing page.
	 * @return Customer Order Number
	 */
	public String getCustomerOrderNo() {
		return cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[5]"));
	}
	
	/**
	 * This method is used to get Pickup Consignee from the listing page.
	 * @return Pickup Consignee
	 */
	public String getPickupCon() {
		return cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[6]"));
	}
	
	/**
	 * This method is used to get carrier code from the listing page.
	 * @return carrier code
	 */
	public String getCarrierCode() {
		return cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[10]"));
	}
	
	/**
	 * This methods is used to get actual wt from listing page
	 * @return
	 */
	public String getActulwt() {
		return cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[8]"));
	}
	
	/**
	 * This methods is used to get actual pieces from listing page	
	 * @return
	 */
	public String getActualpieces() {
		String value = cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[7]"));
		return value.split("/")[1].trim();
	}
	
	/**
	 * Deletes an order only if its status is "closed".
	 * If the status does not match, the test case will be skipped.
	 */
	public void deleteCloseOrder() {
		cp.waitForPopupToDisappear();
	    String status = cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[11]"));
	    if (status.equals("Closed")) {
	        wt.waitToClick(By.xpath("//*[@title='Click to delete']"), 10);
	        cp.waitAndClickWithJS(yesbutton, 10);
	        
	    } else {
	        throw new SkipException("Skipping test: Order status is not 'closed'");
	    }
	}
	
	
	/**
	 * This method used to click on expand btn
	 */
	public void clickOnExpandBtn() {
		wt.waitToClick(expandBtn, 10);
	}
	
	/**
	 * This method is used to get closeout line Customer Order Number from the listing page.
	 * @return Customer Order Number 
	 */
	public String getLineCustOrderNo() {
		return cp.getMandatoryText(By.xpath("(//table/tbody/tr[1]/td[4])[2]"));
	}
	
	/**
	 * This methods is used to create order and selecting existing customer
	 * @param ordernumber
	 * @throws InterruptedException
	 */
	public void orderCreationforDuplicateCustomer(String ordernumber,String customer) throws InterruptedException {
		cp.waitForLoaderToDisappear();
		AddNewOrderBtn();
		cp.waitForLoaderToDisappear();
		//selectAnyWarehouseOrSkip();
		driver.findElement(searchcustomer).click();
		WebElement search = driver.findElement(input);
		System.out.println("Customer name "+ customer);
		cp.selectSearchableDropdown(search, customer);
		selectAnyConsigneeOrSkip();
		enterCustOrdNum(ordernumber);
		selectFirstOrderType();
	}
	public String getCustomer() {
		return cp.getMandatoryText(By.xpath("//tbody//tr//td[4]"));
	}
	
	/**
	 * This method is used to Select multiple consignees
	 * 
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleConsigneesByIndex(List<Integer> indices) throws InterruptedException {
		cp.waitForLoaderToDisappear();
		driver.findElement(pickupConsignee).click();
		for (Integer index : indices) {
			try {
				String consigneeXPath = "(//li[contains(@class, 'p-multiselect-item')])[" + index + "]";
				WebElement consigneeElement = driver.findElement(By.xpath(consigneeXPath));
				consigneeElement.click();
			} catch (Exception e) {
				logger.severe("Could not select consignee at index: " + index + ", Error: " + e.getMessage());
			}
		}
	}
	
	/**
	 * This method is used to Select multiple LDA
	 * 
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleLDAByIndex(List<Integer> indices) throws InterruptedException {
		cp.waitForLoaderToDisappear();
		cp.selectMultipleByIndex(selectpickupLda, indices, "LDA");
	}
	
	/** 
	 * This methods is used to Search and validate order number 
	 * @throws InterruptedException
	 */
	public void searchAndValidateOrderNumber() throws InterruptedException {
		cp.clickClearButton();
		cp.searchAndValidate(fetchCloseoutOrderTextLocator, ordernum, "CloseOut Order Number");
		cp.waitForPopupToDisappear();
	}

	/** 
	 * This methods is used to Search and validate customer order number 
	 * @throws InterruptedException
	 */
	public void searchAndValidateCustomerOrderNumber() throws InterruptedException {
		cp.searchAndValidateCustomerOrderNumber(fetchcustordertext);
		cp.waitForPopupToDisappear();
	}
	
	/** 
	 * This methods is used to Search and validate order number 
	 * @throws InterruptedException
	 */
	public void searchAndValidatePickupLDACode() throws InterruptedException {
		cp.clickClearButton();
		cp.searchAndValidate(fetchpickupldacode, pickupLdaCode, "Pickup LDA code");
		cp.waitForPopupToDisappear();
	}
	 
}
