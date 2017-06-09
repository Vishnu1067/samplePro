package com.implementation;


import com.cucumber.listener.ExtentCucumberFormatter;
import com.mobile.appium.manager.AppiumDriverManager;
import com.pageobjects.Login;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.junit.Assert;


public class StepDefs extends ExtentCucumberFormatter {

    Login login;

    public StepDefs() {
        super();
        login = new Login(AppiumDriverManager.getDriver());
    }


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

        Assert.assertFalse("Login Message is displayed", login.isDisplayUserURL());
    }

    @And("^I should see Welcome page with user URL$")
    public void verifyDisplayURL() {

        Assert.assertTrue("Login Message is not displayed", login.isDisplayUserURL());
    }
}
