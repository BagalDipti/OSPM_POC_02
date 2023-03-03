package com.medical.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource(name = "userService")
    private UserDetailsService userDetailsService;

    @Autowired
    private UnauthorizedEntryPoint unauthorizedEntryPoint;

    @Autowired 
    private BCryptPasswordEncoder bCryptPasswordEncoder;
  
   

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    private PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/signup","/user/login").permitAll()
                .antMatchers("/user/getAllUser").hasRole("PROVIDER")
                .antMatchers(HttpMethod.GET, "/user/findUserById/{userId}").access("@userSecurity.hasUserId(authentication,#userId)")
                .antMatchers(HttpMethod.PUT, "/user/updateUserById/{userId}").access("@userSecurity.hasUserId(authentication,#userId)")
                .antMatchers(HttpMethod.POST, "/user/addToCartByUserId/{userId}").access("@userSecurity.hasUserId(authentication,#userId)")
                .antMatchers(HttpMethod.GET, "/user/viewCartByUser/{userId}").access("@userSecurity.hasUserId(authentication,#userId)")
                .antMatchers(HttpMethod.DELETE, "/user/removeProduct/{userId}/{cartId}").access("@userSecurity.hasUserId(authentication,#userId)")
                .antMatchers(HttpMethod.POST, "/user/placeOrder/{userId}").access("@userSecurity.hasUserId(authentication,#userId)")
                .antMatchers(HttpMethod.GET, "/user/viewOrderByUser/{userId}").access("@userSecurity.hasUserId(authentication,#userId)")

                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    // @Bean
    // public BCryptPasswordEncoder encoder(){
    //     return new BCryptPasswordEncoder();
    // }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationFilter();
    }

}

