package com.uiFramework.Genesis.web.pages;

import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;

import com.uiFramework.Genesis.helper.WaitHelper;

public class NavigationPages extends OutboundPalletPage {

    WebDriver driver = null;
    WaitHelper wt;
    CommonPage cp = null;

    InboundPalletPage ip = null;
    InbTruckPage it = null;
    InboundQAPage iqa = null;
    OutboundTruckPage otp = null;
    UpdateTracking ut = null;
    DocQueue dq = null;
    protected OutboundQApage oqp = null;
    ConsigneeQA cqa = null;
    OrderPage op = null;

    String ordData;

    // simple JS click fallback used by safeClick()
    private JavascriptExecutor js() { return (JavascriptExecutor) driver; }

    public NavigationPages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.ip = new InboundPalletPage(driver);
        this.it = new InbTruckPage(driver);
        this.iqa = new InboundQAPage(driver);
        this.otp = new OutboundTruckPage(driver);
        this.cp = new CommonPage(driver);
        this.wt = new WaitHelper(driver);
        this.dq = new DocQueue(driver);
        this.ut = new UpdateTracking(driver);
        this.oqp = new OutboundQApage(driver);
        this.cqa = new ConsigneeQA(driver);
        this.op = new OrderPage(driver);
    }

    // --- Locators ---
    private By palletMenu = By.xpath("//i[@title='Pallet']");
    private By barMenu = By.xpath("//i[@title='Barcode Printing']");
    private By inboundpalletMenu = By.xpath("//a[@href='/pallet/inbound-pallet']");
    private By outboundpalletMenu = By.xpath("//a[@href='/pallet/outbound-pallet']");
    private By truckMenu = By.xpath("//i[@title='Truck']");
    private By inboundTruckMenu = By.xpath("//a[@href='/truck/inbound-truck']");
    private By obtTruckMenu = By.xpath("//a[@href='/truck/outbound-truck']");
    private By docMenu = By.xpath("//i[@title='Documents']");
    private By qaMenu = By.xpath("//i[@title='QA']");
    private By inboundqaMenu = By.xpath("//a[@href='/qa/inbound-qa']");
    private By tracking = By.xpath("//i[@title='Tracking & ETA']");
    private By QAMenu = By.xpath("//i[@title='QA']");
    private By outboundQAMenu = By.xpath("//a[@href='/qa/outbound-qa']");
    private By QAMenu1 = By.xpath("//*[@id='root']/div[3]/div[1]/div/div/ul/li[8]/div");
    private By consigneeQAMenu = By.xpath("//*[@id='root']/div[3]/div[1]/div/div/ul/div/div/div/ul[3]/a/li");
    private By trackingMenu = By.xpath("//i[@title='Tracking & ETA']");
    private By trackingUpdateMenu = By.xpath("//a[@href='/outbound-truck/update-tracking-no-eta']");
    private By dropForDrpDwn = By.xpath("(//*[text()='LDA'])[2]");
    private By palletFormDrpDwn = By.xpath("(//*[text()='WAREHOUSE'])[2]");
    private By searchDrpDwn = By.xpath("//*[@class='p-dropdown-filter p-inputtext p-component']");
    private By searchOrdNo = By.xpath("//*[@placeholder='Enter Order No']");
    private By searchContain = By.xpath("(//input[@placeholder='Contains...'])[3]");
    private By createpickup = By.xpath("//*[@id='outPalletCreateOrderpickupLocationId']");
    private By listpickup = By.xpath("//*[@id='outPalletpickupLocationId']");
    private By createtruckpickup = By.xpath("//*[@id='outboutTruckCreateHeaderpickupLocation']");
    private By docGenMenu = By.xpath("//a[@href='/documents/document-queue']");

    // Return POD
    private By podmenu = By.xpath("//i[@title='Return POD']");
    private By podsubmenu = By.xpath("//a[@href='/return-pod']");
    private By schedate = By.xpath("//*[@id='podUploadschedDeliveryDateTime']//*[@aria-label='Choose Date']");
    private By deldate = By.xpath("//*[@id='podUploaddeliveryDateTime']//*[@aria-label='Choose Date']");
    private By dateSel = By.xpath("//td[@data-p-today='true']");
    private By clickoutside = By.xpath("//*[contains(text(),'Add Return POD Details')]");
    private By enternote = By.xpath("//*[@placeholder='Enter Notes']");
    private By clearfilter = By.xpath("(//*[@class='p-icon' and @data-pc-section='filterclearicon'])[3]");

    // Common loader (was referenced but not declared in original file)
    private By loader = By.xpath("//*[@src='/static/media/loading.db43a6dd94d78914920a.gif']");
	private By entertracking = By.xpath("//*[@placeholder='Enter Tracking Number']");
	private By savetrackingbtn  = By.xpath("//*[@title='Click to save tracking details']");
	private By scheduleDateOnOrder  = By.xpath("//*[@aria-label='Choose Date']");
	private By input = By.xpath("//*[@class='p-dropdown-filter p-inputtext p-component']");
	private By selectval = By.xpath("//*[@class='p-dropdown-items']");

    // Small resilience helpers
    private void safeClick(By locator) {
        try {
            wt.waitToClick(locator, 10);
        } catch (Exception e) {
            try {
                WebElement el = driver.findElement(locator);
                js().executeScript("arguments[0].click();", el);
            } catch (Exception ignored) {}
        }
    }

    private void clearAndType(By locator, String text) {
        try {
            cp.clickAndSendKeys(locator, text);
        } catch (StaleElementReferenceException | NoSuchElementException e) {
            cp.clickAndSendKeys(locator, text);
        }
    }

    // ---------------- Inbound Pallet/Truck/QA flows ----------------

    /** Open Inbound Pallet menu */
    public void inboundMenu() throws TimeoutException {
        cp.waitForLoaderToDisappear();
        safeClick(barMenu);
        safeClick(palletMenu);
        safeClick(inboundpalletMenu);
        cp.waitForLoaderToDisappear();
    }

    public void inboundPalletProcess(String order) throws TimeoutException, InterruptedException {
        inboundMenu();
        ip.createPallet();
        ip.searchOrd(order);
        ip.createPalletforMorder();
    }

    public String getIBPL() {        
        cp.waitForLoaderToDisappear();
        return cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[2]"));
    }

    public void inboundTruckProcess(String IBPL) throws TimeoutException, InterruptedException {
        inboundTruckMenu();
        it.createTruck();
        it.search1Order(IBPL); // kept as-is to match your existing API
        it.selectLTL("10", cm.getcharacterString(15));
        it.addTruckDetails(IBPL);
        it.truckMarkasArrived();
        cp.waitForPopupToDisappear();
    }

    public String getTruckNo() {
        return cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[3]"));
    }

    public void inboundQAProcess(String trukno) throws TimeoutException, InterruptedException {
        inboundQaMenu();
        it.search1Order(trukno); // kept as-is to match your existing API
        iqa.viewButton();
        iqa.checkValidationQA();
    }

    // ---------------- Outbound Pallet creation helpers ----------------

    public String[] dataArray = new String[5];

    public void outboundPalletForWHToOrigin(String order, String pickup) throws TimeoutException {
        outboundMenu();
        createPallet();
        cp.searchSection();
        selectDropForDrpDwn("Origin");
        selectPalletFormDrpDwn("Warehouse");
        selectPickup(createpickup, pickup);
        searchOrdNo(order);
        cp.Search();
        createPalletforMorder();

        cp.searchSection();
        cp.clickClearButton();
        selectDropForDrpDwn("Origin");
        selectPickup(listpickup, pickup);
        searchOrdNo(order);
        cp.Search();
        dataArray[0] = getOBPL();
        System.out.println("Wh to origin OBPL= " + dataArray[0]);
    }

    public void outboundPalletForLDAToOrigin(String order, String pickup) throws TimeoutException {
        outboundMenu();
        createPallet();
        cp.searchSection();
        selectDropForDrpDwn("Origin");
        selectPalletFormDrpDwn("LDA");
        selectPickup(createpickup, pickup);
        searchOrdNo(order);
        cp.Search();
        createPalletforMorder();

        cp.searchSection();
        cp.clickClearButton();
        selectDropForDrpDwn("Origin");
        selectPalletFormDrpDwn("LDA");
        selectPickup(listpickup, pickup);
        searchOrdNo(order);
        cp.Search();
        dataArray[1] = getOBPL();
        System.out.println("LDA to origin OBPL= " + dataArray[1]);
    }

    public void outboundPalletForLDAToLDA(String order, String pickup) throws TimeoutException {
        outboundMenu();
        createPallet();
        cp.searchSection();
        selectPalletFormDrpDwn("LDA");
        selectPickup(createpickup, pickup);
        searchOrdNo(order);
        cp.Search();
        createPalletforMorder();

        cp.searchSection();
        cp.clickClearButton();
        selectPalletFormDrpDwn("LDA");
        selectPickup(listpickup, pickup);
        searchOrdNo(order);
        cp.Search();
        dataArray[2] = getOBPL();
        System.out.println("LDA to LDA OBPL= " + dataArray[2]);
    }

    public void outboundPalletForWHToLDA(String order, String pickup) throws TimeoutException {
        outboundMenu();
        createPallet();
        cp.searchSection();
        selectPickup(createpickup, pickup);
        searchOrdNo(order);
        cp.Search();
        createPalletforMorder();

        cp.searchSection();
        cp.clickClearButton();
        selectPickup(listpickup, pickup);
        searchOrdNo(order);
        cp.Search();
        dataArray[3] = getOBPL();
        System.out.println("WH to LDA OBPL= " + dataArray[3]);
    }

    public void selectPickup(By element, String val) {
        wt.waitToClick(element, 10);
        cp.DrpDwnValueSel(searchDrpDwn, val);
        cp.waitForLoaderToDisappear();
    }

    /** Get OBPL number from list */
    public String getOBPL() {
        return cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[3]"));
    }

    /**
     * Generic creator for outbound pallets across combinations.
     */
    public void createOutboundPallet(String order, String pickup, String pickFor, String dropFor, int arrayIndex)
            throws TimeoutException {
        if (order == null || pickup == null) {
            throw new SkipException("Skipping test outbound Pallet " + pickFor + " to " + dropFor
                    + " because one or more required arguments are null.");
        }

        outboundMenu();
        createPallet();
        cp.searchSection();

        if (dropFor != null && !dropFor.isEmpty()) selectDropForDrpDwn(dropFor);
        if (pickFor != null && !pickFor.isEmpty()) selectPalletFormDrpDwn(pickFor);

        selectPickup(createpickup, pickup);
        searchOrdNo(order);
        cp.Search();
        createPalletforMorder();

        cp.searchSection();
        cp.clickClearButton();

        if (dropFor != null && !dropFor.isEmpty()) selectDropForDrpDwn(dropFor);
        if (pickFor != null && !pickFor.isEmpty()) selectPalletFormDrpDwn(pickFor);

        selectPickup(listpickup, pickup);
        searchOrdNo(order);
        cp.Search();

        dataArray[arrayIndex] = getOBPL();
        System.out.println(pickFor + " to " + dropFor + " OBPL= " + dataArray[arrayIndex]);
    }

    // ---------------- Menu navigation shortcuts ----------------

    public void outboundMenu() throws TimeoutException {
        cp.waitForLoaderToDisappear();
        safeClick(barMenu);
        safeClick(palletMenu);
        safeClick(outboundpalletMenu);
        cp.waitForLoaderToDisappear();
    }

    public void inboundTruckMenu() throws TimeoutException {
        cp.waitForLoaderToDisappear();
        safeClick(barMenu);
        safeClick(truckMenu);
        safeClick(inboundTruckMenu);
        cp.waitForLoaderToDisappear();
    }

    public void outboundTruckMenu() throws TimeoutException {
        cp.waitForLoaderToDisappear();
        safeClick(barMenu);
        safeClick(truckMenu);
        safeClick(obtTruckMenu);
        cp.waitForLoaderToDisappear();
    }

    public void inboundQaMenu() throws TimeoutException, InterruptedException {
        cp.waitForLoaderToDisappear();
        safeClick(docMenu);
        safeClick(qaMenu);
        Thread.sleep(300);
        safeClick(inboundqaMenu);
        cp.waitForLoaderToDisappear();
    }

    /** Outbound QA menu path: Tracking → QA → Outbound QA */
    public void outboundQAMenu() throws TimeoutException, InterruptedException {
        cp.waitForLoaderToDisappear();
        safeClick(tracking);
        Thread.sleep(300);
        safeClick(QAMenu);
        Thread.sleep(300);
        safeClick(outboundQAMenu);
        cp.waitForLoaderToDisappear();
    }

    /** Consignee QA menu path: Documents → QA → Consignee QA */
    public void consigneeQAMenu() throws TimeoutException, InterruptedException {
        cp.waitForLoaderToDisappear();
        safeClick(docMenu);
        safeClick(QAMenu1);
        Thread.sleep(300);
        safeClick(consigneeQAMenu);
        cp.waitForLoaderToDisappear();
    }

    /** Documents → Document Queue */
    public void docQueueMenu() throws TimeoutException, InterruptedException {
        cp.waitForLoaderToDisappear();
        safeClick(docMenu);
        Thread.sleep(300);
        safeClick(docGenMenu);
        cp.waitForLoaderToDisappear();
    }

    /** Tracking & ETA → Update Tracking */
    public void updteTrackMenu() throws TimeoutException, InterruptedException {
        cp.waitForLoaderToDisappear();
        safeClick(trackingMenu);
        Thread.sleep(300);
        safeClick(trackingUpdateMenu);
        cp.waitForLoaderToDisappear();
    }

    // ---------------- End-to-end helper chains ----------------

    public void navigationTest(String ordno) throws TimeoutException, InterruptedException {
        outboundPalletProcess(ordno);
        outboundTruckProcess(ordno, ordno);
        trkingDetailUpdateProcess();
        // outboundQAProcess(...);
        // consigneeQAProcess(...);
    }

    public void outboundPalletProcess(String ordno) throws TimeoutException {
        super.outboundMenu();
        super.createPallet();
        cp.searchSection();
        selectDropForDrpDwn("");
        selectPalletFormDrpDwn("");
        searchOrdNo("");
        cp.Search();
        super.createPalletforMorder();

        cp.searchSection();
        selectDropForDrpDwn("");
        selectPalletFormDrpDwn("");
        searchOrdNo("");
        cp.Search();

        super.getOutboundPallet();
        ordData = cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[3]"));
    }

    private void selectDropForDrpDwn(String val) {
        wt.waitToClick(dropForDrpDwn, 10);
        cp.DrpDwnValueSel(searchDrpDwn, val);
        cp.waitForLoaderToDisappear();
    }

    private void selectPalletFormDrpDwn(String val) {
        wt.waitToClick(palletFormDrpDwn, 10);
        cp.DrpDwnValueSel(searchDrpDwn, val);
        cp.waitForLoaderToDisappear();
    }

    private void searchOrdNo(String val) {
        clearAndType(searchOrdNo, val);
    }

    /**
     * Create Outbound Truck for given context.
     */
    public void obtTruckProcess(String pickup, String obpl, String pickFor, String dropFor)
            throws TimeoutException, InterruptedException {
        if (pickup == null || obpl == null) {
            throw new SkipException("Skipping test outbound Truck " + pickFor + " to " + dropFor
                    + " because one or more required arguments are null.");
        }
        otp.truckMenu();
        otp.createTruck();
        selectPalletFormDrpDwn(pickFor);
        selectPickupForTruck(pickup);
        selectDropForDrpDwn(dropFor);
        otp.selectLTL("10", cm.getcharacterString(15));
        otp.searchPallet1(obpl);
        otp.selectCheckbox();
        otp.saveObtTruck();
        cp.captureToastMessage();
    }

    public void selectPickupForTruck(String val) {
        wt.waitToClick(createtruckpickup, 10);
        cp.DrpDwnValueSel(searchDrpDwn, val);
        cp.waitForLoaderToDisappear();
    }

    public void outboundTruckProcess(String val, String LDA) throws InterruptedException, TimeoutException {
        otp.truckMenu();
        otp.createTruck();
        cp.searchSection();
        selectPalletFormDrpDwn(val);
        selectDropForDrpDwn(LDA);
        otp.searchPallet1(ordData);
        otp.selectLTL("10", "Resopnse number-102912 and Instructions");
        otp.searchPallet();
        otp.selectCheckbox();
        otp.saveObtTruck();
    }

    // ---------------- Document Queue / Doc Numbers ----------------

    public void docGenerationProcess(String order, int index) throws TimeoutException, InterruptedException {
        dq.docQueueMenu();
        dq.clickCloseoutBtn();
        dq.clickReconsinmntBtn();
        dq.checkAndSelectOrder();
        dq.searchOrder1(order);
        dq.clickLDABtn();
        dq.clickGeneratedDoc();
        dq.searchOrderInGenScreen1(order);
        getDocumentNumber(index);
    }

    public String[] manifestArray = new String[10];
    public String[] lbolArray = new String[10];
    public String[] ldaaArray = new String[10];

    public void getDocumentNumber(int index) {
        try {
            manifestArray[index] = cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[3]"));
            lbolArray[index] = cp.getMandatoryText(By.xpath("//table/tbody/tr[2]/td[3]"));
            ldaaArray[index] = cp.getMandatoryText(By.xpath("//table/tbody/tr[2]/td[7]"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- Tracking ----------------

    public void trkingDetailUpdateProcess() throws TimeoutException, InterruptedException {
        ut.updteTrackMenu();
        ut.enterTrackingDetailsInLoop();
    }

    // ---------------- QA flows ----------------

    public void outboundQAProcess(String lda, String manifest) throws TimeoutException, InterruptedException {
        oqp.outboundQAMenu();
        oqp.selectLDA(lda);
        oqp.selectLBOL(manifest);
        cp.Search();
        oqp.HandlePopup();
        oqp.markSameQty();
        oqp.clickOnUpdateRecPiece();
    }

    public void consigneeQAProcess(String lbol) throws InterruptedException {
        cqa.selectLBOL(lbol);
        cp.Search();
        cqa.upload_file();
        cp.waitForPopupToDisappear();
        cqa.scheduleDelieverySelection();
        cqa.scheduleDateSelection();
        cqa.enterSignBy("David");
        Thread.sleep(1000);
        cqa.ClickSaveValBtn();
    }

    public String[] outboundPalletProcess_array(String[] ordArray) throws TimeoutException {
        String[] palletArray = new String[ordArray.length];
        super.outboundMenu();

        for (int i = 0; i < ordArray.length; i++) {
            super.createPallet();
            cp.searchSection();
            searchOrdNo(ordArray[i]);
            cp.Search();

            if (i == 0) {
                super.createPalletforMorder();
            }

            String palletNo = super.getOutboundPallet();
            palletArray[i] = palletNo;
        }

        return palletArray;
    }

    // ---------------- Return POD ----------------

    public void returnPODProcess(String order) throws InterruptedException {
        cp.waitForLoaderToDisappear();
        wt.waitToClickWithAction(podmenu, 10);
        Thread.sleep(300);
        wt.waitToClick(podsubmenu, 10);
        cp.waitForLoaderToDisappear();

        cp.searchColoumFilter(searchContain, order);
        wt.waitToClick(By.xpath("//tbody//tr[1]//td[1]"), 10);

        scheduleDelDateSelection();
        deliveryDateSelection();

        cqa.upload_file();
        cp.clickAndSendKeys(enternote, cm.getcharacterString(20));
        cp.save();
        cp.captureToastMessage();
        cp.waitForLoaderToDisappear();

        wt.waitToClick(clearfilter, 10);
        wt.waitForElement(loader, 4);
        cp.waitForLoaderToDisappear();
    }

    public void scheduleDelDateSelection() throws InterruptedException {
        Thread.sleep(300);
        wt.waitToClick(schedate, 10);
        Thread.sleep(300);
        wt.waitToClick(dateSel, 10);
        wt.waitToClick(clickoutside, 10);
        wt.waitToClick(clickoutside, 10);
    }

    public void deliveryDateSelection() throws InterruptedException {
        Thread.sleep(300);
        wt.waitToClick(deldate, 10);
        Thread.sleep(300);
        wt.waitToClick(dateSel, 10);
        wt.waitToClick(clickoutside, 10);
        wt.waitToClick(clickoutside, 10);
    }
    
    public void endToEndOrder() throws Throwable {
		createOrder();
		inboundPalletProcess(MGLOrderno);
		ordData= getIBPL();
		System.out.println("ibpl no "+ordData);
		inboundTruckProcess(ordData);
		ordData= getTruckNo();
		System.out.println("truck no "+ordData);
		inboundQAProcess(ordData);
		createOutboundPallet(MGLOrderno, MWarehouse,  "Warehouse", "LDA", 0);
		//createOutboundPallet("9869", "envision",  "Warehouse", "LDA", 0);
		obtTruckProcess(MWarehouse, dataArray[0] , "Warehouse", "LDA");
		//obtTruckProcess("envision", dataArray[0] , "Warehouse", "LDA");
		docGenerationProcess(MGLOrderno, 0);
		//docGenerationProcess("GL-10024", 0);
		//UpdateTrackingFromOrder();
		//trkingDetailUpdateProcess();
		
			
	}
    
    public void createOrder() throws Throwable {
		op.orderCreation(cm.getNumericString(7));
		op.enetrpieces("10");
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		selectCarrier("567");
		op.confirm();
		cp.waitAndClickWithJS(yes, 5);
		cp.waitAndClickWithJS(yes, 5);
		cp.waitForLoaderToDisappear();
		MGLOrderno = driver.findElement(getglorder).getAttribute("value");
		MWarehouse = cp.getAttributeValue(getwarehouse, "valuelabel");
		System.out.println("orderno" + MGLOrderno);
		System.out.println("warehouse" + MWarehouse);
		cp.waitForLoaderToDisappear();
	}
    
    private By selcarrier = By.xpath("(//*[@id='deliveringLocalCarrier'])[1]"); 
	private void selectCarrier(String carrier) throws InterruptedException {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(selcarrier, 10);
		DrpDwnValueSel(input, carrier);
		
	}
	
	public void DrpDwnValueSel(By input, String DrpDwnValue) {
		driver.findElement(input).click();
		driver.findElement(input).clear();
		driver.findElement(input).sendKeys(DrpDwnValue);
		wt.waitToClick(selectval, 5);
	}
	
	public void UpdateTrackingFromOrder() throws InterruptedException {
		try {
		oqp.orderpageMenulanding();
		searchOrderno(MGLOrderno);
		editBtn();
		cp.waitAndClickWithJS(entertracking, 10);
		cp.clickAndSendKeys(entertracking, cm.getAlphaNumericString(7));
		wt.waitToClick(scheduleDateOnOrder, 10);
		Thread.sleep(1000);
		wt.waitToClick(dateSel, 10);
		wt.waitToClick(savetrackingbtn, 10);
		cp.waitForLoaderToDisappear();
		wt.waitToClick(barMenu, 10);

		} catch (Exception e) {
		System.out.println("Error found While update tracking From order edir.");
		}

		}
	

}
