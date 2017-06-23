package com.pageobjects;

import com.mobile.report.factory.ExtentTestManager;
import com.mobile.utils.ConfigurationManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static com.mobile.appium.manager.AppiumDriverManager.getDriver;

public class PGSearchResidential {

    AppiumDriver<MobileElement> driver;
    ConfigurationManager prop;

    public PGSearchResidential(AppiumDriver<MobileElement> driver) {

        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        this.driver = driver;
        prop = ConfigurationManager.getInstance();
    }

    @AndroidFindBy(xpath = "//*[@class='android.support.v7.app.ActionBar$Tab' and @index='0']")
    @iOSFindBy(xpath = "//*[@name='SearchFormHeaderView']/XCUIElementTypeOther[1]")
    private WebElement rentTab;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/location_text")
    @iOSFindBy(id = "Enter MRT, location or property name")
    private WebElement locationTextBox;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/edit_text")
    @iOSFindBy(id = "Enter MRT, location or property name")
    private WebElement locationSearchTextBox;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/button")
    @iOSFindBy(xpath = "//XCUIElementTypeApplication[1]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]" +
            "/XCUIElementTypeOther[1]/XCUIElementTypeOther[3]/XCUIElementTypeTable[1]/XCUIElementTypeCell[3]/XCUIElementTypeStaticText[2]")
    private List<WebElement> typeTextBox;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/button")
    @iOSFindBy(xpath = "//XCUIElementTypeApplication[1]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]" +
            "/XCUIElementTypeOther[1]/XCUIElementTypeOther[3]/XCUIElementTypeTable[1]/XCUIElementTypeCell[4]/XCUIElementTypeStaticText[2]")
    private List<WebElement> subTypeTextBox;

    @AndroidFindBy(id = "android:id/text1")
    private List<WebElement> popupListTypes;

    @AndroidFindBy(id = "android:id/button1")
    @iOSFindBy(xpath = "//*[@name='Done']")
    private WebElement okButton;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/np_from")
    private WebElement priceFrom;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/np_to")
    private WebElement priceTo;

    @AndroidFindBy(xpath = "//*[@id='com.allproperty.android.consumer.sg.debug:id/button' and @text()='Price Range']")
    private WebElement priceRange;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/btnSearch")
    @iOSFindBy(xpath = "//*[@name='SearchButton']")
    private WebElement searchButton;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/done_button")
    @iOSFindBy(xpath = "//*[@name='Done']")
    private WebElement doneButton;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/district_button")
    @iOSFindBy(id = "Search By Districts")
    private WebElement searchByDistrict;

    @AndroidFindBy(xpath = "//*[@resource-id='com.allproperty.android.consumer.sg.debug:id/header_title' and @index='0']")
    @iOSFindBy(xpath = "//XCUIElementTypeApplication[1]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]" +
            "/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[3]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeStaticText[1]")
    private WebElement propertiesCount;


    public void clickBuyTab() {

        waitForElement(rentTab, 20);
        rentTab.click();
    }


    public void locationByDistrict(String location) {

        locationTextBox.click();
        searchByDistrict.click();
        clickExpectedList(location);
        doneButton.click();
    }

    public void selectType(String type) {

        clickType();
        clickExpectedList(type);

        if (prop.getProperty("Platform").equalsIgnoreCase("ios")) {
            doneButton.click();
        }
    }

    public void selectSubType(String type) {

        clickSubType();
        clickExpectedList(type);
        okButton.click();
    }

    public void clickSearchButton() {
        searchButton.click();
    }

    private void clickType() {

        if (prop.getProperty("Platform").equalsIgnoreCase("ios")) {
            driver.findElement(By.xpath("//*[@name='contentLabel']")).click();
        } else {
            for (WebElement typeSelect : typeTextBox) {

                if (typeSelect.getText().equalsIgnoreCase("Any Residential")) {
                    typeSelect.click();
                    break;
                }
            }
        }
    }

    private void clickSubType() {

        if (prop.getProperty("Platform").equalsIgnoreCase("ios")) {
            driver.findElement(By.xpath("//*[@name='mainTableView']/XCUIElementTypeCell[4]/XCUIElementTypeStaticText[2]")).click();
        } else {
            for (WebElement typeSelect : subTypeTextBox) {

                if (typeSelect.getText().equalsIgnoreCase("Any")) {
                    typeSelect.click();
                    break;
                }
            }
        }
    }

    private void clickExpectedList(String type) {

        if (prop.getProperty("Platform").equalsIgnoreCase("ios")) {
            driver.findElement(By.xpath("//*[@name='"+type+"']")).click();

        } else {
            for (WebElement selectType : popupListTypes) {

                if (selectType.getText().equalsIgnoreCase(type)) {
                    selectType.click();
                    break;
                }
            }
        }
    }

    public void propertiesCount() {
        waitForElement(propertiesCount, 30);
        ExtentTestManager.logger("Total Properties List : " + propertiesCount.getText());
    }

    public void waitForElement(WebElement element, int waitTime) {
        WebDriverWait webDriverWait = new WebDriverWait(getDriver(), waitTime);
        webDriverWait.until(ExpectedConditions.visibilityOf(element));
    }

}