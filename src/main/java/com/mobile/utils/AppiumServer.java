package com.mobile.utils;

import com.mobile.configuration.AndroidDeviceConfiguration;
import com.mobile.configuration.IOSDeviceConfiguration;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.remote.DesiredCapabilities;

public class AppiumServer {


    public String deviceUDID;
    public String category = null;


    private DeviceManager deviceManager;
    public IOSDeviceConfiguration iosDeviceConfiguration;
    public AndroidDeviceConfiguration androidDeviceConfiguration;
    public AppiumManager appiumManager;
    public ConfigurationManager properties;
    public AppiumDriver<MobileElement> driver = null;
    public DeviceCapabilityManager deviceCapabilityManager;
    public AvailablePorts ports;

    public AppiumServer() {

        iosDeviceConfiguration = new IOSDeviceConfiguration();
        androidDeviceConfiguration = new AndroidDeviceConfiguration();
        deviceManager = DeviceManager.getInstance();
        appiumManager = new AppiumManager();
        properties = ConfigurationManager.getInstance();
        deviceCapabilityManager = new DeviceCapabilityManager();
        ports = new AvailablePorts();

    }


    public synchronized AppiumServiceBuilder startAppiumServer(String deviceName) {

        if (deviceName.isEmpty()) {

            deviceUDID = deviceManager.getNextAvailableDeviceId();
        }

        System.out.println("***** Device UDID ***** " + deviceUDID);

        if (System.getProperty("os.name").toLowerCase().contains("mac")) {

            getDeviceCategory();
        } else {

            category = androidDeviceConfiguration.getDeviceModel(deviceUDID);
        }

        AppiumServiceBuilder appiumServiceBuilder = checkOSAndStartServer("StepDefs");

        if (appiumServiceBuilder != null) {
            return appiumServiceBuilder;
        }

        return null;
    }

    private AppiumServiceBuilder getAppiumServiceBuilder(String methodName) {

        String webKitPort = iosDeviceConfiguration.startIOSWebKit(deviceUDID);
        return appiumManager.appiumServerForIOS(deviceUDID, methodName, webKitPort);
    }

    private AppiumServiceBuilder checkOSAndStartServer(String methodName) {

        if (System.getProperty("os.name").toLowerCase().contains("mac")) {

            if (getMobilePlatform(deviceUDID).equals(MobilePlatform.IOS)) {
                AppiumServiceBuilder webKitPort = getAppiumServiceBuilder(methodName);
                if (webKitPort != null) {
                    return webKitPort;
                }
            } else {
                return appiumManager.appiumServerForAndroid(deviceUDID, methodName);
            }
        } else {
            return appiumManager.appiumServerForAndroid(deviceUDID, methodName);
        }
        return null;
    }

    private void getDeviceCategory() {

        if (iosDeviceConfiguration.checkiOSDevice(deviceUDID)) {

            iosDeviceConfiguration.setIOSWebKitProxyPorts(deviceUDID);
            category = iosDeviceConfiguration.getDeviceName(deviceUDID).replace(" ", "_");

        } else if (!iosDeviceConfiguration.checkiOSDevice(deviceUDID)) {

            category = androidDeviceConfiguration.getDeviceModel(deviceUDID);
            System.out.println(category);
        }
    }

    private MobilePlatform getMobilePlatform(String deviceUDID) {

        MobilePlatform platform;

        if (deviceUDID.length() == IOSDeviceConfiguration.IOS_UDID_LENGTH) {

            platform = MobilePlatform.IOS;
        } else {

            platform = MobilePlatform.ANDROID;
        }
        return platform;
    }

//    public AppiumServer createChildNodeWithCategory(String methodName,
//                                                    String tags) {
//        child = parentTest.get().createNode(methodName, category
//                + deviceUDID.replaceAll("\\W", "_")).assignCategory(tags);
//        test.set(child);
//        return this;
//    }

    public synchronized AppiumDriver<MobileElement> startAppiumServerInParallel(
            String methodName, DesiredCapabilities iosCaps,
            DesiredCapabilities androidCaps) {

        startingServerInstance(iosCaps, androidCaps);
        //startLogResults(getClass().getMethod(methodName).getName());
        return driver;
    }

    public void startingServerInstance(DesiredCapabilities iosCaps, DesiredCapabilities androidCaps) {

        if (properties.getProperty("APP_TYPE").equalsIgnoreCase("web")) {

            driver = new AndroidDriver<MobileElement>(appiumManager.getAppiumUrl(),
                    deviceCapabilityManager.androidWeb());
        } else {

            if (System.getProperty("os.name").toLowerCase().contains("mac")) {

                if (properties.getProperty("IOS_APP_PATH") != null
                        && iosDeviceConfiguration.checkiOSDevice(deviceUDID)) {
                    if (iosCaps == null) {
                        iosCaps = deviceCapabilityManager.iosNative(deviceUDID);
                        if (iosDeviceConfiguration.getIOSDeviceProductVersion(deviceUDID)
                                .contains("10")) {
                            iosCaps.setCapability(MobileCapabilityType.AUTOMATION_NAME,
                                    AutomationName.IOS_XCUI_TEST);
                            iosCaps.setCapability(IOSMobileCapabilityType
                                    .WDA_LOCAL_PORT, ports.getPort());
                        }
                    }

                    driver = new IOSDriver<MobileElement>(appiumManager.getAppiumUrl(), iosCaps);

                } else if (!iosDeviceConfiguration.checkiOSDevice(deviceUDID)) {

                    if (androidCaps == null) {
                        androidCaps = deviceCapabilityManager.androidNative(deviceUDID);
                    }
                    driver = new AndroidDriver<>(appiumManager.getAppiumUrl(), androidCaps);
                }
            } else {
                if (androidCaps == null) {
                    androidCaps = deviceCapabilityManager.androidNative(deviceUDID);
                }
                driver = new AndroidDriver<>(appiumManager.getAppiumUrl(), androidCaps);
            }
        }
    }

}
