package com.implementation;

import com.cucumber.listener.ExtentCucumberFormatter;
import com.pageobjects.PGAgentNet;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static com.mobile.appium.manager.AppiumDriverManager.getDriver;


public class StepWebPGAgentNetLogin extends ExtentCucumberFormatter {

    PGAgentNet agentNet = new PGAgentNet(getDriver());

    @Given("^I launch Property guru AgentNet in mobile$")
    public void launchAdminNet() {

        getDriver().get("http://agentnet.staging.propertyguru.com.sg");
    }

    @And("^I enter agentnet user name as \"([^\"]*)\"$")
    public void enterUserName(String userName) {

        agentNet.enterUserName(userName);
    }

    @And("^I enter agentnet password as \"([^\"]*)\"$")
    public void enterPassword(String password) {

        agentNet.enterPassword(password);
    }

    @When("^I click on Sign in button in agentnet$")
    public void clickSignIn() {

        agentNet.clickLoginButton();
    }

    @Then("^I should see AgentNet home page$")
    public void homePageAdminNet() {

        agentNet.waitForAgentNetHomePage();
    }


}
