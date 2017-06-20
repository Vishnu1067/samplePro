package com.cucumber.listener;

import com.aventstack.extentreports.Status;
import com.mobile.appium.manager.*;
import com.mobile.configuration.AndroidDeviceConfiguration;
import com.mobile.configuration.IOSDeviceConfiguration;
import com.mobile.report.factory.ExtentManager;
import com.mobile.report.factory.ExtentTestManager;
import com.mobile.utils.ConfigurationManager;
import com.mobile.utils.MobilePlatform;
import com.mobile.video.recoder.XpathXML;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static com.mobile.appium.manager.AppiumDriverManager.getDriver;


/**
 * Cucumber custom format listener which generates ExtentsReport html file
 */
public class ExtentCucumberFormatter implements Reporter, Formatter {

    private final DeviceAllocationManager deviceAllocationManager;
    public AppiumServerManager appiumServerManager;
    public AppiumDriverManager appiumDriverManager;
    public DeviceSingleton deviceSingleton;
    public ReportManager reportManager;
    public LinkedList<Step> testSteps;
    private AndroidDeviceConfiguration androidDevice;
    private IOSDeviceConfiguration iosDevice;
    public String deviceModel;
    public XpathXML xpathXML = new XpathXML();
    private ConfigurationManager prop;
    private String CI_BASE_URI = null;


    public ExtentCucumberFormatter() {

        reportManager = ReportManager.getInstance();
        appiumServerManager = new AppiumServerManager();
        appiumDriverManager = new AppiumDriverManager();
        deviceAllocationManager = DeviceAllocationManager.getInstance();
        deviceSingleton = DeviceSingleton.getInstance();
        iosDevice = new IOSDeviceConfiguration();
        androidDevice = new AndroidDeviceConfiguration();
        prop = ConfigurationManager.getInstance();

    }

    public void before(Match match, Result result) {
    }

    public void result(Result result) {

        if ("passed".equals(result.getStatus())) {

            reportManager.test.get().log(Status.PASS, testSteps.poll().getName());

        } else if ("failed".equals(result.getStatus())) {

            String failed_StepName = testSteps.poll().getName();
            reportManager.test.get().log(Status.FAIL, result.getErrorMessage());
            String context = getDriver().getContext();
            boolean contextChanged = false;

            if ("Android".equals(getDriver().getSessionDetails().get("platformName").toString())
                    && !"NATIVE_APP".equals(context)) {

                getDriver().context("NATIVE_APP");
                contextChanged = true;
            }
            File srcFile = (getDriver())
                    .getScreenshotAs(OutputType.FILE);

            if (contextChanged) {
                getDriver().context(context);
            }
            if (getDriver().getSessionDetails().get("platformName").toString().equals("Android")) {

                deviceModel = androidDevice.getDeviceModel();
                //screenShotAndFrame(failed_StepName, scrFile, "android");

            } else if (getDriver().getSessionDetails().get("platformName").toString().equals("iOS")) {

                deviceModel = iosDevice.getIOSDeviceProductTypeAndVersion();
                //screenShotAndFrame(failed_StepName, scrFile, "iPhone");
            }

            attachScreenShotToReport(srcFile, failed_StepName);

        } else if ("skipped".equals(result.getStatus())) {

            reportManager.test.get().log(Status.SKIP, testSteps.poll().getName());

        } else if ("undefined".equals(result.getStatus())) {

            reportManager.test.get().log(Status.WARNING, testSteps.poll().getName());
        }
    }

    public void after(Match match, Result result) {

    }

    public void match(Match match) {

    }

    public void embedding(String s, byte[] bytes) {
    }

    public void write(String s) {
        //ExtentTestManager.endTest(parent);
    }

    public void syntaxError(String s, String s1, List<String> list, String s2, Integer integer) {

    }

    public void uri(String s) {

    }


