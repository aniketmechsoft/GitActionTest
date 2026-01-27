package com.uiFramework.Genesis.web.pages;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.WaitHelper;

import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;

public class productionPage {
	WebDriver driver = null;
	CommonMethods cm;
	CommonPage cp = null;
	WaitHelper wt;
	
	public productionPage(WebDriver driver) {
		this.driver = driver;
		this.cm = new CommonMethods(driver);
		this.cp = new CommonPage(driver);
		this.wt = new WaitHelper(driver);
	}
	
	private By createBtn = By.xpath("//button[contains(., 'Create')]");
	private By confirm = By.id("ConfirmTenderButton");
	private By isrequired = By.xpath("//*[contains(text(), 'is required')]");
	private By warehouse = By.xpath("(//*[.='Select Warehouse'])[4]");
	private By customer = By.xpath("(//*[.='Select Customer'])[4]");
	private By distributor = By.xpath("(//*[.='Select Distributor'])[4]");
	private By consignee = By.xpath("//*[@placeholder='Select Consignee']");
	private By singleConsignee = By.xpath("(//*[.='Select Consignee'])[4]");
	private By localcarrier = By.xpath("(//*[.='Select Delivering Local Carrier'])[4]");
	private By picklocation = By.xpath("(//*[.='Select Pickup Location'])[4]");
	private By droplocation = By.xpath("(//*[.='Select Drop Location'])[4]");
	private By ordertype = By.xpath("(//*[.='Select Order Type'])[4]");
	private By dropdownList = By
			.xpath("//div[contains(@class,'p-dropdown-items-wrapper')]//ul[contains(@class,'p-dropdown-items')]/li");
	private By input = By.xpath("//input[@class='p-dropdown-filter p-inputtext p-component']");
	private By clist = By.xpath("//ul[@class='p-autocomplete-items']//li");
	private By clearicon = By.xpath("(//*[@data-pc-section='clearicon'])[2]");
	private By ordersource = By.xpath("//div[@id='orderSource']");
	private By addnote = By.xpath("//*[@placeholder='Enter Add Note']");
	private By state = By.xpath("(//*[.='Select State'])[4]");
	private By multiSelDropDownList = By.xpath("//ul[contains(@class,'p-multiselect-items')]//li");
	private By selectstatusippage = By.xpath("(//*[.='2 items selected'])[3]");
	private By picklocationObt = By.xpath("//*[@aria-label='Select Pickup Location']");
	private By palletFrom = By.xpath("//*[@class='p-dropdown-label p-inputtext'][contains(., 'WAREHOUSE')]");
	private By selectLDAFromList = By.xpath("//*[@class='p-dropdown-item-label'][contains(text(),'LDA')]");
	private By LTLtext = By.xpath("//span[contains(text(),'Select LTL')]");
	private By selectLDA = By.xpath("(//*[.='Select LDA'])[4]");
	private By thirdpaty = By.xpath("//span[contains(text(),'Select 3rd Party Billing')]");
	private By truckname = By.xpath("//*[@placeholder='Enter Truck Name']");
	private By description = By.xpath("//*[@aria-label='Select Description']");
	private By dropfor = By.xpath("//*[@class='p-dropdown-label p-inputtext'][contains(text(),'LDA')]");
	private By selectOriginFromList = By.xpath("//*[@class='p-dropdown-item-label'][contains(text(),'Origin')]");
	private By selectedWarehouse = By.xpath("//*[@aria-label='Select Warehouse']");
	private By docStatus = By.xpath("//*[@aria-label='Select Document Status']");
	private By csr = By.xpath("(//*[.='Select Customer Service Rep'])[4]");
	private By selectfirstElement = By.xpath("(//div[contains(@class,'p-dropdown-items-wrapper')]//ul[contains(@class,'p-dropdown-items')]/li)[1]");
	private By manifest = By.xpath("//*[@placeholder='Select Manifest']");
	private By lbol = By.xpath("//*[@placeholder='Select LBOL']");
	private By pendingStatus = By.xpath("(//*[.='Pending'])[4]");
	private By addOrdBtn = By.xpath("//button[contains(., 'Add New')]");
	private By newlda = By.xpath("(//*[.='Select New Local Carrier'])[4]");
	private By orglocalcarrier = By.xpath("(//*[.='Select Original Local Carrier'])[4]");
	private By closeouttype = By.xpath("(//*[.='Select Closeout Type'])[4]");
	private By pickupconsignee = By.xpath("//*[@placeholder='Select Pickup Consignee']");
	private By auditorname = By.xpath("//*[@placeholder='Enter Auditor Name']");
	private By refreshBtn = By.xpath("//*[@title='Click to Refresh']");
	
