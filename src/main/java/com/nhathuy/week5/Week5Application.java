package com.nhathuy.week5;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import com.nhathuy.week5.config.StorageProperties;
import com.nhathuy.week5.service.IStorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Week5Application {

    public static void main(String[] args) {
        SpringApplication.run(Week5Application.class, args);
    }

    @Bean
    CommandLineRunner init(IStorageService storageService) {
        return (args) -> {
            storageService.init();
        };
    }
}
