//@smoke

Feature: Word Press Sample App

  Scenario: Login Invalid User name and Password

    Given I enter email "test@test.com" and password "123456"
    When I click on Signin Button
    And I should not see Welcome page with user URL
