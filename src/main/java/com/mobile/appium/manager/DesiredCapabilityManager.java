package com.mobile.appium.manager;

import com.mobile.configuration.IOSDeviceConfiguration;
import com.mobile.utils.AvailablePorts;
import com.mobile.utils.ConfigurationManager;
import com.mobile.utils.MobilePlatform;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DesiredCapabilityManager {

    private final ConfigurationManager prop;
    private AvailablePorts availablePorts;
    private IOSDeviceConfiguration iosDevice;
    private DeviceManager deviceManager;

    public DesiredCapabilityManager() {

        prop = ConfigurationManager.getInstance();
        availablePorts = new AvailablePorts();
        iosDevice = new IOSDeviceConfiguration();
        deviceManager = new DeviceManager();
    }

    public DesiredCapabilities androidNative() {

        DesiredCapabilities androidCapabilities = new DesiredCapabilities();
        androidCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        androidCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceManager.getDeviceName());
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
        androidCapabilities.setCapability(MobileCapabilityType.UDID, DeviceManager.getDeviceUDID());
        androidCapabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT,
                availablePorts.getPort());

        return androidCapabilities;
    }

    public DesiredCapabilities androidWeb() {

        DesiredCapabilities androidWebCapabilities = new DesiredCapabilities();
        androidWebCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, MobilePlatform.ANDROID);
        androidWebCapabilities
                .setCapability(MobileCapabilityType.BROWSER_NAME,
                        prop.getProperty("BROWSER_TYPE"));
        androidWebCapabilities.setAcceptInsecureCerts(true);
        androidWebCapabilities.setCapability(MobileCapabilityType.UDID,
                DeviceManager.getDeviceUDID());

        androidWebCapabilities.setCapability("appPackage", "com.android.chrome");
        androidWebCapabilities.setCapability("appActivity", "com.google.android.apps.chrome.Main");

        return androidWebCapabilities;
    }

    public DesiredCapabilities iosNative() {

        DesiredCapabilities iOSCapabilities = new DesiredCapabilities();

        iOSCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,
                MobilePlatform.IOS);
        iOSCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,
                "10.3.2");
        iOSCapabilities.setCapability(MobileCapabilityType.APP,
                prop.getProperty("IOS_APP_PATH"));
        iOSCapabilities.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, true);
        iOSCapabilities
                .setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone");
        iOSCapabilities.setCapability(MobileCapabilityType.UDID, DeviceManager.getDeviceUDID());

        //if (iosDevice.getIOSDeviceProductVersion().contains("10")) {

        iOSCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,
                AutomationName.IOS_XCUI_TEST);

        iOSCapabilities.setCapability(IOSMobileCapabilityType
                .WDA_LOCAL_PORT, availablePorts.getPort());
        //}
        return iOSCapabilities;
    }
}
