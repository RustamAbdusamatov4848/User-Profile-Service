Feature: Delete User Test

  Scenario: Delete User Successfully
    Given UP Service is up and running
    Given User Endpoint available
    Given an existing user
    When a client wants to delete a user with existing id
    Then response code for deleting user  is 204

  Scenario: Delete Non-existing User
    Given UP Service is up and running
    Given User Endpoint available
    When a client wants to delete a user with non-existing id 10000000
    Then response code for deleting user  is 404
