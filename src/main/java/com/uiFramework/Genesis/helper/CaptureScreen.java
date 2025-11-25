package com.uiFramework.Genesis.helper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.google.common.io.Files;

public class CaptureScreen {
    // Define the directory where screenshots will be stored
    public static File reportDirectory = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "screenShots");

    public String getScreenShot(String fileName, WebDriver driver) {
        if (driver == null) {
            System.out.println("Driver is null, cannot take screenshot.");
            return null;
        }

        if (fileName.isEmpty()) {  // Fixed string comparison
            fileName = "blank";
        }

        File destFile = null;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

        // Capture screenshot
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            // Ensure directory exists
            if (!reportDirectory.exists()) {
                reportDirectory.mkdirs();
            }

            // Create destination file
            destFile = new File(reportDirectory + File.separator + fileName + "_" + formatter.format(calendar.getTime()) + ".png");

            // Copy file correctly
            Files.copy(srcFile, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return destFile.toString();
    }
}
