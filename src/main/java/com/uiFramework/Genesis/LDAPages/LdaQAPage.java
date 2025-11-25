package com.uiFramework.Genesis.LDAPages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.WaitHelper;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.NavigationPages;
import com.uiFramework.Genesis.web.pages.OrderPage;

public class LdaQAPage extends NavigationPages {
	WebDriver driver;
	OrderPage op = null;
	CommonPage cp;
	CommonMethods cm = null;
	WaitHelper wt;

	public LdaQAPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		this.cp = new CommonPage(driver);
		cm = new CommonMethods(driver);
		this.wt = new WaitHelper(driver);
		op = new OrderPage(driver);

	}

	protected By selectstatus = By.xpath("//*[@id='status']");
	private By entermanifest = By.xpath("//*[@placeholder='Enter Manifest No']");
	private By editbtn = By.xpath("(//button[@title='Click to edit'])[1]");
	private By updaterecpiece = By.xpath("//*[@title='Click to update received pieces']");
	private By enterRecpieces = By.xpath("//*[@placeholder='Received Pieces']");
	private By enterLitRecpieces = By.xpath("//*[@placeholder='Lit Received Pieces']");
	private By No = By.xpath("//button[contains(@class,'cnclBTn') and normalize-space(text())='No']");
	protected By Yes = By.xpath("//button[contains(., 'Yes') and contains(@class, 'cnfmBTn')]");
	private By recdate = By.xpath("//*[@placeholder='Received Date']");
	private By voicemaildate = By.xpath("//*[@placeholder='Voice Mail Date']");
	private By recDatebtn = By.xpath("//*[@aria-label='Choose Date']");
	private By voicemailDatebtn = By.xpath("(//*[@aria-label='Choose Date'])[2]");
	private By dateSel = By.xpath("//td[@data-p-today='true']");
	private By clearfilter = By.xpath("//*[@data-pc-section='filterclearicon']");
	
	/**
	 * This method is used to select a status from the status dropdown based on the
	 * value passed as parameter.
	 * 
	 * @param status the status value to be selected
	 */
	public void selectStatus(String status) {
		cp.waitForPopupToDisappear();
		wt.waitToClick(selectstatus, 10);
		wt.waitToClick(By.xpath("//span[contains(text(),'" + status + "')]"), 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * This method is used to search for a specific manifest by entering the
	 * manifest number into the manifest input field.
	 * 
	 * @param manifest the manifest number or identifier to search
	 */
	public void searchManifest(String manifest) {
		cp.clickAndSendKeys(entermanifest, manifest);
	}

	/**
	 * This method is used to fetch the manifest number displayed in the manifest
	 * table.
	 * 
	 * @return the manifest number as String
	 */
	public String getManifest() {
		return cp.getMandatoryText(By.xpath("//tbody//tr/td[2]"));
	}

	
	public boolean isTrackingNoDisplayed() {
	    try {
	        return !driver.findElement(By.xpath("//table//tr[1]/td[7]")).getText().trim().isEmpty();
	    } catch (NoSuchElementException e) {
	        return false;
	    }
	}

	/**
	 * This method is used to edit the selected manifest by clicking on the edit
	 * button.
	 */
	public void editManifest() {
		wt.waitToClick(editbtn, 10);
		cp.waitForLoaderToDisappear();
	}
	
	public String getManifestFromQA() {
		return cp.getMandatoryText(By.xpath("//*[@class='labelBoxK__labelData']"));
	}

	/**
	 * This method is used to fetch the total number of pieces displayed on the
	 * screen.
	 * 
	 * @return total pieces as an integer
	 */
	public int getTotalPieces() {
		return Integer.parseInt(cp.getMandatoryText(By.xpath("(//*[@class='labelBoxK__labelData'])[3]")));
	}

	/**
	 * This method fetches all elements with class 'p-inputgroup-addon', extracts
	 * the numeric values directly, and returns their total sum.
	 *
	 * @return total sum of numbers
	 */
	public int getTotalPiecesSum() {
		List<WebElement> elements = driver.findElements(By.xpath("//span[@class='p-inputgroup-addon']"));
		int sum = 0;

		for (WebElement el : elements) {
			if (el.getText().replace("/", "").trim().matches("\\d+")) {
				sum += Integer.parseInt(el.getText().replace("/", "").trim());
			}
		}
		System.out.println("total pieces sum" + sum);
		return sum;
	}
	
	/**
	 * This method used to click on update received pieces
	 */
	public void clickOnUpdatePieces() {
		wt.waitToClick(updaterecpiece, 10);
	}
	/**
	 * This method used to click on mark as received btn
	 */
	public void clickMarkAsRecPiecesBtn() {
		cp.waitForPopupToDisappear();
		oqp.markSameQty();
	}

	/**
	 * This method fetches the order pieces count from the element and returns it as
	 * an integer.
	 *
	 * Example: if the element text is "/ 17" → it will return 17.
	 *
	 * @return order pieces as an integer
	 */
	public int getOrdPieces() {
		return Integer
				.parseInt(cp.getMandatoryText(By.xpath("//span[@class='p-inputgroup-addon']")).replace("/", "").trim());
	}

	/**
	 * Fetches the entered pieces value from input field and returns as int.
	 */
	public int getEnetrPieces() {
		return Integer.parseInt(cp.getAttributeValue(enterRecpieces, "value").trim());
	}

	/**
	 * Fetches the ordered liter pieces from UI and returns as int.
	 */
	public int getLitOrdPieces() {
		return Integer.parseInt(
				cp.getMandatoryText(By.xpath("(//span[@class='p-inputgroup-addon'])[2]")).replace("/", "").trim());
	}

	/**
	 * Fetches the entered liter pieces value from input field and returns as int.
	 */
	public int getEnetrLitPieces() {
		return Integer.parseInt(cp.getAttributeValue(enterLitRecpieces, "value").trim());
	}

	/**
	 * Returns the QA status text from the 8th column of the first row.
	 */
	public String getQAStatus() {
		return cp.getMandatoryText(By.xpath("//tbody//tr/td[8]"));
	}

	/**
	 * Navigates to the order menu page.
	 */
	public void navigateOrderMenu() throws InterruptedException {
		Thread.sleep(2000);
		oqp.orderpageMenulanding();
	}

	/**
	 * Clicks the edit button on the first row where pieces are fully matched (e.g.,
	 * "22/22").
	 */
	public void editPendingManifest() {
		List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));

		for (int i = 1; i <= rows.size(); i++) {
			String pieces = cp.getMandatoryText(By.xpath("//table/tbody/tr[" + i + "]/td[5]"));

			String[] split = pieces.split("/");
			if (split.length == 2 && split[0].trim().equals(split[1].trim())) {
				WebElement editBtn = driver
						.findElement(By.xpath("//table/tbody/tr[" + i + "]/td//button[@title='Click to edit']"));
				editBtn.click();
				cp.waitForLoaderToDisappear();
				break;
			}
		}
	}

	/**
	 * Returns a string representing pieces adjusted by the given value. Example: if
	 * current pieces = 5, passing 1 returns "6", passing -1 returns "4".
	 */
	public String getMoreOrLessPiece(int i) {
		int pc = getOrdPieces();
		return String.valueOf(pc + i);
	}

	/**
	 * Returns a string representing liter pieces adjusted by the given value.
	 * Example: if current liter pieces = 5, passing 1 returns "6", passing -1
	 * returns "4".
	 */
	public String getMoreOrLessLitPiece(int i) {
		int pc = getLitOrdPieces();
		return String.valueOf(pc + i);
	}

	/**
	 * Enters actual pieces value into the corresponding input field.
	 */
	public void enterActualPieces(String pieces) {
		cp.clickAndSendKeys(enterRecpieces, pieces);
	}

	/**
	 * Enters actual liter pieces value into the corresponding input field.
	 */
	public void enterLitPieces(String pieces) {
		cp.clickAndSendKeys(enterLitRecpieces, pieces);
	}

	/**
	 * Checks if the "Total pieces and received pieces do not match" message is displayed.
	 *
	 * @return true if the message is displayed, false otherwise
	 */
	public boolean isPopDisplayed() {
	    try {
	        WebElement message = driver.findElement(By.xpath("//h2[contains(text(),'Total pieces and received pieces do not match')]"));
	        return message.isDisplayed();
	    } catch (NoSuchElementException e) {
	        return false;
	    }
	}
	
	/**
	 * This method used to click on No btn on pop up
	 */
	public void clickOnNo() {
		cp.waitAndClickWithJS(No, 5);
	}
	
	/**
	 * This method used to click on yes btn on pop up
	 */
	public void clickOnYes() {
		cp.waitAndClickWithJS(Yes, 5);
	}
	
	public WebElement isUpdatePiecsBtnDisable() {
		return oqp.verifyUpdatePiecsBtnDisable();
	}
	
	public void clearDates() {
		driver.findElement(recdate).clear();
		driver.findElement(voicemaildate).clear();
		
	}
	
	public void ReceivedDateSelection() throws InterruptedException {
		cp.waitForPopupToDisappear();
		wt.waitToClick(recDatebtn, 10);
		Thread.sleep(1000);
		wt.waitToClick(dateSel, 10);
		wt.waitToClick(recDatebtn, 10);
	}
	
	public void voiceMailDateSelection(String date) throws InterruptedException {
		cp.waitForPopupToDisappear();
		cp.clickAndSendKeys(voicemaildate, date);
//		wt.waitToClick(voicemailDatebtn, 10);
//		Thread.sleep(1000);
//		wt.waitToClick(dateSel, 10);
//		wt.waitToClick(voicemailDatebtn, 10);
	}

	public String getCurrentDateTime() {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
	    return LocalDateTime.now().format(formatter);
	}
	
	/**
	 * This methods is used to check coloum filter
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void checkColoumFilter(String pageName) throws InterruptedException, IOException {

		String commonPath = System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\Locators.JSON";

		String jsonContent = new String(Files.readAllBytes(Paths.get(commonPath)));
		JSONObject root = new JSONObject(jsonContent);

		//String pageName = "inboundPalletPage";

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
			System.out.println("Page not found: " + pageName);
		}

	}
	/**
	 * This method use to click clear filter icon if available
	 */
	public void clearFilter() {
		try {
			wt.waitToClick(clearfilter, 5);
			Thread.sleep(3000);
			cp.waitForLoaderToDisappear();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	


}
