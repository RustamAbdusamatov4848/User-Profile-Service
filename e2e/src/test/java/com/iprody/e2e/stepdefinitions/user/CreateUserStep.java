package com.iprody.e2e.stepdefinitions.user;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openapitools.client.api.UserControllerApi;
import org.openapitools.client.model.UserDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateUserStep {
    private final UserControllerApi userControllerApi;
    private UserDto userDto;
    private ResponseEntity<UserDto> responseEntity;
    private HttpStatusCode statusCode;

    public CreateUserStep(UserControllerApi userControllerApi) {
        this.userControllerApi = userControllerApi;
    }

    @Given("user with valid data")
    public void userWithValidData(DataTable userTable) {
        userDto = mapToUserDto(userTable);
        userDto.setEmail(generateDistinctEmail());
    }

    @Given("user with invalid data")
    public void userWithInvalidData(DataTable userTable) {
        userDto = mapToUserDto(userTable);
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
        assertUserDtoResponse();
    }

    private UserDto mapToUserDto(DataTable userTable) {
        List<Map<String, String>> list = userTable.asMaps();
        Map<String, String> map = list.getFirst();

        UserDto user = new UserDto();
        user.setFirstName(map.get("firstName"));
        user.setLastName(map.get("lastName"));
        user.setEmail(map.get("email"));
        return user;
    }

    private static String generateDistinctEmail() {
        return "user" + System.currentTimeMillis() + "@test.com";
    }

    private void assertUserDtoResponse() {
        UserDto responseUserDto = responseEntity.getBody();
        assertNotNull(responseUserDto);
        assertNotNull(responseUserDto.getId());
        assertNotNull(responseUserDto.getUserContactId());
        assertEquals(userDto.getFirstName(), responseUserDto.getFirstName());
        assertEquals(userDto.getLastName(), responseUserDto.getLastName());
        assertEquals(userDto.getEmail(), responseUserDto.getEmail());
    }
}
