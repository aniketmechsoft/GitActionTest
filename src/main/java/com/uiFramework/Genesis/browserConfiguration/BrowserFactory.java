package com.uiFramework.Genesis.browserConfiguration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class BrowserFactory extends SafariDriverFactory{

    private final String downloadDir;
    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public BrowserFactory() {
        Path dir = Paths.get(System.getProperty("user.dir"), "target", "downloads");
        try {
            Files.createDirectories(dir);
        } catch (Exception e) {
            System.out.println("Could not create download directory: " + e.getMessage());
        }
        this.downloadDir = dir.toAbsolutePath().toString();
    }

    private boolean isLinux() {
        return System.getProperty("os.name", "").toLowerCase().contains("linux");
    }

    private boolean isMac() {
        return System.getProperty("os.name", "").toLowerCase().contains("mac");
    }

    private boolean headless() {
        return Boolean.parseBoolean(System.getProperty("headless", "false"));
    }

    // -------------------- Chrome --------------------
    private ChromeOptions getChromeOptions() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory", downloadDir);
        prefs.put("download.directory_upgrade", true);
        prefs.put("intl.accept_languages", "en");
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
//        options.addArguments("--window-size=1366,768");
//        options.addArguments("--disable-popup-blocking");
//        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-allow-origins=*");

        if (isLinux() || headless()) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }
        return options;
    }

    // -------------------- Firefox --------------------
    private FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("browser.download.folderList", 2);
        options.addPreference("browser.download.dir", downloadDir);
        options.addPreference("browser.download.useDownloadDir", true);
        options.addPreference("browser.helperApps.neverAsk.saveToDisk",
                String.join(",",
                        "application/pdf",
                        "application/octet-stream",
                        "application/zip",
                        "text/csv",
                        "application/vnd.ms-excel",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        options.addPreference("pdfjs.disabled", true);
        options.addPreference("browser.download.manager.showWhenStarting", false);

        if (isLinux() || headless()) {
            options.addArguments("--headless");
        }
        return options;
    }

    // -------------------- Edge --------------------
    private EdgeOptions getEdgeOptions() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory", downloadDir);
        prefs.put("download.directory_upgrade", true);

        EdgeOptions options = new EdgeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--window-size=1366,768");

        if (isLinux() || headless()) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }
        return options;
    }

    // -------------------- Safari --------------------
    private SafariOptions getSafariOptions() {
        if (!isMac()) {
            throw new UnsupportedOperationException("SafariDriver only works on macOS.");
        }
        SafariOptions options = new SafariOptions();
        options.setCapability("safari:diagnose", true);
        options.setCapability("safari:automaticInspection", false);
        options.setUseTechnologyPreview(false);
        return options;
    }

    private WebDriver getSafariDriver() {
        try {
            Runtime.getRuntime().exec("killall safaridriver");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("No zombie SafariDrivers running.");
        }

        try {
            Runtime.getRuntime().exec("safaridriver --enable");
        } catch (Exception e) {
            System.out.println("SafariDriver enable command failed: " + e.getMessage());
        }

        WebDriver safariDriver = new SafariDriver(getSafariOptions());
        safariDriver.manage().window().fullscreen();
        new WebDriverWait(safariDriver, Duration.ofSeconds(5))
                .until(webDriver -> true); 
        System.out.println("SafariDriver started successfully in full screen.");
        
        return safariDriver;
    }

    // -------------------- Main Driver Factory --------------------
    public WebDriver createDriver(String browserName) {
        String b = browserName == null ? "" : browserName.toLowerCase();
        WebDriver driver;

        switch (b) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(getChromeOptions());
                driver.manage().window().maximize();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(getFirefoxOptions());
                driver.manage().window().maximize();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver(getEdgeOptions());
                driver.manage().window().maximize();
                break;
            case "safari":
               // driver = getSafariDriver();
            	driver = super.getDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().deleteAllCookies();
        driverThreadLocal.set(driver);
        return driver;
    }

    // -------------------- Public APIs --------------------
    public static WebDriver getDriver(String browserName) {
        BrowserFactory factory = new BrowserFactory();
        if (driverThreadLocal.get() == null) {
            factory.createDriver(browserName);
        }
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}

