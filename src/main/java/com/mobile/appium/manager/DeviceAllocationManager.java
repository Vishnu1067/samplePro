package com.mobile.appium.manager;

import com.mobile.configuration.AndroidDeviceConfiguration;
import com.mobile.configuration.IOSDeviceConfiguration;
import com.mobile.utils.ConfigurationManager;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DeviceAllocationManager - Handles device initialisation, allocation and de-allocation
 */
public class DeviceAllocationManager {

    private ArrayList<String> devices = new ArrayList<>();
    private ConcurrentHashMap<String, Boolean> deviceMapping =
            new ConcurrentHashMap<>();
    private static DeviceAllocationManager instance;
    private static AndroidDeviceConfiguration androidDevice = new AndroidDeviceConfiguration();
    private static IOSDeviceConfiguration iosDevice;
    private ConfigurationManager prop;

    private DeviceAllocationManager() {

        iosDevice = new IOSDeviceConfiguration();
        prop = ConfigurationManager.getInstance();
        initializeDevices();
    }

    public static DeviceAllocationManager getInstance() {
        if (instance == null) {
            instance = new DeviceAllocationManager();
        }
        return instance;
    }

    private void initializeDevices() {

        try {
            if (System.getProperty("os.name").toLowerCase().contains("mac")) {

                if (iosDevice.getIOSUDID() != null && prop.getProperty("Platform").equalsIgnoreCase("ios")) {

                    if (IOSDeviceConfiguration.validDeviceIds.size() > 0) {
                        devices.addAll(IOSDeviceConfiguration.validDeviceIds);
                    } else {
                        devices.addAll(IOSDeviceConfiguration.deviceUDIDiOS);
                    }
                } else {
                    getAndroidDeviceSerial();
                }
            } else {
                getAndroidDeviceSerial();
            }
            for (String device : devices) {
                deviceMapping.put(device, true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAndroidDeviceSerial() {

        if (androidDevice.getDeviceSerial() != null) {

            if (AndroidDeviceConfiguration.validDeviceIds.size() > 0) {
                devices.addAll(AndroidDeviceConfiguration.validDeviceIds);
            } else {
                devices.addAll(AndroidDeviceConfiguration.deviceSerial);
            }
        }
    }

    public ArrayList<String> getDevices() {

        return devices;
    }

    public synchronized String getNextAvailableDeviceId() {

        ConcurrentHashMap.KeySetView<String, Boolean> devices = deviceMapping.keySet();
        int i = 0;

        for (String device : devices) {
            Thread t = Thread.currentThread();
            t.setName("Thread_" + i);
            i++;
            if (deviceMapping.get(device)) {
                deviceMapping.put(device, false);
                return device;
            }
        }
        return null;
    }

    public void freeDevice() {

        deviceMapping.put(DeviceManager.getDeviceUDID(), true);
    }

    public void allocateDevice(String device, String deviceUDID) {

        if (device == null) {
            DeviceManager.setDeviceUDID(deviceUDID);
        } else {
            DeviceManager.setDeviceUDID(device);
        }
    }
}
