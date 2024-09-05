package com.iprody.e2e.stepdefinitions;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import org.springframework.http.HttpStatus;

public class CommonStep {

    @Given("UP Service is up and running")
    public void upServiceIsUpAndRunning() {
        RestAssured.given()
                .get("http://localhost:8080/user-profile-metrics/health")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Given("User Endpoint available")
    public void userEndpointAvailable() {
        RestAssured.given()
                .get("http://localhost:8080/user-profile-metrics/mappings")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

}
