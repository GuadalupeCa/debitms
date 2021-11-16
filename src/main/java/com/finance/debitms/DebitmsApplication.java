package com.finance.debitms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@EnableEurekaClient
@SpringBootApplication
public class DebitmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebitmsApplication.class, args);

	}

}
