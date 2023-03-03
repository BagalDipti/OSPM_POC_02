package com.medical.serviceimpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.medical.model.Role;
import com.medical.model.User;
import com.medical.model.UserDto;
import com.medical.repository.UserRepository;
import com.medical.service.RoleService;
import com.medical.service.UserService;



@Service(value = "userService")
public class UserServiceimpl implements UserService,UserDetailsService{

	@Autowired
	private UserRepository userRepository;

	@Autowired
    private RoleService roleService;

	@Autowired
    private BCryptPasswordEncoder bcryptEncoder;
	


	@Override
	public User save(User user) {
		return userRepository.save(user);
	}



	@Override
	public Object userUpdate(User user, Integer userId) {
		try {
			User singleUser = this.userRepository.getOne(userId);
			if(user.getUserName()!=null) {
				singleUser.setUserName(user.getUserName());
			}
			if(user.getUserEmail()!=null) {
				singleUser.setUserEmail(user.getUserEmail());
			}
			if(user.getPassword()!=null) {
				singleUser.setPassword(user.getPassword());
			}
			if(user.getShopName()!=null) {
				singleUser.setShopName(user.getShopName());
			}
			if(user.getShopAddress()!=null) {
				singleUser.setShopAddress(user.getShopAddress());
			}
			if(user.getUserMobileNo()!=0) {
				singleUser.setUserMobileNo(user.getUserMobileNo());
			}
			return ResponseEntity.ok(this.userRepository.save(singleUser)).getBody();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalid JSON").getBody();
		}
	}



	@Override
	public User findById(int id) {
		return this.userRepository.findById(id).get();
		
	}


	@Override
	public Object getAllUsers() {
		return this.userRepository.findAll();
	}



	@Override
	public void deleteById(int userId) {
		this.userRepository.deleteById(userId);
	}



	@Override
	public Object getUserByShopId(long shopId) {
		return this.userRepository.findByShopId(shopId);
	}



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), getAuthority(user));

    }

	private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }



	@Override
	public User save(UserDto user) {

        User nUser = user.getUserFromDto();
        nUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        Set<Role> roleSet = new HashSet<>();


        if(nUser.getRole().matches("USER")){
            Role role1 = roleService.findByName("USER");
            roleSet.add(role1);
         }
 
        //  if(nUser.getRole().matches("PROVIDER")){
        //     Role role2 = roleService.findByName("PROVIDER");
        //     roleSet.add(role2);
        //  }

        nUser.setRoles(roleSet);
        return userRepository.save(nUser);
    }

}






