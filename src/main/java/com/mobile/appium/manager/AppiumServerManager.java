package com.mobile.appium.manager;

import com.mobile.configuration.IOSDeviceConfiguration;
import com.mobile.utils.AvailablePorts;
import com.mobile.utils.ConfigurationManager;
import com.mobile.utils.MobilePlatform;
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
public class AppiumServerManager {

    private AvailablePorts availablePorts;
    private IOSDeviceConfiguration iosDeviceConfiguration;
    private ConfigurationManager prop;
    private static ThreadLocal<AppiumDriverLocalService> appiumDriverLocalService = new ThreadLocal<>();


    private static AppiumDriverLocalService getServer() {
        return appiumDriverLocalService.get();
    }

    private static void setServer(AppiumDriverLocalService server) {
        appiumDriverLocalService.set(server);
    }

    public AppiumServerManager() {

        iosDeviceConfiguration = new IOSDeviceConfiguration();
        availablePorts = new AvailablePorts();
        prop = ConfigurationManager.getInstance();
    }


    public void startAppiumServer(String methodName) {

        if (System.getProperty("os.name").toLowerCase().contains("mac")) {

            if (DeviceManager.getMobilePlatform().equals(MobilePlatform.IOS)) {
                startAppiumServerForIOS(methodName);
            } else {
                startAppiumServerForAndroid(methodName);
            }

        } else {
            startAppiumServerForAndroid(methodName);
        }
    }

    public void stopAppiumServer() {

        destroyAppiumNode();

        if (DeviceManager.getMobilePlatform().equals(MobilePlatform.IOS)) {
            iosDeviceConfiguration.destroyIOSWebKitProxy();
        }
    }


    /**
     * start appium with auto generated ports : appium port, chrome port,
     * bootstrap port and device UDID
     */

    private void startAppiumServerForAndroid(String methodName) {

        System.out.println(
                "**************************************************************************\n");
        System.out.println("Starting Appium Server to handle Android Device " + DeviceManager.getDeviceUDID() + "\n");
        System.out.println(
                "**************************************************************************\n");

        AppiumDriverLocalService appiumDriverLocalService;
        int port = availablePorts.getPort();
        int chromePort = availablePorts.getPort();
        int bootstrapPort = availablePorts.getPort();
        int selendroidPort = availablePorts.getPort();

        AppiumServiceBuilder builder =
                new AppiumServiceBuilder().withAppiumJS(new File(prop.getProperty("APPIUM_JS_PATH")))
                        .withArgument(GeneralServerFlag.LOG_LEVEL, "debug")
                        .withLogFile(new File(
                                System.getProperty("user.dir") + "/target/appiumlogs/"
                                        + DeviceManager.getDeviceUDID().replaceAll("\\W", "_") + "__"
                                        + methodName.replace(" ", "_") + ".txt"))
                        .withArgument(AndroidServerFlag.CHROME_DRIVER_PORT, Integer.toString(chromePort))
                        .withArgument(AndroidServerFlag.BOOTSTRAP_PORT_NUMBER,
                                Integer.toString(bootstrapPort))
                        .withIPAddress("127.0.0.1")
                        .withArgument(AndroidServerFlag.SUPPRESS_ADB_KILL_SERVER)
                        .withArgument(AndroidServerFlag.SELENDROID_PORT, Integer.toString(selendroidPort))
                        .usingPort(port);
        /* and so on */
        ;
        appiumDriverLocalService = builder.build();
        appiumDriverLocalService.start();
        setServer(appiumDriverLocalService);

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

    private void startAppiumServerForIOS(String methodName) {

        String webKitPort = iosDeviceConfiguration.startIOSWebKit();

        System.out
                .println("**********************************************************************\n");
        System.out.println("Starting Appium Server to handle IOS " + DeviceManager.getDeviceUDID() + "\n");
        System.out
                .println("**********************************************************************\n");

        File classPathRoot = new File(System.getProperty("user.dir"));
        int port = availablePorts.getPort();
        AppiumDriverLocalService appiumDriverLocalService;
        AppiumServiceBuilder builder =
                new AppiumServiceBuilder().withAppiumJS(new File(prop.getProperty("APPIUM_JS_PATH")))
                        .withArgument(GeneralServerFlag.LOG_LEVEL, "debug").withLogFile(new File(
                        System.getProperty("user.dir") + "/target/appiumlogs/"
                                + DeviceManager.getDeviceUDID().replaceAll("\\W", "_") + "__"
                                + methodName.replaceAll(" ", "_") + ".txt"))
                        .withArgument(webKitProxy, webKitPort)
                        .withIPAddress("127.0.0.1")
                        .withArgument(GeneralServerFlag.TEMP_DIRECTORY,
                                new File(String.valueOf(classPathRoot)).getAbsolutePath() + "/target/" + "tmp_"
                                        + port)
                        .usingPort(port);

        appiumDriverLocalService = builder.build();
        appiumDriverLocalService.start();
        setServer(appiumDriverLocalService);

    }

    public URL getAppiumUrl() {

        return getServer().getUrl();
    }

    private void destroyAppiumNode() {

        getServer().stop();

        if (getServer().isRunning()) {
            System.out.println("AppiumDriverManager didn't shut... Trying to quit again....");
            getServer().stop();
        }
    }
}
