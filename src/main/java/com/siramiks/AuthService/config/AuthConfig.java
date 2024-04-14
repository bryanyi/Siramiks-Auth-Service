package com.siramiks.AuthService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AuthConfig {

  @Bean
  public UserDetailsService userDetailsService() {
    return new CustomUserDetailService();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // .requestMatchers("/static/**", "/auth/test/", "/auth/register", "/auth/getToken", "/auth/validateToken").permitAll()
    return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((authorize) ->
                    authorize
                            .requestMatchers("/static/**", "/api/v1/auth/test/", "/api/v1/auth/register", "/api/v1/auth/getToken", "/api/v1/auth/validateToken").permitAll()
                            .anyRequest().permitAll()
            )
            .build();
  }

  //  @Bean
  //  public WebSecurityCustomizer webSecurityCustomizer() {
  //    return webSecurity -> webSecurity.ignoring().requestMatchers("/auth/test");
  //  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }


}
