package com.uiFramework.Genesis.browserConfiguration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class SafariDriverFactory {
	private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            try {
            	
                try {
                    Runtime.getRuntime().exec("killall safaridriver");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println("No zombie SafariDrivers running.");
                }

                Runtime.getRuntime().exec("safaridriver --enable");

                SafariOptions options = new SafariOptions();
                options.setCapability("safari:diagnose", true);  
                options.setCapability("safari:automaticInspection", false); 
                options.setUseTechnologyPreview(false);
                
                driver = new SafariDriver(options);

                driver.get("https://devwebdave.azurewebsites.net/");
                Thread.sleep(3000);
                
                driver.manage().window().fullscreen();

                Thread.sleep(3000);

                System.out.println("SafariDriver started successfully in full screen.");

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to start SafariDriver", e);
            }
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            System.out.println("SafariDriver closed.");
        }
    }

}
