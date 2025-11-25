package com.uiFramework.Genesis.helper;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitHelper {
    private final WebDriver driver;
    private final boolean isSafari;
    private final boolean isIOS;

    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        boolean safari = false, ios = false;
        try {
            if (driver instanceof RemoteWebDriver) {
                Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
                String browser = String.valueOf(caps.getBrowserName()).toLowerCase();
                safari = browser.contains("safari");
                Object platformName = caps.getCapability("platformName");
                ios = platformName != null && "ios".equalsIgnoreCase(String.valueOf(platformName));
            }
        } catch (Exception ignored) {}
        this.isSafari = safari;
        this.isIOS = ios;
    }

    /* =========================
       Core wait constructors
       ========================= */
    public void setImplicitWait(long timeoutSeconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeoutSeconds));
    }

    public void pageLoadTime(long timeoutSeconds) {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeoutSeconds));
    }

    private WebDriverWait getWait(long timeOutInSeconds, long pollingEveryInMillis) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        wait.pollingEvery(Duration.ofMillis(pollingEveryInMillis));
        wait.ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchFrameException.class);
        return wait;
    }

    private WebDriverWait getWait(long timeOutInSeconds) {
        return getWait(timeOutInSeconds, 250);
    }

    /* =========================
       Utility (Safari-safe)
       ========================= */
    private void scrollIntoView(WebElement el) {
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);
        }
    }

    private void jsClick(WebElement el) {
        if (!(driver instanceof JavascriptExecutor)) return;
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private void safeClickElement(WebElement el) {
        try {
            el.click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException ex) {
            try {
                scrollIntoView(el);
                el.click();
            } catch (Exception ex2) {
                jsClick(el); // reliable fallback esp. on Safari
            }
        } catch (Exception e) {
            // last resort
            jsClick(el);
        }
    }

    private void robustClear(WebElement el) {
        try {
            el.clear();
            return;
        } catch (InvalidElementStateException ignored) {}
        // Select-all + delete (covers iOS/desktop Safari)
        try { el.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE); } catch (Exception ignored) {}
        try { el.sendKeys(Keys.chord(Keys.COMMAND, "a"), Keys.DELETE); } catch (Exception ignored) {}
    }

    /* =========================
       Visibility / Clickability
       ========================= */

    /** Prefer this (By) over the WebElement variant */
    public WebElement waitForElementVisibleWithPollingTime(By locator, int timeOutInSeconds, int pollingEveryInMilliSec) {
        return getWait(timeOutInSeconds, pollingEveryInMilliSec)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Kept for compatibility; prefer the By version above. */
    public void waitForElementVisibleWithPollingTime(WebElement element, int timeOutInSeconds, int pollingEveryInMiliSec) {
        getWait(timeOutInSeconds, pollingEveryInMiliSec)
                .until(d -> {
                    try { return element.isDisplayed(); }
                    catch (StaleElementReferenceException e) { return false; }
                });
    }

    public WebElement waitForElementClickable(By locator, int timeOutInSeconds) {
        return getWait(timeOutInSeconds).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean waitForElementNotPresent(By locator, long timeOutInSeconds) {
        return getWait(timeOutInSeconds).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public boolean waitForElementNotPresent(WebElement element, long timeOutInSeconds) {
        // invisibilityOf returns true if element is not present or stale
        return getWait(timeOutInSeconds).until(ExpectedConditions.invisibilityOf(element));
    }

    public void waitForFrameToBeAvailableAndSwitchToIt(By frameLocator, long timeOutInSeconds) {
        getWait(timeOutInSeconds).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
    }

    public void waitForFrameToBeAvailableAndSwitchToIt(WebElement frameElement, long timeOutInSeconds) {
        getWait(timeOutInSeconds).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
    }

    public void waitForDocumentReady(long timeOutInSeconds) {
        if (!(driver instanceof JavascriptExecutor)) return;
        getWait(timeOutInSeconds).until(d ->
            "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState")));
    }

    /* =========================
       Original API (fixed)
       ========================= */

    /** Was calling findElement inside EC; now uses locator-based EC. */
    public void waitForElement(By locator, int timeOutInSeconds) {
        getWait(timeOutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Visible then click (with Safari-safe fallback). */
    public void visibility(By locator, int timeOutInSeconds) {
        WebElement el = getWait(timeOutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
        safeClickElement(el);
    }

    /** Clickable then click (with scroll/JS fallback). */

	public void waitToClick(By locator, int timeOutInSeconds) {
		WebElement el = waitForElementClickable(locator, timeOutInSeconds);
		safeClickElement(el);
	}
 
//    public void waitToClick(By locator, int timeOutInSeconds) {
//        WebElement el = waitForElementClickable(locator, timeOutInSeconds);
//
//        try {
//            // Normal click
//            el.click();
//        } catch (Exception e) {
//            // Safari or tricky element fallback using JS
//            ((JavascriptExecutor) driver).executeScript(
//                "arguments[0].scrollIntoView(true); arguments[0].click();", el);
//        }
//    }
    
//    public void waitToClick(By locator, int timeoutSec) throws org.openqa.selenium.ElementClickInterceptedException {
//        int attempts = 0;
//        while (attempts++ < 3) {
//            try {
//                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
//                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
//
//                // scroll into view (helps Safari with fixed headers)
//                ((JavascriptExecutor) driver)
//                    .executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", el);
//
//                try {
//                    el.click(); // first try: native click
//                    return;
//                } catch (org.openqa.selenium.ElementNotInteractableException e) {
//
//                    // second try: Actions click
//                    try {
//                        new org.openqa.selenium.interactions.Actions(driver)
//                            .moveToElement(el)
//                            .pause(Duration.ofMillis(100))
//                            .click()
//                            .perform();
//                        return;
//                    } catch (org.openqa.selenium.WebDriverException ignored) {
//                        // third try: JS click (often needed in Safari for overlays)
//                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
//                        return;
//                    }
//                }
//            } catch (org.openqa.selenium.StaleElementReferenceException stale) {
//                // re-locate and retry
//            }
//        }
//        // last attempt: let the normal wait throw if still not clickable
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
//        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
//    }


    public void waitToClickwtException(By locator, int timeOutInSeconds) {
        try {
            waitToClick(locator, timeOutInSeconds);
        } catch (Exception ignored) {}
    }

//    /** On Safari/iOS, Actions click is flaky; fall back to safeClick. */
//    public void waitToClickWithAction(By locator, int timeOutInSeconds) {
//        WebElement target = getWait(timeOutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
//        getWait(timeOutInSeconds).until(ExpectedConditions.elementToBeClickable(target));
//        if (isSafari || isIOS) {
//            safeClickElement(target);
//            return;
//        }
//        try {
//            new Actions(driver).moveToElement(target).click().perform();
//        } catch (Exception e) {
//            safeClickElement(target);
//        }
//    }
    
    public void waitToClickWithAction(By locator, int timeoutSec) {
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(timeoutSec));
        wait.ignoreAll(java.util.Set.of(
            org.openqa.selenium.StaleElementReferenceException.class,
            org.openqa.selenium.ElementClickInterceptedException.class));

        // Wait until it's really clickable, based on the LOCATOR (avoids stale instances)
        WebElement el = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(locator));

        // Always center it first – helps both Chrome & Safari
        scrollIntoViewCenter(el);

        // Safari / iOS: avoid Actions – try native click, then JS click
        if (isSafari || isIOS) {
            try {
                el.click();
            } catch (Exception e) {
                jsClickk(el);
            }
            return;
        }

        // Desktop non-Safari: try Actions first, then native click, then JS
        try {
            new org.openqa.selenium.interactions.Actions(driver)
                .moveToElement(el)
                .pause(java.time.Duration.ofMillis(50))
                .click()
                .perform();
        } catch (Exception actionsFailed) {
            try {
                // Re-acquire and try a plain click in case the element went stale/intercepted
                el = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(locator));
                el.click();
            } catch (Exception nativeFailed) {
                jsClickk(el);
            }
        }
    }

    private void scrollIntoViewCenter(WebElement el) {
        ((org.openqa.selenium.JavascriptExecutor) driver)
            .executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", el);
    }

    private void jsClickk(WebElement el) {
        ((org.openqa.selenium.JavascriptExecutor) driver)
            .executeScript("arguments[0].click();", el);
    }


    public void waitToClickByWebelement(WebElement element, int timeOutInSeconds) {
        WebDriverWait wait = getWait(timeOutInSeconds);
        wait.until(d -> {
            try { return element.isDisplayed(); }
            catch (StaleElementReferenceException e) { return false; }
        });
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (StaleElementReferenceException ignored) {}
        safeClickElement(element);
    }

    public void invisibility(WebElement element, int timeOutInSeconds) {
        getWait(timeOutInSeconds).until(ExpectedConditions.invisibilityOf(element));
    }

    /* =========================
       Lists
       ========================= */

    public void waitForElementVisibleWithPollingTimeForList(List<WebElement> elements,
                                                            int timeOutInSeconds,
                                                            int pollingEveryInMilliSec) {
        getWait(timeOutInSeconds, pollingEveryInMilliSec).until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    /** New: locator-based list visibility (preferred). */
    public List<WebElement> waitForAllVisible(By locator, int timeOutInSeconds, int pollingEveryInMilliSec) {
        return getWait(timeOutInSeconds, pollingEveryInMilliSec)
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /* =========================
       Typing helper (optional)
       ========================= */

    /** Optional convenience: wait, clear robustly, then type (Safari/iOS-safe). */
    public void type(By locator, String text, int timeOutInSeconds) {
        WebElement el = getWait(timeOutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
        robustClear(el);
        el.sendKeys(text);
    }
}
