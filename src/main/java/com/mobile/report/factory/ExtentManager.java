package com.mobile.report.factory;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentXReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.mobile.utils.CommandPrompt;
import com.mobile.utils.ConfigurationManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ExtentManager {

    private static ConfigurationManager configFileManager;
    private static ExtentReports extent;
    private static String filePath = System.getProperty("user.dir") + "/target/ExtentReports.html";
    private static CommandPrompt commandPrompt = new CommandPrompt();

    public synchronized static ExtentReports getExtent() {

        if (extent == null) {

            configFileManager = ConfigurationManager.getInstance();
            extent = new ExtentReports();
            extent.attachReporter(getHtmlReporter());

            if (System.getenv("ExtentX") != null && System.getenv("ExtentX")
                    .equalsIgnoreCase("true")) {
                extent.attachReporter(getExtentXReporter());
            }

            extent.setSystemInfo("Selenium Java Version", "3.3.1");
            extent.setSystemInfo("Environment", "Prod");

            String appiumVersion;

            String command = "node " + configFileManager.getProperty("APPIUM_JS_PATH") + " -v";
            appiumVersion = commandPrompt.runCommand(command);

            extent.setSystemInfo("AppiumClient", "5.0.0-BETA6");
            extent.setSystemInfo("AppiumServer", appiumVersion.replace("\n", ""));
            extent.setSystemInfo("Runner", configFileManager.getProperty("RUNNER"));
            List statusHierarchy = Arrays.asList(
                    Status.FATAL,
                    Status.FAIL,
                    Status.ERROR,
                    Status.WARNING,
                    Status.PASS,
                    Status.SKIP,
                    Status.DEBUG,
                    Status.INFO
            );

            extent.config().statusConfigurator().setStatusHierarchy(statusHierarchy);
        }
        return extent;
    }

    private static ExtentHtmlReporter getHtmlReporter() {

        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(filePath);
        URL inputUrl = null;

        try {
            inputUrl = Thread.currentThread().getContextClassLoader().getResource("extent.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }

        File dest = new File(System.getProperty("user.dir") + "/target/extent.xml");

        try {
            FileUtils.copyURLToFile(inputUrl, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        htmlReporter.loadXMLConfig(System.getProperty("user.dir") + "/target/extent.xml");
        // make the charts visible on report open
        htmlReporter.config().setChartVisibilityOnOpen(true);

        // report title
        //String documentTitle = prop.getProperty("documentTitle", "aventstack - Extent");
        htmlReporter.config().setDocumentTitle("AppiumTestDistribution");
        htmlReporter.config().setReportName("AppiumTestDistribution");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.STANDARD);
        return htmlReporter;
    }

    private static ExtentXReporter getExtentXReporter() {

        String host = configFileManager.getProperty("MONGODB_SERVER");
        Integer port = Integer.parseInt(configFileManager.getProperty("MONGODB_PORT"));
        ExtentXReporter extentx = new ExtentXReporter(host, port);

        // project name
        String projectName = configFileManager.getProperty("projectName", "ExtentReports");
        extentx.config().setProjectName(projectName);

        // report or build name
        String reportName = configFileManager.getProperty("reportName", "ExtentReports");
        extentx.config().setReportName(reportName);

        // server URL
        // ! must provide this to be able to upload snapshots
        String url = host + ":" + port;

        if (!url.isEmpty()) {
            extentx.config().setServerUrl(url);
        }
        return extentx;
    }
}
