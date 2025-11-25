package com.uiFramework.Genesis.helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelHelper {

    @DataProvider(name = "loginData")
    public Object[][] getExcelData() throws IOException {
        // Correct file path
        String excelLocation = "src/main/resources/configfile/testdata.xlsx";
        String sheetName = "Sheet1";

        Object[][] dataSets = null;

        try (FileInputStream file = new FileInputStream(new String(excelLocation));
             XSSFWorkbook workbook = new XSSFWorkbook(file)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            int totalRows = sheet.getLastRowNum();
            int totalColumns = sheet.getRow(0).getLastCellNum();

            dataSets = new Object[totalRows][totalColumns]; // Initialize dataSets

            for (int i = 1; i <= totalRows; i++) { // Start from row 1 (skip headers)
                Row row = sheet.getRow(i);
                for (int j = 0; j < totalColumns; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    switch (cell.getCellType()) {
                        case STRING:
                            dataSets[i - 1][j] = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            dataSets[i - 1][j] = cell.getNumericCellValue();
                            break;
                        case BOOLEAN:
                            dataSets[i - 1][j] = cell.getBooleanCellValue();
                            break;
                        default:
                            dataSets[i - 1][j] = ""; // For blank cells
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle IOException
        }

        return dataSets; // Return the 2D array to the DataProvider
    }
}
