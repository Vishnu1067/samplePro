@smoke

Feature: Property Guru Search Residential Buy

  Scenario: Search Residential Buy

    Given Property guru dashboard launch
    Then I click on Residential button
    And I choose Buy option
    And I enter the location by District as "D05 Buona Vista / West Coast / Clementi New Town"
    And I select the type as "Apartment / Condo"
    And I select the sub type as "Condominium"
    #And I select the price range as "S$ 200,000" and "S$ 6M"
    When I click on Search button
    Then I should see the Property listing
