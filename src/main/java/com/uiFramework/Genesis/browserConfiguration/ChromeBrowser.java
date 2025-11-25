package com.uiFramework.Genesis.browserConfiguration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.util.HashMap;

public class ChromeBrowser {

    private ChromeOptions getChromeOptions() {
        
        String downloadDir = System.getProperty("user.dir") + "\\downloadedFiles";
        File downloadFolder = new File(downloadDir);
        if (!downloadFolder.exists()) {
            downloadFolder.mkdirs(); 
        }

        HashMap<String, Object> prefs = new HashMap<>();
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory", downloadDir);
        prefs.put("intl.accept_languages", "en");
        prefs.put("profile.default_content_setting_values.notifications", 2);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");

        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            options.addArguments("--headless", "--window-size=1920,1080", "--no-sandbox");
        }

        return options;
    }

    private WebDriver getChromeDriver(ChromeOptions options) {
        // No need to setSystemProperty anymore 🎉
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(options);
    }

    public static WebDriver getBrowserInstance() {
        ChromeBrowser obj = new ChromeBrowser();
        return obj.getChromeDriver(obj.getChromeOptions());
    }
}
