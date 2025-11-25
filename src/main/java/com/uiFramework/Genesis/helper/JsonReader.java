package com.uiFramework.Genesis.helper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
public class JsonReader {

  //method for validating multilple data
    public static JSONArray getFilterData(String filePath, String pageName) {
        JSONArray filtersArray = null;
        try {
           
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));

           
            JSONObject rootObject = new JSONObject(jsonContent);

          
            if (rootObject.has("pages") && rootObject.getJSONObject("pages").has(pageName)) {
           
                JSONObject pageData = rootObject.getJSONObject("pages").getJSONObject(pageName);

                
                filtersArray = pageData.getJSONArray("columns");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return filtersArray;
    }
   
}

