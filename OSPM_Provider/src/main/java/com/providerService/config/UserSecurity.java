package com.providerService.config;


import com.providerService.repository.providerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {
	
	@Autowired
	providerRepository providerRepository;
	
	public boolean hasUserId(Authentication authentication, Long id) {
		
		Long providerID=providerRepository.findByUserName(authentication.getName()).getId();
//		System.out.println(userId+"  "+userID);
            if(providerID==id)
            	return true;
            
            return false;
       
    }
}