package com.pageobjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class Login {

    public Login(AppiumDriver<MobileElement> driver) {

        PageFactory.initElements(driver, this);

    }

    @AndroidFindBy(id = "org.wordpress.android:id/nux_username")
    private static WebElement emailTextBox;


    @AndroidFindBy(id = "org.wordpress.android:id/nux_password")
    private static WebElement passwordTextBox;

    @AndroidFindBy(id = "org.wordpress.android:id/nux_sign_in_button")
    private static WebElement signInButton;

    @AndroidFindBy(id = "org.wordpress.android:id/my_site_subtitle_label")
    private static WebElement userWordPressURL;


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


}

