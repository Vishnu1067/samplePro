package com.implementation;

import com.cucumber.listener.ExtentCucumberFormatter;
import com.pageobjects.PGDashboard;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

import static com.mobile.appium.manager.AppiumDriverManager.getDriver;


public class StepPGDashboard extends ExtentCucumberFormatter {

    PGDashboard propertyGuru = new PGDashboard(getDriver());

    @Given("^Property guru dashboard launch$")
    public void waitForDashboard() {

        propertyGuru.waitForPageToLoad(propertyGuru.residential(), 120);
    }

    @And("^I verify Residential title$")
    public void verifyResidential() {

        propertyGuru.isResidentialExist();
    }

    @And("^I verify New Launches title$")
    public void verifyNewLaunches() {

        propertyGuru.isNewLauncheslExist();
    }

    @And("^I verify Condo Directory title$")
    public void verifyCondoDirectory() {

        propertyGuru.isCondoDirectoryExist();
    }

    @And("^I verify Commercial title$")
    public void verifyCommercial() {

        propertyGuru.isCommercialExist();
    }


}
