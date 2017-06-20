package output;
import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(strict = true,
    features = {"classpath:features/PGSearchResidentialBuyType.feature"},
    format = {"json:target/2.json", "pretty"},
    monochrome = false,
    tags = {"@smoke"},
    plugin = {"com.cucumber.listener.ExtentCucumberFormatter:"},
    glue = { "com.implementation" })
public class Parallel02IT extends AbstractTestNGCucumberTests {
}