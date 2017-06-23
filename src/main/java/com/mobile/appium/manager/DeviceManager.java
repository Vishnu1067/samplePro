package com.mobile.appium.manager;

import com.mobile.configuration.AndroidDeviceConfiguration;
import com.mobile.configuration.IOSDeviceConfiguration;
import com.mobile.utils.ConfigurationManager;
import com.mobile.utils.MobilePlatform;

/**
 * Device Manager - Handles all device related information's e.g UDID, Model, etc
 */
public class DeviceManager {


    private static ThreadLocal<String> deviceUDID = new ThreadLocal<>();
    private static AndroidDeviceConfiguration androidDevice;
    private static IOSDeviceConfiguration iosDevice;
    private static ConfigurationManager prop;

    public DeviceManager() {

        iosDevice = new IOSDeviceConfiguration();
        androidDevice = new AndroidDeviceConfiguration();
        prop = ConfigurationManager.getInstance();
    }

    public static String getDeviceUDID() {

        return deviceUDID.get();
    }

    protected static void setDeviceUDID(String udID) {

        deviceUDID.set(udID);
    }

    public static MobilePlatform getMobilePlatform() {

        if (DeviceManager.getDeviceUDID() != null && prop.getProperty("Platform").equalsIgnoreCase("ios")) {
            return MobilePlatform.IOS;
        } else {
            return MobilePlatform.ANDROID;
        }
    }

    public String getDeviceModel() {

        if (getMobilePlatform().equals(MobilePlatform.ANDROID) && prop.getProperty("Platform").equalsIgnoreCase("android")) {
            return androidDevice.getDeviceModel();
        } else if (getMobilePlatform().equals(MobilePlatform.IOS) && prop.getProperty("Platform").equalsIgnoreCase("ios")) {
            return iosDevice.getIOSDeviceProductTypeAndVersion();
        }
        return null;
    }

    public String getPlatformVersion() {

        if (getMobilePlatform().equals(MobilePlatform.ANDROID) && prop.getProperty("Platform").equalsIgnoreCase("android")) {
            return androidDevice.getDeviceManufacturer();
        } else if (getMobilePlatform().equals(MobilePlatform.IOS) && prop.getProperty("Platform").equalsIgnoreCase("ios")) {
            return iosDevice.getIOSDeviceProductVersion();
        }
        return null;

    }

    public String getDeviceName() {

        if (getMobilePlatform().equals(MobilePlatform.ANDROID) && prop.getProperty("Platform").equalsIgnoreCase("android")) {
            return androidDevice.getDeviceName();
        } else if (getMobilePlatform().equals(MobilePlatform.IOS) && prop.getProperty("Platform").equalsIgnoreCase("ios")) {
            return iosDevice.getDeviceName();
        }
        return null;
    }

    public String getDeviceCategory() {

        if (iosDevice.deviceUDIDiOS.contains(DeviceManager.getDeviceUDID()) && prop.getProperty("Platform").equalsIgnoreCase("ios")) {
            return iosDevice.getDeviceName().replace(" ", "_");
        } else {
            return androidDevice.getDeviceModel();
        }
    }

}