	/**
	 * Returns the count of mandatory field validation messages.
	 * @return number of mandatory field errors
	 */
	public int getMandFieldCount() {
		System.out.println("Verifying required Field On Save button");
		return driver.findElements(isrequired).size();
	}
	
	/**
	 * THis method used to click on Warehouse field
	 */
	public void clickWarehouse() {
		wt.waitToClickWithAction(warehouse, 5);
	}
	
	/**
	 * This method used to check drop down is empty or not
	 * @param name
	 * @return
	 */
	public boolean checkDropDownValue(String name) {
		
	    try {
	        List<WebElement> list = driver.findElements(dropdownList);

	        if (list != null && !list.isEmpty()) {
	            System.out.println(""+ name + " Dropdown has " + list.size() + " values.");
	            return true;
	        } else {
	            System.out.println(""+ name + " Dropdown is empty.");
	            return false;
	        }

	    } catch (Exception e) {
	        System.out.println("Error while checking dropdown: " + e.getMessage());
	        return false;
	    }
	}
	/**
	 * This method used to check multi selection drop down list is null or not
	 * @param name
	 * @return
	 */
	public boolean checkDropDownValueForMultiSel(String name) {
	    try {
	        List<WebElement> list = driver.findElements(multiSelDropDownList);

	        if (list != null && !list.isEmpty()) {
	            System.out.println(""+ name + " Dropdown has " + list.size() + " values.");
	            return true;
	        } else {
	            System.out.println(""+ name + " Dropdown is empty.");
	            return false;
	        }
	    } catch (Exception e) {
	        System.out.println("Error while checking dropdown: " + e.getMessage());
	        return false;
	    }
	}
	
	/**
	 * This method used to check consignees drop down list is null or not
	 * @param name
	 * @return
	 */
	public boolean checkDropDownValueForDependsDrpdwn(String name) {
	    try {
	        List<WebElement> list = driver.findElements(clist);

	        if (list != null && !list.isEmpty()) {
	            System.out.println(""+ name +" Dropdown has " + list.size() + " values.");
	            return true;
	        } else {
	            System.out.println(""+ name +" Dropdown is empty.");
	            return false;
	        }

	    } catch (Exception e) {
	        System.out.println("Error while checking dropdown: " + e.getMessage());
	        return false;
	    }
	}
	
	/**
	 * This method used to click on customer drop down
	 */
	public void clickCustomerDD() {
		wt.waitToClickWithAction(customer, 5);
	}
	
