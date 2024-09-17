package com.iprody.e2e.stepdefinitions.user;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openapitools.client.api.UserControllerApi;
import org.openapitools.client.model.Role;
import org.openapitools.client.model.UserDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetUsersByRoleStep {
    private final UserControllerApi userControllerApi;
    private HttpStatusCode statusCode;
    private ResponseEntity<List<UserDto>> responseEntity;

    public GetUsersByRoleStep(UserControllerApi userControllerApi) {
        this.userControllerApi = userControllerApi;
    }

    @When("a client want to get all users with role {string}")
    public void aClientWantToGetAllUsersWithSpecifiedRole(String role) {
        responseEntity = userControllerApi.getUsersByRoleWithHttpInfo(Role.valueOf(role));
        statusCode = responseEntity.getStatusCode();
    }

    @Then("response code for success found users with specified role is {int}")
    public void responseCodeForSuccessFoundUsers(int requiredStatusCode) {
        assertEquals(requiredStatusCode, statusCode.value());
    }

    @Then("response body contains all users with specified role")
    public void responseBodyContainsAllUsersWithSpecifiedRole() {
        List<UserDto> userDtoList = responseEntity.getBody();
        assertNotNull(userDtoList);
        assertTrue(userDtoList.stream()
                .allMatch(userDto -> Role.MANAGER.equals(userDto.getUserRole())));
    }
}
