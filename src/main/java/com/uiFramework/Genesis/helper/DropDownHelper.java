package com.uiFramework.Genesis.helper;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DropDownHelper {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // sensible defaults for PrimeNG-like menus
    private static final By DEFAULT_MULTI_OPTIONS = By.cssSelector("li.p-multiselect-item");
    private static final By DEFAULT_LISTBOX_OPTIONS = By.cssSelector("li.p-dropdown-item");

    public DropDownHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // -----------------------
    // <select> helpers
    // -----------------------

    public void selectUsingValue(WebElement element, String value) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        new Select(element).selectByValue(value);
    }

    public void selectUsingIndex(WebElement element, int index) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        new Select(element).selectByIndex(index);
    }

    public void selectUsingVisibleText(WebElement element, String visibleText) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        new Select(element).selectByVisibleText(visibleText);
    }

    public void deSelectUsingValue(WebElement element, String value) {
        new Select(element).deselectByValue(value);
    }

    public void deSelectUsingIndex(WebElement element, int index) {
        new Select(element).deselectByIndex(index);
    }

    public void deSelectUsingVisibleText(WebElement element, String visibleText) {
        new Select(element).deselectByVisibleText(visibleText);
    }

    public List<String> getAllDropDownData(WebElement element) {
        return new Select(element).getOptions().stream()
                .map(WebElement::getText)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    // -----------------------
    // Searchable/type-ahead dropdowns
    // -----------------------

    /** Your original helper, now with waits. */
    public void DrpDwnValueSel(WebElement input, String value) {
        wait.until(ExpectedConditions.elementToBeClickable(input)).click();
        input.clear();
        input.sendKeys(value);
        // give the list time to filter, then confirm with ENTER
        wait.until(driver -> input.getAttribute("value") != null && input.getAttribute("value").length() > 0);
        input.sendKeys(Keys.ARROW_DOWN);
        input.sendKeys(Keys.ENTER);
    }

    /** Overload that accepts a locator for convenience. */
    public void DrpDwnValueSel(By inputLocator, String value) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputLocator));
        DrpDwnValueSel(input, value);
    }

    /**
     * Selects a single option from an open listbox (e.g., PrimeNG) by visible text.
     * Provide the menu container (already opened) and option locator.
     */
    public void selectFromOpenListByText(By optionsLocator, String textExact) {
        List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(optionsLocator));
        for (WebElement opt : options) {
            if (textExact.equals(opt.getText().trim())) {
                wait.until(ExpectedConditions.elementToBeClickable(opt)).click();
                return;
            }
        }
        throw new NoSuchElementException("Option not found: " + textExact);
    }

    // -----------------------
    // Multi-select helpers
    // -----------------------

    /**
     * Fixed & reliable multi-select by index.
     * @param dropdownToggle The element that opens the multiselect panel.
     * @param optionsLocator Locator that targets each option element (li).
     * @param indices 1-based indices of items to toggle/select.
     */
    public void selectMultipleByIndex(WebElement dropdownToggle, By optionsLocator, List<Integer> indices) {
        wait.until(ExpectedConditions.elementToBeClickable(dropdownToggle)).click();
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(optionsLocator));

        for (Integer idx : indices) {
            if (idx == null || idx < 1 || idx > options.size()) continue;
            WebElement target = options.get(idx - 1);
            clickSafely(target);
        }
        // keep panel open/closed as caller expects; if you need to close:
        // dropdownToggle.click();
    }

    /**
     * Backwards-compatible signature you already had. Assumes PrimeNG multiselect.
     * `elementName` not used (kept for signature compatibility).
     */
    public void selectMultipleByIndex(WebElement dropdownLocator, List<Integer> indices, String elementName) {
        selectMultipleByIndex(dropdownLocator, DEFAULT_MULTI_OPTIONS, indices);
    }

    /** Multi-select by visible text. */
    public void selectMultipleByText(WebElement dropdownToggle, By optionsLocator, List<String> texts) {
        wait.until(ExpectedConditions.elementToBeClickable(dropdownToggle)).click();
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(optionsLocator));

        for (String t : texts) {
            if (t == null) continue;
            WebElement match = options.stream()
                    .filter(o -> t.equalsIgnoreCase(o.getText().trim()))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Option not found: " + t));
            clickSafely(match);
        }
    }

    // -----------------------
    // Utilities
    // -----------------------

    public boolean isOptionPresent(WebElement element, String visibleText) {
        return new Select(element).getOptions().stream()
                .anyMatch(o -> visibleText.equals(o.getText().trim()));
    }

    public List<String> getVisibleOptions(By optionsLocator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(optionsLocator))
                .stream().map(WebElement::getText).map(String::trim).collect(Collectors.toList());
    }

    /** Safe click with tiny retry for stale elements. */
    private void clickSafely(WebElement el) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
        } catch (StaleElementReferenceException sere) {
            // one quick retry
            el.click();
        }
    }
}
