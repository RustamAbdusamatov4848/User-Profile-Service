package com.iprody.e2e.stepdefinitions.user;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openapitools.client.api.UserControllerApi;
import org.openapitools.client.model.UserDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateUserStep {
    private final UserControllerApi userControllerApi;
    private ResponseEntity<UserDto> responseEntity;
    private HttpStatusCode statusCode;
    private UserDto userDto;

    public UpdateUserStep(UserControllerApi userControllerApi) {
        this.userControllerApi = userControllerApi;
    }

    @When("a client wants to update a user with id {long}")
    public void aClientWantsToUpdateAUser(Long userId) {
        userDto = generateUniqueUserDtoForUpdate(userId);
        responseEntity = userControllerApi.updateUserWithHttpInfo(userDto);
        statusCode = responseEntity.getStatusCode();
    }

    @When("wants to update a user with invalid parameters")
    public void wantsToUpdateAUserWithInvalidParameters(DataTable userTableForUpdate) {
        List<Map<String, String>> list = userTableForUpdate.asMaps();
        Map<String, String> map = list.getFirst();

        userDto = new UserDto();
        userDto.setId(Long.valueOf(map.get("id")));
        userDto.setEmail(map.get("email"));

        try {
            responseEntity = userControllerApi.updateUserWithHttpInfo(userDto);
        } catch (RestClientResponseException e) {
            statusCode = e.getStatusCode();
        }
    }

    @When("a client wants to update a user with non-existing id {long}")
    public void aClientWantsToUpdateAUserWithNonExistingId(Long id) {
        userDto = generateUniqueUserDtoForUpdate(id);

        try {
            responseEntity = userControllerApi.updateUserWithHttpInfo(userDto);
        } catch (RestClientResponseException e) {
            statusCode = e.getStatusCode();
        }
    }

    @Then("response code for update user is {int}")
    public void responseCodeForUpdateUser(int requiredStatusCode) {
        assertEquals(requiredStatusCode, statusCode.value());
    }

    private UserDto generateUniqueUserDtoForUpdate(Long id) {
        UserDto user = new UserDto();
        user.setId(id);
        user.setFirstName("firstName" + System.currentTimeMillis());
        user.setLastName("lastName" + System.currentTimeMillis());
        user.setEmail(generateDistinctEmail());

        return user;
    }

    private static String generateDistinctEmail() {
        return "user" + System.currentTimeMillis() + "@test.com";
    }
}
