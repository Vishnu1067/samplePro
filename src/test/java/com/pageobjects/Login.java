package com.pageobjects;

import com.mobile.appium.manager.AppiumDriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Login {

    public Login(AppiumDriver<MobileElement> driver) {

        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @AndroidFindBy(id = "org.wordpress.android:id/nux_username")
    private WebElement emailTextBox;

    @AndroidFindBy(id = "org.wordpress.android:id/nux_password")
    private WebElement passwordTextBox;

    @AndroidFindBy(id = "org.wordpress.android:id/nux_sign_in_button")
    private WebElement signInButton;

    @AndroidFindBy(id = "org.wordpress.android:id/my_site_subtitle_label")
    private WebElement userWordPressURL;


    public void typeUserName(String userName) {
        emailTextBox.clear();
        emailTextBox.sendKeys(userName);
    }

    public void typePassword(String password) {
        passwordTextBox.clear();
        passwordTextBox.sendKeys(password);
    }

    public void clickSignIn() {

        signInButton.click();
    }

    public String getUserURL() {

        return userWordPressURL.getText();
    }

    public boolean isDisplayUserURL() {

        boolean isURLDisplay;

        try {
            WebDriverWait wait = new WebDriverWait(AppiumDriverManager.getDriver(), 20);
            wait.until(ExpectedConditions.visibilityOf(userWordPressURL));
            isURLDisplay = true;
        } catch (Exception e) {
            isURLDisplay = false;
        }

        return isURLDisplay;
    }

}

