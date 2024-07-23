package com.iprody.e2e;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = E2EApp.class)
@CucumberContextConfiguration
public class CucumberTestConfig {

}
