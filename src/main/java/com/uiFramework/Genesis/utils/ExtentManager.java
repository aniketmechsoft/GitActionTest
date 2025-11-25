package com.uiFramework.Genesis.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentManager {
	
	private static final ThreadLocal<ExtentReports> extentThreadLocal = new ThreadLocal<>();

    private ExtentManager() {}

    public static ExtentReports getInstance(String platformName) {
        if (extentThreadLocal.get() == null) {
            synchronized (ExtentManager.class) {
                if (extentThreadLocal.get() == null) {
                    String reportPath = resolveReportPath(platformName);
                    ExtentReports extent = createInstance(reportPath, platformName);
                    extentThreadLocal.set(extent);
                }
            }
        }
        return extentThreadLocal.get();
    }

    private static ExtentReports createInstance(String fileName, String platformName) {
        ExtentSparkReporter spark = new ExtentSparkReporter(fileName);
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setDocumentTitle("Genesis Automation Suite");
        spark.config().setReportName("Automation Report - " + platformName.toUpperCase());
        spark.config().setEncoding("utf-8");

        ExtentReports er = new ExtentReports();
        er.attachReporter(spark);
        er.setSystemInfo("Platform", platformName);
        er.setSystemInfo("OS", System.getProperty("os.name"));
        er.setSystemInfo("Java Version", System.getProperty("java.version"));
        er.setSystemInfo("Tester", "Swapnil Shingare");
        return er;
    }

    private static String resolveReportPath(String platformName) {
        String ts = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now());
        Path baseDir = Paths.get(System.getProperty("user.dir"), "target", "extent-reports", platformName);
        ensureDirExists(baseDir);
        return baseDir.resolve("extent_" + ts + ".html").toString();
    }

    private static void ensureDirExists(Path dir) {
        try {
            Files.createDirectories(dir);
        } catch (IOException ignored) {}
    }

    public static void flushReport() {
        ExtentReports extent = extentThreadLocal.get();
        if (extent != null) {
            extent.flush();
            extentThreadLocal.remove();
        }
    }

}
