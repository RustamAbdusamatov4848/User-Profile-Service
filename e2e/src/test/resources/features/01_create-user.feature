Feature: Create User Test

  Scenario: Create User Successfully
    Given UP Service is up and running
    Given User Endpoint available
    Given user with valid data
      | firstName | lastName | email   |
      | Pole      | Smith    | <email> |
    When a client wants to create a user
    Then response code for valid user is 201
    Then response body contains created user

  Scenario:  Client error while creating a user with invalid parameters
    Given UP Service is up and running
    Given User Endpoint available
    Given user with invalid data
      | firstName | lastName | email         |
      | Pole      | Smith    | invalid-email |
    When a client wants to create a user with invalid mandatory parameters
    Then response code for invalid user is 400
