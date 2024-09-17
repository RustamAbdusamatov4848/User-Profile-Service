Feature: Get users by role

  Scenario: Get all users with successfully
    Given UP Service is up and running
    Given User Endpoint available
    When a client want to get all users with role "MANAGER"
    Then response code for success found users with specified role is 200
    Then response body contains all users with specified role