	/**
	 * This method used to click carrier drop down and if add note available then click 
	 */
	public void clickLocalCarrier() {
		try {
			driver.findElement(addnote).click();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		wt.waitToClickWithAction(localcarrier, 5);
	}
	
	/**
	 * This method used to click on consignee drop down
	 * @throws InterruptedException
	 */
	public void clickConsignee() throws InterruptedException {
		Thread.sleep(2000);
		wt.waitToClickWithAction(consignee, 5);
	}
	
	/**
	 * This method used to clear pick location and then click on pickup drop down
	 */
	public void clickPickup() {
		try {
		driver.findElement(clearicon).click();
		Thread.sleep(800);
		} catch (Exception e) {
			// TODO: handle exception
		}
		wt.waitToClickWithAction(picklocation, 5);
	}
	
	/**
	 * This method used to click on drop location
	 * @throws InterruptedException
	 */
	public void clickDroplocation() throws InterruptedException {
		Thread.sleep(2500);
		wt.waitToClickWithAction(droplocation, 5);
	}
	
	/**
	 * This method used to enter value and select that value
	 * @param data
	 * @throws InterruptedException
	 */
	public void selectEnterValue(String data) throws InterruptedException {
		Thread.sleep(2500);
		cp.DrpDwnValueSelArrowDw(input, data);
		cp.waitForLoaderToDisappear();
		Thread.sleep(500);
	}
	
	/**
	 * This method used to click on order source and send keys
	 * @param data
	 */
	public void selectOrderSource(String data) {
		wt.waitToClickWithAction(ordersource, 5);
	//	WebElement search = driver.findElement(input);
		cp.DrpDwnValueSelArrowDw(input, data);
		cp.waitForLoaderToDisappear();
	}
	
	/**
	 * This method used to click on distributor
	 */
	public void clickDistibutor() {
		wt.waitToClickWithAction(distributor, 5);
	}
	
	/**
	 * This method used to click on oder type 
	 */
	public void clickOrdertype() {
		try {
			driver.findElement(addnote).click();
		} catch (Exception e) {
			// TODO: handle exception
		}
		wt.waitToClickWithAction(ordertype, 5);
	}
	
	/**
	 * This method used to click on state drop down
	 */
	public void clickState() {
		try {
			driver.findElement(addnote).click();
		} catch (Exception e) {
			// TODO: handle exception
		}
		wt.waitToClickWithAction(state, 5);
	}

	/**
	 * Scrolls through and validates grid data dynamically based on the grid type.
	 * Works for both frozen and scrollable panes.
	 *
	 * @param classname Either "frozen-pane" or "scrollable-pane" — determines which grid section to operate on.
	 * @throws InterruptedException if thread sleep is interrupted.
	 */
	//frozen-pane
	//scrollable-pane
//	public void verifyColumnFilter1(String classname) throws InterruptedException {
//	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//	    
//	    List<WebElement> gridCells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
//	            By.xpath("//*[@class='" + classname + "']//tbody//tr[2]//td")
//	        ));
//	    
//	    int columnCount = gridCells.size();
//
//	    for (int i = 1; i <= columnCount; i++) {
//
//	        String cellXPath = "//*[@class='" + classname + "']//tbody//tr[2]//td[" + i + "]";
//	        WebElement cell = gridCells.get(i - 1);
//	        Thread.sleep(500);
//	        String text = cell.getText().trim();
//
//	        if (text.isEmpty()) continue;
//
//	        System.out.println("Cell " + i + " value: " + text);
//
//	        String filterXPath = "//*[@class='" + classname + "']//thead//tr[2]//th[" + i + "]//input";
//	        List<WebElement> filters = driver.findElements(By.xpath(filterXPath));
//
//	        if (filters.isEmpty() || !filters.get(0).isDisplayed()) {
//	            System.out.println("No visible filter for column " + i);
//	            continue;
//	        }
//
//	        WebElement filter = filters.get(0);
//	        filter.clear();
//	        
//	        
//	        //Check column label is LDA then Spite text
//	        if ("LDA".equals(driver.findElement(By.xpath(
//	    	        "//*[@class='scrollable-pane']//thead//tr[1]//th[" + i + "]//*[@class='p-column-title']"))
//	    	        .getText().trim())) {
//	    	        text = text.contains("-") ? text.split("-")[0].trim() : text.trim();
//	    	        
//	    	        if (!text.matches("\\d+")) continue; //only get numeric value if not then skip filter
//	    	    }
//	        
//	      //Check column label is QA Status then Spite text
//	        if ("QA Status".equals(driver.findElement(By.xpath(
//	    	        "//*[@class='scrollable-pane']//thead//tr[1]//th[" + i + "]//*[@class='p-column-title']"))
//	    	        .getText().trim())) {
//	    	        text = text.split("\n")[0].trim();
//	 
//	    	    }
//	        
//	        System.out.println("textis "+text);
//
//	        filter.sendKeys(text);
//	        Thread.sleep(2000) ;
//	        cp.waitForLoaderToDisappear();
//
//	        String filteredText = driver.findElement(By.xpath(cellXPath)).getText().trim();
//	        System.out.println("After filter, cell[" + i + "] text: " + filteredText);
//
//	        Assert.assertTrue(filteredText.contains(text),
//	                "Column filter not matching — expected to contain: " + text + " but got: " + filteredText);
//	        Thread.sleep(1000);
//	    }
//	}
	
	/**
	 * Scrolls through and validates grid data dynamically based on the grid type.
	 * Works for both frozen and scrollable panes.
	 *
	 * @param classname Either "frozen-pane" or "scrollable-pane" — determines which grid section to operate on.
	 * @throws InterruptedException if thread sleep is interrupted.
	 */
	//frozen-pane
	//scrollable-pane
	public void verifyColumnFilter(String classname) throws InterruptedException {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    List<WebElement> initialCells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
	            By.xpath("//*[@class='" + classname + "']//tbody//tr[2]//td")));

