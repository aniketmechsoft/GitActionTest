package com.uiFramework.Genesis.LDAPages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.WaitHelper;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.OrderPage;

public class DriverPage extends LdaQAPage {

	WebDriver driver;
	OrderPage op = null;
	CommonPage cp;
	CommonMethods cm = null;
	WaitHelper wt;

	public DriverPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		this.cp = new CommonPage(driver);
		cm = new CommonMethods(driver);
		this.wt = new WaitHelper(driver);
		op = new OrderPage(driver);

	}

	private By selectdrivermenu = By.xpath("//i[@title='Drivers']");
	private By selectlbolmenu = By.xpath("//i[@title='LBOL-Driver Assignment']");
	private By addriver = By.id("btnAddDriver");
	private By enterusername = By.xpath("//*[@placeholder='User Name']");
	private By entername = By.xpath("//*[@placeholder='Enter Name']");
	private By entermobilenumber = By.xpath("//*[@placeholder='Enter Mobile Number']");
	private By enteremailid = By.xpath("//*[@placeholder='Enter Email Id']");
	private By enteraddress1 = By.xpath("//*[@placeholder='Enter Address 1']");
	private By selectstate = By.xpath("//*[@class='p-dropdown-label p-inputtext p-placeholder']");
	private By selectcity = By.xpath("//*[@placeholder='Select City']");
	private By enterzipcode = By.xpath("//*[@placeholder='Enter Zip Code']");
	private By enteremployeeid = By.xpath("//*[@placeholder='Enter Employee Id']");
	private By enteraddress2 = By.xpath("//*[@placeholder='Enter Address 2']");
	private By enteraddress3 = By.xpath("//*[@placeholder='Enter Address 3']");
	private By removebtn = By.xpath("//*[@title='Click to remove']");
	private By resetpassword = By.xpath("//*[@title='Click to reset password']");
	//private By Yes = By.xpath("//button[contains(., 'Yes') and contains(@class, 'cnfmBTn')]");
	private By password = By.xpath("//*[@placeholder='Password']");
	private By resetbtnlist = By.xpath("//*[@data-testid='LockResetIcon']");
	private By clickdriverdrpdwn = By.xpath("//*[@aria-label='Select Driver Name']");
	//LBOL assign page
	private By assigndrvbtn = By.xpath("//*[@title='Click to assign driver']");
	private By assignstatus = By.xpath("(//*[@class='p-dropdown-item-label'])[2]");
	private By enterlbol = By.xpath("//*[@placeholder='Enter LBOL No']");
	
	/**
	 * Clicks on driver menu 
	 */
	public void clickDriverMenu() {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(selectdrivermenu, 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * This method user to return username from page
	 * @return
	 */
	public String getUsername() {
		return cp.getMandatoryText(By.xpath("//tr//td[3]"));
	}

	/**
	 * This method used to click on add driver btn
	 */
	public void clickAddDriver() {
		wt.waitToClick(addriver, 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Returns the count of mandatory field validation messages.
	 * 
	 * @return number of mandatory field errors
	 * @throws InterruptedException 
	 */
	public int getMandFieldCount() throws InterruptedException {
		Thread.sleep(2000);
		return driver.findElements(By.xpath("//*[contains(text(), 'is required')]")).size();
	}

	/**
	 * This method used to enter usernmae and if name blank then skip method
	 * @param username
	 */
	public void enterUsername(String username) {
		if (username == null) {
			throw new SkipException("Skipping test: username is null or empty");
		}
		cp.clickAndSendKeys(enterusername, username);
	}
	/**
	 * This mthod used to enter driver name
	 * @param name
	 */
	private void enterName(String name) {
		cp.clickAndSendKeys(entername, name);
	}
	/**
	 * This method used to enter mobile number
	 * @param no
	 */
	private void enterMobileNo(String no) {
		cp.clickAndSendKeys(entermobilenumber, no);
	}
	/**
	 * This method used to enter email id
	 * @param id
	 */
	private void enterEmailId(String id) {
		cp.clickAndSendKeys(enteremailid, id);
		;
	}

	/**
	 * This method used to enetr address 1
	 * @param add
	 */
	private void enterAddress1(String add) {
		cp.clickAndSendKeys(enteraddress1, add);
	}

	/**
	 * This method used to enter zip code
	 * @param zip
	 */
	private void enterZipcode(String zip) {
		cp.clickAndSendKeys(enterzipcode, zip);
	}

	/**
	 * This method used to elect state
	 */
	private void selectState() {
		wt.waitToClick(selectstate, 10);
		wt.waitToClick(By.xpath("(//*[@class='p-dropdown-item'])[2]"), 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * This method used to select city
	 * @param city
	 */
	private void selectCity(String city) {
		cp.clickAndSendKeys(selectcity, city);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * This method used to enter all mandatory filed for req. driver create
	 * @param city
	 */
	public void enterMandatoryFields(String city) {
		enterName(cm.getcharacterString(7));
		enterMobileNo(cm.getNumericString(10));
		enterEmailId(cm.getAlphaNumericString(5)+"@gmail.com");
		enterAddress1(cm.getAlphaNumericString(20));
		selectState();
		selectCity(city);
		enterZipcode(cm.getNumericString(5));

	}

	/**
	 * THis method is used  enter non mandatory field
	 */
	public void enterNonMandateFiled() {
		try {
			cp.clickAndSendKeys(enteremployeeid, cm.getAlphaNumericString(8));
			cp.clickAndSendKeys(enteraddress2, cm.getAlphaNumericString(12));
			cp.clickAndSendKeys(enteraddress3, cm.getAlphaNumericString(9));

		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	/**
	 * This method used to upload driver photo
	 */
	public void uploadDriverPhoto() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		By fileInputLocator = By.xpath("//input[@type='file']");
		WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(fileInputLocator));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fileInput);

		String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\attachments\\DriverAuto.jpg";
		fileInput.sendKeys(filePath);
	}
	
	/**
	 * This method return true in removed button displayed
	 * @return
	 */
	public boolean isRemoveButtonDisplayed() {
		cp.waitForLoaderToDisappear();
		List<WebElement> removeBtn = driver.findElements(removebtn);
		return !removeBtn.isEmpty() && removeBtn.get(0).isDisplayed();
	}
	
	/**
	 * This method used to click on reset password btn
	 */
	public void clickOnResetBtn() {
		cp.waitForPopupToDisappear();
		wt.waitToClick(resetpassword, 10);
		cp.waitAndClickWithJS(Yes, 10);
	}
	
	/**
	 * This method used to get password from password field
	 * @return
	 */
	public String getPassword() {
		cp.waitForPopupToDisappear();
		return cp.getAttributeValue(password, "value");
	}

	/**
	 * This method used to click on reset btn from Listing page
	 */
	public void clickOnResetBtnOnListing() {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(resetbtnlist, 10);
		cp.waitAndClickWithJS(Yes, 10);
	}
	
	/**
	 * This method used to click on removed btn
	 * @throws InterruptedException 
	 */
	public void clickOnRemovedBtn() throws InterruptedException {
		wt.waitToClick(removebtn, 10);
		Thread.sleep(2000);
	}
	/**
	 * This method used to click LBOL-DriverAssignment menu
	 */
	public void LbolAssignmentMenu() {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(selectlbolmenu, 10);
		cp.waitForLoaderToDisappear();
	}
	
	/**
	 * This method used to click driver drop down
	 */
	public void selectDriverDrpDwn() {
		wt.waitToClick(clickdriverdrpdwn, 10);
	}
	
	/**
	 * This method used to get size of driver list
	 * @return
	 */
	public int getDriver() {
	    return driver.findElements(By.xpath("//*[@class='p-dropdown-item']")).size();
	}
	
	/**
	 * This method used to select driver from drop down list
	 */
	public void selectDriver() {
		wt.waitToClick(By.xpath("(//*[@class='p-dropdown-item'])[1]"), 10);
	}
	
	/**
	 * This method used to get LBOL
	 * @return
	 */
	public String getLBOL() {
		return cp.getMandatoryText(By.xpath("//tbody/tr/td[1]"));
	}
	
	/**
	 * This method used to click assigned driver button
	 */
	public void clickToAssignDriver() {
		wt.waitToClickWithAction(assigndrvbtn, 10);
	}
	
	/**
	 * This method used select assign status
	 */
	public void selectAssignStatus() {
		cp.waitForPopupToDisappear();
		wt.waitToClick(selectstatus, 10);
		wt.waitToClick(assignstatus, 10);	
	}
	/**
	 * This method used to enter LBOL
	 * @param lbol
	 */
	
	public void enterlbol(String lbol) {
		cp.clickAndSendKeys(enterlbol, lbol);
	}
	
	/**
	 * This method used to assign multiple driver for LBOL
	 */
	public void assigneDriverForMultiLBOL() {
		cp.waitForLoaderToDisappear();
		List<WebElement> row=driver.findElements(By.xpath("//tbody//tr"));
		int loopCount = (row.size() > 3) ? 2 : row.size();
		
		for(int i=1; i<=loopCount; i++) {	
			wt.waitToClick(By.xpath("(//*[@aria-label='Select Driver Name'])["+ i +"]"), 10);
			wt.waitToClick(By.xpath("(//*[@class='p-dropdown-item'])[1]"), 10);
		}
	}
   private By drvstatus=By.xpath("//span[text()='Active']");
   private By inactive=By.xpath("//*[text()='Inactive']");
   
	public void inactiveDriver() {
		wt.waitToClick(drvstatus, 10);
		wt.waitToClick(inactive, 10);
		
	}
	
}
