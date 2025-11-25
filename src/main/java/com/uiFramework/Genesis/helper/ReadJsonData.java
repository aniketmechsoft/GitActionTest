package com.uiFramework.Genesis.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class ReadJsonData {
    private static JsonNode jsonData; // Store JSON in memory

    // Load JSON once at startup
    public static void loadJson(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonData = objectMapper.readTree(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load JSON file: " + filePath);
        }
    }

    // Retrieve nested values dynamically, for handling cases where parent/child keys exist
    public static String getNestedValue(String parentKey, String childKey) {
        if (jsonData != null && jsonData.has(parentKey)) {
            JsonNode parentNode = jsonData.get(parentKey);
            if (parentNode.has(childKey)) {
                return parentNode.get(childKey).asText();
            } else {
                throw new RuntimeException("Child key not found: " + parentKey + "." + childKey);
            }
        } else {
            throw new RuntimeException("Parent key not found: " + parentKey);
        }
    }
}