package com.implementation;

import com.cucumber.listener.ExtentCucumberFormatter;
import com.pageobjects.PGAdminNet;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static com.mobile.appium.manager.AppiumDriverManager.getDriver;


public class StepWebPGAdminNetLogin extends ExtentCucumberFormatter {

    PGAdminNet adminNet = new PGAdminNet(getDriver());

    @Given("^I launch Property guru AdminNet in mobile$")
    public void launchAdminNet() {

        getDriver().get("http://admin.integration.propertyguru.com.sg");
    }

    @And("^I enter adminnet user name as \"([^\"]*)\"$")
    public void enterUserName(String userName) {

        adminNet.enterUserName(userName);
    }

    @And("^I enter adminnet password as \"([^\"]*)\"$")
    public void enterPassword(String password) {

        adminNet.enterPassword(password);
    }

    @When("^I click on Sign in button in adminnet$")
    public void clickSignIn() {

        adminNet.clickLoginButon();
    }

    @Then("^I should see AdminNet home page$")
    public void homePageAdminNet() {

        adminNet.waitForAdminNetHomePage();
    }


}
