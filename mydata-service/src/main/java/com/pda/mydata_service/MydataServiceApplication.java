package com.pda.mydata_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.pda")
public class MydataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MydataServiceApplication.class, args);
	}

}
