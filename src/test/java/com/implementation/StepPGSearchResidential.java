package com.implementation;

import com.cucumber.listener.ExtentCucumberFormatter;
import com.pageobjects.PGDashboard;
import com.pageobjects.PGSearchResidential;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static com.mobile.appium.manager.AppiumDriverManager.getDriver;

public class StepPGSearchResidential extends ExtentCucumberFormatter {

    PGDashboard propertyGuru = new PGDashboard(getDriver());
    PGSearchResidential searchResidential = new PGSearchResidential(getDriver());

    @Then("^I click on Residential button$")
    public void clickResidential() {

        propertyGuru.residential().click();
    }

    @And("^I choose Buy option$")
    public void chooseBuy() {

        searchResidential.clickBuyTab();
    }

    @And("^I enter the location by District as \"([^\"]*)\"$")
    public void enterLocation(String location) {

        searchResidential.locationByDistrict(location);
    }

    @And("^I select the type as \"([^\"]*)\"$")
    public void selectType(String type) {

        searchResidential.selectType(type);
    }

    @And("^I select the sub type as \"([^\"]*)\"$")
    public void selectSubType(String type) {

        searchResidential.selectSubType(type);
    }

    @When("^I click on Search button$")
    public void clickSearchButton() {

        searchResidential.clickSearchButton();
    }

    @Then("^I should see the Property listing$")
    public void resultPropertyListing() throws Exception {

        searchResidential.propertiesCount();
    }


}
