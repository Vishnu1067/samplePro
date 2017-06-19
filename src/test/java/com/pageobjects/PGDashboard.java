package com.pageobjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.mobile.appium.manager.AppiumDriverManager.getDriver;

public class PGDashboard {

    public PGDashboard(AppiumDriver<MobileElement> driver) {

        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }


    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/residential_title")
    private WebElement residentialTitle;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/projects_title")
    private WebElement newLaunchesTitle;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/condo_title")
    private WebElement condoDirectory;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/commercial_title")
    private WebElement commercial;


    public WebElement residential() {
        return residentialTitle;
    }

    public void isResidentialExist() {

        Assert.assertTrue("Residential doesn't exist", residentialTitle.isDisplayed());

    }

    public void isNewLauncheslExist() {

        Assert.assertTrue("New Launches doesn't exist", newLaunchesTitle.isDisplayed());

    }

    public void isCondoDirectoryExist() {

        Assert.assertTrue("Condo Directory doesn't exist", condoDirectory.isDisplayed());

    }

    public void isCommercialExist() {

        Assert.assertTrue("Commercial doesn't exist", commercial.isDisplayed());

    }

    public void waitForPageToLoad(WebElement id, int waitTime) {
        WebDriverWait wait = new WebDriverWait(getDriver(), waitTime);
        wait.until(ExpectedConditions.elementToBeClickable(id));
    }

}
