package com.pageobjects;

import com.aventstack.extentreports.Status;
import com.mobile.appium.manager.ReportManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static com.mobile.appium.manager.AppiumDriverManager.getDriver;

public class PGSearchResidential {

    AppiumDriver<MobileElement> driver;
    ReportManager reportManager;

    public PGSearchResidential(AppiumDriver<MobileElement> driver) {

        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        this.driver = driver;
        reportManager = ReportManager.getInstance();
    }

    @AndroidFindBy(xpath = "//*[@class='android.support.v7.app.ActionBar$Tab' and @index='0']")
    private WebElement buyTab;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/location_text")
    private WebElement locationTextBox;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/edit_text")
    private WebElement locationSearchTextBox;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/condo_auto_suggest")
    private List<WebElement> suggestionsListSearch;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/button")
    private List<WebElement> typeTextBox;

    @AndroidFindBy(id = "android:id/text1")
    private List<WebElement> popupListTypes;

    @AndroidFindBy(id = "android:id/button1")
    private WebElement okButton;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/np_from")
    private WebElement priceFrom;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/np_to")
    private WebElement priceTo;

    @AndroidFindBy(xpath = "//*[@id='com.allproperty.android.consumer.sg.debug:id/button' and @text()='Price Range']")
    private WebElement priceRange;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/btnSearch")
    private WebElement searchButton;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/done_button")
    private WebElement doneButton;

    @AndroidFindBy(id = "com.allproperty.android.consumer.sg.debug:id/district_button")
    private WebElement searchByDistrict;

    @AndroidFindBy(xpath = "//*[@resource-id='com.allproperty.android.consumer.sg.debug:id/header_title' and @index='0']")
    private WebElement propertiesCount;

    public void clickBuyTab() {

        waitForElement(buyTab, 20);
        buyTab.click();
    }


    public void locationByDistrict(String location) {

        locationTextBox.click();
        searchByDistrict.click();
        clickExpectedList(location);
        doneButton.click();

    }

    public void typeAndSelectLocation(String location) {

        locationTextBox.click();
        locationSearchTextBox.clear();
        locationSearchTextBox.sendKeys(location);

        for (WebElement locationSearchSuggestion : suggestionsListSearch) {
            if (locationSearchSuggestion.getText().contains(location)) {
                locationSearchSuggestion.click();
                break;
            }
        }
    }

    public void selectType(String type) {

        clickType();
        clickExpectedList(type);
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

        for (WebElement typeSelect : typeTextBox) {

            if (typeSelect.getText().equalsIgnoreCase("Any Residential")) {
                typeSelect.click();
                break;
            }
        }
    }

    private void clickSubType() {

        for (WebElement typeSelect : typeTextBox) {

            if (typeSelect.getText().equalsIgnoreCase("Any")) {
                typeSelect.click();
                break;
            }
        }
    }

    private void clickExpectedList(String type) {

        for (WebElement selectType : popupListTypes) {

            if (selectType.getText().equalsIgnoreCase(type)) {
                selectType.click();
                break;
            }
        }
    }

    public void propertiesCount() {
        waitForElement(propertiesCount, 30);
        reportManager.test.get().log(Status.INFO, "Total Properties List : " + propertiesCount.getText());
    }

    public void waitForElement(WebElement element, int waitTime) {
        WebDriverWait webDriverWait = new WebDriverWait(getDriver(), waitTime);
        webDriverWait.until(ExpectedConditions.visibilityOf(element));
    }

}