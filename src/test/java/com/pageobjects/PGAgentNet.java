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

public class PGAgentNet {

    public PGAgentNet(AppiumDriver<MobileElement> driver) {

        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @FindBy(id = "emailInput")
    private WebElement emailTextBox;

    @FindBy(id = "next")
    private WebElement nextButton;

    @FindBy(id = "inputPassword")
    private WebElement passwordTextButton;

    @FindBy(className = "agentnet")
    private WebElement agentNetHomePage;

    public void enterUserName(String username) {
        emailTextBox.clear();
        emailTextBox.sendKeys(username);
        nextButton.click();
    }

    public void enterPassword(String password) {
        passwordTextButton.clear();
        passwordTextButton.sendKeys(password);
    }

    public void clickLoginButton() {

        nextButton.click();
    }

    public void waitForAgentNetHomePage() {

        waitForElementVisibility(agentNetHomePage, 10);
    }

    public void waitForElementVisibility(WebElement id, int waitTime) {
        WebDriverWait wait = new WebDriverWait(getDriver(), waitTime);
        wait.until(ExpectedConditions.visibilityOf(id));
    }
}
