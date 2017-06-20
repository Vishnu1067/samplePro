package com.pageobjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.mobile.appium.manager.AppiumDriverManager.getDriver;

public class PGAdminNet {

    public PGAdminNet(AppiumDriver<MobileElement> driver) {

        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @FindBy(name = "userid")
    private WebElement userNameTextField;

    @FindBy(name = "password")
    private WebElement passwordTextField;

    @FindBy(name = "submit")
    private WebElement loginButton;

    @FindBy(name = "agent_dashboard")
    private static WebElement agentDashBoardForm;

    public void enterUserName(String username) {
        userNameTextField.clear();
        userNameTextField.sendKeys(username);
    }

    public void enterPassword(String password) {
        passwordTextField.clear();
        passwordTextField.sendKeys(password);
    }

    public void clickLoginButon() {

        loginButton.click();
    }

    public void waitForAdminNetHomePage() {

        waitForElementVisibility(agentDashBoardForm, 10);
    }

    public void waitForElementVisibility(WebElement id, int waitTime) {
        WebDriverWait wait = new WebDriverWait(getDriver(), waitTime);
        wait.until(ExpectedConditions.visibilityOf(id));
    }

}
