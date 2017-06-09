package com.mobile.appium.manager;

import com.mobile.configuration.AndroidDeviceConfiguration;
import com.mobile.configuration.IOSDeviceConfiguration;
import com.mobile.utils.MobilePlatform;

import java.io.IOException;

/**
 * Device Manager - Handles all device related information's e.g UDID, Model, etc
 */
public class DeviceManager {


    private static ThreadLocal<String> deviceUDID = new ThreadLocal<>();
    private static AndroidDeviceConfiguration androidDevice;
    private static IOSDeviceConfiguration iosDevice;


    public DeviceManager() {

        iosDevice = new IOSDeviceConfiguration();
        androidDevice = new AndroidDeviceConfiguration();
    }

    public static String getDeviceUDID() {
        return deviceUDID.get();
    }

    protected static void setDeviceUDID(String udID) {
        deviceUDID.set(udID);
    }

    public static MobilePlatform getMobilePlatform() {
        if (DeviceManager.getDeviceUDID() != null) {
            return MobilePlatform.IOS;
        } else {
            return MobilePlatform.ANDROID;
        }
    }

    public String getDeviceModel() throws InterruptedException, IOException {
        if (getMobilePlatform().equals(MobilePlatform.ANDROID)) {
            return androidDevice.getDeviceModel();
        } else if (getMobilePlatform().equals(MobilePlatform.IOS)) {
            return iosDevice.getIOSDeviceProductTypeAndVersion();
        }
        return null;
    }

    public String getDeviceCategory() throws Exception {
        if (iosDevice.deviceUDIDiOS.contains(DeviceManager.getDeviceUDID())) {
            return iosDevice.getDeviceName().replace(" ", "_");
        } else {
            return androidDevice.getDeviceModel();
        }
    }

}