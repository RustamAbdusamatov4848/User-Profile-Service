package com.iprody.e2e.stepdefinitions.user;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openapitools.client.api.UserControllerApi;
import org.openapitools.client.model.UserDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FindUserStep {
    private final UserControllerApi userControllerApi;
    private ResponseEntity<UserDto> responseEntity;
    private HttpStatusCode statusCode;

    public FindUserStep(UserControllerApi userControllerApi) {
        this.userControllerApi = userControllerApi;
    }

    @When("a client wants to find a user with existing id {long}")
    public void aClientWantsToFindAUserWithExistingId(Long id) {
        responseEntity = userControllerApi.getUserByIdWithHttpInfo(id);
        statusCode = responseEntity.getStatusCode();
    }

    @When("a client wants to find a user with non-existing id {long}")
    public void aClientWantsToFindAUserWithNonExistingId(Long id) {
        try {
            responseEntity = userControllerApi.getUserByIdWithHttpInfo(id);
        } catch (HttpClientErrorException e) {
            statusCode = e.getStatusCode();
        }
    }

    @Then("response code for existing user is {int}")
    public void responseCodeForExistingUser(int requiredStatusCode) {
        assertEquals(requiredStatusCode, statusCode.value());
    }

    @Then("response code for non-existing user is {int}")
    public void responseCodeForNonExistingUser(int requiredStatusCode) {
        assertEquals(requiredStatusCode, statusCode.value());
    }

    @Then("response body contains found user")
    public void responseBodyContainsFoundUser() {
        UserDto responseUserDto = responseEntity.getBody();
        assertNotNull(responseUserDto);
        assertNotNull(responseUserDto.getId());
        assertNotNull(responseUserDto.getUserContactId());
    }
}
