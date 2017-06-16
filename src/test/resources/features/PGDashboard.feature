#@smoke

Feature: Property Guru Dashboard

  Scenario: Verify Property Guru Dashboard

    Given Property guru dashboard launch
    And I verify Residential title
    And I verify New Launches title
    And I verify Condo Directory title
    And I verify Commercial title
