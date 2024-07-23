package com.iprody.e2e.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DemoSampleStepDefinitions {

    @Given("the environment is setup correctly")
    public void the_environment_is_setup_correctly() {

    }

    @When("I run the tests")
    public void i_run_the_test() {

    }

    @Then("everything should be fine")
    public void everything_should_be_fine(){
        assertTrue(true);
    }
}
