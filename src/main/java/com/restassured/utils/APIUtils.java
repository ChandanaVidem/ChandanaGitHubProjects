package com.restassured.utils;

import com.restassured.baseactions.CommonActions;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APIUtils extends CommonActions {

    public static RequestSpecification requestSpecification;

    public static Response getPostResponse(String baseUrl, String basePath, String requestBody, String contentType) {
        requestSpecification = RestAssured.given().baseUri(baseUrl).
                basePath(basePath).body(requestBody).contentType(contentType);
        return requestSpecification.post();
    }
}
