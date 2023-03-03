package com.Apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OspmApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(OspmApiGatewayApplication.class, args);
	}

}
