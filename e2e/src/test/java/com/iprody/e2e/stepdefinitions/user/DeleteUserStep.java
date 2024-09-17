package com.iprody.e2e.stepdefinitions.user;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openapitools.client.api.UserControllerApi;
import org.openapitools.client.model.Role;
import org.openapitools.client.model.UserDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteUserStep {
    private final UserControllerApi userControllerApi;
    private ResponseEntity<Void> responseEntity;
    private HttpStatusCode statusCode;
    private UserDto userDto;

    public DeleteUserStep(UserControllerApi userControllerApi) {
        this.userControllerApi = userControllerApi;
    }

    @Given("an existing user")
    public void anExistingUser() {
        userDto = generateUniqueUserDtoForDelete();
        userDto = userControllerApi.createUser(userDto);
    }

    @When("a client wants to delete a user with existing id")
    public void aClientWantsToDeleteAUserWithExistingId() {
        responseEntity = userControllerApi.deleteUserByIdWithHttpInfo(userDto.getId());
        statusCode = responseEntity.getStatusCode();
    }

    @When("a client wants to delete a user with non-existing id {long}")
    public void aClientWantsToDeleteAUserWithNonExistingId(Long id) {
        try {
            responseEntity = userControllerApi.deleteUserByIdWithHttpInfo(id);
        } catch (RestClientResponseException e) {
            statusCode = e.getStatusCode();
        }
    }

    @Then("response code for deleting user  is {int}")
    public void responseCodeForDeletingUser(int requiredStatusCode) {
        assertEquals(requiredStatusCode, statusCode.value());
    }

    private UserDto generateUniqueUserDtoForDelete() {
        UserDto user = new UserDto();
        user.setFirstName("firstName" + System.currentTimeMillis());
        user.setLastName("lastName" + System.currentTimeMillis());
        user.setEmail(generateDistinctEmail());
        user.setUserRole(Role.MANAGER);
        return user;
    }

    private static String generateDistinctEmail() {
        return "user" + System.currentTimeMillis() + "@test.com";
    }
}
