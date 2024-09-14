Feature: Update Contacts Test

  Scenario: Update User successfully
    Given UP Service is up and running
    And User Endpoint available
    When a client want to operate over user with 1
    And a client wants to update its contacts
    Then response code for update user contacts is 204
    And response body contains user with successfully updated contacts

  Scenario: Client error while updating a user with invalid parameters
    Given UP Service is up and running
    And User Endpoint available
    When a client want to operate over user with 1
    And a client wants to update its contacts with invalid parameters
    Then response code for update user contacts is 400

