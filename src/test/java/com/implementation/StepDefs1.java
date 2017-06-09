package com.implementation;


import com.cucumber.listener.ExtentCucumberFormatter;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;


public class StepDefs1 extends ExtentCucumberFormatter {

    public StepDefs1() {
        super();
    }

    @Given("^I am on Testing 1$")
    public void i_am_on_Testing() {
        System.out.println("I am Testing");
    }

    @When("^I am still testing 1$")
    public void i_am_still_testing() {
        System.out.println("I am still testing");
    }
}
