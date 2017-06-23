package com.pageobjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
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
    @iOSFindBy(xpath = "//*[@name='PropertyGuru']/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]" +
            "/XCUIElementTypeOther[1]/XCUIElementTypeOther[2]/XCUIElementTypeCollectionView[1]/XCUIElementTypeCell[3]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]" +
            "/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeCollectionView[1]/XCUIElementTypeCell[1]")
    private WebElement residentialTitle;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/projects_title")
    @iOSFindBy(xpath = "//*[@name='PropertyGuru']/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]" +
            "/XCUIElementTypeOther[2]/XCUIElementTypeCollectionView[1]/XCUIElementTypeCell[3]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]" +
            "/XCUIElementTypeCollectionView[1]/XCUIElementTypeCell[2]")
    private WebElement newLaunchesTitle;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/condo_title")
    @iOSFindBy(xpath = "//*[@name='PropertyGuru']/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]" +
            "/XCUIElementTypeOther[1]/XCUIElementTypeOther[2]/XCUIElementTypeCollectionView[1]/XCUIElementTypeCell[3]/XCUIElementTypeOther[1]" +
            "/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeCollectionView[1]/XCUIElementTypeCell[3]")
    private WebElement condoDirectory;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/commercial_title")
    @iOSFindBy(xpath = "//*[@name='PropertyGuru']/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]" +
            "/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[2]/XCUIElementTypeCollectionView[1]/XCUIElementTypeCell[3]/XCUIElementTypeOther[1]" +
            "/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeCollectionView[1]/XCUIElementTypeCell[4]")
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
