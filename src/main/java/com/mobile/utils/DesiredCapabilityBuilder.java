package com.mobile.utils;


import com.mobile.appium.manager.DeviceManager;
import com.mobile.configuration.IOSDeviceConfiguration;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.json.simple.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class DesiredCapabilityBuilder {

    private AvailablePorts availablePorts;
    private IOSDeviceConfiguration iosDevice;

    public static ThreadLocal<DesiredCapabilities> desiredCapabilitiesThreadLocal
            = new ThreadLocal<>();

    public DesiredCapabilityBuilder() {
        availablePorts = new AvailablePorts();
        iosDevice = new IOSDeviceConfiguration();
    }

    public static DesiredCapabilities getDesiredCapability() {
        return desiredCapabilitiesThreadLocal.get();
    }

    public DesiredCapabilities buildDesiredCapability(String jsonPath) {

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        JSONObject jsonParsedObject = new JsonParser(jsonPath).getJsonParsedObject();
        jsonParsedObject
                .forEach((caps, values) -> {
                    if (caps.equals("app")) {
                        Path path = FileSystems.getDefault().getPath(values.toString());
                        if (!path.getParent().isAbsolute()) {
                            desiredCapabilities.setCapability(caps.toString(), path.normalize()
                                    .toAbsolutePath().toString());
                        }
                    } else {
                        desiredCapabilities.setCapability(caps.toString(), values.toString());
                    }
                });
        //Check for web
        if (DeviceManager.getMobilePlatform().equals(MobilePlatform.ANDROID)) {

            if (desiredCapabilities.getCapability("automationName") == null
                    || desiredCapabilities.getCapability("automationName")
                    .toString() != "UIAutomator2") {
                desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,
                        AutomationName.ANDROID_UIAUTOMATOR2);
            }
            desiredCapabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT,
                    availablePorts.getPort());
            appPackage(desiredCapabilities);
            desiredCapabilities.setCapability(MobileCapabilityType.UDID,
                    DeviceManager.getDeviceUDID());

        } else if (DeviceManager.getMobilePlatform().equals(MobilePlatform.IOS)) {
            appPackageBundle(desiredCapabilities);

            if (iosDevice.getIOSDeviceProductVersion()
                    .contains("10")) {
                desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,
                        AutomationName.IOS_XCUI_TEST);
                desiredCapabilities.setCapability(IOSMobileCapabilityType
                        .WDA_LOCAL_PORT, availablePorts.getPort());
            }
            desiredCapabilities.setCapability(MobileCapabilityType.UDID,
                    DeviceManager.getDeviceUDID());
        }

        desiredCapabilitiesThreadLocal.set(desiredCapabilities);
        return desiredCapabilities;
    }

    public void appPackage(DesiredCapabilities desiredCapabilities) {

        if (System.getenv("APP_PACKAGE") != null) {

            desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
                    System.getenv("APP_PACKAGE"));
        }
    }

    private void appPackageBundle(DesiredCapabilities iOSCapabilities) {

        if (System.getenv("APP_PACKAGE") != null) {

            iOSCapabilities
                    .setCapability(IOSMobileCapabilityType.BUNDLE_ID,
                            System.getenv("APP_PACKAGE"));
        }
    }
}
