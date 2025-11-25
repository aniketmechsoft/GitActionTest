package com.uiFramework.Genesis.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Duration;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public abstract class AppiumUtils {

    public AppiumDriverLocalService service;

    /* ---------------------- UTILITIES ---------------------- */

    public Double getFormattedAmount(String amount) {
        // e.g. "$123.45" or "₹123.45" -> 123.45
        String digits = amount.replaceAll("[^0-9.]", "");
        return Double.parseDouble(digits);
    }

    public List<HashMap<String, String>> getJsonData(String jsonFilePath) throws IOException {
        String jsonContent = FileUtils.readFileToString(new File(jsonFilePath), StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {});
    }

    public void waitForElementToAppear(WebElement ele, AppiumDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(ele));
    }

    public String getScreenshotPath(String testCaseName, WebDriver driver) throws IOException {
        File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path dest = Paths.get(System.getProperty("user.dir"), "reports", testCaseName + ".png");
        Files.copy(source.toPath(), dest);
        return dest.toString();
    }

    /* ----------------- APPIUM SERVER HANDLING ----------------- */

    /**
     * Start Appium server cross-platform.
     * Strategy:
     * 1) If system properties/env vars specify paths -> use them
     * 2) Else try to discover with `npm root -g` -> <root>/appium/build/lib/main.js
     * 3) If discovery fails, throw with friendly hint
     *
     * To override:
     *  -Dappium.main.js=C:\\Users\\you\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js
     *  -Dnode.binary=C:\\Program Files\\nodejs\\node.exe
     * or env:
     *  APPIUM_MAIN_JS=..., NODE_BINARY=...
     */
    public AppiumDriverLocalService startAppiumServer(String ipAddress, int port) {
        File appiumJS = resolveAppiumMainJs();
        File nodeBinary = resolveNodeBinary();

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress(ipAddress)
                .usingPort(port);

        if (nodeBinary != null) {
            builder = builder.usingDriverExecutable(nodeBinary);
        }
        if (appiumJS != null) {
            builder = builder.withAppiumJS(appiumJS);
        }

        service = AppiumDriverLocalService.buildService(builder);
        service.start();

        // tiny buffer helps WDA/UiAutomator2 on cold start
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        return service;
    }

    /**
     * Prefer connecting to an already running Appium server (recommended on Windows).
     * Example:
     *   Manually start:  appium --base-path / --port 4723
     *   Then call new URL("http://127.0.0.1:4723/")
     * Use this method if you don't want Java to spawn Appium.
     */
    public boolean isServerRunning() {
        return service != null && service.isRunning();
    }

    public void stopAppiumServer() {
        if (service != null) {
            service.stop();
        }
    }

    /* ----------------- PATH RESOLUTION HELPERS ----------------- */

    private File resolveAppiumMainJs() {
        // 1) JVM prop
        String p = System.getProperty("appium.main.js");
        if (nonEmpty(p) && new File(p).exists()) return new File(p);

        // 2) Env var
        p = System.getenv("APPIUM_MAIN_JS");
        if (nonEmpty(p) && new File(p).exists()) return new File(p);

        // 3) Discover via `npm root -g`
        String npmRoot = runAndCapture(new String[]{"npm", "root", "-g"});
        if (nonEmpty(npmRoot)) {
            npmRoot = npmRoot.trim();
            // Build the candidate path cross-platform
            Path candidate = Paths.get(npmRoot, "appium", "build", "lib", "main.js");
            if (Files.exists(candidate)) return candidate.toFile();
        }

        // 4) Common fallbacks per OS
        if (isMac()) {
            Path nvmMain = Paths.get(System.getProperty("user.home"),
                    ".nvm", "versions", "node", "v20.19.5", "lib", "node_modules", "appium", "build", "lib", "main.js");
            if (Files.exists(nvmMain)) return nvmMain.toFile();
        } else if (isWindows()) {
            // Typical Roaming npm global root
            Path roaming = Paths.get(System.getProperty("user.home"),
                    "AppData", "Roaming", "npm", "node_modules", "appium", "build", "lib", "main.js");
            if (Files.exists(roaming)) return roaming.toFile();
        }

        // If we got here, we couldn't find it; let builder still work if Appium is on PATH,
        // otherwise callers will get a clear exception.
        return null;
    }

    private File resolveNodeBinary() {
        // 1) JVM prop
        String p = System.getProperty("node.binary");
        if (nonEmpty(p) && new File(p).exists()) return new File(p);

        // 2) Env var
        p = System.getenv("NODE_BINARY");
        if (nonEmpty(p) && new File(p).exists()) return new File(p);

        // 3) OS-specific common paths
        if (isMac()) {
            Path nvmNode = Paths.get(System.getProperty("user.home"),
                    ".nvm", "versions", "node", "v20.19.5", "bin", "node");
            if (Files.exists(nvmNode)) return nvmNode.toFile();
        } else if (isWindows()) {
            // nvm-windows symlink target is usually in Program Files\nodejs
            Path programFilesNode = Paths.get(System.getenv().getOrDefault("ProgramFiles", "C:\\Program Files"),
                    "nodejs", "node.exe");
            if (Files.exists(programFilesNode)) return programFilesNode.toFile();

            // Or inside user profile if Node was installed per-user
            Path userNode = Paths.get(System.getProperty("user.home"),
                    "AppData", "Local", "Programs", "nodejs", "node.exe");
            if (Files.exists(userNode)) return userNode.toFile();
        }

        // 4) Last resort: return null to let AppiumServiceBuilder try PATH-resolved "node"
        return null;
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win");
    }
    private static boolean isMac() {
        return System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac");
    }
    private static boolean nonEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static String runAndCapture(String[] cmd) {
        try {
            Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
                String out = br.readLine();
                p.waitFor();
                return out == null ? "" : out;
            }
        } catch (Exception e) {
            return "";
        }
    }
}
