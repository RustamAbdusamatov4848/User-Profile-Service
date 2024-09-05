package com.iprody.e2e.stepdefinitions;

import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openapitools.client.api.UserControllerApi;
import org.openapitools.client.model.UserDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class CreateUserStep {
    private final UserControllerApi userControllerApi;
    private UserDto userDto;
    private ResponseEntity<?> responseEntity;
    private HttpStatusCode statusCode;

    public CreateUserStep(UserControllerApi userControllerApi) {
        this.userControllerApi = userControllerApi;
    }

    @Given("customer with valid data")
    public void userWithValidData(UserDto userDto) {
        this.userDto = userDto;
    }

    @Given("customer with invalid data")
    public void userWithInvalidData(UserDto userDto) {
        this.userDto = userDto;
    }

    @When("a client wants to create a user")
    public void aClientWantsToCreateAUserWithMandatoryParameters() {
        responseEntity = userControllerApi.createUserWithHttpInfo(userDto);
        statusCode = responseEntity.getStatusCode();
    }

    @When("a client wants to create a user with invalid mandatory parameters")
    public void aClientWantsToCreateAUserWithInvalidMandatoryParameters() {
        try {
            responseEntity = userControllerApi.createUserWithHttpInfo(userDto);
        } catch (HttpClientErrorException e) {
            statusCode = e.getStatusCode();
        }
    }

    @Then("response code for valid user is {int}")
    public void responseCodeValidUser(int requiredStatusCode) {
        assertEquals(requiredStatusCode, statusCode.value());
    }

    @Then("response code for invalid user is {int}")
    public void responseCodeInvalidUser(int requiredStatusCode) {
        assertEquals(requiredStatusCode, statusCode.value());
    }

    @Then("response body contains created user")
    public void responseBody() {
        UserDto responseUserDto = (UserDto) responseEntity.getBody();
        assertNotNull(responseUserDto.getId());
        assertNotNull(responseUserDto.getUserContactId());
        assertEquals(userDto.getFirstName(), responseUserDto.getFirstName());
        assertEquals(userDto.getLastName(), responseUserDto.getLastName());
        assertEquals(userDto.getEmail(), responseUserDto.getEmail());
    }

    @DataTableType
    public UserDto validUserEntry(Map<String, String> entry) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(entry.get("firstName"));
        userDto.setLastName(entry.get("lastName"));
        String email = entry.get("email");
        if (email.contains("invalid")) {
            userDto.setEmail(email);
        } else {
            userDto.setEmail(generateDistinctEmail());
        }

        return userDto;
    }

    private static String generateDistinctEmail() {
        return "user" + System.currentTimeMillis() + "@test.com";
    }
}
