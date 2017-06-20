@smoke_web

Feature: Property Guru AdminNet Mobile Web

  Scenario: Verify Property Guru AdminNet Login

    Given I launch Property guru AdminNet in mobile
    And I enter adminnet user name as "qa_admin_sg"
    And I enter adminnet password as "qa_gurus11"
    When I click on Sign in button in adminnet
    Then I should see AdminNet home page
