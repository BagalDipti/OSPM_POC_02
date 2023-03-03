package com.ospm.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OspmProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(OspmProductApplication.class, args);
	}

}
