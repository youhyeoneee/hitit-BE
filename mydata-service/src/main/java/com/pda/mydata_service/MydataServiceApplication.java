package com.pda.mydata_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.pda")
@EnableJpaRepositories(basePackages = {"com.pda.mydata_service"})
public class MydataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MydataServiceApplication.class, args);
	}

}
