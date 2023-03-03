package com.Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class OspmServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OspmServerApplication.class, args);
	}

}
