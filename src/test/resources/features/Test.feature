@smoke

Feature: Word Press Sample App

  Scenario: Login Invalid User name and Password

    Given I enter email and password
    And I should see error message as "The username or password you entered in incorrect"
