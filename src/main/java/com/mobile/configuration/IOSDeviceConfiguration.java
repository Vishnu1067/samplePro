package com.mobile.configuration;


import com.mobile.appium.manager.DeviceManager;
import com.mobile.utils.AvailablePorts;
import com.mobile.utils.CommandPrompt;
import com.mobile.utils.ConfigurationManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class IOSDeviceConfiguration {


    public static ConcurrentHashMap<Long, Integer> appiumServerProcess = new ConcurrentHashMap<>();
    public static ArrayList<String> deviceUDIDiOS = new ArrayList<>();
    private final ConfigurationManager prop;
    public HashMap<String, String> deviceMap = new HashMap<>();
    public Process p1;
    public static List<String> validDeviceIds = new ArrayList<>();
    public final static int IOS_UDID_LENGTH = 40;

    CommandPrompt commandPrompt = new CommandPrompt();
    AvailablePorts ap = new AvailablePorts();

    String profile = "system_profiler SPUSBDataType | sed -n -E -e '/(iPhone|iPad|iPod)/"
            + ",/Serial/s/ *Serial Number: *(.+)/\\1/p'\n";

    public IOSDeviceConfiguration() {

        prop = ConfigurationManager.getInstance();
    }

    public ArrayList<String> getIOSUDID() {

        try {
            int startPos = 0;
            int endPos = IOS_UDID_LENGTH - 1;
            String getIOSDeviceID = commandPrompt.runProcessCommandToGetDeviceID(profile);
            if (getIOSDeviceID == null || getIOSDeviceID.equalsIgnoreCase("") || getIOSDeviceID
                    .isEmpty()) {
                return null;
            } else {
                while (endPos < getIOSDeviceID.length()) {
                    if (validDeviceIds.size() > 0) {
                        if (validDeviceIds.contains(
                                getIOSDeviceID.substring(startPos, endPos + 1))) {
                            if (!deviceUDIDiOS.contains(getIOSDeviceID)) {
                                deviceUDIDiOS.add(getIOSDeviceID.substring(startPos, endPos + 1)
                                        .replace("\n", ""));
                            }
                        }
                    } else {
                        deviceUDIDiOS.add(getIOSDeviceID.substring(startPos, endPos + 1)
                                .replace("\n", ""));
                    }
                    startPos += IOS_UDID_LENGTH;
                    endPos += IOS_UDID_LENGTH;
                }
                return deviceUDIDiOS;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param UDID    - Device Serial ID
     * @param appPath - Path of the .ipa file
     */
    public void installApp(String UDID, String appPath) {
        System.out.println("Installing App on device ******** " + UDID);
        commandPrompt.runCommand("ideviceinstaller --udid " + UDID + " --install " + appPath);
    }

    /**
     * @param UDID     - Device Serial ID
     * @param bundleID - Bundle ID of the .ipa file
     */
    public void unInstallApp(String UDID, String bundleID) {
        System.out.println("Uninstalling App on device ******* " + UDID);
        System.out.println("ideviceinstaller --udid " + UDID + " -U " + bundleID);
        commandPrompt.runCommand("ideviceinstaller --udid " + UDID + " -U " + bundleID);
    }

    /**
     * @param bundleID
     * @return
     */
    public boolean checkIfAppIsInstalled(String bundleID) {
        boolean appAlreadyExists =
                commandPrompt.runCommand("ideviceinstaller --list-apps").contains(bundleID);
        return appAlreadyExists;
    }

    /**
     * Need to fix bug not fetching the version and product type for
     * report category
     */

    public String getIOSDeviceProductTypeAndVersion() {
        return commandPrompt
                .runCommandThruProcessBuilder("ideviceinfo --udid " + DeviceManager.getDeviceUDID() + " | grep ProductType");
    }

    public String getDeviceName() {
        String deviceName =
                commandPrompt.runCommandThruProcessBuilder("idevicename --udid "
                        + DeviceManager.getDeviceUDID());
        return deviceName;
    }

    public String getIOSDeviceProductVersion() {

        return commandPrompt
                .runCommandThruProcessBuilder("ideviceinfo --udid "
                        + DeviceManager.getDeviceUDID()
                        + " | grep ProductVersion");
    }

    public boolean checkiOSDevice() {
        String getIOSDeviceID = commandPrompt.runCommand("idevice_id --list");
        return getIOSDeviceID.contains(DeviceManager.getDeviceUDID());
    }

    public HashMap<String, String> setIOSWebKitProxyPorts() {
        try {
            int webKitProxyPort = ap.getPort();
            deviceMap.put(DeviceManager.getDeviceUDID(), Integer.toString(webKitProxyPort));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceMap;
    }

    public String startIOSWebKit() {

        int port;

        try {
            setIOSWebKitProxyPorts();
            String serverPath = prop.getProperty("APPIUM_JS_PATH");
            File file = new File(serverPath);
            File currentPath = new File(file.getParent());
            System.out.println(currentPath);

            file = new File(currentPath + "/.." + "/..");
            String ios_web_lit_proxy_runner =
                    file.getCanonicalPath() + "/bin/ios-webkit-debug-proxy-launcher.js";

            port = ap.getPort();

            String webkitRunner =
                    ios_web_lit_proxy_runner + " -c " + DeviceManager.getDeviceUDID() + ":" + deviceMap.get(DeviceManager.getDeviceUDID()) + " -d";
            System.out.println(webkitRunner);
            p1 = Runtime.getRuntime().exec(webkitRunner);
            System.out.println(
                    "WebKit Proxy is started on device " + DeviceManager.getDeviceUDID() + " and with port number " + deviceMap
                            .get(DeviceManager.getDeviceUDID()) + " and in thread " + Thread.currentThread().getId());

            //Add the Process ID to hashMap, which would be needed to kill IOSwebProxy when required
            appiumServerProcess.put(Thread.currentThread().getId(), getPid(p1));
            System.out.println("Process ID's:" + appiumServerProcess);
            return String.valueOf(port);
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }

    }

    public long getPidOfProcess() {

        long pid = -1;

        try {
            if (p1.getClass().getName().equals("java.lang.UNIXProcess")) {
                Field f = p1.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                pid = f.getLong(p1);
                f.setAccessible(false);
            }
        } catch (Exception e) {
            pid = -1;
        }
        return pid;
    }

    public int getPid(Process process) {

        try {
            Class<?> cProcessImpl = process.getClass();
            Field fPid = cProcessImpl.getDeclaredField("pid");
            if (!fPid.isAccessible()) {
                fPid.setAccessible(true);
            }
            return fPid.getInt(process);
        } catch (Exception e) {
            return -1;
        }
    }

    public void destroyIOSWebKitProxy() {

        try {

            Thread.sleep(3000);
            if (appiumServerProcess.get(Thread.currentThread().getId()) != -1) {
                String process = "pgrep -P " + appiumServerProcess.get(Thread.currentThread().getId());
                Process p2 = Runtime.getRuntime().exec(process);
                BufferedReader r = new BufferedReader(new InputStreamReader(p2.getInputStream()));
                String command = "kill -9 " + r.readLine();
                System.out.println("Kills webkit proxy");
                System.out.println("******************" + command);
                Runtime.getRuntime().exec(command);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkExecutePermissionForIOSDebugProxyLauncher() {

        try {

            String serverPath = prop.getProperty("APPIUM_JS_PATH");
            File file = new File(serverPath);
            File currentPath = new File(file.getParent());
            file = new File(currentPath + "/.." + "/..");
            File executePermission =
                    new File(file.getCanonicalPath() + "/bin/ios-webkit-debug-proxy-launcher.js");

            if (executePermission.exists()) {
                if (executePermission.canExecute() == false) {
                    executePermission.setExecutable(true);
                    System.out.println("Access Granted for iOSWebKitProxyLauncher");
                } else {
                    System.out.println("iOSWebKitProxyLauncher File already has access to execute");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setValidDevices(List<String> validDeviceIds) {

        validDeviceIds.forEach(deviceList -> {
            if (deviceList.length() == IOSDeviceConfiguration.IOS_UDID_LENGTH) {
                validDeviceIds.add(deviceList);
            }
        });
    }

    public static ArrayList<String> getDeviceUDIDiOS() {
        return deviceUDIDiOS;
    }
}
