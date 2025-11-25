package com.uiFramework.Genesis.common;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonMethods {
	WebDriver driver;

	public CommonMethods(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * This method logins to the application
	 * 
	 * @param driver
	 * @param number
	 * @param pass
	 */
	public void loginToApplication(WebDriver driver, String username, String pass) throws InterruptedException {
		driver.findElement(By.name("userName")).sendKeys(username);
		driver.findElement(By.name("password")).sendKeys(pass);

		WebElement signInButton = driver.findElement(By.xpath("//button[text()='SIGN IN']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", signInButton);

	}

//method to scroll using action class
	public void scrollToElementUsingActions(WebElement element) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element).perform();
	}

	public void scrolldown(WebDriver driver) {
		try {
			Thread.sleep(2000);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("scrollBy(0, 1200)");
			Thread.sleep(2000);
		} catch (Exception e) {
		}
	}

//method for data provider
	public List<HashMap<String, String>> getJsonData(String jsonFilePath) throws IOException {
		String jsonContent = FileUtils.readFileToString(new File(jsonFilePath), StandardCharsets.UTF_8);

		ObjectMapper mapper = new ObjectMapper();
		List<HashMap<String, String>> data = mapper.readValue(jsonContent,
				new TypeReference<List<HashMap<String, String>>>() {
				});

		return data;

	}

	// method to validate data in i symbol
	public static Map<String, String> getXPathSet(String fieldName, String filePath) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Map<String, String>> allFields;
		try {
			allFields = mapper.readValue(new File(filePath), new TypeReference<Map<String, Map<String, String>>>() {
			});
			return allFields.getOrDefault(fieldName, Collections.emptyMap());
		} catch (IOException e) {
			throw new RuntimeException("Error reading xpaths.json for " + fieldName, e);
		}
	}

	/**
	 * Method returns alpha numeric string of mentioned length
	 * 
	 * @param n
	 * @return
	 */
	public String getAlphaNumericString(int n) {

		// chose a Character random from this String
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

		// create StringBuffer size of AlphaNumericString
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {

			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int) (AlphaNumericString.length() * Math.random());

			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		}

		return sb.toString();
	}

	public String getNumericString1(int n) {
		// chose a Character random from this String
		String AlphaNumericString = "123456789";
		// create StringBuffer size of AlphaNumericString
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int) (AlphaNumericString.length() * Math.random());
			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}

	public String getNumericString(int n) {
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			int digit = ThreadLocalRandom.current().nextInt(1, 10);
			sb.append(digit);
		}
		return sb.toString();

	}

	public String getcharacterString(int n) {

		// chose a Character random from this String
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz";
		;

		// create StringBuffer size of AlphaNumericString
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {

			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int) (AlphaNumericString.length() * Math.random());

			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		}

		return sb.toString();
	}

}
