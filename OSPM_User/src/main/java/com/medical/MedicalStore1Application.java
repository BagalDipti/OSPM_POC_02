package com.medical;


import java.util.List;

import javax.annotation.PostConstruct;

import com.medical.model.Role;
import com.medical.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
@EnableEurekaClient
public class MedicalStore1Application {

	@Autowired
    private RoleRepository roleRepository;

	
	@PostConstruct
	private void postConstruct() {
		Role role1 = new Role(1,  "PROVIDER");
		Role role2 = new Role(2, "USER");
	
		 List<Role> role = List.of(role1,role2);
		 roleRepository.saveAll(role);

	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(MedicalStore1Application.class, args);
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
		
	}

	@Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}
