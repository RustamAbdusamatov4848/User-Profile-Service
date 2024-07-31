package com.iprody.userprofileservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Configuration
public class DataSourceConfig {
    Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${spring.datasource.url}")
    private String dbURL;

    @Value("${spring.datasource.username}")
    private String usernameSecret;

    @Value("${spring.datasource.password}")
    private String passwordSecret;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(dbURL);
        String username = getSecretFromFile(usernameSecret).orElse(usernameSecret);
        String password = getSecretFromFile(passwordSecret).orElse(passwordSecret);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    private Optional<String> getSecretFromFile(String secretFile) {
        String secret = null;

        try {
            secret = Files.readString(Path.of(secretFile));
        } catch (IOException e) {
            logger.warn("Can't get secret from file: [{}]", e.getMessage());
        }
        return Optional.ofNullable(secret);
    }
}
