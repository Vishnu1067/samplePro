package com.implementation;


import com.cucumber.listener.ExtentCucumberFormatter;
import com.pageobjects.Login;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.HashMap;

import static com.mobile.appium.manager.AppiumDriverManager.getDriver;


public class StepDefs extends ExtentCucumberFormatter {

    Login login = new Login(getDriver());
    HashMap<String, String> userName = new HashMap<>();

    @Given("^I enter email \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void enterEmailAndPassword(String userName, String password) {
        login.typeUserName(userName);
        login.typePassword(password);

    }

    @When("^I click on Signin Button$")
    public void clickSignInButton() {
        login.clickSignIn();
    }

    @And("^I should not see Welcome page with user URL$")
    public void verifyNotDisplayURL() {

        Assert.assertFalse("Login Message is displayed", login.isDisplayUserURL(10));
    }

    @And("^I should see Welcome page with user URL$")
    public void verifyDisplayURL() {

        Assert.assertTrue("Login Message is not displayed", login.isDisplayUserURL(20));
    }

    @Given("^I click on Create New Account$")
    public void clickNewAccount() {
        login.clickCreateNewAccount();
    }

    @And("^I fill the registration form$")
    public void fillRegistration() {

        userName = login.registerAccount();
    }

    @Then("^I should not see Welcome page with URL for new user$")
    public void verifyAccount() {

//        try {
//            Thread.sleep(10000);
//        } catch (Exception e) {
//
//        }

        Assert.assertTrue("Login Message is not displayed", login.isDisplayUserURL(30));

        String userNmae = userName.get("userName") + ".wordpress.com";

        Assert.assertEquals(userNmae, login.getUserURL());
    }
}
