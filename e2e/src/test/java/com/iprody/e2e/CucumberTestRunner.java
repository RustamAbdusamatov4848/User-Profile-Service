package com.iprody.e2e;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"},
        glue = {"com.iprody.e2e.stepdefinitions", "com.iprody.e2e.config"},
        plugin = {"json:target/cucumber-json-report.json",
                "html:target/cucumber-html-report.html"})
public class CucumberTestRunner {

}
