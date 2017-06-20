@smoke_web

Feature: Property Guru AgentNet Mobile Web

  Scenario: Verify Property Guru AgentNet Login

    Given I launch Property guru AgentNet in mobile
    And I enter agentnet user name as "qacorporateservices@gmail.com"
    And I enter agentnet password as "qa_gurus11"
    When I click on Sign in button in agentnet
    Then I should see AgentNet home page
