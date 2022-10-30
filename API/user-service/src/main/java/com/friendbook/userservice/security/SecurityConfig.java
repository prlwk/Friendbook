package com.friendbook.userservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.friendbook.userservice.security.jwt.JwtAuthFilter;
import com.friendbook.userservice.security.jwt.JwtAuthProvider;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private JwtAuthProvider jwtAuthenticationProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .authorizeRequests()
                .antMatchers("/user/registration").permitAll()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/refresh-tokens*").permitAll()
                .antMatchers("/user/id/**").permitAll()
                .antMatchers("/user/password-recovery*").permitAll()
                .antMatchers("/user/confirm-account*").permitAll()
                .antMatchers("/send-code-for-recovery-password*").permitAll()
                .antMatchers("/send-code-for-confirmation-account*").permitAll()
                .antMatchers("/user/check-email-exists*").permitAll()
                .antMatchers("/user/image*").permitAll()
                .anyRequest().authenticated().and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).exceptionHandling();
    }
}
