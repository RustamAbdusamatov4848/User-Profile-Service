package com.iprody.e2e.stepdefinitions.user_contacts;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openapitools.client.api.UserControllerApi;
import org.openapitools.client.model.UserContactDto;
import org.openapitools.client.model.UserDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FindUserContactsStep {
    private final UserControllerApi userControllerApi;
    private ResponseEntity<UserContactDto> responseEntity;
    private HttpStatusCode statusCode;
    private UserDto userDto;

    public FindUserContactsStep(UserControllerApi userControllerApi) {
        this.userControllerApi = userControllerApi;
    }

    @When("a client want to operate over user with id {long}")
    public void aClientWantToOperateOverUser(Long userId) {
        userDto = userControllerApi.getUserById(userId);
    }

    @When("a client wants to find its contacts")
    public void aClientWantsToFindItsContactsWithExistingId() {
        Long userId = userDto.getId();
        responseEntity = userControllerApi.getUserContactByUserIdWithHttpInfo(userId);
        statusCode = responseEntity.getStatusCode();
    }

    @When("a client want to operate over non-existing user with id {long}")
    public void aClientWantToOperateOverNonExistingUser(Long userId){
        try {
            userDto = userControllerApi.getUserById(userId);
        } catch (RestClientResponseException e) {
            statusCode = e.getStatusCode();
            userDto = null;
        }
    }

    @Then("response code for find user contacts is {int}")
    public void responseCodeForSuccessfulSearchIs200(int requiredStatusCode) {
        assertEquals(requiredStatusCode, statusCode.value());
    }

    @Then("response body contains found user contacts")
    public void responseBodyContainsFoundUserContacts() {
        UserContactDto foundEntity = responseEntity.getBody();
        assertNotNull(foundEntity);
        assertNotNull(foundEntity.getId());
    }

    @Then("its contacts is non-exist")
    public void itsContactsIsNonExist(){
        assertNull(userDto);
    }
}
