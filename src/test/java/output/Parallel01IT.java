package output;
import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(strict = true,
    features = {"classpath:features/PGDashboard.feature"},
    format = {"json:target/1.json", "pretty"},
    monochrome = false,
    tags = {"@smoke"},
    plugin = {"com.cucumber.listener.ExtentCucumberFormatter:"},
    glue = { "com.implementation" })
public class Parallel01IT extends AbstractTestNGCucumberTests {
}