package com.implementation;


import com.mobile.core.ExtentCucumberFormatter;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;


public class StepDefs extends ExtentCucumberFormatter {

    @Given("^I am on Testing$")
    public void i_am_on_Testing() {
        System.out.println("I am Testing");
    }

    @When("^I am still testing$")
    public void i_am_still_testing() {
        System.out.println("I am still testing");
    }
}
