package com.implementation;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(strict = true,
        features = {"classpath:features"},
        format = {"json:target/1.json", "pretty"},
        monochrome = false,
        tags = {"@smoke"},
        plugin = {"com.mobile.cucumber.listener.ExtentCucumberFormatter"},
        glue = {"com.implementation"})
public class TestRunner extends AbstractTestNGCucumberTests {

}