Feature: Update User Test

  Scenario: Update User Successfully
    Given UP Service is up and running
    Given User Endpoint available
    When a client wants to update a user with id 1
    Then response code for update user is 204

  Scenario: Client error while updating a user with invalid parameters
    Given UP Service is up and running
    And User Endpoint available
    When wants to update a user with invalid parameters
      | id | email         |
      | 1  | invalid-email |
    Then response code for update user is 400

  Scenario: Client error while updating non-existing user
    Given UP Service is up and running
    And User Endpoint available
    When a client wants to update a user with non-existing id 1000000
    Then response code for update user is 404
