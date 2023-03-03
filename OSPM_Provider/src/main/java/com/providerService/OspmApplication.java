package com.providerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.providerService.repository.providerRepository;

@SpringBootApplication
@EnableEurekaClient
public class OspmApplication {
	
	@Autowired
	private providerRepository providerRepository;
	
	/*
	 * @Autowired private BCryptPasswordEncoder bcryptEncoder;
	 */
	 
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
		
	}
	
	
	// @PostConstruct
	// private void postConstruct() {
		
	// 	String password = "Provide1";
		
	// 	Provider provider = new Provider(1001l, "Serum Services", "Provide1", password, "provider1001@gmail.com");
		
	// 	providerRepository.save(provider);
	// }

	public static void main(String[] args) {
		SpringApplication.run(OspmApplication.class, args);
	}

	@Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

}
