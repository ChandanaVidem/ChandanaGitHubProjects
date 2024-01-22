package com.restassured.baseactions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CommonActions extends Base {
    public String readJsonFromFile(String path) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(path));
        String jsonRequest = obj.toString();
        JSONObject json = new JSONObject(jsonRequest);
        return json.toString();
    }

    public String updateJsonObject(JSONObject JsonRequestBody, HashMap<String, Object> keysTobeUpdated) throws Exception {
        for (Map.Entry<String, Object> map : keysTobeUpdated.entrySet()) {
            String keyString = map.getKey();
            // get the keys of json object
            Iterator iterator = JsonRequestBody.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                // if the key is a string, then update the value
                if ((JsonRequestBody.optJSONArray(key) == null) && (JsonRequestBody.optJSONObject(key) == null)) {
                    if ((key.equals(keyString))) {
                        // put new value
                        JsonRequestBody.put(key, map.getValue());
                    }
                }

                // if it's jsonobject
                if (JsonRequestBody.optJSONObject(key) != null) {
                    updateJsonObject(JsonRequestBody.getJSONObject(key), keysTobeUpdated);
                }

                // if it's jsonarray
                if (JsonRequestBody.optJSONArray(key) != null) {
                    JSONArray jArray = JsonRequestBody.getJSONArray(key);
                    for (int i = 0; i < jArray.length(); i++) {
                        for (String keyValues : keysTobeUpdated.keySet()) {
                            if (keyValues.contains("index" + i)) {
                                keysTobeUpdated.put(keyValues.replace("index" + i, ""), keysTobeUpdated.get(keyValues));
                            }
                        }
                        updateJsonObject(jArray.getJSONObject(i), keysTobeUpdated);
                    }
                }
            }
        }
        return JsonRequestBody.toString();

    }

    public static String updateJsonPropertiesWithKeyPath(String jsonString, Map<String, String> updates) {
        JSONObject jsonString1 = new JSONObject(jsonString);

        for (Map.Entry<String, String> entry : updates.entrySet()) {
            String keyPath = entry.getKey();
            String newValue = entry.getValue();

            // Split the key path into individual keys and array indices
            String[] keys = keyPath.split("\\.");
            int arrayIndex = -1;
            JSONObject jsonObject = jsonString1;

            for (int i = 0; i < keys.length; i++) {
                if (keys[i].contains("[")) {
                    // Extract the array index from the key
                    String[] parts = keys[i].split("\\[");
                    keys[i] = parts[0];
                    arrayIndex = Integer.parseInt(parts[1].substring(0, parts[1].length() - 1));
                }

                if (i < keys.length - 1) {
                    // Navigate through the keys to find the target object
                    if (arrayIndex != -1) {
                        JSONArray array = jsonObject.getJSONArray(keys[i]);
                        jsonObject = array.getJSONObject(arrayIndex);
                        arrayIndex = -1;
                    } else {
                        jsonObject = jsonObject.getJSONObject(keys[i]);
                    }
                } else {
                    // Update the target property with the new value
                    if (arrayIndex != -1) {
                        JSONArray array = jsonObject.getJSONArray(keys[i]);
                        array.getJSONObject(arrayIndex).put(keys[i], newValue);
                    } else {
                        jsonObject.put(keys[i], newValue);
                    }
                }
            }
        }

        // Convert the modified JSONObject back to a JSON string
        return jsonString1.toString(2);
    }
}
