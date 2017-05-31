package com.implementation;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(strict = true,
        features = {"classpath:features/Test.feature"},
        format = {"json:target/1.json", "pretty"},
        monochrome = false,
        tags = {"@smoke"},
        plugin = {"com.mobile.core.ExtentCucumberFormatter:"},
        glue = {"com.implementation"})
public class Runner extends AbstractTestNGCucumberTests {

}
