package com.iprody.e2e;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"e2e/src/test/resources/features"},
                 plugin = {"json:e2e/target/cucumber-json-report.json",
                           "html:e2e/target/cucumber-html-report.html"})
public class CucumberTestRunner {

}
