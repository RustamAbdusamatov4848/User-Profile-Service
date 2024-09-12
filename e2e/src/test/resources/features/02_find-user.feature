Feature: Find User Test

  Scenario: Find user successfully
    Given UP Service is up and running
    And User Endpoint available
    When a client wants to find a user with existing id 1
    Then response code for existing user is 200
    Then response body contains found user

  Scenario: Client error while no user found
    Given UP Service is up and running
    And User Endpoint available
    When a client wants to find a user with non-existing id 1000000
    Then response code for non-existing user is 404
