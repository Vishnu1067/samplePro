package com.mobile.utils;

import org.testng.TestNG;
import org.testng.collections.Lists;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.ParallelMode;
import org.testng.xml.XmlTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MyTestExecutor {

    public MyTestExecutor() {

    }

    public boolean runCucumberTest() {

        TestNG testNG = new TestNG();
        List<String> suites = Lists.newArrayList();
        suites.add(System.getProperty("user.dir") + "/target/parallel.xml");
        testNG.setTestSuites(suites);
        testNG.run();
        return testNG.hasFailure();
    }

    public XmlSuite constructXmlSuiteForParallelCucumber(int deviceCount, ArrayList<String> deviceSerial) {

        XmlSuite suite = new XmlSuite();
        suite.setName("TestNG Forum");
        suite.setThreadCount(deviceCount);
        suite.setParallel(ParallelMode.TESTS);
        suite.setVerbose(2);

        for (int i = 0; i < deviceCount; i++) {
            XmlTest test = new XmlTest(suite);
            test.setName("TestNG Test" + i);
            test.setPreserveOrder("false");
            test.addParameter("device", deviceSerial.get(i));
            test.setPackages(getPackages());
        }

        try {

            File file = new File(System.getProperty("user.dir") + "/target/parallel.xml");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(suite.toXml());
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return suite;
    }

    public XmlSuite constructXmlSuiteDistributeCucumber(int deviceCount) {

        XmlSuite suite = new XmlSuite();
        suite.setName("TestNG Forum");
        suite.setThreadCount(deviceCount);
        suite.setParallel(ParallelMode.CLASSES);
        suite.setVerbose(2);
        XmlTest test = new XmlTest(suite);
        test.setName("TestNG Test");
        test.addParameter("device", "");
        test.setPackages(getPackages());

        try {

            File file = new File(System.getProperty("user.dir") + "/target/parallel.xml");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(suite.toXml());
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return suite;
    }

    private static List<XmlPackage> getPackages() {

        List<XmlPackage> allPackages = new ArrayList<>();
        XmlPackage eachPackage = new XmlPackage();
        eachPackage.setName("output");
        allPackages.add(eachPackage);
        return allPackages;
    }

}
