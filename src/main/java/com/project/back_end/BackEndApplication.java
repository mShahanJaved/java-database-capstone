package com.project.back_end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(
    scanBasePackages = {"com.project.back_end"}
)
@EntityScan({"com.project.back_end.models"})
@EnableJpaRepositories(basePackages = {"com.project.back_end.repository"})
@EnableMongoRepositories(basePackages = {"com.project.back_end.repository"})
public class BackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }
}
