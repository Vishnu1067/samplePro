package com.mobile.appium.manager;

import com.mobile.configuration.IOSDeviceConfiguration;
import com.mobile.utils.AvailablePorts;
import com.mobile.utils.ConfigurationManager;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DesiredCapabilityManager {

    private final ConfigurationManager prop;
    private AvailablePorts availablePorts;
    private IOSDeviceConfiguration iosDevice;

    public DesiredCapabilityManager() {

        prop = ConfigurationManager.getInstance();
        availablePorts = new AvailablePorts();
        iosDevice = new IOSDeviceConfiguration();
    }

    public DesiredCapabilities androidNative() {

        System.out.println("Setting Android Desired Capabilities:");
        DesiredCapabilities androidCapabilities = new DesiredCapabilities();
        androidCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        androidCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
        androidCapabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
                prop.getProperty("APP_ACTIVITY"));
        androidCapabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
                prop.getProperty("APP_PACKAGE"));
        androidCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,
                AutomationName.ANDROID_UIAUTOMATOR2);

        //check Selendroid (androidCapabilities);
        androidCapabilities
                .setCapability(MobileCapabilityType.APP,
                        prop.getProperty("ANDROID_APP_PATH"));
        System.out.println(DeviceManager.getDeviceUDID() + Thread.currentThread().getId());

        androidCapabilities.setCapability(MobileCapabilityType.UDID, DeviceManager.getDeviceUDID());
        androidCapabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT,
                availablePorts.getPort());

        return androidCapabilities;
    }

    public DesiredCapabilities androidWeb() {
        DesiredCapabilities androidWebCapabilities = new DesiredCapabilities();
        androidWebCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
        androidWebCapabilities
                .setCapability(MobileCapabilityType.BROWSER_NAME,
                        prop.getProperty("BROWSER_TYPE"));
        androidWebCapabilities.setCapability(MobileCapabilityType.TAKES_SCREENSHOT, true);
        androidWebCapabilities.setCapability(MobileCapabilityType.UDID,
                DeviceManager.getDeviceUDID());
        return androidWebCapabilities;
    }

    public DesiredCapabilities iosNative() {

        DesiredCapabilities iOSCapabilities = new DesiredCapabilities();
        System.out.println("Setting iOS Desired Capabilities:");

        iOSCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,
                "iOS");
        iOSCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,
                "10.0");
        iOSCapabilities.setCapability(MobileCapabilityType.APP,
                prop.getProperty("IOS_APP_PATH"));
        iOSCapabilities.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, true);
        iOSCapabilities
                .setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone");
        iOSCapabilities.setCapability(MobileCapabilityType.UDID, DeviceManager.getDeviceUDID());

        if (iosDevice.getIOSDeviceProductVersion()
                .contains("10")) {
            iOSCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,
                    AutomationName.IOS_XCUI_TEST);

            iOSCapabilities.setCapability(IOSMobileCapabilityType
                    .WDA_LOCAL_PORT, availablePorts.getPort());
        }
        return iOSCapabilities;
    }
}
