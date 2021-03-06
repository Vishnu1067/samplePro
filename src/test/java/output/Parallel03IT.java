package output;
import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(strict = true,
    features = {"classpath:features/Test11.feature"},
    format = {"json:target/3.json", "pretty"},
    monochrome = false,
    tags = {"@smoke"},
    plugin = {"com.cucumber.listener.ExtentCucumberFormatter:"},
    glue = { "com.implementation" })
public class Parallel03IT extends AbstractTestNGCucumberTests {
}