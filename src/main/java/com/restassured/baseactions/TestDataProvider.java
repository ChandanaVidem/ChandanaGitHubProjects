package com.restassured.baseactions;

import org.testng.annotations.DataProvider;

public class TestDataProvider extends Base {
    @DataProvider
    public Object[][] getPetTestData() {
        Object[][] data = new Object[1][2];
        data[0] = new Object[]{"Shitzu", "Suzie", "Chikki Pet"};
        return data;
    }
}
