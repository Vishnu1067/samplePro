package com.mobile.appium.manager;

import com.annotation.Author;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.mobile.report.factory.ExtentTestManager;
import com.mobile.utils.GetDescriptionForChildNode;
import org.testng.IInvokedMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * ReportManager - Handles all Reporting activities e.g communication with ExtentManager, etc
 */
public class ReportManager {

    private DeviceManager deviceManager;
    public ThreadLocal<ExtentTest> parentTest = new ThreadLocal<>();
    public ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    public ExtentTest parent;
    public ExtentTest child;
    private GetDescriptionForChildNode getDescriptionForChildNode;
    public String category = null;


    public ReportManager() {
        deviceManager = new DeviceManager();
    }

    public ExtentTest createParentNodeExtent(String methodName, String testDescription)
            throws IOException, InterruptedException {

        parent = ExtentTestManager.createTest(methodName, testDescription,
                deviceManager.getDeviceName()
                        + "__" + DeviceManager.getDeviceUDID());
        parentTest.set(parent);

        ExtentTestManager.getTest().log(Status.INFO,
                "<a target=\"_parent\" href=" + "appiumlogs/"
                        + DeviceManager.getDeviceUDID().replaceAll("\\W", "_") + "__"
                        + methodName.replace(" ", "_")
                        + ".txt" + ">Appium Server Logs</a>");
        return parent;
    }

    public void setAuthorName(IInvokedMethod methodName) throws Exception {

        String authorName;
        boolean methodNamePresent;
        ArrayList<String> listeners = new ArrayList<>();
        String descriptionMethodName;
        String description = methodName.getTestMethod()
                .getConstructorOrMethod().getMethod()
                .getAnnotation(Test.class).description();
        getDescriptionForChildNode = new GetDescriptionForChildNode(methodName, description)
                .invoke();
        methodNamePresent = getDescriptionForChildNode.isMethodNamePresent();
        descriptionMethodName = getDescriptionForChildNode.getDescriptionMethodName();
        if (System.getProperty("os.name").toLowerCase().contains("mac")
                && System.getenv("Platform").equalsIgnoreCase("iOS")
                || System.getenv("Platform")
                .equalsIgnoreCase("Both")) {
            category = deviceManager.getDeviceCategory();
        } else {
            category = deviceManager.getDeviceModel();
        }
        if (methodNamePresent) {
            authorName = methodName.getTestMethod()
                    .getConstructorOrMethod().getMethod()
                    .getAnnotation(Author.class).name();
            Collections.addAll(listeners, authorName.split("\\s*,\\s*"));
            child = parentTest.get()
                    .createNode(descriptionMethodName,
                            category + "_" + DeviceManager.getDeviceUDID()).assignAuthor(
                            String.valueOf(listeners));
            test.set(child);
        } else {
            child = parentTest.get().createNode(descriptionMethodName,
                    category + "_" + DeviceManager.getDeviceUDID());
            test.set(child);
        }
    }

    public void createChildNodeWithCategory(String methodName, String tags) {

        child = parentTest.get().createNode(methodName,
                DeviceManager.getDeviceUDID()).assignCategory(tags);
        test.set(child);
    }
}
