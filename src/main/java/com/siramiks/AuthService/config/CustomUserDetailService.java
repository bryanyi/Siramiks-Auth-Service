package com.siramiks.AuthService.config;

import com.siramiks.AuthService.entity.User;
import com.siramiks.AuthService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class CustomUserDetailService implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> existingUser = userRepository.findByEmail(username);
    return existingUser.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found by the name of " + username));
  }
}
