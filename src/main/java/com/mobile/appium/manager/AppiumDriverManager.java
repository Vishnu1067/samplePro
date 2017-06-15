package com.mobile.appium.manager;

import com.mobile.configuration.IOSDeviceConfiguration;
import com.mobile.utils.ConfigurationManager;
import com.mobile.utils.DesiredCapabilityBuilder;
import com.mobile.utils.MobilePlatform;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.Optional;

public class AppiumDriverManager {


    private static ThreadLocal<AppiumDriver> appiumDriver = new ThreadLocal<>();
    private IOSDeviceConfiguration iosDeviceConfiguration;
    private AppiumServerManager appiumServerManager;
    private DesiredCapabilityManager desiredCapabilityManager;
    private DesiredCapabilityBuilder desiredCapabilityBuilder;
    private ConfigurationManager prop;

    public AppiumDriverManager() {

        iosDeviceConfiguration = new IOSDeviceConfiguration();
        desiredCapabilityManager = new DesiredCapabilityManager();
        appiumServerManager = new AppiumServerManager();
        desiredCapabilityBuilder = new DesiredCapabilityBuilder();
        prop = ConfigurationManager.getInstance();

    }

    public static AppiumDriver getDriver() {

        return appiumDriver.get();
    }

    protected static void setDriver(AppiumDriver driver) {

        appiumDriver.set(driver);
    }

    public void startAppiumDriver(Optional<DesiredCapabilities> iosCaps,
                                  Optional<DesiredCapabilities> androidCaps) {

        AppiumDriver<MobileElement> currentDriverSession;

        if (prop.getProperty("APP_TYPE").equalsIgnoreCase("web")) {

            currentDriverSession = new AndroidDriver<>(appiumServerManager.getAppiumUrl(),
                    desiredCapabilityManager.androidWeb());
            AppiumDriverManager.setDriver(currentDriverSession);

        } else {

            if (System.getProperty("os.name").toLowerCase().contains("mac")) {

                if (iosDeviceConfiguration.deviceUDIDiOS.contains(DeviceManager.getDeviceUDID())) {

                    currentDriverSession = new IOSDriver<>(appiumServerManager.getAppiumUrl(),
                            iosCaps.orElse(desiredCapabilityManager.iosNative()));
                    AppiumDriverManager.setDriver(currentDriverSession);

                } else if (!iosDeviceConfiguration.deviceUDIDiOS.contains(DeviceManager.getDeviceUDID())) {

                    currentDriverSession = new AndroidDriver<>(appiumServerManager.getAppiumUrl(),
                            androidCaps.orElse(desiredCapabilityManager.androidNative()));
                    AppiumDriverManager.setDriver(currentDriverSession);
                }

            } else {
                currentDriverSession = new AndroidDriver<>(appiumServerManager.getAppiumUrl(),
                        androidCaps
                                .orElse(desiredCapabilityManager.androidNative()));
                AppiumDriverManager.setDriver(currentDriverSession);
            }
        }
    }

    // Should be used by Cucumber as well
    public void startAppiumDriver() {

        DesiredCapabilities iOS = null;
        DesiredCapabilities android = null;

        String userSpecifiedAndroidCaps = System.getProperty("user.dir")
                + "/caps/android.json";
        String userSpecifiediOSCaps = System.getProperty("user.dir")
                + "/caps/iOS.json";

        if (DeviceManager.getMobilePlatform().equals(MobilePlatform.ANDROID)) {
            android = getDesiredAndroidCapabilities(android, userSpecifiedAndroidCaps);

        } else {
            iOS = getDesiredIOSCapabilities(iOS, userSpecifiediOSCaps);
        }

        startAppiumDriver(Optional.ofNullable(iOS), Optional.ofNullable(android));
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

    }

    public DesiredCapabilities getDesiredIOSCapabilities(DesiredCapabilities iOS,
                                                         String userSpecifiediOSCaps) {
        String iOSJsonFilePath;
        if (DeviceManager.getMobilePlatform().equals(MobilePlatform.IOS)) {

            if (prop.getProperty("IOS_CAPS") != null) {
                iOSJsonFilePath = prop.getProperty("IOS_CAPS");
                desiredCapabilityBuilder
                        .buildDesiredCapability(iOSJsonFilePath);
                iOS = DesiredCapabilityBuilder.getDesiredCapability();

            } else if (new File(userSpecifiediOSCaps).exists()) {
                iOSJsonFilePath = userSpecifiediOSCaps;
                desiredCapabilityBuilder
                        .buildDesiredCapability(iOSJsonFilePath);
                iOS = DesiredCapabilityBuilder.getDesiredCapability();
            }
        }
        return iOS;
    }

    public DesiredCapabilities getDesiredAndroidCapabilities(DesiredCapabilities android,
                                                             String userSpecifiedAndroidCaps) {
        String androidJsonFilePath;

        if (DeviceManager.getMobilePlatform().equals(MobilePlatform.ANDROID)) {

            if (prop.getProperty("ANDROID_CAPS") != null) {

                androidJsonFilePath = prop.getProperty("ANDROID_CAPS");
                desiredCapabilityBuilder
                        .buildDesiredCapability(androidJsonFilePath);
                android = DesiredCapabilityBuilder.getDesiredCapability();

            } else if (new File(userSpecifiedAndroidCaps).exists()) {

                androidJsonFilePath = userSpecifiedAndroidCaps;
                desiredCapabilityBuilder
                        .buildDesiredCapability(androidJsonFilePath);
                android = DesiredCapabilityBuilder.getDesiredCapability();
            }
        }
        return android;
    }

}