    public void feature(Feature feature) {

        String[] tagsArray = getTagArray(feature.getTags());
        String tags = String.join(",", tagsArray);

        if (prop.getProperty("RUNNER").equalsIgnoreCase("parallel")) {


            String deviceUDID = deviceAllocationManager.getNextAvailableDeviceId();
            String[] deviceThreadNumber = Thread.currentThread().getName().toString().split("_");

            try {

                if (prop.getProperty("CI_BASE_URI") != null) {
                    CI_BASE_URI = prop.getProperty("CI_BASE_URI").toString().trim();
                } else if (CI_BASE_URI == null || CI_BASE_URI.isEmpty()) {
                    CI_BASE_URI = System.getProperty("user.dir");
                }

                deviceAllocationManager.allocateDevice(
                        xpathXML.parseXML(Integer
                                .parseInt(deviceThreadNumber[1])),
                        deviceUDID);

                if (DeviceManager.getDeviceUDID() == null) {
                    System.out.println("No devices are free to run test or Failed to run test");
                }

                reportManager.createParentNodeExtent(feature.getName(), "")
                        .assignCategory(tags);
                appiumServerManager.startAppiumServer(feature.getName());

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            try {

                deviceAllocationManager.allocateDevice("", deviceSingleton.getDeviceUDID());
                reportManager.createParentNodeExtent(feature.getName(), "")
                        .assignCategory(tags);
                appiumServerManager.startAppiumServer(feature.getName());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String[] getTagArray(List<Tag> tags) {
        String[] tagArray = new String[tags.size()];
        for (int i = 0; i < tags.size(); i++) {
            tagArray[i] = tags.get(i).getName();
        }
        return tagArray;
    }

    public void scenarioOutline(ScenarioOutline scenarioOutline) {

    }

    public void examples(Examples examples) {

    }

    public void startOfScenarioLifeCycle(Scenario scenario) {
        createAppiumInstance(scenario);
        this.testSteps = new LinkedList<>();
    }

    public void createAppiumInstance(Scenario scenario) {

        String[] tagsArray = getTagArray(scenario.getTags());
        String tags = String.join(",", tagsArray);
        try {
            startAppiumServer(scenario, tags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startAppiumServer(Scenario scenario, String tags) {

        reportManager.createChildNodeWithCategory(scenario.getName(), tags);
        appiumDriverManager.startAppiumDriver();
    }

    public void background(Background background) {

    }

    public void scenario(Scenario scenario) {

    }

    public void step(Step step) {
        testSteps.add(step);
    }

    public void endOfScenarioLifeCycle(Scenario scenario) {

        //ExtentManager.getExtent().flush();
//        System.out.println("****** (endOfScenarioLifeCycle) Quit Driver Instance ******* " + getDriver().toString());
//        getDriver().quit();
//        appiumServerManager.stopAppiumServer();
    }

    public void done() {

    }

    public void close() {

    }

    public void eof() {

        ExtentManager.getExtent().flush();
        deviceAllocationManager.freeDevice();
        System.out.println("****** (eof) Quit Driver Instance ******* " + getDriver().toString());
        getDriver().quit();
        appiumServerManager.stopAppiumServer();
    }


//    public void screenShotAndFrame(String failed_StepName, File scrFile, String device) {
//
//        try {
//            File framePath =
//                    new File(System.getProperty("user.dir") + "/src/test/resources/frames/");
//
//            String destFile = System.getProperty("user.dir") + "/target/screenshot/"
//                    + device + "/"
//                    + DeviceManager.getDeviceUDID().replaceAll("\\W", "_") + "/"
//                    + deviceModel.replaceAll(" ", "_")
//                    + "/failed_" + failed_StepName.replaceAll(" ", "_") + ".jpeg";
//
//            FileUtils.copyFile(scrFile, new File(destFile));
//            File[] files1 = framePath.listFiles();
//            if (framePath.exists()) {
//                for (int i = 0; i < files1.length; i++) {
//                    if (files1[i].isFile()) { //this line weeds out other directories/folders
//                        Path p = Paths.get(files1[i].toString());
//                        String fileName = p.getFileName().toString().toLowerCase();
//                        if (deviceModel.toString().toLowerCase()
//                                .contains(fileName.split(".png")[0].toLowerCase())) {
//                            try {
//                                imageUtils.wrapDeviceFrames(
//                                        files1[i].toString(),
//                                        System.getProperty("user.dir")
//                                                + "/target/screenshot/" + device
//                                                + "/" + DeviceManager.getDeviceUDID()
//                                                .replaceAll("\\W", "_") + "/"
//                                                + deviceModel + "/failed_"
//                                                + failed_StepName.replaceAll(" ", "_") + ".jpeg",
//                                        System.getProperty("user.dir")
//                                                + "/target/screenshot/" + device
//                                                + "/" + DeviceManager.getDeviceUDID()
//                                                .replaceAll("\\W", "_") + "/"
//                                                + deviceModel + "/failed_"
//                                                + failed_StepName.replaceAll(" ", "_")
//                                                + "_framed.jpeg");
//                                break;
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            } catch (IM4JavaException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Resource Directory was not found");
//        }
//    }

    public void attachScreenShotToReport(File srcFile, String stepName) {

        try {
            String platform = null;

            if (DeviceManager.getMobilePlatform().equals(MobilePlatform.ANDROID)) {
                platform = "android";

            } else if (DeviceManager.getMobilePlatform().equals(MobilePlatform.IOS)) {
                platform = "iPhone";
            }

            String imagePath = System.getProperty("user.dir")
                    + "/target/screenshot/"
                    + platform + "/"
                    + DeviceManager.getDeviceUDID().replaceAll("\\W", "_") + "/"
                    + deviceModel.replaceAll(" ", "_")
                    + "/failed_" + stepName.replaceAll(" ", "_") + ".jpeg";

            FileUtils.copyFile(srcFile, new File(imagePath.trim()));

            reportManager.test.get().log(Status.INFO, "Fail : " + ExtentTestManager.getTest().addScreenCaptureFromPath(imagePath));

        } catch (Exception e) {

        }
    }

}