	    int columnCount = initialCells.size();

	    for (int i = 1; i <= columnCount; i++) {
	    	
	        String cellXPath = "//*[@class='" + classname + "']//tbody//tr[2]//td[" + i + "]";
	        WebElement cell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cellXPath)));

	        String text = cell.getText().trim();
	        Thread.sleep(300);

	        if (text.isEmpty()) continue;

	        System.out.println("Cell " + i + " value: " + text);

	        String filterXPath = "//*[@class='" + classname + "']//thead//tr[2]//th[" + i + "]//input";
	        List<WebElement> filters = driver.findElements(By.xpath(filterXPath));

	        if (filters.isEmpty() || !filters.get(0).isDisplayed()) {
	            System.out.println("No visible filter for column " + i);
	            continue;
	        }

	        WebElement filter = driver.findElement(By.xpath(filterXPath));
	        filter.clear();

	        // Get header title fresh
	        String headerTitle = driver.findElement(
	        	    By.xpath("//*[@class='scrollable-pane']//thead//tr[1]//th[" + i + "]//*[@class='p-column-title']")
	        	).getText().trim();

	        // Special case: LDA column
	        if ("LDA".equals(headerTitle)) {
	            text = text.contains("-") ? text.split("-")[0].trim() : text.trim();
	            if (!text.matches("\\d+")) continue; // Skip non-numeric
	        }

	        // Special case: QA Status column
	        if ("QA Status".equals(headerTitle)) {
	            text = text.split("\n")[0].trim();
	        }

	        System.out.println("Filtering with: " + text);

	        filter.sendKeys(text);
	        Thread.sleep(1500);
	        cp.waitForLoaderToDisappear();

	        // Re-locate the cell after filtering (fresh lookup)
	        WebElement filteredCell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cellXPath)));
	        String filteredText = filteredCell.getText().trim();

	        System.out.println("After filter, cell[" + i + "] text: " + filteredText);

	        Assert.assertTrue(filteredText.contains(text),
	                "Column filter not matching — expected to contain: " + text + " but got: " + filteredText);
	    }
	}

	/**
	 * Scrolls through and validates grid data dynamically based on the grid type.
	 * Works for fix grid pattern
	 *
	 * @throws InterruptedException if thread sleep is interrupted.
	 */
	//This method for Remaining for optimization pages
	public void verifyColumnFilterForFixGrid() throws InterruptedException {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    
	    List<WebElement> gridCells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
	            By.xpath("//*[@id='panel1bh-content']//tbody//tr[1]//td")
	        ));
	    
	    int columnCount = gridCells.size();

	    for (int i = 1; i <= columnCount; i++) {

	        // Always re-locate fresh to avoid stale
	        String cellXPath = "//*[@id='panel1bh-content']//tbody//tr[1]//td[" + i + "]";
	        WebElement cell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cellXPath)));

	        Thread.sleep(500);
	        String text = cell.getText().trim();

	        if (text.isEmpty()) continue;

	        System.out.println("Cell " + i + " value: " + text);

	        String filterXPath = "//*[@id='panel1bh-content']//thead//tr[2]//th[" + i + "]//input";
	        List<WebElement> filters = driver.findElements(By.xpath(filterXPath));

	        if (filters.isEmpty() || !filters.get(0).isDisplayed()) {
	            System.out.println("No visible filter for column " + i);
	            continue;
	        }

	        WebElement filter = filters.get(0);
	        filter.clear();
	        
	        String header = driver.findElement(By.xpath(
	                "//*[@id='panel1bh-content']//thead//tr[1]//th[" + i + "]//*[@class='p-column-title']")
	        ).getText().trim();
	        
	        if ("LDA".equals(header) || "Original LDA".equals(header) ||"New LDA".equals(header)) {

	            text = text.contains("-") ? text.split("-")[0].trim() : text.trim();
	            // only get numeric values; skip others
	            if (!text.matches("\\d+")) {
	                continue;
	            }
	        }
	        
	      //Check column label is QA Status then Spite text
	        if ("QA Status".equals(driver.findElement(By.xpath(
	    	        "//*[@id='panel1bh-content']//thead//tr[1]//th[" + i + "]//*[@class='p-column-title']"))
	    	        .getText().trim())) {
	    	        text = text.split("\n")[0].trim();
	 
	    	    }    
	        System.out.println("Filter apply "+text);

	        filter.sendKeys(text);
	        Thread.sleep(2000) ;
	        cp.waitForLoaderToDisappear();

	        String filteredText = driver.findElement(By.xpath(cellXPath)).getText().trim();
	        System.out.println("After filter, cell[" + i + "] text: " + filteredText);

	        Assert.assertTrue(filteredText.contains(text),
	                "Column filter not matching — expected to contain: " + text + " but got: " + filteredText);
	    }
	}
	
	/**
	 * Checks whether the column at the specified index has the header name "LDA".
	 * If the header matches "LDA", this method processes the given text value by:
	 * - Removing any substring that comes after a hyphen ("-"), if present.
	 * - Trimming extra spaces.
	 *
	 * @param i     The column index to check in the table header (1-based index).
	 * @param text  The text value to be validated or modified based on the column name.
	 * @return      The processed text if the column name is "LDA"; otherwise, returns the original text.
	 */
