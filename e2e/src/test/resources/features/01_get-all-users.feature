Feature: Get All Users Test

  Scenario: Get all users successfully
    Given UP Service is up and running
    Given User Endpoint available
    When a client want to get all users
    Then response code for get all users success is 200
    Then response body contains all users
