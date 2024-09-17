package com.iprody.e2e.stepdefinitions.user;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openapitools.client.api.UserControllerApi;
import org.openapitools.client.model.UserDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetAllUsersStep {
    private final UserControllerApi userControllerApi;
    private ResponseEntity<List<UserDto>> responseEntity;
    private HttpStatusCode statusCode;

    public GetAllUsersStep(UserControllerApi userControllerApi) {
        this.userControllerApi = userControllerApi;
    }

    @When("a client want to get all users")
    public void aClientWantToGetAllUsers() {
        responseEntity = userControllerApi.getAllUsersWithHttpInfo(0, 10);
        statusCode = responseEntity.getStatusCode();
    }

    @Then("response code for get all users success is {int}")
    public void responseCodeForGetAllUsers(int requiredStatusCode) {
        assertEquals(requiredStatusCode, statusCode.value());
    }

    @Then("response body contains all users")
    public void responseBodyContainsAllUsers() {
        List<UserDto> userDtoList = responseEntity.getBody();
        assertNotNull(userDtoList);
        assertFalse(userDtoList.isEmpty());
        assertEquals(userDtoList.size(), 10);
    }
}
