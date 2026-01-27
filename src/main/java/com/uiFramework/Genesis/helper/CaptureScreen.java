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

    public static File reportDirectory = new File(
            System.getProperty("user.dir")
            + File.separator + "target"
            + File.separator + "extent-reports"+ File.separator + "screenShots");

    public String getScreenShot(String fileName, WebDriver driver) {

        if (driver == null) {
            System.out.println("Driver is null, cannot take screenshot.");
            return null;
        }

        if (fileName == null || fileName.isEmpty()) {
            fileName = "blank";
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            if (!reportDirectory.exists()) {
                reportDirectory.mkdirs();
            }

            File destFile = new File(
                    reportDirectory + File.separator
                    + fileName + "_" + formatter.format(calendar.getTime()) + ".png");

            Files.copy(srcFile, destFile);

            // ✅ Relative path for Extent
            return "../screenShots/" + destFile.getName();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
