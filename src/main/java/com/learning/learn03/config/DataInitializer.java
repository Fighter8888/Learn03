package com.learning.learn03.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final InitializerService initializerService;

    public DataInitializer(InitializerService initializerService) {
        this.initializerService = initializerService;
    }

    public void run(String... args) {
        initializerService.createRolesIfNotExist();
        initializerService.createPrincipalIfNotExists();
        initializerService.createMajorIfNotExists();
    }
}