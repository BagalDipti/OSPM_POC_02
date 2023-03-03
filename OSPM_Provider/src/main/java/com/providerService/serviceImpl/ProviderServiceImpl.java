package com.providerService.serviceImpl;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.providerService.model.Provider;
import com.providerService.model.Role;
import com.providerService.repository.providerRepository;
import com.providerService.service.ProviderService;
import com.providerService.service.RoleService;

@Service(value="userService")
public class ProviderServiceImpl implements ProviderService,UserDetailsService{

	@Autowired
	private providerRepository providerRepository;

	@Autowired
    private RoleService roleService;

	@Autowired
    private BCryptPasswordEncoder bcryptEncoder;
	
	
	public Set<Provider> getAllProviders() {
		return new LinkedHashSet<>(this.providerRepository.findAll());
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Provider provider = providerRepository.findByUserName(username);
        if(provider == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(provider.getUserName(), provider.getPassword(), getAuthority(provider));

    }

	private Set<SimpleGrantedAuthority> getAuthority(Provider provider) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        provider.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }


	@Override
    public Object add(Provider provider) {
        // Provider nprovider = Provider.getProviderFromDto();
        provider.setPassword(bcryptEncoder.encode(provider.getPassword()));
        Set<Role> roleSet = new HashSet<>();
 
         if(provider.getRole().matches("PROVIDER")){
			Role role2 = roleService.findByName("PROVIDER");
            roleSet.add(role2);
         }

         provider.setRoles(roleSet);

        return this.providerRepository.save(provider);
    }
}
