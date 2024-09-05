package com.iprody.e2e.config;

import org.openapitools.client.api.UserControllerApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class E2eTestConfig {
    @Bean
    public UserControllerApi userControllerApi() {
        return new UserControllerApi();
    }
}
