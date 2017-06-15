package com.mobile.appium.manager;

import com.github.lalyos.jfiglet.FigletFont;
import com.mobile.configuration.AndroidDeviceConfiguration;
import com.mobile.configuration.IOSDeviceConfiguration;
import com.mobile.report.HtmlReporter;
import com.mobile.utils.ConfigurationManager;
import com.mobile.utils.MyTestExecutor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * This class picks the devices connected
 * and distributes across multiple thread.
 *
 */


public class ParallelThread {

    private ConfigurationManager configurationManager;
    private DeviceAllocationManager deviceAllocationManager;
    protected int deviceCount = 0;
    Map<String, String> devices = new HashMap<>();
    ArrayList<String> iOSDevices = new ArrayList<>();
    private AndroidDeviceConfiguration androidDevice;
    private IOSDeviceConfiguration iosDevice;
    private MyTestExecutor myTestExecutor;
    List<Class> testCases;
    private HtmlReporter htmlReporter;

    public ParallelThread() {

        deviceAllocationManager = DeviceAllocationManager.getInstance();
        configurationManager = ConfigurationManager.getInstance();
        iosDevice = new IOSDeviceConfiguration();
        androidDevice = new AndroidDeviceConfiguration();
        myTestExecutor = new MyTestExecutor();
        htmlReporter = new HtmlReporter();
    }

    public ParallelThread(List<String> validDeviceIds) {

        iosDevice = new IOSDeviceConfiguration();
        androidDevice = new AndroidDeviceConfiguration();
        configurationManager = ConfigurationManager.getInstance();
        androidDevice.setValidDevices(validDeviceIds);
        iosDevice.setValidDevices(validDeviceIds);
        deviceAllocationManager = DeviceAllocationManager.getInstance();
        myTestExecutor = new MyTestExecutor();
        htmlReporter = new HtmlReporter();
    }

    public boolean runner() {
        figlet(configurationManager.getProperty("RUNNER"));
        return triggerTest();
    }

    private boolean triggerTest() {

        return parallelExecution();
    }

    private boolean parallelExecution() {

        String operSys = System.getProperty("os.name").toLowerCase();
        File f = new File(System.getProperty("user.dir") + "/target/appiumlogs/");

        if (!f.exists()) {

            try {
                f.mkdir();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }

        if (androidDevice.getDevices() != null && configurationManager.getProperty("ANDROID_APP_PATH") != null) {

            devices = androidDevice.getDevices();
            deviceCount = devices.size() / 8;

            System.out.println("Device Count : " + deviceCount);

            File adb_logs = new File(System.getProperty("user.dir") + "/target/adblogs/");

            if (!adb_logs.exists()) {
                try {
                    adb_logs.mkdir();
                } catch (SecurityException se) {
                    se.printStackTrace();
                }
            }
            createSnapshotFolderAndroid("android");
        }

        if (operSys.contains("mac") && configurationManager.getProperty("IOS_APP_PATH") != null) {

            if (deviceAllocationManager.getDevices().size() > 0) {

                iosDevice.checkExecutePermissionForIOSDebugProxyLauncher();
                iOSDevices = deviceAllocationManager.getDevices();
                deviceCount = iOSDevices.size();
                createSnapshotFolderiOS("iPhone");
            }
        }

        if (deviceCount == 0) {
            figlet("No Devices Connected");
            System.exit(0);
        }

        testCases = new ArrayList<>();

        boolean hasFailures = false;

        if (configurationManager.getProperty("RUNNER").equalsIgnoreCase("distribute")) {
            myTestExecutor.constructXmlSuiteDistributeCucumber(deviceCount);
            hasFailures = myTestExecutor.runCucumberTest();
            htmlReporter.generateReports();

        } else if (configurationManager.getProperty("RUNNER").equalsIgnoreCase("parallel")) {
            myTestExecutor.constructXmlSuiteForParallelCucumber(deviceCount, deviceAllocationManager.getDevices());
            hasFailures = myTestExecutor.runCucumberTest();
            htmlReporter.generateReports();
        }

        return hasFailures;
    }

    private void createSnapshotFolderAndroid(String platform) {

        for (int i = 1; i <= (devices.size() / 4); i++) {
            String deviceSerial = devices.get("deviceID" + i);
            if (deviceSerial != null) {
                createPlatformDirectory(platform);
                File file = new File(
                        System.getProperty("user.dir") + "/target/screenshot/" + platform + "/"
                                + deviceSerial.replaceAll("\\W", "_"));

                if (!file.exists()) {
                    if (file.mkdir()) {
                        System.out.println("Android " + deviceSerial + " Directory is created!");
                    } else {
                        System.out.println("Failed to create directory!");
                    }
                }
            }
        }
    }

    private void createSnapshotFolderiOS(String platform) {

        for (int i = 0; i < iOSDevices.size(); i++) {
            String deviceSerial = iOSDevices.get(i);
            createPlatformDirectory(platform);
            File file = new File(
                    System.getProperty("user.dir") + "/target/screenshot/" + platform + "/"
                            + deviceSerial.replaceAll("\\W", "_"));
            if (!file.exists()) {
                if (file.mkdir()) {
                    System.out.println("IOS " + deviceSerial + " Directory is created!");
                } else {
                    System.out.println("Failed to create directory!");
                }
            }
        }
    }

    private void createPlatformDirectory(String platform) {

        File file2 = new File(System.getProperty("user.dir") + "/target/screenshot");
        if (!file2.exists()) {
            file2.mkdir();
        }

        File file3 = new File(System.getProperty("user.dir") + "/target/screenshot/" + platform);
        if (!file3.exists()) {
            file3.mkdir();
        }
    }

    public void addPluginToCucumberRunner() {

        try {

            File dir = new File(System.getProperty("user.dir") + "/src/test/java/output/");
            System.out.println("Getting all files in " + dir.getCanonicalPath()
                    + " including those in subdirectories");
            List<File> files =
                    (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            for (File file : files) {
                BufferedReader read = new BufferedReader(new FileReader(file.getAbsoluteFile()));
                ArrayList list = new ArrayList();

                String dataRow = read.readLine();
                while (dataRow != null) {
                    list.add(dataRow);
                    dataRow = read.readLine();
                }

                FileWriter writer = new FileWriter(file.getAbsoluteFile()); //same as your file name above so that it will replace it
                writer.append("package output;");

                for (int i = 0; i < list.size(); i++) {
                    writer.append(System.getProperty("line.separator"));
                    writer.append((CharSequence) list.get(i));
                }
                writer.flush();
                writer.close();

                Path path = Paths.get(file.getAbsoluteFile().toString());
                List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                lines.add(12, "plugin = {\"com.mobile.cucumber.listener.ExtentCucumberFormatter\"},");
                Files.write(path, lines, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void figlet(String text) {
        String asciiArt1 = null;
        try {
            asciiArt1 = FigletFont.convertOneLine(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(asciiArt1);
    }
}
