package com.finance.debitms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class DebitmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebitmsApplication.class, args);
	}

}
