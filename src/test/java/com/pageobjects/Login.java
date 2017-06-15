package com.pageobjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import static com.mobile.appium.manager.AppiumDriverManager.getDriver;


public class Login {

    public Login(AppiumDriver<MobileElement> driver) {

        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @CacheLookup
    @AndroidFindBy(id = "org.wordpress.android:id/nux_username")
    private WebElement emailTextBox;

    @CacheLookup
    @AndroidFindBy(id = "org.wordpress.android:id/nux_password")
    private WebElement passwordTextBox;

    @CacheLookup
    @AndroidFindBy(xpath = ".//*[@text='Sign in']")
    private WebElement signInButton;

    @CacheLookup
    @AndroidFindBy(id = "org.wordpress.android:id/my_site_subtitle_label")
    private WebElement userWordPressURL;


    @AndroidFindBy(id = "org.wordpress.android:id/nux_create_account_button")
    private WebElement createNewAccount;

    @AndroidFindBy(id = "org.wordpress.android:id/email_address")
    private WebElement emailRegister;

    @AndroidFindBy(id = "org.wordpress.android:id/username")
    private WebElement userNameRegister;

    @AndroidFindBy(id = "org.wordpress.android:id/password")
    private WebElement passwordRegister;

    @AndroidFindBy(id = "org.wordpress.android:id/signup_button")
    private WebElement signUp;

    public void clickCreateNewAccount() {
        createNewAccount.click();
    }

    public HashMap<String, String> registerAccount() {

        String email = getEmailAddress();
        String userName = "vish" + getTimeStamp("HHmmsssss") + getRandomNumber(20, 2000);

        HashMap<String, String> emailUserName = new HashMap<>();


        // swipeUp(createNewAccount);

        emailUserName.put("email", email);
        emailUserName.put("userName", userName);

        emailRegister.clear();
        emailRegister.sendKeys(email);

        userNameRegister.clear();
        userNameRegister.sendKeys(userName);

        passwordRegister.clear();
        passwordRegister.sendKeys("Hello12345678");

        clickBackButton();
        signUp.click();

        return emailUserName;

    }

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

    public boolean isDisplayUserURL(int waitTime) {

        boolean isURLDisplay;

        try {
            waitForElement(userWordPressURL, waitTime);
            isURLDisplay = true;
        } catch (Exception e) {
            isURLDisplay = false;
        }

        return isURLDisplay;
    }

    public String userURL() {

        return userWordPressURL.getText();
    }


    private static String getEmailAddress() {

        String email = System.currentTimeMillis() + "@test.com";
        if (email.startsWith("0")) {
            email = email.replace("0", "1");
        }
        return email;
    }

    private static int getRandomNumber(int min, int max) {

        Random ran = new Random();
        int x = min + ran.nextInt(max);
        return x;

    }

    public void waitForElement(WebElement element, int waitTime) {
        WebDriverWait webDriverWait = new WebDriverWait(getDriver(), waitTime);
        webDriverWait.until(ExpectedConditions.visibilityOf(element));
    }

    public static String getTimeStamp(String format) {

        Date dt = new Date();
        String date = new SimpleDateFormat(format).format(dt);
        return date;

    }

    public void clickBackButton() {

        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }
        getDriver().navigate().back(); //Closes keyboard & Comes out of edit mode
    }

    public void waitForPageToLoad(WebElement id) {
        WebDriverWait wait = new WebDriverWait(getDriver(), 15);
        wait.until(ExpectedConditions.elementToBeClickable(id));
    }

    public static void swipeUp(WebElement element) {

        TouchAction action = new TouchAction(getDriver());
        action.press(0, 500)
                .waitAction(200)
                .moveTo(element)
                .release()
                .perform();


    }

}