//	public String checkColumnName(int i, String text) {
//	    if ("LDA".equals(driver.findElement(By.xpath(
//	        "//*[@class='scrollable-pane']//thead//tr[1]//th[" + i + "]//*[@class='p-column-title']"))
//	        .getText().trim())) {
//	        text = text.contains("-") ? text.split("-")[0].trim() : text.trim();
//	        
//	        if (!text.matches("\\d+")) {
//	        	continue;    // not numeric → skip
//	        } else {
//	            return text;   // numeric → return
//	        }
//	        
//	    }
//	    return text;
//	}
	
	/**
	 * This method used to click on create btn
	 * @throws InterruptedException
	 */
	public void clickTocreate() throws InterruptedException {
		cp.waitForPopupToDisappear();
		cp.waitForLoaderToDisappear();
		wt.waitToClick(createBtn, 10);
	}
	
	/**
	 * This method used to click check in status drop down on inbound pallet page
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultiplestatusByIndex(List<Integer> indices) throws InterruptedException {
	    cp.waitForLoaderToDisappear();
	    cp.selectMultipleByIndex(selectstatusippage, indices, "Status");
	}
	
	/**
	 * This method used to click on pickup location
	 */
	public void clickOnPickupLocation() {
		wt.waitToClickWithAction(picklocationObt, 5);
	}
	/**
	 * This method used to select 'LDA'  name drop down from list
	 */
	public void palletFromLDA() {
		wt.waitToClick(palletFrom, 5);
		wt.waitToClick(selectLDAFromList, 5);
	}
	
	/**
	 * This method used to click on LTL drop down
	 */
	public void clickOnLTL() {
		wt.waitToClickWithAction(LTLtext, 5);
	}
	/**
	 * This method used to LDA drop down
	 */
	public void clickOnLDA() {
		wt.waitToClickWithAction(selectLDA, 5);
	}
	
	/**
	 * This method used to select first LDA(element) from list
	 */
	public void selectFirstLDA() {
		wt.waitToClick(selectfirstElement, 10);
		cp.waitForLoaderToDisappear();
	}
	/**
	 * This method used to click on truck name then click on third party
	 */
	public void clickOn3rdPartyBilling() {
		try {
			wt.waitToClick(truckname, 10);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		wt.waitToClickWithAction(thirdpaty, 5);
	}
	/**
	 * This method used to click on description drop down
	 */
	public void clickOnDescription() {
		try {
			wt.waitToClick(truckname, 10);
		} catch (Exception e) {
			// TODO: handle exception
		}
		wt.waitToClickWithAction(description, 5);
	}
	
	/**
	 * This method used to select 'origin' from drop down
	 * @throws InterruptedException
	 */
	public void dropforOrigin() throws InterruptedException {
		wt.waitToClick(dropfor, 5);
		wt.waitToClick(selectOriginFromList, 5);
		Thread.sleep(500);
	}
	/**
	 * This method used to select warehouse from list
	 */
	public void clickSelWarehouse() {
		wt.waitToClickWithAction(selectedWarehouse, 5);
	}
	
	/**
	 * This method used to click on doc status
	 */
	public void clickDocumentStatus() {
		wt.waitToClickWithAction(docStatus, 5);
	}
	/**
	 * This method used to click on CSR drop down
	 */
	public void clickCustomerServiceRep() {
		wt.waitToClickWithAction(csr, 5);
	}
	
	/**
	 * This method used to click on Manifest drop down
	 */
	public void clickOnManifest() {
		wt.waitToClick(manifest, 5);
	}
	
	/**
	 * This method used to click on status drop down
	 */
	public void clickOnStatus() {
		wt.waitToClick(selectstatusippage, 5);
	}
	
	/**
	 * This method used to click on LBOL drop down
	 */
	public void clickOnLBOL() {
		wt.waitToClick(lbol, 5);
	}
	
	/**
	 * This method used to click on status those are default selected as 'pending'
	 */
	public void clickOnPendingStatus() {
		wt.waitToClick(pendingStatus, 5);
	}
	
	/**
	 * This method used to click On drop down where uaer can select single consignee
	 * @throws InterruptedException
	 */
	public void clicksingleConDrpdwn() throws InterruptedException {
		Thread.sleep(2000);
		wt.waitToClick(singleConsignee, 5);
	}
	
	/**
	 * This method used to click on add order button
	 * @throws InterruptedException
	 */
	public void clickToAddOrd() throws InterruptedException {
		cp.waitForPopupToDisappear();
		cp.waitForLoaderToDisappear();
		wt.waitToClick(addOrdBtn, 10);
	}
	
	/**
	 * This method used to click on new LDA drop down
	 */
	public void clickNewLDA() {
		wt.waitToClick(newlda, 5);
	}
	
	/**
	 * This method used to click on pickup consignee drop down
	 */
	public void clickPickConsignee() {
		wt.waitToClick(pickupconsignee, 5);
	}
	
	/**
	 * This method used to click on origin local carrier drop down
	 */
	public void clickOrgLocalcarrier() {
		try {
			wt.waitToClick(auditorname, 2);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		wt.waitToClickWithAction(orglocalcarrier, 5);
	}
	/**
	 * This method used to click on closeout type drop down
	 */
	public void clickCloseoutType() {
		try {
			wt.waitToClick(auditorname, 2);
		} catch (Exception e) {
			// TODO: handle exception
		}	
		wt.waitToClickWithAction(closeouttype, 5);
	}
	
	/**
	 * This method use to click on refresh button to clear all applied filter
	 */
	public void clickToRefresh() {
		wt.waitToClick(refreshBtn, 10);
		cp.waitForLoaderToDisappear();
	}

}
