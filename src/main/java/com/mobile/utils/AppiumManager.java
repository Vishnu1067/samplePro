package com.mobile.utils;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.service.local.flags.ServerArgument;

import java.io.File;
import java.net.URL;

/**
 * Appium Manager - this class contains method to start and stops appium server
 * To execute the tests from eclipse, you need to set PATH as
 * /usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin in run configuration
 */
public class AppiumManager {

    private AvailablePorts availablePorts = new AvailablePorts();
    public AppiumDriverLocalService appiumDriverLocalService;
    private ConfigurationManager prop;


    public AppiumManager() {
        prop = ConfigurationManager.getInstance();
    }

    /**
     * start appium with auto generated ports : appium port, chrome port,
     * bootstrap port and device UDID
     */

    public AppiumServiceBuilder appiumServerForAndroid(String deviceID, String methodName) {

        System.out.println(
                "**************************************************************************\n");
        System.out.println("Starting Appium Server to handle Android Device::" + deviceID + "\n");
        System.out.println(
                "**************************************************************************\n");

        int port = availablePorts.getPort();
        int chromePort = availablePorts.getPort();
        int bootstrapPort = availablePorts.getPort();
        int selendroidPort = availablePorts.getPort();

        AppiumServiceBuilder builder =
                new AppiumServiceBuilder().withAppiumJS(new File(prop.getProperty("APPIUM_JS_PATH")))
                        .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
//                        .withLogFile(new File(
//                        System.getProperty("user.dir") + "/target/appiumlogs/" + deviceID
//                                .replaceAll("\\W", "_") + "__" + methodName + ".txt"))
                        .withArgument(AndroidServerFlag.CHROME_DRIVER_PORT, Integer.toString(chromePort))
                        .withArgument(AndroidServerFlag.BOOTSTRAP_PORT_NUMBER,
                                Integer.toString(bootstrapPort))
                        .withIPAddress("127.0.0.1")
                        .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                        .withArgument(AndroidServerFlag.SUPPRESS_ADB_KILL_SERVER)
                        .withArgument(AndroidServerFlag.SELENDROID_PORT, Integer.toString(selendroidPort))
                        .usingPort(port);
        /* and so on */
        ;
        appiumDriverLocalService = builder.build();
        appiumDriverLocalService.start();
        return builder;

    }

    /**
     * start appium with auto generated ports : appium port, chrome port,
     * bootstrap port and device UDID
     */
    ServerArgument webKitProxy = new ServerArgument() {
        @Override
        public String getArgument() {
            return "--webkit-debug-proxy-port";
        }
    };

    public AppiumServiceBuilder appiumServerForIOS(String deviceID, String methodName,
                                                   String webKitPort) {

        System.out
                .println("**********************************************************************\n");
        System.out.println("Starting Appium Server to handle IOS::" + deviceID + "\n");
        System.out
                .println("**********************************************************************\n");

        File classPathRoot = new File(System.getProperty("user.dir"));
        int port = availablePorts.getPort();

        AppiumServiceBuilder builder =
                new AppiumServiceBuilder().withAppiumJS(new File(prop.getProperty("APPIUM_JS_PATH")))
                        .withArgument(GeneralServerFlag.LOG_LEVEL, "info").withLogFile(new File(
                        System.getProperty("user.dir") + "/target/appiumlogs/" + deviceID
                                .replaceAll("\\W", "_") + "__" + methodName + ".txt"))
                        .withArgument(webKitProxy, webKitPort)
                        .withIPAddress("127.0.0.1")
                        .withArgument(GeneralServerFlag.LOG_LEVEL, "debug")
                        .withArgument(GeneralServerFlag.TEMP_DIRECTORY,
                                new File(String.valueOf(classPathRoot)).getAbsolutePath() + "/target/" + "tmp_"
                                        + port).withArgument(GeneralServerFlag.SESSION_OVERRIDE).usingPort(port);
        appiumDriverLocalService = builder.build();
        appiumDriverLocalService.start();
        return builder;

    }

    public URL getAppiumUrl() {

        return appiumDriverLocalService.getUrl();
    }

    public void destroyAppiumNode() {

        appiumDriverLocalService.stop();

        if (appiumDriverLocalService.isRunning()) {
            System.out.println("AppiumServer didn't shut... Trying to quit again....");
            appiumDriverLocalService.stop();
        }
    }
}