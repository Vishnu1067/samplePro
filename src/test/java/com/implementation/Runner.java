package com.implementation;

//@CucumberOptions(strict = true,
//        features = {"classpath:features/Test.feature"},
//        format = {"json:target/1.json", "pretty"},
//        monochrome = false,
//        tags = {"@smoke"},
//        plugin = {"com.cucumber.listener.ExtentCucumberFormatter"},
//        glue = {"com.implementation"})
//public class Runner extends AbstractTestNGCucumberTests {
//
//}


import com.mobile.utils.ParallelThread;
import org.junit.Test;
import org.testng.Assert;

public class Runner {

    @Test
    public void testCukesRunner() throws Exception {
        ParallelThread cucumberParallelThread = new ParallelThread();
        boolean hasFailures = cucumberParallelThread.runner("output");
        Assert.assertFalse(hasFailures, "Testcases have failed in parallel execution");
    }
}