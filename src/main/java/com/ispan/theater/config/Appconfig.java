package com.ispan.theater.config;


import com.ispan.theater.service.AdminService;
import com.ispan.theater.service.CustomUserDetailsService;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.context.annotation.Lazy;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.ispan.theater.filter.JwtAuthenticationFilter;


@Configuration
@EnableCaching
@EnableScheduling
@EnableWebSecurity
public class Appconfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    private final AdminService adminService;
    private final CustomUserDetailsService customUserDetailsService;
    public Appconfig(@Lazy AdminService adminService,@Lazy CustomUserDetailsService customUserDetailsService,@Lazy PasswordEncoder passwordEncoder) {
        this.adminService = adminService;
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }
    private final PasswordEncoder passwordEncoder;
    @Bean
    JwtAuthenticationFilter JwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authorize) -> {//test /order/movie/getOrderCondition、/order/movie/getOrderDetail、/order/movie/deleteOrder、/order/movie/getOrderBackStage暫時開權限
                    authorize.requestMatchers("/order/movie/test","/order/movie/getOrderCondition","/order/movie/getOrderDetail","/order/movie/deleteOrder","/order/movie/getOrderBackStage","/user/pass/**", "/order-redirect", "/order/movie/linePayConfirm", "/order/movie/findMovie"
                    				, "/order/movie/findAllCinema", "/order/movie/dates", "/order/movie/times", "/order/movie/ecPayConfirm"
                                    , "/order/movie/tickets", "/backstage/movie/**", "/moviePicture/**", "/comment/**"
                                    , "/login", "/api/login","/order/movie/findCinemaData","/backstage/food/**","/backstage/food/photo/**","/backstage/foodpicture/**","/back/custService/**").permitAll()
                            .requestMatchers("/admin/**","/user/backside/**").hasRole("ADMIN")
                            .anyRequest().authenticated();
                });
        return http.build();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration,HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(adminService).passwordEncoder(passwordEncoder);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }
    

    
    
}
