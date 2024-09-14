package com.iprody.e2e.stepdefinitions.user_contacts;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openapitools.client.api.UserControllerApi;
import org.openapitools.client.model.UserContactDto;
import org.openapitools.client.model.UserDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClientResponseException;

import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateContactsStep {
    private final UserControllerApi userControllerApi;
    private UserDto userDto;
    private UserContactDto userContactDto;
    private HttpStatusCode statusCode;

    public UpdateContactsStep(UserControllerApi userControllerApi) {
        this.userControllerApi = userControllerApi;
    }

    @When("a client want to operate over user with {long}")
    public void aClientWantToOperateOverUser(Long userId) {
        userDto = userControllerApi.getUserById(userId);
    }

    @When("a client wants to update its contacts")
    public void aClientWantsToUpdateItsContacts() {
        this.userContactDto = generateValidUserContactDto();
        statusCode = userControllerApi
                .updateUserContactByUserIdWithHttpInfo(userDto.getId(), userContactDto)
                .getStatusCode();
    }

    @When("a client wants to update its contacts with invalid parameters")
    public void aClientWantsToUpdateItsContactsWithInvalidParameters(){
        this.userContactDto = getInvalidUserContactDto();
        try {
            userControllerApi.updateUserContactByUserIdWithHttpInfo(userDto.getId(), userContactDto);
        } catch (RestClientResponseException e) {
            statusCode = e.getStatusCode();
        }
    }

    @Then("response code for update user contacts is {int}")
    public void responseCodeForUpdate(int requiredStatusCode) {
        assertEquals(requiredStatusCode, statusCode.value());
    }

    @Then("response body contains user with successfully updated contacts")
    public void responseBodyContainsUserWithSuccessfullyUpdatedContacts() {
        UserContactDto updatedUserContactDto = userControllerApi.getUserContactByUserId(userDto.getId());
        assertUpdateUserContacts(userContactDto, updatedUserContactDto);
    }

    private UserContactDto generateValidUserContactDto() {
        UserContactDto userContactDto = new UserContactDto();
        userContactDto.setId(userDto.getUserContactId());
        userContactDto.setTelegramId(generateTelegramId());
        userContactDto.setMobilePhone(generateMobilePhone());
        return userContactDto;
    }

    private UserContactDto getInvalidUserContactDto() {
        UserContactDto userContactDto = new UserContactDto();
        userContactDto.setId(userDto.getUserContactId());
        userContactDto.setTelegramId("user!$%^");
        userContactDto.setMobilePhone("01234");
        return userContactDto;
    }

    private void assertUpdateUserContacts(UserContactDto expected, UserContactDto actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTelegramId(), actual.getTelegramId());
        assertEquals(expected.getMobilePhone(), actual.getMobilePhone());
    }

    private String generateTelegramId() {
        String randomSuffix = UUID
                .randomUUID()
                .toString()
                .substring(0, 8)
                .replace("-", "");

        return "@user" + randomSuffix;
    }

    private String generateMobilePhone() {
        Random random = new Random();
        long number = 1000000000L + random.nextLong(9000000000L);
        return "+1" + number;
    }

}
