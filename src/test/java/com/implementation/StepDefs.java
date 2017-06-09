package com.implementation;


import com.pageobjects.Login;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;


public class StepDefs {


    Login login = new Login(DriverManager.getDriver());

    @Given("^I enter email and password$")
    public void i_am_on_Testing() {
        login.typeUserName("testing@test");
        login.typePassword("test");

    }

    @And("^I should see error message as \"(.*)\"$")
    public void i_am_still_testing(String errorMessage) {
        System.out.println("I am still testing");
    }
}
