Feature: Find Contacts Test

  Scenario: Find user successfully
    Given UP Service is up and running
    Given User Endpoint available
    When a client want to operate over user with id 1
    When a client wants to find its contacts
    Then response code for find user contacts is 200
    Then response body contains found user contacts

  Scenario: Client error while no user found
    Given UP Service is up and running
    And User Endpoint available
    When a client want to operate over non-existing user with id 10000000
    Then response code for find user contacts is 404
    Then its contacts is non-exist
