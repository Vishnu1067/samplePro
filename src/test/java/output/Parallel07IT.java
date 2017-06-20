package output;
import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(strict = true,
    features = {"classpath:features/WebPGAgentNetLogin.feature"},
    format = {"json:target/7.json", "pretty"},
    monochrome = false,
    tags = {"@smoke"},
    plugin = {"com.cucumber.listener.ExtentCucumberFormatter:"},
    glue = { "com.implementation" })
public class Parallel07IT extends AbstractTestNGCucumberTests {
}