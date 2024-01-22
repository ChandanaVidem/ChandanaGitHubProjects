package com.restassured.tests;

import com.restassured.baseactions.APIEndpoints;
import com.restassured.baseactions.CommonActions;
import com.restassured.baseactions.TestDataProvider;
import com.restassured.utils.APIUtils;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;

public class PetStoreTests extends CommonActions {
    public long id;

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "getPetTestData", testName = "Create Pet with valid data", groups = "Smoke")
    public void createPet(String categoryName, String petName, String petTag) throws IOException, ParseException {
        HashMap<String, String> dataTobeUpdate = new HashMap<>();
        dataTobeUpdate.put("category.name", categoryName);
        dataTobeUpdate.put("tags[0].name", petTag);
        dataTobeUpdate.put("name", petName);
        String jsonRequest = readJsonFromFile("src/main/resources/CreatePetPayload.json");
        String updatedJsonRequest = updateJsonPropertiesWithKeyPath(jsonRequest, dataTobeUpdate);
        node.info("Json Request After Update is " + "\n" + updatedJsonRequest);
        Response response = APIUtils.getPostResponse(APIEndpoints.Base_URL, APIEndpoints.CREATE_PET_Endpoint,
                updatedJsonRequest, "application/json");
        Assert.assertEquals(response.getStatusCode(), 200);
        node.pass("Status code is verified " + response.getStatusCode());
        node.info(response.getBody().asString());
        JSONObject jsonResponse = new JSONObject(response.getBody().asString());
        if (jsonResponse.get("id") != null) {
            id = jsonResponse.getLong("id");
            node.pass("Id is " + id);
        } else {
            node.fail(" Test case failed");
        }

    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "getPetTestData", testName = "Get pet By Id", groups = "Smoke")
    public void getPetById(String categoryName, String petName, String petTag) throws IOException, ParseException {

    }
}
