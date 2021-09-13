package com.smu.urvoice.config.security;

import com.smu.urvoice.filter.JwtAuthenticationFilter;
import com.smu.urvoice.filter.JwtAuthorizationFilter;
import com.smu.urvoice.service.user.CustomUserDetailsService;
import com.smu.urvoice.service.user.UserService;
import com.smu.urvoice.service.user.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http
                .csrf().disable()   // csrf와 session은 JWT 기반 Security에서는 사용하지 않으므로 disable 처리 (remove csrf and state in session because in jwt we do not need them)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager())) // jwt filters 추가
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), this.userService))
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/users").permitAll()
                .antMatchers(HttpMethod.POST, "/login", "/signUp").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}



